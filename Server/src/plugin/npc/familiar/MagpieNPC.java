package plugin.npc.familiar;

import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.free.crafting.gem.Gems;
import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.content.skill.member.summoning.familiar.Forager;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.world.update.flag.context.Graphics;

/**
 * Represents the Magpie familiar.
 * @author Aero
 * @author Vexia
 */
@InitializablePlugin
public class MagpieNPC extends Forager {

	/**
	 * The items to forage.
	 */
	private static final Item[] ITEMS = new Item[] { Gems.SAPPHIRE.getUncut(), Gems.EMERALD.getUncut(), Gems.RUBY.getUncut(), Gems.DIAMOND.getUncut() };

	/**
	 * Constructs a new {@code MagpieNPC} {@code Object}.
	 */
	public MagpieNPC() {
		this(null, 6824);
	}

	/**
	 * Constructs a new {@code MagpieNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public MagpieNPC(Player owner, int id) {
		super(owner, id, 3400, 12041, 3, ITEMS);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new MagpieNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		visualize(Animation.create(8020), Graphics.create(1336));
		return true;
	}

	@Override
	public void visualizeSpecialMove() {
		owner.getSkills().updateLevel(Skills.THIEVING, 2);
		owner.visualize(new Animation(7660), new Graphics(1296));
	}

	@Override
	public int getRandom() {
		return 14;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6824 };
	}

}
