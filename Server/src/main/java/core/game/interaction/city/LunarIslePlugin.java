package core.game.interaction.city;

import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.TeleportManager.TeleportType;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * The plugin used to handle the interactions on Lunar Isle (Moonclan island).
 * @author Splinter
 * @version 1.0
 */
@Initializable
public final class LunarIslePlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(16777).getHandlers().put("option:close", this);
		SceneryDefinition.forId(16774).getHandlers().put("option:open", this);
		NPCDefinition.forId(4512).getHandlers().put("option:go-inside", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final int id = node.getId();
		switch (id) {
		case 16777:
		case 16774:
			player.getTeleporter().send(Location.create(2101, 3926, 0), TeleportType.FAIRY_RING);
			break;
		case 4512:
			player.getTeleporter().send(Location.create(2451, 4645, 0), TeleportType.FAIRY_RING);
			break;
		}
		return true;
	}
}
