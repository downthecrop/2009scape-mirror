package plugin.npc.other;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.diary.DiaryType;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.world.map.zone.ZoneBorders;

/**
 * Represents a Jogre NPC.
 * @author Vexia
 */
@InitializablePlugin
public class JogreNPC extends AbstractNPC {

	/**
	 * Represents the potzole zone borders.
	 */
	private static final ZoneBorders POTHOLE_ZONE = new ZoneBorders(2818, 9463, 2902, 9537);

	/**
	 * If the jogre is in the pothole zone.
	 */
	private boolean inPothole;

	/**
	 * Constructs a new {@code JogreNPC} {@code Object}
	 * @param id the id.
	 * @param location the location.
	 */
	public JogreNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public void init() {
		super.init();
		inPothole = POTHOLE_ZONE.insideBorder(this);
	}

	/**
	 * Constructs a new {@code JogreNPC} {@code Object}
	 */
	public JogreNPC() {
		this(-1, null);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new JogreNPC(id, location);
	}

	@Override
	public void finalizeDeath(Entity killer) {
		if (inPothole && killer.isPlayer()) {
			Player player = killer.asPlayer();
			if (!player.getAchievementDiaryManager().hasCompletedTask(DiaryType.KARAMJA, 0, 9)) {
				player.getAchievementDiaryManager().updateTask(player, DiaryType.KARAMJA, 0, 9, true);
			}
		}
		super.finalizeDeath(killer);
	}

	@Override
	public int[] getIds() {
		return new int[] { 113 };
	}

}
