package core.game.system.script.context;

import core.game.content.activity.ActivityManager;
import core.game.node.entity.player.Player;
import core.game.system.script.ScriptContext;

import java.util.Arrays;

/**
 * The start activity instruction.
 * @author Emperor
 */
public final class StartActivityInstruction extends ScriptContext {

	/**
	 * The activity to start.
	 */
	private final String activity;

	/**
	 * The arguments.
	 */
	private final Object[] arguments;

	/**
	 * Constructs a new {@code StartActivityInstruction} {@code Object}.
	 */
	public StartActivityInstruction() {
		this(null);
	}

	/**
	 * Constructs a new {@code StartActivityInstruction} {@code Object}.
	 * @param activity The name of the activity to start.
	 * @param arguments The arguments.
	 */
	public StartActivityInstruction(String activity, Object... arguments) {
		super("startactivity");
		this.activity = activity;
		this.arguments = arguments;
	}

	@Override
	public boolean execute(Object... args) {
		Player player = (Player) args[0];
		args = new Object[arguments.length + 1];
		args[0] = player;
		for (int i = 0; i < arguments.length; i++) {
			args[i + 1] = arguments[i];
		}
		// System.out.println("Starting activity " + activity + " - " +
		// Arrays.toString(args));
		ActivityManager.start(player, activity, false, args);
		return true;
	}

	@Override
	public ScriptContext parse(Object... params) {
		String activityName = null;
		Object[] args = new Object[params.length - 1];
		int paramIndex = 0;
		for (int i = 0; i < params.length; i++) {
			Object o = params[i];
			if (o instanceof String && activityName == null) {
				activityName = (String) o;
				continue;
			}
			args[paramIndex++] = params[i];
		}
		if (paramIndex != args.length) {
			args = Arrays.copyOf(args, paramIndex);
		}
		StartActivityInstruction context = new StartActivityInstruction(activityName, args);
		context.parameters = params;
		return context;
	}

}