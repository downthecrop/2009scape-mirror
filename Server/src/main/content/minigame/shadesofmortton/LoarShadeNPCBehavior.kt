package content.minigame.shadesofmortton

import content.data.Quests
import content.region.morytania.handlers.ShadeNPCBehavior
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.impl.Animator
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs09.consts.Components
import org.rs09.consts.NPCs

/**
 * Transformation and behavior logic for the Shades of Mort'ton
 */

// the shades you can fight
class LoarShadeNPCBehavior : NPCBehavior(NPCs.LOAR_SHADOW_1240, NPCs.LOAR_SHADE_1241) {

    override fun beforeDamageReceived(self: NPC, attacker: Entity, state: BattleState) {
        // transforms the shadow into a shade
        ShadeNPCBehavior.shadowToShade(self)

        super.beforeDamageReceived(self, attacker, state)
    }

    override fun onRespawn(self: NPC) {
        // flags the shade as no longer in-game, this will also set isWalks to true in the tick() TODO: add back in
        //removeAttribute(self, MorttonTempleUtils.ATTR_SHADE_ATT)

        super.onRespawn(self)
    }

    override fun onDeathFinished(self: NPC, killer: Entity) {
        /* TODO: add back in
        // quest: counts the loar shade kills during Shades of Mort'ton
        if (killer is Player) {
            val stage = getQuestStage(killer, Quests.SHADES_OF_MORTTON)
            when (stage) {
                15 -> {
                    setQuestStage(killer, Quests.SHADES_OF_MORTTON, 20)
                    sendMessage(killer, "That's one Shade!")
                }

                20 -> {
                    setQuestStage(killer, Quests.SHADES_OF_MORTTON, 25)
                    sendMessage(killer, "That's two Shades!")
                }

                25 -> {
                    setQuestStage(killer, Quests.SHADES_OF_MORTTON, 30)
                    sendMessage(killer, "That's three Shades!")
                }

                30 -> {
                    setQuestStage(killer, Quests.SHADES_OF_MORTTON, 35)
                    sendMessage(killer, "That's four Shades!")
                }

                35 -> {
                    setQuestStage(killer, Quests.SHADES_OF_MORTTON, 40)
                    sendMessage(killer, "That's all five Shades!")
                }
            }

            // minigame: increase sanctity by 2% each kill (only if you have advanced to the temple building quest stage)
            if (inBorders(killer, MorttonTempleUtils.templeZone) && stage >= 47) {
                openOverlay(killer, Components.TEMPLE_SCREEN_328)
                MorttonTempleUtils.updateSanctity(killer, (2 * 30))
            }
        }
         */
        super.onDeathFinished(self, killer)
    }

    override fun beforeAttackFinalized(self: NPC, victim: Entity, state: BattleState) {
        // transforms the shadows into shades
        ShadeNPCBehavior.shadowToShade(self)

        // shade attacks have a 1/20 chance to lower str by 1 on a successful hit
        if (RandomFunction.roll(20)) {
            adjustLevel(victim, Skills.STRENGTH, -1)
            animate(self, ShadeNPCBehavior.SHADE_DRAIN_ANIM, true)
            visualize(victim, Animation(ShadeNPCBehavior.SHADE_VICTIM_DRAIN_ANIM, Animator.Priority.HIGH), ShadeNPCBehavior.SHADE_DRAIN_GFX)
        }

        super.beforeAttackFinalized(self, victim, state)
    }

    override fun tick(self: NPC): Boolean {
        /* TODO: add back in
        // if the shade is currently participating in Shades of Mort'ton temple building
        val inGame = getAttribute(self, MorttonTempleUtils.ATTR_SHADE_ATT, false)

        // make sure non-in-game shades don't remain movement locked
        if (!inGame) self.isWalks = true
         */

        return super.tick(self)
    }
}