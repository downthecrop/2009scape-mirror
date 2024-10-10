package core.game.bots

import core.api.*
import core.game.interaction.NodeUsageEvent
import core.game.interaction.PluginInteractionManager
import core.game.interaction.UseWithHandler
import core.cache.def.impl.ItemDefinition
import core.game.component.Component
import core.game.consumable.Consumable
import content.data.consumables.Consumables
import core.game.consumable.Food
import content.data.consumables.effects.HealingEffect
import core.game.interaction.DestinationFlag
import core.game.interaction.MovementPulse
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.scenery.Scenery
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.path.Pathfinder
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.ChatMessage
import core.game.world.update.flag.context.Graphics
import core.game.world.update.flag.*
import core.tools.RandomFunction
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.rs09.consts.Items
import core.ServerConstants.Companion.SERVER_GE_NAME
import core.game.ge.GrandExchange
import core.game.ge.GrandExchangeOffer
import core.game.interaction.IntType
import core.game.interaction.InteractionListeners
import core.tools.SystemLogger
import core.game.system.config.ItemConfigParser
import core.game.world.GameWorld
import core.game.world.repository.Repository
import core.tools.Log
import core.tools.colorize
import java.util.*
import java.util.concurrent.CountDownLatch
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt
import core.ServerConstants
import core.api.utils.Vector
import kotlin.random.Random

class ScriptAPI(private val bot: Player) {
    val GRAPHICSUP = Graphics(1576)
    val ANIMATIONUP = Animation(8939)
    val GRAPHICSDOWN = Graphics(1577)
    val ANIMATIONDOWN = Animation(8941)

    /**
     * Gets the distance between two nodes
     * @param n1 the first node
     * @param n2 the second node
     * @author Ceikry
     */
    fun distance(n1: Node, n2: Node): Double {
        return sqrt((n1.location.x - n2.location.x.toDouble()).pow(2.0) + (n2.location.y - n1.location.y.toDouble()).pow(2.0))
    }

    fun interact(bot: Player, node: Node?, option: String){

        if(node == null) return

        val type = when(node){
            is Scenery -> IntType.SCENERY
            is NPC -> IntType.NPC
            is Item -> IntType.ITEM
            else -> null
        } ?: return
        val opt: Option? = node.interaction.options.filter {it != null && it.name.equals(option, true) }.firstOrNull()

        if(opt == null){
            log(this::class.java, Log.WARN,  "Invalid option name provided: $option")
            return
        }

        if(!InteractionListeners.run(node.id, type, option, bot, node)) node.interaction.handle(bot, opt)
    }

    fun useWith(bot: Player, itemId: Int, node: Node?) {
        if(node == null) return

		val type = when(node){
			is Scenery -> IntType.SCENERY
			is NPC -> IntType.NPC
			is Item -> IntType.ITEM
			else -> null
		} ?: return

		val item = bot.inventory.getItem(Item(itemId))

		val childNode = node.asScenery()?.getChild(bot)

        if (InteractionListeners.run(item, node, type, bot))
            return
        if (childNode != null && childNode.id != node.id) {
            if (InteractionListeners.run(item, childNode, type, bot))
                return
        }
        val flipped = type == IntType.ITEM && item.id < node.id
        val event = if (flipped)
            NodeUsageEvent(bot, 0, node, item)
        else
            NodeUsageEvent(bot, 0, item, childNode ?: node)
        if (PluginInteractionManager.handle(bot, event))
            return
        UseWithHandler.run(event)
    }

    fun sendChat(message: String) {
        bot.sendChat(message)
        bot.updateMasks.register(EntityFlag.Chat, ChatMessage(bot, message, 0, 0))
    }

    /**
     * Gets the nearest node with a name contained in the list of acceptable names
     * @param acceptedNames the list of accepted npc/object names
     * @return the nearest node with a matching name or null
     * @author Ceikry
     */
    fun getNearestNodeFromList(acceptedNames: List<String>, isObject: Boolean): Node? {
        if (isObject)
            return processEvaluationList(RegionManager.forId(bot.location.regionId).planes[bot.location.z].objectList, acceptedName = acceptedNames)
        else
            return processEvaluationList(RegionManager.forId(bot.location.regionId).planes[bot.location.z].entities, acceptedName = acceptedNames)
    }

