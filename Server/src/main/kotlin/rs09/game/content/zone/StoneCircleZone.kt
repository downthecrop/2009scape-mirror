package rs09.game.content.zone

import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Handles the stone circle zone south of Ardougne.
 * @author Kya
 */
@Initializable
class StoneCircleZone : MapZone("stone circle", true), Plugin<Any> {
    /**
     * Constructs a new `StoneCircleZone` `Object`.
     */
    override fun configure() {
        register(ZoneBorders(2558, 3219, 2559, 3225))
        register(ZoneBorders(2560, 3219, 2564, 3225))
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any? {
        return null
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun enter(e: Entity): Boolean {
        if (e is Player) {
            e.varpManager.get(1184).setVarbit(28, 1).send(e)
        }
        return super.enter(e)
    }

    override fun leave(e: Entity, logout: Boolean): Boolean {
        if (!logout && e is Player) {
            e.varpManager.get(1184).setVarbit(28, 0).send(e)
        }
        return super.leave(e, logout)
    }
}