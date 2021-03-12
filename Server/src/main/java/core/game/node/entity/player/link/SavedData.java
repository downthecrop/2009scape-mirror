package core.game.node.entity.player.link;

import core.game.node.entity.player.Player;

import core.game.node.entity.player.link.spawn.SpawnData;
import rs09.game.system.SystemLogger;

import java.nio.ByteBuffer;

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
	 * The spawn data to save.
	 */
	private final SpawnData spawnData = new SpawnData();

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
	 * Method used to save an activity var that isn't valued at default.
	 * @param buffer the buffer.
	 * @param var the variable to save.
	 */
	public static final void save(final ByteBuffer buffer, final Object var, final int index) {
		if (var instanceof Integer ? (int) var != 0 : var instanceof Double ? (double) var != 0.0 : var instanceof Byte ? (byte) var != 0 : var instanceof Short ? (short) var != 0 : var instanceof Long ? (long) var != 0L : var instanceof Boolean ? (boolean) var != false : var != null) {
			buffer.put((byte) index);
			if (var instanceof Integer) {
				buffer.putInt((int) var);
			} else if (var instanceof Byte) {
				buffer.put((byte) var);
			} else if (var instanceof Short) {
				buffer.putShort((short) var);
			} else if (var instanceof Long) {
				buffer.putLong((long) var);
			} else if (var instanceof Boolean) {
				buffer.put((byte) 1);
			} else if (var instanceof Double) {
				buffer.putDouble((double) var);
			} else if (var instanceof double[]) {
				double[] doubleArray = ((double[]) var);
				for (int i = 0; i < doubleArray.length; i++) {
					buffer.putDouble(doubleArray[i]);
				}
			} else if (var instanceof boolean[]) {
				boolean[] booleanArray = ((boolean[]) var);
				for (int i = 0; i < booleanArray.length; i++) {
					buffer.put((byte) (booleanArray[i] ? 1 : 0));
				}
			} else if (var instanceof int[]) {
				int[] intArray = ((int[]) var);
				for (int i = 0; i < intArray.length; i++) {
					buffer.putInt(intArray[i]);
				}
			}
		}
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
	 * Gets the boolean value.
	 * @param buffer the buffer.
	 * @return the value.
	 */
	public static boolean getBoolean(ByteBuffer buffer) {
		return getBoolean(buffer.get());
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
	
	/**
	 * Gets the spawnData.
	 * @return the spawnData
	 */
	public SpawnData getSpawnData() {
		return spawnData;
	}

}
