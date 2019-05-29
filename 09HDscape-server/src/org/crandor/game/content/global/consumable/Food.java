package org.crandor.game.content.global.consumable;

import org.crandor.game.content.skill.free.cooking.CookingPulse;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.audio.Audio;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;

/**
 * Represents a consumable/cookable food.
 * @author 'Vexia
 * @date 22/12/2013
 */
public class Food extends Consumable {

	/**
	 * Represents the food consumtion sound.
	 */
	public static final Audio SOUND = new Audio(2393, 1, 1);

	/**
	 * Represents the raw representationf of this food (if any).
	 */
	private Item raw;

	/**
	 * Represents the burnt representation of this food (if any).
	 */
	private Item burnt;

	/**
	 * Represents the cooking properties of this food.
	 */
	private CookingProperties cookingProperties;

	/**
	 * Constructs a new {@code Food} {@code Object}.
	 * @param item the item.
	 * @param raw the raw item.
	 * @param burnt the burnt item.
	 * @param foodProperties the food properties.
	 * @param cookingProperties the cooking properties.
	 */
	public Food(final Item item, final Item raw, final Item burnt, ConsumableProperties foodProperties, CookingProperties cookingProperties) {
		super(item, foodProperties);
		this.raw = raw;
		this.burnt = burnt;
		this.cookingProperties = cookingProperties;
	}

	/**
	 * Constructs a new {@code Food} {@code Object}.
	 * @param item the item.
	 * @param foodProperties the food properties.
	 * @param cookingProperties the cooking properties.
	 */
	public Food(final Item item, final ConsumableProperties foodProperties, final CookingProperties cookingProperties) {
		this(item, null, null, foodProperties, cookingProperties);
	}

	/**
	 * Constructs a new {@code Food} {@code Object}.
	 * @param item the item id.
	 * @param raw the raw item id.
	 * @param burnt the burnt item id.
	 * @param foodProperties the food properties.
	 * @param cookingProperties the cooking properties.
	 */
	public Food(final int item, final int raw, final int burnt, final ConsumableProperties foodProperties, final CookingProperties cookingProperties) {
		this(new Item(item), new Item(raw), new Item(burnt), foodProperties, cookingProperties);
	}

	/**
	 * Constructs a new {@code Food} {@code Object}.
	 * @param foodProperties the food properties.
	 */
	public Food(final Item item, final Item raw, final Item burnt, ConsumableProperties foodProperties) {
		this(item, raw, burnt, foodProperties, null);
	}

	/**
	 * Constructs a new {@code Food} {@code Object}.
	 * @param item the item.
	 * @param foodProperties the consumable properties.
	 */
	public Food(final int item, ConsumableProperties foodProperties) {
		this(new Item(item), null, null, foodProperties);
	}

	/**
	 * Constructs a new {@code Food} {@code Object}.
	 * @param item the item.
	 * @param emptyItem the empty item.
	 * @param foodProperties the consumable properties.
	 */
	public Food(final int item, final int emptyItem, ConsumableProperties foodProperties) {
		this(new Item(item), null, null, foodProperties);
		this.emptyItem = new Item(emptyItem, 1);
	}

	/**
	 * Constructs a new {@code Food} {@code Object}.
	 * @param item the item.
	 * @param emptyItem the empty item.
	 * @param foodProperties the consumable properties.
	 */
	public Food(final int item, final int emptyItem, String emptyMessage, ConsumableProperties foodProperties) {
		this(new Item(item), null, null, foodProperties);
		this.emptyMessage = emptyMessage;
		this.emptyItem = new Item(emptyItem);
	}

	/**
	 * Constructs a new {@code Food} {@code Object}.
	 * @param item the item.
	 * @param health the health.
	 */
	public Food(final int item, final int health) {
		this(new Item(item), null, null, new ConsumableProperties(health));
	}

	/**
	 * Constructs a new {@code Food} {@code Object}.
	 * @param item the item.
	 * @param health the health.
	 */
	public Food(final int item, final int health, String... messages) {
		this(new Item(item), null, null, new ConsumableProperties(health));
		this.messages = messages;
	}

	/**
	 * Constructs a new {@code Food} {@code Object}.
	 */
	public Food() {
		/**
		 * empty.
		 */
	}

	@Override
	public void consume(final Item item, final Player player) {
		int restore = 2;
		if (getProperties() != null) {
			restore = getProperties().getHealing();
		}
		consume(item, player, restore, messages);
	}

	@Override
	public void consume(final Item item, final Player player, int heal, String... messages) {
		final int initial = player.getSkills().getLifepoints();
		remove(player, item);
		message(player, item, initial, messages == null ? this.messages : messages);
	}

	@Override
	public boolean interact(final Player player, final Node node) {
		if (node.getId() == 114 && !player.getQuestRepository().isComplete("Cook's Assistant")) {
			player.getPacketDispatch().sendMessage("You need to have completed the Cook's Assistant quest in order to use that range.");
			return false;
		}
		if (getCookingProperties().isRange() && ((GameObject) node).getName().toLowerCase().equals("fire")) {
			return false;
		}
		if (getCookingProperties().isSpit() && !((GameObject) node).getName().toLowerCase().equals("fire")) {
			return false;
		}
		return true;
	}

	/**
	 * Method used to start cooking the food.
	 * @param player the player.
	 * @param object the object.
	 * @param amount the amount.
	 */
	public void cook(final Player player, final GameObject object, final int amount) {
		player.getPulseManager().run(new CookingPulse(player, object, this, amount));
	}

	/**
	 * Gets the cookingProperties.
	 * @return The cookingProperties.
	 */
	public CookingProperties getCookingProperties() {
		return cookingProperties;
	}

	/**
	 * Gets the raw.
	 * @return The raw.
	 */
	public Item getRaw() {
		return raw;
	}

	/**
	 * Gets the burnt.
	 * @return The burnt.
	 */
	public Item getBurnt() {
		return burnt;
	}

	/**
	 * Gets the value if the food has cooking properties.
	 * @return <code>True</code> if so.
	 */
	public boolean hasCookingProperties() {
		return cookingProperties != null;
	}

	/**
	 * Gets the value if it has a raw item.
	 * @return <code>True</code> if so.
	 */
	public boolean hasRaw() {
		return raw != null;
	}

	/**
	 * Gets the value if it has a burnt item.
	 * @return <code>True</code> if so.
	 */
	public boolean hasBurnt() {
		return burnt != null;
	}

	/**
	 * Gets the message when you eat.
	 * @return the message to display.
	 */
	public String getEatMessage() {
		return "You eat the " + getItem().getName().toLowerCase() + ".";
	}

}
