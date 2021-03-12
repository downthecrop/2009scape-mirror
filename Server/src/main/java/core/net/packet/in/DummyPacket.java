package core.net.packet.in;

import core.game.node.entity.player.Player;
import rs09.game.system.SystemLogger;
import core.net.packet.IncomingPacket;
import core.net.packet.IoBuffer;

public class DummyPacket implements IncomingPacket {
    @Override
    public void decode(Player player, int opcode, IoBuffer buffer) {
        SystemLogger.logInfo("Received opcode " + opcode + " packet.");
    }
}
