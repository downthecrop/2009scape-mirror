package core.game.node.entity.skill.fishing;

import core.cache.def.impl.NPCDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to start fishing.
 * @author Ceikry
 * @version 1.2
 */
@Initializable
public final class FishingOptionPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.setOptionHandler("net", this);
		NPCDefinition.setOptionHandler("bait", this);
		NPCDefinition.setOptionHandler("lure", this);
		NPCDefinition.setOptionHandler("cage", this);
		NPCDefinition.setOptionHandler("harpoon", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		NPC npc = (NPC) node;
		FishingSpot spot = FishingSpot.forId(npc.getId());
		if (spot == null) {
			return false;
		}
		FishingOption opt = spot.getOptionByName(option);

		if (opt == null) {
			return false;
		}
		player.getPulseManager().run(new FishingPulse(player, npc, opt));
		return true;
	}

	@Override
	public Location getDestination(Node node, Node n) {
		return null;
	}
}
