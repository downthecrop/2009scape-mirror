package core

import TestUtils
import core.api.closeInterface
import core.api.impact
import core.api.logWithStack
import core.api.registerTimer
import core.game.node.entity.Entity
import core.game.system.timer.RSTimer
import core.game.system.timer.TimerFlag
import core.tools.Log
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TimerTests {
    init { TestUtils.preTestSetup() }

    @Test fun timerWithNoFlagsShouldNotBeClearedOnDeath() {
        TestUtils.getMockPlayer("noflagnoclear").use { p ->
            var incrementer = 0
            val timer = object : RSTimer(1) {
                override fun run(entity: Entity): Boolean {
                    incrementer++
                    return true
                }
            }

            registerTimer(p, timer)
            impact(p, p.skills.lifepoints)
            TestUtils.advanceTicks(TestUtils.PLAYER_DEATH_TICKS, false)
            closeInterface(p) //close the interface that opens after death - as it would pause the timer

            TestUtils.advanceTicks(18, false)
            Assertions.assertEquals(20, incrementer)
        }
    }

    @Test fun timerWithClearOnDeathFlagShouldClearOnDeath() {
        TestUtils.getMockPlayer("clearflagtimer").use { p ->
            var incrementer = 0
            val timer = object : RSTimer(1, flags = arrayOf(TimerFlag.ClearOnDeath)) {
                override fun run(entity: Entity): Boolean {
                    incrementer++
                    return true
                }
            }

            registerTimer(p, timer)
            impact(p, p.skills.lifepoints)
            TestUtils.advanceTicks(TestUtils.PLAYER_DEATH_TICKS, false)
            closeInterface(p) //close the interface that opens after death - as it would pause the timer

            TestUtils.advanceTicks(18, false)
            Assertions.assertEquals(2, incrementer)
        }
    }
}