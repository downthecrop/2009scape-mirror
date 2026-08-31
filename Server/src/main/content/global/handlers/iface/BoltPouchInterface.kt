package content.global.handlers.iface

import core.api.*
import core.game.global.action.EquipHandler.Companion.unequip
import org.rs09.consts.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.Log

/**
 * Bolt Pouch Interface
 * Uses 396.cs2
 */

class BoltPouchInterface: InterfaceListener {

    /**
     * iface components, attributes, and varbits used to define this all
     *
     * The varbits 2469 and 2470 are packaged in varp 834 (size 16).
     * The varbits 2471 and 2472 are packaged in varp 835 (size 16).
     */
    companion object {
        val REMOVE_SLOT_1 = 4 // interface button
        val WIELD_SLOT_1 = 3  // interface button
        val ICON_SLOT_1 = 2   // interface icon component
        val TEXT_SLOT_1 = 25  // interface text component
        val STORED_ID_SLOT_1 = "/save:boltpouch-slot1" // attribute, this is the stored item ID
        val QTY_SLOT_1 = 2469 // varbit

        val REMOVE_SLOT_2 = 8
        val WIELD_SLOT_2 = 7
        val ICON_SLOT_2 = 6
        val TEXT_SLOT_2 = 26
        val STORED_ID_SLOT_2 = "/save:boltpouch-slot2"
        val QTY_SLOT_2 = 2470

        val REMOVE_SLOT_3 = 12
        val WIELD_SLOT_3 = 11
        val ICON_SLOT_3 = 10
        val TEXT_SLOT_3 = 27
        val STORED_ID_SLOT_3 = "/save:boltpouch-slot3"
        val QTY_SLOT_3 = 2471

        val REMOVE_SLOT_4 = 16
        val WIELD_SLOT_4 = 15
        val ICON_SLOT_4 = 14
        val TEXT_SLOT_4 = 28
        val STORED_ID_SLOT_4 = "/save:boltpouch-slot4"
        val QTY_SLOT_4 = 2472

        val UNWIELD_EQUIP_SLOT = 19
        val ICON_EQUIP_SLOT = 18
        val TEXT_EQUIP_SLOT = 29
        val QTY_EQUIP_SLOT = 24
    }

    /**
     * Interface interactions
     */
    override fun defineInterfaceListeners() {
        onOpen(Components.XBOWS_POUCH_433) { player, _ ->
            updateBolts(player)
            return@onOpen true
        }

        on(Components.XBOWS_POUCH_433) { player, _, _, buttonID, _, _ ->
            when(buttonID) {
                REMOVE_SLOT_1 -> removeBolts(player, STORED_ID_SLOT_1, QTY_SLOT_1)
                REMOVE_SLOT_2 -> removeBolts(player, STORED_ID_SLOT_2, QTY_SLOT_2)
                REMOVE_SLOT_3 -> removeBolts(player, STORED_ID_SLOT_3, QTY_SLOT_3)
                REMOVE_SLOT_4 -> removeBolts(player, STORED_ID_SLOT_4, QTY_SLOT_4)
                WIELD_SLOT_1  -> wieldBolts(player,  STORED_ID_SLOT_1, QTY_SLOT_1)
                WIELD_SLOT_2  -> wieldBolts(player,  STORED_ID_SLOT_2, QTY_SLOT_2)
                WIELD_SLOT_3  -> wieldBolts(player,  STORED_ID_SLOT_3, QTY_SLOT_3)
                WIELD_SLOT_4  -> wieldBolts(player,  STORED_ID_SLOT_4, QTY_SLOT_4)
                UNWIELD_EQUIP_SLOT -> unwieldBolts(player)
            }
            return@on true
        }

    }

    /**
     * Helper Functions
     */

