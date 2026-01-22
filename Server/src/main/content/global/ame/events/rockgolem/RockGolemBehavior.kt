package content.global.ame.events.rockgolem

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.InteractionType
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.update.flag.context.Animation
import org.rs09.consts.NPCs

class RockGolemBehavior() : NPCBehavior(NPCs.ROCK_GOLEM_413, NPCs.ROCK_GOLEM_414, NPCs.ROCK_GOLEM_415, NPCs.ROCK_GOLEM_416, NPCs.ROCK_GOLEM_417, NPCs.ROCK_GOLEM_418) {
    private val swingHandler = RockGolemSwingHandler()

    override fun getSwingHandlerOverride(self: NPC, original: CombatSwingHandler): CombatSwingHandler {
        return swingHandler
    }

    override fun getXpMultiplier(self: NPC, attacker: Entity): Double {
        return super.getXpMultiplier(self, attacker) / 16.0
    }
}

private class RockGolemSwingHandler : CombatSwingHandler(CombatStyle.RANGE) {
    override fun canSwing(entity: Entity, victim: Entity): InteractionType? {
        return type?.swingHandler?.canSwing(entity, victim)
    }

    override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
        if (entity == null || victim == null) return -1

        val prayingRange = victim.hasProtectionPrayer(CombatStyle.RANGE)

        type = if (prayingRange) {
            CombatStyle.MELEE
        } else {
            CombatStyle.RANGE
        }

        if (type == CombatStyle.MELEE && entity.location.getDistance(victim.location) > 1) return -1

        return type?.swingHandler?.swing(entity, victim, state) ?: 0
    }

    override fun impact(entity: Entity?, victim: Entity?, state: BattleState?) {
        type?.swingHandler?.impact(entity, victim, state)
    }

    override fun visualizeImpact(entity: Entity?, victim: Entity?, state: BattleState?) {
        type?.swingHandler?.visualizeImpact(entity, victim, state)
    }

    override fun visualize(entity: Entity, victim: Entity?, state: BattleState?) {
        when (type) {
            CombatStyle.MELEE -> {
                entity.animate(Animation(153))
            }

            CombatStyle.RANGE -> {
                val speed = (100 + (Projectile.getLocation(entity).getDistance(victim!!.getLocation()) * 5)).toInt()
                Projectile.create(entity, victim, 968, 48, 36, 80, speed, 5, entity.size() shl 5).send()
                entity.animate(Animation(5347))
            }

            else -> {}
        }
    }

    override fun calculateAccuracy(entity: Entity?): Int {
        return type?.swingHandler?.calculateAccuracy(entity) ?: 0
    }

    override fun calculateHit(entity: Entity?, victim: Entity?, modifier: Double): Int {
        return type?.swingHandler?.calculateHit(entity, victim, modifier) ?: 0
    }

    override fun calculateDefence(victim: Entity?, attacker: Entity?): Int {
        return type?.swingHandler?.calculateDefence(victim, attacker) ?: 0
    }

    override fun getSetMultiplier(e: Entity?, skillId: Int): Double {
        return type?.swingHandler?.getSetMultiplier(e, skillId) ?: 0.0
    }
}
