package content.region.misc.miscellania.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 */

@Initializable
class RunaDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(core.game.dialogue.FacialExpression.FRIENDLY,"Would you like to try some fine Miscellanian ale,", "your Royal Highness?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> {
                npc(core.game.dialogue.FacialExpression.ASKING, "Well I say Miscellanian, but it's actually brewed", "on the mainland.").also { stage++ }
            }

            1 -> {
                npc(core.game.dialogue.FacialExpression.FRIENDLY, "Would you like to try some anyway?").also { stage++ }
            }

            2 -> {
                options("Yes, please.", "No, thank you.", "What's it like living down here?").also { stage++ }
            }

            3 -> when(buttonId){
                1 -> {
                    player(core.game.dialogue.FacialExpression.ASKING, "Yes please.").also { stage = 10 }
                }

                2 -> {
                    player(core.game.dialogue.FacialExpression.NEUTRAL, "No thank you.").also { stage = 99 }
                }

                3 -> {
                    player(core.game.dialogue.FacialExpression.ASKING, "What's it like living down here?").also { stage = 20 }
                }
            }

            10 -> {
                end().also { npc.openShop(player) }
            }

            20 -> {
                npc(core.game.dialogue.FacialExpression.HALF_WORRIED, "Business is booming!").also { stage++ }
            }

            21 -> {
                npc(core.game.dialogue.FacialExpression.HALF_WORRIED, "Now, if only I hadn't taken a loss to the beer I sold", "to those teenagers.").also { end() }
            }

            99 -> {
                end()
            }
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return RunaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.RUNA_3920)
    }
}