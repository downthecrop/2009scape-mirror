package core.game.container;

/**
 * Represents a container listener.
 * @author Emperor
 */
public interface ContainerListener {

	/**
	 * Updates the changed item slots in the container.
	 * @param c The container we're listening to.
	 * @param event The container event.
	 */
	void update(Container c, ContainerEvent event);

	/**
	 * Updates the entire container.
	 * @param c The container.
	 */
	void refresh(Container c);
}