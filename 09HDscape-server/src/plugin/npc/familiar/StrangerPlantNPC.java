package plugin.npc.familiar;

import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.content.skill.member.summoning.familiar.Forager;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.impl.Projectile;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.state.EntityState;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Represents the Stranger Plant familiar.
 * @author Aero
 */
@InitializablePlugin
public class StrangerPlantNPC extends Forager {

	/**
	 * Constructs a new {@code StrangerPlantNPC} {@code Object}.
	 */
	public StrangerPlantNPC() {
		this(null, 6827);
	}

	/**
	 * Constructs a new {@code StrangerPlantNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public StrangerPlantNPC(Player owner, int id) {
		super(owner, id, 4900, 12045, 6, new Item(464));
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new StrangerPlantNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		if (!canCombatSpecial(special.getTarget())) {
			return false;
		}
		Entity target = special.getTarget();
		if (RandomFunction.random(2) == 1) {
			target.getStateManager().register(EntityState.POISONED, false, 40, target);
		}
		animate(Animation.create(8211));
		Projectile.ranged(this, target, 1508, 50, 40, 1, 45).send();
		target.graphics(Graphics.create(1511));
		sendFamiliarHit(target, 2);
		return false;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6827, 6828 };
	}

}
