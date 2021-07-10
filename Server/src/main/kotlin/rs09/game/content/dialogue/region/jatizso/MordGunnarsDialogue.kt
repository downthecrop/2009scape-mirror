package rs09.game.content.dialogue.region.jatizso

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.game.util.region.rellekka.RellekkaDestination
import rs09.game.util.region.rellekka.RellekkaUtils
import rs09.tools.END_DIALOGUE

@Initializable
class MordGunnarsDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return MordGunnarsDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if(npc.id == NPCs.MORD_GUNNARS_5481){
            npcl(FacialExpression.FRIENDLY, "Would you like to sail to Jatizso?")
        } else {
            npcl(FacialExpression.FRIENDLY, "Would you like to sail back to Rellekka?")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> options("Yes, please.", "No, thanks.").also { stage++ }
            1 -> when(buttonId){
                1 -> playerl(FacialExpression.FRIENDLY, "Yes, please!").also { stage++ }
                2 -> playerl(FacialExpression.FRIENDLY, "No, thank you.").also { stage = END_DIALOGUE }
            }

            2 -> {
                end()
                RellekkaUtils.sail(player, if(npc.id == NPCs.MORD_GUNNARS_5481) RellekkaDestination.RELLEKKA_TO_JATIZSO else RellekkaDestination.JATIZSO_TO_RELLEKKA)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MORD_GUNNARS_5482, NPCs.MORD_GUNNARS_5481)
    }

}