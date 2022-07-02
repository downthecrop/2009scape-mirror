package rs09.game.content.dialogue.region.alkharid

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.DialogueFile
import rs09.game.content.dialogue.Topic
import rs09.tools.END_DIALOGUE

/**
 * @author bushtail
 */

@Initializable
class SabreenDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return SabreenDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FacialExpression.FRIENDLY, "Hi!").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            0 -> showTopics(
                Topic("Can you heal me?", AlKharidHealDialogue(true)),
                Topic("Do you see a lot of injured fighters?", 101),
                Topic("Do you come here often?", 201)
            )

            101 -> npcl(FacialExpression.FRIENDLY, "I work here, so yes!").also { stage = END_DIALOGUE }

            201 -> npcl(FacialExpression.HALF_THINKING, "Yes I do. Thankfully we can cope with almost anything. Jaraah really is a wonderful surgeon, his methods are a little unorthodox but he gets the job done.").also { stage++ }
            202 -> npcl(FacialExpression.HALF_GUILTY, "I shouldn't tell you this but his nickname is 'The Butcher'.").also { stage++ }
            203 -> player(FacialExpression.HALF_WORRIED, "That's reassuring.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SABREEN_960)
    }
}