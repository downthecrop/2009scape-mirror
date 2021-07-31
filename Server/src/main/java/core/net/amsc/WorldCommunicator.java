package core.net.amsc;

import core.game.node.entity.player.info.Rights;
import core.game.node.entity.player.info.login.Response;
import core.game.system.task.TaskExecutor;
import core.net.EventProducer;
import core.net.IoSession;
import core.net.NioReactor;
import core.net.producer.MSHSEventProducer;
import kotlin.Unit;
import rs09.game.node.entity.player.info.login.LoginParser;
import rs09.game.system.SystemLogger;
import rs09.game.world.GameWorld;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles world communication.
 * @author Emperor
 */
public final class WorldCommunicator {

	/**
	 * The handshake events producer.
	 */
	private static final EventProducer HANDSHAKE_PRODUCER = new MSHSEventProducer();

	/**
	 * The current state.
	 */
	private static ManagementServerState state = ManagementServerState.CONNECTING;

	/**
	 * The I/O session.
	 */
	private static IoSession session;

	/**
	 * The world information.
	 */
	private static final WorldStatistics[] WORLDS = new WorldStatistics[10];

	/**
	 * The current login attempts.
	 */
	private static final Map<String, LoginParser> loginAttempts = new ConcurrentHashMap<>();

	/**
	 * The NIO reactor.
	 */
	private static NioReactor reactor;

	/**
	 * Registers a new world.
	 * @param session The session.
	 */
	public static void register(IoSession session) {
		WorldCommunicator.session = session;
		session.setProducer(HANDSHAKE_PRODUCER);
		session.write(true);
		WORLDS[GameWorld.getSettings().getWorldId() - 1] = new WorldStatistics(GameWorld.getSettings().getWorldId());
		session.setObject(WORLDS[GameWorld.getSettings().getWorldId() - 1]);
	}

	/**
	 * Registers a login attempt.
	 * @param parser The login attempt.
	 */
	public static void register(final LoginParser parser) {
		LoginParser p = loginAttempts.get(parser.getDetails().getUsername());
		if (p != null && GameWorld.getTicks() - p.getTimeStamp() < 50 && p.getDetails().getRights() == Rights.REGULAR_PLAYER) {
			parser.getDetails().getSession().write(Response.ALREADY_ONLINE, true);
			return;
		}
		loginAttempts.put(parser.getDetails().getUsername(), parser);
		TaskExecutor.executeSQL(() -> {
			if (!parser.getDetails().parse()) {
				parser.getDetails().getSession().write(Response.INVALID_LOGIN_SERVER, true);
				return Unit.INSTANCE;
			}
			MSPacketRepository.sendPlayerRegistry(parser);
			return Unit.INSTANCE;
		});
	}

	/**
	 * Attempts to connect to the management server.
	 */
	public static void connect() {
		try {
			SystemLogger.logInfo("Attempting to connect to management server...");
			setState(ManagementServerState.CONNECTING);
			/*if (isLocallyHosted()) {
				SystemLogger.log("Management server is hosted on local machine!");
				reactor = NioReactor.connect(GameWorld.getSettings().getMsAddress(), 5555);
			} else {*/
				reactor = NioReactor.connect(GameWorld.getSettings().getMsAddress(), 5555);
			//}
			reactor.start();
		} catch (Throwable e) {
			SystemLogger.logErr("Unable to connect to management server");
			e.printStackTrace();
			terminate();
		}
	}

	/**
	 * Checks if the Management server is locally hosted.
	 * @return {@code True} if so.
	 * @throws IOException When an I/O exception occurs.
	 */
	private static boolean isLocallyHosted() throws IOException {
		InetAddress address = InetAddress.getByName(GameWorld.getSettings().getMsAddress());
		if (address.isAnyLocalAddress() || address.isLoopbackAddress()) {
			return true;
		}
		return NetworkInterface.getByInetAddress(address) != null;
	}

	/**
	 * Terminates the world communicator.
	 */
	public static void terminate() {
		setState(ManagementServerState.NOT_AVAILABLE);
		if (reactor != null) {
			reactor.terminate();
			reactor = null;
		}
	}

	/**
	 * Gets and removes the login attempt for the given username.
	 * @param username The username.
	 * @return The login attempt.
	 */
	public static LoginParser finishLoginAttempt(String username) {
		return loginAttempts.remove(username);
	}

	/**
	 * Gets the local world.
	 * @return The world statistics of this world server.
	 */
	public static WorldStatistics getLocalWorld() {
		return WORLDS[GameWorld.getSettings().getWorldId() - 1];
	}

	/**
	 * Gets the id of the world the player is connected to.
	 * @param playerName The player's name.
	 * @return The world id, or -1 if the player wasn't connected.
	 */
	public static int getWorld(String playerName) {
		for (int i = 0; i < WORLDS.length; i++) {
			if (WORLDS[i].getPlayers().contains(playerName)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the world statistics for the given index.
	 * @param id The world id.
	 * @return The world statistics.
	 */
	public static WorldStatistics getWorld(int id) {
		return WORLDS[id - 1];
	}

	/**
	 * Gets the session.
	 * @return the session
	 */
	public static IoSession getSession() {
		return session;
	}

	/**
	 * Checks if this world is connected to the Management server.
	 * @return {@code True} if so.
	 */
	public static boolean isEnabled() {
		return state == ManagementServerState.AVAILABLE;
	}

	/**
	 * Gets the login attempts mapping.
	 * @return The login attempts mapping.
	 */
	public static Map<String, LoginParser> getLoginAttempts() {
		return loginAttempts;
	}

	/**
	 * Gets the state.
	 * @return the state
	 */
	public static ManagementServerState getState() {
		return state;
	}

	/**
	 * Sets the state.
	 * @param state the state to set.
	 */
	public static void setState(ManagementServerState state) {
		if (WorldCommunicator.state != state) {
			WorldCommunicator.state = state;
			state.set();
			SystemLogger.logInfo("Management server status: " + state + ".");
		}
	}

	/**
	 * Gets the reactor.
	 * @return the reactor
	 */
	public static NioReactor getReactor() {
		return reactor;
	}

	/**
	 * Sets the reactor.
	 * @param reactor the reactor to set.
	 */
	public static void setReactor(NioReactor reactor) {
		WorldCommunicator.reactor = reactor;
	}

}