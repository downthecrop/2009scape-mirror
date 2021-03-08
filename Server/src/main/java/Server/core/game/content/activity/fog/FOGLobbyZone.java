package core.game.content.activity.fog;

import core.game.component.Component;
import core.game.interaction.Option;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBuilder;
import core.plugin.Plugin;

/**
 * Represents a zone where players can prepare for a game.
 * @author Vexia
 */
public class FOGLobbyZone extends MapZone implements Plugin<Object> {

	/**
	 * Constructs a new {@code FOGHallZone} {@code Object}
	 */
	public FOGLobbyZone() {
		super("Fog Lobby", true);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ZoneBuilder.configure(this);
		return this;
	}

	@Override
	public boolean enter(Entity e) {
		if (e.isPlayer()) {
			sendInterface(e.asPlayer());
		}
		return super.enter(e);
	}

	@Override
	public boolean interact(Entity e, Node target, Option option) {
		if (!e.isPlayer()) {
			return super.interact(e, target, option);
		}
		Player player = e.asPlayer();
		switch (target.getId()) {
		case 30203:
			player.teleport(Location.create(3242, 3574, 0));
			return true;
		}
		return super.interact(e, target, option);
	}

	/**
	 * Sends the fist of guthix lobby interface.
	 * @param player the player.
	 */
	private void sendInterface(Player player) {
		player.getInterfaceManager().openOverlay(new Component(FOGActivityPlugin.WAITING_INTERFACE));
		player.getPacketDispatch().sendInterfaceConfig(FOGActivityPlugin.WAITING_INTERFACE, 17, true);
		player.getPacketDispatch().sendInterfaceConfig(FOGActivityPlugin.WAITING_INTERFACE, 26, true);
		player.getPacketDispatch().sendString("Rating: " + player.getSavedData().getActivityData().getFogRating(), FOGActivityPlugin.WAITING_INTERFACE, 7);
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public void configure() {
		super.registerRegion(6743);
	}

}
