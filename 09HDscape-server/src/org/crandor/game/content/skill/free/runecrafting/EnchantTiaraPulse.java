package org.crandor.game.content.skill.free.runecrafting;

import org.crandor.game.content.skill.SkillPulse;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;

/**
 * Represents the enchanting of a tiara pulse.
 * @author 'Vexia
 * @date 02/11/2013
 */
public class EnchantTiaraPulse extends SkillPulse<Item> {

	/**
	 * Represents the tiara.
	 */
	private final Tiara tiara;

	/**
	 * Represents the item tiara.
	 */
	private final Item TIARA = new Item(5525);

	/**
	 * Represents the amount.
	 */
	private int amount;

	/**
	 * Constructs a new {@code EnchantTiaraPulse.java} {@code Object}.
	 * @param player the player.
	 * @param node the node.
	 */
	public EnchantTiaraPulse(Player player, Item node, final Tiara tiara, final int amount) {
		super(player, node);
		this.tiara = tiara;
		this.amount = amount;
	}

	@Override
	public void start() {
		super.start();
		int tiaraAmt = player.getInventory().getAmount(TIARA);
		int talsminAmt = player.getInventory().getAmount(node);
		if (tiaraAmt > talsminAmt) {
			amount = talsminAmt;
		} else {
			amount = tiaraAmt;
		}
	}

	@Override
	public boolean checkRequirements() {
		if (!player.getInventory().containsItem(TIARA)) {
			player.getPacketDispatch().sendMessage("You need a tiara.");
			return false;
		}
		return true;
	}

	@Override
	public void animate() {
	}

	@Override
	public boolean reward() {
		if (getDelay() == 1) {
			setDelay(2);
			return false;
		}
		if (player.getInventory().remove(TIARA) && player.getInventory().remove(tiara.getTalisman().getTalisman())) {
			player.getInventory().add(tiara.getTiara());
			player.getSkills().addExperience(Skills.RUNECRAFTING, tiara.getExperience(), true);
		}
		amount--;
		return amount == 0;
	}

	@Override
	public void message(int type) {
		if (type == 1) {
			player.getPacketDispatch().sendMessage("You bind the power of the talisman into your tiara.");
		}
	}

}
