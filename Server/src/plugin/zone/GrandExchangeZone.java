package plugin.zone;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.content.dialogue.DialogueInterpreter;
import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.content.dialogue.FacialExpression;
import org.crandor.game.content.global.shop.Shop;
import org.crandor.game.content.global.shop.ShopViewer;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.Option;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.info.portal.DonatorType;
import org.crandor.game.node.entity.player.link.TeleportManager.TeleportType;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.node.object.ObjectBuilder;
import org.crandor.game.system.mysql.impl.ShopSQLHandler;
import org.crandor.game.system.task.Pulse;
import org.crandor.game.system.task.TaskExecutor;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.zone.MapZone;
import org.crandor.game.world.map.zone.ZoneBorders;
import org.crandor.game.world.map.zone.ZoneBuilder;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The grand exchange map zone.
 * @author Emperor
 * 
 */
@InitializablePlugin
public final class GrandExchangeZone extends MapZone implements Plugin<Object> {

	/**
	 * The store that sells items in exchange for credits.
	 */
	private static final CreditStore CREDIT_STORE = new CreditStore();

	/**
	 * Constructs a new {@code GrandExchangeZone} {@code Object}.
	 */
	public GrandExchangeZone() {
		super("grand exchange", true);
	}

	@Override
	public void configure() {
//		addObjects();
		PluginManager.definePlugin(new TeleporterDialogue(), new GnomeTravellerPlugin(), new RewardTraderDialogue());
		ShopSQLHandler.getUidShops().put(1485756, CREDIT_STORE);
		super.register(new ZoneBorders(3146, 3472, 3183, 3508));
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ZoneBuilder.configure(this);
		return this;
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public boolean interact(Entity entity, Node node, Option option) {
		if(entity instanceof Player){
			Player player = entity.asPlayer();
			switch(option.getName()){
			case "Talk-to":
				switch(node.getId()){
				case 8631:
					entity.asPlayer().getDialogueInterpreter().open(123656);
					return true;
				case 3322:
				case 8029:
				case 8174:
				case 8139:
					ShopSQLHandler.getShops().get(node.getId()).open(player);
					break;
				}
				break;
			case "Trade":
				switch(node.getId()){
				case 8631:
					CREDIT_STORE.open(entity.asPlayer());
					return true;
				}
				break;
			}
		}
		return super.interact(entity, node, option);
	}

	/**
	 * The store that sells items in exchange for credits. Point prices are read from the item config table.
	 * @author Splinter
	 */
	public static class CreditStore extends Shop {

		/**
		 * The default credit price if no item configuration was found.
		 */
		private final int DEFAULT_PRICE = 50;

		/**
		 * Constructs a new {@Code CreditStore} {@Code Object}
		 */
		public CreditStore() {
			super(GameWorld.getName() + " Credit Shop",
					new Item[]{
							new Item(962, 100), // Christmas cracker
							new Item(1050, 100), // Santa hat
							new Item(1053, 100), // Green h'ween mask
							new Item(1055, 100), // Blue h'ween mask
							new Item(1057, 100), // Red h'ween mask
							new Item(1959, 100), // Pumpkin
							new Item(1961, 100), // Easter egg
							new Item(4566, 100), // Rubber chicken
							new Item(1037, 100), // Bunny ears
							new Item(7927, 100), // Easter ring
					},
			false);
			setPointShop(true);
		}

		@Override
		public void open(final Player player) {
			TaskExecutor.executeSQL(new Runnable() {
				@Override
				public void run() {
					if (player.getDetails().getShop().syncCredits()) {
						CreditStore.super.open(player);
						int credits = player.getDetails().getShop().getCredits();
						player.sendMessage("<col=3498db>You currently have " + credits + (credits == 1 ? " credit" : " credits") + " to spend.");
					}
				}

			});
		}

		@Override
		public void buy(final Player player, final int slot, final int amount, final int tabIndex) {
			TaskExecutor.executeSQL(new Runnable() {
				@Override
				public void run() {
					if (player.getDetails().getShop().syncCredits()) {
						if(checkConditions(player, slot, amount, tabIndex)){
							CreditStore.super.buy(player, slot, amount, tabIndex);
							player.sendMessage("You now have <col=CC0000>"+player.getDetails().getShop().getCredits()+"</col> credit(s) remaining.");
						}	
					} else {
						player.sendMessages("Sorry, you cannot buy from the shop right now. Our database needs to sync", "some remaining data.");
					}
				} 
			});
		}

		@Override
		public boolean canSell(Player player, Item item, ItemDefinition def) {
			player.sendMessage("You cannot sell items to this store.");
			return false;
		}

		@Override
		public String getPointsName() {
			return "credit";
		}

		@Override
		public void value(Player player, ShopViewer viewer, Item item, boolean sell) {
			if(sell){
				player.sendMessage("You cannot sell items to this store.");
			} else {
				player.sendMessage(item.getName().contains("ring") ? "This item costs "+getBuyPrice(item, player)+" "+getPointsName()+"s plus a regular unimbued version of the ring." : "This item costs "+getBuyPrice(item, player)+" "+getPointsName()+"s.");	
			}
		}

		@Override
		public int getBuyPrice(Item item, Player player) {
			return item.getDefinition().getConfiguration("point_price", DEFAULT_PRICE);
		}

		@Override
		public void decrementPoints(Player player, int decrement) {
			player.getDetails().getShop().setCredits(player.getDetails().getShop().getCredits() - decrement, true);
		}

		@Override
		public int getPoints(Player player) {
			return player.getDetails().getShop().getCredits();
		}

		/**
		 * Checks to see if the player is eligible to buy the item.
		 * @return true if so.
		 */
		private boolean checkConditions(Player player, int slot, int amount, int tabIndex){
			String itemName = CreditStore.super.getItems()[slot].getName();
			int buyId = CreditStore.super.getItems()[slot].getId();
			if(itemName.contains("ring")){
				if(!player.getInventory().containsItem(getUnimbued(buyId, amount))){
					player.sendMessage("You need 35 credits plus a regular version of the ring in order to upgrade to imbued.");
					return false;	
				} else {
					if(player.getDetails().getShop().getCredits() > getBuyPrice(CreditStore.super.getItems()[slot], player) * amount){
						if(player.getInventory().remove(getUnimbued(buyId, amount))){
							return true;
						} else {
							player.sendMessage("You don't have that many unimbued rings.");
						}	
					} else{
						player.sendMessage("You don't have that many credits.");
						return false;
					}
				}
			} else {
				return true;
			}
			return false;
		}

		/**
		 * Gets the un-imbued ring ID.
		 * @return
		 */
		private Item getUnimbued(int imbuedRing, int amount){
			switch(imbuedRing){
			case 14810:
				return new Item(6737, amount);
			case 14808:
				return new Item(6733, amount);
			case 14809:
				return new Item(6735, amount);
			case 14807:
				return new Item(6731, amount);
			}
			return new Item(-1);
		}

	}

	/**
	 * Represents the dialogue plugin for the GE rewards trader
	 * @author Splinter
	 */
	@InitializablePlugin
	public final class RewardTraderDialogue extends DialoguePlugin {

		/**
		 * Constructs a new {@code RewardTraderDialogue} {@code Object}.
		 */
		public RewardTraderDialogue() {
			/**
			 * empty.
			 */
		}

		/**
		 * Constructs a new {@code RewardTraderDialogue} {@code Object}.
		 * @param player the player.
		 */
		public RewardTraderDialogue(Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new RewardTraderDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			interpreter.sendDialogues(8631, FacialExpression.OSRS_NORMAL, "Greetings, "+player.getUsername()+". I offer a wide assortment", "of valuable goods that you may be interested in.");
			stage = 0;
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (stage) {
			case 0:
				interpreter.sendDialogues(8631, FacialExpression.OSRS_NORMAL, "The items I have for sale are in exchange for your", "continued dedication in assisting "+GameWorld.getName()+". Please","continue to show support by voting daily on our", "website.");
				stage = 1;
				break;
			case 1:
				interpreter.sendOptions("Select an Option", "How do I obtain credits?", "Let me see your store.", "Nevermind.");
				stage = 2;
				break;
			case 2:
				switch(buttonId){
				case 1:
					interpreter.sendDialogues(player, null, "How do I obtain "+GameWorld.getName()+" credits?");
					stage = 3;
					break;
				case 2:
					interpreter.sendDialogues(player, null, "Let me see your store.");
					stage = 10;
					break;
				case 3:
					interpreter.sendDialogues(player, null, "Nevermind.");
					stage = 15;
					break;
				}
				break;
			case 3:
				interpreter.sendDialogues(8631, FacialExpression.OSRS_NORMAL, "You receive 1 credit for every 10 levels gained");
				stage = 4;
				break;
			case 4:
				interpreter.sendDialogues(8631, FacialExpression.OSRS_NORMAL, "You may have to log out of your account and back in", "for your credits to visually appear in your account.", "To view how many credits you have, you may type", "::shop or ::credits while in-game.");
				stage = 1;
				break;
			case 10:
				interpreter.sendDialogues(8631, FacialExpression.OSRS_NORMAL, "Certainly.");
				stage = 11;
				break;
			case 11:
				end();
				CREDIT_STORE.open(player);
				break;
			case 15: 
				end();
				break;
			}
			return true;
		}

		@Override
		public int[] getIds() {
			return new int[] { 8631, 123656 };
		}
	}

	/**
	 * Represents the dialogue plugin for the GE Teleporter
	 * @author Splinter
	 * 
	 */
	@InitializablePlugin
	public static final class TeleporterDialogue extends DialoguePlugin {

		/**
		 * The teleport destinations.
		 */
		private static final Object[][][] TELEPORTS = new Object[][][] {
			{{"Training"}, {"Cows", Location.create(3256, 3266, 0)}, {"Stronghold of Security", Location.create(3080, 3421, 0)}, {"Rock crabs", Location.create(2672, 3712, 0)}, {"Al-Kharid Warriors", Location.create(3293, 3180, 0)}, {"Neitiznot Yaks", Location.create(2326, 3803, 0)}, {"Desert Bandits", Location.create(3176, 2987, 0)}, {"TzHaar", Location.create(2450, 5165, 0)}, {"Fremmenik Slayer Dungeon", Location.create(2796, 3615, 0)}, {"H.A.M. Hideout", Location.create(3149, 9652, 0)}, {"Armoured Zombies", Location.create(3239, 3606, 0)}, {"Asgarnian Ice Caves", Location.create(3014, 9579, 0)}, {"Slayer Tower", Location.create(3429, 3535, 0)}, {"Brimhaven Dungeon (bring 875 gp)", Location.create(2744, 3152, 0)}, {"Brine Rat Cave", Location.create(2690, 10124, 0)}, {"Ape Atoll Dungeon", Location.create(2764, 9103, 0)}},
			{{"Cities"}, {"Lumbridge", Location.create(3222, 3217, 0)}, {"Falador", Location.create(2965, 3380, 0)}, {"Edgeville", Location.create(3088, 3491, 0)}, {"Varrock", Location.create(3213, 3428, 0)}, {"Ardougne", Location.create(2663, 3305, 0)}, {"Seer's Village", Location.create(2713, 3484, 0)}, {"Burthorpe", Location.create(2898, 3544, 0)}, {"Rellekka", Location.create(2659, 3657, 0)}, {"Neitiznot", Location.create(2335, 3803, 0)}, {"Al-Kharid", Location.create(3293, 3184, 0)}, {"Karamja/Brimhaven", Location.create(2910, 3152, 0)}, {"Yanille", Location.create(2605, 3093, 0)}, {"Mos'le Harmless", Location.create(3676, 2975, 0)}, {"Tree Gnome Stronghold", Location.create(2450, 3422, 0)}, {"Shilo Village", Location.create(2849, 2961, 0)}, {"Piscatoris Fishing Colony", Location.create(2344, 3655, 0)}},
			{{"Minigames"}, {"Pest Control", Location.create(2659, 2649, 0)}, {"Duel Arena", Location.create(3352, 3265, 0)}, {"Sorceress' Garden", Location.create(3315, 3163, 0)}, {"Warrior's Guild", Location.create(2881, 3546, 0)}, {"Puro Puro(requires Lost City)", Location.create(2592, 4317, 0)}, {"Fight Pits", Location.create(2399, 5178, 0)}, {"Clan Wars", Location.create(3272, 3687, 0)}, {"Magic Training Arena", Location.create(3363, 3298, 0)}}, 
		};

		/**
		 * The lastIndex displayed.
		 */
		private int firstIndex;

		/**
		 * The option index.
		 */
		private int optionIndex;

		/**
		 * Constructs a new {@code TeleporterDialogue} {@code Object}.
		 */
		public TeleporterDialogue() {
			/**
			 * empty.
			 */
		}

		/**
		 * Constructs a new {@code TeleporterDialogue} {@code Object}.
		 * @param player the player.
		 */
		public TeleporterDialogue(Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new TeleporterDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(player.getSavedData().getGlobalData().getGlobalTeleporterDelay() - System.currentTimeMillis());
			if (minutes < 1) {
				minutes = 1;
			}
			interpreter.sendItemMessage(563, "This is the " + GameWorld.getName() + " teleporter. You may use it every","five minutes to freely teleport to many places.", (player.getSavedData().getGlobalData().getGlobalTeleporterDelay() > System.currentTimeMillis() ? "<col=CC4000>You are on cooldown for the next "+ (minutes)+" minute(s)." : ""));
			stage = -5;
			return true;
		}


		@Override
		public boolean handle(int interfaceId, int buttonId) {
			int index;
			switch (stage) {
			case -10:
				end();
				break;
			case -5:
				interpreter.sendOptions("Select an Option", "Buy teletabs", "Use teleporter");
				stage = 0;
				break;
			case 0:
				switch(buttonId){
				case 1:
					end();
					ShopSQLHandler.openUid(player, 200);
					break;
				case 2:
					/*if (player.getSavedData().getGlobalData().getGlobalTeleporterDelay() > System.currentTimeMillis()) {
						long millis = player.getSavedData().getGlobalData().getGlobalTeleporterDelay() - System.currentTimeMillis();
						int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(millis);
						if (minutes < 1) {
							minutes = 1;
						}
						interpreter.sendDialogue("You need to wait " + minutes + " more minute" + (minutes > 1 ? "s" : "") + " in order to use the free", "teleportation system again.");
						stage = -10;
						return true;
					}*/
					player.removeAttribute("global_teleporter");
					String[] options = new String[TELEPORTS.length];
					for (int i = 0; i < TELEPORTS.length; i++) {
						options[i] = (String) TELEPORTS[i][0][0];
					}
					interpreter.sendOptions("Select an Option", options);
					stage = 1;
					break;
				}
				break;
			case 1:
				index = buttonId - 1;
				optionIndex = index;
				sendOptions();
				stage = 2;
				break;
			case 2:
				index = buttonId - 1;
				int teleIndex = (firstIndex + index) + 1;
				int optionSize = 3;
				for (int i = 0; i < 3; i++) {
					if (firstIndex + 1 + i > TELEPORTS[optionIndex].length-1) {
						optionSize = i;
						break;
					}
				}
				if (teleIndex > firstIndex + optionSize) {
					firstIndex = firstIndex + optionSize;
					sendOptions();
					break;
				}
				Object teleports[] = TELEPORTS[optionIndex][teleIndex];
				if (optionIndex == 2 && teleports[teleIndex] == "Puro Puro(requires Lost City)" && !player.getQuestRepository().isComplete("Lost City")) {
					interpreter.sendDialogue("You need to have completed Lost City to teleport here.");
					break;
				}
				send(player, (Location) teleports[1]);
				break;
			}
			return true;
		}

		/**
		 * Sends the options.
		 */
		private void sendOptions() {
			if (firstIndex >= TELEPORTS[optionIndex].length -1) {
				firstIndex = 0;
			}
			int optionSize = 3;
			Object[][] data = TELEPORTS[optionIndex];
			for (int i = 0; i < 3; i++) {
				if (firstIndex + 1 + i > data.length-1) {
					optionSize = i;
					break;
				}
			}
			String [] options = new String[optionSize + 1];
			for (int i = 0; i < optionSize; i++) {
				options[i] = (String) data[firstIndex + (i + 1)][0];
			}
			options[options.length - 1] = "More Options";
			interpreter.sendOptions("Select an Option", options);
		}

		/**
		 * Sends the player to the specified location.
		 */
		private void send(final Player player, final Location loc){
			player.getDialogueInterpreter().close();
			player.getPulseManager().run(new Pulse(1, player) {
				int counter = 5;
				@Override
				public boolean pulse() {
					switch(--counter){
					case -1:
						player.getInterfaceManager().closeChatbox();
						player.getSavedData().getGlobalData().setGlobalTeleporterDelay(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(player.getDonatorType() == DonatorType.EXTREME ? 1 : player.getDonatorType() == DonatorType.REGULAR ? 3 : 5));
						player.getTeleporter().send(loc, TeleportType.TELE_OTHER);
						return true;
					default:
						player.getDialogueInterpreter().sendPlainMessage(true, "You will be teleported in... "+counter+".", "Move away to cancel.");
						return false;
					}
				}
			});
		}

		@Override
		public int[] getIds() {
			return new int[] { 543543 };
		}
	}

	/**
	 * Handles the gnome traveller whom buys low level skilling items.
	 * @author Vexia
	 *
	 */
	public static class GnomeTravellerPlugin extends UseWithHandler {

		/**
		 * The items the gnome accepts.
		 */
		private static final Object[][] ITEMS = new Object[][] {{1511, 70}, {1521, 120}, {1519, 90}, {434, 50}, {436, 70}, {438, 70}, {440, 120}, {317, 50}, {315, 70}, {327, 50}, {325, 70}, {321, 50}, {319, 70}, {335, 50}, {333, 70}, {249, 145}, {199, 130}, {253, 200}, {203, 190}, {255, 220}, {205, 160}, {2351, 410}, {52, 10}, {2349, 135}};

		/**
		 * Constructs the gnome traveller plugin.
		 */
		public GnomeTravellerPlugin() {
			super(getItemIds());
		}

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			addHandler(2138, NPC_TYPE, this);
			PluginManager.definePlugin(new GnomeDialogue());
			return this;
		}

		@Override
		public boolean handle(NodeUsageEvent event) {
			int price = getPrice(event.getUsedItem().getId());
			if (price < 1 && event.getUsedItem().getDefinition().getNoteId() > -1) {
				price = getPrice(event.getUsedItem().getDefinition().getNoteId());
			}
			if (price < 1) {
				return false;
			}
			Player player = event.getPlayer();
			player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("gnome-traveller"), new Item(event.getUsedItem().getId(), player.getInventory().getAmount(event.getUsedItem())), price);
			return true;
		}

