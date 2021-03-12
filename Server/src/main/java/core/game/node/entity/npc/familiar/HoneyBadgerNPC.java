package core.game.node.entity.npc.familiar;

import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.game.world.update.flag.context.Graphics;

/**
 * Represents the Honey Badger familiar.
 * @author Aero
 * @author Vexia
 */
@Initializable
public class HoneyBadgerNPC extends Familiar {

	/**
	 * Constructs a new {@code HoneyBadgerNPC} {@code Object}.
	 */
	public HoneyBadgerNPC() {
		this(null, 6845);
	}

	/**
	 * Constructs a new {@code HoneyBadgerNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public HoneyBadgerNPC(Player owner, int id) {
		super(owner, id, 2500, 12065, 4, WeaponInterface.STYLE_AGGRESSIVE);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new HoneyBadgerNPC(owner, id);
	}

	@Override
	public void visualizeSpecialMove() {
		owner.visualize(Animation.create(7660), Graphics.create(1399));
	}

	@Override
	public String getText() {
		return "Raaaar!";
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		if (isCharged()) {
			return false;
		}
		charge();
		visualize(new Animation(7928, Priority.HIGH), Graphics.create(1397));
		return true;
	}

	@Override
	public boolean isCharged() {
		if (charged) {
			owner.getPacketDispatch().sendMessage("Your honey badger is already enraged!");
			return true;
		}
		return false;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6845, 6846 };
	}

}
