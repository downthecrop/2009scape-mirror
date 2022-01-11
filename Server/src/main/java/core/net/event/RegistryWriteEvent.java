package core.net.event;

import core.cache.misc.buffer.ByteBufferUtils;
import rs09.game.world.GameWorld;
import core.net.Constants;
import core.net.IoSession;
import core.net.IoWriteEvent;

import java.nio.ByteBuffer;

/**
 * Handles game world registry writing events.
 * @author Emperor
 */
public final class RegistryWriteEvent extends IoWriteEvent {

	/**
	 * The string check.
	 */
	private static final String CHECK = "12x4578f5g45hrdjiofed59898";

	/**
	 * Constructs a new {@code RegistryWriteEvent} {@code Object}.
	 * @param session The I/O session.
	 * @param context The writing context.
	 */
	public RegistryWriteEvent(IoSession session, Object context) {
		super(session, context);
	}

	@Override
	public void write(IoSession session, Object context) {
		ByteBuffer buffer = ByteBuffer.allocate(128);
		buffer.put((byte) GameWorld.getSettings().getWorldId());
		buffer.putInt(Constants.REVISION);
		buffer.put((byte) GameWorld.getSettings().getCountryIndex());
		buffer.put((byte) (GameWorld.getSettings().isMembers() ? 1 : 0));
		buffer.put((byte) (GameWorld.getSettings().isPvp() ? 1 : 0));
		buffer.put((byte) (GameWorld.getSettings().isQuickChat() ? 1 : 0));
		buffer.put((byte) (GameWorld.getSettings().isLootshare() ? 1 : 0));
		ByteBufferUtils.putString(GameWorld.getSettings().getActivity(), buffer);
		buffer.put(CHECK.getBytes());
		session.queue((ByteBuffer) buffer.flip());
	}

}