		/**
		 * Gets the price of an item.
		 * @param itemId The item id.
		 * @return The price.
		 */
		public int getPrice(int itemId) {
			for (int i = 0; i < ITEMS.length; i++) {
				if (itemId == (int) ITEMS[i][0]) {
					return (int) ITEMS[i][1];
				}
			}
			return 0;
		}

		/**
		 * Gets the item ids.
		 * @return The item ids.
		 */
		public static int[] getItemIds() {
			List<Integer> items = new ArrayList<>();
			for (int i = 0; i < ITEMS.length; i++) {
				items.add((int) ITEMS[i][0]);
				if (ItemDefinition.forId((int) ITEMS[i][0]).getNoteId() > -1 ) {
					items.add(ItemDefinition.forId((int) ITEMS[i][0]).getNoteId());
				}
			}
			int[] array = new int[items.size()];
			for (int i = 0; i < array.length; i++) {
				array[i] = items.get(i);
			}
			return array;
		}

		/**
		 * The gnome dialogue.
		 * @author Vexia
		 *
		 */
		@InitializablePlugin
		public class GnomeDialogue extends DialoguePlugin {

			/**
			 * The item used.
			 */
			private Item item;

			/**
			 * The price offered.
			 */
			private int price;

			/**
			 * Constructs the {@code GnomeDialogue}
			 * @param player The player.
			 */
			public GnomeDialogue(Player player) {
				super(player);
			}

