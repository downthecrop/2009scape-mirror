package core.game.system.mysql.impl;

import core.ServerConstants;
import core.game.node.entity.player.info.PlayerDetails;
import core.game.system.mysql.SQLColumn;
import core.game.system.mysql.SQLEntryHandler;
import core.game.system.mysql.SQLManager;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Handles player details parsing/saving using SQL.
 * @author Emperor
 */
public final class PlayerSQLHandler extends SQLEntryHandler<PlayerDetails> {

	/**
	 * Constructs a new {@code PlayerSQLHandler} {@code Object}.
	 * @param entry The player details.
	 */
	public PlayerSQLHandler(PlayerDetails entry) {
		super(entry, (SQLManager.LOCAL ? "global" : ServerConstants.DATABASE_NAME) + ".members", "username", entry.getUsername());
	}

	@Override
	public void parse() throws SQLException {
		for (SQLColumn column : entry.getSqlManager().getTable().getColumns()) {
			if (!column.isParse()) {
				continue;
			}
			if (column.getType() == Integer.class) {
				column.setValue(result.getInt(column.getName()));
			} else if (column.getType() == String.class) {
				column.setValue(result.getString(column.getName()));
			} else if (column.getType() == Boolean.class) {
				column.setValue(result.getBoolean(column.getName()));
			} else if (column.getType() == Long.class) {
				column.setValue(result.getLong(column.getName()));
			} else if (column.getType() == Timestamp.class) {
				column.setValue(result.getTimestamp(column.getName()));
			} else if (column.getType() == BigInteger.class) {
				column.setValue(result.getLong(column.getName()));
			}
		}
	}

	@Override
	public String getReadSelection() {
		StringBuilder selection = new StringBuilder();
		boolean first = true;
		for (SQLColumn column : entry.getSqlManager().getTable().getColumns()) {
			if (!first) {
				selection.append(",");
			} else {
				first = false;
			}
			selection.append(column.getName());
		}
		return selection.toString();
	}

	@Override
	public void create() throws SQLException {
		if (entry == null) {
			return;
		}
		String[] names = new String[entry.getSqlManager().getTable().getColumns().length];
		for (int i = 0; i < entry.getSqlManager().getTable().getColumns().length; i++) {
			names[i] = entry.getSqlManager().getTable().getColumns()[i].getName();
		}
		PreparedStatement statement = getWritingStatement(true, names);
		for (int i = 0; i < entry.getSqlManager().getTable().getColumns().length; i++) {
			SQLColumn column = entry.getSqlManager().getTable().getColumns()[i];
			if (column.getType() == Integer.class) {
				statement.setInt(i + 2, (int) column.getValue());
			} else if (column.getType() == String.class) {
				statement.setString(i + 2, (String) column.getValue());
			} else if (column.getType() == Boolean.class) {
				statement.setBoolean(i + 2, (boolean) column.getValue());
			} else if (column.getType() == Long.class) {
				statement.setLong(i + 2, (Long) column.getValue());
			} else if (column.getType() == Timestamp.class) {
				statement.setTimestamp(i + 2, (Timestamp) column.getValue());
			} else if (column.getType() == java.math.BigInteger.class) {
				statement.setLong(i + 2, (Long) column.getValue());
			}
		}
		statement.executeUpdate();
		SQLManager.close(statement.getConnection());
	}

	@Override
	public void save() throws SQLException {
		List<SQLColumn> updated = entry.getSqlManager().getTable().getChanged();
		if (updated.isEmpty()) {
			return;
		}
		String[] names = new String[updated.size()];
		for (int i = 0; i < names.length; i++) {
			names[i] = updated.get(i).getName();
		}
		PreparedStatement statement = getWritingStatement(false, names);
		for (int i = 0; i < updated.size(); i++) {
			SQLColumn column = updated.get(i);
			if (column.getType() == Integer.class) {
				statement.setInt(i + 1, (int) column.getValue());
			} else if (column.getType() == String.class) {
				statement.setString(i + 1, (String) column.getValue());
			} else if (column.getType() == Boolean.class) {
				statement.setBoolean(i + 1, (boolean) column.getValue());
			} else if (column.getType() == Long.class) {
				statement.setLong(i + 1, (Long) column.getValue());
			} else if (column.getType() == Timestamp.class) {
				statement.setTimestamp(i + 1, (Timestamp) column.getValue());
			} else if (column.getType() == BigInteger.class) {
				statement.setLong(i + 1, (Long) column.getValue());
			}
		}
		statement.executeUpdate();
		SQLManager.close(statement.getConnection());
	}

	@Override
	public Connection getConnection() {
		return SQLManager.getConnection();
	}

}