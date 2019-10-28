package org.crandor.game.content.global.consumable;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.audio.Audio;
import org.crandor.game.node.item.Item;

/**
 * Represents a drink.
 * @author 'Vexia
 * @date 23/12/2013
 */
public class Drink extends Consumable {

	/**
	 * Represents the sound of drinking.
	 */
	public static final Audio SOUND = new Audio(4580, 1, 0);

	/**
	 * Represents the drink consumtions.
	 */
	private Item[] drinks;

	/**
	 * Constructs a new {@code Drink} {@code Object}.
	 */
	public Drink() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code Drink} {@code Object}.
	 * @param item the item.
	 * @param properties the properties.
	 */
	public Drink(Item item, ConsumableProperties properties, final Item[] drinks, final String... messages) {
		super(item, properties);
		this.drinks = drinks;
		this.messages = messages;
	}

	/**
	 * Constructs a new {@code Drink} {@code Object}.
	 * @param item the item.
	 * @param properties the properties.
	 */
	public Drink(final int item, final ConsumableProperties properties) {
		super(new Item(item), properties);
	}

	/**
	 * Constructs a new {@code Drink} {@code Object}.
	 * @param item the item.
	 * @param properties the properties.
	 * @param messages the messages
	 */
	public Drink(Item item, ConsumableProperties properties, final String... messages) {
		super(item, properties);
		this.messages = messages;
	}

	/**
	 * Constructs a new {@code Drink} {@code Object}.
	 * @param item the item.
	 * @param properties the properties.
	 * @param messages the messages
	 */
	public Drink(final int item, final ConsumableProperties properties, final String... messages) {
		this(new Item(item), properties, messages);
	}

	@Override
	public void consume(final Item item, final Player player) {
		final int initial = player.getSkills().getLifepoints();
		remove(player, item);
		message(player, item, initial);
	}

	/**
	 * Gets the drink consumtions.
	 * @return the drinks.
	 */
	public Item[] getDrinks() {
		return drinks;
	}

}
