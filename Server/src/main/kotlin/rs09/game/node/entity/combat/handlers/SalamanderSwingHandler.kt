package rs09.game.node.entity.combat.handlers

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import rs09.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.InteractionType
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics

/**
 * Handles a combat swing using a salamander.
 * @author Vexia
 */
class SalamanderSwingHandler(private var style: CombatStyle) : CombatSwingHandler(style) {
    override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
        val index = entity!!.properties.attackStyle.style
        style = when(index) {
            7 -> CombatStyle.MAGIC
            4 -> CombatStyle.RANGE
            else -> CombatStyle.MELEE
        }
        return style.swingHandler.swing(entity, victim, state)
    }

    override fun impact(entity: Entity?, victim: Entity?, state: BattleState?) {
        style.swingHandler.impact(entity, victim, state)
    }

    override fun visualize(entity: Entity, victim: Entity?, state: BattleState?) {
        style.swingHandler.visualize(entity, victim, state)
        entity.visualize(Animation.create(5247), Graphics(952, 100))
    }

    override fun visualizeImpact(entity: Entity?, victim: Entity?, state: BattleState?) {
        style.swingHandler.visualizeImpact(entity, victim, state)
    }

    override fun calculateAccuracy(entity: Entity?): Int {
        return style.swingHandler.calculateAccuracy(entity)
    }

    override fun calculateHit(entity: Entity?, victim: Entity?, modifier: Double): Int {
        return style.swingHandler.calculateHit(entity, victim, modifier)
    }

    override fun addExperience(entity: Entity?, victim: Entity?, state: BattleState?) {
        entity!!.skills.addExperience(Skills.HITPOINTS, state!!.estimatedHit * 1.33, true)
        if (state.style == CombatStyle.MAGIC) {
            entity.skills.addExperience(Skills.MAGIC, state.estimatedHit * 2.toDouble(), true)
        }
        if (state.style == CombatStyle.RANGE) {
            entity.skills.addExperience(Skills.RANGE, state.estimatedHit * 4.toDouble(), true)
        }
        if (state.style == CombatStyle.MELEE) {
            entity.skills.addExperience(Skills.STRENGTH, state.estimatedHit * 4.toDouble(), true)
        }
    }

    override fun calculateDefence(victim: Entity?, attacker: Entity?): Int {
        return style.swingHandler.calculateDefence(victim, attacker)
    }

    override fun getSetMultiplier(e: Entity?, skillId: Int): Double {
        return style.swingHandler.getSetMultiplier(e, skillId)
    }

    override fun canSwing(entity: Entity, victim: Entity): InteractionType? {
        return style.swingHandler.canSwing(entity, victim)
    }

    companion object {
        /**
         * The instance for the swing handler.
         */
        val INSTANCE = SalamanderSwingHandler(CombatStyle.MELEE)
    }

}