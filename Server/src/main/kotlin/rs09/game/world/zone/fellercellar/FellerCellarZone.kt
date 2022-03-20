package rs09.game.world.zone.fellercellar

import api.sendMessage
import api.submitWorldPulse
import core.game.node.entity.Entity
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import rs09.tools.stringtools.colorize

@Initializable
class FellerCellarZone : MapZone("Feller Cellar Zone",true), Plugin<Any> {

    var pulseStarted = false

    override fun newInstance(arg: Any?): Plugin<Any> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any {
        return Unit
    }

    override fun configure() {
        super.register(ZoneBorders(3207,9614,3220,9626))
    }
    override fun enter(e: Entity?): Boolean {
        if(!pulseStarted){
            submitWorldPulse(FellerCellar.fellerPulse)
            pulseStarted = true
        }
        if (e != null && e.isPlayer) {
            FellerCellar.shuffleTracks(e.asPlayer(), FellerCellar.random)
            FellerCellar.FellerCellarPlayerList.add(e.asPlayer())
            if(FellerCellar.random == 7){
                e.asPlayer().sendMessage(colorize("%RNow playing: ${FellerCellar.SONG_NAMES[FellerCellar.random]}"))
            }
            else sendMessage(e.asPlayer(), colorize("%GNow Playing: ${FellerCellar.SONG_NAMES[FellerCellar.random]}"))
        }
        return super.enter(e)
    }

    override fun leave(e: Entity?, logout: Boolean): Boolean {
        if (e != null && e.isPlayer) {
            FellerCellar.FellerCellarPlayerList.remove(e.asPlayer())
        }
        return super.leave(e, logout)
    }

}