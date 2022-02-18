package rs09.game.node.entity.combat

import core.game.component.Component
import core.game.container.Container
import core.game.container.impl.EquipmentContainer
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.InteractionType
import core.game.node.entity.combat.equipment.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.audio.Audio
import core.game.node.entity.player.link.prayer.PrayerType
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.summoning.familiar.Familiar
import core.game.world.map.RegionManager
import core.game.world.map.path.Pathfinder
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import rs09.game.system.SystemLogger
import rs09.game.system.config.ItemConfigParser
import java.util.*
import kotlin.math.floor

/**
 * Handles a combat swing.
 * @author Emperor
 * @author Ceikry - Kotlin refactoring, general cleanup
 */
abstract class CombatSwingHandler(var type: CombatStyle?) {
    /**
     * The mapping of the special attack handlers.
     */
    private var specialHandlers: MutableMap<Int, CombatSwingHandler?>? = null

    /**
     * Starts the combat swing.
     * @param entity The attacking entity.
     * @param victim The victim.
     * @param state The battle state instance.
     * @return The amount of ticks before impact of the attack.
     */
    abstract fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int

    /**
     * Handles the impact of the combat swing (victim getting hit).
     * @param entity The attacking entity.
     * @param victim The victim.
     * @param state The battle state instance.
     */
    abstract fun impact(entity: Entity?, victim: Entity?, state: BattleState?)

    /**
     * Visualizes the impact itself (end animation, end GFX, ...)
     * @param entity The attacking entity.
     * @param victim The victim.
     * @param state The battle state instance.
     */
    abstract fun visualizeImpact(entity: Entity?, victim: Entity?, state: BattleState?)

    /**
     * Calculates the maximum accuracy of the entity.
     * @param entity The entity.
     * @return The maximum accuracy value.
     */
    abstract fun calculateAccuracy(entity: Entity?): Int

    /**
     * Calculates the maximum strength of the entity.
     * @param entity The entity.
     * @param victim The victim.
     * @param modifier The modifier.
     * @return The maximum strength value.
     */
    abstract fun calculateHit(entity: Entity?, victim: Entity?, modifier: Double): Int

    /**
     * Calculates the maximum defence of the entity.
     * @param victim The entity.
     * @param attacker The entity to defend against.
     * @return The maximum defence value.
     */
    abstract fun calculateDefence(victim: Entity?, attacker: Entity?): Int

    /**
     * Gets the void set multiplier.
     * @param e The entity.
     * @param skillId The skill id.
     * @return The multiplier.
     */
    abstract fun getSetMultiplier(e: Entity?, skillId: Int): Double

    /**
     * Visualizes the combat swing (start animation, GFX, projectile, ...)
     * @param entity The attacking entity.
     * @param victim The victim.
     * @param state The battle state instance.
     */
    open fun visualize(entity: Entity, victim: Entity?, state: BattleState?) {
        entity.animate(getAttackAnimation(entity, type))
    }

    /**
     * Method called when the impact method got called.
     * @param entity The attacking entity.
     * @param victim The victim.
     * @param state The battle state.
     */
    fun onImpact(entity: Entity?, victim: Entity?, state: BattleState?) {
        if (entity is Player && victim != null) {
            DegradableEquipment.degrade(entity as Player?, victim, true)
        }
        if (state == null) {
            return
        }
        if (state.targets != null && state.targets.isNotEmpty()) {
            if (!(state.targets.size == 1 && state.targets[0] == state)) {
                for (s in state.targets) {
                    if (s != null && s != state) {
                        onImpact(entity, s.victim, s)
                    }
                }
                return
            }
        }
        victim!!.onImpact(entity, state)
    }

    /**
     * Gets the currently worn armour set, if any.
     * @param e The entity.
     * @return The armour set worn.
     */
    open fun getArmourSet(e: Entity?): ArmourSet? {
        return null
    }

