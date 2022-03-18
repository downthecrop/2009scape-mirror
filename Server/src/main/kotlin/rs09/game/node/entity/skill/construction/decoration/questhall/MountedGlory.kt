package rs09.game.node.entity.skill.construction.decoration.questhall

import api.teleport
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.audio.Audio;
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import rs09.game.interaction.InteractionListener
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MountedGlory : InteractionListener() {

    val MOUNTED_GLORY = 13523

    val TELEPORTS = arrayOf(
        Location.create(3087, 3495, 0),
        Location.create(2919, 3175, 0),
        Location.create(3104, 3249, 0),
        Location.create(3304, 3124, 0)
    )

    override fun defineListeners() {
        on(MOUNTED_GLORY, SCENERY,"Edgeville") { player, _ ->
            mountedGloryTeleport(player,0)
            return@on true
        }

        on(MOUNTED_GLORY, SCENERY,"Karamja") { player, _ ->
            mountedGloryTeleport(player,1)
            return@on true
        }

        on(MOUNTED_GLORY, SCENERY,"Draynor Village") { player, _ ->
            mountedGloryTeleport(player,2)
            return@on true
        }

        on(MOUNTED_GLORY, SCENERY,"Al Kharid") { player, _ ->
            mountedGloryTeleport(player,3)
            return@on true
        }
    }
    private fun mountedGloryTeleport(player : Player, int : Int) {
        Executors.newSingleThreadScheduledExecutor().schedule({
        player.pulseManager.run(object : Pulse() {
            var counter = 0
            override fun pulse(): Boolean {
                when (counter++) {
                    1 -> {
                        player.lock(5)
                        player.visualize(Animation(714), Graphics(308, 100, 50))
                        player.getAudioManager().send(Audio(200), true);
                    }
                    4 -> player.animator.reset().also { teleport(player, TELEPORTS[int]) }
                }
                return false
            }
        })
        }, 0, TimeUnit.SECONDS)
    }
}