package content.minigame.blastfurnace

import core.api.*
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
            val barCounts = HashMap<Int, Int>()
            if (playerBars.isEmpty()) {
                return@onOpen true
            } else {
                playerBars.forEach { barItem ->
                    when (barItem.id) {
                        Items.BRONZE_BAR_2349 -> barCounts[barItem.id] = bronzeBit++ 
                        Items.IRON_BAR_2351 -> barCounts[barItem.id] = ironBit++ 
                        Items.STEEL_BAR_2353 -> barCounts[barItem.id] = steelBit++ 
                        Items.MITHRIL_BAR_2359 -> barCounts[barItem.id] = mithrilBit++ 
                        Items.ADAMANTITE_BAR_2361 -> barCounts[barItem.id] = adamantiteBit++ 
                        Items.RUNITE_BAR_2363 -> barCounts[barItem.id] = runiteBit++ 
                        Items.SILVER_BAR_2355 -> barCounts[barItem.id] = silverBit++ 
                        Items.GOLD_BAR_2357 -> barCounts[barItem.id] = goldBit++ 
                    }
                }
                for ((id, amount) in barCounts) {
                    when (id) {
                        Items.BRONZE_BAR_2349 -> setVarbit(player, 941, amount)
                        Items.IRON_BAR_2351 -> setVarbit(player, 942, amount)
                        Items.STEEL_BAR_2353 -> setVarbit(player, 943, amount)
                        Items.MITHRIL_BAR_2359 -> setVarbit(player, 944, amount)
                        Items.ADAMANTITE_BAR_2361 -> setVarbit(player, 945, amount)
                        Items.RUNITE_BAR_2363 -> setVarbit(player, 946, amount)
                        Items.SILVER_BAR_2355 -> setVarbit(player, 948, amount)
                        Items.GOLD_BAR_2357 -> setVarbit(player, 947, amount)
                    }
                }
                return@onOpen true
            }
        }

        on(Components.BLAST_FURNACE_BAR_STOCK_28){ player, _, _, buttonID, _, _ ->
            val bar = BFBars.forId(buttonID) ?: return@on false
            val isAll = buttonID == bar.allButtonId
            val playerAmt = getVarbit(player, bar.varbit)
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

            setVarbit(player, bar.varbit, playerAmt - amtToWithdraw)

            while(amtToWithdraw > 0){
                if(addItem(player, barItemId, 1) && player.blastBars.remove(Item(barItemId))) {
                    amtToWithdraw--
                } else break
            }
            if(player.blastBars.isEmpty) 
                setVarp(player, 543, 0)
            return@on true
        }
    }

    internal enum class BFBars(val buttonId: Int, val varbit: Int, val allButtonId: Int = buttonId + 2){
        BRONZE(43, 941, 44),
        IRON(40, 942, 41),
        STEEL(36, 943),
        MITHRIL(33, 944),
        ADAMANT(30, 945),
        RUNITE(27, 946),
        SILVER(24, 948),
        GOLD(21, 947);

        companion object {
            private val idMap = values().associateBy { it.buttonId }

            fun forId(buttonId: Int): BFBars? {
                return idMap[buttonId] ?: idMap[buttonId - 2] ?: idMap[buttonId - 1]
            }
        }
    }

}
