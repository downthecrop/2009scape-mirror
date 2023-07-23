package content.region.asgarnia.burthorpe.quest.deathplateau

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.tools.END_DIALOGUE
import org.rs09.consts.Items

class IOUNoteDialogueFile : DialogueFile() {
    var a = 0
    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, DeathPlateau.questName)) {
            in 15..16 -> {
                when (stage) {
                    0 -> player(FacialExpression.NEUTRAL, "The IOU says that Harold owes me some money.").also { stage++ }
                    1 -> player(FacialExpression.EXTREMELY_SHOCKED, "Wait just a minute!").also { stage++ }
                    2 -> playerl(FacialExpression.EXTREMELY_SHOCKED, "The IOU is written on the back of the combination! The stupid guard had it in his back pocket all the time!").also { stage++ }
                    3 -> {
                        if (removeItem(player!!, Items.IOU_3103)) {
                            addItemOrDrop(player!!, Items.COMBINATION_3102)
                            setQuestStage(player!!, DeathPlateau.questName, 16)
                            sendItemDialogue(player!!, Items.COMBINATION_3102, "You have found the combination!").also { stage++ }
                        }
                    }
                    4 -> {
                        end()
                        stage = END_DIALOGUE
                        openInterface(player!!, 220)
                        setInterfaceText(player!!, "<col=3D1E00>Red is North of Blue. Yellow is South of Purple.", 220, 7)
                        setInterfaceText(player!!, "<col=3D1E00>Green is North of Purple. Blue is West of", 220, 8)
                        setInterfaceText(player!!, "<col=3D1E00>Yellow. Purple is East of Red.", 220, 9)
                    }
                }
            }
        }
    }
}