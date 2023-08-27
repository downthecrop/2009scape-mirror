package content.minigame.blastfurnace

import content.global.skill.smithing.smelting.Bar
import core.api.*
import core.game.interaction.InterfaceListener
import core.game.system.task.Pulse
import org.rs09.consts.Components

class BlastFurnaceInterfaceListener : InterfaceListener {
    override fun defineInterfaceListeners() {
        onOpen(Components.BLAST_FURNACE_BAR_STOCK_28) { player, component ->
            val state = BlastFurnace.getPlayerState(player)
            state.setBarClaimVarbits()
            state.checkBars()
            return@onOpen true
        }

        onOpen(Components.BLAST_FURNACE_TEMP_GAUGE_30) {player, _ ->
            submitIndividualPulse(player, object : Pulse() {
                override fun pulse(): Boolean {
                    val anim = BlastFurnace.state.furnaceTemp + 2452
                    animateInterface(player, 30, 4, anim)
                    return false
                }
            })
            return@onOpen true
        }
        onClose(Components.BLAST_FURNACE_TEMP_GAUGE_30) {player, _ -> player.pulseManager.clear(); return@onClose true }

        on(Components.BLAST_FURNACE_BAR_STOCK_28){ player, _, _, buttonID, _, _ ->
            val (isAll, bar) = getBarForButton(buttonID)
            val state = BlastFurnace.getPlayerState(player)
            state.claimBars(bar, if (isAll) state.container.getBarAmount(bar) else 1)
            return@on true
        }
    }

    private fun getBarForButton (id: Int) : Pair<Boolean, Bar> {
        return when (id) {
            43,44 -> Pair(id == 44, Bar.BRONZE)
            40,41 -> Pair(id == 41, Bar.IRON)
            36,38 -> Pair(id == 38, Bar.STEEL)
            33,35 -> Pair(id == 35, Bar.MITHRIL)
            30,32 -> Pair(id == 32, Bar.ADAMANT)
            27,29 -> Pair(id == 29, Bar.RUNITE)
            24,26 -> Pair(id == 26, Bar.SILVER)
            21,23 -> Pair(id == 23, Bar.GOLD)
            else -> Pair(false, Bar.BRONZE)
        }
    }
}
