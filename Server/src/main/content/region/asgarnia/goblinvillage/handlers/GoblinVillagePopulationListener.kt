package content.region.asgarnia.goblinvillage.handlers

import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.DeathTask
import core.game.world.map.RegionManager.getLocalNpcs
import org.rs09.consts.Scenery

class GoblinVillagePopulationListener : InteractionListener{
    override fun defineListeners() {
        on(Scenery.SIGNPOST_31301, IntType.SCENERY, "read") { player, node ->
            var population = 3 // This covers General Wartface, General Bentnoze and Grubfoot
            val npcs = getLocalNpcs(player, 50) // This radius covers more than the goblin village.
            // There should be 18 Goblins walking around.
            // 10 are within the village walls where the generals are.
            // 8 of them are fighting each other on the west of the path to the village.
            // Total should be 21. They respawn really fast.
            for (n in npcs) {
                if (n.name == "Goblin" && !DeathTask.isDead(n)) {
                    population++
                }
                player.dialogueInterpreter.sendPlainMessage(false, "Welcome to Goblin Village.", "Current population: $population")
            }
            return@on true
        }
    }
}