			/**
			 * Constructs the {@code GnomeDialogue}
			 */
			public GnomeDialogue() {
				/*
				 * empty.
				 */
			}

			@Override
			public DialoguePlugin newInstance(Player player) {
				return new GnomeDialogue(player);
			}

			@Override
			public boolean open(Object... args) {
				item = (Item) args[0];
				price = (int) args[1];
				player.getDialogueInterpreter().sendDialogues(2138, FacialExpression.OSRS_NORMAL, "A total of " + item.getAmount() + " to sell, not too shabby.", "I'll offer you " + price + " for each for a total of", price * item.getAmount() + " gold.");;
				stage = 1;
				return true;
			}

			@Override
			public boolean handle(int interfaceId, int buttonId) {
				switch (stage) {
				case 1:
					options("Sell " + item.getAmount() + " for " + price + " each.", "No.");
					stage++;
					break;
				case 2:
					switch (buttonId) {
					case 1:
						if (player.getInventory().remove(item)) {
							player.getInventory().add(new Item(995, price * item.getAmount()));
							interpreter.sendItemMessage(new Item(995, price), "You hand over your items in exchange for gold.");
							stage = 5;
							break;
						}
						end();
						break;
					case 2:
						player("I think I will hold on to these for now.");
						stage = 3;
						break;
					}
					break;
				case 3:
					player.getDialogueInterpreter().sendDialogues(2138, FacialExpression.OSRS_NORMAL, "Very well. I will be here if you decide to sell me", "raw materials.", "Just keep in mind I offer a better bargain than",  "most other vendors!");
					stage++;
					break;
				case 4:
					end();
					break;
				case 5:
					player.getDialogueInterpreter().sendDialogues(2138, FacialExpression.OSRS_NORMAL, "Pleasure doing business with you, " + player.getUsername() + ".");
					stage = 4;
					break;
				}
				return true;
			}

