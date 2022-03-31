package core.game.node.entity.npc.revenant;

import java.util.ArrayList;
import java.util.List;

import core.game.node.entity.npc.NPC;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;
import core.tools.RandomFunction;

/**
 * Handles the revenants.
 * @author Vexia
 */
@Initializable
public class RevenantPlugin implements Plugin<Object> {

	/**
	 * The revenants npc.
	 */
	private static final List<NPC> REVENANTS = new ArrayList<>(20);

	/**
	 * The spawning locations.
	 */
	private static final Location[] SPAWN_LOCATIONS = new Location[] { new Location(2968, 3695), new Location(2956, 3716), new Location(2969, 3753), new Location(2967, 3817), new Location(2976, 3849), new Location(2976, 3725), new Location(2973, 3576), new Location(2982, 3850), new Location(3348, 3822), new Location(3362, 3808), new Location(3026, 3734), new Location(2961, 3792), new Location(2984, 3801), new Location(2974, 3744), new Location(2984, 3718), new Location(2990, 3695), new Location(2960, 3590), new Location(3020, 3674), new Location(3019, 3723), new Location(3019, 3822), Location.create(3123, 3567, 0), Location.create(3123, 3567, 0), Location.create(3201, 3678, 0), Location.create(3220, 3755, 0), Location.create(3249, 3882, 0), Location.create(3283, 3893, 0), Location.create(3034, 3938, 0), Location.create(2964, 3617, 0), Location.create(3253, 3922, 0), Location.create(3205, 3907, 0), Location.create(3194, 3895, 0), Location.create(3168, 3788, 0), Location.create(3287, 3557, 0), Location.create(3327, 3558, 0), Location.create(3364, 3536, 0), Location.create(3262, 3595, 0), Location.create(3099, 3957, 0), Location.create(3028, 3915, 0), Location.create(3285, 3922, 0), Location.create(2980, 3855, 0), Location.create(3243, 3917, 0), Location.create(3190, 3630, 0), Location.create(3188, 3585, 0), Location.create(3117, 3590, 0), Location.create(3136, 3624, 0), Location.create(3356, 3701, 0) };

	/**
	 * The maximum amount of revenants spawned.
	 */
	private static final int MAX = 20;

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ClassScanner.definePlugin(new RevenantNPC());
		//CorruptEquipment.init();
		//PVPEquipment.init();
		spawn();
		return this;
	}

	/**
	 * Spawns the revenants.
	 */
	public static void spawn() {
		int size = REVENANTS.size();
		int left = MAX - size;
		List<Location> taken = new ArrayList<>(20);
		for (NPC n : REVENANTS) {
			taken.add(n.getProperties().getSpawnLocation());
		}
		if (left > 0) {
			int spawnAmount = RandomFunction.random(1, left);
			if (size == 0) {
				spawnAmount = MAX;
			}
			for (int i = 0; i < spawnAmount; i++) {
				Location loc = null;
				while (loc == null) {
					Location l = RandomFunction.getRandomElement(SPAWN_LOCATIONS);
					if (taken.contains(l)) {
						continue;
					}
					loc = l;
				}
				taken.add(loc);
				RevenantType type = RandomFunction.getRandomElement(RevenantType.values());
				int id = type.getIds()[0];
				NPC revenant = NPC.create(id, loc);
				revenant.init();
			}
		}
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	/**
	 * Gets the revenants.
	 * @return the revenants
	 */
	public static List<NPC> getRevenants() {
		return REVENANTS;
	}

}
