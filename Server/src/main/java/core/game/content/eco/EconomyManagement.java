package core.game.content.eco;

/**
 * Represents a managing class for the economy.
 * @author Emperor
 */
public final class EconomyManagement {

	/**
	 * The current economy state.
	 */
	private static EcoStatus ecoState = EcoStatus.BOOSTING;

	/**
	 * The modification rate.
	 */
	private static double modificationRate = 10;

	/**
	 * Sets the ecoState.
	 * @param ecoState The ecoState to set.
	 */
	public static void update(EcoStatus ecoState, double modificationRate) {
		boolean update = EconomyManagement.ecoState != ecoState;
		EconomyManagement.ecoState = ecoState;
		if (EconomyManagement.modificationRate != modificationRate) {
			EconomyManagement.modificationRate = modificationRate;
			update = true;
		}
		if (update) {
			System.out.println("-------------------------------------------------------------------");
			System.out.println(" Switched economy management status to " + ecoState + " with a rate of " + modificationRate + " |");
			System.out.println("-------------------------------------------------------------------");
		}
	}

	/**
	 * Sets the ecoState.
	 * @param ecoState The ecoState to set.
	 */
	public static void updateEcoState(EcoStatus ecoState) {
		update(ecoState, modificationRate);
	}

	/**
	 * Sets the modificationRate.
	 * @param modificationRate The modificationRate to set.
	 */
	public static void updateModificationRate(double modificationRate) {
		update(ecoState, modificationRate);
	}

	/**
	 * Gets the ecoState.
	 * @return The ecoState.
	 */
	public static EcoStatus getEcoState() {
		return ecoState;
	}

	/**
	 * Gets the modificationRate.
	 * @return The modificationRate.
	 */
	public static double getModificationRate() {
		return modificationRate;
	}
}