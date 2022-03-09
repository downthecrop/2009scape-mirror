package core.game.node.entity.npc.familiar;

import core.plugin.Initializable;
import core.game.node.entity.skill.SkillBonus;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;

/**
 * Represents the Spirit Larupia familiar.
 * @author Aero
 */
@Initializable
public class SpiritLarupiaNPC extends Familiar {

	/**
	 * Constructs a new {@code SpiritLarupiaNPC} {@code Object}.
	 */
	public SpiritLarupiaNPC() {
		this(null, 7337);
	}

	/**
	 * Constructs a new {@code SpiritLarupiaNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public SpiritLarupiaNPC(Player owner, int id) {
		super(owner, id, 4900, 12784, 6, WeaponInterface.STYLE_CONTROLLED);
		boosts.add(new SkillBonus(Skills.HUNTER, 5));		
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new SpiritLarupiaNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		final Entity target = special.getTarget();
		if (!canCombatSpecial(target)) {
			return false;
		}
		target.getSkills().updateLevel(Skills.STRENGTH, -1, target.getSkills().getStaticLevel(Skills.STRENGTH) - 1);
		faceTemporary(target, 2);
		projectile(target, 1371);
		sendFamiliarHit(target, 10);
		visualize(Animation.create(5229), Graphics.create(1370));
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 7337, 7338 };
	}

}
