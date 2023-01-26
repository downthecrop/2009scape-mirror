package core.game.interaction

import core.api.StartupListener

interface Listener : StartupListener {
    override fun startup() {
        defineListeners()
    }

    fun defineListeners()
}