package content.region.desert.alkharid.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE

/**
 * @author bushtail
 */

@Initializable
class AablaDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return AablaDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(core.game.dialogue.FacialExpression.FRIENDLY, "Hi!").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            0 -> showTopics(
                Topic("Can you heal me?", AlKharidHealDialogue(true)),
                Topic("Do you see a lot of injured fighters?", 101),
                Topic("Do you come here often?", 201)
            )

            101 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "I work here, so yes!").also { stage = END_DIALOGUE }

            201 -> npcl(core.game.dialogue.FacialExpression.HALF_THINKING, "Yes I do. Thankfully we can cope with almost anything. Jaraah really is a wonderful surgeon, his methods are a little unorthodox but he gets the job done.").also { stage++ }
            202 -> npcl(core.game.dialogue.FacialExpression.HALF_GUILTY, "I shouldn't tell you this but his nickname is 'The Butcher'.").also { stage++ }
            203 -> player(core.game.dialogue.FacialExpression.HALF_WORRIED, "That's reassuring.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AABLA_959)
    }
}