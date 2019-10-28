package plugin.skill.farming;

import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.content.skill.member.farming.FarmingItemHolder;
import org.crandor.game.content.skill.member.farming.FarmingPatch;
import org.crandor.game.content.skill.member.farming.wrapper.PatchWrapper;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.RegionManager;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;
import org.crandor.tools.StringUtils;

/**
 * Represents the plguin used for the crop watcher.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class GardenerPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (Gardener gardener : Gardener.values()) {
			NPCDefinition.forId(gardener.getNpc()).getConfigurations().put("option:pay", this);
			NPCDefinition.forId(gardener.getNpc()).getConfigurations().put("option:pay (north)", this);
			NPCDefinition.forId(gardener.getNpc()).getConfigurations().put("option:pay (south)", this);
			NPCDefinition.forId(gardener.getNpc()).getConfigurations().put("option:pay (north-west)", this);
			NPCDefinition.forId(gardener.getNpc()).getConfigurations().put("option:pay (south-east)", this);
		}
		new GardenerDialogue().init();
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final NPC npc = (NPC) node;
		player.getDialogueInterpreter().open(npc.getId(), npc, option);
		return true;
	}

	/**
	 * Represents the dialogue plugin used for the crop watcher.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public static final class GardenerDialogue extends DialoguePlugin {

		/**
		 * Represents the items needed to removr a tree.
		 */
		private static final Item TREE_COINS = new Item(995, 200);

		/**
		 * Represents the gardener.
		 */
		private Gardener gardener;

		/**
		 * Represents the object.
		 */
		private GameObject object;

		/**
		 * Represents the wrapper being used.
		 */
		private PatchWrapper wrapper;

		/**
		 * Represents the pay option.
		 */
		private String option;

		/**
		 * Constructs a new {@code CropwatcherDialogue} {@code Object}.
		 * @param player the player.
		 */
		public GardenerDialogue(final Player player) {
			super(player);
		}

		/**
		 * Constructs a new {@code CropwatcherDialogue} {@code Object}.
		 */
		public GardenerDialogue() {
			/**
			 * empty.
			 */
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new GardenerDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			npc = (NPC) args[0];
			if (args.length > 1) {
				option = (String) args[1];
				if (!setData()) {
					end();
					player.sendMessage("Error occured! Report to admin.");
					return true;
				}
				if (!canProtect()) {
					return true;
				}
				if (!hasItem()) {
					npc("I want " + getItemName() + " for that.");
					stage = 11;
				} else {
					interpreter.sendOptions("Pay " + getItemName() + "?", "Yes", "No");
					stage = 15;
				}
				return true;
			}
			options("Would you look after my crops for me?", "Can you give me any farming advice?", "Can you sell me something?", "That's all thanks.");
			stage = 0;
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (stage) {
			case 10:
				gardener = Gardener.forId(npc.getId());
				if (gardener.getLocations().length > 1) {
					options(npc.getDefinition().getOptions()[2], npc.getDefinition().getOptions()[3]);
					stage = 82;
					return true;
				}
				if (!setData()) {
					end();
					player.sendMessage("Error occured! Report to admin.");
					return true;
				}
				if (!canProtect()) {
					return true;
				}
				if (!hasItem()) {
					npc("If you like, but I want " + getItemName() + " for that.");
					stage = 11;
				} else {
					options("Okay, it's a deal.", "No, that's too much.");
					stage = 16;
				}
				break;
			case 11:
				player("I'm afraid I don't have enough of those at the moment.");
				stage = 12;
				break;
			case 12:
				npc("Well, I'm not wasting my time for free.");
				stage = 34;
				break;
			case 13:
				player("Thanks, maybe another time.");
				stage = 34;
				break;
			case 15:
				switch (buttonId) {
				case 1:
					pay();
					break;
				case 2:
					end();
					break;
				}
				break;
			case 16:
				switch (buttonId) {
				case 1:
					player("Okay, it's a deal.");
					stage = 17;
					break;
				case 2:
					player("No, that's too much.");
					stage = 12;
					break;
				}
				break;
			case 17:
				pay();
				break;
			case 18:
				end();
				break;
			case 80:// remove tree.
				switch (buttonId) {
				case 1:
					if (!player.getInventory().containsItem(TREE_COINS)) {
						player("Sorry, I don't seem to have enough money.");
						stage = 34;
						return true;
					}
					end();
					if (player.getInventory().remove(TREE_COINS)) {
						interpreter.sendDialogue("The gardener obligingly removes your tree.");
						wrapper.getCycle().clear(player);
						return true;
					}
					break;
				case 2:
					player("No thanks.");
					stage = 34;
					break;
				}
				break;
			case 82:// pay (south), etc
				switch (buttonId) {
				case 1:
					option = npc.getDefinition().getOptions()[2];
					break;
				case 2:
					option = npc.getDefinition().getOptions()[3];
					break;
				}
				setData();
				pay();
				break;
			case 0:
				switch (buttonId) {
				case 1:
					player("Would you look after my crops for me?");
					stage = 10;
					break;
				case 2:
					player("Can you give me farming advice?");
					stage = 20;
					break;
				case 3:
					player("Can you sell me something?");
					stage = 30;
					break;
				case 4:
					player("That's all, thanks.");
					stage = 40;
					break;
				}
				break;
			case 20:
				npc("There's four main Farming areas - Elstan looks after", "an area south of Falador, Dantaera has one to the", "north of Catherby, Krage has one near Ardougne,", "and Lyra looks after a place in north Morytania.");
				stage = 21;
				break;
			case 21:
				options("Would you look after my crops for me?", "Can you give me any farming advice?", "Can you sell me something?", "That's all thanks.");
				stage = 0;
				break;
			case 30:
				npc("That depends on whether I have it to sell.", "What is it that you're looking for?");
				stage = 31;
				break;
			case 31:
				options("Some plant cure.", "A bucket of compost.", "A rake.", "(See more items)");
				stage = 32;
				break;
			case 32:
				switch (buttonId) {
				case 1:
					player("Some plant cure.");
					purchase(ShopItem.PLANT_CURE);
					break;
				case 2:
					player("A bucket of compost.");
					purchase(ShopItem.COMPOST);
					break;
				case 3:
					player("A rake.");
					purchase(ShopItem.RAKE);
					break;
				case 4:
					options("A watering can.", "A gardening trowel.", "A seed dibber.", "(See previous items)", "Forget it.");
					stage = 33;
					break;
				}
				break;
			case 33:
				switch (buttonId) {
				case 1:
					player("A watering can.");
					purchase(ShopItem.WATERING_CAN);
					break;
				case 2:
					player("A gardening trowel.");
					purchase(ShopItem.GARDENING_TROWEL);
					break;
				case 3:
					player("A seed dibber.");
					purchase(ShopItem.SEED_DIBBER);
					break;
				case 4:
					options("Some plant cure.", "A bucket of compost.", "A rake.", "(See more items)");
					stage = 32;
					break;
				case 5:
					player("Forget it, you don't have anything I need.");
					stage = 34;
					break;
				}
				break;
			case 34:
				end();
				break;
			case 35:
				options("Yes, that sounds like a fair price.", "No thanks, I can get that much cheaper elsewhere.");
				stage = 36;
				break;
			case 36:
				switch (buttonId) {
				case 1:
					player("Yes, that sounds like a fair price.");
					stage = 37;
					break;
				case 2:
					player("No thanks, I can get that much cheaper elsewhere.");
					stage = 34;
					break;
				}
				break;
			case 37:
				final ShopItem item = player.getAttribute("farming:shop-item", ShopItem.PLANT_CURE);
				if (item.purchase(player)) {
					end();
				} else {
					stage = 40;
				}
				break;
			case 40:
				end();
				break;
			}
			return true;
		}

		/**
		 * Sets the data.
		 */
		private boolean setData() {
			if (npc == null || option == null) {
				return false;
			}
			gardener = Gardener.forId(npc.getId());
			if (gardener == null) {
				return false;
			}
			object = RegionManager.getObject(gardener.getLocations()[option == null ? 0 : option.equals("pay") || option.contains("north") ? 0 : 1]);
			if (object == null) {
				return false;
			}
			wrapper = player.getFarmingManager().getPatchWrapper(object.getWrapper().getId());
			return wrapper != null;
		}

		/**
		 * Method used to pay the gardener.
		 */
		private void pay() {
			Item item = getItem();
			if (item == null) {
				return;
			}
			if (!canProtect() || !player.getInventory().containsItem(item)) {
				return;
			}
			if (player.getInventory().remove(item)) {
				wrapper.getCycle().setProtection(true);
				npc("That'll do nicely, Sir. Leave it with me - I'll make sure it", "grows for you.");
				stage = 18;
			}
		}

		/**
		 * Gets the item name.
		 * @return the name.
		 */
		private String getItemName() {
			final Item item = getItem();
			if (item == null) {
				return "report-me";
			}
			final int amount = item.getAmount();
			final FarmingItemHolder holder = FarmingItemHolder.forHolder(item);
			String name = item.getName().toLowerCase();
			if (holder != null) {
				if (holder.isBasket()) {
					name = String.valueOf(amount) + " basket of " + holder.name().toLowerCase() + "s";
				} else {
					name = String.valueOf(amount) + " sack of " + holder.name().toLowerCase() + "s";
				}
			}
			if (amount < 2) {
				return name;
			} else {
				if (holder == null) {
					return amount + " " + name + "s";
				} else {
					return name;
				}
			}
		}

		/**
		 * Checks if the player has the required item.
		 * @return {@code True} if so.
		 */
		private boolean hasItem() {
			return getItem() != null && player.getInventory().containsItem(getItem());
		}

		/**
		 * Gets the item to use.
		 * @return the item.
		 */
		private Item getItem() {
			if (wrapper == null || wrapper.getNode() == null || wrapper.getNode().getProtection() == null) {
				return null;
			}
			final Item[] items = wrapper.getNode().getProtection();
			if (items == null) {
				return null;
			}
			if (items.length <= 0) {
				return null;
			}
			return items[0];
		}

		/**
		 * Checks if the patch can be protected.
		 * @return {@code True} if so.
		 */
		private boolean canProtect() {
			if (wrapper.getCycle().getDiseaseHandler().isDiseased() || wrapper.getCycle().getDeathHandler().isDead()) {
				npc("You need to plant a healthy plant in that patch.");
				stage = 34;
				return false;
			}
			if (wrapper.getCycle().getGrowthHandler().isFullGrown() && (wrapper.getPatch() == FarmingPatch.TREE || wrapper.getPatch() == FarmingPatch.FRUIT_TREE)) {
				interpreter.sendOptions("Pay 200 gp to remove the tree?", "Yes, get rid of the tree.", "No thanks.");
				stage = 80;
				return false;
			}
			if (wrapper.getCycle().isProtected()) {
				npc("I'm already looking after that patch for you.");
				stage = 34;
				return false;
			}
			if (!wrapper.getCycle().getGrowthHandler().isGrowing() && !wrapper.getCycle().getWaterHandler().isWatered()) {
				npc("You don't have anything planted in that patch. Plant", "something and I might agree to look after it for you.");
				stage = 34;
				return false;
			}
			if (wrapper.getNode().getProtection() == null) {
				npc("Sorry, I can't watch over that plant.");
				stage = 34;
				return false;
			}
			return true;
		}

		/**
		 * Method used to attempt to buy an item.
		 * @param item the item.
		 */
		private void purchase(ShopItem item) {
			player.setAttribute("farming:shop-item", item);
			final String type = (item.equals(ShopItem.RAKE) || item.equals(ShopItem.WATERING_CAN) || item.equals(ShopItem.GARDENING_TROWEL) || item.equals(ShopItem.SEED_DIBBER) ? "one" : "some");
			if (item == ShopItem.GARDENING_TROWEL || item == ShopItem.SEED_DIBBER) {
				npc("A " + item.getItem().getName().toLowerCase() + ", eh? I might have one spare ... tell", "you what, I'll sell it to you for 15GP if you like.");
			} else {
				npc(StringUtils.formatDisplayName(item.getItem().getName()) + ", eh? I might have " + type + " of those.", "Tell you what, I'll sell you " + type + " " + item.getItem().getName().toLowerCase() + " for " + item.getPrice().getAmount() + "", "GP if you like.");
			}
			stage = 35;
		}

		@Override
		public int[] getIds() {
			final int[] ids = new int[Gardener.values().length];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = Gardener.values()[i].getNpc();
			}
			return ids;
		}

	}

	/**
	 * Represents a shop item that can be purchased.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public enum ShopItem {
		PLANT_CURE(new Item(6036), new Item(995, 25)), COMPOST(new Item(6032), new Item(995, 35)), RAKE(new Item(5341), new Item(995, 15)), WATERING_CAN(new Item(5331), new Item(995, 25)), GARDENING_TROWEL(new Item(5325), new Item(995, 15)), SEED_DIBBER(new Item(5343), new Item(995, 15));

		/**
		 * Represents the item.
		 */
		private final Item item;

		/**
		 * Represents the price.
		 */
		private final Item price;

		/**
		 * Constructs a new {@code ShopItem} {@code Object}.
		 * @param item the item.
		 * @param price the price.
		 */
		ShopItem(final Item item, final Item price) {
			this.item = item;
			this.price = price;
		}

		/**
		 * Checks if the purchase was a success.
		 * @param player the player.
		 * @return {@code True} if so.
		 */
		public boolean purchase(final Player player) {
			if (!player.getInventory().hasSpaceFor(item)) {
				player.getDialogueInterpreter().sendDialogues(player, null, "Sorry, I don't seem to have enough inventory space.");
				return false;
			}
			if (!player.getInventory().containsItem(price)) {
				player.getDialogueInterpreter().sendDialogues(player, null, "Sorry, I don't seem to have enough coins with me", "at this time.");
				return false;
			}
			return player.getInventory().remove(price) && player.getInventory().add(item);
		}

		/**
		 * Gets the item.
		 * @return The item.
		 */
		public Item getItem() {
			return item;
		}

		/**
		 * Gets the price.
		 * @return The price.
		 */
		public Item getPrice() {
			return price;
		}

	}

	/**
	 * Represents a crop watcher npc.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public enum Gardener {
		ALAIN(2339, new Location(2935, 3437, 0)), TREZNOR(2341, new Location(3228, 3458, 0)), AMAETHWR(2860, new Location(2345, 3162, 0)), BOLONGO(2343, new Location(2475, 3445, 0)), DANTAERA(2324, new Location(2808, 3467, 0), new Location(2808, 3459, 0)), DREVEN(2335, new Location(3181, 3357, 0)), ELLENA(2331, new Location(2860, 3433, 0)), ELSTAN(2323, new Location(3051, 3308, 0), new Location(3058, 3308, 0)), GARTH(2330, new Location(2764, 3212, 0)), GILETH(2344, new Location(2489, 3179, 0)), FAYETH(2342, new Location(3192, 3230, 0)), FRANCIS(2327, new Location(3810, 3336, 0)), FRIZZY_SKERNIP(4560, new Location(3059, 3257, 0)), HESKEL(2340, new Location(3003, 3372, 0)), KRAGEN(2325, new Location(2665, 3378, 0), new Location(2667, 3371, 0)), LYRA(2326, new Location(3605, 3525, 0), new Location(3598, 3526, 0)), MY_ARM(4947, new Location(2826, 3694, 0)), PRAISTAN_EBOLA(4562, new Location(2801, 3202, 0)), PRISSY_SCILLA(1037, new Location(2435, 3414, 0)), ROHNEN(2334, new Location(2664, 3525, 0)), SELENA(2332, new Location(2575, 3106, 0)), TARIA(2336, new Location(2940, 3221, 0)), TORRELL(2338, new Location(2617, 3225, 0)), VASQUEN(2333, new Location(3227, 3315, 0));

		/**
		 * Represents the npc id.
		 */
		private final int npc;

		/**
		 * Represents the location of patches.
		 */
		private final Location[] locations;

		/**
		 * Constructs a new {@code Gardener} {@code Object}.
		 * @param npc the npc.
		 * @param locations the locations.
		 */
		Gardener(int npc, final Location... locations) {
			this.npc = npc;
			this.locations = locations;
		}

		/**
		 * Gets the gardener for the id.
		 * @param id the id.
		 * @return the gardener.
		 */
		public static Gardener forId(final int id) {
			for (Gardener gardener : values()) {
				if (gardener.getNpc() == id) {
					return gardener;
				}
			}
			return null;
		}

		/**
		 * Gets the locations.
		 * @return The locations.
		 */
		public Location[] getLocations() {
			return locations;
		}

		/**
		 * Gets the npc.
		 * @return The npc.
		 */
		public int getNpc() {
			return npc;
		}

	}
}
