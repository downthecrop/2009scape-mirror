package core.net.packet.in;

import core.game.node.entity.player.Player;
import rs09.game.system.command.CommandSystem;
import core.game.system.monitor.PlayerMonitor;
import rs09.game.world.GameWorld;
import core.net.packet.IncomingPacket;
import core.net.packet.IoBuffer;

/**
 * Handles an incoming command packet.
 * @author Emperor
 * @author 'Vexia
 */
public final class CommandPacket implements IncomingPacket {

	@Override
	public void decode(Player player, int opcode, IoBuffer buffer) {
		final int data = buffer.get();
		if (buffer.toByteBuffer().hasRemaining()) {
			final String message = ((char) data + buffer.getString()).toLowerCase();
			if (!GameWorld.getSettings().isDevMode()) {
				int last = player.getAttribute("commandLast", 0);
				if (last > GameWorld.getTicks()) {
					return;
				}
				player.setAttribute("commandLast", GameWorld.getTicks() + 1);
			}
			if (CommandSystem.Companion.getCommandSystem().parse(player, message)) {
				player.getMonitor().log(message, PlayerMonitor.COMMAND_LOG);
			}
		}
	}

}