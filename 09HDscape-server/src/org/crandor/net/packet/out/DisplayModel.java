package org.crandor.net.packet.out;

import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.OutgoingPacket;
import org.crandor.net.packet.context.DisplayModelContext;

/**
 * Represents the outgoing packet for the displaying of a node model on an
 * interface.
 * @author Emperor
 */
public final class DisplayModel implements OutgoingPacket<DisplayModelContext> {

	@Override
	public void send(DisplayModelContext context) {
		IoBuffer buffer;
		switch (context.getType()) {
		case PLAYER:
			buffer = new IoBuffer(66);
			buffer.putLEShortA(context.getPlayer().getInterfaceManager().getPacketCount(1));
			buffer.putIntA(context.getInterfaceId() << 16 | context.getChildId());
			break;
		case NPC:
			buffer = new IoBuffer(73);
			buffer.putShortA(context.getNodeId());
			buffer.putLEInt((context.getInterfaceId() << 16) | context.getChildId());
			buffer.putLEShort(context.getPlayer().getInterfaceManager().getPacketCount(1));
			break;
		case ITEM:
			int value = context.getAmount() > 0 ? context.getAmount() : context.getZoom();
			buffer = new IoBuffer(50);
			buffer.putInt(value);
			buffer.putIntB((context.getInterfaceId() << 16) | context.getChildId());
			buffer.putLEShortA(context.getNodeId());
			buffer.putLEShort(context.getPlayer().getInterfaceManager().getPacketCount(1));
			break;
		case MODEL:
			buffer = new IoBuffer(130);
			buffer.putLEInt(context.getInterfaceId() << 16 | context.getChildId());
			buffer.putLEShortA(context.getPlayer().getInterfaceManager().getPacketCount(1));
			buffer.putShortA(context.getNodeId());
			break;
		default:
			return;
		}
		context.getPlayer().getSession().write(buffer);
	}

}