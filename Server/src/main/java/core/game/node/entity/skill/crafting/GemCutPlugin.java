package core.game.node.entity.skill.crafting;

import rs09.game.content.dialogue.SkillDialogueHandler;
import rs09.game.content.dialogue.SkillDialogueHandler.SkillDialogue;
import core.game.node.entity.skill.crafting.gem.GemCutPulse;
import core.game.node.entity.skill.crafting.gem.Gems;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to cut a gem.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class GemCutPlugin extends UseWithHandler {

	/**
	 * Constructs a new {@code GemCutPlugin} {@Code Object}
	 */
	public GemCutPlugin() {
		super(1623, 1621, 1619, 1617, 1631, 6571, 1625, 1627, 1629, 6571);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(1755, ITEM_TYPE, this);
		return null;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		final Gems gem = Gems.forId(event.getUsedItem().getId() == 1755 ? event.getBaseItem() : event.getUsedItem());
		SkillDialogueHandler handler = new SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, gem.getGem()) {
			@Override
			public void create(final int amount, int index) {
				player.getPulseManager().run(new GemCutPulse(player, gem.getUncut(), amount, gem));
			}

			@Override
			public int getAll(int index) {
				return player.getInventory().getAmount(gem.getUncut());
			}

		};
		if (player.getInventory().getAmount(gem.getUncut()) == 1) {
			handler.create(0, 1);
		} else {
			handler.open();
		}
		return true;
	}

}
