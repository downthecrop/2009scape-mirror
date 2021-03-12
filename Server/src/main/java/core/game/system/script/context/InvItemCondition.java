package core.game.system.script.context;

import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.system.script.ScriptContext;

import java.util.Arrays;

/**
 * ScriptAPI condition for checking if a player has an item in the inventory.
 * @author Emperor
 */
public final class InvItemCondition extends ScriptContext {

	/**
	 * The items required.
	 */
	private final Item[] items;

	/**
	 * Constructs a new {@code InvItemCondition} {@code Object}.
	 */
	public InvItemCondition() {
		this(null);
	}

	/**
	 * Constructs a new {@code InvItemCondition} {@code Object}.
	 * @param items the items required.
	 */
	public InvItemCondition(Item[] items) {
		super("hasitem");
		this.items = items;
	}

	@Override
	public boolean execute(Object... args) {
		return ((Player) args[0]).getInventory().containsItems(items);
	}

	@Override
	public ScriptContext parse(Object... params) {
		Item[] items = new Item[params.length / 2];
		int index = 0;
		for (int i = 0; i < params.length; i++) {
			if (params[i] instanceof Integer) {
				items[index++] = new Item((Integer) params[i], (Integer) params[i + 1]);
				i++;
			}
		}
		if (index != items.length) {
			items = Arrays.copyOf(items, index);
		}
		ScriptContext context = new InvItemCondition(items);
		context.setParameters(params);
		return context;
	}
}