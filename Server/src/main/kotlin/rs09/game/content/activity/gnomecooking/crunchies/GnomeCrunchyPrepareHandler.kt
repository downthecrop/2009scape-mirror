package rs09.game.content.activity.gnomecooking.crunchies

import core.cache.def.impl.ItemDefinition
import core.game.component.Component
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin

private const val HALF_BAKED_CRUNCHY = 2201
private const val CRUNCHY_INTERFACE = 437

/**
 * Opens the gnome crunchy interface
 */
@Initializable
class GnomeCrunchyPrepareHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.forId(HALF_BAKED_CRUNCHY).handlers["option:prepare"] = this
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        player.interfaceManager.open(Component(CRUNCHY_INTERFACE))
        return true
    }
}