    /**
     * Checks if the hit will be accurate.
     * @param entity The entity.
     * @param victim The victim.
     * @return `True` if the hit is accurate.
     */
    fun isAccurateImpact(entity: Entity?, victim: Entity?): Boolean {
        return isAccurateImpact(entity, victim, type, 1.0, 1.0)
    }

    /**
     * Checks if the hit will be accurate.
     * @param entity The entity.
     * @param victim The victim.
     * @param style The combat style used.
     * @return `True` if the hit is accurate.
     */
    fun isAccurateImpact(entity: Entity?, victim: Entity?, style: CombatStyle?): Boolean {
        return isAccurateImpact(entity, victim, style, 1.0, 1.0)
    }

    /**
     * Checks if the hit will be accurate.
     * @param entity The entity.
     * @param victim The victim.
     * @param style The combat style (null to ignore prayers).
     * @param accuracyMod The accuracy modifier.
     * @param defenceMod The defence modifier.
     * @return `True` if the hit is accurate.
     */
    fun isAccurateImpact(entity: Entity?, victim: Entity?, style: CombatStyle?, accuracyMod: Double, defenceMod: Double): Boolean {
        var mod = 1.33
        if (victim == null || style == null) {
            return false
        }
        if (victim is Player && entity is Familiar && victim.prayer[PrayerType.PROTECT_FROM_SUMMONING]) {
            mod = 0.0
        }
        val attack = calculateAccuracy(entity) * accuracyMod * mod * getSetMultiplier(entity, Skills.ATTACK)
        val defence = calculateDefence(victim, entity) * defenceMod * getSetMultiplier(victim, Skills.DEFENCE)
        val chance: Double = if (attack > defence) {
            1 - ((defence + 2) / ((2 * attack) + 1))
        } else {
            attack / ((2 * defence) + 1)
        }
        val ratio = chance * 100
        val accuracy = floor(ratio)
        val block = floor(101 - ratio)
        val acc = Math.random() * accuracy
        val def = Math.random() * block
        return (acc > def).also { if(entity?.username?.toLowerCase() == "test10") SystemLogger.logInfo("Should hit: $it") }
    }

    /**
     * Checks if the entity can execute a combat swing at the victim.
     * @param entity The entity.
     * @param victim The victim.
     * @return `True` if so.
     */
    open fun canSwing(entity: Entity, victim: Entity): InteractionType? {
        return isAttackable(entity, victim)
    }

    /**
     * Checks if the victim can be attacked by the entity.
     * @param entity The attacking entity.
     * @param victim The entity being attacked.
     * @return `True` if so.
     */
    open fun isAttackable(entity: Entity, victim: Entity): InteractionType? {
        val comp = entity.getAttribute("autocast_component",null) as Component?
        if((comp != null || type == CombatStyle.MAGIC) && (entity.properties.autocastSpell == null || entity.properties.autocastSpell.spellId == 0) && entity is Player){
            val weapEx = entity.getExtension<Any>(WeaponInterface::class.java) as WeaponInterface?
            if(comp != null){
                entity.interfaceManager.close(comp)
                entity.interfaceManager.openTab(weapEx)
                entity.properties.combatPulse.stop()
                entity.attack(victim)
                entity.removeAttribute("autocast_component")
            }
            weapEx?.setAttackStyle(1)
            weapEx?.updateInterface()
            entity.debug("Adjusting attack style")
        }
        if (entity.location == victim.location) {
            return if (entity.index < victim.index && victim.properties.combatPulse.getVictim() === entity) {
                InteractionType.STILL_INTERACT
            } else InteractionType.NO_INTERACT
        }
        val el = entity.location
        val vl = victim.location
        val evl = vl.transform(victim.size(), victim.size(), 0)
        if (el.x >= vl.x && el.x < evl.x && el.y >= vl.y && el.y < evl.y || el.z != vl.z) {
            return InteractionType.NO_INTERACT
        }
        return InteractionType.STILL_INTERACT
    }

