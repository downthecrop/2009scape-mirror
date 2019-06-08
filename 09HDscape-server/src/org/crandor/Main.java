package org.crandor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.crandor.game.system.SystemLogger;
import org.crandor.game.system.SystemShutdownHook;
import org.crandor.game.system.mysql.SQLManager;
import org.crandor.game.world.GameSettings;
import org.crandor.game.world.GameWorld;
import org.crandor.gui.ConsoleFrame;
import org.crandor.net.NioReactor;
import org.crandor.net.amsc.WorldCommunicator;
import org.crandor.tools.TimeStamp;
import org.crandor.tools.backup.AutoBackup;

/**
 * The main class, for those that are unable to read the class' name.
 * @author Emperor
 * @author Vexia
 * 
 */
public final class Main extends Application {

	/**
	 * The time stamp of when the server started running.
	 */
	public static long startTime;

	/**
	 * The NIO reactor.
	 */
	public static NioReactor reactor;

	private static AutoBackup backup;

	/**
	 * The main method, in this method we load background utilities such as
	 * cache and our world, then end with starting networking.
	 * @param args The arguments cast on runtime.
	 * @throws Throwable When an exception occurs.
	 */
	public static void main(String... args) throws Throwable {
		if (args.length > 0) {
			GameWorld.setSettings(GameSettings.parse(args));
		}
		if (GameWorld.getSettings().isGui()) {
			ConsoleFrame.getInstance().init();
		}
		startTime = System.currentTimeMillis();
		final TimeStamp t = new TimeStamp();
//		backup = new AutoBackup();
		GameWorld.prompt(true);
		SQLManager.init();
		Runtime.getRuntime().addShutdownHook(new Thread(new SystemShutdownHook()));
		SystemLogger.log("Starting NIO reactor...");
		reactor = NioReactor.configure(43594 + GameWorld.getSettings().getWorldId());
		WorldCommunicator.connect();
		reactor.start();
		SystemLogger.log(GameWorld.getName() + " flags " + GameWorld.getSettings().toString());
		SystemLogger.log(GameWorld.getName() + " started in " + t.duration(false, "") + " milliseconds.");

	}

	/**
	 * Gets the startTime.
	 * @return the startTime
	 */
	public static long getStartTime() {
		return startTime;
	}

	/**
	 * Sets the bastartTime.
	 * @param startTime the startTime to set.
	 */
	public static void setStartTime(long startTime) {
		Main.startTime = startTime;
	}

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Main Frame.fxml"));
        primaryStage.setTitle("Management Panel");
        primaryStage.setScene(new Scene(root, 600, 450));
        primaryStage.show();
    }
}