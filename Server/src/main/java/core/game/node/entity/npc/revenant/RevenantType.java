package core.game.node.entity.npc.revenant;

/**
 * A revenant type.
 * @author Vexia
 */
public enum RevenantType {
	IMP(8, 6604, 6635, 6655, 6666, 6677, 6697, 6703, 6715), GOBLIN(12, 6605, 6612, 6616, 6620, 6636, 6637, 6638, 6639, 6651, 6656, 6657, 6658, 6667, 6678, 6679, 6680, 6681, 6693, 6698, 6699, 6704, 6705, 6706, 6707, 6716, 6717, 6718, 6719), ICEFIEND(20, 6606, 6621, 6628, 6640, 6659, 6682, 6694, 6708, 6720), PYREFIEND(20, 6622, 6631, 6641, 6660, 6668, 6683, 6709, 6721), HOGOBLIN(20, 6608, 6642, 6661, 6684, 6710, 6722, 6727), VAMPYRE(22, 6613, 6623, 6643, 6652, 6662, 6669, 6671, 6674, 6685, 6695, 6700, 6711, 6723), WEREWOLF(23, 6607, 6609, 6614, 6617, 6625, 6632, 6644, 6663, 6675, 6686, 6701, 6712, 6724, 6728), CYCLOPS(24, 6645, 6687), HELLHOUND(25, 6646, 6688), DEMON(25, 6647, 6689), ORK(31, 6610, 6615, 6618, 6624, 6626, 6629, 6633, 6648, 6653, 6664, 6670, 6672, 6690, 6696, 6702, 6713, 6725, 6729), DARK_BEAST(36, 6649, 6691), KNIGHT(42, 6611, 6619, 6627, 6630, 6634, 6650, 6654, 6665, 6673, 6676, 6692, 6714, 6726, 6730), DRAGON(60, 6998, 6999);

	/**
	 * The max hit.
	 */
	private final int maxHit;

	/**
	 * The ids of the revenant.
	 */
	private final int[] ids;

	/**
	 * Constructs a new {@code RevenantType} {@code Object}
	 * @param maxHit the max hit.
	 * @param ids the ids.
	 */
	private RevenantType(int maxHit, int... ids) {
		this.maxHit = maxHit;
		this.ids = ids;
	}

	/**
	 * Gets the revenant type by the id.
	 * @param id the id.
	 * @return the type.
	 */
	public static RevenantType forId(int id) {
		for (RevenantType type : values()) {
			for (int i : type.getIds()) {
				if (i == id) {
					return type;
				}
			}
		}
		return null;
	}

	/**
	 * Gets the ids.
	 * @return the ids.
	 */
	public int[] getIds() {
		return ids;
	}

	/**
	 * Gets the maxHit.
	 * @return the maxHit
	 */
	public int getMaxHit() {
		return maxHit;
	}
}
