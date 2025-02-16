package content.region.kandarin.ardougne.westardougne

import core.api.EquipmentSlot
import core.api.allInEquipment
import core.api.getItemFromEquipment
import core.api.inEquipment
import core.game.node.entity.player.Player
import org.rs09.consts.Items

object MournerUtilities {

    const val NO_GEAR = 0
    const val JUST_MASK = 1
    const val JUST_GEAR = 2
    const val EXTRA_GEAR = 3

    /**
     * Check if the player is wearing just mourner gear
     * @return 0 incomplete gear 1 mask only 2 complete gear only 3 complete gear with extras
     */
    fun wearingMournerGear(player: Player): Int {
        // Check that the play has all of these items
        if (inEquipment(player, Items.GAS_MASK_1506)) {
            // We have a mask
            if (!allInEquipment(
                    player, Items.MOURNER_TOP_6065, Items.MOURNER_TROUSERS_6067,
                    Items.MOURNER_BOOTS_6069, Items.MOURNER_GLOVES_6068, Items.MOURNER_CLOAK_6070
                )
            ) {
                // We have only a mask
                return JUST_MASK
            }
            else {
                // Check if we have other gear

                // These use up slots 0, 1, 4, 7, 9, 10. Check the others are empty
                val mournerSlots = arrayOf(
                    EquipmentSlot.HEAD, EquipmentSlot.CAPE, EquipmentSlot.CHEST,
                    EquipmentSlot.LEGS, EquipmentSlot.HANDS, EquipmentSlot.FEET
                )
                for (slot in EquipmentSlot.values()) {
                    // Skip the slots that we know have the gear equipped
                    if (mournerSlots.contains(slot)) continue
                    if (getItemFromEquipment(player, slot) != null) {
                        return EXTRA_GEAR
                    }
                }
                return JUST_GEAR
            }

        }
        else {
            // We don't even have a mask
            return NO_GEAR
        }
    }
}