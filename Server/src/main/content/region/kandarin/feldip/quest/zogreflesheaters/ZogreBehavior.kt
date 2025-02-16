package content.region.kandarin.feldip.quest.zogreflesheaters

import core.api.getOrStartTimer
import core.api.inEquipment
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.system.timer.impl.Disease
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class ZogreBehavior : NPCBehavior(*zogreIds) {
    companion object {
        private val zogreIds = intArrayOf(
                NPCs.ZOGRE_2044,
                NPCs.ZOGRE_2045,
                NPCs.ZOGRE_2046,
                NPCs.ZOGRE_2047,
                NPCs.ZOGRE_2048,
                NPCs.ZOGRE_2049,
                NPCs.ZOGRE_2051,
                NPCs.ZOGRE_2052,
                NPCs.ZOGRE_2053,
                NPCs.ZOGRE_2054,
                NPCs.ZOGRE_2055,
        )
    }

    override fun beforeDamageReceived(self: NPC, attacker: Entity, state: BattleState) {
        if (attacker is Player) {
            if (inEquipment(attacker, Items.COMP_OGRE_BOW_4827)) {
                return
            }
            state.estimatedHit = (state.estimatedHit * 0.25).toInt()
            if (state.secondaryHit > 0) {
                state.secondaryHit = (state.secondaryHit * 0.25).toInt()
            }
        }
    }

    override fun beforeAttackFinalized(self: NPC, victim: Entity, state: BattleState) {
        val disease = getOrStartTimer<Disease>(victim, 10)
        disease.hitsLeft = 10
    }
}