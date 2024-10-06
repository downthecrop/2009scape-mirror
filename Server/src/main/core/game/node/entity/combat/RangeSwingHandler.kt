package core.game.node.entity.combat

import content.global.skill.skillcapeperks.SkillcapePerks
import core.api.*
import core.game.container.impl.EquipmentContainer
import core.game.node.entity.Entity
import core.game.node.entity.combat.equipment.*
import core.game.node.entity.combat.equipment.Weapon.WeaponType
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.config.ItemConfigParser
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Graphics
import core.tools.Log
import core.tools.RandomFunction
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor

/**
 * Handles the range combat swings.
 * @author Emperor
 * @author Ceikry, conversion to Kotlin + cleanup
 */
open class RangeSwingHandler (vararg flags: SwingHandlerFlag)
/**
 * Constructs a new `RangeSwingHandler` {@Code Object}.
 */
    : CombatSwingHandler(CombatStyle.RANGE, *flags) {
    override fun canSwing(entity: Entity, victim: Entity): InteractionType? {
        if (!isProjectileClipped(entity, victim, false)) {
            return InteractionType.NO_INTERACT
        }
        var distance = 7
        if (entity is Player && (entity.getExtension<Any>(WeaponInterface::class.java) as WeaponInterface).weaponInterface.interfaceId == 91) {
            distance -= 2
        }
        if (entity.properties.attackStyle.style == WeaponInterface.STYLE_LONG_RANGE) {
            distance += 2
        }
        if (entity is Player) {
            val rw = RangeWeapon.get(entity.getEquipment().getNew(EquipmentContainer.SLOT_WEAPON).getId())
            if(rw != null && (rw.weaponType == WeaponType.DOUBLE_SHOT || rw.weaponType == WeaponType.DEGRADING)) {
                // Dark bow and crystal bow have a 10-square range, independent of whether longrange stance is used
                distance = 10
            }
        }
        var goodRange = victim.centerLocation.withinDistance(entity.centerLocation, getCombatDistance(entity, victim, distance))
        var type = InteractionType.STILL_INTERACT
        if (victim.walkingQueue.isMoving && !goodRange) {
            goodRange = victim.centerLocation.withinDistance(entity.centerLocation, getCombatDistance(entity, victim, ++distance))
            type = InteractionType.MOVE_INTERACT
        }
        if (goodRange && super.canSwing(entity, victim) != InteractionType.NO_INTERACT) {
            if (type == InteractionType.STILL_INTERACT) {
                entity.walkingQueue.reset()
            }
            return type
        }
        return InteractionType.NO_INTERACT
    }

    override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
        configureRangeData(entity, state)
        if (state!!.weapon == null || !hasAmmo(entity, state)) {
            entity!!.properties.combatPulse.stop()
            return -1
        }
        var hit = 0
        if (isAccurateImpact(entity, victim, CombatStyle.RANGE)) {
            val max = calculateHit(entity, victim, 1.0).also { if(entity?.name?.toLowerCase() == "test10") log(this::class.java, Log.FINE,  "Damage: $it") }
            state.maximumHit = max
            hit = RandomFunction.random(max + 1)
        }
        state.estimatedHit = hit
        if (state.weapon.type == WeaponType.DOUBLE_SHOT) {
            if (isAccurateImpact(entity, victim, CombatStyle.RANGE)) {
                hit = RandomFunction.random(calculateHit(entity, victim, 1.0) + 1)
            }
            state.secondaryHit = hit
        }
        if (entity == null || victim!!.location == null) {
            return -1
        }
        if(state.estimatedHit > victim.skills.lifepoints) state.estimatedHit = victim.skills.lifepoints
        if(state.estimatedHit + state.secondaryHit > victim.skills.lifepoints) state.secondaryHit -= ((state.estimatedHit + state.secondaryHit) - victim.skills.lifepoints)
        useAmmo(entity, state, victim.location)
        return 1 + ceil(entity.location.getDistance(victim.location) * 0.3).toInt()
    }

    /**
     * Configures the range data.
     * @param entity The entity.
     * @param state The battle state.
     */
    fun configureRangeData(entity: Entity?, state: BattleState?) {
        state!!.style = CombatStyle.RANGE
        val w: Weapon?
        if (entity is Player) {
            val rw = RangeWeapon.get(entity.equipment.getNew(3).id)
            if (rw == null) {
                log(this::class.java, Log.ERR,  "Unhandled range weapon used - [item id=" + entity.equipment.getNew(3).id + "].")
                return
            }
            w = Weapon(entity.equipment[3], rw.ammunitionSlot, entity.equipment.getNew(rw.ammunitionSlot))
            w.type = rw.weaponType
            state.rangeWeapon = rw
            state.ammunition = Ammunition.get(w.ammunition.id)
        } else {
            w = Weapon(null)
        }
        state.weapon = w
    }

    override fun adjustBattleState(entity: Entity, victim: Entity, state: BattleState) {
        if (state.ammunition != null && entity is Player) {
            val damage = state.ammunition.poisonDamage
            if (state.estimatedHit > 0 && damage > 8 && RandomFunction.random(10) < 4) {
                applyPoison (victim, entity, damage)
            }
        }
        super.adjustBattleState(entity, victim, state)
    }

    override fun visualize(entity: Entity, victim: Entity?, state: BattleState?) {
        var start: Graphics? = null
        if (state!!.ammunition != null) {
            start = state.ammunition.startGraphics
            state.ammunition.projectile.copy(entity, victim, 5.0).send()
            if (state.weapon.type == WeaponType.DOUBLE_SHOT && state.ammunition.darkBowGraphics != null) {
                start = state.ammunition.darkBowGraphics
                val speed = (55 + entity.location.getDistance(victim!!.location) * 10).toInt()
                Projectile.create(entity, victim, state.ammunition.projectile.projectileId, 40, 36, 41, speed, 25).send()
            }
        } else if (entity is NPC) {
            if (entity.definition.combatGraphics[0] != null) {
                start = entity.definition.combatGraphics[0]
            }
            val g = entity.definition.combatGraphics[1]
            if (g != null) {
                Projectile.ranged(entity, victim, g.id, g.height, 36, 41, 5).send()
            }
        }
        val weapon: RangeWeapon? = state.weapon?.let { RangeWeapon.get(it.id) }
        val anim = entity.properties.attackAnimation.id
        weapon?.let {
            if ((anim == 422 || anim == 423)) {
                entity.visualize(it.animation, start)
                return
            }
        }
        entity.visualize(entity.properties.attackAnimation, start)
    }

    override fun impact(entity: Entity?, victim: Entity?, state: BattleState?) {
        if (state!!.ammunition != null && state.ammunition.effect != null && state.ammunition.effect.canFire(state)) {
            state.ammunition.effect.impact(state)
        }
        val hit = state.estimatedHit
        victim!!.impactHandler.handleImpact(entity, hit, CombatStyle.RANGE, state)
        if (state.secondaryHit > -1) {
            val hitt = state.secondaryHit
            GameWorld.Pulser.submit(object : Pulse(1, victim) {
                override fun pulse(): Boolean {
                    victim.impactHandler.handleImpact(entity, hitt, CombatStyle.RANGE, state)
                    return true
                }
            })
        }
    }

    override fun visualizeImpact(entity: Entity?, victim: Entity?, state: BattleState?) {
        victim!!.animate(victim.properties.defenceAnimation)
    }

    override fun calculateAccuracy(entity: Entity?): Int {
        entity ?: return 0

        val styleAttackBonus = entity.properties.bonuses[entity.properties.attackStyle.bonusType] + 64
        when (entity) {
            is Player -> {
                var effectiveRangedLevel = entity.skills.getLevel(Skills.RANGE).toDouble()
                if(!flags.contains(SwingHandlerFlag.IGNORE_PRAYER_BOOSTS_ACCURACY))
                    effectiveRangedLevel = floor(effectiveRangedLevel + (entity.prayer.getSkillBonus(Skills.RANGE) * effectiveRangedLevel))
                if(entity.properties.attackStyle.style == WeaponInterface.STYLE_RANGE_ACCURATE) effectiveRangedLevel += 3
                effectiveRangedLevel += 8
                effectiveRangedLevel *= getSetMultiplier(entity, Skills.RANGE)
                if(SkillcapePerks.isActive(SkillcapePerks.ACCURATE_MARKSMAN,entity)) effectiveRangedLevel *= 1.1

                effectiveRangedLevel = floor(effectiveRangedLevel)
                if (!flags.contains(SwingHandlerFlag.IGNORE_STAT_BOOSTS_ACCURACY))
                    effectiveRangedLevel *= styleAttackBonus
                else effectiveRangedLevel *= 64

                return effectiveRangedLevel.toInt()
            }
            is NPC -> {
                val rangedLevel = entity.skills.getLevel(Skills.RANGE) + 9
                return rangedLevel * styleAttackBonus
            }
        }

        return 0
    }

    override fun calculateHit(entity: Entity?, victim: Entity?, modifier: Double): Int {
        entity ?: return 0

        var styleStrengthBonus = entity.properties.bonuses[14] + 64
        when (entity) {
            is Player -> {
                if(entity.equipment[EquipmentContainer.SLOT_WEAPON] != null && RangeWeapon.get(entity.equipment[EquipmentContainer.SLOT_WEAPON].id).ammunitionSlot != EquipmentContainer.SLOT_ARROWS && entity.equipment[EquipmentContainer.SLOT_ARROWS] != null)
                    styleStrengthBonus -= entity.equipment[EquipmentContainer.SLOT_ARROWS].definition.getConfiguration<IntArray>(ItemConfigParser.BONUS)[14]
                var effectiveStrengthLevel = entity.skills.getLevel(Skills.RANGE).toDouble()
                if(!flags.contains(SwingHandlerFlag.IGNORE_PRAYER_BOOSTS_DAMAGE))
                    effectiveStrengthLevel = floor(effectiveStrengthLevel + (entity.prayer.getSkillBonus(Skills.RANGE) * effectiveStrengthLevel))
                if(entity.properties.attackStyle.style == WeaponInterface.STYLE_RANGE_ACCURATE) effectiveStrengthLevel += 3
                effectiveStrengthLevel += 8
                effectiveStrengthLevel *= getSetMultiplier(entity, Skills.RANGE)
                effectiveStrengthLevel = floor(effectiveStrengthLevel)
                if (!flags.contains(SwingHandlerFlag.IGNORE_STAT_BOOSTS_DAMAGE))
                    effectiveStrengthLevel *= styleStrengthBonus
                else effectiveStrengthLevel *= 64

                return (floor((0.5 + (effectiveStrengthLevel / 640.0))) * modifier).toInt()
            }
            is NPC -> {
                val rangedLevel = entity.skills.getLevel(Skills.RANGE) + 9
                return (floor((0.5 + (rangedLevel * styleStrengthBonus / 640.0))) * modifier).toInt()
            }
        }

        return 0
    }

    override fun calculateDefence(victim: Entity?, attacker: Entity?): Int {
        victim ?: return 0
        attacker ?: return 0

        val styleDefenceBonus = victim.properties.bonuses[attacker.properties.attackStyle.bonusType + 5] + 64
        when (victim) {
            is Player -> {
                var effectiveDefLevel = victim.skills.getLevel(Skills.DEFENCE).toDouble()
                effectiveDefLevel = floor(effectiveDefLevel + (victim.prayer.getSkillBonus(Skills.DEFENCE) * effectiveDefLevel))
                if (victim.properties.attackStyle.style == WeaponInterface.STYLE_DEFENSIVE || victim.properties.attackStyle.style == WeaponInterface.STYLE_LONG_RANGE) effectiveDefLevel += 3
                else if (victim.properties.attackStyle.style == WeaponInterface.STYLE_CONTROLLED) effectiveDefLevel += 1
                effectiveDefLevel += 8
                effectiveDefLevel *= getSetMultiplier(victim, Skills.DEFENCE)
                return effectiveDefLevel.toInt() * styleDefenceBonus
            }
            is NPC -> {
                val defLevel = victim.skills.getLevel(Skills.DEFENCE) + 9
                return defLevel * styleDefenceBonus
            }
        }

        return 0
    }

    override fun getSetMultiplier(e: Entity?, skillId: Int): Double {
        if(skillId == Skills.RANGE) {
            if(e is Player && e.isWearingVoid(CombatStyle.RANGE)) {
                return 1.2
            }
        }
        return 1.0
    }

    override fun getArmourSet(e: Entity?): ArmourSet? {
        return if (ArmourSet.KARIL.isUsing(e)) {
            ArmourSet.KARIL
        } else super.getArmourSet(e)
    }

    companion object {
        /**
         * Checks if the entity has the ammunition needed to proceed.
         * @param e The entity.
         * @param state The battle state.
         * @return `True` if so.
         */
        fun hasAmmo(e: Entity?, state: BattleState?): Boolean {
            if (e !is Player) {
                return true
            }
            val type = state!!.weapon.type
            val amount = if (type == WeaponType.DOUBLE_SHOT) 2 else 1
            if (type == WeaponType.DEGRADING) {
                return true
            }
            val item = e.equipment[state.weapon.ammunitionSlot]
            if (item != null && item.amount >= amount) {
                if (!state.rangeWeapon.ammunition.contains(item.id)) {
                    e.packetDispatch.sendMessage("You can't use this type of ammunition with this bow.")
                    return false
                }
                return true
            }
            if (type == WeaponType.DOUBLE_SHOT) {
                state.weapon.type = WeaponType.DEFAULT
                return hasAmmo(e, state)
            }
            e.packetDispatch.sendMessage("You do not have enough ammo left.")
            return false
        }

        /**
         * Uses the ammunition for the range weapon.
         * @param e The entity.
         * @param state The battle state.
         * @param location The drop location.
         */
        fun useAmmo(e: Entity, state: BattleState, location: Location?) {
            var dropLocation = location
            if (e !is Player) {
                return
            }
            val type = state.weapon.type
            val amount = if (type == WeaponType.DOUBLE_SHOT) 2 else 1
            if (type == WeaponType.DEGRADING) {
                return
            }
            val ammo = state.weapon.ammunition
            if (state.weapon.ammunitionSlot == -1 || ammo == null) {
                return
            }
            val dropRate = getDropRate(e)
            if (dropRate == -1.0) {
                return
            }
            e.equipment.replace(Item(ammo.id, ammo.amount - amount, ammo.charge), state.weapon.ammunitionSlot)
            if (dropLocation == null) {
                return
            }
            val flag = RegionManager.getClippingFlag(dropLocation)
            if (flag and 0x200000 != 0) { //Water
                dropLocation = null
            }
            if (dropLocation != null && state.rangeWeapon.isDropAmmo) {
                val rate = 5 * (1.0 + e.skills.getLevel(Skills.RANGE) * 0.01) * dropRate
                if (RandomFunction.randomize(rate.toInt()) != 0) {
                    val drop = GroundItemManager.increase(Item(ammo.id, amount), dropLocation, e)
                    if (drop != null) {
                        if (e.getAttribute<Any?>("duel:ammo", null) != null) {
                            (e.getAttribute<Any>("duel:ammo") as ArrayList<GroundItem?>).add(drop)
                        }
                    }
                }
            }
            if (e.equipment[state.rangeWeapon.ammunitionSlot] == null) {
                e.packetDispatch.sendMessage("You have no ammo left in your quiver!")
            }
        }

        /**
         * Checks if ammunition should be saved.
         * @param e The entity.
         * @return `True` if so.
         */
        private fun getDropRate(e: Entity?): Double {
            if (e is Player) {
                val cape = e.equipment[EquipmentContainer.SLOT_CAPE]
                val weapon = e.equipment[EquipmentContainer.SLOT_WEAPON]
                if (cape != null && (cape.id == 10498 || cape.id == 10499) && weapon != null && weapon.id != 10034 && weapon.id != 10033) {
                    val rate = 80
                    if (RandomFunction.random(100) < rate) {
                        val torso = e.equipment[EquipmentContainer.SLOT_CHEST]
                        val modelId = torso?.definition?.maleWornModelId1 ?: -1
                        if (modelId == 301 || modelId == 306 || modelId == 3379) {
                            e.packetDispatch.sendMessage("Your armour interferes with Ava's device.")
                            return 1.0
                        }
                        return (-1).toDouble()
                    }
                    return 0.33
                }
            }
            return 1.0
        }
    }
}
