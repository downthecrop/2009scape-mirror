package core.game.node.entity.skill.crafting.limestone;

import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;

/**
 * 
 * The skill pulse for chiseling a limestone into a limestone brick.
 * 
 * @author Jamix77
 *
 */
public class ChiselLimestonePulse extends SkillPulse<Item> {
	
	/**
	 * Represents the chisel item.
	 */
	private static final Item CHISEL = new Item(1755);

	/**
	 * Represents the ticks passed.
	 */
	private int ticks;
	
	public ChiselLimestonePulse(Player player, Item node) {
		super(player, node);
		this.resetAnimation= false;
	}

	@Override
	public boolean checkRequirements() {
		if (!player.getInventory().containsItem(CHISEL)) {
			return false;
		}
		if (!player.getInventory().containsItem(new Item(3211))) {
			return false;
		}
		if (player.getSkills().getLevel(Skills.CRAFTING) < 12) {
			player.getPacketDispatch().sendMessage("You need a crafting level of 12 to make limestone bricks.");
			return false;
		}
		return true;
	}

	@Override
	public void animate() {
		if (ticks % 5 == 0 || ticks < 1) {
			player.getAudioManager().send(2586);
			player.animate(Animation.create(4470));
		}
	}

	@Override
	public boolean reward() {
		if (++ticks % 2 != 0) {
			return false;
		}
		if (player.getInventory().remove(new Item(3211))) {
			
		    player.getInventory().add(new Item(3420));
			
			player.getSkills().addExperience(Skills.CRAFTING, 6, true);
		}
		return !player.getInventory().containsItem(new Item(3211));
	}

}
