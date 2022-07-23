package rs09.game.node.entity.skill.thieving

import core.game.world.map.Location
import org.rs09.consts.Scenery

enum class PickableDoors(val id: Int, val location: Location, val levelRequirement: Int, val xpReward: Double, val requireLockpick: Boolean) {
    ARDOUGNE_EAST_GEMSTALL_BUILDING(
        Scenery.DOOR_34812,
        Location(2672, 3301, 0),
        1,
        4.0,
        false
    ),

    ARDOUGNE_EAST_BAKERSTALL_BUILDING(
        Scenery.DOOR_34812,
        Location(2672, 3308, 0),
        14,
        15.0,
        false
    ),

    WILDERNESS_MAGIC_AXE_HUT_NORTH(
        Scenery.DOOR_2557,
        Location(3190, 3963, 0),
        1,
        22.5,
        true
    ),

    WILDERNESS_MAGIC_AXE_HUT_SOUTH(
        Scenery.DOOR_2557,
        Location(3190, 3957, 0),
        1,
        22.5,
        true
    ),

    ARDOUGNE_NORTHWEST_FURNACE_BUILDING(
        Scenery.DOOR_2556,
        Location(2610, 3116, 0),
        16,
        15.0,
        false
    ),

    ARDOUGNE_SEWERS_LEFT_GATE(
        Scenery.GATE_2552,
        Location(2655, 9715, 0),
        32,
        25.0,
        false
    ),

    ARDOUGNE_SEWERS_RIGHT_GATE(
        Scenery.GATE_2552,
        Location(2655, 9714, 0),
        32,
        25.0,
        false
    ),

    WILDERNESS_PIRATE_HIDEOUT_EAST(
        Scenery.DOOR_2558,
        Location(3044, 3956, 0),
        39,
        35.0,
        false
    ),

    WILDERNESS_PIRATE_HIDEOUT_NORTH(
        Scenery.DOOR_2558,
        Location(3044, 3959, 0),
        39,
        35.0,
        false
    ),

    WILDERNESS_PIRATE_HIDEOUT_WEST(
        Scenery.DOOR_2558,
        Location(3038, 3956, 0),
        39,
        35.0,
        false
    ),

    ARDOUGNE_CHAOS_DRUID_TOWER(
        Scenery.DOOR_2554,
        Location(2565, 3356, 0),
        46,
        37.0,
        false
    ),

    ARDOUGNE_CASTLE_UPSTAIRS_NORTH(
        Scenery.DOOR_2555,
        Location(2579, 3307, 1),
        61,
        50.0,
        false
    ),

    ARDOUGNE_CASTLE_UPSTAIRS_SOUTH(
        Scenery.DOOR_2555,
        Location(2579, 3286, 1),
        61,
        50.0,
        false
    ),

    YANILLE_AGILITY_DUNGEON(
        Scenery.DOOR_2559,
        Location(2601, 9482, 0),
        82,
        50.0,
        true
    );

    companion object {
        private val idMap = HashMap<Int, PickableDoors>(PickableDoors.values().size * 5)

        init {
            PickableDoors.values().forEach {
                idMap[it.id] = it
            }
        }

        @JvmStatic
        fun forID(id: Int): PickableDoors? {
            return idMap[id]
        }
    }
}