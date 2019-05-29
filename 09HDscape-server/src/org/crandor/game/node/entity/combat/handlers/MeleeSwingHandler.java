package org.crandor.game.node.entity.combat.handlers;

import org.crandor.game.container.Container;
import org.crandor.game.container.impl.EquipmentContainer;
import org.crandor.game.content.global.tutorial.TutorialSession;
import org.crandor.game.content.global.tutorial.TutorialStage;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.combat.CombatStyle;
import org.crandor.game.node.entity.combat.CombatSwingHandler;
import org.crandor.game.node.entity.combat.InteractionType;
import org.crandor.game.node.entity.combat.equipment.ArmourSet;
import org.crandor.game.node.entity.combat.equipment.Weapon;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.state.EntityState;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.path.Pathfinder;
import org.crandor.tools.RandomFunction;

/**
 * Handles a melee combat swing.
 * @author Emperor
 */
public class MeleeSwingHandler extends CombatSwingHandler {

	/**
	 * Constructs a new {@code MeleeSwingHandler} {@Code Object}.
	 */
	public MeleeSwingHandler() {
		super(CombatStyle.MELEE);
	}

	@Override
	public InteractionType canSwing(Entity entity, Entity victim) {
		if (victim == null || entity == null) {
			return InteractionType.NO_INTERACT;
		}
		int distance = usingHalberd(entity) ? 2 : 1;
		InteractionType type = InteractionType.STILL_INTERACT;
		boolean goodRange = canMelee(entity, victim, distance);
		if (!goodRange && victim.getProperties().getCombatPulse().getVictim() != entity && victim.getWalkingQueue().isMoving() && entity.size() == 1) {
			type = InteractionType.MOVE_INTERACT;
			distance += entity.getWalkingQueue().isRunningBoth() ? 2 : 1;
			goodRange = canMelee(entity, victim, distance);
		}
		if (!isProjectileClipped(entity, victim, true)) {
			return InteractionType.NO_INTERACT;
		}
		if (goodRange && super.canSwing(entity, victim) != InteractionType.NO_INTERACT) {
			if (type == InteractionType.STILL_INTERACT) {
				entity.getWalkingQueue().reset();
			}
			return type;
		}
		return InteractionType.NO_INTERACT;
	}

	/**
	 * Checks if the entity is using a halberd.
	 * @param entity The entity.
	 * @return {@code True} if so.
	 */
	private static boolean usingHalberd(Entity entity) {
		if (entity instanceof Player) {
			Item weapon = ((Player) entity).getEquipment().get(EquipmentContainer.SLOT_WEAPON);
			if (weapon != null) {
				return (weapon.getId() >= 3190 && weapon.getId() <= 3204) || weapon.getId() == 6599;
			}
		} else if (entity instanceof NPC) {
			NPC npc = (NPC) entity;
			if (npc != null) {
				return npc.getId() == 8612;
			}
		}
		return false;
	}

