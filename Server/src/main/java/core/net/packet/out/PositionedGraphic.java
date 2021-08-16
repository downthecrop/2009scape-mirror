package core.net.packet.out;

import core.game.world.map.Location;
import core.game.world.update.flag.context.Graphics;
import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.context.PositionedGraphicContext;
import rs09.game.system.SystemLogger;

/**
 * The positioned graphic outgoing packet.
 * @author Emperor
 */
public final class PositionedGraphic implements OutgoingPacket<PositionedGraphicContext> {

	@Override
	public void send(PositionedGraphicContext context) {
		Location l = context.getLocation();
		Graphics g = context.getGraphic();
		int offsetHash = (context.offsetX << 4) | context.offsetY;
		IoBuffer buffer = new IoBuffer()
				.put(26)					//update current scene x and scene y client-side
				.putC(context.sceneX)  		//this has to be done for each graphic being sent
				.put(context.sceneY)        //opcode 26 is the lastSceneX/lastSceneY update packet
				.put(17).put(offsetHash)   	//send the graphics
				.putShort(g.getId())
				.put(g.getHeight())
				.putShort(g.getDelay());
		context.getPlayer().getSession().write(buffer);
	}

}