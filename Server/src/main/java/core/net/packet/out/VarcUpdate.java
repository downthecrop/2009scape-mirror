package core.net.packet.out;

import core.game.node.entity.player.Player;
import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.context.VarcUpdateContext;

public class VarcUpdate implements OutgoingPacket<VarcUpdateContext> {
    @Override
    public void send(VarcUpdateContext varcUpdateContext) {
        Player player = varcUpdateContext.getPlayer();
        if(varcUpdateContext.value <= 255) {
            IoBuffer buffer = new IoBuffer(65);
            buffer.putLEShort(player.getInterfaceManager().getPacketCount(1));
            buffer.putC((byte) varcUpdateContext.value);
            buffer.putLEShortA(varcUpdateContext.varcId);
            player.getSession().write(buffer);
        } else {
            IoBuffer buffer = new IoBuffer(69);
            buffer.putLEShortA(player.getInterfaceManager().getPacketCount(1));
            buffer.putInt(varcUpdateContext.value);
            buffer.putShortA(varcUpdateContext.varcId);
            player.getSession().write(buffer);
        }
    }
}
