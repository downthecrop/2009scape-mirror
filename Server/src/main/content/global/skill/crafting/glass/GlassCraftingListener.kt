package content.global.skill.crafting.glass

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import org.rs09.consts.Components
import org.rs09.consts.Items

class GlassCraftingListener : InteractionListener, InterfaceListener {

    companion object {
        private const val OP_MAKE_ONE = 155
        private const val OP_MAKE_FIVE = 196
        private const val OP_MAKE_ALL = 124
        private const val OP_MAKE_X = 199

        private const val GLASS_BLOWING_PIPE = Items.GLASSBLOWING_PIPE_1785
        private const val MOLTEN_GLASS = Items.MOLTEN_GLASS_1775
        private const val GLASS_BLOWING_INTERFACE = Components.CRAFTING_GLASS_542
    }

    override fun defineListeners() {
        onUseWith(IntType.ITEM, GLASS_BLOWING_PIPE, MOLTEN_GLASS) { player, _, _ ->
            openInterface(player, GLASS_BLOWING_INTERFACE)
            return@onUseWith true
        }
    }

    override fun defineInterfaceListeners() {
        on(GLASS_BLOWING_INTERFACE) { player, _, opcode, buttonID, _, _ ->
            val product = GlassProduct.forButtonID(buttonID) ?: return@on true

            if (!inInventory(player, GLASS_BLOWING_PIPE)) {
                sendMessage(player, "You need a glassblowing pipe to do this.")
                return@on true
            }

            if (!inInventory(player, MOLTEN_GLASS)) {
                sendMessage(player, "You need molten glass to do this.")
                return@on true
            }

            if (!hasLevelDyn(player, Skills.CRAFTING, product.minimumLevel)) {
                sendMessage(player, "You need a Crafting level of ${product.minimumLevel} to make this.")
                return@on true
            }

            when (opcode) {
                OP_MAKE_ONE -> make(player, product, 1)
                OP_MAKE_FIVE -> make(player, product, 5)
                OP_MAKE_ALL -> make(player, product, amountInInventory(player, MOLTEN_GLASS))
                OP_MAKE_X -> sendInputDialogue(player, InputType.AMOUNT, "Enter the amount:") { value ->
                    make(player, product, Integer.parseInt(value.toString()))
                }
                else -> return@on true
            }

            return@on true
        }
    }

    private fun make(player: Player, product: GlassProduct, amount: Int) {
        closeInterface(player)
        submitIndividualPulse(player, GlassCraftingPulse(player, product, amount))
    }
}