package plugin.barbtraining.fishing

import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.ObjectDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
/**
 * Option handlers for barbarian fishing.
 * @author Ceikry
 */
class OptionHandlers : OptionHandler(){
    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        when(node?.id){
            25268 -> handleSearch(player).also { return true }
            1176 -> handleFish(player).also{return true}
        }
        return false
    }

    fun handleFish(player: Player?){
        player?.pulseManager?.run(BarbFishingPulse(player))
        player?.sendMessage("You attempt to catch a fish...")
    }

    fun handleSearch(player: Player?){
        if(player?.getAttribute("barbtraining:fishing",false) == true){
            if(!player.inventory.containsItem(Item(11323))){
                player.inventory.add(Item(11323))
                player.sendMessage("Under the bed you find a fishing rod.")
            } else {
                player.sendMessage("You find nothing under the bed")
            }
        } else {
            player?.sendMessage("Maybe I should speak to Otto before looking under his bed.")
        }
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ObjectDefinition.forId(25268).handlers.put("option:search",this)
        NPCDefinition.forId(1176).handlers.put("option:fish",this)
        return this
    }

}