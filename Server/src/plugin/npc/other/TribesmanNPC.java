package plugin.npc.other;

import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.system.mysql.impl.NPCConfigSQLHandler;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.world.map.Location;

/**
 * Represents the tribesamn npc.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class TribesmanNPC extends AbstractNPC {

	/**
	 * Represents the npc ids.
	 */
	private static final int[] IDS = new int[] { 191, 2496, 2497 };

	/**
	 * Constructs a new {@code TribesmanNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	public TribesmanNPC(int id, Location location) {
		super(id, location, true);
		getDefinition().getConfigurations().put(NPCConfigSQLHandler.POISONOUS, true);
	}

	/**
	 * Constructs a new {@code TribesmanNPC} {@code Object}.
	 */
	public TribesmanNPC() {
		super(0, null);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new TribesmanNPC(id, location);
	}

	@Override
	public int[] getIds() {
		return IDS;
	}

}
