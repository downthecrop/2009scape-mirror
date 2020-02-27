package org.crandor.game.system.mysql.impl;

import org.crandor.ServerConstants;
import org.crandor.cache.Cache;
import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.node.entity.impl.Animator.Priority;
import org.crandor.game.node.entity.player.link.audio.Audio;
import org.crandor.game.system.SystemLogger;
import org.crandor.game.system.mysql.SQLEntryHandler;
import org.crandor.game.system.mysql.SQLManager;
import org.crandor.game.world.update.flag.context.Animation;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles the parsing of item configs.
 * @author Vexia
 *
 */
public class ItemConfigSQLHandler extends SQLEntryHandler<Object> {

	/**
	 * The tradeable item configuration key.
	 */
	public static final String TRADEABLE = "tradeable";

	/**
	 * The lendable item configuration key.
	 */
	public static final String LENDABLE = "lendable";
	
	/**
	 * If the item is spawnable.
	 */
	public static final String SPAWNABLE = "spawnable";

	/**
	 * The destroy item configuration key.
	 */
	public static final String DESTROY = "destroy";

	/**
	 * The two-handed item configuration key.
	 */
	public static final String TWO_HANDED = "two_handed";

	/**
	 * The high-alchemy price item configuration key.
	 */
	public static final String HIGH_ALCHEMY = "high_alchemy";

	/**
	 * The low-alchemy price item configuration key.
	 */
	public static final String LOW_ALCHEMY = "low_alchemy";

	/**
	 * The shop price item configuration key.
	 */
	public static final String SHOP_PRICE = "shop_price";

	/**
	 * The grand exchange price item configuration key.
	 */
	public static final String GE_PRICE = "grand_exchange_price";

	/**
	 * The examine item configuration key.
	 */
	public static final String EXAMINE = "examine";

	/**
	 * The weight item configuration key.
	 */
	public static final String WEIGHT = "weight";

	/**
	 * The bonuses item configuration key.
	 */
	public static final String BONUS = "bonuses";

	/**
	 * The absorb item configuration key.
	 */
	public static final String ABSORB = "absorb";

	/**
	 * The equipment slot item configuration key.
	 */
	public static final String EQUIP_SLOT = "equipment_slot";

	/**
	 * The attack speed item configuration key.
	 */
	public static final String ATTACK_SPEED = "attack_speed";

	/**
	 * The remove hair item configuration key.
	 */
	public static final String REMOVE_HEAD = "remove_head";

	/**
	 * The remove beard item configuration key.
	 */
	public static final String REMOVE_BEARD = "remove_beard";

	/**
	 * The remove sleeves item configuration key.
	 */
	public static final String REMOVE_SLEEVES = "remove_sleeves";

	/**
	 * The stand anim item configuration key.
	 */
	public static final String STAND_ANIM = "stand_anim";

	/**
	 * The stand-run anim item configuration key.
	 */
	public static final String STAND_TURN_ANIM = "stand_turn_anim";

	/**
	 * The walk anim item configuration key.
	 */
	public static final String WALK_ANIM = "walk_anim";

	/**
	 * The run anim item configuration key.
	 */
	public static final String RUN_ANIM = "run_anim";

	/**
	 * The turn 180 anim item configuration key.
	 */
	public static final String TURN180_ANIM = "turn180_anim";

	/**
	 * The turn 90cw anim item configuration key.
	 */
	public static final String TURN90CW_ANIM = "turn90cw_anim";

	/**
	 * The turn 90ccw anim item configuration key.
	 */
	public static final String TURN90CCW_ANIM = "turn90ccw_anim";

	/**
	 * The weapon interface.
	 */
	public static final String WEAPON_INTERFACE = "weapon_interface";

	/**
	 * If the item has a special attack bar.
	 */
	public static final String HAS_SPECIAL = "has_special";

	/**
	 * The item's attack animations.
	 */
	public static final String ATTACK_ANIMS = "attack_anims";

	/**
	 * The items destroy message.
	 */
	public static final String DESTROY_MESSAGE = "destroy_message";

	/**
	 * The requirements.
	 */
	public static final String REQUIREMENTS = "requirements";

	/**
	 * The grand exchange buying limit attribute key.
	 */
	public static final String GE_LIMIT = "ge_buy_limit";

	/**
	 * The defence animation key.
	 */
	public static final String DEFENCE_ANIMATION = "defence_anim";

	/**
	 * The attack sound key.
	 */
	public static final String ATTACK_AUDIO = "attack_audios";

	/**
	 * The point price.
	 */
	public static final String POINT_PRICE = "point_price";

	/**
	 * If the item is bankable.
	 */
	public static final String BANKABLE = "bankable";

	/**
	 * If the item is a rare item.
	 */
	public static final String RARE_ITEM = "rare_item";

	/**
	 * The tokkul price of an item.
	 */
	public static final String TOKKUL_PRICE = "tokkul_price";

	/**
	 * The render animation id of an item.
	 */
	public static final String RENDER_ANIM_ID = "render_anim";
	