    /**
     * Gets the nearest node with matching id.
     * @param id the id to look for
     * @param object whether or not the node we are looking for is an object.
     * @return the closest node with matching id or null.
     * @author Ceikry
     */
    fun getNearestNode(id: Int, isObject: Boolean): Node? {
        if (isObject)
            return processEvaluationList(RegionManager.forId(bot.location.regionId).planes[bot.location.z].objectList, acceptedId = id)
        else
            return processEvaluationList(RegionManager.forId(bot.location.regionId).planes[bot.location.z].entities, acceptedId = id)
    }

    /**
     * Gets the nearest node with name entityName
     * @param entityName the name of the node to look for
     * @return the nearest node with a matching name or null
     * @author Ceikry
     */
    fun getNearestNode(entityName: String): Node? {
        return processEvaluationList(RegionManager.forId(bot.location.regionId).planes[bot.location.z].entities, acceptedName = listOf(entityName))
    }

    /**
     * Gets the nearest node with a matching name.
     * @param name the name to look for.
     * @param object whether or not the node we are looking for is an object.
     * @return the nearest matching node or null.
     * @author Ceikry
     */
    fun getNearestNode(name: String, isObject: Boolean): Node? {
        if (isObject)
            return processEvaluationList(RegionManager.forId(bot.location.regionId).planes[bot.location.z].objectList, acceptedName = listOf(name))
        else
            return processEvaluationList(RegionManager.forId(bot.location.regionId).planes[bot.location.z].entities, acceptedName = listOf(name))
    }

    fun getNearestObjectByPredicate(predicate: (Node?) -> Boolean): Node? {
        return processEvaluationList(RegionManager.forId(bot.location.regionId).planes[bot.location.z].objectList, acceptedPredicate = predicate)
    }

    fun evaluateViability (e: Node?, minDistance: Double, maxDistance: Double, acceptedNames: List<String>? = null, acceptedId: Int = -1, acceptedPredicate: ((Node?) -> Boolean)? = null): Boolean {
        if (e == null || !e.isActive) 
            return false
        if (acceptedId != -1 && e.id != acceptedId)
            return false

        val dist = distance(bot, e)
        if (dist > maxDistance || dist > minDistance)
            return false

        if (acceptedPredicate != null) {
            return acceptedPredicate(e) && !Pathfinder.find(bot, e).isMoveNear
        } else {
            val name = e?.name
            return (acceptedNames?.stream()?.anyMatch({ s -> s.equals(name, true) }) ?: true && !Pathfinder.find(bot, e).isMoveNear)
        }
    }

    fun processEvaluationList (list: List<Node>, acceptedName: List<String>? = null, acceptedId: Int = -1, acceptedPredicate: ((Node?) -> Boolean)? = null): Node? {
        var entity: Node? = null
        var minDistance = Double.MAX_VALUE
        val maxDistance = ServerConstants.MAX_PATHFIND_DISTANCE.toDouble()
        for (e in list) {
            if (evaluateViability(e, minDistance, maxDistance, acceptedName, acceptedId, acceptedPredicate)) {
                entity = e
                minDistance = distance(bot, e)
            }
        }
        return entity
    }

    /**
     * Gets the nearest ground item with matching ID from the list in AIRepository.
     * @param id the id of the ground item we are looking for.
     * @return the nearest GroundItem with a matching id or null
     * @author Ceikry
     */
    private fun getNearestGroundItem(id: Int): GroundItem? {
        var distance = 11.0
        var closest: GroundItem? = null
        if(AIRepository.getItems(bot) != null) {
            for (item in AIRepository.getItems(bot)!!.filter { it: GroundItem -> it.distance(bot.location) < 10 }) {
                if (item.id == id) {
                    //distance = item.distance(bot.location)
                    closest = item
                }
            }
            if (!GroundItemManager.getItems().contains(closest)){
                AIRepository.getItems(bot)?.remove(closest)
                return null
            }
        } else {
            val items: ArrayList<GroundItem>? = bot.getAttribute("botting:drops",null)
            if(items != null){
                for(item in items.filter { it.distance(bot.location) < 10 }){
                    if(item.id == id) return item.also { items.remove(item); bot.setAttribute("botting:drops",items) }
                }
            }
        }
        return closest
    }

