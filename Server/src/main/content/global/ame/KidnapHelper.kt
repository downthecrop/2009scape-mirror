package content.global.ame

import core.ServerConstants
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager.TeleportType
import core.game.world.map.Location

fun kidnapPlayer(player: Player, loc: Location, type: TeleportType) {
    setAttribute(player, "kidnapped-by-random", true) //only used in POH code when you leave the hut, so does not need /save. Do not rely on this outside of its intended POH use case.
    if (getAttribute(player, "/save:original-loc", null) == null) {
        setAttribute(player, "/save:original-loc", player.location)
    }
    teleport(player, loc, type)
}

fun returnPlayer(player: Player) {
    player.locks.unlockTeleport()
    val destination = getAttribute(player, "/save:original-loc", ServerConstants.HOME_LOCATION ?: Location.create(3222, 3218, 0))
    teleport(player, destination)
    unlock(player)
    removeAttributes(player, "/save:original-loc", "kidnapped-by-random")
}
