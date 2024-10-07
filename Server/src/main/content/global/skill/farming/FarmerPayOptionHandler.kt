package content.global.skill.farming

import core.api.openDialogue
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

class FarmerPayOptionHandler : InteractionListener {

    override fun defineListeners() {
        on(IntType.NPC,"pay","pay (north)","pay (north-west)") { player, node ->
            return@on attemptPay(player,node,0)
        }

        on(IntType.NPC,"pay (south)","pay (south-east)") { player, node ->
            return@on attemptPay(player,node,1)
        }
    }

    fun attemptPay(player: Player, node: Node, index: Int): Boolean {
        val farmer = Farmers.forId(node.id) ?: return false
        val patch = farmer.patches[index].getPatchFor(player)

        openDialogue(player, FarmerPayOptionDialogue(patch, true), node.asNpc())
        return true
    }
}