package content.minigame.blastfurnace

import core.tools.RandomFunction

class BlastState {
    var disableBreaking = false
    var forceBreaking = false
    var potPipeBroken = false
    var pumpPipeBroken = false
    var beltBroken = false
    var cogBroken = false
    var ticksElapsed = 0

    var stoveTemp = 0
        private set
    var furnaceTemp = 0
        private set
    var cokeInStove = 0
        private set

    fun tick(pumping: Boolean, pedaling: Boolean) {
        ticksElapsed++

        adjustStoveTemperature()
        adjustFurnaceTemperature(pumping)

        checkForCokeDecrement()
        checkForBreakage(pedaling, pumping)
    }

    private fun adjustStoveTemperature() {
        if (cokeInStove > 0)
            stoveTemp = (stoveTemp + 1).coerceAtMost(100)
        else stoveTemp = (stoveTemp - 1).coerceAtLeast(0)
    }

    private fun adjustFurnaceTemperature(pumping: Boolean) {
        if (pumping && !pumpPipeBroken && !potPipeBroken && !beltBroken && !cogBroken) {
            when (stoveTemp) {
                in 1..32 -> furnaceTemp += 1
                in 32..66 -> furnaceTemp += 2
                in 67..100 -> furnaceTemp += 3
            }
        } else furnaceTemp--

        furnaceTemp = furnaceTemp
            .coerceAtLeast(0)
            .coerceAtMost(100)
    }

    private fun checkForBreakage(pedaling: Boolean, pumping: Boolean) {
        if (disableBreaking) return
        if (pumping && (!potPipeBroken || !pumpPipeBroken)) {
            if (RandomFunction.roll(50) || forceBreaking) {
                if (RandomFunction.nextBool()) potPipeBroken = true
                else pumpPipeBroken = true
            }
        }

        if (pedaling && (!beltBroken || !cogBroken)) {
            if (RandomFunction.roll(50) || forceBreaking)
                beltBroken = true
            else if (RandomFunction.roll(50) || forceBreaking)
                cogBroken = true
        }
    }

    private fun checkForCokeDecrement() {
        if (ticksElapsed % 10 == 0)
            cokeInStove = (cokeInStove - 1).coerceAtLeast(0)
    }

    fun addCoke (amount: Int) {
        cokeInStove += amount.coerceAtMost(BlastConsts.COKE_LIMIT - cokeInStove)
    }
}