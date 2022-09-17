package rs09.game.node.entity.skill.cooking

import api.*
import api.events.ResourceProducedEvent
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs09.consts.Items
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType

class DoughMakingListener : InteractionListener {
    companion object {
        private val FULL_WATER_CONTAINERS_TO_EMPTY_CONTAINERS = hashMapOf(
            Items.BUCKET_OF_WATER_1929 to Items.BUCKET_1925,
            Items.BOWL_OF_WATER_1921 to Items.BOWL_1923,
            Items.JUG_OF_WATER_1937 to Items.JUG_1935
        )
    }

    override fun defineListeners() {
        onUseWith(
            IntType.ITEM,
            FULL_WATER_CONTAINERS_TO_EMPTY_CONTAINERS.keys.toIntArray(),
            Items.POT_OF_FLOUR_1933
        ) { player, waterContainer, flourContainer ->
            openDialogue(player, DoughMakeDialogue(waterContainer.asItem(), flourContainer.asItem()))
            return@onUseWith true
        }
    }

    private class DoughMakeDialogue(val waterContainer: Item, val flourContainer: Item) : DialogueFile() {
        companion object {
            private const val STAGE_PRESENT_OPTIONS = 0
            private const val STAGE_PROCESS_OPTION = 1

            private enum class DoughProduct(val itemId: Int, val itemName: String, val requiredCookingLevel: Int) {
                BREAD_DOUGH(Items.BREAD_DOUGH_2307, getItemName(Items.BREAD_DOUGH_2307), 1),
                PASTRY_DOUGH(Items.PASTRY_DOUGH_1953, getItemName(Items.PASTRY_DOUGH_1953), 1),
                PIZZA_DOUGH(Items.PIZZA_BASE_2283, getItemName(Items.PIZZA_BASE_2283), 35),
                PITTA_DOUGH(Items.PITTA_DOUGH_1863, getItemName(Items.PITTA_DOUGH_1863), 58),
            }
        }

        override fun handle(componentID: Int, buttonID: Int) {
            when (stage) {
                STAGE_PRESENT_OPTIONS -> {
                    player!!.dialogueInterpreter.sendOptions(
                        "What do you wish to make?",
                        *(DoughProduct.values().map { "${it.itemName}." }.toTypedArray())
                    ).also { stage++ }
                }
                STAGE_PROCESS_OPTION -> runTask(player!!, 1) {
                    end()
                    val selectedDoughProduct = DoughProduct.values()[buttonID - 1]
                    if (hasLevelDyn(player!!, Skills.COOKING, selectedDoughProduct.requiredCookingLevel)) {
                        if (freeSlots(player!!) < 1) {
                            sendMessage(
                                player!!,
                                "Not enough space in your inventory."
                            )
                            return@runTask
                        }

                        if (removeItem(player!!, waterContainer) && removeItem(player!!, flourContainer)) {
                            addItem(player!!, selectedDoughProduct.itemId)
                            player!!.dispatch(ResourceProducedEvent(selectedDoughProduct.itemId, 1, player!!))

                            val emptyWaterContainerId = FULL_WATER_CONTAINERS_TO_EMPTY_CONTAINERS[waterContainer.id]!!
                            addItem(player!!, emptyWaterContainerId)

                            addItem(player!!, Items.EMPTY_POT_1931)

                            sendMessage(
                                player!!,
                                "You mix the flower and the water to make some ${selectedDoughProduct.itemName.toLowerCase()}."
                            )
                        }
                    } else {
                        sendDialogue(
                            player!!,
                            "You need a Cooking level of at least ${selectedDoughProduct.requiredCookingLevel} in order to make ${selectedDoughProduct.itemName.toLowerCase()}."
                        )
                    }
                }
            }
        }
    }
}