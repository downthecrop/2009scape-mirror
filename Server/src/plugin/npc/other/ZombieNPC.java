package plugin.npc.other;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.diary.AchievementDiary;
import org.crandor.game.node.entity.player.link.diary.DiaryType;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.world.map.Location;

/**
 * Handles the zombie npc.
 * @author Vexia
 *
 */
@InitializablePlugin
public class ZombieNPC extends AbstractNPC {

	/**
	 * Constructs the {@code ZombieNPC}
	 */
	public ZombieNPC() {
		super(-1, null);
	}
	
	/**
	 * Constructs the {@code ZombieNPC} 
	 * @param id The id.
	 * @param location The location.
	 */
	public ZombieNPC(int id, Location location) {
		super(id, location);
	}
	
	@Override
	public void finalizeDeath(Entity killer) {
		super.finalizeDeath(killer);
		if (killer instanceof Player) {
			Player p = killer.asPlayer();
			AchievementDiary diary = p.getAchievementDiaryManager().getDiary(DiaryType.LUMBRIDGE);
			if (!diary.isComplete(0, 5) && (p.getViewport().getRegion().getId() == 12438 || p.getViewport().getRegion().getId() == 12439) ) {
				diary.updateTask(p, 0, 5, true);
			}
		}
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new ZombieNPC(id, location);
	}

	@Override
	public int[] getIds() {
		return new int[] {73};
	}

}
