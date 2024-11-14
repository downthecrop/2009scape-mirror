package core.api

import com.moandjiezana.toml.Toml
import content.data.consumables.*
import content.data.skill.SkillingTool
import content.global.handlers.iface.ge.StockMarket
import content.global.skill.slayer.SlayerManager
import content.global.skill.slayer.Tasks
import content.global.skill.summoning.familiar.BurdenBeast
import core.ServerConstants
import core.api.utils.GlobalKillCounter
import core.api.utils.Vector
import core.cache.def.impl.AnimationDefinition
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.SceneryDefinition
import core.cache.def.impl.VarbitDefinition
import core.game.activity.Cutscene
import core.game.component.Component
import core.game.consumable.*
import core.game.container.impl.EquipmentContainer
import core.game.dialogue.DialogueFile
import core.game.dialogue.SkillDialogueHandler
import core.game.ge.GrandExchangeRecords
import core.game.interaction.*
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.impl.Animator
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.LogType
import core.game.node.entity.player.info.PlayerMonitor
import core.game.node.entity.player.link.HintIconManager
import core.game.node.entity.player.link.IronmanMode
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.audio.Audio
import core.game.node.entity.player.link.emote.Emotes
import core.game.node.entity.player.link.prayer.PrayerType
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.player.link.quest.QuestRepository
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.requirement.*
import core.game.shops.Shops
import core.game.system.config.ItemConfigParser
import core.game.system.config.ServerConfigParser
import core.game.system.task.Pulse
import core.game.system.timer.*
import core.game.system.timer.impl.*
import core.game.world.GameWorld
import core.game.world.GameWorld.Pulser
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.RegionManager.getRegionChunk
import core.game.world.map.path.Pathfinder
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.repository.Repository
import core.game.world.update.flag.*
import core.game.world.update.flag.chunk.AnimateObjectUpdateFlag
import core.game.world.update.flag.context.*
import core.net.packet.PacketRepository
import core.net.packet.context.DefaultContext
import core.net.packet.context.MusicContext
import core.net.packet.out.AudioPacket
import core.net.packet.out.MusicPacket
import core.tools.*
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds
import java.io.*
import java.util.regex.*
import kotlin.math.*

/**
 * Gets a skilling tool which the player has the level to use and is in their inventory.
 * @param player the player to get the tool for
 * @param pickaxe whether or not we are trying to get a pickaxe.
 * @return the tool which meets the requirements or null if none.
 */
fun getTool(player: Player, pickaxe: Boolean): SkillingTool? {
    return if (pickaxe) SkillingTool.getPickaxe(player) else SkillingTool.getHatchet(player)
}

/**
 * Check if a player has a given level (buffable/debuffable)
 * @param player the player to check the stat for
 * @param skill the Skill to check. There is an enum for this called Skills. Example: Skills.STRENGTH
 * @param level the level to check against
 * @return true if the check succeeds, false otherwise
 */
fun hasLevelDyn(player: Player, skill: Int, level: Int): Boolean {
    return player.skills.getLevel(skill) >= level
}

/**
 * Check if a player's static skill level meets a certain value
 * @param player the player to check the stat for
 * @param skill the Skill to check. There is an enum for this called Skills. Example: Skills.STRENGTH
 * @param level the level to check against
 * @return true if the check succeeds, false otherwise
 */
fun hasLevelStat(player: Player, skill: Int, level: Int): Boolean {
    return player.skills.getStaticLevel(skill) >= level
}

/**
 * Check the amount of a given item in the player's inventory
 * @param player the player whose inventory to check
 * @param id the ID of the item to check for the amount of
 * @return the amount of the given ID in the player's inventory
 */
fun amountInInventory(player: Player, id: Int): Int {
    return player.inventory.getAmount(id)
}

/**
 * Check the amount of a given item in the player's bank
 * @param player the player to check
 * @param id the ID of the item to check for
 * @param includeSecondary if the secondary bank should be included in the search
 * @return the amount of the ID in the player's bank.
 */
fun amountInBank(player: Player, id: Int, includeSecondary: Boolean = true): Int {
    return getAmountInBank(player, id) + if (includeSecondary) getAmountInBank(player, id, true) else 0
}

/**
 * More performant way to check the amount of a given item in the player's bank
 * Uses the alwaysStack property of banks to short circuit the search.
 * @param player the player to check
 * @param id the ID of the item to check for
 * @param secondary if the secondary bank should be searched instead
 * @return the amount of the ID in the player's bank.
 */
private fun getAmountInBank(player: Player, id: Int, secondary: Boolean = false): Int {
    val bank = if (secondary) player.bankSecondary.toArray() else player.bankPrimary.toArray()
    bank.forEach { if (it?.id == id) return it.amount }
    return 0
}

/**
 * Check the amount of a given item in the player's equipment slots
 * @param player the player to check
 * @param id the ID of the item to check for
 * @return the amount of the ID in the player's equipment.
 */
fun amountInEquipment(player: Player, id: Int): Int {
    val slot = itemDefinition(id).getConfiguration(ItemConfigParser.EQUIP_SLOT, -1)
    val equipped = player.equipment[slot] ?: return 0
    return if (equipped.id == id) equipped.amount else 0
}

/**
 * Check if an item exists in a player's inventory
 * @param player the player whose inventory to check
 * @param id the ID of the item to check for
 * @param amount the amount to check for
 * @return true if the player has >= the given item in the given amount, false otherwise.
 */
fun inInventory(player: Player, id: Int, amount: Int = 1): Boolean {
    return player.inventory.contains(id, amount)
}

/**
 * Check if an item exists in a player's bank
 * @param player the player whose bank to check
 * @param id the ID of the item to check for
 * @param amount the amount to check for, defaults to 1
 * @return true if the item exists in the given amount in the player's bank
 */
fun inBank(player: Player, id: Int, amount: Int = 1): Boolean {
    return amountInBank(player, id) >= amount
}

/**
 * Check if an item exists in a player's equipment
 * @param player the player whose equipment to check
 * @param id the ID of the item to check for
 * @param amount the amount to check for, defaults to 1
 * @return true if the item exists in the given amount in the player's equipment
 */
fun inEquipment(player: Player, id: Int, amount: Int = 1): Boolean {
    return amountInEquipment(player, id) >= amount
}

/**
 * Check if an item exists in a player's equipment or inventory
 * @param player the player whose equipment to check
 * @param id the ID of the item to check for
 * @param amount the amount to check for, defaults to 1
 * @return true if the item exists in the given amount in the player's equipment or inventory
 */
fun inEquipmentOrInventory(player: Player, id: Int, amount: Int = 1): Boolean {
    // Proper, but slower implementation. Use faster unless need to check amounts split between equip/inv
    //return amountInEquipment(player, id) + amountInInventory(player, id) >= amount
    return inEquipment(player, id, amount) || inInventory(player, id, amount)
}

/**
 * Check that a set of items is equipped by the given player
 */
fun allInEquipment(player: Player, vararg ids: Int): Boolean {
    return ids.all { id ->
        inEquipment(player, id)
    }
}

/**
 * Check that at least one item from a set of items is equipped by the given player
 * @param player the player
 * @param ids the set of item ids to check
 * @return true if the player has at least one of the items equipped, false if none are equipped
 */
fun anyInEquipment(player: Player, vararg ids: Int): Boolean {
    return ids.any { id ->
        inEquipment(player, id)
    }
}

/**
 * Gets the item in the given equipment slot for the given player
 * @param player the player whose equipment to pull from
 * @param slot the Equipment slot to use, EquipmentSlot enum contains the options.
 * @return the Item in the given slot, or null if none.
 */
fun getItemFromEquipment(player: Player, slot: EquipmentSlot): Item? {
    return player.equipment.get(slot.ordinal)
}

class ContainerisedItem(val container: core.game.container.Container?, val itemId: Int) {
    fun remove() : Boolean {
        return this.container?.remove(this.itemId.asItem()) ?: false
    }
    fun exists() : Boolean {
        return this.container != null && this.itemId > -1
    }
}

/**
 * Check if player has the specified item ID equipped, in inventory, or in their bank
 * @param id The item ID to check
 * @return A ContainerisedItem containing the container and the item ID if found, otherwise ContainerisedItem(null, -1) if not found
 */
fun hasAnItem(player: Player, id: Int): ContainerisedItem {
    return hasAnItem(player, arrayOf(id), false)
}

/**
 * Check if player has the specified item ID equipped, in inventory, or in their bank
 * @param id The item ID to check
 * @param checkSecondBank Whether to check the player's second bank.
 * @return A ContainerisedItem containing the container and the item ID if found, otherwise ContainerisedItem(null, -1) if not found
 */
fun hasAnItem(player: Player, id: Int, checkSecondBank: Boolean): ContainerisedItem {
    return hasAnItem(player, arrayOf(id), checkSecondBank)
}

/**
 * Check if player has any of the specified item IDs equipped, in inventory, or in their bank
 * @param ids An array of item IDs to check
 * @param checkSecondBank Whether to check the player's second bank.
 * @return A ContainerisedItem containing the container and the item ID if found, otherwise ContainerisedItem(null, -1) if not found
 */
fun hasAnItem(player: Player, ids: Array<Int>, checkSecondBank: Boolean): ContainerisedItem {
    val searchSpace = if (checkSecondBank) {
        arrayOf(player.inventory, player.equipment, player.bankPrimary, player.bankSecondary)
    } else {
        arrayOf(player.inventory, player.equipment, player.bankPrimary)
    }
    for (container in searchSpace) {
        for (id in ids) {
            if (container.containItems(id)) {
                return ContainerisedItem(container, id)
            }
        }
    }
    return ContainerisedItem(null, -1)
}

/**
 * Check if a player has an item equipped which corresponds to the given God
 * @param player the player to check
 * @param god the God whose equipment we are checking for
 * @return true if the player has an item corresponding to the given god, false otherwise
 */
fun hasGodItem(player: Player, god: God): Boolean {
    god.validItems.forEach { if (inEquipment(player, it)) return true }
    return false
}

/**
 * Check if the given player should have the "remove nothing" RoW effect active.
 * @param player the player we are checking
 * @return whether we should ignore nothings
 */
fun shouldRemoveNothings(player: Player) : Boolean {
    val ring = getItemFromEquipment(player, EquipmentSlot.RING)
    return ring != null && ring.id in Items.RING_OF_WEALTH_14638..Items.RING_OF_WEALTH4_14646
}

/**
 * Remove an item from a player's inventory
 * @param player the player whose inventory to remove the item from
 * @param item the ID or Item object to remove from the player's inventory
 * @param container the Container to remove the items from. An enum exists for this in the api package called Container. Ex: api.Container.BANK
 */
fun <T> removeItem(player: Player, item: T, container: Container = Container.INVENTORY): Boolean {
    item ?: return false
    val it = when (item) {
        is Item -> item
        is Int -> Item(item)
        else -> throw IllegalStateException("Invalid value passed for item")
    }

    return when (container) {
        Container.INVENTORY -> player.inventory.remove(it)
        Container.BANK -> player.bank.remove(it) || player.bankSecondary.remove(it)
        Container.EQUIPMENT -> player.equipment.remove(it)
    }
}

/**
 * Add an item to the given player's given container
 * @param player the player whose container to modify
 * @param id the ID of the item to add
 * @param amount the amount of the item to add
 * @param container the Container to modify
 * @return true if the item was successfully added
 */
fun addItem(player: Player, id: Int, amount: Int = 1, container: Container = Container.INVENTORY): Boolean {
    val cont = when (container) {
        Container.INVENTORY -> player.inventory
        Container.BANK -> player.bank
        Container.EQUIPMENT -> player.equipment
    }

    return cont.add(Item(id, amount))
}

/**
 * Replaces the item in the given slot in the given container with the given item.
 * @param player the player whose container to modify
 * @param slot the slot to use
 * @param item the item to replace the slot with
 * @param currentItem the current item that is being replaced
 * @param container the Container to modify
 * @return the item that was previously in the slot, or null if none.
 */
fun replaceSlot(player: Player, slot: Int, item: Item, currentItem: Item? = null, container: Container = Container.INVENTORY): Item? {
    val cont = when (container) {
        Container.INVENTORY -> player.inventory
        Container.EQUIPMENT -> player.equipment
        Container.BANK -> player.bank
    }

    if (item.id == 65535 || item.amount <= 0) {
        return cont.replace(null, slot)
    }

    if (currentItem == null) {
        return cont.replace(item, slot)
    }

    if (cont.remove(currentItem, slot, true)) {
        return cont.replace(item, slot)
    }

    PlayerMonitor.log(player, LogType.DUPE_ALERT, "Potential slot-replacement-based dupe attempt, slot: $slot, item: $item")
    val other = when (container) {
        Container.INVENTORY -> Container.EQUIPMENT
        else -> Container.INVENTORY
    }
    if (removeItem(player, currentItem, other))
        return cont.replace(item, slot)
    return null
}

/**
 * Replaces all items a player owns anywhere (equipment, inventory, bank, second bank)
 * @param player the player whose inventory to remove the item from
 * @param itemId the item ID to replace
 * @param replaceId the replacement item ID
 * @author Player Name
 */
