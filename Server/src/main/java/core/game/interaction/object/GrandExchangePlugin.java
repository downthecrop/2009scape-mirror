package core.game.interaction.object;

import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.plugin.Initializable;
import core.game.ge.GEGuidePrice;
import core.game.ge.GrandExchangeDatabase;
import core.game.ge.GEGuidePrice.GuideType;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.plugin.Plugin;
import rs09.game.interaction.inter.ge.ExchangeItemSets;
import rs09.game.interaction.inter.ge.StockMarket;

/**
 * Represents the plugin used for grand exchange npc and object options.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class GrandExchangePlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(28089).getHandlers().put("option:use", this);
		SceneryDefinition.forId(28089).getHandlers().put("option:exchange", this);
		SceneryDefinition.forId(28089).getHandlers().put("option:collect", this);
		SceneryDefinition.forId(28089).getHandlers().put("option:history", this);
		SceneryDefinition.forId(28089).getHandlers().put("option:sets", this);
		for (int i : new int[] { 6528, 6529, 6530, 6531 }) {
			NPCDefinition.forId(i).getHandlers().put("option:exchange", this);
			NPCDefinition.forId(i).getHandlers().put("option:history", this);
			NPCDefinition.forId(i).getHandlers().put("option:sets", this);
		}
		NPCDefinition.forId(6527).getHandlers().put("option:info-combat", this);
		NPCDefinition.forId(6526).getHandlers().put("option:info-logs", this);
		NPCDefinition.forId(6525).getHandlers().put("option:info-runes", this);
		NPCDefinition.forId(6524).getHandlers().put("option:info-herbs", this);
		NPCDefinition.forId(6523).getHandlers().put("option:info-ores", this);
		new GENPCPlugin().newInstance(arg);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (player.getIronmanManager().checkRestriction() && !option.equals("sets")) {
		    return true;
		}
		switch (option) {
		case "use":
			player.getDialogueInterpreter().open(6528, NPC.create(6528, player.getLocation()));
			break;
		case "exchange":
			if (!GrandExchangeDatabase.hasInitialized()) {
				player.getDialogueInterpreter().sendDialogue("The Grand Exchange desk seems to be closed...");
				break;
			}
			StockMarket.openFor(player);
			break;
		case "history":
			player.getExchangeRecords().openHistoryLog(player);
			break;
		case "collect":
			if (!GrandExchangeDatabase.hasInitialized()) {
				player.getDialogueInterpreter().sendDialogue("The Grand Exchange desk seems to be closed...");
				break;
			}
			player.getExchangeRecords().openCollectionBox();
			break;
		case "info-logs":
			GEGuidePrice.open(player, GuideType.LOGS);
			break;
		case "info-ores":
			GEGuidePrice.open(player, GuideType.ORES);
			break;
		case "info-herbs":
			GEGuidePrice.open(player, GuideType.HERBS);
			break;
		case "info-runes":
			GEGuidePrice.open(player, GuideType.RUNES);
			break;
		case "info-combat":
			GEGuidePrice.open(player, GuideType.WEAPONS_AND_ARMOUR);
			break;
		case "sets":
			ExchangeItemSets.openFor(player);
			break;
		}
		return true;
	}

	@Override
	public Location getDestination(Node node, Node n) {
		if (n instanceof NPC) {
			switch (n.getDirection()) {
			case EAST:
				return n.getLocation().transform(-1, 0, 0);
			case NORTH:
				return n.getLocation().transform(0, 1, 0);
			case SOUTH:
				return n.getLocation().transform(0, -1, 0);
			case WEST:
				return n.getLocation().transform(1, 0, 0);
			default:
				break;
			}
		}
		return node.getLocation().getDistance(n.getLocation()) < 2 ? node.getLocation() : null;
	}

	/**
	 * Represents a g.e npc option handler.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public final class GENPCPlugin extends OptionHandler {

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			NPCDefinition.forId(6528).getHandlers().put("option:talk-to", this);
			NPCDefinition.forId(6529).getHandlers().put("option:talk-to", this);
			NPCDefinition.forId(6535).getHandlers().put("option:talk-to", this);
			NPCDefinition.forId(6533).getHandlers().put("option:talk-to", this);
			NPCDefinition.forId(6535).getHandlers().put("option:bank", this);
			NPCDefinition.forId(6533).getHandlers().put("option:collect", this);
			return this;
		}

		@Override
		public boolean handle(Player player, Node node, String option) {
			switch (option) {
			case "talk-to":
				final NPC npc = (NPC) node;
				return player.getDialogueInterpreter().open(npc.getId(), npc);
			case "bank":
				player.getBank().open();
				break;
			case "collect":
				player.getExchangeRecords().openCollectionBox();
				break;
			}
			return true;
		}

		@Override
		public Location getDestination(Node node, Node n) {
			if (node instanceof Player) {
				Player player = (Player) node;
				if (player.getZoneMonitor().isInZone("Donator Zone")) {
					return null;
				}
			}
			switch (n.getDirection()) {
			case EAST:
				return n.getLocation().transform(1, 0, 0);
			case NORTH:
				return n.getLocation().transform(0, 1, 0);
			case SOUTH:
				return n.getLocation().transform(0, -1, 0);
			case WEST:
				return n.getLocation().transform(-1, 0, 0);
			default:
				break;
			}
			return null;
		}
	}
}
