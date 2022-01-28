package rs09.game.interaction.inter

import api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.crafting.SilverProduct
import core.game.system.task.Pulse
import org.rs09.consts.Components
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.InterfaceListener

class SilverInterface : InterfaceListener() {

    val IFACE = Components.CRAFTING_SILVER_CASTING_438
    val ANIM = getAnimation(899)

    override fun defineListeners() {
        onOpen(IFACE){player, _ ->
            sendItemOnInterface(player, IFACE, 17, 1718)
            sendItemOnInterface(player, IFACE, 24, 1724)
            sendItemOnInterface(player, IFACE, 31, 2961)
            sendItemOnInterface(player, IFACE, 38, 4201)
            sendItemOnInterface(player, IFACE, 45, 5525)
            sendItemOnInterface(player, IFACE, 53, 7637)
            sendItemOnInterface(player, IFACE, 60, 6748)
            sendItemOnInterface(player, IFACE, 67, 9382)
            sendItemOnInterface(player, IFACE, 74, 13154)
            return@onOpen true
        }

        on(IFACE){player, _, opcode, buttonID, _, _ ->
            val product = SilverProduct.forId(buttonID) ?: return@on true

            if(!inInventory(player, product.needed)){
                sendMessage(player, "You need a ${itemDefinition(product.needed).name.toLowerCase()} to make this item.")
                return@on true
            }

            if(product == SilverProduct.SILVTHRIL_ROD || product == SilverProduct.SILVTHRIL_CHAIN){
                sendMessage(player, "You can't do that yet.")
                return@on true
            }

            if(!hasLevelDyn(player, Skills.CRAFTING, product.level)){
                sendMessage(player, "You need a Crafting level of ${product.level} to make this.")
                return@on true
            }

            val amt = when(opcode){
                155 -> 1
                196 -> 5
                124 -> amountInInventory(player, Items.SILVER_BAR_2355)
                199 -> -1 //Make X
                else -> return@on true
            }

            if(amt == -1) sendInputDialogue(player, true, "Enter the amount:"){value ->
                make(player,product,value as Int)
            } else make(player, product, amt)
            return@on true
        }
    }

    fun make(player: Player, product: SilverProduct, amount: Int){
        var amt = amount
        closeInterface(player)
        submitIndividualPulse(player, object : Pulse(){
            override fun pulse(): Boolean {
                if(amt < 1) return true
                if(!inInventory(player, product.needed) || !inInventory(player, Items.SILVER_BAR_2355)) return true;
                animate(player, ANIM)

                if(removeItem(player, Items.SILVER_BAR_2355, api.Container.INVENTORY)){
                    addItem(player, product.product, if(product == SilverProduct.SILVER_BOLTS) 10 else 1)
                    rewardXP(player, Skills.CRAFTING, product.exp)

                    if(product == SilverProduct.UNBLESSED && player.location.withinDistance(location(3226,3254,0))){
                        player.achievementDiaryManager.finishTask(player, DiaryType.LUMBRIDGE, 2, 8)
                    }
                    delay = 3
                } else return true

                amt--
                return false
            }
        })
    }
}

class SilverBarUseWith : InteractionListener() {
    val FURNACES = intArrayOf(2966, 3044, 3294, 4304, 6189, 11009, 11010, 11666, 12100, 12809, 18497, 18525, 18526, 21879, 22721, 26814, 28433, 28434, 30021, 30510, 36956, 37651)
    override fun defineListeners() {
        onUseWith(SCENERY, Items.SILVER_BAR_2355, *FURNACES){ player, _, _ ->
            openInterface(player, Components.CRAFTING_SILVER_CASTING_438)
            return@onUseWith true
        }
    }
}