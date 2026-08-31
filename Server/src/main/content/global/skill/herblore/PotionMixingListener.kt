package content.global.skill.herblore

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item

/**
 * The unfinished/finished potion interaction listener.
 * Converted from Vexia's plugins.
 */
class PotionMixingListener : InteractionListener {

    override fun defineListeners() {

        // for unfinished potions
        val baseIds = UnfinishedPotion.values().map { it.base.id }.distinct().toIntArray()
        val unfIngredientIds = UnfinishedPotion.values().map { it.ingredient.id }.distinct().toIntArray()

        // for finished potions
        val unfinishedIds = FinishedPotion.values().map { it.unfinished.id }.distinct().toIntArray()
        val finIngredientIds = FinishedPotion.values().map { it.ingredient.id }.distinct().toIntArray()

        // create an unfinished potion
        onUseWith(IntType.ITEM, baseIds, *unfIngredientIds) { player, used, with ->
            if (used !is Item || with !is Item) return@onUseWith false

            val unf = UnfinishedPotion.forItem(used, with) ?: return@onUseWith false
            val potion = GenericPotion.transform(unf)

            mixPotion(player, potion)
            return@onUseWith true
        }

        // create a finished potion
        onUseWith(IntType.ITEM, unfinishedIds, *finIngredientIds) { player, used, with ->
            if (used !is Item || with !is Item) return@onUseWith false

            val finished = FinishedPotion.getPotion(used, with) ?: return@onUseWith false
            val potion = GenericPotion.transform(finished)

            // Check for a secondary requirement
            if (!finished.canMake(player)) {
                // This is authentic text I sourced from Shades of Mort'ton, trying to mix Serum 207 before the quest teaches you the recipe. Per a later wiki article on Guthix Rest, the same text is used.
                sendMessage(player, "You're not sure what effect this might have.")
                return@onUseWith true
            }

            mixPotion(player, potion)
            return@onUseWith true
        }
    }

    /**
     * Helper function to mix the potions
     */
    private fun mixPotion(player: Player, potion: GenericPotion) {
        if (amountInInventory(player, potion.base.id) == 1
            || amountInInventory(player, potion.ingredient.id) == 1
        ) {
            // If you only have 1, make the potion immediately without opening dialogue
            player.pulseManager.run(HerblorePulse(player, potion.base, 1, potion))
        } else {
            // If you have multiple, open the creation dialogue
            sendSkillDialogue(player) {
                withItems(potion.product)
                calculateMaxAmount { amountInInventory(player, potion.base.id) }
                create { _, amount ->
                    player.pulseManager.run(HerblorePulse(player, potion.base, amount, potion))
                }
            }
        }
    }
}