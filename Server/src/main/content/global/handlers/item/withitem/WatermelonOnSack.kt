package content.global.handlers.item.withitem

import core.api.Container
import core.api.*
import core.game.node.entity.skill.Skills
import org.rs09.consts.Items
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

class WatermelonOnSack : InteractionListener {
    val SACK = Items.HAY_SACK_6058
    val WATERMELON = Items.WATERMELON_5982

    override fun defineListeners() {
        onUseWith(IntType.ITEM, SACK, WATERMELON){ player, used, _ ->
            if(getStatLevel(player, Skills.FARMING) >= 23){
                if (removeItem(player,SACK, Container.INVENTORY) && removeItem(player,WATERMELON,Container.INVENTORY) && addItem(player, Items.SCARECROW_6059)) {
                    rewardXP(player, Skills.FARMING, 25.0)
                    sendMessage(player, "You stab the watermelon on top of the spear, finishing your scarecrow")
                }
            }else{
                sendMessage(player, "Your Farming level is not high enough to do this")
            }
            return@onUseWith true
        }
    }
}