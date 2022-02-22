package rs09.game.content.dialogue.region.isafdar

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 */

@Initializable
class TyrasGuard(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FacialExpression.FRIENDLY,"What is it?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> options("What's going on around here?", "Do you know what's south of here?", "I'll leave you alone.").also { stage++ }
            1 -> when (buttonId) {
                1 -> player(FacialExpression.ASKING, "What's going on around here?").also { stage = 10 }
                2 -> player(FacialExpression.ASKING, "Do you know what's south of here?").also { stage = 20 }
                3 -> player(FacialExpression.NEUTRAL, "I'll leave you alone.").also { stage = 99 }
            }

            10 -> npcl(FacialExpression.NEUTRAL, "Sorry, I shouldn't give out sensitive information to civilians. You should go to General Hining, in the camp along the road.").also { stage++ }
            11 -> options("Do you know what's south of here?", "Okay, thanks.").also { stage++ }
            12 -> when (buttonId) {
                1 -> player(FacialExpression.ASKING, "Do you know what's south of here?").also { stage = 20 }
                2 -> player(FacialExpression.NEUTRAL, "Ok, thanks.").also { stage = 99 }
            }

            20 -> npcl(FacialExpression.NEUTRAL, "No. We sent a scouting party in that direction, when we first established our camp. Some of the men got lost in the swamps. Eventually we listed them as dead.").also { stage++ }
            21 -> npcl(FacialExpression.NEUTRAL, "Then suddenly they returned, with a wild gleam in their eyes, raving about gods and snakes and all kinds of madness.").also { stage++ }
            22 -> npcl(FacialExpression.NEUTRAL, "We had to drive them out, in case their condition infected the rest of the troops. Their wives will be given a full widow pension when we return home.").also { stage++ }
            23 -> npcl(FacialExpression.NEUTRAL, "General Hining concluded that, whatever is down there, it's not affiliated with any of the elf factions, and it should be left alone.").also { stage++ }
            24 -> player(FacialExpression.FRIENDLY, "Thank you.").also { stage = 0 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return TyrasGuard(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TYRAS_GUARD_1203)
    }
}
