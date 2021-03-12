package core.game.node.entity.npc.quest.gertrudes_cat;

import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.game.world.map.Location;

/**
 * Represents the plugin used for the fluff npc.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class FluffNPC extends AbstractNPC {

	/**
	 * The NPC ids of NPCs using this plugin.
	 */
	private static final int[] ID = { 2997 };

	/**
	 * Constructs a new {@code FluffNPC} {@code Object}.
	 */
	public FluffNPC() {
		super(0, null);
	}

	/**
	 * Constructs a new {@code FluffNPC} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	private FluffNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new FluffNPC(id, location);
	}

	@Override
	public boolean isHidden(final Player player) {
		if (player.getQuestRepository().getQuest("Gertrude's Cat").getStage(player) < 20) {
			return true;
		}
		return player.getAttribute("hidefluff", 0L) > System.currentTimeMillis();
	}

	@Override
	public int[] getIds() {
		return ID;
	}

}
