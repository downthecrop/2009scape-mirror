package content.global.handlers.iface

import core.api.*
import core.game.dialogue.FacialExpression
import core.game.interaction.InterfaceListener
import core.game.node.item.Item
import core.tools.StringUtils
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class MysticStaffEnchantInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(INTERFACE_332) { player, _, _, buttonID, _, _ ->
            val staff = buttonMap[buttonID] ?: return@on true
            val price = if (inEquipment(player, Items.SEERS_HEADBAND_14631)) 27000 else 40000

            if (!inInventory(player, staff.basicID)) {
                sendMessage(player, "You don't have a${if (StringUtils.isPlusN(getItemName(staff.basicID))) "n" else ""} ${getItemName(staff.basicID)} to enchant.")
                return@on true
            }

            closeInterface(player)

            if (!inInventory(player, Items.COINS_995, price)) {
                sendNPCDialogue(player, NPCs.THORMAC_389, "I need ${String.format("%,d", price)} coins for materials. Come back when you have the money!", FacialExpression.NEUTRAL)
                return@on true
            }

            if (removeItem(player, Item(staff.basicID, 1)) && removeItem(player, Item(Items.COINS_995, price))) {
                sendNPCDialogue(player, NPCs.THORMAC_389, "Just a moment... hang on... hocus pocus abra-cadabra... there you go! Enjoy your enchanted staff!", FacialExpression.NEUTRAL)
                addItem(player, staff.enchantedID, 1)
            }
            return@on true
        }
    }

    enum class EnchantedStaff(val enchantedID: Int, val basicID: Int, val buttonID: Int) {
        AIR(Items.MYSTIC_AIR_STAFF_1405, Items.AIR_BATTLESTAFF_1397, 21),
        WATER(Items.MYSTIC_WATER_STAFF_1403, Items.WATER_BATTLESTAFF_1395, 22),
        EARTH(Items.MYSTIC_EARTH_STAFF_1407, Items.EARTH_BATTLESTAFF_1399, 23),
        FIRE(Items.MYSTIC_FIRE_STAFF_1401, Items.FIRE_BATTLESTAFF_1393, 24),
        LAVA(Items.MYSTIC_LAVA_STAFF_3054, Items.LAVA_BATTLESTAFF_3053, 25),
        MUD(Items.MYSTIC_MUD_STAFF_6563, Items.MUD_BATTLESTAFF_6562, 26),
        STEAM(Items.MYSTIC_STEAM_STAFF_11738, Items.STEAM_BATTLESTAFF_11736, 27),
    }

    companion object {
        private const val INTERFACE_332 = 332

        val buttonMap = HashMap<Int, EnchantedStaff>()

        init {
            for (staff in EnchantedStaff.values()) {
                buttonMap[staff.buttonID] = staff
            }
        }
    }
}