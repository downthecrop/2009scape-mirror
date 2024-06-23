package content.global.handlers.item.equipment.special

import core.api.EquipmentSlot
import core.api.getItemFromEquipment
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.InteractionType
import core.game.node.entity.combat.equipment.Weapon
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.Animations
import kotlin.math.ceil
import kotlin.math.floor

/**
 * Handles a combat swing using a salamander.
 * @author Vexia
 * @author itsmedoggo
 */
class SalamanderSwingHandler(private var style: CombatStyle) : CombatSwingHandler(style) {
    override fun canSwing(entity: Entity, victim: Entity): InteractionType? {
        checkStyle(entity)
        if (!isProjectileClipped(entity, victim, false)) {
            return InteractionType.NO_INTERACT
        }
        var distance = 1
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
        state!!.style = style
        if (entity is Player) {
            state.weapon = Weapon(entity.equipment[3])
        }
        if (state!!.weapon == null || !hasAmmo(entity, state)) {
            entity!!.properties.combatPulse.stop()
            return -1
        }
        var hit = 0
        if (isAccurateImpact(entity, victim, style)) {
            val max = calculateHit(entity, victim, 1.0)
            state.maximumHit = max
            hit = RandomFunction.random(max + 1)
        }
        state.estimatedHit = hit
        if (entity == null || victim!!.location == null) {
            return -1
        }
        if(state.estimatedHit > victim.skills.lifepoints) state.estimatedHit = victim.skills.lifepoints
        useAmmo(entity, state)
        return 1
    }

    override fun impact(entity: Entity?, victim: Entity?, state: BattleState?) {
        style.swingHandler.impact(entity, victim, state)
    }

    override fun visualize(entity: Entity, victim: Entity?, state: BattleState?) {
        entity.visualize(Animation.create(5247), Graphics(952, 100))
    }

    override fun visualizeImpact(entity: Entity?, victim: Entity?, state: BattleState?) {
        style.swingHandler.visualizeImpact(entity, victim, state)
    }

    override fun calculateAccuracy(entity: Entity?): Int {
        //Checking style is necessary for ::calc_accuracy to function after changing attack style but before attacking
        checkStyle(entity)
        return style.swingHandler.calculateAccuracy(entity)
    }

    override fun calculateHit(entity: Entity?, victim: Entity?, modifier: Double): Int {
        entity ?: return 0

        //Checking style is necessary for ::calcmaxhit to function after changing attack style but before attacking
        checkStyle(entity)
        if (style == CombatStyle.MAGIC) {
            val level = entity!!.skills.getLevel(Skills.MAGIC, true)
            var bonus = entity.properties.bonuses[13]
            if (entity is Player) {
                bonus += getSalamanderMagicDamageBonus(entity.equipment[3].id)
            }
            var cumulativeStr = level.toDouble()
            cumulativeStr *= getSetMultiplier(entity, Skills.MAGIC)
            cumulativeStr *= (bonus + 64)
            return floor((0.5 + (ceil(cumulativeStr) / 640.0)) * modifier).toInt()
        }
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

    /**
     * Sets the local style
     * @param e The entity
     */
    private fun checkStyle(e: Entity?) {
        val index = e!!.properties.attackStyle.style
        style = when(index) {
            WeaponInterface.STYLE_DEFENSIVE_CAST -> CombatStyle.MAGIC
            WeaponInterface.STYLE_RANGE_ACCURATE -> CombatStyle.RANGE
            else -> CombatStyle.MELEE
        }
    }

    companion object {
        /**
         * The instance for the swing handler.
         */
        val INSTANCE = SalamanderSwingHandler(CombatStyle.RANGE)

        /**
         * Gets the id of the tar used by a salamander.
         * @param id The salamander id.
         * @return The tar id.
         */
        fun getAmmoId(id: Int): Int {
            when (id) {
                Items.SWAMP_LIZARD_10149 -> {
                    return Items.GUAM_TAR_10142
                }
                Items.ORANGE_SALAMANDER_10146 -> {
                    return Items.MARRENTILL_TAR_10143
                }
                Items.RED_SALAMANDER_10147 -> {
                    return Items.TARROMIN_TAR_10144
                }
                Items.BLACK_SALAMANDER_10148 -> {
                    return Items.HARRALANDER_TAR_10145
                }
                else -> {
                    return -1
                }
            }
        }

        /**
         * Gets the magic damage bonus of a salamander.
         * @param id The salamander id.
         * @return The magic damage bonus.
         */
        fun getSalamanderMagicDamageBonus(id: Int): Int {
            when (id) {
                Items.SWAMP_LIZARD_10149 -> {
                    return 56
                }
                Items.ORANGE_SALAMANDER_10146 -> {
                    return 59
                }
                Items.RED_SALAMANDER_10147 -> {
                    return 77
                }
                Items.BLACK_SALAMANDER_10148 -> {
                    return 92
                }
                else -> {
                    return 0
                }
            }
        }

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
            val ammo = getItemFromEquipment(e, EquipmentSlot.AMMO)
            if (ammo == null) {
                e.packetDispatch.sendMessage("You do not have enough ammo left.")
                return false
            }
            if (ammo.id == (getAmmoId(state!!.weapon.id))) {
                return true
            }
            e.packetDispatch.sendMessage("You can't use this type of ammunition with this salamander.")
            return false
        }

        /**
         * Uses the ammunition for the range weapon.
         * @param e The entity.
         * @param state The battle state.
         * @param location The drop location.
         */
        fun useAmmo(e: Entity, state: BattleState) {
            if (e !is Player) {
                return
            }
            e.equipment.remove(Item(getAmmoId(state!!.weapon.id), 1))
        }
    }

}