package plugin.npc.familiar;

import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.member.summoning.familiar.BurdenBeast;
import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.world.update.flag.context.Graphics;

/**
 * Represents the Bull Ant familiar.
 * @author Aero
 */
@InitializablePlugin
public class BullAntNPC extends BurdenBeast {

	/**
	 * Constructs a new {@code BullAntNPC} {@code Object}.
	 */
	public BullAntNPC() {
		this(null, 6867);
	}

	/**
	 * Constructs a new {@code BullAntNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public BullAntNPC(Player owner, int id) {
		super(owner, id, 3000, 12087, 12, 9, WeaponInterface.STYLE_CONTROLLED);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new BullAntNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		if (owner.getSettings().getRunEnergy() >= 100) {
			owner.getPacketDispatch().sendMessage("You already have full run energy.");
			return false;
		}
		int amount = owner.getSkills().getStaticLevel(Skills.AGILITY) / 2;
		visualize(Animation.create(7896), Graphics.create(1382));
		owner.getSettings().updateRunEnergy(-amount);
		return true;
	}

	@Override
	public void visualizeSpecialMove() {
		owner.visualize(new Animation(7660), new Graphics(1296));
	}

	@Override
	public int[] getIds() {
		return new int[] { 6867, 6868 };
	}

}
