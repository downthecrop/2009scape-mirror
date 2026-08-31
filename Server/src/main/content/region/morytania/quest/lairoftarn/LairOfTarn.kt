package content.region.morytania.quest.lairoftarn

/**
 * Lair of Tarn Razorlor is a miniquest that rewards the Salve Amulet (e)
 */

// Primary Source: https://www.youtube.com/watch?v=egdH7BM4WZk&t=16s
// Additional Sources:
// https://www.youtube.com/watch?v=dsbpqNzzmhA (lots of examines, thank you, zatch359.)
// https://www.youtube.com/watch?v=oBNDa1QzuNQ (player triggers floor traps)
// https://www.youtube.com/watch?v=Poz4OEyeIqY (more trap triggers)

class LairOfTarn {
    companion object {
        const val ATTR_KILLED_TARN = "/save:miniquest:lairoftarn-killedtarn"  // True after you kill Tarn (you can reset this with ::setqueststage 73 0)
        const val ATTR_LOG_20904_DISABLED = "miniquest:lairoftarn-log20904"   // True if you disable the trap. Clears once you cross the ledge.
        const val ATTR_LOG_20905_DISABLED = "miniquest:lairoftarn-log20905"   // True if you disable the trap. Clears once you cross the ledge.
        const val ATTR_REGION_BASE = "tarn-region"                            // used to track the instanced region base coords
        const val ATTR_TARN_CUTSCENE = "tarn-cutscene"                        // used to track if player is in a cutscene
    }
}