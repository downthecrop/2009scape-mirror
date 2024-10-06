package core.game.node.entity.combat

import content.global.skill.skillcapeperks.SkillcapePerks
import content.global.skill.slayer.SlayerEquipmentFlags
import content.global.skill.slayer.SlayerManager
import core.api.*
import core.api.EquipmentSlot
import core.game.container.impl.EquipmentContainer
import core.game.node.entity.Entity
import core.game.node.entity.combat.equipment.ArmourSet
import core.game.node.entity.combat.equipment.Weapon
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.path.Pathfinder
import core.tools.RandomFunction
import org.rs09.consts.Items
import kotlin.math.ceil
import kotlin.math.floor

/**
 * Handles a melee combat swing.
 * @author Emperor
 * @author Ceikry, Kotlin conversion + cleanup
 */
open class MeleeSwingHandler (vararg flags: SwingHandlerFlag)
/**
 * Constructs a new `MeleeSwingHandler` {@Code Object}.
 */
    : CombatSwingHandler(CombatStyle.MELEE, *flags) {
    override fun canSwing(entity: Entity, victim: Entity): InteractionType? {
        //Credits wolfenzi, https://www.rune-server.ee/2009scape-development/rs2-server/snippets/608720-arios-hybridding-improve.html
        var distance = if (usingHalberd(entity)) 2 else 1
        var type = InteractionType.STILL_INTERACT
        var goodRange = canMelee(entity, victim, distance)
        if (!goodRange && victim.properties.combatPulse.getVictim() !== entity && victim.walkingQueue.isMoving && entity.size() == 1) {
            type = InteractionType.MOVE_INTERACT
            distance += if (entity.walkingQueue.isRunningBoth) 2 else 1
            goodRange = canMelee(entity, victim, distance)
        }
        if (!isProjectileClipped(entity, victim, !usingHalberd(entity))) {
            return InteractionType.NO_INTERACT
        }
        val isRunning = entity.walkingQueue.runDir != -1
        val enemyRunning = victim.walkingQueue.runDir != -1
        // THX 4 fix tom <333.
        if (super.canSwing(entity, victim) != InteractionType.NO_INTERACT) {
            val maxDistance = if (isRunning) if (enemyRunning) 3 else 4 else 2
            if (entity.walkingQueue.isMoving
                    && entity.location.getDistance(victim.location) <= maxDistance) {
                return type
            } else if (goodRange) {
                if (type == InteractionType.STILL_INTERACT) entity.walkingQueue.reset()
                return type
            }
        }
        return InteractionType.NO_INTERACT
    }

    override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
        var hit = 0
        state!!.style = CombatStyle.MELEE
        if (entity is Player) {
            state.weapon = Weapon(entity.equipment[3])
        }
        if (entity!!.properties.armourSet === ArmourSet.VERAC && RandomFunction.random(100) < 25) {
            state.armourEffect = ArmourSet.VERAC
        }
        if (state.armourEffect === ArmourSet.VERAC || isAccurateImpact(entity, victim, CombatStyle.MELEE)) {
            var max = calculateHit(entity, victim, 1.0)
            if (victim != null) {
                if (entity is NPC && state.armourEffect === ArmourSet.VERAC && victim.hasProtectionPrayer(CombatStyle.MELEE)) max = max * 2 / 3
            }
            state.maximumHit = max
            hit = RandomFunction.random(max + 1) + (if (entity is Player && state.armourEffect === ArmourSet.VERAC) 1 else 0)
        }
        state.estimatedHit = hit
        if(victim != null) {
            if (state.estimatedHit > victim.skills.lifepoints) state.estimatedHit = victim.skills.lifepoints
            if (state.estimatedHit + state.secondaryHit > victim.skills.lifepoints) state.secondaryHit -= ((state.estimatedHit + state.secondaryHit) - victim.skills.lifepoints)
        }
        return 1
    }

    override fun visualize(entity: Entity, victim: Entity?, state: BattleState?) {
        entity.animate(entity.properties.attackAnimation)
    }

    override fun impact(entity: Entity?, victim: Entity?, state: BattleState?) {
        var targets = state!!.targets
        if (targets == null) {
            targets = arrayOf<BattleState?>(state)
        }
        for (s in targets) {
            var hit = s!!.estimatedHit
            if (hit > -1) {
                victim!!.impactHandler.handleImpact(entity, hit, CombatStyle.MELEE, s)
            }
            hit = s.secondaryHit
            if (hit > -1) {
                victim!!.impactHandler.handleImpact(entity, hit, CombatStyle.MELEE, s)
            }
        }
    }

    override fun visualizeImpact(entity: Entity?, victim: Entity?, state: BattleState?) {
        victim!!.animate(victim.properties.defenceAnimation)
    }

    override fun adjustBattleState(entity: Entity, victim: Entity, state: BattleState) {
        if (entity is Player) {
            if (state.estimatedHit > 0) {
                val name = entity.equipment.getNew(3).name
                var damage = -1
                if (name.contains("(p++") || name.contains("(s)") || name.contains("(kp)")) {
                    damage = 68
                } else if (name.contains("(p+)")) {
                    damage = 58
                } else if (name.contains("(p)")) {
                    damage = 48
                }
                if (damage > -1 && RandomFunction.random(10) < 4) {
                    applyPoison (victim, entity, damage)
                }
            }
        }
        super.adjustBattleState(entity, victim, state)
    }

    override fun calculateAccuracy(entity: Entity?): Int {
        //formula taken from wiki: https://oldschool.runescape.wiki/w/Damage_per_second/Melee#Step_six:_Calculate_the_hit_chance Yes I know it's old school. It's the best resource we have for potentially authentic formulae.
        entity ?: return 0

        val styleAttackBonus = entity.properties.bonuses[entity.properties.attackStyle.bonusType] + 64
        when (entity) {
            is Player -> {
                var effectiveAttackLevel = entity.skills.getLevel(Skills.ATTACK).toDouble()
                if(!flags.contains(SwingHandlerFlag.IGNORE_PRAYER_BOOSTS_ACCURACY))
                    effectiveAttackLevel = floor(effectiveAttackLevel + (entity.prayer.getSkillBonus(Skills.ATTACK) * effectiveAttackLevel))
                if(entity.properties.attackStyle.style == WeaponInterface.STYLE_ACCURATE) effectiveAttackLevel += 3
                else if(entity.properties.attackStyle.style == WeaponInterface.STYLE_CONTROLLED) effectiveAttackLevel += 1
                effectiveAttackLevel += 8
                if(SkillcapePerks.isActive(SkillcapePerks.PRECISION_STRIKES, entity)){ //Attack skillcape perk
                    effectiveAttackLevel += 6
                }
                effectiveAttackLevel *= getSetMultiplier(entity, Skills.ATTACK)
                effectiveAttackLevel = floor(effectiveAttackLevel)
                if (!flags.contains(SwingHandlerFlag.IGNORE_STAT_BOOSTS_ACCURACY))
                    effectiveAttackLevel *= styleAttackBonus
                else effectiveAttackLevel *= 64

                val victimName = entity.properties.combatPulse.getVictim()?.name ?: "none"

                // attack bonus for specialized equipments (salve amulets, slayer equips)
                val amuletId = getItemFromEquipment(entity, EquipmentSlot.NECK)?.id ?: 0
                if ((amuletId == Items.SALVE_AMULET_4081 || amuletId == Items.SALVE_AMULETE_10588) && checkUndead(victimName)) {
                    effectiveAttackLevel *= if (amuletId == Items.SALVE_AMULET_4081) 1.15 else 1.2
                } else if (getSlayerTask(entity)?.ids?.contains((entity.properties.combatPulse?.getVictim()?.id ?: 0)) == true) {
                    effectiveAttackLevel *= SlayerEquipmentFlags.getDamAccBonus(entity) //Slayer Helm/ Black Mask/ Slayer cape
                    if (getSlayerTask(entity)?.dragon == true && inEquipment(entity, Items.DRAGON_SLAYER_GLOVES_12862))
                        effectiveAttackLevel *= 1.1
                }

                return effectiveAttackLevel.toInt()
            }
            is NPC -> {
                val attackLevel = entity.skills.getLevel(Skills.ATTACK) + 9
                return attackLevel * styleAttackBonus
            }
        }

        return 0


    }

    override fun calculateHit(entity: Entity?, victim: Entity?, modifier: Double): Int {
        entity ?: return 0

        var styleStrengthBonus = entity.properties.bonuses[11] + 64
        when (entity) {
            is Player -> {
                var effectiveStrengthLevel = entity.skills.getLevel(Skills.STRENGTH).toDouble()
                if(!flags.contains(SwingHandlerFlag.IGNORE_PRAYER_BOOSTS_DAMAGE))
                    effectiveStrengthLevel = floor(effectiveStrengthLevel + (entity.prayer.getSkillBonus(Skills.STRENGTH) * effectiveStrengthLevel))
                if(entity.properties.attackStyle.style == WeaponInterface.STYLE_AGGRESSIVE) effectiveStrengthLevel += 3
                else if (entity.properties.attackStyle.style == WeaponInterface.STYLE_CONTROLLED) effectiveStrengthLevel += 1
                effectiveStrengthLevel += 8
                effectiveStrengthLevel *= getSetMultiplier(entity, Skills.STRENGTH)
                effectiveStrengthLevel = floor(effectiveStrengthLevel)
                if (!flags.contains(SwingHandlerFlag.IGNORE_STAT_BOOSTS_DAMAGE))
                    effectiveStrengthLevel *= styleStrengthBonus
                else effectiveStrengthLevel *= 64
                if (getSlayerTask(entity)?.ids?.contains((entity.properties.combatPulse?.getVictim()?.id ?: 0)) == true)
                    effectiveStrengthLevel *= SlayerEquipmentFlags.getDamAccBonus(entity) //Slayer Helm/ Black Mask/ Slayer cape

                return (floor((0.5 + (effectiveStrengthLevel / 640.0))) * modifier).toInt()
            }
            is NPC -> {
                val strengthLevel = entity.skills.getLevel(Skills.STRENGTH) + 9
                return (floor((0.5 + (strengthLevel * styleStrengthBonus / 640.0))) * modifier).toInt()
            }
        }

        return 0
    }

    override fun calculateDefence(victim: Entity?, attacker: Entity?): Int {
        //authentic formula, taken from OSRS wiki: https://oldschool.runescape.wiki/w/Damage_per_second/Melee#Step_five:_Calculate_the_Defence_roll
        victim ?: return 0
        attacker ?: return 0

        val styleDefenceBonus = victim.properties.bonuses[attacker.properties.attackStyle.bonusType + 5] + 64
        when (victim) {
            is Player -> {
                var effectiveDefenceLevel = victim.skills.getLevel(Skills.DEFENCE).toDouble()
                effectiveDefenceLevel = floor(effectiveDefenceLevel + (victim.prayer.getSkillBonus(Skills.DEFENCE) * effectiveDefenceLevel))
                if (victim.properties.attackStyle.style == WeaponInterface.STYLE_DEFENSIVE || victim.properties.attackStyle.style == WeaponInterface.STYLE_LONG_RANGE) effectiveDefenceLevel += 3
                else if (victim.properties.attackStyle.style == WeaponInterface.STYLE_CONTROLLED) effectiveDefenceLevel += 1
                effectiveDefenceLevel += 8
                effectiveDefenceLevel *= getSetMultiplier(victim, Skills.DEFENCE)
                return effectiveDefenceLevel.toInt() * styleDefenceBonus
            }
            is NPC -> {
                val defLevel = victim.skills.getLevel(Skills.DEFENCE) + 9
                return defLevel * styleDefenceBonus
            }
        }

        return 0
    }

    override fun getSetMultiplier(e: Entity?, skillId: Int): Double {
        if (e!!.properties.armourSet === ArmourSet.DHAROK && skillId == Skills.STRENGTH) {

            return 1.0 + (e!!.skills.maximumLifepoints - e.skills.lifepoints) * 0.01
        }
        if(e is Player && e.isWearingVoid(CombatStyle.MELEE) && (skillId == Skills.ATTACK || skillId == Skills.STRENGTH)) {
            return 1.1
        }
        return 1.0
    }

    override fun getArmourSet(e: Entity?): ArmourSet? {
        if (ArmourSet.DHAROK.isUsing(e)) {
            return ArmourSet.DHAROK
        }
        if (ArmourSet.GUTHAN.isUsing(e)) {
            return ArmourSet.GUTHAN
        }
        if (ArmourSet.VERAC.isUsing(e)) {
            return ArmourSet.VERAC
        }
        return if (ArmourSet.TORAG.isUsing(e)) {
            ArmourSet.TORAG
        } else super.getArmourSet(e)
    }

    /**
     * Check to see whether an NPC is classified as undead.
     * @param name
     * @return true if so
     */
    private fun checkUndead(name: String): Boolean {
        return (name == "Zombie" || name.contains("rmoured") || name == "Ankou" || name == "Crawling Hand" || name == "Banshee" || name == "Ghost" || name == "Ghast" || name == "Mummy" || name.contains("Revenant")
                || name == "Skeleton" || name == "Zogre" || name == "Spiritual Mage")
    }

    companion object {
        /**
         * Checks if the entity is using a halberd.
         * @param entity The entity.
         * @return `True` if so.
         */
        private fun usingHalberd(entity: Entity): Boolean {
            if (entity is Player) {
                val weapon = entity.equipment[EquipmentContainer.SLOT_WEAPON]
                if (weapon != null) {
                    return weapon.id in 3190..3204 || weapon.id == 6599
                }
            } else if (entity is NPC) {
                return entity.id == 8612
            }
            return false
        }

        /**
         * Checks if the entity can execute a melee swing from its current location.
         * @param entity The attacking entity.
         * @param victim The victim.
         * @return `True` if so.
         */
        fun canMelee(entity: Entity, victim: Entity?, distance: Int): Boolean {
            val e = entity.location
            if (victim == null) {
                return false
            }
            if (entity.id == 7135 && entity.location.withinDistance(victim.location, 2)) {
                return true
            }
            val x = victim.location.x
            val y = victim.location.y
            val size = entity.size()
            if (distance == 1) {
                for (i in 0 until size) {
                    if (Pathfinder.isStandingIn(e.x - 1, e.y + i, 1, 1, x, y, victim.size(), victim.size())) {
                        return true
                    }
                    if (Pathfinder.isStandingIn(e.x + size, e.y + i, 1, 1, x, y, victim.size(), victim.size())) {
                        return true
                    }
                    if (Pathfinder.isStandingIn(e.x + i, e.y - 1, 1, 1, x, y, victim.size(), victim.size())) {
                        return true
                    }
                    if (Pathfinder.isStandingIn(e.x + i, e.y + size, 1, 1, x, y, victim.size(), victim.size())) {
                        return true
                    }
                }
                if (e == victim.location) {
                    return true
                }
                return victim.getSwingHandler(false).type == CombatStyle.MELEE && e.withinDistance(victim.location, 1) && victim.properties.combatPulse.getVictim() === entity && entity.index < victim.index
            }
            return entity.centerLocation.withinDistance(victim.centerLocation, distance + (size shr 1) + (victim.size() shr 1))
        }
    }
}
