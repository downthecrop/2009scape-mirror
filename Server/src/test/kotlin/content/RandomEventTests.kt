package content

import TestUtils
import content.global.ame.RandomEventNPC
import core.api.*
import core.game.system.timer.impl.AntiMacro
import core.game.world.map.zone.impl.WildernessZone
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.rs09.consts.Items

class RandomEventTests {
    init {
        TestUtils.preTestSetup()
    }

    @Test fun loginShouldRegisterRandomEventTimer() {
        TestUtils.getMockPlayer("antimacroAutoRegister").use { p ->
            Assertions.assertNotNull(getTimer<AntiMacro>(p))
        }
    }

    @Test fun loginShouldSetNextSpawn() {
        TestUtils.getMockPlayer("antimacroautosetsnextspawnifunset").use { p ->
            val timer = getTimer<AntiMacro>(p) ?: Assertions.fail("AntiMacro timer is null.")
            Assertions.assertNotEquals(getWorldTicks(), timer.nextExecution)
            Assertions.assertEquals(true, timer.nextExecution - getWorldTicks() >= AntiMacro.MIN_DELAY_TICKS)
            Assertions.assertEquals(true, timer.nextExecution - getWorldTicks() <= AntiMacro.MAX_DELAY_TICKS)
        }
    }

    @Test fun remainingDelayShouldPersistRelog() {
        TestUtils.getMockPlayer("antimacrotimeremainingpersists").use { p ->
            val timer = getTimer<AntiMacro>(p) ?: Assertions.fail("AntiMacro timer is null.")
            timer.nextExecution = getWorldTicks() + 666
            p.relog(ticksToWait = 100)
            Assertions.assertEquals(getWorldTicks() + 666, getTimer<AntiMacro>(p)!!.nextExecution)
        }
    }

    @Test fun delayShouldBeRestartedOnceDepleted() {
        TestUtils.getMockPlayer("delayrestartoncedepleted").use { p ->
            val timer = getTimer<AntiMacro>(p) ?: Assertions.fail("AntiMacro timer is null.")
            TestUtils.advanceTicks(2, false)
            timer.nextExecution = 5 //run in 5 ticks
            TestUtils.advanceTicks(5, false)
            Assertions.assertEquals(true, timer.nextExecution - getWorldTicks() >= AntiMacro.MIN_DELAY_TICKS)
            Assertions.assertEquals(true, timer.nextExecution - getWorldTicks() <= AntiMacro.MAX_DELAY_TICKS)
        }
    }

    @Test fun shouldSpawnRandomEventGivenNoRestrictions() {
        TestUtils.getMockPlayer("antimacroshouldspawnrandom").use {p ->
            val timer = getTimer<AntiMacro>(p) ?: Assertions.fail("AntiMacro timer is null!")
            TestUtils.advanceTicks(5, false)
            timer.nextExecution = getWorldTicks() + 5
            TestUtils.advanceTicks(10, false)
            Assertions.assertNotNull(p.getAttribute(AntiMacro.EVENT_NPC, null))
            Assertions.assertEquals(true, p.getAttribute<RandomEventNPC>(AntiMacro.EVENT_NPC).isActive)
        }
    }

    @Test fun teleportAndNotePunishmentShouldNotAffectAlreadyNotedItems() {
        TestUtils.getMockPlayer("teleportpunishment1").use {p ->
            val timer = getTimer<AntiMacro>(p) ?: Assertions.fail("AntiMacro timer is null!")
            TestUtils.advanceTicks(5, false)
            timer.nextExecution = getWorldTicks() + 5
            TestUtils.advanceTicks(10, false)

            addItem(p, Items.RAW_SHARK_384, 1000)
            getAttribute<RandomEventNPC?>(p, AntiMacro.EVENT_NPC, null)!!.noteAndTeleport()

            Assertions.assertEquals(1000, amountInInventory(p, Items.RAW_SHARK_384))
        }
    }

