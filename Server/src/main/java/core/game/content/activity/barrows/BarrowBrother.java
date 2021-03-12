package core.game.content.activity.barrows;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.DeathTask;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.HintIconManager;
import core.game.world.map.Location;
import core.tools.RandomFunction;

/**
 * Handles a barrow brother NPC.
 * @author Emperor
 */
public final class BarrowBrother extends NPC {

	/**
	 * The player to target.
	 */
	private final Player player;

	/**
	 * Constructs a new {@code BarrowBrother} {@code Object}.
	 * @param player The target.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	public BarrowBrother(Player player, int id, Location location) {
		super(id, location);
		this.player = player;
	}

	@Override
	public void init() {
		super.init();
		super.setRespawn(false);
		if (location.getZ() == 3) {
			sendChat("You dare disturb my rest!");
		} else {
			sendChat("You dare steal from us!");
		}
		getProperties().getCombatPulse().attack(player);
		HintIconManager.registerHintIcon(player, this);
	}

	@Override
	public void handleTickActions() {
		if (DeathTask.isDead(player)) {
			return;
		}
		if (!player.isActive() || !player.getLocation().withinDistance(location)) {
			clear();
			return;
		}
		if (!getProperties().getCombatPulse().isAttacking()) {
			getProperties().getCombatPulse().attack(player);
		}
	}

	@Override
	public void sendImpact(BattleState state) {
		int maxHit = 0;
		switch (getId()) {
		case 2025:// ahrim
			maxHit = 25;
			break;
		case 2026:// dharok
			maxHit = 60;
			break;
		case 2027:// guthan
			maxHit = 24;
			break;
		case 2028:// karil
			maxHit = 20;
			break;
		case 2029:// torag
			maxHit = 23;
			break;
		case 2030:// verac
			maxHit = 25;
			break;
		}
		if (state.getEstimatedHit() > maxHit) {
			state.setEstimatedHit(RandomFunction.random(maxHit - 10, maxHit));
		}
		if (state.getSecondaryHit() > maxHit) {
			state.setSecondaryHit(RandomFunction.random(maxHit - 10, maxHit));
		}
	}

	@Override
	public void finalizeDeath(Entity killer) {
		super.finalizeDeath(killer);
		if (killer == player) {
			player.getSavedData().getActivityData().getBarrowBrothers()[getBrotherIndex()] = true;
			BarrowsActivityPlugin.sendConfiguration(player);
		}
	}

	@Override
	public void clear() {
		super.clear();
		if (player.isActive()) {
			player.getHintIconManager().clear();
		}
		player.removeAttribute("barrow:npc");
		player.removeAttribute("brother:" + getBrotherIndex());
	}

	/**
	 * Gets the barrow brother index.
	 * @return The index.
	 */
	private int getBrotherIndex() {
		return getId() - 2025;
	}

	/**
	 * Gets the player.
	 * @return The player.
	 */
	public Player getPlayer() {
		return player;
	}
}