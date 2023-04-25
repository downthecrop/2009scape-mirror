package content.region.asgarnia.falador.quest.blackknightsfortress;

import core.cache.def.impl.ItemDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.global.action.ClimbActionHandler;
import core.game.global.action.DoorActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.repository.Repository;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Plugin;
import org.rs09.consts.Items;

import static core.api.ContentAPIKt.allInEquipment;

/**
 * Represents the black knights fortress node option plugin.
 * @author 'Vexia
 * @version 1.0
 */
public final class BKFortressPlugin extends OptionHandler {

	/**
	 * Represents the listening animation.
	 */
	private static final Animation LISTEN_ANIM = new Animation(4195);

	/**
	 * Represents the lowering animation.
	 */
	private static final Animation LOWER_ANIM = new Animation(4552);

	/**
	 * Represents the first animation.
	 */
	private static final Animation FIRST_ANIM = new Animation(4549);

	/**
	 * Represents the last anim.
	 */
	private static final Animation LAST_ANIM = new Animation(4551);

	/**
	 * Represents the smoke graphic.
	 */
	private static final Graphics SMOKE = new Graphics(86, 109, 1);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(9589).getHandlers().put("option:read", this);
		SceneryDefinition.forId(74).getHandlers().put("option:open", this);
		SceneryDefinition.forId(73).getHandlers().put("option:open", this);
		SceneryDefinition.forId(2337).getHandlers().put("option:open", this);
		SceneryDefinition.forId(2338).getHandlers().put("option:open", this);
		SceneryDefinition.forId(2341).getHandlers().put("option:push", this);
		SceneryDefinition.forId(17148).getHandlers().put("option:climb-up", this);
		SceneryDefinition.forId(17149).getHandlers().put("option:climb-down", this);
		SceneryDefinition.forId(17160).getHandlers().put("option:climb-down", this);
		SceneryDefinition.forId(2342).getHandlers().put("option:listen-at", this);
		return this;
	}

	@Override
	public boolean handle(final Player player, Node node, String option) {
		final int id = node instanceof Item ? ((Item) node).getId() : ((Scenery) node).getId();
		Scenery object = node instanceof Scenery ? ((Scenery) node) : null;
		Location dest = null;
		switch (id) {
		case 2342:// listen at grill.
			player.animate(LISTEN_ANIM);
			GameWorld.getPulser().submit(new Pulse(2, player) {
				@Override
				public boolean pulse() {
					player.animate(LOWER_ANIM);
					player.getDialogueInterpreter().open(992752973);
					return true;
				}
			});
			break;
		case 17160:
			if (object.getLocation().equals(new Location(3022, 3518, 1))) {
				dest = Location.create(3022, 3517, 0);
			}
			if (dest != null) {
				ClimbActionHandler.climb(player, new Animation(828), dest);
			} else {
				ClimbActionHandler.climbLadder(player, (Scenery) node, option);
			}
			break;
		case 17149:
			if (object.getLocation().equals(new Location(3023, 3513, 2))) {
				dest = Location.create(3023, 3514, 1);
			} else if (object.getLocation().equals(new Location(3025, 3513, 2))) {
				dest = Location.create(3025, 3514, 1);
			} else if (object.getLocation().equals(new Location(3016, 3519, 2))) {
				dest = Location.create(3016, 3518, 1);
			} else if (object.getLocation().equals(new Location(3015, 3519, 1))) {
				dest = Location.create(3015, 3518, 0);
			} else if (object.getLocation().equals(new Location(3017, 3516, 2))) {
				dest = Location.create(3017, 3515, 1);
			}
			if (dest != null) {
				ClimbActionHandler.climb(player, new Animation(828), dest);
			} else {
				ClimbActionHandler.climbLadder(player, (Scenery) node, option);
			}
			break;
		case 17148:
			if (object.getLocation().equals(new Location(3021, 3510, 0))) {
				dest = Location.create(3022, 3510, 1);
			} else if (object.getLocation().equals(new Location(3015, 3519, 0))) {
				dest = Location.create(3015, 3518, 1);
			} else if (object.getLocation().equals(new Location(3016, 3519, 0))) {
				dest = Location.create(3016, 3518, 2);
			}
			if (dest != null) {
				ClimbActionHandler.climb(player, new Animation(828), dest);
			} else {
				ClimbActionHandler.climbLadder(player, (Scenery) node, option);
			}
			break;
		case 2341:
			player.getPacketDispatch().sendMessage("You push against the wall. You find a secret passage.");
			DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
			return true;
		case 2338:
			if (player.getLocation().getX() > 3019) {
				DoorActionHandler.handleAutowalkDoor(player, (Scenery) node); // big table room door
				return true;
			}
			player.getDialogueInterpreter().open(4605, Repository.findNPC(4605), true, true);
			break;
			case 2337: // Guard Door - only check for uniform from outside
				switch (player.getLocation().getY()) {
					case 3514: // Outside constant Y location, block the player for checks
						if(allInEquipment(player, Items.BRONZE_MED_HELM_1139, Items.IRON_CHAINBODY_1101)){
							DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
						}
						else player.getDialogueInterpreter().open(4605, Repository.findNPC(4604), true);
						break;
					case 3515: //Inside constant Y location, let the player through
						DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
					default:
						break;
				}
				break;
		case 74:
		case 73:// large door scenery id 73
			if (player.getLocation().getX() == 3008) { // only opened from inside
				DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
				return true;
			}
			player.getPacketDispatch().sendMessage("You can't open this door."); // large door to the fortress
			break;
		case 9589:
			switch (option) {
			case "read":// 4549, 4551
				if (player.getInventory().remove((Item) node)) {
					player.lock();
					GameWorld.getPulser().submit(new Pulse(1) {
						int counter = 0;

						@Override
						public boolean pulse() {
							switch (counter++) {
							case 1:
								player.animate(FIRST_ANIM);
								player.getDialogueInterpreter().sendDialogue("Infiltrate fortress... sabotage secret weapon... self", "destruct in 3...2...ARG!");
								break;
							case 5:
								player.graphics(SMOKE);
								break;
							case 7:
								player.getInterfaceManager().closeChatbox();
								player.animate(LAST_ANIM);
								player.unlock();
								return true;
							}
							return false;
						}
					});
				}
				break;
			}
			break;
		}
		return true;
	}

	@Override
	public boolean isWalk() {
		return false;
	}

	@Override
	public boolean isWalk(final Player player, final Node node) {
		return !(node instanceof Item);
	}

}
