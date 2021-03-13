package rs09.game.content.activity.gnomecooking.battas

import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items

/**
 * Handles garnishing of battas
 * @author Ceikry
 */
@Initializable
class GnomeBattaGarnisher : UseWithHandler(9479,9481,9484,9486) {
    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(Items.EQUA_LEAVES_2128, ITEM_TYPE,this)
        addHandler(Items.GNOME_SPICE_2169, ITEM_TYPE,this)
        return this
    }

    override fun handle(event: NodeUsageEvent?): Boolean {
        event ?: return false
        val player = event.player
        val used = event.used.asItem()
        val with = event.usedWith.asItem()
        var product = -1
        when(with.id){
            Items.EQUA_LEAVES_2128 -> {
                when(used.id){
                    9486 -> product = Items.WORM_BATTA_2253
                    9484 -> product = Items.VEGETABLE_BATTA_2281
                    9479 -> product = Items.CHEESE_PLUSTOM_BATTA_2259
                }
            }

            Items.GNOME_SPICE_2169 -> {
                when(used.id){
                    9481 -> product = Items.FRUIT_BATTA_2277
                }
            }
        }
        if(product == -1) return false
        player.inventory.remove(used)
        player.inventory.remove(with)
        player.inventory.add(Item(product))
        player.skills.addExperience(Skills.COOKING,88.0)
        return true
    }
}