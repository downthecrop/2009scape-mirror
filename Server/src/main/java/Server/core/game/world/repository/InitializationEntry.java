package core.game.world.repository;

import core.game.node.Node;

/**
 * Wraps around a node to represent its entry in a queue.
 * @author Emperor
 */
public class InitializationEntry {

	/**
	 * The node.
	 */
	private final Node node;

	/**
	 * If the node is being removed from the game, rather than added.
	 */
	private boolean removal;

	/**
	 * Constructs a new {@code InitializationEntry} {@code Object}.
	 * @param node The node.
	 */
	public InitializationEntry(Node node) {
		this(node, false);
	}

	/**
	 * Constructs a new {@code InitializationEntry} {@code Object}.
	 * @param node The node.
	 * @param removal If the node should be removed from the game, rather than
	 * added.
	 */
	public InitializationEntry(Node node, boolean removal) {
		this.node = node;
		this.removal = removal;
	}

	/**
	 * Initializes the node.
	 * @return The node instance.
	 */
	public Node initialize() {
		node.setActive(true);
		return node;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof InitializationEntry)) {
			return false;
		}
		return ((InitializationEntry) o).node.equals(node);
	}

	@Override
	public int hashCode() {
		if (node != null) {
			return node.hashCode();
		}
		return super.hashCode();
	}

	/**
	 * Gets the node.
	 * @return The node.
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * Gets the removal.
	 * @return The removal.
	 */
	public boolean isRemoval() {
		return removal;
	}

	/**
	 * Sets the removal.
	 * @param removal The removal to set.
	 */
	public void setRemoval(boolean removal) {
		this.removal = removal;
	}
}