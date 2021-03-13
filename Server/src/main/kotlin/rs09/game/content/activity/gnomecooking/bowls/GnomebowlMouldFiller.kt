package rs09.game.content.activity.gnomecooking.bowls

import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items

private const val GNOME_BOWL_MOLD = 2166
private const val GIANNE_DOUGH = 2171

@Initializable
class GnomebowlMouldFiller : UseWithHandler(GIANNE_DOUGH) {
    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(GNOME_BOWL_MOLD, ITEM_TYPE,this)
        return this
    }

    override fun handle(event: NodeUsageEvent?): Boolean {
        event ?: return false
        val player = event.player
        val used = event.used.asItem()
        val with = event.usedWith.asItem()
        player.inventory.remove(used)
        player.inventory.remove(with)
        player.inventory.add(Item(Items.RAW_GNOMEBOWL_2178))
        return true
    }

}