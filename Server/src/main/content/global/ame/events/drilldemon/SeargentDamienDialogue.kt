package content.global.ame.events.drilldemon

import core.game.system.task.Pulse
import core.game.dialogue.DialogueFile
import core.game.system.timer.impl.AntiMacro
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE

class SeargentDamienDialogue(val isCorrect: Boolean = false) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        var correctAmt = player!!.getAttribute(DrillDemonUtils.DD_CORRECT_COUNTER,0)
        if(correctAmt == 4 && AntiMacro.getEventNpc(player!!) == null) {
            when(stage){
                0 -> npc(core.game.dialogue.FacialExpression.OLD_NORMAL,"My god you actually did it, you limp","wristed worm-bodied MAGGOT! Take this","and get out of my sight.").also { stage++ }
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
        } else if(AntiMacro.getEventNpc(player!!) == null){
            when(stage){
                START_DIALOGUE -> if(isCorrect) npc(core.game.dialogue.FacialExpression.OLD_NORMAL,"Good! Now...").also { stage++ } else npc(core.game.dialogue.FacialExpression.OLD_ANGRY1,"WRONG, MAGGOT!").also { stage++ }
                1 -> {
                    end()
                    DrillDemonUtils.changeSignsAndAssignTask(player!!)
                }
            }
        } else {
            when(stage){
                START_DIALOGUE -> npc(core.game.dialogue.FacialExpression.OLD_NORMAL,"Would you like to come work out?").also { stage++ }
                1 -> options("Yes, please.", "No, thanks.").also { stage++ }
                2 -> when(buttonID){
                    1 -> {
                        end()
                        DrillDemonUtils.teleport(player!!)
                        AntiMacro.terminateEventNpc(player!!)
                        stage = END_DIALOGUE
                    }
                    2 -> {
                        end()
                        AntiMacro.terminateEventNpc(player!!)
                        stage = END_DIALOGUE
                    }
                }
            }
        }
    }
}