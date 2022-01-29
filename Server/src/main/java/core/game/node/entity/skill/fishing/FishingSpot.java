package core.game.node.entity.skill.fishing;

import java.util.HashMap;

/**
 * Represents a fishing spot.
 * @author ceikry
 */
public enum FishingSpot {

	NET_BAIT(new int[] { 952, 316, 319, 320, 323, 325, 326, 327, 330, 332, 404, 1331, 2724, 4908, 7045 }, FishingOption.SMALL_NET, FishingOption.BAIT),
	LURE_BAIT(new int[] { 309, 310, 311, 314, 315, 317, 318, 328, 329, 331, 403, 927, 1189, 1190, 3019 }, FishingOption.LURE, FishingOption.L_BAIT), 
	CAGE_HARPOON(new int[] { 312, 321, 324, 333, 405, 1332, 1399, 3804, 5470, 7046}, FishingOption.CAGE, FishingOption.HARPOON), 
	NET_HARPOON(new int[] { 313, 322, 334, 406, 1191, 1333, 1405, 1406, 3574, 3575, 5471, 7044 }, FishingOption.BIG_NET, FishingOption.N_HARPOON), 
	HARPOON_NET(new int[] { 3848, 3849 }, FishingOption.HARPOON, FishingOption.H_NET, FishingOption.BARB_HARPOON),
	SPOT_MORTMYRE(new int[] {5748, 5749}, FishingOption.MORTMYRE_ROD),
	SPOT_LUMDSAWMP(new int[] {2067, 2068}, FishingOption.LUMBDSWAMP_ROD),
	SPOT_KBWANJI(new int[] {1174}, FishingOption.KBWANJI_NET),
	SPOT_KARAMBWAN(new int[] {1177}, FishingOption.KARAMBWAN_VES),
	BAIT_EELS(new int[] {800}, FishingOption.OILY_FISHING_ROD);

	public static HashMap<Integer,FishingSpot> spotMap = new HashMap<>();

	static{
		FishingSpot[] spots = values();
		for (FishingSpot spot : spots) {
			int[] spotIds = spot.ids;
			for (int spotId : spotIds) {
				spotMap.putIfAbsent(spotId, spot);
			}
		}
	}

	public static FishingSpot forId(int npcId) {
		return spotMap.get(npcId);
	}


	/**
	 * The NPC ids.
	 */
	private final int[] ids;

	/**
	 * The fishing options.
	 */
	private FishingOption options[];

	/**
	 * Constructs a new {@code FishingSpot} {@code Object}.
	 * @param ids The NPC ids.
	 */
	FishingSpot(int[] ids, FishingOption... options) {
		this.ids = ids;
		this.options = options;
	}

	public FishingOption getOptionByName(String name){
		for(FishingOption o : this.options){
			if(o.getName().equals(name)){return o;}
		}
		System.out.println("Unhandled fishing spot option, spot id: " + this.ids[0] + " desired fishing option: " + name);
		return FishingOption.SMALL_NET; //safe, default return.
	}

	/**
	 * Gets the ids.
	 * @return The ids.
	 */
	public int[] getIds() {
		return ids;
	}

	/**
	 * Gets the options.
	 * @return The options.
	 */
	public FishingOption[] getOptions() {
		return options;
	}

}
