package core.game.node.entity.skill.hunter;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.hunter.bnet.ImplingNode;
import core.game.system.task.Pulse;
import core.game.world.map.Location;
import core.game.world.map.path.Pathfinder;
import core.game.world.update.flag.context.Graphics;
import core.tools.RandomFunction;
import rs09.game.system.config.NPCConfigParser;
import rs09.game.world.GameWorld;

/**
 * Handles an impling npc.
 * @author Vexia
 */
public final class ImplingNPC extends AbstractNPC {

	/**
	 * The impling node.
	 */
	private final ImplingNode impling;

	/**
	 * Constructs a new {@code ImplingNPC} {@code Object}.
	 */
	public ImplingNPC() {
		super(0, null);
		this.impling = null;
		this.setWalks(true);
		this.setWalkRadius(20);
	}

	/**
	 * Constructs a new {@code ImplingNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	public ImplingNPC(int id, Location location, ImplingNode impling) {
		super(id, location);
		this.impling = impling;
		if (impling != null) {
			this.getDefinition().getHandlers().put(NPCConfigParser.RESPAWN_DELAY, impling.getRespawnTime());
		}
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new ImplingNPC(id, location, null);
	}

	@Override
	public void handleTickActions() {
		if (!getLocks().isMovementLocked()) {
			if (isWalks() && !getPulseManager().hasPulseRunning() && nextWalk < GameWorld.getTicks()) {
				setNextWalk();
				Location l = getLocation().transform(-5 + RandomFunction.random(getWalkRadius()), -5 + RandomFunction.random(getWalkRadius()), 0);
				if (canMove(l)) {
					Pathfinder.find(this, l, true, Pathfinder.PROJECTILE).walk(this);
				}
			}
		}
		if (RandomFunction.random(100) < 4) {
			sendChat("Tee hee!");
		}
		int nextTeleport = getAttribute("nextTeleport", -1);
		if (nextTeleport > -1 && GameWorld.getTicks() > nextTeleport) {
			setAttribute("nextTeleport", GameWorld.getTicks() + 600);
			graphics(new Graphics(590));
			GameWorld.getPulser().submit(new Pulse(1) {
				@Override
				public boolean pulse() {
					teleport(ImpetuousImpulses.LOCATIONS[RandomFunction.random(ImpetuousImpulses.LOCATIONS.length)]);
					return true;
				}
			});
			return;
		}
	}

	@Override
	public boolean isAttackable(Entity entity, CombatStyle style, boolean message) {
		if (style != CombatStyle.MAGIC) {
			return false;
		}
		if (entity.getProperties().getSpell().getSpellId() == 12 || entity.getProperties().getSpell().getSpellId() == 30 || entity.getProperties().getSpell().getSpellId() == 56) {
			return true;
		}
		return super.isAttackable(entity, style, message);
	}

	@Override
	public void handleDrops(Player p, Entity killer) {
		getProperties().setTeleportLocation(getProperties().getSpawnLocation());
	}

	@Override
	public void checkImpact(BattleState state) {
	}

	@Override
	public int[] getIds() {
		return new int[] {};
	}

	/**
	 * Gets the impling.
	 * @return The impling.
	 */
	public ImplingNode getImpling() {
		return impling;
	}

}
