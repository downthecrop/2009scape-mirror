package content.region.asgarnia.trollheim.handlers;

import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.component.Component;
import core.game.activity.ActivityManager;
import core.game.activity.ActivityPlugin;
import core.game.activity.CutscenePlugin;
import core.game.dialogue.DialoguePlugin;
import core.game.global.action.ClimbActionHandler;
import core.game.global.action.DoorActionHandler;
import core.game.node.entity.skill.Skills;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.game.world.repository.Repository;
import core.game.world.update.flag.context.Animation;
import core.net.packet.PacketRepository;
import core.net.packet.context.CameraContext;
import core.net.packet.context.CameraContext.CameraType;
import core.net.packet.out.CameraViewPacket;
import core.plugin.Plugin;
import core.plugin.Initializable;
import core.plugin.ClassScanner;

/**
 * Represents the plugin used to handle all trollheim node interations.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class TrollheimPlugin extends OptionHandler {

	/**
	 * Represents the locations to use.
	 */
	private static final Location[] LOCATIONS = new Location[] { new Location(2269, 4752, 0), new Location(2858, 3577, 0), Location.create(2808, 10002, 0), Location.create(2796, 3615, 0), Location.create(2907, 10019, 0), Location.create(2904, 3643, 0), new Location(2908, 3654, 0), Location.create(2907, 10035, 0), new Location(2893, 10074, 0), new Location(2893, 3671, 0) };

	/**
	 * Represents the climbing boots item.
	 */
	private static final Item CLIMBING_BOOTS = new Item(3105);

	/**
	 * Represents the climb down animation.
	 */
	private static final Animation CLIMB_DOWN = new Animation(1148);

	/**
	 * Represents the climb up animation.
	 */
	private static final Animation CLIMB_UP = new Animation(740);

	/**
	 * Represents the climb fail animation.
	 */
	public static final Animation CLIMB_FAIL = new Animation(739);

	/**
	 * Represents the low jumpa nimation.
	 */
	private static final Animation JUMP = new Animation(839);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(3735).getHandlers().put("option:enter", this);
		SceneryDefinition.forId(32738).getHandlers().put("option:exit", this);
		NPCDefinition.forId(1069).getHandlers().put("option:talk-to", this);
		SceneryDefinition.forId(3742).getHandlers().put("option:read", this);
		SceneryDefinition.forId(3774).getHandlers().put("option:leave", this);
		SceneryDefinition.forId(3723).getHandlers().put("option:climb", this);// located
		// in
		// circle
		// area.
		SceneryDefinition.forId(3722).getHandlers().put("option:climb", this);// located
		// in
		// circle
		// area.
		SceneryDefinition.forId(3748).getHandlers().put("option:climb", this);
		SceneryDefinition.forId(3790).getHandlers().put("option:climb", this);// pos=Location.create(2858,
		// 3627,
		// 0)
		SceneryDefinition.forId(3791).getHandlers().put("option:climb", this);// pos=Location.create(2858,
		// 3627,
		// 0)
		SceneryDefinition.forId(3782).getHandlers().put("option:open", this);// arena
		// entrance
		SceneryDefinition.forId(3783).getHandlers().put("option:open", this);// arena
		// entrance
		SceneryDefinition.forId(3762).getHandlers().put("option:open", this);// secret
		// door.
		SceneryDefinition.forId(4499).getHandlers().put("option:enter", this);// entrance
		// near
		// golden
		// apple.
		SceneryDefinition.forId(4500).getHandlers().put("option:enter", this);// entrance
		// near
		// golden
		// apple.
		SceneryDefinition.forId(9303).getHandlers().put("option:climb", this);// lvl
		// 41.
		SceneryDefinition.forId(3782).getHandlers().put("option:open", this);// arena
		// entrance.
		SceneryDefinition.forId(3783).getHandlers().put("option:open", this);// arena
		// entrance.
		SceneryDefinition.forId(3785).getHandlers().put("option:open", this);// arena
		// exit.
		SceneryDefinition.forId(3786).getHandlers().put("option:open", this);// arena
		// exit.
		SceneryDefinition.forId(3757).getHandlers().put("option:enter", this);// arena
		// cave
		// entrance.
		SceneryDefinition.forId(3758).getHandlers().put("option:exit", this);// arena
		// cave
		// exit.
		SceneryDefinition.forId(9327).getHandlers().put("option:climb", this);// lvl
		// 64
		SceneryDefinition.forId(9304).getHandlers().put("option:climb", this);
		;// lvl
			// 43
		SceneryDefinition.forId(3803).getHandlers().put("option:climb", this);
		;// lvl
			// 43
			// near
			// trollheim
			// top.
		SceneryDefinition.forId(3804).getHandlers().put("option:climb", this);
		;// lvl
			// 43
			// near
			// trollheim
			// top.
		SceneryDefinition.forId(9306).getHandlers().put("option:climb", this);// lvl
		// 47
		SceneryDefinition.forId(9305).getHandlers().put("option:climb", this);// lvl
		// 44
		SceneryDefinition.forId(3759).getHandlers().put("option:enter", this);// top
		// cave.
		SceneryDefinition.forId(3771).getHandlers().put("option:enter", this);// stronghold
																					// to
																					// herb
																					// patch
		SceneryDefinition.forId(18834).getHandlers().put("option:climb-up", this);// stronghold
																						// ladder
		SceneryDefinition.forId(18833).getHandlers().put("option:climb-down", this);// stronghold
																							// ladder
		ClassScanner.definePlugin(new WoundedSoldier());
		ClassScanner.definePlugin(new WarningZone());
		ActivityManager.register(new WarningCutscene());
		ClassScanner.definePlugin(new TrollNPC());
		return this;
	}

	@Override
	public boolean handle(final Player player, Node node, String option) {
		final int id = node instanceof Scenery ? ((Scenery) node).getId() : ((NPC) node).getId();
		final Location loc = node.getLocation();
		int xOffset = 0, yOffset = 0;
		Direction direction = Direction.SOUTH;
		switch (option) {
		case "enter":
			switch (id) {
			case 3735:
				player.getProperties().setTeleportLocation(LOCATIONS[0]);
				break;
			case 4499:
				player.getProperties().setTeleportLocation(LOCATIONS[2]);
				break;
			case 4500:
				player.getProperties().setTeleportLocation(LOCATIONS[3]);
				break;
			case 3723:
				player.getProperties().setTeleportLocation(LOCATIONS[4]);
				break;
			case 3757:
				player.getProperties().setTeleportLocation(loc.equals(new Location(2907, 3652, 0)) ? LOCATIONS[7] : LOCATIONS[4]);
				break;
			case 3759:
				player.getProperties().setTeleportLocation(LOCATIONS[8]);
				break;
			case 3771:
				player.teleport(new Location(2837, 10090, 2));
				break;
			}
			break;
		case "leave":
			player.teleport(new Location(2840, 3690));
			break;
		case "exit":
			switch (id) {
			case 32738:
				if (loc.equals(new Location(2892, 10072, 0))) {
					player.getProperties().setTeleportLocation(LOCATIONS[0]);
					return true;
				}
				player.teleport(LOCATIONS[1]);
				break;
			case 3758:
				player.getProperties().setTeleportLocation(loc.equals(new Location(2906, 10036, 0)) ? LOCATIONS[6] : LOCATIONS[5]);
				break;
			}
			break;
		case "talk-to":
			switch (id) {
			case 1069:
				player.getDialogueInterpreter().open(id, node);
				break;
			}
			break;
		case "read":
			switch (id) {
			case 3742:
				ActivityManager.start(player, "trollheim-warning", false);
				break;
			}
			break;
		case "open":
			switch (id) {
			case 3785:// exit
			case 3786:// exit
			case 3782:// entrance
			case 3783:// entrance.
				DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
				return true;
			case 3672:
				player.getPacketDispatch().sendMessage("You don't know how to open the secret door.");
				break;
			}
			break;
		case "climb-up":
			switch (id) {
			case 18834:
				ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, new Location(2828, 3678), "You clamber onto the windswept roof of the Troll Stronghold.");
				break;
			}
			break;
		case "climb-down":
			switch (id) {
			case 18833:
				ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_DOWN, new Location(2831, 10076, 2), "You clamber back inside the Troll Stronghold.");
				break;
			}
			break;
		case "climb":
			if (!player.getEquipment().containsItem(CLIMBING_BOOTS)) {
				player.getPacketDispatch().sendMessage("You need Climbing boots to negotiate these rocks.");
				return true;
			}
			final Scenery object = ((Scenery) node);
			switch (id) {
			case 3723:
				bandaid(player, player.getLocation(), object.getLocation().transform(0, 2, 0), CLIMB_UP);
				break;
			case 3722:
				bandaid(player, player.getLocation(), object.getLocation().transform(0, -3, 0), CLIMB_DOWN, CLIMB_DOWN, object.getDirection());
				break;
			case 3790:// rock scalling.
				case 3791:
					xOffset = player.getLocation().getX() < loc.getX() ? 2 : -2;
				//bandaid(player, player.getLocation(), object.getLocation().transform(xOffset, yOffset, 0), CLIMB_DOWN, CLIMB_DOWN, direction);
				player.getProperties().setTeleportLocation(object.getLocation().transform(xOffset, yOffset, 0));
				break;
				//bandaid(player, player.getLocation(), object.getLocation().transform(xOffset, yOffset, 0), CLIMB_UP);
				case 3748:
				player.getPacketDispatch().sendMessage("You climb onto the rock...");
				if (loc.equals(new Location(2821, 3635, 0))) {
					bandaid(player, player.getLocation(), loc.transform(player.getLocation().getX() > loc.getX() ? -1 : 1, 0, 0), JUMP);
				} else if (loc.equals(new Location(2910, 3687, 0)) || loc.equals(new Location(2910, 3686, 0))) {
					if (player.getSkills().getLevel(Skills.AGILITY) < 43) {
						player.getPacketDispatch().sendMessage("You need an Agility level of 43 in order to climb this mountain side.");
						return true;
					}
					if (player.getLocation().equals(Location.create(2911, 3687, 0))) {
						bandaid(player, player.getLocation(), Location.create(2909, 3687, 0), JUMP);
					} else if (player.getLocation().equals(new Location(2909, 3687, 0))) {
						bandaid(player, player.getLocation(), Location.create(2911, 3687, 0), JUMP);
					} else if (player.getLocation().equals(Location.create(2911, 3686, 0))) {
						bandaid(player, player.getLocation(), Location.create(2909, 3686, 0), JUMP);
					} else {
						bandaid(player, player.getLocation(), Location.create(2911, 3686, 0), JUMP);
					}
				} else if (loc.equals(new Location(2857, 3612, 0)) || loc.equals(new Location(2856, 3612, 0))) {
					if (player.getSkills().getLevel(Skills.AGILITY) < 15) {
						player.getPacketDispatch().sendMessage("You need an Agility level of 15 in order to climb this rock.");
						return true;
					}
					bandaid(player, player.getLocation(), player.getLocation().getY() < object.getLocation().getY() ? player.getLocation().transform(0, 2, 0) : player.getLocation().transform(0, -2, 0), JUMP);
				} else {
					bandaid(player, player.getLocation(), player.getLocation().getY() < object.getLocation().getY() ? player.getLocation().transform(0, 2, 0) : player.getLocation().transform(0, -2, 0), JUMP);
				}
				player.getPacketDispatch().sendMessage("...and step down the other side.");
				break;
			case 3803:
			case 3804:
				if (player.getSkills().getLevel(Skills.AGILITY) < 43) {
					player.getPacketDispatch().sendMessage("You need an Agility level of 43 in order to climb this mountain side.");
					return true;
				}
				if (player.getLocation().equals(Location.create(2884, 3684, 0))) {
					bandaid(player, player.getLocation(), Location.create(2886, 3684, 0), CLIMB_UP);
				} else if (player.getLocation().equals(Location.create(2884, 3683, 0))) {
					bandaid(player, player.getLocation(), Location.create(2886, 3683, 0), CLIMB_UP);
				} else if (player.getLocation().equals(Location.create(2886, 3683, 0))) {
					bandaid(player, player.getLocation(), Location.create(2884, 3683, 0), CLIMB_DOWN, CLIMB_DOWN, Direction.EAST);
				} else if (player.getLocation().equals(Location.create(2888, 3660, 0)) || player.getLocation().equals(Location.create(2887, 3660, 0))) {
					bandaid(player, player.getLocation(), player.getLocation().transform(0, 2, 0), CLIMB_UP);
				} else if (player.getLocation().equals(Location.create(2888, 3662, 0)) || player.getLocation().equals(Location.create(2887, 3662, 0))) {
					bandaid(player, player.getLocation(), player.getLocation().transform(0, -2, 0), CLIMB_DOWN, CLIMB_DOWN, Direction.NORTH);
				} else {
					bandaid(player, player.getLocation(), Location.create(2884, 3684, 0), CLIMB_DOWN, CLIMB_DOWN, Direction.EAST);
				}
				break;
			case 9306:
				if (player.getSkills().getLevel(Skills.AGILITY) < 47) {
					player.getPacketDispatch().sendMessage("You need an Agility level of 47 in order to climb this mountain side.");
					return true;
				}
				if (player.getLocation().equals(Location.create(2903, 3680, 0))) {
					bandaid(player, player.getLocation(), Location.create(2900, 3680, 0), CLIMB_UP);
				} else {
					bandaid(player, player.getLocation(), Location.create(2903, 3680, 0), CLIMB_DOWN, CLIMB_DOWN, Direction.WEST);
				}
				break;
			case 9303:
				if (player.getSkills().getLevel(Skills.AGILITY) < 41) {
					player.getPacketDispatch().sendMessage("You need an Agility level of 41 in order to climb this mountain side.");
					return true;
				}
				if (player.getLocation().getX() > loc.getX()) {
					bandaid(player, player.getLocation(), object.getLocation().transform(-2, 0, 0), CLIMB_DOWN, CLIMB_DOWN, Direction.EAST);
				} else {
					bandaid(player, player.getLocation(), object.getLocation().transform(2, 0, 0), CLIMB_UP);
				}
				break;
			case 9304:
				if (player.getSkills().getLevel(Skills.AGILITY) < 47) {
					player.getPacketDispatch().sendMessage("You need an Agility level of 47 in order to climb this mountain side.");
					return true;
				}
				if (player.getLocation().equals(Location.create(2878, 3665, 0))) {
					bandaid(player, player.getLocation(), new Location(2878, 3668, 0), CLIMB_UP);
				} else {
					bandaid(player, player.getLocation(), new Location(2878, 3665, 0), CLIMB_DOWN, CLIMB_DOWN, Direction.NORTH);
				}
				break;
			case 9305:
				if (player.getSkills().getLevel(Skills.AGILITY) < 44) {
					player.getPacketDispatch().sendMessage("You need an Agility level of 44 in order to climb this mountain side.");
					return true;
				}
				if (player.getLocation().equals(new Location(2909, 3684, 0))) {
					bandaid(player, player.getLocation(), Location.create(2907, 3682, 0), CLIMB_UP, CLIMB_UP, Direction.SOUTH);
				} else {
					bandaid(player, player.getLocation(), Location.create(2909, 3684, 0), CLIMB_DOWN, CLIMB_DOWN, Direction.SOUTH);

				}
				break;
			case 9327:
				if (player.getSkills().getLevel(Skills.AGILITY) < 64) {
					player.getPacketDispatch().sendMessage("You need an Agility level of 64 in order to climb this mountain side.");
					return true;
				}
				if (object.getLocation().equals(new Location(2916, 3672, 0))) {
					bandaid(player, player.getLocation(), Location.create(2918, 3672, 0), CLIMB_UP);
				} else if (object.getLocation().equals(new Location(2917, 3672, 0))) {
					bandaid(player, player.getLocation(), Location.create(2915, 3672, 0), CLIMB_DOWN, CLIMB_DOWN, Direction.EAST);
				} else if (object.getLocation().equals(new Location(2923, 3673, 0))) {
					bandaid(player, player.getLocation(), Location.create(2921, 3672, 0), CLIMB_UP);
				} else if (object.getLocation().equals(new Location(2922, 3672, 0))) {
					bandaid(player, player.getLocation(), Location.create(2924, 3673, 0), CLIMB_DOWN, CLIMB_DOWN, Direction.WEST);
				}
				break;
			}
			break;
		}
		return true;
	}

	public void bandaid(Player player, Location l, Location e, Animation a){
		player.getProperties().setTeleportLocation(e);
	}

	public void bandaid(Player player, Location l, Location e, Animation a, Animation b, Direction d){
		player.getProperties().setTeleportLocation(e);
	}

	@Override
	public Location getDestination(Node node, Node n) {
		if (n instanceof Scenery) {
			final Scenery object = ((Scenery) n);
			if (object.getId() == 3782) {
				if (node.getLocation().getX() >= 2897) {
					return Location.create(2897, 3618, 0);
				}
			} else if (object.getId() == 3804) {
				if (object.getLocation().equals(new Location(2885, 3684, 0)) && node.getLocation().getX() >= 2885) {
					return object.getLocation().transform(1, 0, 0);
				}
			} else if (object.getId() == 9306 && node.getLocation().getX() >= 2902) {
				return Location.create(2903, 3680, 0);
			}
		}
		return null;
	}

	/**
	 * Gets the opposite direction.
	 * @param dir the dir.
	 * @return the opposite direction.
	 */
	private Direction getOpposite(Direction dir) {
		switch (dir) {
		case EAST:
			return Direction.WEST;
		case NORTH:
			return Direction.SOUTH;
		case SOUTH:
			return Direction.NORTH;
		case WEST:
			return Direction.EAST;
		default:
			break;
		}
		return null;
	}

	/**
	 * Represents the wounded soldiers dialogue plugin.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public final class WoundedSoldier extends DialoguePlugin {

		/**
		 * Constructs a new {@code WoundedSoldier} {@code Object}.
		 */
		public WoundedSoldier() {
			/**
			 * empty.
			 */
		}

		/**
		 * Constructs a new {@code TrolleimPlugin} {@code Object}.
		 * @param player the player.
		 */
		public WoundedSoldier(final Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new WoundedSoldier(player);
		}

		@Override
		public boolean open(Object... args) {
			player("Are you OK?");
			stage = 0;
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (stage) {
			case 0:
				npc("Urrrgggh.....");
				stage = 1;
				break;
			case 1:
				npc("I'll be OK, the trolls only leave the plateau at nightfall.", "The guys are bringing a stretcher shortly.");
				stage = 2;
				break;
			case 2:
				player("As long as you're sure.");
				stage = 3;
				break;
			case 3:
				npc("It's my own fault really, I was having a walk and", "wandered past the danger sign. The trolls throw rocks", "down at any one who goes up the path!");
				stage = 4;
				break;
			case 4:
				npc("Don't go up there!");
				stage = 5;
				break;
			case 5:
				player("OK, thanks for the warning.");
				stage = 6;
				break;
			case 6:
				end();
				break;
			}
			return true;
		}

		@Override
		public int[] getIds() {
			return new int[] { 1069 };
		}

	}

	/**
	 * Represents the warning map zone.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public static final class WarningZone extends MapZone implements Plugin<Object> {

		/**
		 * Represents the warning component interface,
		 */
		private static final Component COMPONENT = new Component(581);

		/**
		 * Constructs a new {@code WarningZone} {@code Object}.
		 */
		public WarningZone() {
			super("trollheim-warning", true);
		}

		@Override
		public boolean enter(final Entity entity) {
			if (entity instanceof Player) {
				final Player p = (Player) entity;
				if (p.getWalkingQueue().getFootPrint().getY() < 3592) {
					p.getWalkingQueue().reset();
					p.getPulseManager().clear();
					p.getInterfaceManager().open(COMPONENT);
				}
			}
			return super.enter(entity);
		}

		@Override
		public void configure() {
			register(new ZoneBorders(2837, 3592, 2838, 3593));
		}

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			ZoneBuilder.configure(this);
			return this;
		}

		@Override
		public Object fireEvent(String identifier, Object... args) {
			return null;
		}
	}

	/**
	 * Represents the warning cutscene plugin.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public static final class WarningCutscene extends CutscenePlugin {

		/**
		 * Represents the throwing animation.
		 */
		private static final Animation THROW = new Animation(1142);

		/**
		 * Represents the troll location.
		 */
		private static final Location TROLL_LOCATION = new Location(2851, 3598, 0);

		/**
		 * Constructs a new {@code WarningCutscene} {@code Object}.
		 */
		public WarningCutscene() {
			super("trollheim-warning");
		}

		/**
		 * Constructs a new {@code WarningCutscene} {@code Object}.
		 * @param p the player.
		 */
		public WarningCutscene(Player p) {
			super("trollheim-warning", false);
			this.player = p;
		}

		@Override
		public ActivityPlugin newInstance(Player p) throws Throwable {
			return new WarningCutscene(p);
		}

		@Override
		public void open() {
			final NPC npc = Repository.findNPC(TROLL_LOCATION);
			Location loc = Location.create(2849, 3597, 0);
			PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.POSITION, loc.getX() - 2, loc.getY(), 1300, 1, 30));
			PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.ROTATION, loc.getX() + 22, loc.getY() + 10, 1300, 1, 30));
			GameWorld.getPulser().submit(new Pulse(1, player) {
				int count = 0;

				@Override
				public boolean pulse() {
					switch (count++) {
					case 4:
						if (npc != null) {
							npc.faceTemporary(player, 3);
							npc.animate(THROW);
						}
						break;
					case 6:
						WarningCutscene.this.stop(false);
						PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraType.RESET, 0, 0, 1300, 1, 30));
						return true;
					}
					return false;
				}
			});
		}

		@Override
		public int getMapState() {
			return 0;
		}

		@Override
		public Location getSpawnLocation() {
			return null;
		}

		@Override
		public void configure() {
			ActivityManager.register(this);
		}

	}

	/**
	 * Represents the a trollheim troll.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public final class TrollNPC extends AbstractNPC {

		/**
		 * Constructs a new {@code TrollNPC} {@code Object}.
		 */
		public TrollNPC() {
			super(0, null);
		}

		/**
		 * Constructs a new {@code TrollNPC} {@code Object}.
		 * @param id the id.
		 * @param location the location.
		 */
		public TrollNPC(int id, Location location) {
			super(id, location, true);
			this.setAggressive(true);
		}

		@Override
		public AbstractNPC construct(int id, Location location, Object... objects) {
			return new TrollNPC(id, location);
		}

		@Override
		public int[] getIds() {
			return new int[] { 1106, 1107, 1108, 1109, 1110, 1111, 1112, 1138 };
		}

	}
}
