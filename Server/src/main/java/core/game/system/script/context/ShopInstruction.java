package core.game.system.script.context;

import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.system.script.ScriptContext;
import core.game.world.map.RegionManager;

/**
 * Handles the opening of a shop instruction.
 * @author 'Vexia
 */
public class ShopInstruction extends ScriptContext {

	/**
	 * The id to open.
	 */
	private int id;

	/**
	 * Constructs a new {@code ShopInstruction} {@code Object}.
	 */
	public ShopInstruction() {
		this(-1);
	}

	/**
	 * Constructs a new {@code ShopInstruction} {@code Object}.
	 * @param id the id.
	 */
	public ShopInstruction(int id) {
		super("openshop");
		this.setInstant(false);
		this.id = id;
	}

	@Override
	public boolean execute(Object... args) {
		Player player = (Player) args[0];
		NPC n = RegionManager.getNpc(player, id);
		n.openShop(player);
		return true;
	}

	@Override
	public ScriptContext parse(Object... params) {
		int id = -1;
		if (params[0] instanceof Integer) {
			id = (Integer) params[0];
		}
		ShopInstruction context = new ShopInstruction(id);
		context.parameters = params;
		return context;
	}

}