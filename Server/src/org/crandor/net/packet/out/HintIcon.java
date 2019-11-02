package org.crandor.net.packet.out;

import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.OutgoingPacket;
import org.crandor.net.packet.context.HintIconContext;

/**
 * Handles the Hint icon outgoing packet.
 * @author Emperor
 */
public final class HintIcon implements OutgoingPacket<HintIconContext> {

	@Override
	public void send(HintIconContext context) {
		IoBuffer buffer = new IoBuffer(217);
		buffer.put(context.getSlot() << 6 | context.getTargetType()).put(context.getArrowId());
		if (context.getArrowId() > 0) {
			if (context.getTargetType() == 1 || context.getTargetType() == 10) {
				buffer.putShort(context.getIndex()).putShort(0) // Skip 3 bytes
						.put(0);
			} else if (context.getLocation() != null) {
				buffer.putShort(context.getLocation().getX()).putShort(context.getLocation().getY()).put(context.getHeight());
			} else {
				buffer.putShort(0).putShort(0).put(0); // Skip all bytes.
			}
			buffer.putShort(context.getModelId());
		}
		context.getPlayer().getSession().write(buffer);
	}

}