package org.crandor.game.node.entity.impl;

import org.crandor.cache.misc.buffer.ByteBufferUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles an entity's game attributes.
 * @author Emperor
 */
public final class GameAttributes {

	/**
	 * The attributes mapping.
	 */
	private final Map<String, Object> attributes = new HashMap<>();

	/**
	 * The list of attributes to save.
	 */
	private final List<String> savedAttributes = new ArrayList<>();

	/**
	 * Constructs a new {@code GameAttributes} {@code Object}.
	 */
	public GameAttributes() {
		/*
		 * Empty.
		 */
	}

	/**
	 * Writes the attribute data to the player buffer.
	 * @param buffer The player's data buffer.
	 */
	public void dump(ByteBuffer buffer) {
		buffer.put((byte) savedAttributes.size());
		for (String key : savedAttributes) {
			Object value = attributes.get(key);
			if (value == null) {
				buffer.put((byte) -1);
			} else if (value instanceof Integer) {
				buffer.put((byte) 0).putInt((Integer) value);
			} else if (value instanceof Short) {
				buffer.put((byte) 1).putShort((Short) value);
			} else if (value instanceof Byte) {
				buffer.put((byte) 2).put((Byte) value);
			} else if (value instanceof Long) {
				buffer.put((byte) 3).putLong((Long) value);
			} else if (value instanceof String) {
				ByteBufferUtils.putString((String) value, buffer.put((byte) 4));
			} else if (value instanceof Boolean) {
				buffer.put((byte) 5).put((byte) ((Boolean) value ? 1 : 0));
			} else {
				System.err.println("[GameAttributes]: Could not save attribute - [attribute=" + key + ", value=" + value + "]!");
				buffer.put((byte) -1);
			}
			ByteBufferUtils.putString(key, buffer);
		}
	}

	/**
	 * Parses the saved attributes from the buffer.
	 * @param buffer The buffer.
	 */
	public void parse(ByteBuffer buffer) {
		int size = buffer.get() & 0xFF;
		for (int i = 0; i < size; i++) {
			Object value = null;
			switch (buffer.get()) {
			case 0:
				value = buffer.getInt();
				break;
			case 1:
				value = buffer.getShort();
				break;
			case 2:
				value = buffer.get();
				break;
			case 3:
				value = buffer.getLong();
				break;
			case 4:
				value = ByteBufferUtils.getString(buffer);
				break;
			case 5:
				value = (buffer.get() == 1);
				break;
			case -1:
				continue;
			}
			String key = ByteBufferUtils.getString(buffer);
			attributes.put(key, value);
			if (!savedAttributes.contains(key)) {
				savedAttributes.add(key);
			}
		}
	}

	/**
	 * Sets an attribute value.
	 * @param key The attribute name.
	 * @param value The attribute value.
	 */
	public void setAttribute(String key, Object value) {
		if (key.startsWith("/save:")) {
			key = key.substring(6, key.length());
			if (!savedAttributes.contains(key)) {
				savedAttributes.add(key);
			}
		}
		attributes.put(key, value);
	}

	/**
	 * Gets an attribute.
	 * @param key The attribute name.
	 * @return The attribute value.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String key) {
		if (!attributes.containsKey(key)) {
			return null;
		}
		return (T) attributes.get(key);
	}

	/**
	 * Gets an attribute.
	 * @param string The attribute name.
	 * @param fail The value to return if the attribute is null.
	 * @return The attribute value, or the fail argument when null.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String string, T fail) {
		Object object = attributes.get(string);
		if (object != null) {
			return (T) object;
		}
		return fail;
	}

	/**
	 * Removes an attribute.
	 * @param string The attribute name.
	 */
	public void removeAttribute(String string) {
		savedAttributes.remove(string);
		attributes.remove(string);
	}

	/**
	 * Gets the attributes.
	 * @return The attributes.
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	/**
	 * Gets the savedAttributes.
	 * @return The savedAttributes.
	 */
	public List<String> getSavedAttributes() {
		return savedAttributes;
	}
}