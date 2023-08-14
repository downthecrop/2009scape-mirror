package core

import TestUtils
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.skill.Skills
import core.game.system.timer.RSTimer
import core.game.system.timer.TimerFlag
import core.game.system.timer.impl.SkillRestore
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

    @Test fun skillRestoreTimerShouldSlowlyRaiseLoweredStats() {
        TestUtils.getMockPlayer("statrestore-slowrestore").use { p ->
            val timer = SkillRestore()
            registerTimer(p, timer)
            p.skills.staticLevels[Skills.FARMING] = 20
            setTempLevel(p, Skills.FARMING, 10)

            TestUtils.advanceTicks(timer.restoreTicks[Skills.FARMING] + 3, false)
            Assertions.assertEquals(11, getDynLevel(p, Skills.FARMING))
        }
    }
    @Test fun skillRestoreTimerShouldSlowlyLowerBoostedStats() {
        TestUtils.getMockPlayer("statrestore-slowdrain").use { p ->
            val timer = SkillRestore()
            p.timers.registerTimer(timer)
            setTempLevel(p, Skills.FARMING, 6)

            TestUtils.advanceTicks(timer.restoreTicks[Skills.FARMING] + 3, false)
            Assertions.assertEquals(5, getDynLevel(p, Skills.FARMING))
        }
    }

    @Test fun skillRestoreTimerShouldRaiseLoweredHp() {
        TestUtils.getMockPlayer("statrestore-raiseloweredhp").use { p ->
            val timer = SkillRestore()
            registerTimer(p, timer)
            p.skills.lifepoints /= 2
            TestUtils.advanceTicks(600, false)
            Assertions.assertEquals(10, p.skills.lifepoints)
        }
    }

    @Test fun skillRestoreTimerShouldNeverLowerBoostedHp() {
        TestUtils.getMockPlayer("statrestore-neverlowerhp").use { p ->
            val timer = SkillRestore()
            registerTimer(p, timer)
            p.skills.lifepoints = 50

            TestUtils.advanceTicks(500, false)
            Assertions.assertEquals(50, p.skills.lifepoints)
        }
    }
}