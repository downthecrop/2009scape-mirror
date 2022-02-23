package rs09.game.node.entity.combat

import core.game.container.impl.EquipmentContainer
import core.game.interaction.MovementPulse
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.combat.InteractionType
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.impl.Animator
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.audio.Audio
import core.game.node.entity.skill.Skills
import core.game.node.entity.state.EntityState
import core.game.node.item.Item
import core.game.system.task.Pulse
import rs09.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction

/**
 * The combat-handling pulse implementation.
 * @author Emperor
 */
class CombatPulse(
        /**
         * The entity.
         */
        private val entity: Entity?) : Pulse(1, entity, null) {

    /**
     * The victim.
     */
    private var victim: Entity? = null

    /**
     * Gets the style.
     * @return The style.
     */
    /**
     * Sets the style.
     * @param style The style to set.
     */
    /**
     * The current combat style used.
     */
    var style = CombatStyle.MELEE

    /**
     * @return the temporaryHandler.
     */
    /**
     * @param temporaryHandler the temporaryHandler to set.
     */
    /**
     * The temporary combat swing handler.
     */
    var temporaryHandler: CombatSwingHandler? = null

    /**
     * Gets the handler.
     * @return The handler.
     */
    /**
     * Sets the handler.
     * @param handler The handler to set.
     */
    /**
     * The current combat swing handler.
     */
    var handler = style.swingHandler

    /**
     * @return the lastVictim.
     */
    /**
     * @param lastVictim the lastVictim to set.
     */
    /**
     * The last victim.
     */
    var lastVictim: Entity? = null

    /**
     * The tick value of when we can start another hit-cycle.
     */
    private var nextAttack = -1

    /**
     * The combat time out counter.
     */
    private var combatTimeOut = 0

    /**
     * The movement handling pulse.
     */
    private val movement: MovementPulse

    /**
     * The last attack sent.
     */
    var lastSentAttack = 0

    /**
     * The last attack recieved.
     */
    var lastReceivedAttack = 0
    override fun pulse(): Boolean {
        if (victim == null || DeathTask.isDead(entity) || DeathTask.isDead(victim)) {
            return true
        }
        if (!entity!!.viewport.region.isActive || !victim!!.viewport.region.isActive) {
            return true
        }
        if (!interactable()) {
            return if (entity.walkingQueue.isMoving) {
                false
            } else combatTimeOut++ > entity.properties.combatTimeOut
        }
        combatTimeOut = 0
        entity.face(victim)
        if (nextAttack <= GameWorld.ticks) {
            victim ?: return false
            val v: Entity = victim!!
            var handler = temporaryHandler
            if (handler == null) {
                handler = entity.getSwingHandler(true)
            }
            if (!v.isAttackable(entity, handler!!.type, true)) {
                return true
            }
            if (!swing(entity, victim, handler)) {
                temporaryHandler = null
                updateStyle()
                return false
            }
            var speed = entity.properties.attackSpeed
            val magic = handler!!.type == CombatStyle.MAGIC
            if (entity is Player && magic) {
                speed = 5
            } else if (entity.properties.attackStyle.style == WeaponInterface.STYLE_RAPID) {
                speed--
            }
            if (!magic && entity.stateManager.hasState(EntityState.MIASMIC)) {
                speed = (speed * 1.5).toInt()
            }
            setNextAttack(speed)
            temporaryHandler = null
            setCombatFlags(v)
        }
        return victim != null && victim!!.skills.lifepoints < 1 || entity.skills.lifepoints < 1
    }

    /**
     * Sets the "in combat" flag for the victim and handles closing.
     * @param victim The victim.
     */
    fun setCombatFlags(victim: Entity?) {
        if (victim == null || entity == null) {
            return
        }
        if (entity is Player) {
            val p = entity
            if (!p.attributes.containsKey("keepDialogueAlive")) {
                p.interfaceManager.close()
                p.interfaceManager.closeChatbox()
            }
        }
        if (victim is Player) {
            if (entity is Player && entity.skullManager.isWilderness) {
                entity.skullManager.checkSkull(victim)
            }
            if (!victim.getAttributes().containsKey("keepDialogueAlive")) {
                victim.interfaceManager.closeChatbox()
                victim.interfaceManager.close()
            }
        }
        if (!victim.pulseManager.isMovingPulse) {
            victim.pulseManager.clear("combat")
        }
        victim.setAttribute("combat-time", System.currentTimeMillis() + 10000)
        victim.setAttribute("combat-attacker", entity)
    }

    /**
     * Checks if the mover can interact with the victim.
     * @return `True` if so.
     */
    private fun interactable(): Boolean {
        if (victim == null) {
            return false
        }
        if (entity is NPC && victim is Player && entity.isHidden(victim as Player?)) {
            stop()
            return false
        }
        if (victim is NPC && entity is Player && (victim as NPC).isHidden(entity as Player?)) {
            stop()
            return false
        }
        if (entity is NPC && !entity.asNpc().canStartCombat(victim)) {
            stop()
            return false
        }
        val type = canInteract()
        if (type == InteractionType.STILL_INTERACT) {
            return true
        }
        if (entity == null || victim == null || entity.locks.isMovementLocked) {
            return false
        }
        movement.findPath()
        return type == InteractionType.MOVE_INTERACT
    }

    /**
     * Sets the combat style.
     */
    fun updateStyle() {
        if (entity == null) {
            return
        }
        if (entity is Player) {
            val p = entity
            if (p.properties.spell != null) {
                style = CombatStyle.MAGIC
                return
            }
            if (p.properties.autocastSpell != null) {
                style = CombatStyle.MAGIC
                return
            }
            style = when (p.properties.attackStyle.bonusType) {
                WeaponInterface.BONUS_MAGIC -> CombatStyle.MAGIC
                WeaponInterface.BONUS_RANGE -> CombatStyle.RANGE
                else -> CombatStyle.MELEE
            }
        }
    }

    /**
     * Attacks the node.
     * @param victim The victim node.
     */
    fun attack(victim: Node?) {
        if (victim == null) {
            return
        }
        if (entity!!.locks.isInteractionLocked) {
            return
        }
        if (victim === this.victim && isAttacking) {
            return
        }
        //makes sure lumbridge dummies can't attack back (lol)
        if (victim is Player && (entity.id == 4474 || entity.id == 7891)) {
            return
        }
        if(entity is Player) {
            if ((victim.id == 4474 || victim.id == 7891) && entity.asPlayer().properties.combatLevel >= 30){
                entity.asPlayer().sendMessage("You are too experienced to gain anything from these.")
                return
            }
        }
        if (victim is NPC) {
            if (entity is Player && victim !== this.victim && victim !== lastVictim) {
                // Loar Shade Transformation Animation
                val shade = Animation(1288, 0, Animator.Priority.VERY_HIGH)
                val player = entity
                val mask = player.equipment[EquipmentContainer.SLOT_HAT]
                if (victim.getId() == 1240) {
                    victim.animate(shade)
                    victim.transform(1241)
                }
                if (mask != null && mask.id >= 8901 && mask.id < 8920 && RandomFunction.random(50) == 0) {
                    player.packetDispatch.sendMessage("Your black mask startles your enemy, you have " +
                            (if (mask.id == 8919) "no" else ((8920 - mask.id) / 2).toString()) + " charges left.")
                    player.equipment.replace(Item(mask.id + 2), EquipmentContainer.SLOT_HAT)
                    var drain = 3 + victim.skills.getLevel(Skills.DEFENCE) / 14
                    if (drain > 10) {
                        drain = 10
                    }
                    victim.skills.updateLevel(Skills.DEFENCE, -drain, victim.skills.getStaticLevel(Skills.DEFENCE) - drain)
                }
            }
            victim.walkingQueue.reset()
        }
        setVictim(victim)
        entity.onAttack(victim as Entity?)
        entity.pulseManager.run(this)
    }

    /**
     * Sets the victim.
     * @param victim The victim.
     */
    fun setVictim(victim: Node?) {
        super.addNodeCheck(1, victim)
        movement.setLast(null)
        movement.setDestination(victim)
        this.victim = victim as Entity?
        combatTimeOut = 0
    }

    /**
     * Sets the next attack.
     * @param ticks The amount of ticks.
     */
    fun setNextAttack(ticks: Int) {
        nextAttack = GameWorld.ticks + ticks
    }

    /**
     * Delays the next attack.
     * @param ticks The amount of ticks to delay the next attack with.
     */
    fun delayNextAttack(ticks: Int) {
        nextAttack += ticks
    }

    /**
     * Gets the next attack tick.
     * @return The next attack tick.
     */
    fun getNextAttack(): Int {
        return nextAttack
    }

    /**
     * Checks if we can fight with the victim.
     * @return `True` if so.
     */
    fun canInteract(): InteractionType? {
        if (victim == null) {
            return InteractionType.NO_INTERACT
        }
        return if (temporaryHandler != null) {
            temporaryHandler!!.canSwing(entity!!, victim!!)
        } else entity!!.getSwingHandler(false).canSwing(entity, victim!!)
    }

    override fun start() {
        super.start()
        entity!!.face(victim)
    }

    override fun stop() {
        super.stop()
        entity!!.setAttribute("combat-stop", GameWorld.ticks)
        if (victim != null) {
            lastVictim = victim
        }
        super.addNodeCheck(1, null.also { victim = it })
        entity.face(null)
        entity.properties.spell = null
    }

    override fun removeFor(pulseType: String): Boolean {
        var pulse = pulseType
        if (isAttacking) {
            pulse = pulse.toLowerCase()
            if (pulse.startsWith("interaction:attack")) {
                if (victim.hashCode() == pulse.replace("interaction:attack:", "").toInt()) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * Checks if this entity is attacking.
     * @return `True` if so.
     */
    val isAttacking: Boolean
        get() = victim != null && victim!!.isActive && isRunning

    /**
     * If the entity has an attacker.
     * @return `True` if so.
     */
    val isInCombat: Boolean
        get() {
            val entity = entity!!.getAttribute<Entity>("combat-attacker")
            return entity != null && entity.properties.combatPulse.isAttacking
        }

    /**
     * Gets the current victim.
     * @return The victim.
     */
    fun getVictim(): Entity? {
        return victim
    }

    companion object {

        /**
         * Executes a combat swing.
         * @param entity The entity.
         * @param victim The victim.
         * @param handler The combat swing handler.
         * @return `True` if successfully started the swing.
         */
        fun swing(entity: Entity?, victim: Entity?, handler: CombatSwingHandler?): Boolean {
            val state = BattleState(entity, victim)
            val set = handler!!.getArmourSet(entity)
            entity!!.properties.armourSet = set
            val delay = handler.swing(entity, victim, state)
            if (delay < 0) {
                return false
            }
            if (victim == null) {
                entity.faceTemporary(victim, 1) // face back to entity.
            }
            handler.adjustBattleState(entity, victim!!, state)
            handler.addExperience(entity, victim, state)
            handler.visualize(entity, victim, state)
            if (delay - 1 < 1) {
                handler.visualizeImpact(entity, victim, state)
            }
            handler.visualizeAudio(entity, victim, state)
            if (set != null && set.effect(entity, victim, state)) {
                set.visualize(entity, victim)
            }
            GameWorld.Pulser.submit(object : Pulse(delay - 1, entity, victim) {
                var impact = false
                override fun pulse(): Boolean {
                    if (DeathTask.isDead(victim)) {
                        return true
                    }
                    if (impact || getDelay() == 0) {
                        if (state.estimatedHit != 0 && victim is NPC && entity is Player) {
                            val n = victim.asNpc()
                            val audio = n.getAudio(1)
                            if (audio != null) {
                                entity.asPlayer().audioManager.send(audio, true)
                            }
                        } else if (state.estimatedHit != 0 && victim is Player) {
                            val sounds = intArrayOf(516, 517, 518)
                            val audio = Audio(sounds[RandomFunction.random(sounds.size)])
                            audio.send(victim.asPlayer(), true)
                        }
                        handler.impact(entity, victim, state)
                        handler.onImpact(entity, victim, state)
                        return true
                    }
                    setDelay(1)
                    impact = true
                    handler.visualizeImpact(entity, victim, state)
                    return false
                }
            })
            return true
        }
    }

    init {
        movement = object : MovementPulse(entity, null) {
            override fun pulse(): Boolean {
                return false
            }
        }
    }
}