    /**
     * Takes the nearest ground item with a matching id if it exists.
     * @param id the id to look for
     * @author Ceikry
     */
    fun takeNearestGroundItem(id: Int) : Boolean{
        val item = getNearestGroundItem(id)
        if(item != null){
            item.interaction?.handle(bot, item.interaction[2])
            return true
        }
        else return false
    }

    /**
     * Gets the nearest GameObject to loc with matching objectId
     * @param loc the location we are checking around
     * @param objectId the id of the object we are looking for
     * @return the nearest matching object or null.
     * @author Ceikry
     */
    fun getNearestGameObject(loc: Location, objectId: Int): Scenery? {
        var nearestObject: Scenery? = null
        val minDistance = Double.MAX_VALUE
        for (o in RegionManager.forId(loc.regionId).planes[0].objects) {
            for (obj in o) {
                if (obj != null) {
                    if (distance(loc, obj) < minDistance && obj.id == objectId) {
                        nearestObject = obj
                    }
                }
            }
        }
        return nearestObject
    }

    /**
     * Gets a list of NPCs that are attackable around the entity.
     * @param entity the entity to search around.
     * @param radius the radius around entity to search in.
     * @param name the name of the NPC to look for.
     * @return an Array of the nearest NPCs with matching name, or null.
     * @author Ceikry
     */
    private fun findTargets(entity: Entity, radius: Int, name: String? = null): List<Entity>? {
        val targets: MutableList<Entity> = ArrayList()
        val localNPCs: Array<Any> = RegionManager.getLocalNpcs(entity, radius).toTypedArray()
        var length = localNPCs.size
        if (length > 5) {
            length = 5
        }
        for (i in 0 until length) {
            val npc = localNPCs[i] as NPC
            run { if (checkValidTargets(npc, name)) targets.add(npc) }
        }
        return if (targets.size == 0) null else targets
    }

    /**
     * Checks if the given target is a valid target for a combat bot.
     * @param target the NPC that we are checking
     * @param name the expected name of the NPC.
     * @return true if the target is valid, false if not.
     * @author Ceikry
     */
    private fun checkValidTargets(target: NPC, name: String?): Boolean {
        if (!target.isActive) {
            return false
        }
        if (!target.properties.isMultiZone && target.inCombat()) {
            return false
        }
        if (name != null){
            if(target.name != name)
                return false
        }
        return target.definition.hasAction("attack")
    }

    /**
     * Attacks npcs in the given radius of the bot.
     * @param bot the bot that is attacking
     * @param radius the radius to attack in
     * @return true if successfully attacking an NPC within that radius, false if not.
     * @author Ceikry
     */
    fun attackNpcsInRadius(bot: Player, radius: Int): Boolean {
        if (bot.inCombat()) return true
        var creatures: List<Entity>? = findTargets(bot, radius) ?: return false
        bot.attack(creatures!![RandomFunction.getRandom(creatures.size - 1)])
        return if (creatures.isNotEmpty()) {
            true
        } else {
            creatures = findTargets(bot, radius)
            if (!creatures!!.isEmpty()) {
                bot.attack(creatures[RandomFunction.getRandom(creatures.size - 1)])
                return true
            }
            false
        }
    }

    /**
     * Function to iteratively walk a bot to a faraway location. Limited by doors and large obstacles like mountains.
     * @param loc the location to walk to.
     * @author Ceikry
     */
    fun walkTo(loc: Location){
        if(!bot.walkingQueue.isMoving) {
            walkToIterator(loc)
        }
    }

    /**
     * Function to iteratively walk an array of Locations to get over complex obstacles.
     * @param steps the array of locations to walk to.
     * @author dginovker
     */
    fun walkArray(steps: Array<Location>){
        bot.pulseManager.run(object : Pulse(){
            var stepIndex = 0
            override fun pulse(): Boolean {
                // If the stepIndex is out of bounds, we're done
                if(stepIndex >= steps.size) return true
                // If we're near the last step, we're done
                if (bot.location.withinDistance(steps[steps.size - 1], 2)) {
                    return true
                }
                // If we're not near the next step, walk to it
                if (!bot.location.withinDistance(steps[stepIndex], 5)) {
                    walkTo(steps[stepIndex])
                    return false
                }
                // If we're near the next step, increment the step index
                stepIndex++

                return false
            }
        })
    }

