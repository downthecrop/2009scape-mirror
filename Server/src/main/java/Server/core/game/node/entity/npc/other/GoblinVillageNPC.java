package core.game.node.entity.npc.other;

import java.util.List;

import core.game.node.entity.Entity;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.npc.NPC;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Represents the goblin village npc.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class GoblinVillageNPC extends AbstractNPC {

	/**
	 * The NPC ids of NPCs using this plugin.
	 */
	private static final int[] ID = { /** r */
	4483, /** g */
	4488, /** g */
	4489, /** r */
	4484, /** g */
	4491, /** r */
	4485, /** g */
	4486, /** g */
	4492, /** g */
	4487, /** r */
	4481, /** r */
	4479, /** r */
	4482, /** r */
	4480 };

	/**
	 * Represents the red goblins.
	 */
	private static final int[] RED_GOBLINS = new int[] { 4483, 4484, 4485, 4481, 4479, 4482, 4480 };

	/**
	 * Represents the green goblins.
	 */
	private static final int GREEN_GOBLINS[] = new int[] { 4488, 4489, 4491, 4486, 4492, 4487 };

	/**
	 * Represents the red messages.
	 */
	private static final String[] RED_MESSAGES = new String[] { "Red armour best!", "Green armour stupid!", "Red!", "Red not green!", "Stupid greenie!" };

	/**
	 * Represents the green messages.
	 */
	private static final String[] GREEN_MESSAGES = new String[] { "Green armour best!", "Green!", "Stupid reddie!" };

	/**
	 * Represents the delay.
	 */
	private long delay = 0L;

	/**
	 * Represents if the goblin is green.
	 */
	private boolean green = true;

	/**
	 * Constructs a new {@code GoblinVillageNPC} {@code Object}.
	 */
	public GoblinVillageNPC() {
		super(0, null);
	}

	/**
	 * Constructs a new {@code GoblinVillageNPC} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	private GoblinVillageNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new GoblinVillageNPC(id, location);
	}

	@Override
	public void init() {
		super.init();
		for (int i : RED_GOBLINS) {
			if (getId() == i) {
				green = false;
			}
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (delay < System.currentTimeMillis() && !getProperties().getCombatPulse().isAttacking()) {
			final int rand = RandomFunction.random(1, 4);
			if (rand == 2) {
				final List<NPC> surronding = RegionManager.getLocalNpcs(this, 10);
				for (NPC n : surronding) {
					if (n.getId() == getId()) {
						continue;
					}
					if (n.getProperties().getCombatPulse().isAttacking()) {
						continue;
					}
					for (int i : green ? RED_GOBLINS : GREEN_GOBLINS) {
						if (n.getId() == i) {
							n.lock(5);
							getProperties().getCombatPulse().attack(n);
							break;
						}
					}
				}
			}
			delay = System.currentTimeMillis() + 5000;// 15000;
		} else {
			final int rand = RandomFunction.random(3);
			if (rand != 1) {
				return;
			}
			final Entity oponent = getProperties().getCombatPulse().getVictim();
			if (oponent == null) {
				return;
			}
			if (!(oponent instanceof NPC)) {
				return;
			}
			if (oponent.getLocation().getDistance(getLocation()) > 4) {
				return;
			}
			boolean goblin = false;
			for (int i : green ? RED_GOBLINS : GREEN_GOBLINS) {
				if (i == ((NPC) oponent).getId()) {
					goblin = true;
					break;
				}
			}
			if (goblin && RandomFunction.random(0, 3) == 2) {
				sendChat(green ? GREEN_MESSAGES[RandomFunction.random(GREEN_MESSAGES.length)] : RED_MESSAGES[RandomFunction.random(RED_MESSAGES.length)]);
			}
		}
	}

	@Override
	public void finalizeDeath(Entity killer) {
		super.finalizeDeath(killer);
	}

	@Override
	public int[] getIds() {
		return ID;
	}

}
