package content.region.kandarin.ardougne.plaguecity.dialogue

import core.api.sendNPCDialogue
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class RecruiterDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FacialExpression.FRIENDLY, "Citizens of West Ardougne. Who will join the Royal Army of Ardougne? It is a very noble cause. Fight alongside King Tyras, crusading in the darklands of the west!").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> sendNPCDialogue(player, NPCs.MAN_726, "Plague bringer!").also { stage++ }
            1 -> sendNPCDialogue(player, NPCs.MAN_726, "King Tyras is scum!").also { stage++ }
            2 -> npcl(FacialExpression.FRIENDLY, "Tyras will be informed of these words of treason!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return RecruiterDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.RECRUITER_720)
    }

}