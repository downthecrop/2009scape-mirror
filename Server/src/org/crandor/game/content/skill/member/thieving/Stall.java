package org.crandor.game.content.skill.member.thieving;

import org.crandor.ServerConstants;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.repository.Repository;
import org.crandor.tools.RandomFunction;

/**
 * Represents a thieving stall.
 * @author 'Vexia
 */
public enum Stall {
	VEGETABLE_STALL(new int[] { 4706, 4708, }, 2, new int[][] { { 1957, 1 }, { 1965, 1 }, { 1942, 1 } }, "some vegetables from the vegetable's merchant stall.", 10, 634, 10), BAKER_STALL(new int[] { 2561, 6163, 34384 }, 5, new int[][] { { 1891, 1 }, { 2309, 1 }, { 1901, 1 } }, "a cake from the baker's stall.", 16, 634, 11), CRAFTING_STALL(new int[] { 6166 }, 5, new int[][] { { 1755, 1 }, { 1592, 1 }, { 1901, 1 }, { 1597, 1 } }, "some crafting equipment from the crafting merchant stall", 16, 634, 18), TEA_STALL(new int[] { 635, 6574 }, 5, new int[][] { { 712, 1 } }, "a cup of tea.", 16, 634, 10), SILK_STALL(new int[] { 34383, 2560 }, 20, new int[][] { { 950, 1 } }, "some silk from the silk stall.", 24, 634, 10), WINE_STALL(new int[] { 34383, 14011 }, 22, new int[][] { { 1937, 1 }, { 1993, 1 }, { 1987, 1 }, { 1935, 1 }, { 7919, 1 } }, "some wine from the wine stall.", 27, 634, 20), SEED_STALL(new int[] { 7053 }, 27, new int[][] { { 5305, 1 }, { 5306, 1 }, { 5308, 1 }, { 5319, 3 }, { 5318, 2 }, { 5324, 1 }, { 5322, 2 } }, "some seed's from the seed merchant's stall.", 10, 634, 10), FUR_STALL(new int[] { 34387, 2563 }, 35, new int[][] { { 6814, 1 }, { 958, 1 } }, "some fur from the fur stall.", 36, 634, 22), FISH_STAILL(new int[] { 4277, 4705, 4707 }, 42, new int[][] { { 331, 1 }, { 359, 1 }, { 377, 1 } }, "some fish from the fish stall.", 42, 634, 22), CROSSBOW_STALL(new int[] { 4277, 4705, 4707 }, 49, new int[][] { { 9375, 3 }, { 9420, 1 }, { 9440, 1 } }, "something from the crossbow stall.", 52, 634, 22), SILVER_STALL(new int[] { 2565, 6164, 34382 }, 50, new int[][] { { 442, 1 } }, "some silver from the silver stall.", 54, 634, 50), SPICE_STALL(new int[] { 2564, 34386 }, 65, new int[][] { { 2007, 1 } }, "some spice from the spice stall.", 81, 634, 100), MAGIC_STALL(new int[] { 4705, 4707 }, 65, new int[][] { { 556, 1 }, { 557, 1 }, { 554, 1 }, { 555, 1 }, { 563, 1 } }, "some runes from the rune's stall.", 100, 634, 100), SCIMITAR_STALL(new int[] { 4705, 4707 }, 65, new int[][] { { 1323, 1 } }, "a scimitar from the scimitar stall.", 100, 634, 100), GEM_STALL(new int[] { 2562, 6162, 34385 }, 75, new int[][] { { 1623, 1 } }, "a gem from the gem stall.", 160, 634, 340),
	// ape atoll:
	SCIMITAR_APE_STALL(new int[] { 4878 }, 65, new int[][] { { 1323, 1 } }, "a scimitar from the scimitar stall.", 100, 4797, 120), MAGIC_APE_STALL(new int[] { 4877 }, 65, new int[][] { { 556, 1 }, { 557, 1 }, { 554, 1 }, { 555, 1 }, { 563, 1 } }, "some runes from the magic stall.", 100, 4797, 120), MONEKY_GENERAL(new int[] { 4876 }, 5, new int[][] { { 1931, 1 }, { 2347, 1 }, { 590, 1 } }, "a general item from the general stall.", 16, 4797, 10), MONKEY_FOOD(new int[] { 4875 }, 5, new int[][] { { 1963, 1 } }, "a banana from the food stall.", 16, 4797, 10), CRAFTING_APE(new int[] { 4874 }, 5, new int[][] { { 1755, 1 }, { 1592, 1 }, { 1901, 1 }, { 1597, 1 } }, "a crafting item from the crafting stall.", 16, 4797, 10);

	/**
	 * Constructs a new {@code Stall} {@code Object}.
	 * @param ids the object id.
	 * @param level the level.
	 * @param reward the reward.
	 * @param experience the experience.
	 * @param message the message.
	 * @param temporary the temporary object.
	 * @param delay the delay.
	 */
	Stall(int[] objectId, final int level, final int[][] reward, final String message, final double experience, final int temporary, final int delay) {
		this.ids = objectId;
		this.level = level;
		this.reward = reward;
		this.message = message;
		this.experience = experience;
		this.temporary = temporary;
		this.delay = delay;
	}

	/**
	 * The object id.
	 */
	private final int[] ids;

	/**
	 * The required level.
	 */
	private final int level;

	/**
	 * The reward.
	 */
	private final int[][] reward;

	/**
	 * The experience rewarded.
	 */
	private final double experience;

	/**
	 * The steal item.
	 */
	private final String message;

	/**
	 * The temporary object.
	 */
	private final int temporary;

	/**
	 * The respawn delay.
	 */
	private final int delay;

	/**
	 * Gets the ids.
	 * @return The ids.
	 */
	public int[] getIds() {
		return ids;
	}

	/**
	 * Gets the level.
	 * @return The level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gets the reward.
	 * @return The reward.
	 */
	public int[][] getReward() {
		return reward;
	}

	/**
	 * Gets the experience.
	 * @return The experience.
	 */
	public double getExperience() {
		return experience;
	}

	/**
	 * Gets the message.
	 * @return The message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets the temporary.
	 * @return The temporary.
	 */
	public int getTemporary() {
		return temporary;
	}

	/**
	 * Gets the delay.
	 * @return The delay.
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Gets the random loot.
	 * @return the loot.
	 */
	public Item getRandomLoot() {
		int[] loot = getReward()[RandomFunction.random(getReward().length)];
		final Item item = new Item(loot[0], loot[1]);
		return item;
	}

	/**
	 * Gets the current respawn duration (in ticks).
	 * @return The respawn duration.
	 */
	public int getRespawnDuration() {
		int minimum = delay & 0xFFFF;
		int maximum = (delay >> 16) & 0xFFFF;
		double playerRatio = ServerConstants.MAX_PLAYERS / Repository.getPlayers().size();
		return (int) (minimum + ((maximum - minimum) / playerRatio));
	}

	/**
	 * Gets the stall by the object.
	 * @param object the object.
	 * @return the stall.
	 */
	public static Stall forObject(final GameObject object) {
		for (Stall stall : Stall.values()) {
			for (int i : stall.getIds()) {
				if (i == object.getId()) {
					return stall;
				}
			}
		}
		return null;
	}
}
