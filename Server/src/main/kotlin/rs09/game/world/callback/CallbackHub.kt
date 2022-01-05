package rs09.game.world.callback

import core.game.node.entity.skill.hunter.ImpetuousImpulses
import core.game.world.callback.CallBack
import core.game.world.map.zone.ZoneBuilder
import rs09.game.ge.GrandExchange
import rs09.game.ge.OfferManager
import rs09.game.system.SystemLogger
import java.util.*

/**
 * Initializes a few world pulses that need to continuously run
 * @author Vexia
 */
object CallbackHub {
    private var calls: MutableList<CallBack> = ArrayList()

    fun call(): Boolean {
        calls.add(ZoneBuilder())
        calls.add(GrandExchange)
        calls.add(ImpetuousImpulses())
        for (call in calls) {
            if (!call.call()) {
                SystemLogger.logErr("A callback was stopped, callback=" + call.javaClass.simpleName + ".")
                return false
            }
        }
        calls.clear()
        return true
    }
}