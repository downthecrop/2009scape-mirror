package content.global.handlers.iface

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.consts.Components
import org.rs09.consts.Items
import core.game.interaction.InterfaceListener

class CreditShopInterface : InterfaceListener {

    val CREDIT_SHOP = Components.CREDIT_SHOP
    val TEXT_CHILD = 39

    override fun defineInterfaceListeners() {
        on(CREDIT_SHOP){player, component, opcode, buttonID, slot, itemID ->
            val item = getItem(buttonID)

            if(opcode == 155){
                sendDialogue(player, "This item costs ${item.price} credits.")
                return@on true
            }

            if(buttonID == 14 || buttonID == 21){
                val specific = when(opcode){
                    196 -> if(buttonID == 14) Items.RED_PARTYHAT_1038 else Items.RED_HWEEN_MASK_1057
                    124 -> if(buttonID == 14) Items.GREEN_PARTYHAT_1044 else Items.GREEN_HWEEN_MASK_1053
                    199 -> if(buttonID == 14) Items.BLUE_PARTYHAT_1042 else Items.BLUE_HWEEN_MASK_1055
                    234 -> Items.YELLOW_PARTYHAT_1040
                    168 -> Items.PURPLE_PARTYHAT_1046
                    166 -> Items.WHITE_PARTYHAT_1048
                    else -> Items.DWARF_WEED_SEED_5303
                }
                attemptPurchase(player,specific,item.price)
            } else {
                attemptPurchase(player,item.id,item.price)
            }
            return@on true
        }

        onOpen(CREDIT_SHOP){player, component ->
            sendCredits(player)
            return@onOpen true
        }
    }

    private fun getItem(buttonID: Int): ShopItem {
        return when(buttonID){
            14 -> ShopItem(Items.BLUE_PARTYHAT_1042,75)
            18 -> ShopItem(Items.SCYTHE_1419,100)
            20 -> ShopItem(Items.JANGLES_THE_MONKEY_14648,200)
            17 -> ShopItem(Items.CHRISTMAS_CRACKER_962,65)
            21 -> ShopItem(Items.BLUE_HWEEN_MASK_1055,65)
            16 -> ShopItem(Items.SANTA_HAT_1050,65)
            19 -> ShopItem(Items.BUNNY_EARS_1037,150)
            15 -> ShopItem(Items.EASTER_RING_7927,100)
            else -> ShopItem(0,0)
        }
    }

    fun sendCredits(player: Player){
        setInterfaceText(player, "You have ${player.details.credits} credits to spend.", CREDIT_SHOP, TEXT_CHILD)
    }

    fun attemptPurchase(player: Player, item: Int, price: Int){
        if(player.details.credits < price){
            sendDialogue(player, "You don't have enough credits for that.")
            return
        }

        if(player.inventory.add(Item(item))){
            player.details.credits -= price
        } else {
            sendDialogue(player, "You don't have enough inventory space for that.")
        }
        sendCredits(player)
    }

    internal class ShopItem(val id: Int, val price: Int)
}