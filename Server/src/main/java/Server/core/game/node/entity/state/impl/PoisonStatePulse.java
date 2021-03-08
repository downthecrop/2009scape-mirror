package core.game.node.entity.state.impl;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.ImpactHandler;
import core.game.node.entity.player.Player;
import core.game.node.entity.state.StatePulse;

import java.nio.ByteBuffer;

/**
 * Handles the poisoned state.
 * @author Emperor
 */
public final class PoisonStatePulse extends StatePulse {

	/**
	 * The current amount of poison damage.
	 */
	private int damage;

	/**
	 * The entity poisoning this entity.
	 */
	private Entity poisoner;

	/**
	 * Constructs a new {@code PoisonStatePulse} {@code Object}.
	 * @param entity The entity.
	 */
	public PoisonStatePulse(Entity entity) {
		super(entity, 30);
	}

	@Override
	public boolean canRun(Entity entity) {
		return !entity.isPoisonImmune();
	}

	@Override
	public void start() {
		super.start();
		if (entity instanceof Player) {
			((Player) entity).getPacketDispatch().sendMessage("<col=990000>You have been poisoned!</col>");
		}
	}

	@Override
	public boolean pulse() {
		if (!poisoner.isActive()) {
			poisoner = entity;
		}
		if (damage / 10 > 0) {
			entity.getImpactHandler().manualHit(poisoner, damage / 10, ImpactHandler.HitsplatType.POISON);
		}
		damage -= 2;
		if (damage < 10) {
			if (entity instanceof Player) {
				((Player) entity).getPacketDispatch().sendMessage("The poison has wore off.");
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean isSaveRequired() {
		return damage > 9;
	}

	@Override
	public void save(ByteBuffer buffer) {
		buffer.put((byte) damage);
	}

	@Override
	public StatePulse parse(Entity entity, ByteBuffer buffer) {
		return create(entity, buffer.get() & 0xFF, entity);
	}

	@Override
	public StatePulse create(Entity entity, Object... args) {
		PoisonStatePulse pulse = new PoisonStatePulse(entity);
		pulse.damage = (Integer) args[0];
		pulse.poisoner = (Entity) args[1];
		return pulse;
	}

	/**
	 * Gets the damage.
	 * @return The damage.
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * Sets the damage.
	 * @param damage The damage to set.
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * Gets the poisoner.
	 * @return The poisoner.
	 */
	public Entity getPoisoner() {
		return poisoner;
	}

	/**
	 * Sets the poisoner.
	 * @param poisoner The poisoner to set.
	 */
	public void setPoisoner(Entity poisoner) {
		this.poisoner = poisoner;
	}

}