package org.crandor.game.content.skill.member.farming.tool;

import org.crandor.game.content.skill.member.farming.FarmingConstant;
import org.crandor.game.content.skill.member.farming.wrapper.PatchWrapper;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.update.flag.context.Animation;

/**
 * Represents the tool reward for filling a plant pot.
 * @author 'Vexia
 */
public final class PlantPotPulse extends ToolAction {

	/**
	 * Represents the animation to use.
	 */
	private static final Animation ANIMATION = new Animation(2271);

	/**
	 * Represents the filled pot item.
	 */
	private static final Item FILLED_POT = new Item(5354);

	/**
	 * Represents the ticks passed.
	 */
	private int ticks;

	/**
	 * Constructs a new {@code PlantPotPulse} {@code Object}
	 */
	public PlantPotPulse() {
		super(null, null, null);
	}

	/**
	 * Constructs a new {@code PlantPotPulse} {@code Object}.
	 * @param player the player.
	 * @param wrapper the wrapper.
	 * @param tool the tool.
	 */
	public PlantPotPulse(final Player player, final PatchWrapper wrapper, final Item tool) {
		super(player, wrapper, tool);
	}

	@Override
	public ToolAction newInstance(Player player, PatchWrapper wrapper, Item tool) {
		return new PlantPotPulse(player, wrapper, tool);
	}

	@Override
	public boolean pulse() {
		if (++ticks % 2 != 0) {
			return false;
		}
		player.getAnimator().reset();
		player.getInventory().replace(FILLED_POT, tool.getSlot());
		return true;
	}

	@Override
	public boolean canInteract(String command) {
		if (!player.getInventory().containsItem(FarmingConstant.GARDENING_TROWEL)) {
			player.getPacketDispatch().sendMessage("You need a gardening trowel to do that.");
			return false;
		}
		if (wrapper.isWeedy()) {
			player.getPacketDispatch().sendMessage("You need to weed this patch first.");
			return false;
		}
		if (!wrapper.isEmpty()) {
			player.getPacketDispatch().sendMessage("You can only fill a plantpot from an empty farming patch.");
			return false;
		}
		player.animate(ANIMATION);
		return true;
	}

}
