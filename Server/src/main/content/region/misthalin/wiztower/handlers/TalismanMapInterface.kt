package content.region.misthalin.wiztower.handlers

import core.api.*
import core.game.interaction.InteractionListener
import org.rs09.consts.Scenery
import org.rs09.consts.Components

// http://youtu.be/T62dugdfzSQ
/** Map for talismans in Wizard Tower basement. I display everything, because let's be real, you can find this online... */
class TalismanMapInterface : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(Scenery.MAP_38421, Scenery.MAP_38422), SCENERY, "study") { player, node ->
            openInterface(player, Components.RCGUILD_MAP_780)
            // Air talisman to Death talisman
            for(i in 35..48) {
                setComponentVisibility(player, Components.RCGUILD_MAP_780, i, false)
            }
            return@on true
        }
    }
}