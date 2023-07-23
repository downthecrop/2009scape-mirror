package content.region.asgarnia.burthorpe.handlers;

import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.global.action.ClimbActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the option plugin to handle interactions with 'nodes' in
 * burthorpe.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class BurthorpeOptionPlugin extends OptionHandler {

	/**
	 * Represents the thieving guide location.
	 */
	private static final Location GUIDE_LOCATION = new Location(3061, 4985, 1);

	/**
	 * Represents the bar location.
	 */
	private static final Location BAR_LOCATION = new Location(2906, 3537, 0);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(7257).getHandlers().put("option:enter", this);// thieving
		// guide
		// trapdoor.
		SceneryDefinition.forId(7258).getHandlers().put("option:enter", this);// thieving
		// guide
		// passegeway.
		SceneryDefinition.forId(4624).getHandlers().put("option:climb-down", this);
		SceneryDefinition.forId(4627).getHandlers().put("option:climb-up", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final int id = node instanceof NPC ? ((NPC) node).getId() : ((Scenery) node).getId();
		switch (option) {
		case "climb-down":
			switch (id) {
			case 4624:
				ClimbActionHandler.climb(player, null, Location.create(2205, 4934, 1));
				break;
			}
			break;
		case "climb-up":
			switch (id) {
			case 4627:
				ClimbActionHandler.climb(player, null, Location.create(2899, 3565, 0));
				break;
			}
			break;
		case "enter":
			switch (id) {
			case 7257:
				player.getProperties().setTeleportLocation(GUIDE_LOCATION);
				break;
			case 7258:
				player.getProperties().setTeleportLocation(BAR_LOCATION);
				break;
			}
			break;
		}
		return true;
	}

}
