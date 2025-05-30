package content.region.kandarin.feldip.quest.zogreflesheaters

import core.api.getOrStartTimer
import core.api.inEquipment
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.system.timer.impl.Disease
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class SkogreBehavior : NPCBehavior(*skogreIds) {
    companion object {
        @JvmField
        val skogreIds = intArrayOf(
                NPCs.SKOGRE_2050,
                NPCs.SKOGRE_2056,
                NPCs.SKOGRE_2057,
        )
    }

    override fun beforeDamageReceived(self: NPC, attacker: Entity, state: BattleState) {
        if (attacker is Player) {
            if (inEquipment(attacker, Items.COMP_OGRE_BOW_4827)) {
                return
            }
            if (state.spell != null && state.spell.type == SpellType.CRUMBLE_UNDEAD) {
                state.estimatedHit = (state.estimatedHit * 0.5).toInt()
                if (state.secondaryHit > 0) {
                    state.secondaryHit = (state.secondaryHit * 0.5).toInt()
                }
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