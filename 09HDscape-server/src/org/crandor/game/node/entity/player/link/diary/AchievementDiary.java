package org.crandor.game.node.entity.player.link.diary;

import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.game.component.Component;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.info.login.SavingModule;

import java.nio.ByteBuffer;

/**
 * Represents an achievement diary.
 * @author Vexia
 */
public class AchievementDiary implements SavingModule {

	/**
	 * The component id of the diary.
	 */
	public static final int DIARY_COMPONENT = 275;

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
	private final boolean[] started = new boolean[3];

	/**
	 * If the rewards have been given.
	 */
	private final boolean[] rewarded = new boolean[3];

	/**
	 * The completed achievements.
	 */
	private final boolean[][] completed;

	/**
	 * Constructs a new {@code AchievementDiary} {@code Object}
	 * @param type the diary type.
	 * @param name the name.
	 */
	public AchievementDiary(DiaryType type) {
		this.type = type;
		this.completed = new boolean[type.getAchievements().length][50];
	}

	/**
	 * Open the achievement diary.
	 * @param player the player.
	 */
	public void open(Player player) {
		clear(player);
		sendString(player, "<red>Achievement Diary - " + type.getName(), 2);
		sendString(player, (isComplete() ? GREEN : hasStarted() ? YELLOW : "<red>") + type.getName() + " Area Tasks", 11);
		boolean complete;
		String line = "";
		int child = 13;
		for (int level = 0; level < type.getAchievements().length; level++) {
			sendString(player, getStatus(level) + getLevel(level) + "", child);
			child++;
			for (int i = 0; i < type.getAchievements(level).length; i++) {
				complete = isComplete(level, i);
				line = (complete ? "<str>" : "") + (complete ? "<str>" + type.getAchievements(level)[i] : type.getAchievements(level)[i]);
				if (line.contains("<br><br>")) {
					String[] lines = line.split("<br><br>");
					for (String l : lines) {
						sendString(player, l, child);
						child++;
					}
				} else {
					sendString(player, line, child);
					child++;
				}
			}
			child++;
		}
		//	sendString(player, builder.toString(), 11);
		//Changes the size of the scroll bar
		//player.getPacketDispatch().sendRunScript(1207, "i", new Object[] { 330 });
		//sendString(player, builder.toString(), 11);
		if (!player.getInterfaceManager().isOpened()) {
			player.getInterfaceManager().open(new Component(DIARY_COMPONENT));
		}
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

	@Override
	public void save(ByteBuffer buffer) {
		buffer.put((byte) 1);
		for (int i = 0; i < 3; i++) {
			buffer.put((byte) (started[i] ? 1 : 0));
		}
		buffer.put((byte) 2).put((byte) completed.length);
		for (int i = 0; i < completed.length; i++) {
			buffer.put((byte) type.getAchievements(i).length);
			for (int x = 0; x < type.getAchievements(i).length; x++) {
				buffer.put((byte) (completed[i][x] ? 1 : 0));
			}
		}
		buffer.put((byte) 3).put((byte) rewarded.length);
		for (int i = 0; i < rewarded.length; i++) {
			buffer.put((byte) (rewarded[i] ? 1 : 0));
		}
		buffer.put((byte) 0);
	}

	@Override
	public void parse(ByteBuffer buffer) {
		int opcode, size;
		while ((opcode = buffer.get()) != 0) {
			switch (opcode) {
			case 1:
				for (int i = 0; i < 3; i++) {
					started[i] = buffer.get() == 1;
				}
				break;
			case 2:
				size = buffer.get() & 0xFF;
				for (int i = 0; i < size; i++) {
					int size_ = buffer.get() & 0xFF;
					for (int x = 0; x < size_; x++) {
						completed[i][x] = buffer.get() == 1;
					}
				}
				break;
			case 3:
				size = buffer.get() & 0xFF;
				for (int i = 0; i < size; i++) {
					rewarded[i] = buffer.get() == 1;
				}
				break;
			}
		}
	}

	/**
	 * Draws the status of the diary.
	 * @param player the player.
	 */
	public void drawStatus(Player player) {
		if (hasStarted()) {
			player.getPacketDispatch().sendString((isComplete() ? GREEN : YELLOW) + type.getName(), 259, type.getChild());
			for (int i = 0; i < 3; i++) {
				player.getPacketDispatch().sendString((isComplete(i) ? GREEN : hasStarted(i) ? YELLOW : "<col=FF0000>") + getLevel(i), 259, type.getChild() + (i + 1));
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
		if (!started[level]) {
			started[level] = true;
		}
		if (!complete) {
			player.sendMessage("<col=0040ff>Well done! A " + type.getName() + " task has been updated.");
		} else {
			completed[level][index] = true;
			player.sendMessages("<col=dc143c>Well done! You have completed " + (level == 0 ? "an easy" : level == 1 ? "a medium" : "a hard") + " task in the " + type.getName() + " area. Your", "<col=dc143c>Achievement Diary has been updated.");
		}
		if (isComplete(level)) {
			player.sendMessages("<col=dc143c>You have completed all of the " + getLevel(level).toLowerCase() + " tasks in the " + type.getName() + " area.", "<col=dc143c>Speak to " + NPCDefinition.forId(type.getNpc(level)).getName() + " to claim your reward.");
		}
		drawStatus(player);
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
	public void setStarted(int level) {
		this.started[level] = true;
	}

	/**
	 * Sets an achievement as completed.
	 * @param level the level.
	 * @param index the index.
	 */
	public void setCompleted(int level, int index) {
		this.completed[level][index] = true;
	}

	/**
	 * Checks if the achievement level is completed.
	 * @param level the level.
	 * @return {@code True} if so.
	 */
	public boolean isComplete(int level) {
		for (int i = 0; i < type.getAchievements(level).length; i++) {
			if (!completed[level][i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if an achievement is complete.
	 * @param level the level.
	 * @param index the index.
	 * @return {@code True} if an achievement is completed.
	 */
	public boolean isComplete(int level, int index) {
		return completed[level][index];
	}

	/**
	 * Checks if an achievement diary is complete.
	 * @return {@code True} if completed.
	 */
	public boolean isComplete() {
		for (int i = 0; i < completed.length; i++) {
			for (int x = 0; x < type.getAchievements(i).length; x++) {
				if (!completed[i][x]) {
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
	 * Gets the level string.
	 * @param level the level.
	 * @return the string format.
	 */
	public String getLevel(int level) {
		return level == 0 ? "Easy" : level == 1 ? "Medium" : "Hard";
	}

	/**
	 * Gets the status for a level of completion of the achievement.
	 * @param level the level.
	 * @return the string color status.
	 */
	public String getStatus(int level) {
		return !hasStarted(level) ? RED : isComplete(level) ? GREEN : YELLOW;
	}

	/**
	 * Checks if a diary is started.
	 * @return {@code True} if so.
	 */
	public boolean hasStarted() {
		for (int i = 0; i < 3; i++) {
			if (started[i]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the achievement level has been started.
	 * @param level the level.
	 * @return {@code True} if so.
	 */
	public boolean hasStarted(int level) {
		return started[level];
	}

	/**
	 * Sets the level as rewarded.
	 * @param level the level.
	 */
	public void setRewarded(int level) {
		this.rewarded[level] = true;
	}

	/**
	 * Checks if the reward has been given.
	 * @param level the level.
	 * @return {@code True} if so.
	 */
	public boolean hasReward(int level) {
		return rewarded[level];
	}

	/**
	 * Gets the completed.
	 * @return the completed
	 */
	public boolean[][] getCompleted() {
		return completed;
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
	public boolean[] getStarted() {
		return started;
	}

	/**
	 * Gets the rewarded.
	 * @return the rewarded
	 */
	public boolean[] getRewarded() {
		return rewarded;
	}

}
