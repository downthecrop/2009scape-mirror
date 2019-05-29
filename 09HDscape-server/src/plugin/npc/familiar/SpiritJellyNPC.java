package plugin.npc.familiar;

import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.game.node.entity.impl.Projectile;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.world.update.flag.context.Animation;

/**
 * Represents the Spirit Jelly familiar.
 * @author Aero
 */
@InitializablePlugin
public class SpiritJellyNPC extends Familiar {

	/**
	 * Constructs a new {@code SpiritJellyNPC} {@code Object}.
	 */
	public SpiritJellyNPC() {
		this(null, 6992);
	}

	/**
	 * Constructs a new {@code SpiritJellyNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public SpiritJellyNPC(Player owner, int id) {
		super(owner, id, 4300, 12027, 6, WeaponInterface.STYLE_AGGRESSIVE);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new SpiritJellyNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		final Entity target = (Entity) special.getNode();
		if (!canCombatSpecial(target)) {
			return false;
		}
		faceTemporary(target, 2);
		sendFamiliarHit(target, 13);
		animate(Animation.create(8575));
		Projectile.magic(this, target, 1360, 40, 36, 51, 10).send();
		target.getSkills().updateLevel(Skills.ATTACK, -3, target.getSkills().getStaticLevel(Skills.ATTACK) - 3);
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6992, 6993 };
	}

}
