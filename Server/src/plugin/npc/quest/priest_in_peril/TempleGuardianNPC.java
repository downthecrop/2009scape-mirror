package plugin.npc.quest.priest_in_peril;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.quest.Quest;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.world.map.Location;

/**
 * Handles the temple guardian npc.
 * @author 'Vexia
 */
@InitializablePlugin
public class TempleGuardianNPC extends AbstractNPC {

	/**
	 * The NPC ids of NPCs using this plugin.
	 */
	private static final int[] ID = { 7711 };

	/**
	 * Constructs a new {@code AlKharidWarriorPlugin} {@code Object}.
	 */
	public TempleGuardianNPC() {
		super(0, null);
	}

	/**
	 * Constructs a new {@code AlKharidWarriorPlugin} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	private TempleGuardianNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new TempleGuardianNPC(id, location);
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public void finalizeDeath(final Entity killer) {
		super.finalizeDeath(killer);
		final Player p = ((Player) killer);
		final Quest quest = p.getQuestRepository().getQuest("Priest in Peril");
		if (quest.getStage(p) == 11) {
			quest.setStage(p, 12);
		}
	}

	@Override
	public int[] getIds() {
		return ID;
	}

}
