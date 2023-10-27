package content.global.ame.events.swarm

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs09.consts.NPCs

class SwarmNPCBehavior : NPCBehavior(NPCs.SWARM_411) {
    override fun beforeAttackFinalized(self: NPC, victim: Entity, state: BattleState) {
        state.estimatedHit = RandomFunction.getRandom(1)
    }
}