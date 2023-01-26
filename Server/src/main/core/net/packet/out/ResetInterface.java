package core.net.packet.out;

import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.context.InterfaceContext;

public class ResetInterface implements OutgoingPacket<InterfaceContext> {
    @Override
    public void send(InterfaceContext ic) {
        IoBuffer buffer = new IoBuffer(149);
        buffer.putShort(ic.getPlayer().getInterfaceManager().getPacketCount(1));
        buffer.putInt(ic.getInterfaceId());
        ic.getPlayer().getSession().write(buffer);
    }
}
