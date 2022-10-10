package rs09.game.ai.general.scriptrepository

import core.game.ge.OfferState
import core.game.interaction.DestinationFlag
import core.game.interaction.MovementPulse
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.InteractionType
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.prayer.BoneBuryingOptionPlugin
import core.game.node.entity.state.EntityState
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.impl.WildernessZone
import core.tools.RandomFunction
import org.rs09.consts.Items
import rs09.game.ai.AIRepository
import rs09.game.ai.pvmbots.CombatBotAssembler
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListeners
import rs09.game.node.entity.combat.CombatSwingHandler
import rs09.game.node.entity.combat.handlers.MagicSwingHandler
import rs09.game.node.entity.combat.handlers.MeleeSwingHandler
import rs09.game.node.entity.combat.handlers.RangeSwingHandler
import kotlin.random.Random

/**
 * A bot script for killing green dragons in the wilderness.. Capable of banking, selling on ge, eating, trash talking, buries bones when fleeing and more.
 * @param style The combat style the bot is going to use.
 * @param area (optional) What area the bot tries to kill dragons in.
 * @author Ceikry
 */
class GreenDragonKiller(val style: CombatStyle, area: ZoneBorders? = null) : Script() {
    companion object {
        val westDragons = ZoneBorders(2971,3606,2991,3628)
        val wildernessLine = ZoneBorders(3078,3523,3096,3523)
        val edgevilleLine = ZoneBorders(3078,3520,3096,3520)
        val bankZone = ZoneBorders(3092,3489,3094,3493)
        val trashTalkLines = arrayOf("Bro, seriously?", "Ffs.", "Jesus christ.", "????", "Friendly!", "Get a life dude", "Do you mind??? lol", "Lol.", "Kek.", "One sec burying all the bones.", "Yikes.", "Yeet", "Ah shit, here we go again.", "Cmonnnn", "Plz", "Do you have nothing better to do?", "Cmon bro pls", "I just need to get my prayer up bro jesus", "Reeeeeee", "I cant believe you've done this", "Really m8", "Zomg", "Aaaaaaaaaaaaaaaaaaaaa", "Rofl.", "Oh god oh fuck oh shit", "....", ":|", "A q p", "Hcim btw", "I hope the revenants kill your mum", "Wrap your ass titties", "Why do this", "Bruh", "Straight sussin no cap fr fr", "This ain't bussin dawg", "Really bro?")
    }
    var state = State.TO_BANK
    var handler: CombatSwingHandler? = null
    var lootDelay = 0
    var offerMade = false
    var trashTalkDelay = 0

    var food = if (Random.nextBoolean()){
        Items.LOBSTER_379
    } else if(Random.nextBoolean()){
        Items.SWORDFISH_373
    } else {
        Items.SHARK_385
    }

    var myBorders: ZoneBorders? = null
    val type = CombatBotAssembler.Type.MELEE

