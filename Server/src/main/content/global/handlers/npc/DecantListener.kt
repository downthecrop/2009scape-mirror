package content.global.handlers.npc

import content.data.consumables.Consumables
import content.global.skill.cooking.fermenting.CalquatDecant
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
        val decantables = potions + CalquatDecant.allKegIds
    }

    override fun defineListeners() {
        on(IntType.NPC,"decant"){ player, node ->
            // Decant potions
            val (potionRemove, potionAdd) = decantContainer(player.inventory)
            for (item in potionRemove)
                if (!removeItem(player, item)) { return@on false }
            for (item in potionAdd)
                addItem(player, item.id, item.amount)

            // Decant kegs
            val (kegRemove, kegAdd) = CalquatDecant.decantCalquat(player.inventory)
            for (item in kegRemove)
                if (!removeItem(player, item)) { return@on false }
            for (item in kegAdd)
                addItem(player, item.id, item.amount)

            player.dialogueInterpreter.open(DecantingDialogue(),node.asNpc())
            return@on true
        }

        onUseWith(IntType.ITEM, decantables, *decantables) { player, used, with ->
            if (used !is Item) return@onUseWith false
            if (with !is Item) return@onUseWith false

            // Check if these are kegs - handle with CalquatDecant
            if (CalquatDecant.isKeg(used.id) || CalquatDecant.isKeg(with.id)) {
                return@onUseWith CalquatDecant.combineKegs(player, used, with)
            }

            // Otherwise handle as potions
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

        // Decant keg into beer glass
        onUseWith(IntType.ITEM, Items.BEER_GLASS_1919, *CalquatDecant.allKegIds) { player, used, with ->
            if (used !is Item || with !is Item) return@onUseWith false

            // Determine which is the glass and which is the keg
            val (glass, keg) = if (used.id == Items.BEER_GLASS_1919) {
                used to with
            } else {
                with to used
            }

            return@onUseWith CalquatDecant.pourIntoGlass(player, glass, keg)
        }

        // Barbarian decanting: use a 4-dose potion and an empty vial together to make two 2-dose potions
        onUseWith(IntType.ITEM, potions, Items.VIAL_229) { player, used, with ->
            // Check if player has started barbarian herblore training
            if (getAttribute(player, "/save:barbtraining:herblore", 0) == 0) {
                return@onUseWith false
            }

            if (used !is Item) return@onUseWith false
            if (with !is Item) return@onUseWith false

            // Determine which item is the potion and which is the vial
            val (potionItem) = if (used.id == Items.VIAL_229) {
                Pair(with, used)
            } else {
                Pair(used, with)
            }

            // Check if the potion is 4 doses
            val potion = Consumables.getConsumableById(potionItem.id)?.consumable as? Potion ?: return@onUseWith false
            val dose = potion.getDose(potionItem)

            if (dose != 4) {
                return@onUseWith false
            }

            // Get the 2-dose version of this potion by index
            val twoDoseId = potion.ids[2]

            // Replace ingredients with products
            replaceSlot(player, with.slot, Item(twoDoseId))
            replaceSlot(player, used.slot, Item(twoDoseId))

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