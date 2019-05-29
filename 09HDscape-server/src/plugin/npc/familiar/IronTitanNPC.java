package plugin.npc.familiar;

import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.combat.CombatStyle;
import org.crandor.game.node.entity.combat.equipment.SwitchAttack;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.game.node.entity.combat.handlers.MultiSwingHandler;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Represents the Iron Titan familiar.
 * @author Aero
 */
@InitializablePlugin
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
						state.setStyle(current.getStyle());
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
