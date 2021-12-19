package core.net.packet.in;

import static api.ContentAPIKt.*;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.RunScript;
import core.net.packet.IncomingPacket;
import core.net.packet.IoBuffer;
import core.tools.StringUtils;
import kotlin.jvm.functions.Function1;

/**
 * Represents the incoming packet to handle a run script.
 * @author 'Vexia
 */
public class RunScriptPacketHandler implements IncomingPacket {

	@Override
	public void decode(Player player, int opcode, IoBuffer buffer) {
		final Function1 script = player.getAttribute("runscript", null);
		if (script == null || player.getLocks().isInteractionLocked()) {
			return;
		}
		Object value = "";
		if (opcode == 244) {
			value = StringUtils.longToString(buffer.getLong());
		} else if (opcode == 23) {
			value = buffer.getInt();
		} else if (opcode == 65){
			value = buffer.getString();//"longInput"
		} else {
			value = buffer.getInt();
		}
		try {
			script.invoke(value);
		} catch (NumberFormatException nfe){
			sendDialogue(player, "That number's a bit large, don't you think?");
		}
		player.removeAttribute("runscript");
	}
}
