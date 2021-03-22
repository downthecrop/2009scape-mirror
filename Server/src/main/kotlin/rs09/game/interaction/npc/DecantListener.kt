package rs09.game.interaction.npc

import core.game.content.consumable.Consumables
import core.game.content.consumable.Potion
import core.game.node.entity.player.Player
import core.game.node.item.Item
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.InteractionListener
import rs09.tools.END_DIALOGUE

class DecantListener : InteractionListener() {

    override fun defineListeners() {
        on(NPC,"decant"){player, node ->
            decant(player)
            player.dialogueInterpreter.open(DecantingDialogue(),node.asNpc())
            return@on true
        }
    }


    fun decant(p: Player) {
        val potcounts = HashMap<Potion, Int>()
        val results: List<Item>
        for (i in 0..27) {
            val pot = (Consumables.getConsumableById(p.inventory.getId(i)) ?: continue) as Potion
            if (pot != null) {
                val dosage = p.inventory[i].name.replace("[^\\d.]".toRegex(), "").toInt()
                if (potcounts[pot] != null) {
                    potcounts.replace(pot, potcounts[pot]!! + dosage)
                } else {
                    potcounts.putIfAbsent(pot, dosage)
                }
                p.inventory.remove(Item(p.inventory.getId(i)))
            }
        }
        potcounts.keys.forEach{ pot: Potion ->
            val full_count = potcounts[pot]!! / pot.ids.size
            val partial_dose_amt = potcounts[pot]!! % pot.ids.size
            p.inventory.add(Item(pot.ids[0], full_count))
            if (partial_dose_amt > 0) p.inventory
                .add(Item(pot.ids[pot.ids.size - partial_dose_amt]))
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