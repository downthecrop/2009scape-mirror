package core.game.node.entity.player.link;

import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.net.packet.PacketRepository;
import core.net.packet.context.HintIconContext;
import core.net.packet.out.HintIcon;

/**
 * The player's hint icon manager.
 * @author Emperor
 */
public final class HintIconManager {

	/**
	 * The maximum size of hint icons.
	 */
	public static final int MAXIMUM_SIZE = 8;

	/**
	 * The default arrow id.
	 */
	public static final int DEFAULT_ARROW = 1;

	/**
	 * The default model id.
	 */
	public static final int DEFAULT_MODEL = -1;

	/**
	 * The arrow in a circle model id.
	 */
	public static final int ARROW_CIRCLE_MODEL = 40497;

	/**
	 * The hint icons.
	 */
	private HintIconContext[] hintIcons = new HintIconContext[MAXIMUM_SIZE];

	/**
	 * Constructs a new {@code HintIconManager} {@code Object}.
	 */
	public HintIconManager() {
		/*
		 * empty.
		 */
	}

	/**
	 * Registers a new hint icon.
	 * @param player The player.
	 * @param target The entity target.
	 * @return The slot of the hint icon.
	 */
	public static int registerHintIcon(Player player, Node target) {
		return registerHintIcon(player, target, DEFAULT_ARROW, DEFAULT_MODEL, player.getHintIconManager().freeSlot());
	}

	/**
	 * Registers a new hint icon.
	 * @param player The player.
	 * @param target The entity target.
	 * @return The slot of the hint icon.
	 */
	public static int registerHeightHintIcon(Player player, Node target, int height) {
		return registerHintIcon(player, target, DEFAULT_ARROW, DEFAULT_MODEL, player.getHintIconManager().freeSlot(), height);
	}

	/**
	 * Registers a new hint icon.
	 * @param player The player.
	 * @param target The entity target.
	 * @param arrowId The arrow id to use.
	 * @return The slot of the hint icon.
	 */
	public static int registerHintIcon(Player player, Node target, int arrowId) {
		return registerHintIcon(player, target, arrowId, DEFAULT_MODEL, player.getHintIconManager().freeSlot());
	}

	/**
	 * Registers a new hint icon.
	 * @param player The player.
	 * @param target The entity target.
	 * @param arrowId The arrow id to use.
	 * @param modelId The model id.
	 * @return The slot of the hint icon.
	 */
	public static int registerHintIcon(Player player, Node target, int arrowId, int modelId) {
		return registerHintIcon(player, target, arrowId, modelId, player.getHintIconManager().freeSlot());
	}

	/**
	 * Registers a new hint icon.
	 * @param player The player.
	 * @param target The entity target.
	 * @param arrowId The arrow id to use.
	 * @param modelId The model id.
	 * @param slot The slot.
	 * @return The slot of the hint icon.
	 */
	public static int registerHintIcon(Player player, Node target, int arrowId, int modelId, int slot) {
		if (slot < 0) {
			return -1;
		}
		if (target == null) {
			return 0;
		}
		HintIconManager mng = player.getHintIconManager();
		HintIconContext icon = new HintIconContext(player, slot, arrowId, target, modelId);
		PacketRepository.send(HintIcon.class, icon);
		mng.hintIcons[slot] = icon;
		return slot;
	}

	/**
	 * Registers a new hint icon.
	 * @param player The player.
	 * @param target The entity target.
	 * @param arrowId The arrow id to use.
	 * @param modelId The model id.
	 * @param slot The slot.
	 * @param height The height of the hint icon.
	 * @return The slot of the hint icon.
	 */
	public static int registerHintIcon(Player player, Node target, int arrowId, int modelId, int slot, int height) {
		int type = 2;
		if (target instanceof Entity) {
			type = target instanceof Player ? 10 : 1;
		}
		return registerHintIcon(player, target, arrowId, modelId, slot, height, type);
	}

	/**
	 * Registers a new hint icon.
	 * @param player The player.
	 * @param target The entity target.
	 * @param arrowId The arrow id to use.
	 * @param modelId The model id.
	 * @param slot The slot.
	 * @param height The height of the hint icon.
	 * @param targetType The target type.
	 * @return The slot of the hint icon.
	 */
	public static int registerHintIcon(Player player, Node target, int arrowId, int modelId, int slot, int height, int targetType) {
		if (slot < 0) {
			return -1;
		}
		HintIconManager mng = player.getHintIconManager();
		HintIconContext icon = new HintIconContext(player, slot, arrowId, targetType, target, modelId, height);
		PacketRepository.send(HintIcon.class, icon);
		mng.hintIcons[slot] = icon;
		return slot;
	}

	/**
	 * Removes a hint icon.
	 * @param player The player.
	 * @param slot The hint icon slot.
	 */
	public static void removeHintIcon(Player player, int slot) {
		if (slot < 0) {
			return;
		}
		HintIconManager mng = player.getHintIconManager();
		HintIconContext icon = mng.hintIcons[slot];
		if (icon != null) {
			icon.setTargetType(0);
			PacketRepository.send(HintIcon.class, icon);
			mng.hintIcons[slot] = null;
		}
	}

	/**
	 * Clears the hint icons.
	 */
	public void clear() {
		for (int i = 0; i < hintIcons.length; i++) {
			HintIconContext icon = hintIcons[i];
			if (icon != null) {
				removeHintIcon(icon.getPlayer(), i);
			}
		}
	}

	/**
	 * Gets a free hint icon slot.
	 * @return The free slot.
	 */
	public int freeSlot() {
		for (int i = 0; i < hintIcons.length; i++) {
			if (hintIcons[i] == null) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the hint icon for the given slot.
	 * @param slot The slot.
	 * @return The hint icon context.
	 */
	public HintIconContext getIcon(int slot) {
		return hintIcons[slot];
	}
}