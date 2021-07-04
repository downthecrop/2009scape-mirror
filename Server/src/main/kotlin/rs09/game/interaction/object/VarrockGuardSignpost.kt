package rs09.game.interaction.`object`

import api.ContentAPI
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import rs09.game.interaction.InteractionListener

class VarrockGuardSignpost : InteractionListener() {
    override fun defineListeners() {
            val zone = object : MapZone("Varrock Guards", true){
            var deathCounter = 0
                override fun interact(e: Entity?, target: Node?, option: Option?): Boolean {
                    if(option != null && option.name.toLowerCase().contains("pickpocket") && e != null && e.name.toLowerCase().contains("guard")){
                        deathCounter++
                    }
                    return false
                }
        }

        ContentAPI.registerMapZone(zone, ZoneBorders(3225,3445,3198,3471))

        on(31298, SCENERY, "read"){player, node ->
            ContentAPI.sendDialogue(player, "Guards in the Varrock Palace are on full alert due to increasing levels of pickpocketing. So far today, ${zone.deathCounter} guards have had their money pickpocketed in the palace or at the city gates.")
            return@on true
        }
    }
}