fun replaceAllItems(player: Player, itemId: Int, replaceId: Int) {
    val item = Item(itemId)
    for (container in arrayOf(player.inventory, player.equipment, player.bankPrimary, player.bankSecondary)) {
        val hasItems = container.getAll(item)
        if (!item.definition.isStackable && (container == player.inventory || container == player.equipment)) {
            for (target in hasItems) {
                val newItem = Item(replaceId, target.amount)
                container.replace(newItem, target.slot, true)
            }
        } else {
            if (hasItems.size > 0) {
                val target = hasItems[0]
                var count = 0
                for (x in hasItems) {
                    count += x.amount
                }
                val newItem = Item(replaceId, count)
                container.replace(newItem, target.slot, true)
            }
            if (hasItems.size > 1) {
                for (i in 1 until hasItems.size) {
                    container.remove(hasItems[i], hasItems[i].slot, true)
                }
            }
        }
    }
}

/**
 * Add an item with a variable quantity or drop it if a player does not have enough space
 * @param player the player whose inventory to add to
 * @param id the ID of the item to add to the player's inventory
 * @param amount the amount of the ID to add to the player's inventory, defaults to 1
 */
fun addItemOrDrop(player: Player, id: Int, amount: Int = 1) {
    val item = Item(id, amount)
    if(amount == 1 || item.definition.isStackable) {
        if (!player.inventory.add(item)) GroundItemManager.create(item, player)
    } else {
        val singleItem = Item(id, 1)
        for(i in 0 until amount) {
            if(!player.inventory.add(singleItem)) GroundItemManager.create(singleItem, player)
        }
    }
}

/**
 * Add an item with a variable quantity or bank it if a player does not have enough space, or drop it if that still doesn't work
 * @param player the player whose inventory to add to
 * @param id the ID of the item to add to the player's inventory
 * @param amount the amount of the ID to add to the player's inventory, defaults to 1
 */
fun addItemOrBank(player: Player, id: Int, amount: Int = 1) {
    val item = Item(id, amount)
    if (!player.inventory.add(item)) {
        if (player.bankPrimary.add(item)) {
            sendMessage(player, colorize("%RThe ${item.name} has been sent to your bank."))
        } else if (player.bankSecondary.add(item)) {
            sendMessage(player, colorize("%RThe ${item.name} has been sent to your secondary bank."))
        } else {
            GroundItemManager.create(item, player)
            sendMessage(player, colorize("%RAs your inventory and bank account(s) are all full, the ${item.name} has been placed on the ground under your feet. Don't forget to grab it. (Also consider cleaning out some stuff, maybe? I mean, Jesus!)"))
        }
    }
}

/**
 * Clears an NPC with the "poof" smoke graphics commonly seen with random event NPCs.
 * @param npc the NPC object to initialize
 */
fun poofClear(npc: NPC) {
    submitWorldPulse(object : Pulse() {
        var counter = 0
        override fun pulse(): Boolean {
            when (counter++) {
                2 -> {
                    npc.isInvisible = true; Graphics.send(Graphics(86), npc.location)
                }
                3 -> npc.clear().also { return true }
            }
            return false
        }
    })
}

/**
 * Get number of free slots in a player's inventory
 * @param player the player to check
 * @return the number of free slots in the player's inventory
 */
fun freeSlots(player: Player): Int {
    return player.inventory.freeSlots()
}

/**
 * Get an animation by ID.
 * @param id the ID of the animation to use
 * @return an Animation object with the given ID.
 */
fun getAnimation(id: Int): Animation {
    return Animation(id)
}

/**
 * Get an animation by ID with priority
 * @param id the ID of the animation to get
 * @param priority the Animator.Priority enum instance to represent the desired priority
 * @return an Animation object with the given ID and priority
 */
fun getAnimationWithPriority(id: Int, priority: Animator.Priority): Animation {
    return Animation(id, Animator.Priority.values()[priority.ordinal])
}

/**
 * Reset a player's animator
 * @param player the player whose animator to reset
 */
fun resetAnimator(player: Player) {
    player.animator.animate(Animation(-1, Animator.Priority.VERY_HIGH))
}

/**
 *  Get the number of ticks an animation lasts
 *  @param animation the Animation object to check the duration of
 *  @return the number of ticks the given animation lasts for
 */
fun animationDuration(animation: Animation): Int {
    return animation.definition.durationTicks
}

fun animationCycles (animation: Int) : Int {
    val def = AnimationDefinition.forId(animation)
    return def.cycles
}

/**
 * Give a player some amount of experience in a specific skill
 * @param player the player to award XP to
 * @param skill the Skill ID to reward XP for. There is a Skills enum you can use for this. Example: Skills.STRENGTH
 * @param amount the amount, including decimal place, of experience to award
 */
fun rewardXP(player: Player, skill: Int, amount: Double) {
    player.skills.addExperience(skill, amount)
}

/**
 * Replace an object with the given revert timer
 * @param toReplace the Scenery instance we are replacing
 * @param with the ID of the Scenery we wish to replace toReplace with
 * @param forTicks the number of ticks the object should be replaced for. Use -1 for permanent.
 * @param loc the location to move the new object to if necessary. Defaults to null.
 */
fun replaceScenery(toReplace: Scenery, with: Int, forTicks: Int, loc: Location? = null) {
    val newLoc = when (loc) {
        null -> toReplace.location
        else -> loc
    }
    if (forTicks == -1) {
        SceneryBuilder.replace(toReplace, toReplace.transform(with, toReplace.rotation, newLoc))
    } else {
        SceneryBuilder.replace(toReplace, toReplace.transform(with, toReplace.rotation, newLoc), forTicks)
    }
    toReplace.isActive = false
}

/** 
 * Add a scenery to the world
 * @param sceneryId the ID of the scenery to add
 * @param location the location to place it at
 * @param rotation the rotation of the scenery (default 0)
 * @param type the type of the scenery (default 22)
 * @return the created scenery
*/
fun addScenery (sceneryId: Int, location: Location, rotation: Int = 0, type: Int = 22) : Scenery {
    val scenery = Scenery(sceneryId, location, type, rotation)
    SceneryBuilder.add(scenery)
    return scenery
}

fun addScenery (scenery: Scenery) {
    SceneryBuilder.add(scenery)
}

/**
 * Remove a scenery from the world
 * @param scenery the Scenery object to remove.
*/
fun removeScenery (scenery: Scenery) {
    SceneryBuilder.remove(scenery)
}

/**
 * Replace an object with the given revert timer with the given rotation
 * @param toReplace the Scenery instance we are replacing
 * @param with the ID of the Scenery we wish to replace toReplace with
 * @param forTicks the number of ticks the object should be replaced for. Use -1 for permanent.
 * @Param rotation the Direction of the rotation it should use. Direction.NORTH, Direction.SOUTH, etc
 * @param loc the location to move the new object to if necessary. Defaults to null.
 */
fun replaceScenery(toReplace: Scenery, with: Int, forTicks: Int, rotation: Direction, loc: Location? = null) {
    val newLoc = when (loc) {
        null -> toReplace.location
        else -> loc
    }
    val rot = when (rotation) {
        Direction.NORTH_WEST -> 0
        Direction.NORTH -> 1
        Direction.NORTH_EAST -> 2
        Direction.EAST -> 4
        Direction.SOUTH_EAST -> 7
        Direction.SOUTH -> 6
        Direction.SOUTH_WEST -> 5
        Direction.WEST -> 3
    }
    if (forTicks == -1) {
        SceneryBuilder.replace(toReplace, toReplace.transform(with, rot, newLoc))
    } else {
        SceneryBuilder.replace(toReplace, toReplace.transform(with, rot, newLoc), forTicks)
    }
    toReplace.isActive = false
}

/**
 * Gets the name of an item.
 * @param id the ID of the item to get the name of
 * @return the name of the item
 */
fun getItemName(id: Int): String {
    return ItemDefinition.forId(id).name
}

/**
 * Removes a ground item
 * @param node the GroundItem object to remove
 */
fun removeGroundItem(node: GroundItem) {
    GroundItemManager.destroy(node)
}

/**
 * Checks if a ground item is valid/still exists/should exist
 * @param node the GroundItem object to check the validity of
 * @return true if the node is valid, false otherwise
 */
fun isValidGroundItem(node: GroundItem): Boolean {
    return GroundItemManager.getItems().contains(node)
}

/**
 * Checks if a player has space for an item
 * @param player the player whose inventory to check
 * @param item the Item to check against
 * @return true if the player's inventory has space for the item
 */
fun hasSpaceFor(player: Player, item: Item): Boolean {
    return player.inventory.hasSpaceFor(item)
}

/**
 * Get the number of ticks passed since server startup
 */
fun getWorldTicks(): Int {
    return GameWorld.ticks
}

/**
 * Plays a jingle by id
 * @param player the player to play the jingle for
 * @param jingleId the jingle to play
 */
fun playJingle(player: Player, jingleId: Int) {
    PacketRepository.send(MusicPacket::class.java, MusicContext(player, jingleId, true))
}

/**
 * Impact an enemy with the given amount of damage and the given hitsplat type
 * @param entity the entity to damage
 * @param amount the amount of damage to deal
 * @param type the type of hit splat to use, ImpactHandler.HitsplatType is an enum containing these options.
 */
fun impact(entity: Entity, amount: Int, type: ImpactHandler.HitsplatType = ImpactHandler.HitsplatType.NORMAL) {
    entity.impactHandler.manualHit(entity, amount, type)
    if (entity is Player) playHurtAudio(entity)
}

/**
 * Get an item's definition
 * @param id the ID of the item to get the definition of
 * @return the ItemDefinition for the given ID.
 */
fun itemDefinition(id: Int): ItemDefinition {
    return ItemDefinition.forId(id)
}

/**
 * Check whether a node contains an interaction option.
 *
 * @param node: An NPC, a scenery object or an item to inspect.
 * @param option: The name of the interaction option to check for existence of.
 *
 * @return A Boolean value indicating the presence of the specified option in the given node.
 *
 * @author vddCore
 */
fun hasOption(node: Node, option: String): Boolean {
    return when (node) {
        is NPC -> node.definition.hasAction(option)
        is Scenery -> node.definition.hasAction(option)
        is Item -> node.definition.hasAction(option)
        else -> throw IllegalArgumentException("Expected an NPC, Scenery or an Item, got ${node.javaClass.simpleName}.")
    }
}

/**
 * Send an object animation
 */
fun animateScenery(player: Player, obj: Scenery, animationId: Int, global: Boolean = false) {
    player.packetDispatch.sendSceneryAnimation(obj, getAnimation(animationId), global)
}

/**
 * Send an object animation independent of a player
 */
fun animateScenery(obj: Scenery, animationId: Int) {
    val animation = Animation(animationId)
    animation.setObject(obj)
    getRegionChunk(obj.location).flag(AnimateObjectUpdateFlag(animation))
}

/**
 * Produce a ground item owned by the player
 */
fun produceGroundItem(player: Player, item: Int) {
    GroundItemManager.create(Item(item), player)
}

/**
 * Produces a ground item with the given ID and amount at the given location
 * @param owner the owner of the ground item, use null for none.
 * @param id the id of the item.
 * @param amount the amount of the item.
 * @param location the location of the item.
 * @return the created ground item.
 */
fun produceGroundItem(owner: Player?, id: Int, amount: Int, location: Location) : GroundItem {
   return GroundItemManager.create(Item(id, amount), location, owner)
}

/**
 * Spawns a projectile
 */
fun spawnProjectile(source: Entity, dest: Entity, projectileId: Int) {
    Projectile.create(source, dest, projectileId).send()
}

/**
 * Spawns a projectile with more advanced parameters
 * @param source the initial Location of the projectile
 * @param dest the final Location of the projectile
 * @param projectile the ID of the gfx used for the projectile
 * @param startHeight the height the projectile spawns at
 * @param endHeight the height the projectile ends at
 * @param delay the delay before the projectile spawns
 * @param speed the speed the projectile travels at
 * @param angle the angle the projectile travels at
 */
fun spawnProjectile(
    source: Location,
    dest: Location,
    projectile: Int,
    startHeight: Int,
    endHeight: Int,
    delay: Int,
    speed: Int,
    angle: Int
) {
    Projectile.create(
        source,
        dest,
        projectile,
        startHeight,
        endHeight,
        delay,
        speed,
        angle,
        source.getDistance(dest).toInt()
    ).send()
}

/**
 * Causes the given entity to face the given toFace
 * @param entity the entity you wish to face something
 * @param toFace the thing to face
 * @param duration how long you wish to face the thing for
 */
fun face(entity: Entity, toFace: Node, duration: Int = -1) {
    if (duration == -1) {
        when (toFace) {
            is Location -> entity.faceLocation(toFace)
            is Entity -> entity.face(toFace)
        }
    } else {
        when (toFace) {
            is Location -> entity.faceTemporary(toFace.asNpc(), duration)
            else -> entity.faceTemporary(toFace as Entity, duration)
        }
    }
}

/**
 * Causes the given entity to reset its face direction.
 * @param entity the entity to reset.
*/
fun resetFace (entity: Entity) {
    entity.face(null)
    entity.faceLocation(entity.location)
}

/**
 * Opens the given interface for the given player
 * @param player the player to open the interface for
 * @param id the ID of the interface to open
 */
fun openInterface(player: Player, id: Int) {
    player.interfaceManager.open(Component(id))
}

/**
 * Opens the given interface as an overlay for the given player
 * @param player the player to open the interface for
 * @param id the ID of the interface to open
 */
fun openOverlay(player: Player, id: Int) {
    player.interfaceManager.openOverlay(Component(id))
}

/**
 * Closes any open overlays for the given player
 * @param player the player to close for
 */
