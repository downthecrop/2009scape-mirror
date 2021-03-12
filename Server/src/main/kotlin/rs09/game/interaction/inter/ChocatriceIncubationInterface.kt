package rs09.game.interaction.inter

import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.plugin.Plugin

/**
 * I found a bunch of varps for this interface so I've documented them here.
 * to set, for example, COLD/HOT bar height, you need to send player.configmanager.set(GAUGE_VARP, (height shl COLD_OFFSET) or (height shl HOT_OFFSET))
 * bar gauges go up to 12
 * heat gauge at the top goes a full 360, though the edge is at roughly 160
 * @author Ceikry
 */
private const val GAUGE_UPDATE_SCRIPT = 894
private const val COLD_OFFSET = 22
private const val HOT_OFFSET  = 18
private const val GAUGE_OFFSET = 15
private const val GAUGE_VARP = 1205
private const val METER_VARP = 1201
private const val BUTTON_ADD_COLD = 16
private const val BUTTON_ADD_HOT = 18

class IncubationInterface : ComponentPlugin() {

    override fun handle(player: Player?, component: Component?, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(717, this)
        return this
    }
}