	/**
	 * Checks if the entity can execute a melee swing from its current location.
	 * @param entity The attacking entity.
	 * @param victim The victim.
	 * @return {@code True} if so.
	 */
	public static boolean canMelee(Entity entity, Entity victim, int distance) {
		Location e = entity.getLocation();
		if (victim == null) {
			return false;
		}
		if (entity.getId() == 7135 && entity.getLocation().withinDistance(victim.getLocation(), 2)) {
			return true;
		}
		int x = victim.getLocation().getX();
		int y = victim.getLocation().getY();
		int size = entity.size();
		if (distance == 1) {
			for (int i = 0; i < size; i++) {
				if (Pathfinder.isStandingIn(e.getX() - 1, e.getY() + i, 1, 1, x, y, victim.size(), victim.size())) {
					return true;
				}
				if (Pathfinder.isStandingIn(e.getX() + size, e.getY() + i, 1, 1, x, y, victim.size(), victim.size())) {
					return true;
				}
				if (Pathfinder.isStandingIn(e.getX() + i, e.getY() - 1, 1, 1, x, y, victim.size(), victim.size())) {
					return true;
				}
				if (Pathfinder.isStandingIn(e.getX() + i, e.getY() + size, 1, 1, x, y, victim.size(), victim.size())) {
					return true;
				}
			}
			if (e.equals(victim.getLocation())) {
				return true;
			}
			if (victim.getSwingHandler(false).getType() == CombatStyle.MELEE && e.withinDistance(victim.getLocation(), 1) && victim.getProperties().getCombatPulse().getVictim() == entity && entity.getIndex() < victim.getIndex()) {
				return true; // This might cause diagonal combat in some
				// circumstances.
			}
			return false;
		}
		return entity.getCenterLocation().withinDistance(victim.getCenterLocation(), distance + (size >> 1) + (victim.size() >> 1));
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		int hit = 0;
		state.setStyle(CombatStyle.MELEE);
		if (entity instanceof Player) {
			state.setWeapon(new Weapon(((Player) entity).getEquipment().get(3)));
		}
		if (entity.getProperties().getArmourSet() == ArmourSet.VERAC && RandomFunction.random(100) < 21) {
			state.setArmourEffect(ArmourSet.VERAC);
		}
		if (state.getArmourEffect() == ArmourSet.VERAC || isAccurateImpact(entity, victim, CombatStyle.MELEE)) {
			int max = calculateHit(entity, victim, 1.0);
			state.setMaximumHit(max);
			hit = RandomFunction.random(max);
		}
		state.setEstimatedHit(hit);
		return 1;
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		entity.animate(entity.getProperties().getAttackAnimation());
	}

	@Override
	public void impact(Entity entity, Entity victim, BattleState state) {
		BattleState[] targets = state.getTargets();
		if (targets == null) {
			targets = new BattleState[] { state };
		}
		for (BattleState s : targets) {
			int hit = s.getEstimatedHit();
			if (hit > -1) {
				victim.getImpactHandler().handleImpact(entity, hit, CombatStyle.MELEE, s);
			}
			hit = s.getSecondaryHit();
			if (hit > -1) {
				victim.getImpactHandler().handleImpact(entity, hit, CombatStyle.MELEE, s);
			}
		}
	}