fun closeOverlay(player: Player) {
    player.interfaceManager.closeOverlay()
}

/**
 * Runs the given Emote for the given Entity
 * @param entity the entity to run the emote on
 * @param emote the Emotes enum entry to run
 */
fun emote(entity: Entity, emote: Emotes) {
    entity.animate(emote.animation)
}

/**
 * Sends a message to the given player.
 *
 * The message will be split on word boundaries into multiple lines so
 * that none of the lines will overflow the player's message box.
 *
 * @param player the player to send the message to.
 * @param message the message to send.
 */
fun sendMessage(player: Player, message: String) {
    player.sendMessages(*splitLines(message, 86))
}

/**
 * Forces an above-head chat message for the given entity
 * @param entity the entity to send the chat for
 * @param message the message to display
 */
fun sendChat(entity: Entity, message: String, delay: Int = -1) {
    if (delay > -1) {
        queueScript(entity, delay, QueueStrength.SOFT) {
            entity.sendChat(message)
            return@queueScript stopExecuting(entity)
        }
    } else entity.sendChat(message)
}

/**
 * Sends a message to a player's dialogue box
 * @param player the player to send the dialogue to
 * @param message the message to send, lines are split automatically.
 */
fun sendDialogue(player: Player, message: String) {
    player.dialogueInterpreter.sendDialogue(*splitLines(message))
}

/**
 * Sends a message to the player's dialogue box
 * @param player the player to send the dialogue to
 * @param lines the lines of dialogue to send. No automatic splitting.
 */
fun sendDialogueLines(player: Player, vararg message: String) {
    player.dialogueInterpreter.sendDialogue(*message)
}

/**
 * Sends options dialogue to the given player.
 * @param player the player to send the options to.
 * @param title the title of the dialogue options
 * @param options the options to present to the player
 */
fun sendDialogueOptions(player: Player, title: String, vararg options: String) {
    player.dialogueInterpreter.sendOptions(title, *options)
}

/**
 * Plays an animation on the entity
 * @param entity the entity to animate
 * @param anim the animation to play, can be an ID or an Animation object.
 * @param forced whether or not to force the animation (usually not necessary)
 */
fun <T> animate(entity: Entity, anim: T, forced: Boolean = false) {
    val animation = when (anim) {
        is Int -> Animation(anim)
        is Animation -> anim
        else -> throw IllegalStateException("Invalid value passed for anim")
    }

    if (forced) {
        entity.animator.forceAnimation(animation)
    } else {
        entity.animator.animate(animation)
    }
}

/**
 * Plays audio for the player
 * @param player the player to play the defined audio for
 * @param id the audio id to play
 * @param delay the delay in client cycles (50 cycles = 1 second)
 * @param loops the number of times to loop audio (for some audio ids it is used to define how many times to loop the audio)
 * @param location the location where the audio will play from (The sound will fade with distance)
 * @param radius the distance the audio can be heard from the defined location (default = 8 tiles if undefined)
 */
@JvmOverloads
fun playAudio(player: Player, id: Int, delay: Int = 0, loops: Int = 1, location: Location? = null, radius: Int = Audio.defaultAudioRadius) {
    PacketRepository.send(AudioPacket::class.java, DefaultContext(player, Audio(id, delay, loops, radius), location))
}

/**
 * Plays audio for players near a defined location
 * @param location the location where the audio will play from (The sound will fade with distance)
 * @param id the audio id to play
 * @param delay the delay in client cycles (50 cycles = 1 second)
 * @param loops the number of times to loop audio (for some audio ids it is used to define how many times to loop the audio)
 * @param radius the distance the audio can be heard from the defined location (default = 8 tiles if undefined)
 */
@JvmOverloads
fun playGlobalAudio(location: Location, id: Int, delay: Int = 0, loops: Int = 1, radius: Int = Audio.defaultAudioRadius) {
    val nearbyPlayers = RegionManager.getLocalPlayers(location, radius)
    for (player in nearbyPlayers) {
        PacketRepository.send(AudioPacket::class.java, DefaultContext(player, Audio(id, delay, loops, radius), location))
    }
}

/**
 * Plays a random hurt audio for the player based on gender
 * @param player the player to play hurt audio for
 * @param delay the delay in client cycles (50 cycles = 1 second)
 */
fun playHurtAudio(player: Player, delay: Int = 0) {
    val maleHurtAudio = intArrayOf(Sounds.HUMAN_HIT4_516, Sounds.HUMAN_HIT5_517, Sounds.HUMAN_HIT_518, Sounds.HUMAN_HIT_6_522)
    val femaleHurtAudio = intArrayOf(Sounds.FEMALE_HIT_506, Sounds.FEMALE_HIT_507, Sounds.FEMALE_HIT2_508, Sounds.FEMALE_HIT_2_510)
    if (player.isMale) {
        playAudio(player, maleHurtAudio.random(), delay)
    } else {
        playAudio(player, femaleHurtAudio.random(), delay)
    }
}

/**
 * Opens a dialogue with the given dialogue key or dialogue file, depending which is passed.
 * @param player the player to open the dialogue for
 * @param dialogue either the dialogue key or an instance of a DialogueFile
 * @param args various args to pass to the opened dialogue
 */
fun openDialogue(player: Player, dialogue: Any, vararg args: Any) {
    player.dialogueInterpreter.close()
    when (dialogue) {
        is Int -> player.dialogueInterpreter.open(dialogue, *args)
        is DialogueFile -> player.dialogueInterpreter.open(dialogue, *args)
        is SkillDialogueHandler -> dialogue.open()
        else -> log(ContentAPI::class.java, Log.ERR, "Invalid object type passed to openDialogue() -> ${dialogue.javaClass.simpleName}")
    }
}

/**
 * Closes any opened dialogue.
 */
fun closeDialogue(player: Player) {
    player.dialogueInterpreter.close()
    player.interfaceManager.closeChatbox()
}

/**
 * Gets an NPC with the given ID from the repository.
 * @param id the ID of the NPC to locate
 * @returns an NPC instance matching the ID if it finds one, null otherwise
 */
fun findNPC(id: Int): NPC? {
    return Repository.findNPC(id)
}

/**
 * Gets the spawned scenery from the world map using the given coordinates
 * @param x the X coordinate to use
 * @param y the Y coordinate to use
 * @param z the Z coordinate to use
 */
fun getScenery(x: Int, y: Int, z: Int): Scenery? {
    return RegionManager.getObject(z, x, y)
}

/**
 * Gets the spawned scenery from the world map using the given Location object.
 * @param loc the Location object to use.
 */
fun getScenery(loc: Location): Scenery? {
    return RegionManager.getObject(loc)
}

/**
 * Gets an NPC within render distance of the refLoc that matches the given ID
 * @param refLoc the Location to find the closes NPC to
 * @param id the ID of the NPC to locate
 * @returns an NPC instance matching the ID if it finds one, null otherwise
 */
fun findNPC(refLoc: Location, id: Int): NPC? {
    return Repository.npcs.firstOrNull { it.id == id && it.location.withinDistance(refLoc) }
}

/**
 * Gets an NPC with the given ID in the same general area  as the given Entity
 * @param entity the entity to search around
 * @param id the ID of the NPC to locate
 * @returns an NPC matching the given ID or null if none is found
 */
fun findLocalNPC(entity: Entity, id: Int): NPC? {
    return RegionManager.getLocalNpcs(entity).firstOrNull { it.id == id }
}

/**
 * Gets an NPC with the given ID in the same general area  as the given Entity
 * @param location The location to search around.
 * @param distance The maximum distance to the entity.
 * @returns an NPC matching the given ID or null if none is found
 */
fun findLocalNPCs(location: Location, distance: Int): MutableList<NPC> {
    return RegionManager.getLocalNpcs(location, distance)
}


/**
 * Gets a list of nearby NPCs that match the given IDs.
 * @param entity the entity to check around
 * @param ids the IDs of the NPCs to look for
 */
fun findLocalNPCs(entity: Entity, ids: IntArray): List<NPC> {
    return RegionManager.getLocalNpcs(entity).filter { it.id in ids }.toList()
}

/**
 * Gets a list of nearby NPCs that match the given IDs.
 * @param entity the entity to check around
 * @param ids the IDs of the NPCs to look for
 * @param distance The maximum distance to the entity.
 */
fun findLocalNPCs(entity: Entity, ids: IntArray, distance: Int): List<NPC> {
    return RegionManager.getLocalNpcs(entity, distance).filter { it.id in ids }.toList()
}

/**
 * @param regionId the ID of the region
 * @return a [ZoneBorders] encapsulating the entire region indicated by the provided regionId
 */
fun getRegionBorders(regionId: Int): ZoneBorders {
    return ZoneBorders.forRegion(regionId)
}

/**
 * Gets the value of an attribute key from the Entity's attributes store
 * @param entity the entity to get the attribute from
 * @param attribute the attribute key to use
 * @param default the default value to return if the attribute does not exist
 */
fun <T> getAttribute(entity: Entity, attribute: String, default: T): T {
    return entity.getAttribute(attribute, default)
}

/**
 * Sets an attribute key to the given value in an Entity's attribute store
 * @param entity the entity to set the attribute for
 * @param attribute the attribute key to use
 * @param value the value to set the attribute to
 */
fun <T> setAttribute(entity: Entity, attribute: String, value: T) {
    entity.setAttribute(attribute, value)
}

fun removeAttribute(entity: Entity, attribute: String) {
    entity.removeAttribute(attribute.replace("/save:",""))
}

fun removeAttributes(entity: Entity, vararg attributes: String) {
    for (attribute in attributes) removeAttribute(entity, attribute)
}

/**
 * Adds the given timer to the entity's active timers.
 * @param entity the entity whose timers are being added to
 * @param timer the timer being added
**/
fun registerTimer (entity: Entity, timer: RSTimer?) {
    if (timer == null) return
    entity.timers.registerTimer (timer)
}

/**
 * Used to fetch the existing, active, non-abstract and non-anonymous timer with the given identifier, or start a new timer if none exists and return that.
 * @param entity the entity whose timers we're retrieving
 * @param identifier the identifier of the timer, refer to the individual timer class for this token.
 * @param args various args to pass to the initialization of the timer, if applicable.
 * @return Either the existing active timer, or a new timer initialized with the passed args if none exists yet.
**/
fun getOrStartTimer (entity: Entity, identifier: String, vararg args: Any) : RSTimer? {
    val existing = getTimer (entity, identifier)
    if (existing != null)
        return existing
    return spawnTimer (identifier, *args).also { registerTimer (entity, it) }
}

/**
 * Used to fetch the existing, active, non-abstract and non-anonymous timer with the given type, or start a new timer if none exists and return that.
 * @param entity the entity whose timer we're retrieving
 * @param T the type of timer we are fetching
 * @param args the various args to pass to the initialization of the timer, if applicable.
 * @return Either the existing, active timer or a new timer initialized with the passed args if none exists yet.
**/
inline fun <reified T: RSTimer> getOrStartTimer (entity: Entity, vararg args: Any) : T {
    val existing = getTimer <T> (entity)
    if (existing != null)
        return existing
    return spawnTimer <T> (*args).also { registerTimer (entity, it) }
}

/**
 * Used to fetch a new instance of a registered (see: non-anonymous, non-abstract) timer with the given configuration args
 * @param identifier the string identifier for the timer. e.g. poison's is "poison"
 * @param args various arbitrary arguments to be passed to the timer's constructor. Refer to the timer in question for what the args are expected to be.
 * @return a timer instance configured with your given args, or null if no timer with the given key exists in the registry.
**/
fun spawnTimer (identifier: String, vararg args: Any) : RSTimer? {
    return TimerRegistry.getTimerInstance (identifier, *args)
}

/**
 * Used to fetch a new instance of a registered (see: non-anonymous, non-abstract) timer with the given configuration args
 * @param T the type of the timer you're trying to retrieve. The timer registry will be searched for a timer of this type.
 * @param args various arbitrary arguments to be passed to the timer's constructor. Refer to the timer in question for what the args are expected to be.
 * @return a timer instance configured with your given args, or null if the timer is not listed in the registry (if this happens, your timer is either abstract or anonymous.)
**/
inline fun <reified T: RSTimer> spawnTimer (vararg args: Any) : T {
    return TimerRegistry.getTimerInstance <T> (*args)!!
}

/**
 * Used to check if a timer of the given type is registered and active in the entity's timers.
 * @param T the type of timer
 * @param entity the entity whose timers are being checked
 * @return true if there is a timer registered and active with the given type.
**/
inline fun <reified T: RSTimer> hasTimerActive (entity: Entity) : Boolean {
    return getTimer<T>(entity) != null
}

/**
 * Used to get the active instance of a timer with the given type from the entity's timers.
 * @param T the type of timer
 * @param entity the entity whose timers we are checking
 * @return the active instance of the given type in the entity's timers, or null.
*/
inline fun <reified T: RSTimer> getTimer (entity: Entity) : T? {
    return entity.timers.getTimer<T>()
}

/**
 * Removes any active timers of the given type from the entity's active timers. This will remove ALL matching instances.
 * @param T the type of timer
 * @param entity the entity whose timers are being checked
**/
inline fun <reified T: RSTimer> removeTimer (entity: Entity) {
    entity.timers.removeTimer<T>()
}

