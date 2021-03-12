package core.game.content.dialogue.book;

/**
 * Represents a set of pages on a book.
 * @author 'Vexia
 */
public class PageSet {

	/**
	 * Represents the set of pages.
	 */
	private final Page[] pages;

	/**
	 * Constructs a new {@code PageSet} {@code Object}.
	 * @param pages the pages.
	 */
	public PageSet(final Page... pages) {
		this.pages = pages;
	}

	/**
	 * Gets the pages.
	 * @return The pages.
	 */
	public Page[] getPages() {
		return pages;
	}
}
