package core.game.node.entity.skill.herblore;

import core.plugin.Initializable;
import rs09.game.content.dialogue.SkillDialogueHandler;
import rs09.game.content.dialogue.SkillDialogueHandler.SkillDialogue;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.plugin.Plugin;

/**
 * Represents the plugin used to handle the creation of creation an unf-potion.
 * @author 'Vexia
 */
@Initializable
public final class UnfinishedPotionPlugin extends UseWithHandler {

	/**
	 * Constructs a new {@code UnfinishedPotionPlugin} {@code Object}.
	 */
	public UnfinishedPotionPlugin() {
		super(227, 5935);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (UnfinishedPotion potion : UnfinishedPotion.values()) {
			addHandler(potion.getIngredient().getId(), ITEM_TYPE, this);
		}
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final UnfinishedPotion unf = UnfinishedPotion.forItem(event.getUsedItem(), event.getBaseItem());
		if (unf == null) {
			return false;
		}
		final GenericPotion potion = GenericPotion.transform(unf);
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

}
