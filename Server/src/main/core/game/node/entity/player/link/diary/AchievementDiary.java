package core.game.node.entity.player.link.diary;

import core.cache.def.impl.NPCDefinition;
import core.game.component.Component;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import core.game.diary.DiaryLevel;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents an achievement diary.
 * @author Vexia
 */
public class AchievementDiary {

	/**
	 * The component id of the diary.
	 */
	public static final int DIARY_COMPONENT = 275;

	/**
	 * Completed levels for this diary
	 */
	public static final ArrayList<Integer> completedLevels = new ArrayList<Integer>();

	/**
	 * Represents the red color code string.
	 */
	private static final String RED = "<col=8A0808>";

	/**
	 * Represents the blue color code string.
	 */
	private static final String BLUE = "<col=08088A>";

	/**
	 * Represents the yellow color code string.
	 */
	private static final String YELLOW = "<col=F7FE2E>";

	/**
	 * Represets the green color code string.
	 */
	private static final String GREEN = "<col=3ADF00>";

	/**
	 * The diary type.
	 */
	private final DiaryType type;

	/**
	 * The task types started.
	 */
	private final boolean[] levelStarted = new boolean[3];

	/**
	 * If the rewards have been given.
	 */
	private final boolean[] levelRewarded = new boolean[3];

	/**
	 * The completed achievements.
	 */
	private final boolean[][] taskCompleted;

	/**
	 * Constructs a new {@code AchievementDiary} {@code Object}
	 * @param type the diary type.
	 */
	public AchievementDiary(DiaryType type) {
		this.type = type;
		this.taskCompleted = new boolean[type.getAchievements().length][25];
	}

	/**
	 * Open the achievement diary.
	 * @param player the player.
	 */
	public void open(Player player) {
		clear(player);
		sendString(player, "<red>Achievement Diary - " + type.getName(), 2);
		int child = 12;

		sendString(player, (isComplete() ? GREEN : isStarted() ? YELLOW : "<red>") + type.getName() + " Area Tasks", child++);
		//child++;

		if (!type.getInfo().isEmpty() && !this.isStarted()) {
			sendString(player, type.getInfo(), child++);
			child += type.getInfo().split("<br><br>").length;
		}
		child++;

		boolean complete;
		String line;
		for (int level = 0; level < type.getAchievements().length; level++) {
			sendString(player, getStatus(level) + getLevel(level) + "", child++);
			child++;
			for (int i = 0; i < type.getAchievements(level).length; i++) {
				complete = isComplete(level, i);
				line = type.getAchievements(level)[i];
				if (line.contains("<br><br>")) {
					String[] lines = line.split("<br><br>");
					for (String l : lines) {
						sendString(player, complete ? "<str><str>" + l : l, child++);
					}
				} else {
					sendString(player, complete ? "<str><str>" + line : line, child++);
				}
				sendString(player, "*", child++);
			}
			child++;
		}
		if (!player.getInterfaceManager().isOpened()) {
			player.getInterfaceManager().open(new Component(DIARY_COMPONENT));
		}
		// Changes the size of the scroll bar (see 1207.cs2 for more)
		// (args1: 1 is to start from top of scroll) (args0: child-12 lines to display)
		player.getPacketDispatch().sendRunScript(1207, "ii", 1, child - 10);
	}

	/**
	 * Clears the diary screen.
	 * @param player the player.
	 */
	private void clear(Player player) {
		for (int i = 0; i < 311; i++) {
			player.getPacketDispatch().sendString("", DIARY_COMPONENT, i);
		}
	}

	public void parse(JSONObject data){
		JSONArray startedArray = (JSONArray) data.get("startedLevels");
		for(int i = 0; i < startedArray.size(); i++){
			levelStarted[i] = (boolean) startedArray.get(i);
		}
		JSONArray completedArray = (JSONArray) data.get("completedLevels");
		for(int i = 0; i < completedArray.size(); i++){
			JSONArray level = (JSONArray) completedArray.get(i);
			boolean completed = true;
			for(int j = 0; j < level.size(); j++){
				taskCompleted[i][j] = (boolean) level.get(j);
				if(!taskCompleted[i][j]){completed = !completed;}
				completedLevels.add(i);
			}
		}
		JSONArray rewardedArray = (JSONArray) data.get("rewardedLevels");
		for(int i = 0; i < rewardedArray.size(); i++){
			levelRewarded[i] = (boolean) rewardedArray.get(i);
		}
	}

