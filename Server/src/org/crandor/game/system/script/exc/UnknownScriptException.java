package org.crandor.game.system.script.exc;

/**
 * An exception thrown when the script type is unknown.
 * @author Emperor
 */
public class UnknownScriptException extends ScriptException {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = -4897522544605020858L;

	/**
	 * Constructs a new {@code UnknownScriptException} {@code Object}.
	 * @param message The cause.
	 * @param lineId The parsing line index.
	 */
	public UnknownScriptException(String message, int lineId) {
		super(message, lineId);
	}

}
