package core.game.node.entity.npc.familiar;

import core.plugin.Initializable;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.equipment.Weapon;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.Player;
import core.game.node.entity.state.EntityState;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;

/**
 * Represents the Spirit Scorpion familiar.
 * @author Vexia
 * @author Aero
 */
@Initializable
public class SpiritScorpionNPC extends Familiar {

	/**
	 * Constructs a new {@code SpiritScorpionNPC} {@code Object}.
	 */
	public SpiritScorpionNPC() {
		this(null, 6837);
	}

	/**
	 * Constructs a new {@code SpiritScorpionNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public SpiritScorpionNPC(Player owner, int id) {
		super(owner, id, 1700, 12055, 6, WeaponInterface.STYLE_CONTROLLED);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new SpiritScorpionNPC(owner, id);
	}

	@Override
	public void adjustPlayerBattle(BattleState state) {
		if (state.getStyle() == CombatStyle.RANGE) {
			final Weapon weapon = state.getWeapon();
			if (isCharged() && new Item(weapon.getId() + 6).getName().startsWith(weapon.getName())) {
				final Entity victim = state.getVictim();
				setCharged(false);
				victim.getStateManager().register(EntityState.POISONED, false, 10, owner);
			}
		}
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		if (isCharged()) {
			return false;
		}
		charge();
		owner.graphics(new Graphics(1355, 180), 2);
		visualize(new Animation(6261), new Graphics(1354, 95));
		Projectile.create(this, owner, 1355, 95, 50, 50, 10).send();
		return false;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6837, 6838 };
	}

}
