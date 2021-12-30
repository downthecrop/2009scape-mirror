package rs09.game.interaction.region.falador

import core.game.node.item.GroundItem
import core.game.world.map.RegionManager
import org.rs09.consts.Items
import rs09.game.content.global.action.PickupHandler
import rs09.game.interaction.InteractionListener

class WineOfZamorakInteraction : InteractionListener() {

    override fun defineListeners() {
        on(Items.WINE_OF_ZAMORAK_245,GROUNDITEM,"take"){player, wine ->
            if(player.location.regionId != 11574){
                PickupHandler.take(player, wine as GroundItem)
                return@on true
            }
            val npcs = RegionManager.getLocalNpcs(player)
            for (n in npcs) {
                if (n.id == 188) {
                    n.sendChat("Hands off zamorak's wine!")
                    n.properties.combatPulse.attack(player)
                }
            }
            return@on true
        }
    }
}
