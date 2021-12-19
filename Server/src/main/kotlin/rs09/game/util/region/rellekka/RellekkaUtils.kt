package rs09.game.util.region.rellekka

import api.*
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs09.consts.Components

object RellekkaUtils {
    @JvmStatic
    fun sail(player: Player, destination: RellekkaDestination){
        lock(player, 100)
        openOverlay(player, 115)
        openInterface(player, Components.MISC_SHIPJOURNEY_224)
        animateInterface(player, Components.MISC_SHIPJOURNEY_224, 7, destination.shipAnim)

        val animDuration = animationDuration(getAnimation(destination.shipAnim))
        submitWorldPulse(object : Pulse(animDuration){
            override fun pulse(): Boolean {
                teleport(player, destination.destLoc)
                closeInterface(player)
                closeOverlay(player)
                unlock(player)
                return true
            }
        })
    }
}

enum class RellekkaDestination(val destName: String, val destLoc: Location, val shipAnim: Int){
    RELLEKKA_TO_JATIZSO("Jatizso", Location.create(2421, 3781, 0), 5766),
    JATIZSO_TO_RELLEKKA("Rellekka", Location.create(2644, 3710, 0), 5767),
    RELLEKKA_TO_NEITIZNOT("Neitiznot",Location(2310, 3782, 0), 5764),
    NEITIZNOT_TO_RELLEKKA("Rellekka", Location(2644, 3710, 0), 5765)
}