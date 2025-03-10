package content.region.kandarin.seers.quest.merlinsquest;

import core.cache.def.impl.NPCDefinition;
import core.game.dialogue.DialoguePlugin;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.quest.Quest;
import core.game.node.item.Item;
import core.plugin.Plugin;
import core.game.shops.Shops;
import core.plugin.ClassScanner;
import content.data.Quests;

/**
 * Represents the dialogue plugin used to handle the candle maker npc.
 * @author 'Vexia
 * @author Splinter
 * @version 1.0
 */
public final class CandleMakerDialogue extends DialoguePlugin {

	/**
	 * Constructs a new {@code CandleMakerDialogue} {@code Object}.
	 */
	public CandleMakerDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code CandleMakerDialogue} {@code Object}.
	 * @param player the player.
	 */
	public CandleMakerDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new CandleMakerDialogue(player);
	}

	@Override
	public void init() {
		super.init();
		ClassScanner.definePlugin(new OptionHandler() {

			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				NPCDefinition.forId(getIds()[0]).getHandlers().put("option:trade", this);
				return this;
			}

			@Override
			public boolean handle(Player player, Node node, String option) {
				NPC npc = node.asNpc();
				Quest quest = player.getQuestRepository().getQuest(Quests.MERLINS_CRYSTAL);
				if (quest.getStage(player) > 60) {
					Shops.openId(player, 56);
				} else {
					npc.openShop(player);
				}
				return true;
			}

		});
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		npc("Hi! Would you be interested in some of my fine", "candles?");
		stage = 2;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		final Quest quest = player.getQuestRepository().getQuest(Quests.MERLINS_CRYSTAL);
		switch (stage) {
		case 2:
			if (quest.getStage(player) == 50 || quest.getStage(player) == 60) {// the player has defeated mordred and learned about the black candles
				options("Have any black candles?", "Yes, let me see your stock.", "No thanks.");
				stage = 3;
			} else if (quest.getStage(player) > 60) {// they're farther along in the quest
				options("Have any black candles?", "Yes, let me see your stock.", "No thanks.");
				stage = 25;
			} else {// they haven't started the quest or are too low
				options("Yes, let me see your stock.", "No thanks.");
				stage = 10;
			}
			break;
		case 3:
			switch (buttonId) {
			case 1:
				player("Have you got any black candles?");
				stage = 4;
				break;
			case 2:
				player("Yes, let me see your stock.");
				stage = 30;
				break;
			case 3:
				player("No thank you.");
				stage = 40;
				break;
			}
			break;
		case 4:
			npc("BLACK candles???");
			stage = 5;
			break;
		case 5:
			npc("Hmmm. In the candle making trade, we have a tradition", "that it's very bad luck to make black candles.");
			stage = 6;
			break;
		case 6:
			npc("VERY bad luck.");
			stage = 7;
			break;
		case 7:
			player("I will pay good money for one.");
			stage = 8;
			break;
		case 8:
			npc("I still dunno...");
			stage = 9;
			break;
		case 9:
			npc("Tell you what. I'll supply you with a black candle...");
			stage = 11;
			break;
		case 11:
			npc("IF you can bring me a bucket FULL of wax.");
			stage = 40;
			break;
		case 10:
			switch (buttonId) {
			case 1:
				player("Yes, let me see your stock.");
				stage = 30;
				break;
			case 2:
				player("No thank you.");
				stage = 40;
				break;
			}
			break;
		case 25:
			switch (buttonId) {
			case 1:
				npc("Ah, you again. You're quite a trend setter. Can't believe", "the number of black candle requests I've had since you", "came. I couldn't pass up a business opportunity like that,", "bad luck or no. So I'm selling them now. Would you be");
				stage = 26;
				break;
			case 2:
				player("Yes, let me see your stock.");
				stage = 30;
				break;
			case 3:
				player("No thank you.");
				stage = 40;
				break;
			}
			break;
		case 26:
			npc("interested in purchasing another?");
			stage = 27;
			break;
		case 27:
			options("Yes, let me see your stock.", "No thanks.");
			stage = 10;
			break;
		case 30:
			end();
			if (quest.getStage(player) > 60) {
				Shops.openId(player, 56);
			} else {
				npc.openShop(player);
			}
			break;
		case 40:
			if (quest.getStage(player) == 50 && player.getInventory().contains(MerlinCrystalPlugin.BUCKET_OF_WAX.getId(), 1)) {
				npc("Wha- what's that? You've already got a bucket of wax!");
				stage = 41;
			} else {
				end();
			}
			break;
		case 41:
			npc("Give it 'ere and I'll trade you for a black candle.");
			stage = 42;
			break;
		case 42:
			interpreter.sendDialogue("You hand over a bucket of wax in exchange for a black candle.");
			player.getInventory().remove(new Item(MerlinCrystalPlugin.BUCKET_OF_WAX.getId(), 1));
			player.getInventory().add(new Item(MerlinCrystalPlugin.BLACK_CANDLE.getId(), 1));
			stage = 43;
			break;
		case 43:
			end();
			break;

		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 562 };
	}
}