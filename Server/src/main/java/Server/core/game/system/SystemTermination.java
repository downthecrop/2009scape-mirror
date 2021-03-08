package core.game.system;

import core.ServerConstants;
import core.game.ge.GrandExchangeDatabase;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.login.PlayerParser;
import core.game.world.repository.Repository;
import core.game.ge.OfferManager;
import core.game.interaction.object.dmc.DMCHandler;

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
			for(Player player : Repository.getPlayers()){
				DMCHandler dmc = player.getAttribute("dmc",null);
				if(dmc != null){
					dmc.clear(false);
				}
			}
			save(ServerConstants.DATA_PATH);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		SystemLogger.logInfo("[SystemTerminator] Server successfully terminated!");
		Runtime.getRuntime().removeShutdownHook(ServerConstants.SHUTDOWN_HOOK);
		System.exit(0);
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
		GrandExchangeDatabase.save();
		OfferManager.save();
		SystemLogger.flushLogs();
		SystemLogger.logInfo("[SystemTerminator] Saved Grand Exchange databases!");
		Repository.getDisconnectionQueue().clear();
		for (Iterator<Player> it = Repository.getPlayers().iterator(); it.hasNext();) {
			try {
				Player p = it.next();
				if (p != null && !p.isArtificial()) { // Should never be null.
					p.removeAttribute("combat-time");
					p.clear();
					PlayerParser.save(p);
					p.getDetails().save();
					p.getLogoutPlugins().forEach(playerPlugin -> {
						try {
							playerPlugin.newInstance(p);
						} catch (Throwable throwable) {
							throwable.printStackTrace();
						}
					});
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
//		ServerStore.dump(directory + "store/");
		SystemLogger.logInfo("[SystemTerminator] Saved player accounts!");
	}
}