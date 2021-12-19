package rs09.game.interaction.item.withobject

import api.*
import api.EquipmentSlot
import core.cache.def.impl.ItemDefinition
import rs09.game.interaction.InteractionListener
import rs09.game.system.SystemLogger

class HatStand : InteractionListener() {

    val hats = ItemDefinition.getDefinitions().values.filter { it.getConfiguration("equipment_slot",0) == EquipmentSlot.HAT.ordinal }.map { it.id }.toIntArray()
    val hat_stand = 374

    override fun defineListeners() {
        onUseWith(SCENERY, hats, hat_stand){player, used, with ->
            sendDialogue(player, "It'd probably fall off if I tried to do that.")
            return@onUseWith true
        }
    }
}