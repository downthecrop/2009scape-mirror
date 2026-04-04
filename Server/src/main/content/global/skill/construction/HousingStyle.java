package content.global.skill.construction;

import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;

import java.util.Arrays;

/**
 * The styles of houses.
 * @author Emperor
 *
 */
public enum HousingStyle {

    // open door ids are doorId + 1
	
	BASIC_WOOD          (1, 5000, 7503, 0, 13100, 13101, 13098, Decoration.BASIC_WOOD_WINDOW),
	BASIC_STONE         (10, 5000, 7503, 1, 13094, 13096, 1902, Decoration.BASIC_STONE_WINDOW),
	WHITEWASHED_STONE   (20, 7500, 7503, 2, 13006, 13007, 1415, Decoration.WHITEWASHED_STONE_WINDOW),
	FREMENNIK_STYLE_WOOD(30, 10000, 7503, 3, 13109, 13107, 13111, Decoration.FREMENNIK_WINDOW),
	TROPICAL_WOOD       (40, 15000, 7759, 0, 13016, 13015, 13011, Decoration.TROPICAL_WOOD_WINDOW),
	FANCY_STONE         (50, 25000, 7759, 1, 13119, 13118, 13116, Decoration.FANCY_STONE_WINDOW);

    /**
     * Array of all Dungeon Wall IDs.
     * From Region 7503, Location.create(1898, 5084, 0)
     */
    private static final int[] DUNGEON_WALL_IDS = {
            13019, 13020, 13021, 13022, 13023, 13024, 13025, 13026, 13027, 13028,
            13029, 13030, 13031, 13032, 13033, 13034, 13035, 13036, 13037, 13046,
            13048, 13049, 13050, 13051, 13055, 13056, 13058, 13059, 13060, 13061,
            13062, 13063, 13065, 13066, 13067, 13068, 13069, 13070, 13072, 13073,
            13074, 13075, 13076, 13077, 13079, 13080, 13081, 13082, 13083, 13084,
            13086, 13087, 13088, 13089
    };

    /**
     * Checks if the provided ID is a dungeon wall.
     * @param id The object ID.
     * @return {@code true} if it's a dungeon wall.
     */
    public static boolean isDungeonWall(int id) {
        return Arrays.binarySearch(DUNGEON_WALL_IDS, id) >= 0;
    }
	
	/**
	 * The level required.
	 */
	private final int levelRequirement;
	
	/**
	 * The cost.
	 */
	private final int cost;
	
	/**
	 * The region id.
	 */
	private final int regionId;
	
	/**
	 * The plane.
	 */
	private final int plane;
	
	/**
	 * The door id.
	 */
	private final int doorId;
	
	/**
	 * The second door id.
	 */
	private final int secondDoorId;
	
	/**
	 * The wall id.
	 */
	private final int wallId;
	
	/**
	 * The window style
	 */
	private final Decoration window;

    /**
     * Checks if the player has the level.
     *
     * @param player the player.
     * @return {@code True} if so.
     */
    public boolean hasLevel(Player player) {
        return player.getSkills().getStaticLevel(Skills.CONSTRUCTION) >= levelRequirement;
    }

	/**
	 * Constructs a new {@code HousingStyle} {@code Object}
	 * @param level The level required.
	 * @param cost The cost of the style.
	 * @param regionId The region id for this style.
	 * @param plane The plane for this style.
	 * @param doorId The door object id used in this style.
	 */
	private HousingStyle(int level, int cost, int regionId, int plane, int doorId, int secondDoorId, int wallId, Decoration window) {
		this.levelRequirement = level;
		this.cost = cost;
		this.regionId = regionId;
		this.plane = plane;
		this.doorId = doorId;
		this.secondDoorId = secondDoorId;
		this.wallId = wallId;
		this.window = window;
	}

	/**
	 * Gets the level.
	 * @return the level
	 */
	public int getLevel() {
		return levelRequirement;
	}

	/**
	 * Gets the cost.
	 * @return the cost
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * Gets the regionId.
	 * @return the regionId
	 */
	public int getRegionId() {
		return regionId;
	}

	/**
	 * Gets the plane.
	 * @return the plane
	 */
	public int getPlane() {
		return plane;
	}

	/**
	 * Gets the door used in this style.
	 * @return The door object id.
	 */
	public int getDoorId() {
		return doorId;
	}

	/**
	 * Gets the wall used in this style.
	 * @return The wall object id.
	 */
	public int getWallId() {
		return wallId;
	}
	
	/**
	 * Gets the window id for this style of house
	 * @return The windows object id
	 */
	public Decoration getWindowStyle() {
		return window;
	}

	/**
	 * Gets the secondDoorId value.
	 * @return The secondDoorId.
	 */
	public int getSecondDoorId() {
		return secondDoorId;
	}
}