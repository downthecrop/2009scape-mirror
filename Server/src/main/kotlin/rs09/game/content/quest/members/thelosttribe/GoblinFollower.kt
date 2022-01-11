package rs09.game.content.quest.members.thelosttribe

import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs09.consts.Components
import rs09.game.world.GameWorld

/**
 * Small object for the goblin follow options
 * @author Ceikry
 */
object GoblinFollower {
    fun sendToMines(player: Player){
        travel(player,Location.create(3319, 9616, 0))
    }
    fun sendToLumbridge(player: Player){
        travel(player,Location.create(3232, 9610, 0))
    }

    private fun travel(player: Player,location: Location){
        GameWorld.Pulser.submit(object: Pulse(){
            var counter = 0
            override fun pulse(): Boolean {
                when(counter++){
                    0 -> player.lock().also { player.interfaceManager.open(Component(Components.FADE_TO_BLACK_120)) }
                    3 -> player.properties.teleportLocation = location
                    4 -> {
                        player.interfaceManager.close(Component(Components.FADE_TO_BLACK_120))
                        player.interfaceManager.open(Component(Components.FADE_FROM_BLACK_170))
                    }
                    6 -> player.unlock().also { player.interfaceManager.close(Component(170));  return true }
                }
                return false
            }
        })
    }
}