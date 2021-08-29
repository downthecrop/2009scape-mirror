package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.component.Component;
import core.game.content.global.action.ClimbActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to handle the swamp hole.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class LumbridgeSwampHolePlugin extends OptionHandler {

	/**
	 * Represents the top location.
	 */
	private static final Location TOP = Location.create(3169, 3173, 0);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(10375).getHandlers().put("option:take", this);
		SceneryDefinition.forId(5947).getHandlers().put("option:climb-down", this);
		SceneryDefinition.forId(5946).getHandlers().put("option:climb", this);
		SceneryDefinition.forId(15566).getHandlers().put("option:read", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (option) {
		case "climb-down":
			if (!player.getSavedData().getGlobalData().hasTiedLumbridgeRope()) {
				player.getDialogueInterpreter().open(70099, "There is a sheer drop below the hole. You will need a rope.");
				return true;
			} else {
				ClimbActionHandler.climb(player, new Animation(827), Location.create(3168, 9572, 0));
			}
		case "climb":
			player.getProperties().setTeleportLocation(TOP);
			break;
		case "take":
			if (player.getInventory().freeSlots() < 2) {
				player.getPacketDispatch().sendMessage("You do not have enough inventory space.");
				return true;
			}
			if (player.getInventory().add(new Item(5341), new Item(952))) {
				SceneryBuilder.replace(((Scenery) node), ((Scenery) node).transform(10373), 300);
			}
			break;
		case "read":
			player.getPacketDispatch().sendString("<col=8A0808>~-~-~ WARNING ~-~-~", 220, 5);
			player.getPacketDispatch().sendString("<col=8A0808>Noxious gases vent into this cave.", 220, 7);
			player.getPacketDispatch().sendString("<col=8A0808>Naked flames may cause an explosion!", 220, 8);
			player.getPacketDispatch().sendString("<col=8A0808>Beware of vicious head-grabbing beasts!", 220, 10);
			player.getPacketDispatch().sendString("<col=8A0808>Contact a Slayer master for protective headgear.", 220, 11);
			player.getInterfaceManager().open(new Component(220));
			break;
		}
		return true;
	}

}
