package plugin.npc.familiar;

import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.state.EntityState;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Represents the Bloated Leech familiar.
 * @author Aero
 */
@InitializablePlugin
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
		owner.getStateManager().remove(EntityState.POISONED);
		for (int i = 0; i < Skills.SKILL_NAME.length; i++) {
			if (owner.getSkills().getLevel(i) < owner.getSkills().getStaticLevel(i)) {
				owner.getSkills().setLevel(i, owner.getSkills().getStaticLevel(i));
			}
		}
		owner.getSkills().rechargePrayerPoints();
		getImpactHandler().manualHit(owner, RandomFunction.random(2), HitsplatType.NORMAL);
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6843, 6844 };
	}

}
