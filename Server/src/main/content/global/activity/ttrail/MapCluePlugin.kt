package content.global.activity.ttrail

import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.plugin.Plugin
import org.rs09.consts.Components
import org.rs09.consts.Items

/**
 * Initializes the map clues.
 */

// TODO: This plugin should be converted to a listener as part of a larger rewrite of Treasure Trails.

class MapCluePlugin : MapClueScroll {

    /**
     * Constructs a new [MapCluePlugin] dummy instance for plugin loading.
     */
    constructor() : this(null, -1, null, -1, null)

    /**
     * Constructs a new [MapCluePlugin] instance.
     * @param name the name.
     * @param clueId the clue id.
     * @param level the level.
     * @param interfaceId the interface id.
     * @param location the location.
     */
    constructor(name: String?, clueId: Int, level: ClueLevel?, interfaceId: Int, location: Location?) : super(name, clueId, level, interfaceId, location)

    /**
     * Constructs a new [MapCluePlugin] instance with an object and zone borders.
     * @param name the name.
     * @param clueId the clue id.
     * @param level the level.
     * @param interfaceId the interface id.
     * @param location the location.
     * @param object the object id.
     * @param borders the borders.
     */
    constructor(name: String?, clueId: Int, level: ClueLevel?, interfaceId: Int, location: Location?, `object`: Int, vararg borders: ZoneBorders) : super(name, clueId, level, interfaceId, location, `object`, *borders)

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        register(MapCluePlugin("mcgruber-crate", Items.CLUE_SCROLL_2743, ClueLevel.MEDIUM, Components.TRAIL_MAP10_355, Location.create(2658, 3488, 0), 357, ZoneBorders(2657, 3487, 2660, 3489)))
        register(MapCluePlugin("ardy-crate", Items.CLUE_SCROLL_2747, ClueLevel.MEDIUM, Components.TRAIL_MAP16_361, Location.create(2565, 3248, 0), 354, ZoneBorders(2564, 3247, 2566, 3249)))
        register(MapCluePlugin("wildy-crate", Items.CLUE_SCROLL_2773, ClueLevel.HARD, Components.TRAIL_MAP14_359, Location.create(3026, 3628, 0), 354, ZoneBorders(3025, 3627, 3027, 3629)))
        register(MapCluePlugin("goblin-crate", Items.CLUE_SCROLL_2774, ClueLevel.HARD, Components.TRAIL_MAP13_358, Location.create(2457, 3182, 0), 18506, ZoneBorders(2456, 3181, 2458, 3183)))
        register(MapCluePlugin("miscellania-dig", Items.CLUE_SCROLL_2776, ClueLevel.MEDIUM, Components.TRAIL_MAP21_340, Location.create(2536, 3865, 0)))
        register(MapCluePlugin("crafting-guild-dig", Items.CLUE_SCROLL_2778, ClueLevel.MEDIUM, Components.TRAIL_MAP07_352, Location.create(2906, 3294, 0)))
        register(MapCluePlugin("wizard-tower-dig", Items.CLUE_SCROLL_2745, ClueLevel.EASY, Components.TRAIL_MAP11_356, Location.create(3110, 3152, 0)))
        register(MapCluePlugin("east-dig", Items.CLUE_SCROLL_2805, ClueLevel.EASY, Components.TRAIL_MAP15_360, Location.create(2651, 3231, 0)))
        register(MapCluePlugin("rimmington", Items.CLUE_SCROLL_2780, ClueLevel.MEDIUM, Components.TRAIL_MAP17_362, Location.create(2924, 3209, 0)))
        register(MapCluePlugin("west-ardy", Items.CLUE_SCROLL_2782, ClueLevel.HARD, Components.TRAIL_MAP12_357, Location.create(2488, 3308, 0)))
        register(MapCluePlugin("yanille", Items.CLUE_SCROLL_2783, ClueLevel.HARD, Components.TRAIL_MAP08_353, Location.create(2616, 3077, 0)))
        register(MapCluePlugin("mcgruber", Items.CLUE_SCROLL_2785, ClueLevel.EASY, Components.TRAIL_MAP09_354, Location.create(2611, 3481, 0)))
        register(MapCluePlugin("falador-stone", Items.CLUE_SCROLL_2786, ClueLevel.EASY, Components.TRAIL_MAP06_351, Location.create(3043, 3399, 0)))
        register(MapCluePlugin("champions-guild", Items.CLUE_SCROLL_2788, ClueLevel.EASY, Components.TRAIL_MAP01_346, Location.create(3166, 3360, 0)))
        register(MapCluePlugin("falador-statue", Items.CLUE_SCROLL_2790, ClueLevel.EASY, Components.TRAIL_MAP18_337, Location.create(2970, 3415, 0)))
        register(MapCluePlugin("varrock-mine", Items.CLUE_SCROLL_2792, ClueLevel.EASY, Components.TRAIL_MAP02_347, Location.create(3289, 3373, 0)))
        register(MapCluePlugin("khazard-altar", Items.CLUE_SCROLL_2793, ClueLevel.MEDIUM, Components.TRAIL_MAP23_342, Location.create(2455, 3230, 0)))
        register(MapCluePlugin("light-house", Items.CLUE_SCROLL_2794, ClueLevel.MEDIUM, Components.TRAIL_MAP24_343, Location.create(2578, 3597, 0)))
        register(MapCluePlugin("north-seers", Items.CLUE_SCROLL_2796, ClueLevel.MEDIUM, Components.TRAIL_MAP25_344, Location.create(2666, 3562, 0)))
        register(MapCluePlugin("legends-guild", Items.CLUE_SCROLL_2797, ClueLevel.HARD, Components.TRAIL_MAP20_339, Location.create(2723, 3338, 0)))
        register(MapCluePlugin("wilderness", Items.CLUE_SCROLL_2799, ClueLevel.HARD, Components.TRAIL_MAP19_338, Location.create(3021, 3911, 0)))
        register(MapCluePlugin("draynor", Items.CLUE_SCROLL_2801, ClueLevel.MEDIUM, Components.TRAIL_MAP03_348, Location.create(3092, 3227, 0)))
        register(MapCluePlugin("abandoned-mine", Items.CLUE_SCROLL_2803, ClueLevel.MEDIUM, Components.TRAIL_MAP22_341, Location.create(3434, 3266, 0)))
        register(MapCluePlugin("ranging-guild-dig", Items.CLUE_SCROLL_3536, ClueLevel.HARD, Components.TRAIL_MAP04_349, Location.create(2702, 3429, 0)))
        register(MapCluePlugin("lumberyard-crate", Items.CLUE_SCROLL_3538, ClueLevel.HARD, Components.TRAIL_MAP05_350, Location.create(3309, 3503, 0), 2620, ZoneBorders(3308, 3502, 3310, 3504)))

        @Suppress("UNCHECKED_CAST")
        return this as Plugin<Any>
    }
}