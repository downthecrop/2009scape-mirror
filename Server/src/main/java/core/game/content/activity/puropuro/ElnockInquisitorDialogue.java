package core.game.content.activity.puropuro;

import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.skill.hunter.bnet.BNetTypes;
import core.game.node.entity.skill.hunter.bnet.ImplingNode;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;

import core.game.content.activity.puropuro.ElnockInquisitorDialogue.ElnockExchangeInterfaceHandler.ElnockExchange;

/**
 * Handles the elnock inquisitor dialogue.
 * @author Vexia
 */
public final class ElnockInquisitorDialogue extends DialoguePlugin {

	/**
	 * The scroll item.
	 */
	private static final Item SCROLL = new Item(11273);

	/**
	 * Constructs a new {@code ElnockInquisitorDialogue} {@code Object}.
	 */
	public ElnockInquisitorDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code ElnockInquisitorDialogue} {@code Object}.
	 * @param player the player.
	 */
	public ElnockInquisitorDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new ElnockInquisitorDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc("Ah, good day, it's you again. What can I do for you?");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			if (!player.hasItem(SCROLL)) {
				npc("Ah, I notice you don't own an impling collector's scroll.");
				stage = 900;
				break;
			}
			options("Can you remind me how to catch implings again?", "Can I trade some jarred implings please?", "Do you have some spare equipment I can use?");
			stage++;
			break;
		case 1:
			switch (buttonId) {
			case 1:
				player("Can you remind me how to catch implings again?");
				stage = 10;
				break;
			case 2:
				player("Can I trade some jarred implings please?");
				stage = 20;
				break;
			case 3:
				player("Do you have some spare equipment I can use?");
				stage = 30;
				break;
			}
			break;
		case 10:
			npc("Certainly.");
			stage++;
			break;
		case 11:
			npc("Firstly you will need a butterfly net in which to catch", "them and at least one special impling jar to store an", "impling.");
			stage++;
			break;
		case 12:
			npc("You will also require some experience as a Hunter", "since these creatures are elusive. The more immature", "implings require less experience, but some of the rarer", "implings are extraordinarily hard to find and catch.");
			stage++;
			break;
		case 13:
			npc("Once you have caught one, you may break the jar", "open and obtain the object the impling is carrying.", "Alternatively, you may exchange certain combinations of", "jars with me. I will return the jars to my clients. In");
			stage++;
			break;
		case 14:
			npc("exchange I will be able to provide you with some", "equipment that may help you hunt butterflies more", "effectively.");
			stage++;
			break;
		case 15:
			npc("also beware. Those imps walking around the maze do", "not like the fact that their kindred spirits are being", "captured and will attempt to steal any full jars you have", "on you, setting the implings free.");
			stage++;
			break;
		case 16:
			end();
			break;
		case 20:
			end();
			openShop(player);
			break;
		case 30:
			if (!player.getSavedData().getActivityData().isElnockSupplies()) {
				player.getSavedData().getActivityData().setElnockSupplies(true);
				player.getInventory().add(new Item(10010), player);
				player.getInventory().add(new Item(11262, 1), player);
				player.getInventory().add(new Item(11260, 6), player);
				npc("Here you go!");
				stage++;
			} else {
				npc("Since I have already given you some equipment for free,", "I'll be willing to sell you some now.");
				stage = 500;
			}
			break;
		case 31:
			npc("If you are ready to start hunting implings, then enter", "the main part of the maze.");
			stage++;
			break;
		case 32:
			npc("Just push through the wheat that surrounds the centre", "of the maze and get catching!");
			stage++;
			break;
		case 33:
			end();
			break;
		case 900:
			options("Yes, please.", "No, thanks.");
			stage++;
			break;
		case 901:
			switch (buttonId) {
			case 1:
				player.getInventory().add(SCROLL, player);
				interpreter.sendItemMessage(SCROLL, "Elnock gives you a scroll. If you check it whilst in the maze, you will see how many of each impling you have captured.");
				stage++;
				break;
			case 2:
				end();
				break;
			}
			break;
		case 902:
			end();
			break;
		case 500:
			player("*sigh*", "Alright, sell me some Impling jars.");
			stage++;
			break;
		case 501:
			npc("I'll sell you five jars for 25,000gp, what do you say?");
			stage++;
			break;
		case 502:
			if (player.getInventory().contains(995, 25000)) {
				player("Wow, that's expensive!", "Alright... here's some gold for a few jars.");
				stage++;
			} else {
				player("Actually, I don't have that many coins at the moment.");
				stage = 902;
			}
			break;
		case 503:
			npc("Young one, that is the price you pay for", "forgetting to save some impling jars!");
			if (player.getInventory().remove(new Item(995, 25000))) {
				player.getInventory().add(new Item(11260, 5));
			}
			stage++;
			break;
		case 504:
			end();
			break;
		}
		return true;
	}

	@Override
	public void init() {
		super.init();
		ClassScanner.definePlugin(new ElnockExchangeInterfaceHandler());
	}

	@Override
	public int[] getIds() {
		return new int[] { 6070 };
	}

	/**
	 * Opens the shop exchange.
	 * @param player the player.
	 */
	public static void openShop(Player player) {
		int[] vals = new int[] { 22, 25, 28, 31 };
		player.getInterfaceManager().open(new Component(540));
		for (int i = 0; i < ElnockExchange.values().length; i++) {
			ElnockExchange e = ElnockExchange.values()[i];
			player.getPacketDispatch().sendItemZoomOnInterface(e.getSendItem(), 115, 540, vals[i]);
		}
	}

	/**
	 * Handles the elnock exchange interface.
	 * @author Vexia
	 */
	public static final class ElnockExchangeInterfaceHandler extends ComponentPlugin {

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			ComponentDefinition.forId(540).setPlugin(this);
			return this;
		}

		@Override
		public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
			ElnockExchange exchange = player.getAttribute("exchange", null);
			if (button == 34) {
				player.getConfigManager().set(1018, 0);
				if (exchange == null) {
					player.sendMessage("Making a selection before confirming.");
					return true;
				}
				if (!exchange.hasItems(player)) {
					player.sendMessage("You don't have the required implings in a jar to trade for this.");
					return true;
				}
				if (exchange == ElnockExchange.JAR_GENERATOR && player.hasItem(ElnockExchange.JAR_GENERATOR.getReward())) {
					player.sendMessage("You can't have more than one jar generator at a time.");
					return true;
				}
				if (!player.getInventory().hasSpaceFor(exchange.getReward())) {
					player.sendMessage("You don't have enough inventory space.");
					player.getInterfaceManager().close();
					player.removeAttribute("exchange");
					return true;
				}
				if (exchange == ElnockExchange.IMPLING_JAR ? player.getInventory().remove(ElnockExchange.getItem(player)) : player.getInventory().remove(exchange.getRequired())) {
					player.getInterfaceManager().close();
					player.removeAttribute("exchange");
					player.getInventory().add(exchange.getReward(), player);
				}
				return true;
			}
			exchange = ElnockExchange.forButton(button);
			if (exchange != null) {
				player.setAttribute("exchange", exchange);
				player.getConfigManager().set(1018, exchange.getConfigValue());
			}
			return true;
		}

		/**
		 * An elnock exchange.
		 * @author Vexia
		 */
		public enum ElnockExchange {
			IMP_REPELLANT(23, 444928, 11271, new Item(11262), new Item(11238, 3), new Item(11240, 2), new Item(11242)), MAGIC_BUTTERFLY(26, 707072, 11268, new Item(11259), new Item(11242, 3), new Item(11244, 2), new Item(11246)), JAR_GENERATOR(29, 969216, 11267, new Item(11258), new Item(11246, 3), new Item(11248, 2), new Item(11250)), IMPLING_JAR(32, 1231360, 11269, new Item(11260, 3)) {

				@Override
				public boolean hasItems(Player player) {
					for (ImplingNode node : BNetTypes.getImplings()) {
						if (player.getInventory().containsItem(node.getReward())) {
							return true;
						}
					}
					return false;
				}

			};

			/**
			 * The button.
			 */
			private final int button;

			/**
			 * The config value.
			 */
			private final int configValue;

			/**
			 * The send item.
			 */
			private final int sendItem;

			/**
			 * The reward.
			 */
			private final Item reward;

			/**
			 * The items required.
			 */
			private final Item[] required;

			/**
			 * Constructs a new {@code ElnockExchange} {@code Object}.
			 * @param button the button.
			 * @param configValue the value.
			 * @param reward the reward.
			 * @param required the required.
			 */
			private ElnockExchange(int button, int configValue, final int sendItem, Item reward, Item... required) {
				this.button = button;
				this.configValue = configValue;
				this.reward = reward;
				this.sendItem = sendItem;
				this.required = required;
			}

			/**
			 * Gets the item.
			 * @param player the player,
			 * @return the item.
			 */
			public static Item getItem(Player player) {
				for (ImplingNode node : BNetTypes.getImplings()) {
					if (player.getInventory().containsItem(node.getReward())) {
						return node.getReward();
					}
				}
				return null;
			}

			/**
			 * Checks if the player has the items.
			 * @param player the player.
			 * @return {@code True} if so.
			 */
			public boolean hasItems(Player player) {
				return player.getInventory().containsItems(getRequired());
			}

			/**
			 * Gets the value.
			 * @param button the button.
			 * @return the value.
			 */
			public static ElnockExchange forButton(int button) {
				for (ElnockExchange e : values()) {
					if (e.getButton() == button) {
						return e;
					}
				}
				return null;
			}

			/**
			 * Gets the reward.
			 * @return The reward.
			 */
			public Item getReward() {
				return reward;
			}

			/**
			 * Gets the button.
			 * @return The button.
			 */
			public int getButton() {
				return button;
			}

			/**
			 * Gets the configValue.
			 * @return The configValue.
			 */
			public int getConfigValue() {
				return configValue;
			}

			/**
			 * Gets the required.
			 * @return The required.
			 */
			public Item[] getRequired() {
				return required;
			}

			/**
			 * Gets the sendItem.
			 * @return The sendItem.
			 */
			public int getSendItem() {
				return sendItem;
			}
		}
	}
}
