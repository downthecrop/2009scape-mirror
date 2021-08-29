package core.game.node.scenery;

import core.game.node.item.Item;
import core.game.world.map.Location;

/**
 * Represents a constructed object.
 * @author Emperor
 */
public class Constructed extends Scenery {
	
	/**
	 * The scenery we've replaced.
	 */
	private Scenery replaced;
	
	/**
	 * The ground items places after the tick is up.
	 */
	private Item[] items;
	
	/**
	 * Constructs a new Constructed object.
	 * @param id The object id.
	 * @param x The object x-coordinate.
	 * @param y The object y-coordinate.
	 * @param z The object z-coordinate.
	 */
	public Constructed(int id, int x, int y, int z) {
		super(id, Location.create(x, y, z), 10, 0);
	}
	
	/**
	 * Constructs a new Constructed object.
	 * @param id The object id.
	 * @param location The object's location.
	 */
	public Constructed(int id, Location location) {
		super(id, location, 10, 0);
	}
	
	/**
	 * Constructs a new Constructed object.
	 * @param id The object id.
	 * @param location The object's location.
	 * @param rotation The object's rotation.
	 */
	public Constructed(int id, Location location, int rotation) {
		super(id, location, 10, rotation);
	}

	/**
	 * Constructs a new {@code Constructed} {@code Object}.
	 * @param id The object id.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @param type The object type.
	 * @param rotation The rotation.
	 */
	public Constructed(int id, int x, int y, int z, int type, int rotation) {
		super(id, Location.create(x, y, z), type, rotation);
	}
	
	/**
	 * Constructs a new {@code Constructed} {@code Object}.
	 * @param id The object id.
	 * @param type The object type.
	 * @param rotation The rotation.
	 */
	public Constructed(int id, int type, int rotation) {
		super(id, Location.create(0, 0, 0), type, rotation);
	}
	
	/**
	 * Constructs a new {@code Constructed} {@code Object}.
	 * @param id The object id.
	 * @param location The location.
	 * @param type The object type.
	 * @param rotation The rotation.
	 */
	public Constructed(int id, Location location, int type, int rotation) {
		super(id, location, type, rotation);
	}
	
	@Override
	public boolean isPermanent() {
		return false;
	}
	
	@Override
	public Constructed asConstructed() {
		return this;
	}
	
	/**
	 * Gets the replaced.
	 * @return The replaced.
	 */
	public Scenery getReplaced() {
		return replaced;
	}

	/**
	 * Sets the replaced.
	 * @param replaced The replaced to set.
	 */
	public void setReplaced(Scenery replaced) {
		this.replaced = replaced;
	}

	/**
	 * @return the items
	 */
	public Item[] getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(Item[] items) {
		this.items = items;
	}
	
}