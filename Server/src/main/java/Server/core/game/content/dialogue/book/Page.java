package core.game.content.dialogue.book;

/**
 * Represents a page on a book.
 * @author 'Vexia
 * @date 1/1/14
 */
public class Page {

	/**
	 * Represents the lines on a page.
	 */
	private final BookLine[] lines;

	/**
	 * Constructs a new {@code Page} {@code Object}.
	 * @param lines the lines.
	 */
	public Page(BookLine... lines) {
		this.lines = lines;
	}

	/**
	 * Gets the lines.
	 * @return The lines.
	 */
	public BookLine[] getLines() {
		return lines;
	}

}
