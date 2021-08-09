package core.net.packet.out;

import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.context.VarbitContext;

public class Varbit implements OutgoingPacket<VarbitContext> {
    @Override
    public void send(VarbitContext varbitContext) {
        IoBuffer buffer = new IoBuffer(37);
        buffer.put((byte) varbitContext.value);
        buffer.putLEShort(varbitContext.varbitId);
        varbitContext.getPlayer().getSession().write(buffer);
    }
}