    /**
     * Gets the dragonfire message.
     * @param protection The protection value.
     * @param fireName The fire breath name.
     * @return The message to send.
     */
    fun getDragonfireMessage(protection: Int, fireName: String): String {
        if (protection and 0x4 != 0) {
            if (protection and 0x2 != 0) {
                return "Your potion and shield fully protects you from the dragon's $fireName."
            }
            return if (protection and 0x8 != 0) {
                "Your prayer and shield absorbs most of the dragon's $fireName."
            } else "Your shield absorbs most of the dragon's $fireName."
        }
        if (protection and 0x2 != 0) {
            return if (protection and 0x8 != 0) {
                "Your prayer and potion absorbs most of the dragon's $fireName."
            } else "Your antifire potion helps you defend the against the dragon's $fireName."
        }
        return if (protection and 0x8 != 0) {
            "Your magic prayer absorbs some of the dragon's $fireName."
        } else "You are horribly burnt by the dragon's $fireName."
    }

    /**
     * Visualizes the audio.
     * @param entity the entity.
     * @param victim the victim.
     * @param state the state.
     */
    fun visualizeAudio(entity: Entity, victim: Entity, state: BattleState) {
        if (entity is Player) {
            val styleIndex = entity.settings.attackStyleIndex
            if (state.weapon != null && state.weapon.item != null) {
                val weapon = state.weapon.item
                val audios = weapon.definition.getConfiguration<Array<Audio>>(ItemConfigParser.ATTACK_AUDIO, null)
                if (audios != null) {
                    var audio: Audio? = null
                    if (styleIndex < audios.size) {
                        audio = audios[styleIndex]
                    }
                    if (audio == null || audio.id == 0) {
                        audio = audios[0]
                    }
                    entity.asPlayer().audioManager.send(audio, true, entity.location)
                }
            } else {
                entity.asPlayer().audioManager.send(2564)
            }
        } else if (entity is NPC && victim is Player) {
            val npc = entity.asNpc()
            val audio = npc.getAudio(0)
            audio?.send(victim.asPlayer(), true, entity.location)
        }
    }

    /**
     * Gets the combat distance.
     * @param e The entity.
     * @param v The victim.
     * @param rawDistance The distance.
     * @return The actual distance used for combat.
     */
    open fun getCombatDistance(e: Entity, v: Entity, rawDistance: Int): Int {
        var distance = rawDistance
        if (e is NPC) {
            if (e.definition.combatDistance > 0) {
                distance = e.definition.combatDistance
            }
        }
        return (e.size() shr 1) + (v.size() shr 1) + distance
    }

    /**
     * Formats the hit for the victim. (called as
     * victim.getSwingHandler(false).formatHit(victim, hit))
     * @param victim The entity receiving the hit.
     * @param rawHit The hit to format.
     * @return The formatted hit.
     */
    fun formatHit(victim: Entity, rawHit: Int): Int {
        var hit = rawHit
        if (hit < 1) {
            return hit
        }
        if (hit > victim.skills.lifepoints) {
            hit = victim.skills.lifepoints
        }
        return hit
    }

