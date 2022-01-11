package rs09.game.content.zone.keldagrim

import core.game.component.Component
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs09.consts.Components
import rs09.game.world.GameWorld

object KeldagrimCartMethods {
    @JvmStatic
    fun goToKeldagrim(player: Player){
        GameWorld.Pulser.submit(TravelToKeldagrimPulse(player))
    }

    @JvmStatic
    fun leaveKeldagrimTo(player: Player, dest: Location){
        GameWorld.Pulser.submit(TravelFromKeldagrimPulse(player,dest))
    }
}


class TravelFromKeldagrimPulse(val player: Player, val dest: Location): Pulse(){
    var counter = 0
    override fun pulse(): Boolean {
        when(counter++){
            0 -> player.lock().also { player.interfaceManager.open(Component(Components.FADE_FROM_BLACK_170)) }
            4 -> {
                player.properties.teleportLocation = Location.create(2911, 10171, 0)
                player.appearance.rideCart(true)
            }
            5 -> {
                player.walkingQueue.reset()
                player.walkingQueue.addPath(2936, 10171)
            }
            6 -> {
                player.interfaceManager.close(Component(Components.FADE_FROM_BLACK_170))
                player.interfaceManager.open(Component(Components.FADE_FROM_BLACK_170))
            }
            14 -> {
                player.interfaceManager.open(Component(Components.FADE_TO_BLACK_120))
            }
            21 -> {
                player.walkingQueue.reset()
                player.properties.teleportLocation = dest
                player.appearance.rideCart(false)
            }
            23 -> {
                player.interfaceManager.close(Component(Components.FADE_TO_BLACK_120))
                player.interfaceManager.open(Component(Components.FADE_FROM_BLACK_170))
            }
            25 -> {
                player.unlock()
                player.interfaceManager.close(Component(Components.FADE_FROM_BLACK_170))
                return true
            }
        }
        return false
    }
}

class TravelToKeldagrimPulse(val player: Player) : Pulse(){
    var counter = 0
    var cartNPC = NPC(1546)
    override fun pulse(): Boolean {
        when(counter++){
            0 -> player.lock().also { player.interfaceManager.open(Component(115)) }
            3 -> player.properties.teleportLocation = Location.create(2942, 10175, 0).also { player.appearance.rideCart(true) }
            5 -> {
                player.walkingQueue.reset()
                player.walkingQueue.addPath(2915, 10175)
            }
            7 -> {
                player.interfaceManager.close(Component(115))
                player.interfaceManager.open(Component(170))
            }
            19 -> {
                player.interfaceManager.close(Component(170))
                player.unlock()
                player.appearance.rideCart(false)
                cartNPC.location = player.location
                cartNPC.direction = Direction.WEST
                cartNPC.init()
                player.properties.teleportLocation = player.location.transform(0,1,0)
            }
            33 -> return true.also { cartNPC.clear() }

        }
        return false
    }
}