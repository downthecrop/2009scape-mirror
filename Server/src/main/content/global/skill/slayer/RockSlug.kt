package content.global.skill.slayer

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import org.rs09.consts.Items
import java.lang.Integer.max

class RockSlug : NPCBehavior(*Tasks.ROCK_SLUGS.ids), InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.NPC, Items.BAG_OF_SALT_4161, *ids, handler = ::handleSaltUsage)
    }

    override fun beforeDamageReceived(self: NPC, attacker: Entity, state: BattleState) {
        val lifepoints = self.skills.lifepoints
        if (state.estimatedHit + max(state.secondaryHit, 0) > lifepoints - 1) {
            state.estimatedHit = lifepoints - 1
            state.secondaryHit = -1
            setAttribute(self, "shouldRun", true)
        }
    }

    override fun tick(self: NPC): Boolean {
        if (getAttribute(self, "shouldRun", false)){
            self.properties.combatPulse.stop()
            forceWalk(self, self.properties.spawnLocation, "smart")
            removeAttribute(self, "shouldRun")
        }
        return true
    }

    private fun handleSaltUsage(player: Player, used: Node, with: Node) : Boolean {
        if (with !is NPC) return false
        if (!removeItem(player, used.id)) return false
        if (with.skills.lifepoints >= 5)
            sendMessage(player, "Your bag of salt is ineffective. The Rockslug is not weak enough.")
        else {
            sendMessage(player, "The Rockslug shrivels up and dies.")
            with.impactHandler.manualHit(player, with.skills.lifepoints, ImpactHandler.HitsplatType.NORMAL)
        }
        return true
    }
}