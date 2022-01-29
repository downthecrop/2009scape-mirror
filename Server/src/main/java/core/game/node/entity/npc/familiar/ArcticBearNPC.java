package core.game.node.entity.npc.familiar;

import core.game.node.entity.skill.SkillBonus;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.game.world.update.flag.context.Graphics;

/**
 * Represents the Arctic Bear familiar.
 * @author Aero
 */
@Initializable
public class ArcticBearNPC extends Familiar {

	/**
	 * Constructs a new {@code ArcticBearNPC} {@code Object}.
	 */
	public ArcticBearNPC() {
		this(null, 6839);
	}

	/**
	 * Constructs a new {@code ArcticBearNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public ArcticBearNPC(Player owner, int id) {
		super(owner, id, 2800, 12057, 6, WeaponInterface.STYLE_CONTROLLED);
		boosts.add(new SkillBonus(Skills.HUNTER, 7));		
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new ArcticBearNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		Entity target = special.getTarget();
		if (!canCombatSpecial(target)) {
			return false;
		}
		animate(Animation.create(4926));
		graphics(Graphics.create(1405));
		Projectile p = Projectile.magic(this, target, 1406, 40, 40, 1, 10);
		p.setSpeed(25);
		p.send();
		sendFamiliarHit(target, 15, Graphics.create(1407));
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6839, 6840 };
	}

}