/**
 * Used to check if a timer with the given identifier string is registered and active in the entity's timers.
 * @param identifier the identifier token assigned to the timer. You'll have to refer to the actual timer class for this string.
 * @param entity the entity whose timers are being checked
 * @return true if there's a timer with the given identifier active in the entity's timers
**/
fun hasTimerActive (entity: Entity, identifier: String) : Boolean {
    return getTimer (entity, identifier) != null
}

/**
 * Used to get the active instance of a timer with the given identifier from the entity's timers.
 * @param identifier the string identifier of the timer we are looking for. You'll have to refer to the actual timer class for this string.
 * @param entity the entity whose timers are being checked
 * @return the instance of the active timer if found, null otherwise.
**/
fun getTimer (entity: Entity, identifier: String) : RSTimer? {
    return entity.timers.getTimer(identifier)
}

/**
 * Removes any active timers with the given identifier from the entity's active timers. This will remove ALL matching instances.
 * @param identifier the identifier token assigned to the timer. You'll have to refer to the actual timer class for this string.
 * @param entity the entity whose timers are being checked
**/
fun removeTimer (entity: Entity, identifier: String) {
    entity.timers.removeTimer(identifier)
}

/**
 * Removes the given timer from the entity's active timers. Note this variant will only work with a reference to the same exact timer you want to remove.
 * @param entity the entity whose timers we are modifying
 * @param timer the timer to remove
**/
fun removeTimer (entity: Entity, timer: RSTimer) {
    entity.timers.removeTimer(timer)
}

/**
 * Locks the given entity for the given number of ticks
 * @param entity the entity to lock
 * @param duration the number of ticks to lock for
 */
fun lock(entity: Entity, duration: Int) {
    entity.lock(duration)
}

/**
 * Locks specifically an entity's interactions, allowing movement still
 * @param entity the entity to lock
 * @param duration the duration in ticks to lock for
 */
fun lockInteractions(entity: Entity, duration: Int) {
    entity.locks.lockInteractions(duration)
}

/**
 * Unlocks the given entity
 * @param entity the entity to unlock
 */
fun unlock(entity: Entity) {
    entity.unlock()
}

/**
 * Transforms an NPC for the given number of ticks
 * @param npc the NPC object to transform
 * @param transformTo the ID of the NPC to turn into
 * @param restoreTicks the number of ticks until the NPC returns to normal
 */
fun transformNpc(npc: NPC, transformTo: Int, restoreTicks: Int) {
    npc.transform(transformTo)
    Pulser.submit(object : Pulse(restoreTicks) {
        override fun pulse(): Boolean {
            npc.reTransform()
            return true
        }
    })
}

/**
 * Produces a Location object using the given x,y,z values
 */
fun location(x: Int, y: Int, z: Int): Location {
    return Location.create(x, y, z)
}

/**
 * Checks if the given entity is within the given ZoneBorders
 */
fun inBorders(entity: Entity, borders: ZoneBorders): Boolean {
    return borders.insideBorder(entity)
}

/**
 * Checks if the given entity is within the given borders
 */
fun inBorders(entity: Entity, swX: Int, swY: Int, neX: Int, neY: Int): Boolean {
    return ZoneBorders(swX, swY, neX, neY).insideBorder(entity)
}

/**
 * AHeals the given entity for the given number of hitpoints
 */
fun heal(entity: Entity, amount: Int) {
    entity.skills.heal(amount)
}

fun getVarp (player: Player, varpIndex: Int) : Int {
    return player.varpMap[varpIndex] ?: 0
}

fun getVarbit (player: Player, def: VarbitDefinition) : Int {
    val mask = def.mask
    val current = getVarp (player, def.varpId)
    return (current shr def.startBit) and mask
}

fun getVarbit (player: Player, varbitId: Int) : Int {
    val def = VarbitDefinition.forId(varbitId)
    return getVarbit (player, def)
}

@JvmOverloads
fun setVarp (player: Player, varpIndex: Int, value: Int, save: Boolean = false) {
    player.varpMap[varpIndex] = value
    if (player.saveVarp[varpIndex] != true && save)
        player.saveVarp[varpIndex] = true //only set if we're choosing to save. Prevents accidental unsaving. if you REALLY want to unsave a varp, use unsaveVarp.
    player.packetDispatch.sendVarp(varpIndex, value)
}

fun saveVarp (player: Player, varpIndex: Int) {
    player.saveVarp[varpIndex] = true
}

fun unsaveVarp (player: Player, varpIndex: Int) {
    player.saveVarp.remove(varpIndex)
}

@JvmOverloads
fun setVarbit (player: Player, def: VarbitDefinition, value: Int, save: Boolean = false) {
    val mask = def.mask
    val current = getVarp (player, def.varpId) and (mask shl def.startBit).inv()
    val newValue = (value and mask) shl def.startBit
    setVarp (player, def.varpId, current or newValue, save)
}

@JvmOverloads
fun setVarbit (player: Player, varbitId: Int, value: Int, save: Boolean = false) {
    val def = VarbitDefinition.forId(varbitId)

    if (def == null) {
        logWithStack (ContentAPI::class.java, Log.ERR, "Trying to setVarbit $varbitId, which doesn't seem to exist.")
        return
    }
    
    setVarbit (player, def, value, save)
}

fun setVarc (player: Player, varc: Int, value: Int)
{
    player.packetDispatch.sendVarcUpdate(varc.toShort(), value)
}

fun reinitVarps (player: Player) {
    for ((index, value) in player.varpMap) {
        setVarp(player, index, value, player.saveVarp[index] ?: false)
    }
}

/**
 * Force an entity to walk to a given destination.
 * @param entity the entity to forcewalk
 * @param dest the Location object to walk to
 * @param type the type of pathfinder to use. "smart" for the SMART pathfinder, "clip" for the noclip pathfinder, anything else for DUMB.
 */
fun forceWalk(entity: Entity, dest: Location, type: String) {
    if (type == "clip") {
        ForceMovement(entity, dest, 10, 10).run()
        return
    }
    val pathfinder = when (type) {
        "smart" -> Pathfinder.SMART
        else -> Pathfinder.DUMB
    }
    val path = Pathfinder.find(entity, dest, true, pathfinder)
    path.walk(entity)
}

/**
 * Returns a location truncated to the appropriate pathfinding limit
**/
fun truncateLoc (mover: Entity, destination: Location) : Pair<Boolean,Location> {
    val vector = Vector.betweenLocs(mover.location, destination)
    val normVec = vector.normalized()
    val mag = vector.magnitude()

    var multiplier = if (mover is NPC) 14.0 else ServerConstants.MAX_PATHFIND_DISTANCE.toDouble()
    var clampedMultiplier = min(multiplier, mag)

    var truncated = multiplier == clampedMultiplier

    return Pair(truncated, mover.location.transform(normVec * clampedMultiplier))
}

