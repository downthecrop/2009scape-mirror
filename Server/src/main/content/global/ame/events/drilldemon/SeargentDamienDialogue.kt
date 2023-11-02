package content.global.ame.events.drilldemon

import core.api.getAttribute
import core.api.sendItemDialogue
import core.api.unlock
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class SeargentDamienDialogue(var isCorrect: Boolean = false, var eventStart: Boolean = false) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.SERGEANT_DAMIEN_2790)
        val correctCounter = player!!.getAttribute(DrillDemonUtils.DD_CORRECT_COUNTER,0)
        when(stage) {
            0 -> {
                if (correctCounter >= 4) {
                    npcl(FacialExpression.OLD_NORMAL, "Well I'll be, you actually did it private. Now take this and get yourself out of my sight.")
                    stage = 100
                } else if (eventStart) {
                    npcl(FacialExpression.OLD_NORMAL, "Move yourself private! Follow my orders and you may, just may, leave here in a fit state for my corps!")
                    DrillDemonUtils.changeSignsAndAssignTask(player!!)
                    stage = 0
                    eventStart = false
                } else {
                    npcl(FacialExpression.OLD_NORMAL, when (getAttribute(player!!, DrillDemonUtils.DD_KEY_TASK, -1)) {
                        DrillDemonUtils.DD_SIGN_JOG -> if (!isCorrect) "Wrong exercise, worm! Get yourself over there and jog on that mat private!" else "Get yourself over there and jog on that mat private!"
                        DrillDemonUtils.DD_SIGN_JUMP -> if (!isCorrect) "Wrong exercise, worm! I want to see you on that mat doing star jumps private!" else "I want to see you on that mat doing star jumps private!"
                        DrillDemonUtils.DD_SIGN_PUSHUP -> if (!isCorrect) "Wrong exercise, worm! Drop and give me push ups on that mat private!" else "Drop and give me push ups on that mat private!"
                        DrillDemonUtils.DD_SIGN_SITUP -> if (!isCorrect) "Wrong exercise, worm! Get on that mat and give me sit ups private!" else "Get on that mat and give me sit ups private!"
                        else -> "REEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE"
                        })
                    stage = 1
                    unlock(player!!)
                }
                }
            1 -> {
                when (getAttribute(player!!, DrillDemonUtils.DD_KEY_TASK, -1)) {
                DrillDemonUtils.DD_SIGN_JOG -> sendItemDialogue(player!!, Items.RUN_10947, "Go to this mat and jog on the spot!")
                DrillDemonUtils.DD_SIGN_JUMP -> sendItemDialogue(player!!, Items.STARJUMP_10949, "Go to this mat and do some starjumps!")
                DrillDemonUtils.DD_SIGN_PUSHUP -> sendItemDialogue(player!!, Items.PUSHUP_10946, "Go to this mat and do some pushups!")
                DrillDemonUtils.DD_SIGN_SITUP -> sendItemDialogue(player!!, Items.SITUP_10948, "Go to this mat and do some sit ups!")
            }
                stage = END_DIALOGUE
            }
            100 -> {
                end()
                DrillDemonUtils.cleanup(player!!)
                DrillDemonUtils.reward(player!!)
            }
        }
    }
}