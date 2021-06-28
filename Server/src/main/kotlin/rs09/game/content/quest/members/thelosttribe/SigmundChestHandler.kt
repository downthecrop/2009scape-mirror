package rs09.game.content.quest.members.thelosttribe

import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items

@Initializable
/**
 * Handles sigmund's chest
 * @author Ceikry
 */
class SigmundChestHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(6910).handlers["option:open"] = this
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        if(player.questRepository.getQuest("Lost Tribe").getStage(player) == 47 && player.inventory.contains(Items.KEY_423,1)){
            player.inventory.remove(Item(Items.KEY_423))
            for(item in arrayOf(Items.HAM_ROBE_4300,Items.HAM_SHIRT_4298,Items.HAM_HOOD_4302).map { Item(it) }){
                if(!player.inventory.add(item)){
                    GroundItemManager.create(item,player)
                }
            }
            player.questRepository.getQuest("Lost Tribe").setStage(player,48)
        } else {
            player.sendMessage("This chest requires a key.")
        }
        return true
    }
}