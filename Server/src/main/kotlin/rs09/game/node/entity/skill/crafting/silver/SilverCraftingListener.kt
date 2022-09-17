package rs09.game.node.entity.skill.crafting.silver

import api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import org.rs09.consts.Components
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import rs09.game.interaction.InterfaceListener

import core.game.node.scenery.Scenery as CoreScenery

/**
 * Handles silver crafting interface interactions
 * as well as silver -> furnace use-with interaction.
 *
 * @author vddCore
 */
class SilverCraftingListener : InterfaceListener, InteractionListener {
    companion object {
        private const val OP_MAKE_ONE = 155
        private const val OP_MAKE_FIVE = 196
        private const val OP_MAKE_ALL = 124
        private const val OP_MAKE_X = 199

        private val FURNACES = intArrayOf(
            Scenery.FURNACE_2966, Scenery.FURNACE_3044, Scenery.FURNACE_3294, Scenery.FURNACE_4304,
            Scenery.FURNACE_6189, Scenery.FURNACE_11009, Scenery.FURNACE_11010, Scenery.FURNACE_11666,
            Scenery.FURNACE_12100, Scenery.FURNACE_12809, Scenery.FURNACE_18497, Scenery.FURNACE_18525,
            Scenery.FURNACE_18526, Scenery.FURNACE_21879, Scenery.FURNACE_22721, Scenery.FURNACE_26814,
            Scenery.FURNACE_28433, Scenery.FURNACE_28434, Scenery.FURNACE_30021, Scenery.FURNACE_30510,
            Scenery.FURNACE_36956, Scenery.FURNACE_37651
        )

        private const val ATTRIBUTE_FURNACE_ID = "crafting:silver:furnace_id"
    }

    override fun defineInterfaceListeners() {
        onOpen(Components.CRAFTING_SILVER_CASTING_438) { player, _ ->
            sendItemOnInterface(player, Components.CRAFTING_SILVER_CASTING_438, 17, Items.HOLY_SYMBOL_1718)
            sendItemOnInterface(player, Components.CRAFTING_SILVER_CASTING_438, 24, Items.UNHOLY_SYMBOL_1724)
            sendItemOnInterface(player, Components.CRAFTING_SILVER_CASTING_438, 31, Items.SILVER_SICKLE_2961)
            sendItemOnInterface(player, Components.CRAFTING_SILVER_CASTING_438, 38, Items.CONDUCTOR_4201)
            sendItemOnInterface(player, Components.CRAFTING_SILVER_CASTING_438, 45, Items.TIARA_5525)
            sendItemOnInterface(player, Components.CRAFTING_SILVER_CASTING_438, 53, Items.SILVTHRILL_ROD_7637)
            sendItemOnInterface(player, Components.CRAFTING_SILVER_CASTING_438, 60, Items.DEMONIC_SIGIL_6748)
            sendItemOnInterface(player, Components.CRAFTING_SILVER_CASTING_438, 67, Items.SILVER_BOLTS_UNF_9382)
            sendItemOnInterface(player, Components.CRAFTING_SILVER_CASTING_438, 74, Items.SILVTHRIL_CHAIN_13154)

            return@onOpen true
        }

        on(Components.CRAFTING_SILVER_CASTING_438) { player, _, opcode, buttonID, _, _ ->
            val product = SilverProduct.forButtonID(buttonID) ?: return@on true

            if (!inInventory(player, product.requiredItemId)) {
                sendMessage(
                    player,
                    "You need a ${itemDefinition(product.requiredItemId).name.lowercase()} to make this item."
                )
                return@on true
            }

            if (product == SilverProduct.SILVTHRILL_ROD || product == SilverProduct.SILVTHRIL_CHAIN) {
                sendMessage(player, "You can't do that yet.")
                return@on true
            }

            if (!hasLevelDyn(player, Skills.CRAFTING, product.minimumLevel)) {
                sendMessage(player, "You need a Crafting level of ${product.minimumLevel} to make this.")
                return@on true
            }

            when (opcode) {
                OP_MAKE_ONE -> make(player, product, 1)
                OP_MAKE_FIVE -> make(player, product, 5)
                OP_MAKE_ALL -> make(player, product, amountInInventory(player, Items.SILVER_BAR_2355))
                OP_MAKE_X -> sendInputDialogue(player, InputType.AMOUNT, "Enter the amount:") { value ->
                    make(player, product, Integer.parseInt(value.toString()))
                }
                else -> return@on true
            }

            return@on true
        }
    }

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, Items.SILVER_BAR_2355, *FURNACES) { player, _, with ->
            setAttribute(player, ATTRIBUTE_FURNACE_ID, with)
            openInterface(player, Components.CRAFTING_SILVER_CASTING_438)
            return@onUseWith true
        }
    }

    private fun make(player: Player, product: SilverProduct, amount: Int) {
        closeInterface(player)

        submitIndividualPulse(
            player,
            SilverCraftingPulse(
                player,
                product,
                getAttribute(player, ATTRIBUTE_FURNACE_ID, CoreScenery(-1, -1, 0)),
                amount
            )
        )
    }
}