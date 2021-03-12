package core.game.node.entity.skill.slayer;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.equipment.SwitchAttack;
import core.game.node.entity.combat.handlers.DragonfireSwingHandler;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.AbstractNPC;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import rs09.game.node.entity.combat.CombatSwingHandler;
import rs09.game.node.entity.combat.handlers.MultiSwingHandler;

/**
 * Handles a brutal dragon npc.
 * @author Vexia
 */
@Initializable
public final class BrutalDragonNPC extends AbstractNPC {

	/**
	 * The dragonfire attack.
	 */
	private static final SwitchAttack DRAGONFIRE = DragonfireSwingHandler.get(false, 52, new Animation(81, Priority.HIGH), Graphics.create(1), null, null);

	/**
	 * Handles the combat.
	 */
	private final CombatSwingHandler combatAction = new MultiSwingHandler(true, new SwitchAttack(CombatStyle.MELEE.getSwingHandler(), new Animation(80, Priority.HIGH)), new SwitchAttack(CombatStyle.MELEE.getSwingHandler(), new Animation(80, Priority.HIGH)), new SwitchAttack(CombatStyle.MAGIC.getSwingHandler(), new Animation(81, Priority.HIGH), null, null, Projectile.create((Entity) null, null, 500, 20, 20, 41, 40, 18, 255)), DRAGONFIRE);

	/**
	 * Constructs a new {@code BrutalDragonNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	public BrutalDragonNPC(int id, Location location) {
		super(id, location);
	}

	/**
	 * Constructs a new {@code MithrilDragonNPC} {@code Object}.
	 */
	public BrutalDragonNPC() {
		super(0, null);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new BrutalDragonNPC(id, location);
	}

	@Override
	public CombatSwingHandler getSwingHandler(boolean swing) {
		return combatAction;
	}

	@Override
	public int getDragonfireProtection(boolean fire) {
		return 0x2 | 0x4 | 0x8;
	}

	@Override
	public int[] getIds() {
		return new int[] { 5362 };
	}
}
