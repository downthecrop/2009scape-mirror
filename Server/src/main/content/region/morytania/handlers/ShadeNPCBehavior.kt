package content.region.morytania.handlers

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.impl.Animator
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

/**
 * Transformation and behavior logic for the Shades of Mort'ton
 */

// all the shades and shadows, sans Loar (handled separately since it is in the minigame)
class ShadeNPCBehavior : NPCBehavior(*shadows, *shades) {

    override fun beforeDamageReceived(self: NPC, attacker: Entity, state: BattleState) {
        // transforms the shadows into shades
        shadowToShade(self)

        super.beforeDamageReceived(self, attacker, state)
    }

    override fun beforeAttackFinalized(self: NPC, victim: Entity, state: BattleState) {
        // transforms the shadows into shades
        shadowToShade(self)

        // shade attacks have a 1/20 chance to lower str by 1 on a successful hit
        if (RandomFunction.roll(20)) {
            adjustLevel(victim, Skills.STRENGTH, -1)
            animate(self, SHADE_DRAIN_ANIM, true)
            visualize(victim, Animation(SHADE_VICTIM_DRAIN_ANIM, Animator.Priority.HIGH), SHADE_DRAIN_GFX)
        }

        super.beforeAttackFinalized(self, victim, state)
    }

    // helpers for this and Shades of Mort'ton stuff
    companion object {
        val shadows = intArrayOf(NPCs.PHRIN_SHADOW_1243, NPCs.RIYL_SHADOW_1245, NPCs.ASYN_SHADOW_1247, NPCs.FIYR_SHADOW_1249)
        val shades = intArrayOf(NPCs.PHRIN_SHADE_1244, NPCs.RIYL_SHADE_1246, NPCs.ASYN_SHADE_1248, NPCs.FIYR_SHADE_1250)

        const val SHADE_DRAIN_GFX = 293
        const val SHADE_VICTIM_DRAIN_ANIM = 1307
        const val SHADE_DRAIN_ANIM = 1283
        //const val SHADE_MELEE_ANIM = 1284
        //const val SHADE_DEF_ANIM = 1286
        //const val SHADE_DEATH_ANIM = 1287
        const val SHADE_UP_ANIM = 1288

        // transforms a shadow to a shade
        fun shadowToShade(self: NPC) {
            if (self.id in shadows || self.id == NPCs.LOAR_SHADOW_1240) {
                val anim = Animation(SHADE_UP_ANIM)
                animate(self, anim, true)
                playGlobalAudio(self.location, Sounds.SHADE_APPEAR_744)
                self.transformWithHpCarryover(self.id+1)
            }
        }
    }
}