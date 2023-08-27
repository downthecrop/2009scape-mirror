package content.minigame.blastfurnace

import core.api.animateScenery
import core.api.getScenery
import core.api.replaceScenery
import core.game.world.map.Location

class BFSceneryController {
    fun updateBreakable (potPipeBroken: Boolean, pumpPipeBroken: Boolean, beltBroken: Boolean, cogBroken: Boolean) {
        val beltObj = getScenery(beltGearRight)!!
        val gearObj = getScenery(cogRightLoc)!!
        val potPipe = getScenery(potPipeLoc)!!
        val pumpPipe = getScenery(pumpPipeLoc)!!

        if (potPipeBroken && potPipe.id != BROKEN_POT_PIPE)
            replaceScenery(potPipe, BROKEN_POT_PIPE, -1)
        else if (!potPipeBroken && potPipe.id == BROKEN_POT_PIPE)
            replaceScenery(potPipe, DEFAULT_POT_PIPE, -1)

        if (pumpPipeBroken && pumpPipe.id != BROKEN_PUMP_PIPE)
            replaceScenery(pumpPipe, BROKEN_PUMP_PIPE, -1)
        else if (!pumpPipeBroken && pumpPipe.id == BROKEN_PUMP_PIPE)
            replaceScenery(pumpPipe, DEFAULT_PUMP_PIPE, -1)

        if (beltBroken && beltObj.id != BROKEN_BELT)
            replaceScenery(beltObj, BROKEN_BELT, -1)
        else if (!beltBroken && beltObj.id == BROKEN_BELT)
            replaceScenery(beltObj, DEFAULT_BELT, -1)

        if (cogBroken && gearObj.id != BROKEN_COG)
            replaceScenery(gearObj, BROKEN_COG, -1)
        else if (!cogBroken && gearObj.id == BROKEN_COG)
            replaceScenery(gearObj, DEFAULT_COG, -1)
    }

    fun updateAnimations (pedaling: Boolean, beltBroken: Boolean, cogBroken: Boolean) {
        val belt1 = getScenery(belt1Loc)!!
        val belt2 = getScenery(belt2Loc)!!
        val belt3 = getScenery(belt3Loc)!!
        val beltGearLeft = getScenery(beltGearLeft)!!
        val beltGearRight = getScenery(beltGearRight)!!
        val cogLeft = getScenery(cogLeftLoc)!!
        val cogRight = getScenery(cogRightLoc)!!
        val cogCenter = getScenery(centralGearLoc)!!

        val beltAnim = if (pedaling && !beltBroken && !cogBroken) BELT_ANIM else -1
        val gearAnim = if (pedaling && !beltBroken && !cogBroken) GEAR_ANIM else -1

        animateScenery(belt1, beltAnim)
        animateScenery(belt2, beltAnim)
        animateScenery(belt3, beltAnim)
        animateScenery(beltGearLeft, gearAnim)
        animateScenery(beltGearRight, gearAnim)
        animateScenery(cogLeft, gearAnim)
        animateScenery(cogRight, gearAnim)
        animateScenery(cogCenter, gearAnim)
    }

    fun updateStove (temp: Int) {
        val stoveObj = getScenery(stoveLoc)!!

        if (temp >= 67 && stoveObj.id != STOVE_HOT)
            replaceScenery(stoveObj, STOVE_HOT, -1)
        else if (temp in 34..66 && stoveObj.id != STOVE_WARM)
            replaceScenery(stoveObj, STOVE_WARM, -1)
        else if (temp in 0..33 && stoveObj.id != STOVE_COLD)
            replaceScenery(stoveObj, STOVE_COLD, -1)
    }

    fun resetAllScenery() {
        val beltObj = getScenery(beltGearRight)!!
        val gearObj = getScenery(cogRightLoc)!!
        val potPipe = getScenery(potPipeLoc)!!
        val pumpPipe = getScenery(pumpPipeLoc)!!
        val stoveObj = getScenery(stoveLoc)!!
        replaceScenery(gearObj, DEFAULT_COG, -1)
        replaceScenery(beltObj, DEFAULT_BELT, -1)
        replaceScenery(pumpPipe, DEFAULT_PUMP_PIPE, -1)
        replaceScenery(potPipe, DEFAULT_POT_PIPE, -1)
        replaceScenery(stoveObj, STOVE_COLD, -1)
    }

    companion object {
        val belt1Loc       = Location(1943, 4967, 0)
        val belt2Loc       = Location(1943, 4966, 0)
        val belt3Loc       = Location(1943, 4965, 0)
        var potPipeLoc     = Location(1943, 4961, 0)
        var pumpPipeLoc    = Location(1947, 4961, 0)
        var cogLeftLoc     = Location(1945, 4965, 0)
        var cogRightLoc    = Location(1945, 4967, 0)
        var beltGearLeft   = Location(1944, 4965, 0)
        var beltGearRight  = Location(1944, 4967, 0)
        var centralGearLoc = Location(1945, 4966, 0)
        var stoveLoc       = Location(1948, 4963, 0)

        val DEFAULT_BELT = 9102
        val BROKEN_BELT = 9103
        val DEFAULT_COG = 9104
        val BROKEN_COG = 9105
        val DEFAULT_POT_PIPE = 9116
        val BROKEN_POT_PIPE = 9117
        val DEFAULT_PUMP_PIPE = 9120
        val BROKEN_PUMP_PIPE = 9121
        val STOVE_COLD = 9085
        val STOVE_WARM = 9086
        val STOVE_HOT = 9087
        val BELT_ANIM = 2435
        val GEAR_ANIM = 2436
    }
}