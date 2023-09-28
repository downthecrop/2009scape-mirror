package core.game.worldevents.holiday

import core.game.worldevents.holiday.halloween.randoms.*

enum class HolidayRandomEvents(val npc: HolidayRandomEventNPC, val type: String) {
    BLACK_CAT(npc = BlackCatHolidayRandomNPC(), "halloween"),
    SPIDER(npc = SpiderHolidayRandomNPC(), "halloween"),
    GHOST(npc = GhostHolidayRandomNPC(), "halloween"),
    ZOMBIE(npc = ZombieHolidayRandomNPC(), "halloween"),
    WITCH(npc = WitchHolidayRandomNPC(), "halloween"),
    DEATH(npc = DeathHolidayRandomNPC(), "halloween"),
    VAMPIRE(npc = VampireHolidayRandomNPC(), "halloween");

    companion object {
        @JvmField
        val halloweenEventsList = ArrayList<HolidayRandomEvents>()
        val christmasEventsList = ArrayList<HolidayRandomEvents>()

        init {
            populateMappings()
        }

        fun getHolidayRandom(type: String): HolidayRandomEvents {
            return when (type) {
                "halloween" -> halloweenEventsList.random()
                "christmas" -> christmasEventsList.random()
                else -> throw Exception("Invalid event type!")
            }
        }

        private fun populateMappings() {
            for (event in values()) {
                when(event.type) {
                    "halloween" -> halloweenEventsList.add(event)
                    "christmas" -> christmasEventsList.add(event)
                }
            }
        }
    }
}
