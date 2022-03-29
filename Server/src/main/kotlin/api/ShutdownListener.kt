package api

import rs09.game.system.SystemLogger

interface ShutdownListener {
    /**
     * NOTE: This should NOT reference nonstatic class-local variables.
     */
    fun shutdown()
}