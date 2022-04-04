package rs09.game.content.activity.pyramidplunder

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs09.consts.Items

/**
 * An object that simply contains Pyramid Plunder data.
 */
object PlunderData {
    val playerLocations = HashMap<Player, PlunderRoom>()
    val players = ArrayList<Player>()
    val doorVarbits = arrayOf(2366, 2367, 2368, 2369)
    val doors = Array(8){doorVarbits[0]}
    val timeLeft = HashMap<Player, Int>()

    val pyramidEntranceVarbits = arrayOf(2371,2372,2373,2374)
    var currentEntrance = pyramidEntranceVarbits.random()
    var nextEntranceSwitch = 0L
    val mummy = NPC(4476, Location.create(1968, 4428, 2)).also { it.isNeverWalks = true; it.init() }

    val artifacts = arrayOf(
        arrayOf(Items.IVORY_COMB_9026, Items.POTTERY_SCARAB_9032, Items.POTTERY_STATUETTE_9036),
        arrayOf(Items.STONE_SCARAB_9030, Items.STONE_STATUETTE_9038, Items.STONE_SEAL_9042),
        arrayOf(Items.GOLDEN_SCARAB_9028, Items.GOLDEN_STATUETTE_9034, Items.GOLD_SEAL_9040)
    )

    val rooms = arrayOf(
        PlunderRoom(1, Location.create(1927, 4477, 0), Location.create(1929, 4469, 0), Direction.SOUTH),
        PlunderRoom(2, Location.create(1954, 4477, 0), Location.create(1955, 4467, 0), Direction.SOUTH),
        PlunderRoom(3, Location.create(1977, 4471, 0), Location.create(1975, 4458, 0), Direction.SOUTH),
        PlunderRoom(4, Location.create(1927, 4453, 0), Location.create(1937, 4454, 0), Direction.EAST),
        PlunderRoom(5, Location.create(1965, 4444, 0), Location.create(1955, 4449, 0), Direction.WEST),
        PlunderRoom(6, Location.create(1927, 4424, 0), Location.create(1925, 4433, 0), Direction.NORTH),
        PlunderRoom(7, Location.create(1943, 4421, 0), Location.create(1950, 4427, 0), Direction.NORTH),
        PlunderRoom(8, Location.create(1974, 4420, 0), Location.create(1971, 4431, 0), Direction.NORTH)
    )
}

/**
 * A simple way to represent a pyramid plunder room.
 */
data class PlunderRoom(val room: Int, val entrance: Location, val mummyLoc: Location, val spearDirection: Direction)