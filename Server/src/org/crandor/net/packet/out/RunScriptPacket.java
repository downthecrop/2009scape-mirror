package org.crandor.net.packet.out;

import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.OutgoingPacket;
import org.crandor.net.packet.PacketHeader;
import org.crandor.net.packet.context.RunScriptContext;

/**
 * The run script outgoing packet.
 * @author Emperor
 */
public class RunScriptPacket implements OutgoingPacket<RunScriptContext> {

	@Override
	public void send(RunScriptContext context) {
		String string = context.getString();
		Object[] objects = context.getObjects();
		IoBuffer buffer = new IoBuffer(115, PacketHeader.SHORT);
		buffer.putShort(context.getPlayer().getInterfaceManager().getPacketCount(1));
		buffer.putString(string);
		int j = 0;
		for (int i = (string.length() - 1); i >= 0; i--) {
			if (string.charAt(i) == 's') {
				buffer.putString((String) objects[j]);
			} else {
				buffer.putInt((Integer) objects[j]);
			}
			j++;
		}
		buffer.putInt(context.getId());
		context.getPlayer().getDetails().getSession().write(buffer);
	}
}
