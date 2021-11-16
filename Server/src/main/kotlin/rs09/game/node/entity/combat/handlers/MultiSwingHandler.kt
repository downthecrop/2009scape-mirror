package rs09.game.node.entity.combat.handlers

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import rs09.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.InteractionType
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.npc.NPC
import core.tools.RandomFunction

/**
 * Handles combat swings with switching combat styles.
 * @author Emperor
 * @author Ceirky, Kotlin conversion
 */
open class MultiSwingHandler(meleeDistance: Boolean, vararg attacks: SwitchAttack) : CombatSwingHandler(CombatStyle.RANGE) {
    /**
     * The attacks available.
     */
    val attacks: Array<SwitchAttack>

    /**
     * If the entity has to be in melee distance to switch to melee.
     */
    val isMeleeDistance: Boolean

    /**
     * The current attack.
     */
    protected var current: SwitchAttack

    /**
     * The current attack.
     */
    protected var next: SwitchAttack

    /**
     * Constructs a new `MultiSwingHandler` `Object`.
     * @param attacks The available attacks.
     */
    constructor(vararg attacks: SwitchAttack) : this(true, *attacks) {}

    override fun canSwing(entity: Entity, victim: Entity): InteractionType? {
        return if (isMeleeDistance) {
            CombatStyle.RANGE.swingHandler.canSwing(entity, victim)
        } else next.handler.canSwing(entity, victim)
    }

    override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
        current = next
        if (isMeleeDistance && current.style == CombatStyle.MELEE && CombatStyle.MELEE.swingHandler.canSwing(entity!!, victim!!) != InteractionType.STILL_INTERACT) {
            for (attack in attacks) {
                if (attack.style != CombatStyle.MELEE) {
                    current = attack
                    break
                }
            }
        }
        val style = current.style
        type = style
        val index = RandomFunction.randomize(attacks.size)
        val pick = getNext(entity, victim, state, index)
        next = pick
        if (current.isUseHandler) {
            return current.handler.swing(entity, victim, state)
        }
        var ticks = 1
        if (style != CombatStyle.MELEE) {
            ticks += Math.ceil(entity!!.location.getDistance(victim!!.location) * if (type == CombatStyle.MAGIC) 0.5 else 0.3).toInt()
        }
        var hit = 0
        if (isAccurateImpact(entity, victim, style)) {
            val max = calculateHit(entity, victim, 1.0)
            state!!.maximumHit = max
            hit = RandomFunction.random(max)
        }
        state!!.estimatedHit = hit
        state.style = style
        return ticks
    }

    override fun visualize(entity: Entity, victim: Entity?, state: BattleState?) {
        if (current.isUseHandler) {
            current.handler.visualize(entity, victim, state)
            return
        }
        entity.visualize(current.animation, current.startGraphic)
        if (current.projectile != null) {
            current.projectile.transform(entity, victim, entity is NPC, 46, if (current.style == CombatStyle.MAGIC) 10 else 5).send()
        }
    }

    override fun impact(entity: Entity?, victim: Entity?, state: BattleState?) {
        if (current.isUseHandler) {
            current.handler.impact(entity, victim, state)
            return
        }
        var targets = state!!.targets
        if (targets == null) {
            targets = arrayOf<BattleState?>(state)
        }
        for (s in targets) {
            var hit = s!!.estimatedHit
            if (hit > -1) {
                victim!!.impactHandler.handleImpact(entity, hit, current.style, s)
            }
            hit = s.secondaryHit
            if (hit > -1) {
                victim!!.impactHandler.handleImpact(entity, hit, current.style, s)
            }
        }
    }

    override fun adjustBattleState(entity: Entity, victim: Entity, state: BattleState) {
        if (current.isUseHandler) {
            current.handler.adjustBattleState(entity, victim, state)
            return
        }
        super.adjustBattleState(entity, victim, state)
    }

    override fun addExperience(entity: Entity?, victim: Entity?, state: BattleState?) {
        current.handler.addExperience(entity, victim, state)
    }
    override fun visualizeImpact(entity: Entity?, victim: Entity?, state: BattleState?) {
        if (current.isUseHandler) {
            current.handler.visualizeImpact(entity, victim, state)
            return
        }
        victim!!.visualize(victim.properties.defenceAnimation, current.endGraphic)
    }

    override fun calculateAccuracy(entity: Entity?): Int {
        return current.handler.calculateAccuracy(entity)
    }

    override fun calculateHit(entity: Entity?, victim: Entity?, modifier: Double): Int {
        return if (current.maximumHit > -1) {
            current.maximumHit
        } else current.handler.calculateHit(entity, victim, modifier)
    }

    override fun calculateDefence(victim: Entity?, attacker: Entity?): Int {
        return current.handler.calculateDefence(victim, attacker)
    }

    override fun getSetMultiplier(e: Entity?, skillId: Int): Double {
        return current.handler.getSetMultiplier(e, skillId)
    }

    /**
     * Checks if an attack switch can be selected.
     * @param attack The attack to switch to.
     * @return `True` if selectable.
     */
    fun canSelect(attack: SwitchAttack?): Boolean {
        return true
    }

    /**
     * Gets the next switch attack.
     * @param entity the entity.
     * @param victim the victim.
     * @param state the state.
     * @param index the index.
     * @return the next attack.
     */
    fun getNext(entity: Entity?, victim: Entity?, state: BattleState?, index: Int): SwitchAttack {
        var index = index
        var pick = attacks[index]
        while (!pick.canSelect(entity, victim, state)) {
            index = RandomFunction.randomize(attacks.size)
            pick = attacks[index]
        }
        return pick
    }

    /**
     * Constructs a new `MultiSwingHandler` `Object`.
     * @param meleeDistance If the entity has to be in melee distance to switch
     * to melee.
     * @param attacks The available attacks.
     */
    init {
        current = attacks[0]
        next = current
        isMeleeDistance = meleeDistance && !(attacks.size == 1 && attacks[0].style == CombatStyle.MELEE)
        this.attacks = attacks as Array<SwitchAttack>
    }
}