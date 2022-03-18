package rs09.game.content.dialogue.region.rellekka

import api.questStage
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import rs09.game.content.quest.members.thefremenniktrials.CouncilWorkerFTDialogue

@Initializable
class CouncilWorkerDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if(questStage(player, "Fremennik Trials") in 1..99){
            loadFile(CouncilWorkerFTDialogue(1))
        } else {
            npc(FacialExpression.FRIENDLY,"'Ello there.").also { stage = 0 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return CouncilWorkerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(1287)
    }

}