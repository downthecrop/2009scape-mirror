package content.global.activity.ttrail;

import core.game.component.Component;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.tools.Log;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import org.rs09.consts.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static core.api.ContentAPIKt.log;

/**
 * Represents a clue scroll plugin.
 * @author Vexia
 */
public abstract class ClueScrollPlugin extends MapZone implements Plugin<Object> {

	/**
	 * The wilderness cape ids.
	 */
	protected static final int[] WILDY_CAPES = new int[] { Items.TEAM_1_CAPE_4315, Items.TEAM_2_CAPE_4317, Items.TEAM_3_CAPE_4319, Items.TEAM_4_CAPE_4321, Items.TEAM_5_CAPE_4323, Items.TEAM_6_CAPE_4325, Items.TEAM_7_CAPE_4327, Items.TEAM_8_CAPE_4329, Items.TEAM_9_CAPE_4331, Items.TEAM_10_CAPE_4333, Items.TEAM_11_CAPE_4335, Items.TEAM_12_CAPE_4337, Items.TEAM_13_CAPE_4339, Items.TEAM_14_CAPE_4341, Items.TEAM_15_CAPE_4343, Items.TEAM_16_CAPE_4345, Items.TEAM_17_CAPE_4347, Items.TEAM_18_CAPE_4349, Items.TEAM_19_CAPE_4351, Items.TEAM_20_CAPE_4353, Items.TEAM_21_CAPE_4355, Items.TEAM_22_CAPE_4357, Items.TEAM_23_CAPE_4359, Items.TEAM_24_CAPE_4361, Items.TEAM_25_CAPE_4363, Items.TEAM_26_CAPE_4365, Items.TEAM_27_CAPE_4367, Items.TEAM_28_CAPE_4369, Items.TEAM_29_CAPE_4371, Items.TEAM_30_CAPE_4373, Items.TEAM_31_CAPE_4375, Items.TEAM_32_CAPE_4377, Items.TEAM_33_CAPE_4379, Items.TEAM_34_CAPE_4381, Items.TEAM_35_CAPE_4383, Items.TEAM_36_CAPE_4385, Items.TEAM_37_CAPE_4387, Items.TEAM_38_CAPE_4389, Items.TEAM_39_CAPE_4391, Items.TEAM_40_CAPE_4393, Items.TEAM_41_CAPE_4395, Items.TEAM_42_CAPE_4397, Items.TEAM_43_CAPE_4399, Items.TEAM_44_CAPE_4401, Items.TEAM_45_CAPE_4403, Items.TEAM_46_CAPE_4405, Items.TEAM_47_CAPE_4407, Items.TEAM_48_CAPE_4409, Items.TEAM_49_CAPE_4411, Items.TEAM_50_CAPE_4413 };

	/**
	 * The mapping of clue scrolls.
	 */
	private static final Map<Integer, ClueScrollPlugin> CLUE_SCROLLS = new HashMap<>();

	/**
	 * A map of pre organized clue scrolls.
	 */
	private static final Map<ClueLevel, List<ClueScrollPlugin>> ORGANIZED = new HashMap<>();

	/**
	 * The id of the clue scroll.
	 */
	protected final int clueId;

	/**
	 * The clue scroll level.
	 */
	protected final ClueLevel level;

	/**
	 * The interface id of the clue.
	 */
	protected final int interfaceId;

	/**
	 * The zone borders.
	 */
	protected final ZoneBorders[] borders;

