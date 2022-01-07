package rs09.game.content.dialogue.region.miscellania

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 */

@Initializable
class OsvaldDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FacialExpression.FRIENDLY,"Welcome to the Miscellania food store.","We've only opened recently.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0-> {
                npc(FacialExpression.NEUTRAL, "Would you like to buy anything,",
                    "your Royal Highness?").also { stage++ }
            }

            1 -> {
                options("Could you show me what you have for sale?",
                    "No thank you, I don't need food just now.",
                    "What's it like living down here?").also { stage++ }
            }

            2 -> when(buttonId){
                1 -> {
                    player(FacialExpression.ASKING, "Could you show me what you have for sale?").also { stage = 10 }
                }

                2 -> {
                    player(FacialExpression.NEUTRAL, "No thank you, I don't need food just now.").also { end() }
                }

                3 -> {
                    player(FacialExpression.ASKING, "What's it like living down here?").also { stage = 20 }
                }
            }

            10 -> {
                end().also { npc.openShop(player) }
            }

            20 -> {
                npc(FacialExpression.FRIENDLY, "The town's thriving.",
                    "I'm sure it'll soon be as busy as Rellekka!").also { stage = 99 }
            }

            99 -> {
                end()
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return OsvaldDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.OSVALD_3923)
    }
}