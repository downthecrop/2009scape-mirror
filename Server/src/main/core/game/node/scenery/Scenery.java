package core.game.node.scenery;

import core.cache.def.impl.VarbitDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.DestinationFlag;
import core.game.interaction.Interaction;
import core.game.node.Node;
import core.game.node.entity.impl.GameAttributes;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a scenery.
 * @author Emperor
 */
public class Scenery extends Node {

	/**
	 * The object id.
	 */
	private final int id;

	/**
	 * Object's type.
	 */
	private final int type;
	
	/**
	 * The rotation.
	 */
	private int rotation;

	/**
	 * The object's definition.
	 */
	private final SceneryDefinition definition;
	
	/**
	 * The restore pulse.
	 */
	private Pulse restorePulse;
	
	/**
	 * The destruction pulse.
	 */
	private Pulse destructionPulse;
	
	/**
	 * The charge of this object.
	 */
	private int charge = 1000;
	
	/**
	 * The entity's attributes.
	 */
	private final GameAttributes attributes = new GameAttributes();
	
	/**
	 * The child objects.
	 */
	private final Scenery[] childs;
	
	/**
	 * The scenery wrapper (used for object configurations).
	 */
	private Scenery wrapper;
	
	/**
	 * Constructs a new scenery.
	 * @param id The object id.
	 * @param x The object x-coordinate.
	 * @param y The object y-coordinate.
	 * @param z The object z-coordinate.
	 */
	public Scenery(int id, int x, int y, int z) {
		this(id, Location.create(x, y, z), 10, 0);
	}

	/**
	 * Constructs a new scenery.
	 * @param id The object id.
	 * @param location The object's location.
	 */
	public Scenery(int id, Location location) {
		this(id, location, 10, 0);
	}
	
	/**
	 * Constructs a new scenery.
	 * @param id The object id.
	 * @param location The object's location.
	 * @param rotation The object's rotation.
	 */
	public Scenery(int id, Location location, int rotation) {
		this(id, location, 10, rotation);
	}
	
	public Scenery(int id, Location location, int rotation, Direction direction) {
		this(id, location, 10, rotation);
	}

	/**
	 * Constructs a new {@code GameObject} {@code Object}.
	 * @param id The object id.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @param type The object type.
	 * @param rotation The rotation.
	 */
	public Scenery(int id, int x, int y, int z, int type, int rotation) {
		this(id, Location.create(x, y, z), type, rotation);
	}
	
	/**
	 * Constructs a new {@code GameObject} {@code Object}.
	 * @param id The object id.
	 * @param type The object type.
	 * @param rotation The rotation.
	 */
	public Scenery(int id, int type, int rotation) {
		this(id, Location.create(0, 0, 0), type, rotation);
	}
	
	/**
	 * Constructs a new {@code GameObject} {@code Object}.
	 * @param id The object id.
	 * @param location The location.
	 * @param type The object type.
	 * @param rotation The rotation.
	 */
	public Scenery(int id, Location location, int type, int rotation) {
		super(SceneryDefinition.forId(id).getName(), location);
		if (rotation < 0) {
			rotation += 4;
		}
		if (id < 1) {
			type = 22;
		}
		super.destinationFlag = DestinationFlag.OBJECT;
		super.direction = Direction.get(rotation);
		super.interaction = new Interaction(this);
		this.rotation = rotation;
		this.id = id;
		this.location = location;
		this.type = type;
		this.definition = SceneryDefinition.forId(id);
		super.size = definition.sizeX;
		if (definition.childrenIds != null && definition.childrenIds.length > 0) {
			this.childs = new Scenery[definition.childrenIds.length];
			for (int i = 0; i < childs.length; i++) {
				childs[i] = transform(definition.childrenIds[i]);
				childs[i].wrapper = this;
			}
		} else {
			childs = null;
		}
	}

    public Scenery(Scenery other) {
        this(other.getId(), other.getLocation(), other.getType(), other.getRotation());
    }
	
	/**
	 * Called when an object is removed.
	 */
	public void remove() {};
	
	/**
	 * Gets the current x-size.
	 * @return The current size.
	 */
	public int getSizeX() {
		if (direction.toInteger() % 2 != 0) {
			return definition.sizeY;
		}
		return definition.sizeX;
	}
	
	/**
	 * Gets the current y-size.
	 * @return The current size.
	 */
	public int getSizeY() {
		if (direction.toInteger() % 2 != 0) {
			return definition.sizeX;
		}
		return definition.sizeY;
	}
	
	@Override
	public void setActive(boolean active) {
		if (super.active && !active && destructionPulse != null) {
			destructionPulse.pulse();
		}
		super.setActive(active);
	}

	/**
	 * Gets the child object shown for the current player.
	 * @param player The player.
	 * @return The child object.
	 */
	public Scenery getChild(Player player) {
		if (childs != null) {
			SceneryDefinition def = definition.getChildObject(player);
			for (Scenery child : childs) {
				if (child.getId() == def.getId()) {
					return child;
				}
			}
		}
		return this;
	}
	
