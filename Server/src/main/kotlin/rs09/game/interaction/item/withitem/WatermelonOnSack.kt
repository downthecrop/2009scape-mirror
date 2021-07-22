package rs09.game.interaction.item.withitem

import api.Container
import api.ContentAPI
import core.game.node.entity.skill.Skills
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class WatermelonOnSack : InteractionListener() {
    val SACK = Items.HAY_SACK_6058
    val WATERMELON = Items.WATERMELON_5982

    override fun defineListeners() {
        onUseWith(ITEM, SACK, WATERMELON){player, used, _ ->
            if(ContentAPI.getStatLevel(player, Skills.FARMING) >= 23){
                ContentAPI.removeItem(player,SACK, Container.INVENTORY)
                ContentAPI.removeItem(player,WATERMELON,Container.INVENTORY)
                ContentAPI.addItem(player, Items.SCARECROW_6059)
                ContentAPI.rewardXP(player, Skills.FARMING, 25.0)
                ContentAPI.sendMessage(player, "You stab the watermelon on top of the spear, finishing your scarecrow")
            }else{
                ContentAPI.sendMessage(player, "Your Farming level is not high enough to do this")
            }
            return@onUseWith true
        }
    }
}