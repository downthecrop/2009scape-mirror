package core.game.content.zone;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import core.cache.def.impl.SceneryDefinition;
import core.game.content.activity.ActivityManager;
import core.game.interaction.Option;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import org.json.simple.JSONObject;
import rs09.ServerStore;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.game.world.map.zone.ZoneRestriction;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;
import core.tools.RandomFunction;

/**
 * Handles the chaos tunnels.
 * @author Vexia
 */
@Initializable
public final class ChaosTunnelZone extends MapZone implements Plugin<Object> {

	/**
	 * The entrance data.
	 */
	private static final Object[][] ENTRANCE_DATA = new Object[][] { { 28891, new Location(3182, 5471, 0), 28782, new Location(3059, 3549, 0) },// 1
		{ 28893, new Location(3248, 5489, 0), 28782, new Location(3120, 3571, 0) },// 2
		{ 28892, new Location(3292, 5479, 0), 28782, new Location(3166, 3561, 0) },// 3
		{ 28893, new Location(3234, 5558, 0), 28782, new Location(3107, 3640, 0) },// 4
		{ 28892, new Location(3290, 5538, 0), 28782, new Location(3165, 3617, 0) },// 5
	};

	/**
	 * The mapping of portals.
	 */
	private static final Map<Location, Location> PORTALS = new HashMap<>();

