package content.global.handlers.npc

import content.data.consumables.Consumables
import core.api.*
import core.game.consumable.Potion
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs09.consts.Items

class DecantListener : InteractionListener {

    companion object {
        val potions = Consumables.potions.toIntArray()
    }

    override fun defineListeners() {
        on(IntType.NPC,"decant"){ player, node ->
            val (toRemove, toAdd) = decantContainer(player.inventory)
            for (item in toRemove)
                removeItem(player, item)
            for (item in toAdd)
                addItem(player, item.id, item.amount)
            player.dialogueInterpreter.open(DecantingDialogue(),node.asNpc())
            return@on true
        }

        onUseWith(IntType.ITEM, potions, *potions) { player, used, with ->
            if (used !is Item) return@onUseWith false
            if (with !is Item) return@onUseWith false

            // Verify these are both the same potion types
            val potionUsed = Consumables.getConsumableById(used.id)?.consumable as? Potion ?: return@onUseWith false
            val potionWith = Consumables.getConsumableById(with.id)?.consumable as? Potion ?: return@onUseWith false

            if (potionUsed != potionWith) {
                return@onUseWith false
            }

            // Dosage math
            val usedDose = potionUsed.getDose(used)
            val withDose = potionWith.getDose(with)

            // Shouldn't be able to combine a 4 dose potion
            if (usedDose == 4 || withDose == 4) {
                return@onUseWith false
            }

            val totalDosage = usedDose + withDose
            val fullDoses = totalDosage / 4
            val leftoverDose = totalDosage % 4

            // Replace the targeted potion item with a (4) dose potion
            if (fullDoses != 0) {
                replaceSlot(player, with.slot, Item(potionWith.ids.first()), with, Container.INVENTORY)
            }

            // Replace the targeted potion item with the updated dosage amount
            if (leftoverDose != 0 && fullDoses == 0) {
                replaceSlot(player, with.slot, Item(potionUsed.ids[potionUsed.ids.size - totalDosage]), with, Container.INVENTORY)

            // Replace the used with potion item with the updated dosage amount
            } else if (leftoverDose != 0) {
                replaceSlot(player, used.slot, Item(potionUsed.ids[potionUsed.ids.size - leftoverDose]), used, Container.INVENTORY)
            }

            // Replace the used with potion item with an empty vial
            if (leftoverDose == 0 || fullDoses == 0) {
                replaceSlot(player, used.slot, Item(Items.VIAL_229), used, Container.INVENTORY)
            }

            // Send message/Play sound
            val amountString = when {
                totalDosage >= 4 -> "four"
                totalDosage == 3 -> "three"
                else -> "two"
            }

            sendMessage(player, "You have combined the liquid into $amountString doses.")
            playAudio(player, 2401)

            return@onUseWith true
        }
    }

    internal class DecantingDialogue : DialogueFile(){
        override fun handle(componentID: Int, buttonID: Int) {
            when(stage++){
                0 -> npc("There you go!")
                1 -> player("Thanks!").also { stage = END_DIALOGUE }
            }
        }
    }
}