    /**
     * @param loc the location to walk to.
     * @param radius tiles around the location the bot could walk to.
     * @author Kermit
     */
    fun randomWalkTo(loc: Location, radius: Int) {
        if(!bot.walkingQueue.isMoving) {
            var newloc = loc.transform(RandomFunction.random(radius,-radius),RandomFunction.random(radius,-radius), 0)
            walkToIterator(newloc)
        }
    }

    /**
     * @param location the location you want the coordinates randomized for.
     * @param xMin the minimum range value X coordinates should be randomized by, must be xMin <= xMax ex: -1 min 1 max.
     * @param xMax the maximum range value X coordinates should be randomized by, must be xMin <= xMax ex: -1 min 1 max.
     * @param yMin the minimum range value Y coordinates should be randomized by, must be yMin <= yMax ex: -1 min 1 max.
     * @param yMax the maximum range value Y coordinates should be randomized by, must be yMin <= yMax ex: -1 min 1 max.
     * @param staticZ this value is static and does not change from what is given, must be actual Z value of location.
     * @author Kermit
     */
    fun randomizeLocationInRanges(location: Location, xMin: Int, xMax: Int, yMin: Int, yMax: Int, staticZ: Int): Location {
        val newX = location.x + Random.nextInt(xMin, xMax)
        val newY = location.y + Random.nextInt(yMin, yMax)
        return Location(newX, newY, staticZ)
    }

    /**
     * The iterator for long-distance walking. Limited by doors and large obstacles like mountains.
     * @param loc the location to find a path to.
     * @author Ceikry
     */
    private fun walkToIterator(loc: Location){
        var diffX = loc.x - bot.location.x
        var diffY = loc.y - bot.location.y
        
        val vec = Vector.betweenLocs(bot.location, loc)
        val norm = vec.normalized()
        val tiles = kotlin.math.min(kotlin.math.floor(vec.magnitude()).toInt(), ServerConstants.MAX_PATHFIND_DISTANCE - 1)
        val loc = bot.location.transform(norm * tiles)
        bot.pulseManager.run(object : MovementPulse(bot, loc) { override fun pulse() : Boolean { return true } })
    }

    /**
     * Attacks npcs in the given radius of the bot.
     * @param bot the bot that is attacking
     * @param radius the radius to attack in
     * @param name the name of the NPC to target.
     * @return true if successfully attacking an NPC within that radius, false if not.
     * @author Ceikry
     */
    fun attackNpcInRadius(bot: Player, name: String, radius: Int): Boolean {
        if (bot.inCombat()) return true
        var creatures: List<Entity>? = findTargets(bot, radius, name) ?: return false
        bot.attack(creatures!![RandomFunction.getRandom(creatures.size - 1)])
        return if (creatures.isNotEmpty()) {
            true
        } else {
            creatures = findTargets(bot, radius, name)
            if (!creatures!!.isEmpty()) {
                bot.attack(creatures.random())
                return true
            }
            false
        }
    }

    /**
     * Extension function to find the distance between a location and a ground item.
     * @param loc the location to check the distance from.
     * @return a Double representing the distance.
     * @author Ceikry
     */
    fun GroundItem.distance(loc: Location): Double{
        return location.getDistance(loc)
    }

    /**
     * A function for teleporting the bot to the GE
     * @author Ceikry
     */
    fun teleportToGE() : Boolean{
        if (bot.isTeleBlocked) {
            return false
        }
        bot.lock()
        bot.visualize(ANIMATIONUP, GRAPHICSUP)
        bot.impactHandler.disabledTicks = 4
        val location = Location.create(3165, 3482, 0)
        bot.pulseManager.run(object : Pulse(4, bot) {
            override fun pulse(): Boolean {
                bot.unlock()
                bot.properties.teleportLocation = location
                bot.pulseManager.clear()
                bot.animator.reset()
                return true
            }
        })
        return true
    }