    @Test fun teleportAndNotePunishmentShouldNoteNotableUnnotedItems() {
        TestUtils.getMockPlayer("teleportpunishment2").use {p ->
            val timer = getTimer<AntiMacro>(p) ?: Assertions.fail("AntiMacro timer is null!")
            TestUtils.advanceTicks(5, false)
            timer.nextExecution = getWorldTicks() + 5
            TestUtils.advanceTicks(10, false)

            addItem(p, Items.ABYSSAL_WHIP_4151, 5)
            getAttribute<RandomEventNPC?>(p, AntiMacro.EVENT_NPC, null)!!.noteAndTeleport()

            Assertions.assertEquals(5, amountInInventory(p, Items.ABYSSAL_WHIP_4152))
            Assertions.assertEquals(0, amountInInventory(p, Items.ABYSSAL_WHIP_4151))
        }
    }

    @Test fun teleportAndNotePunishmentShouldNotAffectUnnotableItems() {
        TestUtils.getMockPlayer("teleportpunishment3").use {p ->
            val timer = getTimer<AntiMacro>(p) ?: Assertions.fail("AntiMacro timer is null!")
            TestUtils.advanceTicks(5, false)
            timer.nextExecution = getWorldTicks() + 5
            TestUtils.advanceTicks(10, false)

            addItem(p, Items.AIR_RUNE_556, 30)
            getAttribute<RandomEventNPC?>(p, AntiMacro.EVENT_NPC, null)!!.noteAndTeleport()

            Assertions.assertEquals(30, amountInInventory(p, Items.AIR_RUNE_556))
        }
    }

    @Test fun randomEventShouldNotSpawnInEventRestrictedArea() {
        //Wilderness is Random Event restricted
        WildernessZone().configure()
        TestUtils.getMockPlayer("antimacronospawninrestrictedzone").use { p ->
            p.location = WildernessZone.getInstance().borders[0].randomLoc
            val timer = getTimer<AntiMacro>(p) ?: Assertions.fail("AntiMacro timer was null!")
            TestUtils.advanceTicks(5, false)
            timer.nextExecution = getWorldTicks() + 5
            TestUtils.advanceTicks(10, false)
            Assertions.assertNull(AntiMacro.getEventNpc(p))
        }
    }

    @Test fun randomEventShouldNotSpawnIfOneAlreadyActive() {
        TestUtils.getMockPlayer("antimacronospawnifalreadyhas").use { p ->
            val timer = getTimer<AntiMacro>(p) ?: Assertions.fail("AntiMacro timer was null!")
            TestUtils.advanceTicks(5, false)
            timer.nextExecution = getWorldTicks() + 5
            TestUtils.advanceTicks(10, false)
            Assertions.assertNotNull(AntiMacro.getEventNpc(p))

            val previousEvent = AntiMacro.getEventNpc(p)

            timer.nextExecution = getWorldTicks() + 5
            TestUtils.advanceTicks(10, false)
            Assertions.assertNotNull(AntiMacro.getEventNpc(p))
            Assertions.assertEquals(previousEvent, AntiMacro.getEventNpc(p))
        }
    }

    @Test fun randomEventShouldSpawnIfCurrentOneIsInactive() {
        TestUtils.getMockPlayer("antimacrospawnifcurrentinactive").use { p ->
            val timer = getTimer<AntiMacro>(p) ?: Assertions.fail("AntiMacro timer was null!")
            TestUtils.advanceTicks(5, false)
            timer.nextExecution = getWorldTicks() + 5
            TestUtils.advanceTicks(10, false)
            Assertions.assertNotNull(AntiMacro.getEventNpc(p))

            val previousEvent = AntiMacro.getEventNpc(p)
            AntiMacro.terminateEventNpc(p)

            timer.nextExecution = getWorldTicks() + 5
            TestUtils.advanceTicks(10, false)
            Assertions.assertNotNull(AntiMacro.getEventNpc(p))
            Assertions.assertNotEquals(previousEvent, AntiMacro.getEventNpc(p))
        }
    }

    @Test fun randomEventSystemShouldSupportPauseAndUnpause() {
        TestUtils.getMockPlayer("antimacropause").use { p ->
            val timer = getTimer<AntiMacro>(p) ?: Assertions.fail("AntiMacro timer was null!")
            TestUtils.advanceTicks(5, false)
            timer.nextExecution = getWorldTicks() + 5

            AntiMacro.pause(p)
            TestUtils.advanceTicks(10, false)
            Assertions.assertNull(AntiMacro.getEventNpc(p))

            AntiMacro.unpause(p)
            timer.nextExecution = getWorldTicks() + 5
            TestUtils.advanceTicks(10, false)
            Assertions.assertNotNull(AntiMacro.getEventNpc(p))
        }
    }
}