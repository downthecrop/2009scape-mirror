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
		buffer.putShort(context.getId()).putString("");
		buffer.putInt(context.getValue());
		context.getPlayer().getDetails().getSession().write(buffer);
	}
}