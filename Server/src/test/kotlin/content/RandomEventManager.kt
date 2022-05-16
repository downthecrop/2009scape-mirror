package content

import TestUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import rs09.game.world.GameWorld

class RandomEventManager {
    companion object {init {
        TestUtils.preTestSetup()
    }}

    @Test fun loginShouldEnableManager() {
        val p = TestUtils.getMockPlayer("Bill")
        rs09.game.content.ame.RandomEventManager().login(p)
        val manager = rs09.game.content.ame.RandomEventManager.getInstance(p)
        Assertions.assertNotNull(manager)
        Assertions.assertEquals(true, manager!!.enabled)
    }

    @Test fun loginShouldSetNextSpawn() {
        val p = TestUtils.getMockPlayer("Bill")
        rs09.game.content.ame.RandomEventManager().login(p)
        val manager = rs09.game.content.ame.RandomEventManager.getInstance(p)
        Assertions.assertNotNull(manager)
        Assertions.assertEquals(true, manager!!.nextSpawn > GameWorld.ticks)
    }

    @Test fun shouldSpawnRandomEventWithinMAXTICKSGivenNoRestrictions() {
        val p = TestUtils.getMockPlayer("Bill")
        p.setAttribute("tutorial:complete", true) //tutorial has to be complete to spawn randoms
        rs09.game.content.ame.RandomEventManager().login(p)
        TestUtils.advanceTicks(rs09.game.content.ame.RandomEventManager.MAX_DELAY_TICKS + 5)
        Assertions.assertNotNull(p.getAttribute("re-npc", null))
    }
}