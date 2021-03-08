package core.game.node.entity.skill.summoning.pet;

import core.game.node.entity.player.info.login.SavingModule;

import java.nio.ByteBuffer;

/**
 * A class containing pet details for a certain pet.
 * @author Emperor
 */
public final class PetDetails implements SavingModule {

	/**
	 * The hunger rate.
	 */
	private double hunger = 0.0;

	/**
	 * The growth rate.
	 */
	private double growth = 0.0;

	/**
	 * The current stage of the pet (0 - baby, 1 - grown, 2 - overgrown).
	 */
	private int stage;

	/**
	 * Constructs a new {@code PetDetails} {@code Object}.
	 * @param growth The growth value.
	 */
	public PetDetails(double growth) {
		this.growth = growth;
	}

	@Override
	public void save(ByteBuffer buffer) {
		buffer.putDouble(hunger);
		buffer.putDouble(growth);
		buffer.put((byte) stage);
	}

	@Override
	public void parse(ByteBuffer buffer) {
		hunger = buffer.getDouble();
		growth = buffer.getDouble();
		stage = buffer.get();
	}

	/**
	 * Increases the hunger value by the given amount.
	 * @param amount The amount.
	 */
	public void updateHunger(double amount) {
		hunger += amount;
		if (hunger < 0.0) {
			hunger = 0.0;
		} else if (hunger > 100.0) {
			hunger = 100.0;
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
	 * Gets the stage.
	 * @return The stage.
	 */
	public int getStage() {
		return stage;
	}

	/**
	 * Sets the stage.
	 * @param stage The stage to set.
	 */
	public void setStage(int stage) {
		this.stage = stage;
	}

}