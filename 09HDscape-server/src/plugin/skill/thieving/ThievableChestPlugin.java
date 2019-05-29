package plugin.skill.thieving;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.global.action.DoorActionHandler;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.node.object.ObjectBuilder;
import org.crandor.game.system.task.Pulse;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.map.Direction;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;
import org.crandor.tools.RandomFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the thieving of chests.
 * @author Vexia
 */
@InitializablePlugin
public final class ThievableChestPlugin extends OptionHandler {

	/**
	 * The doors to pick.
	 */
	private static final int[] DOORS = new int[] { 2550, 2551, 2554, 2555, 2556, 2557, 2558, 2559, 5501, 7246, 9565, 13314, 13317, 13320, 13323, 13326, 13344, 13345, 13346, 13347, 13348, 13349, 15759, 34005, 34805, 34806, 34812 };

	/**
	 * The list of pickable doors.
	 */
	private static final List<PickableDoor> pickableDoors = new ArrayList<>();

	/**
	 * The lock pick item.
	 */
	private static final Item LOCK_PICK = new Item(1523);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (int i : DOORS) {
			ObjectDefinition.forId(i).getConfigurations().put("option:pick-lock", this);
			ObjectDefinition.forId(i).getConfigurations().put("option:open", this);
		}
		for (Chest chest : Chest.values()) {
			for (int id : chest.getObjectIds()) {
				ObjectDefinition def = ObjectDefinition.forId(id);
				def.getConfigurations().put("option:open", this);
				def.getConfigurations().put("option:search for traps", this);
			}
		}
		pickableDoors.add(new PickableDoor(new Location[] { Location.create(2672, 3308, 0) }, 1, 3.8));
		pickableDoors.add(new PickableDoor(new Location[] { Location.create(2672, 3301, 0) }, 14, 15));
		pickableDoors.add(new PickableDoor(new Location[] { Location.create(2610, 3316, 0) }, 15, 15));
		pickableDoors.add(new PickableDoor(new Location[] { Location.create(3190, 3957, 0) }, 32, 25, true));
		pickableDoors.add(new PickableDoor(new Location[] { Location.create(2565, 3356, 0) }, 46, 37.5));
		pickableDoors.add(new PickableDoor(new Location[] { Location.create(2579, 3286, 1) }, 61, 50));
		pickableDoors.add(new PickableDoor(new Location[] { Location.create(3018, 3187, 0) }, 1, 0.0));
		pickableDoors.add(new PickableDoor(new Location[] { Location.create(2601, 9482, 0) }, 82, 0.0, true));
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final Chest chest = Chest.forId(node.getId());
		PickableDoor door = null;
		if (chest == null) {
			door = forDoor(node.getLocation());
			if (door == null) {
				player.getPacketDispatch().sendMessage("The door is locked.");
				return true;
			}
		}
		switch (option) {
		case "open":
			if (chest != null) {
				chest.open(player, (GameObject) node);
				return true;
			}
			door.open(player, (GameObject) node);
			return true;
		case "pick-lock":
			door.pickLock(player, (GameObject) node);
			return true;
		case "search for traps":
			chest.searchTraps(player, (GameObject) node);
			return true;
		}
		return true;
	}

	/**
	 * Gets a pickable door.
	 * @param location the location.
	 * @return the door.
	 */
	private PickableDoor forDoor(Location location) {
		for (PickableDoor door : pickableDoors) {
			for (Location l : door.getLocations()) {
				if (l.equals(location)) {
					return door;
				}
			}
		}
		return null;
	}

	@Override
	public Location getDestination(Node node, Node n) {
		if (n instanceof GameObject) {
			GameObject object = (GameObject) n;
			if (object.getDefinition().hasAction("pick-lock")) {
				return DoorActionHandler.getDestination((Entity) node, object);
			}
		}
		return null;
	}

	/**
	 * Represents a pickable door.
	 * @author Vexia
	 */
	public class PickableDoor {

		/**
		 * The locations of the door.
		 */
		private final Location[] locations;

		/**
		 * The level.
		 */
		private final int level;

		/**
		 * The experience required.
		 */
		private final double experience;

		/**
		 * If it requires a lockpick.
		 */
		private final boolean lockpick;

		/**
		 * Constructs a new {@code PickableDoor} {@code Object}.
		 * @param locations the locations.
		 * @param level the level.
		 * @param experience the experience.
		 * @param lockpick the lock pick.
		 */
		public PickableDoor(final Location[] locations, int level, double experience, boolean lockpick) {
			this.locations = locations;
			this.level = level;
			this.experience = experience;
			this.lockpick = lockpick;
		}

		/**
		 * Constructs a new {@code PickableDoor} {@code Object}.
		 * @param location the locations.
		 * @param level the level.
		 * @param experience the experience.
		 */
		public PickableDoor(Location[] locations, int level, double experience) {
			this(locations, level, experience, false);
		}

		/**
		 * Gets the location.
		 * @return The location.
		 */
		public Location[] getLocations() {
			return locations;
		}

		/**
		 * Opens a pickable door.
		 * @param player the player.
		 * @param object the object.
		 */
		public void open(Player player, GameObject object) {
			if (isInside(player, object)) {
				DoorActionHandler.handleAutowalkDoor(player, object);
				player.getPacketDispatch().sendMessage("You go through the door.");
			} else {
				player.getPacketDispatch().sendMessage("The door is locked.");
			}
		}

		/**
		 * Picks a lock on a door.
		 * @param player the player.
		 * @param object the object.
		 */
		public void pickLock(Player player, GameObject object) {
			boolean success = RandomFunction.random(12) >= 4;
			if (isInside(player, object)) {
				player.getPacketDispatch().sendMessage("The door is already unlocked.");
				return;
			}
			if (player.getSkills().getLevel(Skills.THIEVING) < level) {
				player.sendMessage("You attempt to pick the lock.");
				boolean hit = RandomFunction.random(10) < 5;
				player.getImpactHandler().manualHit(player, RandomFunction.random(1, 3), HitsplatType.NORMAL);
				player.sendMessage(hit ? "You have activated a trap on the lock." : "You fail to pick the lock.");
				return;
			}
			if (lockpick && !player.getInventory().containsItem(LOCK_PICK)) {
				player.sendMessage("You need a lockpick in order to pick this lock.");
				return;
			}
			if (success) {
				player.getSkills().addExperience(Skills.THIEVING, experience, true);
				DoorActionHandler.handleAutowalkDoor(player, object);
			}
			player.getPacketDispatch().sendMessage("You attempt to pick the lock.");
			player.getPacketDispatch().sendMessage("You " + (success ? "manage" : "fail") + " to pick the lock.");
		}

		/**
		 * Checks if we're behind the door/inside the building.
		 * @param player the player.
		 * @param object the object.
		 * @return {@code True} if so.
		 */
		private boolean isInside(Player player, GameObject object) {
			boolean inside = false;
			Direction dir = Direction.getLogicalDirection(player.getLocation(), object.getLocation());
			Direction direction = object.getDirection();
			if (direction == Direction.SOUTH && dir == Direction.WEST) {
				inside = true;
			} else if (direction == Direction.EAST && dir == Direction.SOUTH) {
				inside = true;
			} else if (direction == Direction.NORTH && dir == Direction.EAST) {
				inside = true;
			}
			return inside;
		}

		/**
		 * Gets the level.
		 * @return The level.
		 */
		public int getLevel() {
			return level;
		}

		/**
		 * Gets the experience.
		 * @return The experience.
		 */
		public double getExperience() {
			return experience;
		}

		/**
		 * Gets the lockpick.
		 * @return The lockpick.
		 */
		public boolean isLockpick() {
			return lockpick;
		}

	}

	/**
	 * Represents a thievable chest.
	 * @author Vexia
	 */
	public static enum Chest {
		TEN_COIN(2566, 13, 7.8, new Item[] { new Item(995, 10) }, 7), NATURE_RUNE(2567, 28, 25, new Item[] { new Item(995, 3), new Item(561, 1) }, 8), FIFTY_COIN(2568, 43, 125, new Item[] { new Item(995, 50) }, 55), STEEL_ARROWHEADS(2573, 47, 150, new Item[] { new Item(41, 5) }, 210), BLOOD_RUNES(2569, 59, 250, new Item[] { new Item(995, 500), new Item(565, 2) }, 135), PALADIN(2570, 72, 500, new Item[] { new Item(995, 1000), new Item(383, 1), new Item(449, 1), new Item(1623, 1) }, 120);

		/**
		 * The object id.
		 */
		private final int[] objectIds;

		/**
		 * The level required.
		 */
		private final int level;

		/**
		 * The experience gained.
		 */
		private final double experience;

		/**
		 * The rewards.
		 */
		private final Item[] rewards;

		/**
		 * The respawn time.
		 */
		private final int respawn;

		/**
		 * The current respawn time.
		 */
		private int currentRespawn;

		/**
		 * Constructs a new {@code Chest} {@code Object}.
		 * @param objectId the object id.
		 * @param level the level.
		 * @param experience the experience.
		 * @param rewards the rewards.
		 * @param respawn the respawn time.
		 */
		private Chest(int[] objectIds, int level, double experience, Item[] rewards, int respawn) {
			this.objectIds = objectIds;
			this.level = level;
			this.experience = experience;
			this.rewards = rewards;
			this.respawn = respawn;
		}

		/**
		 * Constructs a new {@code Chest} {@code Object}.
		 * @param objectId the object id.
		 * @param level the level.
		 * @param experience the experience.
		 * @param rewards the rewards.
		 * @param respawn the respawn time.
		 */
		private Chest(int objectId, int level, double experience, Item[] rewards, int respawn) {
			this(new int[] { objectId }, level, experience, rewards, respawn);
		}

		/**
		 * Opens the chest for a reward.
		 * @param player the player.
		 * @param object the object.
		 */
		private void open(final Player player, final GameObject object) {
			if (isRespawning()) {
				player.sendMessage("It looks like this chest has already been looted.");
				return;
			}
			player.lock(2);
			player.sendMessage("You have activated a trap on the chest.");
			player.getImpactHandler().manualHit(player, getHitAmount(player), HitsplatType.NORMAL);
		}

		/**
		 * Searches for traps on a chest.
		 * @param player the player.
		 * @param object the object.
		 */
		private void searchTraps(final Player player, final GameObject object) {
			player.faceLocation(object.getLocation());
			if (isRespawning()) {
				player.sendMessage("It looks like this chest has already been looted.");
				return;
			}
			player.lock();
			player.animate(Animation.create(536));
			if (player.getSkills().getLevel(Skills.THIEVING) < level) {
				player.lock(2);
				player.sendMessage("You search the chest for traps.");
				player.sendMessage("You find nothing.", 1);
				return;
			}
			if (player.getInventory().freeSlots() == 0) {
				player.getPacketDispatch().sendMessage("Not enough inventory space.");
				return;
			}
			player.sendMessage("You find a trap on the chest...");
			player.getImpactHandler().setDisabledTicks(6);
			GameWorld.submit(new Pulse(1, player) {
				int counter;

				@Override
				public boolean pulse() {
					switch (++counter) {
					case 2:
						player.sendMessage("You disable the trap.");
						break;
					case 4:
						player.animate(Animation.create(536));
						player.sendMessage("You open the chest.");
						break;
					case 6:
						player.unlock();
						for (Item i : rewards) {
							player.getInventory().add(i, player);
						}
						player.sendMessage("You find treasure inside!");
						player.getSkills().addExperience(Skills.THIEVING, experience, true);
						if (object.isActive()) {
							ObjectBuilder.replace(object, object.transform(2574), 3);
						}
						setRespawn();
						return true;
					}
					return false;
				}
			});
		}

		/**
		 * Sets the respawn delay.
		 */
		public void setRespawn() {
			currentRespawn = GameWorld.getTicks() + (int) (respawn / 0.6);
		}

		/**
		 * Checks if the chest is respawning.
		 * @return {@code True} if so.
		 */
		public boolean isRespawning() {
			return currentRespawn > GameWorld.getTicks();
		}

		/**
		 * Gets the amount of damage to deal.
		 * @param player The player.
		 * @return The amount of damage.
		 */
		protected static int getHitAmount(Player player) {
			int hit = player.getSkills().getLifepoints() / 12;
			if (hit < 2) {
				hit = 2;
			}
			return hit;
		}

		/**
		 * Gets a chest by the id.
		 * @param id the id.
		 * @return the chest.
		 */
		public static Chest forId(int id) {
			for (Chest chest : values()) {
				for (int i : chest.getObjectIds()) {
					if (i == id) {
						return chest;
					}
				}
			}
			return null;
		}

		/**
		 * Gets the objectId.
		 * @return The objectId.
		 */
		public int[] getObjectIds() {
			return objectIds;
		}

		/**
		 * Gets the level.
		 * @return The level.
		 */
		public int getLevel() {
			return level;
		}

		/**
		 * Gets the experience.
		 * @return The experience.
		 */
		public double getExperience() {
			return experience;
		}

		/**
		 * Gets the rewards.
		 * @return The rewards.
		 */
		public Item[] getRewards() {
			return rewards;
		}

		/**
		 * Gets the respawn.
		 * @return The respawn.
		 */
		public int getRespawn() {
			return respawn;
		}

	}
}
