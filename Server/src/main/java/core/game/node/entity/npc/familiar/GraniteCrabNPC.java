package core.game.node.entity.npc.familiar;

import core.plugin.Initializable;
import core.game.node.entity.skill.SkillBonus;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.fishing.Fish;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.skill.summoning.familiar.Forager;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.tools.RandomFunction;

/**
 * Represents the Granite Crab familiar.
 * @author Vexia
 * @author Aero
 */
@Initializable
public class GraniteCrabNPC extends Forager {

	/**
	 * The fish item.
	 */
	private static final Item[] FISH = new Item[] { Fish.COD.getItem(), Fish.PIKE.getItem(), Fish.SEAWEED.getItem(), Fish.OYSTER.getItem() };

	/**
	 * Constructs a new {@code GraniteCrabNPC} {@code Object}.
	 */
	public GraniteCrabNPC() {
		this(null, 6796);
	}

	/**
	 * Constructs a new {@code GraniteCrabNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public GraniteCrabNPC(Player owner, int id) {
		super(owner, id, 1800, 12009, 12, WeaponInterface.STYLE_DEFENSIVE);
		boosts.add(new SkillBonus(Skills.FISHING, 1));
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new GraniteCrabNPC(owner, id);
	}

	@Override
	public void handlePassiveAction() {
		if (RandomFunction.random(4) == 1) {
			final Item item = FISH[RandomFunction.random(FISH.length)];
			animate(Animation.create(8107));
			if (item.getId() == Fish.COD.getItem().getId() || item.getId() == Fish.PIKE.getItem().getId()) {
				owner.getSkills().addExperience(Skills.FISHING, 5.5);
			}
			produceItem(item);
		}
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		owner.getSkills().updateLevel(Skills.DEFENCE, 4);
		visualize(Animation.create(8109), Graphics.create(1326));
		return true;
	}

	@Override
	public void visualizeSpecialMove() {
		owner.visualize(new Animation(7660), new Graphics(1296));
	}

	@Override
	public int[] getIds() {
		return new int[] { 6796, 6797 };
	}

}
