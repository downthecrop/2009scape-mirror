package core.game.node.entity.combat.handlers;

import core.game.container.impl.EquipmentContainer;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import rs09.game.node.entity.combat.CombatSwingHandler;
import core.game.node.entity.combat.InteractionType;
import core.game.node.entity.combat.equipment.SwitchAttack;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.state.EntityState;
import core.game.node.item.Item;
import rs09.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.tools.RandomFunction;

/**
 * Handles dragonfire combat.
 * @author Emperor
 */
public class DragonfireSwingHandler extends CombatSwingHandler {

	/**
	 * If the NPC has to be in melee range.
	 */
	private boolean meleeRange;

	/**
	 * The maximum hit.
	 */
	private int maximumHit;

	/**
	 * The attack data.
	 */
	private SwitchAttack attack;

	/**
	 * IF the dragon attack is firey or icey.
	 */
	private boolean fire;

	/**
	 * Constructs a new {@code DragonfireSwingHandler} {@code Object}.
	 * @param meleeRange If the NPC has to be in melee range.
	 * @param maximumHit The maximum hit.
	 * @param fire if firey.
	 */
	public DragonfireSwingHandler(boolean meleeRange, int maximumHit, SwitchAttack attack, boolean fire) {
		super(CombatStyle.MAGIC);
		this.meleeRange = meleeRange;
		this.maximumHit = maximumHit;
		this.attack = attack;
		this.fire = fire;
	}

	/**
	 * Gets the switch attack instance for a dragonfire attack.
	 * @param meleeRange If the attack is melee range.
	 * @param maximumHit The maximum hit.
	 * @param animation The animation.
	 * @param startGraphic The start graphic.
	 * @param endGraphic The end graphic.
	 * @param projectile The projectile.
	 * @return The switch attack instance.
	 */
	public static SwitchAttack get(boolean meleeRange, int maximumHit, Animation animation, Graphics startGraphic, Graphics endGraphic, Projectile projectile) {
		SwitchAttack attack = new SwitchAttack(null, animation, startGraphic, endGraphic, projectile).setUseHandler(true);
		attack.setHandler(new DragonfireSwingHandler(meleeRange, maximumHit, attack, true));
		return attack;
	}

	/**
	 * Gets the switch attack instance for a dragonfire attack.
	 * @param meleeRange If the attack is melee range.
	 * @param maximumHit The maximum hit.
	 * @param animation The animation.
	 * @param startGraphic The start graphic.
	 * @param endGraphic The end graphic.
	 * @param projectile The projectile.
	 * @return The switch attack instance.
	 */
	public static SwitchAttack get(boolean meleeRange, int maximumHit, Animation animation, Graphics startGraphic, Graphics endGraphic, Projectile projectile, boolean fire) {
		SwitchAttack attack = new SwitchAttack(null, animation, startGraphic, endGraphic, projectile).setUseHandler(true);
		attack.setHandler(new DragonfireSwingHandler(meleeRange, maximumHit, attack, fire));
		return attack;
	}

	@Override
	public InteractionType canSwing(Entity entity, Entity victim) {
		if (meleeRange) {
			return CombatStyle.MELEE.getSwingHandler().canSwing(entity, victim);
		}
		return CombatStyle.MAGIC.getSwingHandler().canSwing(entity, victim);
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		int max = calculateHit(entity, victim, 1.0);
		int hit = RandomFunction.random(max);
		assert state != null;
		state.setMaximumHit(max);
		state.setStyle(CombatStyle.MAGIC);
		state.setEstimatedHit(hit);
		if (meleeRange) {
			return 1;
		}
		int ticks = 2 + (int) Math.floor(entity.getLocation().getDistance(victim.getLocation()) * 0.5);
		entity.setAttribute("fireBreath", GameWorld.getTicks() + (ticks + 2));
		return ticks;
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		entity.visualize(attack.getAnimation(), attack.getStartGraphic());
		if (attack.getProjectile() != null) {
			attack.getProjectile().copy(entity, victim, 5).send();
		}
	}

	@Override
	public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
		if (entity instanceof NPC && victim instanceof Player) {
			Player p = (Player) victim;
			p.getPacketDispatch().sendMessage(getDragonfireMessage(victim.getAttribute("fire_resistance", 0), !fire ? "icy breath" : "fiery breath"));
			Item shield = p.getEquipment().get(EquipmentContainer.SLOT_SHIELD);
			if (shield != null && (shield.getId() == 11283 || shield.getId() == 11284)) {
				if (shield.getId() == 11284) {
					p.getEquipment().replace(new Item(11283), EquipmentContainer.SLOT_SHIELD);
					shield = p.getEquipment().get(EquipmentContainer.SLOT_SHIELD);
					shield.setCharge(0);
				}
				if (shield.getCharge() < 1000) {
					shield.setCharge(shield.getCharge() + 20);
					EquipmentContainer.updateBonuses(p);
					p.getPacketDispatch().sendMessage("Your dragonfire shield glows more brightly.");
				} else {
					p.getPacketDispatch().sendMessage("Your dragonfire shield is already fully charged.");
				}
				p.faceLocation(entity.getCenterLocation());
				victim.visualize(Animation.create(6695), Graphics.create(1163));
				return;
			}
		}
		if (!fire && victim.getAttribute("freeze_immunity", -1) < GameWorld.getTicks() && RandomFunction.random(4) == 2) {
			victim.getStateManager().set(EntityState.FROZEN, 16);
			victim.graphics(Graphics.create(502));
		}
		Graphics graphic = attack != null ? attack.getEndGraphic() : null;
		victim.visualize(victim.getProperties().getDefenceAnimation(), graphic);
	}

	@Override
	public void impact(Entity entity, Entity victim, BattleState state) {
		assert state != null;
		int hit = state.getEstimatedHit();
		if (hit > -1) {
			victim.getImpactHandler().handleImpact(entity, hit, CombatStyle.MAGIC, state);
		}
		hit = state.getSecondaryHit();
		if (hit > -1) {
			victim.getImpactHandler().handleImpact(entity, hit, CombatStyle.MAGIC, state);
		}
	}

	@Override
	public void adjustBattleState(Entity entity, Entity victim, BattleState state) {
		if (victim.isPlayer()  && !fire) {
			Item item = victim.asPlayer().getEquipment().get(EquipmentContainer.SLOT_SHIELD);
			if (item != null && (item.getId() == 2890 || item.getId() == 9731) && state.getEstimatedHit() > 10) {
				state.setEstimatedHit(RandomFunction.random(10));
			}
		}
		CombatStyle style = state.getStyle();
		super.adjustBattleState(entity, victim, state);
		state.setStyle(style);
	}

	@Override
	protected int getFormattedHit(Entity entity, Entity victim, BattleState state, int hit) {
		return formatHit(victim, hit);
	}

	@Override
	public int calculateAccuracy(Entity entity) {
		return 4000;
	}

	@Override
	public int calculateHit(Entity entity, Entity victim, double modifier) {
		int damage = maximumHit;
		if (victim instanceof Player) {
			int val = victim.getDragonfireProtection(fire);
			if ((val & 0x2) != 0) {
				damage *= 0.1;
			}
			if ((val & 0x4) != 0) {
				damage *= 0.1;
			}
			if ((val & 0x8) != 0) {
				damage *= 0.6;
			}
		}
		return damage;
	}

	@Override
	public int calculateDefence(Entity victim, Entity attacker) {
		return CombatStyle.MAGIC.getSwingHandler().calculateDefence(victim, attacker);
	}

	@Override
	public double getSetMultiplier(Entity e, int skillId) {
		return 1.0;
	}

}