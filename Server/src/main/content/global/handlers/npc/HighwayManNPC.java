package content.global.handlers.npc;

import core.game.node.entity.Entity;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.plugin.Initializable;
import core.game.world.map.Location;

/**
 * Represents the highway man npc.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class HighwayManNPC extends AbstractNPC {

	/**
	 * Constructs a new {@code HighwayManNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	public HighwayManNPC(int id, Location location) {
		super(id, location);
	}

	/**
	 * Constructs a new {@code HighwayManNPC} {@code Object}.
	 */
	public HighwayManNPC() {
		super(0, null);
	}

	@Override
	public void finalizeDeath(Entity killer) {
		super.finalizeDeath(killer);
		if (getId() == 180) {
			if (killer instanceof Player) {
				final Player player = killer.asPlayer();
				if (!player.getAchievementDiaryManager().getDiary(DiaryType.FALADOR).isComplete(0, 10)) {
					player.getAchievementDiaryManager().getDiary(DiaryType.FALADOR).updateTask(player, 0, 10, true);
				}
			}
		}
	}

	@Override
	public void onAttack(Entity target) {
		sendChat("Stand and deliver!");
	}

	@Override
	public int[] getIds() {
		return new int[] { 180, 2677 };
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new HighwayManNPC(id, location);
	}

}
