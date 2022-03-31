package core.game.content.activity.magearena;

import core.cache.def.impl.ItemDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.content.dialogue.DialogueAction;
import core.game.content.global.GodType;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.agility.AgilityHandler;
import core.game.node.item.GroundItem;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.game.content.global.action.PickupHandler;
import rs09.game.world.GameWorld;
import rs09.plugin.ClassScanner;

/**
 * Handles the mage arena activity.
 * @author Vexia
 */
@Initializable
public final class MageArenaPlugin extends OptionHandler {

	/**
	 * The pool destinations.
	 */
	private static final Location[] POOL_DESTINATIONS = new Location[] { Location.create(2542, 4718, 0), Location.create(2509, 4689, 0) };

	/**
	 * The mage arena zone.
	 */
	public static final MageArenaZone MAGE_ARENA = new MageArenaZone();

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ClassScanner.definePlugin(MAGE_ARENA);
		ClassScanner.definePlugin(new KolodionNPC());
		ClassScanner.definePlugin(new MageArenaNPC());
		ClassScanner.definePlugin(new LundailDialogue());
		ClassScanner.definePlugin(new KolodionDialogue());
		ClassScanner.definePlugin(new ChamberGuardianDialogue());
		ItemDefinition.forId(2412).getHandlers().put("option:drop", this);
		ItemDefinition.forId(2413).getHandlers().put("option:drop", this);
		ItemDefinition.forId(2414).getHandlers().put("option:drop", this);
		ItemDefinition.forId(2412).getHandlers().put("option:take", this);
		ItemDefinition.forId(2413).getHandlers().put("option:take", this);
		ItemDefinition.forId(2414).getHandlers().put("option:take", this);
		SceneryDefinition.forId(2873).getHandlers().put("option:pray at", this);
		SceneryDefinition.forId(2874).getHandlers().put("option:pray at", this);
		SceneryDefinition.forId(2875).getHandlers().put("option:pray at", this);
		SceneryDefinition.forId(2878).getHandlers().put("option:step-into", this);
		SceneryDefinition.forId(2879).getHandlers().put("option:step-into", this);
		return this;
	}

	@Override
	public boolean handle(final Player player, final Node node, String option) {
		switch (node.getId()) {
		case 2879:
		case 2878:
			final Location destination = POOL_DESTINATIONS[2879 - node.getId()];
			if (player.getSavedData().getActivityData().hasKilledKolodion()) {
				player.getDialogueInterpreter().sendDialogue("You step into the pool of sparkling water. You feel energy rush", "through your veins.");
				player.getDialogueInterpreter().addAction(new DialogueAction() {
					@Override
					public void handle(Player player, int buttonId) {
						handlePool(player, true, destination, (Scenery) node);
					}
				});
				return true;
			}
			player.lock(1);
			GameWorld.getPulser().submit(new Pulse(1, player) {
				@Override
				public boolean pulse() {
					handlePool(player, false, destination, (Scenery) node);
					return true;
				}
			});
			break;
		case 2873:
		case 2874:
		case 2875:
			GodType.forObject(node.getId()).pray(player, (Scenery) node);
			break;
		case 2412:
		case 2413:
		case 2414:
			GodType type = GodType.forCape(((Item) node));
			if (option.toLowerCase().equals("take")) {
				GroundItem g = (GroundItem) node;
				if (GodType.hasAny(player)) {
					GroundItemManager.destroy(g);
					player.sendMessages("You may only possess one sacred cape at a time.", "The conflicting powers of the capes drive them apart.");
				} else {
					PickupHandler.take(player, g);
				}
				return true;
			} else {
				if (type != null) {
					player.sendMessage(type.getDropMessage());
					player.getInventory().remove(type.getCape());
				}
			}
			break;
		}
		return true;
	}

	@Override
	public boolean isWalk(Player player, Node node) {
		if (node instanceof GroundItem) {
			return true;
		}
		return !(node instanceof Item);
	}

	@Override
	public boolean isWalk() {
		return false;
	}

	@Override
	public Location getDestination(Node node, Node n) {
		switch (n.getId()) {
		case 2878:
			int y = node.getLocation().getY(),
			x = node.getLocation().getX();
			Location dest = Location.create(2544, 4720, 0);
			Direction dir = Direction.WEST;
			if (y <= 4718) {
				dest = Location.create(2542, 4718, 0);
				dir = Direction.NORTH;
			} else if (y == 4722) {
				dest = Location.create(2542, 4722, 0);
				dir = Direction.SOUTH;
			} else if (x <= 2541) {
				dest = Location.create(2540, 4720, 0);
				dir = Direction.EAST;
			}
			((Player) node).setAttribute("mb-loc", dest);
			((Player) node).setAttribute("mb-dir", dir);
			return dest;
		case 2873:
		case 2874:
		case 2875:
			return n.getLocation().transform(0, -2, 0);
		}
		return null;
	}

	/**
	 * Handles the pool.
	 * @param player the player.
	 * @param enter if entered.
	 * @param dest the destination.
	 */
	public void handlePool(final Player player, boolean enter, final Location dest, final Scenery object) {
		final Location start = player.getAttribute("mb-loc", player.getLocation());
		final Location end = player.getLocation().transform(player.getAttribute("mb-dir", Direction.NORTH), 1);
		player.removeAttribute("mc-loc");
		player.removeAttribute("mb-dir");
		if (enter) {
			final Location middle = object.getId() == 2879 ? Location.create(2509, 4687, 0) : Location.create(2542, 4720, 0);
			player.lock();
			AgilityHandler.walk(player, -1, start, middle, new Animation(1426), 0.0, null);
			GameWorld.getPulser().submit(new Pulse(1, player) {
				int counter;

				@Override
				public boolean pulse() {
					switch (++counter) {
					case 3:
						player.getPacketDispatch().sendPositionedGraphic(68, 1, 0, middle);
						break;
					case 4:
						player.unlock();
						player.getProperties().setTeleportLocation(dest);
						return true;
					}
					return false;
				}

			});
			return;
		}
		player.sendMessage("You step into the pool.");
		AgilityHandler.walk(player, -1, start, end, new Animation(1426), 0.0, "Your boots get wet.");
		GameWorld.getPulser().submit(new Pulse(1, player) {

			@Override
			public boolean pulse() {
				player.getAppearance().setDefaultAnimations();
				player.getAppearance().sync();
				player.getProperties().setTeleportLocation(start);
				return true;
			}

		});
	}
}
