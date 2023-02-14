package content.global.activity.jobs

import content.global.activity.jobs.impl.BoneBuryingJobs
import content.global.activity.jobs.impl.CombatJobs
import content.global.activity.jobs.impl.ProductionJobs
import core.api.addItem
import core.api.amountInInventory
import core.api.hasAnItem
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import core.tools.StringUtils
import org.rs09.consts.Items

/**
 * The dialogue file to handle a player asking about work with the employer that assigned them
 * their current job.
 */
class CheckJobDialogueFile : DialogueFile() {
    companion object {
        const val TURN_IN_ITEMS_1 = 1
        const val TURN_IN_ITEMS_2 = 2
        const val TURN_IN_ITEMS_3 = 3
        const val TURN_IN_ITEMS_4 = 4
        const val TURN_IN_ITEMS_5 = 5
        const val REMIND_JOB = 101
        const val GET_TASK_LIST = 201
        const val GET_NEW_JOB_1 = 301
        const val GET_NEW_JOB_2 = 302
        const val GET_NEW_JOB_3 = 303
        const val GET_NEW_JOB_4 = 304
        const val GET_NEW_JOB_5 = 305
        const val GET_NEW_JOB_NONE = 306
        const val GET_NEW_JOB_REACHED_LIMIT = 307
    }

    override fun handle(componentID: Int, buttonID: Int) {
        val playerJobManager: JobManager = JobManager.getInstance(player!!)
        val job = playerJobManager.job ?: return
        val originalAmount = playerJobManager.jobOriginalAmount

        when (stage) {
            START_DIALOGUE -> showTopics(
                IfTopic(
                    FacialExpression.HAPPY,
                    "I have the items you wanted.",
                    TURN_IN_ITEMS_1,
                    (job.type == JobType.PRODUCTION && amountInInventory(player!!, (job as ProductionJobs).itemId) > 0)
                ), Topic(
                    FacialExpression.HALF_ASKING, "Can you remind me what job you gave me?", REMIND_JOB
                ), Topic(
                    FacialExpression.HALF_ASKING, "Can I have a task list to remind me of my job?", GET_TASK_LIST
                ), Topic(
                    FacialExpression.HALF_GUILTY, "I'd like a new job please.", GET_NEW_JOB_1
                )
            )

            TURN_IN_ITEMS_1 -> {
                job as ProductionJobs
                val itemName = Item(job.itemId).name.lowercase()
                val itemNamePluralized = if (playerJobManager.jobAmount > 1) StringUtils.plusS(itemName) else itemName
                npcl("Splendid! Now, I needed $originalAmount $itemNamePluralized.")
                stage = TURN_IN_ITEMS_2
            }

            TURN_IN_ITEMS_2 -> {
                if (playerJobManager.jobAmount == originalAmount) {
                    npcl("So far, you haven't brought me any, so I'm waiting for ${playerJobManager.jobAmount}.")
                } else {
                    npcl("So far, you have brought me ${originalAmount - playerJobManager.jobAmount}, so I'm waiting for ${playerJobManager.jobAmount}.")
                }
                stage = TURN_IN_ITEMS_3
            }

            TURN_IN_ITEMS_3 -> {
                if (amountInInventory(player!!, (job as ProductionJobs).itemId) >= playerJobManager.jobAmount) {
                    npcl("I can see that you have plenty, would you like to hand some over to complete your job?")
                } else {
                    npcl("I can see that you have some, would you like to hand them over?")
                }
                stage = TURN_IN_ITEMS_4
            }

            TURN_IN_ITEMS_4 -> showTopics(
                Topic("Yes, I'll give you what I have.", TURN_IN_ITEMS_5, skipPlayer = true),
                Topic("No, I'll keep hold of it for now.", END_DIALOGUE, skipPlayer = true)
            )

            TURN_IN_ITEMS_5 -> {
                playerJobManager.turnInItems()

                if (playerJobManager.jobAmount == 0) {
                    playerJobManager.rewardPlayer()
                    npcl("There you go, thanks for your help. You're quite a skilled worker!")
                } else {
                    npcl("Thanks for your help. Come back when you've got more!")
                }
                stage = END_DIALOGUE
            }

            REMIND_JOB -> {
                when (job.type) {
                    JobType.PRODUCTION -> {
                        job as ProductionJobs
                        val itemName = Item(job.itemId).name.lowercase()
                        val itemNamePluralized =
                            if (playerJobManager.jobAmount > 1) StringUtils.plusS(itemName) else itemName
                        npcl("You still need to gather ${playerJobManager.jobAmount} more $itemNamePluralized.")
                    }

                    JobType.BONE_BURYING -> {
                        job as BoneBuryingJobs
                        val boneName = Item(job.boneIds.first()).name.lowercase()
                        val boneNamePluralized =
                            if (playerJobManager.jobAmount > 1) StringUtils.plusS(boneName) else boneName
                        npcl("You still need to bury ${playerJobManager.jobAmount} more $boneNamePluralized in the ${job.buryArea.title}.")
                    }

                    JobType.COMBAT -> {
                        job as CombatJobs
                        val monsterName = NPC(job.monsterIds.first()).name.lowercase()
                        val monsterNamePluralized =
                            if (playerJobManager.jobAmount > 1) StringUtils.plusS(monsterName) else monsterName
                        npcl("You still need to kill ${playerJobManager.jobAmount} more $monsterNamePluralized.")
                    }
                }
                stage = END_DIALOGUE
            }

            GET_TASK_LIST -> {
                if (hasAnItem(player!!, Items.TASK_LIST_13464).exists()) {
                    npcl("You already have one of those!")
                } else {
                    when (addItem(player!!, Items.TASK_LIST_13464)) {
                        true -> npcl("Here you go then.")
                        false -> dialogue("You don't have enough space in your inventory.")
                    }
                }

                stage = END_DIALOGUE
            }

            GET_NEW_JOB_1 -> npcl("Let me see if I've got any work for you.").also {
                if (playerJobManager.hasReachedJobLimit()) {
                    stage = GET_NEW_JOB_REACHED_LIMIT
                } else {
                    playerJobManager.generate(npc!!)
                    stage = if (playerJobManager.hasJob()) GET_NEW_JOB_2 else GET_NEW_JOB_NONE
                }
            }

            GET_NEW_JOB_2 -> npcl(playerJobManager.getAssignmentMessage()).also { stage = GET_NEW_JOB_3 }
            // Historically, the player would also have the option to ask for an easier task
            GET_NEW_JOB_3 -> playerl("Okay, off I go!").also { stage = GET_NEW_JOB_4 }
            GET_NEW_JOB_4 -> npcl("There, I've updated your task list to show your new job. Best of luck!").also {
                stage = GET_NEW_JOB_5
            }

            GET_NEW_JOB_5 -> playerl("Thanks.").also { stage = END_DIALOGUE }
            GET_NEW_JOB_NONE -> npcl("I'm sorry, I don't currently have any jobs that you're qualified for.").also {
                stage = END_DIALOGUE
            }

            GET_NEW_JOB_REACHED_LIMIT -> npcl("You've hit your limit for the day. Come back tomorrow!").also {
                stage = END_DIALOGUE
            }
        }
    }
}