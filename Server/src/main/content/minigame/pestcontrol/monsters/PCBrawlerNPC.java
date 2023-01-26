package content.minigame.pestcontrol.monsters;

import content.minigame.pestcontrol.PestControlSession;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;

/**
 * Handles the pest control brawler NPCs.
 * @author Emperor
 */
public final class PCBrawlerNPC extends AbstractNPC {
	/**
	 * The pest control session.
	 */
	private PestControlSession session;

	/**
	 * Constructs a new {@code PCBrawlerNPC} {@code Object}.
	 */
	public PCBrawlerNPC() {
		super(3772, null);
	}

	/**
	 * Constructs a new {@code PCBrawlerNPC} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	public PCBrawlerNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public void init() {
		super.setAggressive(true);
		super.init();
		super.getDefinition().setCombatDistance(1);
		super.walkRadius = 64;
		getProperties().getCombatPulse().setStyle(CombatStyle.MELEE);
		session = getExtension(PestControlSession.class);
	}

    @Override
    public boolean shouldPreventStacking(Entity mover) {
        return true;
    }

	@Override
	public void onImpact(final Entity entity, BattleState state) {
		super.onImpact(entity, state);
		if (session != null && state != null && entity instanceof Player) {
			int total = 0;
			if (state.getEstimatedHit() > 0) {
				total += state.getEstimatedHit();
			}
			if (state.getSecondaryHit() > 0) {
				total += state.getSecondaryHit();
			}
			session.addZealGained((Player) entity, total);
		}
	}


	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new PCBrawlerNPC(id, location);
	}

	@Override
	public int[] getIds() {
        return new int[] { 3772, 3773, 3774, 3775, 3776 };
	}
}
