package core.game.system;

import api.ShutdownListener;
import core.game.ge.GrandExchangeDatabase;
import core.game.interaction.object.dmc.DMCHandler;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.login.PlayerParser;
import rs09.Server;
import rs09.ServerConstants;
import rs09.ServerStore;
import rs09.game.content.global.GlobalKillCounter;
import rs09.game.system.SystemLogger;
import rs09.game.world.GameWorld;
import rs09.game.world.repository.Repository;

import java.io.File;
import java.util.Iterator;

/**
 * Handles the terminating of the system.
 * @author Emperor
 * 
 */
public final class SystemTermination {

	/**
	 * Constructs a new {@code SystemTermination} {@code Object}.
	 */
	protected SystemTermination() {
		/*
		 * empty.
		 */
	}

	/**
	 * Terminates the system safely.
	 */
	public void terminate() {
		SystemLogger.logInfo("[SystemTerminator] Initializing termination sequence - do not shutdown!");
		try {
			Server.setRunning(false);
			for (Iterator<Player> it = Repository.getPlayers().iterator(); it.hasNext();) {
				try {
					Player p = it.next();
					if (p != null && !p.isArtificial()) { // Should never be null.
						p.getDetails().save();
						p.clear();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			GameWorld.getShutdownListeners().forEach(ShutdownListener::shutdown);
			if(ServerConstants.DATA_PATH != null)
				save(ServerConstants.DATA_PATH);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		SystemLogger.logInfo("[SystemTerminator] Server successfully terminated!");
	}

	/**
	 * Saves all system data on the directory.
	 * @param directory The base directory.
	 */
	public void save(String directory) {
		File file = new File(directory);
		SystemLogger.logInfo("[SystemTerminator] Saving data [dir=" + file.getAbsolutePath() + "]...");
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		Server.getReactor().terminate();
		long start = System.currentTimeMillis();
		while(!Repository.getDisconnectionQueue().isEmpty() && System.currentTimeMillis() - start < 5000L){
			Repository.getDisconnectionQueue().update();
			try {
				Thread.sleep(100);
			} catch (Exception ignored) {}
		}
		Repository.getDisconnectionQueue().update();
		SystemLogger.flushLogs();
		Repository.getDisconnectionQueue().clear();
	}
}
