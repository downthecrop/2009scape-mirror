package content.region.kandarin.ardougne.plaguecity.dialogue

import core.api.inEquipment
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class CivilianDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if(inEquipment(player, Items.GAS_MASK_1506)) {
            playerl(FacialExpression.FRIENDLY, "Hello there.").also { stage = 4 }
        } else {
            playerl(FacialExpression.FRIENDLY, "Hello there.").also { stage = 0 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FacialExpression.FRIENDLY, "I'm a bit busy to talk right now, sorry.").also { stage++ }
            1 -> playerl(FacialExpression.FRIENDLY, "Why? What are you doing?").also { stage++ }
            2 -> npcl(FacialExpression.FRIENDLY, "Trying to kill these mice! What I really need is a cat!").also { stage++ }
            3 -> playerl(FacialExpression.FRIENDLY, "No, you're right, you don't see many around.").also { stage = END_DIALOGUE }
            4 -> npcl(FacialExpression.FRIENDLY, "If you Mourners really wanna help, why don't you do something about these mice?!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return CivilianDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CIVILIAN_785, NPCs.CIVILIAN_786, NPCs.CIVILIAN_787)
    }
}