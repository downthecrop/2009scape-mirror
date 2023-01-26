package content.minigame.blastfurnace

import core.game.interaction.InterfaceListener

/**Handles adding and removing players to the temp gauge viewing list.
 * Only updates the gauge if people are actually looking at it
 * @author phil lips*/

class PhunnyGaugeTempInterfaceListener : InterfaceListener {

    override fun defineInterfaceListeners() {
        onOpen(30) {player, component ->
            BlastFurnace.gaugeViewList.add(player)
            return@onOpen true
        }

        onClose(30) {player, component ->
            BlastFurnace.gaugeViewList.remove(player)
            return@onClose true
        }
    }


}