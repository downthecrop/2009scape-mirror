package core.game.system.script.context;

import core.game.node.entity.Entity;
import core.game.system.script.ScriptContext;
import core.tools.RandomFunction;

/**
 * Sets a random value.
 * @author Emperor
 */
public final class SetRandomInstruction extends ScriptContext {

	/**
	 * Sets the value.
	 */
	private final int value;

	/**
	 * Constructs a new {@code SetRandomInstruction} {@code Object}.
	 */
	public SetRandomInstruction() {
		this(1);
	}

	/**
	 * Constructs a new {@code SetRandomInstruction} {@code Object}.
	 * @param value The value.
	 */
	public SetRandomInstruction(int value) {
		super("setrandom");
		this.value = value;
	}

	@Override
	public ScriptContext parse(Object... params) {
		int value = 0;
		if (params[0] instanceof Integer) {
			value = (Integer) params[0];
		}
		SetRandomInstruction context = new SetRandomInstruction(value);
		context.parameters = params;
		return context;
	}

	@Override
	public boolean execute(Object... args) {
		((Entity) args[0]).setAttribute("asc_random", RandomFunction.randomize(value));
		return true;
	}

}