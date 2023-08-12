package content

import TestUtils
import content.global.ame.RandomEventManager
import core.game.node.item.Item
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.rs09.consts.Items
import content.global.ame.RandomEventNPC
import core.api.getTimer
import core.api.getWorldTicks
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
            p.relog()
            Assertions.assertEquals(getWorldTicks() + 666, getTimer<AntiMacro>(p)!!.nextExecution)
        }
    }

    @Test fun delayShouldBeRestartedOnceDepleted() {
        TestUtils.getMockPlayer("delayrestartoncedepleted").use { p ->
            val timer = getTimer<AntiMacro>(p) ?: Assertions.fail("AntiMacro timer is null.")
            TestUtils.advanceTicks(2, false)
            timer.nextExecution = 5 //run in 5 ticks
            TestUtils.advanceTicks(5, false)
            Assertions.assertNotEquals(6, timer.nextExecution)
            Assertions.assertEquals(true, timer.nextExecution - getWorldTicks() >= AntiMacro.MIN_DELAY_TICKS)
            Assertions.assertEquals(true, timer.nextExecution - getWorldTicks() <= AntiMacro.MAX_DELAY_TICKS)
        }
    }

    @Test fun shouldSpawnRandomEventWithinMAXTICKSGivenNoRestrictions() {
        val p = TestUtils.getMockPlayer("Bill")
        p.setAttribute("tutorial:complete", true) //tutorial has to be complete to spawn randoms
        RandomEventManager.MIN_DELAY_TICKS = 10
        RandomEventManager.MAX_DELAY_TICKS = 20
        RandomEventManager().login(p)
        TestUtils.advanceTicks(RandomEventManager.MAX_DELAY_TICKS + 5)
        RandomEventManager.MIN_DELAY_TICKS = 3000
        RandomEventManager.MAX_DELAY_TICKS = 9000
        Assertions.assertNotNull(p.getAttribute("re-npc", null))
    }

    @Test fun teleportAndNotePunishmentShouldNotAffectAlreadyNotedItems() {
        val p = TestUtils.getMockPlayer("Shitforbrains")
        p.setAttribute("tutorial:complete", true)
        RandomEventManager().login(p)

        p.inventory.add(Item(Items.RAW_SHARK_384, 1000))
        content.global.ame.RandomEventManager.getInstance(p)?.fireEvent()
        p.getAttribute<RandomEventNPC>("re-npc")!!.noteAndTeleport()

        Assertions.assertEquals(1000, p.inventory.getAmount(Items.RAW_SHARK_384))
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