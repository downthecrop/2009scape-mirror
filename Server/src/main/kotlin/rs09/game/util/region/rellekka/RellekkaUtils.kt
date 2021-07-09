package rs09.game.util.region.rellekka

import api.ContentAPI
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs09.consts.Components

object RellekkaUtils {
    @JvmStatic
    fun sail(player: Player, destination: RellekkaDestination){
        ContentAPI.lock(player, 100)
        ContentAPI.openOverlay(player, 115)
        ContentAPI.openInterface(player, Components.MISC_SHIPJOURNEY_224)
        ContentAPI.animateInterface(player, Components.MISC_SHIPJOURNEY_224, 7, destination.shipAnim)

        val animDuration = ContentAPI.animationDuration(ContentAPI.getAnimation(destination.shipAnim))
        ContentAPI.submitWorldPulse(object : Pulse(animDuration){
            override fun pulse(): Boolean {
                ContentAPI.teleport(player, destination.destLoc)
                ContentAPI.closeInterface(player)
                ContentAPI.closeOverlay(player)
                ContentAPI.unlock(player)
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