package content.region.misc.keldagrim.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class BentamirDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            START_DIALOGUE -> npcl(FacialExpression.OLD_ANGRY1, "Do you mind? You're in my home.").also { stage++ }
            1 -> playerl(FacialExpression.FRIENDLY, "I'm sorry, should I have knocked?").also { stage++ }
            2 -> npcl(FacialExpression.OLD_ANGRY1, "Very funny, human.").also { stage++ }
            3 -> npcl(FacialExpression.OLD_ANGRY1, "We can't all live in plush houses, you know. But that doesn't mean us mining dwarves don't work hard.").also { stage++ }
            4 -> playerl(FacialExpression.FRIENDLY, "Where do you do your mining?").also { stage++ }
            5 -> npcl(FacialExpression.OLD_NOT_INTERESTED, "Normally the coal mine to the north. We need a lot of coal to keep our steam engines going, you know.").also { stage++ }
            6 -> playerl(FacialExpression.FRIENDLY, "Can I do a bit of mining there as well?").also { stage++ }
            7 -> npcl(FacialExpression.OLD_NOT_INTERESTED, "I'm not sure, but as long as no one notices I don't think anyone is going to care.").also {
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return BentamirDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BENTAMIR_2192)
    }
}