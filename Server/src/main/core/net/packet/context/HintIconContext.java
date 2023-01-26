package core.net.packet.context;

import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.net.packet.Context;

/**
 * Represents the hint icon packet context.
 * @author Emperor
 */
public final class HintIconContext implements Context {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The hint icon slot.
	 */
	private final int slot;

	/**
	 * The target type.
	 */
	private int targetType;

	/**
	 * The arrow id.
	 */
	private int arrowId;

	/**
	 * The entity index.
	 */
	private final int index;

	/**
	 * The model id.
	 */
	private final int modelId;

	/**
	 * The location.
	 */
	private final Location location;

	/**
	 * The height.
	 */
	private int height;

	/**
	 * Constructs a new {@code HintIconContext} {@code Object}. <br> This hint
	 * icon is entity based.
	 * @param player The player.
	 * @param slot The hint icon slot.
	 * @param arrowId The arrow id.
	 * @param target The target.
	 * @param modelId The model id.
	 */
	public HintIconContext(Player player, int slot, int arrowId, Node target, int modelId) {
		this(player, slot, arrowId, -1, target, modelId);
		targetType = 2;
		if (target instanceof Entity) {
			targetType = target instanceof Player ? 10 : 1;
		}
	}

	/**
	 * Constructs a new {@code HintIconContext} {@code Object}. <br> This hint
	 * icon is entity based.
	 * @param player The player.
	 * @param slot The hint icon slot.
	 * @param arrowId The arrow id.
	 * @param targetType The target type.
	 * @param target The index of the entity.
	 * @param modelId The model id.
	 */
	public HintIconContext(Player player, int slot, int arrowId, int targetType, Node target, int modelId) {
		this(player, slot, arrowId, targetType, target, modelId, 0);
	}

	/**
	 * Constructs a new {@code HintIconContext} {@code Object}. <br> This hint
	 * icon is entity based.
	 * @param player The player.
	 * @param slot The hint icon slot.
	 * @param arrowId The arrow id.
	 * @param targetType The target type.
	 * @param target The index of the entity.
	 * @param modelId The model id.
	 * @param height The height.
	 */
	public HintIconContext(Player player, int slot, int arrowId, int targetType, Node target, int modelId, int height) {
		this.player = player;
		this.slot = slot;
		this.targetType = targetType;
		this.arrowId = arrowId;
		this.modelId = modelId;
		this.height = height;
		if (target instanceof Entity) {
			this.index = ((Entity) target).getIndex();
			this.location = null;
		} else {
			this.location = target.getLocation();
			this.index = -1;
		}
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the slot.
	 * @return The slot.
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * Gets the targetType.
	 * @return The targetType.
	 */
	public int getTargetType() {
		return targetType;
	}

	/**
	 * Gets the arrowId.
	 * @return The arrowId.
	 */
	public int getArrowId() {
		return arrowId;
	}

	/**
	 * Gets the index.
	 * @return The index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets the modelId.
	 * @return The modelId.
	 */
	public int getModelId() {
		return modelId;
	}

	/**
	 * Gets the location.
	 * @return The location.
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Sets the targetType.
	 * @param targetType The targetType to set.
	 */
	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}

	/**
	 * Sets the arrowId.
	 * @param arrowId The arrowId to set.
	 */
	public void setArrowId(int arrowId) {
		this.arrowId = arrowId;
	}

	/**
	 * Gets the height.
	 * @return The height.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the height.
	 * @param height The height to set.
	 */
	public void setHeight(int height) {
		this.height = height;
	}

}