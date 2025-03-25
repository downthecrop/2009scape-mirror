package content.global.ame

import core.ServerConstants
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager.TeleportType
import core.game.world.map.Location

fun kidnapPlayer(player: Player, loc: Location, type: TeleportType) {
    setAttribute(player, "kidnapped-by-random", true)
    if (getAttribute(player, "/save:original-loc", null) == null) {
        setAttribute(player, "/save:original-loc", player.location)
    }
    teleport(player, loc, type)
}

fun returnPlayer(player: Player) {
    // Prevent returning more than once and sending the player back to HOME_LOCATION
    if (getAttribute<Location?>(player, "kidnapped-by-random", null) == null) {
        return
    }

    player.locks.unlockTeleport()
    val destination = getAttribute(player, "/save:original-loc", ServerConstants.HOME_LOCATION ?: Location.create(3222, 3218, 0))
    teleport(player, destination)
    unlock(player)
    removeAttributes(player, "/save:original-loc", "kidnapped-by-random")
}