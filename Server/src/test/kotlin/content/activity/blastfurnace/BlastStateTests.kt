package content.activity.blastfurnace

import content.minigame.blastfurnace.BlastState
import content.minigame.blastfurnace.BlastConsts
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BlastStateTests {
    @Test
    fun tickPassedWithCokeInStoveShouldIncreaseStoveTemp() {
        val state = BlastState(); state.disableBreaking = true
        state.addCoke(1)
        Assertions.assertEquals(0, state.stoveTemp)

        state.tick(true, false)
        Assertions.assertEquals(1, state.stoveTemp)
    }

    @Test
    fun stoveTempShouldNeverExceed100() {
        val state = BlastState(); state.disableBreaking = true
        state.addCoke(BlastConsts.COKE_LIMIT)

        for (i in 0 until 150)
            state.tick(true, false)
        Assertions.assertEquals(100, state.stoveTemp)
    }

    @Test fun cokeShouldDisappearFromStove() {
        val state = BlastState(); state.disableBreaking = true
        state.addCoke(1)
        for (i in 0 until 10)
            state.tick(true, false)
        Assertions.assertEquals(0, state.cokeInStove)
    }

    @Test fun stoveTempShouldLowerWithoutCoke() {
        val state = BlastState(); state.disableBreaking = true
        state.addCoke(1)
        for (i in 0 until 10)
            state.tick(true, false)
        Assertions.assertEquals(10, state.stoveTemp)
        for (i in 0 until 10)
            state.tick(true, false)
        Assertions.assertEquals(0, state.stoveTemp)
    }

    @Test
    fun stoveTempShouldNeverGoBelow0() {
        val state = BlastState(); state.disableBreaking = true
        for (i in 0 until 100)
            state.tick(true, false)
        Assertions.assertEquals(0, state.stoveTemp)
    }

    @Test fun furnaceTempShouldIncreaseProportionallyToStoveTempWhilePumping() {
        val testData = arrayOf(
            Pair(0, 0),
            Pair(1, 1),
            Pair(4, 2),
            Pair(7, 3)
        )
        for ((cokeToAdd, expectedTempRise) in testData) {
            val state = BlastState(); state.disableBreaking = true
            state.addCoke(cokeToAdd)
            for (i in 0 until state.cokeInStove * 10)
                state.tick(false, false)

            state.tick(true, false)
            Assertions.assertEquals(expectedTempRise, state.furnaceTemp)
        }
    }

    @Test fun pumpingShouldNotTransferHeatIfPipesBroken() {
        val state1 = BlastState(); state1.disableBreaking = true
        state1.addCoke(BlastConsts.COKE_LIMIT)
        state1.pumpPipeBroken = true
        for (i in 0 until 20) state1.tick(true, false)
        Assertions.assertEquals(0, state1.furnaceTemp)

        val state2 = BlastState(); state2.disableBreaking = true
        state2.addCoke(BlastConsts.COKE_LIMIT)
        state2.potPipeBroken = true
        for (i in 0 until 20) state2.tick(true, false)
        Assertions.assertEquals(0, state2.furnaceTemp)
    }

    @Test fun pumpingShouldNotTransferHeatIfBeltBroken() {
        val state = BlastState(); state.disableBreaking = true
        state.addCoke(BlastConsts.COKE_LIMIT)
        state.beltBroken = true
        for (i in 0 until 20) state.tick(true, false)
        Assertions.assertEquals(0, state.furnaceTemp)
    }
}