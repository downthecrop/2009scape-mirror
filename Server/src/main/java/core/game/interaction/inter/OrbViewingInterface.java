package core.game.interaction.inter;

import rs09.ServerConstants;
import core.cache.def.impl.SceneryDefinition;
import core.game.component.CloseEvent;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;

/**
 * Handles an orb viewing interface.
 * @author 'Vexia
 * @author Emperor
 * @version 1.0
 */
@Initializable
public final class OrbViewingInterface extends ComponentPlugin {

	/*
	 * Order: South-west, South-east, North-west, North-east, Centre
	 */

	/**
	 * The fight pit locations.
	 */
	private static final Location[] FIGHT_PITS = { Location.create(2388, 5138, 0), Location.create(2411, 5137, 0), Location.create(2409, 5158, 0), Location.create(2384, 5157, 0), Location.create(2398, 5150, 0) };

	/**
	 * The clan war locations.
	 */
	private static final Location[] CLAN_WARS = { Location.create(3277, 3725, 0), Location.create(3315, 3725, 0), Location.create(3316, 3829, 0), Location.create(3277, 3827, 0), Location.create(3296, 3776, 0) };

	/**
	 * The bounty hunter viewing orb locations.
	 */
	private static final Location[][] BOUNTY_HUNTER = { { // Low level crater
			Location.create(2752, 5695, 0), Location.create(2816, 5695, 0), Location.create(2783, 5783, 0), Location.create(2826, 5785, 0), Location.create(2784, 5727, 0) }, { // Mid
			// level
			// crater
					Location.create(3008, 5695, 0), Location.create(3072, 5695, 0), Location.create(3039, 5783, 0), Location.create(3082, 5785, 0), Location.create(3040, 5727, 0) }, { // High
			// level
			// crater
					Location.create(3264, 5695, 0), Location.create(3328, 5695, 0), Location.create(3295, 5783, 0), Location.create(3338, 5785, 0), Location.create(3296, 5727, 0) } };

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(374, this);
		ComponentDefinition.put(649, this);
		ClassScanner.definePlugin(new OptionHandler() {
			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				SceneryDefinition.forId(9391).getHandlers().put("option:look-into", this);
				SceneryDefinition.forId(28194).getHandlers().put("option:look-into", this);
				SceneryDefinition.forId(28209).getHandlers().put("option:view", this);
				SceneryDefinition.forId(28210).getHandlers().put("option:view", this);
				SceneryDefinition.forId(28211).getHandlers().put("option:view", this);
				return this;
			}

			@Override
			public boolean handle(Player player, Node node, String option) {
				int interfaceId = 649;
				switch (node.getId()) {
				case 9391:
					interfaceId = 374;
					player.setAttribute("viewing_orb", FIGHT_PITS);
					break;
				case 28194:
					player.setAttribute("viewing_orb", CLAN_WARS);
					break;
				case 28209:
				case 28210:
				case 28211:
					player.setAttribute("viewing_orb", BOUNTY_HUNTER[node.getId() - 28209]);
					break;
				}
				viewOrb(player, interfaceId);
				return true;
			}

			@Override
			public Location getDestination(Node node, Node n) {
				switch (n.getId()) {
				case 28194:
					return n.getLocation().transform(1, 0, 0);
				}
				return null;
			}
		});
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		Location[] locations = player.getAttribute("viewing_orb");
		if (locations == null) {
			return false;
		}
		if (button != 5) {
			move(player, locations[15 - button]);
		} else {
			stopViewing(player, true);
		}
		return true;
	}

	/**
	 * Method used to move to a viewing location.
	 * @param location the location.
	 */
	private void move(final Player player, final Location location) {
		player.getLocks().lockMovement(100000000);
		player.getProperties().setTeleportLocation(location);
		if (player.getAppearance().getNpcId() == -1) {
			player.getAppearance().transformNPC(-2);
			player.getAppearance().sync();
		}
	}

	/**
	 * Views an orb.
	 * @param player the player.
	 * @param interfaceId the interface id.
	 */
	private void viewOrb(final Player player, final int interfaceId) {
		final Component component = new Component(interfaceId).setCloseEvent(new ViewCloseEvent());
		player.setAttribute("view-location", player.getLocation());
		player.getInterfaceManager().openSingleTab(component);
		player.getPulseManager().run(new Pulse(1, player) {
			@Override
			public boolean pulse() {
				return false;
			}

			@Override
			public void stop() {
				super.stop();
				stopViewing(player, true);
			}
		});
	}

	/**
	 * Ends the viewing session.
	 * @param player the player.
	 * @param close if we should close the interface.
	 */
	private static void stopViewing(final Player player, boolean close) {
		if (close) {
			player.getInterfaceManager().closeSingleTab();
		}
		player.removeAttribute("viewing_orb");
		player.unlock();
		player.getAppearance().transformNPC(-1);
		player.getAppearance().sync();
		player.getPulseManager().clear();
		player.getProperties().setTeleportLocation(player.getAttribute("view-location", ServerConstants.HOME_LOCATION));
	}

	/**
	 * Handles the close event of a viewing interface.
	 * @author 'Vexia
	 */
	public static final class ViewCloseEvent implements CloseEvent {

		@Override
		public boolean close(Player player, Component c) {
			stopViewing(player, false);
			return true;
		}

	}
}
