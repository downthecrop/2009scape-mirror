package api

import core.game.node.entity.player.Player

interface LoginListener {
    fun login(player: Player)
}