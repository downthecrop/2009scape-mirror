package core.game.content.ttrail;

import core.game.component.Component;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import rs09.game.system.SystemLogger;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.plugin.Plugin;
import core.tools.RandomFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a clue scroll plugin.
 * @author Vexia
 */
public abstract class ClueScrollPlugin extends MapZone implements Plugin<Object> {

	/**
	 * The wilderness cape ids.
	 */
	protected static final int[] WILDY_CAPES = new int[] { 4315, 4316, 4317, 4318, 4319, 4320, 4321, 4322, 4323, 4324, 4325, 4326, 4327, 4328, 4329, 4330, 4331, 4332, 4333, 4334, 4335, 4336, 4337, 4338, 4339, 4340, 4341, 4342, 4343, 4344, 4345, 4346, 4347, 4348, 4349, 4350, 4351, 4352, 4353, 4354, 4355, 4356, 4357, 4358, 4359, 4360, 4361, 4362, 4363, 4364, 4365, 4366, 4367, 4368, 4369, 4370, 4371, 4372, 4373, 4374, 4375, 4376, 4377, 4378, 4379, 4380, 4381, 4382, 4383, 4384, 4385, 4386, 4387, 4388, 4389, 4390, 4391, 4392, 4393, 4394, 4395, 4396, 4397, 4398, 4399, 4400, 4401, 4402, 4403, 4404, 4405, 4406, 4407, 4408, 4409, 4410, 4411, 4412, 4413, 4414, 10638 };

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
	 * @param casket if we give a ckaset.
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
		if (!player.getTreasureTrailManager().hasTrail() || player.getTreasureTrailManager().hasTrail() && clue.getId() != player.getTreasureTrailManager().getClueId()) {
			player.getTreasureTrailManager().startTrail(this);
		}
		int currentStage = player.getTreasureTrailManager().getTrailStage();
		if (currentStage >= player.getTreasureTrailManager().getTrailLength()) {
			player.getTreasureTrailManager().clearTrail();
		} else {
			player.getTreasureTrailManager().incrementStage();
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
			SystemLogger.logErr("Error! Plugin already registered with clue id - " + clue.getClueId() + ", trying to register " + clue.getClass().getCanonicalName() + " the real plugin using the id is " + CLUE_SCROLLS.get(clue.getClueId()).getClass().getCanonicalName() + "!");
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
			SystemLogger.logErr("Error! There are no clues for level " + clueLevel + "!");
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
