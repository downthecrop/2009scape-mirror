package rs09.game.content.jobs

import api.*
import rs09.game.content.jobs.impl.GatheringJobs
import rs09.game.content.jobs.impl.SlayingJobs
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.json.simple.JSONObject
import org.rs09.consts.Items
import rs09.ServerStore
import rs09.ServerStore.Companion.getInt

object JobManager {
    @JvmStatic
    fun updateJobRemaining(player: Player, amount: Int = 0) {
        val cur = getAttribute(player, "jobs:amount", 0)
        val new = cur - amount
        val jobType = JobType.values()[getAttribute(player, "jobs:type", 0)]
        val jobId = getAttribute(player, "jobs:id", 0)

        when (jobType) {
            JobType.GATHERING -> { }

            JobType.SLAYING -> {
                val job = SlayingJobs.values()[jobId]

                if (new % 5 == 0 && new > 0) {
                    sendMessage(
                        player,
                        "You have $new ${NPC(job.ids[0]).name.lowercase()} left to go."
                    )
                } else if (new == 0) {
                    sendMessage(
                        player,
                        "<col=00AA00>You have completed your job.</col>"
                    )
                }
            }
        }

        setAttribute(player, "jobs:amount", new)
    }

    @JvmStatic
    fun rewardPlayer(player: Player, npc: NPC) {
        val amt = getAttribute(player, "jobs:original_amount", 0)
        val jobType = JobType.values()[getAttribute(player, "jobs:type", 0)]
        val jobId = getAttribute(player, "jobs:id", 0)

        when (jobType) {
            JobType.GATHERING -> {
                val it = Item(GatheringJobs.values()[jobId].itemId)
                var amount = amountInInventory(player, it.id)
                val needed = getAttribute(player, "jobs:amount", 0)

                if (amount == 0) {
                    openDialogue(player, CancelJobDialogueFile(), npc)
                    return
                }

                if (amount < needed) {
                    sendItemDialogue(
                        player,
                        GatheringJobs.values()[jobId].itemId,
                        "You still need to gather ${needed - amount} more."
                    )

                    removeItem(player, Item(it.id, amount))
                    setAttribute(player, "jobs:amount", needed - amount)

                    return
                }

                if (amount > needed) {
                    amount = needed
                }

                removeItem(player, Item(it.id, amount))
            }

            JobType.SLAYING -> {
                val needed = getAttribute(player, "jobs:amount", 0)

                if (needed > 0) {
                    sendDialogue(
                        player,
                        "You still need to kill $needed more ${NPC(SlayingJobs.values()[jobId].ids[0]).name.lowercase()}."
                    )

                    return
                }

                sendDialogue(
                    player,
                    "Excellent work, thank you! Here's your reward."
                )
            }
        }

        addItemOrDrop(player, Items.COINS_995, 250 * amt)

        removeAttribute(player, "jobs:id")
        removeAttribute(player, "jobs:amount")
        removeAttribute(player, "jobs:original_amount")
        removeAttribute(player, "jobs:type")

        getStoreFile()[player.username.lowercase()] = getStoreFile().getInt(player.username.lowercase()) + 1
    }

    fun getStoreFile(): JSONObject {
        return ServerStore.getArchive("daily-jobs-tracking")
    }
}