	/**
	 * Constructs a new {@Code ClueScrollPlugin} {@Code Object}
	 * @param clueId the id.
	 * @param level the level.
	 * @param interfaceId the id.
	 */
	public ClueScrollPlugin(final String name, final int clueId, ClueLevel level, final int interfaceId, final ZoneBorders... borders) {
		super(name, true);
		this.clueId = clueId;
		this.level = level;
		this.interfaceId = interfaceId;
		this.borders = borders;
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	/**
	 * Rewards the player with a casket.
	 * @param player the player.
	 * @param casket if we give a casket.
	 */
	public void reward(Player player, boolean casket) {
		Item clue = player.getInventory().getItem(new Item(getClueId()));
		if (clue == null) {
			return;
		}
		nextStage(player, clue);
		if (casket) {
			player.getInventory().replace(level.getCasket(), clue.getSlot());
		} else {
			player.getInventory().remove(clue);
		}
	}

	/**
	 * Rewards the player.
	 * @param player the player.
	 */
	public void reward(Player player) {
		reward(player, true);
	}

	/**
	 * Increments the next stage of the clue.
	 * @param player the player.
	 * @param clue the clue.
	 */
	public void nextStage(Player player, Item clue) {
		if (!TreasureTrailManager.getInstance(player).hasTrail() || TreasureTrailManager.getInstance(player).hasTrail() && clue.getId() != TreasureTrailManager.getInstance(player).getClueId()) {
			TreasureTrailManager.getInstance(player).startTrail(this);
		}
		int currentStage = TreasureTrailManager.getInstance(player).getTrailStage();
		if (currentStage >= TreasureTrailManager.getInstance(player).getTrailLength()) {
			TreasureTrailManager.getInstance(player).clearTrail();
		} else {
			TreasureTrailManager.getInstance(player).incrementStage();
		}
	}

	/**
	 * Reads a clue scroll.
	 * @param player the player.
	 */
	public void read(Player player) {
		if (player.getInterfaceManager().isOpened()) {
			player.sendMessage("Please finish what you're doing first.");
			return;
		}
		player.getInterfaceManager().open(new Component(interfaceId));
	}

	/**
	 * Reigsters a clue scroll into the repository.
	 * @param clue the plugin.
	 */
	public void register(ClueScrollPlugin clue) {
		if(clue.getClueId() == 2681)
			return;
		if (CLUE_SCROLLS.containsKey(clue.getClueId())) {
			log(this.getClass(), Log.ERR,  "Error! Plugin already registered with clue id - " + clue.getClueId() + ", trying to register " + clue.getClass().getCanonicalName() + " the real plugin using the id is " + CLUE_SCROLLS.get(clue.getClueId()).getClass().getCanonicalName() + "!");
			return;
		}
		List<ClueScrollPlugin> organized = (List<ClueScrollPlugin>) ORGANIZED.get(clue.getLevel());
		if (organized == null) {
			organized = new ArrayList<>(20);
		}
		organized.add(clue);
		ZoneBuilder.configure(clue);
		CLUE_SCROLLS.put(clue.getClueId(), clue);
		ORGANIZED.put(clue.getLevel(), organized);
	}

	@Override
	public void configure() {		
		if (borders == null) {
			return;
		}
		for (ZoneBorders border : borders) {
			register(border);
		}
	}

	/**
	 * Gets a clue item.
	 * @param clueLevel the level.
	 * @return the item.
	 */
	public static Item getClue(ClueLevel clueLevel) {
		List<ClueScrollPlugin> clues = ORGANIZED.get(clueLevel);
		if (clues == null) {
			log(ClueScrollPlugin.class, Log.ERR, "Error! There are no clues for level " + clueLevel + "!");
			return null;
		}
		ClueScrollPlugin clue = clues.get(RandomFunction.random(clues.size()));
		return new Item(clue.getClueId());
	}

	/**
	 * Checks if the player has equipment.
	 * @param player the player.
	 * @param equipment the equipment.
	 * @return {@code True} if so.
	 */
	public boolean hasEquipment(Player player, int[][] equipment) {
		if (equipment == null || equipment.length == 0) {
			return true;
		}
		int hasAmt = 0;
		for (int i = 0; i < equipment.length; i++) {
			for (int k = 0; k < equipment[i].length; k++) {
				if (player.getEquipment().contains(equipment[i][k], 1)) {
					hasAmt++;
					break;
				}
			}
		}
		return hasAmt == equipment.length;
	}

	/**
	 * Gets the bclueId.
	 * @return the clueId
	 */
	public int getClueId() {
		return clueId;
	}

	/**
	 * Gets the blevel.
	 * @return the level
	 */
	public ClueLevel getLevel() {
		return level;
	}

	/**
	 * Gets the bclueScrolls.
	 * @return the clueScrolls
	 */
	public static Map<Integer, ClueScrollPlugin> getClueScrolls() {
		return CLUE_SCROLLS;
	}

	/**
	 * Gets the binterfaceId.
	 * @return the interfaceId
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

}
