package plugin.npc.other;

import org.crandor.game.node.entity.combat.CombatStyle;
import org.crandor.game.node.entity.combat.CombatSwingHandler;
import org.crandor.game.node.entity.combat.equipment.SwitchAttack;
import org.crandor.game.node.entity.combat.handlers.DragonfireSwingHandler;
import org.crandor.game.node.entity.combat.handlers.MultiSwingHandler;
import org.crandor.game.node.entity.impl.Animator.Priority;
import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.world.update.flag.context.Graphics;

/**
 * Handles a chromatic dragon (green, blue, red, ..).
 * @author Emperor
 */
@InitializablePlugin
public final class ChromaticDragonNPC extends AbstractNPC {

	/**
	 * The dragonfire attack.
	 */
	private static final SwitchAttack DRAGONFIRE = DragonfireSwingHandler.get(true, 52, new Animation(81, Priority.HIGH), new Graphics(1, 64), null, null);

	/**
	 * Handles the combat.
	 */
	private final CombatSwingHandler combatAction = new MultiSwingHandler(false, new SwitchAttack(CombatStyle.MELEE.getSwingHandler(), new Animation(80, Priority.HIGH)), new SwitchAttack(CombatStyle.MELEE.getSwingHandler(), new Animation(91, Priority.HIGH)), DRAGONFIRE);

	/**
	 * Constructs a new {@code ChromaticDragonNPC} {@code Object}.
	 */
	public ChromaticDragonNPC() {
		super(941, null);
	}

	/**
	 * Constructs a new {@code ChromaticDragonNPC} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	public ChromaticDragonNPC(int id, Location location) {
		super(id, location);
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
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new ChromaticDragonNPC(id, location);
	}

	@Override
	public int[] getIds() {
		return new int[] { 941, 4677, 4678, 4679, 4680, // Green dragon
				55, 4681, 4682, 4683, 4684, // Blue dragon
				53, 4669, 4670, 4671, 4672, // Red dragon
				54, 4673, 4674, 4675, 4676, // Black dragon
				8629 //lava dragon
		};
	}

}