	/**
	 * Draws the status of the diary.
	 * @param player the player.
	 */
	public void drawStatus(Player player) {
		if (isStarted()) {
			player.getPacketDispatch().sendString((isComplete() ? GREEN : YELLOW) + type.getName(), 259, type.getChild());
			for (int i = 0; i < 3; i++) {
				player.getPacketDispatch().sendString((isComplete(i) ? GREEN : isStarted(i) ? YELLOW : "<col=FF0000>") + getLevel(i), 259, type.getChild() + (i + 1));
			}
		}
	}

	/**
	 * Induces a task update.
	 * @param player the player.
	 * @param level the level.
	 * @param index the index of the task.
	 * @param complete if it's completed.
	 */
	public void updateTask(Player player, int level, int index, boolean complete) {
		if (!levelStarted[level]) {
			levelStarted[level] = true;
		}
		if (!complete) {
			player.sendMessage("Well done! A " + type.getName() + " task has been updated.");
		} else {
			taskCompleted[level][index] = true;
			int tempLevel = this.type == DiaryType.LUMBRIDGE ? level - 1 : level;
			player.sendMessages("Well done! You have completed "
					+ (tempLevel == -1 ? "a beginner" : tempLevel == 0 ? "an easy" : tempLevel == 1 ? "a medium" : "a hard")
					+ " task in the " + type.getName() + " area. Your", "Achievement Diary has been updated.");
		}
		if (isComplete(level)) {
			player.sendMessages("Congratulations! You have completed all of the " + getLevel(level).toLowerCase()
					+ " tasks in the " + type.getName() + " area.", "Speak to "
					+ NPCDefinition.forId(type.getNpc(level)).getName() + " to claim your reward.");
		}
		drawStatus(player);
	}

	public void finishTask(Player player, int level, int index) {
		if (!this.isComplete(level, index)) {
			this.updateTask(player, level, index, true);
			boolean complete = true;
			for(int i = 0; i < taskCompleted[level].length; i++){
				if(!taskCompleted[level][i]) {
					complete = false;
					break;
				}
			}
			if(complete){
				completedLevels.add(level);
			} else if(completedLevels.contains(level)) completedLevels.remove((Object) level);
		}
	}

	/**
	 * Resets a task to un-start
	 */
	public void resetTask(Player player, int level, int index) {
		taskCompleted[level][index] = false;
		if (!isStarted(level)) {
			this.levelStarted[level] = false;
		}
		if (!isComplete(level)) {
			this.levelRewarded[level] = false;
		}
		drawStatus(player);
	}

	public boolean checkComplete(DiaryLevel level){
		if(type != DiaryType.LUMBRIDGE && level == DiaryLevel.BEGINNER){
			return false;
		}

		if(level == DiaryLevel.BEGINNER){
			return completedLevels.contains(level.ordinal());
		}

		return completedLevels.contains(level.ordinal() - 1);
	}

	/**
	 * Sends a string on the diary interface.
	 * @param player the player.
	 * @param string the string.
	 * @param child the child.
	 */
	private void sendString(Player player, String string, int child) {
		player.getPacketDispatch().sendString(string.replace("<blue>", BLUE).replace("<red>", RED), DIARY_COMPONENT, child);
	}

	/**
	 * Sets the diary for the level as started.
	 * @param level the level.
	 */
	public void setLevelStarted(int level) {
		this.levelStarted[level] = true;
	}

	/**
	 * Sets an achievement as completed.
	 * @param level the level.
	 * @param index the index.
	 */
	public void setCompleted(int level, int index) {
		this.taskCompleted[level][index] = true;
	}

	/**
	 * Checks if the achievement level is started.
	 * @param level the level.
	 * @return {@code True} if so.
	 */
	public boolean isStarted(int level) {
		return this.levelStarted[level];
	}

