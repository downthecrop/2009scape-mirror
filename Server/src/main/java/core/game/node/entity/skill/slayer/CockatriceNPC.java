package core.game.node.entity.skill.slayer;

import core.game.node.entity.combat.BattleState;
import rs09.game.node.entity.combat.CombatSwingHandler;
import core.game.node.entity.npc.AbstractNPC;
import core.game.world.map.Location;
import core.plugin.Initializable;

/**
 * Handles a cockatrice npc.
 * @author Vexia
 */
@Initializable
public final class CockatriceNPC extends AbstractNPC {

	/**
	 * Constructs a new {@code CockatriceNPC} {@code Object}.
	 */
	public CockatriceNPC() {
		super(0, null);
	}

	/**
	 * Constructs a new {@code CockatriceNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	public CockatriceNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new CockatriceNPC(id, location);
	}

	@Override
	public CombatSwingHandler getSwingHandler(boolean swing) {
		return MirrorShieldHandler.SINGLETON;
	}

	@Override
	public void checkImpact(BattleState state) {
		super.checkImpact(state);
		MirrorShieldHandler.SINGLETON.checkImpact(state);
	}

	@Override
	public int[] getIds() {
		return new int[] { 1620, 1621 };
	}

}
