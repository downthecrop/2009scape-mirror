package content.region.kandarin.ardougne.quest.fightarena.dialogues.guards

import core.api.isEquipped
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class KhazardGuard254Dialogue(player: Player? = null) : DialoguePlugin(player) {

    // Khazard Guards - Inside the prison.
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (isEquipped(player!!, Items.KHAZARD_HELMET_74) && isEquipped(player!!, Items.KHAZARD_ARMOUR_75)) {
            playerl(FacialExpression.FRIENDLY, "Hello.").also { stage = 0 }
        } else {
            playerl(FacialExpression.FRIENDLY, "Hi.").also { stage = 7 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FacialExpression.FRIENDLY, "I've never seen you around here before!").also { stage = 1 }
            1 -> playerl(FacialExpression.FRIENDLY, "Long live General Khazard!").also { stage = 2 }
            2 -> npcl(FacialExpression.FRIENDLY, "Erm.. yes.. soldier, I take it you're new around here?").also { stage = 3 }
            3 -> playerl(FacialExpression.FRIENDLY, "You could say that.").also { stage = 4 }
            4 -> npcl(FacialExpression.FRIENDLY, "Khazard died two hundred years ago. However his dark spirit remains in the form of the undead maniac General Khazard. Remember he is your master, always watching.").also { stage = 5 }
            5 -> npcl(FacialExpression.FRIENDLY, "Got that newbie?").also { stage = 6 }
            6 -> playerl(FacialExpression.FRIENDLY, "Undead, maniac, master. Got it, loud and clear.").also { stage = END_DIALOGUE }
        }
        when (stage) {
            7 -> npcl(FacialExpression.ANNOYED, "This area is restricted, leave now!").also { stage = 8 }
            8 -> npcl(FacialExpression.ANGRY, "OUT! And don't come back!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return KhazardGuard254Dialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KHAZARD_GUARD_254)
    }
}