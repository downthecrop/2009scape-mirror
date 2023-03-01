package core.game.system;

import core.api.PersistWorld;
import core.api.ShutdownListener;
import core.game.node.entity.player.Player;
import core.Server;
import core.ServerConstants;
import core.ServerStore;
import core.game.bots.AIRepository;
import core.game.node.entity.player.info.PlayerMonitor;
import core.tools.Log;
import core.tools.SystemLogger;
import core.game.world.GameWorld;
import core.game.world.repository.Repository;

import java.io.File;
import java.util.Iterator;

import static core.api.ContentAPIKt.log;

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
		log(this.getClass(), Log.INFO, "Initializing termination sequence - do not shutdown!");
		try {
			log(this.getClass(), Log.INFO, "Shutting down networking...");
			Server.setRunning(false);
			log(this.getClass(), Log.INFO, "Stopping all bots...");
			AIRepository.clearAllBots();
			Server.getReactor().terminate();
			log(this.getClass(), Log.INFO, "Stopping all pulses...");
			GameWorld.getMajorUpdateWorker().stop();
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
			PlayerMonitor.flushRemainingEventsImmediately();
			ServerStore s = null;
			for (PersistWorld wld : GameWorld.getWorldPersists()) {
				if (wld instanceof ServerStore)
					s = (ServerStore) wld;
				else
					wld.save();
			}
			//ServerStore should ***always*** save last. Fudging a race condition here :)
			if (s != null)
				s.save();
			if(ServerConstants.DATA_PATH != null)
				save(ServerConstants.DATA_PATH);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		log(this.getClass(), Log.INFO, "Server successfully terminated!");
	}

	/**
	 * Saves all system data on the directory.
	 * @param directory The base directory.
	 */
	public void save(String directory) {
		File file = new File(directory);
		log(this.getClass(), Log.INFO, "Saving data [dir="+ file.getAbsolutePath() + "]...");
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
		Repository.getDisconnectionQueue().clear();
	}
}
