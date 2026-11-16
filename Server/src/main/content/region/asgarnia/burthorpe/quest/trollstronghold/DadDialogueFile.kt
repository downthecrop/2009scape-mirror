package content.region.asgarnia.burthorpe.quest.trollstronghold

import content.data.Quests
import core.api.getQuestStage
import core.api.queueScript
import core.api.setAttribute
import core.api.setQuestStage
import core.api.stopExecuting
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE

class DadDialogueFile(private val dialogueNum: Int = 0) : DialogueFile() {
    private fun incrementQuestStage(player: Player) {
        if (getQuestStage(player, Quests.TROLL_STRONGHOLD) == 4) {
            setQuestStage(player, Quests.TROLL_STRONGHOLD, 5)
        }
    }

    override fun handle(componentID: Int, buttonID: Int) {
        when (dialogueNum) {
            1 -> when (stage) {
                START_DIALOGUE -> npcl(FacialExpression.OLD_HAPPY, "No human pass through arena without defeating Dad!").also {
                    stage = END_DIALOGUE
                    setQuestStage(player!!, Quests.TROLL_STRONGHOLD, 3)
                }
            }
            2 -> when (stage) {
                START_DIALOGUE -> npcl(FacialExpression.OLD_NORMAL, "Tiny human brave. Dad squish!").also { stage++ }
                1 -> npc!!.attack(player).also {
                    npc!!.skills.lifepoints = npc!!.skills.maximumLifepoints // Reset dad to max hitpoints.
                    setQuestStage(player!!, Quests.TROLL_STRONGHOLD, 4)
                    stage = END_DIALOGUE
                }
            }
            3 -> when (stage) {
                START_DIALOGUE -> npcl(FacialExpression.OLD_NORMAL, "Stop! You win. Not hurt Dad.").also { stage++ }
                1 -> showTopics(
                        Topic(FacialExpression.FRIENDLY, "I'll be going now.", 2),
                        Topic(FacialExpression.ANGRY_WITH_SMILE, "I'm not done yet! Prepare to die!", 3)
                )
                2 -> {
                    end()
                    incrementQuestStage(player!!)
                }
                3 -> {
                    end()
                    setAttribute(npc!!, "ruthless ${player!!.name}", true)
                    player!!.attack(npc)
                    incrementQuestStage(player!!)
                }
            }
        }
    }
}