package content.region.kandarin.ardougne.westardougne.dialogue

import content.region.kandarin.ardougne.westardougne.MournerUtilities
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class ChildDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FacialExpression.FRIENDLY, "Hello there.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val noun = if (MournerUtilities.wearingMournerGear(player) > MournerUtilities.JUST_MASK) "mourners" else "strangers"
        npcl(FacialExpression.CHILD_NORMAL, "I'm not allowed to speak with $noun.").also { stage = END_DIALOGUE }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return ChildDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CHILD_356, NPCs.CHILD_355)
    }

}