package content.region.kandarin.feldip.quest.zogreflesheaters

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs09.consts.Items
import kotlin.math.min

public enum class BrutalArrows(val nailItem: Int, val level: Int, val product: Int, val exp: Double) {
    BRONZE_BRUTAL(Items.BRONZE_NAILS_4819, 7, Items.BRONZE_BRUTAL_4773, 8.4),
    IRON_BRUTAL(Items.IRON_NAILS_4820, 18, Items.IRON_BRUTAL_4778, 15.6),
    STEEL_BRUTAL(Items.STEEL_NAILS_1539, 33, Items.STEEL_BRUTAL_4783, 30.6),
    BLACK_BRUTAL(Items.BLACK_NAILS_4821, 38, Items.BLACK_BRUTAL_4788, 39.0),
    MITHRIL_BRUTAL(Items.MITHRIL_NAILS_4822, 49, Items.MITHRIL_BRUTAL_4793, 45.0),
    ADAMANT_BRUTAL(Items.ADAMANTITE_NAILS_4823, 62, Items.ADAMANT_BRUTAL_4798, 61.2),
    RUNE_BRUTAL(Items.RUNE_NAILS_4824, 77, Items.RUNE_BRUTAL_4803, 75.0)
    ;

    companion object {
        @JvmField
        val nailItemMap = values().associateBy { it.nailItem }
        val nailItemArray = nailItemMap.values.map { it.nailItem }.toIntArray()
    }
}
/**
 * This handles potions and fletching related to zogre flesh eaters.
 *
 * Relicym's Balm is unique to zogre flesh eaters.
 * Maybe move this to herblore when it can handle quest requirements better.
 */
class ZogrePotionAndFletchingListeners  : InteractionListener {

    override fun defineListeners() {
        // ROGUES_PURSE_POTIONUNF_4840 is already in UnfinishedPotion.java
        onUseWith(IntType.ITEM, Items.ROGUES_PURSE_POTIONUNF_4840, Items.CLEAN_SNAKE_WEED_1526) { player, used, with ->
            if (!hasLevelStat(player, Skills.HERBLORE, 8)) {
                sendMessage(player, "You need a herblore level of 8 to make this mix.")
                return@onUseWith true
            }
            if (getQuestStage(player, ZogreFleshEaters.questName) < 7) {
                sendMessage(player, "You need to have partially completed Zogre Flesh Eaters to make this mix.")
                return@onUseWith true
            }

            if(removeItem(player, used) && removeItem(player, with)) {
                sendMessage(player, "You add the snake weed to the rogues purse solution and make Relicyms Balm.")
                addItem(player, Items.RELICYMS_BALM3_4844)
                rewardXP(player, Skills.HERBLORE, 40.0)
            }
            return@onUseWith true
        }

        // FletchingListeners.kt
        // ACHEY_TREE_LOGS_2862 -> UNSTRUNG_COMP_BOW_4825 -> COMP_OGRE_BOW_4827
        // Requirement to wield comp ogre bow.
        onEquip(Items.COMP_OGRE_BOW_4827) { player, _ ->
            if (getQuestStage(player, ZogreFleshEaters.questName) >= 8){
                return@onEquip true
            }
            sendMessage(player, "You need to complete part of Zogre Flesh Eaters to equip this.")
            return@onEquip false
        }


        onUseWith(IntType.ITEM, BrutalArrows.nailItemArray, Items.FLIGHTED_OGRE_ARROW_2865) { player, used, with ->
            fun getMaxAmount(_unused: Int = 0): Int {
                val tips = amountInInventory(player, used.id)
                val shafts = amountInInventory(player, with.id)
                return min(tips, shafts)
            }

            fun process() {
                val amountThisIter = min(6, getMaxAmount())
                if (removeItem(player, Item(used.id, amountThisIter)) && removeItem(player, Item(with.id, amountThisIter))) {
                    addItem(player, BrutalArrows.nailItemMap[used.id]!!.product, amountThisIter)
                    sendMessage(player, "You make $amountThisIter brutal arrows.")
                    rewardXP(player, Skills.FLETCHING, BrutalArrows.nailItemMap[used.id]!!.exp)
                }
            }

            if (getQuestStage(player, ZogreFleshEaters.questName) < 7) {
                sendMessage(player, "You need to complete part of Zogre Flesh Eaters to make these.")
                return@onUseWith true
            }

            if (getStatLevel(player, Skills.FLETCHING) < BrutalArrows.nailItemMap[used.id]!!.level) {
                sendMessage(player, "You need a Fletching level of " + BrutalArrows.nailItemMap[used.id]!!.level + " to make these.")
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                create { id, amount ->
                    runTask(
                            player,
                            delay = 2,
                            repeatTimes = min(amount, getMaxAmount() / 6 + 1),
                            task = ::process
                    )
                }
                calculateMaxAmount(::getMaxAmount)
                withItems(Item(BrutalArrows.nailItemMap[used.id]!!.product, 5))
            }
            return@onUseWith true
        }

    }
}