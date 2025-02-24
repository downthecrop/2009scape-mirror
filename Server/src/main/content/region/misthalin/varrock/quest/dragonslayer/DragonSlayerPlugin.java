package content.region.misthalin.varrock.quest.dragonslayer;

import core.cache.def.impl.ItemDefinition;
import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.global.action.ClimbActionHandler;
import core.game.global.action.DoorActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.impl.ForceMovement;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.entity.player.link.quest.Quest;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;

import java.util.List;

import static core.api.ContentAPIKt.*;
import content.data.Quests;


/**
 * Represents the plugin used to handle node interactions related to dragon slayer.
 *
 * @author Vexia
 * 
 */
public final class DragonSlayerPlugin extends OptionHandler {

    /**
     * Represents the hammer animation.
     */
    private static final Animation HAMMER_ANIM = new Animation(3676);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(25115).getHandlers().put("option:open", this);// magic
		// door.
		NPCDefinition.forId(747).getHandlers().put("option:trade", this);// oziach.
		SceneryDefinition.forId(2595).getHandlers().put("option:open", this);// maze
		// main
		// door.
		// maze first floor.
		SceneryDefinition.forId(32968).getHandlers().put("option:open", this);
		SceneryDefinition.forId(2602).getHandlers().put("option:open", this);
		SceneryDefinition.forId(2596).getHandlers().put("option:open", this);// red
		// door.
		SceneryDefinition.forId(1752).getHandlers().put("option:climb-up", this);
		SceneryDefinition.forId(25038).getHandlers().put("option:climb-up", this);// trapdoor
		// ladder
		SceneryDefinition.forId(25214).getHandlers().put("option:open", this);// trapdoor
		SceneryDefinition.forId(1746).getHandlers().put("option:climb-down", this);// ladder
		SceneryDefinition.forId(2605).getHandlers().put("option:climb-down", this);// ladder

		// maze second floor.
		SceneryDefinition.forId(2597).getHandlers().put("option:open", this);// orange
		// door.
		SceneryDefinition.forId(1747).getHandlers().put("option:climb-up", this);
		SceneryDefinition.forId(25045).getHandlers().put("option:climb-down", this);
		// maze third floor
		SceneryDefinition.forId(2598).getHandlers().put("option:open", this);// yellow
		// door.
		// basement floor
		SceneryDefinition.forId(2599).getHandlers().put("option:open", this);// blue
		// door.
		SceneryDefinition.forId(2600).getHandlers().put("option:open", this);// purple
		// door.
		SceneryDefinition.forId(2601).getHandlers().put("option:open", this);// green
		// door.
		SceneryDefinition.forId(2603).getHandlers().put("option:open", this);// closed
		// chest.
		SceneryDefinition.forId(2604).getHandlers().put("option:search", this);// search
		// chest.
		SceneryDefinition.forId(2604).getHandlers().put("option:close", this);// search
		// chest.
		SceneryDefinition.forId(1755).getHandlers().put("option:climb-up", this);
		// map parts
		ItemDefinition.forId(DragonSlayer.MAZE_PIECE.getId()).getHandlers().put("option:study", this);
		ItemDefinition.forId(DragonSlayer.MAGIC_PIECE.getId()).getHandlers().put("option:study", this);
		ItemDefinition.forId(DragonSlayer.WORMBRAIN_PIECE.getId()).getHandlers().put("option:study", this);
		ItemDefinition.forId(DragonSlayer.CRANDOR_MAP.getId()).getHandlers().put("option:study", this);
		// dwarv mine
		SceneryDefinition.forId(2587).getHandlers().put("option:open", this);
		NPCDefinition.forId(745).getHandlers().put("option:talk-to", this);
		// lady lumby
		SceneryDefinition.forId(25036).getHandlers().put("option:repair", this);
		SceneryDefinition.forId(2589).getHandlers().put("option:repair", this);

