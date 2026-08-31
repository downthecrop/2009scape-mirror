package content.global.handlers.item.equipment

import content.region.kandarin.quest.barbariantraining.BarbarianTraining
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.interaction.InteractionListener

/**
 * Prevents players from equipping hastas without the required barbarian training.
 * @author Bishop
 */

class HastaEquipListener : InteractionListener {
    override fun defineListeners() {
        val hastaIds = ItemDefinition.getDefinitions().values
            .filter { it.name.lowercase().contains("hasta") }
            .map { it.id }
            .toIntArray()

        onEquip(hastaIds) { player, _ ->
            if (getAttribute(player, BarbarianTraining.attributeHasta, 0) < 2) {
                sendDialogue(player, "You must begin the relevant section of Otto Godblessed's barbarian training.")
                return@onEquip false
            }
            return@onEquip true
        }
    }
}