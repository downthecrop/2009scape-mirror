package core.game.interaction.item;

import core.cache.def.impl.ItemDefinition;
import core.game.component.CloseEvent;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import rs09.game.world.GameWorld;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;
import core.tools.RandomFunction;

/**
 * Handles a morph item.
 * @author Vexia
 */
@Initializable
public class MorphItemPlugin implements Plugin<Object> {

	/**
	 * The easter egg ids.
	 */
	protected static final int[] EASTER_EGG_IDS = new int[] { 3689, 3690, 3691, 3692, 3693, 3694 };

	/**
	 * The morph component.
	 */
	private static final Component COMPONENT = new Component(375).setCloseEvent(new CloseEvent() {

		@Override
		public boolean close(Player player, Component c) {
			unmorph(player);
			return true;
		}

	});

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(7927).getHandlers().put("equipment", this);
		ItemDefinition.forId(6583).getHandlers().put("equipment", this);
		ClassScanner.definePlugin(new MorphInterfacePlugin());
		return this;
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		final Player player = (Player) args[0];
		final Item item = (Item) args[1];
		switch (identifier) {
		case "equip":
			morph(player, item);
			return false;
		}
		return true;
	}

	/**
	 * Morphs the player.
	 * @param player the player.
	 * @param item the item.
	 */
	private void morph(Player player, Item item) {
		int morphId = item.getId() == 6583 ? 2626 : EASTER_EGG_IDS[RandomFunction.random(EASTER_EGG_IDS.length)];
		player.getInterfaceManager().close();
		player.getAppearance().transformNPC(morphId);
		player.getInterfaceManager().removeTabs(0, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14);
		player.getLocks().lockMovement(GameWorld.getTicks() + 900000000);
		player.getLocks().lockInteractions(GameWorld.getTicks() + 90000000);
		player.getLocks().lockTeleport(GameWorld.getTicks() + 900000000);
		player.getInterfaceManager().openSingleTab(COMPONENT);
		player.getAppearance().sync();
		player.getWalkingQueue().reset();
	}

	/**
	 * Unmorphs the player.
	 * @param player the player.
	 */
	private static void unmorph(Player player) {
		player.getAppearance().transformNPC(-1);
		player.unlock();
		player.getInterfaceManager().restoreTabs();
	}

	/**
	 * Handles the morph interface plugin.
	 * @author Vexia
	 */
	public class MorphInterfacePlugin extends ComponentPlugin {

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			ComponentDefinition.forId(375).setPlugin(this);
			return this;
		}

		@Override
		public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
			player.getInterfaceManager().closeSingleTab();
			return true;
		}

	}

}
