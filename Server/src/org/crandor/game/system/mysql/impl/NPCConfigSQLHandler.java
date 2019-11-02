package org.crandor.game.system.mysql.impl;

import org.crandor.ServerConstants;
import org.crandor.cache.Cache;
import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.game.content.global.ttrail.ClueLevel;
import org.crandor.game.node.entity.combat.CombatStyle;
import org.crandor.game.node.entity.impl.Animator.Priority;
import org.crandor.game.node.entity.player.link.audio.Audio;
import org.crandor.game.system.SystemLogger;
import org.crandor.game.system.mysql.SQLEntryHandler;
import org.crandor.game.system.mysql.SQLManager;
import org.crandor.game.world.update.flag.context.Animation;

import java.sql.*;
import java.util.Map;

/**
 * Handles the NPC configuration parsing.
 * @author Vexia
 * 
 */
public final class NPCConfigSQLHandler extends SQLEntryHandler<Object> {

	/**
	 * The weakness attribute.
	 */
	public static final String WEAKNESS = "weakness";

	/**
	 * The lifepoints attribute.
	 */
	public static final String LIFEPOINTS = "lifepoints";

	/**
	 * The attack level attribute.
	 */
	public static final String ATTACK_LEVEL = "attack_level";

	/**
	 * The strength level attribute.
	 */
	public static final String STRENGTH_LEVEL = "strength_level";

	/**
	 * The defence level attribute.
	 */
	public static final String DEFENCE_LEVEL = "defence_level";

	/**
	 * The range level attribute.
	 */
	public static final String RANGE_LEVEL = "range_level";

	/**
	 * The range level attribute.
	 */
	public static final String MAGIC_LEVEL = "magic_level";

	/**
	 * The examine attribute.
	 */
	public static final String EXAMINE = "examine";

	/**
	 * The slayer task attribute.
	 */
	public static final String SLAYER_TASK = "slayer_task";

	/**
	 * The poisonous attribute.
	 */
	public static final String POISONOUS = "poisonous";

	/**
	 * The aggressive attribute.
	 */
	public static final String AGGRESSIVE = "aggressive";

	/**
	 * The respawn delay attribute.
	 */
	public static final String RESPAWN_DELAY = "respawn_delay";

	/**
	 * The attack speed attribute.
	 */
	public static final String ATTACK_SPEED = "attack_speed";

	/**
	 * The poison immune attribute.
	 */
	public static final String POISON_IMMUNE = "poison_immune";

	/**
	 * The bonuses attribute.
	 */
	public static final String BONUSES = "bonuses";

	/**
	 * The start graphic attribute.
	 */
	public static final String START_GRAPHIC = "start_gfx";

	/**
	 * The projectile attribute.
	 */
	public static final String PROJECTILE = "projectile";

	/**
	 * The end graphic attribute.
	 */
	public static final String END_GRAPHIC = "end_gfx";

	/**
	 * The combat style attribute.
	 */
	public static final String COMBAT_STYLE = "combat_style";

	/**
	 * The aggressive radius attribute.
	 */
	public static final String AGGRESSIVE_RADIUS = "agg_radius";

	/**
	 * The slayer experience attribute.
	 */
	public static final String SLAYER_EXP = "slayer_exp";

	/**
	 * The amount to poison.
	 */
	public static final String POISON_AMOUNT = "poison_amount";

	/**
	 * The movement radius.
	 */
	public static final String MOVEMENT_RADIUS = "movement_radius";

	/**
	 * The spawn animation.
	 */
	public static final String SPAWN_ANIMATION = "spawn_animation";

	/**
	 * The start height.
	 */
	public static final String START_HEIGHT = "start_height";

	/**
	 * The projectile height.
	 */
	public static final String PROJECTILE_HEIGHT = "prj_height";

	/**
	 * The end height.
	 */
	public static final String END_HEIGHT = "end_height";

	/**
	 * The clue level.
	 */
	public static final String CLUE_LEVEL = "clue_level";

	/**
	 * The spell id.
	 */
	public static final String SPELL_ID = "spell_id";

	/**
	 * The combat audio.
	 */
	public static final String COMBAT_AUDIO = "combat_audio";
	
	/**
	 * The melee-attack anim.
	 */
	public static final String MELEE_ANIMATION = "melee_animation";
	
	/**
	 * The defence anim.
	 */
	public static final String DEFENCE_ANIMATION = "defence_animation";
	
	/**
	 * The death anim.
	 */
	public static final String DEATH_ANIMATION = "death_animation";
	
	/**
	 * The range anim.
	 */
	public static final String RANGE_ANIMATION = "range_animation";
	
	/**
	 * The magic anim.
	 */
	public static final String MAGIC_ANIMATION = "magic_animation";

	/**
	 * The combat style protected from.
	 */
	public static final String PROTECT_STYLE = "protect_style";

	/**
	 * Constructs a new {@Code NPCConfiguration} {@Code Object}
	 */
	public NPCConfigSQLHandler() {
		super(null, (SQLManager.LOCAL ? "server" : ServerConstants.DATABASE_NAMES[0]) + ".npc_configs", "", "");
	}

	@Override
	public void parse() throws SQLException {
		connection = getConnection();
		if (connection == null) {
			SQLManager.close(connection);
			return;
		}
		int count = 0;
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE id <= '" + Cache.getNPCDefinitionsSize() + "'");
		ResultSet set = statement.executeQuery();
		while (set.next()) {
			parseNpc(set.getInt("id"), set);
			count++;
		}
		SystemLogger.log("Parsed " + count + " NPC configurations...");
		SQLManager.close(statement.getConnection());
	}

	/**
	 * Parses an npc from a result set.
	 * @param npcId the npc Id.
	 * @param set the set.
	 */
	private void parseNpc(int npcId, ResultSet set) throws SQLException {
		NPCDefinition definition =  NPCDefinition.forId(npcId);
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
				case MELEE_ANIMATION:
				case RANGE_ANIMATION:
				case MAGIC_ANIMATION:
				case DEATH_ANIMATION:
				case DEFENCE_ANIMATION:
					configs.put(name, new Animation((int) object, Priority.HIGH));
					break;
				case COMBAT_STYLE:
				case PROTECT_STYLE :
					configs.put(name, CombatStyle.values()[(int) object]);
					break;
				case CLUE_LEVEL:
					configs.put(name, ClueLevel.values()[(int) object]);
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
				case COMBAT_AUDIO:
					configs.put(name, parseAudioArray(string));
					break;
				case BONUSES:
					configs.put(name, parseIntArray(string));
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