    /**
     * A function for selling a given item on the GE.
     * @param id the ID of the item to sell on the GE. Pulls from the bot's bank.
     * @author Ceikry
     * @author Angle
     */
    fun sellOnGE(id: Int){
        class toCounterPulse : MovementPulse(bot, Location.create(3165, 3487, 0)){
            override fun pulse(): Boolean {
                var actualId = id
                val itemAmt = bot.bank.getAmount(id)
                if (ItemDefinition.forId(id).noteId == id){
                    actualId = Item(id).noteChange
                }
                val canSell = GrandExchange.addBotOffer(actualId, itemAmt)
                if (canSell && saleIsBigNews(actualId, itemAmt)) {
                    Repository.sendNews(SERVER_GE_NAME + " just offered " + itemAmt + " " + ItemDefinition.forId(actualId).name.toLowerCase() + " on the GE.")
                }
                bot.bank.remove(Item(id, itemAmt))
                bot.bank.refresh()
                return true
            }
        }
        bot.pulseManager.run(toCounterPulse())
    }

    /**
     * Function to sell all items in a bot's bank on the Grand Exchange, if they are tradable.
     * @author Ceikry
     */
    fun sellAllOnGe(){
        class toCounterPulseAll : MovementPulse(bot, Location.create(3165, 3487, 0)){
            override fun pulse(): Boolean {
                for(item in bot.bank.toArray()) {
                    item ?: continue
                    if (item.id == Items.LOBSTER_379) continue
                    if (item.id == Items.SWORDFISH_373) continue
                    if (item.id == Items.SHARK_385) continue
                    if(!item.definition.isTradeable) {continue}
                    val itemAmt = item.amount
                    var actualId = item.id
                    if (ItemDefinition.forId(actualId).noteId == actualId){
                        actualId = Item(actualId).noteChange
                    }
                    val canSell = GrandExchange.addBotOffer(actualId, itemAmt)
                    if (canSell && saleIsBigNews(actualId, itemAmt)) {
                        Repository.sendNews(SERVER_GE_NAME + " just offered " + itemAmt + " " + ItemDefinition.forId(actualId).name.toLowerCase() + " on the GE.")
                    }
                    bot.bank.remove(item)
                    bot.bank.refresh()
                }
                return true
            }
        }
        bot.pulseManager.run(toCounterPulseAll())
    }

    /**
     * Function to sell all items in a bot's bank on the Grand Exchange, if they are tradable.
     * @author Ceikry & Kermit
     */
    fun sellAllOnGeAdv(){
        val ge: Scenery? = getNearestNode("Desk", true) as Scenery?
        class toCounterPulseAll : MovementPulse(bot, ge, DestinationFlag.OBJECT) {
            override fun pulse(): Boolean {
                for(item in bot.bank.toArray()) {
                    item ?: continue
                    if(!item.definition.isTradeable) {continue}
                    val itemAmt = item.amount
                    var actualId = item.id
                    if (ItemDefinition.forId(actualId).noteId == actualId){
                        actualId = Item(actualId).noteChange
                    }
                    val canSell = GrandExchange.addBotOffer(actualId, itemAmt)
                    if (canSell && saleIsBigNews(actualId, itemAmt)) {
                        when (actualId){
                            1511 -> continue
                            1513 -> continue
                            1515 -> continue
                            1517 -> continue
                            1519 -> continue
                            1521 -> continue
                            else -> sendNews(SERVER_GE_NAME + " just offered " + itemAmt + " " + ItemDefinition.forId(actualId).name.lowercase() + " on the GE.")
                        }
                    }
                    bot.bank.remove(item)
                    bot.bank.refresh()
                }
                return true
            }
        }
        if (ge != null) {
            bot.pulseManager.run(toCounterPulseAll())
        }
    }

    /**
     * Function to bank all items that are not excluded at a nearby bank.
     * @author Kermit & Ceikry
     */
    fun depositAtBank(){
        val bank: Scenery? = getNearestNode("Bank booth", true) as Scenery?
        class BankingPulse : MovementPulse(bot, bank, DestinationFlag.OBJECT) {
            override fun pulse(): Boolean {
                bot.faceLocation(bank?.location)
                for (item in bot.inventory.toArray()) {
                    item ?: continue
                    when (item.id) {
                        Items.RUNE_AXE_1359, Items.TINDERBOX_590, Items.ADAMANT_PICKAXE_1271, Items.COINS_995 -> continue
                    }
                    bot.bank.add(item)
                    bot.inventory.remove(item)
                }
//                log(this::class.java, Log.FINE, "${bot.username} Just finished banking at ${bot.location} || Bank contents: ${bot.bank}")
                return true
            }
        }
        if (bank != null) {
            bot.pulseManager.run(BankingPulse())
        }
    }

