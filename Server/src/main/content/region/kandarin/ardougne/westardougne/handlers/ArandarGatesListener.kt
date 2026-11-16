package content.region.kandarin.ardougne.westardougne.handlers

import content.data.Quests
import core.api.*
import core.game.global.action.DoorHelper
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.RegionManager.getLocalNPCs
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

class ArandarGatesListener : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(Scenery.HUGE_GATE_3944, Scenery.HUGE_GATE_3945), IntType.SCENERY, "enter") { player, node ->
            if (hasRequirement(player, Quests.REGICIDE)) {
                handleDoor(player, node.asScenery())
            } else {
                val closest = getLocalNPCs(player.location)
                    .filter { it.id == NPCs.ELF_WARRIOR_1201 }
                    .minByOrNull { it.location.getDistance(player.location) }
                if (closest != null) {
                    face(player, closest.location)
                }
                sendNPCDialogue(player, NPCs.ELF_WARRIOR_1201, "Oi! What are you doing? Get away from there!")
            }
            return@on true
        }

        setDest(IntType.SCENERY, intArrayOf(Scenery.HUGE_GATE_3944, Scenery.HUGE_GATE_3945), "enter") { entity, node ->
            DoorHelper.getDestination(entity, node.asScenery())
        }
    }
}