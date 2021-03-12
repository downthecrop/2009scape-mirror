package core.game.node.entity.skill.construction.decoration.chapel;


import core.cache.def.impl.ObjectDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.object.GameObject;
import core.game.node.object.ObjectBuilder;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;

/**
 * Handles the lighting of the torches of the Chapel.
 * @author Splinter
 */
@Initializable
public class TorchLightPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (int i = 13202; i < 13214; i++) {
			ObjectDefinition.forId(i).getHandlers().put("option:light", this);
		}
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (player.getIronmanManager().checkRestriction() && !player.getHouseManager().isInHouse(player)) {
			return true;
		}
		if (!player.getInventory().containsItem(new Item(590)) || !player.getInventory().containsItem(new Item(251))) {
			player.getDialogueInterpreter().sendDialogue("You'll need a tinderbox and a clean marrentill herb in order to", "light the burner.");
			return true;
		}
		if (player.getInventory().remove(new Item(251))) {
			player.lock(1);
			player.animate(Animation.create(3687));
			player.sendMessage("You burn some marrentill in the incense burner.");
			ObjectBuilder.replace(node.asObject(), new GameObject(node.asObject().getId() + 1, node.getLocation()), RandomFunction.random(100, 175));
		}
		return true;
	}

}