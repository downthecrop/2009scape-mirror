package core.game.content.activity.pestcontrol;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.DeathTask;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;

/**
 * Handles the Squire NPC.
 * @author Emperor
 */
public final class PCSquireNPC extends AbstractNPC {

	/**
	 * The pest control session.
	 */
	private PestControlSession session;

	/**
	 * If the amount of lifepoints should be updated.
	 */
	private boolean updateLifepoints;

	/**
	 * Constructs a new {@code PCSquireNPC} {@code Object}.
	 */
	public PCSquireNPC() {
		super(3782, null, false);
	}

	/**
	 * Constructs a new {@code PCSquireNPC} {@code Object}.
	 * @param id The npc id.
	 * @param location The spawn location.
	 */
	public PCSquireNPC(int id, Location location) {
		super(id, location, false);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new PCSquireNPC(id, location);
	}

	@Override
	public boolean isAttackable(Entity entity, CombatStyle style, boolean message) {
		if (DeathTask.isDead(this) || entity instanceof Player) {
			return false;
		}
		return true;
	}

	@Override
	public void init() {
		super.setRespawn(false);
		super.getProperties().setRetaliating(false);
		super.init();
		super.getProperties().setDefenceAnimation(Animation.create(-1));
		super.getProperties().setDeathAnimation(Animation.create(-1));
		session = getExtension(PestControlSession.class);
	}

	@Override
	public void finalizeDeath(Entity killer) {
		super.finalizeDeath(killer);
	}

	@Override
	public void tick() {
		super.tick();
		if (updateLifepoints && session != null && (session.getTicks() % 10 == 0)) {
			if (getSkills().getLifepoints() > 50) {
				session.sendString(Integer.toString(getSkills().getLifepoints()), 1);
			} else {
				session.sendString("<col=FF0000>" + Integer.toString(getSkills().getLifepoints()), 1);
			}
			updateLifepoints = false;
		}
	}

	@Override
	public void onImpact(final Entity entity, BattleState state) {
		updateLifepoints = true;
		super.onImpact(entity, state);
	}

	@Override
	public int[] getIds() {
		return new int[] { 3782, 3785 };
	}

}