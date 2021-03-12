package core.game.content.eco;

/**
 * The several statuses used for the economy.
 * @author Emperor
 */
public enum EcoStatus {

	/**
	 * The status set when money should be drained from the economy.
	 */
	DRAINING,

	/**
	 * The status set when money should be pumped into the economy.
	 */
	BOOSTING,

	/**
	 * If we are maintaining the current economy.
	 */
	MAINTAINING;

}