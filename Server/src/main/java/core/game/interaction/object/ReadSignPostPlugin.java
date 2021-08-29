package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.component.CloseEvent;
import core.game.component.Component;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.net.packet.PacketRepository;
import core.net.packet.context.MinimapStateContext;
import core.net.packet.out.MinimapState;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the reading of a sign post.
 * @author 'Vexia
 */
@Initializable
public class ReadSignPostPlugin extends OptionHandler {

	/**
	 * Represents the areas on the sign.
	 * @author 'Vexia
	 */
	public enum Signs {
		NEAR_LUMBRIDGE(18493, "North to farms and<br> Varrock.", "The River Lum lies to<br> the south.", "West to<br>Lumbridge.", "East to Al<br>Kharid - toll<br>gate; bring some<br>money."), NEAR_VARROCK(24263, "Sheep lay this way.", "South through farms<br> to Al Kharid and<br> Lumbridge", "West to Champion's Guild<br> and Varrock south<br> gate.", "East to Al Kharid mine and<br> follow the path north to<br> Varrock east gate.");

		public static Signs forId(int id) {
			for (Signs sign : Signs.values()) {
				if (sign == null) {
					continue;
				}
				if (sign.object == id) {
					return sign;
				}
			}
			return null;
		}

		/**
		 * The object id.
		 */
		private int object;

		/**
		 * The directions.
		 */
		private String directions[];

		/**
		 * Constructs a new {@code ReadSignPostPlugin.java} {@code Object}.
		 * @param object the object.
		 * @param directions the directions.
		 */
		Signs(int object, String... directions) {
			this.object = object;
			this.directions = directions;
		}
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 2));
		player.getInterfaceManager().open(new Component(135)).setCloseEvent(new CloseEvent() {
			@Override
			public boolean close(Player player, Component c) {
				PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
				return true;
			}
		});
		final Scenery object = (Scenery) node;
		Signs sign = Signs.forId(object.getId());
		if (sign == null) {
			return false;
		}
		String[] dirs = sign.directions;
		if (object.getLocation().getX() == 3107 && object.getLocation().getY() == 3296) {
			dirs[0] = "North to Draynor<br> Manor";
			dirs[1] = "South to Draynor<br> Village";
			dirs[2] = "West to Port<br> Sarim";
			dirs[3] = "East to<br> Lumbridge";
		}
		player.getPacketDispatch().sendString(dirs[0], 135, 3); // North
		player.getPacketDispatch().sendString(dirs[1], 135, 9); // South
		player.getPacketDispatch().sendString(dirs[2], 135, 12); // West
		player.getPacketDispatch().sendString(dirs[3], 135, 8); // East
		return true;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(2366).getHandlers().put("option:read", this);
		SceneryDefinition.forId(2367).getHandlers().put("option:read", this);
		SceneryDefinition.forId(2368).getHandlers().put("option:read", this);
		SceneryDefinition.forId(2369).getHandlers().put("option:read", this);
		SceneryDefinition.forId(2370).getHandlers().put("option:read", this);
		SceneryDefinition.forId(2371).getHandlers().put("option:read", this);
		SceneryDefinition.forId(4132).getHandlers().put("option:read", this);
		SceneryDefinition.forId(4133).getHandlers().put("option:read", this);
		SceneryDefinition.forId(4134).getHandlers().put("option:read", this);
		SceneryDefinition.forId(4135).getHandlers().put("option:read", this);
		SceneryDefinition.forId(5164).getHandlers().put("option:read", this);
		SceneryDefinition.forId(10090).getHandlers().put("option:read", this);
		SceneryDefinition.forId(13873).getHandlers().put("option:read", this);
		SceneryDefinition.forId(15522).getHandlers().put("option:read", this);
		SceneryDefinition.forId(18493).getHandlers().put("option:read", this);
		SceneryDefinition.forId(24263).getHandlers().put("option:read", this);
		SceneryDefinition.forId(25397).getHandlers().put("option:read", this);
		SceneryDefinition.forId(30039).getHandlers().put("option:read", this);
		SceneryDefinition.forId(30040).getHandlers().put("option:read", this);
		SceneryDefinition.forId(31296).getHandlers().put("option:read", this);
		SceneryDefinition.forId(31298).getHandlers().put("option:read", this);
		SceneryDefinition.forId(31299).getHandlers().put("option:read", this);
		SceneryDefinition.forId(31300).getHandlers().put("option:read", this);
//		ObjectDefinition.forId(31301).getConfigurations().put("option:read", this);//goblin village
		return this;
	}
}
