package core.game.content.activity.pestcontrol;

import core.cache.def.impl.NPCDefinition;
import core.game.content.global.travel.ship.Ships;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Plugin;

/**
 * Handles a pest control squire's options.
 * @author Emperor
 */
public final class PestControlSquire extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(3781).getHandlers().put("option:talk-to", this);
		NPCDefinition.forId(3781).getHandlers().put("option:leave", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		NPC squire = (NPC) node;
		PestControlSession session = squire.getExtension(PestControlSession.class);
		switch (option) {
		case "talk-to":
			if (session == null) {
				OptionHandler handler = NPCDefinition.getOptionHandlers().get(option);
				handler.handle(player, node, option);
				return true;
			} else {
				player.getDialogueInterpreter().open(3781, ((NPC) node), true);
			}
			return true;
		case "leave":
			if (session == null) {
				Ships.PEST_TO_PORT_SARIM.sail(player);
				return true;
			}
			player.getProperties().setTeleportLocation(session.getActivity().getLeaveLocation());
			return true;
		}
		return false;
	}

}