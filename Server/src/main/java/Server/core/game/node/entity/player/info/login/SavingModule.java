package core.game.node.entity.player.info.login;

import java.nio.ByteBuffer;

/**
 * Represents a saving module.
 * @author Emperor
 */
public interface SavingModule {

	/**
	 * Writes data on the byte buffer.
	 * @param buffer The byte buffer.
	 */
	void save(ByteBuffer buffer);

	/**
	 * Parses data from the byte buffer.
	 * @param buffer The byte buffer.
	 */
	void parse(ByteBuffer buffer);
}