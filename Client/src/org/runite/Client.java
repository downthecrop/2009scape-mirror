package org.runite;

import org.crandor.game.node.entity.player.link.ConfigurationManager;
import org.crandor.net.packet.out.Config;
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
	public static GameSetting SETTINGS = new GameSetting("2009Scape", "34.68.75.237", 1, "live", false, false);
	
	/**
	 * The main method.
	 r @param args the arguments casted on runtime.
     r_game

	 */
	public static void main(String[]args) {
		System.out.println("Running liveserver client");
		Configurations.LOCAL_SERVER = false;
		Configurations.LOCAL_MS = false;
		Configurations.MS_IP = Configurations.LOCAL_MS ? "127.0.0.1" : "34.68.75.237"; //Needs to be done because of order it's otherwise set

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
