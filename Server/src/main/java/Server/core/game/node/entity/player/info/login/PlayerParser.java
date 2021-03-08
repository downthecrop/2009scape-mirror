package core.game.node.entity.player.info.login;

import core.ServerConstants;
import core.game.node.entity.combat.CombatSpell;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager;
import core.game.node.entity.player.link.emote.Emotes;
import core.game.system.SystemLogger;
import core.game.world.map.Location;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Arrays;

/**
 * Class used to abstract the process of loading a player save.
 * @author Ceikry
 */
public final class PlayerParser {
	/**
	 * Parses or creates the player's save file depending on whether or not it exists.
	 * @param player The player.
	 */
	public static boolean parse(Player player) {
		File JSON = new File(ServerConstants.PLAYER_SAVE_PATH + player.getName() + ".json");

		try {
			if (JSON.exists()) { //parse the new JSON type.
				new PlayerSaveParser(player).parse();
			} else { //Create new save
				makeFromTemplate(player);
				new PlayerSaveParser(player).parse();
			}
			return true;
		} catch (Exception e){
			return false;
		}
	}
	/**
	 * Saves the player's details to the character file at data/players/player_name.json
	 * @param player The player.
	 */
	public static void save(Player player) {
		new PlayerSaver(player).save();
	}

	/**
	 * Copies the template at data/players/template/template.json to data/players/player_name.json
	 * @param player the player to copy the template for.
	 */
	public static void makeFromTemplate(Player player){
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(ServerConstants.PLAYER_SAVE_PATH + "template/template.json");
			os = new FileOutputStream(ServerConstants.PLAYER_SAVE_PATH + player.getName() + ".json");
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} catch (Exception ignored){
		} finally {
			try {
				assert is != null;
				is.close();
				assert os != null;
				os.close();
			} catch (Exception f){
				SystemLogger.logWarn("Unable to close file copiers in PlayerParser.java line 216.");
			}
		}
	}
}