	@Override
	public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
		if (entity instanceof Player) {
			if (TutorialSession.getExtension(((Player) entity)).getStage() == 51) {
				TutorialStage.load(((Player) entity), 52, false);
			}
			if (TutorialSession.getExtension(((Player) entity)).getStage() == 54) {
				TutorialStage.load(((Player) entity), 55, false);
			}
		}
		victim.animate(victim.getProperties().getDefenceAnimation());
	}

	@Override
	public void adjustBattleState(Entity entity, Entity victim, BattleState state) {
		if (entity instanceof Player) {
			Player p = (Player) entity;
			if (state.getEstimatedHit() > 0) {
				String name = p.getEquipment().getNew(3).getName();
				int damage = -1;
				if (name.contains("(p++") || name.contains("(s)") || name.contains("(kp)")) {
					damage = 68;
				} else if (name.contains("(p+)")) {
					damage = 58;
				} else if (name.contains("(p)")) {
					damage = 48;
				}
				if (damage > -1 && RandomFunction.random(10) < 4) {
					victim.getStateManager().register(EntityState.POISONED, false, damage, entity);
				}
			}
		}
		super.adjustBattleState(entity, victim, state);
	}

	@Override
	public int calculateAccuracy(Entity entity) {
		int baseLevel = entity.getSkills().getStaticLevel(Skills.ATTACK);
		int weaponRequirement = baseLevel;
		if (entity instanceof Player) {
			Item weapon = ((Player) entity).getEquipment().get(3);
			if (weapon != null) {
				weaponRequirement = weapon.getDefinition().getRequirement(Skills.ATTACK);
			} else {
				weaponRequirement = 1;
			}
		}
		double weaponBonus = 0.0;
		if (baseLevel > weaponRequirement) {
			weaponBonus = (baseLevel - weaponRequirement) * .3;
		}
		WeaponInterface.AttackStyle style = entity.getProperties().getAttackStyle();
		int level = entity.getSkills().getLevel(Skills.ATTACK);
		double prayer = 1.0;
		if (entity instanceof Player) {
		    prayer += ((Player) entity).getPrayer().getSkillBonus(Skills.ATTACK);
		}
		double additional = 1.0; // Black mask/slayer helmet/salve/...
		if(entity != null && entity.getProperties().getCombatPulse().getVictim() != null){
			if(!entity.getProperties().getCombatPulse().getVictim().isPlayer() && entity instanceof Player){
				if(checkUndead(entity.getProperties().getCombatPulse().getVictim().getName()) && entity.asPlayer().getEquipment().get(EquipmentContainer.SLOT_AMULET) != null){
					if (entity.asPlayer().getEquipment().get(EquipmentContainer.SLOT_AMULET).getId() == 10588) {
						additional += 0.20;
					} else if (entity.isPlayer() && entity.asPlayer().getEquipment().get(EquipmentContainer.SLOT_AMULET).getId() == 4081) {
						additional += 0.15;
					}
				}
			}
		}
		int styleBonus = 0;
		if (style.getStyle() == WeaponInterface.STYLE_ACCURATE) {
			styleBonus = 3;
		} else if (style.getStyle() == WeaponInterface.STYLE_CONTROLLED) {
			styleBonus = 1;
		}
		double effective = Math.floor(((level * prayer) * additional) + styleBonus + weaponBonus);
		int bonus = entity.getProperties().getBonuses()[style.getBonusType()];
		return (int) Math.floor((((effective + 8) * (bonus + 64)) / 10) * 1.10);
	}

	@Override
	public int calculateHit(Entity entity, Entity victim, double modifier) {
		int level = entity.getSkills().getLevel(Skills.STRENGTH);
		int bonus = entity.getProperties().getBonuses()[11];
		double prayer = 1.0;
		if (entity instanceof Player) {
			prayer += ((Player) entity).getPrayer().getSkillBonus(Skills.STRENGTH);
		}
		double cumulativeStr = Math.floor(level * prayer);
		if (entity.getProperties().getAttackStyle().getStyle() == WeaponInterface.STYLE_AGGRESSIVE) {
			cumulativeStr += 3;
		} else if (entity.getProperties().getAttackStyle().getStyle() == WeaponInterface.STYLE_CONTROLLED) {
			cumulativeStr += 1;
		}
		cumulativeStr *= getSetMultiplier(entity, Skills.STRENGTH);
		double hit = (16 + cumulativeStr + (bonus / 8) + ((cumulativeStr * bonus) * 0.016865)) * modifier;
		return (int) (hit / 10) + 1;
	}

	@Override
	public int calculateDefence(Entity entity, Entity attacker) {
		WeaponInterface.AttackStyle style = entity.getProperties().getAttackStyle();
		int styleBonus = 0;
		if (style.getStyle() == WeaponInterface.STYLE_DEFENSIVE || style.getStyle() == WeaponInterface.STYLE_LONG_RANGE) {
			styleBonus = 3;
		} else if (style.getStyle() == WeaponInterface.STYLE_CONTROLLED) {
			styleBonus = 1;
		}
		int level = entity.getSkills().getLevel(Skills.DEFENCE);
		double prayer = 1.0;
		if (entity instanceof Player) {
			prayer += ((Player) entity).getPrayer().getSkillBonus(Skills.DEFENCE);
		}
		double effective = Math.floor((level * prayer) + styleBonus);
		int equipment = entity.getProperties().getBonuses()[attacker.getProperties().getAttackStyle().getBonusType() + 5];
		return (int) Math.floor(((effective + 8) * (equipment + 64)) / 10);
	}

	@Override
	public double getSetMultiplier(Entity e, int skillId) {
		if (e.getProperties().getArmourSet() == ArmourSet.DHAROK && skillId == Skills.STRENGTH) {
//			System.out.println("Fiist number -> " + 1.0 + ((e.getSkills().getMaximumLifepoints() - e.getSkills().getLifepoints()) * 0.01));
			return 1.0 + ((e.getSkills().getMaximumLifepoints() - e.getSkills().getLifepoints()) * 0.01);
		}
		if (e instanceof Player) {
			Player player = (Player) e;
			Container c = ((Player) e).getEquipment();
			int itemId = c.getNew(EquipmentContainer.SLOT_HAT).getId();
			if ((skillId == Skills.ATTACK || skillId == Skills.STRENGTH) && ((itemId >= 8901 && itemId <= 8921) || itemId == 13263)) {
				Entity victim = e.getProperties().getCombatPulse().getVictim();
				if (victim instanceof NPC && ((NPC) victim).getTask() == player.getSlayer().getTask()) {
					return 1.15;
				}
			}
			if (containsVoidSet(c) && c.getNew(EquipmentContainer.SLOT_HAT).getId() == 11665) {
				return 1.1;
			}
		}
		return 1.0;
	}

	@Override
	public void addExperience(Entity entity, Entity victim, BattleState state) {
		if (entity instanceof Player && state != null) {
			if (victim instanceof Player && entity.asPlayer().getIronmanManager().isIronman()) {
				return;
			}
			int hit = state.getEstimatedHit();
			if (hit < 0) {
				hit = 0;
			}
			if (state.getSecondaryHit() > 0) {
				hit += state.getSecondaryHit();
			}
			double experience = hit * EXPERIENCE_MOD;
			Player p = (Player) entity;
			boolean famExp = entity.getAttribute("fam-exp", false) && p.getFamiliarManager().hasFamiliar();
			if (!famExp) {
				entity.getSkills().addExperience(Skills.HITPOINTS, hit * 1.33, true);
			}
			switch (famExp ? p.getFamiliarManager().getFamiliar().getAttackStyle() : p.getProperties().getAttackStyle().getStyle()) {
			case WeaponInterface.STYLE_ACCURATE:
				p.getSkills().addExperience(Skills.ATTACK, experience, true);
				break;
			case WeaponInterface.STYLE_AGGRESSIVE:// strength.
				p.getSkills().addExperience(Skills.STRENGTH, experience, true);
				break;
			case WeaponInterface.STYLE_DEFENSIVE:// defence.
				p.getSkills().addExperience(Skills.DEFENCE, experience, true);
				break;
			case WeaponInterface.STYLE_CONTROLLED:// shared.
				experience /= 3;
				p.getSkills().addExperience(Skills.ATTACK, experience, true);
				p.getSkills().addExperience(Skills.STRENGTH, experience, true);
				p.getSkills().addExperience(Skills.DEFENCE, experience, true);
				break;
			case WeaponInterface.STYLE_CAST:// familiar.
				p.getSkills().addExperience(Skills.MAGIC, experience, true);
				break;
			}
		}
	}

	@Override
	public ArmourSet getArmourSet(Entity e) {
		if (ArmourSet.DHAROK.isUsing(e)) {
			return ArmourSet.DHAROK;
		}
		if (ArmourSet.GUTHAN.isUsing(e)) {
			return ArmourSet.GUTHAN;
		}
		if (ArmourSet.VERAC.isUsing(e)) {
			return ArmourSet.VERAC;
		}
		if (ArmourSet.TORAG.isUsing(e)) {
			return ArmourSet.TORAG;
		}
		return super.getArmourSet(e);
	}
	
    /**
     * Check to see whether an NPC is classified as undead.
     * @param name
     * @return true if so
     */
    public boolean checkUndead(String name){
	if(name.equals("Zombie") || name.contains("rmoured") || name.equals("Ankou") || name.equals("Crawling Hand") || name.equals("Banshee")
		|| name.equals("Ghost") || name.equals("Ghast") || name.equals("Mummy") || name.contains("Revenant")
		|| name.equals("Skeleton") || name.equals("Zogre") || name.equals("Spiritual Mage")){
	    return true;
	}
		return false;
    }
}