	/**
	 * Sets the child object index.
	 * @param player The player.
	 * @param index The child object.
	 */
	public void setChildIndex(Player player, int index) {
		SceneryDefinition def = getDefinition();
		if (childs == null && wrapper != null) {
			def = wrapper.getDefinition();
		}
		if (def.getVarbitID() > -1) {
			VarbitDefinition config = def.getConfigFile();
			if (config != null) {
				player.varpManager.get(config.getVarpId()).setVarbit(config.getStartBit(),index).send(player);
			}
		} else if (def.getConfigId() > -1) {
			player.varpManager.get(def.getConfigId()).setVarbit(0,index);
		}
	}

	/**
	 * Gets a transformed object of this object.
	 * @param id The new object id.
	 * @return The constructed scenery.
	 */
	public Scenery transform(int id) {
		return new Scenery(id, location, type, rotation);
	}

	/**
	 * Gets a transformed object of this object.
	 * @param id The new object id.
	 * @param rotation The new rotation.
	 * @return The constructed scenery.
	 */
	public Scenery transform(int id, int rotation) {
		return new Scenery(id, location, type, rotation);
	}

	/**
	 * Gets a transformed object of this object.
	 * @param id The new object id.
	 * @param rotation The new rotation.
	 * @param location The new location.
	 * @return The constructed scenery.
	 */
	public Scenery transform(int id, int rotation, Location location) {
		return new Scenery(id, location, type, rotation);
	}
	
	/**
	 * Gets a transformed object of this object.
	 * @param id The new object id.
	 * @param rotation The new rotation.
	 * @param type The object type.
	 * @return The constructed scenery.
	 */
	public Scenery transform(int id, int rotation, int type) {
		return new Scenery(id, location, type, rotation);
	}
	
	/**
	 * If the object is permanent.
	 * @return {@code True} if so.
	 */
	public boolean isPermanent() {
		return true;
	}

	/**
	 * Gets this scenery as Constructed object.
	 * @return The {@link Constructed} object.
	 */
	public Constructed asConstructed() {
		return new Constructed(id, location, type, rotation);
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the rotation
	 */
	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rot) {
		rotation = rot;
	}

	/**
	 * @return the location
	 */
	@Override
	public Location getLocation() {
		return location;
	}

	/**
	 * Gets the definition.
	 * @return The definition.
	 */
	public SceneryDefinition getDefinition() {
		return definition;
	}
	
	@Override
	public Location getCenterLocation() {
		return location.transform(getSizeX() >> 1, getSizeY() >> 1, 0);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Scenery)) {
			return false;
		}
		Scenery other = (Scenery) obj;
		return other.id == id && other.location.equals(location) && rotation == other.rotation && other.type == type;
	}
	
	@Override
	public String toString() {
		return "[Scenery " + id + ", " + location + ", type=" + type + ", rot=" + rotation + "]";
	}

	/**
	 * Gets the restorePulse.
	 * @return The restorePulse.
	 */
	public Pulse getRestorePulse() {
		return restorePulse;
	}

	/**
	 * Sets the restorePulse.
	 * @param restorePulse The restorePulse to set.
	 */
	public void setRestorePulse(Pulse restorePulse) {
		this.restorePulse = restorePulse;
	}

	/**
	 * Gets the charge.
	 * @return The charge.
	 */
	public int getCharge() {
		return charge;
	}

	/**
	 * Sets the charge.
	 * @param charge The charge to set.
	 */
	public void setCharge(int charge) {
		this.charge = charge;
	}

	/**
	 * Gets the destructionPulse.
	 * @return The destructionPulse.
	 */
	public Pulse getDestructionPulse() {
		return destructionPulse;
	}

	/**
	 * Sets the destructionPulse.
	 * @param destructionPulse The destructionPulse to set.
	 */
	public void setDestructionPulse(Pulse destructionPulse) {
		this.destructionPulse = destructionPulse;
	}

	/**
	 * @return the attributes.
	 */
	public GameAttributes getAttributes() {
		return attributes;
	}

	/**
	 * Gets the childs.
	 * @return The childs.
	 */
	public Scenery[] getChilds() {
		return childs;
	}

	/**
	 * Gets the wrapper.
	 * @return The wrapper.
	 */
	public Scenery getWrapper() {
		if (wrapper == null) {
			return this;
		}
		return wrapper;
	}

	/**
	 * Sets the wrapper.
	 * @param wrapper The wrapper to set.
	 */
	public void setWrapper(Scenery wrapper) {
		this.wrapper = wrapper;
	}

	@SuppressWarnings("SuspiciousNameCombination")
	@NotNull
	public List<Location> getOccupiedTiles() {
		List<Location> occupied = new ArrayList<>();
		occupied.add(location);

		int sizeX = getSizeX();
		int sizeY = getSizeY();

		if (rotation % 2 == 1) {
			int tmp = sizeX;
			sizeX = sizeY;
			sizeY = tmp;
		}

		boolean sub = rotation >= 2;

		if (sizeX > 1) {
			for (int i = 1; i < sizeX; i++) {
				int modifier = sub ? -i : i;
				occupied.add(location.transform(modifier, 0, 0));
			}
		}

		if (sizeY > 1) {
			for (int i = 1; i < sizeY; i++) {
				int modifier = sub ? -i : i;
				occupied.add(location.transform(0, modifier, 0));
			}
		}

		return occupied;
	}
}
