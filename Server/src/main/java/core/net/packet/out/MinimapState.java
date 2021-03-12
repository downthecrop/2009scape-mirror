package core.net.packet.out;

import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.context.MinimapStateContext;

/**
 * Handles the sending of the minimap state outgoing packet.
 * @author Emperor
 */
public final class MinimapState implements OutgoingPacket<MinimapStateContext> {

	@Override
	public void send(MinimapStateContext context) {
		IoBuffer buffer = new IoBuffer(192).put(context.getState());
		buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().getOutput());context.getPlayer().getDetails().getSession().write(buffer);
	}

}