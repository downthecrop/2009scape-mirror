package content.global.handlers.item

import content.global.handlers.iface.BoltPouchInterface.Companion.QTY_SLOT_1
import content.global.handlers.iface.BoltPouchInterface.Companion.QTY_SLOT_2
import content.global.handlers.iface.BoltPouchInterface.Companion.QTY_SLOT_3
import content.global.handlers.iface.BoltPouchInterface.Companion.QTY_SLOT_4
import content.global.handlers.iface.BoltPouchInterface.Companion.STORED_ID_SLOT_1
import content.global.handlers.iface.BoltPouchInterface.Companion.STORED_ID_SLOT_2
import content.global.handlers.iface.BoltPouchInterface.Companion.STORED_ID_SLOT_3
import content.global.handlers.iface.BoltPouchInterface.Companion.STORED_ID_SLOT_4
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.consts.*

/**
 * Listener for the bolt pouch.
 */

class BoltPouchListener: InteractionListener {

    companion object {
        val BOLT_POUCH_ALLOWED_ITEMS = intArrayOf(
            // metal bolts and their poisoned variants
            Items.BRONZE_BOLTS_877, Items.BRONZE_BOLTSP_878, Items.BRONZE_BOLTSP_PLUS_6061, Items.BRONZE_BOLTSP_PLUS_PLUS_6062,
            Items.IRON_BOLTS_9140, Items.IRON_BOLTS_P_9287, Items.IRON_BOLTSP_PLUS_9294, Items.IRON_BOLTSP_PLUS_PLUS_9301,
            Items.BLURITE_BOLTS_9139, Items.BLURITE_BOLTSP_9286, Items.BLURITE_BOLTSP_PLUS_9293, Items.BLURITE_BOLTSP_PLUS_PLUS_9300,
            Items.SILVER_BOLTS_9145, Items.SILVER_BOLTS_P_9292, Items.SILVER_BOLTSP_PLUS_9299, Items.SILVER_BOLTSP_PLUS_PLUS_9306,
            Items.BLACK_BOLTS_13083, Items.BLACK_BOLTSP_13084, Items.BLACK_BOLTSP_PLUS_13085, Items.BLACK_BOLTSP_PLUS_PLUS_13086,
            Items.STEEL_BOLTS_9141, Items.STEEL_BOLTS_P_9288, Items.STEEL_BOLTSP_PLUS_9295, Items.STEEL_BOLTSP_PLUS_PLUS_9302,
            Items.MITHRIL_BOLTS_9142, Items.MITHRIL_BOLTS_P_9289, Items.MITHRIL_BOLTSP_PLUS_9296, Items.MITHRIL_BOLTSP_PLUS_PLUS_9303,
            Items.ADAMANT_BOLTS_9143, Items.ADAMANT_BOLTS_P_9290, Items.ADAMANT_BOLTSP_PLUS_9297, Items.ADAMANT_BOLTSP_PLUS_PLUS_9304,
            Items.RUNE_BOLTS_9144, Items.RUNITE_BOLTS_P_9291, Items.RUNITE_BOLTSP_PLUS_9298, Items.RUNITE_BOLTSP_PLUS_PLUS_9305,

            // gem and enchanted gem bolts
            Items.OPAL_BOLTS_879, Items.OPAL_BOLTS_E_9236,
            Items.PEARL_BOLTS_880, Items.PEARL_BOLTS_E_9238,
            Items.JADE_BOLTS_9335, Items.JADE_BOLTS_E_9237,
            Items.TOPAZ_BOLTS_9336, Items.TOPAZ_BOLTS_E_9239,
            Items.SAPPHIRE_BOLTS_9337, Items.SAPPHIRE_BOLTS_E_9240,
            Items.EMERALD_BOLTS_9338, Items.EMERALD_BOLTS_E_9241,
            Items.RUBY_BOLTS_9339, Items.RUBY_BOLTS_E_9242,
            Items.DIAMOND_BOLTS_9340, Items.DIAMOND_BOLTS_E_9243,
            Items.DRAGON_BOLTS_9341, Items.DRAGON_BOLTS_E_9244,
            Items.ONYX_BOLTS_9342,Items.ONYX_BOLTS_E_9245,

            // special bolts
            Items.KEBBIT_BOLTS_10158, Items.LONG_KEBBIT_BOLTS_10159,
            Items.BARBED_BOLTS_881,
            Items.BROAD_TIPPED_BOLTS_13280,
            Items.BONE_BOLTS_8882,
        )
    }

