package plugin.npc.bosses.kraken;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.CombatPulse;
import org.crandor.game.node.entity.combat.CombatStyle;
import org.crandor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.node.entity.npc.IdleAbstractNPC;
import org.crandor.game.system.task.Pulse;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.Plugin;

/**
 * Handles the Kraken's enormous tentacle.
 * @author Emperor
 *
 */
public final class EnormousTentacleNPC extends IdleAbstractNPC {

	/**
	 * The kraken NPC.
	 */
	protected KrakenNPC kraken;
	
	/**
	 * Constructs a new {@code KrakenNPC} {@code Object}.
	 */
	public EnormousTentacleNPC() {
		this(8615, Location.create(3691, 5809, 0));
	}
	
	/**
	 * Constructs a new {@code EnormousTentacleNPC} {@code Object}.
	 * @param npcId The active NPC id.
	 * @param location The location.
	 */
	public EnormousTentacleNPC(int npcId, Location location) {
		super(8617, npcId, location);
	}
	
	@Override
	public void init() {
		super.init();
		configureBossData();
		setRespawn(false);
	}
	
	@Override
	public boolean isIgnoreProtection(CombatStyle style) {
		return true;
	}
	
	@Override
	public void handleTickActions() {
		boolean i = isIdle();
		if (kraken != null && !kraken.isIdle()) {
			setIdle(true);
		}
		super.handleTickActions();
		setIdle(i);
	}
	
	@Override
	public boolean inDisturbingRange(final Entity disturber) {
		if (!super.inDisturbingRange(disturber)) {
			return false;
		}
		CombatPulse.taskSwing(disturber, this, false, new Pulse(0) {
			@Override
			public boolean pulse() {
				EnormousTentacleNPC.this.disturb(disturber);
				return true;
			}
		});
		return true;
	}
	
	@Override
	public boolean canDisturb(Entity disturber) {
		return false;
	}

	@Override
	public void disturb(final Entity disturber) {
		getImpactHandler().manualHit(disturber, 1, HitsplatType.NORMAL);
		super.disturb(disturber);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new EnormousTentacleNPC(id, location);
	}
	
	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		return super.newInstance(arg);
	}

	@Override
	public int[] getIds() {
		return new int[] { 8615, 8617 };
	}

}