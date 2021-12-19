package rs09.game.interaction.`object`

import api.*
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import rs09.GlobalStats
import rs09.game.interaction.InteractionListener
import rs09.game.system.SystemLogger

class VarrockGuardSignpost : InteractionListener() {
    override fun defineListeners() {
            val zone = object : MapZone("Varrock Guards", true){
                override fun interact(e: Entity?, target: Node?, option: Option?): Boolean {
                    if(option != null && option.name.toLowerCase().contains("pickpocket") && target != null && target.name.toLowerCase().contains("guard")){
                        GlobalStats.incrementGuardPickpockets()
                    }
                    return false
                }
        }

        registerMapZone(zone, ZoneBorders(3225,3445,3198,3471))
        registerMapZone(zone, ZoneBorders(3222,3375,3199,3387))
        registerMapZone(zone, ZoneBorders(3180,3420,3165,3435))
        registerMapZone(zone, ZoneBorders(3280,3422,3266,3435))

        on(31298, SCENERY, "read"){player, _ ->
            val pickpocketCount = GlobalStats.getDailyGuardPickpockets()
            SystemLogger.logInfo("Is equal? ${pickpocketCount == 0}")
            when(pickpocketCount){
                0 -> sendDialogue(player,  "The Varrock Palace guards are pleased to announce that crime is at an all-time low, without a single guard in the palace or at the city gates being pickpocketed today.")
                1 -> sendDialogue(player, "One of the Varrock Palace guards was pickpocketed today. He was close to tears at having lost his last few gold pieces." )
                else -> sendDialogue(player, "Guards in the Varrock Palace are on full alert due to increasing levels of pickpocketing. So far today, $pickpocketCount guards have had their money pickpocketed in the palace or at the city gates.")
            }
            return@on true
        }
    }
}