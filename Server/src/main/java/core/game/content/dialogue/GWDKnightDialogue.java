package core.game.content.dialogue;

import core.cache.def.impl.NPCDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the dying knight dialogue.
 * @author Emperor
 * @version 1.0
 */
@Initializable
public final class GWDKnightDialogue extends DialoguePlugin {

	/**
	 * Represents the scroll item.
	 */
	private static final Item SCROLL = new Item(11734);

	/**
	 * Constructs a new {@code GWDKnightDialogue} {@code Object}.
	 */
	public GWDKnightDialogue() {
		NPCDefinition.forId(6201).getHandlers().put("option:talk-to", new OptionHandler() {

			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				return this;
			}

			@Override
			public boolean handle(Player player, Node node, String option) {
				NPC npc = (NPC) node;
				return player.getDialogueInterpreter().open(6201, npc);
			}

			@Override
			public Location getDestination(Node n, Node node) {
				return node.getLocation().transform(0, -1, 0);
			}
		});
	}

	/**
	 * Constructs a new {@code GWDKnightDialogue} {@code Object}.
	 * @param player the player.
	 */
	public GWDKnightDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new GWDKnightDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		if ((player.getConfigManager().get(1048) & 16) != 0) {
			player.getPacketDispatch().sendMessage("The knight has already died.");
			return false;
		}
		player("Who are you? What are you doing here in the snow?");
		stage = 0;
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (stage) {
		case 0:
			npc("My name is...Sir Gerry. I am...a member of a", "secret...society of knights. My time is short and I", "need...your help.");
			stage = 1;
			break;
		case 1:
			player("A secret society of knights? What a surprise! Is there", "an old charter or decree that says if you're a knight", "you have to belong to a secret order?");
			stage = 2;
			break;
		case 2:
			npc("I'm sorry, my friend... I do not understand your", "meaning. Please, time is short... Take this scroll to Sir", "Tiffy. You will find him in Falador park... You should", "not...read it... It contains information for his eyes only.");
			stage = 3;
			break;
		case 3:
			if (player.getInventory().add(SCROLL)) {
				interpreter.sendItemMessage(11734, "The knight hands you a scroll.");
				player.varpManager.get(1048).setVarbit(4,1).send(player);
				stage = 5;
			} else {
				stage = 4;
			}
			break;
		case 4:
			interpreter.sendDialogue("The knight tries to give you something, but your inventory is full.");
			stage = 5;
			break;
		case 5:
			end();
			break;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6202 };
	}
}
