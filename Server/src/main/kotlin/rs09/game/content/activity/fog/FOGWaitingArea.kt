package rs09.game.content.activity.fog

import api.ContentAPI
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import rs09.game.system.SystemLogger

object FOGWaitingArea {
    var pulseStarted = false
    val currentlyWaiting = ArrayList<Player>()
    val activeSessions = ArrayList<FOGSession>()

    val pulse = object : Pulse(10){
        override fun pulse(): Boolean {
            SystemLogger.logInfo("Ticking")
            if(currentlyWaiting.size < 4) return false

            val sortedByCombat = currentlyWaiting.sortedBy { it.properties.currentCombatLevel }.toMutableList()
            if(sortedByCombat.size % 2 != 0) sortedByCombat.removeLast()

            var previousPlayer: Player? = null

            for(player in sortedByCombat){
                if(previousPlayer == null) previousPlayer = player
                else{
                    val session = FOGSession(previousPlayer, player)
                    session.start()
                    SystemLogger.logInfo("Starting session")
                    activeSessions.add(session)
                    previousPlayer = null
                }
            }

            return false
        }
    }

    fun register(player: Player): Boolean {
        if(!pulseStarted) ContentAPI.submitWorldPulse(pulse).also { pulseStarted = true }
        return currentlyWaiting.add(player)
    }

    fun clear(player: Player): Boolean {
        return currentlyWaiting.remove(player)
    }

    fun isFull(): Boolean {
        return activeSessions.size >= 125
    }
}