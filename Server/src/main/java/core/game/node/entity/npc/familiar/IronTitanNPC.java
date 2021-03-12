package core.game.node.entity.npc.familiar;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.equipment.SwitchAttack;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.tools.RandomFunction;
import rs09.game.node.entity.combat.handlers.MultiSwingHandler;

/**
 * Represents the Iron Titan familiar.
 * @author Aero
 */
@Initializable
public class IronTitanNPC extends Familiar {

	/**
	 * If the titan is using its special move.
	 */
	private boolean specialMove;

	/**
	 * The attacks.
	 */
	private static final SwitchAttack[] ATTACKS = { new SwitchAttack(CombatStyle.MELEE.getSwingHandler(), Animation.create(8183)) };
	
	/**
	 * Constructs a new {@code IronTitanNPC} {@code Object}.
	 */
	public IronTitanNPC() {
		this(null, 7375);
	}
	/**
	 * Constructs a new {@code IronTitanNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public IronTitanNPC(Player owner, int id) {
		super(owner, id, 6000, 12822, 12, WeaponInterface.STYLE_DEFENSIVE);
		super.setCombatHandler(new MultiSwingHandler(true, ATTACKS) {
			@Override
			public int swing(Entity entity, Entity victim, BattleState s) {
				int ticks = super.swing(entity, victim, s);
				if (specialMove) {
					BattleState[] states = new BattleState[3];
					for (int i = 1; i < 3; i++) {
						BattleState state = states[i] = new BattleState(entity, victim);
						int hit = 0;
						if (isAccurateImpact(entity, victim)) {
							int max = calculateHit(entity, victim, 1.0);
							state.setMaximumHit(max);
							hit = RandomFunction.random(max);
							state.setEstimatedHit(hit);
						}
						state.setEstimatedHit(hit);
						state.setStyle(getCurrent().getStyle());
					}
					states[0] = s;
					s.setTargets(states);
					specialMove = false;
				}
				return ticks;
			}
		});
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new IronTitanNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		if (specialMove) {
			owner.getPacketDispatch().sendMessage("Your familiar is already charging its attack.");
			return false;
		}
		specialMove = true;
		visualize(Animation.create(8183), Graphics.create(1450));
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 7375, 7376 };
	}

}
