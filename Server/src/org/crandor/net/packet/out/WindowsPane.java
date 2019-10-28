package org.crandor.net.packet.out;

import org.crandor.net.packet.IoBuffer;
import org.crandor.net.packet.OutgoingPacket;
import org.crandor.net.packet.context.WindowsPaneContext;

/**
 * Handles the windows pane outgoing packet.
 * @author Emperor
 */
public final class WindowsPane implements OutgoingPacket<WindowsPaneContext> {

	@Override
	public void send(WindowsPaneContext context) {
		IoBuffer buffer = new IoBuffer(145);
		buffer.putLEShortA(context.getWindowId());
		buffer.putS(context.getType());
		buffer.putLEShortA(context.getPlayer().getInterfaceManager().getPacketCount(1));
		context.getPlayer().getDetails().getSession().write(buffer);
	}

}