package org.crandor.cache.def;

import java.util.HashMap;
import java.util.Map;

import org.crandor.tools.StringUtils;
import org.crandor.game.node.Node;

/**
 * Represent's a node's definitions.
 * @author Emperor
 * @param <T> The node type.
 */
public class Definition<T extends Node> {

	/**
	 * The node id.
	 */
	protected int id;

	/**
	 * The name.
	 */
	protected String name = "null";

	/**
	 * The examine info.
	 */
	protected String examine;

	/**
	 * The options.
	 */
	protected String[] options;

	/**
	 * The configurations.
	 */
	protected final Map<String, Object> configurations = new HashMap<String, Object>();

	/**
	 * Constructs a new {@code Definition} {@code Object}.
	 */
	public Definition() {
		/*
		 * empty.
		 */
	}

	/**
	 * Checks if this node has options.
	 * @return {@code True} if so.
	 */
	public boolean hasOptions() {
		return hasOptions(true);
	}

	/**
	 * Checks if this node has options.
	 * @param examine If examine should be treated as an option.
	 * @return {@code True} if so.
	 */
	public boolean hasOptions(boolean examine) {
		if (name.equals("null") || options == null) {
			return false;
		}
		for (String option : options) {
			if (option != null && !option.equals("null")) {
				if (examine || !option.equals("Examine")) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets a configuration of this item's definitions.
	 * @param key The key.
	 * @return The configuration value.
	 */
	@SuppressWarnings("unchecked")
	public <V> V getConfiguration(String key) {
		return (V) configurations.get(key);
	}

	/**
	 * Gets a configuration from this item's definitions.
	 * @param key The key.
	 * @param fail The object to return if there was no value found for this
	 * key.
	 * @return The value, or the fail object.
	 */
	@SuppressWarnings("unchecked")
	public <V> V getConfiguration(String key, V fail) {
		V object = (V) configurations.get(key);
		if (object == null) {
			return fail;
		}
		return object;
	}

	/**
	 * Gets the id.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the examine.
	 * @return The examine.
	 */
	public String getExamine() {
		if (examine == null) {
			examine = "It's a" + (StringUtils.isPlusN(name) ? "n " : " ") + name + ".";
		}
		return examine;
	}

	/**
	 * Sets the examine.
	 * @param examine The examine to set.
	 */
	public void setExamine(String examine) {
		this.examine = examine;
	}

	/**
	 * Gets the options.
	 * @return The options.
	 */
	public String[] getOptions() {
		return options;
	}

	/**
	 * Sets the options.
	 * @param options The options to set.
	 */
	public void setOptions(String[] options) {
		this.options = options;
	}

	/**
	 * Gets the configurations.
	 * @return The configurations.
	 */
	public Map<String, Object> getConfigurations() {
		return configurations;
	}

}