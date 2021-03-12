package core.cache.crypto;

/**
 * Represents a ISAAC key pair, for both input and output.
 * @author `Discardedx2
 */
public final class ISAACPair {

	/**
	 * The input cipher.
	 */
	private ISAACCipher input;

	/**
	 * The output cipher.
	 */
	private ISAACCipher output;

	/**
	 * Constructs a new {@code ISAACPair} {@code Object}.
	 * @param input The input cipher.
	 * @param output The output cipher.
	 */
	public ISAACPair(ISAACCipher input, ISAACCipher output) {
		this.input = input;
		this.output = output;
	}

	/**
	 * Gets the input cipher.
	 * @return The input cipher.
	 */
	public ISAACCipher getInput() {
		return input;
	}

	/**
	 * Gets the output cipher.
	 * @return The output cipher.
	 */
	public ISAACCipher getOutput() {
		return output;
	}

}
