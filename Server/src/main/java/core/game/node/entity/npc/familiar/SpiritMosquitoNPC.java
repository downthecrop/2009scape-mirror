package core.game.node.entity.npc.familiar;

import core.plugin.Initializable;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;

/**
 * Represents the Spirit Mosquito familiar.
 * @author Vexia
 * @author Aero
 */
@Initializable
public class SpiritMosquitoNPC extends Familiar {

	/**
	 * Constructs a new {@code SpiritMosquitoNPC} {@code Object}.
	 */
	public SpiritMosquitoNPC() {
		this(null, 7331);
	}

	/**
	 * Constructs a new {@code SpiritMosquitoNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public SpiritMosquitoNPC(Player owner, int id) {
		super(owner, id, 1200, 12778, 3, WeaponInterface.STYLE_ACCURATE);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new SpiritMosquitoNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		final Entity target = (Entity) special.getNode();
		if (!canAttack(target)) {
			return false;
		}
		visualize(Animation.create(8032), Graphics.create(1442));
		getProperties().getCombatPulse().attack(target);
		return true;
	}

	@Override
	public boolean isPoisonImmune() {
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 7331, 7332 };
	}

}
