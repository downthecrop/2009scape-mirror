package content.global.activity.jobs

import content.global.activity.jobs.impl.BoneBuryingJobs
import content.global.activity.jobs.impl.CombatJobs
import content.global.activity.jobs.impl.ProductionJobs
import core.api.addItem
import core.api.hasAnItem
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import core.tools.StringUtils
import org.rs09.consts.Items

/**
 * The dialogue file to handle a player asking about work with any employer which they aren't currently working for.
 */
class StartJobDialogueFile : DialogueFile() {
    companion object {
        const val GET_TASK_LIST_1 = 1
        const val GET_TASK_LIST_2 = 2
        const val GET_TASK_LIST_YES = 3
        const val GET_JOB_1 = 101
        const val GET_JOB_2 = 102
        const val GET_JOB_3 = 103
        const val GET_JOB_4 = 104
        const val GET_JOB_5 = 105
        const val GET_JOB_NONE = 201
        const val GET_JOB_EMPLOYED_1 = 301
        const val GET_JOB_EMPLOYED_2 = 302
        const val GET_JOB_LIMIT_REACHED = 401
    }

    override fun handle(componentID: Int, buttonID: Int) {
        val playerJobManager: JobManager = JobManager.getInstance(player!!)

        when (stage) {
            START_DIALOGUE -> playerl(FacialExpression.ASKING, "Do you have any jobs for me?").also {
                stage = if (hasAnItem(player!!, Items.TASK_LIST_13464).exists()) {
                    if (playerJobManager.hasJob()) {
                        GET_JOB_EMPLOYED_1
                    } else {
                        GET_JOB_1
                    }
                } else {
                    GET_TASK_LIST_1
                }
            }

            GET_TASK_LIST_1 -> npcl("Well, first of all, I can see that you don't have a task list to remind you about your current job. Would you like a task list now?").also {
                stage = GET_TASK_LIST_2
            }

            GET_TASK_LIST_2 -> showTopics(
                Topic("Yes, please.", GET_TASK_LIST_YES),
                Topic("No, thanks.", if (playerJobManager.hasJob()) GET_JOB_EMPLOYED_1 else GET_JOB_1)
            )

            GET_TASK_LIST_YES -> {
                when (addItem(player!!, Items.TASK_LIST_13464)) {
                    true -> npcl("Here you go then.").also {
                        stage = if (playerJobManager.hasJob()) GET_JOB_EMPLOYED_1 else GET_JOB_1
                    }

                    false -> dialogue("You don't have enough space in your inventory.").also { stage = END_DIALOGUE }
                }
            }

            GET_JOB_1 -> npcl("Let me see if I've got any work for you.").also {
                if (playerJobManager.hasReachedJobLimit()) {
                    stage = GET_JOB_LIMIT_REACHED
                } else {
                    playerJobManager.generate(npc!!)
                    stage = if (playerJobManager.hasJob()) GET_JOB_2 else GET_JOB_NONE
                }
            }

            GET_JOB_2 -> npcl(playerJobManager.getAssignmentMessage()).also { stage = GET_JOB_3 }
            // Historically, the player would also have the option to ask for an easier task
            GET_JOB_3 -> playerl("Okay, off I go!").also { stage = GET_JOB_4 }
            GET_JOB_4 -> npcl("There, I've updated your task list to show your new job. Best of luck!").also {
                stage = GET_JOB_5
            }

            GET_JOB_5 -> playerl("Thanks.").also { stage = END_DIALOGUE }
            GET_JOB_EMPLOYED_1 -> {
                val job = playerJobManager.job ?: return
                val employerName = NPC(job.employer.npcId).name
                val amount = playerJobManager.jobAmount
                val originalAmount = playerJobManager.jobOriginalAmount

                if (amount == 0) {
                    npcl("Hang about. Aren't you working for $employerName? It looks like you've complete their job, return to them for your reward.")
                    stage = END_DIALOGUE
                } else {
                    when (job.type) {
                        JobType.PRODUCTION -> {
                            job as ProductionJobs
                            val itemName = Item(job.itemId).name.lowercase()
                            val itemNamePluralized = if (originalAmount > 1) StringUtils.plusS(itemName) else itemName
                            npcl("Hang about. Aren't you working for $employerName? You were asked for $originalAmount $itemNamePluralized and still have $amount more to go.")
                        }

                        JobType.BONE_BURYING -> {
                            job as BoneBuryingJobs
                            val boneName = Item(job.boneIds.first()).name.lowercase()
                            val boneNamePluralized = if (originalAmount > 1) StringUtils.plusS(boneName) else boneName
                            npcl("Hang about. Aren't you working for $employerName? You were asked to bury $originalAmount $boneNamePluralized in the ${job.buryArea.title} and still have $amount more to go.")
                        }

                        JobType.COMBAT -> {
                            job as CombatJobs
                            val monsterName = NPC(job.monsterIds.first()).name.lowercase()
                            val monsterNamePluralized = if (originalAmount > 1) StringUtils.plusS(monsterName) else monsterName
                            npcl("Hang about. Aren't you working for $employerName? You were asked to kill $originalAmount $monsterNamePluralized and still have $amount more to go.")
                        }
                    }
                    stage = GET_JOB_EMPLOYED_2
                }
            }

            GET_JOB_EMPLOYED_2 -> showTopics(
                Topic("Oh yes. I better go and finish their job first.", END_DIALOGUE),
                Topic("I'd like a new job instead, please.", GET_JOB_1)
            )

            GET_JOB_NONE -> npcl("I'm sorry, I don't currently have any jobs that you're qualified for.").also {
                stage = END_DIALOGUE
            }

            GET_JOB_LIMIT_REACHED -> npcl("You've hit your limit for the day. Come back tomorrow!").also {
                stage = END_DIALOGUE
            }
        }
    }
}