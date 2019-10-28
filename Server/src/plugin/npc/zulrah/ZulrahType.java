package plugin.npc.zulrah;

/**
 * Represents the Zulrah Type.
 * @author Empathy
 *
 */
public enum ZulrahType {
	MAGIC(8622),
	RANGE(8620),
	MELEE(8621),
	JAD(8620); 	
	
	/**
	 * The npc id of a zulrah type.
	 */
	private final int npcId;
	
	/**
	 * Constructs a new {@code ZulrahType} object.
	 * @param npcId The npcId
	 */
	ZulrahType(int npcId) {
		this.npcId = npcId;
	}
	
	/**
	 * @return The npc id.
	 */
	public int getNpcId() {
		return npcId;
	}
}
