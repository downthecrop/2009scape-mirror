package core.game.node.entity.npc.other;

import core.game.node.entity.combat.CombatSpell;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.equipment.SwitchAttack;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import rs09.game.node.entity.combat.CombatSwingHandler;
import rs09.game.node.entity.combat.handlers.MultiSwingHandler;

/**
 * Represents the catablepon npc type.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
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
