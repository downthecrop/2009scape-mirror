package rs09.game.content.dialogue.region.rellekka

import api.addItem
import api.questStage
import api.sendItemDialogue
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.AchievementDiary
import core.game.node.entity.player.link.diary.DiaryType
import core.plugin.Initializable
import org.rs09.consts.Items;
import rs09.game.content.quest.members.thefremenniktrials.CouncilWorkerFTDialogue
import rs09.game.content.dialogue.region.rellekka.CouncilWorkerDiaryDialogue

@Initializable
class CouncilWorkerDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if(questStage(player, "Fremennik Trials") in 1..99){
            player.dialogueInterpreter.open((CouncilWorkerFTDialogue(1)))
        }
        else if(player.achievementDiaryManager.getDiary(DiaryType.FREMENNIK).isComplete(0, true)){
            player.dialogueInterpreter.open((CouncilWorkerDiaryDialogue()))
        }
        else{
            player(FacialExpression.FRIENDLY,"Hello.")
            npc(FacialExpression.FRIENDLY,"How do. You planning on crossing this here bridge and heading up to Rellekka then?").also { stage = 0 }
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