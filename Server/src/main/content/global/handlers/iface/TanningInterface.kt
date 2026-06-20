package content.global.handlers.iface

import content.global.skill.crafting.TanningProduct
import core.api.amountInInventory
import core.api.sendInputDialogue
import core.game.interaction.InterfaceListener

class TanningInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(324) { player, _, opcode, buttonID, _, _ ->
            val product = TanningProduct.forId(buttonID)

            when (opcode) {
                155 -> TanningProduct.tan(player, 1, product)
                196 -> TanningProduct.tan(player, 5, product)
                124 -> TanningProduct.tan(player, 10, product)
                199 -> sendInputDialogue(player, true, "Enter the amount:") { value ->
                    TanningProduct.tan(player, value as Int, product)
                }
                234 -> TanningProduct.tan(player, amountInInventory(player, product.item), product)
            }
            return@on true
        }
    }
}
