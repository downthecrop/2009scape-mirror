package rs09.game.interaction.`object`

import core.game.node.`object`.GameObject
import core.game.node.`object`.ObjectBuilder
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs09.consts.Items
import rs09.game.interaction.OptionListener

@Initializable
/**
 * Handles the muddy chest
 * @author Ceikry
 */
class MuddyChestHandler : OptionListener() {

    private val CHEST = 170

    override fun defineListeners() {

        on(CHEST,OBJECT,"open"){player,node ->
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
            return@on true
        }

    }

    val chestLoot = arrayOf(
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