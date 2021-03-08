package core.game.content.activity.gnomecooking.bowls

import core.cache.def.impl.ItemDefinition
import core.game.component.Component
import core.tools.Items
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class GnomeBowlPrepareHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.forId(Items.HALF_BAKED_BOWL_2177).handlers["option:prepare"] = this
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        player.interfaceManager.open(Component(435))
        return true
    }

}