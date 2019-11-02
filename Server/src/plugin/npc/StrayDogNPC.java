package plugin.npc;

import java.util.ArrayList;
import java.util.List;

import org.crandor.game.interaction.DestinationFlag;
import org.crandor.game.interaction.MovementPulse;
import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.map.path.Pathfinder;
import org.crandor.game.world.map.zone.impl.BankZone;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Handles the temple guardian npc.
 * @author 'Vexia
 */
@InitializablePlugin
public class StrayDogNPC extends AbstractNPC {

	/**
	 * The NPC ids of NPCs using this plugin.
	 */
	private static final int[] ID = { 5918, 5917 };

	/**
	 * Represents the target player.
	 */
	private Player target;

	/**
	 * Represents the delay.
	 */
	private long delay;

	/**
	 * Represents the array list of players.
	 */
	private List<Player> players = new ArrayList<>();

	/**
	 * Constructs a new {@code AlKharidWarriorPlugin} {@code Object}.
	 */
	public StrayDogNPC() {
		super(0, null, true);
	}

	/**
	 * Constructs a new {@code AlKharidWarriorPlugin} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	private StrayDogNPC(int id, Location location) {
		super(id, location, true);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new StrayDogNPC(id, location);
	}

	@Override
	public void tick() {
		super.tick();
		if (delay < System.currentTimeMillis() && RandomFunction.random(1, 16) == 2) {
			getPulseManager().clear();
			target = null;
			players = RegionManager.getLocalPlayers(this, 7);
			if (players.size() != 0) {
				target = players.get(RandomFunction.random(players.size()));
				getPulseManager().run(getFollowPulse(target), "movement");
				delay = System.currentTimeMillis() + 150000;
			}
		}
		if (target != null && target.getZoneMonitor().isInZone("bank")) {
			Pathfinder.find(this, getProperties().getSpawnLocation()).walk(this);
			getPulseManager().clear();
			target = null;
			delay = System.currentTimeMillis() + 150000;
		}
	}

	@Override
	public Location getMovementDestination() {
		return super.getMovementDestination();
	}

	@Override
	public boolean canMove(Location l) {
		if (BankZone.VARROCK_EAST.insideBorder(l) || BankZone.VARROCK_WEST.insideBorder(l)) {
			return false;
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return ID;
	}

	/**
	 * Gets the following pulse.
	 * @param target the target.
	 * @return the movement pulse.
	 */
	public MovementPulse getFollowPulse(final Player target) {
		return new MovementPulse(this, target, DestinationFlag.FOLLOW_ENTITY) {
			@Override
			public boolean pulse() {
				return false;
			}
		};
	}

	@Override
	public int getWalkRadius() {
		return 17;
	}
}
