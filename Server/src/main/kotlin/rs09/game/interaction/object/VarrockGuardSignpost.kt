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
            var pickpocketCounter = 0
                override fun interact(e: Entity?, target: Node?, option: Option?): Boolean {
                    if(option != null && option.name.toLowerCase().contains("pickpocket") && target != null && target.name.toLowerCase().contains("guard")){
                        pickpocketCounter++
                    }
                    return false
                }
        }

        ContentAPI.registerMapZone(zone, ZoneBorders(3225,3445,3198,3471))
        ContentAPI.registerMapZone(zone, ZoneBorders(3222,3375,3199,3387))
        ContentAPI.registerMapZone(zone, ZoneBorders(3180,3420,3165,3435))
        ContentAPI.registerMapZone(zone, ZoneBorders(3280,3422,3266,3435))

        on(31298, SCENERY, "read"){player, node ->
            ContentAPI.sendDialogue(player, "Guards in the Varrock Palace are on full alert due to increasing levels of pickpocketing. So far today, ${zone.pickpocketCounter} guards have had their money pickpocketed in the palace or at the city gates.")
            return@on true
        }
    }
}