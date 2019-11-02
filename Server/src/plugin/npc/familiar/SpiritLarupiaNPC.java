package plugin.npc.familiar;

import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.world.update.flag.context.Graphics;

/**
 * Represents the Spirit Larupia familiar.
 * @author Aero
 */
@InitializablePlugin
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