    // removes the bolts from the pouch by changing the stored ID to 0 and setting the qty varbit to 0
    fun removeBolts(player: Player, boltSlotId: String, boltSlotQty: Int) {
        val boltId = getAttribute(player, boltSlotId, -1)
        val boltQty = getVarbit(player, boltSlotQty)

        // error check in case arios fucks up my varbits
        if (boltId == -1 && boltQty != 0) {
            // log error
            log(this::class.java, Log.ERR, "Invalid bolt ID '$boltSlotId' for ${player.name}.")
            sendMessage(player, "ERROR: Invalid bolt ID. Please report this.")

            // reset slot values
            setVarbit(player, boltSlotQty, 0, true)
            removeAttribute(player, boltSlotId)

            return
        }

        if (boltQty == 0) {
            sendMessage(player, "There's nothing to remove from that slot.")
            return
        }

        val slot = when(boltSlotQty) {
            QTY_SLOT_1 -> "first"
            QTY_SLOT_2 -> "second"
            QTY_SLOT_3 -> "third"
            else       -> "fourth"
        }

        if (addItem(player, boltId, boltQty)) {
            setVarbit(player, boltSlotQty, 0, true)
            removeAttribute(player, boltSlotId)
            sendMessage(player, "You remove all the bolts from the $slot slot of your bolt pouch.")
        } else {
            sendMessage(player, "You don't have enough space in your inventory to do that.")
        }

        updateBolts(player)
    }

    // wields the bolts
    fun wieldBolts(player: Player, boltSlotId: String, boltSlotQty: Int) {
        // any equipped ammo
        val ammo = getItemFromEquipment(player, EquipmentSlot.AMMO)
        val ammoId = ammo?.id ?: -1

        // bolts to equip
        val boltId = getAttribute(player, boltSlotId, -1)
        val boltQty = getVarbit(player, boltSlotQty)
        val bolt = Item(boltId, boltQty)

        // error check in case arios fucks up my varbits
        if (boltId == -1 && boltQty != 0) {
            // log error
            log(this::class.java, Log.ERR, "Invalid bolt ID '$boltSlotId' for ${player.name}.")
            sendMessage(player, "ERROR: Invalid bolt ID. Please report this.")

            // reset slot values
            setVarbit(player, boltSlotQty, 0, true)
            removeAttribute(player, boltSlotId)

            return
        }

        // pouch slot is empty
        if (boltQty == 0) {
            sendMessage(player, "There's nothing in that slot to wield.")
            return
        }

        // check requirements
        if (!bolt.definition.hasRequirement(player, true, true)) {
            return
        }

        // same bolt type already equipped, add to stack and return
        if (ammoId == boltId) {
            val newQty = (ammo!!.amount + boltQty).coerceAtMost(Int.MAX_VALUE)
            replaceSlot(player, EquipmentSlot.AMMO.ordinal, Item(boltId, newQty), container=Container.EQUIPMENT)
            setVarbit(player, boltSlotQty, 0, true)
            removeAttribute(player, boltSlotId)
            sendMessage(player, "You add the bolts to your equipped stack.")
            updateBolts(player)
            return
        }

        // different bolt type, unequip existing first
        if (ammoId != -1) {
            if (freeSlots(player) < 1) {
                sendMessage(player, "You don't have enough space in your inventory to do that.")
                return
            }
            unequip(player, EquipmentSlot.AMMO.ordinal, ammoId)
        }

        // equip new bolts
        if (player.equipment.add(Item(boltId, boltQty), true, EquipmentSlot.AMMO.ordinal)) {
            setVarbit(player, boltSlotQty, 0, true)
            removeAttribute(player, boltSlotId)
            sendMessage(player, "You wield some bolts from your bolt pouch.")
        }

        updateBolts(player)
    }

    // unwields the bolts
    fun unwieldBolts(player: Player) {
        val ammo = getItemFromEquipment(player, EquipmentSlot.AMMO)?.id ?: -1

        if (ammo == -1) {
            sendMessage(player, "You're not wielding anything.")
            return
        }

        unequip(player, EquipmentSlot.AMMO.ordinal, ammo)
        updateBolts(player)
    }

