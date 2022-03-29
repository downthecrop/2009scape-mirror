package api

import rs09.game.system.SystemLogger

interface ShutdownListener {
    fun shutdown()
}