    // helper function to drop the bolts
    private fun dropBolts(player: Player, boltSlotId: String, boltSlotQty: Int) {
        val boltId = getAttribute(player, boltSlotId, -1)
        val boltQty = getVarbit(player, boltSlotQty)

        if (boltQty == 0) {
            return
        }

        // drops bolts, changes the stored ID to 0, and sets the qty varbit to 0
        produceGroundItem(player, boltId, boltQty, player.location)
        setVarbit(player, boltSlotQty, 0, true)
        removeAttribute(player, boltSlotId)
    }

    override fun defineListeners() {
        // open pouch interface
        on(Items.BOLT_POUCH_9433, IntType.ITEM, "open") { player, _ ->
            openInterface(player, Components.XBOWS_POUCH_433)
            return@on true
        }

        // destroy pouch - drops bolts
        on(Items.BOLT_POUCH_9433, IntType.ITEM, "destroy") { player, node ->
            val item = node as Item
            player.dialogueInterpreter.sendDestroyItem(item.id, "You can get another pouch from Hirko in Keldagrim.")
            addDialogueAction(player) { _, button ->
                if (button == 3) {
                    if (removeItem(player, item)) {
                        playAudio(player, Sounds.DESTROY_OBJECT_2381)

                        dropBolts(player, STORED_ID_SLOT_1, QTY_SLOT_1)
                        dropBolts(player, STORED_ID_SLOT_2, QTY_SLOT_2)
                        dropBolts(player, STORED_ID_SLOT_3, QTY_SLOT_3)
                        dropBolts(player, STORED_ID_SLOT_4, QTY_SLOT_4)
                    }
                }
            }
            return@on true
        }

        // bolts used on pouch: add to pouch
        onUseWith(IntType.ITEM, Items.BOLT_POUCH_9433, *BOLT_POUCH_ALLOWED_ITEMS) { player, _, with ->
            val id = with.id
            val qtyUsed = amountInInventory(player, id)

            // get either the first empty slot by querying the varbits, or a slot that already has the used bolts in it and at least one free space
            var targetQtySlot = -1
            var targetIdSlot = ""

            // slot 1
            if (getVarbit(player, QTY_SLOT_1) == 0 || (id == getAttribute(player, STORED_ID_SLOT_1, 0) && getVarbit(player, QTY_SLOT_1) < 255)) {
                targetQtySlot = QTY_SLOT_1
                targetIdSlot = STORED_ID_SLOT_1

            // slot 2
            } else if (getVarbit(player, QTY_SLOT_2) == 0 || (id == getAttribute(player, STORED_ID_SLOT_2, 0) && getVarbit(player, QTY_SLOT_2) < 255)) {
                targetQtySlot = QTY_SLOT_2
                targetIdSlot = STORED_ID_SLOT_2

            // slot 3
            } else if (getVarbit(player, QTY_SLOT_3) == 0 || (id == getAttribute(player, STORED_ID_SLOT_3, 0) && getVarbit(player, QTY_SLOT_3) < 255)) {
                targetQtySlot = QTY_SLOT_3
                targetIdSlot = STORED_ID_SLOT_3

            // slot 4
            } else if (getVarbit(player, QTY_SLOT_4) == 0 || (id == getAttribute(player, STORED_ID_SLOT_4, 0) && getVarbit(player, QTY_SLOT_4) < 255)) {
                targetQtySlot = QTY_SLOT_4
                targetIdSlot = STORED_ID_SLOT_4
            }

            // based on target values set earlier, add the bolts
            if (targetQtySlot != -1) {
                val currentQty = getVarbit(player, targetQtySlot)
                val spaceLeft = 255 - currentQty
                val toAdd = minOf(qtyUsed, spaceLeft)

                if (removeItem(player, Item(id, toAdd))) {
                    setVarbit(player, targetQtySlot, toAdd + currentQty, true)
                    setAttribute(player, targetIdSlot, id)
                    sendMessage(player, "You store some bolts in your pouch.")
                }

            // all slots are full
            } else {
                sendMessage(player, "You don't have enough space in your bolt pouch to do that.")
            }
            return@onUseWith true
        }
    }
}