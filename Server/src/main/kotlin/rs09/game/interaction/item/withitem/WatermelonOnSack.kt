package rs09.game.interaction.item.withitem

import api.Container
import api.*
import core.game.node.entity.skill.Skills
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class WatermelonOnSack : InteractionListener() {
    val SACK = Items.HAY_SACK_6058
    val WATERMELON = Items.WATERMELON_5982

    override fun defineListeners() {
        onUseWith(ITEM, SACK, WATERMELON){player, used, _ ->
            if(getStatLevel(player, Skills.FARMING) >= 23){
                removeItem(player,SACK, Container.INVENTORY)
                removeItem(player,WATERMELON,Container.INVENTORY)
                addItem(player, Items.SCARECROW_6059)
                rewardXP(player, Skills.FARMING, 25.0)
                sendMessage(player, "You stab the watermelon on top of the spear, finishing your scarecrow")
            }else{
                sendMessage(player, "Your Farming level is not high enough to do this")
            }
            return@onUseWith true
        }
    }
}