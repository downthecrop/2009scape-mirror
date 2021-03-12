package core.game.container;

/**
 * Represents the container types.
 * @author Emperor
 */
public enum ContainerType {

	/**
	 * The default container type.
	 */
	DEFAULT,

	/**
	 * If the container is used for a shop.
	 */
	SHOP,

	/**
	 * The container should always stack items.
	 */
	ALWAYS_STACK,

	/**
	 * The container should never stack items.
	 */
	NEVER_STACK;

}