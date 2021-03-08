package core.game.interaction.`object`

import core.cache.def.impl.ObjectDefinition
import core.tools.Items
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.`object`.GameObject
import core.game.node.`object`.ObjectBuilder
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
/**
 * Handles the muddy chest
 * @author Ceikry
 */
class MuddyChestHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ObjectDefinition.forId(170).handlers["option:open"] = this
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        node ?: return false
        val key = Item(Items.MUDDY_KEY_991)
        if(player.inventory.containsItem(key)){
            player.inventory.remove(key)
            player.animator.animate(Animation(536))
            ObjectBuilder.replace(node.asObject(),GameObject(171, node.location, node.asObject().rotation),3)
            for(item in chestLoot){
                if(!player.inventory.add(item)) GroundItemManager.create(item,player)
            }
        } else {
            player.sendMessage("This chest is locked and needs some sort of key.")
        }
        return true
    }

    val chestLoot = arrayListOf(
            Item(Items.UNCUT_RUBY_1619),
            Item(Items.MITHRIL_BAR_2359),
            Item(Items.MITHRIL_DAGGER_1209),
            Item(Items.ANCHOVY_PIZZA_2297),
            Item(Items.LAW_RUNE_563,2),
            Item(Items.DEATH_RUNE_560,2),
            Item(Items.CHAOS_RUNE_562,10),
            Item(995,50)
    )
}