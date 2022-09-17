package rs09.game.node.entity.player.graves

import api.*
import core.game.node.item.Item
import org.rs09.consts.Components
import org.rs09.consts.Items
import rs09.game.interaction.InterfaceListener

class GravePurchaseInterface : InterfaceListener {
    val BUTTON_CONFIRM = 34
    val AVAILABLE_GRAVES_BITFIELD = 0xFFF //Enable all graves, authentically the quest-locked ones should be excluded from the bitfield, but y'know.
    val AVAILABLE_GRAVES_VARBIT = 4191
    val CURRENT_GRAVE_VARBIT = 4190

    override fun defineInterfaceListeners() {
        onOpen (Components.GRAVESTONE_SHOP_652) {player, _ ->
            val userType = GraveController.getGraveType(player).ordinal
            setVarbit(player, AVAILABLE_GRAVES_VARBIT, AVAILABLE_GRAVES_BITFIELD)
            setVarbit(player, CURRENT_GRAVE_VARBIT, userType)

            val settings = IfaceSettingsBuilder()
                .enableAllOptions()
                .build()
            player.packetDispatch.sendIfaceSettings(settings, 34, Components.GRAVESTONE_SHOP_652, 0, 13)
            return@onOpen true
        }

        on (Components.GRAVESTONE_SHOP_652, BUTTON_CONFIRM) { player, _, _, _, slot, _ ->
            val selectedType = GraveType.values()[slot]
            val userType = GraveController.getGraveType(player)
            val activeGrave = GraveController.activeGraves[player.details.uid]

            if (activeGrave != null){
                sendDialogue(player, "You cannot change graves while you have a grave active.")
                return@on true
            }

            if (selectedType == userType) {
                sendDialogue(player, "You already have that gravestone!")
                return@on true
            }

            val cost = selectedType.cost
            val requirement = selectedType.requiredQuest

            if (requirement.isNotEmpty() && !isQuestComplete(player, requirement)) {
                sendDialogue(player, "That gravestone requires completion of $requirement.")
                return@on true
            }

            if (selectedType != GraveType.MEM_PLAQUE && amountInInventory(player, Items.COINS_995) < cost) {
                sendDialogue(player, "You do not have enough coins to afford that gravestone.")
                return@on true
            }

            if (selectedType == GraveType.MEM_PLAQUE || removeItem(player, Item(995, cost))) {
                GraveController.updateGraveType(player, selectedType)
                sendDialogue(player, "Your grave has been updated.")
            }

            closeInterface(player)
            return@on true
        }
    }
}