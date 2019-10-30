package org.runite;

import org.runite.jagex.GameShell;

/**
 * Handles the launching of our Game Client.
 * @author Keldagrim Development Team
 *
 */

/*

NOTICE: THIS IS THE LIVESERVER CLIENT. For development purposes, use GameLaunch.java instead!!!

 */
public class Client {

	/**
	 * The game settings.
	 */
	public static GameSetting SETTINGS = new GameSetting("2009Scape", "34.74.91.45", 1, "live", false, false);
	
	/**
	 * The main method.
	 * @param args the arguments casted on runtime.
	 */
	public static void main(String[]args) {
		for (int i = 0; i < args.length; i++) {
			String[] cmd = args[i].split("=");
			switch (cmd[0]) {
			case "ip":
				SETTINGS.setIp(cmd[1]);
				break;
			case "world":
				SETTINGS.setWorld(Integer.parseInt(cmd[1]));
				break;
			}
		}
		launch(false);
	}
	
	/**
	 * Launches the client in a determined mode.
	 * @param swiftkit If we're launching swift kit.
	 */
	public static void launch(boolean swiftkit) {
		GameShell.launchDesktop();
	}
	
}
