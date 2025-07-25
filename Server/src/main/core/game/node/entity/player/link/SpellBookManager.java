package core.game.node.entity.player.link;

import core.game.component.Component;
import core.game.node.entity.combat.spell.MagicSpell;
import core.game.node.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static core.api.ContentAPIKt.setVarbit;

/**
 * Represents a managing class of a players spell book.
 * @author Vexia
 */
public final class SpellBookManager {

	/**
	 * Represents the current interface if of the spell book.
	 */
	private int spellBook = SpellBook.MODERN.getInterfaceId();

	/**
	 * Constructs a new {@code SpellBookManager} {@code Object}.
	 */
	public SpellBookManager() {
		/*
		 * empty.
		 */
	}

	/**
	 * Sets the spell book.
	 * @param book the spell book.
	 */
	public void setSpellBook(SpellBook book) {
		this.spellBook = book.getInterfaceId();
	}

	/**
	 * Updates the spell book.
	 * @param player the player.
	 */
	public void update(Player player) {
		player.getInterfaceManager().openTab(new Component(spellBook));
		setVarbit(player, 357, Objects.requireNonNull(SpellBook.forInterface(spellBook)).ordinal());
	}

	/**
	 * Gets the spell book.
	 * @return the spellBook
	 */
	public int getSpellBook() {
		return spellBook;
	}

	/**
	 * Represents a characters spell book.
	 * @author 'Vexia
	 * @author Emperor
	 */
	public enum SpellBook {

		/**
		 * The modern magic spell book.
		 */
		MODERN(192),

		/**
		 * The ancient magicks spell book.
		 */
		ANCIENT(193),

		/**
		 * The lunar magic spell book.
		 */
		LUNAR(430);

		/**
		 * The interface id of this spell book.
		 */
		private final int interfaceId;

		/**
		 * The spells mapping.
		 */
		private final Map<Integer, MagicSpell> spells = new HashMap<>();

		/**
		 * Creates the spell book.
		 * @param interfaceId The spellbook interface id.
		 */
		SpellBook(int interfaceId) {
			this.interfaceId = interfaceId;
		}

		/**
		 * @return The interfaceId.
		 */
		public int getInterfaceId() {
			return interfaceId;
		}

		/**
		 * Method used to get the book.
		 * @param id the id.
		 */
		public static SpellBook forInterface(final int id) {
			for (SpellBook book : SpellBook.values()) {
				if (book.interfaceId == id) {
					return book;
				}
			}
			return null;
		}

		/**
		 * Registers a new spell.
		 * @param buttonId The button id.
		 * @param spell The spell.
		 */
		public void register(int buttonId, MagicSpell spell) {
			spell.setSpellId(buttonId);
			spells.put(buttonId, spell);
		}

		/**
		 * Gets a spell from the spellbook.
		 * @param buttonId The button id.
		 * @return The spell.
		 */
		public MagicSpell getSpell(int buttonId) {
			return spells.get(buttonId);
		}
	}

}