package rs09.game.content.dialogue.region.neitiznot

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.tools.END_DIALOGUE

@Initializable
class LisseIsaaksonDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return LisseIsaaksonDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npcl(FacialExpression.FRIENDLY, "Hello, visitor!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> playerl(FacialExpression.ASKING, "Hello. What are you up to?").also { stage++ }
            1 -> npcl(FacialExpression.FRIENDLY, "Ah, I was about to collect some yak's milk to make yak cheese.").also { stage++ }
            2 -> playerl(FacialExpression.HALF_WORRIED, "Eughr! Though I am curious. Can I try some?").also { stage++ }
            3 -> npcl(FacialExpression.SAD, "Sorry, no. The last outlander who ate my cheese was ill for a month.").also { stage++ }
            4 -> playerl(FacialExpression.ASKING, "So why don't you get ill as well?").also { stage++ }
            5 -> npcl(FacialExpression.FRIENDLY, "Well, we eat yak milk products every day, from when we're born. So I suppose we're used to it. Anyway I should stop yakking - haha - and get on with my work.").also { stage++ }
            6 -> playerl(FacialExpression.HAPPY, "I'm glad to see that puns are common everywhere in Gielinor; even here.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LISSE_ISAAKSON_5513)
    }

}