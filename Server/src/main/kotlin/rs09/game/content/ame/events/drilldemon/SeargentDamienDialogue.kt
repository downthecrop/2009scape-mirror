package rs09.game.content.ame.events.drilldemon

import core.game.content.dialogue.FacialExpression
import core.game.system.task.Pulse
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

class SeargentDamienDialogue(val isCorrect: Boolean = false) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        var correctAmt = player!!.getAttribute(DrillDemonUtils.DD_CORRECT_COUNTER,0)
        if(correctAmt == 4 && player!!.antiMacroHandler.event == null) {
            when(stage){
                0 -> npc(FacialExpression.OLD_NORMAL,"My god you actually did it, you limp","wristed worm-bodied MAGGOT! Take this","and get out of my sight.").also { stage++ }
                1 -> {
                    end()
                    player!!.unlock()
                    DrillDemonUtils.cleanup(player!!)
                    player!!.pulseManager.run(object : Pulse(2){
                        override fun pulse(): Boolean {
                            DrillDemonUtils.reward(player!!)
                            return true
                        }
                    })
                }
            }
        } else if(player!!.antiMacroHandler.event == null){
            when(stage){
                START_DIALOGUE -> if(isCorrect) npc(FacialExpression.OLD_NORMAL,"Good! Now...").also { stage++ } else npc(FacialExpression.OLD_ANGRY1,"WRONG, MAGGOT!").also { stage++ }
                1 -> {
                    end()
                    DrillDemonUtils.changeSignsAndAssignTask(player!!)
                }
            }
        } else {
            when(stage){
                START_DIALOGUE -> npc(FacialExpression.OLD_NORMAL,"Would you like to come work out?").also { stage++ }
                1 -> options("Yes, please.", "No, thanks.").also { stage++ }
                2 -> when(buttonID){
                    1 -> {
                        end()
                        DrillDemonUtils.teleport(player!!)
                        player!!.antiMacroHandler.event?.terminate()
                        stage = END_DIALOGUE
                    }
                    2 -> {
                        end()
                        player!!.antiMacroHandler.event?.terminate()
                        stage = END_DIALOGUE
                    }
                }
            }
        }
    }
}