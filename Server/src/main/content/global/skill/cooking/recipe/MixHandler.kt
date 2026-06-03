package content.global.skill.cooking.recipe

import core.api.*
import core.game.dialogue.SkillDialogueHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import kotlin.math.min

fun standardMix(player: Player, used: Item, with: Item, product: Int, level: Int, experience: Double, message: String, failMessage: String): Boolean {
    // Avoids sending dialogue if not enough ingredients to create (relevant only for curry leaves)
    if (amountInInventory(player, used.id) < used.amount || amountInInventory(player, with.id) < with.amount) {
        // Not sure about the authentic message
        sendMessage(player, "You don't have enough ingredients to make that.")
        return true
    }

    // Avoids sending dialogue if only one can be created
    if (amountInInventory(player, used.id) == used.amount || amountInInventory(player, with.id) == with.amount) {
        if (getDynLevel(player, Skills.COOKING) < level) {
            sendDialogue(player, failMessage)
            return true
        }

        if (removeItem(player, used) && removeItem(player, with)) {
            sendMessage(player, message)
            rewardXP(player, Skills.COOKING, experience)
            addItem(player, product)
            containerIngredient(player, used.id)
            containerIngredient(player, with.id)
        }
        return true
    }

    mixHandler(
        player,
        used,
        with,
        product,
        level,
        experience,
        message,
        failMessage
    ).open()
    return true
}

private fun mixHandler(player: Player, used: Item, with: Item, product: Int, level: Int, experience: Double, message: String, failMessage: String): SkillDialogueHandler {
    val handler: SkillDialogueHandler =
        object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, product.asItem()) {
            override fun create(amount: Int, index: Int) {
                if (!playerMeetsInitialRequirements()) return
                MixScript(player, used, with, product, amount, level, experience, message, failMessage).invoke()
            }

            private fun playerMeetsInitialRequirements(): Boolean {
                if (getDynLevel(player, Skills.COOKING) < level) {
                    sendDialogue(player, failMessage)
                    return false
                }
                return true
            }

            override fun getAll(index: Int): Int {
                return min(
                    amountInInventory(player, used.id)/used.amount,
                    amountInInventory(player, with.id)/with.amount
                )
            }
        }
    return handler
}
