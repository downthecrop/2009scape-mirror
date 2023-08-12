package content

import TestUtils
import content.global.ame.RandomEventManager
import core.game.node.item.Item
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.rs09.consts.Items
import content.global.ame.RandomEventNPC
import core.api.*
import core.game.system.timer.impl.AntiMacro
import org.json.simple.JSONObject

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
        val p = TestUtils.getMockPlayer("shitforbrains2")
        p.setAttribute("tutorial:complete", true)
        RandomEventManager().login(p)

        p.inventory.add(Item(4151, 5))
        content.global.ame.RandomEventManager.getInstance(p)?.fireEvent()
        p.getAttribute<RandomEventNPC>("re-npc")!!.noteAndTeleport()

        Assertions.assertEquals(5, p.inventory.getAmount(4152))
        Assertions.assertEquals(0, p.inventory.getAmount(4151))
    }

    @Test fun teleportAndNotePunishmentShouldNotAffectUnnotableItems() {
        val p = TestUtils.getMockPlayer("shitforbrains3")
        p.setAttribute("tutorial:complete", true)
        RandomEventManager().login(p)

        p.inventory.add(Item(Items.AIR_RUNE_556, 30))
        content.global.ame.RandomEventManager.getInstance(p)?.fireEvent()
        p.getAttribute<RandomEventNPC>("re-npc")!!.noteAndTeleport()

        Assertions.assertEquals(30, p.inventory.getAmount(Items.AIR_RUNE_556))
    }
}