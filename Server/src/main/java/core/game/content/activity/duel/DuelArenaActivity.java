package core.game.content.activity.duel;

import java.util.ArrayList;
import java.util.List;

import core.game.component.Component;
import core.game.content.activity.ActivityPlugin;
import core.game.interaction.Option;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.impl.PulseManager;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.request.RequestType;
import core.game.world.map.Location;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.plugin.Initializable;
import rs09.plugin.ClassScanner;
import core.tools.RandomFunction;

/**
 * Handles the Duel Arena activity.
 * @author Emperor
 * @author Vexia
 * 
 */
@Initializable
public final class DuelArenaActivity extends ActivityPlugin {

	/**
	 * The friendly duel request.
	 */
	public static final RequestType FRIEND_REQUEST = new RequestType("Sending duel offer...", ":duelfriend:", new DuelReqModule(false));

	/**
	 * The staked duel request.
	 */
	public static final RequestType STAKE_REQUEST = new RequestType("Sending duel offer...", ":duelstake:", new DuelReqModule(true));

	/**
	 * The dueling areas.
	 */
	public static final DuelArea[] DUEL_AREAS = new DuelArea[] { new DuelArea(0, new ZoneBorders(3332, 3244, 3357, 3258), false, Location.create(3345, 3251, 0)), new DuelArea(1, new ZoneBorders(3364, 3244, 3388, 3259), true, Location.create(3378, 3251, 0)), new DuelArea(2, new ZoneBorders(3333, 3224, 3357, 3239), true, Location.create(3345, 3231, 0)), new DuelArea(3, new ZoneBorders(3364, 3225, 3388, 3240), false, Location.create(3376, 3231, 0)), new DuelArea(4, new ZoneBorders(3333, 3205, 3357, 3220), false, Location.create(3346, 3212, 0)), new DuelArea(5, new ZoneBorders(3364, 3206, 3388, 3221), true, Location.create(3377, 3213, 0)) };

	/**
	 * The challenge option.
	 */
	public static final Option CHALLENGE_OPTION = new Option("Challenge", 0);

	/**
	 * The select duel type component.
	 */
	public static final Component DUEL_TYPE_SELECT = new Component(640);

	/**
	 * The overlay.
	 */
	private static final Component OVERLAY = new Component(638);

	/**
	 * The scoreboard.
	 */
	private static final String[] SCOREBOARD = new String[50];

	/**
	 * Constructs a new {@code DuelArenaActivity} {@code Object}.
	 */
	public DuelArenaActivity() {
		super("Duel arena", false, false, true);
	}

	@Override
	public boolean enter(Entity e) {
		if (e instanceof Player) {
			Player player = (Player) e;
			player.getInterfaceManager().openOverlay(OVERLAY);
			player.getInteraction().set(CHALLENGE_OPTION);
			player.getConfigManager().set(286, 0);
		}
		return super.enter(e);
	}

	@Override
	public boolean leave(Entity e, boolean logout) {
		if (e instanceof Player) {
			Player player = (Player) e;
			player.getInterfaceManager().closeOverlay();
			player.getInteraction().remove(CHALLENGE_OPTION);
		}
		return super.leave(e, logout);
	}

	@Override
	public ActivityPlugin newInstance(Player p) throws Throwable {
		return this;
	}

	@Override
	public Location getSpawnLocation() {
		return null;
	}

	@Override
	public boolean continueAttack(Entity e, Node target, CombatStyle style, boolean message) {
		if (e.isPlayer() && e.asPlayer().getZoneMonitor().getZones().size() > 1) {
			return super.continueAttack(e, target, style, message);
		}
		return false;
	}

	@Override
	public boolean interact(Entity e, Node target, Option o) {
		if (e.isPlayer()) {
			switch (target.getId()) {
			case 3192:
				openScoreboard(e.asPlayer());
				return true;
			}
		}
		return super.interact(e, target, o);
	}

	@Override
	public boolean death(Entity e, Entity killer) {
		if (e.isPlayer() && e.asPlayer().getZoneMonitor().getZones().size() > 1) {
			return true;
		}
		if (e instanceof Player && killer instanceof Player) {
			e.getSkills().heal(100);
			PulseManager.cancelDeathTask(e);
			return true;
		}
		return true;
	}

	@Override
	public void configure() {
		for (DuelArea area : DUEL_AREAS) {
			ZoneBuilder.configure(area);
		}
		parseScoreboard();
		register(new ZoneBorders(3325, 3201, 3396, 3280));
		ClassScanner.definePlugin(new DuelArea.ForfeitTrapdoorPlugin());
		ClassScanner.definePlugins(new DuelSession(null, null, false), new DuelComponentPlugin(), new ChallengeOptionPlugin());
	}

	/**
	 * Opens the scoreboard.
	 * @param player the player.
	 */
	public static void openScoreboard(Player player) {
		player.lock(2);
		parseScoreboard();
		player.getInterfaceManager().open(new Component(632));
		int index = 0;
		for (int i = 16; i < 65; i++) {
			player.getPacketDispatch().sendString(SCOREBOARD[index] == null ? "" : SCOREBOARD[index], 632, i - 1);
			index++;
		}
	}

	/**
	 * Parses the scoreboard data from the database.
	 */
	public static void parseScoreboard() {
		/*Connection connection = SQLManager.getConnection();
		if (connection == null) {
			return;
		}
		java.sql.PreparedStatement statement;
		try {
			statement = connection.prepareStatement("SELECT * FROM duel_scoreboard WHERE id <= 50");
			ResultSet set = statement.executeQuery();
			while (set.next()) {
				SCOREBOARD[set.getInt(1) - 1] = set.getString(2);
			}
			statement = connection.prepareStatement("DELETE FROM duel_scoreboard WHERE id > 50");
			statement.executeUpdate();
		} catch (SQLException e) {
			SQLManager.close(connection);
			e.printStackTrace();
		}
		SQLManager.close(connection);*/
	}

	/**
	 * Inserts an entry into the scoreboard.
	 * @param winner the winner.
	 * @param looser the looser.
	 */
	public static void insertEntry(Player winner, Player looser) {
		/*String entry = winner.getUsername() + " (" + winner.getProperties().getCurrentCombatLevel() + ") beat " + looser.getUsername() + " (" + looser.getProperties().getCurrentCombatLevel() + ")";
		Connection connection = SQLManager.getConnection();
		if (connection == null) {
			return;
		}
		java.sql.PreparedStatement statement;
		try {
			statement = connection.prepareStatement("UPDATE duel_scoreboard SET id = ID + 1");
			statement.executeUpdate();
			statement = connection.prepareStatement("INSERT INTO duel_scoreboard (id,entry) VALUES(?,?)");
			statement.setInt(1, 1);
			statement.setString(2, entry);
			statement.executeUpdate();
		} catch (SQLException e) {
			SQLManager.close(connection);
			e.printStackTrace();
		}
		SQLManager.close(connection);*/
	}

	/**
	 * Gets the dueling area.
	 * @param obstacles if we're using obstacles.
	 * @return {@code DuelArea} the area.
	 */
	public static DuelArea getDuelArea(boolean obstacles) {
		List<DuelArea> options = new ArrayList<>(20);
		for (DuelArea area : DUEL_AREAS) {
			if (!obstacles && area.isObstacles() || obstacles && !area.isObstacles()) {
				continue;
			}
			options.add(area);
		}
		return options.get(RandomFunction.random(options.size()));
	}

}
