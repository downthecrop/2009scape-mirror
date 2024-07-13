package content.region.morytania.canifis.handlers

import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items

class WerewolfBehavior : NPCBehavior(*HUMAN_NPCS) {
    companion object {
        // There are 20 humans that can turn into werewolves. They are all in series, so a range toIntArray() is easier.
        private val HUMAN_NPCS = (6026 .. 6045).toIntArray()
        private val WEREWOLF_NPCS = (6006 .. 6025).toIntArray()
        private val HUMAN_OUT_ANIMATION = Animation(6554)
        private val WEREWOLF_IN_ANIMATION = Animation(6543) // This is not used as there is a corresponding gfx.
        private val WEREWOLF_IN_GFXS = (1079 .. 1098).toIntArray() // Play each werewolf's gfx with the animation.
    }

    override fun afterDamageReceived(self: NPC, attacker: Entity, state: BattleState) {
        if(DeathTask.isDead(self)){
            // Don't transform if you are killed
            return
        }
        if (attacker is Player) {
            if (!inEquipment(attacker, Items.WOLFBANE_2952, 1) && self.id in HUMAN_NPCS) {
                delayAttack(self, 3)
                delayAttack(attacker, 3)
                lock(self, 3)
                queueScript(self, 0, QueueStrength.SOFT) { stage: Int ->
                    when (stage) {
                        0 -> {
                            visualize(self, HUMAN_OUT_ANIMATION, WEREWOLF_IN_GFXS[self.id - 6026])
                            return@queueScript delayScript(self, WEREWOLF_IN_ANIMATION.duration)
                        }
                        1 -> {
                            transformNpc(self, WEREWOLF_NPCS[self.id - 6026], 200)
                            return@queueScript delayScript(self, 1)
                        }
                        2 -> {
                            self.properties.combatPulse.attack(attacker)
                            return@queueScript stopExecuting(self)
                        }
                        else -> return@queueScript stopExecuting(self)
                    }
                }
            }
        }
    }

    override fun onRespawn(self: NPC) {
        if (self.id in WEREWOLF_NPCS){
            self.reTransform()
        }
        super.onRespawn(self)
    }
}
