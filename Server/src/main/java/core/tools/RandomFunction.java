package core.tools;

import core.game.node.entity.npc.drop.DropFrequency;
import core.game.node.item.ChanceItem;
import core.game.node.item.Item;
import core.game.node.item.WeightedChanceItem;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a class used for random methods.
 * @author Vexia
 */
public class RandomFunction {

	/**
	 * The random instance.
	 */
	public static final Random RANDOM = new Random();

	/**
	 * Method used to ease the access of the random class.
	 * @param a the minimum random value.
	 * @param b the maximum random value.
	 * @return the value as an {@link Double}.
	 */
	public static final double random(double a, double b) {
		final double min = Math.min(a, b);
		final double max = Math.max(a, b);
		return min + (max - min) * RANDOM.nextDouble();
	}

	/**
	 * Method used to ease the access of the random class.
	 * @param a the minimum random value.
	 * @param b the maximum random value.
	 * @return the value as an {@link Integer}.
	 */
	public static final int random(int a, int b) {
		final int n = Math.abs(b - a);
		return Math.min(a, b) + (n == 0 ? 0 : random(n));
	}

	/**
	 * Method to roll for a random 1/X chance
	 * @param chance the 1/chance rate for the roll to succeed
	 * @return true if you hit the roll, false otherwise
	 */
	public static boolean roll(int chance){
		return random(chance) == chance / 2;
	}

	/**
	 * Calculates the chance of succeeding at a skilling event
	 * @param low - Success chance at level 1
	 * @param high - Success chance at level 99
	 * @param level - Level required
	 * @return percent chance of success
	 */
	public static double getSkillSuccessChance(double low, double high, int level) {
		// 99 & 98 numbers should *not* be adjusted for level cap > 99
		int value = (int)(Math.floor(low*( (99-level)/98.0 ) ) + Math.floor(high*((level-1)/98.0)) + 1);
		return Math.min(Math.max(value / 256D, 0), 1) * 100.0;
	}


	/**
	 * Returns either the supplied integer, or -1 times the supplied integer.
	 * @param value the value.
	 * @return the integer.
	 */
	public static int randomSign(int value) {
		return RANDOM.nextBoolean() ? value : -value;
	}

	/**
	 * Method used to return the integer.
	 * @param maxValue the value.
	 * @return the value.
	 */
	public static final int getRandom(int maxValue) {
		return (int) (Math.random() * (maxValue + 1));
	}

	/**
	 * Method used to return the random double.
	 * @param maxValue the value.
	 * @return the double.
	 */
	public static final double getRandomDouble(double maxValue) {
		return (Math.random() * (maxValue + 1));
	}

	/**
	 * Method used to ease the access of the random class.
	 * @param maxValue the maximum value.
	 * @return the random integer.
	 */
	public static final int random(int maxValue) {
		if (maxValue <= 0) {
			return 0;
		}
		return RANDOM.nextInt(maxValue);
	}

	public static final double randomDouble(double maxValue) {
		return ThreadLocalRandom.current().nextDouble(0.0,maxValue);
	}

	public static final double randomDouble(double min, double max){
		return ThreadLocalRandom.current().nextDouble(min,max);
	}

	public static int nextInt(int val)
	{
		return random(val);
	}

	/**
	 * Generates a random number with a distrobution like:
     * Where intensity is how intense the peak is (higher = more steep)
	 * 		      *
	 *          *   *
	 *         *      *
	 *     * *          * *
	 * * *                  * * *
	 */
	public static int normalRandDist(int max, int intensity) {
		int sum = 0;
		for (int j = 0; j < intensity; j++) {
			sum += RANDOM.nextInt(max);
		}
		return sum/intensity;
	}

	/**
	 * Generates a random number with a distribution like:
	 * 		      *
	 *          *   *
	 *         *      *
	 *     * *          * *
	 * * *                  * * *
	 */
	public static int normalRandDist(int max) {
		return (RANDOM.nextInt(max) + RANDOM.nextInt(max))/2;
	}

	/**
	 * Generates a random number with a distribution like:
	 *
	 * *
	 *    *
	 *       *
	 *          *
	 * See some results: https://www.desmos.com/calculator/clzv66l7hk
	 */
	public static int linearDecreaseRand(int max) {
		double seed = RANDOM.nextDouble();
		double modifier = RANDOM.nextDouble();
		return (int) (seed*modifier*max);
	}

