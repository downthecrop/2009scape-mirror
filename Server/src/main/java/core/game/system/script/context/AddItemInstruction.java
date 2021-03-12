package core.game.system.script.context;

import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.system.script.ScriptContext;

/**
 * Used for adding an item to the inventory.
 * @author 'Vexia
 */
public final class AddItemInstruction extends ScriptContext {

	/**
	 * The id of the item.
	 */
	private final int id;

	/**
	 * The amount to add.
	 */
	private final int amount;

	/**
	 * Constructs a new {@code PauseInstruction} {@code Object}.
	 */
	public AddItemInstruction() {
		this(-1, -1);
	}

	/**
	 * Constructs a new {@code AddItemInstruction} {@code Object}.
	 * @param id the id.
	 * @param amount the amount.
	 */
	public AddItemInstruction(int id, int amount) {
		super("additem");
		this.id = id;
		this.amount = amount;
	}

	@Override
	public ScriptContext parse(Object... params) {
		int id = 0;
		int amount = 0;
		if (params[0] instanceof Integer) {
			id = (Integer) params[0];
		}
		if (params[1] instanceof Integer) {
			amount = (Integer) params[1];
		}
		AddItemInstruction context = new AddItemInstruction(id, amount);
		context.parameters = params;
		return context;
	}

	@Override
	public boolean execute(final Object... args) {
		final Player player = (Player) args[0];
		player.getInventory().add(new Item(id, amount), player);
		return true;
	}

}