package core.game.node.entity.skill.farming

import core.game.content.dialogue.FacialExpression
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.NPCs

@Initializable
class LeprechaunNoter : UseWithHandler(*Plantable.values().map{ it.harvestItem }.toIntArray()) {
    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(NPCs.TOOL_LEPRECHAUN_3021, NPC_TYPE,this)
        addHandler(NPCs.GOTH_LEPRECHAUN_8000, NPC_TYPE,this)
        addHandler(NPCs.TOOL_LEPRECHAUN_4965, NPC_TYPE,this)
        return this
    }

    override fun handle(event: NodeUsageEvent?): Boolean {
        event ?: return false
        val player = event.player ?: return false
        val usedItem = event.usedItem ?: return false
        val npc = event.usedWith.asNpc() ?: return false

        val expr = when(npc.id){
            3021 -> FacialExpression.OLD_NORMAL
            else -> FacialExpression.FRIENDLY
        }

        if(usedItem.noteChange != usedItem.id){
            val amt = player.inventory.getAmount(usedItem.id)
            if(player.inventory.remove(Item(usedItem.id,amt))){
                player.inventory.add(Item(usedItem.noteChange,amt))
            }
            player.dialogueInterpreter.sendDialogues(npc.id,expr,"There ya go.")
        } else {
            player.dialogueInterpreter.sendDialogues(npc.id,expr,"But that's already a note!")
        }
        return true
    }

}