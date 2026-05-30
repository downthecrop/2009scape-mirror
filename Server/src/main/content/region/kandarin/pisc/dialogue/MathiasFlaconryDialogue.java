package content.region.kandarin.pisc.dialogue;

import core.game.container.impl.EquipmentContainer;
import core.game.dialogue.DialoguePlugin;
import core.game.dialogue.FacialExpression;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.game.node.item.Item;
import org.rs09.consts.Items;

/**
 * Handles the MathiasFlaconryDialogue dialogue.
 * @author 'Vexia
 */
@Initializable
public class MathiasFlaconryDialogue extends DialoguePlugin {

	public MathiasFlaconryDialogue() {
	}

	public MathiasFlaconryDialogue(Player player) {
		super(player);
	}

	@Override
	public int[] getIds() {
		return new int[] { 5093 };
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 90:
			interpreter.sendOptions("Select an Option", "Ok, that seems reasonable.", "I'm not interested then, thanks.");
			stage = 91;
			break;
		case 91:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Ok, that seems reasonable.");
				stage = 95;
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "I'm not interested then, thanks.");
				stage = 323;
				break;

			}
			break;
		case 323:
			interpreter.sendDialogues(5093, FacialExpression.NEUTRAL, "Well, you're welcome to come back if you change", "your mind.");
			stage = 967;
			break;
		case 95:
			if (player.getBank().containsItem(FALCON) || player.getEquipment().containsItem(FALCON) || player.getInventory().containsItem(FALCON)) {
				interpreter.sendDialogues(5093, FacialExpression.HALF_GUILTY, "You already have a falcon!");
				stage = 99;
				return true;
			}
			if (player.getEquipment().get(EquipmentContainer.SLOT_HANDS) != null || player.getEquipment().get(EquipmentContainer.SLOT_SHIELD) != null || player.getEquipment().get(EquipmentContainer.SLOT_WEAPON) != null) {
				interpreter.sendDialogues(5093, FacialExpression.HALF_GUILTY, "Sorry, you really need both hands free for falconry. I'd", "suggest that you put away your weapons and gloves before", "we start.");
				stage = 99;
				break;
			}
			if (player.getInventory().contains(995, 500)) {
				player.getInventory().remove(new Item(995, 500));
				player.getEquipment().add(new Item(10024), true, false);
				interpreter.sendDialogue("The falconer gives you a large leather glove and brings one of the", "smaller birds over to land on it.");
				stage = 97;
			} else {
				end();
				player.getPacketDispatch().sendMessage("You need 500 gold coins.");
			}
			break;
		case 97:
			interpreter.sendDialogues(5093, FacialExpression.HALF_GUILTY, "Don't worry; I'll keep an eye on you to make sure", "you don't upset it too much.");
			stage = 99;
			break;
		case 99:
			end();
			break;
		case 500:
			interpreter.sendDialogues(5093, FacialExpression.HALF_GUILTY, "Greetings. Can I help you at all?");
			stage = 501;
			break;
		case 501:
			interpreter.sendOptions("Select an Option", "Do you have any quests I could do?", "What is this place?", "Could I have a go with your bird?");
			stage = 502;
			break;
		case 502:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.ASKING, "Do you have any quests I could do?");
				stage = 600;
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.ASKING, "What is this place?");
				stage = 700;
				break;
			case 3:
				interpreter.sendDialogues(player, FacialExpression.ASKING, "Could I have a go with your bird?");
				stage = 800;
				break;
			}
			break;
		case 600:
			interpreter.sendDialogues(5093, FacialExpression.ASKING, "A quest? What a strange notion. Do you normally go", "around asking complete strangers for quests?");
			stage = 601;
			break;
		case 601:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Er, yes, now you come to mention it.");
			stage = 602;
			break;
		case 602:
			interpreter.sendDialogues(5093, FacialExpression.HALF_GUILTY, "Oh, ok then. Well, no, I don't; sorry.");
			stage = 967;
			break;

		case 700:
			interpreter.sendDialogues(5093, FacialExpression.HALF_GUILTY, "A good question; straight and to the point. My name is ", "Matthias, I am a falconer, and this is where I train", "my birds.");
			stage = 701;
			break;
		case 701:
			interpreter.sendOptions("Select an Option", "Do you have any quests I could do?", "That sounds like fun; could I have a go?", "That doesn't sound like my sort of thing.", "What's this falconry thing all about then?");
			stage = 702;
			break;
		case 702:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.ASKING, "Do you have any quests I could do?");
				stage = 600;
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.ASKING, "That sounds like fun; could I have a go?");
				stage = 800;
				break;
			case 3:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "That doesn't sound like my sort of thing.");
				stage = 720;
				break;
			case 4:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "What's this falconry thing all about then?");
				stage = 750;
				break;
			}
			break;
		case 720:
			interpreter.sendDialogues(5093, FacialExpression.HALF_GUILTY, "Fair enough; it does require a great deal of patience and", "skill, so I can understand if you might feel intimidated.");
			stage = 967;
			break;
		case 750:
			interpreter.sendDialogues(5093, FacialExpression.NEUTRAL, "Well, some people see it as a sport, although such a term", "does not really convey the amount of patience and", "dedication required to be proficient at the task.");
			stage = 751;
			break;
		case 751:
			interpreter.sendDialogues(5093, FacialExpression.NEUTRAL, "Putting it simply, it is the training and use of birds of", "prey in hunting quarry.");
			stage = 752;
			break;
		case 752:
			interpreter.sendDialogues(player, FacialExpression.ASKING, "So it's like keeping a pet then?");
			stage = 753;
			break;
		case 753:
			interpreter.sendDialogues(5093, FacialExpression.NEUTRAL, "Not exactly, no. Such a bird can never really be", "considered tame in the same way that a dog can.");
			stage = 754;
			break;
		case 754:
			interpreter.sendDialogues(5093, FacialExpression.NEUTRAL, "They can be trained to associate people or places with", "food though, and, as such, a good falconer can get a", "trained bird to do as he wishes.");
			stage = 701;
			break;

		case 800:
			if (player.getSkills().getLevel(Skills.HUNTER) < 43) {
				npc("Try coming back when you're more experienced", "I wouldn't want my birds being injured.");
				stage = 967;
				return true;
			}
			interpreter.sendDialogues(5093, FacialExpression.HALF_GUILTY, "Training falcons is a lot of work and I doubt you're up", "to the task. However, I suppose I could let you try", "hunting with one.");
			stage = 801;
			break;
		case 801:
			interpreter.sendDialogues(5093, FacialExpression.HALF_GUILTY, "I have some tamer birds that I occasionally lend to rich", "noblemen who consider it a sufficiently refined sport for", "their tastes, and you look like the kind who might", "appreciate a good hunt.");
			stage = 802;
			break;
		case 802:
			interpreter.sendDialogues(5093, FacialExpression.NEUTRAL, "I'd have to request a small fee, mind you; how does", "500 gold pieces sound?");
			stage = 90;
			break;

		case 900:
			interpreter.sendOptions("Select an Option", "Yes, please.", "No thank you.");
			stage = 901;
			break;
		case 901:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, null, "Yes, please.");
				stage = 920;
				break;
			case 2:
				end();
				break;
			}
			break;
		case 920:
			player.getBank().remove(FALCON, new Item(10023));
			player.getInventory().remove(new Item(10023), FALCON);
			player.getEquipment().add(FALCON, true, false);
			interpreter.sendDialogue("The falconer gives you a large leather glove and brings one of the", "smaller birds over to land on it.");
			stage = 97;
			break;
		case 967:
			end();
			break;

		case 1000:
			interpreter.sendDialogues(5093, FacialExpression.HALF_GUILTY, "Ah, you're back. How are you getting along with her then?");
			stage = 1001;
			break;
		case 1001:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "It's certainly harder than it looks.");
			stage = 1002;
			break;
		case 1002:
			interpreter.sendDialogues(5093, FacialExpression.HALF_GUILTY, "Sorry, but I was talking to the falcon, not you. But yes it", "is. Have you had enough yet?");
			stage = 1003;
			break;
		case 1003:
			interpreter.sendOptions("Select an Option", "Actually, I'd like to keep trying a little longer.", "I think I'll leave it for now.");
			stage = 1004;
			break;
		case 1004:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.ASKING, "Actually, I'd like to keep trying a little longer.");
				stage = 1010;
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "I think I'll leave it for now.");
				stage = 1020;
				break;
			}
			break;
		case 1010:
			interpreter.sendDialogues(5093, FacialExpression.NEUTRAL, "Ok then, just come talk to me when you're done.");
			stage = 967;
			break;
		case 1020:
			player.getInventory().remove(FALCON);
			player.getEquipment().remove(FALCON, true);

			interpreter.sendDialogue("You give the falcon and glove back to Matthias.");
			stage = 967;
			break;
		}

		return true;
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new MathiasFlaconryDialogue(player);
	}

	private final Item FALCON = new Item(10024);

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		boolean quick = false;
		if (player.getEquipment().contains(10023, 1)) {
			interpreter.sendDialogues(5093, null, "Oh, it looks like you've lost your falcon.", "Would you like a new one?");
			stage = 900;
			return true;
		}
		if (player.getEquipment().contains(10024, 1) || player.getInventory().contains(10024, 1)) {
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Hello again.");
			stage = 1000;
			return true;
		}
		if (args.length == 2)
			quick = true;
		if (quick) {
			interpreter.sendDialogues(5093, FacialExpression.HALF_GUILTY, "If you wish to try falconry, I request a small fee. How", "does 500 gold coins sound?");
			stage = 90;
			return true;
		}
		interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Hello there.");
		stage = 500;
		return true;
	}
}
