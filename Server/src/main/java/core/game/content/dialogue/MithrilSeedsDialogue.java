package core.game.content.dialogue;

import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.game.world.update.flag.player.FaceLocationFlag;

/**
 * Represents the dialogue plugin used for mithril seeds.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class MithrilSeedsDialogue extends DialoguePlugin {

	/**
	 * Represents the animation to use.
	 */
	private static final Animation ANIMATION = new Animation(827);

	/**
	 * Represents the flower object.
	 */
	private Scenery flower;

	/**
	 * Constructs a new {@code MithrilSeedPluginDialogue}.
	 */
	public MithrilSeedsDialogue() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code MithrilSeedPluginDialogue}.
	 */
	public MithrilSeedsDialogue(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new MithrilSeedsDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		flower = (Scenery) args[0];
		player.getDialogueInterpreter().sendOptions("Select an Option", "Pick the flowers.", "Leave the flowers.");
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (interfaceId) {
		case 228: // Options chatbox interface id.
			switch (buttonId) {
			case 1: // First option
				player.lock(2);
				player.faceLocation(FaceLocationFlag.getFaceLocation(player, flower));
				player.animate(ANIMATION);
				GameWorld.getPulser().submit(new Pulse(2, player, flower) {
					@Override
					public boolean pulse() {
						Item reward = new Item(2460 + ((flower.getId() - 2980) << 1));
						if (reward == null || !player.getInventory().hasSpaceFor(reward)) {
							player.getPacketDispatch().sendMessage("Not enough space in your inventory!");
							return true;
						}
						if (SceneryBuilder.remove(flower)) {
							player.getInventory().add(reward);
							player.getPacketDispatch().sendMessage("You pick the flowers.");
						}
						return true;
					}
				});
				break;
			}
			break;
		}
		end();
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 1 << 16 | 1 };
	}

}
