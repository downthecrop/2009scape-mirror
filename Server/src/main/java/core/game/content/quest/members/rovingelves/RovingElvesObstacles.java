package core.game.content.quest.members.rovingelves;

import static api.ContentAPIKt.*;
import core.cache.def.impl.SceneryDefinition;
import core.game.node.entity.skill.agility.AgilityHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;
import java.util.Arrays;
import java.util.List;

/**
 * Handles all the agility obstacles for Roving Elves.
 * @authors Splinter & downthecrop
 */
public final class RovingElvesObstacles extends OptionHandler {

	//Messages
	private String LEAF_SUCCESS_MSG = "You safely jump across.";
	private String LEAF_LADDER_MSG = "You climb out of the pit.";
	private String STICK_SUCCESS_MSG = "You manage to skillfully pass the trap.";
	private String WIRE_SUCCESS_MSG = "You successfully step over the tripwire.";

	private	final Animation OVER = new Animation(839);
	private	final Animation THROUGH = new Animation(1237);
	private	final Animation STICK_TRAP = new Animation(819);
	private	final Animation LEAF_TRAP = new Animation(1115);
	private	final Animation WIRE_TRAP = new Animation(1236);

	private	final Location STICK_TRAP_NORTH = new Location(0,2,0);
	private	final Location STICK_TRAP_SOUTH = new Location(0,-1,0);
	private	final Location STICK_TRAP_EAST = new Location(2,0,0);
	private	final Location STICK_TRAP_WEST = new Location(-1,0,0);

	private	final Location WIRE_TRAP_NORTH = new Location(0,2,0);
	private	final Location WIRE_TRAP_SOUTH = new Location(0,-1,0);
	private	final Location WIRE_TRAP_EAST = new Location(2,0,0);
	private	final Location WIRE_TRAP_WEST = new Location(-1,0,0);

	private	final Location FOREST_NORTH = new Location(0,2,0);
	private	final Location FOREST_SOUTH = new Location(0,-1,0);
	private	final Location FOREST_EAST = new Location(2,0,0);
	private	final Location FOREST_WEST = new Location(-1,0,0);

	private final Location LEAF_TRAP_CLIMB = new Location(2274, 3172, 0);

	private final List<Integer> illegalJumpsY = Arrays.asList(3174);

	private Location nodeCenter(Node node){
		if(node.asScenery().getRotation() % 2 == 0)
			return node.getLocation().transform(1,0,0);
		return node.getLocation().transform(0,1,0);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(3922).getHandlers().put("option:pass", this);
		SceneryDefinition.forId(3999).getHandlers().put("option:enter", this);
		SceneryDefinition.forId(3939).getHandlers().put("option:enter", this);
		SceneryDefinition.forId(3921).getHandlers().put("option:step-over", this);
		SceneryDefinition.forId(3998).getHandlers().put("option:enter", this);
		SceneryDefinition.forId(3938).getHandlers().put("option:enter", this);
		SceneryDefinition.forId(3937).getHandlers().put("option:enter", this);
		SceneryDefinition.forId(3924).getHandlers().put("option:jump", this);
		SceneryDefinition.forId(3925).getHandlers().put("option:jump", this);
		SceneryDefinition.forId(8742).getHandlers().put("option:pass", this);
		SceneryDefinition.forId(3927).getHandlers().put("option:climb",this);
		return this;
	}


