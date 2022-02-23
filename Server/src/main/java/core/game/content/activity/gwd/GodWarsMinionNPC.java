package core.game.content.activity.gwd;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.DeathTask;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import rs09.game.world.GameWorld;
import core.plugin.Initializable;
import core.game.world.map.Location;

/**
 * Handles a god wars minion NPC.
 * @author Emperor
 */
@Initializable
public final class GodWarsMinionNPC extends AbstractNPC {

	/**
	 * The boss NPC.
	 */
	private NPC boss;

	/**
	 * Constructs a new {@code GodWarsMinionNPC} {@code Object}.
	 */
	public GodWarsMinionNPC() {
		super(6223, null);
	}

	/**
	 * Constructs a new {@code GodWarsMinionNPC} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	public GodWarsMinionNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void tick() {
		super.tick();
		if (boss != null && boss.inCombat()) {
			Entity target = boss.getAttribute("combat-attacker");
			if (target != null && (target != getProperties().getCombatPulse().getVictim() || !getProperties().getCombatPulse().isAttacking())) {
				if (!getProperties().getCombatPulse().isAttacking() && !DeathTask.isDead(this)) {
					// getProperties().getCombatPulse().attack(target);
				} else {
					getProperties().getCombatPulse().setVictim(target);
					face(target);
				}
			}
		}
	}

	@Override
	public void finalizeDeath(Entity killer) {
		super.finalizeDeath(killer);
		getProperties().getCombatPulse().stop();
		if (boss != null && boss.getRespawnTick() > GameWorld.getTicks()) {
			setRespawnTick(boss.getRespawnTick());
		}
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new GodWarsMinionNPC(id, location);
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		switch (identifier) {
		case "set_boss":
			boss = (NPC) args[0];
			return true;
		}
		return null;
	}

	@Override
	public boolean isAttackable(Entity entity, CombatStyle style, boolean message) {
		if (boss != null && boss.getId() == 6222 && style == CombatStyle.MELEE && entity instanceof Player) {
            if(message) {
                ((Player) entity).getPacketDispatch().sendMessage("The aviansie is flying too high for you to attack using melee.");
            }
			return false;
		}
		return super.isAttackable(entity, style, message);
	}

	@Override
	public int[] getIds() {
		return new int[] { 6204, 6206, 6208, 6223, 6225, 6227, 6248, 6250, 6252, 6261, 6263, 6265 };
	}

}
