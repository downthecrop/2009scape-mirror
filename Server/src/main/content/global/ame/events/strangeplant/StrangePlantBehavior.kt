package content.global.ame.events.strangeplant

import core.api.applyPoison
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.system.timer.impl.AntiMacro
import core.tools.RandomFunction
import org.rs09.consts.NPCs

class StrangePlantBehavior() : NPCBehavior(NPCs.STRANGE_PLANT_408) {
    override fun canBeAttackedBy(self: NPC, attacker: Entity, style: CombatStyle, shouldSendMessage: Boolean): Boolean {
        return !(attacker !is Player || AntiMacro.getEventNpc(attacker.asPlayer()) != self)
    }

    override fun beforeAttackFinalized(self: NPC, victim: Entity, state: BattleState) {
        state.estimatedHit = RandomFunction.getRandom(3)
        if (RandomFunction.roll(10))
            applyPoison(victim, self, 10)
    }

    override fun beforeDamageReceived(self: NPC, attacker: Entity, state: BattleState) {
        if (state.estimatedHit > 3)
            state.estimatedHit = RandomFunction.getRandom(3)
        if (state.secondaryHit > 3)
            state.secondaryHit = RandomFunction.getRandom(3)
    }

    override fun onDeathStarted(self: NPC, killer: Entity) {
        AntiMacro.terminateEventNpc(killer.asPlayer())
    }
}