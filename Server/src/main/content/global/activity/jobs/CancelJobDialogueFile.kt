package content.global.activity.jobs

import core.api.*
import content.global.activity.jobs.impl.GatheringJobs
import content.global.activity.jobs.impl.SlayingJobs
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import org.rs09.consts.Items
import core.game.dialogue.DialogueFile
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE

class CancelJobDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            START_DIALOGUE -> showTopics(
                Topic(core.game.dialogue.FacialExpression.HALF_ASKING, "How am I doing on my job?", 1),
                Topic(core.game.dialogue.FacialExpression.HALF_GUILTY, "I'd like to cancel my job.", 10)
            )

            1 -> {
                val jobType = JobType.values()[getAttribute(player!!, "jobs:type", 0)]
                val jobId = getAttribute(player!!, "jobs:id", 0)
                val needed = getAttribute(player!!, "jobs:amount", 0)

                when (jobType) {
                    JobType.GATHERING -> {
                        sendItemDialogue(
                            player!!,
                            Item(GatheringJobs.values()[jobId].itemId),
                            "You still need to gather $needed more."
                        )
                    }

                    JobType.SLAYING -> {
                        sendDialogue(
                            player!!,
                            "You still need to kill $needed more ${NPC(SlayingJobs.values()[jobId].ids[0]).name.lowercase()}"
                        )
                    }
                }

                stage = END_DIALOGUE
            }

            10 -> npc("It will cost 500 coins.")
                  .also { stage++ }

            11 -> showTopics(
                Topic(core.game.dialogue.FacialExpression.HAPPY, "Yes, please.", 20),
                Topic(core.game.dialogue.FacialExpression.NEUTRAL, "No, thanks.", 30)
            )

            20 -> npc("Alright then, hand over the money.")
                  .also { stage++ }

            21 -> if (inInventory(player!!, Items.COINS_995, 500)) {
                player("Here you go.")

                removeItem(player!!, Item(Items.COINS_995, 500))
                removeAttribute(player!!, "jobs:id")
                removeAttribute(player!!, "jobs:amount")
                removeAttribute(player!!, "jobs:original_amount")
                removeAttribute(player!!, "jobs:type")

                stage = END_DIALOGUE
            } else {
                player(
                    core.game.dialogue.FacialExpression.HALF_WORRIED,
                    "Oh, I don't seem to have the money..."
                ).also { stage++ }
            }

            22 -> npc("Ah, that sucks! Get to work.")
                  .also { stage = END_DIALOGUE }

            30 -> npc("Alright then, get to work!")
                  .also { stage = END_DIALOGUE }
        }
    }
}