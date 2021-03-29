package rs09.game.content.jobs

import GatheringJobs
import SlayingJob
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

/**
 * Handles the work-for actions for the NPCs
 * @author Ceikry
 */
class WorkForInteractionListener : InteractionListener() {
    val possibleWeaponLooks = arrayListOf(
        Items.BRONZE_SCIMITAR_1321,
        Items.STEEL_SCIMITAR_1325,
        Items.RUNE_SCIMITAR_1333,
        Items.BRONZE_MACE_1422,
        Items.MITHRIL_WARHAMMER_1343
    )

    val gatheringMap = mapOf<Int,List<GatheringJobs>>(
        0 to listOf(GatheringJobs.AIR_RUNE,GatheringJobs.COWHIDES,GatheringJobs.WATER_RUNE),
        922 to listOf(GatheringJobs.ASHES,GatheringJobs.AIR_RUNE,GatheringJobs.WATER_RUNE),
        3807 to listOf(GatheringJobs.COWHIDES),
        4899 to listOf(GatheringJobs.CAKE,GatheringJobs.ANCHOVY_PIZZA,GatheringJobs.COOKED_SALMON,GatheringJobs.COOKED_TROUT,GatheringJobs.MEAT_PIE,GatheringJobs.MEAT_PIZZA,GatheringJobs.PLAIN_PIZZA),
        4901 to listOf(GatheringJobs.RAW_SALMON,GatheringJobs.RAW_SHRIMP,GatheringJobs.RAW_TROUT),
        4902 to listOf(GatheringJobs.COPPER_ORE,GatheringJobs.TIN_ORE,GatheringJobs.COAL,GatheringJobs.IRON_ORE,GatheringJobs.SILVER_ORE,GatheringJobs.GOLD_ORE),
        4903 to listOf(GatheringJobs.SILVER_ORE),
        4904 to listOf(GatheringJobs.BRONZE_BAR,GatheringJobs.IRON_BAR,GatheringJobs.STEEL_BAR),
        4906 to listOf(GatheringJobs.LOG,GatheringJobs.OAK,GatheringJobs.WILLOW)
    )

    val typeMap = mapOf(
        0 to 0,
        705 to 1,
        922 to 0,
        1861 to 1,
        3807 to 0,
        4899 to 0,
        4901 to 0,
        4902 to 0,
        4903 to 0,
        4904 to 0,
        4906 to 0,
        4707 to 1
    )

    val IDs = typeMap.keys.toIntArray()

    override fun defineListeners() {

        on(IDs,NPC,"work-for"){ player,node ->
            var amount = 0
            var jobId = 0

            if(player.getAttribute("jobs:reset_time",Long.MAX_VALUE) < System.currentTimeMillis()){
                player.setAttribute("/save:jobs:dailyAmt",0)
            }

            if(player.getAttribute("jobs:id",-1) != -1){
                JobManager.rewardPlayer(player,node.asNpc())
                return@on true
            }

            val type = typeMap[node.id] ?: return@on false
            jobId = if(type == 0) {
                var job = gatheringMap[node.id]?.random()
                while(!checkRequirement(player,job)){
                    job = gatheringMap[node.id]?.random()
                }
                amount = job?.getAmount() ?: 0
                job?.ordinal ?: 0
            } else {
                SlayingJob.values().random().ordinal.also { amount = SlayingJob.values()[it].getAmount() }
            }

            when(type){
                0 -> {
                    val job = GatheringJobs.values()[jobId]
                    player.dialogueInterpreter.sendItemMessage(job.itemId,"You are assigned to gather $amount ${Item(job.itemId).name.toLowerCase()}")

                    // Have the Fishing Tutor send you on an errand
                    if (node.id == 4901) player.achievementDiaryManager.finishTask(player, DiaryType.LUMBRIDGE, 0, 14);
                }
                1 -> {
                    val job = SlayingJob.values()[jobId]
                    player.dialogueInterpreter.sendItemMessage(possibleWeaponLooks.random(),"You are assigned to kill $amount ${NPC(job.ids[0]).name.toLowerCase()}")
                }
            }

            player.setAttribute("/save:jobs:id",jobId)
            player.setAttribute("/save:jobs:amount",amount)
            player.setAttribute("/save:jobs:original_amount",amount)
            player.setAttribute("/save:jobs:type",type)

            return@on true
        }

    }

    fun checkRequirement(player: Player, jobs: GatheringJobs?): Boolean{
        jobs ?: return true
        val requirement: Pair<Int,Int> = Pair(jobs.lvlReq,jobs.skill)
        if(player.skills.getLevel(requirement.second) < requirement.first){
            return false
        }
        return true
    }
}