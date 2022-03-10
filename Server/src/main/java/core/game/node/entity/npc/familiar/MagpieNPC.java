package core.game.node.entity.npc.familiar;

import core.game.node.entity.skill.SkillBonus;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.crafting.gem.Gems;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.skill.summoning.familiar.Forager;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.game.world.update.flag.context.Graphics;

/**
 * Represents the Magpie familiar.
 * @author Aero
 * @author Vexia
 */
@Initializable
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
		boosts.add(new SkillBonus(Skills.THIEVING, 3));			
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
