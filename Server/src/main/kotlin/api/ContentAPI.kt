package api

import Cutscene
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.component.Component
import core.game.container.impl.EquipmentContainer
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
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.audio.Audio
import core.game.node.entity.player.link.emote.Emotes
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.player.link.quest.QuestRepository
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.gather.SkillingTool
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
import rs09.game.content.dialogue.DialogueFile
import rs09.game.content.dialogue.SkillDialogueHandler
import rs09.game.content.global.GlobalKillCounter
import rs09.game.interaction.InteractionListeners
import rs09.game.system.SystemLogger
import rs09.game.system.config.ItemConfigParser
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
    return if(pickaxe) SkillingTool.getPickaxe(player) else SkillingTool.getHatchet(player)
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

fun amountInInventory(player: Player, id: Int): Int{
    return player.inventory.getAmount(id)
}

/**
 * Check the amount of a given item in the player's bank
 * @param player the player to check
 * @param id the ID of the item to check for
 * @return the amount of the ID in the player's bank.
 */

fun amountInBank(player: Player, id: Int): Int{
    return player.bank.getAmount(id)
}

/**
 * Check the amount of a given item in the player's equipment slots
 * @param player the player to check
 * @param id the ID of the item to check for
 * @return the amount of the ID in the player's equipment.
 */

fun amountInEquipment(player: Player, id: Int): Int{
    return player.equipment.getAmount(id)
}

/**
 * Check that an item is equipped by the given player
 */
fun isEquipped(player: Player, id: Int): Boolean {
    return amountInEquipment(player, id) > 0
}

/**
 * Check if a player has an item equipped which corresponds to the given God
 * @param player the player to check
 * @param god the God whose equipment we are checking for
 * @return true if the player has an item corresponding to the given god, false otherwise
 */
