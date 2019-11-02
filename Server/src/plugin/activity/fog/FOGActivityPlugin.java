package plugin.activity.fog;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.activity.ActivityPlugin;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.zone.ZoneBorders;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.PluginManager;

/**
 * Represents the fist of guthix activity.
 * @author Vexia
 */
public class FOGActivityPlugin extends ActivityPlugin {

	/**
	 * The maximum amount of players in a game.
	 */
	public static final int MAX_PLAYERS = 250;

	/**
	 * The waiting interface id.
	 */
	public static final int WAITING_INTERFACE = 731;

	/**
	 * The current fist of guthix round.
	 */
	private int round;

	/**
	 * Constructs a new {@code FOGActivityPlugin} {@code Object}
	 */
	public FOGActivityPlugin() {
		super("Fist of Guthix", false, true, true);
	}

	@Override
	public ActivityPlugin newInstance(Player p) throws Throwable {
		return new FOGActivityPlugin();
	}

	@Override
	public Location getSpawnLocation() {
		return null;
	}

	@Override
	public void configure() {
		PluginManager.definePlugin(new FOGLobbyZone());
		PluginManager.definePlugin(new FOGWaitingZone());
		PluginManager.definePlugin(new OptionHandler() {
			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				ObjectDefinition.forId(30204).getConfigurations().put("option:enter", this);
				return this;
			}

			@Override
			public boolean handle(Player player, Node node, String option) {
				switch (node.getId()) {
				case 30204:
					player.teleport(Location.create(1675, 5599, 0));
					return true;
				}
				return true;
			}
		});
		register(new ZoneBorders(1625, 5638, 1715, 5747));
	}

	/**
	 * Gets the round.
	 * @return the round
	 */
	public int getRound() {
		return round;
	}

	/**
	 * Sets the round.
	 * @param round the round to set.
	 */
	public void setRound(int round) {
		this.round = round;
	}

}