	@Override
	public boolean handle(final Player player, Node node, String option) {

		player.lock(5);
		player.faceLocation(node.asScenery().getLocation());
		Boolean isNorthOrSouth = true;

		if (node.getId() == 3922 && node.asScenery().getRotation() % 2 == 0){
			//Facing East or West Stick Trap
			isNorthOrSouth = false;
		} else if (node.asScenery().getRotation() % 2 != 0) {
			isNorthOrSouth = false;
		}

		// Direction of the NODE in relation to the player
		Direction NORTH_SOUTH = player.getLocation().getY() <= node.getLocation().getY() ? Direction.NORTH : Direction.SOUTH;
		Direction EAST_WEST = player.getLocation().getX() <= node.getLocation().getX() ? Direction.EAST : Direction.WEST;

		switch (node.getId()) {
			case 8742:
				player.teleport(player.getLocation().transform(EAST_WEST, 2));
				break;
			case 3999:
				AgilityHandler.forceWalk(player, -1, player.getLocation(),
						player.getLocation().transform(NORTH_SOUTH, 3), OVER, 25, 0, null);
				break;
			//Wire Trap
			case 3921:
				if (isNorthOrSouth) {
					if (NORTH_SOUTH == Direction.NORTH)
						AgilityHandler.forceWalk(player, -1, node.getLocation().transform(WIRE_TRAP_SOUTH),
								node.getLocation().transform(WIRE_TRAP_NORTH), WIRE_TRAP, 100, 0, WIRE_SUCCESS_MSG);
					else
						AgilityHandler.forceWalk(player, -1, node.getLocation().transform(WIRE_TRAP_NORTH),
								node.getLocation().transform(WIRE_TRAP_SOUTH), WIRE_TRAP, 100, 0, WIRE_SUCCESS_MSG);
				} else {
					if (EAST_WEST == Direction.EAST)
						AgilityHandler.forceWalk(player, -1, node.getLocation().transform(WIRE_TRAP_WEST),
								node.getLocation().transform(WIRE_TRAP_EAST), WIRE_TRAP, 100, 0, WIRE_SUCCESS_MSG);
					else
						AgilityHandler.forceWalk(player, -1, node.getLocation().transform(WIRE_TRAP_EAST),
								node.getLocation().transform(WIRE_TRAP_WEST), WIRE_TRAP, 100, 0, WIRE_SUCCESS_MSG);
				}
				break;
			//Stick Trap
			case 3922:
				if (isNorthOrSouth) {
					if (NORTH_SOUTH == Direction.NORTH)
						AgilityHandler.forceWalk(player, -1, node.getLocation(),
								node.getLocation().transform(STICK_TRAP_NORTH), STICK_TRAP, 25, 0, STICK_SUCCESS_MSG);
					else
						AgilityHandler.forceWalk(player, -1, node.getLocation(),
								node.getLocation().transform(STICK_TRAP_SOUTH), STICK_TRAP, 25, 0, STICK_SUCCESS_MSG);
				} else {
					if (EAST_WEST == Direction.EAST)
						AgilityHandler.forceWalk(player, -1, node.getLocation().transform(STICK_TRAP_WEST),
								node.getLocation().transform(STICK_TRAP_EAST), STICK_TRAP, 25, 0, STICK_SUCCESS_MSG);
					else
						AgilityHandler.forceWalk(player, -1, node.getLocation().transform(STICK_TRAP_EAST),
								node.getLocation().transform(STICK_TRAP_WEST), STICK_TRAP, 25, 0, STICK_SUCCESS_MSG);
				}
				break;
			// Dense Forest
			case 3998:
			case 3939:
			case 3938:
				if (isNorthOrSouth) {
					if (NORTH_SOUTH == Direction.NORTH)
						AgilityHandler.forceWalk(player, -1, nodeCenter(node).transform(FOREST_SOUTH),
								nodeCenter(node).transform(FOREST_NORTH), THROUGH, 25, 0, null);
					else
						AgilityHandler.forceWalk(player, -1, nodeCenter(node).transform(FOREST_NORTH),
								nodeCenter(node).transform(FOREST_SOUTH), THROUGH, 25, 0, null);
				} else {
					if (EAST_WEST == Direction.EAST)
						AgilityHandler.forceWalk(player, -1, nodeCenter(node).transform(FOREST_WEST),
								nodeCenter(node).transform(FOREST_EAST), THROUGH, 25, 0, null);
					else
						AgilityHandler.forceWalk(player, -1, nodeCenter(node).transform(FOREST_EAST),
								nodeCenter(node).transform(FOREST_WEST), THROUGH, 25, 0, null);
				}
				break;
			// Climb Over Dense Forest
			case 3937:
				if (isNorthOrSouth) {
					if (NORTH_SOUTH == Direction.NORTH)
						AgilityHandler.forceWalk(player, -1, nodeCenter(node).transform(FOREST_SOUTH),
								nodeCenter(node).transform(FOREST_NORTH), OVER, 25, 0, null);
					else
						AgilityHandler.forceWalk(player, -1, nodeCenter(node).transform(FOREST_NORTH),
								nodeCenter(node).transform(FOREST_SOUTH), OVER, 25, 0, null);
				} else {
					if (EAST_WEST == Direction.EAST)
						AgilityHandler.forceWalk(player, -1, nodeCenter(node).transform(FOREST_WEST),
								nodeCenter(node).transform(FOREST_EAST), OVER, 25, 0, null);
					else
						AgilityHandler.forceWalk(player, -1, nodeCenter(node).transform(FOREST_EAST),
								nodeCenter(node).transform(FOREST_WEST), OVER, 25, 0, null);
				}
				break;
			// Leaf Trap Center
			case 3924:
				if(!illegalJumpsY.contains(player.getLocation().getY())){
					if (isNorthOrSouth) {
						AgilityHandler.forceWalk(player, -1, node.getLocation().transform(NORTH_SOUTH,-1),
								node.getLocation().transform(NORTH_SOUTH, 2), LEAF_TRAP, 25, 0, LEAF_SUCCESS_MSG);
					} else {
						AgilityHandler.forceWalk(player, -1, node.getLocation().transform(EAST_WEST,-2),
								node.getLocation().transform(EAST_WEST, 2), LEAF_TRAP, 25, 0, LEAF_SUCCESS_MSG);
					}
				}
				break;
			// Leaf Trap Edges
			case 3925:
				if(!illegalJumpsY.contains(node.getLocation().getY())){
					if (!isNorthOrSouth) {
						AgilityHandler.forceWalk(player, -1, node.getLocation().transform(NORTH_SOUTH,-1),
								node.getLocation().transform(NORTH_SOUTH, 3), LEAF_TRAP, 25, 0, LEAF_SUCCESS_MSG);
					} else {
						AgilityHandler.forceWalk(player, -1, nodeCenter(node).transform(EAST_WEST,-1),
								node.getLocation().transform(EAST_WEST, 3), LEAF_TRAP, 25, 0, LEAF_SUCCESS_MSG);
					}
				}
				break;
			case 3927:
				player.teleport(LEAF_TRAP_CLIMB);
				sendMessage(player,LEAF_LADDER_MSG);
				break;
			}

		return true;
	}

	@Override
	public Location getDestination(Node node, Node n) {
		switch (node.getId()) {
		case 3999:
			return new Location(2188, 3162);
		case 3998:
			return new Location(2188, 3171);
		}
		return null;
	}

	@Override
	public boolean isWalk(Player player, Node node) {
		return !(node instanceof Item);
	}

	@Override
	public boolean isWalk() {
		return false;
	}

}
