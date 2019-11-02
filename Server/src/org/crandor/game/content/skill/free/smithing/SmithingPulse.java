package org.crandor.game.content.skill.free.smithing;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.content.global.tutorial.TutorialSession;
import org.crandor.game.content.global.tutorial.TutorialStage;
import org.crandor.game.content.skill.SkillPulse;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.info.portal.Perks;
import org.crandor.game.node.entity.player.link.diary.DiaryType;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.tools.StringUtils;

/**
 * Represents the pulse used to smith a bar.
 * @author 'Vexia
 */
public class SmithingPulse extends SkillPulse<Item> {

	/**
	 * Represents the animation to use.
	 */
	private static final Animation ANIMATION = new Animation(898);

	/**
	 * Represents the bar being made.
	 */
	private final Bars bar;

	/**
	 * Represents the amount to make.
	 */
	private int amount;

	/**
	 * Constructs a new {@code SmithingPulse} {@code Object}.
	 * @param player the player.
	 * @param item the item.
	 */
	public SmithingPulse(Player player, Item item, Bars bar, int ammount) {
		super(player, item);
		this.bar = bar;
		this.amount = ammount;
	}

	@Override
	public boolean checkRequirements() {
		if (!player.getInventory().contains(bar.getBarType().getBarType(), bar.getSmithingType().getRequired() * amount)) {
			amount = player.getInventory().getAmount(new Item(bar.getBarType().getBarType()));
		}
		player.getInterfaceManager().close();
		if (player.getSkills().getLevel(Skills.SMITHING) < bar.getLevel()) {
			player.getDialogueInterpreter().sendDialogue("You need a Smithing level of " + bar.getLevel() + " to make a " + ItemDefinition.forId(bar.getProduct()).getName() + ".");
			return false;
		}
		if (!player.getInventory().contains(bar.getBarType().getBarType(), bar.getSmithingType().getRequired())) {
			player.getDialogueInterpreter().sendDialogue("You don't have enough " + ItemDefinition.forId(bar.getBarType().getBarType()).getName().toLowerCase() + "s to make a " + bar.getSmithingType().name().replace("TYPE_", "").replace("_", " ").toLowerCase() + ".");
			return false;
		}
		if (!player.getInventory().contains(2347, 1)) {
			player.getDialogueInterpreter().sendDialogue("You need a hammer to work the metal with.");
			return false;
		}
		if (TutorialSession.getExtension(player).getStage() <= TutorialSession.MAX_STAGE && node.getId() != Bars.BRONZE_DAGGER.getProduct()) {
			return false;
		}
		return true;
	}

	@Override
	public void animate() {
		player.animate(ANIMATION);
	}

	@Override
	public boolean reward() {
		if (getDelay() == 1) {
			setDelay(4);
			return false;
		}
		player.getInventory().remove(new Item(bar.getBarType().getBarType(), bar.getSmithingType().getRequired()));
		Perks.addDouble(player, (new Item(node.getId(), bar.getSmithingType().getProductAmount())));
		player.getSkills().addExperience(Skills.SMITHING, bar.getBarType().getExperience() * bar.getSmithingType().getRequired(), true);
		String message = StringUtils.isPlusN(ItemDefinition.forId(bar.getProduct()).getName().toLowerCase()) == true ? "an" : "a";
		player.getPacketDispatch().sendMessage("You hammer the " + bar.getBarType().getBarName().toLowerCase().replace("smithing", "") + "and make " + message + " " + ItemDefinition.forId(bar.getProduct()).getName().toLowerCase() + ".");
		if (TutorialSession.getExtension(player).getStage() == 42) {
			TutorialStage.load(player, 43, false);
		}
		if (bar == Bars.MITHRIL_PLATEBODY && player.getViewport().getRegion().getId() == 12439 && !player.getAchievementDiaryManager().getDiary(DiaryType.LUMBRIDGE).isComplete(2, 0)) {
			player.getAchievementDiaryManager().updateTask(player, DiaryType.LUMBRIDGE, 2, 0, true);
		}
		amount--;
		return amount < 1;
	}

	@Override
	public void message(int type) {

	}

}
