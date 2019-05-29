package org.crandor.game.content.global.consumable;

import org.crandor.game.content.skill.SkillBonus;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a potion effect enumeration.
 * @author Emperor
 */
public enum PotionEffect {
	STRENGTH_POTION(new Effect("Strength potion", new int[] { 113, 115, 117, 119 }, new SkillBonus(Skills.STRENGTH, 0.08, 1))), ATTACK_POTION(new Effect("Attack potion", new int[] { 2428, 121, 123, 125 }, new SkillBonus(Skills.ATTACK, 0.08, 1))), DEFENCE_POTION(new Effect("Defence potion", new int[] { 2432, 133, 135, 137 }, new SkillBonus(Skills.DEFENCE, 0.08, 1))), RANGING_POTION(new Effect("Ranging potion", new int[] { 2444, 169, 171, 173 }, new SkillBonus(Skills.RANGE, 0.08, 1))), MAGIC_POTION(new Effect("Magic potion", new int[] { 3040, 3042, 3044, 3046 }, new SkillBonus(Skills.MAGIC, 0.08, 1))), SUPER_STRENGTH(new Effect("super strength", new int[] { 2440, 157, 159, 161 }, new SkillBonus(Skills.STRENGTH, 0.15, 5))), SUPER_ATTACK(new Effect("super attack", new int[] { 2436, 145, 147, 149 }, new SkillBonus(Skills.ATTACK, 0.15, 5))), SUPER_DEFENCE(new Effect("super defence", new int[] { 2442, 163, 165, 167 }, new SkillBonus(Skills.DEFENCE, 0.15, 5))), ANTI_POISON(new Effect("Antipoison", new int[] { 2446, 175, 177, 179 }, null)), STRONG_ANTI_POISON(new Effect("Antipoison+", new int[] { 5943, 5945, 5947, 5949 }, null)), SUPER_STRONG_ANTI_POISON(new Effect("Antipoison++", new int[] { 5952, 5954, 5956, 5958 }, null)), SUPER_ANTIPOISON(new Effect("Super antipoison", new int[] { 2448, 181, 183, 185 }, null)), RELICYMS_BALM(new Effect("Relicym's balm", new int[] { 4842, 4844, 4846, 4848 }, null)), AGILITY_POTION(new Effect("Agility potion", new int[] { 3032, 3034, 3036, 3038 }, new SkillBonus(Skills.AGILITY, 0.04))), HUNTER_POTION(new Effect("Hunter potion", new int[] { 9998, 10000, 10002, 10004 }, new SkillBonus(Skills.HUNTER, 0.04)));

	/**
	 * Constructs a new {@code PotionEffect} {@code Object}.
	 * @param name The potion name (as it appears in the message).
	 * @param itemIds The item ids of all the doses (starting from full potion
	 * to 1 dose).
	 * @param skillBonus The skill bonus.
	 */
	PotionEffect(final Effect effect) {
		this.effect = effect;
	}

	/**
	 * Represents the potion effect.
	 */
	private final Effect effect;

	/**
	 * The potions mapping.
	 */
	private static Map<Integer, PotionEffect> potions = new HashMap<>();

	/**
	 * Populate the potions mapping.
	 */
	static {
		for (PotionEffect p : PotionEffect.values())
			for (int id : p.effect.getItemIds()) {
				potions.put(id, p);
			}
	}

	/**
	 * @return the name.
	 */
	public String getName() {
		return effect.getName();
	}

	/**
	 * @return the itemIds.
	 */
	public int[] getItemIds() {
		return effect.getItemIds();
	}

	/**
	 * Gets the skill bones.
	 * @param player the player.
	 * @return the bonus.
	 */
	public SkillBonus getSkillBonus(final Player player) {
		return effect.getSkillBonus(player);
	}

	/**
	 * @return the skillBonus.
	 */
	public SkillBonus getSkillBonus() {
		return effect.getSkillBonus();
	}

	/**
	 * Getsd the effect.
	 * @return the effect.
	 */
	public Effect getEffect() {
		return effect;
	}

	/**
	 * Get the potion for the given item id.
	 * @param itemId The item id.
	 * @return The potion.
	 */
	public static PotionEffect forId(int itemId) {
		return potions.get(itemId);
	}

	/**
	 * Represents a potion effect.
	 * @author Emperor
	 * @author 'Vexia
	 */
	public static class Effect {

		/**
		 * The name of the potion.
		 */
		private final String name;

		/**
		 * The item ids.
		 */
		private final int[] itemIds;

		/**
		 * The skill bonus.
		 */
		private final SkillBonus skillBonus;

		/**
		 * Constructs a new {@code PotionEffect} {@code Object}.
		 * @param name the name.
		 * @param itemIds the item ids.
		 * @param skillBonus the bonus.
		 */
		public Effect(String name, int[] itemIds, SkillBonus skillBonus) {
			this.name = name;
			this.itemIds = itemIds;
			this.skillBonus = skillBonus;
		}

		/**
		 * @return the name.
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the itemIds.
		 */
		public int[] getItemIds() {
			return itemIds;
		}

		/**
		 * Gets the skill bones.
		 * @param player the player.
		 * @return the bonus.
		 */
		public SkillBonus getSkillBonus(final Player player) {
			return skillBonus;
		}

		/**
		 * @return the skillBonus.
		 */
		public SkillBonus getSkillBonus() {
			return skillBonus;
		}
	}
}