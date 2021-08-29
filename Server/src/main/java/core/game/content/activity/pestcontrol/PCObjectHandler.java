package core.game.content.activity.pestcontrol;

import core.cache.def.impl.SceneryDefinition;
import rs09.game.world.GameWorld;
import core.game.content.activity.ActivityManager;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import rs09.game.ai.pvmbots.PvMBotsBuilder;
import core.game.node.entity.player.info.Rights;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;

import java.util.ArrayList;

/**
 * Handles pest control objects.
 * @author Sir Kermit & Emperor
 */
public final class PCObjectHandler extends OptionHandler {

	//public boolean pcbotsSpawned = false;
	public boolean PCnBotsSpawned = false;
	public boolean PCiBotsSpawned = false;
	public ArrayList<String> playersJoined = new ArrayList<>(20);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		// Barricades
		SceneryDefinition.forId(14227).getHandlers().put("option:repair", this);
		SceneryDefinition.forId(14228).getHandlers().put("option:repair", this);
		SceneryDefinition.forId(14229).getHandlers().put("option:repair", this);
		SceneryDefinition.forId(14230).getHandlers().put("option:repair", this);
		SceneryDefinition.forId(14231).getHandlers().put("option:repair", this);
		SceneryDefinition.forId(14232).getHandlers().put("option:repair", this);
		// Gates
		SceneryDefinition.forId(14233).getHandlers().put("option:open", this);
		SceneryDefinition.forId(14234).getHandlers().put("option:close", this);
		SceneryDefinition.forId(14235).getHandlers().put("option:open", this);
		SceneryDefinition.forId(14236).getHandlers().put("option:close", this);
		SceneryDefinition.forId(14237).getHandlers().put("option:open", this);
		SceneryDefinition.forId(14237).getHandlers().put("option:repair", this);
		SceneryDefinition.forId(14238).getHandlers().put("option:close", this);
		SceneryDefinition.forId(14238).getHandlers().put("option:repair", this);
		SceneryDefinition.forId(14239).getHandlers().put("option:open", this);
		SceneryDefinition.forId(14239).getHandlers().put("option:repair", this);
		SceneryDefinition.forId(14240).getHandlers().put("option:close", this);
		SceneryDefinition.forId(14240).getHandlers().put("option:repair", this);
		SceneryDefinition.forId(14241).getHandlers().put("option:open", this);
		SceneryDefinition.forId(14241).getHandlers().put("option:repair", this);
		SceneryDefinition.forId(14242).getHandlers().put("option:close", this);
		SceneryDefinition.forId(14242).getHandlers().put("option:repair", this);
		SceneryDefinition.forId(14243).getHandlers().put("option:open", this);
		SceneryDefinition.forId(14243).getHandlers().put("option:repair", this);
		SceneryDefinition.forId(14244).getHandlers().put("option:close", this);
		SceneryDefinition.forId(14244).getHandlers().put("option:repair", this);
		SceneryDefinition.forId(14245).getHandlers().put("option:open", this);
		SceneryDefinition.forId(14245).getHandlers().put("option:repair", this);
		SceneryDefinition.forId(14246).getHandlers().put("option:close", this);
		SceneryDefinition.forId(14246).getHandlers().put("option:repair", this);
		SceneryDefinition.forId(14247).getHandlers().put("option:open", this);
		SceneryDefinition.forId(14247).getHandlers().put("option:repair", this);
		SceneryDefinition.forId(14248).getHandlers().put("option:close", this);
		SceneryDefinition.forId(14248).getHandlers().put("option:repair", this);
		// Towers
		SceneryDefinition.forId(14296).getHandlers().put("option:climb", this);
		// Lander crossing plank
		SceneryDefinition.forId(14315).getHandlers().put("option:cross", this);
		SceneryDefinition.forId(25631).getHandlers().put("option:cross", this);
		SceneryDefinition.forId(25632).getHandlers().put("option:cross", this);
		return this;
	}

	/**
	 * Starts the pest control.
	 * @param p The player.
	 * @param name The name of the activity.
	 * @param destination The destination location (in the lander).
	 */
	private void startActivity(Player p, String name, Location destination) {
		if (ActivityManager.start(p, name, false)) {
			p.getPacketDispatch().sendMessage("You board the lander.");
			p.getProperties().setTeleportLocation(destination);
		}
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		int pestBotsAmount = 0;
		int pestBots2Amount = 0;
		Scenery object = (Scenery) node;
		if (option.equals("cross")) {
			if (player.getFamiliarManager().hasFamiliar() && player.getRights() != Rights.ADMINISTRATOR) {
				player.getPacketDispatch().sendMessage("You can't take a follower on the lander.");
				return true;
			}
			switch (object.getId()){
				case 14315: // Novice
					if (!GameWorld.getPCnBotsSpawned() && !player.isArtificial()) { //First person to join gets bots to play with
						GameWorld.setPCnBotsSpawned(true);
						for (pestBotsAmount = 0; pestBotsAmount <= 35; pestBotsAmount++) {
							PvMBotsBuilder.createPestControlTestBot(new Location(2657, 2640));
						}
					}
					if (!playersJoined.contains(player.getUsername()) && !player.isArtificial()) { //You also get +1 bot for every friend
						playersJoined.add(player.getUsername());
					}

					startActivity(player, "pest control novice", Location.create(2661, 2639, 0));
					return true;
				case 25631: // Intermediate
					if (!GameWorld.getPCiBotsSpawned() && !player.isArtificial()) { //First person to join gets bots to play with
						GameWorld.setPCiBotsSpawned(true);
						for (pestBots2Amount = 0; pestBots2Amount <= 50; pestBots2Amount++ ) {
							PvMBotsBuilder.createPestControlTestBot2(new Location(2644, 2644));
						}
					}
					if (!playersJoined.contains(player.getUsername()) && !player.isArtificial()) { //You also get +1 bot for every friend
						playersJoined.add(player.getUsername());
					}

					startActivity(player, "pest control intermediate", Location.create(2640, 2644, 0));
					return true;
				case 25632: // Veteran
					startActivity(player, "pest control veteran", Location.create(2634, 2653, 0));
					return true;
			}
		}
		PestControlSession session = player.getExtension(PestControlSession.class);
		if (session == null) {
			return true;
		}
		if (object.getId() == 14296) {
			handleTurretTower(player, session, object);
			return true;
		}
		if (!object.isActive() || !session.getBarricades().contains(object)) {
			return true;
		}
		if (option.equals("repair")) { // Handle barricades
			repair(player, session, object, object.getId() - (object.getId() < 14233 ? 3 : 4));
			return true;
		}
		if (object.getId() > 14232) {
			handleGates(player, session, object);
			return true;
		}
		return false;
	}

	/**
	 * Handles a turret tower ladder.
	 * @param player The player.
	 * @param session The session.
	 * @param object The object.
	 */
	private void handleTurretTower(Player player, PestControlSession session, Scenery object) {
		int x = object.getLocation().getLocalX();
		int y = object.getLocation().getLocalY();
		if (x == 45 && y == 41) {
			if (player.getLocation().getLocalX() < x + 1) {
				player.getProperties().setTeleportLocation(session.getRegion().getBaseLocation().transform(x + 1, 41, 0));
			} else {
				player.getProperties().setTeleportLocation(session.getRegion().getBaseLocation().transform(x - 1, 41, 0));
			}
		} else if ((x == 42 || x == 23) && y == 26) {
			if (player.getLocation().getLocalY() > 25) {
				player.getProperties().setTeleportLocation(session.getRegion().getBaseLocation().transform(x, 25, 0));
			} else {
				player.getProperties().setTeleportLocation(session.getRegion().getBaseLocation().transform(x, 27, 0));
			}
		} else if (x == 20 && y == 41) {
			if (player.getLocation().getLocalX() > 19) {
				player.getProperties().setTeleportLocation(session.getRegion().getBaseLocation().transform(x - 1, 41, 0));
			} else {
				player.getProperties().setTeleportLocation(session.getRegion().getBaseLocation().transform(x + 1, 41, 0));
			}
		}
	}

	/**
	 * Repairs an object.
	 * @param player The player.
	 * @param session The pest control session.
	 * @param object The scenery to repair.
	 * @param newId The repaired object id.
	 */
	private void repair(Player player, PestControlSession session, Scenery object, int newId) {
		if (!player.getInventory().contains(2347, 1)) {
			player.getPacketDispatch().sendMessage("You'll need a hammer to make any repairs!");
			return;
		}
		if (!player.getInventory().remove(new Item(1511, 1))) {
			player.getPacketDispatch().sendMessage("You'll need some logs to make any repairs!");
			return;
		}
		session.addZealGained(player, 5);
		player.animate(Animation.create(898));
		if (session.getBarricades().remove(object)) {
			Scenery replacement = object.transform(newId, object.getRotation(), getObjectType(newId));
			session.getBarricades().add(replacement);
			SceneryBuilder.replace(object, replacement);
		}
	}

	/**
	 * Opens the gates.
	 * @param player The player.
	 * @param session The pest control session.
	 * @param object The scenery.
	 */
	private static void handleGates(Player player, PestControlSession session, Scenery object) {
		boolean open = (object.getId() % 2) != 0;
		Scenery second = getSecondDoor(object);
		if (second == null) {
			return;
		}
		if (object.getId() > 14240 || second.getId() > 14240) {
			player.getPacketDispatch().sendMessage("It's too damaged to be moved!");
			return;
		}
		int rotation = getRotation(object);
		int dir = open ? object.getRotation() : rotation;
		Direction direction = Direction.get(!open ? dir : ((3 + dir) % 4));
		if (session.getBarricades().contains(object) && session.getBarricades().contains(second)) {
			session.getBarricades().remove(object);
			session.getBarricades().remove(second);

			Location l = object.getLocation().transform(direction.getStepX(), direction.getStepY(), 0);
			Scenery replacement = new Scenery(object.getId() + (open ? 1 : -1), l, 0, open ? rotation : ((direction.toInteger() + 3) % 4));
			SceneryBuilder.replace(object, replacement);
			session.getBarricades().add(replacement);

			l = second.getLocation().transform(direction.getStepX(), direction.getStepY(), 0);
			replacement = new Scenery(second.getId() + (open ? 1 : -1), l, 0, open ? getRotation(second) : ((direction.toInteger() + 3) % 4));
			SceneryBuilder.replace(second, replacement);
			session.getBarricades().add(replacement);
		}
	}

	/**
	 * Gets the rotation for the given object.
	 * @param object The object.
	 * @return The rotation.
	 */
	private static final int getRotation(Scenery object) {
		int id = object.getId();
		if (id > 14236) {
			id -= 4;
		}
		switch (id) {
			case 14233:
				return (object.getRotation() + 1) % 4;
			case 14234:
				return object.getRotation() % 4;
			case 14235:
				return (object.getRotation() + 3) % 4;
			case 14236:
				return (object.getRotation() + 2) % 4;
		}
		return 0;
	}

	/**
	 * Gets the second door.
	 * @param object The first door object.
	 * @return The second door object.
	 */
	public static Scenery getSecondDoor(Scenery object) {
		Location l = object.getLocation();
		Scenery o = null;
		if ((o = RegionManager.getObject(l.transform(-1, 0, 0))) != null && o.getName().equals(object.getName())) {
			return o;
		}
		if ((o = RegionManager.getObject(l.transform(1, 0, 0))) != null && o.getName().equals(object.getName())) {
			return o;
		}
		if ((o = RegionManager.getObject(l.transform(0, -1, 0))) != null && o.getName().equals(object.getName())) {
			return o;
		}
		if ((o = RegionManager.getObject(l.transform(0, 1, 0))) != null && o.getName().equals(object.getName())) {
			return o;
		}
		return null;
	}

	/**
	 * Gets the object type for the given object id.
	 * @param objectId The object id.
	 * @return The object type.
	 */
	private static int getObjectType(int objectId) {
		if (objectId == 14225 || objectId == 14226 || objectId == 14228 || objectId == 14229) {
			return 9;
		}
		return 0;
	}

}