    /**
     * Function to determine whether or not to bother everyone on the server
     * with the big news that a bot is selling something to the GE, based on item value
     * @param itemID
     * @param amount
     * @author Gexja
     */
    fun saleIsBigNews(itemID: Int, amount: Int): Boolean {
        return ItemDefinition.forId(itemID).getAlchemyValue(true) * amount >= (GameWorld.settings?.ge_announcement_limit
            ?: 500)
    }

    /**
     * Function to teleport a bot to the given location.
     * @param loc the location to teleport to
     * @author Ceikry
     */
    fun teleport(loc: Location) : Boolean {
        if (bot.isTeleBlocked) {
            return false
        }
        bot.lock()
        bot.visualize(ANIMATIONUP, GRAPHICSUP)
        bot.impactHandler.disabledTicks = 4
        val location = loc
        GameWorld.Pulser.submit(object : Pulse(4, bot) {
            override fun pulse(): Boolean {
                bot.unlock()
                bot.properties.teleportLocation = location
                bot.pulseManager.clear()
                bot.animator.reset()
                return true
            }
        })
        return true
    }

    /**
     * Takes the given item out of the bot's inventory and places it into the bank. Banks all items with matching ID.
     * @param item the ID of the item to bank
     * @author Ceikry
     */
    fun bankItem(item: Int){
        class BankingPulse() : Pulse(20){
            override fun pulse(): Boolean {
                val logs = bot.inventory.getAmount(item)
                bot.inventory.remove(Item(item, logs))
                bot.bank.add(Item(item, logs))
                return true
            }
        }
        bot.pulseManager.run(BankingPulse())
    }

    /**
     * Takes every item out of the bots inventory and places it into the bank.
     * @param none
     * @author cfunnyman joe
     */
    fun bankAll(onComplete: (() -> Unit)? = null){
        class BankingPulse() : Pulse(20){
            override fun pulse(): Boolean {
                for(item in bot.inventory.toArray()){
					if(item != null) {
						var itemAmount = bot.inventory.getAmount(item)

						if(bot.inventory.remove(item)) {
							bot.bank.add(item)
						}
					}
                }
				if(onComplete != null) {
					onComplete?.invoke()
				}
                return true
            }
        }
        bot.pulseManager.run(BankingPulse())
    }

    /**
     * Function that makes the bot eat the food item in their inventory if their HP is below 2/3.
     * @author Ceikry
     * @param foodId the ID of the food item to eat
     */
    fun eat(foodId: Int) {
        val foodItem = Item(foodId)
        if (bot.skills.getStaticLevel(Skills.HITPOINTS) * RandomFunction.random(0.5, 0.75) >= bot.skills.lifepoints && bot.inventory.containsItem(foodItem)) {
            bot.lock(3)
            //this.animate(new Animation(829));
            val food = bot.inventory.getItem(foodItem)
            var consumable: Consumable? = Consumables.getConsumableById(foodId)?.consumable
            if (consumable == null) {
                consumable = Food(intArrayOf(food.id), HealingEffect(1))
            }
            consumable.consume(food, bot)
            bot.properties.combatPulse.delayNextAttack(3)
        }
    }

    /**
     * Same as the eat function, except that it forces the bot to eat regardless of HP.
     * @author Ceikry
     * @param foodId the ID of the food item to eat.
     */
    fun forceEat(foodId: Int) {
        val foodItem = Item(foodId)
        if (bot.inventory.containsItem(foodItem)) {
            bot.lock(3)
            //this.animate(new Animation(829));
            val food = bot.inventory.getItem(foodItem)
            var consumable: Consumable? = Consumables.getConsumableById(foodId)?.consumable
            if (consumable == null) {
                consumable = Food(intArrayOf(foodId), HealingEffect(1))
            }
            consumable.consume(food, bot)
            bot.properties.combatPulse.delayNextAttack(3)
        }
    }

