package core.game.node.entity.player.link.grave;

/**
 * A grave stone type.
 * @author Vexia
 */
public enum GraveType {
	MEMORIAL_PLAQUE(0, 5, 6565, "In memory of @name, who died here."), FLAG(50, 6, 6568, "In memory of @name, who died here."), SMALL(500, 6, 6571, "In loving memory of our dear friend @name, who died in this place @mins ago."), ORNATE(5000, 8, 6574, "In loving memory of our dear friend @name, who died in this place @mins ago."), FRONT_OF_LIFE(50000, 10, 6577, "In your travels, pause awhile to remember @name, who passed away in this spot."), STELE(50000, 10, 6580, "In your travels, pause awhile to remember @name, who passed away in this spot."), SARADOMIN(50000, 10, 6583, "@name, an enlightened severant of Saradomin, perished in this place."), ZAMRAOK(50000, 10, 6586, "@name a most bloodthirsty follower of Zamorak, perished in this place."), GUTHIX(50000, 10, 6589, "@name walked with the Balance of Guthix, perished in this place."), BANDOS(50000, 10, 6592, "@name, a vicious warrior dedicated to Bandos, perished in this place."), ARMADYL(50000, 10, 6595, "@name a follower of the Law of Aramdyl, perished in this place."), MEMORIAL_STONE(50000, 10, 6598, "@name, servant of the Unknown Power, perished in this place."), ANGEL_OF_DEATH(500000, 12, 6601, "Ye frails who gaze upon this sight, forget not the date of @name, once mighty, now surrendered to the inescapable grasp of destiny, Requiescat in pace.");

	/**
	 * The cost of the grave stone.
	 */
	private final int cost;

	/**
	 * The decay time of this gravestone.
	 */
	private final int decay;

	/**
	 * The npc id of this gravestone.
	 */
	private final int npcId;

	/**
	 * The message to display on the grave.
	 */
	private final String message;

	/**
	 * Constructs a new {@code GraveType} {@code Object}.
	 * @param cost the cost.
	 * @param decay the decay.
	 */
	GraveType(final int cost, final int decay, final int npc, final String message) {
		this.cost = cost;
		this.decay = decay;
		this.npcId = npc;
		this.message = message;
	}

	/**
	 * Gets the decay.
	 * @return the decay
	 */
	public int getDecay() {
		return decay;
	}

	/**
	 * Gets the cost.
	 * @return the cost
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * Gets the npcId.
	 * @return the npcId
	 */
	public int getNpcId() {
		return npcId;
	}

	/**
	 * Gets the message.
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

}
