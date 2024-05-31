package content.region.morytania.canifis.dialogue

import content.global.skill.crafting.TanningProduct
import core.api.inInventory
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.entity.npc.NPC
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

/**
 * Handles Sbott's dialogue.
 */
@Initializable
class SbottDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FacialExpression.HAPPY, "Hello stranger. Would you like to me to tan any hides for you?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            //0 -> npc(FacialExpression.HAPPY, "Soft leather - 2 gp per hide", "Hard leather - 5 gp per hide", "Snakeskins - 25 gp per hide", "Dragon leather - 45 gp per hide.").also { stage++ }
            0 -> npc(FacialExpression.HAPPY, "Soft leather - 1 gp per hide", "Hard leather - 3 gp per hide", "Snakeskins - 20 gp per hide", "Dragon leather - 20 gp per hide.").also { stage++ }
            1 -> {
                var hasHides = false

                for (tanningProduct in TanningProduct.values()) {
                    if (inInventory(player, tanningProduct.item)) {
                        hasHides = true
                        break
                    }
                }

                if(hasHides) {
                    npcl(FacialExpression.FRIENDLY, "I see you have brought me some hides. Would you like me to tan them for you?").also { stage = 10 }
                } else {
                    playerl(FacialExpression.HALF_GUILTY, "No thanks, I haven't any hides.").also { stage = END_DIALOGUE }
                }
            }

            10 -> options("Yes please.", "No thanks.").also { stage++ }

            11 -> when (buttonId) {
                1 -> playerl(FacialExpression.HAPPY, "Yes please.").also { stage = 12 }
                2 -> playerl(FacialExpression.NEUTRAL, "No thanks.").also { stage = 13 }
            }

            12 -> end().also { TanningProduct.open(player, NPCs.SBOTT_1041) }
            13 -> npcl(FacialExpression.FRIENDLY, "Very well, @g[sir,madam], as you wish.").also { stage = END_DIALOGUE }
        }

        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return SbottDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SBOTT_1041)
    }
}
