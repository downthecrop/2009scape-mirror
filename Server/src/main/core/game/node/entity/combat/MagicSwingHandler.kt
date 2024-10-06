package core.game.node.entity.combat

import content.global.skill.skillcapeperks.SkillcapePerks
import core.game.node.entity.Entity
import core.game.node.entity.combat.equipment.ArmourSet
import core.game.node.entity.combat.spell.SpellType
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.tools.RandomFunction
import kotlin.math.floor

/**
 * Handles the magic combat swings.
 * @author Emperor
 * @author Ceikry, Kotlin conversion + cleanup
 */
open class MagicSwingHandler (vararg flags: SwingHandlerFlag)
    : CombatSwingHandler(CombatStyle.MAGIC, *flags) {

    override fun canSwing(entity: Entity, victim: Entity): InteractionType? {
        if (!isProjectileClipped(entity, victim, false)) {
            return InteractionType.NO_INTERACT
        }
        var distance = 10
        var type = InteractionType.STILL_INTERACT
        var goodRange = victim.centerLocation.withinDistance(entity.centerLocation, getCombatDistance(entity, victim, distance))
        if (victim.walkingQueue.isMoving && !goodRange) {
            goodRange = victim.centerLocation.withinDistance(entity.centerLocation, getCombatDistance(entity, victim, ++distance))
            type = InteractionType.MOVE_INTERACT
        }
        if (goodRange && isAttackable(entity, victim) != InteractionType.NO_INTERACT) {
            if (type == InteractionType.STILL_INTERACT) {
                entity.walkingQueue.reset()
            }
            return type
        }
        return InteractionType.NO_INTERACT
    }

    override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
        var spell = entity!!.properties.spell
        if (spell == null) {
            if (entity.properties.autocastSpell.also { spell = it } == null) {
                return -1
            }
        }
        state!!.style = CombatStyle.MAGIC
        if (!spell!!.meetsRequirements(entity, true, true)) {
            entity.properties.spell = null
            val inter = entity.getExtension<WeaponInterface>(WeaponInterface::class.java)
            if (inter != null) {
                inter.selectAutoSpell(-1, true)
                entity.properties.combatPulse.updateStyle()
            }
            return -1
        }
        val max = calculateHit(entity, victim, spell!!.getMaximumImpact(entity, victim, state).toDouble())
        state.targets = spell!!.getTargets(entity, victim)
        state.spell = spell
        for (s in state.targets) {
            var hit = -1
            s.spell = spell
            if (isAccurateImpact(entity, s.victim, CombatStyle.MAGIC)) {
                s.maximumHit = max
                hit = RandomFunction.random(max)
            }
            s.style = CombatStyle.MAGIC
            s.estimatedHit = hit
        }
        if (spell === entity.properties.spell) {
            entity.properties.spell = null
            if (entity.properties.autocastSpell == null) {
                entity.properties.combatPulse.stop()
            }
        }
        var ticks = 2 + floor(entity.location.getDistance(victim!!.location) * 0.5).toInt()
        if (spell!!.type === SpellType.BLITZ) {
            ticks++
        }
        if(state.estimatedHit > victim.skills.lifepoints) state.estimatedHit = victim.skills.lifepoints
        if(state.estimatedHit + state.secondaryHit > victim.skills.lifepoints) state.secondaryHit -= ((state.estimatedHit + state.secondaryHit) - victim.skills.lifepoints)
        return ticks
    }

    override fun adjustBattleState(entity: Entity, victim: Entity, state: BattleState) {
        if (state.targets == null) {
            super.adjustBattleState(entity, victim, state)
            if (state.spell != null) {
                state.spell.fireEffect(entity, victim, state)
            }
            return
        }
        for (s in state.targets) {
            if (s != null) {
                super.adjustBattleState(entity, s.victim, s)
                if (s.spell != null) {
                    s.spell.fireEffect(entity, s.victim, s)
                }
            }
        }
    }

    override fun visualize(entity: Entity, victim: Entity?, state: BattleState?) {
        state!!.spell.visualize(entity, victim)
    }

    override fun impact(entity: Entity?, victim: Entity?, state: BattleState?) {
        if (state!!.targets == null) {
            if (state.estimatedHit > -1) {
                victim!!.impactHandler.handleImpact(entity, state.estimatedHit, CombatStyle.MAGIC, state)
            }
            return
        }
        for (s in state.targets) {
            if (s == null || s.estimatedHit < 0) {
                continue
            }
            val hit = s.estimatedHit
            s.victim.impactHandler.handleImpact(entity, hit, CombatStyle.MAGIC, s)
        }
    }

    override fun visualizeImpact(entity: Entity?, victim: Entity?, state: BattleState?) {
        if (state!!.targets == null) {
            if (state.spell != null) {
                state.spell.visualizeImpact(entity, victim, state)
            }
            return
        }
        for (s in state.targets) {
            if (s != null) {
                state.spell.visualizeImpact(entity, s.victim, s)
            }
        }
    }

    override fun calculateAccuracy(entity: Entity?): Int {
        entity ?: return 0

        val styleAttackBonus = entity.properties.bonuses[WeaponInterface.BONUS_MAGIC] + 64
        when (entity) {
            is Player -> {
                var effectiveMagicLevel = entity.skills.getLevel(Skills.MAGIC, true).toDouble()
                if(!flags.contains(SwingHandlerFlag.IGNORE_PRAYER_BOOSTS_ACCURACY))
                    effectiveMagicLevel = floor(effectiveMagicLevel + (entity.prayer.getSkillBonus(Skills.MAGIC) * effectiveMagicLevel))
                effectiveMagicLevel += 8
                effectiveMagicLevel *= getSetMultiplier(entity, Skills.MAGIC)
                effectiveMagicLevel = floor(effectiveMagicLevel)

                if (!flags.contains(SwingHandlerFlag.IGNORE_STAT_BOOSTS_ACCURACY))
                    effectiveMagicLevel *= styleAttackBonus
                else effectiveMagicLevel *= 64

                return effectiveMagicLevel.toInt()
            }
            is NPC -> {
                val magicLevel = entity.skills.getLevel(Skills.MAGIC) + 9
                return magicLevel * styleAttackBonus
            }
        }

        return 0
    }

    override fun calculateHit(entity: Entity?, victim: Entity?, modifier: Double): Int {
        var baseDamage = modifier
        if (baseDamage < 2.0 || entity is NPC) {
            if (baseDamage > 2.0) {
                baseDamage = 1.0
            }
            val level = entity!!.skills.getLevel(Skills.MAGIC, true)
            val bonus = entity.properties.bonuses[if (entity is Player) 14 else 13]
            val cumulativeStr = level.toDouble()
            return 1 + ((14 + cumulativeStr + bonus.toDouble() / 8 + cumulativeStr * bonus * 0.016865) * baseDamage).toInt() / 10
        }
        var levelMod = 1.0
        val entityMod = entity!!.getLevelMod(entity, victim)
        if (entityMod != 0.0) {
            levelMod += entityMod
        }
        return (baseDamage * levelMod).toInt() + 1
    }

    override fun calculateDefence(victim: Entity?, attacker: Entity?): Int {
        victim ?: return 0
        attacker ?: return 0

        val styleDefenceBonus = victim.properties.bonuses[WeaponInterface.BONUS_MAGIC + 5] + 64
        when (victim) {
            is Player -> {
                var effectiveDefenceLevel = victim.skills.getLevel(Skills.DEFENCE).toDouble()
                effectiveDefenceLevel = floor(effectiveDefenceLevel + (victim.prayer.getSkillBonus(Skills.DEFENCE) * effectiveDefenceLevel))
                if (victim.properties.attackStyle.style == WeaponInterface.STYLE_DEFENSIVE || victim.properties.attackStyle.style == WeaponInterface.STYLE_LONG_RANGE) effectiveDefenceLevel += 3
                else if (victim.properties.attackStyle.style == WeaponInterface.STYLE_CONTROLLED) effectiveDefenceLevel += 1
                effectiveDefenceLevel *= getSetMultiplier(victim, Skills.DEFENCE)

                var effectiveMagicLevel = victim.skills.getLevel(Skills.MAGIC).toDouble()
                effectiveMagicLevel = floor(effectiveMagicLevel + (victim.prayer.getSkillBonus(Skills.MAGIC) * effectiveMagicLevel))

                effectiveDefenceLevel = effectiveDefenceLevel * 0.3 + effectiveMagicLevel * 0.7 + 8
                return effectiveDefenceLevel.toInt() * styleDefenceBonus
            }
            is NPC -> {
                val defLevel = victim.skills.getLevel(Skills.MAGIC) + 9
                return defLevel * styleDefenceBonus
            }
        }

        return 0
    }

    override fun getSetMultiplier(e: Entity?, skillId: Int): Double {
        if(skillId == Skills.MAGIC) {
            if(e is Player && e.isWearingVoid(CombatStyle.MAGIC)) {
                return 1.3
            }
        }
        return 1.0
    }

    override fun addExperience(entity: Entity?, victim: Entity?, state: BattleState?) {
        if (state?.spell != null && entity is Player) {
            state.spell.addExperience(entity, state.totalDamage)
            return
        }

        super.addExperience(entity, victim, state)
    }


    override fun getArmourSet(e: Entity?): ArmourSet? {
        return if (ArmourSet.AHRIM.isUsing(e)) {
            ArmourSet.AHRIM
        } else super.getArmourSet(e)
    }
}
