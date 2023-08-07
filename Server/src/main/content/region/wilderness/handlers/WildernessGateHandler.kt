package content.region.wilderness.handlers

import core.api.*
import core.game.interaction.*
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.node.Node
import core.game.global.action.DoorActionHandler
import core.game.dialogue.*
import core.tools.*

class WildernessGateHandler : InteractionListener {
    val gates = intArrayOf(1597, 1596)

    override fun defineListeners() {
        on (gates, IntType.SCENERY, "open", handler = ::handleGate)
    }

    private fun handleGate (player: Player, node: Node) : Boolean {
        if (player.location.y > 3890) {
            val isEntering = !player.skullManager.isDeepWilderness()

            if (isEntering)
                openDialogue(player, GateDialogue(node.asScenery()))
            else {
                if (player.properties.combatPulse.isInCombat)
                    sendMessage(player, "You cannot leave while you are under attack.")
                else {
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                    player.skullManager.isDeepWilderness = false
                }
            }
        } else DoorActionHandler.handleAutowalkDoor (player, node.asScenery())

        return true
    }

    class GateDialogue (val gate: Scenery) : DialogueFile() {
        override fun handle (interfaceId: Int, buttonId: Int) {
            when (stage) {
                0 -> sendDialogueLines(player!!, "WARNING!", "Beyond this gate you enter the deep wilderness!", "Anyone will be able to attack you without consequence!", "You WILL NOT be able to leave during combat!").also { stage++ }
                1 -> showTopics (
                        Topic(FacialExpression.NEUTRAL, "I wish to proceed.", 10, true),
                        Topic(FacialExpression.NEUTRAL, "Nevermind.", END_DIALOGUE, true)
                     )
                10 -> {
                    end()
                    DoorActionHandler.handleAutowalkDoor (player!!, gate)
                    player!!.skullManager.isDeepWilderness = true
                }
            }
        }
    }
}
