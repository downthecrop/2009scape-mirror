package content.region.fremennik.neitiznot.handlers;

import core.game.dialogue.SkillDialogueHandler;
import core.game.dialogue.SkillDialogueHandler.SkillDialogue;
import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import content.global.skill.crafting.armour.LeatherCrafting;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;

/**
 * Handles the crafting of yak armour.
 * @author Vexia
 */
public class YakArmourPlugin extends UseWithHandler {

	/**
	 * The body item.
	 */
	private static final Item BODY = new Item(10822);

	/**
	 * The legs item.
	 */
	private static final Item LEGS = new Item(10824);

	/**
	 * Constructs a new {@code YakArmourPlugin} {@code Object}
	 */
	public YakArmourPlugin() {
		super(1733);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(10820, ITEM_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(final NodeUsageEvent event) {
		final Player player = event.getPlayer();
		SkillDialogueHandler dialogue = new SkillDialogueHandler(player, SkillDialogue.TWO_OPTION, LEGS, BODY) {

			@Override
			public void create(final int amount, int index) {
				player.getPulseManager().run(new YakArmourPulse(player, index == 1 ? BODY : LEGS, index, amount));
			}

			@Override
			public int getAll(int index) {
				return player.getInventory().getAmount(event.getUsedItem());
			}

		};
		dialogue.open();
		return true;
	}

	/**
	 * Handles the crafting of yak armour.
	 * @author Vexia
	 */
	public class YakArmourPulse extends SkillPulse<Item> {

		/**
		 * The index.
		 */
		private final int index;
		private final int YAK_BODY_INDEX = 1;

		/**
		 * The ticks.
		 */
		private int ticks;

		/**
		 * The amount.
		 */
		private int amount;

		/**
		 * Constructs a new {@code YakArmourPulse} {@code Object}
		 * @param player the player.
		 * @param node the node.
		 * @param index the index.
		 */
		public YakArmourPulse(Player player, Item node, int index, int amount) {
			super(player, node);
			this.index = index;
			this.amount = amount;
		}

		@Override
		public boolean checkRequirements() {
			int level = (index == YAK_BODY_INDEX ? 46 : 43);
			if (player.getSkills().getLevel(Skills.CRAFTING) < level) {
				player.getDialogueInterpreter().sendDialogue("You need a Crafting level of at least " + level + " in order to do this.");
				return false;
			}
			if (!player.getInventory().contains(LeatherCrafting.NEEDLE, 1)) {
				return false;
			}
			if (!player.getInventory().containsItem(LeatherCrafting.THREAD)) {
				player.getDialogueInterpreter().sendDialogue("You need some thread to make anything out of leather.");
				return false;
			}
			int reqAmount = index == YAK_BODY_INDEX ? 2 : 1;
			if (!player.getInventory().contains(10820, reqAmount)) {
				player.getDialogueInterpreter().sendDialogue("You don't have the required amount of yak-hide in order to do this.");
				return false;
			}
			player.getInterfaceManager().close();
			return true;
		}

		@Override
		public void animate() {
			if (ticks % 5 == 0) {
				player.animate(Animation.create(1249));
			}
		}

		@Override
		public boolean reward() {
			if (++ticks % 5 != 0) {
				return false;
			}
			int reqAmount = index == YAK_BODY_INDEX ? 2 : 1;
			if (player.getInventory().remove(new Item(10820, reqAmount))) {
			    player.getInventory().add(node);
				player.getSkills().addExperience(Skills.CRAFTING, 32, true);
				LeatherCrafting.decayThread(player);
				player.sendMessage("You make " + node.getName().toLowerCase() + ".");
			}
			amount--;
			return amount < 1;
		}

	}

}
