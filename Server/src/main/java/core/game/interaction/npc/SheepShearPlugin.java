package core.game.interaction.npc;

import core.cache.def.impl.NPCDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Represents the plugin used to shear a sheep.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class SheepShearPlugin extends OptionHandler {

	/**
	 * Represents the animation to use.
	 */
	private static final Animation ANIMATION = new Animation(893);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.setOptionHandler("shear", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final NPC sheep = (NPC) node;
		if (sheep.getId() == 3579) {
			player.animate(ANIMATION);
			player.getPacketDispatch().sendMessage("The... whatever it is... manages to get away from you!");
			sheep.animate(Animation.create(3570));
			sheep.moveStep();
			return true;
		}
		if (!player.getInventory().contains(1735, 1)) {
			player.getPacketDispatch().sendMessage("You need shears to shear a sheep.");
			return true;
		}
		if (sheep.getDefinition().hasAction("attack")) {
			player.getPacketDispatch().sendMessage("That one looks a little too violent to shear...");
			return true;
		}
		if (player.getInventory().freeSlots() == 0) {
			player.getPacketDispatch().sendMessage("You don't have enough space in your inventory to carry any wool you would shear.");
			return true;
		}
		sheep.lock(3);
		sheep.getWalkingQueue().reset();
		player.animate(ANIMATION);
		int random = RandomFunction.random(1, 5);
		if (random != 4) {
			sheep.getLocks().lockMovement(2);
			sheep.transform(5153);
			player.getPacketDispatch().sendMessage("You get some wool.");
			player.getInventory().add(new Item(1737, 1));// 5160
			GameWorld.getPulser().submit(new Pulse(80, sheep) {
				@Override
				public boolean pulse() {
					sheep.reTransform();
					return true;
				}

			});
		} else {
			player.getPacketDispatch().sendMessage("The sheep manages to get away from you!");
			sheep.moveStep();
		}
		return true;
	}

}
