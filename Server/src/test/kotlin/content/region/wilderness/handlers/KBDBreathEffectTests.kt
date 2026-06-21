package content.region.wilderness.handlers

import TestUtils
import core.api.getTimer
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.equipment.FireType
import core.game.system.timer.impl.Poison
import core.game.world.map.Location
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class KBDBreathEffectTests {
    init {
        TestUtils.preTestSetup()
    }

    @Test fun toxicBreathShouldPoisonOnImpactNotOnLaunch() {
        val kbd = KingBlackDragonNPC(50, Location.create(2273, 4698, 0))
        val handler = kbd.getSwingHandler(false)
        // KBD rolls a random FireType per swing, force toxic breath for determinism
        val fireTypeField = handler.javaClass.getDeclaredField("fireType")
        fireTypeField.isAccessible = true
        fireTypeField.set(handler, FireType.TOXIC_BREATH)

        TestUtils.getMockPlayer("kbdbreathtiming").use { player ->
            val state = BattleState(kbd, player)
            state.estimatedHit = 0

            handler.adjustBattleState(kbd, player, state)
            Assertions.assertNull(getTimer<Poison>(player), "Breath effect must not apply on the launch tick.")

            handler.impact(kbd, player, state)
            Assertions.assertNotNull(getTimer<Poison>(player), "Breath effect must apply when the attack lands.")
        }
    }

    @Test fun toxicBreathShouldNotPoisonVictimWhoTeleportedMidFlight() {
        val kbd = KingBlackDragonNPC(50, Location.create(2273, 4698, 0))
        val handler = kbd.getSwingHandler(false)
        val fireTypeField = handler.javaClass.getDeclaredField("fireType")
        fireTypeField.isAccessible = true
        fireTypeField.set(handler, FireType.TOXIC_BREATH)

        TestUtils.getMockPlayer("kbdbreathteleport").use { player ->
            val state = BattleState(kbd, player)
            state.estimatedHit = 0

            handler.adjustBattleState(kbd, player, state)
            // Disable impacts like TeleportManager.send does for a real teleport
            player.impactHandler.setDisabledTicks(12)

            handler.impact(kbd, player, state)
            Assertions.assertNull(getTimer<Poison>(player), "Breath effect must not apply to a victim whose impacts are disabled (teleported away).")
        }
    }
}
