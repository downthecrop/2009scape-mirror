package core.game.global.action

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.combat.graves.GraveController
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.login.PlayerParser
import core.game.node.entity.player.link.audio.Audio
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.config.ItemConfigParser

class DropListener : InteractionListener {
    override fun defineListeners() {
        on(IntType.ITEM, "drop", "destroy", "dissolve", handler = ::handleDropAction)
    }

    companion object {
        @JvmStatic fun drop(player: Player, item: Item) : Boolean {
            return handleDropAction(player, item)
        }
        private fun handleDropAction(player: Player, node: Node) : Boolean {
            val option = getUsedOption(player)
            var item = node as? Item ?: return false
            if (option == "drop") {
                if (GraveController.hasGraveAt(player.location)) {
                    sendMessage(player, "You cannot drop items on top of graves!")
                    return false
                }
                if (getAttribute(player, "equipLock:${node.id}", 0 ) > getWorldTicks())
                    return false

                queueScript (player, strength = QueueStrength.SOFT) {
                    if (player.inventory.replace(null, item.slot) != item) return@queueScript stopExecuting(player)
                    item = item.dropItem
                    player.audioManager.send(Audio(if (item.id == 995) 10 else 2739, 1, 0))
                    GroundItemManager.create(item, player.location, player)
                    setAttribute(player, "droppedItem:${item.id}", getWorldTicks() + 2)
                    PlayerParser.save(player)
                    return@queueScript stopExecuting(player)
                }
            } else if (option == "destroy" || option == "dissolve" || item.definition.handlers.getOrDefault(ItemConfigParser.DESTROY, false) as Boolean) {
                player.dialogueInterpreter.open(9878, item)
            }
            return true
        }
    }
}