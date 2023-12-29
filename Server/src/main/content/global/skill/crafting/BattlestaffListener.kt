package content.global.skill.crafting

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import org.rs09.consts.Items
import kotlin.math.min

class BattlestaffListener : InteractionListener {

    private val battlestaff = Items.BATTLESTAFF_1391
    val orbs = BattlestaffProduct.values().map { it.requiredOrbItemId }.toIntArray()

    override fun defineListeners() {
        onUseWith(IntType.ITEM, orbs, battlestaff) { player, used, with ->
            val product = BattlestaffProduct.productMap[used.id] ?: return@onUseWith true

            if (!hasLevelDyn(player, Skills.CRAFTING, product.minimumLevel)) {
                sendMessage(player, "You need a Crafting level of ${product.minimumLevel} to make this.")
                return@onUseWith true
            }

            // Avoids sending dialogue if only one can be created
            if (amountInInventory(player, used.id) == 1 || amountInInventory(player, with.id) == 1) {

                if (removeItem(player, product.requiredOrbItemId) && removeItem(player, Items.BATTLESTAFF_1391)) {
                    addItem(player, product.producedItemId, product.amountProduced)
                    rewardXP(player, Skills.CRAFTING, product.experience)
                }

                if (product.producedItemId == Items.AIR_BATTLESTAFF_1397) {
                    player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 2, 6)
                }

                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(product.producedItemId)
                create { _, amount ->

                    runTask(player, 2, amount) {
                        if (amount < 1) return@runTask

                        if (removeItem(player, product.requiredOrbItemId) && removeItem(player, Items.BATTLESTAFF_1391)) {
                            addItem(player, product.producedItemId, product.amountProduced)
                            rewardXP(player, Skills.CRAFTING, product.experience)
                        }

                        if (product.producedItemId == Items.AIR_BATTLESTAFF_1397) {
                            player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 2, 6)
                        } else return@runTask
                    }
                }

                calculateMaxAmount { _ ->
                    min(amountInInventory(player, with.id), amountInInventory(player, used.id))
                }
            }

            return@onUseWith true
        }
    }

    enum class BattlestaffProduct(
            val requiredOrbItemId: Int,
            val producedItemId: Int,
            val amountProduced: Int,
            val minimumLevel: Int,
            val experience: Double,
    ) {
        WATER_BATTLESTAFF(Items.WATER_ORB_571, Items.WATER_BATTLESTAFF_1395, 1, 54, 100.0),
        EARTH_BATTLESTAFF(Items.EARTH_ORB_575, Items.EARTH_BATTLESTAFF_1399, 1, 58, 112.5),
        FIRE_BATTLESTAFF(Items.FIRE_ORB_569, Items.FIRE_BATTLESTAFF_1393, 1, 62, 125.0),
        AIR_BATTLESTAFF(Items.AIR_ORB_573, Items.AIR_BATTLESTAFF_1397, 1, 66, 137.5);

        companion object {
            val productMap = HashMap<Int, BattlestaffProduct>()

            init {
                for (product in BattlestaffProduct.values()) {
                    productMap[product.requiredOrbItemId] = product
                }
            }
        }
    }
}