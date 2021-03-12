package core.game.node.entity.player.info.login;

/**
 * Represents a type of {@link LoginParser}
 * @author 'Vexia
 * @date July-16-13
 */
public enum LoginType {

	/**
	 * Represents the <strong>Login</strong> type of a default connection.
	 */
	NORMAL_LOGIN(16),

	/**
	 * Represents the <strong>Login</strong> type of a reconnection.
	 */
	RECONNECT_TYPE(18);

	/**
	 * The type of {@link LoginParser}.
	 */
	private int type;

	/**
	 * Constructs a new {@code LoginType} {@code Object}.
	 * @param type the type of login.
	 */
	LoginType(int type) {
		this.setType(type);
	}

	/**
	 * Method used to return the login type from the id sent from the client.
	 * @param type the type.
	 * @return {@link #LoginType(int)}
	 */
	public static LoginType fromType(int type) {
		for (LoginType login : LoginType.values()) {
			if (login.getType() == type) {
				return login;
			}
		}
		return null;
	}

	/**
	 * @return the type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}
}