/**
 * Force a player to move from the start location to the dest location
 * @param player the player we are moving
 * @param start the start location
 * @param dest the end location
 * @param startArrive the number of client cycles to take moving the player to the start location
 * @param destArrive the number of client cycles to take moving the player to the end location from the start location
 * @param direction (optional) the direction to face the player during the movement
 * @param anim (optional) the animation to use throughout the movement
 * @param callback (optional) a callback called when the forced  movement completes
 * @see NOTE: There are 30 client cycles per second.
*/
fun forceMove (player: Player, start: Location, dest: Location, startArrive: Int, destArrive: Int, dir: Direction? = null, anim: Int = 819, callback: (()->Unit)? = null) {
    var direction: Direction

    if (dir == null) {
        var delta = Location.getDelta(start, dest)
        var x = abs(delta.x)
        var y = abs(delta.y)

        if (x > y) 
            direction = Direction.getDirection(delta.x, 0) 
        else 
            direction = Direction.getDirection(0, delta.y)
    } else direction = dir

    val startLoc = Location.create(start)
    val destLoc = Location.create(dest)
    var startArriveTick = getWorldTicks() + cyclesToTicks (startArrive) + 1
    var destArriveTick = startArriveTick + cyclesToTicks (destArrive)
    var maskSet = false

    delayEntity(player, (destArriveTick - getWorldTicks()) + 1)
    queueScript (player, 0, QueueStrength.SOFT) { stage: Int ->
        if (!finishedMoving(player))
            return@queueScript keepRunning(player)
        if (!maskSet) {
            var ctx = ForceMoveCtx (startLoc, destLoc, startArrive, destArrive, direction)
            player.updateMasks.register(EntityFlag.ForceMove, ctx)
            maskSet = true
        }

        var tick = getWorldTicks()
        if (tick < startArriveTick) {
            return@queueScript keepRunning(player)
        } else if (tick < destArriveTick) {
            if (animationFinished(player))
                animate (player, anim)
            return@queueScript keepRunning(player)
        } else if (tick >= destArriveTick) {
            try {
                callback?.invoke()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            player.properties.teleportLocation = dest
            return@queueScript stopExecuting(player)
        }
        return@queueScript stopExecuting(player)
    }
}

/**
 * Interrupts a given entity's walking queue
 * @param entity the entity to interrupt
 */
fun stopWalk(entity: Entity) {
    entity.walkingQueue.reset()
}

/**
 * Returns a list of all valid children IDs for a given scenery ID
 */
fun getChildren(scenery: Int): IntArray {
    return SceneryDefinition.forId(scenery).getChildrenIds().filter { it != -1 }.toIntArray()
}

/**
 * Adjusts the charge for the given node.
 * @param node the node to adjust the charge of
 * @param amount the amount to adjust by
 */
fun adjustCharge(node: Node, amount: Int) {
    when (node) {
        is Item -> node.charge += amount
        is Scenery -> node.charge += amount
        else -> log(ContentAPI::class.java, Log.ERR, "Attempt to adjust the charge of invalid type: ${node.javaClass.simpleName}")
    }
}

/**
 * Get the current charge of the given node
 * @param node the node whose charge to check
 * @return amount of charges the node has, or -1 if the node does not accept charges.
 */
fun getCharge(node: Node): Int {
    when (node) {
        is Item -> return node.charge
        is Scenery -> return node.charge
        else -> log(ContentAPI::class.java, Log.ERR, "Attempt to get charge of invalid type: ${node.javaClass.simpleName}")
            .also { return -1 }
    }
}

/**
 * Set the charge of the given node to the given amount.
 * @param node the node to set the charge for
 * @param charge the amount to set the node's charge to (default is 1000)
 */
fun setCharge(node: Node, charge: Int) {
    when (node) {
        is Item -> node.charge = charge
        is Scenery -> node.charge = charge
        else -> log(ContentAPI::class.java, Log.ERR, "Attempt to set the charge of invalid type: ${node.javaClass.simpleName}")
    }
}

/**
 * Gets the used option in the context of an interaction.
 * @param player the player to get the used option for.
 * @return the option the player used
 */
fun getUsedOption(player: Player): String {
    return player.getAttribute("interact:option", "INVALID")
}

/**
 * Used to play both an Animation and Graphics object simultaneously.
 * Use -1 in place of anim or graphics if not needed.
 * @param entity the entity to perform this on
 * @param anim the Animation object to use, can also be an ID.
 * @param gfx the Graphics object to use, can also be an ID.
 */
fun <A, G> visualize(entity: Entity, anim: A, gfx: G) {
    val animation = when (anim) {
        is Int -> Animation(anim)
        is Animation -> anim
        else -> throw IllegalStateException("Invalid parameter passed for animation.")
    }

    val graphics = when (gfx) {
        is Int -> Graphics(gfx)
        is Graphics -> gfx
        else -> throw IllegalStateException("Invalid parameter passed for graphics.")
    }

    entity.visualize(animation, graphics)
}

/**
 * Used to submit a pulse to the GameWorld's Pulser.
 * @param pulse the Pulse object to submit
 */
fun submitWorldPulse(pulse: Pulse) {
    Pulser.submit(pulse)
}

/**
 * Used to submit a pulse to the GameWorld's Pulser, albeit with a cleaner syntax.
 * @param task an anonymous function that will be run in the Pulse
 * @return the newly submitted Pulse
 */
fun runWorldTask(task: () -> Unit): Pulse {
    val pulse = object : Pulse() {
        override fun pulse(): Boolean {
            task.invoke()
            return true
        }
    }

    submitWorldPulse(pulse)
    return pulse
}

/**
 * Teleports or "instantly moves" an entity to a given Location object.
 * @param entity the entity to move
 * @param loc the Location object to move them to
 * @param type the teleport type to use (defaults to instant). An enum exists as TeleportManager.TeleportType.
 */
fun teleport(entity: Entity, loc: Location, type: TeleportManager.TeleportType = TeleportManager.TeleportType.INSTANT) : Boolean {
    if (type == TeleportManager.TeleportType.INSTANT) {
        entity.properties.teleportLocation = loc
        return true
    }
    else return entity.teleporter.send(loc, type)
}

/**
 * Sets the dynamic or "temporary" (restores) level of a skill.
 * @param entity the entity to set the level for
 * @param skill the Skill to set. A Skills enum exists that can be used. Ex: Skills.STRENGTH
 * @param level the level to set the skill to
 */
fun setTempLevel(entity: Entity, skill: Int, level: Int) {
    entity.skills.setLevel(skill, level)
}

/**
 * Gets the static (unchanging/max) level of an entity's skill
 * @param entity the entity to get the level for
 * @param skill the Skill to get the level of. A Skills enum exists that can be used. Ex: Skills.STRENGTH
 * @return the static level of the skill
 */
fun getStatLevel(entity: Entity, skill: Int): Int {
    return entity.skills.getStaticLevel(skill)
}

/**
 * Gets the dynamic (boostable/debuffable/restoring) level of an entity's skill
 * @param entity the entity to get the level for
 * @param skill the Skill to get the level of. A Skills enum exists that can be used. Ex: Skills.STRENGTH
 * @return the dynamic level of the skill
 */
fun getDynLevel(entity: Entity, skill: Int): Int {
    return entity.skills.getLevel(skill)
}

/**
 * Adjusts (buffs/debuffs) the given Skill by the amount given.
 * @param entity the entity to adjust the skill for
 * @param skill the Skill to adjust. A Skills enum exists that can be used. Ex: Skills.STRENGTH
 * @param amount the amount to adjust the skill by. Ex-Buff: 5, Ex-Debuff: -5
 */
fun adjustLevel(entity: Entity, skill: Int, amount: Int) {
    entity.skills.setLevel(skill, entity.skills.getStaticLevel(skill) + amount)
}

/**
 * Remove all of a given item from the given container
 * @param player the player to remove the item from
 * @param item the item to remove. Can be an Item object or an ID.
 * @param container the Container to remove the item from. An enum exists for this called Container. Ex: Container.BANK
 */
fun <T> removeAll(player: Player, item: T, container: Container = Container.INVENTORY): Boolean {
    item ?: return false
    val it = when (item) {
        is Item -> item.id
        is Int -> item
        else -> throw IllegalStateException("Invalid value passed as item")
    }

    return when (container) {
        Container.EQUIPMENT -> player.equipment.remove(Item(it, amountInEquipment(player, it)))
        Container.BANK -> {
            val amountInPrimary = amountInBank(player, it, false)
            val amountInSecondary = amountInBank(player, it, true) - amountInPrimary
            player.bank.remove(Item(it, amountInPrimary)) && player.bankSecondary.remove(Item(it, amountInSecondary))
        }
        Container.INVENTORY -> player.inventory.remove(Item(it, amountInInventory(player, it)))
    }
}

/**
 * Sends a string to a specific interface child
 * @param player the player to send the packet to
 * @param string the string to send to the child
 * @param iface the ID of the interface to use
 * @param child the index of the child to send the string to
 */
fun setInterfaceText(player: Player, string: String, iface: Int, child: Int) {
    player.packetDispatch.sendString(string, iface, child)
}

/**
 * Allows you to hide or show specific children in an interface
 * @param player the player to send the packet to
 * @param iface the ID of the interface to use
 * @param child the index of the child to hide or show
 * @param hide if the child should be hidden or not
 */
fun setComponentVisibility(player: Player, iface: Int, child: Int, hide: Boolean) {
    player.packetDispatch.sendInterfaceConfig(iface, child, hide)
}

/**
 * Closes any open (non-chat) interfaces for the player
 * @param player the player to close the interface for
 */
fun closeInterface(player: Player) {
    player.interfaceManager.close()
}

/**
 * Closes any opened tab interfaces for the player
 * @param player the player to close the tab for
 */
fun closeTabInterface(player: Player) {
    player.interfaceManager.closeSingleTab()
}

/**
 * Closes any open (both chat and non-chat) interfaces for the player
 * @param player the player to close the interfaces for
 */
fun closeAllInterfaces(player: Player) {
    player.interfaceManager.close()
    player.interfaceManager.closeChatbox()
    player.dialogueInterpreter.close()
}

/**
 * Sends a dialogue that uses the player's chathead.
 * @param player the player to send the dialogue to
 * @param msg the message to send.
 * @param expr the FacialExpression to use. An enum exists for these called FacialExpression. Defaults to FacialExpression.FRIENDLY
 */
fun sendPlayerDialogue(player: Player, msg: String, expr: core.game.dialogue.FacialExpression = core.game.dialogue.FacialExpression.FRIENDLY) {
    player.dialogueInterpreter.sendDialogues(player, expr, *splitLines(msg))
}

/**
 * Sends a player model to a specific interface child
 * @param player the player to send the packet to and whose model to use
 * @param iface the ID of the interface to send it to
 * @param child the index of the child on the interface to send the model to
 */
fun sendPlayerOnInterface(player: Player, iface: Int, child: Int) {
    player.packetDispatch.sendPlayerOnInterface(iface, child)
}

/**
 * Sends a dialogue that uses the player's chathead.
 * @param player the player to send the dialogue to
 * @param npc the ID of the NPC to use for the chathead
 * @param msg the message to send.
 * @param expr the FacialExpression to use. An enum exists for these called FacialExpression. Defaults to FacialExpression.FRIENDLY
 */
fun sendNPCDialogue(player: Player, npc: Int, msg: String, expr: core.game.dialogue.FacialExpression = core.game.dialogue.FacialExpression.FRIENDLY) {
    player.dialogueInterpreter.sendDialogues(npc, expr, *splitLines(msg))
}

/**
 * Sends a dialogue that uses the player's chathead.
 * @param player the player to send the dialogue to
 * @param npc the ID of the NPC to use for the chathead
 * @param expr the FacialExpression to use. An enum exists for these called FacialExpression.
 * @param msg the message to send.
 */
fun sendNPCDialogueLines(player: Player, npc: Int, expr: core.game.dialogue.FacialExpression, hideContinue: Boolean, vararg msgs: String) {
    val dialogueComponent = player.dialogueInterpreter.sendDialogues(npc, expr, *msgs)
    player.packetDispatch.sendInterfaceConfig(dialogueComponent.id, msgs.size + 4, hideContinue)
}

/**
 * Sends an animation to a specific interface child
 * @param player the player to send the packet to
 * @param anim the ID of the animation to send to the interface
 * @param iface the ID of the interface to send the animation to
 * @param child the index of the child on the interface to send the model to
 */
fun sendAnimationOnInterface(player: Player, anim: Int, iface: Int, child: Int) {
    player.packetDispatch.sendAnimationInterface(anim, iface, child)
}

/**
 * Register a logout listener to a player. Logout listeners are methods that run when a player logs out.
 * @param player the player to register the listener for
 * @param handler the method to run when the listener is invoked (when the player logs out)
 */
fun registerLogoutListener(player: Player, key: String, handler: (p: Player) -> Unit) {
    player.logoutListeners[key] = handler
}

/**
 * Removes a logout listener based on the key from a player
 * @param player the player to remove the logout listner from
 * @param key the key of the logout listener to remove.
 */
fun clearLogoutListener(player: Player, key: String) {
    player.logoutListeners.remove(key)
}

/**
 * Sends an item to a specific child on an interface
 * @param player the player to send the packet to
 * @param iface the ID of the interface to send the item onto
 * @Param child the index of the child on the interface to send the item onto
 * @param item the ID of the item to send
 * @param amount the amount of the item to send - defaults to 1
 */
fun sendItemOnInterface(player: Player, iface: Int, child: Int, item: Int, amount: Int = 1) {
    player.packetDispatch.sendItemOnInterface(item, amount, iface, child)
}

/**
 * Sends a zoomed item to a specific child on an interface
 * @param player the player to send the packet to
 * @param iface the ID of the interface to send the item onto
 * @Param child the index of the child on the interface to send the item onto
 * @param item the ID of the item to send
 * @param zoom the amount of zoom to apply to the item - defaults to 230
 */
fun sendItemZoomOnInterface(player: Player, iface: Int, child: Int, item: Int, zoom: Int = 230) {
    player.packetDispatch.sendItemZoomOnInterface(item, zoom, iface, child)
}

/**
 * Sends a dialogue box with a single item and some text
 * @param player the player to send it to
 * @param item the ID of the item to show
 * @param message the text to display
 */
fun sendItemDialogue(player: Player, item: Any, message: String) {
    val dialogueItem = when (item) {
        is Item -> item
        is Int -> Item(item)
        else -> {
            throw java.lang.IllegalArgumentException("Expected an Item or an Int, got ${item::class.java.simpleName}.")
        }
    }

    player.dialogueInterpreter.sendItemMessage(dialogueItem, *splitLines(message))
}

/**
 * Sends a dialogue box with two items and some text
 * @param player the player to send it to
 * @param item1 the ID of the first item to show
 * @param item2 the ID of the second item to show
 * @param message the text to display
 */
fun sendDoubleItemDialogue(player: Player, item1: Int, item2: Int, message: String) {
    player.dialogueInterpreter.sendDoubleItemMessage(item1, item2, message)
}

/**
 * Send an input dialogue to retrieve a specified value from the player
 * @param player the player to send the input dialogue to
 * @param numeric whether or not the input is numeric
 * @param prompt what to prompt the player
 * @param handler the method that handles the value gained from the input dialogue
 */
fun sendInputDialogue(player: Player, numeric: Boolean, prompt: String, handler: (value: Any) -> Unit) {
    if (numeric) sendInputDialogue(player, InputType.NUMERIC, prompt, handler)
    else sendInputDialogue(player, InputType.STRING_SHORT, prompt, handler)
}

/**
 * Send input dialogues based on type. Some dialogues are special and can't be covered by the other sendInputDialogue method
 * @param player the player to send the input dialogue to
 * @param type the input type to send - an enum is available for this called InputType
 * @param prompt what to prompt the player
 * @param handler the method that handles the value from the input dialogue
 */
fun sendInputDialogue(player: Player, type: InputType, prompt: String, handler: (value: Any) -> Unit) {
    when (type) {
        InputType.AMOUNT -> {
            player.setAttribute("parseamount", true)
            player.dialogueInterpreter.sendInput(true, prompt)
        }

        InputType.NUMERIC, InputType.STRING_SHORT -> player.dialogueInterpreter.sendInput(
            type != InputType.NUMERIC,
            prompt
        )

        InputType.STRING_LONG -> player.dialogueInterpreter.sendLongInput(prompt)
        InputType.MESSAGE -> player.dialogueInterpreter.sendMessageInput(prompt)
    }

    player.setAttribute("runscript", handler)
    player.setAttribute("input-type", type)
}

/**
 * Forces an NPC to "flee" from a player or other entity
 * @param entity the entity to make flee
 * @param from the entity to flee from
 */
fun flee(entity: Entity, from: Entity) {
    lock(entity, 5)
    face(entity, from, 5)

    val diffX = entity.location.x - from.location.x
    val diffY = entity.location.y - from.location.y

    forceWalk(entity, entity.location.transform(diffX, diffY, 0), "DUMB")
}

/**
 * Submits an individual or "weak" pulse to a specific entity's pulse manager. Pulses submitted this way can be overridden by other pulses.
 * @param entity the entity to submit the pulse to
 * @param pulse the pulse to submit
 */
fun submitIndividualPulse(entity: Entity, pulse: Pulse) {
    entity.pulseManager.run(pulse)
}

/**
 * Similar to submitIndividualPulse, but for non-repeating tasks, with a cleaner syntax.
 */
fun runTask(entity: Entity, delay: Int = 0, repeatTimes: Int = 1, task: () -> Unit) {
    var cycles = repeatTimes
    entity.pulseManager.run(object : Pulse(delay) {
        override fun pulse(): Boolean {
            task.invoke()
            return --cycles == 0
        }
    })
}

/**
 * Gets the number of QP a player has
 * @param player the player to get the QP for
 * @return the number of QP the player has
 */
fun getQuestPoints(player: Player): Int {
    return player.questRepository.points
}

/**
 * Gets the stage for the given quest for the given player
 */
fun getQuestStage(player: Player, quest: String): Int {
    return player.questRepository.getStage(quest)
}

/**
 * Sets the stage for the given quest for the given player
 */
fun setQuestStage(player: Player, quest: String, stage: Int) {
    player.questRepository.setStage(QuestRepository.getQuests()[quest]!!, stage)
    player.questRepository.syncronizeTab(player)
}

/**
 * Check if a quest is in progress
 */
fun isQuestInProgress(player: Player, quest: String, startStage: Int, endStage: Int): Boolean {
    return player.questRepository.getStage(quest) in startStage..endStage
}

/**
 * Check if a quest is complete
 */
fun isQuestComplete(player: Player, quest: String): Boolean {
    return player.questRepository.getStage(quest) == 100
}

/**
 * Get the quest for a player.
 * @param player The player to get the quest for.
 * @param quest The quest name string
 * @return the quest object
 */
fun getQuest(player: Player, quest: String): Quest {
    return player.questRepository.getQuest(quest)
}


/**
 * Check if a player meets the requirements to start a quest, and then starts it if they do. Returns success bool
 */
fun startQuest(player: Player, quest: String): Boolean {
    val quest = player.questRepository.getQuest(quest)
    val canStart = quest.hasRequirements(player)
    if (!canStart) return false
    quest.start(player)
    return true
}

/**
 * Finishes a quest, gives rewards, marks as completed, etc
 */
fun finishQuest(player: Player, quest: String) {
    player.questRepository.getQuest(quest).finish(player)
}

/**
 * Gets a scenery definition from the given ID
 * @param id the ID of the scenery to get the definition for.
 * @return the scenery definition
 */
fun sceneryDefinition(id: Int): SceneryDefinition {
    return SceneryDefinition.forId(id)
}

/**
 * Register a map zone
 * @param zone the zone to register
 * @param borders the ZoneBorders that compose the zone
 */
fun registerMapZone(zone: MapZone, borders: ZoneBorders) {
    ZoneBuilder.configure(zone)
    zone.register(borders)
}

/**
 * Animates a component of an interface.
 * @param player the player to animate the interface for.
 * @param iface the ID of the interface to animate.
 * @param child the child on the interface to animate.
 * @param anim the ID of the animation to use.
 */
fun animateInterface(player: Player, iface: Int, child: Int, anim: Int) {
    player.packetDispatch.sendAnimationInterface(anim, iface, child)
}

/**
 * Adds a climb destination to the ladder handler.
 * @param ladderLoc the location of the ladder/stairs object you want to climb.
 * @param dest the destination for the climb.
 */
fun addClimbDest(ladderLoc: Location, dest: Location) {
    core.game.global.action.SpecialLadders.add(ladderLoc, dest)
}

/**
 * Sends a news announcement in game chat.
 * @param message the message to announce
 */
fun sendNews(message: String) {
    Repository.sendNews(message, 12, "CC6600")
}

/**
 * Sends a given Graphics object, or graphics ID, to the given location.
 * @param gfx the Graphics object, or the Integer ID of the graphics, to send. Either works.
 * @param location the location to send it to
 */
fun <G> sendGraphics(gfx: G, location: Location) {
    when (gfx) {
        is Int -> Graphics.send(Graphics(gfx), location)
        is Graphics -> Graphics.send(gfx, location)
    }
}

/**
 * Instructs the given player's client to execute a given CS2 script with the given arguments
 * @param player the Player
 * @param scriptId the ID of the CS2 script to execute
 * @param arguments the various arguments passed to the script. 
**/
fun runcs2 (player: Player, scriptId: Int, vararg arguments: Any) {
    var typeString = StringBuilder()
    var finalArgs = Array<Any?> (arguments.size) { null }
    try {
        for (i in 0 until arguments.size) {
            val arg = arguments[i]
            if (arg is Int)
                typeString.append("i")
            else if (arg is String)
                typeString.append("s")
            else
                throw IllegalArgumentException("Argument at index $i ($arg) is not an acceptable type! Only string and int are accepted.")
            finalArgs[arguments.size - i - 1] = arg
        }
        player.packetDispatch.sendRunScript(scriptId, typeString.toString(), *finalArgs)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Opens a generic item selection prompt with a glowing background, with your own callback to handle the selection.
 * @param player the player we are openinig the prompt for
 * @param options the right-click options the items should have
 * @param keepAlive whether or not the selection prompt should remain open for multiple interactions
 * @param callback a callback to handle the selection. The parameters passed to the callback are the slot in the inventory of the selected item, and the 0-9 index of the option clicked.
**/
@JvmOverloads
fun sendItemSelect (player: Player, vararg options: String, keepAlive: Boolean = false, callback: (slot: Int, optionIndex: Int) -> Unit) {
    player.interfaceManager.openSingleTab(Component(12))
    val scriptArgs = arrayOf ((12 shl 16) + 18, 93, 4, 7, 0, -1, "", "", "", "", "", "", "", "", "")
    for (i in 0 until min(9, options.size))
        scriptArgs[6 + i] = options[i]
    runcs2(player, 150, *scriptArgs)
    val settings = IfaceSettingsBuilder()
        .enableOptions(0 until 9)
        .build()
    player.packetDispatch.sendIfaceSettings(settings, 18, 12, 0, 28)
    setAttribute(player, "itemselect-callback", callback)
    setAttribute(player, "itemselect-keepalive", keepAlive)
}

fun announceIfRare(player: Player, item: Item) {
    if (item.definition.getConfiguration(ItemConfigParser.RARE_ITEM, false)) {
        sendNews("${player.username} has just received: ${item.amount} x ${item.name}.")
        GlobalKillCounter.incrementRareDrop(player, item)
    }
}

fun hasRequirement (player: Player, req: QuestReq, message: Boolean = true) : Boolean {
    var (isMet, unmetReqs) = req.evaluate(player)
    val messageList = ArrayList<String>()

    var totalSoftQp = 0
    var totalHardQp = 0

    for (req in unmetReqs) {
        when (req) {
            is QPCumulative -> totalSoftQp += req.amount
            is QPReq -> if (req.amount > totalHardQp) totalHardQp = req.amount
        }
    }

    var neededQp = min(max(totalSoftQp, totalHardQp), player.questRepository.getAvailablePoints())

    isMet = isMet && neededQp <= player.questRepository.getPoints()

    if (isMet) return true
    
    if (unmetReqs.size == 2 && unmetReqs[0] is QuestReq) {
        messageList.add ("This requires completion of ${(unmetReqs[0] as QuestReq).questReq.questName} to access.")
    } else {
        messageList.add ("You need the pre-reqs for ${req.questReq.questName} to access this.")
        messageList.add ("Please check the page in your quest journal for more info.")
    }

    if (message)
        for (message in messageList) 
            sendMessage(player, message)

    return false
}

@JvmOverloads
fun hasRequirement (player: Player, quest: String, message: Boolean = true) : Boolean {
    val questReq = QuestRequirements.values().filter { it.questName.equals(quest, true) }.firstOrNull() ?: return false
    return hasRequirement(player, QuestReq(questReq), message)
}

/**
 * Generates a list of skill names which the player has mastered
 * @param player the player
 * @return a List<String> of skill names
 */
fun getMasteredSkillNames(player: Player): List<String> {
    val hasMastered = player.getSkills().masteredSkills > 0
    val masteredSkills = ArrayList<String>()

    if (hasMastered) {
        for ((skillId, skillName) in Skills.SKILL_NAME.withIndex()) {
            //phil you were looping every skill and performing a string comparison (another loop) for every one here.
            //that's some real shit code bro.
            //you were also performing a dynamic level check rather than a static.
            if (hasLevelStat(player, skillId, 99)) {
                masteredSkills.add(skillName)
            }
        }
    }
    return masteredSkills
}

/**
 * Empties the provided container into the player's bank.
 * @param player the player
 * @param container the container to be emptied.
 * @return The amount of items which were successfully transferred to the bank.
 *
 * @author ceik
 * @author James Triantafylos
 * @author vddCore
 */
fun dumpContainer(player: Player, container: core.game.container.Container): Int {
    val bank = player.bank
    var dumpedCount = 0

    run beginDepositing@{
        container.toArray().filterNotNull().forEach { item ->
            if (!bank.hasSpaceFor(item)) {
                sendMessage(player, "You have no more space in your bank.")
                return@beginDepositing
            }

            if (!bank.canAdd(item)) {
                sendMessage(player, "A magical force prevents you from banking the ${item.name}.")
                return@forEach
            } else {
                if (container is EquipmentContainer) {
                    if (!InteractionListeners.run(item.id, player, item, false)) {
                        sendMessage(player, "A magical force prevents you from removing your ${item.name}.")
                        return@forEach
                    }
                }

                container.remove(item)
                bank.add(unnote(item), false)
                dumpedCount++
            }
        }
    }

    container.update()
    bank.update()

    return dumpedCount
}

/**
 * Attempts to empty the player's Beast of Burden's inventory
 * into the player's bank.
 *
 * @param player The player whose Beast of Burden's inventory to empty.
 *
 * @author vddCore
 */
fun dumpBeastOfBurden(player: Player) {
    val famMan = player.familiarManager

    if (!famMan.hasFamiliar()) {
        sendMessage(player, "You don't have a familiar.")
        return
    }

    if (famMan.familiar !is BurdenBeast) {
        sendMessage(player, "Your familiar is not a Beast of Burden.")
        return
    }

    val beast: BurdenBeast = (famMan.familiar as BurdenBeast)

    if (beast.container.isEmpty) {
        sendMessage(player, "Your familiar's inventory is empty.")
        return
    }

    val itemCount = beast.container.itemCount()
    val dumpedCount = dumpContainer(player, beast.container)


    when {
        dumpedCount == itemCount -> sendMessage(player, "Your familiar's inventory was deposited into your bank.")
        dumpedCount > 0 -> {
            val remainPhrase = when {
                (itemCount - dumpedCount == 1) -> "item remains"
                else -> "items remain"
            }

            sendMessage(player, "${itemCount - dumpedCount} $remainPhrase in your familiar's inventory.")
        }
    }
}

/**
 * Gets the player's familiar boost in the given skill
 *
 * @param player The player who owns the familiar.
 * @param skill The skill to check boost of.
 * @return The amount of skill boost gained from the player's familiar.
 *
 * @author bushtail
 */
fun getFamiliarBoost(player: Player, skill: Int): Int {
    return player.familiarManager.getBoost(skill)
}

/**
 * Converts an item into its noted representation.
 *
 * @param item The item to convert.
 * @return Noted form of the item or the item itself if it's already, or cannot be, noted.
 *
 * @author vddCore
 */
fun note(item: Item): Item {
    if (!item.definition.isUnnoted)
        return item

    if (item.definition.noteId < 0)
        return item

    return Item(item.definition.noteId, item.amount, item.charge)
}

/**
 * Converts a noted item into its unnoted representation.
 *
 * @param item The item to convert.
 * @return Unnoted form of the item, or the item itself if it's already unnoted.
 *
 * @author vddCore
 */
fun unnote(item: Item): Item {
    if (item.definition.isUnnoted)
        return item

    return Item(item.noteChange, item.amount, item.charge)
}

/**
 * Checks if the player has the Seal of Passage equipped or if it's present in the inventory.
 *
 * @param player The player to inspect for item's presence.
 * @return True if Seal of Passage present, false otherwise.
 */
fun hasSealOfPassage(player: Player): Boolean {
    return inEquipmentOrInventory(player, Items.SEAL_OF_PASSAGE_9083)
}

/**
 * Returns a boolean indicating if the player has a house.
 * @param player the player.
 * @return boolean indicating presence of house.
 */
fun hasHouse(player: Player): Boolean {
    return player.houseManager.hasHouse()
}

fun Player.getCutscene(): Cutscene? {
    return getAttribute<Cutscene?>(this, Cutscene.ATTRIBUTE_CUTSCENE, null)
}

fun Player.getCutsceneStage(): Int {
    return getAttribute(this, Cutscene.ATTRIBUTE_CUTSCENE_STAGE, 0)
}

fun getServerConfig(): Toml {
    return ServerConfigParser.tomlData ?: Toml()
}

fun getPathableRandomLocalCoordinate(target: Entity, radius: Int, center: Location, maxAttempts: Int = 3): Location {
    var maxRadius = Vector.deriveWithEqualComponents(ServerConstants.MAX_PATHFIND_DISTANCE.toDouble()).x - 1
    var effectiveRadius = min(radius, maxRadius.toInt())
    val swCorner = center.transform(-effectiveRadius, -effectiveRadius, center.z)
    val neCorner = center.transform(effectiveRadius, effectiveRadius, center.z)
    val borders = ZoneBorders(swCorner.x, swCorner.y, neCorner.x, neCorner.y, center.z)

    var attempts = maxAttempts
    var success: Boolean
    while (attempts-- > 0) {
        val dest = borders.randomLoc
        val path = Pathfinder.find(center, dest, target.size())
        success = path.isSuccessful && !path.isMoveNear
        if (success) return dest
    }

    return target.location
}

/**
 * Returns a pathable cardinal location in a 1 tile radius in order of west, south, east, north.
 * @param entity the entity used to check if the loc is pathable
 * @param center the center location
 */
fun getPathableCardinal(target: Entity, center: Location) : Location {
    var tiles = center.cardinalTiles

    for (tile in tiles) {
        val path = Pathfinder.find(center, tile, target.size())
        if (path.isSuccessful && !path.isMoveNear)
            return tile
    }

    return center
}

/**
 * Returns the player's active slayer task.
 * @author bushtail
 * @param player the player whose task we are checking.
 * @return the slayer task.
 */
fun getSlayerTask(player: Player): Tasks? {
    return SlayerManager.getInstance(player).activeTask
}

/**
 * Returns the name of the player's active slayer task.
 * @author bushtail
 * @param player the player whose task we are checking.
 * @return the name of the slayer task.
 */
fun getSlayerTaskName(player: Player): String {
    return SlayerManager.getInstance(player).taskName
}

/**
 * Returns the player's remaining kills for their active slayer task.
 * @author bushtail
 * @param player the player whose task we are checking.
 * @return the remaining kills of the slayer task.
 */
fun getSlayerTaskKillsRemaining(player: Player): Int {
    return SlayerManager.getInstance(player).amount
}

/**
 * Returns the player's slayer master as npc.
 * @author bushtail
 * @param player the player whose master we are checking.
 * @return the slayer master as NPC.
 */
fun getSlayerMaster(player: Player): NPC {
    return findNPC(SlayerManager.getInstance(player).master?.npc as Int) as NPC
}

/**
 * Returns the player's slayer master location as string.
 * @author bushtail
 * @param player the player whose master we are checking.
 * @return the slayer master location as String.
 */
fun getSlayerMasterLocation(player: Player): String {
    return when (getSlayerMaster(player).id) {
        NPCs.CHAELDAR_1598 -> "Zanaris"
        NPCs.DURADEL_8275 -> "Shilo Village"
        NPCs.MAZCHNA_8274 -> "Canifis"
        NPCs.TURAEL_8273 -> "Taverley"
        NPCs.VANNAKA_1597 -> "Edgeville Dungeon"
        else -> "The Backrooms"
    }
}

/**
 * Returns the player's slayer task tip as String.
 * @author bushtail
 * @param player the player whose task tip we are checking.
 * @return the task tip as String.
 */
fun getSlayerTip(player: Player): Array<out String> {
    return if (hasSlayerTask(player)) {
        getSlayerTask(player)?.tip!!
    } else {
        arrayOf("You need something new to hunt.")
    }
}

/**
 * Returns the player's slayer task flags as Int.
 * @author bushtail
 * @param player the player whose task flags we are checking.
 * @return the task flags as Int.
 */
fun getSlayerTaskFlags(player: Player): Int {
    return SlayerManager.getInstance(player).flags.taskFlags
}

/**
 * Returns whether or not the player has a task.
 * @author bushtail
 * @param player the player whose task we are checking.
 * @return has task as Boolean.
 */
fun hasSlayerTask(player: Player): Boolean {
    return SlayerManager.getInstance(player).hasTask()
}

/**
 * Checks whether a player plays in a specific Ironman mode.
 *
 * @param player Player whose Ironman status to check.
 * @param restriction The Ironman restriction level to check the player against.
 * @return Whether the player is restricted to the provided Ironman mode.
 */
fun hasIronmanRestriction(player: Player, restriction: IronmanMode): Boolean {
    return player.ironmanManager.isIronman
        && player.ironmanManager.mode.ordinal >= restriction.ordinal
}

/**
 * Conditionally executes an action based on the player's Ironman status.
 *
 * @param player Player whose action to restrict.
 * @param restriction Ironman mode that will be used as the restriction criterion.
 * @param action The action to be restricted.
 */
fun restrictForIronman(player: Player, restriction: IronmanMode, action: () -> Unit) {
    if (!player.ironmanManager.checkRestriction(restriction)) {
        action()
    }
}

/**
 * Opens the given player's Grand Exchange interface.
 *
 * @author vddCore
 * @param player The player whose Grand Exchange interface to open.
 */
fun openGrandExchange(player: Player) {
    restrictForIronman(player, IronmanMode.ULTIMATE) {
        StockMarket.openFor(player)
    }
}

/**
 * Checks if the player has any items to collect from the Grand Exchange
 *
 * @author vddCore
 * @param player The player whose Grand Exchange collection box to inspect.
 */
fun hasAwaitingGrandExchangeCollections(player: Player): Boolean {
    val records = GrandExchangeRecords.getInstance(player)

    for (record in records.offerRecords) {
        val offer = records.getOffer(record)

        return offer != null
            && offer.withdraw[0] != null
    }

    return false
}

/**
 * Opens the given player's Grand Exchange collection box.
 *
 * It will send a prohibition message to Ultimate Ironmen.
 *
 * @author vddCore
 * @param player The player whose collection box to open.
 */
fun openGrandExchangeCollectionBox(player: Player) {
    restrictForIronman(player, IronmanMode.ULTIMATE) {
        GrandExchangeRecords.getInstance(player).openCollectionBox()
    }
}

/**
 * Opens the given player's bank account. If the player has a PIN set,
 * the PIN interface will be shown first.
 *
 * It will send a prohibition message to Ultimate Ironmen.
 *
 * @author vddCore
 * @param player The player whose bank account to open.
 */
fun openBankAccount(player: Player) {
    restrictForIronman(player, IronmanMode.ULTIMATE) {
        player.bank.open()
    }
}

/**
 * Opens the given player's bank deposit box interface.
 *
 * It will send a prohibition message to Ultimate Ironmen.
 *
 * @author vddCore
 * @param player The player whose bank account to open.
 */
fun openDepositBox(player: Player) {
    restrictForIronman(player, IronmanMode.ULTIMATE) {
        player.bank.openDepositBox()
    }
}

/**
 * Opens the given player's bank PIN settings interface.
 *
 * It will send a prohibition message to Ultimate Ironmen.
 *
 * @author vddCore
 * @param player The player whose PIN settings to open.
 */
fun openBankPinSettings(player: Player) {
    restrictForIronman(player, IronmanMode.ULTIMATE) {
        player.bankPinManager.openSettings()
    }
}

enum class SecondaryBankAccountActivationResult {
    SUCCESS,
    ALREADY_ACTIVE,
    NOT_ENOUGH_MONEY,
    INTERNAL_FAILURE
}
/**
 * Activates the secondary bank account and handles all the fees required.
 *
 * @author vddCore
 * @param player The player whose secondary bank account to activate.
 * @returns Whether the operation was successful or not.
 */
fun activateSecondaryBankAccount(player: Player): SecondaryBankAccountActivationResult {
    if (hasIronmanRestriction(player, IronmanMode.ULTIMATE)) {
        return SecondaryBankAccountActivationResult.INTERNAL_FAILURE
    }

    if (hasActivatedSecondaryBankAccount(player)) {
        return SecondaryBankAccountActivationResult.ALREADY_ACTIVE
    }

    val cost = 5000000
    val coinsInInventory = amountInInventory(player, Items.COINS_995)
    val coinsInBank = amountInBank(player, Items.COINS_995)
    val coinsTotal = coinsInInventory + coinsInBank

    if (cost > coinsTotal) {
        return SecondaryBankAccountActivationResult.NOT_ENOUGH_MONEY
    }

    val operationResult = if (cost > coinsInInventory) {
        val amountToTakeFromBank = cost - coinsInInventory

        removeItem(player, Item(Items.COINS_995, coinsInInventory), Container.INVENTORY)
                && removeItem(player, Item(Items.COINS_995, amountToTakeFromBank), Container.BANK)
    } else {
        removeItem(player, Item(Items.COINS_995, cost))
    }

    return if (operationResult) {
        setAttribute(player, "/save:UnlockedSecondaryBank", true)
        SecondaryBankAccountActivationResult.SUCCESS
    } else {
        sendMessage(player, "$cost;$coinsInInventory;$coinsInBank;$coinsTotal")
        SecondaryBankAccountActivationResult.INTERNAL_FAILURE
    }
}

/**
 * Checks if the player has unlocked their secondary bank account.
 *
 * @author vddCore
 * @param player The player whose secondary bank activation status to inspect.
 * @return Secondary bank activation status.
 */
fun hasActivatedSecondaryBankAccount(player: Player): Boolean {
    return getAttribute(player, "UnlockedSecondaryBank", false)
}

/**
 * Toggles between the player's primary and/or secondary bank account.
 * Has no effect if the secondary bank account hasn't been activated.
 *
 * It will send a prohibition message to Ultimate Ironmen.
 *
 * @author vddCore
 * @param player The player whose bank accounts to toggle.
 */
fun toggleBankAccount(player: Player) {
    restrictForIronman(player, IronmanMode.ULTIMATE) {
        if (!hasActivatedSecondaryBankAccount(player)) {
            return@restrictForIronman
        }

        player.useSecondaryBank = !player.useSecondaryBank
    }
}

/**
 * Checks if a player is currentl using their secondary bank account.
 *
 * @author vddCore
 * @param player The player whose bank account toggle state to inspect.
 */
fun isUsingSecondaryBankAccount(player: Player): Boolean {
    return player.useSecondaryBank
}

/**
 * Returns 'primary' or 'secondary' strings based on which
 * bank account is activated for the given user.
 *
 * @author vddCore
 * @param player The player whose bank account name to retrieve.
 * @param invert Whether to invert the return value.
 * @return Bank account name according to the given criteria.
 */
fun getBankAccountName(player: Player, invert: Boolean = false): String {
    return if (isUsingSecondaryBankAccount(player)) {
        if (invert) "primary" else "secondary"
    } else {
        if (invert) "secondary" else "primary"
    }
}

/**
 * Opens a shop for the given NPC in the provided player's context.
 *
 * @author vddCore
 * @param player The player serving as the shop context.
 * @param npc The NPC ID whose shop to open.
 */
fun openNpcShop(player: Player, npc: Int): Boolean {
    val shop = Shops.shopsByNpc[npc]

    if (shop != null) {
        shop.openFor(player)
        return true
    }

    return false
}

/**
 * Skill Dialogue builder.
 * @param player the player to send the dialogue for.
 * Use like:
 * sendSkillDialogue(player) {
 *    withItems(Items.EXAMPLE_0, Items.EXAMPLE_1)
 *    create { id, amount ->
 *       doSomethingWith(id, amount)
 *    }
 *    calculateMaxAmount { id ->
 *       return someAmount
 *    }
 * }
 */
fun sendSkillDialogue(player: Player, init: SkillDialogueBuilder.() -> Unit) {
    val builder = SkillDialogueBuilder()
    builder.player = player
    builder.init()

    if (builder.items.size !in 1..5) {
        throw IllegalStateException("Invalid number of items passed to skill dialogue (min 1, max 5): ${builder.items.size}")
    }

    val type = when (builder.items.size) {
        1 -> SkillDialogueHandler.SkillDialogue.ONE_OPTION
        2 -> SkillDialogueHandler.SkillDialogue.TWO_OPTION
        3 -> SkillDialogueHandler.SkillDialogue.THREE_OPTION
        4 -> SkillDialogueHandler.SkillDialogue.FOUR_OPTION
        5 -> SkillDialogueHandler.SkillDialogue.FIVE_OPTION
        else -> null
    }

    object : SkillDialogueHandler(player, type, *builder.items) {
        override fun create(amount: Int, index: Int) {
            builder.creationCallback(builder.items[index].id, amount)
        }

        override fun getAll(index: Int): Int {
            return builder.totalAmountCallback(builder.items[index].id)
        }
    }.open()
}

class SkillDialogueBuilder {
    internal lateinit var player: Player
    internal var items: Array<Item> = arrayOf<Item>()
    internal var creationCallback: (itemId: Int, amount: Int) -> Unit = {_,_ -> }
    internal var totalAmountCallback: (itemId: Int) -> Int = {id -> amountInInventory(player, id)}

    fun withItems(vararg item: Item) {
        items = arrayOf(*item)
    }

    fun withItems(vararg item: Int) {
        items = item.map { Item(it) }.toTypedArray()
    }

    fun create(method: (itemId: Int, amount: Int) -> Unit) {
        creationCallback = method
    }

    fun calculateMaxAmount(method: (itemId: Int) -> Int) {
        totalAmountCallback = method
    }
}

/**
 * Registers a hint icon with the given height at the given location
 * @param player the player to register the hint icon for
 * @param height the height of the hint icon
 * @param location the location of the hint icon
 */
fun registerHintIcon(player: Player, location: Location, height: Int) {
    setAttribute(player, "hinticon", HintIconManager.registerHintIcon(player, location, 1, -1, player.hintIconManager.freeSlot(), height, 3))
}

/**
 * Registers a hint icon for the given Node.
 * @param player the player to show a hint icon to.
 * @param node the Node to register a hint icon for.
 */
fun registerHintIcon(player: Player, node: Node) {
    if (getAttribute(player, "hinticon", null) != null)
        return
    setAttribute(player, "hinticon", HintIconManager.registerHintIcon(player, node))
}

/**
 * Clears the active ContentAPI-originated hint icon
 * @param player the player to clear the active hint icon for
 */
fun clearHintIcon(player: Player) {
    val slot = getAttribute(player, "hinticon", -1)
    player.removeAttribute("hinticon")
    HintIconManager.removeHintIcon(player, slot)
}

/**
 * Gets the equipment slot the item belongs to
 * @param item the Id of the item to check
 * @return the EquipmentSlot, or null if the item cannot be equipped, or has no slot defined.
 */
fun equipSlot(item: Int) : EquipmentSlot? {
    return EquipmentSlot
        .values()
        .getOrNull(itemDefinition(item).getConfiguration(ItemConfigParser.EQUIP_SLOT, -1))
}

/**
 * Adjusts the user's credits by the given amount
 * @param player the player whose credits to adjust
 * @param amt the amount to adjust by - add with positive, remove with negative.
 * @return true if successful. Success is defined as always true when adding, but can be false if we are trying to remove, and it would put the amount below 0.
 */
fun updateCredits(player: Player, amount: Int) : Boolean {
    val creds = getCredits(player) + amount

    if (creds < 0)
        return false
    else
        player.details.accountInfo.credits = creds

    return true
}

/**
 * Gets the number of credits a user has.
 * @param player the player to check
 * @return the number of credits they have
 */
fun getCredits(player: Player) : Int {
    return player.details.accountInfo.credits
}

/**
 * Asserts that a quest is required, and sends the player "You must have completed the $questName quest $message"
 * @param player the player we are checking
 * @param questName the name of the quest we are checking for
 * @param message the text appended to "You must have completed the $questName quest ..." if the quest is not complete.
 * @return whether or not the quest has been completed
 */
fun requireQuest(player: Player, questName: String, message: String) : Boolean {
    if (!isQuestComplete(player, questName)) {
        sendMessage(player, "You must have completed the $questName quest $message")
        return false
    }
    return true
}


/**
 * Determines whether or not specified node is a player
 * @param entity the node whom we are checking
 * @return whether or not the entity is a player
 */
fun isPlayer(node: Node) : Boolean {
    return (node is Player)
}

/**
 * Adds a dialogue action to the player's dialogue interpreter
 * @param player the player to add the dialogue action to
 * @param action the dialogue action to add
 */
fun addDialogueAction(player: Player, action: core.game.dialogue.DialogueAction) {
    player.dialogueInterpreter.addAction(action)
}

/**
 * Logs a message to the server console
 * @param origin simply put (Kotlin) this::class.java or (Java) this.getClass()
 * @param type the type of log: Log.FINE (default, visible on VERBOSE), Log.INFO (visible on DETAILED), Log.WARN (visible on CAUTIOUS), Log.ERR (always visible)
 * @param message the actual message to log.
 */
fun log(origin: Class<*>, type: Log, message: String) {
    SystemLogger.processLogEntry(origin, type, message)
}

/**
 * Logs a message to the server console along with a stack trace leading up to it.
 * @param origin simply put (Kotlin) this::class.java or (Java) this.getClass()
 * @param type the type of log: Log.FINE (default, visible on VERBOSE), Log.INFO (visible on DETAILED), Log.WARN (visible on CAUTIOUS), Log.ERR (always visible)
 * @param message the actual message to log.
*/
fun logWithStack(origin: Class<*>, type: Log, message: String) {
    try {
        throw Exception(message)
    } catch (e: Exception) {
        log(origin, type, "${exceptionToString(e)}")
    }
}

/**
 * Takes an exception as an argument, and sends back the complete exception with stack trace as a standard string. Useful for various things.
 * @param e The exception you wish to convert.
 * @return a string with the full stack trace of the exception
**/
fun exceptionToString (e: Exception) : String {
    val sw = StringWriter()
    val pw = PrintWriter(sw)
    e.printStackTrace(pw)
    return sw.toString()
}

/**
 * Used by content handlers to check if the entity is done moving yet
 */
fun finishedMoving(entity: Entity) : Boolean {
    return entity.clocks[Clocks.MOVEMENT] < GameWorld.ticks
}


/**
 * Delay the execution of the currently running script
 */
fun delayScript(entity: Entity, ticks: Int): Boolean {
    entity.scripts.getActiveScript()?.let { it.nextExecution = GameWorld.ticks + ticks }
    return false
}

/**
 * Set the global delay for the entity, pausing execution of all queues/scripts until passed.
 */
fun delayEntity(entity: Entity, ticks: Int) {
    entity.scripts.delay = GameWorld.ticks + ticks
    lock(entity, ticks) //TODO: REMOVE WHEN EVERYTHING IMPORTANT USES PROPER QUEUES - THIS IS INCORRECT BEHAVIOR
}

fun apRange(entity: Entity, apRange: Int) {
    entity.scripts.apRange = apRange
    entity.scripts.apRangeCalled = true
}

fun hasLineOfSight(entity: Entity, target: Node) : Boolean {
    return CombatSwingHandler.isProjectileClipped (entity, target, false)
}

fun animationFinished(entity: Entity) : Boolean {
    return entity.clocks[Clocks.ANIMATION_END] < GameWorld.ticks
}

fun clearScripts(entity: Entity) : Boolean {
    entity.scripts.reset()
    return true
}

fun restartScript(entity: Entity) : Boolean {
    if (entity.scripts.getActiveScript()?.persist != true) {
        log(entity.scripts.getActiveScript()!!::class.java, Log.ERR, "Tried to call restartScript on a non-persistent script! Either use stopExecuting() or make the script persistent.")
        return clearScripts(entity)
    }
    return true
}

fun keepRunning(entity: Entity) : Boolean {
    entity.scripts.getActiveScript()?.nextExecution = getWorldTicks() + 1
    return false
}

fun stopExecuting(entity: Entity) : Boolean {
    if (entity.scripts.getActiveScript()?.persist == true) {
        log(entity.scripts.getActiveScript()!!::class.java, Log.ERR, "Tried to call stopExecuting() on a persistent script! To halt execution of a persistent script, you MUST call clearScripts()!")
        return clearScripts(entity)
    }
    return true
}

fun queueScript(entity: Entity, delay: Int = 1, strength: QueueStrength = QueueStrength.WEAK, persist: Boolean = false, script: (stage: Int) -> Boolean) {
    val s = QueuedScript(script, strength, persist)
    s.nextExecution = getWorldTicks() + delay
    entity.scripts.addToQueue(s, strength)
}

/**
 * Sets the clock to the value of WORLD_TICSK + ticks.
 * @param entity the entity whose clock we are updating
 * @param clock the clock we are updating. Please use [core.game.interaction.Clocks] for this argument. 
 * @param ticks the number of ticks to delay by
 * @return always returns false so this can be used as a script return value.
**/
fun delayClock(entity: Entity, clock: Int, ticks: Int) : Boolean {
    entity.clocks[clock] = getWorldTicks() + ticks
    return false
}

/**
 * Checks if a clock is ready (have we elapsed any delay put into it)
 * @param entity the entity whose clock we are checking
 * @param clock the clock we are checking. Please use [core.game.interaction.Clocks] for this argument.
 * @return true if we have elapsed the clock's wait
**/
fun clockReady(entity: Entity, clock: Int) : Boolean {
    return entity.clocks[clock] <= getWorldTicks()
}

fun delayAttack(entity: Entity, ticks: Int) {
    entity.properties.combatPulse.delayNextAttack(3)
    entity.clocks[Clocks.NEXT_ATTACK] = getWorldTicks() + ticks
}

fun stun(entity: Entity, ticks: Int) {
    stun(entity, ticks, true)
}

fun stun(entity: Entity, ticks: Int, sendMessage: Boolean) {
    entity.walkingQueue.reset()
    entity.pulseManager.clear()
    entity.locks.lockMovement(ticks)
    entity.clocks[Clocks.STUN] = getWorldTicks() + ticks
    entity.graphics(Graphics(80, 96))
    if (entity is Player) {
        playAudio(entity.asPlayer(), Sounds.STUNNED_2727)
        entity.animate(Animation(424, Animator.Priority.VERY_HIGH))
        if (sendMessage) {
            sendMessage(entity, "You have been stunned!")
        }
    }
}

fun isStunned(entity: Entity) : Boolean {
    return entity.clocks[Clocks.STUN] >= getWorldTicks()
}

/**
 * Applies poison to the target. (In other words, creates and starts a poison timer.)
 * @param entity the entity who will be receiving the poison damage.
 * @param source the entity to whom credit for the damage should be awarded (the attacker.) You should award credit to the victim if the poison is sourceless (e.g. from a trap or plant or something)
 * @param severity the severity of the poison damage. Severity is not a 1:1 representation of damage, rather the formula `floor((severity + 4) / 5)` is used. Severity is decreased by 1 with each application of the poison, and ends when it reaches 0.
 * @see To those whe ask "why severity instead of plain damage?" to which the answer is: severity is how it works authentically, and allows for scenarios where, e.g. a poison should hit 6 once, and then drop to 5 immediately.
**/
fun applyPoison (entity: Entity, source: Entity, severity: Int) {
    if(hasTimerActive<PoisonImmunity>(entity)) {
        return
    }
    val existingTimer = getTimer<Poison>(entity)

    if (existingTimer != null) {
        existingTimer.severity = severity
        existingTimer.damageSource = source
    } else {
        registerTimer(entity, spawnTimer<Poison>(source, severity))
    }
}

fun isPoisoned (entity: Entity) : Boolean {
    return getTimer<Poison>(entity) != null
}

fun curePoison (entity: Entity) {
    if (!hasTimerActive<Poison>(entity))
        return
    removeTimer<Poison>(entity)
    if (entity is Player)
        sendMessage(entity, "Your poison has been cured.")
}

fun setCurrentScriptState(entity: Entity, state: Int) {
    val script = entity.scripts.getActiveScript()
    if (script == null) {
        log(ContentAPI::class.java, Log.WARN, "Tried to set a script state when no script was being ran!")
        if (GameWorld.settings?.isDevMode != true) return
        throw IllegalStateException("Script execution mistake - check stack trace and above warning log!")
    }
    script.state = state - 1 //set it to 1 below the state so that on next execution the state is at the expected value.
}

/**
 * Modifies prayer points by value
 * @param player the player to modify prayer points
 * @param amount the amount of points to modify by (positive for increment, negative for decrement)
 */
fun modPrayerPoints(player: Player, amount: Double) {
    if(amount > 0) player.skills.incrementPrayerPoints(amount)
    else if(amount < 0) player.skills.decrementPrayerPoints(amount.absoluteValue)
    else return
}

/**
 * Iterates a container and "decants" every potion type in the container. (Decanting is the process of condensing multiple variable-dose potions into the minimum number of max-dose potions)
 * @param container the container to iterate
 * @return a pair where the first element is the list of items to be removed, and the second element is the list of items to add.
*/
fun decantContainer (container: core.game.container.Container) : Pair<List<Item>, List<Item>> {
    val doseCount = HashMap<Consumable,Int>()
    val toRemove = ArrayList<Item>()
    val toAdd = ArrayList<Item>()
    val doseRegex = Pattern.compile("(\\([0-9]\\))") //Matches "(2)" in "Defense potion (2)"

    for (item in container.toArray()) {
        if (item == null) continue
        val potionType = Consumables.getConsumableById(item.id)?.consumable as? Potion ?: continue
        val matcher = doseRegex.matcher(item.name)
        if (!matcher.find()) continue
        val dosage = matcher.group(1).replace("(","").replace(")","").toIntOrNull() ?: continue
        doseCount[potionType] = (doseCount[potionType] ?: 0) + dosage
        toRemove.add(item)
    }

    for ((consumable, count) in doseCount) {
        val maxDose = consumable.ids.size
        val totalMaxDosePotions = count / maxDose
        val remainderDose = count % maxDose
        if (totalMaxDosePotions > 0)
            toAdd.add(Item(consumable.ids[0], totalMaxDosePotions))
        if (remainderDose > 0)
            toAdd.add(Item(consumable.ids[consumable.ids.size - remainderDose]))
    }

    val emptyVials = toRemove.size - toAdd.sumBy { it.amount }
    if (emptyVials > 0)
        toAdd.add(Item(229, emptyVials))
    return Pair<List<Item>,List<Item>>(toRemove, toAdd)
}

/** 
 * Checks whether the user has a valid anti-dragonfire shield equipped.
 * @param player the player whose shield we are checking.
 * @param wyvern an optional parameter (default false) whether or not we are checking against wyvern fire, which ignores normal anti-dragon-shields, but accepts elemental/mind shields, etc.
 * @return whether or not the player is protected by their shield. 
 * @see <a href=https://runescape.wiki/w/Dragonfire?oldid=2068032>Source</a> 
**/
fun hasDragonfireShieldProtection(player: Player, wyvern: Boolean = false): Boolean {
    val shield = getItemFromEquipment(player, EquipmentSlot.SHIELD) ?: return false
    return when (shield.id) {
        Items.ELEMENTAL_SHIELD_2890, Items.MIND_SHIELD_9731 -> wyvern
        Items.ANTI_DRAGON_SHIELD_1540 -> !wyvern
        Items.DRAGONFIRE_SHIELD_11283, Items.DRAGONFIRE_SHIELD_11284 -> true
        else -> false
    }
}

/**
 * Calculates the expected max hit of dragonfire after checking/applying valid protections.
 * @param entity the entity whose protection we are checking.
 * @param maxDamage the max damage the dragonfire can do without protection.
 * @param wyvern an optional parameter (default false) that defines whether or not this is wyvern fire. Wyvern fire ignores anti-fire potions and regular anti-dragon shield.
 * @param unprotectableDamage an optional parameter (default 0) that defines how much damage cannot be protected against. Think of it as a minimum-max-hit.
 * @param sendMessage an optional parameter (default false) whether or not to send a message notifying the player of the damage being blocked.
 * @return the maximum amount of damage that can be done to the player.
 * @see <a href=https://runescape.wiki/w/Dragonfire?oldid=2068032>Source</a> 
**/
fun calculateDragonfireMaxHit(entity: Entity, maxDamage: Int, wyvern: Boolean = false, unprotectableDamage: Int = 0, sendMessage: Boolean = false): Int {
    val hasShield: Boolean
    val hasPotion: Boolean
    val hasPrayer: Boolean

    if (entity is Player) {
        hasShield = hasDragonfireShieldProtection(entity, wyvern)
        hasPotion = !wyvern && getAttribute(entity, "fire:immune", 0) >= getWorldTicks()
        hasPrayer = entity.prayer.get(PrayerType.PROTECT_FROM_MAGIC)

        if (sendMessage) {
            var message = "You are horribly burnt by the ${if (wyvern) "icy" else "fiery"} breath."
            if (hasShield && hasPotion)
                message = "Your potion and shield fully absorb the ${if (wyvern) "icy" else "fiery"} breath."
            else if (hasShield)
                message = "Your shield absorbs most of the ${if (wyvern) "icy" else "fiery"} breath."
            else if (hasPotion)
                message = "Your potion absorbs some of the fiery breath."
            else if (hasPrayer)
                message = "Your prayer absorbs some of the ${if (wyvern) "icy" else "fiery"} breath."
            sendMessage(entity, message)
        }
    } else {
        val dragonfireTokens = entity.getDragonfireProtection(!wyvern)
        hasShield = dragonfireTokens and 0x2 != 0
        hasPotion = dragonfireTokens and 0x4 != 0
        hasPrayer = dragonfireTokens and 0x8 != 0
    }

    var effectiveDamage = maxDamage.toDouble()
    if (hasPrayer && !hasShield && !hasPotion)
        effectiveDamage -= 0.6 * maxDamage
    else {
        if (hasShield)
            effectiveDamage -= 0.9 * maxDamage
        if (hasPotion)
            effectiveDamage -= 0.1 * maxDamage
    }

    return Math.max(unprotectableDamage, effectiveDamage.toInt())
}

/**
 * Opens a single interface tab from given ID.
 * @param player the player to open interface for
 * @param component the component ID to open
 */
fun openSingleTab(player: Player, component: Int) {
    player.interfaceManager.openSingleTab(Component(component))
}

private class ContentAPI
