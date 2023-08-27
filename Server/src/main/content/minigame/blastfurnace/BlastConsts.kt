package content.minigame.blastfurnace

import core.game.world.map.Location

object BlastConsts {
    val ENTRANCE_LOC = Location (1940, 4958, 0)
    val EXIT_LOC = Location (2931, 10197, 0)
    val STAIRLOC_ENTRANCE = Location (2930, 10196)
    val STAIRLOC_EXIT = Location (1939, 4956)
    val STAIR_ENTRANCE_ID = 9084
    val STAIR_EXIT_ID = 9138
    val BELT = 9100
    val PEDALS = 9097
    val STOVE = intArrayOf(BFSceneryController.STOVE_COLD, BFSceneryController.STOVE_WARM, BFSceneryController.STOVE_HOT)
    val PUMP = 9090
    val COKE = 9088
    val TEMP_GAUGE = 9089
    val SINK = 9143
    val DISPENSER = intArrayOf(9093, 9094, 9095, 9096)

    val SMITH_REQ = 60
    val ENTRANCE_FEE = 2500
    val FEE_ENTRANCE_DURATION = 1000
    val COAL_LIMIT = 226
    val ORE_LIMIT = 28
    val BAR_LIMIT = 28
    val COKE_LIMIT = 15
}