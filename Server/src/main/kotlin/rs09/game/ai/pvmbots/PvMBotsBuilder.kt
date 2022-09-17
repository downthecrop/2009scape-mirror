package rs09.game.ai.pvmbots

import core.game.world.map.Location
import rs09.game.ai.minigamebots.pestcontrol.PestControlTestBot
import rs09.game.ai.minigamebots.pestcontrol.PestControlTestBot2

// import sun.util.resources.CalendarData;
class PvMBotsBuilder {

    companion object {
        var botsSpawned = 0

        fun create(l: Location?): PvMBots {
            botsSpawned++
            return PvMBots(l)
        }

        @JvmStatic
        fun createPestControlTestBot2(l: Location?): PestControlTestBot2 {
            botsSpawned++
            return PestControlTestBot2(l!!)
        }
        @JvmStatic
        fun createPestControlTestBot(l: Location?): PestControlTestBot {
            botsSpawned++
            return PestControlTestBot(l!!)
        }

    }
}