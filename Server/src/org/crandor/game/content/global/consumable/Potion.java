package org.crandor.game.content.global.consumable;

import org.crandor.game.content.global.consumable.PotionEffect.Effect;
import org.crandor.game.content.skill.SkillBonus;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;

/**
 * Represents a potion.
 * @author Emperor
 * @author 'Vexia
 * @date 23/12/2013
 */
public class Potion extends Drink {

	/**
	 * Represents the effect the potion has.
	 */
	private Effect effect;

	/**
	 * Constructs a new {@code Potion} {@code Object}.
	 */
	public Potion() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code Potion} {@code Object}.
	 * @param item the item.
	 * @param properties the properties.
	 */
	public Potion(Item item, ConsumableProperties properties, final Effect effect, final Item... drinks) {
		super(item, properties, drinks);
		this.effect = effect;
		this.emptyItem = VIAL;
	}

	/**
	 * Constructs a new {@code Potion} {@code Object}.
	 * @param item the item.
	 * @param effect the effect.
	 */
	public Potion(Item item, final Effect effect) {
		super(item, null, toDrinkArray(effect));
		this.effect = effect;
	}

	/**
	 * Constructs a new {@code Potion} {@code Object}.
	 * @param item the item.
	 * @param effect the effect.
	 */
	public Potion(int item, final Effect effect) {
		super(new Item(item), null, toDrinkArray(effect));
		this.effect = effect;
	}

	/**
	 * Constructs a new {@code Potion} {@code Object}.
	 * @param effect the effect.
	 */
	public Potion(final Effect effect) {
		this(effect.getItemIds()[0], effect);
	}

	/**
	 * Constructs a new {@code Potion} {@code Object}.
	 * @param effect the ffect.
	 */
	public Potion(final PotionEffect effect) {
		this(effect.getItemIds()[0], effect.getEffect());
	}

	@Override
	public void consume(final Item item, final Player player) {
		effect(player, item);
		message(player, item, 0);
		remove(player, item);
	}

	@Override
	public void message(final Player player, final Item item, final int initial, String... messages) {
		player.animate(ANIMATION);
		player.getPulseManager().clear();
		player.getAudioManager().send(SOUND);
		player.getPacketDispatch().sendMessage("You drink some of " + (item.getName().contains("brew") ? "the foul liquid" : "your " + item.getName().replace("(4)", "").replace("(3)", "").replace("(2)", "").replace("(1)", "").trim().toLowerCase()) + ".");
	}

	@Override
	public void remove(Player player, Item item) {
		remove(player, item, 1, true);
	}

	/**
	 * Method used to remove an amount of doses.
	 * @param player the player.
	 * @param item the item.
	 * @param doses the doses.
	 * @param message if we message.
	 */
	public void remove(final Player player, final Item item, final int doses, boolean message) {
		int dose = getDose(item) - (doses == 1 ? 0 : doses - 1);
		int replaceId;
		if(item.getId() == 11465 || item.getId() == 11467){
			replaceId = (int) (dose == 1 ? VIAL.getId() : effect.getItemIds()[(2 - dose) + 1]);
		} else {
			replaceId = (int) (dose == 1 ? VIAL.getId() : effect.getItemIds()[(4 - dose) + 1]);
		}
		if (replaceId != -1) {
			dose--;
			player.getInventory().replace(new Item(replaceId), item.getSlot());
			if (message) {
				String m = dose == 1 ? "You have 1 dose of potion left." : dose == 0 ? "You have finished your potion." : "You have " + dose + " doses of potion left.";
				player.getPacketDispatch().sendMessage(m);
			}
		}
	}

	/**
	 * Method used to effect a player.
	 * @param player the player.
	 * @param item the item.
	 */
	public void effect(final Player player, final Item item) {
		SkillBonus b = effect.getSkillBonus(player);
		if (b != null) {
			int level = player.getSkills().getStaticLevel(b.getSkillId());
			level = (int) (b.getBaseBonus() + level + (level * b.getBonus()));
			if (player.getSkills().getLevel(b.getSkillId()) <= level) {
				player.getSkills().setLevel(b.getSkillId(), level);
			}
		}
	}

	/**
	 * Method used to handle an effect.
	 * @param player the player.
	 * @param b the bonus.
	 */
	public void effect(final Player player, SkillBonus b) {
		int level = player.getSkills().getStaticLevel(b.getSkillId());
		int boost = (int) (b.getBaseBonus() + (level * b.getBonus()));
		player.getSkills().updateLevel(b.getSkillId(), boost, level + boost);
	}

	/**
	 * Gets the dose item.
	 * @param item the item.
	 * @return the dose.
	 */
	public int getDose(Item item) {
		for (int i = 0; i < effect.getItemIds().length; i++) {
			if (effect.getItemIds()[i] == item.getId()) {
				return effect.getItemIds().length - i;
			}
		}
		return -1;
	}

	/**
	 * Gets the potion effect.
	 * @return the effect.
	 */
	public Effect getEffect() {
		return effect;
	}

	/**
	 * Gets the drink array of items.
	 * @parma effect the effect.
	 * @return the array.
	 */
	public static Item[] toDrinkArray(Effect effect) {
		Item[] array = new Item[effect.getItemIds().length];
		for (int i = 0; i < effect.getItemIds().length; i++) {
			array[i] = new Item(effect.getItemIds()[i]);
		}
		return array;
	}

	@Override
	public String getEmptyMessage(Item item) {
		return "You empty the vial.";
	}

	@Override
	public Item getEmptyItem() {
		return VIAL;
	}
}
