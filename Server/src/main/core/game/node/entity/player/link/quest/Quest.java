package core.game.node.entity.player.link.quest;

import content.data.Quests;
import core.game.component.Component;
import core.game.node.entity.player.Player;
import core.plugin.Plugin;
import core.plugin.PluginManifest;
import core.plugin.PluginType;

import java.util.Arrays;
import java.util.Random;

import static core.api.ContentAPIKt.*;

/**
 * A skeleton plugin for a quest.
 * @author Vexia
 * 
 */
@PluginManifest(type = PluginType.QUEST)
public abstract class Quest implements Plugin<Object> {

	/**
	 * Represents the red string.
	 */
	public static final String RED = "<col=8A0808>";

	/**
	 * Represents the bright red string.
	 */
	public static final String BRIGHT_RED = "<col=FF0000>";

	/**
	 * Represents the blue string.
	 */
	public static final String BLUE = "<col=08088A>";

	/**
	 * Represents the black string.
	 */
	public static final String BLACK = "<col=000000>";

	/**
	 * The constant representing the journal component.
	 */
	public static final int JOURNAL_COMPONENT = 275;

	/**
	 * The constant representing the quest reward component.
	 */
	public static final int REWARD_COMPONENT = 277;
	
	/**
	 * The quest as a Quests item (currently only used for the quest's name).
	 */
	private final Quests quest;
	
	/**
	 * The index id of the quest.
	 */
	private final int index;
	
	/**
	 * The button id of the quest.
	 */
	private final int buttonId;

	/**
	 * The rewarded quest points.
	 */
	private final int questPoints;
	
	/**
	 * The config values based on stage.
	 */
	private final int[] configs;

	/**
	 * Constructs a new {@link Quest}
	 * @param quest of the quest. Prereqs reference this
	 * @param index of the quest, usually buttonId + 1
	 * @param buttonId of the quest on the quest list in game
	 * @param questPoints rewarded after completing quest
	 * @param configs of Varp/Varbit and values to set the quest color to red/yellow/green. e.g. {234, 0, 1, 10}
	 * <br><br>
	 * Configs are made of either 4/5 numbers:<br>
	 * 4 numbers: {1: VARP to set, 2: red quest name, 3: yellow quest name, 4: green quest name}<br>
	 * 5 numbers: {1: VARP(Ignored), 2: VARPBIT to set, 3: red quest name, 4: yellow quest name, 5: green quest name}<br>
	 * <br>
	 * VARP/VARPBIT is set to values before/during/after quest at stage 0/1-99/100.
	 * Get these values from the VARPTOOL.<br>
	 * <br>
	 * If you see VARP (e.g. ./get_varp.sh 120):<br>
	 * if (VARP[26] == 80 || VARP[26] == 90) return 2; if (VARP[26] == 0) return 0; return 1; }; if (arg0 == 89)<br>
	 * Use 4 numbers: {26, 0, 1, 80} -> {VARP, return 0, return 1, return 2}<br>
	 * <br>
	 * If you see VARPBIT (e.g. ./get_varp.sh 119):<br>
	 * if (VARPBIT[451] > 1) return 2; if (VARPBIT[451] == 0) return 0; return 1; }; if (arg0 == 88)<br>
	 * Use 5 numbers: {0, 451, 0, 1, 2} -> {Ignore, VARPBIT, return 0, return 1, return 2}<br>
	 */
	public Quest(Quests quest, int index, int buttonId, int questPoints, int...configs) {
		this.quest = quest;
		this.index = index;
		this.buttonId = buttonId;
		this.questPoints = questPoints;
		this.configs = configs;
	}
	
	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public abstract Quest newInstance(Object object);
	
	/**
	 * Starts this quest.
	 * @param player The player.
	 */
	public void start(Player player) {
		player.getQuestRepository().setStage(this, 10);
		player.getQuestRepository().syncronizeTab(player);
	}
	
	/**
	 * Draws the text on the quest journal.
	 * @param player The player.
	 * @param stage The stage to draw.
	 */
	public void drawJournal(Player player, int stage) {
		for (int i = 0; i < 311; i++) {
			player.getPacketDispatch().sendString("" , JOURNAL_COMPONENT, i);
		}
		player.getPacketDispatch().sendString("<col=8A0808>" + getQuest() + "</col>", JOURNAL_COMPONENT, 2);
	
	}

	/**
	 * Finishes the quest.
	 * @param player The player.
	 */
	public void finish(Player player) {
		if(player.getQuestRepository().isComplete(quest)) {
			throw new IllegalStateException("Tried to complete quest " + quest + " twice, which is not allowed!");
		}
		for (int i = 0; i < 18; i++) {
			if (i == 9 || i == 3 || i == 6) {
				continue;
			}
			player.getPacketDispatch().sendString("", 277, i);
		}
		player.getQuestRepository().setStage(this, 100);
		player.getQuestRepository().incrementPoints(getQuestPoints());
		player.getQuestRepository().syncronizeTab(player);
		player.getInterfaceManager().open(new Component(277).setCloseEvent((p, c) -> {
			this.questCloseEvent(p, c);
			return true;
		}));
		player.getPacketDispatch().sendString("" + player.getQuestRepository().getPoints() + "", 277, 7);
		player.getPacketDispatch().sendString("You have completed the " + getQuest() + " Quest!", 277, 4);
		player.getPacketDispatch().sendMessage("Congratulations! Quest complete!");
		int questJingles[] = {152, 153, 154};
		playJingle(player, questJingles[new Random().nextInt(3)]);
	}

	/**
	 * Resets the quest. This is called when ::setQuestStage is set to 0.
	 * Useful to override and reset quest player attributes.
	 */
	public void reset(Player player) {}

