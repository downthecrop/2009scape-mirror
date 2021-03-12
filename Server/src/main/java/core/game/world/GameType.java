package core.game.world;


/**
 * Represents the game type.
 * @author Vexia
 *
 */
public enum GameType {
	ECONOMY,
	SPAWN;
	
	/**
	 * Parses the game type.
	 * @param string the string.
	 * @return {@code GameType} the type.
	 */
	public static GameType parse(String string) {
		return Boolean.parseBoolean(string) ? ECONOMY : SPAWN;
	}

	/**
	 * Gets a game type for the name.
	 * @param property the property.
	 * @return the type.
	 */
	public static GameType forName(String property) {
		if (property == null) {
			return null;
		}
		return property.equals("economy") ? ECONOMY : property.equals("spawn") ? SPAWN : null;
	}
	
}