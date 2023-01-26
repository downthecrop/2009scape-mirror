package core.game.bots

import core.game.world.map.Location
import content.minigame.pestcontrol.bots.PestControlTestBot
import content.minigame.pestcontrol.bots.PestControlTestBot2

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