	/**
	 * Function callback when closing the quest.
	 * Override this to follow up on dialogue after the component closes.
	 */
	public void questCloseEvent(Player player, Component component) {}

	/**
	 * Draws a line on the journal component.
	 * @param player The player.
	 * @param message The message.
	 * @param line The line.
	 */
	public void line(Player player, String message, int line) {
		String send = BLUE + "" + message.replace("<n>", "<br><br>").replace("<blue>", BLUE).replace("<red>", RED);
		if (send.contains("<br><br>") || send.contains("<n>")) {
			String[] lines = send.split(send.contains("<br><br>") ? "<br><br>" : "<n>");
			for (int i = 0; i < lines.length; i++) {
				line(player, lines[i].replace("<br><br>", "").replace("<n>", ""), line, false);
				line++;
			}
		} else {
			send = send.replace("!!", RED).replace("??", BLUE).replace("---", BLACK + "<str>").replace("/--", BLUE).replace("%%", BRIGHT_RED).replace("&&", BLUE);
			line(player, send, line, false);
		}
	}

	/**
	 * Draws a line on the quest journal component.
	 * @param player The player.
	 * @param message The message.
	 * @param line The line number.
	 * @param crossed True if the message should be crossed out.
	 */
	public void line(Player player, String message, int line, final boolean crossed) {
		String send;
		if(!crossed){
			send = BLUE + "" + message.replace("<n>", "<br><br>").replace("<blue>", BLUE).replace("<red>", RED);
			send = send.replace("!!", RED).replace("??", BLUE).replace("%%", BRIGHT_RED).replace("&&", BLUE);
		} else {
			send = BLACK + "" + message.replace("<n>", "<br><br>").replace("<blue>", "").replace("<red>", "RED");
			send = send.replace("!!", "").replace("??", "").replace("%%", "").replace("&&", "");
		}
		player.getPacketDispatch().sendString(crossed ? "<str>" + send + "</str>" : send, JOURNAL_COMPONENT, line);
	}

	/**
	 * Limits the quest log scroll to the number of lines minus 9.
	 * Assumes that you start at line = 11 or line = 12.
	 * Call this function at the end of the drawJournal function like: limitScroll(player, line);
	 * @param player The player.
	 * @param line The number of lines to scroll. Due to sendRunScript, it handles less than 12 lines pretty well.
	 * @param startFromTop Whether to open the log at the top, defaults to opening the log at the very bottom.
	 */
	public void limitScrolling(Player player, int line, boolean startFromTop) {
		// sendRunScript reverses the objects you pass in
		// (args1: 0 is to start from bottom of scroll) (args0: child-12 lines to display)
		player.getPacketDispatch().sendRunScript(1207, "ii", startFromTop ? 1 : 0, line - 9); // -9 to give text some padding instead of line - 11 or 12
	}

	/**
	 * Draws text on the quest reward component.
	 * @param player The player.
	 * @param string The string to draw.
	 * @param line The line number to draw on.
	 */
	public void drawReward(Player player, final String string, final int line) {
		player.getPacketDispatch().sendString(string, REWARD_COMPONENT, line);
	}
	
	/**
	 * Sets the player instanced stage.
	 * @param player The player.
	 * @param stage The stage to set.
	 */
	public void setStage(Player player, int stage) {
		player.getQuestRepository().setStage(this, stage);
	}
	
	/**
	 * Gets the config id based on the stage.
	 * @param player The player.
	 * @param stage The stage.
	 * @return The config data.
	 */
	public int[] getConfig(Player player, int stage) {
		if (configs.length < 4) {
			throw new IndexOutOfBoundsException("Quest -> " + quest + " configs array length was not valid. config length = " + configs.length + "!");
		}
		if (configs.length >= 5) {
			// {questVarpId, questVarbitId, valueToSet}
			return new int[] {configs[0], configs[1], stage == 0 ? configs[2] : stage >= 100 ? configs[4] : configs[3]};
		}
		// {questVarpId, valueToSet}
		return new int[] {configs[0], stage == 0 ? configs[1] : stage >= 100 ? configs[3] : configs[2]};
	}

    public void updateVarps(Player player) {
    }
	
	/**
	 * Checks if the quest is in progress.
	 * @param player The player.
	 * @return {@code True} if so.
	 */
	public boolean isStarted(Player player) {
		return getStage(player) > 0  && getStage(player) < 100;
	}
	
	/**
	 * Checks if the quest is completed.
	 * @param player The player.
	 * @return {@code True} if so.
	 */
	public boolean isCompleted(Player player) {
		return getStage(player) >= 100;
	}
	
	/**
	 * Gets the player instanced stage of this quest.
	 * @param player The player.
	 * @return The stage.
	 */
	public int getStage(Player player) {
		return player.getQuestRepository().getStage(this);
	}

	/**
	 * Checks the requirements for the quest.
	 * @param player The player
	 * @return {@code True} if so.
	 */
	public boolean hasRequirements(Player player) {
		return true;
	}

	/**
	 * Gets the name.
	 * @return the name.
	 */
	public Quests getQuest() {
		return quest;
	}

	/**
	 * Gets the index.
	 * @return the index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets the buttonId.
	 * @return the buttonId.
	 */
	public int getButtonId() {
		return buttonId;
	}

	/**
	 * Gets the questPoints.
	 * @return the questPoints.
	 */
	public int getQuestPoints() {
		return questPoints;
	}

	/**
	 * Gets the configs.
	 * @return the configs.
	 */
	public int[] getConfigs() {
		return configs;
	}

	@Override
	public String toString() {
		return "Quest [name=" + quest + ", index=" + index + ", buttonId=" + buttonId + ", questPoints=" + questPoints + ", configs=" + Arrays.toString(configs) + "]";
	}

}
