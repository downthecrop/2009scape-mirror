package content.global.skill.cooking

import content.region.misc.tutisland.handlers.TutorialStage
import core.api.*
import core.game.event.ResourceProducedEvent
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs09.consts.Items
import core.game.dialogue.DialogueFile
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

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
            if (getAttribute(player, "/save:tutorial:complete", false)) {
                openDialogue(player, DoughMakeDialogue(waterContainer.asItem(), flourContainer.asItem()))
                return@onUseWith true
            }
            // Continue the tutorial
            replaceSlot(player, waterContainer.asItem().slot, Item(Items.BUCKET_1925), waterContainer.asItem())
            replaceSlot(player, flourContainer.asItem().slot, Item(Items.EMPTY_POT_1931), flourContainer.asItem())
            addItemOrDrop(player, Items.BREAD_DOUGH_2307)
            setAttribute(player, "tutorial:stage", 20)
            TutorialStage.load(player, 20)
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
                                "You mix the flour and the water to make some ${selectedDoughProduct.itemName.toLowerCase()}."
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