package api

import core.game.node.entity.player.Player

interface LogoutListener {
    fun logout(player: Player)
}