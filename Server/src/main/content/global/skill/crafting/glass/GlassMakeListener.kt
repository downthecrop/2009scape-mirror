package content.global.skill.crafting.glass

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Items
import kotlin.math.min

class GlassMakeListener : InteractionListener {

    companion object{
        private const val SODA_ASH = Items.SODA_ASH_1781
        private const val BUCKET_OF_SAND = Items.BUCKET_OF_SAND_1783
        private const val MOLTEN_GLASS = Items.MOLTEN_GLASS_1775
        private val INPUTS = intArrayOf(SODA_ASH, BUCKET_OF_SAND)
        private val FURNACES = intArrayOf(4304, 6189, 9390, 11010, 11666, 12100, 12809, 18497, 26814, 30021, 30510, 36956, 37651)
    }

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, INPUTS, *FURNACES){ player, _, _ ->

            if (!inInventory(player, SODA_ASH, 1) || !inInventory(player, BUCKET_OF_SAND, 1)) {
                sendMessage(player, "You need at least one heap of soda ash and one bucket of sand to do this.")
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(MOLTEN_GLASS)
                create { id, amount ->
                    submitIndividualPulse(player, GlassMakePulse(player, id, amount))
                }
                calculateMaxAmount { _ ->
                    min(amountInInventory(player, SODA_ASH), amountInInventory(player, BUCKET_OF_SAND))
                }
            }

            return@onUseWith true
        }
    }
}