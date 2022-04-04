package core.game.node.entity.skill.slayer;

import core.cache.def.impl.ItemDefinition;
import core.cache.def.impl.NPCDefinition;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.skill.Skills;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.OptionHandler;
import core.game.interaction.UseWithHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.game.node.entity.skill.slayer.SlayerManager;
import rs09.plugin.ClassScanner;

/**
 * Handles the slayer reward interface plugin.
 * @author Vexia
 * 
 */
@Initializable
public class SlayerRewardPlugin extends ComponentPlugin {

	/**
	 * The assignment component tab.
	 */
	private static final Component ASSIGNMENT = new Component(161);

	/**
	 * The learn component tab.
	 */
	private static final Component LEARN = new Component(163);

	/**
	 * The buy component tab.
	 */
	private static final Component BUY = new Component(164);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.forId(161).setPlugin(this);//assignment
		ComponentDefinition.forId(163).setPlugin(this);//learn
		ComponentDefinition.forId(164).setPlugin(this);//buy
		ClassScanner.definePlugins(new SlayerMasterPlugin(), new SlayerHelmCraftPlugin());
		return this;
	}

	@Override
	public void open(Player player, Component open) {
		updateInterface(player, open);
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		switch (component.getId()) {
		case 161://asignment
			switch (button) {
			case 23://reassign
			case 26:
				if (!SlayerManager.getInstance(player).hasTask()) {
					player.sendMessage("You don't have an active task right now.");
					break;
				}
				if (purchase(player, 30)) {
					SlayerManager.getInstance(player).clear();
					player.sendMessage("You have canceled your current task.");
				}
				break;
			case 24:
			case 27:
				if (SlayerManager.getInstance(player).getTask() == null) {
					player.sendMessage("You don't have a slayer task.");
					break;
				}
				if (SlayerManager.getInstance(player).getRemoved().size() >= 4) {
					player.sendMessage("You can't remove anymore tasks.");
					break;
				}
				if (SlayerManager.getInstance(player).getSlayerPoints() >= 30 && !player.isAdmin()) {
					int size = SlayerManager.getInstance(player).getRemoved().size();
					int qp = player.getQuestRepository().getAvailablePoints();
					if (size == 0 && qp < 50) {
						player.sendMessage("You need 50 quest points as a requirement in order to block one task.");
						break;
					} else if (size == 1 && qp < 100) {
						player.sendMessage("You need 100 quest points as a requirement in order to block two tasks.");
						break;
					} else if (size == 2 && qp < 150) {
						player.sendMessage("You need 150 quest points as a requirement in order to block three tasks.");
						break;
					} else if (size == 3 && qp < 200) {
						player.sendMessage("You need 200 quest points as a requirement in order to block four tasks.");
						break;
					}
				}
				if (purchase(player, 100)) {
					SlayerManager.getInstance(player).getRemoved().add(SlayerManager.getInstance(player).getTask());
					SlayerManager.getInstance(player).clear();
					updateInterface(player, player.getInterfaceManager().getOpened());
				}
				break;	
			case 36:
			case 37:
			case 38:
			case 39:
				int index = 3 - (39 - button);
				if (SlayerManager.getInstance(player).getRemoved().isEmpty() || index > SlayerManager.getInstance(player).getRemoved().size() - 1 || SlayerManager.getInstance(player).getRemoved().get(index) == null) {
					break;
				}
				SlayerManager.getInstance(player).getRemoved().remove(index);
				updateInterface(player, player.getInterfaceManager().getOpened());
				break;
			case 15:
				openTab(player, BUY);
				break;
			case 14:
				openTab(player, LEARN);
				break;
			}
			break;
		case 163://learn
			switch (button) {
			case 14:
				openTab(player, ASSIGNMENT);
				break;
			case 15:
				openTab(player, BUY);
				break;
			case 22://Broad arrows
			case 29:
				if (SlayerManager.getInstance(player).flags.isBroadsUnlocked()) {
					player.sendMessage("You don't need to learn this ability again.");
					break;
				}
				if (purchase(player, 300)) {
					SlayerManager.getInstance(player).flags.unlockBroads();
					updateInterface(player, component);
				}
				break;
			case 23://Slayer ring
			case 30:
				if (SlayerManager.getInstance(player).flags.isRingUnlocked()) {
					player.sendMessage("You don't need to learn this ability again.");
					break;
				}
				if (purchase(player, 300)) {
					SlayerManager.getInstance(player).flags.unlockRing();
					updateInterface(player, component);
				}
				break;
			case 24://Slayer helm
			case 31:
				if (SlayerManager.getInstance(player).flags.isHelmUnlocked()) {
					player.sendMessage("You don't need to learn this ability again.");
					break;
				}
				if (purchase(player, 400)) {
					SlayerManager.getInstance(player).flags.unlockHelm();
					updateInterface(player, component);
				}
				break;
			}
			break;
		case 164://buy
			switch (button) {
			case 16:
				openTab(player, LEARN);
				break;
			case 17:
				openTab(player, ASSIGNMENT);
				break;
			case 24://slayer exp
			case 32:
				if (purchase(player, 400)) {
					player.getSkills().addExperience(Skills.SLAYER, 10000, false);
				}
				break;
			case 26://ring of slaying
			case 33:
				if (player.getInventory().freeSlots() < 1 && SlayerManager.getInstance(player).getSlayerPoints() >= 75) {
					player.sendMessage("You don't have enough inventory space.");
					break;
				}
				if (purchase(player, 75)) {
					player.getInventory().add(new Item(13281), player);
				}
				break;
			case 28:
			case 36:
				if (purchase(player, 35)) {
					player.getInventory().add(new Item(558, 1000), player);
					player.getInventory().add(new Item(560, 250), player);
				}
				break;
			case 34:
			case 37:
				if (purchase(player, 35)) {
					player.getInventory().add(new Item(13280, 250), player);
				}
				break;
			case 35:
			case 39:
				if (purchase(player, 35)) {
					player.getInventory().add(new Item(4172, 250), player);
				}
				break;
			}
			break;
		}
		return true;
	}

	/**
	 * Purchases a slayer point reward.
	 * @param player The player.
	 * @param amount The amount of points.
	 * @return {@code True} if purchased.
	 */
	private boolean purchase(Player player, int amount) {
		if (SlayerManager.getInstance(player).getSlayerPoints() < amount) {
			player.sendMessage("You need " + amount + " slayer points in order to purchase this reward.");
			return false;
		}
		SlayerManager.getInstance(player).setSlayerPoints(SlayerManager.getInstance(player).getSlayerPoints() - amount);
		updateInterface(player, player.getInterfaceManager().getOpened());
		return true;
	}

	/**
	 * Switches the tab on the reward interface.
	 * @param player The player instance
	 * @param open The component to open.
	 */
	private void openTab(Player player, Component open) {
		player.getInterfaceManager().open(open);
		updateInterface(player, open);
	}

	/**
	 * Update the current points text.
	 * @param player the player.
	 * @param open the component.
	 */
	private void updateInterface(Player player, Component open) {
		if (open == null) {
			return;
		}
		String space = "";
		String num = String.valueOf(SlayerManager.getInstance(player).getSlayerPoints());
		if (num != "0")  {
			for (int i = 0; i < num.length(); i++) {
				space += " ";
			}
		}
		switch (open.getId()) {
		case 161://assignment
			int childs[] = new int[] {35, 30, 31, 32};
			String[] letters = new String[] {"A", "B", "C", "D"};
			Tasks task = null;
			for (int i = 0; i < 4; i++) {
				task = i > SlayerManager.getInstance(player).getRemoved().size() - 1 ? null : SlayerManager.getInstance(player).getRemoved().get(i);
				player.getPacketDispatch().sendString(task == null ? letters[i] : task.getName(), open.getId(), childs[i]);
			}  
			player.getPacketDispatch().sendString(space + SlayerManager.getInstance(player).getSlayerPoints(), open.getId(), 19);
			break;
		case 163://learn
			for (int i = 0; i < 3; i++) {
				switch(i){
					case 0:
						player.getPacketDispatch().sendInterfaceConfig(open.getId(), 25 + i, !SlayerManager.getInstance(player).flags.isBroadsUnlocked());
						break;
					case 1:
						player.getPacketDispatch().sendInterfaceConfig(open.getId(), 25 + i, !SlayerManager.getInstance(player).flags.isRingUnlocked());
						break;
					case 2:
						player.getPacketDispatch().sendInterfaceConfig(open.getId(), 25 + i, !SlayerManager.getInstance(player).flags.isHelmUnlocked());
						break;
					default:
						break;
				}
			}
			player.getPacketDispatch().sendString(space + SlayerManager.getInstance(player).getSlayerPoints(), open.getId(), 18);
			break;
		case 164://buy
			player.getPacketDispatch().sendString(space + SlayerManager.getInstance(player).getSlayerPoints(), open.getId(), 20);
			break;
		}
	}

	/**
	 * Handles the slayer master option plugin.
	 * @author Vexia
	 *
	 */
	public class SlayerMasterPlugin extends OptionHandler {

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			for (Master m : Master.values()) {
				NPCDefinition.forId(m.getNpc()).getHandlers().put("option:rewards", this);
			}
			return this;
		}

		@Override
		public boolean handle(Player player, Node node, String option) {
			openTab(player, BUY);
			return true;
		}

	}

	/**
	 * Handles the crafting of a slayer helmet.
	 * @author Vexia
	 *
	 */
	public static class SlayerHelmCraftPlugin extends UseWithHandler {

		/**
		 * The slayer helm item.
		 */
		private static final Item SLAYER_HELM = new Item(13263);

		/**
		 * The spiny helmet.
		 */
		private static final Item SPINY_HELMET = new Item(4551);

		/**
		 * The ingredients needed.
		 */
		private static final int[] INGREDIENTS = new int[] {4168, 4166, 4164, 8921};

		/**
		 * Constructs a new {@Code SlayerHelmCraftPlugin} {@Code Object}
		 */
		public SlayerHelmCraftPlugin() {
			super(INGREDIENTS);
		}

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			addHandler(SPINY_HELMET.getId(), ITEM_TYPE, this);
			ClassScanner.definePlugin(new OptionHandler() {

				@Override
				public Plugin<Object> newInstance(Object arg) throws Throwable {
					ItemDefinition.forId(SLAYER_HELM.getId()).getHandlers().put("option:disassemble", this);
					return this;
				}

				@Override
				public boolean handle(Player player, Node node, String option) {
					if (player.getInventory().freeSlots() < 4) {
						player.sendMessage("You don't have enough inventory space.");
						return true;
					}
					player.lock(1);
					if (player.getInventory().remove(node.asItem())) {
						for (int id : INGREDIENTS) {
							player.getInventory().add(new Item(id));
						}
						player.getInventory().add(SPINY_HELMET);
					}
					player.sendMessage("You dissasemble your Slayer helm.");
					return true;
				}

			});
			return this;
		}

		@Override
		public boolean handle(NodeUsageEvent event) {
			final Player player = event.getPlayer();
			if (player.getSkills().getStaticLevel(Skills.CRAFTING) < 55) {
				player.sendMessage("You need a Crafting level of at least 55 in order to do this.");
				return true;
			}
			if (!SlayerManager.getInstance(player).flags.isHelmUnlocked()) {
				player.sendMessage("You need to unlock the ability to do that first.");
				return true;
			}
			if (!player.getInventory().containItems(INGREDIENTS)) {
				player.sendMessages("You need a nosepeg, facemask, earmuffs, spiny helmet, and a black mask in", "your inventory in order to construct a Slayer helm.");
				return true;
			}
			player.lock(1);
			if (player.getInventory().remove(SPINY_HELMET)) {
				for (int id : INGREDIENTS) {
					if (!player.getInventory().remove(new Item(id))) {
						return true;
					}
				}
				player.getInventory().add(SLAYER_HELM);
				player.sendMessage("You combine the items into a Slayer helm.");
			}
			return true;
		}

	}
}
