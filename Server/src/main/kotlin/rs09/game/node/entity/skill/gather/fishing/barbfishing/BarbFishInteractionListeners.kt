package rs09.game.node.entity.skill.gather.fishing.barbfishing

import core.game.node.item.Item
import rs09.game.interaction.InteractionListener

class BarbFishInteractionListeners : InteractionListener() {
    override fun defineListeners() {

        on(25268,SCENERY,"search"){ player, _ ->
            if(player.getAttribute("barbtraining:fishing",false) == true){
                if(!player.inventory.containsItem(Item(11323))){
                    player.inventory.add(Item(11323))
                    player.sendMessage("Under the bed you find a fishing rod.")
                } else {
                    player.sendMessage("You find nothing under the bed")
                }
            } else {
                player.sendMessage("Maybe I should speak to Otto before looking under his bed.")
            }
            return@on true
        }

        on(1176,NPC,"fish"){player,_ ->
            player.pulseManager.run(BarbFishingPulse(player))
            player.sendMessage("You attempt to catch a fish...")
            return@on true
        }

    }
}