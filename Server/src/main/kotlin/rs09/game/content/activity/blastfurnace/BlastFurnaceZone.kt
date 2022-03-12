package rs09.game.content.activity.blastfurnace

import api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.map.zone.ZoneMonitor
import core.plugin.Initializable
import core.plugin.Plugin

/**Code for defining the Blast Furnace zone, Blast Furnace will only
 * operate and run its logic if there are actual players in this zone
 * @author phil lips*/


//Remove this once the funny dupe gets fixed
//@Initializable
class BlastFurnaceZone : MapZone("Blast Furnace Zone",true), Plugin<Any> {

    var pulseStarted = false

    override fun newInstance(arg: Any?): Plugin<Any> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any {
        return Unit
    }

    override fun configure() {
        super.register(ZoneBorders(1935,4956,1956,4974))
    }

    override fun enter(e: Entity?): Boolean {
        if (!pulseStarted){
            submitWorldPulse(BlastFurnace.blastPulse)
            pulseStarted = true
        }
        if (e != null && e.isPlayer) {
            BlastFurnace.blastFurnacePlayerList.add(e.asPlayer())
        }
        return super.enter(e)
    }

    override fun leave(e: Entity?, logout: Boolean): Boolean {
        if (e != null && e.isPlayer) {
            BlastFurnace.blastFurnacePlayerList.remove(e.asPlayer())
        }
        return super.leave(e, logout)
    }

}