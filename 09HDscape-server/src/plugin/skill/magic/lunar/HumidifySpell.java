package plugin.skill.magic.lunar;

import org.crandor.game.content.skill.free.magic.MagicSpell;
import org.crandor.game.content.skill.free.magic.Runes;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.equipment.SpellType;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the humidify spell.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class HumidifySpell extends MagicSpell {

	/**
	 * Represents the animation of this graphics.
	 */
	private static final Animation ANIMATION = new Animation(6294);

	/**
	 * Repesents the graphick, next spells of this spell.
	 */
	private static final Graphics GRAPHIC = new Graphics(1061);

	/**
	 * Represents the empty vessels.
	 */
	private static final int[] EMPTY = { 229, 1831, 1829, 1827, 1825, 1925, 1923, 1935, 5331, 5332, 5333, 5334, 5335, 5337, 5338, 5339, 6667, 7688, 731, 1980, 434 };

	/**
	 * Represents the full vessels.
	 */
	private static final int[] FULL = { 227, 1823, 1823, 1823, 1823, 1929, 1921, 1937, 5340, 5340, 5340, 5340, 5340, 5340, 5340, 5340, 6668, 7690, 732, 4458, 1761 };

	/**
	 * Constructs a new {@code CureOtherSpell} {@code Object}.
	 */
	public HumidifySpell() {
		super(SpellBook.LUNAR, 68, 65, ANIMATION, null, null, new Item[] { new Item(Runes.ASTRAL_RUNE.getId(), 1), new Item(Runes.WATER_RUNE.getId(), 3), new Item(Runes.FIRE_RUNE.getId(), 1) });
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.LUNAR.register(7, this);
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		Player p = (Player) entity;
		boolean good = false;
		for (int i = 0; i < EMPTY.length; i++) {
			if (p.getInventory().contains(EMPTY[i], 1)) {
				good = true;
				break;
			}
		}
		if (!good) {
			p.sendMessage("You need something which holds water in order to do this spell.");
			return false;
		}
		if (!super.meetsRequirements(p, true, true)) {
			return false;
		}
		p.lock(ANIMATION.getDuration() + 1);
		p.animate(ANIMATION);
		p.graphics(GRAPHIC);
		p.getAudioManager().send(3614);
		for (int k = 0; k < 28; k++) {
			for (int i = 0; i < 21; i++) {
				if (p.getInventory().contains(EMPTY[i], 1)) {
					if (p.getInventory().remove(new Item(EMPTY[i]))) {
						p.getInventory().add(new Item(FULL[i]));
					}
				}
			}
		}
		return true;
	}

}
