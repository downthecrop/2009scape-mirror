package core.game.content.dialogue.book;

/**
 * Represents a book line.
 * @author 'Vexia
 * @date 31/12/2013
 */
public class BookLine {

	/**
	 * Represents the message to display.
	 */
	private final String message;

	/**
	 * Represents the child if of the line.
	 */
	private final int child;

	/**
	 * Constructs a new {@code Page} {@code Object}.
	 * @param message the message.
	 * @param child the child.
	 */
	public BookLine(final String message, final int child) {
		this.message = message;
		this.child = child;
	}

	/**
	 * Gets the message.
	 * @return The message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets the child.
	 * @return The child.
	 */
	public int getChild() {
		return child;
	}

}
