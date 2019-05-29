package plugin.skill.construction.decoration.chapel;


import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.node.object.ObjectBuilder;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;
import org.crandor.tools.RandomFunction;

/**
 * Handles the lighting of the torches of the Chapel.
 * @author Splinter
 */
@InitializablePlugin
public class TorchLightPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (int i = 13202; i < 13214; i++) {
			ObjectDefinition.forId(i).getConfigurations().put("option:light", this);
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