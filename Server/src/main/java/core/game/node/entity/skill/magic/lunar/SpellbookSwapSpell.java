package core.game.node.entity.skill.magic.lunar;

import core.game.component.Component;
import core.game.node.entity.skill.magic.MagicSpell;
import core.game.node.entity.skill.magic.Runes;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.SpellType;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;

/**
 * The spellbook swap spell.
 * @author 'Vexia
 */
@Initializable
public class SpellbookSwapSpell extends MagicSpell {

	/**
	 * Represents the animation of this spell.
	 */
	private final Animation ANIMATION = new Animation(6299);

	/**
	 * Represents the graphics of this spell.
	 */
	private final Graphics GRAPHIC = new Graphics(1062);

	/**
	 * Constructs a new {@code SpellbookSwapSpell} {@code Object}.
	 */
	public SpellbookSwapSpell() {
		super(SpellBook.LUNAR, 96, 130, null, null, null, new Item[] { new Item(Runes.LAW_RUNE.getId(), 1), new Item(Runes.COSMIC_RUNE.getId(), 2), new Item(Runes.ASTRAL_RUNE.getId(), 3) });
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.LUNAR.register(12, this);
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		final Player player = (Player) entity;
		if (!super.meetsRequirements(player, true, true)) {
			return false;
		}
		player.lock(9);
		player.animate(ANIMATION);
		player.graphics(GRAPHIC);
		player.getDialogueInterpreter().open(3264731);
		final int id = RandomFunction.random(1, 500000);
		player.setAttribute("spell:swap", id);
		GameWorld.getPulser().submit(new Pulse(20, player) {
			@Override
			public boolean pulse() {
				if (player.getAttribute("spell:swap", 0) == id) {
					removeTemporarySpell(player);
				}
				return true;
			}

		});
		return true;
	}

	/**
	 * Method used to remove the temp spell swap.
	 * @param player the player.
	 */
	public static void removeTemporarySpell(final Player player) {
		player.removeAttribute("spell:swap");
		player.getSpellBookManager().setSpellBook(SpellBook.LUNAR);
		player.getInterfaceManager().openTab(new Component(SpellBook.LUNAR.getInterfaceId()));
	}
}
