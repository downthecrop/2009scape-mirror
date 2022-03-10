package rs09.game.content.dialogue.region.miscellania

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
class MiscCitizenGoodDayDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FacialExpression.FRIENDLY,"Hello.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> {
                npc(FacialExpression.FRIENDLY, "Good day, Your Royal Highness.").also { stage = 99 }
            }
            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return MiscCitizenGoodDayDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALRIK_1381, NPCs.RUNOLF_3924,
            NPCs.BRODDI_1390, NPCs.EINAR_1380, NPCs.INGRID_3926,
            NPCs.RAGNAR_1379, NPCs.RAGNVALD_1392, NPCs.RANNVEIG_1386,
            NPCs.THORA_3927, NPCs.THORA_1387, NPCs.THORHILD_1382,
            NPCs.VALGERD_1388, NPCs.TJORVI_3925
        )
    }
}