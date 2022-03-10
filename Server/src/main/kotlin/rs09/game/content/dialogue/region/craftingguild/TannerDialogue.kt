package rs09.game.content.dialogue.region.craftingguild

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.tools.END_DIALOGUE

/**
 * @author bushtail
 */

@Initializable
class TannerDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player) : DialoguePlugin {
        return TannerDialogue(player)
    }

    override fun open(vararg args: Any?) : Boolean {
        npc = args[0] as NPC
        npcl(FacialExpression.NEUTRAL, "Greetings friend. I am a manufacturer of leather.")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int) : Boolean {
        when(stage) {
            0 -> options("Can I buy some leather then?", "Leather is rather weak stuff.").also{ stage++ }
            1 -> when(buttonId) {
                1 -> player(FacialExpression.ASKING,"Can I buy some leather then?").also{ stage = 10 }
                2 -> player(FacialExpression.SUSPICIOUS, "Leather is rather weak stuff.").also { stage = 20 }
            }

            10 -> npcl(FacialExpression.FRIENDLY, "Certainly!").also { stage = END_DIALOGUE }.also{ npc.openShop(player) }

            20 -> npcl(FacialExpression.NOD_YES, "Normal leather may be quite weak, but it's very cheap - I " +
                    "make it from cowhides for only 1 gp per hide - and it's so easy to craft that anyone can work with it.").also{ stage++ }
            21 -> npcl(FacialExpression.HALF_THINKING, "Alternatively you could try hard leather. It's not so easy " +
                    "to craft, but I only charge 3 gp per cowhide to prepare it, and it makes much sturdier armour.").also{ stage++ }
            22 -> npcl(FacialExpression.FRIENDLY, "I can also tan snake hides and dragonhides, suitable for crafting" +
                    "into the highest quality armour for rangers.").also{ stage++ }
            23 -> player(FacialExpression.NEUTRAL, "Thanks, I'll bear it in mind.").also { stage = END_DIALOGUE }
        }
        return true
    }
    override fun getIds() : IntArray {
        return intArrayOf(NPCs.TANNER_804)
    }

}