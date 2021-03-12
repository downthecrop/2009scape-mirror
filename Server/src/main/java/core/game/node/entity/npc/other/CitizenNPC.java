package core.game.node.entity.npc.other;

import core.plugin.Initializable;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.npc.AbstractNPC;
import core.game.world.map.Location;

/**
 * Represents a generic citizen npc.
 * @author 'Vexia
 * @version 1.9
 */
@Initializable
public final class CitizenNPC extends AbstractNPC {

	/**
	 * Represents the NPC ids of NPCs using this plugin.
	 */
	private static final int[] ID = { 1, 2, 3, 4, 5, 6, 16, 24, 25, 170, 351, 352, 353, 354, 359, 360, 361, 362, 363, 663, 726, 727, 728, 729, 730, 1086, 2675, 2776, 3224, 3225, 3227, 5923, 5924 };

	/**
	 * Constructs a new {@code CitizenNPC} {@code Object}.
	 */
	public CitizenNPC() {
		super(0, null, true);
	}

	/**
	 * Constructs a new {@code CitizenNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	private CitizenNPC(int id, Location location) {
		super(id, location, true);
	}

	@Override
	public void init() {
		super.init();
		getSkills().setLevel(Skills.HITPOINTS, 7);
		getSkills().setStaticLevel(Skills.HITPOINTS, 7);
		getSkills().setLifepoints(7);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new CitizenNPC(id, location);
	}

	@Override
	public int[] getIds() {
		return ID;
	}

}