    /**
     * Adjusts the battle state object for this combat swing.
     * @param entity The attacking entity.
     * @param victim The victim.
     * @param state The battle state.
     */
    open fun adjustBattleState(entity: Entity, victim: Entity, state: BattleState) {
        EXPERIENCE_MOD = 4.0
        var totalHit = 0
        if (entity is Player) {
            entity.familiarManager.adjustBattleState(state)
        }
        entity.sendImpact(state)
        victim.checkImpact(state)
        //Prevents lumbridge dummies from dying (true to how rs3 / 2009scape in 2009 does it)
        if (victim.id == 4474 && type == CombatStyle.MAGIC || victim.id == 7891 && type == CombatStyle.MELEE) {
            EXPERIENCE_MOD = 0.1
            victim.fullRestore()
            if (state.estimatedHit >= 15) {
                state.estimatedHit = 14
            }
            if (state.secondaryHit >= 15) {
                state.secondaryHit = 14
            }
        }
        if (victim.id == 757) {
            EXPERIENCE_MOD = 0.01
        }
        // Recursively adjustBattleState targets so that multi-target attacks have protection prayers applied.
        if (state.targets != null && state.targets.isNotEmpty()) {
            if (!(state.targets.size == 1 && state.targets[0] == state)) {
                for (s in state.targets) {
                    if (s != null && s != state) {
                        adjustBattleState(entity, victim, s);
                    }
                }
            }
        }
        if (state.estimatedHit > 0) {
            state.estimatedHit = getFormattedHit(entity, victim, state, state.estimatedHit)
            totalHit += state.estimatedHit
        }
        if (state.secondaryHit > 0) {
            state.secondaryHit = getFormattedHit(entity, victim, state, state.secondaryHit)
            totalHit += state.secondaryHit
        }
        if (entity is Player) {
            entity.degrader.checkWeaponDegrades(entity)
            if (totalHit > 0 && entity.prayer[PrayerType.SMITE] && victim.skills.prayerPoints > 0) {
                victim.skills.decrementPrayerPoints(totalHit * 0.25)
            }
            if (entity.getAttribute("1hko", false)) {
                state.estimatedHit = victim.skills.lifepoints
            }
        }
        if (victim is NPC) {
            if (victim.properties.protectStyle != null && state.style == victim.properties.protectStyle) {
                state.neutralizeHits()
            }
        }
    }

    /**
     * Adds the experience for the current combat swing.
     * @param entity The attacking entity.
     * @param victim The victim.
     * @param state The battle state.
     */
    open fun addExperience(entity: Entity?, victim: Entity?, state: BattleState?) {
        if (entity == null || (victim is Player && entity is Player && entity.asPlayer().ironmanManager.isIronman)) {
            return
        }
        var player: Player
        var attStyle: Int

        when(entity)
        {
            is Familiar -> {player = entity.owner; attStyle = entity.attackStyle}
            is Player -> {player = entity; attStyle = entity.properties.attackStyle.style}
            else -> return
        }
        if (state != null) {
            val hit = state.totalDamage
            if (entity is Player) {
                player.skills.addExperience(Skills.HITPOINTS, hit * 1.33, true)
            }

            var skill = -1
            when (attStyle) {
                WeaponInterface.STYLE_DEFENSIVE -> skill = Skills.DEFENCE
                WeaponInterface.STYLE_ACCURATE -> skill = Skills.ATTACK
                WeaponInterface.STYLE_AGGRESSIVE -> skill = Skills.STRENGTH
                WeaponInterface.STYLE_CONTROLLED -> {
                    var experience = hit * EXPERIENCE_MOD
                    experience /= 3.0
                    player.skills.addExperience(Skills.ATTACK, experience, true)
                    player.skills.addExperience(Skills.STRENGTH, experience, true)
                    player.skills.addExperience(Skills.DEFENCE, experience, true)
                    return
                }

                WeaponInterface.STYLE_RANGE_ACCURATE -> skill = Skills.RANGE
                WeaponInterface.STYLE_RAPID -> skill = Skills.RANGE
                WeaponInterface.STYLE_LONG_RANGE -> {
                    player.skills.addExperience(Skills.RANGE, hit * (EXPERIENCE_MOD / 2), true)
                    player.skills.addExperience(Skills.DEFENCE, hit * (EXPERIENCE_MOD / 2), true)
                    return
                }

                WeaponInterface.STYLE_CAST -> skill = Skills.MAGIC
                WeaponInterface.STYLE_DEFENSIVE_CAST -> {
                    player.skills.addExperience(Skills.MAGIC, hit * 1.33, true)
                    player.skills.addExperience(Skills.DEFENCE, hit.toDouble(), true)
                    return
                }
            }
            if (skill < 0) return
            player.skills.addExperience(skill, hit * EXPERIENCE_MOD, true)
        }
    }

