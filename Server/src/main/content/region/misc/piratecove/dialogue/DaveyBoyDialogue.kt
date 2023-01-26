package content.region.misc.piratecove.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 */

@Initializable
class DaveyBoyDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when ((1..2).random()) {
            1 -> playerl(core.game.dialogue.FacialExpression.HALF_ASKING, "What does it take to become first mate on a ship?").also { stage = 0 }
            2 -> npcl(core.game.dialogue.FacialExpression.ANNOYED, "It is customary when stowing away on a vessel to not introduce yourself to the Captains First Mate, oh foolish one.").also { stage = 10 }
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npcl(core.game.dialogue.FacialExpression.LAUGH, "Good question. We have a diplomatic consession at the turn of the financial year. Said pirate is chosen should the existing mate be absent without leave.").also { stage++ }
            1 -> playerl(core.game.dialogue.FacialExpression.THINKING, "I had no idea. I always figured it was all about popularity.").also { stage++ }
            2 -> npc(core.game.dialogue.FacialExpression.FRIENDLY, "It is. I'm just pulling your leg.").also { stage++ }
            3 -> player(core.game.dialogue.FacialExpression.FRIENDLY,"Oh....").also { stage = 99 }

            10 -> player(core.game.dialogue.FacialExpression.ANNOYED, "Hey! I'm not a stowaway!").also { stage++ }
            11 -> player(core.game.dialogue.FacialExpression.ANNOYED, "That Lokar guy invited me aboard...").also { stage++ }
            12 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "I see. Well, don't distract me as I'm making preparations for departure.").also { stage++ }
            13 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Try not to distract any of the crew either, Zamorak knows it's hard enough to get them to do any work around here without strangers wandering round the ship asking them inane questions.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return DaveyBoyDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FIRST_MATE_DAVEY_BOY_4543)
    }
}
