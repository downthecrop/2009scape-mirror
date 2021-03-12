package core.game.node.entity.npc.familiar;

import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.summoning.familiar.BurdenBeast;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.game.world.update.flag.context.Graphics;

/**
 * Represents the Abyssal Lurker familiar.
 * @author Aero
 */
@Initializable
public class AbyssalLurkerNPC extends BurdenBeast {

	/**
	 * Constructs a new {@code AbyssalLurkerNPC} {@code Object}.
	 */
	public AbyssalLurkerNPC() {
		this(null, 6820);
	}

	/**
	 * Constructs a new {@code AbyssalLurkerNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public AbyssalLurkerNPC(Player owner, int id) {
		super(owner, id, 4100, 12037, 3, 7, WeaponInterface.STYLE_CAST);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new AbyssalLurkerNPC(owner, id);
	}

	@Override
	public boolean isAllowed(Player owner, Item item) {
		if (item.getId() != 1436 && item.getId() != 7936) {
			owner.getPacketDispatch().sendMessage("Your familiar can only hold unnoted essence.");
			return false;
		}
		return super.isAllowed(owner, item);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		visualize(Animation.create(7682), Graphics.create(0));
		owner.getSkills().updateLevel(Skills.AGILITY, 4);
		owner.getSkills().updateLevel(Skills.THIEVING, 4);
		return true;
	}

	@Override
	public void visualizeSpecialMove() {
		owner.visualize(new Animation(7660), new Graphics(1296));
	}

	@Override
	public int[] getIds() {
		return new int[] { 6820, 6821 };
	}

}
