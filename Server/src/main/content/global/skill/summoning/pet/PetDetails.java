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
	 * Sets the hunger. (You probably want to use updateHunger() instead.)
	 */
	public void setHunger(double value) {
		this.hunger = value;
	}

	/**
	 * Gets the growth.
	 * @return The growth.
	 */
	public double getGrowth() {
		return growth;
	}

	/**
	 * Sets the growth. (You probably want to use updateGrowth() instead.)
	 */
	public void setGrowth(double value) {
		this.growth = value;
	}
}