	/**
	 * Constructs a new {@Code ChaosTunnelZone} {@Code Object}
	 */
	public ChaosTunnelZone() {
		super("Chaos tunnel", true, ZoneRestriction.CANNON);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ZoneBuilder.configure(this);
		ClassScanner.definePlugin(new OptionHandler() {
			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				for (int i = 0; i < ENTRANCE_DATA.length; i++) {
					SceneryDefinition.forId((int) ENTRANCE_DATA[i][0]).getHandlers().put("option:enter", this);
					SceneryDefinition.forId((int) ENTRANCE_DATA[i][2]).getHandlers().put("option:climb-up", this);
				}
				SceneryDefinition.forId(23074).getHandlers().put("option:climb", this);
				return this;
			}

			@Override
			public boolean handle(Player player, Node node, String option) {
				Object[] data = null;
				switch (option) {
				case "enter":
				case "climb-up":
					switch (node.getId()) {
					case 28891:
					case 28893:
					case 28892:
					case 28782:
						if (option.equals("enter") && player.inCombat()) {
							player.sendMessage("You can't enter the rift when you've recently been in combat.");
							return true;
						}
						for (int i = 0; i < ENTRANCE_DATA.length; i++) {
							if ((int) ENTRANCE_DATA[i][option.equals("enter") ? 0 : 2] == node.getId() && player.getLocation().withinDistance((Location) ENTRANCE_DATA[i][option.equals("enter") ? 3 : 1])) {
								data = ENTRANCE_DATA[i];
								break;
							}
						}
						if (data == null) {
							player.sendMessage("Error! Data null.");
							break;
						}
						player.teleport((Location) data[option.equals("enter") ? 1 : 3]);
						break;
					}
					break;
				case "climb":
					switch (node.getId()) {
					case 23074:
						player.teleport(new Location(3283, 3467, 0));
						break;
					}
					break;
				}
				return true;
			}
		});
		return this;
	}

	@Override
	public boolean interact(Entity entity, Node target, Option option) {
		if (entity instanceof Player) {
			switch (target.getId()) {
			case 29537:
				break;
			case 28779:// portal
				if (target.getLocation().equals(new Location(3326, 5469, 0))) {
					entity.asPlayer().sendMessage("You can't go back throught his portal.");
					return true;
				}
				teleport(entity.asPlayer(), target.asScenery());
				break;
			}
		}
		return super.interact(entity, target, option);
	}

	@Override
	public boolean enter(Entity entity) {
		if (entity instanceof NPC) {
			NPC n = entity.asNpc();
			if (!n.isAggressive()) {
				n.setAggressive(true);
			}
		}
		return super.enter(entity);
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public void configure() {
		register(new ZoneBorders(3116, 5412, 3362, 5584));
		/* top left */
		addLink(3158, 5561, 3162, 5557);
		addLink(3162, 5545, 3166, 5553);
		addLink(3147, 5541, 3143, 5535);
		addLink(3148, 5533, 3153, 5537);
		addLink(3152, 5520, 3156, 5523);
		addLink(3165, 5515, 3173, 5530);
		addLink(3169, 5510, 3159, 5501);
		addLink(3181, 5517, 3185, 5518);
		addLink(3182, 5530, 3187, 5531);
		addLink(3190, 5519, 3190, 5515);
		addLink(3196, 5512, 3202, 5515);
		addLink(3197, 5529, 3201, 5531);
		addLink(3190, 5549, 3190, 5554);
		addLink(3180, 5557, 3174, 5558);
		addLink(3171, 5542, 3168, 5541);
		/* top middle */
		addLink(3206, 5553, 3204, 5546);
		addLink(3226, 5553, 3230, 5547);
		addLink(3214, 5533, 3211, 5533);
		addLink(3208, 5527, 3211, 5523);
		addLink(3238, 5507, 3232, 5501);
		addLink(3241, 5529, 3243, 5526);
		addLink(3261, 5536, 3268, 5534);
		addLink(3252, 5543, 3249, 5546);
		addLink(3262, 5552, 3266, 5552);
		addLink(3256, 5561, 3253, 5561);
		addLink(3297, 5536, 3299, 5533);
		/* top right */
		addLink(3285, 5556, 3291, 5555);
		addLink(3288, 5536, 3289, 5533);
		addLink(3285, 5527, 3282, 5531);
		addLink(3285, 5508, 3280, 5501);
		addLink(3300, 5514, 3297, 5510);
		addLink(3325, 5518, 3323, 5531);
		addLink(3321, 5554, 3315, 5552);
		/* bottom left */
		addLink(3142, 5489, 3141, 5480);
		addLink(3142, 5462, 3154, 5462);
		addLink(3143, 5443, 3155, 5449);
		addLink(3167, 5478, 3171, 5478);
		addLink(3171, 5473, 3167, 5471);
		addLink(3168, 5456, 3178, 5460);
		addLink(3187, 5460, 3189, 5444);
		addLink(3192, 5472, 3186, 5472);
		addLink(3185, 5478, 3191, 5482);
		addLink(3197, 5448, 3204, 5445);
		addLink(3191, 5482, 3185, 5478);
		addLink(3191, 5495, 3194, 5490);
		/* bottom middle */
		addLink(3214, 5456, 3212, 5452);
		addLink(3229, 5454, 3235, 5457);
		addLink(3233, 5445, 3241, 5445);
		addLink(3239, 5498, 3244, 5495);
		addLink(3233, 5470, 3241, 5469);
		addLink(3241, 5445, 3233, 5445);
		addLink(3259, 5446, 3265, 5491);
		addLink(3260, 5491, 3266, 5446);
		addLink(3218, 5478, 3215, 5475);
		addLink(3208, 5471, 3210, 5477);
		/* bottom right */
		addLink(3283, 5448, 3287, 5448);
		addLink(3296, 5455, 3299, 5450);
		addLink(3302, 5469, 3290, 5463);
		addLink(3286, 5470, 3285, 5474);
		addLink(3322, 5480, 3318, 5481);
		addLink(3317, 5496, 3307, 5496);
		addLink(3299, 5484, 3303, 5477);
		addLink(3280, 5460, 3273, 5460);
		addLink(3285, 5474, 3286, 5470);
		addLink(3222, 5474, 3224, 5479);
		addLink(3222, 5488, 3218, 5497);
	}

	/**
	 * Teleports a player through a portal.
	 * @param object the object.
	 * @param player the player.
	 */
	private void teleport(Player player, Scenery object) {
		if (object.getLocation().getX() == 3142 || object.getLocation().getY() == 5545) {
			commenceBorkBattle(player);
			return;
		}
		Location loc = getLocation(object.getLocation());
		if (loc == null) {
			player.sendMessage("Error! Unhandled portal for - " + object + "!");
			return;
		}
		if (!isFixed(player)) {
			boolean stained = isStained(object);
			if (!stained && RandomFunction.random(100) <= 3) {
				stained = true;
				setStainedTime(object);
			}
			if (stained) {
				player.sendMessage("The portal is stained with dark magic.");
				return;
			}
			if (RandomFunction.random(100) <= 3) {
				loc = getRandomLocation();
				player.sendMessage("The dark magic teleports you into a random location.");
			}
		}
		player.teleport(loc);
		player.graphics(Graphics.create(110));
	}

	/**
	 * Starts the instanced battle against Bork.
	 * @param player The player.
	 */
	private void commenceBorkBattle(Player player) {
		if (ServerStore.getBoolean(getStoreFile(), player.getUsername().toLowerCase()) && GameWorld.getSettings().isHosted()) {
			player.getPacketDispatch().sendMessage("The portal's magic is too weak to teleport you right now.");
			return;
		}
		player.lock(10);
		player.graphics(Graphics.create(110));
		getStoreFile().put(player.getUsername().toLowerCase(), true);
		ActivityManager.start(player, "Bork cutscene", false);
	}

	/**
	 * Gets a random location.
	 * @return the location.
	 */
	private Location getRandomLocation() {
		return (Location) RandomFunction.getRandomElement(PORTALS.values().toArray());
	}

	/**
	 * Checks if the object is stained.
	 * @param object the object.
	 * @return {@code True} if so.
	 */
	private boolean isStained(Scenery object) {
		return getStainedTime(object) > GameWorld.getTicks();
	}

	/**
	 * Sets stained time.
	 * @param object the object.
	 */
	private void setStainedTime(Scenery object) {
		object.getAttributes().setAttribute("stained", (int) GameWorld.getTicks() + RandomFunction.random(50, 150));
	}

	/**
	 * Gets the stained tiem.
	 * @param object the object.
	 * @return the time.
	 */
	private int getStainedTime(Scenery object) {
		return object.getAttributes().getAttribute("stained", 0);
	}

	/**
	 * Checks if the portals are fixed.
	 * @param player the player.
	 * @return {@code True} if so.
	 */
	private boolean isFixed(Player player) {
		return false;
	}

	/**
	 * Gets a location from the portal.
	 * @param location the location.
	 * @return the location.
	 */
	public Location getLocation(Location location) {
		Location l = PORTALS.get(location);
		if (l != null) {
			return l;
		}
		for (Entry<Location, Location> entry : PORTALS.entrySet()) {
			if (entry.getValue().equals(location)) {
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * Adds a portal link.
	 * @param x the x.
	 * @param y the x.
	 * @param x2 the second x.
	 * @param y2 the second y.
	 */
	private void addLink(int x, int y, int x2, int y2) {
		addLink(new Location(x, y, 0), new Location(x2, y2, 0));
	}

	/**
	 * Adds a portal link.
	 * @param location the location.
	 * @param loc the second location.
	 */
	private void addLink(Location location, Location loc) {
		PORTALS.put(location, loc);
	}

	/**
	 * Returns the Server Store file for Bork being killed for the day
	 * @return The JSONObject containing the boolean of whether Bork has been killed
	 */
	private JSONObject getStoreFile() {
		return ServerStore.getArchive("daily-bork-killed");
	}

}
