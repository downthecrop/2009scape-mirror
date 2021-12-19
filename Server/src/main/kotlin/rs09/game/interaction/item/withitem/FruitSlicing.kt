package rs09.game.interaction.item.withitem

import api.*
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.Items.LEMON_CHUNKS_2104
import org.rs09.consts.Items.LIME_2120
import org.rs09.consts.Items.LIME_CHUNKS_2122
import org.rs09.consts.Items.LIME_SLICES_2124
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.InteractionListener

class FruitSlicing : InteractionListener() {

    override fun defineListeners() {
        val fruits = Fruit.values().map { it.base.id } .toIntArray()

        onUseWith(ITEM, fruits, Items.KNIFE_946) {player, used, with ->
            val fruit = Fruit.forBase(used.id) ?: return@onUseWith false

            if ((fruit == Fruit.BANANA || fruit == Fruit.LEMON)) {
                if (removeItem(player, used.asItem(), Container.INVENTORY)) {
                    lock(player, 2)
                    animate(player, 1192)
                    addItem(player, fruit.sliced.id, fruit.sliced.amount)
                    sendMessage(player, "You deftly chop the " + fruit.name.toLowerCase() + " into slices.")
                }
            } else {
                //TODO: Below Dialogue is still located in FruitCuttingDialogue.java. Convert to DialogueFile and make this not so garbage.
                openDialogue(player,31237434, fruit)
            }
            return@onUseWith true
        }
    }

    enum class Fruit(val base: Item, val diced: Item?, val sliced: Item) {
        PINEAPPLE(Item(2114), Item(2116), Item(2118, 4)),
        BANANA(Item(1963), null, Item(3162)),
        LEMON(Item(2102), Item(LEMON_CHUNKS_2104), Item(2106)),
        LIME(Item(LIME_2120), Item(LIME_CHUNKS_2122), Item(LIME_SLICES_2124)),
        ORANGE(Item(2108), Item(2110), Item(2112));

        companion object {
            val fruitMap = values().map { it.base.id to it }.toMap()
            /**
             * Method used to get the fruit for the base item.
             * @param item the item.
             * @return the fruit.
             */
            fun forBase(item: Int): Fruit? {
                return fruitMap[item]
            }
        }
    }
}