package core.net.packet.context;

import core.game.node.entity.player.Player;
import rs09.game.system.SystemLogger;
import core.net.packet.Context;

/**
 * Represents a camera context.
 * @author Emperor
 */
public final class CameraContext implements Context {


	/**
	 * Represents the camera types used for determining which packet to send.
	 * @author Emperor
	 */
	public static enum CameraType {
		POSITION(154), 
		ROTATION(125), 
		SET(187), SHAKE(27), RESET(24);

		/**
		 * The opcode.
		 */
		private final int opcode;

		/**
		 * Constructs a new {@code CameraContext} {@Code Object}.
		 * @param opcode The opcode.
		 */
		private CameraType(int opcode) {
			this.opcode = opcode;
		}

		/**
		 * Gets the packet opcode.
		 * @return The opcode.
		 */
		public int opcode() {
			return opcode;
		}
	}

	/*
	 * rotateCamera(Player p, int x, int y, int z, int angle) { MessageBuilder
	 * bldr = new MessageBuilder(26); bldr.writeByte(y); //idk
	 * bldr.writeByte(x); //idk bldr.writeLEShortA(z >>> 2); //idk
	 * bldr.writeByte(0); // speed :S bldr.writeByteS(100);
	 */
	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The camera type.
	 */
	private final CameraType type;

	/**
	 * x The (local) x-coordinate of the camera.
	 */
	private final int x;

	/**
	 * The (local) y-coordinate of the camera.
	 */
	private final int y;

	/**
	 * The height.
	 */
	private final int height;

	/**
	 * The zoom speed.
	 */
	private final int zoomSpeed;

	/**
	 * The speed.
	 */
	private final int speed;

	/**
	 * Constructs a new {@code CameraContext} {@Code Object}.
	 * @param player The player.
	 * @param type The camera type.
	 * @param x The x-coordinate of the camera.
	 * @param y The y-coordinate of the camera.
	 * @param height The height.
	 * @param zoomSpeed The zoom speed.
	 * @param speed The speed.
	 */
	public CameraContext(Player player, CameraType type, int x, int y, int height, int speed, int zoomSpeed) {
		this.player = player;
		this.type = type;
		this.x = x;
		this.y = y;
		this.height = height;
		this.speed = speed;
		this.zoomSpeed = zoomSpeed;
	}

	/**
	 * Method used to transform a camera context.
	 * @param x the x.
	 * @param y the y.
	 * @return the new context.
	 */
	public CameraContext transform(final Player player, final int x, final int y) {
		return new CameraContext(player, type, this.x + x, this.y + y, height, speed, zoomSpeed);
	}

	/**
	 * Method used to transform the context.
	 * @param heightOffset the height offset.
	 * @return the new context.
	 */
	public CameraContext transform(final int heightOffset) {
		return new CameraContext(player, type, x, y, height + heightOffset, speed, zoomSpeed);
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the type.
	 * @return The type.
	 */
	public CameraType getType() {
		return type;
	}

	/**
	 * Gets the x.
	 * @return The x.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y.
	 * @return The y.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Gets the height.
	 * @return The height.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the speed.
	 * @return The speed.
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Gets the zoomSpeed.
	 * @return The zoomSpeed.
	 */
	public int getZoomSpeed() {
		return zoomSpeed;
	}

}