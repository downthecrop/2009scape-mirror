package plugin.npc.other;

import org.crandor.game.node.entity.combat.CombatSpell;
import org.crandor.game.node.entity.combat.CombatStyle;
import org.crandor.game.node.entity.combat.CombatSwingHandler;
import org.crandor.game.node.entity.combat.equipment.SwitchAttack;
import org.crandor.game.node.entity.combat.handlers.MultiSwingHandler;
import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.world.update.flag.context.Animation;

/**
 * Represents the catablepon npc type.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class CatableponNPC extends AbstractNPC {

	/**
	 * Represents the NPC ids of NPCs using this plugin.
	 */
	private static final int[] ID = { 4397, 4398, 4399 };

	/**
	 * Represents the animation to use.
	 */
	private static final Animation ANIMATION = new Animation(4272);

	/**
	 * Represents the swing handler.
	 */
	private final MultiSwingHandler combatHandler = new MultiSwingHandler(new SwitchAttack(CombatStyle.MELEE.getSwingHandler(), null).setUseHandler(true), new SwitchAttack(CombatStyle.MELEE.getSwingHandler(), null).setUseHandler(true), new SwitchAttack(CombatStyle.MELEE.getSwingHandler(), null).setUseHandler(true), new SwitchAttack(CombatStyle.MAGIC.getSwingHandler(), ANIMATION).setUseHandler(true));

	/**
	 * Constructs a new {@code CatableponNPC} {@code Object}.
	 */
	public CatableponNPC() {
		super(0, null);
	}

	/**
	 * Constructs a new {@code CatableponNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	private CatableponNPC(int id, Location location) {
		super(id, location);
		this.setAggressive(true);
	}

	@Override
	public void configure() {
		super.configure();
		super.getProperties().setSpell((CombatSpell) SpellBook.MODERN.getSpell(7));
		super.getProperties().setAutocastSpell((CombatSpell) SpellBook.MODERN.getSpell(7));
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new CatableponNPC(id, location);
	}

	@Override
	public CombatSwingHandler getSwingHandler(boolean swing) {
		return combatHandler;
	}

	@Override
	public int[] getIds() {
		return ID;
	}
}
