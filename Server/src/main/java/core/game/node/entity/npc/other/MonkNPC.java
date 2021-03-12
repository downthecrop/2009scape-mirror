package core.game.node.entity.npc.other;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import rs09.game.node.entity.combat.CombatSwingHandler;
import rs09.game.node.entity.combat.handlers.MeleeSwingHandler;
import core.game.node.entity.npc.AbstractNPC;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Handles a monk NPC.
 * @author Emperor
 */
@Initializable
public final class MonkNPC extends AbstractNPC {

	/**
	 * The monk's combat reward.
	 */
	private static final CombatAction COMBAT = new CombatAction();

	/**
	 * Constructs a new {@code MonkNPC} {@code Object}.
	 */
	public MonkNPC() {
		this(7727, null);
	}

	/**
	 * Constructs a new {@code MonkNPC} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	public MonkNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new MonkNPC(id, location);
	}

	@Override
	public CombatSwingHandler getSwingHandler(boolean swing) {
		return COMBAT;
	}

	@Override
	public int[] getIds() {
		return new int[] { 7727 };
	}

	/**
	 * Handles the combat reward.
	 * @author Emperor
	 */
	private static class CombatAction extends MeleeSwingHandler {

		@Override
		public int swing(Entity entity, Entity victim, BattleState state) {
			if (entity.getSkills().getLifepoints() != entity.getSkills().getMaximumLifepoints() && RandomFunction.randomize(10) < 2) {
				entity.animate(Animation.create(709));
				entity.getSkills().heal(2);
				entity.getProperties().getCombatPulse().setNextAttack(4);
				return -1;
			}
			return super.swing(entity, victim, state);
		}
	}

}
