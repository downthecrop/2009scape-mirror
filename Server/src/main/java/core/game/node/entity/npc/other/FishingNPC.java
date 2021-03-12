package core.game.node.entity.npc.other;

import core.game.node.entity.skill.fishing.FishSpots;
import core.game.node.entity.npc.AbstractNPC;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Represents the fishing npc.
 * @author Vexia
 * 
 */
@Initializable
public final class FishingNPC extends AbstractNPC {

	/**
	 * Represents the fishing spot.
	 */
	private FishSpots spot;

	/**
	 * Represents the delay of switching.
	 */
	private int switchDelay;

	/**
	 * Constructs a new {@code FishingNPC} {@code Object}.
	 */
	public FishingNPC() {
		super(0, null);
		spot = null;
	}

	/**
	 * Constructs a new {@code FishingNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	public FishingNPC(int id, Location location) {
		super(id, location, false);
		switchDelay = getRandomDelay();
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new FishingNPC(id, location);
	}

	@Override
	public void handleTickActions() {
		if (spot == null) {
			return;
		}
		if (switchDelay < GameWorld.getTicks()) {
			moveSpot();
		}
	}
	
	@Override
	public void init() {
		super.init();
		spot = FishSpots.forLocation(getLocation());
	}

	/**
	 * Method used to move a spot.
	 */
	public void moveSpot() {
		if (spot == null) {
			if (isInvisible()) {
				setInvisible(false);
			} else {
				setInvisible(true);
			}
			switchDelay = GameWorld.getTicks() + RandomFunction.random(200, 390);
			return;
		}
		if (spot == FishSpots.TUTORIAL_ISLAND) {
			return;
		}
		Location rand = spot.getLocations()[RandomFunction.random(spot.getLocations().length)];
		if (RegionManager.getLocalNpcs(rand, 0).size() == 0) {
			getProperties().setTeleportLocation(rand);
		}
		switchDelay = GameWorld.getTicks() + getRandomDelay();
	}

	/**
	 * Gets the random delay.
	 * @return the delay.
	 */
	private int getRandomDelay() {
		return RandomFunction.random(200, 390);
	}

	@Override
	public int[] getIds() {
		return new int[] { 316, 319, 320, 323, 325, 326, 327, 330, 332, 404, 952, 1174, 1175, 1331, 2067, 2068, 2724, 4908, 5748, 5749, 7045, 313, 322, 334, 406, 1191, 1333, 1405, 1406, 3574, 3575, 3848, 3849, 5471, 7044, 233, 234, 235, 236, 309, 310, 311, 314, 315, 317, 318, 328, 329, 331, 403, 800, 927, 1189, 1190, 1236, 1237, 1238, 3019, 7636, 312, 321, 324, 333, 405, 1332, 1399, 3804, 5470, 7046 };
	}

}
