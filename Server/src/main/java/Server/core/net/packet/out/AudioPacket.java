package core.net.packet.out;

import core.game.node.entity.player.link.audio.Audio;
import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.context.DefaultContext;

/**
 * Sends an audio packet.
 * @author Vexia
 */
public class AudioPacket implements OutgoingPacket<DefaultContext> {

	//208 music effect
	//4 music
	//172 sound effect
	@Override
	public void send(DefaultContext context) {
		final Audio audio = (Audio) context.getObjects()[0];
		IoBuffer buffer = new IoBuffer(172);
		buffer.putShort(audio.getId());
		buffer.put((byte) audio.getVolume());
		buffer.putShort(audio.getDelay());
		buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().getOutput());context.getPlayer().getSession().write(buffer);
	}

}
