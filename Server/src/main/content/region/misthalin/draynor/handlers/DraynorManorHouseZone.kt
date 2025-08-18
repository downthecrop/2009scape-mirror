package content.region.misthalin.draynor.handlers

import core.game.node.entity.Entity
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalNpcs
import core.game.world.map.path.Pathfinder
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Represents the interior of Draynor Manor.
 *
 * @author Broseki
 */
@Initializable
class DraynorManorHouseZone : MapZone("Draynor Manor House", true), Plugin<Any?> {

    override fun newInstance(arg: Any?): Plugin<Any?> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun move(e: Entity, loc: Location?, destination: Location): Boolean {
        for (n in getLocalNpcs(e, 5)) {
            if (n.isInvisible() || n === e) {
                continue
            }
            if (n.shouldPreventStacking(e)) {
                val s1 = e.size()
                val s2 = n.size()
                val x = destination.getX()
                val y = destination.getY()
                val l = n.getLocation()
                if (Pathfinder.isStandingIn(x, y, s1, s1, l.getX(), l.getY(), s2, s2)) {
                    return false
                }
            }
        }
        return true
    }

    override fun enter(entity: Entity?): Boolean {
        return super.enter(entity)
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any? {
        return null
    }

    override fun configure() {
        register(ZoneBorders(3097, 3373, 3119, 3364))
        register(ZoneBorders(3090, 3363, 3126, 3354))
    }
}