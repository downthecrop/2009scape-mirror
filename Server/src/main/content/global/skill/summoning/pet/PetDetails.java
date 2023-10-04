package content.global.skill.summoning.pet;



/**
 * A class containing pet details for a certain pet.
 * @author Emperor
 * @author Player Name
 */
public final class PetDetails {

	/**
	 * The hunger rate.
	 */
	private double hunger = 0.0;

	/**
	 * The growth rate.
	 */
	private double growth = 0.0;

	/**
	 * The individual, an in principle arbitrary integer read off of the item's charge slot.
	 */
	private int individual;

	/**
	 * Constructs a new {@code PetDetails} {@code Object}.
	 * @param growth The growth value.
	 */
	public PetDetails(double growth) {
		this.growth = growth;
	}

	/**
	 * Increases the hunger value by the given amount.
	 * @param amount The amount.
	 */
	public void updateHunger(double amount) {
		hunger += amount;
		if (hunger < 0.0) {
			hunger = 0.0;
		}
	}

	/**
	 * Increases the growth value by the given amount.
	 * @param amount The amount.
	 */
	public void updateGrowth(double amount) {
		growth += amount;
		if (growth < 0.0) {
			growth = 0.0;
		} else if (growth > 100.0) {
			growth = 100.0;
		}
	}

	/**
	 * Gets the hunger.
	 * @return The hunger.
	 */
	public double getHunger() {
		return hunger;
	}

	/**
	 * Gets the growth.
	 * @return The growth.
	 */
	public double getGrowth() {
		return growth;
	}

	/**
	 * Sets the individual.
	 * @param individual The individual to set.
	 */
	public void setIndividual(int individual) {
		this.individual = individual;
	}

	/**
	 * Gets the individual.
	 * @return The individual.
	 */
	public int getIndividual() {
		return individual;
	}
}