fun hasGodItem(player: Player, god: God): Boolean
{
    god.validItems.forEach { if(amountInEquipment(player, it) > 0) return true }
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

    return when(container){
        Container.INVENTORY -> player.inventory.remove(it)
        Container.BANK -> player.bank.remove(it)
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
    val cont = when(container){
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
    val cont = when(container) {
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

fun addItemOrDrop(player: Player, id: Int, amount: Int = 1){
    val item = Item(id, amount)
    if(!player.inventory.add(item)) GroundItemManager.create(item,player)
}

/**
 * Clears an NPC with the "poof" smoke graphics commonly seen with random event NPCs.
 * @param npc the NPC object to initialize
 */

fun poofClear(npc: NPC){
    submitWorldPulse(object : Pulse(){
        var counter = 0
        override fun pulse(): Boolean {
            when(counter++){
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
    return player.bank.contains(item, amount)
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
    val newLoc = when(loc){
        null -> toReplace.location
        else -> loc
    }
    if (for_ticks == -1) {
        SceneryBuilder.replace(toReplace, toReplace.transform(with,toReplace.rotation,newLoc))
    } else {
        SceneryBuilder.replace(toReplace, toReplace.transform(with,toReplace.rotation, newLoc), for_ticks)
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

fun replaceScenery(toReplace: Scenery, with: Int, for_ticks: Int, rotation: Direction, loc: Location? = null){
    val newLoc = when(loc){
        null -> toReplace.location
        else -> loc
    }
    val rot = when(rotation){
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
        SceneryBuilder.replace(toReplace, toReplace.transform(with,rot, newLoc))
    } else {
        SceneryBuilder.replace(toReplace, toReplace.transform(with,rot,newLoc), for_ticks)
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

fun impact(entity: Entity, amount: Int, type: ImpactHandler.HitsplatType) {
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
 * Send an object animation
 */

fun animateScenery(player: Player, obj: Scenery, animationId: Int, global: Boolean = false) {
    player.packetDispatch.sendSceneryAnimation(obj, getAnimation(animationId), global)
}

/**
 * Send an object animation independent of a player
 */

fun animateScenery(obj: Scenery, animationId: Int){
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

fun spawnProjectile(source: Location, dest: Location, projectile: Int, startHeight: Int, endHeight: Int, delay: Int, speed: Int, angle: Int){
    Projectile.create(source, dest, projectile, startHeight, endHeight, delay, speed, angle, source.getDistance(dest).toInt()).send()
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

fun openOverlay(player: Player, id: Int){
    player.interfaceManager.openOverlay(Component(id))
}

/**
 * Closes any open overlays for the given player
 * @param player the player to close for
 */

fun closeOverlay(player: Player){
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
 * Plays an animation on the entity
 * @param entity the entity to animate
 * @param anim the animation to play, can be an ID or an Animation object.
 * @param forced whether or not to force the animation (usually not necessary)
 */

fun <T> animate(entity: Entity, anim: T, forced: Boolean = false) {
    val animation = when(anim){
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
        else -> SystemLogger.logErr("Invalid object type passed to openDialogue() -> ${dialogue.javaClass.simpleName}")
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

fun getScenery(x: Int, y: Int, z: Int): Scenery?{
    return RegionManager.getObject(z,x,y)
}

/**
 * Gets the spawned scenery from the world map using the given Location object.
 * @param loc the Location object to use.
 */

fun getScenery(loc: Location): Scenery?{
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

fun findLocalNPCs(entity: Entity, ids: IntArray): List<NPC>{
    return RegionManager.getLocalNpcs(entity).filter { it.id in ids }.toList()
}

/**
 * Gets a list of nearby NPCs that match the given IDs.
 * @param entity the entity to check around
 * @param ids the IDs of the NPCs to look for
 * @param distance The maximum distance to the entity.
 */

fun findLocalNPCs(entity: Entity, ids: IntArray, distance: Int): List<NPC>{
    return RegionManager.getLocalNpcs(entity, distance).filter { it.id in ids }.toList()
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

fun transformNpc(npc: NPC, transformTo: Int, restoreTicks: Int){
    npc.transform(transformTo)
    Pulser.submit(object : Pulse(restoreTicks){
        override fun pulse(): Boolean {
            npc.reTransform()
            return true
        }
    })
}

/**
 * Produces a Location object using the given x,y,z values
 */

fun location(x: Int, y: Int, z: Int): Location{
    return Location.create(x,y,z)
}

/**
 * Checks if the given entity is within the given ZoneBorders
 */
fun inBorders(entity: Entity, borders: ZoneBorders): Boolean{
    return borders.insideBorder(entity)
}

/**
 * Checks if the given entity is within the given borders
 */
fun inBorders(entity: Entity, swX: Int, swY: Int, neX: Int, neY: Int): Boolean {
    return ZoneBorders(swX,swY,neX,neY).insideBorder(entity)
}

/**
 * AHeals the given entity for the given number of hitpoints
 */

fun heal(entity: Entity, amount: Int){
    entity.skills.heal(amount)
}

/**
 * Sets the given varbit for the given player
 * @param player the player to set the varbit for
 * @param varpIndex the index of the VARP that contains the desired varbit.
 * @param offset the offset of the desired varbit inside the varp.
 * @param value the value to set the varbit to
 */

fun setVarbit(player: Player, varpIndex: Int, offset: Int, value: Int, save: Boolean = false){
    player.varpManager.get(varpIndex).setVarbit(offset,value).send(player)
    if(save) player.varpManager.flagSave(varpIndex)
}

/**
 * Clears all bits for a given varp index
 * @param player the player to clear for
 * @param varpIndex the index of the varp to clear
 */

fun clearVarp(player: Player, varpIndex: Int){
    player.varpManager.get(varpIndex).clear()
}

/**
 * Gets the value of all bits collected together from a given varp
 * @param player the player to get the varp for
 * @param varpIndex the index of the varp to calculate the value of
 * @return the value of the varp
 */

fun getVarpValue(player: Player, varpIndex: Int): Int{
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
    return player.varpManager.get(varpIndex).getVarbitValue(offset)
}

/**
 * Force an entity to walk to a given destination.
 * @param entity the entity to forcewalk
 * @param dest the Location object to walk to
 * @param type the type of pathfinder to use. "smart" for the SMART pathfinder, "clip" for the noclip pathfinder, anything else for DUMB.
 */

fun forceWalk(entity: Entity, dest: Location, type: String){
    if(type == "clip"){
        ForceMovement(entity, dest, 10, 10).run()
        return
    }
    val pathfinder = when(type){
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

fun stopWalk(entity: Entity){
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

fun adjustCharge(node: Node, amount: Int){
    when(node){
        is Item -> node.charge += amount
        is Scenery -> node.charge += amount
        else -> SystemLogger.logErr("Attempt to adjust the charge of invalid type: ${node.javaClass.simpleName}")
    }
}

/**
 * Get the current charge of the given node
 * @param node the node whose charge to check
 * @return amount of charges the node has, or -1 if the node does not accept charges.
 */

fun getCharge(node: Node): Int{
    when(node){
        is Item -> return node.charge
        is Scenery -> return node.charge
        else -> SystemLogger.logErr("Attempt to get charge of invalid type: ${node.javaClass.simpleName}").also { return -1 }
    }
}

/**
 * Set the charge of the given node to the given amount.
 * @param node the node to set the charge for
 * @param charge the amount to set the node's charge to (default is 1000)
 */

fun setCharge(node: Node, charge: Int){
    when(node){
        is Item -> node.charge = charge
        is Scenery -> node.charge = charge
        else -> SystemLogger.logErr("Attempt to set the charge of invalid type: ${node.javaClass.simpleName}")
    }
}

/**
 * Gets the used option in the context of an interaction.
 * @param player the player to get the used option for.
 * @return the option the player used
 */

fun getUsedOption(player: Player): String {
    return player.getAttribute("interact:option","INVALID")
}

/**
 * Used to play both an Animation and Graphics object simultaneously.
 * @param entity the entity to perform this on
 * @param anim the Animation object to use, can also be an ID.
 * @param gfx the Graphics object to use, can also be an ID.
 */

fun <A,G> visualize(entity: Entity, anim: A, gfx: G){
    val animation = when(anim){
        is Int -> Animation(anim)
        is Animation -> anim
        else -> throw IllegalStateException("Invalid parameter passed for animation.")
    }

    val graphics = when(gfx){
        is Int -> Graphics(gfx)
        is Graphics -> gfx
        else -> throw IllegalStateException("Invalid parameter passed for graphics.")
    }

    entity.visualize(animation,graphics)
}

/**
 * Used to submit a pulse to the GameWorld's Pulser.
 * @param pulse the Pulse object to submit
 */

fun submitWorldPulse(pulse: Pulse){
    GameWorld.Pulser.submit(pulse)
}

/**
 * Teleports or "instantly moves" an entity to a given Location object.
 * @param entity the entity to move
 * @param loc the Location object to move them to
 * @param type the teleport type to use (defaults to instant). An enum exists as TeleportManager.TeleportType.
 */

fun teleport(entity: Entity, loc: Location, type: TeleportManager.TeleportType = TeleportManager.TeleportType.INSTANT){
    if(type == TeleportManager.TeleportType.INSTANT) entity.properties.teleportLocation = loc
    else entity.teleporter.send(loc,type)
}

/**
 * Sets the dynamic or "temporary" (restores) level of a skill.
 * @param entity the entity to set the level for
 * @param skill the Skill to set. A Skills enum exists that can be used. Ex: Skills.STRENGTH
 * @param level the level to set the skill to
 */

fun setTempLevel(entity: Entity, skill: Int, level: Int){
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

fun adjustLevel(entity: Entity, skill: Int, amount: Int){
    entity.skills.setLevel(skill, entity.skills.getStaticLevel(skill) + amount)
}

/**
 * Remove all of a given item from the given container
 * @param player the player to remove the item from
 * @param item the item to remove. Can be an Item object or an ID.
 * @param container the Container to remove the item from. An enum exists for this called Container. Ex: Container.BANK
 */

fun <T> removeAll(player: Player, item: T, container: Container){
    val it = when(item){
        is Item -> item.id
        is Int -> item
        else -> throw IllegalStateException("Invalid value passed as item")
    }

    when(container){
        Container.EQUIPMENT -> player.equipment.remove(Item(it, amountInEquipment(player, it)))
        Container.BANK -> player.bank.remove(Item(it, amountInBank(player, it)))
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

fun setInterfaceText(player: Player, string: String, iface: Int, child: Int){
    player.packetDispatch.sendString(string,iface,child)
}

/**
 * Closes any open (non-chat) interfaces for the player
 * @param player the player to close the interface for
 */

fun closeInterface(player: Player){
    player.interfaceManager.close()
}

/**
 * Closes any opened tab interfaces for the player
 * @param player the player to close the tab for
 */

fun closeTabInterface(player: Player){
    player.interfaceManager.closeSingleTab()
}

/**
 * Sends a dialogue that uses the player's chathead.
 * @param player the player to send the dialogue to
 * @param msg the message to send.
 * @param expr the FacialExpression to use. An enum exists for these called FacialExpression. Defaults to FacialExpression.FRIENDLY
 */

fun sendPlayerDialogue(player: Player, msg: String, expr: FacialExpression = FacialExpression.FRIENDLY){
    player.dialogueInterpreter.sendDialogues(player, expr, *splitLines(msg))
}

/**
 * Sends a player model to a specific interface child
 * @param player the player to send the packet to and whose model to use
 * @param iface the ID of the interface to send it to
 * @param child the index of the child on the interface to send the model to
 */

fun sendPlayerOnInterface(player: Player, iface: Int, child: Int){
    player.packetDispatch.sendPlayerOnInterface(iface,child)
}

/**
 * Sends a dialogue that uses the player's chathead.
 * @param player the player to send the dialogue to
 * @param npc the ID of the NPC to use for the chathead
 * @param msg the message to send.
 * @param expr the FacialExpression to use. An enum exists for these called FacialExpression. Defaults to FacialExpression.FRIENDLY
 */

fun sendNPCDialogue(player: Player, npc: Int, msg: String, expr: FacialExpression = FacialExpression.FRIENDLY){
    player.dialogueInterpreter.sendDialogues(npc, expr, *splitLines(msg))
}

/**
 * Sends an animation to a specific interface child
 * @param player the player to send the packet to
 * @param anim the ID of the animation to send to the interface
 * @param iface the ID of the interface to send the animation to
 * @param child the index of the child on the interface to send the model to
 */

fun sendAnimationOnInterface(player: Player, anim: Int, iface: Int, child: Int){
    player.packetDispatch.sendAnimationInterface(anim,iface,child)
}

/**
 * Register a logout listener to a player. Logout listeners are methods that run when a player logs out.
 * @param player the player to register the listener for
 * @param handler the method to run when the listener is invoked (when the player logs out)
 */

fun registerLogoutListener(player: Player, key: String, handler: (p: Player) -> Unit){
    player.logoutListeners[key] = handler
}

/**
 * Removes a logout listener based on the key from a player
 * @param player the player to remove the logout listner from
 * @param key the key of the logout listener to remove.
 */

fun clearLogoutListener(player: Player, key: String){
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

fun sendItemOnInterface(player: Player, iface: Int, child: Int, item: Int, amount: Int = 1){
    player.packetDispatch.sendItemOnInterface(item,amount,iface,child)
}

/**
 * Sends a dialogue box with a single item and some text
 * @param player the player to send it to
 * @param item the ID of the item to show
 * @param message the text to display
 */

fun sendItemDialogue(player: Player, item: Int, message: String){
    player.dialogueInterpreter.sendItemMessage(item, *splitLines(message))
}

/**
 * Sends a dialogue box with two items and some text
 * @param player the player to send it to
 * @param item1 the ID of the first item to show
 * @param item2 the ID of the second item to show
 * @param message the text to display
 */

fun sendDoubleItemDialogue(player: Player, item1: Int, item2: Int, message: String){
    player.dialogueInterpreter.sendDoubleItemMessage(item1, item2, message)
}

/**
 * Send an input dialogue to retrieve a specified value from the player
 * @param player the player to send the input dialogue to
 * @param numeric whether or not the input is numeric
 * @param prompt what to prompt the player
 * @param handler the method that handles the value gained from the input dialogue
 */

fun sendInputDialogue(player: Player, numeric: Boolean, prompt: String, handler: (value: Any) -> Unit){
    if(numeric) sendInputDialogue(player, InputType.NUMERIC, prompt, handler)
    else sendInputDialogue(player, InputType.STRING_SHORT, prompt, handler)
}

/**
 * Send input dialogues based on type. Some dialogues are special and can't be covered by the other sendInputDialogue method
 * @param player the player to send the input dialogue to
 * @param type the input type to send - an enum is available for this called InputType
 * @param prompt what to prompt the player
 * @param handler the method that handles the value from the input dialogue
 */

fun sendInputDialogue(player: Player, type: InputType, prompt: String, handler: (value: Any) -> Unit){
    when(type){
        InputType.NUMERIC, InputType.STRING_SHORT -> player.dialogueInterpreter.sendInput(type != InputType.NUMERIC, prompt)
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

fun flee(entity: Entity, from: Entity){
    lock(entity, 5)
    face(entity, from, 5)

    val diffX = entity.location.x - from.location.x
    val diffY = entity.location.y - from.location.y

    forceWalk(entity, entity.location.transform(diffX,diffY,0), "DUMB")
}

/**
 * Submits an individual or "weak" pulse to a specific entity's pulse manager. Pulses submitted this way can be overridden by other pulses.
 * @param entity the entity to submit the pulse to
 * @param pulse the pulse to submit
 */

fun submitIndividualPulse(entity: Entity, pulse: Pulse){
    entity.pulseManager.run(pulse)
}

/**
 * Similar to submitIndividualPulse, but for non-repeating tasks, with a cleaner syntax.
 */

fun runTask(entity: Entity, delay: Int = 0, task: () -> Unit){
    entity.pulseManager.run(object : Pulse(delay) {
        override fun pulse(): Boolean {
            task.invoke()
            return true
        }
    })
}

/**
 * Gets the number of QP a player has
 * @param player the player to get the QP for
 * @return the number of QP the player has
 */

fun getQP(player: Player): Int{
    return player.questRepository.points
}

/**
 * Gets the stage for the given quest for the given player
 */

fun questStage(player: Player, quest: String): Int{
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
 * Gets a scenery definition from the given ID
 * @param id the ID of the scenery to get the definition for.
 * @return the scenery definition
 */

fun sceneryDefinition(id: Int): SceneryDefinition{
    return SceneryDefinition.forId(id)
}

/**
 * Register a map zone
 * @param zone the zone to register
 * @param borders the ZoneBorders that compose the zone
 */

fun registerMapZone(zone: MapZone, borders: ZoneBorders){
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

fun animateInterface(player: Player, iface: Int, child: Int, anim: Int){
    player.packetDispatch.sendAnimationInterface(anim,iface,child)
}

/**
 * Adds a climb destination to the ladder handler.
 * @param ladderLoc the location of the ladder/stairs object you want to climb.
 * @param dest the destination for the climb.
 */

fun addClimbDest(ladderLoc: Location, dest: Location){
    SpecialLadders.add(ladderLoc,dest)
}

/**
 * Sends a news announcement in game chat.
 * @param message the message to announce
 */

fun sendNews(message: String){
    Repository.sendNews(message, 12, "CC6600")
}

/**
 * Sends a given Graphics object, or graphics ID, to the given location.
 * @param gfx the Graphics object, or the Integer ID of the graphics, to send. Either works.
 * @param location the location to send it to
 */

fun <G> sendGraphics(gfx: G, location: Location){
    when(gfx){
        is Int -> Graphics.send(Graphics(gfx),location)
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

    if(hasMastered){
        for ((skillId, skillName) in Skills.SKILL_NAME.withIndex()) {
            //phil you were looping every skill and performing a string comparison (another loop) for every one here.
            //that's some real shit code bro.
            //you were also performing a dynamic level check rather than a static.
            if(hasLevelStat(player, skillId, 99)){
                masteredSkills.add(skillName)
            }
        }
    }
    return masteredSkills
}

/**
 * Dumps the given player's given container into that player's bank.
 * @param player the player
 * @param container the player's container to dump.
 * @author ceik
 * @author James Triantafylos
 */

fun dumpContainer(player: Player, container: core.game.container.Container) {
    val bank = player.bank
    container.toArray().filterNotNull().forEach { item ->
        if (!bank.hasSpaceFor(item)) {
            player.packetDispatch.sendMessage("You have no more space in your bank.")
            return
        }
        if (!bank.canAdd(item)) {
            player.packetDispatch.sendMessage("A magical force prevents you from banking your " + item.name + ".")
            return
        } else {
            if (container is EquipmentContainer) {
                if(!InteractionListeners.run(item.id,player,item,false)) {
                    player.packetDispatch.sendMessage("A magical force prevents you from removing that item.")
                    return
                }
            }
            container.remove(item)
            bank.add(if (item.definition.isUnnoted) item else Item(item.noteChange, item.amount))
        }
    }
    container.update()
    bank.update()
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