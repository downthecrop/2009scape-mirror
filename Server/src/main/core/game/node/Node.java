package core.game.node;

import core.game.interaction.DestinationFlag;
import core.game.interaction.InteractPlugin;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.tools.StringUtils;
import core.api.utils.Vector;

/**
 * Represents a node which is anything that is interactable in 2009Scape.
 * @author Emperor
 */
public abstract class Node {

	/**
	 * The name of the node;
	 */
	protected String name;

	/**
	 * The location.
	 */
	protected Location location;

	/**
	 * The index of the node.
	 */
	protected int index;

	/**
	 * The node's direction.
	 */
	protected Direction direction;

	/**
	 * The node's size.
	 */
	protected int size = 1;

	/**
	 * If the node is active.
	 */
	protected boolean active = true;

	/**
	 * The interaction instance.
	 */
	protected InteractPlugin interactPlugin;

	/**
	 * The destination flag.
	 */
	protected DestinationFlag destinationFlag;

	/**
	 * If the node is renderable.
	 */
	protected boolean renderable = true;

	/**
	 * Constructs a new {@code Node} {@code Object}.
	 * @param name The name.
	 * @param location The location.
	 */
	public Node(String name, Location location) {
		this.name = name;
		this.location = location;
	}

	/**
	 * Casts the npc to a player.
	 * @return the npc.
	 */
	public NPC asNpc() {
		return (NPC) this;
	}

	/**
	 * Casts the player.
	 * @return the player.
	 */
	public Player asPlayer() {
		return (Player) this;
	}

	/**
	 * Casts the scenery.
	 * @return the object.
	 */
	public Scenery asScenery() {
		return (Scenery) this;
	}

	/**
	 * Casts the item.
	 * @return the item.
	 */
	public Item asItem() {
		return (Item) this;
	}

	/**
	 * Gets the node id.
	 * @return the id.
	 */
	public int getId() {
		return this instanceof NPC ? ((NPC) this).getId() : this instanceof Scenery ? ((Scenery) this).getId() : this instanceof Item ? ((Item) this).getId() : -1;
	}

	/**
	 * Gets the node id hash (only relevant if the node is an item).
	 * @return the id hash.
	 */
	public int getIdHash() {
		return this instanceof Item ? ((Item) this).getIdHash() : -1;
	}

	/**
	 * Gets the center location.
	 * @return The center location.
	 */
	public Location getCenterLocation() {
		int offset = size >> 1;
		return location.transform(offset, offset, 0);
	}

        public Vector getMathematicalCenter() {
            Location topRight = location.transform(size - 1, size - 1, 0);
            double x = ((double) location.getX() + (double) topRight.getX()) / 2.0;
            double y = ((double) location.getY() + (double) topRight.getY()) / 2.0;
            return new Vector(x, y);
        }

        public Location getFaceLocation (Location fromLoc) {
            Vector center = getMathematicalCenter();
            Vector fromVec = new Vector((double) fromLoc.getX(), (double) fromLoc.getY());
            Vector difference = fromVec.minus(center);
            Vector end = center.plus(difference.invert());
            return Location.create((int)end.getX(), (int)end.getY(), fromLoc.getZ());
        }

	/**
	 * Gets the name of this node.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get a formated username.
	 * @return The username.
	 */
	public String getUsername() {
		return StringUtils.formatDisplayName(name);
	}

	/**
	 * Gets the index.
	 * @return The index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Sets the index.
	 * @param index The index to set.
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Gets the location.
	 * @return The location.
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 * @param location The location to set.
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * Gets the direction.
	 * @return The direction.
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * Sets the direction.
	 * @param direction The direction to set.
	 */
	public void setDirection(Direction direction) {
                if (direction == null)
                    return;
		this.direction = direction;
	}

	/**
	 * Gets the size.
	 * @return The size.
	 */
	public int size() {
		return size;
	}

	/**
	 * Sets the size.
	 * @param size The size to set.
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Gets the active.
	 * @return The active.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the active.
	 * @param active The active to set.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Gets the interaction.
	 * @return The interaction.
	 */
	public InteractPlugin getInteraction() {
		if (interactPlugin != null && !interactPlugin.isInitialized()) {
			interactPlugin.setDefault();
		}
		return interactPlugin;
	}

	/**
	 * Sets the interaction.
	 * @param interactPlugin The interaction to set.
	 */
	public void setInteraction(InteractPlugin interactPlugin) {
		this.interactPlugin = interactPlugin;
	}

	/**
	 * Gets the destinationFlag.
	 * @return The destinationFlag.
	 */
	public DestinationFlag getDestinationFlag() {
		return destinationFlag;
	}

	/**
	 * Sets the destinationFlag.
	 * @param destinationFlag The destinationFlag to set.
	 */
	public void setDestinationFlag(DestinationFlag destinationFlag) {
		this.destinationFlag = destinationFlag;
	}

	/**
	 * Gets the renderable.
	 * @return The renderable.
	 */
	public boolean isRenderable() {
		return renderable;
	}

	/**
	 * Sets the renderable.
	 * @param renderable The renderable to set.
	 */
	public void setRenderable(boolean renderable) {
		this.renderable = renderable;
	}
}
