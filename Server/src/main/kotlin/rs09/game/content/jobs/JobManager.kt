package rs09.game.content.jobs

import GatheringJobs
import SlayingJob
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import org.json.simple.JSONObject
import rs09.ServerStore
import rs09.ServerStore.Companion.getInt
import rs09.game.system.SystemLogger
import java.util.concurrent.TimeUnit

object JobManager {
    @JvmStatic
    fun updateJobRemaining(player: Player, amount: Int = 0) {
        val cur = player.getAttribute("jobs:amount",0)
        val new = cur - amount
        val jobType = player.getAttribute("jobs:type",0)
        val jobId = player.getAttribute("jobs:id",0)

        var gJob: GatheringJobs? = null
        var sJob: SlayingJob? = null

        if(jobType == 0){
            gJob = GatheringJobs.values()[jobId]
        } else {
            sJob = SlayingJob.values()[jobId]
        }

        if(new % 5 == 0 && sJob != null && new > 0){
            player.sendMessage("You have $new ${NPC(sJob.ids[0]).name.toLowerCase()} left to go.")
        }
        if(new == 0 && sJob != null){
            player.sendMessage("You have completed your job.")
        }
        player.setAttribute("jobs:amount",new)
    }

    @JvmStatic
    fun rewardPlayer(player: Player, npc: NPC){
        val amt = player.getAttribute("jobs:original_amount",0)
        val type = player.getAttribute("jobs:type",0)
        val jobId = player.getAttribute("jobs:id",0)
        if(type == 0){
            val it = Item(GatheringJobs.values()[jobId].itemId)
            var amount = player.inventory.getAmount(it)
            val needed = player.getAttribute("jobs:amount",0)
            if(amount == 0){
                player.dialogueInterpreter.open(CancelJobDialogueFile(),npc)
                SystemLogger.logAlert("Opening CancelJob")
                return
            }
            if(amount < needed){
                player.dialogueInterpreter.sendItemMessage(GatheringJobs.values()[jobId].itemId,"You still need to gather ${needed - amount} more.")
                player.inventory.remove(Item(it.id,amount))
                player.setAttribute("jobs:amount",needed - amount)
                return
            }
            if(amount > needed) amount = needed
            player.inventory.remove(Item(it.id,amount))
        } else {
            val needed = player.getAttribute("jobs:amount",0)
            if(needed > 0){
                player.dialogueInterpreter.sendDialogue("You still need to kill $needed more ${NPC(SlayingJob.values()[jobId].ids[0]).name.toLowerCase()}.")
                return
            }
            player.dialogueInterpreter.sendDialogue("Excellent work, thank you! Here's your reward.")
        }
        if(!player.inventory.add(Item(995,250 * amt))){
            GroundItemManager.create(Item(995,250 * amt),player.centerLocation,player)
        }
        player.removeAttribute("jobs:id")
        player.removeAttribute("jobs:amount")
        player.removeAttribute("jobs:original_amount")
        player.removeAttribute("jobs:type")

        getStoreFile()[player.username.toLowerCase()] = getStoreFile().getInt(player.username.toLowerCase()) + 1
    }

    fun getStoreFile(): JSONObject {
        return ServerStore.getArchive("daily-jobs-tracking")
    }
}