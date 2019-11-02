package plugin.npc.familiar;

import org.crandor.game.content.skill.SkillBonus;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.free.fishing.Fish;
import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.content.skill.member.summoning.familiar.Forager;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Represents the Granite Crab familiar.
 * @author Vexia
 * @author Aero
 */
@InitializablePlugin
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