    // updates the bolt interface. Uses the stored ID (attribute) or equipped ID to get the icon and text needed for each slot. qty is updated automatically with 396.cs2 and the varbits
    fun updateBolts(player: Player) {

        // the equipment slot
        val ammo = getItemFromEquipment(player, EquipmentSlot.AMMO)
        val ammoQty = amountInEquipment(player, ammo?.id ?: 0)

        sendItemOnInterface(player, Components.XBOWS_POUCH_433, ICON_EQUIP_SLOT, ammo?.id ?: -1, 90)
        setInterfaceText(player, ammo?.name ?: "Nothing", Components.XBOWS_POUCH_433, TEXT_EQUIP_SLOT)
        if (ammoQty == 0) {
            // RED from 396.cs2: Color(255, 0, 51)
            setInterfaceText(player, "<col=FF0033>0</col>", Components.XBOWS_POUCH_433, QTY_EQUIP_SLOT)
        } else {
            // GREEN from 396.cs2: Color(0, 192, 0)
            setInterfaceText(player, "<col=00C000>$ammoQty</col>", Components.XBOWS_POUCH_433, QTY_EQUIP_SLOT)
        }

        // the pouch slots

        // slot 1
        val slot1 = Item(getAttribute(player, STORED_ID_SLOT_1, 0))
        if (slot1.id == 0) {
            setInterfaceText(player, "Nothing", Components.XBOWS_POUCH_433, TEXT_SLOT_1)
            sendItemOnInterface(player, Components.XBOWS_POUCH_433, ICON_SLOT_1, -1)
        } else {
            setInterfaceText(player, slot1.name, Components.XBOWS_POUCH_433, TEXT_SLOT_1)
            sendItemOnInterface(player, Components.XBOWS_POUCH_433, ICON_SLOT_1, slot1.id, 90)
        }

        // slot 2
        val slot2 = Item(getAttribute(player, STORED_ID_SLOT_2, 0)).definition
        if (slot2.id == 0) {
            setInterfaceText(player, "Nothing", Components.XBOWS_POUCH_433, TEXT_SLOT_2)
            sendItemOnInterface(player, Components.XBOWS_POUCH_433, ICON_SLOT_2, -1)
        } else {
            setInterfaceText(player, slot2.name, Components.XBOWS_POUCH_433, TEXT_SLOT_2)
            sendItemOnInterface(player, Components.XBOWS_POUCH_433, ICON_SLOT_2, slot2.id, 90)
        }

        // slot 3
        val slot3 = Item(getAttribute(player, STORED_ID_SLOT_3, 0)).definition
        if (slot3.id == 0) {
            setInterfaceText(player, "Nothing", Components.XBOWS_POUCH_433, TEXT_SLOT_3)
            sendItemOnInterface(player, Components.XBOWS_POUCH_433, ICON_SLOT_3, -1)
        } else {
            setInterfaceText(player, slot3.name, Components.XBOWS_POUCH_433, TEXT_SLOT_3)
            sendItemOnInterface(player, Components.XBOWS_POUCH_433, ICON_SLOT_3, slot3.id, 90)
        }

        // slot 4
        val slot4 = Item(getAttribute(player, STORED_ID_SLOT_4, 0)).definition
        if (slot4.id == 0) {
            setInterfaceText(player, "Nothing", Components.XBOWS_POUCH_433, TEXT_SLOT_4)
            sendItemOnInterface(player, Components.XBOWS_POUCH_433, ICON_SLOT_4, -1)
        } else {
            setInterfaceText(player, slot4.name, Components.XBOWS_POUCH_433, TEXT_SLOT_4)
            sendItemOnInterface(player, Components.XBOWS_POUCH_433, ICON_SLOT_4, slot4.id, 90)
        }
    }
}