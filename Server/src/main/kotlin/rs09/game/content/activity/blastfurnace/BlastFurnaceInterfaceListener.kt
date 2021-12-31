package rs09.game.content.activity.blastfurnace

import api.*
import core.game.node.item.Item
import org.rs09.consts.Components
import org.rs09.consts.Items
import rs09.game.interaction.InterfaceListener

/**
 * Handles the blast furnace bar stock interface.
 * @author definitely phil who didn't get help from ceikry at all haha :)
 * @version 69.0
 */
class BlastFurnaceInterfaceListener : InterfaceListener() {

    override fun defineListeners() {

        on(Components.BLAST_FURNACE_BAR_STOCK_28){ player, _, _, buttonID, _, _ ->
            val bar = BFBars.forId(buttonID) ?: return@on false
            val isAll = buttonID == bar.allButtonId
            val playerAmt = player.varpManager.get(bar.varpIndex).getVarbitValue(bar.offset)
            if(playerAmt == 0) return@on true

            var amtToWithdraw = if(isAll) playerAmt else 1

            if(freeSlots(player) < amtToWithdraw){
                amtToWithdraw = freeSlots(player)
            }

            val barItemId = when(bar){
                BFBars.BRONZE  -> Items.BRONZE_BAR_2349
                BFBars.IRON    -> Items.IRON_BAR_2351
                BFBars.STEEL   -> Items.STEEL_BAR_2353
                BFBars.MITHRIL -> Items.MITHRIL_BAR_2359
                BFBars.ADAMANT -> Items.ADAMANTITE_BAR_2361
                BFBars.RUNITE  -> Items.RUNITE_BAR_2363
                BFBars.SILVER  -> Items.SILVER_BAR_2355
                BFBars.GOLD    -> Items.GOLD_BAR_2357
            }

            player.varpManager.get(bar.varpIndex).setVarbit(bar.offset, playerAmt - amtToWithdraw).send(player)

            addItem(player, barItemId, amtToWithdraw)
            while(amtToWithdraw > 0){
                player.blastBars.remove(Item(barItemId))
                amtToWithdraw--
            }
            if(player.blastBars.isEmpty) player.varpManager.get(543).clear().send(player)
            return@on true
        }
    }

    internal enum class BFBars(val buttonId: Int, val varpIndex: Int, val offset: Int, val allButtonId: Int = buttonId + 2){
        BRONZE(43, 545, 0, 44),
        IRON(40, 545, 8, 41),
        STEEL(36, 545, 16),
        MITHRIL(33, 545, 24),
        ADAMANT(30, 546, 0),
        RUNITE(27, 546, 8),
        SILVER(24, 546, 24),
        GOLD(21, 546, 16);

        companion object {
            private val idMap = values().map { it.buttonId to it }.toMap()

            fun forId(buttonId: Int): BFBars? {
                return idMap[buttonId] ?: idMap[buttonId - 2] ?: idMap[buttonId - 1]
            }
        }
    }

}