package core.game.node.entity.npc.familiar;

import java.util.List;

import core.plugin.Initializable;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.tools.RandomFunction;

/**
 * Represents the Giant Chinchompa familiar.
 * @author Aero
 * @author Vexia
 */
@Initializable
public class GiantChinchompaNPC extends Familiar {

	/**
	 * Constructs a new {@code GiantChinchompaNPC} {@code Object}.
	 */
	public GiantChinchompaNPC() {
		this(null, 7353);
	}

	/**
	 * Constructs a new {@code GiantChinchompaNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public GiantChinchompaNPC(Player owner, int id) {
		super(owner, id, 3100, 12800, 3, WeaponInterface.STYLE_RANGE_ACCURATE);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new GiantChinchompaNPC(owner, id);
	}

	@Override
	public void onAttack(Entity entity) {
		super.onAttack(entity);
		if (RandomFunction.random(20) == 10) {
			executeSpecialMove(new FamiliarSpecial(null));
		}
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		if (!isOwnerAttackable()) {
			return false;
		}
		final List<Entity> entitys = RegionManager.getLocalEntitys(owner, 6);
		entitys.remove(owner);
		entitys.remove(this);
		sendChat("Squeak!");
		animate(Animation.create(7758));
		graphics(Graphics.create(1364));
		GameWorld.getPulser().submit(new Pulse(3, owner, this) {
			@Override
			public boolean pulse() {
				for (Entity entity : entitys) {
					if (canCombatSpecial(entity, false)) {
						entity.getImpactHandler().manualHit(GiantChinchompaNPC.this, RandomFunction.random(13), HitsplatType.NORMAL);
					}
				}
				dismiss();
				return true;
			}
		});
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 7353, 7354 };
	}

}
