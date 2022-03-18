package core.game.node.entity.skill.fishing;

import core.game.node.item.Item;

import java.util.HashMap;

/**
 * Represents a type of fish to catch.
 * @author ceikry
 */
public enum Fish {
	CRAYFISH(new Item(13435),1, 10, 0.15, 0.5),
	SHRIMP(new Item(317), 1, 10, 0.191, 0.5), 
	SARDINE(new Item(327), 5, 20, 0.148, 0.374), 
	KARAMBWANJI(new Item(3150), 5, 5, 0.4, 0.98),
	HERRING(new Item(345), 10, 30, 0.129, 0.504),
	ANCHOVIE(new Item(321), 15, 40, 0.098, 0.5), 
	MACKEREL(new Item(353), 16, 20, 0.055, 0.258), 
	TROUT(new Item(335), 20, 50, 0.246, 0.468), 
	COD(new Item(341), 23, 45, 0.063, 0.219), 
	PIKE(new Item(349), 25, 60, 0.14, 0.379), 
	SLIMY_EEL(new Item(3379), 28, 65, 0.117, 0.216), 
	SALMON(new Item(331), 30, 70, 0.156, 0.378),
	FROG_SPAWN(new Item(5004), 33, 75, 0.164, 0.379),
	TUNA(new Item(359), 35, 80, 0.109, 0.205), 
	RAINBOW_FISH(new Item(10138), 38, 80, 0.113, 0.254), 
	CAVE_EEL(new Item(5001), 38, 80, 0.145, 0.316), 
	LOBSTER(new Item(377), 40, 90, 0.16, 0.375), 
	BASS(new Item(363), 46, 100, 0.078, 0.16), 
	SWORDFISH(new Item(371), 50, 100, 0.105, 0.191),
	LAVA_EEL(new Item(2148), 53, 30, 0.227, 0.379),
	MONKFISH(new Item(7944), 62, 120, 0.293, 0.356), 
	KARAMBWAN(new Item(3142), 65, 105, 0.414, 0.629), 
	SHARK(new Item(383), 76, 110, 0.121, 0.16), 
	SEA_TURTLE(new Item(395), 79, 38, 0.0, 0.0), 
	MANTA_RAY(new Item(389), 81, 46, 0.0, 0.0), 
	SEAWEED(new Item(401), 16, 1, 0.63, 0.219), 
	CASKET(new Item(405), 16, 10, 0.63, 0.219), 
	OYSTER(new Item(407), 16, 10, 0.63, 0.219);

	/**
	 * Constructs a new {@code Fish} {@code Object}.
	 * @param item the <code>Item</code>
	 * @param level the level.
	 * @param experience the experience.
	 */
	Fish(final Item item, final int level, final double experience, final double lowChance, final double highChance, final int... npcs) {
		this.item = item;
		this.level = level;
		this.experience = experience;
		this.npcs = npcs;
        // The chances are given in the table based on when the fish can first be caught.
        // Linearly extrapolate the success chance at level 1.
        // y = (x - x0) * ((y1 - y0) / (x1 - x0)) + y0
        this.lowChance = (1 - (double)level)*((highChance - lowChance) / (99.0 - (double)level)) + lowChance;
        this.highChance = highChance;
	}

	public static HashMap<Integer,Fish> fishMap = new HashMap<>();
	static{
		for(Fish fish : Fish.values()){
			fishMap.putIfAbsent(fish.item.getId(),fish);
		}
	}
	private final Item item;

	/**
	 * Represents the required level to catch the {@link Fish}.
	 */
	private final int level;

	/**
	 * Represents the experience gained from this fish.
	 */
	private final double experience;

    private final double lowChance;
    private final double highChance;

	/**
	 * The npc ids that give this fish.
	 */
	private final int[] npcs;

	/**
	 * Gets the fish.
	 * @param item the item.
	 * @return the fash.
	 */
	public static Fish forItem(Item item) {
		return fishMap.get(item.getId());
	}

	/**
	 * @return the item.
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * @return the level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return the experience.
	 */
	public double getExperience() {
		return experience;
	}

	/**
	 * Gets the npcs.
	 * @return the npcs
	 */
	public int[] getNpcs() {
		return npcs;
	}

    public double getSuccessChance(int level) {
        return ((double)level - 1.0)*((highChance - lowChance) / (99.0 - 1.0)) + lowChance;
    }
}
