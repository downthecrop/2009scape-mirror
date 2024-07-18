package core.game.global.action

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.combat.graves.GraveController
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.login.PlayerParser
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.config.ItemConfigParser
import content.global.skill.summoning.pet.Pets
import org.rs09.consts.Items
import org.rs09.consts.Sounds

/**
 * Represents the item drop/destroy/dissolve handler.
 * @author Ceikry, ovenbreado
 */
class DropListener : InteractionListener {
    override fun defineListeners() {
        on(IntType.ITEM, "drop", "destroy", "dissolve", handler = ::handleDropAction)
    }

    companion object {
        private val DROP_COINS_SOUND = Sounds.EYEGLO_COIN_10
        private val DROP_ITEM_SOUND = Sounds.PUT_DOWN_2739
        private val DESTROY_ITEM_SOUND = Sounds.DESTROY_OBJECT_2381
        @JvmStatic fun drop(player: Player, item: Item) : Boolean {
            return handleDropAction(player, item)
        }
        private fun handleDropAction(player: Player, node: Node) : Boolean {
            val option = getUsedOption(player)
            var item = node as? Item ?: return false
            if (option == "drop") {
                if (Pets.forId(item.id) != null) {
                    player.familiarManager.summon(item, true, true)
                    return true
                }
                if (GraveController.hasGraveAt(player.location)) {
                    sendMessage(player, "You cannot drop items on top of graves!")
                    return false
                }
                if (getAttribute(player, "equipLock:${node.id}", 0 ) > getWorldTicks())
                    return false

                closeAllInterfaces(player)
                queueScript (player, strength = QueueStrength.SOFT) {
                    if (player.inventory.replace(null, item.slot) != item) return@queueScript stopExecuting(player)
                    val droppedItem = item.dropItem
                    if (droppedItem.id == Items.COINS_995) playAudio(player, DROP_COINS_SOUND) else playAudio(player, DROP_ITEM_SOUND)
                    GroundItemManager.create(droppedItem, player.location, player)
                    setAttribute(player, "droppedItem:${droppedItem.id}", getWorldTicks() + 2)
                    PlayerParser.save(player)
                    return@queueScript stopExecuting(player)
                }
            } else if (option == "destroy" || option == "dissolve" || item.definition.handlers.getOrDefault(ItemConfigParser.DESTROY, false) as Boolean) {
                player.dialogueInterpreter.sendDestroyItem(item.id, item.name)
                addDialogueAction(player) { player, button ->
                    if (button == 3) {
                        if (removeItem(player, item)) {
                            playAudio(player, DESTROY_ITEM_SOUND)
                        }
                    }
                }
            }
            return true
        }
    }
}
