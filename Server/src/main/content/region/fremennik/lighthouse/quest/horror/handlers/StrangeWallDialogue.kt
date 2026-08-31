package content.region.fremennik.lighthouse.quest.horror.handlers

import content.data.Quests
import content.region.fremennik.lighthouse.quest.horror.HorrorFromTheDeep
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.interaction.QueueStrength
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.Sounds

class StrangeWallDialogue(private val items: Int) : DialogueFile() {
    private val itemVarbits: Map<Int, Int> = mapOf(
        Items.AIR_RUNE_556 to HorrorFromTheDeep.HFTD_WALL_AIR,
        Items.FIRE_RUNE_554 to HorrorFromTheDeep.HFTD_WALL_FIRE,
        Items.EARTH_RUNE_557 to HorrorFromTheDeep.HFTD_WALL_EARTH,
        Items.WATER_RUNE_555 to HorrorFromTheDeep.HFTD_WALL_WATER
    )

    // all the arrows you can insert
    private val arrowIds: Set<Int> = setOf(
        Items.BRONZE_ARROW_882,
        Items.BRONZE_ARROWP_883,
        Items.BRONZE_ARROWP_PLUS_5616,
        Items.BRONZE_ARROWP_PLUS_PLUS_5622,
        Items.IRON_ARROW_884,
        Items.IRON_ARROWP_885,
        Items.IRON_ARROWP_PLUS_5617,
        Items.IRON_ARROWP_PLUS_PLUS_5623,
        Items.STEEL_ARROW_886,
        Items.STEEL_ARROWP_887,
        Items.STEEL_ARROWP_PLUS_5618,
        Items.STEEL_ARROWP_PLUS_PLUS_5624,
        Items.MITHRIL_ARROW_888,
        Items.MITHRIL_ARROWP_889,
        Items.MITHRIL_ARROWP_PLUS_5619,
        Items.MITHRIL_ARROWP_PLUS_PLUS_5625,
        Items.ADAMANT_ARROW_890,
        Items.ADAMANT_ARROWP_891,
        Items.ADAMANT_ARROWP_PLUS_5620,
        Items.ADAMANT_ARROWP_PLUS_PLUS_5626,
        Items.RUNE_ARROW_892,
        Items.RUNE_ARROWP_893,
        Items.RUNE_ARROWP_PLUS_5621,
        Items.RUNE_ARROWP_PLUS_PLUS_5627,
        Items.DRAGON_ARROW_11212,
        Items.DRAGON_ARROWP_11227,
        Items.DRAGON_ARROWP_PLUS_11228,
        Items.DRAGON_ARROWP_PLUS_PLUS_11229
    )

    private val swordIds: Set<Int> = setOf(
        Items.BRONZE_SWORD_1277,
        Items.IRON_SWORD_1279,
        Items.STEEL_SWORD_1281,
        Items.MITHRIL_SWORD_1285,
        Items.ADAMANT_SWORD_1287,
        Items.RUNE_SWORD_1289
    )

    override fun handle(componentID: Int, buttonID: Int) {
        val player = player ?: return
        when (stage) {
            0 -> {
                sendPlayerDialogue(player, "I don't think I'll get that back if I put it in there.")
                stage++
            }

            1 -> {
                setTitle(player, 2)
                val item = getItemCategory(items)
                sendDialogueOptions(player, "Really place the $item into the door?", "Yes", "No")
                stage++
            }

            2 -> when (buttonID) {
                1 -> handleItemAction()
                2 -> end()
            }
        }
    }

    private fun handleItemAction() {
        val player = player ?: return
        val varbitName = getVarbitNameForId(items)

        // if null of quest is complete, return
        if (varbitName == null || getVarbit(player, varbitName) == 1) {
            end()
            sendMessage(player, "Nothing interesting happens.")
            return
        }
        end()
        // place item
        if (removeItem(player, Item(items, 1))) {
            sendMessage(player, "You place a ${getItemName(items).lowercase()} into the slot in the wall.")
            setVarbit(player, varbitName, 1, true)

            // if this was all the items, unlock the door
            if (getVarbit(player, HorrorFromTheDeep.HFTD_WALL_ARROW) == 1
                && getVarbit(player, HorrorFromTheDeep.HFTD_WALL_SWORD) == 1
                && getVarbit(player, HorrorFromTheDeep.HFTD_WALL_AIR) == 1
                && getVarbit(player, HorrorFromTheDeep.HFTD_WALL_FIRE) == 1
                && getVarbit(player, HorrorFromTheDeep.HFTD_WALL_WATER) == 1
                && getVarbit(player, HorrorFromTheDeep.HFTD_WALL_EARTH) == 1
            ) {
                queueScript(player, 1, QueueStrength.SOFT) {
                    sendMessage(player, "You hear the sound of something moving within the wall.")
                    playAudio(player, Sounds.STRANGEDOOR_SOUND_1627)
                    setQuestStage(player, Quests.HORROR_FROM_THE_DEEP, 5)
                    return@queueScript stopExecuting(player)
                }
            }
        }
    }

    // gets the attribute string
    private fun getVarbitNameForId(id: Int): Int? {
        // runes
        if (itemVarbits.containsKey(id)) {
            return itemVarbits[id]
        }

        // arrows and swords
        return when (id) {
            in arrowIds -> HorrorFromTheDeep.HFTD_WALL_ARROW
            in swordIds -> HorrorFromTheDeep.HFTD_WALL_SWORD
            else -> null
        }
    }

    // get type of item for dialogue
    private fun getItemCategory(id: Int): String {
        return when {
            itemVarbits.containsKey(id) -> "rune"
            id in arrowIds -> "arrow"
            id in swordIds -> "sword"
            else -> "item"
        }
    }
}
