package plugin.npc.familiar;

import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.content.skill.member.summoning.familiar.Forager;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.game.node.entity.impl.Projectile;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.world.update.flag.context.Graphics;

/**
 * Represents the Evil Turnip familiar.
 * @author Aero
 * @author Vexia
 */
@InitializablePlugin
public class EvilTurnipNPC extends Forager {

	/**
	 * The evil turnip item.
	 */
	private static final Item EVIL_TURNIP = new Item(12136);

	/**
	 * Constructs a new {@code EvilTurnipNPC} {@code Object}.
	 */
	public EvilTurnipNPC() {
		this(null, 6833);
	}

	/**
	 * Constructs a new {@code EvilTurnipNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public EvilTurnipNPC(Player owner, int id) {
		super(owner, id, 3000, 12051, 6, WeaponInterface.STYLE_RANGE_ACCURATE, EVIL_TURNIP);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new EvilTurnipNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		final Entity target = (Entity) special.getNode();
		if (!canCombatSpecial(target)) {
			return false;
		}
		int ticks = 2 + (int) Math.floor(getLocation().getDistance(target.getLocation()) * 0.5);
		getSkills().heal(2);
		faceTemporary(target, 2);
		sendFamiliarHit(target, 10);
		animate(Animation.create(8251));
		target.graphics(Graphics.create(1329), ticks);
		target.getSkills().updateLevel(Skills.MAGIC, -1, 0);
		Projectile.magic(this, target, 1330, 40, 36, 51, 10).send();
		return true;
	}

	@Override
	public int getRandom() {
		return 20;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6833, 6834 };
	}

}
