package rs09.game.interaction.`object`

import api.*
import core.game.node.entity.skill.gather.SkillingResource
import core.game.node.entity.skill.gather.mining.MiningNode
import core.game.system.task.Pulse
import rs09.game.interaction.InteractionListener

/**
 * @author: bushtail
 */

class ProspectListener : InteractionListener() {

    override fun defineListeners() {
        on(SCENERY, "prospect") { player, node ->
            /** Check if the rock contains gems */
            if(MiningNode.forId(node.id).identifier == 13.toByte()) {
                val rock = SkillingResource.forId(node.asScenery().id)
                if(rock == null) {
                    sendMessage(player, "There is no ore currently available in this rock.")
                    return@on true
                }
                sendMessage(player,"You examine the rock for ores...")
                /** Send a simple text string saying it's a gem rock */
                player.pulseManager.run(object : Pulse(3) {
                    override fun pulse(): Boolean {
                        sendMessage(player, "This rock contains gems.")
                        return true
                    }
                })
            }
            /** If it doesn't contain gems */
            else {
                val rock = SkillingResource.forId(node.id)
                if(rock == null) {
                    sendMessage(player, "There is no ore currently available in this rock.")
                    return@on true
                }
                sendMessage(player,"You examine the rock for ores...")
                /** Get the name of the rock's reward and sends a message to the player */
                player.pulseManager.run(object : Pulse(3) {
                    override fun pulse(): Boolean {
                        sendMessage(player, "This rock contains ${itemDefinition(rock.reward).name.toLowerCase()}.")
                        return true
                    }
                })
            }
            return@on true
        }
    }
}