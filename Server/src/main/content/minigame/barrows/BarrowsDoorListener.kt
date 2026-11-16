package content.minigame.barrows

import core.api.handleDoor
import core.game.global.action.DoorHelper
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.RegionManager
import core.tools.RandomFunction
import org.rs09.consts.Scenery

class BarrowsDoorListener : InteractionListener {
    val DOORS = intArrayOf(Scenery.DOOR_6714, Scenery.DOOR_6715, Scenery.DOOR_6733, Scenery.DOOR_6734)

    override fun defineListeners() {
        on(DOORS, IntType.SCENERY, "open") { player, node ->
            handleDoor(player, node.asScenery())
            if (RandomFunction.roll(15)) {
                val brothers = player.savedData.activityData.barrowBrothers
                val alive = (0..5).filter { !brothers[it] }.toIntArray()
                if (alive.isNotEmpty()) {
                    val index = if (alive.size > 1) RandomFunction.random(0, alive.size) else 0
                    val spawnLoc = RegionManager.getTeleportLocation(node.location, 1)
                    BarrowsCrypt.getCrypt(alive[index]).spawnBrother(player, spawnLoc)
                }
            }
            return@on true
        }

        setDest(IntType.SCENERY, DOORS, "open") { entity, node ->
            DoorHelper.getDestination(entity, node.asScenery())
        }
    }
}
