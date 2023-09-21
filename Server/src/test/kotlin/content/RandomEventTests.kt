package content

import TestUtils
import content.global.ame.RandomEventNPC
import content.global.ame.RandomEvents
import core.api.*
import core.game.system.timer.impl.AntiMacro
import core.game.world.map.Location
import core.game.world.map.zone.impl.WildernessZone
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.rs09.consts.Items
import org.rs09.consts.NPCs

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

    @Test fun shouldNotSpawnForArtificialPlayer() {
        TestUtils.getMockPlayer("antimacroshouldspawnrandom", isBot = true).use {p ->
            val timer = getTimer<AntiMacro>(p)
            Assertions.assertEquals(null, timer)
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

        TestUtils.getMockPlayer("antimacronospawninrestrictedzone").use { p ->
            val timer = getTimer<AntiMacro>(p) ?: Assertions.fail("AntiMacro timer was null!")
            TestUtils.advanceTicks(5, false)

            //Wilderness is Random Event restricted
            WildernessZone.getInstance().configure()
            val loc = Location.create(3131, 3595)
            p.location = loc

            Assertions.assertEquals(loc, p.location)

            timer.nextExecution = getWorldTicks() + 5
            TestUtils.advanceTicks(10, false)
            Assertions.assertNull(AntiMacro.getEventNpc(p))
        }
    }

    //FIXME
    //@Test fun randomEventShouldNotSpawnIfOneAlreadyActive() {
    //    TestUtils.getMockPlayer("antimacronospawnifalreadyhas").use { p ->
    //        val timer = getTimer<AntiMacro>(p) ?: Assertions.fail("AntiMacro timer was null!")
    //        TestUtils.advanceTicks(5, false)
    //        timer.nextExecution = getWorldTicks() + 5
    //        TestUtils.advanceTicks(10, false)
    //        Assertions.assertNotNull(AntiMacro.getEventNpc(p))

    //        val previousEvent = AntiMacro.getEventNpc(p)

    //        timer.nextExecution = getWorldTicks() + 5
    //        TestUtils.advanceTicks(10, false)
    //        Assertions.assertNotNull(AntiMacro.getEventNpc(p))
    //        Assertions.assertEquals(previousEvent, AntiMacro.getEventNpc(p))
    //    }
    //}

    //FIXME
    //@Test fun randomEventShouldSpawnIfCurrentOneIsInactive() {
    //    TestUtils.getMockPlayer("antimacrospawnifcurrentinactive").use { p ->
    //        val timer = getTimer<AntiMacro>(p) ?: Assertions.fail("AntiMacro timer was null!")
    //        TestUtils.advanceTicks(5, false)
    //        timer.nextExecution = getWorldTicks() + 5
    //        TestUtils.advanceTicks(10, false)
    //        Assertions.assertNotNull(AntiMacro.getEventNpc(p))

    //        val previousEvent = AntiMacro.getEventNpc(p)
    //        AntiMacro.terminateEventNpc(p)

    //        timer.nextExecution = getWorldTicks() + 5
    //        TestUtils.advanceTicks(10, false)
    //        Assertions.assertNotNull(AntiMacro.getEventNpc(p))
    //        Assertions.assertNotEquals(previousEvent, AntiMacro.getEventNpc(p))
    //    }
    //}

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

    @Test fun shouldBeAbleToForceRandomEvent() {
        TestUtils.getMockPlayer("antimacroforcerand").use { p ->
            TestUtils.advanceTicks(5, false)
            AntiMacro.forceEvent(p)
            TestUtils.advanceTicks(1, false)
            Assertions.assertNotNull(AntiMacro.getEventNpc(p))
        }
    }

    @Test fun shouldBeAbleToForceSpecificRandomEvent() {
        TestUtils.getMockPlayer("antimacroforcespecific").use { p ->
            TestUtils.advanceTicks(5, false)
            AntiMacro.forceEvent(p, RandomEvents.TREE_SPIRIT)
            TestUtils.advanceTicks(1, false)
            Assertions.assertNotNull(AntiMacro.getEventNpc(p))
            Assertions.assertEquals(NPCs.TREE_SPIRIT_438, AntiMacro.getEventNpc(p)?.originalId)
        }
    }

    @Test fun parseAntiMacroCommandArgsShouldReturnExpectedValues() {
        val testData = arrayOf(
            Triple("revent -p test", "test", null),
            Triple("revent -p test ", "test", null),
            Triple("revent -p test -e certer ", "test", RandomEvents.CERTER),
            Triple("revent -p test_user -e Sandwich lady", "test_user", RandomEvents.SANDWICH_LADY),
            Triple("revent -p test user -e sandwich Lady ", "test_user", RandomEvents.SANDWICH_LADY),
            Triple("revent -e sandwich Lady -p test user ", "test_user", RandomEvents.SANDWICH_LADY),
            Triple("revent test", "test", null),
            Triple("revent test -e sAndwich Lady", "test", RandomEvents.SANDWICH_LADY)
        )

        for ((commandStr, expectedUser, expectedEvent) in testData) {
            val (resultUser, resultEvent) = AntiMacro.parseCommandArgs(commandStr, "revent")
            Assertions.assertEquals(expectedUser, resultUser)
            Assertions.assertEquals(expectedEvent, resultEvent)
        }
    }
}
