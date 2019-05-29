package org.crandor.game.system.mysql.impl;

import org.crandor.ServerConstants;
import org.crandor.game.node.entity.player.link.music.MusicEntry;
import org.crandor.game.node.entity.player.link.music.MusicZone;
import org.crandor.game.system.mysql.SQLEntryHandler;
import org.crandor.game.system.mysql.SQLManager;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.map.zone.ZoneBorders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles the parsing of the music zones.
 * @author Vexia
 *
 */
public class MusicConfigSQLHandler extends SQLEntryHandler<Object> {

	/**
	 * Constructs a new {@Code MusicConfigSQLHandler} {@Code Object}
	 */
	public MusicConfigSQLHandler() {
		super(null, (SQLManager.LOCAL ? "server" : ServerConstants.DATABASE_NAMES[0]) + ".music_configs", "", "");
	}

	@Override
	public void parse() throws SQLException {
		connection = getConnection();
		if (connection == null) {
			SQLManager.close(connection);
			return;
		}
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table);
		ResultSet set = statement.executeQuery();
		while (set.next()) {
			parseEntry(set.getInt(1), set);
		}
		SQLManager.close(statement.getConnection());
	}

	@Override
	public void create() throws SQLException {}

	@Override
	public void save() throws SQLException {}

	/**
	 * Parses a music entry. 
	 * @param musicId The musicId.
	 * @param set The result set.
	 * @throws SQLException The exception if thrown.
	 */
	private void parseEntry(int musicId, ResultSet set) throws SQLException {
		MusicEntry entry = new MusicEntry(musicId, set.getString("name"), set.getInt("indexId"));
		MusicEntry.getSongs().put(musicId, entry);
		ZoneBorders borders = null;
		String string = set.getString("borders");
		if (string.length() == 0) {
			return;
		}
		String[] borderArray = string.split("-");
		String[] tokens = null;
		for (String border : borderArray) {
			tokens = border.replace("{", "").replace("}", "").split(",");
			borders = new ZoneBorders(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
			if (border.contains("[")) {//no exception borders
				String exceptions = "";
				for (int i = 4; i < tokens.length; i++) {
					exceptions += tokens[i] + (!tokens[i].contains("]~") ? "," : tokens[i].contains("[") ? "," : "");
				}
				tokens = exceptions.split("~");
				String[] e = null;
				for (String exception : tokens) {
					e = exception.replace("[", "").replace("]", "").split(",");
					borders.addException(new ZoneBorders(Integer.parseInt(e[0]), Integer.parseInt(e[1]), Integer.parseInt(e[2]), Integer.parseInt(e[3])));
				}
				e = null;
				exceptions = null;
			}
			MusicZone zone = new MusicZone(musicId, borders);
			for (Integer id : borders.getRegionIds()) {
				RegionManager.forId(id).getMusicZones().add(zone);
			}
		}
		borderArray = null;
		tokens = null;
		borders = null;
		string = null;
	}
	
	public static void main(String...args) throws Throwable {
		SQLManager.init();
		new MusicConfigSQLHandler().parse();
	}

	@Override
	public Connection getConnection() {
		return SQLManager.getConnection();
	}
}
