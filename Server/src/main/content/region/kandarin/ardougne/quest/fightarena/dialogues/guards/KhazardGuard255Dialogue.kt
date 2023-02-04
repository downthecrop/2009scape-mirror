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
class KhazardGuard255Dialogue(player: Player? = null) : DialoguePlugin(player) {

    // Khazard Guard - Outside.
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (isEquipped(player!!, Items.KHAZARD_HELMET_74) && isEquipped(player!!, Items.KHAZARD_ARMOUR_75)) {
            playerl(FacialExpression.FRIENDLY, "Hello.").also { stage = 0 }
        } else {
            playerl(FacialExpression.FRIENDLY, "Hi.").also { stage = 2 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FacialExpression.ASKING, "Can I help you stranger?").also { stage = 1 }
            1 -> npcl(FacialExpression.FRIENDLY, "Oh, you're a guard as well. That's ok then. We don't like strangers around here.").also { stage = END_DIALOGUE }
        }
        when (stage) {
            2 -> npcl(FacialExpression.ANNOYED, "This area is restricted, leave now!").also { stage = 3 }
            3 -> npcl(FacialExpression.ANGRY, "OUT! And don't come back!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return KhazardGuard255Dialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KHAZARD_GUARD_255)
    }
}