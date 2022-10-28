package api

import Cutscene
import com.moandjiezana.toml.Toml
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.SceneryDefinition
import core.cache.def.impl.VarbitDefinition
import core.game.component.Component
import core.game.container.impl.EquipmentContainer
import core.game.content.dialogue.DialogueAction
import core.game.content.dialogue.FacialExpression
import core.game.content.global.action.SpecialLadders
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.impl.Animator
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.HintIconManager
import core.game.node.entity.player.link.IronmanMode
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.audio.Audio
import core.game.node.entity.player.link.emote.Emotes
import core.game.node.entity.player.link.quest.QuestRepository
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.gather.SkillingTool
import core.game.node.entity.skill.slayer.Tasks
import core.game.node.entity.skill.summoning.familiar.BurdenBeast
import core.game.node.entity.state.EntityState
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.RegionManager.getRegionChunk
import core.game.world.map.path.Pathfinder
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.update.flag.chunk.AnimateObjectUpdateFlag
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.DialogueFile
import rs09.game.content.dialogue.SkillDialogueHandler
import rs09.game.content.global.GlobalKillCounter
import rs09.game.content.global.shops.Shops
import rs09.game.ge.GrandExchangeRecords
import rs09.game.interaction.InteractionListeners
import rs09.game.interaction.inter.ge.StockMarket
import rs09.game.node.entity.skill.slayer.SlayerManager
import rs09.game.system.SystemLogger
import rs09.game.system.config.ItemConfigParser
import rs09.game.system.config.ServerConfigParser
import rs09.game.world.GameWorld
import rs09.game.world.GameWorld.Pulser
import rs09.game.world.repository.Repository

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
 * Check if an item exists in a player's inventory
 * @param player the player whose inventory to check
 * @param item the ID of the item to check for
 * @param amount the amount to check for
 * @return true if the player has >= the given item in the given amount, false otherwise.
 */
