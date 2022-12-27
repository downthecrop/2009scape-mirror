package rs09.game.content.global.action

import api.events.PickUpEvent
import api.getItemName
import core.game.content.dialogue.FacialExpression
import core.game.content.global.GodType
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.audio.Audio
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.runecrafting.RunePouch
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import rs09.game.node.entity.player.info.PlayerMonitor
import rs09.game.system.SystemLogger
import rs09.game.system.config.GroundSpawnLoader
import rs09.game.world.GameWorld

/**
 * A class used to handle the picking up of ground items.
 * @author 'Vexia
 */
object PickupHandler {
    /**
     * Method used to take a ground item.
     * @param player the player.
     * @param item the item.
     * @return `True` if taken.
     */
    @JvmStatic
    fun take(player: Player, item: GroundItem): Boolean {
        if (item.location == null) {
            player.packetDispatch.sendMessage("Invalid ground item!")
            return true
        }
        if (!GroundItemManager.getItems().contains(item)) {
            player.packetDispatch.sendMessage("Too late!")
            return true
        }
        if (player.getAttribute("droppedItem:" + item.id, 0) > GameWorld.ticks) { //Splinter
            SystemLogger.logAlert(this::class.java, "$player tried to do the drop & quick pick-up Ground Item dupe.")
            return true
        }
        if (item !is GroundSpawnLoader.GroundSpawn && item.isRemainPrivate && !item.droppedBy(player)) {
            player.sendMessage("You can't take that item!")
            return true
        }
        val add = Item(item.id, item.amount, item.charge)
        if (!player.inventory.hasSpaceFor(add)) {
            player.packetDispatch.sendMessage("You don't have enough inventory space to hold that item.")
            return true
        }
        if (!canTake(player, item, 0)) {
            return true
        }
        if (item.isActive && player.inventory.add(add)) {
            if (item.dropper is Player && item.dropper.details.uid != player.details.uid){
                PlayerMonitor.logMisc(item.dropper, "DropTrade", "${getItemName(item.id)} x${item.amount} picked up by ${player.name}.")
            }
            if (!RegionManager.isTeleportPermitted(item.location)) {
                player.animate(Animation.create(535))
            }
            GroundItemManager.destroy(item)
/*            if (item.dropper?.isArtificial == true) {
                getItems(item.dropper)?.remove(item)
            }
            */

            player.audioManager.send(Audio(2582, 10, 1))
            player.dispatch(PickUpEvent(item.id))
        }
        return true
    }

    /**
     * Checks if the player can take an item.
     * @param player the player.
     * @param item the item.
     * @param type the type (1= ground, 2=telegrab)
     * @return `True` if so.
     */
    @JvmStatic
    fun canTake(player: Player, item: GroundItem, type: Int): Boolean {
        if (item.dropper != null && !item.droppedBy(player) && player.ironmanManager.checkRestriction()) {
            return false
        }
        if (item.id == 8858 || item.id == 8859) {
            player.dialogueInterpreter.sendDialogues(4300, FacialExpression.FURIOUS, "Hey! You can't take that, it's guild property. Take one", "from the pile.")
            return false
        }
        if (GodType.forCape(item) != null) {
            if (GodType.hasAny(player)) {
                player.sendMessages("You may only possess one sacred cape at a time.", "The conflicting powers of the capes drive them apart.")
                return false
            }
        }
        if (RunePouch.forItem(item) != null) {
            if (player.hasItem(item)) {
                player.sendMessage("A mystical force prevents you from picking up the pouch.")
                return false
            }
        }
        return if (item.hasItemPlugin()) {
            item.plugin.canPickUp(player, item, type)
        } else true
    }
}
