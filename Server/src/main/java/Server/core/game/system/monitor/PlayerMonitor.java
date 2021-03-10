package core.game.system.monitor;

import core.cache.misc.buffer.ByteBufferUtils;
import core.game.node.entity.player.Player;


import java.nio.ByteBuffer;

/**
 * Handles the player monitoring.
 * @author Emperor
 */
public final class PlayerMonitor {

	/**
	 * The public chat log.
	 */
	public static final int PUBLIC_CHAT_LOG = 0;

	/**
	 * The private chat log.
	 */
	public static final int PRIVATE_CHAT_LOG = 1;

	/**
	 * The clan chat log.
	 */
	public static final int CLAN_CHAT_LOG = 2;

	/**
	 * The IP/MAC-address log.
	 */
	public static final int ADDRESS_LOG = 3;

	/**
	 * The command log.
	 */
	public static final int COMMAND_LOG = 4;

	/**
	 * The trade log.
	 */
	public static final int TRADE_LOG = 5;

	/**
	 * The GE logs.
	 */
	public static final int GRAND_EXCHANGE_LOG = 6;

	/**
	 * The duel stake logs.
	 */
	public static final int DUEL_LOG = 7;

	/**
	 * The macro flag for a click when the client was out of focus.
	 */
	public static final int MF_NO_FOCUS_CLICK = 0x1;

	/**
	 * The networth of the player.
	 */
	private long networth;

	/**
	 * If the client is currently focused.
	 */
	private boolean clientFocus = true;

	/**
	 * How likely it is this player has used a macro.
	 */
	private int macroFlag;

	/**
	 * The duplication log.
	 */
	private DuplicationLog duplicationLog;

	/**
	 * The message monitors.
	 */
	private MessageLog[] logs = new MessageLog[8];

	/**
	 * Constructs a new {@code PlayerMonitor} {@code Object}.
	 */
	public PlayerMonitor() {
		logs[PUBLIC_CHAT_LOG] = new MessageLog(500);
		logs[PRIVATE_CHAT_LOG] = new MessageLog(500);
		logs[CLAN_CHAT_LOG] = new MessageLog(500);
		logs[ADDRESS_LOG] = new MessageLog(200);
		logs[COMMAND_LOG] = new MessageLog(200);
		logs[TRADE_LOG] = new MessageLog(200);
		logs[GRAND_EXCHANGE_LOG] = new MessageLog(500);
		logs[DUEL_LOG] = new MessageLog(200);
	}

	/**
	 * Clears the logs.
	 */
	public void clear() {
		for (int i = 0; i < logs.length; i++) {
			if (logs[i] == null) {
				continue;
			}
			logs[i].getMessages().clear();
		}
		duplicationLog.getMessages().clear();
	}

	/**
	 * If the player's actions should be logged.
	 * @return {@code True} if so.
	 */
	public boolean isLogActions() {
		return duplicationLog != null && duplicationLog.isLoggingFlagged();
	}

	/**
	 * Logs the given string.
	 * @param string The string to log.
	 * @param type The log type.
	 */
	public void log(String string, int type) {
		log(string, type, true);
	}

	/**
	 * Logs the given string.
	 * @param string The string to log.
	 * @param type The log type.
	 * @param timeStamp If we should time stamp the logged string.
	 */
	public void log(String string, int type, boolean timeStamp) {
		if (type < 3) {
			String check = string.toLowerCase();
			if (check.contains("dupe") || check.contains("duping") || check.contains("bug") || check.contains("glitch") || check.contains("exploit")) {
				getDuplicationLog().flag(DuplicationLog.DUPE_TALK);
				String prefix = null;
				if (type == PUBLIC_CHAT_LOG) {
					prefix = "Public";
				} else if (type == PRIVATE_CHAT_LOG) {
					prefix = "Private";
				} else {
					prefix = "Clan";
				}
				getDuplicationLog().log(prefix + " chat message: " + string, true);
			}
		}
		logs[type].log(string, timeStamp);
	}

	/**
	 * Checks the networth difference for a player (called on logout).
	 * @param player The player.
	 * @param currentNet The current networth.
	 */
	public void checkNetworth(Player player, long currentNet) {
		long difference = currentNet - this.networth;
		if (difference < 1) { // The player lost money.
			return;
		}
		if (difference > 150_000_000l) {
			getDuplicationLog().flag(DuplicationLog.NW_INCREASE);
			getDuplicationLog().log("Large networth increase - [incr=" + difference + ", old=" + this.networth + ", cur=" + currentNet + "].", true);
		}
	}

	/**
	 * Called when the player does a mouse click.
	 * @param x The x-coordinate of the cursor.
	 * @param y The y-coordinate of the cursor.
	 * @param delay The time between this click and last.
	 * @param rightClick If the right mouse-button was used to click.
	 */
	public void handleMouseClick(int x, int y, int delay, boolean rightClick) {
		if (!clientFocus) {
			macroFlag |= MF_NO_FOCUS_CLICK;
		}
	}
	
	/**
	 * Gets the address log.
	 * @param original the original log.
	 * @param address the address.
	 * @return the address.
	 */
	public static String getAddressLog(String original, String address) {
		String log = "";
		if (original != null && original.length() > 0) {
			log += original;
			if (log.charAt(log.length() - 1) != '|') {
				log += "|";
			}
		}
		if (address != null && address.length() > 0 && (original == null || !original.contains(address))) {
			log += address + "|";
		}
		if (log.length() > 0 && log.charAt(log.length() - 1) == '|') {
			log = log.substring(0, log.length() - 1);
		}
		if (log == null) {
			log = "";
		}
		return log;
	}

	/**
	 * Adds a macro flag.
	 * @param flag The flag.
	 */
	public void addMacroFlag(int flag) {
		macroFlag |= flag;
	}

	/**
	 * Gets the macroFlag.
	 * @return The macroFlag.
	 */
	public int getMacroFlag() {
		return macroFlag;
	}

	/**
	 * Sets the macroFlag.
	 * @param macroFlag The macroFlag to set.
	 */
	public void setMacroFlag(int macroFlag) {
		this.macroFlag = macroFlag;
	}

	/**
	 * Gets the networth.
	 * @return The networth.
	 */
	public long getNetworth() {
		return networth;
	}

	/**
	 * Sets the networth.
	 * @param networth The networth to set.
	 */
	public void setNetworth(long networth) {
		this.networth = networth;
	}

	/**
	 * Gets the clientFocus.
	 * @return The clientFocus.
	 */
	public boolean isClientFocus() {
		return clientFocus;
	}

	/**
	 * Sets the clientFocus.
	 * @param clientFocus The clientFocus to set.
	 */
	public void setClientFocus(boolean clientFocus) {
		this.clientFocus = clientFocus;
	}

	/**
	 * Gets the logs.
	 * @return The logs.
	 */
	public MessageLog[] getLogs() {
		return logs;
	}

	/**
	 * Gets the duplicationLog.
	 * @return The duplicationLog.
	 */
	public DuplicationLog getDuplicationLog() {
		if (duplicationLog == null) {
			duplicationLog = new DuplicationLog();
		}
		return duplicationLog;
	}

}