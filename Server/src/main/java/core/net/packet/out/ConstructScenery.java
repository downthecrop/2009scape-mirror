package core.net.packet.out;

import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.context.BuildSceneryContext;

/**
 * The construct scenery packet.
 * @author Emperor
 */
public final class ConstructScenery implements OutgoingPacket<BuildSceneryContext> {

	/**
	 * Writes the packet.
	 * @param buffer The buffer.
	 * @param object The object.
	 */
	public static IoBuffer write(IoBuffer buffer, Scenery object) {
		Location l = object.getLocation();
		buffer.put(179).putA((object.getType() << 2) | (object.getRotation() & 0x3)).put((l.getChunkOffsetX() << 4) | (l.getChunkOffsetY() & 0x7)).putShortA(object.getId());
		return buffer;
	}

	@Override
	public void send(BuildSceneryContext context) {
		Player player = context.getPlayer();
		Scenery o = context.getScenery();
		IoBuffer buffer = write(UpdateAreaPosition.getBuffer(player, o.getLocation().getChunkBase()), o);
		buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().getOutput());player.getSession().write(buffer);

	}

}
