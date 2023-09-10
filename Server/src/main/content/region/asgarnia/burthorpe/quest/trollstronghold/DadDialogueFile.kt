package content.region.asgarnia.burthorpe.quest.trollstronghold

import core.api.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE

class DadDialogueFile(private val dialogueNum: Int = 0) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when(dialogueNum) {
            1 -> when (stage) {
                START_DIALOGUE -> npcl(FacialExpression.OLD_HAPPY, "No human pass through arena without defeating Dad!").also {
                    stage = END_DIALOGUE
                    setQuestStage(player!!, TrollStronghold.questName, 3)
                }
            }
            2 -> when (stage) {
                START_DIALOGUE -> npcl(FacialExpression.OLD_NORMAL, "Tiny human brave. Dad squish!").also { stage++ }
                1 -> npc!!.attack(player).also {
                    npc!!.skills.lifepoints = npc!!.skills.maximumLifepoints // Reset dad to max hitpoints.
                    setQuestStage(player!!, TrollStronghold.questName, 4)
                    stage = END_DIALOGUE
                }
            }
            3 -> when (stage) {
                START_DIALOGUE -> npcl(FacialExpression.OLD_NORMAL, "Stop! You win. Not hurt Dad.").also { stage++ }
                1 -> showTopics(
                        Topic(FacialExpression.FRIENDLY, "I'll be going now.", END_DIALOGUE),
                        Topic(FacialExpression.ANGRY_WITH_SMILE, "I'm not done yet! Prepare to die!", 2)
                )
                2 -> player!!.attack(npc).also {
                    setQuestStage(player!!, TrollStronghold.questName, 5)
                    stage = END_DIALOGUE
                }
            }
        }
    }
}