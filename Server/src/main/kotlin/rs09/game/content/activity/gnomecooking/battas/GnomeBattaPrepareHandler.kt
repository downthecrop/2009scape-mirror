package rs09.game.content.activity.gnomecooking.battas

import core.cache.def.impl.ItemDefinition
import core.game.component.Component
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items

/**
 * Handles the prepare option for gnome battas
 * @author Ceikry
 */
@Initializable
class GnomeBattaPrepareHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.forId(Items.HALF_BAKED_BATTA_2249).handlers["option:prepare"] = this
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        player.interfaceManager.open(Component(434))
        return true
    }

}