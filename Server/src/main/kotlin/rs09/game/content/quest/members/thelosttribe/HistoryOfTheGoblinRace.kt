package rs09.game.content.quest.members.thelosttribe

import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
/**
 * Handles the HOTGR interface
 * @author Ceikry
 */
class HistoryOfTheGoblinRace : ComponentPlugin() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(183,this)
        return this
    }

    override fun open(player: Player?, component: Component?) {
        player ?: return
        super.open(player, component)
        player.packetDispatch.sendInterfaceConfig(183,17,true)
        val qstage = player.questRepository.getQuest("Lost Tribe").getStage(player)
        component?.setCloseEvent { player, c ->
            if(qstage == 42 || qstage == 41 ) {
                player.dialogueInterpreter.sendDialogues(player, FacialExpression.THINKING, "Hey... The symbol of the 'Dorgeshuun' tribe looks just", "like the symbol on the brooch I found.")
                player.questRepository.getQuest("Lost Tribe").setStage(player, 43)
            }
            player.removeAttribute("hgr-index")
            true
        }
    }

    override fun handle(player: Player?, component: Component?, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        player ?: return false
        when(button){
            16 -> setIndex(player,getIndex(player) + 1)
            17 -> setIndex(player,getIndex(player) - 1)
        }
        update(player)
        return true
    }

    fun update(player: Player){
        val index = getIndex(player)
        player.packetDispatch.sendInterfaceConfig(183,32,index != 0)
        player.packetDispatch.sendInterfaceConfig(183,14,index != 1)
        player.packetDispatch.sendInterfaceConfig(183,15,index != 2)
        player.packetDispatch.sendInterfaceConfig(183,16,index == 2)
        player.packetDispatch.sendInterfaceConfig(183,17,index == 0)
    }

    fun setIndex(player: Player, index: Int){
        player.setAttribute("hgr-index",index)
    }

    fun getIndex(player: Player): Int{
        return player.getAttribute("hgr-index",0)
    }
}