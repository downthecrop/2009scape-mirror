package rs09.game.content.activity.gnomecooking

import core.cache.def.impl.ItemDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class DeliveryBoxHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.forId(9477).handlers["option:check"] = this
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        val jobId = player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_JOB_ORDINAL",-1)
        if(jobId == -1){
            player.dialogueInterpreter.sendDialogue("You do not currently have a job.")
        } else {
            val job = GnomeCookingJob.values()[jobId]
            val item = player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_NEEDED_ITEM", Item(0))
            player.dialogueInterpreter.sendDialogue("I need to deliver a ${item.name.toLowerCase()} to ${NPC(job.npc_id).name.toLowerCase()},","who is ${job.tip}")
        }
        return true
    }

}