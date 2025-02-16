package content.global.skill.fletching;

import core.tools.RandomFunction;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.world.map.zone.ZoneBorders;
import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.tools.StringUtils;
import content.data.Quests;

/**
 * fletching skill pulse
 * @author ceik
 */
public final class FletchingPulse extends SkillPulse<Item> {

	/**
	 * Seers bank zone borders for the diary task
	 */
	private static final ZoneBorders bankZone = new ZoneBorders(2721,3493,2730,3487);

	/**
	 * Represents the animation used in this generic pulse.
	 */
	private static final Animation ANIMATION = new Animation(1248);

	/**
	 * Represents the item we are fletching.
	 */
	private Fletching.FletchingItems fletch;

	/**
	 * Represents the amount to fletch.
	 */
	private int amount = 0;

	/**
	 * Represents the amount to arrows fletched (for ogre arrow shafts which is a random number from 2-6).
	 */
	private int finalAmount = 0;

	/**
	 * Constructs a new {@code FletchingPulse.java} {@code Object}.
	 * @param player
	 * @param node
	 */
	public FletchingPulse(final Player player, final Item node, final int amount, final Fletching.FletchingItems fletch) {
		super(player, node);
		this.amount = amount;
		this.fletch = fletch;
	}

	@Override
	public boolean checkRequirements() {
		if (player.getSkills().getLevel(Skills.FLETCHING) < fletch.level) {
			player.getDialogueInterpreter().sendDialogue("You need a Fletching skill of " + fletch.level + " or above to make " + (StringUtils.isPlusN(fletch.getItem().getName().replace("(u)", "").trim()) ? "an" : "a") + " " + fletch.getItem().getName().replace("(u)", "").trim());
			return false;
		}
		if (amount > player.getInventory().getAmount(node)) {
			amount = player.getInventory().getAmount(node);
		}
		if (fletch == Fletching.FletchingItems.OGRE_ARROW_SHAFT) {
			if (player.getQuestRepository().getQuest(Quests.BIG_CHOMPY_BIRD_HUNTING).getStage(player) == 0) {
				player.getPacketDispatch().sendMessage("You must have started Big Chompy Bird Hunting to make those.");
				return false;
			}
		}
		if (fletch == Fletching.FletchingItems.OGRE_COMP_BOW) {
			// Technically, this isn't supposed to show up till you've asked Grish.
			if (player.getQuestRepository().getQuest(Quests.ZOGRE_FLESH_EATERS).getStage(player) < 8) {
				player.getPacketDispatch().sendMessage("You must have started Zogre Flesh Eaters and asked Grish to make this.");
				return false;
			}
			if (!player.getInventory().contains(2859, 1)) {
				player.getPacketDispatch().sendMessage("You need to have Wolf Bones in order to make this.");
				return false;
			}
		}
		return true;
	}

	@Override
	public void animate() {
		player.animate(ANIMATION);
	}

	@Override
	public boolean reward() {
		if(bankZone.insideBorder(player) && fletch == Fletching.FletchingItems.MAGIC_SHORTBOW) {
			player.getAchievementDiaryManager().finishTask(player, DiaryType.SEERS_VILLAGE, 2, 2);
		}
		if (getDelay() == 1) {
			super.setDelay(4);
			return false;
		}
		if (player.getInventory().remove(node)) {
			final Item item = new Item(fletch.id,fletch.amount);
			if ( fletch == Fletching.FletchingItems.OGRE_ARROW_SHAFT ) {
				// The amount of shafts given is random; between two and six will be made.
				finalAmount = RandomFunction.random(2,6);
				item.setAmount(finalAmount);
			}
			if ( fletch == Fletching.FletchingItems.OGRE_COMP_BOW ) {
				if (!player.getInventory().contains(2859, 1)) {
					return false;
				} else {
					player.getInventory().remove(new Item(2859));
				}
			}
			player.getInventory().add(item);
			player.getSkills().addExperience(Skills.FLETCHING, fletch.experience, true);
			String message = getMessage();
			player.getPacketDispatch().sendMessage(message);

			if (fletch.id == Fletching.FletchingItems.MAGIC_SHORTBOW.id
					&& (new ZoneBorders(2721, 3489, 2724, 3493, 0).insideBorder(player)
					|| new ZoneBorders(2727, 3487, 2730, 3490, 0).insideBorder(player))
					&& !player.getAchievementDiaryManager().hasCompletedTask(DiaryType.SEERS_VILLAGE, 2, 2)) {
				player.setAttribute("/save:diary:seers:fletch-magic-short-bow", true);
			}
		} else {
			return true;
		}
		amount--;
		return amount == 0;
	}

	/**
	 * Method used to get the message of the fletch.
	 * @return the message.
	 */
	public String getMessage() {
		switch (fletch) {
		case ARROW_SHAFT:
			return "You carefully cut the wood into 15 arrow shafts.";
		case OGRE_ARROW_SHAFT:
			return "You carefully cut the wood into " + finalAmount + " arrow shafts.";
		default:
			return "You carefully cut the wood into " + (StringUtils.isPlusN(fletch.getItem().getName()) ? "an" : "a") + " " + fletch.getItem().getName().replace("(u)", "").trim() + ".";
		}
	}
}
