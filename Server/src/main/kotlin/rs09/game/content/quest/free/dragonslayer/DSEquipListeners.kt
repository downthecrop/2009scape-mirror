package rs09.game.content.quest.free.dragonslayer

import api.*
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class DSEquipListeners : InteractionListener() {

    private val restrictedItems = intArrayOf(
        Items.RUNE_PLATEBODY_1127,
        Items.RUNE_PLATEBODY_G_10798,
        Items.RUNE_PLATEBODY_G_2615,
        Items.RUNE_PLATEBODY_G_13800,
        Items.RUNE_PLATEBODY_T_10800,
        Items.RUNE_PLATEBODY_T_13805,
        Items.RUNE_PLATEBODY_T_2623,
        Items.ZAMORAK_PLATEBODY_2653,
        Items.SARADOMIN_PLATEBODY_2661,
        Items.GUTHIX_PLATEBODY_2669,
        Items.GREEN_DHIDE_BODY_1135,
        Items.DHIDE_BODYG_7370,
        Items.DHIDE_BODY_G_7374,
        Items.DHIDE_BODY_T_7372,
        Items.DHIDE_BODY_T_7376
    )

    override fun defineListeners() {
        for(id in restrictedItems){
            onEquip(id){ player, _ ->
                if(!player.questRepository.isComplete("Dragon Slayer")){
                    sendMessage(player, "You must have completed Dragon Slayer to equip this.")
                    return@onEquip false
                }
                return@onEquip true
            }
        }
    }
}