    /**
     * Gets the formated hit.
     * @param attacker The attacking entity.
     * @param victim The victim.
     * @param state The battle state.
     * @param rawHit The hit to format.
     * @return The formated hit.
     */
    protected open fun getFormattedHit(attacker: Entity, victim: Entity, state: BattleState, rawHit: Int): Int {
        var hit = rawHit
        hit = attacker.getFormattedHit(state, hit).toInt()
        if (victim is Player) {
            val player = victim.asPlayer()
            val shield = player.equipment[EquipmentContainer.SLOT_SHIELD]
            if (shield != null && shield.id == 13742) {
                if (RandomFunction.random(100) < 71) {
                    hit -= (hit.toDouble() * 0.25).toInt()
                    if (hit < 1) {
                        hit = 0
                    }
                }
            }
            if (shield != null && shield.id == 13740) {
                val reduce = hit.toDouble() * 0.30
                var drain = hit * 0.15
                if (player.skills.prayerPoints > drain && drain > 0) {
                    if (drain < 1) {
                        drain = 1.0
                    }
                    hit -= reduce.toInt()
                    if (hit < 1) {
                        hit = 0
                    }
                    player.skills.decrementPrayerPoints(drain)
                }
            }
        }
        if (attacker is Player) {
            val player = attacker.asPlayer()
            if (player.equipment[3] != null && player.equipment[3].id == 14726 && state.style == CombatStyle.MAGIC) {
                hit += (hit.toDouble() * 0.15).toInt()
            }
        }
        if (attacker is Familiar && victim is Player) {
            if (victim.prayer[PrayerType.PROTECT_FROM_SUMMONING]) {
                hit = 0
            }
        }
        return formatHit(victim, hit)
    }

    /**
     * Gets the default animation of the entity.
     * @param e The entity.
     * @param style The combat style.
     * @return The attack animation.
     */
    fun getAttackAnimation(e: Entity, style: CombatStyle?): Animation {
        var anim: Animation? = null
        if (type != null && e is NPC) {
            anim = e.properties.getCombatAnimation(style!!.ordinal % 3)
        }
        return anim ?: e.properties.attackAnimation
    }

    /**
     * Registers a special attack handler.
     * @param itemId The item id.
     * @param handler The combat swing handler.
     * @return `True` if succesful.
     */
    fun register(itemId: Int, handler: CombatSwingHandler): Boolean {
        if (specialHandlers == null) {
            specialHandlers = HashMap()
        }
        if (specialHandlers!!.containsKey(itemId)) {
            SystemLogger.logErr("Already contained special attack handler for item " + itemId + " - [old=" + specialHandlers!![itemId]!!::class.java.simpleName + ", new=" + handler.javaClass.simpleName + "].")
            return false
        }
        return specialHandlers!!.put(itemId, handler) == null
    }

    /**
     * Gets the special attack handler for the given item id.
     * @param itemId The item id.
     * @return The special attack handler, or `null` if this item has no
     * special attack handler.
     */
    fun getSpecial(itemId: Int): CombatSwingHandler? {
        if (specialHandlers == null) {
            specialHandlers = HashMap()
        }
        return specialHandlers!![itemId]
    }

    companion object {
        /**
         * The amount of experience to get per hit.
         */
		@JvmField
		var EXPERIENCE_MOD = 4.0

        /**
         * Checks if a projectile can be fired from the node location to the victim
         * location.
         * @param entity The node.
         * @param victim The victim.
         * @param checkClose If we are checking for a melee attack rather than a
         * projectile.
         * @return `True` if so.
         */
		@JvmStatic
		fun isProjectileClipped(entity: Node, victim: Node?, checkClose: Boolean): Boolean {
            for(x1 in 0 until entity.size()) {
                for(y1 in 0 until entity.size()) {
                    val src = entity.location.transform(x1, y1, 0)
                    for(x2 in 0 until victim!!.size()) {
                        for(y2 in 0 until victim!!.size()) {
                            val dst = victim!!.location.transform(x2, y2, 0)
                            val path = Pathfinder.PROJECTILE.find(src, 1, dst, 1, 1, 0, 0, 0, false, RegionManager::getClippingFlag)
                            if(path.isSuccessful && (!checkClose || path.points.size <= 1)) {
                                return true
                            }
                        }
                    }
                }
            }
            return false
        }
    }

}
