package rs09.game.interaction

import api.StartupListener

interface Listener : StartupListener {
    fun defineListeners()
    override fun startup() {
        defineListeners()
    }
}