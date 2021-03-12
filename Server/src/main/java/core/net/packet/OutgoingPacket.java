package core.net.packet;

/**
 * Represents an outgoing packet.
 * @author Emperor
 * @param <T> The context type.
 */
public interface OutgoingPacket<T extends Context> {

	/**
	 * Sends the packet.
	 * @param context The context.
	 */
	public void send(T context);

}