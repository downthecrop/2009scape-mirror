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
class KhazardGuard256Dialogue(player: Player? = null) : DialoguePlugin(player) {

    // Khazard Guard - Chest room.
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (isEquipped(player!!, Items.KHAZARD_HELMET_74) && isEquipped(player!!, Items.KHAZARD_ARMOUR_75)) {
            playerl(FacialExpression.FRIENDLY, "Hello.").also { stage = 0 }
        } else {
            playerl(FacialExpression.FRIENDLY, "Hi.").also { stage = 3 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FacialExpression.ANGRY, "Despicable thieving scum, that was good armour. Did you see anyone around here soldier?").also { stage = 1 }
            1 -> playerl(FacialExpression.SILENT, "Me? No, no one!").also { stage = 2 }
            2 -> npcl(FacialExpression.SUSPICIOUS, "Hmmmm").also { stage = END_DIALOGUE }
        }
        when (stage) {
            3 -> npcl(FacialExpression.ANNOYED, "I don't know who you are. Get out of my house stranger.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return KhazardGuard256Dialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KHAZARD_GUARD_256)
    }
}