package core.game.world.repository;

import core.game.node.Node;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An implementation of an iterator for a node list.
 * @author Graham Edgecombe
 * @author Emperor
 * @param <E> The type of node.
 */
public class NodeListIterator<E extends Node> implements Iterator<E> {

	/**
	 * The nodes.
	 */
	private Node[] nodes;

	/**
	 * The entity list.
	 */
	private NodeList<E> entityList;

	/**
	 * The previous index.
	 */
	private int lastIndex = -1;

	/**
	 * The current index.
	 */
	private int cursor = 0;

	/**
	 * The size of the list.
	 */
	private int size;

	/**
	 * Creates an node list iterator.
	 * @param nodeList The node list.
	 */
	public NodeListIterator(NodeList<E> nodeList) {
		this.entityList = nodeList;
		nodes = nodeList.toArray(new Node[0]);
		size = nodes.length;
	}

	@Override
	public boolean hasNext() {
		while (cursor < size) {
			if (nodes[cursor] != null) {
				return true;
			}
			cursor++;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		lastIndex = cursor++;
		return (E) nodes[lastIndex];
	}

	@Override
	public void remove() {
		if (lastIndex == -1) {
			throw new IllegalStateException();
		}
		entityList.remove(nodes[lastIndex]);
	}

}
