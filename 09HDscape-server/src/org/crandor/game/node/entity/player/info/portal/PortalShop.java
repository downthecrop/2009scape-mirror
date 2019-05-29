package org.crandor.game.node.entity.player.info.portal;

import org.crandor.game.node.entity.player.info.PlayerDetails;
import org.crandor.game.system.mysql.SQLManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the players online shopping.
 * @author Vexia
 *
 */
public final class PortalShop {

	/**
	 * The list of perks this player has.
	 */
	private final List<Perks> perks = new ArrayList<>();

	/**
	 * The player details.
	 */
	private final PlayerDetails details;

	/**
	 * The amount of credits the player has.
	 */
	private int credits;

	/**
	 * Constructs a new {@code PortalShop} {@code Object}.
	 */
	public PortalShop(PlayerDetails details) {
		this.details = details;
	}

	/**
	 * Parses the perks from the db.
	 */
	public void parsePerks(String val) {
		perks.clear();
		String[] tokens = (val.trim()).split(",");
		if (val != null && val.length() > 0 && !val.contains(",")) {
			perks.add(Perks.forId(Integer.parseInt(val.trim())));
			return;
		}
		if (tokens == null) {
			return;
		}
		Perks p = null;
		for (String perk : tokens) {
			if (perk == null || perk == "" || perk.length() == 0) {
				continue;
			}
			p = Perks.forId(Integer.parseInt(perk));
			if (p == null) {
				continue;
			}
			addPerk(p);
		}
	}

	/**
	 * Adds a perk to the list.
	 * @param perk the perk.
	 */
	public void addPerk(Perks perk) {
		if (perk == null || perks.contains(perk)) {
			return;
		}
		perks.add(perk);
	}

	/**
	 * Checks if the player has a perk.
	 * @param perk the perk.
	 * @return {@code True} if so.
	 */
	public boolean hasPerk(Perks perk) {
		return perks.contains(perk);
	}

	/**
	 * Syncs the credits.
	 */
	public boolean syncCredits() {
		if (!SQLManager.isInitialized()) {
			return false;
		}
		Connection connection = SQLManager.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM members WHERE username = '" + getDetails().getUsername() + "'");
			ResultSet set = statement.executeQuery();
			if (set.next()) {
				setCredits(set.getInt("credits"));
				SQLManager.close(connection);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		SQLManager.close(connection);
		return false;
	}

	/**
	 * Syncs the perks.
	 */
	public boolean syncPerks() {
		if (!SQLManager.isInitialized()) {
			return false;
		}
		Connection connection = SQLManager.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM members WHERE username = '" + getDetails().getUsername() + "'");
			ResultSet set = statement.executeQuery();
			if (set.next()) {
				parsePerks(set.getString("perks"));
				SQLManager.close(connection);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		SQLManager.close(connection);
		return false;
	}

	/**
	 * Removes the perk.
	 * @param perk the perk.
	 * @param update if we update sql.
	 */
	public boolean removePerk(Perks perk) {
		if (perk == null || !perks.contains(perk)) {
			return false;
		}
		if (!syncPerks()) {
			return false;
		}
		if (!perks.contains(perk)) {
			return false;
		}
		perks.remove(perk);
		details.getSqlManager().getTable().getColumn("perks").updateValue(getPerkSyntax());
		details.save();
		return true;
	}


	/**
	 * Sets the player's Keldagrim credits.
	 * @param credits The amount of credits.
	 * @param sqlUpdate If the SQL table should be updated.
	 */
	public void setCredits(int credits, boolean sqlUpdate) {
		setCredits(credits);
		getDetails().getSqlManager().getTable().getColumn("credits").updateValue(credits);
		getDetails().getSqlManager().getTable().getColumn("credits").setChanged(true);
		if (sqlUpdate) {
			getDetails().getSqlManager().save();
		}
	}

	/**
	 * Gets the perk syntax.
	 * @return the perk syntax.
	 */
	private String getPerkSyntax() {
		String value = "";
		for (Perks perk : perks) {
			value += perk.getProductId() + ",";
		}
		if (value.length() > 0 && value.charAt(value.length() - 1) == ',') {
			value = value.substring(0, value.length() - 1);
		}
		return value;
	}


	/**
	 * Sets the credits.
	 * @param credits The credits to set.
	 * @deprecated Only use when syncing and parsing.
	 */
	@Deprecated
	public void setCredits(int credits) {
		this.credits = credits;
	}

	/**
	 * Gets the credits.
	 * @return The credits.
	 */
	public int getCredits() {
		return credits;
	}

	/**
	 * Gets the perks.
	 * @return The perks.
	 */
	public List<Perks> getPerks() {
		return perks;
	}

	/**
	 * Gets the details.
	 * @return the details.
	 */
	public PlayerDetails getDetails() {
		return details;
	}

}