			@Override
			public int[] getIds() {
				return new int[] {	DialogueInterpreter.getDialogueKey("gnome-traveller")};
			}

		}
	}

	/**
	 * The objects to spawn in the Grand Exchange Zone.
	 */
	private final void addObjects(){
		ObjectBuilder.add(new GameObject(11402, new Location(3160, 3468)));
		ObjectBuilder.add(new GameObject(11402, new Location(3159, 3468)));
		ObjectBuilder.add(new GameObject(1161, new Location(3149, 3471)));
		ObjectBuilder.add(new GameObject(1161, new Location(3149, 3470)));
		ObjectBuilder.add(new GameObject(1161, new Location(3151, 3470)));
		ObjectBuilder.add(new GameObject(1161, new Location(3150, 3470)));
		ObjectBuilder.add(new GameObject(1161, new Location(3151, 3471)));
		ObjectBuilder.add(new GameObject(1161, new Location(3150, 3471)));
		ObjectBuilder.add(new GameObject(1161, new Location(3152, 3471)));
		ObjectBuilder.add(new GameObject(1161, new Location(3152, 3470)));
		ObjectBuilder.add(new GameObject(29954, new Location(3155, 3469)));
		ObjectBuilder.add(new GameObject(2732, new Location(3182, 3474)));
		ObjectBuilder.add(new GameObject(24343, new Location(3168, 3468), 10, 4));
		ObjectBuilder.add(new GameObject(36781, new Location(3176, 3509)));
		ObjectBuilder.add(new GameObject(17985, new Location(3179, 3471), 22, 1));
		ObjectBuilder.add(new GameObject(2092, new Location(3153, 3510)));
		ObjectBuilder.add(new GameObject(2092, new Location(3154, 3511)));
		ObjectBuilder.add(new GameObject(2092, new Location(3152, 3513), 2));
		ObjectBuilder.add(new GameObject(17010, new Location(3151, 3484), 1));
		ObjectBuilder.add(new GameObject(6552, new Location(3149, 3487), 1));
		ObjectBuilder.add(new GameObject(2096, new Location(3159, 3512)));
		ObjectBuilder.add(new GameObject(2097, new Location(3157, 3513)));
		ObjectBuilder.add(new GameObject(11666, Location.create(3142, 3487, 0)));
		ObjectBuilder.add(new GameObject(2783, Location.create(3143, 3490, 0)));
		ObjectBuilder.add(new GameObject(13656, Location.create(3173, 3485, 0)));
	}
}