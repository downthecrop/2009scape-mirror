package core.game.system.script.context;

import core.game.node.entity.player.Player;
import core.game.system.script.ScriptContext;

/**
 * Sets an attribute.
 * @author Emperor
 */
public final class SetAttributeInstruction extends ScriptContext {

	/**
	 * The attribute key
	 */
	private final String key;

	/**
	 * The object.
	 */
	private final Object value;

	/**
	 * Constructs a new {@code PMessageInstruction} {@code Object}.
	 */
	public SetAttributeInstruction() {
		this(null, null);
	}

	/**
	 * Constructs a new {@code SetAttributeInstruction} {@code Object}.
	 * @param key The attribute key.
	 * @param value The value.
	 */
	public SetAttributeInstruction(String key, Object value) {
		super("setattribute");
		this.key = key;
		this.value = value;
	}

	@Override
	public boolean execute(Object... args) {
		Player player = (Player) args[0];
		player.setAttribute(key, value);
		return true;
	}

	@Override
	public ScriptContext parse(Object... params) {
		String key = null;
		Object value = null;
		for (int i = 0; i < params.length; i++) {
			Object o = params[i];
			if (o instanceof String && key == null) {
				key = (String) o;
			} else {
				value = o;
			}
		}
		SetAttributeInstruction context = new SetAttributeInstruction(key, value);
		context.parameters = params;
		return context;
	}
}