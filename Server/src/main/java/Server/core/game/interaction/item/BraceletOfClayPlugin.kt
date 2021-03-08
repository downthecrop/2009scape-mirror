package core.game.interaction.item

import core.cache.def.impl.ItemDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Handles the bracelet of clay operate option.
 * @author Ceikry
 */
@Initializable
class BraceletOfClayPlugin : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any>? {
        ItemDefinition.forId(11074).handlers["option:operate"] = this
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        var charge = node.asItem().charge
        if (charge > 28) charge = 28
        player.sendMessage("You have $charge uses left.")
        return true
    }

    override fun isWalk(): Boolean {
        return false
    }
}