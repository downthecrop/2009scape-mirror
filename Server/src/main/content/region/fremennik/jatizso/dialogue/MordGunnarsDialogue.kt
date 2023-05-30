package content.region.fremennik.jatizso.dialogue

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import content.region.fremennik.rellekka.handlers.RellekkaDestination
import content.region.fremennik.rellekka.handlers.RellekkaUtils
import core.tools.END_DIALOGUE
import core.api.*

@Initializable
class MordGunnarsDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return MordGunnarsDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if(npc.id == NPCs.MORD_GUNNARS_5481){
            npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Would you like to sail to Jatizso?")
        } else {
            npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Would you like to sail back to Rellekka?")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> options("Yes, please.", "No, thanks.").also { stage++ }
            1 -> when(buttonId){
                1 -> playerl(core.game.dialogue.FacialExpression.FRIENDLY, "Yes, please!").also { stage++ }
                2 -> playerl(core.game.dialogue.FacialExpression.FRIENDLY, "No, thank you.").also { stage = END_DIALOGUE }
            }

            2 -> {
                end()
                if (!hasRequirement(player, "Fremennik Trials"))
                    return true
                RellekkaUtils.sail(player, if(npc.id == NPCs.MORD_GUNNARS_5481) RellekkaDestination.RELLEKKA_TO_JATIZSO else RellekkaDestination.JATIZSO_TO_RELLEKKA)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MORD_GUNNARS_5482, NPCs.MORD_GUNNARS_5481)
    }

}
