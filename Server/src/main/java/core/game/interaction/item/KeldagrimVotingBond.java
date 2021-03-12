//package core.game.interaction.item;
//
//import java.text.DecimalFormat;
//import java.util.concurrent.TimeUnit;
//
//import core.cache.def.impl.ItemDefinition;
//import core.game.content.dialogue.DialogueInterpreter;
//import core.game.content.dialogue.DialoguePlugin;
//import core.game.content.ttrail.ClueLevel;
//import core.game.content.ttrail.ClueScrollPlugin;
//import core.game.interaction.OptionHandler;
//import core.game.node.Node;
//import core.game.node.entity.player.Player;
//import core.game.node.entity.state.EntityState;
//import core.game.node.item.Item;
//import rs09.game.world.repository.Repository;
//import core.plugin.Plugin;
//import rs09.plugin.PluginManager;
//import core.plugin.InitializablePlugin;
//import core.tools.RandomFunction;
//
///**
// * Handles the keldagrim voting bond item.
// * @author Vexia
// *
// */
//@InitializablePlugin
//public class KeldagrimVotingBond extends OptionHandler {
//
//	/**
//	 * The keldagrim bond item.
//	 */
//	private static final Item BOND = new Item(14807);
//
//	/**
//	 * The ultra lamp item.
//	 */
//	private static final Item ULTRA_LAMP = new Item(14820);
//
//	@Override
//	public Plugin<Object> newInstance(Object arg) throws Throwable {
//		ItemDefinition.forId(14807).getConfigurations().put("option:redeem", this);
//		ItemDefinition.forId(14807).getConfigurations().put("option:deposit", this);
//		PluginManager.definePlugin(new keldagrimVotingBondDialogue());
//		return this;
//	}
//
//	@Override
//	public boolean handle(Player player, Node node, String option) {
//		Item item = node.asItem();
//		switch (option) {
//		case "redeem":
//			player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("keldagrim-bond"));
//			break;
//		case "deposit":
//			if (!player.getBank().hasSpaceFor(item)) {
//				player.sendMessage("You don't have enough space in your bank for that item.");
//				return true;
//			}
//			if (player.getInventory().remove(item)) {
//				player.getBank().add(item);
//				player.sendMessage("You deposit your Reward bond into your bank.");
//			}
//			break;
//		}
//		return true;
//	}
//
//	@Override
//	public boolean isWalk() {
//		return false;
//	}
//
//	/**
//	 * Handles the keldagrim voting bond dialogue.
//	 * @author Vexia
//	 *
//	 */
//	public class keldagrimVotingBondDialogue extends DialoguePlugin {
//
//		/**
//		 * Constructs the {@code keldagrimVotingBondDialogue}
//		 */
//		public keldagrimVotingBondDialogue() {
//			/**
//			 * empty.
//			 */
//		}
//
//		/**
//		 * Constructs the {@code keldagrimVotingBondDialogue}
//		 * @param player The player.
//		 */
//		public keldagrimVotingBondDialogue(Player player) {
//			super(player);
//		}
//
//		@Override
//		public DialoguePlugin newInstance(Player player) {
//			return new keldagrimVotingBondDialogue(player);
//		}
//
//		@Override
//		public boolean open(Object... args) {
//			options("Double Experience (1 Hour)", "30K Experience Lamp", "10k-75k Coins", "Clue Scroll");
//			stage = 0;
//			return true;
//		}
//
//		@Override
//		public boolean handle(int interfaceId, int buttonId) {
//			switch (stage) {
//			case 0:
//				stage = 1;
//				switch (buttonId) {
//				case 1:
//					if (player.getSavedData().getGlobalData().hasDoubleExp()) {
//						interpreter.sendItemMessage(14807, "You already have <col=FF0000>double EXP</col> active!");
//						return true;
//					}
//					if (player.getInventory().remove(BOND)) {
//						player.getSavedData().getGlobalData().setDoubleExp(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));
//						player.getStateManager().set(EntityState.DOUBLE_EXPERIENCE, 6000, 0);
//						interpreter.sendItemMessage(14807, "You redeemed an <col=FF0000>hour</col> of double EXP!");
//						Repository.sendNews("" + player.getUsername() + " redeemed an hour of double EXP from a Reward bond!", 15, "<col=FF0000>");
//					}
//					break;
//				case 2:
//					if (!player.getInventory().hasSpaceFor(ULTRA_LAMP)) {
//						interpreter.sendItemMessage(14807, "Sorry, you don't have enough inventory space.");
//						return true;
//					}
//					if (player.getInventory().remove(BOND)) {
//						player.getInventory().add(ULTRA_LAMP);
//						interpreter.sendItemMessage(14807, "You redeem an <col=FF0000>ultra lamp</col>.");
//						Repository.sendNews("" + player.getUsername() + " redeemed an ultra lamp from a Reward bond!", 15, "<col=FF0000>");
//						return true;
//					}
//					break;
//				case 3:
//					Item coins = new Item(995, RandomFunction.random(10000,  75000));
//					if (!player.getInventory().hasSpaceFor(coins)) {
//						interpreter.sendItemMessage(14807, "Sorry, you don't have enough inventory space.");
//						return true;
//					}
//					if (player.getInventory().remove(BOND)) {
//						DecimalFormat formatter = new DecimalFormat("#,###");
//						player.getInventory().add(coins);
//						interpreter.sendItemMessage(14807, "You redeem <col=FF0000>" + formatter.format(coins.getAmount()) + "</col> gold coins.");
//						Repository.sendNews("" + player.getUsername() + " redeemed " + formatter.format(coins.getAmount()) + " gold coins from a Reward bond!", 15, "<col=FF0000>");
//					}
//					break;
//				case 4:
//					if (player.getInventory().freeSlots() < 1) {
//						interpreter.sendItemMessage(14807, "Sorry, you don't have enough inventory space.");
//						return true;
//					}
//					if (player.getTreasureTrailManager().hasClue()) {
//						interpreter.sendItemMessage(14807, "Sorry, you already have a clue scroll.");
//						break;
//					}
//					if (player.getTreasureTrailManager().hasTrail()) {
//						player.getTreasureTrailManager().clearTrail();
//					}
//					Item clue = ClueScrollPlugin.getClue(RandomFunction.getRandomElement(ClueLevel.values()));
//					if (player.getInventory().remove(BOND)) {
//						player.getInventory().add(clue);
//						interpreter.sendItemMessage(14807, "You redeem a <col=FF0000>clue scroll</col>.");
//						Repository.sendNews("" + player.getUsername() + " redeemed a clue scroll from a Reward bond!", 15, "<col=FF0000>");
//					}
//					break;
//				}
//				break;
//			case 1:
//				end();
//				break;
//			}
//			return true;
//		}
//
//		@Override
//		public int[] getIds() {
//			return new int[] {DialogueInterpreter.getDialogueKey("keldagrim-bond")};
//		}
//
//	}
//
//}