	/**
	 * Generates a random number likely in the area above val (I think)
	 */
	public static int normalPlusWeightRandDist(int val, int weight)
	{
		int normalDistRand = (RANDOM.nextInt(val) + RANDOM.nextInt(val))/2;
		return normalDistRand/2 + weight < val ? normalDistRand/2 + weight : normalPlusWeightRandDist(val, weight - 1);
	}

	/**
	 * Gets a chance item.
	 * @param items the items.
	 * @return the chance.
	 */
	public static final ChanceItem getChanceItem(final ChanceItem[] items) {
		double total = 0;
		for (ChanceItem i : items) {
			total += i.getChanceRate();
		}
		final int random = random((int) total);
		double subTotal = 0;
		List<ChanceItem> choices = new ArrayList<>(20);
		for (ChanceItem item : items) {
			choices.add(item);
		}
		Collections.shuffle(choices);
		for (ChanceItem i : choices) {
			subTotal += i.getChanceRate();
			if (random < subTotal) {
				return i;
			}
		}
		return null;
	}

	public static List<Item> rollChanceTable(boolean atLeastOne,List<ChanceItem> table){
		final List<Item> rewards = new ArrayList<>(20);
		final List<Item> always_rewards = new ArrayList<>(20);
		final List<ChanceItem> chanceTable = new ArrayList<ChanceItem>(table);
		boolean isAllAlways = false;
		if(table.stream().filter(item -> item.getChanceRate() == 1).count() == table.size()){
			isAllAlways = true;
		}
		if(table.size() == 1){
			atLeastOne = false;
		}
		if(!isAllAlways) {
			if (atLeastOne) {
				while (rewards.isEmpty()) {
					Collections.shuffle(chanceTable);
					for (ChanceItem item : chanceTable) {
						if (item.getChanceRate() == 0.0) {
							item.setChanceRate(DropFrequency.rate(item.getDropFrequency()));
						}
						boolean roll = RandomFunction.random(1, (int) item.getChanceRate()) == 1;
						if (item.getChanceRate() != 1) {
							if (roll) {
								rewards.add(item.getRandomItem());
								break;
							}
						}
					}
				}
			} else {
				Collections.shuffle(chanceTable);
				for (ChanceItem item : chanceTable) {
					if (item.getChanceRate() == 0.0) {
						item.setChanceRate(DropFrequency.rate(item.getDropFrequency()));
					}
					boolean roll = RandomFunction.random(1, (int) item.getChanceRate()) == 1;
					if (item.getChanceRate() != 1) {
						if (roll) {
							rewards.add(item.getRandomItem());
							break;
						}
					}
				}
			}
		}
		table.stream().filter(item -> item.getChanceRate() == 1).forEach(item -> {
			if(item.getChanceRate() == 0.0){
				item.setChanceRate(DropFrequency.rate(item.getDropFrequency()));
			}
			always_rewards.add(item.getRandomItem());
		});
		return Stream.concat(rewards.stream(),always_rewards.stream()).collect(Collectors.toList());
	}

	public static List<Item> rollChanceTable(boolean atLeastOne,ChanceItem... table){
		return rollChanceTable(atLeastOne, Arrays.asList(table));
	}

	public static Item rollWeightedChanceTable(WeightedChanceItem... table){
		return rollWeightedChanceTable(new ArrayList<>(Arrays.asList(table)));
	}

	public static Item rollWeightedChanceTable(List<WeightedChanceItem> table){
		int sumOfWeights = table.stream().mapToInt(item -> item.weight).sum();
		int rand = random(sumOfWeights);
		Collections.shuffle(table);
		for(WeightedChanceItem item : table){
			if(rand <= item.weight)
				return item.getItem();
			rand -= item.weight;
		}
		//We should get here if and only if the weighted chance table is empty.
		//System.out.println("ERROR ROLLING WEIGHTED CHANCE: WEIGHT SUM AND INDIVIDUAL WEIGHTS DO NOT MATCH!!");
		return null;
	}

	/**
	 * Gets a random value from the array.
	 * @param array The array.
	 * @return A random element of the array.
	 */
	public static <T> T getRandomElement(T[] array) {
		return (T) array[randomize(array.length)];
	}

	/**
	 * Randomizes the value.
	 * @param value the value to randomize.
	 * @return The random value.
	 */
	public static int randomize(int value) {
		if (value < 1) {
			return 0;
		}
		return RANDOM.nextInt(value);
	}
}
