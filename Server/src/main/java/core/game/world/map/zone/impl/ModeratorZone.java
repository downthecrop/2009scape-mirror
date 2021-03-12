package core.game.world.map.zone.impl;

import rs09.ServerConstants;
import core.game.interaction.Option;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.Rights;
import core.game.node.entity.player.link.TeleportManager.TeleportType;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;

/**
 * Represents the moderator zone.
 * @author Vexia
 * 
 */
public class ModeratorZone extends MapZone {

	/**
	 * Represents if the moderator zone is open.
	 */
	public static boolean open = true;

	/**
	 * Represents the center of the zone.
	 */
	public static final Location center = Location.create(2846, 5213, 0);

	/**
	 * Constructs a new {@code ModeratorZone} {@code Object}.
	 */
	public ModeratorZone() {
		super("Moderator Zone", true);
	}

	@Override
	public boolean enter(final Entity entity) {
		if (!(entity instanceof Player)) {
			return true;
		}
		final Player player = ((Player) entity);
		if ((!open && player.getDetails().getRights() != Rights.ADMINISTRATOR)) {
			home(player);
			return false;
		}
		if (player.getDetails().getRights() == Rights.PLAYER_MODERATOR) {
			// player.getInterfaceManager().removeTabs(0, 1, 2, 3, 4, 5, 6, 12);
		} else {
			player.getPacketDispatch().sendMessage(getToggleMessage());
		}
		return true;
	}

	@Override
	public boolean leave(final Entity entity, final boolean logout) {
		if (!(entity instanceof Player)) {
			return true;
		}
		final Player player = ((Player) entity);
		player.getInterfaceManager().restoreTabs();
		return true;
	}

	@Override
	public boolean interact(Entity entity, Node target, Option option) {
		if (entity instanceof Player) {
		}
		return super.interact(entity, target, option);
	}

	@Override
	public void configure() {
		register(new ZoneBorders(2840, 5204, 2853, 5224));
	}

	/**
	 * Method used to toggle the moderator zone.
	 * @param player the player.
	 * @param on the toggle switch.
	 */
	public static final void toggle(final Player player, final boolean on) {
		open = on ? true : false;
		player.getPacketDispatch().sendMessage(getToggleMessage());
		if (!open) {
			for (Player p : RegionManager.getLocalPlayers(center)) {
				if (p == null || p.getDetails().getRights() == Rights.ADMINISTRATOR) {
					continue;
				}
				home(p);
			}
		}
	}

	/**
	 * Method used to get the togglemessage.
	 * @return the message.
	 */
	public static final String getToggleMessage() {
		return "The moderator room is currently " + (open ? "available" : "not available") + " to player moderators.";
	}

	/**
	 * Method used to send a player home.
	 * @param player the player.
	 */
	public static final void home(final Player player) {
		player.getTeleporter().send(ServerConstants.HOME_LOCATION, TeleportType.NORMAL);
	}

	/**
	 * Method used to teleport a player into the zone.
	 * @param player the player.
	 */
	public static final void teleport(final Player player) {
		player.getTeleporter().send(center, TeleportType.NORMAL);
	}

	/**
	 * Method used to check if the moderator zone is open.
	 * @return return <code>True</code> if so.
	 */
	public static boolean isOpen() {
		if (!open) {
			return false;
		}
		return true;
	}

}
