package content.region.wilderness.handlers

import content.region.wilderness.handlers.WildernessGateHandler.GateDialogue
import core.ServerConstants
import core.api.*
import core.game.interaction.*
import core.game.node.entity.player.Player
import core.game.node.Node
import core.game.global.action.DoorActionHandler
import core.game.dialogue.*
import core.tools.*

class WildernessGateHandler : InteractionListener {
    val gates = intArrayOf(1597, 1596)

    override fun defineListeners() {
        on (gates, IntType.SCENERY, "open", handler = ::handleGate)
    }

    private fun handleGate(player: Player, node: Node) : Boolean {
        if (player.location.y > 3890 && ServerConstants.ENHANCED_DEEP_WILDERNESS) {
            val isEntering = !player.skullManager.isDeepWilderness
            if (isEntering) {
                fun enter(player: Player) {
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                    player.skullManager.isDeepWilderness = true
                }
                enterDeepWilderness(player, ::enter, "Beyond this gate you enter the deep wilderness!")
            } else {
                if (player.properties.combatPulse.isInCombat) {
                    sendMessage(player, "You cannot leave while you are under attack.")
                } else {
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                    player.skullManager.isDeepWilderness = false
                }
            }
        } else DoorActionHandler.handleAutowalkDoor(player, node.asScenery())

        return true
    }

    class GateDialogue(val callback: (Player) -> Unit, val firstLine: String) : DialogueFile() {
        override fun handle (interfaceId: Int, buttonId: Int) {
            when (stage) {
                0 -> sendDialogueLines(player!!, "WARNING!", firstLine, "Anyone will be able to attack you without consequence!", "You WILL NOT be able to leave during combat!").also { stage++ }
                1 -> sendDialogueLines(player!!, "While you are there, you will be skulled:","you will lose all your items if you die!","The skull will go away when you leave the deep wilderness.").also { stage++ }
                2 -> sendDialogueLines(player!!, "If you are risking sufficient value, the skull will become red.","This increases your chances of receiving special loot from killing","revenants and the Chaos Elemental!").also { stage++ }
                3 -> showTopics(
                        Topic(FacialExpression.NEUTRAL, "I wish to proceed.", 10, true),
                        Topic(FacialExpression.NEUTRAL, "I wish to proceed, and don't show this warning again.", 11, true),
                        Topic(FacialExpression.NEUTRAL, "Never mind.", END_DIALOGUE, true)
                     )
                10  -> {
                    end()
                    callback(player!!)
                }
                11 -> {
                    player!!.setAttribute("/save:skip-deep-wilderness-warning", true)
                    end()
                    callback(player!!)
                }
            }
        }
    }
}

fun enterDeepWilderness(player: Player, callback: (Player) -> Unit, firstLine: String) {
    if (player.getAttribute("/save:skip-deep-wilderness-warning",false)) {
        callback(player)
    } else {
        openDialogue(player, GateDialogue(callback, firstLine))
    }
}
