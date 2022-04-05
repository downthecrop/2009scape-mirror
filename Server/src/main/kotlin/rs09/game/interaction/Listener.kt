package rs09.game.interaction

import api.StartupListener

interface Listener : StartupListener {
    override fun startup() {
        defineListeners()
    }

    fun defineListeners()
}