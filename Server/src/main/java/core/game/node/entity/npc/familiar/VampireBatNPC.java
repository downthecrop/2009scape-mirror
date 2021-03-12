package core.game.node.entity.npc.familiar;

import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Represents the Vampire Bat familiar.
 * @author Aero
 * @author Vexia
 */
@Initializable
public class VampireBatNPC extends Familiar {

	/**
	 * Constructs a new {@code VampireBatNPC} {@code Object}.
	 */
	public VampireBatNPC() {
		this(null, 6835);
	}

	/**
	 * Constructs a new {@code VampireBatNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public VampireBatNPC(Player owner, int id) {
		super(owner, id, 3300, 12053, 4, WeaponInterface.STYLE_CONTROLLED);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new VampireBatNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		final Entity target = (Entity) special.getNode();
		if (!canCombatSpecial(target)) {
			return false;
		} else {
			if (RandomFunction.random(10) < 4) {
				owner.getSkills().heal(2);
			}
			visualize(Animation.create(8275), Graphics.create(1323));
			target.getImpactHandler().manualHit(this, RandomFunction.random(12), HitsplatType.NORMAL);
			return true;
		}
	}

	@Override
	public int[] getIds() {
		return new int[] { 6835, 6836 };
	}

}
