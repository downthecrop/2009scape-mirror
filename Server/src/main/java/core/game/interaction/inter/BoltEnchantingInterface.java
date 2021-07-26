package core.game.interaction.inter;

import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.plugin.Initializable;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.magic.MagicStaff;
import core.game.node.entity.skill.magic.Runes;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Plugin;

/**
 * Represents the component plugin used to handle the bolt enchanting interface.
 * @author Emperor
 * @author SonicForce41
 */
@Initializable
public final class BoltEnchantingInterface extends ComponentPlugin {

	/**
	 * Represents the graphics to use.
	 */
	private static final Graphics GRAPHICS = new Graphics(759);

	/**
	 * Represents the animation to use.
	 */
	private static final Animation ANIMATION = new Animation(4462);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(432, this);
		return this;
	}

	@Override
	public boolean handle(final Player player, Component component, int opcode, int button, int slot, int itemId) {
		final Bolts bolts = Bolts.forId(button);
		if (bolts != null) {
			player.getInterfaceManager().close();
			if (player.getSkills().getLevel(Skills.MAGIC) < bolts.getLevel()) {
				player.getPacketDispatch().sendMessage("You need a level of " + bolts.getLevel() + " to cast this spell.");
				return true;
			}
			for (Item item : bolts.getRunes()) {
				if (!player.getInventory().containsItem(item) && !usingStaff(player, item.getId())) {
					player.getPacketDispatch().sendMessage("You do not have enough runes to cast this spell.");
					return true;
				}
			}
			final int amount = player.getInventory().getAmount(new Item(bolts.getBolt(), 1));
			if (amount < 1) {
				player.getPacketDispatch().sendMessage("You don't have any bolts to enchant.");
				return true;
			}
			final Item add = new Item(bolts.getEnchanted());
			if (amount > 10 && !player.getInventory().hasSpaceFor(add)) {
				player.getPacketDispatch().sendMessage("Not enough space in your inventory!");
				return true;
			}
			player.graphics(GRAPHICS);
			player.animate(ANIMATION);
			player.getPulseManager().run(new Pulse(1) {
				@Override
				public boolean pulse() {
					int enchant = amount;
					if (enchant > 10) {
						enchant = 10;
					}
					add.setAmount(enchant);
					player.getInventory().remove(new Item(bolts.getBolt(), enchant));
					player.getInventory().add(add);
					for (Item item : bolts.getRunes()) {
						boolean staff = usingStaff(player, item.getId());
						if (!player.getInventory().containsItem(item) && !staff) {
							player.getPacketDispatch().sendMessage("You do not have enough " + item.getName() + "s to cast this spell.");
							return true;
						}
						if (!staff) {
							player.getInventory().remove(item);
						}
					}
					player.getSkills().addExperience(Skills.MAGIC, bolts.getExp(), true);
					player.getPacketDispatch().sendMessage("The magic of the runes coaxes out the true nature of the gem tips.");
					return true;
				}
			});
			return true;
		}
		return false;
	}

	/**
	 * Checks if the player is holding the staff that will be used instead of
	 * the rune.
	 * @param p The player.
	 * @param rune The rune item id.
	 * @return {@code True} if the player is wearing the correct staff for this
	 * rune.
	 */
	public boolean usingStaff(Player p, int rune) {
		Item weapon = p.getEquipment().get(3);
		if (weapon == null) {
			return false;
		}
		MagicStaff staff = MagicStaff.forId(rune);
		if (staff == null) {
			return false;
		}
		int[] staves = staff.getStaves();
		for (int id : staves) {
			if (weapon.getId() == id) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Represents the enumeration of bolts.
	 * @author SonicForce41
	 * @version 1
	 */
	public enum Bolts {
		OPAL(14, 879, 4, new Item[] { new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.AIR_RUNE.getId(), 2) }, 9, 9236), SAPPHIRE(29, 9337, 7, new Item[] { new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.MIND_RUNE.getId(), 1), new Item(Runes.WATER_RUNE.getId(), 1) }, 17, 9240), JADE(18, 9335, 14, new Item[] { new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.EARTH_RUNE.getId(), 2) }, 19, 9237), PEARL(22, 880, 24, new Item[] { new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.WATER_RUNE.getId(), 2) }, 29, 9238), EMERALD(32, 9338, 27, new Item[] { new Item(Runes.NATURE_RUNE.getId(), 1), new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.AIR_RUNE.getId(), 3) }, 37, 9241), RED_TOPAZ(26, 9336, 29, new Item[] { new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.FIRE_RUNE.getId(), 2) }, 33, 9239), RUBY(35, 9339, 49, new Item[] { new Item(Runes.BLOOD_RUNE.getId(), 1), new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.FIRE_RUNE.getId(), 5) }, 59, 9242), DIAMOND(38, 9340, 57, new Item[] { new Item(Runes.LAW_RUNE.getId(), 2), new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.EARTH_RUNE.getId(), 10) }, 67, 9243), DRAGONSTONE(41, 9341, 68, new Item[] { new Item(Runes.SOUL_RUNE.getId(), 1), new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.EARTH_RUNE.getId(), 15) }, 78, 9244), ONYX(44, 9342, 87, new Item[] { new Item(Runes.DEATH_RUNE.getId(), 1), new Item(Runes.COSMIC_RUNE.getId(), 1), new Item(Runes.FIRE_RUNE.getId(), 20) }, 97, 9245);

		/**
		 * The button
		 */
		private int button;

		/**
		 * The actual bolt
		 */
		private int bolt;

		/**
		 * The level
		 */
		private int level;

		/**
		 * The runes required
		 */
		private Item[] runes;

		/**
		 * The exp gained
		 */
		private double exp;

		/**
		 * The enchanted bolt
		 */
		private int enchanted;

		/**
		 * Constructs a new {@code BoltEnchantingInterface} {@code Object}.
		 * @param button the button.
		 * @param bolt the bolt.
		 * @param level the level.
		 * @param runes the runes.
		 * @param exp the exp.
		 * @param enchanted the enchanted.
		 */
		Bolts(int button, int bolt, int level, Item[] runes, double exp, int enchanted) {
			this.button = button;
			this.bolt = bolt;
			this.level = level;
			this.runes = runes;
			this.exp = exp;
			this.enchanted = enchanted;
		}

		/**
		 * Gets the button.
		 * @return The button.
		 */
		public int getButton() {
			return button;
		}

		/**
		 * Gets the bolt.
		 * @return The bolt.
		 */
		public int getBolt() {
			return bolt;
		}

		/**
		 * Gets the level.
		 * @return The level.
		 */
		public int getLevel() {
			return level;
		}

		/**
		 * Gets the runes.
		 * @return The runes.
		 */
		private Item[] getRunes() {
			return runes;
		}

		/**
		 * Gets the exp.
		 * @return The exp.
		 */
		public double getExp() {
			return exp;
		}

		/**
		 * Gets the enchanted.
		 * @return The enchanted.
		 */
		public int getEnchanted() {
			return enchanted;
		}

		/**
		 * Gets the bolt by the id.
		 * @param button the button.
		 * @return the bolt.
		 */
		public static Bolts forId(int button) {
			for (Bolts bolts : Bolts.values()) {
				if (bolts.getButton() == button)
					return bolts;
			}
			return null;
		}
	}

}
