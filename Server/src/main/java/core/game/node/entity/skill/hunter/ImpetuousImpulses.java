package core.game.node.entity.skill.hunter;

import api.StartupListener;
import core.game.node.entity.skill.hunter.bnet.BNetTypes;
import core.game.node.entity.skill.hunter.bnet.ImplingNode;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.tools.RandomFunction;

import java.util.Random;

/**
 * Handles the impetuous impulse minigame.
 * @author Vexia
 * @author Emperor
 */
public final class ImpetuousImpulses implements StartupListener {

	/**
	 * The random instance used.
	 */
	private static final Random RANDOM = new Random();

	/**
	 * The puro puro locations.
	 */
	private static final Location[] PURO_LOCATIONS = new Location[] { new Location(2575, 4314, 0), new Location(2572, 4302, 0), new Location(2575, 4297, 0), new Location(2589, 4297, 0), new Location(2603, 4297, 0), new Location(2612, 4301, 0), new Location(2619, 4296, 0), new Location(2616, 4297, 0), new Location(2620, 4308, 0), new Location(2617, 4313, 0), new Location(2620, 4317, 0), new Location(2611, 4312, 0), new Location(2607, 4305, 0), new Location(2593, 4303, 0), new Location(2578, 4318, 0), new Location(2578, 4330, 0), new Location(2581, 4338, 0), new Location(2586, 4342, 0), Location.create(2585, 4344, 0), Location.create(2573, 4335, 0), Location.create(2570, 4325, 0), Location.create(2567, 4313, 0), Location.create(2566, 4299, 0), Location.create(2573, 4292, 0), Location.create(2603, 4342, 0), Location.create(2616, 4318, 0), Location.create(2616, 4337, 0), Location.create(2584, 4297, 0), Location.create(2596, 4310, 0), Location.create(2596, 4310, 0), Location.create(2588, 4303, 0) };

	/**
	 * The possible locations.
	 */
	public static final Location[] LOCATIONS = new Location[] {
	/*
	 * Camelot
	 */
	Location.create(2718 + RANDOM.nextInt(50), 3441 + RANDOM.nextInt(20), 0),
	/*
	 * Duel arena entrance
	 */
	Location.create(3300 + RANDOM.nextInt(10), 3250 + RANDOM.nextInt(10), 0),
	/*
	 * Castle wars entrance
	 */
	Location.create(2478 + RANDOM.nextInt(20), 3068 + RANDOM.nextInt(20), 0),
	/*
	 * Lumbridge
	 */
	Location.create(3175 + RANDOM.nextInt(10), 3261 + RANDOM.nextInt(10), 0),
	/*
	 * Draynor
	 */
	Location.create(3092 + RANDOM.nextInt(5), 3227 + RANDOM.nextInt(5), 0),
	/*
	 * Falador (east)
	 */
	Location.create(3071 + RANDOM.nextInt(10), 3355 + RANDOM.nextInt(10), 0),
	/*
	 * Varrock (west)
	 */
	Location.create(3131 + RANDOM.nextInt(5), 3401 + RANDOM.nextInt(5), 0),
	/*
	 * Ardougne (south)
	 */
	Location.create(2630 + RANDOM.nextInt(10), 3217 + RANDOM.nextInt(10), 0),
	/*
	 * Yanille (north)
	 */
	Location.create(2594 + RANDOM.nextInt(20), 3121 + RANDOM.nextInt(20), 0),
	/*
	 * Red salamander hunting place (ZMI altar)
	 */
	Location.create(2436 + RANDOM.nextInt(15), 3206 + RANDOM.nextInt(15), 0),
	/*
	 * Shilo hunting area (graahk/larupia/..)
	 */
	Location.create(2763 + RANDOM.nextInt(20), 3003 + RANDOM.nextInt(20), 0),
	/*
	 * Rimmington
	 */
	Location.create(2947 + RANDOM.nextInt(20), 3219 + RANDOM.nextInt(20), 0),
	/*
	 * Karamja volcano
	 */
	Location.create(2856 + RANDOM.nextInt(15), 3150 + RANDOM.nextInt(15), 0),
	/*
	 * Brimhaven dungeon entrance
	 */
	Location.create(2733 + RANDOM.nextInt(10), 3146 + RANDOM.nextInt(10), 0),
	/*
	 * Sophanem
	 */
	Location.create(3299 + RANDOM.nextInt(5), 2784 + RANDOM.nextInt(5), 0),
	/*
	 * Tree gnome stronghold
	 */
	Location.create(2438 + RANDOM.nextInt(20), 3420 + RANDOM.nextInt(20), 0),
	/*
	 * Zanaris (center, just north of grain field)
	 */
	Location.create(2416 + RANDOM.nextInt(3), 4456 + RANDOM.nextInt(3), 0), };

	/**
	 * Spawns the imps.
	 */
	private void spawnImps() {
		int amount = BNetTypes.getImplings().size() + 2;
		for (ImplingNode impling : BNetTypes.getImplings()) {
			if (impling.getLevel() == 83) {
				int rand = RandomFunction.random(1, 2);
				for (int i = 0; i < rand; i++) {
					createImp(impling, true);
				}
				int randAmt = RandomFunction.random(2, 5);
				for (int i = 0; i < randAmt; i++) {
					createImp(impling, false);
				}
				continue;
			}
			for (int i = 0; i < amount; i++) {
				if (RANDOM.nextInt(10) < 4) {
					createImp(impling, false);
					continue;
				}
				createImp(impling, true);
			}
			amount--;
		}
	}

	/**
	 * Creates the impling.
	 * @param impling the impling.
	 * @param puro the puro.
	 */
	private void createImp(ImplingNode impling, boolean puro) {
		ImplingNPC npc = null;
		if (puro) {
			npc = new ImplingNPC(impling.getNpcs()[1], PURO_LOCATIONS[RANDOM.nextInt(PURO_LOCATIONS.length)], impling);
			npc.setAttribute("puroPuro", true);
		} else {
			npc = new ImplingNPC(impling.getNpcs()[0], LOCATIONS[RANDOM.nextInt(LOCATIONS.length)], impling);
			npc.setAttribute("nextTeleport", GameWorld.getTicks() + 600);
		}
		npc.init();
	}

	@Override
	public void startup() {
		spawnImps();
	}

}