    override fun tick() {
        if(!bot.isActive){
            running = false
            return
        }

        checkFoodStockAndEat()

        when(state){

            State.KILLING -> {
                bot.properties.combatPulse.temporaryHandler = handler
                scriptAPI.attackNpcInRadius(bot,"Green dragon",20)
                state = State.LOOT_DELAYER
            }

            State.LOOT_DELAYER -> {
                if(lootDelay < 3)
                    lootDelay++
                else
                    state = State.LOOTING
            }


            State.RUNNING -> {
                val players = RegionManager.getLocalPlayers(bot.location)
                if(players.isEmpty()){
                    state = State.TO_DRAGONS
                } else {
                    if(bot.skullManager.level < 21){
                        if (scriptAPI.teleportToGE())
                            state = State.REFRESHING
                        return
                    }
                    sendTrashTalk()
                    attemptToBuryBone()
                    scriptAPI.walkTo(WildernessZone.getInstance().borders.random().randomLoc)
                }
            }

            State.LOOTING -> {
                lootDelay = 0
                val items = AIRepository.groundItems.get(bot)
                if(items.isNullOrEmpty()) {state = State.KILLING; return}
                if(bot.inventory.isFull) {
                    if(bot.inventory.containsItem(Item(food))){
                        scriptAPI.forceEat(food)
                    } else {
                        state = State.TO_BANK
                    }
                    return
                }
                items.toTypedArray().forEach {it: Item -> scriptAPI.takeNearestGroundItem(it.id)}
            }

            State.TO_BANK -> {
                if(!wildernessLine.insideBorder(bot) && bot.location.y > 3521)
                    scriptAPI.walkTo(wildernessLine.randomLoc)
                if(wildernessLine.insideBorder(bot)){
                    val ditch = scriptAPI.getNearestNode("Wilderness Ditch",true)
                    ditch ?: return
                    ditch.interaction.handle(bot,ditch.interaction[0])
                }
                if(!bankZone.insideBorder(bot))
                    scriptAPI.walkTo(bankZone.randomLoc)
                if(bankZone.insideBorder(bot)){
                    val bank = scriptAPI.getNearestNode("Bank Booth",true)
                    bank ?: return
                    bot.pulseManager.run(object: MovementPulse(bot,bank, DestinationFlag.OBJECT){
                        override fun pulse(): Boolean {
                            bot.faceLocation(bank.location)
                            state = State.BANKING
                            return true
                        }
                    })
                }
            }

            State.BANKING -> {
                bot.pulseManager.run(object: Pulse(25){
                    override fun pulse(): Boolean {
                        for(item in bot.inventory.toArray()){
                            item ?: continue
                            if(item.name.toLowerCase().contains("lobster") || item.name.toLowerCase().contains("swordfish") || item.name.toLowerCase().contains("shark")) continue
                            if(item.id == 995) continue
                            bot.bank.add(item)
                        }
                        bot.inventory.clear()
                        state = if(bot.bank.getAmount(food) < 10)
                            State.TO_GE
                         else
                            State.TO_DRAGONS
                        for(item in inventory)
                            bot.inventory.add(item)
                        scriptAPI.withdraw(food,10)
                        bot.fullRestore()
                        return true
                    }
                })
            }

            State.BUYING_FOOD -> {
                state = State.TO_DRAGONS
                bot.bank.add(Item(food,50))
                bot.bank.refresh()
                scriptAPI.withdraw(food, 10)
            }

            State.TO_DRAGONS -> {
                offerMade = false
                if(bot.location.x >= 3143){
                    if(bot.location != Location.create(3144, 3514, 0))
                        scriptAPI.walkTo(Location.create(3144, 3514, 0))
                    else {
                        val shortcut = scriptAPI.getNearestNode("Underwall Tunnel",true)
                        shortcut ?: return
                        InteractionListeners.run(shortcut.id, IntType.SCENERY, "climb-into", bot, shortcut)
                    }
                } else {
                    if (!edgevilleLine.insideBorder(bot) && bot.location.y < 3520) {
                        scriptAPI.walkTo(edgevilleLine.randomLoc)
                        return
                    }
                    if (edgevilleLine.insideBorder(bot)) {
                        val ditch = scriptAPI.getNearestNode("Wilderness Ditch", true)
                        ditch ?: return
                        ditch.interaction.handle(bot, ditch.interaction[0]).also { return }
                    }
                    if (bot.location.y > 3520 && !myBorders!!.insideBorder(bot))
                        scriptAPI.walkTo(myBorders!!.randomLoc).also { return }
                    if (myBorders!!.insideBorder(bot))
                        state = State.KILLING
                }
            }

            State.TO_GE -> {
                if(bot.location.x < 3143) {
                    if(bot.location == Location.create(3136, 3517, 0)){
                        val shortcut = scriptAPI.getNearestNode("Underwall Tunnel",true)
                        shortcut ?: return
                        InteractionListeners.run(shortcut.id, IntType.SCENERY, "climb-into", bot, shortcut)
                    } else {
                        scriptAPI.walkTo(Location.create(3136, 3517, 0))
                    }
                    return
                }
                if(bot.location != Location.create(3165, 3487, 0)) {
                    scriptAPI.walkTo(Location.create(3165, 3487, 0))
                } else {
                    state = State.SELL_GE
                }
            }

            State.SELL_GE -> {
                scriptAPI.sellAllOnGe()
                state = State.BUYING_FOOD
            }

            State.REFRESHING -> {
                running = false
                return
            }

        }
    }

    private fun attemptToBuryBone() {
        if (bot.inventory.containsAtLeastOneItem(Items.DRAGON_BONES_536)) {
            BoneBuryingOptionPlugin().handle(bot, bot.inventory.get(Item(Items.DRAGON_BONES_536)), "bury")
        }
    }

    private fun checkFoodStockAndEat() {
        if (bot.inventory.getAmount(food) < 3 && state == State.KILLING)
            state = State.TO_BANK
        scriptAPI.eat(food)
    }

    private fun sendTrashTalk() {
        if (trashTalkDelay-- == 0)
            scriptAPI.sendChat(trashTalkLines.random())
        else
            trashTalkDelay = RandomFunction.random(10, 30)
    }

    override fun newInstance(): Script {
        val script = GreenDragonKiller(style)
        val tier = CombatBotAssembler.Tier.MED
        script.bot = CombatBotAssembler().assembleMeleeDragonBot(tier, bot.startLocation)
        return script
    }

    enum class State {
        KILLING,
        RUNNING,
        LOOTING,
        LOOT_DELAYER,
        BANKING,
        TO_BANK,
        TO_DRAGONS,
        TO_GE,
        SELL_GE,
        REFRESHING,
        BUYING_FOOD

    }

    init {
        handler = MeleeSwinger(this)
        equipment.add(Item(Items.ANTI_DRAGON_SHIELD_1540))
        myBorders = westDragons
        skills[Skills.AGILITY] = 99
        bankZone.addException(ZoneBorders(3094, 3492,3094, 3492))
        bankZone.addException(ZoneBorders(3094, 3490,3094, 3490))
    }

    internal class MeleeSwinger(val script: GreenDragonKiller) : MeleeSwingHandler() {
        override fun canSwing(entity: Entity, victim: Entity): InteractionType? {
            if(victim is Player || victim.name.contains("revenant", ignoreCase = true)) {
                script.state = State.RUNNING
                script.bot.pulseManager.clear()
            }
            return super.canSwing(entity, victim)
        }
    }
}