	/**
	 * the archery ticket price of an item.
	 */
	public static final String ARCHERY_TICKET_PRICE = "archery_ticket_price";

	/**
	 * Constructs a new {@Code ItemConfigSQLHandler} {@Code Object}
	 */
	public ItemConfigSQLHandler() {
		super(null, (SQLManager.LOCAL ? "server" : ServerConstants.DATABASE_NAMES[0]) + ".item_configs", "", "");
	}

	@Override
	public void parse() throws SQLException {
		connection = getConnection();
		if (connection == null) {
			SQLManager.close(connection);
			return;
		}
		int count = 0;
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE id <= '" + Cache.getItemDefinitionsSize() + "'");
		ResultSet set = statement.executeQuery();
		while (set.next()) {
			parseItem(set.getInt("id"), set);
			count++;
		}
		SystemLogger.log("Parsed " + count + " Item configurations...");
		SQLManager.close(statement.getConnection());
	}

	/**
	 * Parses an item from a result set.
	 * @param itemId the itemId.
	 * @param set the set.
	 */
	private void parseItem(int itemId, ResultSet set) throws SQLException {
		ItemDefinition definition = ItemDefinition.forId(itemId);
		Map<String, Object> configs = definition.getConfigurations();
		ResultSetMetaData data = set.getMetaData();
		Object object = null;
		String name = null;
		for (int i = 0; i < data.getColumnCount(); i ++) {
			object = set.getObject(i + 1);
			if (object == null) {
				continue;
			}
			name = data.getColumnName(i + 1);
			if (object instanceof Integer) {
				switch (name) {
				case DEFENCE_ANIMATION:
					configs.put(name, Animation.create((int) object));
					break;
				case SHOP_PRICE:
					definition.setValue((int) object);
					configs.put(name,  (int) object);
					break;
				default:
					configs.put(name, (int) object);
					break;
				}
			} else if (object instanceof Double) {
				configs.put(name, (double) object);
			} else if (object instanceof String) {
				String string = (String) object;
				if (string.length() == 0) {
					continue;
				}
				switch (name) {
				case EXAMINE:
					String s = (String) object;
					while (s.length() > 0 && s.charAt(0) == ' ') {
						s = s.substring(1, s.length());
					}
					if (s.length() > 255) {
						s = s.substring(0, 255);
					}
					definition.setExamine(s);
					configs.put(name, s);
					break;
				case REQUIREMENTS:
					String[] datas = string.split("-");
					String[] tokens = null;
					Map<Integer, Integer> requirements = new HashMap<>();
					for (String d : datas) {
						tokens = d.replace("{", "").replace("}", "").split(",");
						requirements.put(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
					}
					configs.put(name, requirements);
					break;
				case ATTACK_AUDIO:
					configs.put(name, parseAudioArray(string));
					break;
				case BONUS:
				case ABSORB:
					configs.put(name, parseIntArray(string));
					break;
				case ATTACK_ANIMS:
					configs.put(name, parseAnimArray(string));
					break;
				default:
					configs.put(name, (String) object);
					break;
				}
			} else if (object instanceof Boolean) {
				configs.put(name, (Boolean) object);
			} else if (object instanceof Long) {
			} else {
				System.err.println("Error! Unhanled object type " + object.getClass().getSimpleName() + ", value = " + object + ", name =" + name);
			}
		}
	}

	/**
	 * Parses an Integer array.
	 * @param string The string.
	 * @return The Integer array.
	 */
	private int[] parseIntArray(String string) {
		String[] tokens = string.split(",");
		int[] array = new int[tokens.length];
		for (int i = 0; i < tokens.length; i++) {
			array[i] = Integer.parseInt(tokens[i]);
		}
		return array;
	}

	/**
	 * Parses an Animation array.
	 * @param string The string.
	 * @return The Animation array.
	 */
	private Animation[] parseAnimArray(String string) {
		String[] tokens = string.split(",");
		Animation[] array = new Animation[tokens.length];
		for (int i = 0; i < tokens.length; i++) {
			array[i] = new Animation(Integer.parseInt(tokens[i]), Priority.HIGH);
		}
		return array;
	}

	/**
	 * Parses an Animation array.
	 * @param string The string.
	 * @return The Animation array.
	 */
	private Audio[] parseAudioArray(String string) {
		String[] tokens = string.split(",");
		Audio[] array = new Audio[tokens.length];
		for (int i = 0; i < tokens.length; i++) {
			array[i] = new Audio(Integer.parseInt(tokens[i]));
		}
		return array;
	}

	@Override
	public void create() throws SQLException {
		PreparedStatement statement = connection.prepareStatement("");
		statement.executeUpdate();
		SQLManager.close(statement.getConnection());
	}

	@Override
	public void save() throws SQLException {
		PreparedStatement statement = connection.prepareStatement("");
		statement.executeUpdate();
		SQLManager.close(statement.getConnection());
	}

	@Override
	public Connection getConnection() {
		return SQLManager.getConnection();
	}
}
