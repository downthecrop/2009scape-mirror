package rs09.game.content.dialogue.region.rellekka

import api.isQuestComplete
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.tools.END_DIALOGUE

/**
 * @author qmqz
 */

@Initializable
class BjornAndEldgrimDialogues(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, "Fremennik Trials")) {
            player(FacialExpression.FRIENDLY, "Hello there.").also { stage = 0 }
        } else if (isQuestComplete(player, "Fremennik Trials")) {
            npc(FacialExpression.DRUNK, "Hey! Itsh you again! Whatshyerfashe!").also { stage = 10 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npcl(FacialExpression.DRUNK, "Hey! scheck it out! Itsh an outerlandub! Yer shud go shpeak to the chieftain!").also { stage++ }
            1 -> player(FacialExpression.ASKING, "The who?").also { stage++ }
            2 -> npcl(FacialExpression.DRUNK, "That guy over there by that stuff! (hic) Yeh, abshoultely! He's da bosh!").also { stage = END_DIALOGUE }

            10 -> player(FacialExpression.ASKING, "${player.name}?").also { stage++ }
            11 -> npcl(FacialExpression.DRUNK, "Nah nah nah, not them, the other one, whatshyerfashe!").also { stage++ }
            12 -> player(FacialExpression.ASKING, "${player.getAttribute("fremennikname","fremmyname")}?").also { stage++ }
            13 -> npc(FacialExpression.DRUNK, "Thatsh what I said diddle I?").also { stage++ }
            14 -> player(FacialExpression.ASKING, "Um.... okay. I'll leave you to your drinking.").also { stage++ }
            15 -> npc(FacialExpression.DRUNK, "Thanksh pal! You're alright!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return BjornAndEldgrimDialogues(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BJORN_1284, NPCs.ELDGRIM_1285)
    }
}
