package org.crandor.net.packet.out;

import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.OutgoingPacket;
import org.crandor.net.packet.context.CSConfigContext;

/**
 * The outgoing packet for client script configs.
 * 
 * @author Torchic
 */
public class CSConfigPacket implements OutgoingPacket<CSConfigContext> {

	@Override
	public void send(CSConfigContext context) {
		IoBuffer buffer = new IoBuffer(115);
		buffer.putShort(context.getId());
		buffer.putString(context.getTypes());

		for (int i = context.getTypes().length() - 1; i >= 0; i--) {
			if (context.getTypes().charAt(i) == 's') {
				buffer.putString((String) context.getParameters()[i]);
			} else {
				buffer.putInt(((Number) context.getParameters()[i]).intValue());
			}
		}

		buffer.putInt(context.getValue());
		context.getPlayer().getDetails().getSession().write(buffer);
	}
}