package plugin.activity.mta;

import org.crandor.game.content.skill.free.magic.MagicSpell;
import org.crandor.game.content.skill.free.magic.Runes;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.equipment.SpellType;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.crandor.game.node.entity.player.link.audio.Audio;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.Plugin;

import plugin.activity.mta.impl.EnchantingZone;
import plugin.activity.mta.impl.EnchantingZone.Shapes;

/**
 * Represents the enchant spells.
 * @author Emperor
 * @author 'Vexia
 * @version 1.0
 */
public final class EnchantSpell extends MagicSpell {

	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = Animation.create(712);

	/**
	 * The graphic.
	 */
	private static final Graphics GRAPHIC = new Graphics(114, 96);

	/**
	 * The enchantable jewellery array.
	 */
	private final Item[][] jewellery;

	/**
	 * Constructs a new {@code EnchantSpell} {@code Object}.
	 */
	public EnchantSpell() {
		this.jewellery = null;
	}

	/**
	 * Constructs a new {@code EnchantSpell} {@code Object}.
	 * @param level The level required.
	 * @param jewellery The jewellery this spell is able to echant.
	 * @param runes The runes required.
	 */
	public EnchantSpell(int level, double experience, Item[][] jewellery, Item[] runes) {
		super(SpellBook.MODERN, level, experience, ANIMATION, GRAPHIC, new Audio(115, 1, 0), runes);
		this.jewellery = jewellery;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		if (!(target instanceof Item) || !(entity instanceof Player)) {
			return false;
		}
		Player p = (Player) entity;
		p.getInterfaceManager().setViewedTab(6);
		Item item = (Item) target;
		Item enchanted = null;
		for (Item[] arr : jewellery) {
			if (arr[0].getId() == item.getId()) {
				enchanted = new Item(arr[1].getId());
				break;
			}
		}
		if (enchanted == null) {
			p.getPacketDispatch().sendMessage("You can't use this spell on this item.");
			return false;
		}
		if (!meetsRequirements(entity, true, true)) {
			return false;
		}
		visualize(entity, target);
		if (p.getInventory().remove(item)) {
			p.getInventory().add(enchanted);
		}
		if (p.getZoneMonitor().isInZone("Enchantment Chamber")) {
			p.graphics(Graphics.create(237, 110));
			int pizazz = 0;
			if (item.getId() == 6903) {
				pizazz = (getSpellId() == 5 ? 1 : getSpellId() == 16 ? 2 : getSpellId() == 28 ? 3 : getSpellId() == 36 ? 4 : getSpellId() == 51 ? 5 : 6) * 2;
			} else {
				Shapes shape = EnchantingZone.Shapes.forItem(item);
				if (shape != null) {
					int convert = p.getAttribute("mta-convert", 0);
					convert += 1;
					if (convert >= 10) {
						pizazz = (getSpellId() == 5 ? 1 : getSpellId() == 16 ? 2 : getSpellId() == 28 ? 3 : getSpellId() == 36 ? 4 : getSpellId() == 51 ? 5 : 6);
						convert = 0;
					}
					p.setAttribute("mta-convert", convert);
					if (shape == EnchantingZone.BONUS_SHAPE) {
						pizazz += 1;
						p.sendMessage("You get " + pizazz + " bonus point" + (pizazz != 1 ? "s" : "") + "!");
					}
				}
			}
			if (pizazz != 0) {
				EnchantingZone.ZONE.incrementPoints(p, MTAType.ENCHANTERS.ordinal(), pizazz);
			}
		}
		return true;
	}

	@Override
	public int getDelay() {
		return 1;
	}

	@Override
	public double getExperience(Player player) {
		if (player.getZoneMonitor().isInZone("Enchantment Chamber")) {
			return getExperience() - (getExperience() * 0.75);
		}
		return getExperience();
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.MODERN.register(5, new EnchantSpell(7, 17.5, new Item[][] { { new Item(1637), new Item(2550) }, { new Item(1656), new Item(3853) }, { new Item(1694), new Item(1727) }, { new Item(11072) }, { new Item(11074) }, { new Item(6899), new Item(6902) }, { new Item(6898), new Item(6902) }, { new Item(6900), new Item(6902) }, { new Item(6901), new Item(6902) }, { new Item(6903), new Item(6902) } }, new Item[] { new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.WATER_RUNE.getId(), 1) }));
		SpellBook.MODERN.register(16, new EnchantSpell(27, 37, new Item[][] { { new Item(1639), new Item(2552) }, { new Item(1658), new Item(5521) }, { new Item(1696), new Item(1729) }, { new Item(6899), new Item(6902) }, { new Item(6898), new Item(6902) }, { new Item(6900), new Item(6902) }, { new Item(6901), new Item(6902) }, { new Item(6903), new Item(6902) } }, new Item[] { new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.AIR_RUNE.getId(), 3) }));
		SpellBook.MODERN.register(28, new EnchantSpell(49, 59, new Item[][] { { new Item(1641), new Item(2568) }, { new Item(1660), new Item(11194) }, { new Item(1698), new Item(1725) }, { new Item(6899), new Item(6902) }, { new Item(6898), new Item(6902) }, { new Item(6900), new Item(6902) }, { new Item(6901), new Item(6902) }, { new Item(6903), new Item(6902) } }, new Item[] { new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.FIRE_RUNE.getId(), 5) }));
		SpellBook.MODERN.register(36, new EnchantSpell(57, 67, new Item[][] { { new Item(1643), new Item(2570) }, { new Item(1662), new Item(11090) }, { new Item(1700), new Item(1731) }, { new Item(6899), new Item(6902) }, { new Item(6898), new Item(6902) }, { new Item(6900), new Item(6902) }, { new Item(6901), new Item(6902) }, { new Item(6903), new Item(6902) } }, new Item[] { new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.EARTH_RUNE.getId(), 10) }));
		SpellBook.MODERN.register(51, new EnchantSpell(68, 78, new Item[][] { { new Item(1645), new Item(2572) }, { new Item(1664), new Item(11105) }, { new Item(1702), new Item(1712) }, { new Item(11115), new Item(11118) }, { new Item(6899), new Item(6902) }, { new Item(6898), new Item(6902) }, { new Item(6900), new Item(6902) }, { new Item(6901), new Item(6902) }, { new Item(6903), new Item(6902) } }, new Item[] { new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.WATER_RUNE.getId(), 15), new Item(Runes.EARTH_RUNE.getId(), 15) }));
		SpellBook.MODERN.register(61, new EnchantSpell(87, 97, new Item[][] { { new Item(6575), new Item(6583) }, { new Item(6577), new Item(11128) }, { new Item(11130), new Item(11133) }, { new Item(6581), new Item(6585) }, { new Item(6899), new Item(6902) }, { new Item(6898), new Item(6902) }, { new Item(6900), new Item(6902) }, { new Item(6901), new Item(6902) }, { new Item(6903), new Item(6902) } }, new Item[] { new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.FIRE_RUNE.getId(), 20), new Item(Runes.EARTH_RUNE.getId(), 20) }));
		return this;
	}
}