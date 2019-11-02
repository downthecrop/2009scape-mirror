package plugin.skill.herblore;

import org.crandor.game.content.dialogue.SkillDialogueHandler;
import org.crandor.game.content.dialogue.SkillDialogueHandler.SkillDialogue;
import org.crandor.game.content.skill.member.herblore.FinishedPotion;
import org.crandor.game.content.skill.member.herblore.GenericPotion;
import org.crandor.game.content.skill.member.herblore.HerblorePulse;
import org.crandor.game.content.skill.member.herblore.UnfinishedPotion;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the finished potion plugin creating.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class FinishedPotionPlugin extends UseWithHandler {

	/**
	 * Constructs a new {@code FinishedPotionPlugin} {@code Object}
	 */
	public FinishedPotionPlugin() {
		super(getUnfinishedItems());
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (FinishedPotion potion : FinishedPotion.values()) {
			addHandler(potion.getIngredient().getId(), ITEM_TYPE, this);
		}
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final FinishedPotion finished = FinishedPotion.getPotion(event.getUsedItem().getName().contains("(unf)") ? event.getUsedItem() : event.getBaseItem(), event.getUsedItem().getName().contains("(unf)") ? event.getBaseItem() : event.getUsedItem());
		if (finished == null) {
			return false;
		}
		final GenericPotion potion = GenericPotion.transform(finished);
		final Player player = event.getPlayer();
		SkillDialogueHandler handler = new SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, potion.getProduct()) {
			@Override
			public void create(final int amount, int index) {
				player.getPulseManager().run(new HerblorePulse(player, potion.getBase(), amount, potion));
			}

			@Override
			public int getAll(int index) {
				return player.getInventory().getAmount(potion.getBase());
			}

		};
		if (player.getInventory().getAmount(potion.getBase()) == 1) {
			handler.create(0, 1);
		} else {
			handler.open();
		}
		return true;
	}

	/**
	 * Method used to gather the unfinished item bases.
	 * @return the ids.
	 */
	public static int[] getUnfinishedItems() {
		int[] ids = new int[UnfinishedPotion.values().length];
		int counter = 0;
		for (UnfinishedPotion potion : UnfinishedPotion.values()) {
			ids[counter] = potion.getPotion().getId();
			counter++;
		}
		return ids;
	}
}
