package core.game.content.activity.bountyhunter;

import java.nio.ByteBuffer;

import core.cache.misc.buffer.ByteBufferUtils;
import core.game.component.Component;
import core.game.node.entity.player.Player;
import core.tools.StringUtils;

/**
 * The score board.
 * @author Emperor
 */
public final class BHScoreBoard {

	/**
	 * The score board size.
	 */
	private static final int SIZE = 10;

	/**
	 * The names.
	 */
	private String[] names = new String[SIZE];

	/**
	 * The scores.
	 */
	private int[] scores = new int[SIZE];

	/**
	 * The bounty hunters scoreboard.
	 */
	private static final BHScoreBoard HUNTERS = new BHScoreBoard();

	/**
	 * The bounty hunter rogues scoreboard.
	 */
	private static final BHScoreBoard ROGUES = new BHScoreBoard();

	/**
	 * Constructs a new {@code BHScoreBoard} {@code Object}.
	 */
	public BHScoreBoard() {
		for (int i = 0; i < SIZE; i++) {
			names[i] = "Nobody yet";
		}
	}

	/**
	 * Initializes the score boards data.
	 */
	public static void init() {
/*		if (true) { // Indicates no cache exists yet.
			return;
		}
		ByteBuffer buffer = file.data();
		for (int i = 0; i < SIZE; i++) {
			HUNTERS.scores[i] = buffer.getInt();
			HUNTERS.names[i] = ByteBufferUtils.getString(buffer);
		}
		for (int i = 0; i < SIZE; i++) {
			ROGUES.scores[i] = buffer.getInt();
			ROGUES.names[i] = ByteBufferUtils.getString(buffer);
		}*/
	}

	/**
	 * Updates the store file for the scores.
	 */
	public static void update() {
		ByteBuffer buffer = ByteBuffer.allocate(500);
		for (int i = 0; i < SIZE; i++) {
			buffer.putInt(HUNTERS.scores[i]);
			ByteBufferUtils.putString(HUNTERS.names[i], buffer);
		}
		for (int i = 0; i < SIZE; i++) {
			buffer.putInt(ROGUES.scores[i]);
			ByteBufferUtils.putString(ROGUES.names[i], buffer);
		}
		buffer.flip();
		//AriosStore.setArchive("bh_scores", buffer);
	}

	/**
	 * Opens the score board.
	 * @param player The player.
	 */
	public void open(Player player) {
		int component = 654;
		if (this == ROGUES) {
			component = 655;
		}
		player.getInterfaceManager().open(new Component(component));
		for (int i = 0; i < SIZE; i++) {
			player.getPacketDispatch().sendString(StringUtils.formatDisplayName(names[i]), component, 15 + i);
			player.getPacketDispatch().sendString(Integer.toString(scores[i]), component, 25 + i);
		}
	}

	/**
	 * Checks if the ratings of the player is good enough for the score board.
	 * @param player The player.
	 */
	public void check(Player player) {
		int score = player.getSavedData().getActivityData().getBountyHunterRate();
		if (this == ROGUES) {
			score = player.getSavedData().getActivityData().getBountyRogueRate();
		}
		for (int i = 0; i < SIZE; i++) {
			if (score > scores[i]) {
				insert(player, score, i);
				update();
				break;
			}
		}
	}

	/**
	 * Inserts the player in the score board.
	 * @param player The player.
	 * @param score The score.
	 * @param index The board index.
	 */
	private void insert(Player player, int score, int index) {
		if (names[index].equals(player.getName())) {
			scores[index] = score;
			return;
		}
		for (int i = SIZE - 2; i >= index; i--) {
			String name = names[i];
			if (name.equals(player.getName())) {
				name = names[--i];
			}
			scores[i + 1] = scores[i];
			names[i + 1] = name;
		}
		names[index] = player.getName();
		scores[index] = score;
	}

	/**
	 * Gets the rogues.
	 * @return The rogues.
	 */
	public static BHScoreBoard getRogues() {
		return ROGUES;
	}

	/**
	 * Gets the hunters.
	 * @return The hunters.
	 */
	public static BHScoreBoard getHunters() {
		return HUNTERS;
	}

}