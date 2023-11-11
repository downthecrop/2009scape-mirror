package core.game.worldevents.holiday.christmas.randoms

import core.api.getAttribute
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.worldevents.holiday.HolidayRandoms
import core.tools.RandomFunction

class SnowmanFightBehavior : NPCBehavior() {
    override fun onDeathStarted(self: NPC, killer: Entity) {
        val holidayRandomPlayer = getAttribute<Player?>(self, "holidayrandomplayer", null) ?: return
        HolidayRandoms.terminateEventNpc(holidayRandomPlayer)
    }

    override fun beforeAttackFinalized(self: NPC, victim: Entity, state: BattleState) {
        state.estimatedHit = RandomFunction.getRandom(1)
    }
}