package rs09.game.interaction.`object`

import api.*
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import org.rs09.consts.Animations
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType

class SeaweedNetHandler : InteractionListener {

    override fun defineListeners() {
        on(NET, IntType.SCENERY, "Take-from"){ player, node ->
            if (!isQuestComplete(player, "Swan Song"))
            {
                sendMessage(player, "You must complete Swan Song first.")
            }
            else if (!hasSpaceFor(player, Item(Items.SEAWEED_401, 1))) {
                sendMessage(player, "You do not have space in your inventory.")
            }
            else {
                submitIndividualPulse(player, object : Pulse() {
                    private var tick = 0
                    override fun pulse(): Boolean {
                        when(tick++){
                            0 -> {
                                animate(player, Animations.HUMAN_BURYING_BONES_827) // no idea what animation is supposed to be used, can't even find a video of this content
                            }
                            1 -> {
                                if (addItem(player, Items.SEAWEED_401)) {
                                    SceneryBuilder.replace(node.asScenery(), Scenery(EMPTY_NET, node.location, node.asScenery().rotation),5)  // osrs wiki says 3 second respawn timer
                                }
                                return true
                            }
                        }
                        return false
                    }
                })
            }
            return@on true
        }
    }

    companion object {
        private const val NET = 14973
        private const val EMPTY_NET = 14972
    }
}
