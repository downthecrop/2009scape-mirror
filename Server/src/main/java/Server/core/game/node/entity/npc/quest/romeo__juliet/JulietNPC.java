package core.game.node.entity.npc.quest.romeo__juliet;

import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.game.world.map.Location;

/**
 * Represents the juliet npc.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class JulietNPC extends AbstractNPC {

	/**
	 * The NPC ids of NPCs using this plugin.
	 */
	private static final int[] ID = { 637 };

	/**
	 * Constructs a new {@code AlKharidWarriorPlugin} {@code Object}.
	 */
	public JulietNPC() {
		super(0, null);
	}

	/**
	 * Constructs a new {@code AlKharidWarriorPlugin} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	private JulietNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new JulietNPC(id, location);
	}

	@Override
	public boolean isHidden(final Player player) {
		return player.getQuestRepository().getQuest("Romeo & Juliet").getStage(player) > 60 && player.getQuestRepository().getQuest("Romeo & Juliet").getStage(player) < 100;
	}

	@Override
	public int[] getIds() {
		return ID;
	}

}
