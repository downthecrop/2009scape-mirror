package rs09.game.node.entity.skill.farming

import core.game.node.Node
import core.game.node.entity.player.Player
import rs09.game.interaction.InteractionListener

class FarmerPayOptionHandler : InteractionListener() {

    override fun defineListeners() {
        on(NPC,"pay","pay (north)","pay (north-west)"){player,node ->
            return@on attemptPay(player,node,0)
        }

        on(NPC,"pay (south)","pay (south-east)"){player,node ->
            return@on attemptPay(player,node,1)
        }
    }

    fun attemptPay(player: Player, node: Node, index: Int): Boolean{
        val farmer = Farmers.forId(node.id) ?: return false
        val patch = farmer.patches[index].getPatchFor(player)

        if(patch.plantable == null){
            player.dialogueInterpreter.sendDialogue("I have nothing to protect in that patch.")
            return true
        }

        if(patch.protectionPaid){
            player.dialogueInterpreter.sendDialogue("I have already paid to protect that patch.")
            return true
        }

        if(patch.isGrown()){
            player.dialogueInterpreter.sendDialogue("This patch is already fully grown!")
            return true
        }

        player.dialogueInterpreter.open(FarmerPayOptionDialogue(patch),node.asNpc())
        return true
    }
}