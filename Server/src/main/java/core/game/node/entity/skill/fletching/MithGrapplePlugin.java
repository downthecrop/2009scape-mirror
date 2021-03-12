package core.game.node.entity.skill.fletching;

import core.game.node.entity.skill.Skills;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the plugin used to make a mithril grapple.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class MithGrapplePlugin extends UseWithHandler {

	/**
	 * Represents the rope grapple.
	 */
	private static final Item ROPE_GRAPPLE = new Item(9419);

	/**
	 * Represents the mith grapple.
	 */
	private static final Item MITH_GRAPPLE = new Item(9418);

	/**
	 * Represents the rope item.
	 */
	private static final Item ROPE = new Item(954);

	/**
	 * Constructs a new {@code MithGrapplePlugin} {@code Object}.
	 */
	public MithGrapplePlugin() {
		super(9416, 954);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(9142, ITEM_TYPE, this);
		addHandler(9418, ITEM_TYPE, this);
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		final Item first = event.getUsedItem();
		final Item second = event.getBaseItem();
		if (first.getId() == 9418 && second.getId() != 954) {
			return false;
		} else if (first.getId() == 9418 && second.getId() == 954) {
			if (player.getInventory().remove(ROPE) && player.getInventory().remove(MITH_GRAPPLE)) {
				player.getInventory().add(ROPE_GRAPPLE);
			}
			return true;
		}
		if (player.getSkills().getLevel(Skills.FLETCHING) < 59) {
			player.getDialogueInterpreter().sendDialogue("You need a fletching level of 59 in order to do that.");
			return true;
		}
		player.getDialogueInterpreter().open(903213, first, second);
		return true;
	}

}
