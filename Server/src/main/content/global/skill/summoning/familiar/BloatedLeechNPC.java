package content.global.skill.summoning.familiar;

import core.plugin.Initializable;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.tools.RandomFunction;

import static core.api.ContentAPIKt.*;

/**
 * Represents the Bloated Leech familiar.
 * @author Aero
 */
@Initializable
public class BloatedLeechNPC extends Familiar {

	/**
	 * Constructs a new {@code BloatedLeechNPC} {@code Object}.
	 */
	public BloatedLeechNPC() {
		this(null, 6843);
	}

	/**
	 * Constructs a new {@code BloatedLeechNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public BloatedLeechNPC(Player owner, int id) {
		super(owner, id, 3400, 12061, 6, WeaponInterface.STYLE_ACCURATE);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new BloatedLeechNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		curePoison(owner);
		removeTimer(owner, "disease");
		for (int i = 0; i < Skills.SKILL_NAME.length; i++) {
			if (owner.getSkills().getLevel(i) < owner.getSkills().getStaticLevel(i)) {
				owner.getSkills().updateLevel(
						i,
						(int) Math.ceil(owner.getSkills().getStaticLevel(i) * 0.2),
						owner.getSkills().getStaticLevel(i)
				);
			}
		}
		owner.getImpactHandler().manualHit(owner, RandomFunction.random(1, 5), HitsplatType.NORMAL);
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6843, 6844 };
	}

}
