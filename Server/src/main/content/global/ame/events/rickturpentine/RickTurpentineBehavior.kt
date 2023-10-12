package content.global.ame.events.rickturpentine

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs09.consts.NPCs

class RickTurpentineBehavior : NPCBehavior(NPCs.RICK_TURPENTINE_2476) {
    override fun beforeAttackFinalized(self: NPC, victim: Entity, state: BattleState) {
        state.estimatedHit = RandomFunction.getRandom(3)
    }
}