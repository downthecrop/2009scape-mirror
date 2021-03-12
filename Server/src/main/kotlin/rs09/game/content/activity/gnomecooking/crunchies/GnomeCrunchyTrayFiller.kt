package rs09.game.content.activity.gnomecooking.crunchies

import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin

private const val GIANNE_DOUGH = 2171
private const val CRUNCHY_TRAY = 2165
private const val RAW_CRUNCHIES = 2202


/**
 * Handles adding GIANNE_DOUGH to CRUNCHY_TRAY to produce RAW_CRUNCHIES
 * @author Ceikry
 */
@Initializable
class GnomeCrunchyTrayFiller : UseWithHandler(GIANNE_DOUGH) {
    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(CRUNCHY_TRAY, ITEM_TYPE,this)
        return this
    }

    override fun handle(event: NodeUsageEvent?): Boolean {
        event ?: return false
        event.player.inventory.remove(event.used.asItem())
        event.player.inventory.remove(event.usedWith.asItem())
        event.player.inventory.add(Item(RAW_CRUNCHIES))
        return true
    }
}