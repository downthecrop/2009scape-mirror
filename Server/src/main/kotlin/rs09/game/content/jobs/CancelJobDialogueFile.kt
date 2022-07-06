package rs09.game.content.jobs

import api.*
import rs09.game.content.jobs.impl.GatheringJobs
import rs09.game.content.jobs.impl.SlayingJobs
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import org.rs09.consts.Items
import rs09.game.content.dialogue.DialogueFile
import rs09.game.content.dialogue.Topic
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

class CancelJobDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            START_DIALOGUE -> showTopics(
                Topic(FacialExpression.HALF_ASKING, "How am I doing on my job?", 1),
                Topic(FacialExpression.HALF_GUILTY, "I'd like to cancel my job.", 10)
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
                Topic(FacialExpression.HAPPY, "Yes, please.", 20),
                Topic(FacialExpression.NEUTRAL, "No, thanks.", 30)
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
                    FacialExpression.HALF_WORRIED,
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