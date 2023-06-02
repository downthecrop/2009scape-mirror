package content.region.kandarin.ardougne.plaguecity.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class MournersDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FacialExpression.FRIENDLY, "Hi.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FacialExpression.SAD, "What are you up to?").also { stage++ }
            1 -> playerl(FacialExpression.NEUTRAL,"Just sight-seeing.").also { stage++ }
            2 -> npcl(FacialExpression.FRIENDLY, "This is no place for sight-seeing. Don't you know there's been a plague outbreak?").also { stage++ }
            3 -> playerl(FacialExpression.FRIENDLY, "Yes, I had heard.").also { stage++ }
            4 -> npcl(FacialExpression.FRIENDLY, "Then I suggest you leave as soon as you can.").also { stage++ }
            5 -> playerl(FacialExpression.FRIENDLY, "Thanks for the advice.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return MournersDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.MOURNER_347, NPCs.MOURNER_348, NPCs.MOURNER_357, NPCs.MOURNER_369, NPCs.MOURNER_371, NPCs.MOURNER_370)
    }

}