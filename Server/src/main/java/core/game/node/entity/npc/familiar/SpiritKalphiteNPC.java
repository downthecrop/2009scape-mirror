package core.game.node.entity.npc.familiar;

import java.util.List;

import core.game.node.entity.skill.summoning.familiar.BurdenBeast;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.game.world.update.flag.context.Graphics;

/**
 * Represents the Spirit Kalphite familiar.
 * @author Vexia
 * @author Aero
 */
@Initializable
public class SpiritKalphiteNPC extends BurdenBeast {

	/**
	 * Constructs a new {@code SpiritKalphiteNPC} {@code Object}.
	 */
	public SpiritKalphiteNPC() {
		this(null, 6994);
	}

	/**
	 * Constructs a new {@code SpiritKalphiteNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public SpiritKalphiteNPC(Player owner, int id) {
		super(owner, id, 2200, 12063, 6, 6, WeaponInterface.STYLE_DEFENSIVE);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new SpiritKalphiteNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		if (!isOwnerAttackable()) {
			return false;
		}
		final List<Entity> entitys = RegionManager.getLocalEntitys(owner, 6);
		visualize(Animation.create(8517), Graphics.create(1350));
		GameWorld.getPulser().submit(new Pulse(1, owner) {
			@Override
			public boolean pulse() {
				int count = 0;
				for (Entity entity : entitys) {
					if (count > 5) {
						return true;
					}
					if (!canCombatSpecial(entity)) {
						continue;
					}
					Projectile.magic(SpiritKalphiteNPC.this, entity, 1349, 40, 36, 50, 5).send();
					sendFamiliarHit(entity, 20);
					count++;
				}
				return true;
			}
		});
		return false;
	}

	@Override
	public String getText() {
		return "Hsssss!";
	}

	@Override
	public int[] getIds() {
		return new int[] { 6994, 6995 };
	}

}
