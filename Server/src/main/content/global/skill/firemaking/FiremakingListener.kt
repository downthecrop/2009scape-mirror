package content.global.skill.firemaking

import core.api.anyInInventory
import core.api.inInventory
import core.api.submitIndividualPulse
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.GroundItem
import org.rs09.consts.Items

/**
 * Listener for lighting logs.
 * @author Bishop
 */

class FiremakingListener : InteractionListener {

    private companion object {
        private val logs = intArrayOf(
            Items.LOGS_1511, Items.OAK_LOGS_1521, Items.MAGIC_LOGS_1513, Items.YEW_LOGS_1515, Items.MAPLE_LOGS_1517,
            Items.WILLOW_LOGS_1519, Items.ACHEY_TREE_LOGS_2862, Items.PYRE_LOGS_3438, Items.OAK_PYRE_LOGS_3440,
            Items.WILLOW_PYRE_LOGS_3442, Items.MAPLE_PYRE_LOGS_3444, Items.YEW_PYRE_LOGS_3446,
            Items.MAGIC_PYRE_LOGS_3448, Items.TEAK_PYRE_LOGS_6211, Items.MAHOGANY_PYRE_LOG_6213,
            Items.MAHOGANY_LOGS_6332, Items.TEAK_LOGS_6333, Items.RED_LOGS_7404, Items.GREEN_LOGS_7405,
            Items.BLUE_LOGS_7406, Items.SCRAPEY_TREE_LOGS_8934, Items.DREAM_LOG_9067, Items.WHITE_LOGS_10328,
            Items.PURPLE_LOGS_10329, Items.ARCTIC_PYRE_LOGS_10808, Items.ARCTIC_PINE_LOGS_10810, Items.SPLIT_LOG_10812,
            Items.WINDSWEPT_LOGS_11035, Items.EUCALYPTUS_LOGS_12581, Items.EUCALYPTUS_PYRE_LOGS_12583,
            Items.JOGRE_BONES_3125
        )
        private val bows = intArrayOf(
            Items.SHORTBOW_841, Items.LONGBOW_839, Items.OAK_SHORTBOW_843, Items.OAK_LONGBOW_845,
            Items.WILLOW_SHORTBOW_849, Items.WILLOW_LONGBOW_847, Items.MAPLE_SHORTBOW_853, Items.MAPLE_LONGBOW_851,
            Items.YEW_SHORTBOW_857, Items.YEW_LONGBOW_855, Items.MAGIC_SHORTBOW_861, Items.MAGIC_LONGBOW_859
        )
    }

    override fun defineListeners() {

        // Handle tinderbox item interaction (inventory)
        onUseWith(IntType.ITEM, Items.TINDERBOX_590, *logs) { player, _, with ->
            submitIndividualPulse(player, FireMakingPulse(player, with.asItem(), null))
            return@onUseWith true
        }

        // Handle tinderbox item interaction (ground item)
        onUseWith(IntType.GROUNDITEM, Items.TINDERBOX_590, *logs) { player, _, with ->
            submitIndividualPulse(player, FireMakingPulse(player, with.asItem(), with as GroundItem))
            return@onUseWith true
        }

        // Handle barbarian item interaction (inventory)
        onUseWith(IntType.ITEM, bows, *logs) { player, used, with ->
            submitIndividualPulse(player, BarbFirePulse(player, with.asItem(), null, used.asItem()))
            return@onUseWith true
        }

        // Handle barbarian item interaction (ground item)
        onUseWith(IntType.GROUNDITEM, bows, *logs) { player, used, with ->
            submitIndividualPulse(player, BarbFirePulse(player, with.asItem(), with as GroundItem, used.asItem()))
            return@onUseWith true
        }

        // Handle "light" option on logs (ground item)
        on(logs, IntType.GROUNDITEM, "light") { player, node ->
            if (node !is GroundItem) return@on false
            when {
                (inInventory(player, Items.TINDERBOX_590)) -> {
                    submitIndividualPulse(player, FireMakingPulse(player, node.asItem(), node))
                    return@on true
                }
                (anyInInventory(player, *bows)) -> {
                    submitIndividualPulse(player, BarbFirePulse(player, node.asItem(), node))
                    return@on true
                }
                else -> {
                    submitIndividualPulse(player, FireMakingPulse(player, node.asItem(), node))
                    return@on true
                }
            }
        }
    }
}
