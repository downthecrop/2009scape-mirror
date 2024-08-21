package content.region.asgarnia.falador.quest.recruitmentdrive

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import org.rs09.consts.NPCs

class SirLeyeBehavior : NPCBehavior(NPCs.SIR_LEYE_2285) {
    var clearTime = 0

    override fun tick(self: NPC): Boolean {
        // You have 400 ticks to kill Sir Leye
        if (clearTime++ > 400) {
            clearTime = 0
            poofClear(self)
        }
        return true
    }

    override fun beforeDamageReceived(self: NPC, attacker: Entity, state: BattleState) {
        val lifepoints = self.skills.lifepoints
        if (attacker is Player) {
            // If you are male, Sir Leye will recover to full health.
            if (attacker.isMale) {
                if (state.estimatedHit + Integer.max(state.secondaryHit, 0) > lifepoints - 1) {
                    self.skills.lifepoints = self.getSkills().getStaticLevel(Skills.HITPOINTS)
                }
            }
        }
    }

    override fun onDeathFinished(self: NPC, killer: Entity) {
        if (killer is Player) {
            clearHintIcon(killer)
            setAttribute(killer, RecruitmentDrive.attributeStagePassFailState, 1)
            removeAttribute(killer, SirKuamFerentseDialogueFile.attributeGeneratedSirLeye)
        }
    }

    // No xp from attacking this dude.
    override fun getXpMultiplier(self: NPC, attacker: Entity): Double {
        return 0.0
    }

}