    /**
     * Function for buying items off the GE.
     * @param itemID the id of the item to buy off the GE.
     * @param amount the amount to buy.
     * @return true if item was successfully bought, false if not.
     */
    fun buyFromGE(bot: Player, itemID: Int, amount: Int){
        GlobalScope.launch {
            val offer = GrandExchangeOffer()
            offer.itemID = itemID
            offer.sell = false
            offer.offeredValue = checkPriceOverrides(itemID) ?: ItemDefinition.forId(itemID).value
            offer.amount = amount
            offer.player = bot
            //GrandExchange.dispatch(bot, offer)
            AIRepository.addOffer(bot, offer)
            var bought: Boolean = false
            val latch = CountDownLatch(1)
            bot.pulseManager.run(object : Pulse(5) {
                override fun pulse(): Boolean {
                    bought = offer.completedAmount == offer.amount
                    latch.countDown()
                    return true
                }
            })
            latch.await()
            if(bought){
                bot.bank.add(Item(offer.itemID, offer.completedAmount))
                bot.bank.refresh()
            }
        }
    }

    /**
     * Method to allow a bot to withdraw items from its bank.
     * @param itemID the item to withdraw.
     * @param amount the amount to withdraw.
     * @author Ceikry
     */
    fun withdraw(itemID: Int, amount: Int){
        var item: Item? = null
        if(bot.bank.containsItem(Item(itemID, amount))){
            item = Item(itemID, amount)
        } else {
            item = Item(itemID, bot.bank.getAmount(itemID))
        }
        if(item.amount == 0) return
        if(!bot.inventory.hasSpaceFor(item)){
            item.amount = bot.inventory.getMaximumAdd(item)
        }
        bot.bank.remove(item)
        bot.inventory.add(item)
    }

    /**
     * Method to equip list of items and set stats
     * Useful for starting a bot up
     * @param items the list of items to equip
     * @author dginovker
     */
    fun equipAndSetStats(items: List<Item>?){
        if (items == null) return
        for(item in items){
            equipAndSetStats(item)
        }
    }

    /**
     * Method to equip a single item and set stats
     * Useful for starting a bot up
     * @param item the item to equip
     * @author dginovker
     */
    fun equipAndSetStats(item: Item){
        val configs = item.definition.handlers
        val slot = configs["equipment_slot"] ?: return
        bot.equipment.add(item, slot as Int,
            false,false)
        val reqs = configs["requirements"]
        if(reqs != null)
            for(req in configs["requirements"] as HashMap<Int,Int>)
                bot.skills.setStaticLevel(req.key, req.value)
        bot.skills.updateCombatLevel()
    }

    /**
     * Method to load appearance and equipment from JSON
     * Useful for starting a bot up
     * @param json the JSON object to load from (dumped via ::dumpappearance)
     * @author dginovker
     */
    fun loadAppearanceAndEquipment(json: JSONObject?) {
        if (json == null) return
        bot.equipment.clear()
        bot.appearance.parse(json["appearance"] as JSONObject)
        val equipment = json["equipment"] as JSONArray
        bot.equipment.parse(equipment)
        bot.appearance.sync()
        for (i in 0 until bot.equipment.capacity()) {
            val item = bot.equipment.get(i)
            if (item != null) {
                equipAndSetStats(item)
            }
        }
        // Set all combat stats to a function of the highest combat stat(otherwise you end up with lopsided stats)
        val highestCombatSkill = bot.skills.getStaticLevel(bot.skills.highestCombatSkillId)
        for (i in 0 until 7) {
            bot.skills.setStaticLevel(i, max((highestCombatSkill * 0.75).toInt(), bot.skills.getStaticLevel(i)))
        }
        bot.skills.updateCombatLevel()
    }

    fun getOverlay(): BottingOverlay {
        return BottingOverlay(bot)
    }

    /**
     * Function to check for price overrides.
     * @param id the id to check for overrides for.
     * @author Ceikry
     */
    fun checkPriceOverrides(id: Int): Int?{
        return when(id){
            else -> itemDefinition(id).getConfiguration(ItemConfigParser.GE_PRICE)
        }
    }

    class BottingOverlay(val player: Player){
        fun init(){
            player.interfaceManager.openOverlay(Component(195))
            player.packetDispatch.sendInterfaceConfig(195,5,true)
        }
        fun setTitle(title: String){
            player.packetDispatch.sendString(colorize("%B$title"),195,7)
        }
        fun setTaskLabel(label: String){
            player.packetDispatch.sendString(colorize("%B$label"),195,8)
        }
        fun setAmount(amount: Int){
            player.packetDispatch.sendString(colorize("%B$amount"),195,9)
        }
    }
}
