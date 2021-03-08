package core.game.system.script.context;

import core.game.node.entity.player.Player;
import core.game.system.script.ScriptContext;

/**
 * ScriptAPI condition for checking if a player has warrior guild tokens.
 * @author 'Vexia
 */
public final class WGuildTokenCondition extends ScriptContext {

	/**
	 * Constructs a new {@code WGuildTokenCondition} {@code Object}.
	 */
	public WGuildTokenCondition() {
		super("hasWarriorTokens");
	}

	@Override
	public boolean execute(Object... args) {
		return ((Player) args[0]).getSavedData().getActivityData().getWarriorGuildTokens() > 0;
	}

	@Override
	public ScriptContext parse(Object... params) {
		ScriptContext context = new WGuildTokenCondition();
		context.setParameters(params);
		return context;
	}
}