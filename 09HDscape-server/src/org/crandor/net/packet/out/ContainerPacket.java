package org.crandor.net.packet.out;

import org.crandor.game.container.ContainerEvent;
import org.crandor.game.node.item.Item;
import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.OutgoingPacket;
import org.crandor.net.packet.PacketHeader;
import org.crandor.net.packet.context.ContainerContext;

/**
 * Represents the outgoing container packet.
 * @author Emperor
 */
public final class ContainerPacket implements OutgoingPacket<ContainerContext> {

	@Override
	public void send(ContainerContext context) {
		IoBuffer buffer = null;
		if (context.isClear()) {
			buffer = new IoBuffer(144);
			buffer.putIntB(context.getInterfaceId() << 16 | context.getChildId());
		} else {
			boolean slotBased = context.getSlots() != null;
			buffer = new IoBuffer(slotBased ? 22 : 105, PacketHeader.SHORT);
			buffer.putShort(context.getInterfaceId());
			buffer.putShort(context.getChildId());
			buffer.putShort(context.getType());
			if (slotBased) {
				for (int slot : context.getSlots()) {
					buffer.putSmart(slot);
					Item item = context.getItems()[slot];
					if (item != null && !item.equals(ContainerEvent.NULL_ITEM)) {
						buffer.putShort(item.getId() + 1);
						int amount = item.getAmount();
						if (amount < 0 || amount > 254) {
							buffer.put(255).putInt(amount);
						} else {
							buffer.put(amount);
						}
					} else {
						buffer.putShort(0);
					}
				}
			} else {
				buffer.putShort(context.getItems().length);
				for (Item item : context.getItems())
					if (item != null) {
						int amount = item.getAmount();
						if (amount < 0 || amount > 254) {
							buffer.putS(255).putInt(amount);
						} else {
							buffer.putS(amount);
						}
						buffer.putShort(item.getId() + 1);
					} else {
						buffer.putS(0).putShort(0);
					}
			}
		}
		context.getPlayer().getSession().write(buffer);
	}

}