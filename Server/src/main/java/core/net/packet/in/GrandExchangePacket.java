package core.net.packet.in;

import api.ContentAPIKt;
import core.game.ge.GrandExchangeDatabase;
import core.game.ge.GrandExchangeEntry;
import core.game.node.entity.player.Player;
import core.net.packet.IncomingPacket;
import core.net.packet.IoBuffer;
import rs09.game.ge.GrandExchange;
import rs09.game.ge.GrandExchangeOffer;
import rs09.game.interaction.inter.ge.StockMarket;

/**
 * Represents the <b>Incoming</b> packet of the grand exchange.
 * @author Emperor
 */
public class GrandExchangePacket implements IncomingPacket {

	@Override
	public void decode(Player player, int opcode, IoBuffer buffer) {
		int itemId = buffer.getShort();
		GrandExchangeOffer offer = player.getAttribute("ge-temp", new GrandExchangeOffer());
		int index = player.getAttribute("ge-index", -1);
		offer.setItemID(itemId);
		offer.setSell(false);
		GrandExchangeEntry entry = GrandExchangeDatabase.getDatabase().get(itemId);
		if(entry == null || !ContentAPIKt.itemDefinition(itemId).isTradeable())
		{
			ContentAPIKt.sendMessage(player, "That item is blacklisted from the grand exchange.");
			return;
		}
		offer.setPlayer(player);
		offer.setAmount(1);
		offer.setOfferedValue(GrandExchange.getRecommendedPrice(itemId, false));
		offer.setIndex(index);
		StockMarket.updateVarbits(player, offer, index, false);
		player.setAttribute("ge-temp",offer);
		player.getPacketDispatch().sendString(GrandExchange.getOfferStats(itemId, false), 105, 142);
		player.getInterfaceManager().closeChatbox();
	}

}
