package content.global.skill.cooking.recipe

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs09.consts.Items

class MixScript(
    private val player: Player,
    private val used: Item,
    private val with: Item,
    private val product: Int,
    private val sets: Int,
    private val level: Int,
    private val experience: Double,
    private val message: String,
    private val failMessage: String
) {

    private val delay = 2

    fun invoke() {
        queueScript(player, delay) { stage -> Int
            if (getDynLevel(player, Skills.COOKING) < level) {
                sendDialogue(player, failMessage)
                return@queueScript stopExecuting(player)
            }

            if (inInventory(player, used.id, used.amount) && inInventory(player, with.id, with.amount)) {
                if(removeItem(player, used) && removeItem(player, with)) {
                    sendMessage(player, message)
                    rewardXP(player, Skills.COOKING, experience)
                    addItem(player, product)
                    containerIngredient(player, used.id)
                    containerIngredient(player, with.id)
                }
            } else {
                return@queueScript stopExecuting(player)
            }

            if (stage >= sets - 1) {
                return@queueScript stopExecuting(player)
            }

            return@queueScript delayScript(player, delay)
        }
    }
}

fun containerIngredient(player: Player, ingredient: Int): Boolean{
    return when (ingredient) {
        Items.BUCKET_OF_WATER_1929, Items.COMPOST_6032 -> addItem(player, Items.BUCKET_1925)
        Items.FRIED_MUSHROOMS_7082 -> addItem(player, Items.BOWL_1923)
        else -> false
    }
}