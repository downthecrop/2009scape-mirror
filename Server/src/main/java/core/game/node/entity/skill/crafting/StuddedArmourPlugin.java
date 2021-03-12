package core.game.node.entity.skill.crafting;

import core.plugin.Initializable;
import rs09.game.content.dialogue.SkillDialogueHandler;
import rs09.game.content.dialogue.SkillDialogueHandler.SkillDialogue;
import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;

/**
 * Represents the studded body plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class StuddedArmourPlugin extends UseWithHandler {

	/**
	 * Represents the steel studs item.
	 */
	private static final Item STEEL_STUDS = new Item(2370);

	/**
	 * Constructs a new {@code StuddedBodyPlugin} {@code Object}.
	 */
	public StuddedArmourPlugin() {
		super(2370);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (StuddedArmour armour : StuddedArmour.values()) {
			addHandler(armour.getItem().getId(), ITEM_TYPE, this);
		}
		return this;
	}

	@Override
	public boolean handle(final NodeUsageEvent event) {
		final Player player = event.getPlayer();
		final StuddedArmour armour = StuddedArmour.forItem(event.getBaseItem());
		SkillDialogueHandler handler = new SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, armour.getStudded()) {

			@Override
			public void create(int amount, int index) {
				player.getPulseManager().run(new StudArmourPulse(player, event.getBaseItem(), armour, amount));
			}

			@Override
			public int getAll(int index) {
				return player.getInventory().getAmount(STEEL_STUDS);
			}

		};
		if (player.getInventory().getAmount(armour.getItem()) == 1) {
			handler.create(1, 0);
		} else {
			handler.open();
		}
		return true;
	}

	/**
	 * Represents a studded armour.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public enum StuddedArmour {
		CHAPS(new Item(1095), new Item(1097), 18, 27), BODY(new Item(1129), new Item(1133), 14, 25);

		/**
		 * Represents the unstudded item.
		 */
		private final Item item;

		/**
		 * Represents the studded item.
		 */
		private final Item studded;

		/**
		 * Represents the level needed.
		 */
		private final int level;

		/**
		 * Represents the experience gained.
		 */
		private final double experience;

		/**
		 * Constructs a new {@code StuddedArmour} {@code Object}.
		 * @param item the item.
		 * @param studded the studded item.
		 * @param level the level.
		 * @param experience the experience.
		 */
		private StuddedArmour(Item item, Item studded, int level, double experience) {
			this.item = item;
			this.studded = studded;
			this.level = level;
			this.experience = experience;
		}

		/**
		 * Gets the studded armour for the item.
		 * @param item the item.
		 * @return the studden armour.
		 */
		public static StuddedArmour forItem(final Item item) {
			for (StuddedArmour armour : values()) {
				if (armour.getItem().getId() == item.getId()) {
					return armour;
				}
			}
			return null;
		}

		/**
		 * Gets the item.
		 * @return The item.
		 */
		public Item getItem() {
			return item;
		}

		/**
		 * Gets the studded.
		 * @return The studded.
		 */
		public Item getStudded() {
			return studded;
		}

		/**
		 * Gets the level.
		 * @return The level.
		 */
		public int getLevel() {
			return level;
		}

		/**
		 * Gets the experience.
		 * @return The experience.
		 */
		public double getExperience() {
			return experience;
		}

	}

	/**
	 * Represents the skill pulse used to stud armour.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public static final class StudArmourPulse extends SkillPulse<Item> {

		/**
		 * Represents the animation to use.
		 */
		private static final Animation ANIMATION = Animation.create(1249);

		/**
		 * Represents the armour being studded.
		 */
		private final StuddedArmour armour;

		/**
		 * Represents the amount to make.
		 */
		private int amount;

		/**
		 * Represents the ticks passed.
		 */
		private int ticks;

		/**
		 * Constructs a new {@code StudArmourPulse} {@code Object}.
		 * @param player the player.
		 * @param node the node.
		 * @param armour the armour.
		 * @param amount the amount.
		 */
		public StudArmourPulse(Player player, Item node, final StuddedArmour armour, final int amount) {
			super(player, node);
			this.armour = armour;
			this.amount = amount;
		}

		@Override
		public boolean checkRequirements() {
			if (player.getSkills().getLevel(Skills.CRAFTING) < armour.getLevel()) {
				player.getPacketDispatch().sendMessage("You need a Crafting level of at least " + armour.getLevel() + " to do this.");
				return false;
			}
			if (!player.getInventory().containsItem(STEEL_STUDS)) {
				player.getPacketDispatch().sendMessage("You need studs in order to make studded armour.");
				return false;
			}
			if (!player.getInventory().containsItem(armour.getItem())) {
				return false;
			}
			return true;
		}

		@Override
		public void animate() {
			if (ticks % 5 == 0) {
				player.animate(ANIMATION);
			}
		}

		@Override
		public boolean reward() {
			if (++ticks % 5 != 0) {
				return false;
			}
			if (player.getInventory().remove(armour.getItem(), STEEL_STUDS)) {
				player.getInventory().add(armour.getStudded());
				player.getSkills().addExperience(Skills.CRAFTING, armour.getExperience(), true);
				player.getPacketDispatch().sendMessage("You make a " + armour.getStudded().getName().toLowerCase() + ".");
			}
			amount--;
			return amount < 1;
		}

		@Override
		public void message(int type) {
			switch (type) {
			case 0:
				player.getPacketDispatch().sendMessage("You use the studs with the " + node.getName().toLowerCase() + ".");
				break;
			}
		}
	}
}
