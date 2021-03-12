package core.net.packet.out;

import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.PacketHeader;
import core.net.packet.context.RunScriptContext;

/**
 * The run script outgoing packet.
 * @author Snickerize
 */
public class RunScriptPacket implements OutgoingPacket<RunScriptContext> {

	/*@Override
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
	}*/
	@Override
	public void send(RunScriptContext context) {
		String types = context.getString();
		Object[] objects = context.getObjects();
		IoBuffer buffer = new IoBuffer(115, PacketHeader.SHORT);
		buffer.putShort(context.getPlayer().getInterfaceManager().getPacketCount(1));

		buffer.putString(types);
		int j = 0;
		for (int i = (types.length() - 1); i >= 0; i--) {
			if (types.charAt(i) == 's') {
				buffer.putString((String) objects[j]);
			} else {
				buffer.putInt((Integer) objects[j]);
			}
			j++;
		}

		buffer.putInt(context.getId());
		buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().getOutput());context.getPlayer().getDetails().getSession().write(buffer);
	}
}
