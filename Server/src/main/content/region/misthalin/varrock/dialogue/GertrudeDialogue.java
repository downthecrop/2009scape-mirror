package content.region.misthalin.varrock.dialogue;

import content.global.skill.summoning.pet.Pet;
import content.global.skill.summoning.pet.PetDetails;
import core.game.container.Container;
import core.game.dialogue.DialoguePlugin;
import core.game.dialogue.FacialExpression;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.quest.Quest;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.tools.RandomFunction;
import org.rs09.consts.Items;

import java.util.Map;

import static core.api.ContentAPIKt.freeSlots;
import static core.api.ContentAPIKt.inInventory;

/**
 * Represents the gertrude dialogue plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class GertrudeDialogue extends DialoguePlugin {

	/**
	 * Represents the coins item.
	 */
	private static final Item COINS = new Item(995, 100);

	/**
	 * Constructs a new {@code GertrudeDialogue} {@code Object}.
	 */
	public GertrudeDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code GertrudeDialogue} {@code Object}.
	 * @param player the player.
	 */
	public GertrudeDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new GertrudeDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		final Quest quest = player.getQuestRepository().getQuest("Gertrude's Cat");
		switch (quest.getStage(player)) {
		case 0:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Hello, are you OK?");
			break;
		case 10:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Hello, Gertrude.");
			stage = 210;
			break;
		case 20:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Hello, Gertrude.");
			stage = 230;
			break;
		case 30:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Hello again.");
			stage = 236;
			break;
		case 50:
			interpreter.sendDialogues(npc, FacialExpression.HALF_WORRIED, "Please bring me my cat back!");
			stage = 235;
			break;
		case 40:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Hello Gertrude.");
			stage = 300;
			break;
		case 60:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Hello Gertrude. Fluffs ran off with her kitten.");
			stage = 320;
			break;
		case 100:
			interpreter.sendDialogues(player, FacialExpression.NEUTRAL, "Hello again.");
			stage = 500;
			break;
		}
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		final Quest quest = player.getQuestRepository().getQuest("Gertrude's Cat");
		switch (stage) {
		case 0:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Do I look OK? Those kids drive me crazy.");
			stage = 1;
			break;
		case 1:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "I'm sorry. It's just that I've lost her.");
			stage = 2;
			break;
		case 2:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Lost who?");
			stage = 3;
			break;
		case 3:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Fluffs, poor Fluffs. She never hurt anyone.");
			stage = 4;
			break;
		case 4:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Who's Fluffs?");
			stage = 5;
			break;
		case 5:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "My beloved feline friends Fluffs. She's been purring by", "my side for almost a decade. Please, could you go", "search for her while I look over the kids?");
			stage = 6;
			break;
		case 6:
			interpreter.sendOptions("Select an Option", "Well, I suppose I could.", "Sorry, I'm too busy to play pet rescue.");
			stage = 7;
			break;
		case 7:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Well, I suppose I could.");
				stage = 100;
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Sorry, I'm to busy too play pet rescue.");
				stage = 200;
				break;
			}
			break;
		case 100:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Really? Thank you so much! I really have no idea", "where she could be!");
			stage = 101;
			break;
		case 101:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "I think my sons, Shilop and Wilough, saw the cat last.", "They'll be out in the market place.");
			stage = 102;
			break;
		case 102:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Alright then, I'll see what I can do.");
			stage = 103;
			break;
		case 103:
			quest.start(player);
			player.getQuestRepository().syncronizeTab(player);
			end();
			break;
		case 200:
			end();
			break;
		case 210:// start of stage 10
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Have you seen my poor Fluffs?");
			stage = 211;
			break;
		case 211:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "I'm afraid not.");
			stage = 212;
			break;
		case 212:
			interpreter.sendDialogues(npc, FacialExpression.HALF_ASKING, "What about Shilop?");
			stage = 213;
			break;
		case 213:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "No sign of him either.");
			stage = 214;
			break;
		case 214:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Hmmm...strange, he should be at the market.");
			stage = 215;
			break;
		case 215:
			end();
			break;
		case 230:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Hello again, did you manage to find Shilop? I can't keep", "an eye on him for the life of me.");
			stage = 231;
			break;
		case 231:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "He does seem quite a handful.");
			stage = 232;
			break;
		case 232:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "You have no idea! Did he help at all?");
			stage = 233;
			break;
		case 233:
			interpreter.sendDialogues(player, FacialExpression.OLD_NORMAL, "I think so, I'm just going to look now.");
			stage = 234;
			break;
		case 234:
			interpreter.sendDialogues(npc, FacialExpression.HAPPY, "Thanks again adventurer!");
			stage = 235;
			break;
		case 235:
			end();
			break;
		case 236:
			interpreter.sendDialogues(npc, FacialExpression.HALF_ASKING, "Hello. How's it going? Any luck?");
			stage = 237;
			break;
		case 237:
			interpreter.sendDialogues(player, FacialExpression.NEUTRAL, "Yes, I've found Fluffs!");
			stage = 238;
			break;
		case 238:
			interpreter.sendDialogues(npc, FacialExpression.HALF_ASKING, "Well well, you are clever!", "Did you bring her back?");
			stage = 239;
			break;
		case 239:
			interpreter.sendDialogues(player, FacialExpression.HALF_WORRIED, "Well, that's the thing, she refuses to leave.");
			stage = 240;
			break;
		case 240:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Oh dear, oh dear! Maybe she's just hungry.", "She loves doogle sardines but I'm all out.");
			stage = 241;
			break;
		case 241:
			interpreter.sendDialogues(player, FacialExpression.HALF_ASKING, "Doogle sardines?");
			stage = 242;
			break;
		case 242:
			interpreter.sendDialogues(npc, FacialExpression.HALF_ASKING, "Yes, raw sardines seasoned with doogle leaves.", "Unfortunately I've used all my doogle leaves,", "but you may find some in the woods out back.");
			stage = 304;
			break;
		case 300:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Hi! Did you find fluffs?");
			stage = 301;
			break;
		case 301:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Yes! But she won't follow me.");
			stage = 302;
			break;
		case 302:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "You should try tempting her with a", "seasoned sardine! Those are her favourite snacks.");
			stage = 303;
			break;
		case 303:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Thanks for the advice!");
			stage = 304;
			break;
		case 304:
			end();
			break;
		case 320:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "You're back! Thank you! Thank you! Fluffs just came", "back! I think she was just upset as she couldn't find her", "kitten.");
			stage = 321;
			break;
		case 321:
			interpreter.sendDialogue("Gertrude gives you a hug.");
			npc.face(player);
			stage = 322;
			break;
		case 322:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "If you hadn't found her kitten it would have died out", "there!");
			stage = 323;
			break;
		case 323:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "That's OK, I like to do my bit.");
			stage = 324;
			break;
		case 324:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "I don't know how to thank you. I have no real material", "possesions. I do have kittens! I can only really look", "after one.");
			stage = 325;
			break;
		case 325:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Well, if it needs a home.");
			stage = 326;
			break;
		case 326:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "I would sell it to my cousin in West Ardougne. I hear", "there's a rat epidemic there. But it's too far.");
			stage = 327;
			break;
		case 327:
			if (player.getInventory().freeSlots() == 0) {
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "I don't seem to have enough inventory space.");
				stage = 1000;
				break;
			} else {
				interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Here you go, look after her and thank you again!");
				stage = 328;
			}
			break;
		case 328:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Oh by the way, the kitten can live in your backpack,", "but to make it grow you must take it out and feed and", "stroke it often.");
			stage = 329;
			break;
		case 329:
			interpreter.sendDialogue("Gertrude gives you a kitten.");
			stage = 330;
			break;
		case 330:
			player.getPacketDispatch().sendMessage("...and some food!");
			end();
			quest.finish(player);
			player.getQuestRepository().syncronizeTab(player);
			break;
		case 331:
			break;
		case 1000:
			interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Good good, See you again.");
			stage = 1001;
			break;
		case 1001:
			end();
			break;
		case 500:
			interpreter.sendDialogues(npc, FacialExpression.HALF_ASKING, "Hello my dear. How's things?");
			stage = 501;
			break;
		case 501:
			interpreter.sendOptions("Select an Option", "I'm fine, thanks.", "Do you have any more kittens?");
			stage = 502;
			break;
		case 502:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "I'm fine, thanks.");
				stage = 1000;
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.HALF_ASKING, "Do you have any more kittens?");
				stage = 503;
				break;
			}
			break;
		case 503:
			boolean has = false;
			int[] kittens = new int[] { 1555, 1556, 1557, 1558, 1559, 1560, 7583 };
			if (player.getFamiliarManager().hasFamiliar() && player.getFamiliarManager().hasPet()) {
				Pet pet = (Pet) player.getFamiliarManager().getFamiliar();
				for (int i : kittens) {
					if (pet.getItemId() == i) {
						has = true;
						break;
					}
				}
			}
			Container[] searchSpace = {player.getInventory(), player.getBank()};
			for (Container container : searchSpace) {
				if (container.containsAtLeastOneItem(kittens)) {
					has = true;
					break;
				}
			}
			if (has) {
				interpreter.sendDialogues(npc, null, "Aren't you still raising that other kitten? Only once it's", "fully grown and it no longer needs your attention will", "I let you have another kitten.");
				stage = 505;
			} else {
				interpreter.sendDialogues(npc, null, "Indeed I have. They are 100 coins each, do you want", "one?");
				stage = 900;
			}
			break;
		case 900:
			interpreter.sendOptions("Select an Option", "Yes please.", "No thanks.");
			stage = 901;
			break;
		case 901:
			switch (buttonId) {
			case 1:// yes
				boolean hasMoney = inInventory(player, Items.COINS_995, 100);
				if (!hasMoney) {
					player.getPacketDispatch().sendMessage("You need 100 coins to buy a kitten.");
					end();
					break;
				} else {
					boolean hasExtraMoney = inInventory(player, Items.COINS_995, 101);
					boolean hasSpace = freeSlots(player) > 0 || !hasExtraMoney;
					if (player.getFamiliarManager().hasFamiliar() && !hasSpace) {
						player.getPacketDispatch().sendMessage("You don't have enough inventory space.");
						end();
						break;
					}
					interpreter.sendDialogues(player, null, "Yes please.");
					stage = 903;
				}
				break;
			case 2:// no
				end();
				break;
			}
			break;
		case 903:
			interpreter.sendDialogues(npc, null, "OK then, here you go.");
			stage = 904;
			break;
		case 904:
			interpreter.sendDialogues(player, null, "Thanks.");
			stage = 905;
			break;
		case 905:
			if (player.getInventory().remove(COINS)) {
				interpreter.sendDialogue("Gertrude gives you another kitten.");
				stage = 906;
				final Item kitten = getKitten();
				if (player.getFamiliarManager().hasFamiliar()) {
					player.getInventory().add(kitten);
				} else {
					player.getFamiliarManager().summon(kitten, true, false);
				}
			}
			break;
		case 906:
			end();
			break;
		case 505:
			end();
			break;
		}
		return true;
	}

	/**
	 * Method used to get a random kitten.
	 * @return the item.
	 */
	public Item getKitten() {
		return new Item(RandomFunction.random(1555, 1560));
	}

	@Override
	public int[] getIds() {
		return new int[] { 780 };
	}
}
