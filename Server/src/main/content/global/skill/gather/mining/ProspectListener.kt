package content.global.skill.gather.mining

import core.api.*
import content.global.skill.gather.mining.MiningNode
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

/**
 * @author: bushtail
 */

class ProspectListener : InteractionListener {

    override fun defineListeners() {
        on(IntType.SCENERY, "prospect") { player, node ->
            val rock = MiningNode.forId(node.asScenery().id)

            if(rock == null) {
                sendMessage(player, "There is no ore currently available in this rock.")
                return@on true
            }

            /** Check if the rock contains gems */
            if(MiningNode.forId(node.id).identifier == 13.toByte()) {
                sendMessage(player,"You examine the rock for ores...")
                /** Send a simple text string saying it's a gem rock */
                player.pulseManager.run(object : Pulse(3) {
                    override fun pulse(): Boolean {
                        sendMessage(player, "This rock contains gems.")
                        return true
                    }
                })
                return@on true
            }

            /** If it doesn't contain gems */
            else {
                sendMessage(player,"You examine the rock for ores...")
                /** Get the name of the rock's reward and sends a message to the player */
                player.pulseManager.run(object : Pulse(3) {
                    override fun pulse(): Boolean {
                        sendMessage(player, "This rock contains ${Item(rock.reward).name.lowercase()}.")
                        return true
                    }
                })
            }
            return@on true
        }
    }
}
