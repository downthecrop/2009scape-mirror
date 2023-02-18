package content.minigame.blastfurnace

import core.api.addItem
import core.api.freeSlots
import core.game.interaction.InterfaceListener
import core.game.node.item.Item
import org.rs09.consts.Components
import org.rs09.consts.Items


/**
 * Handles the blast furnace bar stock interface.
 * @author phil, bushtail
 * @version 2.0
 */
class BlastFurnaceInterfaceListener : InterfaceListener {

    override fun defineInterfaceListeners() {

        onOpen(28,) { player, component ->
            var playerBars = player.blastBars.toArray().filterNotNull()
            var bronzeBit = 1
            var ironBit = 1
            var steelBit = 1
            var mithrilBit = 1
            var adamantiteBit = 1
            var runiteBit = 1
            var silverBit = 1
            var goldBit = 1
            var barTotal = 0
            if (playerBars.isEmpty()) {
                return@onOpen true
            } else {
                playerBars.forEach { barItem ->
                    when (barItem.id) {
                        Items.BRONZE_BAR_2349 -> player.varpManager.get(545).setVarbit(0, bronzeBit++).send(player)
                        Items.IRON_BAR_2351 -> player.varpManager.get(545).setVarbit(8, ironBit++).send(player)
                        Items.STEEL_BAR_2353 -> player.varpManager.get(545).setVarbit(16, steelBit++).send(player)
                        Items.MITHRIL_BAR_2359 -> player.varpManager.get(545).setVarbit(24, mithrilBit++).send(player)
                        Items.ADAMANTITE_BAR_2361 -> player.varpManager.get(546).setVarbit(0, adamantiteBit++).send(player)
                        Items.RUNITE_BAR_2363 -> player.varpManager.get(546).setVarbit(8, runiteBit++).send(player)
                        Items.SILVER_BAR_2355 -> player.varpManager.get(546).setVarbit(24, silverBit++).send(player)
                        Items.GOLD_BAR_2357 -> player.varpManager.get(546).setVarbit(16, goldBit++).send(player)
                    }
                }
                return@onOpen true
            }
        }

        on(Components.BLAST_FURNACE_BAR_STOCK_28){ player, _, _, buttonID, _, _ ->
            val bar = BFBars.forId(buttonID) ?: return@on false
            val isAll = buttonID == bar.allButtonId
            val playerAmt = player.varpManager.get(bar.varpIndex).getVarbitValue(bar.offset) ?: 0
            if(playerAmt == 0) return@on true

            var amtToWithdraw = if(isAll) playerAmt else 1

            if(freeSlots(player) < amtToWithdraw){
                amtToWithdraw = freeSlots(player)
            }

            val barItemId = when(bar){
                BFBars.BRONZE -> Items.BRONZE_BAR_2349
                BFBars.IRON -> Items.IRON_BAR_2351
                BFBars.STEEL -> Items.STEEL_BAR_2353
                BFBars.MITHRIL -> Items.MITHRIL_BAR_2359
                BFBars.ADAMANT -> Items.ADAMANTITE_BAR_2361
                BFBars.RUNITE -> Items.RUNITE_BAR_2363
                BFBars.SILVER -> Items.SILVER_BAR_2355
                BFBars.GOLD -> Items.GOLD_BAR_2357
            }

            player.varpManager.get(bar.varpIndex).setVarbit(bar.offset, playerAmt - amtToWithdraw).send(player)


            while(amtToWithdraw > 0){
                if(addItem(player, barItemId, 1) && player.blastBars.remove(Item(barItemId))) {
                    amtToWithdraw--
                } else break
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
            private val idMap = values().associateBy { it.buttonId }

            fun forId(buttonId: Int): BFBars? {
                return idMap[buttonId] ?: idMap[buttonId - 2] ?: idMap[buttonId - 1]
            }
        }
    }

}