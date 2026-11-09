package content.global.handlers.scenery;

import core.api.Container;
import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.ChanceItem;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import core.game.world.GameWorld;
import org.rs09.consts.Items;

import static core.api.ContentAPIKt.addItem;

/**
 * Represents the thieving guide plugin.
 * @author 'Vexia
 * @date 16/11/2013
 */
@Initializable
public class ThievingGuidePlugin extends OptionHandler {

	/**
	 * The coin looots.
	 */
	private static final ChanceItem[] COINS = new ChanceItem[] {
		new ChanceItem(Items.COINS_995, 20, 20, 90.0),
		new ChanceItem(Items.COINS_995, 40, 40, 80.0)
	};

	/**
	 * The gem items.
	 */
	private static final ChanceItem[] GEMS = new ChanceItem[] {
		new ChanceItem(Items.UNCUT_SAPPHIRE_1623, 1, 1, 80.0),
		new ChanceItem(Items.UNCUT_EMERALD_1621,  1, 1, 60.0),
		new ChanceItem(Items.UNCUT_RUBY_1619,     1, 1, 8.0),
		new ChanceItem(Items.UNCUT_DIAMOND_1617,  1, 1, 7.0)
	};

	/**
	 * Represents the stethoscope item.
	 */
	private static final Item SETHOSCOPE = new Item(5560);

	/**
	 * Represents the required level to crack a safe.
	 */
	private static final int level = 50;

	/**
	 * Represents the experience gained.
	 */
	private static final double experience = 70;

	/**
	 * Represents the animations to use.
	 */
	private static final Animation[] animations = new Animation[] { new Animation(2247), new Animation(2248), new Animation(1113), new Animation(2244) };

	/**
	 * Represents the cracked safe.
	 */
	private static final int CRACKED_SAFE = 7238;

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(7236).getHandlers().put("option:crack", this);// wall
		// safe.
		SceneryDefinition.forId(7227).getHandlers().put("option:disarm", this);// trap
		SceneryDefinition.forId(7256).getHandlers().put("option:open", this);
		return this;
	}

	@Override
	public boolean handle(final Player player, final Node node, String option) {
		switch (option) {
		case "open":
			player.getDialogueInterpreter().sendDialogues(2266, null, "And where do you think you're going? A little too eager", "I think. Come and talk to me before you go wandering", "around in there.");
			break;
		case "crack":
			if (player.getSkills().getLevel(Skills.THIEVING) < 50) {
				player.getPacketDispatch().sendMessage("You need to be level " + level + " thief to crack this safe.");
				return true;
			}
			if (player.getInventory().freeSlots() == 0) {
				player.getPacketDispatch().sendMessage("Not enough inventory space.");
				return true;
			}
			final boolean success = success(player, Skills.THIEVING);
			player.lock(4);
			player.getPacketDispatch().sendMessage("You start cracking the safe.");
			player.animate(animations[success ? 1 : 0]);
			GameWorld.getPulser().submit(new Pulse(3, player) {
				@Override
				public boolean pulse() {
					if (success) {
						handleSuccess(player, (Scenery) node);
						return true;
					}
					final boolean trapped = RandomFunction.random(3) == 1;
					if (trapped) {
						player.getPacketDispatch().sendMessage("You slip and trigger a trap!");
						player.animate(animations[2]);
						player.getImpactHandler().manualHit(player, RandomFunction.random(2, 6), HitsplatType.NORMAL);
						GameWorld.getPulser().submit(new Pulse(1) {
							@Override
							public boolean pulse() {
								player.animate(new Animation(-1, Priority.HIGH));
								return true;
							}
						});
					}
					return true;
				}
			});
			break;
		case "search":
			player.animate(animations[3]);
			player.getPacketDispatch().sendMessage("You temporarily disarm the trap!");
			break;
		}
		return true;
	}

	/**
	 * Handles the success.
	 * @param player the player.
	 * @param object the object.
	 */
	public void handleSuccess(final Player player, final Scenery object) {
		SceneryBuilder.replace(object, object.transform(CRACKED_SAFE), 1);
		player.getPacketDispatch().sendMessage("You get some loot.");
		player.getSkills().addExperience(Skills.THIEVING, experience, true);
		addReward(player);
	}

	/**
	 * Adds an item.
	 * @param player the player.
	 */
	private void addReward(Player player) {
		ChanceItem[] rolledTable = RandomFunction.nextBool() ? GEMS : COINS;
		ChanceItem reward = RandomFunction.getChanceItem(rolledTable);
		addItem(player, reward.getId(), reward.getAmount(), Container.INVENTORY);
	}

	/**
	 * Method used to determine the success of a player when thieving.
	 * @param player the player.
	 * @return <code>True</code> if succesfull, <code>False</code> if not.
	 */
	public final boolean success(final Player player, final int skill) {
		double level = player.getSkills().getLevel(skill);
		double req = 50;
		int mod = player.getInventory().containsItem(SETHOSCOPE) ? 8 : 17;
		double successChance = Math.ceil((level * 50 - req * mod) / req / 3 * 4);
		int roll = RandomFunction.random(99);
		if (successChance >= roll) {
			return true;
		}
		return false;
	}
}
