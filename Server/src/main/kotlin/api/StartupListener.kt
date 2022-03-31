package api

import rs09.game.system.SystemLogger

interface StartupListener {
    /**
     * NOTE: This should NOT reference nonstatic class-local variables.
     */
    fun startup()
}