fun inInventory(player: Player, item: Int, amount: Int = 1): Boolean {
    return player.inventory.contains(item, amount)
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
 * @return the amount of the ID in the player's bank.
 */
fun amountInBank(player: Player, id: Int, includeSecondary: Boolean = true): Int {
    return player.bank.getAmount(id) + if (includeSecondary) player.bankSecondary.getAmount(id) else 0
}

/**
 * Check the amount of a given item in the player's equipment slots
 * @param player the player to check
 * @param id the ID of the item to check for
 * @return the amount of the ID in the player's equipment.
 */
fun amountInEquipment(player: Player, id: Int): Int {
    return player.equipment.getAmount(id)
}

/**
 * Check that an item is equipped by the given player
 */
fun isEquipped(player: Player, id: Int): Boolean {
    return amountInEquipment(player, id) > 0
}

/**
 * Check that a set of items is equipped by the given player
 */
fun areEquipped(player: Player, vararg ids: Int): Boolean {
    return ids.all { id ->
        amountInEquipment(player, id) > 0
    }
}

/**
 * Check that at least one item from a set of items is equipped by the given player
 * @param player the player
 * @param ids the set of item ids to check
 * @return true if the player has at least one of the items equipped, false if none are equipped
 */
fun areAnyEquipped(player: Player, vararg ids: Int): Boolean {
    return ids.any { id ->
        amountInEquipment(player, id) > 0
    }
}

data class ContainerisedItem(val container: core.game.container.Container?, val itemId: Int)

/**
 * Check if player has any of the specified item IDs equipped, in inventory, or in banks
 * Returns a ContainerisedItem containing the container and the item ID if found, otherwise ContainerisedItem(null, -1) if not found
 */
fun hasAnItem(player: Player, vararg ids: Int): ContainerisedItem {
    for (searchSpace in arrayOf(player.inventory, player.equipment, player.bankPrimary, player.bankSecondary)) {
        for (id in ids) {
            if (searchSpace.containItems(id)) {
                return ContainerisedItem(searchSpace, id)
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
    god.validItems.forEach { if (amountInEquipment(player, it) > 0) return true }
    return false
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
 * @param container the Container to modify
 * @return the item that was previously in the slot, or null if none.
 */
fun replaceSlot(player: Player, slot: Int, item: Item, container: Container = Container.INVENTORY): Item? {
    val cont = when (container) {
        Container.INVENTORY -> player.inventory
        Container.EQUIPMENT -> player.equipment
        Container.BANK -> player.bank
    }

    return cont.replace(item, slot)
}

/**
 * Add an item with a variable quantity or drop it if a player does not have enough space
 * @param player the player whose inventory to add to
 * @param id the ID of the item to add to the player's inventory
 * @param amount the amount of the ID to add to the player's inventory, defaults to 1
 */
fun addItemOrDrop(player: Player, id: Int, amount: Int = 1) {
    val item = Item(id, amount)
    if (!player.inventory.add(item)) GroundItemManager.create(item, player)
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
 * Check if an item exists in a player's bank
 * @param player the player whose bank to check
 * @param item the ID of the item to check for
 * @param amount the amount to check for, defaults to 1
 * @return true if the item exists in the given amount in the player's bank
 */
fun inBank(player: Player, item: Int, amount: Int = 1): Boolean {
    return player.bank.contains(item, amount) || player.bankSecondary.contains(item, amount)
}

/**
 * Check if an item exists in a player's equipment
 * @param player the player whose equipment to check
 * @param item the ID of the item to check for
 * @param amount the amount to check for, defaults to 1
 * @return true if the item exists in the given amount in the player's equipment
 */
fun inEquipment(player: Player, item: Int, amount: Int = 1): Boolean {
    return player.equipment.contains(item, amount)
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
 * @param toReplace the GameObject instance we are replacing
 * @param with the ID of the GameObject we wish to replace toReplace with
 * @param for_ticks the number of ticks the object should be replaced for. Use -1 for permanent.
 * @param loc the location to move the new object to if necessary. Defaults to null.
 */
fun replaceScenery(toReplace: Scenery, with: Int, for_ticks: Int, loc: Location? = null) {
    val newLoc = when (loc) {
        null -> toReplace.location
        else -> loc
    }
    if (for_ticks == -1) {
        SceneryBuilder.replace(toReplace, toReplace.transform(with, toReplace.rotation, newLoc))
    } else {
        SceneryBuilder.replace(toReplace, toReplace.transform(with, toReplace.rotation, newLoc), for_ticks)
    }
    toReplace.isActive = false
}

/**
 * Replace an object with the given revert timer with the given rotation
 * @param toReplace the GameObject instance we are replacing
 * @param with the ID of the GameObject we wish to replace toReplace with
 * @param for_ticks the number of ticks the object should be replaced for. Use -1 for permanent.
 * @Param rotation the Direction of the rotation it should use. Direction.NORTH, Direction.SOUTH, etc
 * @param loc the location to move the new object to if necessary. Defaults to null.
 */
fun replaceScenery(toReplace: Scenery, with: Int, for_ticks: Int, rotation: Direction, loc: Location? = null) {
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
    if (for_ticks == -1) {
        SceneryBuilder.replace(toReplace, toReplace.transform(with, rot, newLoc))
    } else {
        SceneryBuilder.replace(toReplace, toReplace.transform(with, rot, newLoc), for_ticks)
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
 * Gets an Audio object with specified id, volume, etc
 */
fun getAudio(id: Int, volume: Int = 10, delay: Int = 1): Audio {
    return Audio(id, volume, delay)
}

/**
 * Impact an enemy with the given amount of damage and the given hitsplat type
 * @param entity the entity to damage
 * @param amount the amount of damage to deal
 * @param type the type of hit splat to use, ImpactHandler.HitsplatType is an enum containing these options.
 */
fun impact(entity: Entity, amount: Int, type: ImpactHandler.HitsplatType = ImpactHandler.HitsplatType.NORMAL) {
    entity.impactHandler.manualHit(entity, amount, type)
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
 * @param player the player to send the message to.
 */
fun sendMessage(player: Player, message: String) {
    player.sendMessage(message)
}

/**
 * Forces an above-head chat message for the given entity
 * @param entity the entity to send the chat for
 * @param message the message to display
 */
fun sendChat(entity: Entity, message: String) {
    entity.sendChat(message)
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
 * Plays the given Audio for the given Entity
 * @param player the player to play the audio for
 * @param audio the Audio to play
 * @param global if other nearby entities should be able to hear it
 */
fun playAudio(player: Player, audio: Audio, global: Boolean = false) {
    player.audioManager.send(audio, global)
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
        else -> SystemLogger.logErr(ContentAPI::class.java, "Invalid object type passed to openDialogue() -> ${dialogue.javaClass.simpleName}")
    }
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

/**
 * Sets the given varbit for the given player
 * @param player the player to set the varbit for
 * @param varpIndex the index of the VARP that contains the desired varbit.
 * @param offset the offset of the desired varbit inside the varp.
 * @param value the value to set the varbit to
 */
fun setVarbit(player: Player, varpIndex: Int, offset: Int, value: Int, save: Boolean = false) {
    player.varpManager.get(varpIndex).setVarbit(offset, value).send(player)
    if (save) player.varpManager.flagSave(varpIndex)
}

/**
 * Sets the given varbit for the given player.
 * @param player the player to set the varbit for.
 * @param varbitId the ID of the varbit to set.
 * @param value the value to set the varbit to.
 * @param save whether or not we should save this setting (default false)
 */
fun setVarbit(player: Player, varbitId: Int, value: Int, save: Boolean = false) {
    player.varpManager.setVarbit(varbitId, value)
    if (save) {
        val def = VarbitDefinition.forId(varbitId)
        player.varpManager.flagSave(def.varpId)
    }
}

/**
 * Clears all bits for a given varp index
 * @param player the player to clear for
 * @param varpIndex the index of the varp to clear
 */
fun clearVarp(player: Player, varpIndex: Int) {
    player.varpManager.get(varpIndex).clear()
}

/**
 * Gets the value of all bits collected together from a given varp
 * @param player the player to get the varp for
 * @param varpIndex the index of the varp to calculate the value of
 * @return the value of the varp
 */
fun getVarpValue(player: Player, varpIndex: Int): Int {
    return player.varpManager.get(varpIndex).getValue()
}

/**
 * Gets the value of a specific varbit
 * @param player the player to get the value for
 * @param varpIndex the index of th varp containinig the desired varbit
 * @param offset the offset of the varbit inside the varp
 * @return the value of the given varbit
 */
fun getVarbitValue(player: Player, varpIndex: Int, offset: Int): Int {
    return player.varpManager.get(varpIndex).getVarbitValue(offset) ?: 0
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
 * Interrupts a given entity's walking queue
 * @param entity the entity to interrupt
 */
fun stopWalk(entity: Entity) {
    entity.walkingQueue.reset()
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
        else -> SystemLogger.logErr(ContentAPI::class.java, "Attempt to adjust the charge of invalid type: ${node.javaClass.simpleName}")
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
        else -> SystemLogger.logErr(ContentAPI::class.java, "Attempt to get charge of invalid type: ${node.javaClass.simpleName}")
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
        else -> SystemLogger.logErr(ContentAPI::class.java, "Attempt to set the charge of invalid type: ${node.javaClass.simpleName}")
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
    GameWorld.Pulser.submit(pulse)
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
 * Sends a dialogue that uses the player's chathead.
 * @param player the player to send the dialogue to
 * @param msg the message to send.
 * @param expr the FacialExpression to use. An enum exists for these called FacialExpression. Defaults to FacialExpression.FRIENDLY
 */
fun sendPlayerDialogue(player: Player, msg: String, expr: FacialExpression = FacialExpression.FRIENDLY) {
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
fun sendNPCDialogue(player: Player, npc: Int, msg: String, expr: FacialExpression = FacialExpression.FRIENDLY) {
    player.dialogueInterpreter.sendDialogues(npc, expr, *splitLines(msg))
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
fun getQP(player: Player): Int {
    return player.questRepository.points
}

/**
 * Gets the stage for the given quest for the given player
 */
fun questStage(player: Player, quest: String): Int {
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
 * Check if a quest is complete
 */
fun isQuestComplete(player: Player, quest: String): Boolean {
    return player.questRepository.getStage(quest) == 100
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
    SpecialLadders.add(ladderLoc, dest)
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


fun announceIfRare(player: Player, item: Item) {
    if (item.definition.getConfiguration(ItemConfigParser.RARE_ITEM, false)) {
        sendNews("${player.username} has just received: ${item.amount} x ${item.name}.");
        GlobalKillCounter.incrementRareDrop(player, item);
    }
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
                bank.add(unnote(item))
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
    return isEquipped(player, Items.SEAL_OF_PASSAGE_9083)
            || inInventory(player, Items.SEAL_OF_PASSAGE_9083)
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
    val swCorner = center.transform(-radius, -radius, center.z)
    val neCorner = center.transform(radius, radius, center.z)
    val borders = ZoneBorders(swCorner.x, swCorner.y, neCorner.x, neCorner.y, center.z)

    var attempts = maxAttempts
    var success: Boolean
    while (attempts-- > 0) {
        val dest = borders.randomLoc
        val path = Pathfinder.find(target, dest)
        success = path.isSuccessful && !path.isMoveNear
        if (success) return dest
    }

    return target.location
}

/**
 * Returns the player's active slayer task.
 * @author bushtail
 * @param player the player whose task we are checking.
 * @return the slayer task.
 */
fun getSlayerTask(player: Player): Tasks? {
    return SlayerManager.getInstance(player).task
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
        SlayerManager.getInstance(player).task?.tip!!
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
 * Determines whether or not a specified entity has a state
 * @param entity the entity whose state we are checking
 * @param state the state to check for
 * @return whether or not the entity has the provided state
 */
fun hasState(entity: Entity, state: EntityState) : Boolean {
    return entity.stateManager.hasState(state)
}

/**
 * Adds a state to the entity
 * @param entity the entity whose state we are adding
 * @param state the state to add
 * @param override whether or not it's to override another state
 */
fun addState(entity: Entity, state: EntityState, override: Boolean, vararg args: Any?) {
    if(!entity.stateManager.hasState(state)) {
        entity.stateManager.register(state, override, *args)
    }
}

/**
 * Removes a state from the entity
 * @param entity the entity whose state we are removing
 * @param state the state to remove
 */
fun removeState(entity: Entity, state: EntityState) {
    entity.stateManager.remove(state)
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
fun addDialogueAction(player: Player, action: DialogueAction) {
    player.dialogueInterpreter.addAction(action)
}

private class ContentAPI