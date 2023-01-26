package content.region.fremennik.neitiznot.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.tools.END_DIALOGUE

@Initializable
class LisseIsaaksonDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return LisseIsaaksonDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Hello, visitor!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> playerl(core.game.dialogue.FacialExpression.ASKING, "Hello. What are you up to?").also { stage++ }
            1 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Ah, I was about to collect some yak's milk to make yak cheese.").also { stage++ }
            2 -> playerl(core.game.dialogue.FacialExpression.HALF_WORRIED, "Eughr! Though I am curious. Can I try some?").also { stage++ }
            3 -> npcl(core.game.dialogue.FacialExpression.SAD, "Sorry, no. The last outlander who ate my cheese was ill for a month.").also { stage++ }
            4 -> playerl(core.game.dialogue.FacialExpression.ASKING, "So why don't you get ill as well?").also { stage++ }
            5 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Well, we eat yak milk products every day, from when we're born. So I suppose we're used to it. Anyway I should stop yakking - haha - and get on with my work.").also { stage++ }
            6 -> playerl(core.game.dialogue.FacialExpression.HAPPY, "I'm glad to see that puns are common everywhere in Gielinor; even here.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LISSE_ISAAKSON_5513)
    }

}