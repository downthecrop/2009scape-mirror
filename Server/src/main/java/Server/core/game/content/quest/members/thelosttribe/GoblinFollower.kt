package core.game.content.quest.members.thelosttribe

import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.tools.Components

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
                    0 -> player.lock().also { player.interfaceManager.open(Component(Components.fade_to_black_115)) }
                    3 -> player.properties.teleportLocation = location
                    4 -> {
                        player.interfaceManager.close(Component(Components.fade_to_black_115))
                        player.interfaceManager.open(Component(Components.fade_from_black_170))
                    }
                    6 -> player.unlock().also { player.interfaceManager.close(Component(170));  return true }
                }
                return false
            }
        })
    }
}