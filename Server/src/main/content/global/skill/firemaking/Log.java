package content.global.skill.firemaking;

import java.util.HashMap;

/**
 * Represents an enumeration of burnable logs.
 * @author 'Vexia
 */
public enum Log {
	NORMAL(1511, 1, 180, 2732, 40, 21),
	ACHEY(2862, 1, 180, 2732, 40, 21),
	OAK(1521, 15, 200, 2732, 60, 35),
	WILLOW(1519, 30, 250, 2732, 90, 50),
	TEAK(6333, 35, 300, 2732, 105, 55),
	ARCTIC_PINE(10810, 42, 500, 2732, 125, 62),
	MAPLE(1517, 45, 300, 2732, 135, 65),
	MAHOGANY(6332, 50, 300, 2732, 157.5, 70),
	EUCALYPTUS(12581, 58, 300, 2732, 193.5, 78),
	YEW(1515, 60, 400, 2732, 202.5, 80),
	MAGIC(1513, 75, 450, 2732, 303.8, 95),
	CURSED_MAGIC(13567, 82, 650, 2732, 303.8, -1),
	PURPLE(10329, 1, 200, 20001, 50, 21),
	WHITE(10328, 1, 200, 20000, 50, 21),
	BLUE(7406, 1, 200, 11406, 50, 21),
	GREEN(7405, 1, 200, 11405, 50, 21),
	RED(7404, 1, 200, 11404, 50, 21),
	JOGRE(3125, 1, 200, 3862, 50, -1);

	public static HashMap<Integer,Log> logMap = new HashMap<>();
	static{
		Log[] logArray = Log.values();
		int logLength = logArray.length;
		for(int i = 0; i < logLength; i++){
			Log log = logArray[i];
			logMap.putIfAbsent(log.logId,log);
		}
	}
	
	/**
	 * The log id.
	 */
	private final int logId;

	/**
	 * The level.
	 */
	private final int level;

	/**
	 * The life.
	 */
	private final int life;

	/**
	 * The fire id.
	 */
	private final int fireId;

	/**
	 * The exp gained.
	 */
	private final double xp;

	/**
	 * The required level for bow-firemaking.
	 */
	private final int barbLevel;

	/**
	 * Constructs a new {@code FireMakingDefinitions.java} {@code Object}.
	 *
	 * @param logId     the log id.
	 * @param level     the level.
	 * @param life      the life.
	 * @param fireId    the fire id.
	 * @param xp        the experience.
	 * @param barbLevel the required level for bow-firemaking.
	 */
	Log(int logId, int level, int life, int fireId, double xp, int barbLevel) {
		this.logId = logId;
		this.level = level;
		this.life = life;
		this.fireId = fireId;
		this.xp = xp;
		this.barbLevel = barbLevel;
	}

	/**
	 * Gets the logId.
	 * @return The logId.
	 */
	public int getLogId() {
		return logId;
	}

	/**
	 * Gets the level.
	 * @return The level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gets the life.
	 * @return The life.
	 */
	public int getLife() {
		return life;
	}

	/**
	 * Gets the fireId.
	 * @return The fireId.
	 */
	public int getFireId() {
		return fireId;
	}

	/**
	 * Gets the xp.
	 * @return The xp.
	 */
	public double getXp() {
		return xp;
	}

	/**
	 * Gets the required level for bow-firemaking.
	 * @return The required level for bow-firemaking.
	 */
	public int getBarbLevel() {
		return barbLevel;
	}

	/**
	 * Gets the log by the id.
	 * @param id the id.
	 * @return the log.
	 */
	public static Log forId(int id) {
		return logMap.get(id);
	}
}