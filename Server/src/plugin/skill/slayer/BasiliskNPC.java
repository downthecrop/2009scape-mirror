package plugin.skill.slayer;

import org.crandor.game.content.skill.member.slayer.MirrorShieldHandler;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.combat.CombatSwingHandler;
import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;

/**
 * Handles the basilisk npc.
 * @author Vexia
 */
@InitializablePlugin
public final class BasiliskNPC extends AbstractNPC {

	/**
	 * Constructs a new {@code BasiliskNPC} {@code Object}.
	 */
	public BasiliskNPC() {
		super(0, null);
	}

	/**
	 * Constructs a new {@code BasiliskNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	public BasiliskNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new BasiliskNPC(id, location);
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
		return new int[] { 1616, 1617 };
	}

}
