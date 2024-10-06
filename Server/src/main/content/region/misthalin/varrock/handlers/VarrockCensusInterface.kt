package content.region.misthalin.varrock.handlers

import core.api.getVarbit
import core.api.setVarbit
import core.game.interaction.InterfaceListener

class VarrockCensusInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(INTERFACE_ID) { player, _, _, buttonID, _, _ ->
            when (buttonID) {
                2 -> setVarbit(player, VARBIT_ID, getVarbit(player, VARBIT_ID).plus(1))
                3 -> setVarbit(player, VARBIT_ID, getVarbit(player, VARBIT_ID).minus(1))
                else -> return@on true
            }
            return@on true
        }

        onClose(INTERFACE_ID) { player, _ ->
            setVarbit(player, VARBIT_ID, 0)
            return@onClose true
        }
    }
    companion object {
        const val INTERFACE_ID = 794
        const val VARBIT_ID = 5390
    }
}