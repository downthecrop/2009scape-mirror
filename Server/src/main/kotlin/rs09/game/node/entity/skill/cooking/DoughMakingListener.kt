package rs09.game.node.entity.skill.cooking

import api.*
import api.events.ResourceProducedEvent
import core.game.node.item.Item
import org.rs09.consts.Items
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.InteractionListener

class DoughMakingListener : InteractionListener() {
    val sourceReturnMap = hashMapOf(
        Items.BUCKET_OF_WATER_1929 to Items.BUCKET_1925,
        Items.BOWL_OF_WATER_1921 to Items.BOWL_1923,
        Items.JUG_OF_WATER_1937 to Items.JUG_1935
    )

    override fun defineListeners() {
        onUseWith(ITEM, sourceReturnMap.keys.toIntArray(), Items.POT_OF_FLOUR_1933){player, used, with ->
            openDialogue(player, DoughMakeDialogue(used.asItem(), with.asItem()))
            return@onUseWith true
        }
    }

    private class DoughMakeDialogue(val used: Item, val with: Item) : DialogueFile()
    {
        val products = intArrayOf(Items.BREAD_DOUGH_2307, Items.PASTRY_DOUGH_1953, Items.PIZZA_BASE_2283, Items.PITTA_DOUGH_1863)
        override fun handle(componentID: Int, buttonID: Int) {
            when(stage)
            {
                0 -> player!!.dialogueInterpreter.sendOptions("What do you wish to make?", "Bread dough.", "Pastry dough.", "Pizza dough.", "Pitta dough.").also { stage++ }
                1 -> runTask(player!!, 1){
                    end()
                    if(removeItem(player!!, used) && removeItem(player!!, with)){
                        addItem(player!!, products[buttonID - 1])
                        player!!.dispatch(ResourceProducedEvent(products[buttonID - 1], 1, player!!))
                        sendMessage(player!!, "You mix the flower and the water to make some ${getItemName(products[buttonID - 1]).toLowerCase()}.")
                    }
                }
            }
        }
    }
}