		// crandor
		SceneryDefinition.forId(25154).getHandlers().put("option:enter", this);
		SceneryDefinition.forId(2606).getHandlers().put("option:open", this);
		SceneryDefinition.forId(25213).getHandlers().put("option:climb", this);
		SceneryDefinition.forId(25161).getHandlers().put("option:climb-over", this);
		NPCDefinition.forId(742).getHandlers().put("option:attack", this);
		NPCDefinition.forId(745).getHandlers().put("option:attack", this);
		return this;
	}

	@Override
	public boolean handle(final Player player, Node node, String option) {
		final Quest quest = player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER);
		final int id = node instanceof Item ? ((Item) node).getId() : node instanceof Scenery ? ((Scenery) node).getId() : ((NPC) node).getId();
		switch (id) {
		case 1755:
			if (player.getLocation().withinDistance(Location.create(2939, 9656, 0))) {
				ClimbActionHandler.climb(player, new Animation(828), Location.create(2939, 3256, 0));
			} else {
				ClimbActionHandler.climbLadder(player, (Scenery) node, option);
				return true;
			}
			break;
		case 742:
			if (player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER).getStage(player) == 40 && (player.getInventory().containsItem(DragonSlayer.ELVARG_HEAD))) {
				player.getPacketDispatch().sendMessage("You have already slain the dragon. Now you just need to return to Oziach for");
				player.getPacketDispatch().sendMessage("your reward!");
				return true;
			}
			if (player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER).getStage(player) > 40) {
				player.getPacketDispatch().sendMessage("You have already slain Elvarg the dragon.");
				return true;
			}
			player.getProperties().getCombatPulse().attack(node);
			player.face((Entity) node);
			break;
		case 25161:
			if (player.getLocation().getX() >= 2847) {
				ForceMovement movement = new ForceMovement(player, player.getLocation(), player.getLocation().transform(player.getLocation().getX() == 2845 ? 2 : -2, 0, 0), new Animation(839));
				movement.run(player, 10);
				return true;
			}
			if (player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER).getStage(player) == 40 && (player.getInventory().containsItem(DragonSlayer.ELVARG_HEAD))) {
				player.getPacketDispatch().sendMessage("You have already slain the dragon. Now you just need to return to Oziach for");
				player.getPacketDispatch().sendMessage("your reward!");
				return true;
			}
			if (player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER).getStage(player) > 40) {
				player.getPacketDispatch().sendMessage("You have already slain the dragon.");
				return true;
			}
			if (player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER).getStage(player) == 40 && !player.getInventory().containsItem(DragonSlayer.ELVARG_HEAD)) {
				ForceMovement movement = new ForceMovement(player, player.getLocation(), player.getLocation().transform(player.getLocation().getX() == 2845 ? 2 : -2, 0, 0), new Animation(839));
				movement.run(player, 10);
				if (player.getLocation().getX() <= 2845) {
					List<NPC> npcs = RegionManager.getLocalNpcs(player);
					for (NPC n : npcs) {
						if (n.getId() == 742) {
							n.getProperties().getCombatPulse().attack(player);
							return true;
						}
					}
				}
			}
			break;
		case 25213:
			ClimbActionHandler.climb(player, new Animation(828), new Location(2834, 3258, 0));
			player.getAchievementDiaryManager().finishTask(player, DiaryType.KARAMJA, 1, 2);
			break;
		case 2606:
                if (player.getLocation().getY() < 9600 && !player.getSavedData().getQuestData().getDragonSlayerAttribute("memorized") && player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER).getStage(player) != 100) {
                    player.getPacketDispatch().sendMessage("The door is securely locked.");
                } else {
                    if (!player.getSavedData().getQuestData().getDragonSlayerAttribute("memorized")) {
                        player.getPacketDispatch().sendMessage("You found a secret door.");
                        player.getPacketDispatch().sendMessage("You remember where the secret door is for future reference.");
                    }
                    player.getAchievementDiaryManager().finishTask(player, DiaryType.KARAMJA, 1, 1);
                    player.getSavedData().getQuestData().setDragonSlayerAttribute("memorized", true);
                    DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
                }
                break;
		case 25154:
			ClimbActionHandler.climb(player, new Animation(828), new Location(2833, 9658, 0));
			break;
		case 25036:
		case 2589:
			if (player.getSavedData().getQuestData().getDragonSlayerAttribute("memorized")) {
				player.getDialogueInterpreter().sendDialogue("You don't need to mess about with broken ships now that you have", "found the secret passage from Karamja.");
				return true;
			}
			if (!player.getInventory().containsItem(DragonSlayer.NAILS)) {
				player.getDialogueInterpreter().sendDialogue("You need 30 steel nails to attach the plank with.");
				return true;
			}
			if (!player.getInventory().containsItem(DragonSlayer.PLANK)) {
				player.getDialogueInterpreter().sendDialogue("You'll need to use wooden planks on this hole to patch it up.");
				return true;
			}
			if (!player.getInventory().containsItem(DragonSlayer.HAMMER)) {
				player.getDialogueInterpreter().sendDialogue("You need a hammer to force the nails in with.");
				return true;
			}
			if (player.getInventory().remove(DragonSlayer.NAILS) && player.getInventory().remove(DragonSlayer.PLANK)) {
				player.lock(2);
				player.animate(HAMMER_ANIM);
				player.getSavedData().getQuestData().setDragonSlayerPlanks(player.getSavedData().getQuestData().getDragonSlayerPlanks() + 1);
				if (player.getSavedData().getQuestData().getDragonSlayerPlanks() < 3) {
					player.getDialogueInterpreter().sendDialogue("You nail a plank over the hole, but you still need more planks to", "close the hole completely.");
				} else {
					player.getSavedData().getQuestData().setDragonSlayerAttribute("repaired", true);
                                        setVarp(player, 177, 1967876);
					player.getDialogueInterpreter().sendDialogue("You nail a final plank over the hole. You have successfully patched", "the hole in the ship.");
				}
			}
			break;
		case 1538:
			player.getInterfaceManager().open(DragonSlayer.MAP_COMPONENT);
			break;
		case 745:
			if (option.equals("attack")) {
				player.getProperties().getCombatPulse().attack(node);
				return true;
			}
			player.getDialogueInterpreter().open(745, ((NPC) node));
			break;
		case 1535:// maze map piece.
			player.getDialogueInterpreter().sendItemMessage(1535, "This is a piece of map that you found in Melzar's Maze. You will need to join it to the other two map pieces before you can see the route to Crandor.");
			break;
		case 1536:
			player.getDialogueInterpreter().sendItemMessage(1536, "This is a piece of map that you got from Wormbrain, the goblin thief. You will need to join it to the other two map pieces before you can see the route to Crandor.");
			break;
		case 1537:// magic map piece.
			player.getDialogueInterpreter().sendItemMessage(1537, "This is a piece of map that you found in a secret chest in the Dwarven Mine. You will need to join it to the other two map pieces before you can see the route to Crandor.");
			break;
		case 2587:
			if (!player.getInventory().containsItem(DragonSlayer.MAGIC_PIECE) && !player.getBank().containsItem(DragonSlayer.MAGIC_PIECE)) {
				player.getDialogueInterpreter().open(3802875);
			} else {
				player.getPacketDispatch().sendMessage("You already have the map piece.");
			}
			break;
		case 25115:// magic door.
			DragonSlayer.handleMagicDoor(player, true);
			return true;
		case 747:// oziach.
			switch (quest.getStage(player)) {
			case 100:
				node.asNpc().openShop(player);
				break;
			case 20:
			case 30:
			case 40:
			case 15:
			case 10:
				player.getDialogueInterpreter().open(((NPC) node).getId(), ((NPC) node), true);
				break;
			default:
				player.getDialogueInterpreter().sendDialogues(((NPC) node), null, "I ain't got nothing to sell ye, adventurer. Leave me be!");
				break;
			}
			break;
		default:
			handleMelzarMaze(player, node, option, id, quest);
			break;
		}
		return true;
	}

	/**
	 * Method used to handle the melzar maze nodes.
	 * @param player the player.
	 * @param node the node.
	 * @param option the option.
	 * @param id the id.
	 * @param quest the quest.
	 * @return <code>True</code> if so.
	 */
	private final boolean handleMelzarMaze(final Player player, final Node node, final String option, final int id, final Quest quest) {
		switch (id) {
		case 2603:
			player.getPacketDispatch().sendMessage("You open the chest.");
			SceneryBuilder.replace(((Scenery) node), ((Scenery) node).transform(2604));
			break;
		case 2605:
			ClimbActionHandler.climb(player, new Animation(827), Location.create(2933, 9640, 0));
			break;
		case 2604:
			switch (option) {
			case "search":
				if (!player.getInventory().containsItem(DragonSlayer.MAZE_PIECE)) {
					if (!player.getInventory().add(DragonSlayer.MAZE_PIECE)) {
						GroundItemManager.create(DragonSlayer.MAZE_PIECE, player);
					}
					player.getDialogueInterpreter().sendItemMessage(DragonSlayer.MAZE_PIECE.getId(), "You find a map piece in the chest.");
				} else {
					player.getPacketDispatch().sendMessage("You find nothing in the chest.");
				}
				break;
			case "close":
				player.getPacketDispatch().sendMessage("You shut the chest.");
				SceneryBuilder.replace(((Scenery) node), ((Scenery) node).transform(2603));
				break;
			}
			break;
		case 2601:// green door.
			if (!player.getInventory().containsItem(DragonSlayer.GREEN_KEY)) {
				player.getPacketDispatch().sendMessage("This door is securely locked.");
			} else {
				player.getInventory().remove(DragonSlayer.GREEN_KEY);
				player.getPacketDispatch().sendMessage("The key disintegrates as it unlocks the door.");
				DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
				return true;
			}
			break;
		case 2600:// purple door.
			if (!player.getInventory().containsItem(DragonSlayer.PURPLE_KEY)) {
				player.getPacketDispatch().sendMessage("This door is securely locked.");
			} else {
				player.getInventory().remove(DragonSlayer.PURPLE_KEY);
				player.getPacketDispatch().sendMessage("The key disintegrates as it unlocks the door.");
				DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
				return true;
			}
			break;
		case 2599:// blue door.
			if (!player.getInventory().containsItem(DragonSlayer.BLUE_KEY)) {
				player.getPacketDispatch().sendMessage("This door is securely locked.");
			} else {
				player.getInventory().remove(DragonSlayer.BLUE_KEY);
				player.getPacketDispatch().sendMessage("The key disintegrates as it unlocks the door.");
				DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
				return true;
			}
			break;
		case 2598:// yellow door.
			if (!player.getInventory().containsItem(DragonSlayer.YELLOW_KEY)) {
				player.getPacketDispatch().sendMessage("This door is securely locked.");
			} else {
				player.getInventory().remove(DragonSlayer.YELLOW_KEY);
				player.getPacketDispatch().sendMessage("The key disintegrates as it unlocks the door.");
				DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
				return true;
			}
			break;
		case 25045:
			if (player.getLocation().getDistance(new Location(2925, 3259, 1)) < 3) {
				ClimbActionHandler.climb(player, new Animation(828), Location.create(2924, 3258, 0));
				return true;
			}
			ClimbActionHandler.climbLadder(player, (Scenery) node, option);
			return true;
		case 1747:// ladder with wrong spawn.
			if (player.getLocation().getDistance(new Location(2940, 3256, 1)) < 3) {
				ClimbActionHandler.climb(player, new Animation(828), Location.create(2940, 3256, 2));
				return true;
			}
			ClimbActionHandler.climbLadder(player, (Scenery) node, option);
			return true;
		case 25214:// trapdoor.
			player.getPacketDispatch().sendMessage("The trapdoor can only be opened from below.");
			break;
		case 25038:// melzar's maze trapdoor ladder
			ClimbActionHandler.climbLadder(player, (Scenery) node, option);
			return true;
		case 1752:
			player.getPacketDispatch().sendMessage("The ladder is broken, I can't climb it.");
			break;
		case 1746:
			if (player.getLocation().getDistance(Location.create(2923, 3241, 1)) < 3) {
				ClimbActionHandler.climb(player, new Animation(828), Location.create(2923, 3241, 0));
				return true;
			}
			if (player.getLocation().getDistance(Location.create(2932, 3245, 2)) < 3) {
				ClimbActionHandler.climb(player, new Animation(828), Location.create(2932, 3245, 1));
				return true;
			}
			ClimbActionHandler.climbLadder(player, (Scenery) node, option);
			return true;
		case 2596:// red door.
			if (!player.getInventory().containsItem(DragonSlayer.RED_KEY)) {
				player.getPacketDispatch().sendMessage("This door is securely locked.");
			} else {
				player.getInventory().remove(DragonSlayer.RED_KEY);
				player.getPacketDispatch().sendMessage("The key disintegrates as it unlocks the door.");
				DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
				return true;
			}
			break;
		case 2597:// orange door.
			if (!player.getInventory().containsItem(DragonSlayer.ORANGE_KEY)) {
				player.getPacketDispatch().sendMessage("This door is securely locked.");
			} else {
				player.getInventory().remove(DragonSlayer.ORANGE_KEY);
				player.getPacketDispatch().sendMessage("The key disintegrates as it unlocks the door.");
				DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
				return true;
			}
		case 32968:
		case 2602:
			if (player.getLocation().equals(new Location(2931, 9640, 0))) {
				DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
				return true;
			}
			if (player.getLocation().equals(new Location(2927, 9649, 0))) {
				DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
				return true;
			}
			if (player.getLocation().equals(Location.create(2924, 9654, 0)) || player.getLocation().equals(Location.create(2938, 3252, 0))) {
				DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
				return true;
			}
			player.getPacketDispatch().sendMessage("The door is locked.");
			break;
		case 2595:
			if (player.getLocation().equals(Location.create(2940, 3248, 0))) {
				DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
				return true;
			}
			if (player.getInventory().containsItem(DragonSlayer.MAZE_KEY)) {
				player.getPacketDispatch().sendMessage("You use the key and the door opens.");
				DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
				return true;
			} else {
				player.getPacketDispatch().sendMessage("This door is securely locked.");
			}
			break;
		}
		return true;
	}

	@Override
	public Location getDestination(Node node, Node n) {
		if (n instanceof Scenery) {
			Scenery obj = (Scenery) n;
			if (obj.getId() == 25115) {
				if (node.getLocation().getX() <= 3049) {
					return Location.create(3049, 9840, 0);
				} else {
					return Location.create(3051, 9840, 0);
				}
			} else if (obj.getId() == 2587) {
				return Location.create(3056, 9841, 0);
			}
		} else if (n instanceof NPC) {
			NPC npc = ((NPC) n);
			if (npc.getId() == 745) {
				return Location.create(3012, 3188, 0);
			}
		}
		return null;
	}

	@Override
	public boolean isWalk() {
		return false;
	}

	@Override
	public boolean isWalk(final Player player, Node node) {
		return !(node instanceof Item);
	}

}
