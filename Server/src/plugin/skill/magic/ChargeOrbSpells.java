package plugin.skill.magic;

import org.crandor.game.content.skill.SkillPulse;
import org.crandor.game.content.skill.free.magic.MagicSpell;
import org.crandor.game.content.skill.free.magic.Runes;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.equipment.SpellType;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the the charging orb magic spell.
 * @author Emperor
 * @version 1.0
 */
@InitializablePlugin
public final class ChargeOrbSpells extends MagicSpell {

	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = Animation.create(791);

	/**
	 * The unpowered orb item.
	 */
	private static final Item UNPOWERED_ORB = new Item(567);

	/**
	 * The object id.
	 */
	private int objectId;

	/**
	 * The item id.
	 */
	private int itemId;

	/**
	 * The button that was clicked.
	 */
	private int buttonId;

	/**
	 * Constructs a new {@code ChargeOrbSpells} {@code Object}
	 */
	public ChargeOrbSpells() {
		/*
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code ChargeOrbSpells} {@code Object}.
	 * @param level The level required.
	 * @param objectId The object id.
	 * @param itemId The item to add.
	 * @param buttonId the button clicked
	 * @param g The graphics.
	 * @param runes The runes required.
	 */
	public ChargeOrbSpells(int level, int objectId, int itemId, int buttonId, Graphics g, Item... runes) {
		super(SpellBook.MODERN, level, level + 10, ANIMATION, g, null, runes);
		this.objectId = objectId;
		this.itemId = itemId;
		this.buttonId = buttonId;
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.MODERN.register(35, new ChargeOrbSpells(56, 2151, 571, 35, new Graphics(149, 96), Runes.WATER_RUNE.getItem(30), Runes.COSMIC_RUNE.getItem(3), UNPOWERED_ORB));
		SpellBook.MODERN.register(39, new ChargeOrbSpells(60, 29415, 575, 39, new Graphics(151, 96), Runes.EARTH_RUNE.getItem(30), Runes.COSMIC_RUNE.getItem(3), UNPOWERED_ORB));
		SpellBook.MODERN.register(46, new ChargeOrbSpells(63, 42322, 569, 46, new Graphics(152, 96), Runes.FIRE_RUNE.getItem(30), Runes.COSMIC_RUNE.getItem(3), UNPOWERED_ORB));
		SpellBook.MODERN.register(49, new ChargeOrbSpells(66, 2152, 573, 49, new Graphics(150, 96), Runes.AIR_RUNE.getItem(30), Runes.COSMIC_RUNE.getItem(3), UNPOWERED_ORB));
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		if (!(target instanceof GameObject)) {
			return false;
		}
		Player p = (Player) entity;
		GameObject obj = (GameObject) target;
		if (obj == null || obj.getId() != objectId) {
			p.getPacketDispatch().sendMessage("You can't cast this spell on this object!");
			return false;
		}
		if (!meetsRequirements(entity, true, true)) {
			return false;
		}
		if (obj.getLocation().getDistance(p.getLocation()) > 3) {
			p.getPacketDispatch().sendMessage("You're standing too far from the obelisk's reach.");
			return false;
		}
		p.faceLocation(obj.getLocation());
		visualize(p, target);
		if (!p.getPulseManager().hasPulseRunning()) {
			p.getPulseManager().run(new ChargeOrbPulse(p, new Item(itemId, 1), target, buttonId));
		}
		p.getInventory().add(new Item(itemId, 1));
		return true;
	}

	/**
	 * Represents the pulse for automatically charging orbs on obelisks.
	 * @author Splinter
	 * @version 1.0
	 */
	public static class ChargeOrbPulse extends SkillPulse<Item> {

		/**
		 * The item we are going to recieve (always an orb).
		 */
		public Item item;

		/**
		 * The target of the spell.
		 */
		public Node target;

		/**
		 * The button (spell) that we clicked.
		 */
		public int buttonId;

		/**
		 * The unpowered orb item.
		 */
		private static final Item UNPOWERED_ORB = new Item(567, 1);

		/**
		 * Constructs a new {@code ChargeOrbPulse} {@code Object}.
		 * @param player the player.
		 * @param node the node.
		 * @param amount the amount.
		 * @param buttonId the clicked button
		 */
		public ChargeOrbPulse(Player player, Item item, Node target, int buttonId) {
			super(player, item);
			this.item = item;
			this.target = target;
			this.buttonId = buttonId;
		}

		@Override
		public boolean checkRequirements() {
			if (player.getInventory().contains(UNPOWERED_ORB.getId(), 1)) {
				return true;
			}
			return false;
		}

		@Override
		public void animate() {
			player.animate(new Animation(791));
		}

		@Override
		public boolean reward() {
			if (getDelay() == 1) {
				super.setDelay(4);
				return false;
			}
			if (ChargeOrbSpells.castSpell(player, SpellBook.MODERN, buttonId, target)) {
				return false;
			} else {
				return true;
			}
		}
	}
}
