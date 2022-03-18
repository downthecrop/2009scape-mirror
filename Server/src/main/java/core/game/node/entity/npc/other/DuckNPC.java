package core.game.node.entity.npc.other;

import core.game.node.entity.Entity;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Represents the a duck npc.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class DuckNPC extends AbstractNPC {

	/**
	 * Represents the force chat to use.
	 */
	private static final String FORCE_CHAT = "Quack!";

	/**
	 * Constructs a new {@code DuckNPC} {@code Object}.
	 */
	public DuckNPC() {
		super(0, null);
	}

	/**
	 * Constructs a new {@code DuckNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	public DuckNPC(int id, Location location) {
		super(id, location, true);
	}

	@Override
	public void tick() {
		if (RandomFunction.random(45) == 5) {
			sendChat(FORCE_CHAT);
		}
		super.tick();
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new DuckNPC(id, location);
	}

	@Override
	public int[] getIds() {
		return new int[] { 46, 2693, 6113 };
	}

}
