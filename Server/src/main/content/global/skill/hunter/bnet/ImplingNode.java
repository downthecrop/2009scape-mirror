package content.global.skill.hunter.bnet;

import core.game.node.entity.player.Player;
import core.game.node.item.Item;

/**
 * Handles the impling node.
 * @author Vexia
 */
public final class ImplingNode extends BNetNode {

	/**
	 * The respawn time.
	 */
	private final int respawnTime;

	/**
	 * Constructs a new {@code ImplingNode} {@code Object}.
	 * @param npcs the npcs.
	 * @param level the level.
	 * @param exp the exp.
	 * @param puroExp the puro exp.
	 * @param reward the reward.
	 */
	public ImplingNode(int[] npcs, int level, double exp, double puroExp, Item reward, final int respawnTime) {
		super(npcs, new int[] { level }, new double[] { exp, puroExp }, null, reward);
		this.respawnTime = respawnTime;
	}

	/**
	 * Constructs a new {@code ImplingNode} {@code Object}.
	 * @param npcs the npcs.
	 * @param level the level.
	 * @param exp the exp.
	 * @param puroExp the puro exp.
	 * @param reward the reward.
	 */
	public ImplingNode(int[] npcs, int level, double exp, double puroExp, Item reward) {
		this(npcs, level, exp, puroExp, reward, 16);
	}

	@Override
	public void message(Player player, int type, boolean success) {
		if (!success) {
			return;
		}
		if(type == 1){
			player.sendMessage("You manage to catch the impling and squeeze it into a jar.");
		}
	}

	@Override
	public double getExperience(Player player) {
		return player.getZoneMonitor().isInZone("puro puro") ? getExperiences()[1] : super.getExperience(player);
	}

	@Override
	public boolean isBareHand(Player player) {
		return false;
	}

	@Override
	public Item getJar() {
		return IMPLING_JAR;
	}

	/**
	 * Gets the respawnTime.
	 * @return The respawnTime.
	 */
	public int getRespawnTime() {
		return respawnTime;
	}
}
