package core.net.packet.out;

import core.game.node.entity.player.Player;
import core.game.system.communication.Contact;
import core.net.amsc.WorldCommunicator;
import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.PacketHeader;
import core.net.packet.context.ContactContext;
import core.tools.StringUtils;

/**
 * Handles the contact packet sending.
 * @author Emperor
 */
public final class ContactPackets implements OutgoingPacket<ContactContext> {

	@Override
	public void send(ContactContext context) {
		IoBuffer buffer = null;
		Player player = context.getPlayer();
		switch (context.getType()) {
		case ContactContext.UPDATE_STATE_TYPE:
			buffer = new IoBuffer(197).put(WorldCommunicator.getState().value());
			break;
		case ContactContext.IGNORE_LIST_TYPE:
			buffer = new IoBuffer(126, PacketHeader.SHORT);
			for (String string : player.getCommunication().getBlocked()) {
				if (string.length() == 0) {
					continue;
				}
				buffer.putLong(StringUtils.stringToLong(string));
			}
			break;
		case ContactContext.UPDATE_FRIEND_TYPE:
			buffer = new IoBuffer(62, PacketHeader.BYTE);
			buffer.putLong(StringUtils.stringToLong(context.getName()));
			buffer.putShort(context.getWorldId());
			Contact c = player.getCommunication().getContacts().get(context.getName());
			if (c != null) {
				buffer.put((byte) c.getRank().getValue());
			} else {
				buffer.put((byte) 0);
			}
			if (context.isOnline()) {
				buffer.putString("World " + context.getWorldId());
			}
			break;
		}
		if (buffer != null) {
			buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().getOutput());player.getSession().write(buffer);
		}
	}

}