package content

import TestUtils
import core.game.node.item.Item
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.rs09.consts.Items
import content.global.ame.RandomEventNPC
import core.game.world.GameWorld

class RandomEventManagerTests {
    companion object {init {
        TestUtils.preTestSetup()
    }}

    @Test fun loginShouldEnableManager() {
        val p = TestUtils.getMockPlayer("Bill")
        content.global.ame.RandomEventManager().login(p)
        val manager = content.global.ame.RandomEventManager.getInstance(p)
        Assertions.assertNotNull(manager)
        Assertions.assertEquals(true, manager!!.enabled)
    }

    @Test fun loginShouldSetNextSpawn() {
        val p = TestUtils.getMockPlayer("Bill")
        content.global.ame.RandomEventManager().login(p)
        val manager = content.global.ame.RandomEventManager.getInstance(p)
        Assertions.assertNotNull(manager)
        Assertions.assertEquals(true, manager!!.nextSpawn > GameWorld.ticks)
    }

    @Test fun shouldSpawnRandomEventWithinMAXTICKSGivenNoRestrictions() {
        val p = TestUtils.getMockPlayer("Bill")
        p.setAttribute("tutorial:complete", true) //tutorial has to be complete to spawn randoms
        content.global.ame.RandomEventManager().login(p)
        TestUtils.advanceTicks(content.global.ame.RandomEventManager.MAX_DELAY_TICKS + 5)
        Assertions.assertNotNull(p.getAttribute("re-npc", null))
    }

    @Test fun teleportAndNotePunishmentShouldNotAffectAlreadyNotedItems() {
        val p = TestUtils.getMockPlayer("Shitforbrains")
        p.setAttribute("tutorial:complete", true)
        content.global.ame.RandomEventManager().login(p)

        p.inventory.add(Item(Items.RAW_SHARK_384, 1000))
        content.global.ame.RandomEventManager.getInstance(p)?.fireEvent()
        p.getAttribute<RandomEventNPC>("re-npc")!!.noteAndTeleport()

        Assertions.assertEquals(1000, p.inventory.getAmount(Items.RAW_SHARK_384))
    }

    @Test fun teleportAndNotePunishmentShouldNoteNotableUnnotedItems() {
        val p = TestUtils.getMockPlayer("shitforbrains2")
        p.setAttribute("tutorial:complete", true)
        content.global.ame.RandomEventManager().login(p)

        p.inventory.add(Item(4151, 5))
        content.global.ame.RandomEventManager.getInstance(p)?.fireEvent()
        p.getAttribute<RandomEventNPC>("re-npc")!!.noteAndTeleport()

        Assertions.assertEquals(5, p.inventory.getAmount(4152))
        Assertions.assertEquals(0, p.inventory.getAmount(4151))
    }

    @Test fun teleportAndNotePunishmentShouldNotAffectUnnotableItems() {
        val p = TestUtils.getMockPlayer("shitforbrains3")
        p.setAttribute("tutorial:complete", true)
        content.global.ame.RandomEventManager().login(p)

        p.inventory.add(Item(Items.AIR_RUNE_556, 30))
        content.global.ame.RandomEventManager.getInstance(p)?.fireEvent()
        p.getAttribute<RandomEventNPC>("re-npc")!!.noteAndTeleport()

        Assertions.assertEquals(30, p.inventory.getAmount(Items.AIR_RUNE_556))
    }
}