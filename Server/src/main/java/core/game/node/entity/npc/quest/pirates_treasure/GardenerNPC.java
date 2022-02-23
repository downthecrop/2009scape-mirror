package core.game.node.entity.npc.quest.pirates_treasure;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.plugin.Initializable;
import core.game.world.map.Location;

/**
 * Represents the abstract garden npc used for pirates treasure.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class GardenerNPC extends AbstractNPC {

	/**
	 * The NPC ids of NPCs using this plugin.
	 */
	private static final int[] ID = { 1217 };

	/**
	 * Constructs a new {@code GardenerNPC} {@code Object}.
	 */
	public GardenerNPC() {
		super(0, null, true);
	}

	/**
	 * Constructs a new {@code RomeoNPC} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	private GardenerNPC(int id, Location location) {
		super(id, location, true);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new GardenerNPC(id, location);
	}

	@Override
	public void tick() {
		super.tick();
		if (getAttribute("target", null) != null) {
			final Player target = (Player) getAttribute("target", null);
			if (getProperties().getCombatPulse().isRunning()) {
				getProperties().getCombatPulse().attack(target);
			}
			if (!target.isActive() || target.getLocation().getDistance(getLocation()) > 16) {
				GameWorld.getPulser().submit(new Pulse(2) {
					@Override
					public boolean pulse() {
						clear();
						return true;
					}
				});
			}
		}
	}

	@Override
	public boolean isAttackable(Entity entity, CombatStyle style, boolean message) {
		final Player target = getAttribute("target", null);
		if (target != entity) {
			return false;
		}
		return super.isAttackable(entity, style, message);
	}

	@Override
	public void finalizeDeath(final Entity killer) {
		final Player target = getAttribute("target", null);
		if (target != null && target == killer) {
			target.getSavedData().getQuestData().setGardenerAttack(true);
		}
		;
		super.finalizeDeath(killer);
	}

	@Override
	public int[] getIds() {
		return ID;
	}

}
