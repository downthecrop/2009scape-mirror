package rs09.game.content.dialogue.region.miscellania

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * Dialogue for Finn in Misc.
 * @author qmqz
 */

@Initializable
class FinnDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FacialExpression.FRIENDLY,"Can I help you, your Royal Highness?").also { stage = 0 }
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> {
                options("Yes please. What are you selling?", "No thanks.", "What's it like living down here?").also { stage++ }
            }

            1 -> when(buttonId){
                1 -> {
                    player(FacialExpression.ASKING, "Yes please. What are you selling?").also { stage = 10 }
                }

                2 -> {
                    player(FacialExpression.NEUTRAL, "No thanks.").also { stage = 99 }
                }

                3 -> {
                    player(FacialExpression.ASKING, "What's it like living down here?").also { stage = 20 }
                }
            }

            10 -> {
                end().also { npc.openShop(player) }
            }

            20 -> {
                npc(FacialExpression.HALF_WORRIED, "A lot drier in the winter than it is above ground.").also { stage = 99 }
            }

            99 -> {
                end()
            }

            }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return FinnDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FINN_3922)
    }
}