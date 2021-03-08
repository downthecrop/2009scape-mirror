package core.game.node.entity.skill.summoning.familiar;

import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.item.Item;

/**
 * A class which represents a familiar special.
 * @author Aero
 */
public class FamiliarSpecial {

	/**
	 * The node.
	 */
	private Node node;

	/**
	 * The interface id.
	 */
	private int interfaceID;

	/**
	 * The component.
	 */
	private int component;

	/**
	 * The item.
	 */
	private Item item;

	/**
	 * Constructs a new {@code FamiliarSpecial} {@code Object}.
	 * @param node The node.
	 */
	public FamiliarSpecial(Node node) {
		this(node, -1, -1, null);
	}

	/**
	 * Constructs a new {@code FamiliarSpecial} {@code Object}.
	 * @param node The node.
	 * @param interfaceID The interface id.
	 * @param component The component.
	 * @param item The item.
	 */
	public FamiliarSpecial(Node node, int interfaceID, int component, Item item) {
		this.node = node;
		this.interfaceID = interfaceID;
		this.component = component;
		this.item = item;
	}

	/**
	 * Gets the node.
	 * @return The node.
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * Sets the node.
	 * @param node The node to set.
	 */
	public void setNode(Node node) {
		this.node = node;
	}

	/**
	 * Gets the interfaceID.
	 * @return The interfaceID.
	 */
	public int getInterfaceID() {
		return interfaceID;
	}

	/**
	 * Sets the interfaceID.
	 * @param interfaceID The interfaceID to set.
	 */
	public void setInterfaceID(int interfaceID) {
		this.interfaceID = interfaceID;
	}

	/**
	 * Gets the component.
	 * @return The component.
	 */
	public int getComponent() {
		return component;
	}

	/**
	 * Sets the component.
	 * @param component The component to set.
	 */
	public void setComponent(int component) {
		this.component = component;
	}

	/**
	 * Gets the item.
	 * @return The item.
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * Sets the item.
	 * @param item The item to set.
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * Gets the target.
	 * @return the target.
	 */
	public Entity getTarget() {
		return (Entity) node;
	}

}