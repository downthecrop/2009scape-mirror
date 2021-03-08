package core.game.content.dialogue.book;

import core.game.component.Component;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;
import core.plugin.PluginManifest;
import core.plugin.PluginType;

/**
 * Represents a dialogue book.
 * @author 'Vexia
 * @date 1/1/14
 */
@PluginManifest(type = PluginType.DIALOGUE)
public abstract class Book extends DialoguePlugin {

	/**
	 * Represents the component interface of the book.
	 */
	private static final Component INTERFACE = new Component(49);

	/**
	 * Represents the red string.
	 */
	protected static final String RED = "<col=8A0808>";

	/**
	 * Represents the blue string.
	 */
	protected static final String BLUE = "<col=08088A>";

	/**
	 * Represents the name of the book.
	 */
	protected String name;

	/**
	 * Represents the id of this book.
	 */
	protected int id;

	/**
	 * Represents the pages of this book.
	 */
	protected PageSet[] sets;

	/**
	 * Represents the index of the page set we're at.
	 */
	protected int index = -1;

	/**
	 * Constructs a new {@code Book} {@code Object}.
	 * @param name the name.
	 * @param id the id.
	 */
	public Book(final Player player, final String name, final int id, final PageSet... sets) {
		this.player = player;
		this.name = name;
		this.id = id;
		this.sets = sets;
	}

	/**
	 * Constructs a new {@code Book} {@code Object}.
	 */
	public Book() {
		/**
		 * empty.
		 */
	}

	@Override
	public boolean open(Object... args) {
		return open(player);
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		switch (buttonId) {
		case 112:
			player.getInterfaceManager().close();
			break;
		case 66:
		case 52:
			next();
			break;
		case 64:
		case 50:
			previous();
			break;
		}
		return true;
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return this;
	}

	/**
	 * Method used to open a book.
	 * @return <code>True</code> if succesfully opened.
	 */
	public boolean open(final Player player) {
		next();
		return true;
	}

	/**
	 * Method used to write the next dialogue.
	 */
	public void next() {
		player.lock();
		index++;
		if (index > sets.length - 1) {
			player.unlock();
			player.getInterfaceManager().close();
			return;
		}
		final Page[] set = sets[index].getPages();
		display(set);
		player.unlock();
	}

	/**
	 * Method used to write the previous dialogue.
	 */
	public void previous() {
		player.lock();
		index--;
		if (index < 0) {
			index = 0;
		}
		final Page[] set = sets[index].getPages();
		display(set);
		player.unlock();
	}

	/**
	 * Method used to display a set of pages.
	 * @param set the set.
	 */
	public void display(Page[] set) {
	}

	/**
	 * Method used when the book is finished.
	 */
	public void finish() {
	}

	/**
	 * Gets the name.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the id.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the index.
	 * @return The index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Sets the index.
	 * @param index The index to set.
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Gets the interface component.
	 * @return the component.
	 */
	public Component getInterface() {
		return INTERFACE;
	}

}
