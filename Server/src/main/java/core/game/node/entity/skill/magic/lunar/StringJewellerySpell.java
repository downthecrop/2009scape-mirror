package core.game.node.entity.skill.magic.lunar;

import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.magic.MagicSpell;
import core.game.node.entity.skill.magic.Runes;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.SpellType;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the jewllery stringing spell.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class StringJewellerySpell extends MagicSpell {

	/**
	 * Represents the animation of this spell.
	 */
	private static final Animation ANIMATION = Animation.create(4412);

	/**
	 * Represents the graphic of this spell.
	 */
	private static final Graphics GRAPHIC = new Graphics(728, 100);

	/**
	 * Represents the array of unstrung jewellery.
	 */
	private static final int[] UNSTRUNG = { 1673, 1675, 1677, 1679, 1681, 1683, 1714 };

	/**
	 * Represents an array of strung jewellery.
	 */
	private static final int[] STRUNG = { 1692, 1694, 1696, 1698, 1700, 1702, 1718 };

	/**
	 * Constructs a new {@code CureOtherSpell} {@code Object}.
	 */
	public StringJewellerySpell() {
		super(SpellBook.LUNAR, 80, 87, null, null, null, new Item[] { new Item(Runes.ASTRAL_RUNE.getId(), 2), new Item(Runes.EARTH_RUNE.getId(), 10), new Item(Runes.WATER_RUNE.getId(), 5) });
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.LUNAR.register(22, this);
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		final Player p = (Player) entity;
		boolean contains = false;
		Item item = null;
		for (int i : UNSTRUNG) {
			if (p.getInventory().contains(i, 1)) {
				contains = true;
				item = new Item(i);
				break;
			}
		}
		if (!contains) {
			p.getPacketDispatch().sendMessage("You need jewellery in order to use this spell.");
			return false;
		}
		if (!meetsRequirements(entity, true, false)) {
			return false;
		}
		p.getPulseManager().run(new LunarStringPulse(p, item));
		return true;
	}

	/**
	 * Represents the skill pulse used to string a jewellery.
	 * @author 'Vexia
	 */
	public class LunarStringPulse extends SkillPulse<Item> {

		/**
		 * Constructs a new {@code LunarStringPulse} {@code Object}.
		 * @param player the player.
		 * @param node the node.
		 */
		public LunarStringPulse(final Player player, final Item node) {
			super(player, node);
		}

		@Override
		public boolean checkRequirements() {
			if (!player.getInventory().containsItem(node)) {
				stop();
				return false;
			}
			return true;
		}

		@Override
		public void animate() {
			player.animate(ANIMATION);
			player.graphics(GRAPHIC);
		}

		@Override
		public boolean reward() {
			if (getDelay() == 1) {
				setDelay(2);
				return false;
			}
			if (meetsRequirements(player, true, true) && player.getInventory().remove(node)) {
				player.getInventory().add(new Item(STRUNG[getIndex()]));
				player.getSkills().addExperience(Skills.CRAFTING, 4, true);
				player.getSkills().addExperience(Skills.MAGIC, 83, true);
			} else {
				return true;
			}
			return nextItem() == null;
		}

		@Override
		public void stop() {
			super.stop();
			player.graphics(new Graphics(-1));
		}

		@Override
		public void message(int type) {
		}

		/**
		 * Method used to get index.
		 * @return the index.
		 */
		public int getIndex() {
			for (int i = 0; i < UNSTRUNG.length; i++) {
				if (node.getId() == UNSTRUNG[i]) {
					return i;
				}
			}
			return -1;
		}

		/**
		 * Method used to get the next string.
		 * @return the string.
		 */
		public Item nextItem() {
			for (int i : UNSTRUNG) {
				if (player.getInventory().contains(i, 1)) {
					node = new Item(i);
				}
			}
			return node;
		}
	}
}