	/**
	 * Checks if the diary is started.
	 * @return {@code True} if so.
	 */
	public boolean isStarted() {
		for (int j = 0; j < type.getLevelNames().length; j++) {
			if (isStarted(j)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if an achievement task is completed.
	 * @param level the level.
	 * @param index the index.
	 * @return {@code True} if an achievement is completed.
	 */
	public boolean isComplete(int level, int index) {
		return taskCompleted[level][index];
	}

	/**
	 * Checks if the achievement level is completed.
	 * @param level the level.
	 * @return {@code True} if so.
	 */
	public boolean isComplete(int level) {
		for (int i = 0; i < type.getAchievements(level).length; i++) {
			if (!taskCompleted[level][i]) {
				return false;
			}
		}
		return true;
	}

	public boolean isComplete(int level, boolean cumulative) {
		if (isComplete(level)) {
			return !cumulative || level <= 0 || isComplete(level - 1, true);
		} else {
			return false;
		}
	}

	/**
	 * Checks if an achievement diary is complete.
	 * @return {@code True} if completed.
	 */
	public boolean isComplete() {
		for (int i = 0; i < taskCompleted.length; i++) {
			for (int x = 0; x < type.getAchievements(i).length; x++) {
				if (!taskCompleted[i][x]) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Gets the level of completion.
	 * @return the level.
	 */
	public int getLevel() {
		return isComplete(2) ? 2 : isComplete(1) ? 1 : isComplete(0) ? 0 : -1;
	}

	/**
	 * Gets the level of reward.
	 * @return the level.
	 */
	public int getReward() {
		return isLevelRewarded(2) ? 2 : isLevelRewarded(1) ? 1 : isLevelRewarded(0) ? 0 : -1;
	}

	/**
	 * Gets the level string.
	 * @param level the level.
	 * @return the string format.
	 */
	public String getLevel(int level) {
		return type.getLevelNames()[level];
	}

	/**
	 * Gets the status for a level of completion of the achievement.
	 * @param level the level.
	 * @return the string color status.
	 */
	public String getStatus(int level) {
		return !isStarted(level) ? RED : isComplete(level) ? GREEN : YELLOW;
	}

	/**
	 * Sets the level as rewarded.
	 * @param level the level.
	 */
	public void setLevelRewarded(int level) {
		this.levelRewarded[level] = true;
	}

	/**
	 * Checks if the reward has been given.
	 * @param level the level.
	 * @return {@code True} if so.
	 */
	public boolean isLevelRewarded(int level) {
		return levelRewarded[level];
	}

	/**
	 * Gets the completed.
	 * @return the completed
	 */
	public boolean[][] getTaskCompleted() {
		return taskCompleted;
	}

	/**
	 * Gets the type.
	 * @return the type
	 */
	public DiaryType getType() {
		return type;
	}

	/**
	 * Gets the started.
	 * @return the started
	 */
	public boolean[] getLevelStarted() {
		return levelStarted;
	}

	/**
	 * Gets the rewarded.
	 * @return the rewarded
	 */
	public boolean[] getLevelRewarded() {
		return levelRewarded;
	}

	/**
	 * Removes the non-lamp reward item for the given level of the given type
	 * @param player the player to remove the reward from
	 * @param type the DiaryType: LUMBRIDGE, FALADOR, etc.
	 * @param level the diary level. 0-indexed.
	 * @return whether or not a reward was removed
	 */
	public static boolean removeRewardsFor(Player player, DiaryType type, int level) {
		Item[] rewards = type.getRewards(level);
		//lamps are always the 2nd reward for a level, don't remove lamps
		boolean hasRemoved =
				player.getInventory().remove(rewards[0])
				||player.getBank().remove(rewards[0])
				||player.getEquipment().remove(rewards[0]);

		if (hasRemoved) {
			player.debug("Removed previous reward");
		}

		return hasRemoved;
	}

	/**
	 * Adds all rewards for the given level of the given type (including lamps)
	 * Will return false if the player can't fit all the items in their inventory.
	 * @param player the player to grant the items to
	 * @param type the DiaryType: LUMBRIDGE, FALADOR, etc.
	 * @param level the diary level. 0-indexed.
	 * @return whether or not we successfully added the reward items to the player's inventory.
	 */
	public static boolean addRewardsFor(Player player, DiaryType type, int level) {
		Item[] rewards = type.getRewards(level);

		int freeSlots = player.getInventory().freeSlots();
		if (freeSlots < rewards.length)
			return false;

		boolean allRewarded = true;
		for (Item reward : rewards) {
			allRewarded &= player.getInventory().add(reward);
		}

		if (!allRewarded) {
			Arrays.stream(rewards).forEach((item) -> {
				boolean _ignored = player.getInventory().remove(item);
			});
		}

		return allRewarded;
	}

	/**
	 * Convenience method. Flags a level as complete, removes previous level's reward item, then adds new reward items.
	 * @param player the player to flag completion for
	 * @param type the DiaryType: LUMBRIDGE, FALADOR, etc.
	 * @param level the diary level. 0-indexed.
	 */
	public static boolean flagRewarded(Player player, DiaryType type, int level) {
		if (level > 0) {
			removeRewardsFor(player, type, level - 1);
		}
		if (addRewardsFor(player, type, level))
			player.getAchievementDiaryManager().getDiary(type).setLevelRewarded(level);
		else {
			player.sendMessage("You do not have enough space in your inventory to claim these rewards.");
			return false;
		}

		return true;
	}

	/**
	 * Determines if a replacement reward can be given for the particular diary and level.
	 * Checks to make sure the player has completed the level and claimed the rewards, and has not claimed
	 * the next diary level.
	 * @param player the player to check
	 * @param type the DiaryType: LUMBRIDGE, FALADOR, etc.
	 * @param level the diary level. 0-indexed.
	 * @return whether or not a replacement can be granted.
	 */
	public static boolean canReplaceReward(Player player, DiaryType type, int level) {
        Item reward = type.getRewards(level)[0];
		boolean claimed = hasCompletedLevel(player, type, level)
				&& hasClaimedLevelRewards(player, type, level)
				&& !player.hasItem(reward);
		return level == 2 ? claimed : claimed && !hasClaimedLevelRewards(player, type, level+1);
	}

	/**
	 * Grants the replacement for the given level provided the player is eligible.
	 * Does not refund lamps.
	 * @param player the player to grant the reward to
	 * @param type the DiaryType: LUMBRIDGE, FALADOR, etc.
	 * @param level the diary level, 0-indexed
	 * @return whether or not the player was granted the replacement
	 */
	public static boolean grantReplacement(Player player, DiaryType type, int level) {
		Item reward = type.getRewards(level)[0]; //Can only replace non-lamp reward
		return canReplaceReward(player, type, level) && player.getInventory().add(reward);
	}

	/**
	 * Checks if a player has completed the given level of the given diary.
	 * @param player the player to check
	 * @param type the DiaryType: LUMBRIDGE, FALADOR, etc.
	 * @param level the level to check, 0-indexed
	 * @return whether or not the player has completed the level.
	 */
	public static boolean hasCompletedLevel(Player player, DiaryType type, int level) {
		if (level > type.getLevelNames().length - 1)
			return false;
		return player.getAchievementDiaryManager().getDiary(type).isComplete(level, true);
	}

	/**
	 * Checks if a player has claimed the rewards for the given level of the given diary
	 * @param player the player to check
	 * @param type the DiaryType: LUMBRIDGE, FALADOR, etc.
	 * @param level the level te check
	 * @return whether or not the player has claimed the rewards
	 */
	public static boolean hasClaimedLevelRewards(Player player, DiaryType type, int level) {
		return player.getAchievementDiaryManager().getDiary(type).isLevelRewarded(level);
	}

	/**
	 * Checks if a player can claim the rewards for the given level of the given diary
	 * Checks to make sure the player hasn't claimed the next level.
	 * @param player the player to check
	 * @param type the DiaryType: LUMBRIDGE, FALADOR, etc.
	 * @param level the level to check
	 * @return whether or not the player can claim the rewards
	 */
	public static boolean canClaimLevelRewards(Player player, DiaryType type, int level) {
		if (level == 2)
			// Cannot be a higher level to claim
			return  hasCompletedLevel(player, type, level) && !hasClaimedLevelRewards(player, type, level);
		else
			return !hasClaimedLevelRewards(player, type, level + 1) && hasCompletedLevel(player, type, level) && !hasClaimedLevelRewards(player, type, level);
	}

	/**
	 * Fetches the rewards for a given diary at the given level.
	 * @param type the DiaryType: LUMBRIDGE, FALADOR, etc.
	 * @param level the level, 0-indexed.
	 * @return an array of the reward items for that level. Includes lamps. The non-lamp reward is always index 0.
	 */
	public static Item[] getRewards(DiaryType type, int level) {
		return type.getRewards(level);
	}
}
