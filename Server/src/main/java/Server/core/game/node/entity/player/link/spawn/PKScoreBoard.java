package core.game.node.entity.player.link.spawn;

import core.cache.ServerStore;
import core.cache.StoreFile;
import core.cache.misc.buffer.ByteBufferUtils;
import core.game.component.Component;
import core.game.node.entity.player.Player;
import core.tools.StringUtils;

import java.nio.ByteBuffer;

/**
 * The score board.
 * @author Emperor
 * @author Vexia
 *
 */
public final class PKScoreBoard {
	
	/**
	 * If it's been initialized.
	 */
	private static boolean initialized;

	/**
	 * The score board size.
	 */
	private static final int SIZE = 49;

	/**
	 * The names.
	 */
	private static String[] names = new String[SIZE];
	
	/**
	 * The scores.
	 */
	private static int[] scores = new int[SIZE];
	
	/**
	 * static block
	 */
	static {
		for (int i = 0; i < SIZE; i++) {
			names[i] = "Nobody yet";
		}
		init();
		initialized = true;
	}

	/**
	 * Initializes the score boards data.
	 */
	public static void init() {
		StoreFile file = ServerStore.get("pk_scores");
		if (file == null) { //Indicates no cache exists yet.
			return;
		}
		ByteBuffer buffer = file.data();
		for (int i = 0; i < SIZE; i++) {
			scores[i] = buffer.getInt();
			names[i] = ByteBufferUtils.getString(buffer);
		}
	}
	
	/**
	 * Updates the store file for the scores.
	 */
	public static void update() {
		ByteBuffer buffer = ByteBuffer.allocate(5000);
		for (int i = 0; i < SIZE; i++) {
			buffer.putInt(scores[i]);
			ByteBufferUtils.putString(names[i], buffer);
		}
		buffer.flip();
		ServerStore.setArchive("pk_scores", buffer);
	}
	
	/**
	 * Opens the score board.
	 * @param player The player.
	 */
	public static void open(Player player) {
		if (!initialized) {
			initialized = true;
			init();
		} 
		int component = 632;
		player.getPacketDispatch().sendString("PK Scoreboard", 632, 14);
		player.getPacketDispatch().sendString("Top 50 (name - kills):", 632, 13);
		player.getInterfaceManager().open(new Component(component));
		String msg = "";
		for (int i = 0; i < SIZE; i++) {
			msg = names[i];
			if (!msg.equals("Nobody yet")) {
				msg = StringUtils.formatDisplayName(msg) + " - " + scores[i];
			}
			player.getPacketDispatch().sendString(msg, component, 16 + i); 
		}
	}
	
	/**
	 * Checks if the ratings of the player is good enough for the score board.
	 * @param player The player.
	 */
	public static void check(Player player) {
		if(player.isAdmin()) {
			return;
		}
		if (!initialized) {
			initialized = true;
			init();
		}
		int score = player.getSavedData().getSpawnData().getKills();
		int myScore = 0;
		for (int i = 0; i < names.length; i++) {
			if (names[i].equals(player.getName()) || names[i].equals(player.getName())) {
				myScore = scores[i];
				break;
			}
		}
		if (score == myScore) {
			return;
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
	 * Resets the board.
	 */
	public static void reset() {
		scores = new int[SIZE];
		names = new String[SIZE];
		for (int i = 0; i < names.length; i++) {
			names[i] = "Nobody yet";
		}
	}

	/**
	 * Inserts the player in the score board.
	 * @param player The player.
	 * @param score The score.
	 * @param index The board index.
	 */
	private static void insert(Player player, int score, int index) {
		if (scores[index] == score) {
			return;
		}
		if (names[index].equals(player.getName()) || names[index].equals(player.getName())) {
			scores[index] = score;
			return;
		}
		for (int i = SIZE - 2; i >= index; i--) {
			String name = names[i];
			if (name.equals(player.getName()) || name.equals(player.getName())) {
				name = names[--i];
			}
			scores[i + 1] = scores[i];
			names[i + 1] = name;
		}
		names[index] = player.getName();
		scores[index] = score;
	}
	
}