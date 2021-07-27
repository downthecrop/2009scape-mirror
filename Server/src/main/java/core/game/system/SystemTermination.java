package core.game.system;

import core.game.ge.GrandExchangeDatabase;
import core.game.interaction.object.dmc.DMCHandler;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.login.PlayerParser;
import rs09.Server;
import rs09.ServerConstants;
import rs09.ServerStore;
import rs09.game.ge.OfferManager;
import rs09.game.system.SystemLogger;
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
			for(Player player : Repository.getPlayers()){
				DMCHandler dmc = player.getAttribute("dmc",null);
				if(dmc != null){
					dmc.clear(false);
				}
			}
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
		for (Iterator<Player> it = Repository.getPlayers().iterator(); it.hasNext();) {
			try {
				Player p = it.next();
				if (p != null && !p.isArtificial()) { // Should never be null.
/*					p.removeAttribute("combat-time");
					p.clear();
					PlayerParser.save(p);*/
					p.getDetails().save();
					p.getLogoutPlugins().forEach(playerPlugin -> {
						try {
							playerPlugin.newInstance(p);
						} catch (Throwable throwable) {
							throwable.printStackTrace();
						}
					});
					p.clear();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		long start = System.currentTimeMillis();
		while(!Repository.getDisconnectionQueue().isEmpty() && System.currentTimeMillis() - start < 5000L){
			Repository.getDisconnectionQueue().update();
			try {
				Thread.sleep(100);
			} catch (Exception ignored) {}
		}
		Repository.getDisconnectionQueue().update();
		GrandExchangeDatabase.save();
		OfferManager.save();
		SystemLogger.flushLogs();
		SystemLogger.logInfo("[SystemTerminator] Saved Grand Exchange databases!");
		Repository.getDisconnectionQueue().clear();
		SystemLogger.logInfo("[SystemTerminator] Saving Server Store...");
		ServerStore.save();
		SystemLogger.logInfo("[SystemTerminator] Server Store Saved!");
//		ServerStore.dump(directory + "store/");
		SystemLogger.logInfo("[SystemTerminator] Saved player accounts!");
	}
}