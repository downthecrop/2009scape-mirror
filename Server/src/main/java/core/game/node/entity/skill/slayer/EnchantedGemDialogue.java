package core.game.node.entity.skill.slayer;

import core.cache.def.impl.NPCDefinition;
import core.game.content.dialogue.DialoguePlugin;
import core.game.content.dialogue.FacialExpression;
import core.game.node.entity.player.Player;
import core.game.world.map.Direction;
import core.plugin.Initializable;

/**
 * Rerpresents the enchanted gem dialogue.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class EnchantedGemDialogue extends DialoguePlugin {

	/**
	 * Represents the id associated with this plugin.
	 */
	public static final int ID = 77777;

	/**
	 * Constructs a new {@code EnchantedGemDialogue} {@code Object}.
	 */
	public EnchantedGemDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code EnchantedGemDialogue} {@code Object}.
	 * @param player the player.
	 */
	public EnchantedGemDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new EnchantedGemDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		player.faceLocation(player.getLocation().transform(1, 0, 0));
		player.setDirection(Direction.EAST);
		interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.HALF_GUILTY, "Hello there " + player.getUsername() + ", what can I help you with?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			interpreter.sendOptions("Select an Option", "How am I doing so far?", "Who are you?", "Where are you?", "Got any tips for me?", "Nothing really.");
			stage = 2;
			break;
		case 2:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "How am I doing so far?");
				stage = 10;
				break;
			case 2:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Who are you?");
				stage = 20;
				break;
			case 3:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Where are you?");
				stage = 30;
				break;
			case 4:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Got any tips for me?");
				stage = 400;
				break;
			case 5:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Nothing really.");
				stage = 99;
				break;

			}
			break;
		case 99:
			end();
			break;
		case 10:
			if (!player.getSlayer().hasTask()) {
				interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.HALF_GUILTY, "You need something new to hunt. Come and see me", "When you can and I'll give you a new task.");
				stage = 11;
				break;
			}
			interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.HALF_GUILTY, "You're currently assigned to kill "  + (player.getSlayer().getTask() == Tasks.JAD ? " TzTok-Jad!" : NPCDefinition.forId((player.getSlayer().getTask().getNpcs()[0])).getName().toLowerCase() + "'s;"), "only " + player.getSlayer().getAmount() + " more to go.");
			player.varpManager.get(2502).setVarbit(1,player.getSlayer().getTask().ordinal());
			player.varpManager.get(2502).setVarbit(8,player.getSlayer().getAmount()).send(player);
			stage = 11;
			break;
		case 11:
			interpreter.sendOptions("Select an Option", "Who are you?", "Where are you?", "Got any tips for me?", "That's all thanks.");
			stage = 12;
			break;
		case 12:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.HALF_GUILTY, "My name's " + NPCDefinition.forId(player.getSlayer().getMaster().getNpc()).getName() + ", I'm the Slayer Master best able", "to train you.");
				stage = 21;
				break;
			case 2:
				interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.HALF_GUILTY, "You'll find me in " + masterLocation() + ", I'll be here when you need a", "new task.");
				stage = 31;
				break;
			case 3:
				if (!player.getSlayer().hasTask()) {
					interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.HALF_GUILTY, "You need something new to hunt.");
					stage = 99;
					break;
				}
				interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.HALF_GUILTY, player.getSlayer().getTask().getTip());
				stage = 401;
				break;
			case 4:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "That's all thanks.");
				stage = 99;
				break;
			case 99:
				end();
				break;
			}
			break;
		case 20:
			interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.HALF_GUILTY, "My name's " + NPCDefinition.forId(player.getSlayer().getMaster().getNpc()).getName() + ", I'm the Slayer Master best able", "to train you.");
			stage = 21;
			break;
		case 21:
			interpreter.sendOptions("Select an Option", "How am I doing so far?", "Where are you?", "Got any tips for me?", "That's all thanks.");
			stage = 25;
			break;
		case 25:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "How am I doing so far?");
				stage = 10;
				break;
			case 2:
				interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.HALF_GUILTY, "You'll find me in " + masterLocation() + ", I'll be here when you need a", "new task.");
				stage = 31;
				break;
			case 3:
				if (!player.getSlayer().hasTask()) {
					interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.HALF_GUILTY, "You need something new to hunt.");
					stage = 99;
					break;
				}
				interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.HALF_GUILTY, player.getSlayer().getTask().getTip());
				stage = 401;
				break;
			case 4:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "That's all thanks.");
				stage = 99;
				break;

			}
			break;
		case 30:
			interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.HALF_GUILTY, "You'll find me in " + masterLocation() + ", I'll be here when you need a", "new task.");
			stage = 31;
			break;
		case 31:
			interpreter.sendOptions("Select an Option", "How am I doing so far?", "Who are you?", "Got any tips for me?", "That's all thanks.");
			stage = 32;
			break;
		case 32:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "How am I doing so far?");
				stage = 10;
				break;
			case 2:
				interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.HALF_GUILTY, "My name's " + NPCDefinition.forId(player.getSlayer().getMaster().getNpc()).getName() + ", I'm the Slayer Master best able", "to train you.");
				stage = 21;
				break;
			case 3:
				if (!player.getSlayer().hasTask()) {
					interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.HALF_GUILTY, "You need something new to hunt.");
					stage = 0;
					break;
				}
				interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.HALF_GUILTY, player.getSlayer().getTask().getTip());
				stage = 401;
				break;
			case 4:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "That's all thanks.");
				stage = 99;
				break;

			}
			break;
		case 400:
			if (!player.getSlayer().hasTask()) {
				interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.HALF_GUILTY, "You need something new to hunt.");
				stage = 0;
				break;
			}
			interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.HALF_GUILTY, player.getSlayer().getTask().getTip());
			stage = 401;
			break;
		case 401:
			interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Great, thanks!");
			stage = 403;
			break;
		case 403:
			interpreter.sendOptions("Select an Option", "How am I doing so far?", "Who are you?", "Where are you?", "That's all thanks.");
			stage = 404;
			break;
		case 404:
			switch (buttonId) {
			case 1:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "How am I doing so far?");
				stage = 10;
				break;
			case 2:
				interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.HALF_GUILTY, "My name's " + NPCDefinition.forId(player.getSlayer().getMaster().getNpc()).getName() + ", I'm the Slayer Master best able", "to train you.");
				stage = 21;
				break;
			case 3:
				interpreter.sendDialogues(player.getSlayer().getMaster().getNpc(), FacialExpression.HALF_GUILTY, "You'll find me in " + masterLocation() + ", I'll be here when you need a", "new task.");
				stage = 31;
				break;
			case 4:
				interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "That's all thanks.");
				stage = 99;
				break;

			}
			break;
		}
		return true;
	}

	/**
	 * Method used to get the masters location.
	 * @return the location.
	 */
	public String masterLocation() {
		if (player.getSlayer().getMaster().getNpc() == Master.MAZCHNA.getNpc())
			return "Canifis";
		else if (player.getSlayer().getMaster().getNpc() == Master.TURAEL.getNpc())
			return "Taverley";
		else if (player.getSlayer().getMaster().getNpc() == Master.CHAELDAR.getNpc())
			return "Zanaris";
		else if (player.getSlayer().getMaster().getNpc() == Master.VANNAKA.getNpc())
			return "Edgeville dungeon";
		else if (player.getSlayer().getMaster().getNpc() == Master.DURADEL.getNpc())
			return "Shilo village";
		else if(player.getSlayer().getMaster() == null) {
			return "the Gnome Stronghold";
		}
		return null;
	}

	@Override
	public int[] getIds() {
		return new int[] { ID };
	}
}
