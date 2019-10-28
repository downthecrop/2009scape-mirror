package org.crandor.game.system.mysql.impl;

import org.crandor.ServerConstants;
import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.equipment.Ammunition;
import org.crandor.game.node.entity.combat.equipment.BoltEffect;
import org.crandor.game.node.entity.combat.equipment.RangeWeapon;
import org.crandor.game.node.entity.impl.Animator.Priority;
import org.crandor.game.node.entity.impl.Projectile;
import org.crandor.game.system.mysql.SQLEntryHandler;
import org.crandor.game.system.mysql.SQLManager;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the parsing of the range data configurations.
 * @author Vexia
 *
 */
public class RangeConfigSQLHandler extends SQLEntryHandler<Object> {

	/**
	 * Constructs a new {@Code RangeConfigSQLHandler} {@Code Object}
	 */
	public RangeConfigSQLHandler() {
		super(null, (SQLManager.LOCAL ? "server" : ServerConstants.DATABASE_NAMES[0]) + ".range_configs", "", "");
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
			parseWeapon(set.getInt(1), set);
		}
		statement = connection.prepareStatement("SELECT * FROM " +(SQLManager.LOCAL ? "server" : ServerConstants.DATABASE_NAMES[0]) + ".ammo_configs");
		set = statement.executeQuery();
		while (set.next()) {
			parseAmmo(set.getInt(1), set);
		}
		SQLManager.close(statement.getConnection());
	}
	
	/**
	 * Parses a range weapon from the result set. 
	 * @param itemId The item id.
	 * @param set The result set.
	 * @throws SQLException The exception.
	 */
	private void parseWeapon(int itemId, ResultSet set) throws SQLException {
		List<Integer> ammunition = new ArrayList<>();
		String[] tokens = set.getString("ammunition").split(",");
		for (String s : tokens) {
			ammunition.add(Integer.parseInt(s));
		}
		RangeWeapon weapon = new RangeWeapon(itemId, new Animation(set.getInt("animation"), Priority.HIGH), ItemDefinition.forId(itemId).getConfiguration(ItemConfigSQLHandler.ATTACK_SPEED, 4), set.getInt("ammo_slot"), set.getInt("weapon_type"), set.getBoolean("drop_ammo"), ammunition);
		RangeWeapon.getRangeWeapons().put(itemId, weapon);
	}

	/**
	 * Parses range ammunition from the result set.
	 * @param itemId The item id.
	 * @param set The result set.
	 * @throws SQLException The SQL Exception.
	 */
	private void parseAmmo(int itemId, ResultSet set) throws SQLException {
		String[] startGfx = set.getString("start_graphic").split(",");
		Graphics start = new Graphics(Integer.parseInt(startGfx[0]), Integer.parseInt(startGfx[1]));
		Graphics darkBow = null;
		if (set.getString("darkbow_graphic") != "") {
			String[] tok = set.getString("darkbow_graphic").split(",");
			darkBow = new Graphics(Integer.parseInt(tok[0]), Integer.parseInt(tok[1]));
		}
		String[] p = set.getString("projectile").split(",");
		Ammunition ammo = new Ammunition(itemId, start, darkBow, Projectile.create((Entity) null, null, Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]), Integer.parseInt(p[3]), Integer.parseInt(p[4]), Integer.parseInt(p[5]), Integer.parseInt(p[6])), set.getInt("poison_damage"));
		BoltEffect effect = BoltEffect.forId(itemId);
		if (effect != null) {
			ammo.setEffect(effect);
		}
		Ammunition.getAmmunition().put(itemId, ammo);
	}

	@Override
	public void create() throws SQLException {}

	@Override
	public void save() throws SQLException {}
	@Override
	public Connection getConnection() {
		return SQLManager.getConnection();
	}
}
