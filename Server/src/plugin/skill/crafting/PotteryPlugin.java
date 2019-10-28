package plugin.skill.crafting;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.dialogue.SkillDialogueHandler;
import org.crandor.game.content.dialogue.SkillDialogueHandler.SkillDialogue;
import org.crandor.game.content.skill.free.crafting.pottery.FirePotteryPulse;
import org.crandor.game.content.skill.free.crafting.pottery.PotteryItem;
import org.crandor.game.content.skill.free.crafting.pottery.PotteryPulse;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used to handle pottery actions.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class PotteryPlugin extends UseWithHandler {

	/**
	 * Represents the soft clay item.
	 */
	private static final Item SOFT_CLAY = new Item(1761);

	/**
	 * Represents the oven ids.
	 */
	private static final int[] OVENS = new int[] { 2643, 4308, 11601, 34802 };

	/**
	 * Constructs a new {@code PotteryPlugin} {@code Object}.
	 */
	public PotteryPlugin() {
		super(1761);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		new FireOvenPlugin().newInstance(arg);
		addHandler(2642, OBJECT_TYPE, this);
		addHandler(2643, OBJECT_TYPE, this);
		addHandler(4308, OBJECT_TYPE, this);
		addHandler(4310, OBJECT_TYPE, this);
		addHandler(20375, OBJECT_TYPE, this);
		addHandler(34801, OBJECT_TYPE, this);
		addHandler(34802, OBJECT_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(final NodeUsageEvent event) {
		final Player player = event.getPlayer();
		new SkillDialogueHandler(player, SkillDialogue.FIVE_OPTION, (Object[]) getPottery(false)) {

			@Override
			public void create(final int amount, int index) {
				player.getPulseManager().run(new PotteryPulse(player, event.getUsedItem(), amount, PotteryItem.values()[index]));
			}

			@Override
			public int getAll(int index) {
				return player.getInventory().getAmount(SOFT_CLAY);
			}

		}.open();
		return true;
	}

	/**
	 * Gets the pottery items.
	 * @param finished if not.
	 * @return the items.
	 */
	private Item[] getPottery(boolean finished) {
		final Item[] items = new Item[PotteryItem.values().length];
		for (int i = 0; i < items.length; i++) {
			items[i] = finished ? PotteryItem.values()[i].getProduct() : PotteryItem.values()[i].getUnfinished();
		}
		return items;
	}

	/**
	 * Represents the fire oven plugin.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public class FireOvenPlugin extends OptionHandler {

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			for (int id : OVENS) {
				ObjectDefinition.forId(id).getConfigurations().put("option:fire", this);
			}
			new FireUseHandler().newInstance(arg);
			return this;
		}

		@Override
		public boolean handle(final Player player, Node node, String option) {
			getSkillHandler(player).open();
			return true;
		}

		/**
		 * Represents the fire use with handler.
		 * @author 'Vexia
		 * @version 1.0
		 */
		public final class FireUseHandler extends UseWithHandler {

			/**
			 * Constructs a new {@code FireUseHandler} {@code Object}.
			 */
			public FireUseHandler() {
				super(1787, 1789, 1791, 5352, 4438);
			}

			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				addHandler(2643, OBJECT_TYPE, this);
				addHandler(4308, OBJECT_TYPE, this);
				addHandler(11601, OBJECT_TYPE, this);
				addHandler(34802, OBJECT_TYPE, this);
				return this;
			}

			@Override
			public boolean handle(NodeUsageEvent event) {
				final Player player = event.getPlayer();
				getSkillHandler(player).open();
				return true;
			}

		}

		/**
		 * Gets the skill handler dialogue.
		 * @param player the player.
		 * @return the dialogue handler.
		 */
		public SkillDialogueHandler getSkillHandler(final Player player) {
			return new SkillDialogueHandler(player, SkillDialogue.FIVE_OPTION, (Object[]) getPottery(true)) {

				@Override
				public void create(final int amount, final int index) {
					player.getPulseManager().run(new FirePotteryPulse(player, PotteryItem.values()[index].getUnfinished(), PotteryItem.values()[index], amount));
				}

				@Override
				public int getAll(int index) {
					return player.getInventory().getAmount(PotteryItem.values()[index].getUnfinished());
				}
			};
		}

	}

}
