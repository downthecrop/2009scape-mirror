package core.game.node.entity.skill.crafting;

import core.plugin.Initializable;
import rs09.game.content.dialogue.SkillDialogueHandler;
import rs09.game.content.dialogue.SkillDialogueHandler.SkillDialogue;
import core.game.node.entity.skill.crafting.armour.SnakeSkin;
import core.game.node.entity.skill.crafting.armour.SnakeSkinPulse;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Plugin;

/**
 * Handles snake skin plugin.
 * @author Vexia
 */
@Initializable
public class SnakeSkinPlugin extends UseWithHandler {

	/**
	 * Constructs a new {@Code SnakeSkinPlugin} {@Code Object}
	 */
	public SnakeSkinPlugin() {
		super(1733);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(6289, ITEM_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(final NodeUsageEvent event) {
		final Player player = event.getPlayer();
		new SkillDialogueHandler(player, SkillDialogue.FIVE_OPTION, (Object[]) getSkins()) {

			@Override
			public void create(final int amount, int index) {
				player.getPulseManager().run(new SnakeSkinPulse(player, event.getUsedItem(), amount, SnakeSkin.values()[index]));
			}

			@Override
			public int getAll(int index) {
				return player.getInventory().getAmount(new Item(6289));
			}

			@Override
			public String getName(Item item) {
				return item.getId() == 6328 ? "Boots" : item.getId() == 6330 ? "Vambs" : item.getId() == 6326 ? "Bandana" : item.getId() == 6324 ? "Chaps" : "Body";
			}

		}.open();
		return true;
	}

	/**
	 * Gets the skins.
	 * @return the item.
	 */
	public Item[] getSkins() {
		Item[] items = new Item[SnakeSkin.values().length];
		for (int i = 0; i < items.length; i++) {
			items[i] = SnakeSkin.values()[i].getProduct();
		}
		return items;
	}

}
