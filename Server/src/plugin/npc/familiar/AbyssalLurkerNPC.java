package plugin.npc.familiar;

import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.member.summoning.familiar.BurdenBeast;
import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.world.update.flag.context.Graphics;

/**
 * Represents the Abyssal Lurker familiar.
 * @author Aero
 */
@InitializablePlugin
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
