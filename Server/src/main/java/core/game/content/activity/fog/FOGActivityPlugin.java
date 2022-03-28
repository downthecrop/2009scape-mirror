package core.game.content.activity.fog;

import core.cache.def.impl.SceneryDefinition;
import core.game.content.activity.ActivityPlugin;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.game.world.map.zone.ZoneBorders;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;

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
		ClassScanner.definePlugin(new FOGLobbyZone());
		ClassScanner.definePlugin(new FOGWaitingZone());
		ClassScanner.definePlugin(new OptionHandler() {
			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				SceneryDefinition.forId(30204).getHandlers().put("option:enter", this);
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
