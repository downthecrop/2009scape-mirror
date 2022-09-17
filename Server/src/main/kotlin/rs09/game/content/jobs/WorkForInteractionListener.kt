package rs09.game.content.jobs

import rs09.game.content.jobs.impl.GatheringJobs
import rs09.game.content.jobs.impl.SlayingJobs
import api.*
import api.events.EventHook
import api.events.JobAssignmentEvent
import api.events.NPCKillEvent
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.ServerStore.Companion.getInt
import rs09.game.Event
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType

/**
 * Handles the work-for actions for the NPCs
 * @author Ceikry
 * @author vddCore
 */
class WorkForInteractionListener : InteractionListener, LoginListener {
    companion object {
        private val WEAPON_ICONS = arrayListOf(
            Items.BRONZE_SCIMITAR_1321,
            Items.STEEL_SCIMITAR_1325,
            Items.RUNE_SCIMITAR_1333,
            Items.BRONZE_MACE_1422,
            Items.MITHRIL_WARHAMMER_1343
        )

        private val EMPLOYERS_AND_JOBS = mapOf(
            NPCs.HANS_0 to listOf(
                GatheringJobs.AIR_RUNE,
                GatheringJobs.COWHIDES,
                GatheringJobs.WATER_RUNE
            ),

            NPCs.AGGIE_922 to listOf(
                GatheringJobs.ASHES,
                GatheringJobs.AIR_RUNE,
                GatheringJobs.WATER_RUNE
            ),

            NPCs.GILLIE_GROATS_3807 to listOf(
                GatheringJobs.COWHIDES
            ),

            NPCs.COOKING_TUTOR_4899 to listOf(
                GatheringJobs.CAKE,
                GatheringJobs.ANCHOVY_PIZZA,
                GatheringJobs.COOKED_SALMON,
                GatheringJobs.COOKED_TROUT,
                GatheringJobs.MEAT_PIE,
                GatheringJobs.MEAT_PIZZA,
                GatheringJobs.PLAIN_PIZZA
            ),

            NPCs.FISHING_TUTOR_4901 to listOf(
                GatheringJobs.RAW_SALMON,
                GatheringJobs.RAW_SHRIMP,
                GatheringJobs.RAW_TROUT
            ),

            NPCs.MINING_TUTOR_4902 to listOf(
                GatheringJobs.COPPER_ORE,
                GatheringJobs.TIN_ORE,
                GatheringJobs.COAL,
                GatheringJobs.IRON_ORE,
                GatheringJobs.SILVER_ORE,
                GatheringJobs.GOLD_ORE
            ),

            NPCs.PRAYER_TUTOR_4903 to listOf(
                GatheringJobs.SILVER_ORE
            ),

            NPCs.SMELTING_TUTOR_4904 to listOf(
                GatheringJobs.BRONZE_BAR,
                GatheringJobs.IRON_BAR,
                GatheringJobs.STEEL_BAR
            ),

            NPCs.WOODCUTTING_TUTOR_4906 to listOf(
                GatheringJobs.LOG,
                GatheringJobs.OAK,
                GatheringJobs.WILLOW
            )
        )

        private val EMPLOYER_JOB_TYPES = mapOf(
            NPCs.HANS_0 to JobType.GATHERING,
            NPCs.MELEE_TUTOR_705 to JobType.SLAYING,
            NPCs.AGGIE_922 to JobType.GATHERING,
            NPCs.RANGED_TUTOR_1861 to JobType.SLAYING,
            NPCs.GILLIE_GROATS_3807 to JobType.GATHERING,
            NPCs.MAGIC_TUTOR_4707 to JobType.SLAYING,
            NPCs.COOKING_TUTOR_4899 to JobType.GATHERING,
            NPCs.FISHING_TUTOR_4901 to JobType.GATHERING,
            NPCs.MINING_TUTOR_4902 to JobType.GATHERING,
            NPCs.PRAYER_TUTOR_4903 to JobType.GATHERING,
            NPCs.SMELTING_TUTOR_4904 to JobType.GATHERING,
            NPCs.WOODCUTTING_TUTOR_4906 to JobType.GATHERING
        )

        private val EMPLOYER_IDS = EMPLOYER_JOB_TYPES.keys.toIntArray()
    }


    override fun login(player: Player) {
        val remaining = getAttribute(player, "jobs:amount", -1)
        val jobType = JobType.values()[getAttribute(player, "jobs:type", 0)]

        if (jobType == JobType.SLAYING && remaining > 0) {
            player.hook(Event.NPCKilled, JobKillHook)
        }
    }

    override fun defineListeners() {
        on(EMPLOYER_IDS, IntType.NPC, "work-for") { player, node ->
            if (JobManager.getStoreFile().getInt(player.username.lowercase()) == 3) {
                sendNPCDialogue(player, node.id, "You've hit your limit for the day. Come back tomorrow!")
                return@on true
            }

            if (getAttribute(player, "jobs:id", -1) != -1) {
                JobManager.rewardPlayer(player, node.asNpc())
                return@on true
            }

            var amount: Int
            val jobType = EMPLOYER_JOB_TYPES[node.id] ?: return@on false
            val jobId = when (jobType) {
                JobType.GATHERING -> {
                    val job = EMPLOYERS_AND_JOBS[node.id]?.filter { checkRequirement(player, it) }?.randomOrNull()

                    if (job == null) {
                        sendNPCDialogue(
                            player,
                            node.id,
                            "I'm sorry, I don't currently have any jobs that you're qualified for.",
                            FacialExpression.HALF_THINKING
                        )
                        return@on true
                    }

                    amount = job.getAmount()
                    job.ordinal
                }

                JobType.SLAYING -> {
                    SlayingJobs.values().random().ordinal.also {
                        amount = SlayingJobs.values()[it].getAmount()
                    }
                }
            }

            when (jobType) {
                JobType.GATHERING -> {
                    val job = GatheringJobs.values()[jobId]

                    sendItemDialogue(
                        player,
                        job.itemId,
                        "You are assigned to gather $amount ${Item(job.itemId).name.lowercase()}"
                    )
                }

                JobType.SLAYING -> {
                    val job = SlayingJobs.values()[jobId]

                    sendItemDialogue(
                        player,
                        WEAPON_ICONS.random(),
                        "You are assigned to kill $amount ${NPC(job.ids[0]).name.lowercase()}"
                    )

                    player.unhook(JobKillHook) //try to unhook just in case they already had it hooked, unlikely but possible.
                    player.hook(Event.NPCKilled, JobKillHook)
                }
            }

            setAttribute(player, "/save:jobs:id", jobId)
            setAttribute(player, "/save:jobs:amount", amount)
            setAttribute(player, "/save:jobs:original_amount", amount)
            setAttribute(player, "/save:jobs:type", jobType.ordinal)

            player.dispatch(JobAssignmentEvent(jobType, node.asNpc()))
            return@on true
        }
    }

    private fun checkRequirement(player: Player, jobs: GatheringJobs?): Boolean {
        jobs ?: return true

        if (getStatLevel(player, jobs.skill) < jobs.lvlReq) {
            return false
        }

        return true
    }

    private object JobKillHook : EventHook<NPCKillEvent> {
        override fun process(entity: Entity, event: NPCKillEvent) {
            if (entity !is Player) return
            if (entity.isArtificial) return

            val job = getAttribute(entity, "jobs:id", -1)

            if (job !in 0 until SlayingJobs.values().size) {
                return
            }

            val ids = SlayingJobs.values()[job].ids

            if (event.npc.id in ids) {
                JobManager.updateJobRemaining(entity, 1)
            }
        }
    }
}