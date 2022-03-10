package rs09.game.content.dialogue.region.rellekka

import core.game.content.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.plugin.Initializable
import rs09.game.content.quest.members.thefremenniktrials.CouncilWorkerFTDialogue

@Initializable
class CouncilWorkerDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun open(vararg args: Any?): Boolean {
        if(player.questRepository.getStage("Fremennik Trials") in 1..99){
            loadFile(CouncilWorkerFTDialogue(1))
        }
        npc("'Ello there.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return CouncilWorkerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(1287)
    }

}