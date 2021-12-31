package rs09.game.content.activity.blastfurnace

import api.*
import rs09.game.interaction.InterfaceListener

/**Handles adding and removing players to the temp gauge viewing list.
 * Only updates the gauge if people are actually looking at it
 * @author phil lips*/

class PhunnyGaugeTempInterfaceListener : InterfaceListener(){

    override fun defineListeners() {
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