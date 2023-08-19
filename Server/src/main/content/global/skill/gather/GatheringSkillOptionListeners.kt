package content.global.skill.gather

import content.global.skill.fishing.FishingSpot
import content.global.skill.gather.fishing.FishingPulse
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player

class GatheringSkillOptionListeners : InteractionListener {

    val ETCETERIA_REGION = 10300

    override fun defineListeners() {

    }

    fun fish(player: Player, node: Node, opt: String): Boolean{
        val npc = node as NPC
        val spot = FishingSpot.forId(npc.id) ?: return false
        val op = spot.getOptionByName(opt) ?: return false
        player.pulseManager.run(FishingPulse(player, npc, op))
        return true
    }
}
