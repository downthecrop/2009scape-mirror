package core.game.node.entity.player.link;

import core.game.node.entity.player.Player;

/**
 * Represents a managing class of saved data related to ingame interactions,
 * such as questing data, npc talking data, etc.
 * @author 'Vexia
 */
public class SavedData {

	/**
	 * Represents the global data to save.
	 */
	private final GlobalData globalData = new GlobalData();

	/**
	 * Represents the activity data to save.
	 */
	private final ActivityData activityData = new ActivityData();

	/**
	 * Represents the quest data to save.
	 */
	private final QuestData questData = new QuestData();

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Constructs a new {@Code SavedData} {@Code Object}
	 * @param player the player.
	 */
	public SavedData(Player player) {
		this.player = player;
	}

	/**
	 * Gets the boolean value.
	 * @param value the value.
	 * @return the value.
	 */
	public static boolean getBoolean(byte value) {
		return value == 1;
	}

	/**
	 * Gets the activityData.
	 * @return The activityData.
	 */
	public ActivityData getActivityData() {
		return activityData;
	}

	/**
	 * Gets the questData.
	 * @return The questData.
	 */
	public QuestData getQuestData() {
		return questData;
	}

	/**
	 * Gets the globalData.
	 * @return The globalData.
	 */
	public GlobalData getGlobalData() {
		return globalData;
	}

	/**
	 * Gets the player.
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
}
