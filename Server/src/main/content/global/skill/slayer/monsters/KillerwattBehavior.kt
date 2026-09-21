package content.global.skill.slayer.monsters

import content.global.skill.slayer.Tasks
import core.api.getAttribute
import core.api.inEquipment
import core.api.playAudio
import core.api.queueScript
import core.api.removeAttributes
import core.api.setAttribute
import core.api.stopExecuting
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.InteractionType
import core.game.node.entity.combat.MagicalRangedSwingHandler
import core.game.node.entity.combat.MultiSwingHandler
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.impl.Animator
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import org.rs09.consts.Animations
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

/**
 * Handles the special mechanics of the Killerwatt slayer monster NPC.
 * @author Bishop
 */

class KillerwattBehavior : NPCBehavior(*Tasks.KILLERWATTS.ids) {

    companion object {
        /**
         * The Killerwatt is relentless and will not forget about any player who has attacked it until one
         * of the two parties dies, the player leaves or logs off, or the Killerwatt is attacked by a
         * different player.
         */
        private const val ATTRIBUTE_TARGET       = "target"
        /**
         * The Killerwatt has a mechanic where it will only elect to use its magical ranged attack when
         * the first strike of the fight was from the opposing party. Otherwise, it will only use this
         * attack in the event that it is safespotted.
         */
        private const val ATTRIBUTE_FIRST_BLOOD  = "first-blood"
        private const val FIRST_BLOOD_NULL       = 0
        private const val FIRST_BLOOD_VICTIM     = 1
        private const val FIRST_BLOOD_KILLERWATT = 2

        private const val KILLERWATT_REGION_ID   = 10577

        private val killerwattMeleeAttack = SwitchAttack(
            CombatStyle.MELEE.swingHandler,
            Animation(Animations.KILLERWATT_ATTACK_MELEE_3163, Animator.Priority.HIGH),
            null, null, null
        )

        // Ranged attack looks exactly like wind strike
        private val killerwattRangedAttack = SwitchAttack(
            MagicalRangedSwingHandler,
            Animation(Animations.KILLERWATT_ATTACK_RANGED_3164, Animator.Priority.HIGH),
            Graphics(90, 96), Graphics(92, 96),
            Projectile.create(
                null as Entity?, null, 91, 40,
                36, 52, 75, 15, 11
            )
        )

        fun aggressUponKillerwatt(player: Player, self: NPC) {
            if (self.id == NPCs.KILLERWATT_3202) {
                playAudio(player, Sounds.KILLERWATT_TRANSFORMS_582)
                self.transformWithHpCarryover(NPCs.KILLERWATT_3201)
            }
            setAttribute(self, ATTRIBUTE_TARGET, player)
            self.attack(player)
        }
    }

    override fun tick(self: NPC): Boolean {
        val target: Player? = getAttribute(self, ATTRIBUTE_TARGET, null)
        if (self.id == NPCs.KILLERWATT_3202 || target == null || !target.isActive || target.location.regionId != KILLERWATT_REGION_ID) {
            removeAttributes(self, ATTRIBUTE_TARGET, ATTRIBUTE_FIRST_BLOOD)
            self.reTransform()
            return super.tick(self)
        }
        if (!self.properties.combatPulse.isInCombat && !target.properties.combatPulse.isInCombat) {
            self.attack(target)
        }
        return super.tick(self)
    }

    override fun beforeAttackFinalized(self: NPC, victim: Entity, state: BattleState) {
        if (getAttribute(self, ATTRIBUTE_FIRST_BLOOD, FIRST_BLOOD_NULL) == FIRST_BLOOD_NULL) {
            setAttribute(self, ATTRIBUTE_FIRST_BLOOD, FIRST_BLOOD_KILLERWATT)
        }
        // Only punish no-boots players on ranged attacks
        if (victim is Player && !inEquipment(victim, Items.INSULATED_BOOTS_7159) && state.style == CombatStyle.RANGE) {
            state.estimatedHit = RandomFunction.random(15)
            victim.properties.defenceAnimation = Animation(Animations.HUMAN_ELECTROCUTE_3171)
            queueScript(victim, 1, QueueStrength.SOFT) {
                val sound = if (RandomFunction.random(2) == 1) {
                    Sounds.KILLERWATT_ELECTROCUTES_577
                } else {
                    Sounds.KILLERWATT_ELECTROCUTE_581
                }
                playAudio(victim, sound)
                victim.properties.updateDefenceAnimation()
                return@queueScript stopExecuting(victim)
            }
        }
    }

    override fun beforeDamageReceived(self: NPC, attacker: Entity, state: BattleState) {
        if (getAttribute(self, ATTRIBUTE_FIRST_BLOOD, FIRST_BLOOD_NULL) == FIRST_BLOOD_NULL) {
            setAttribute(self, ATTRIBUTE_FIRST_BLOOD, FIRST_BLOOD_VICTIM)
        }
        if (attacker is Player && self.id == NPCs.KILLERWATT_3202) {
            aggressUponKillerwatt(attacker, self)
        }
    }

    /**
     * The Killerwatt will use its magical ranged attack exclusively if it cannot close the distance
     * with the player, but if it can once again close the distance it will stop spamming that attack.
     */
    override fun getSwingHandlerOverride(self: NPC, original: CombatSwingHandler): CombatSwingHandler {
        val target: Player? = getAttribute(self, ATTRIBUTE_TARGET, null)
        val isSafeSpotted = (CombatStyle.MELEE.swingHandler.canStepTowards(self, target as Entity) == InteractionType.NO_INTERACT)
        val firstBlood = getAttribute(self, ATTRIBUTE_FIRST_BLOOD, FIRST_BLOOD_NULL)
        if (firstBlood == FIRST_BLOOD_NULL) {
            return MultiSwingHandler(killerwattMeleeAttack)
        }
        return if (isSafeSpotted || (RandomFunction.roll(2) && firstBlood == FIRST_BLOOD_VICTIM)) {
            MultiSwingHandler(killerwattRangedAttack)
        } else {
            MultiSwingHandler(killerwattMeleeAttack)
        }
    }

    override fun onDeathFinished(self: NPC, killer: Entity) {
        removeAttributes(self, ATTRIBUTE_TARGET, ATTRIBUTE_FIRST_BLOOD)
        self.reTransform()
    }

}