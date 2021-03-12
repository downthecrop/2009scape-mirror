package core.game.interaction.npc;

import core.cache.def.impl.NPCDefinition;
import core.game.content.global.travel.ship.ShipCharter;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to handle the "charter" option.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class ShipCharterNPCPlugin extends OptionHandler {

	/**
	 * Represents the ship charter npcs.
	 */
	private static final int[] IDS = new int[] { 4650, 4651, 4652, 4653, 4654, 4655, 4656 };

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (int id : IDS) {
			NPCDefinition.forId(id).getHandlers().put("option:charter", this);
		}
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		ShipCharter.open(player);
		return true;
	}

}
