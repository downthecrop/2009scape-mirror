package org.crandor.game.node.entity.combat.handlers;

import org.crandor.game.container.Container;
import org.crandor.game.container.impl.EquipmentContainer;
import org.crandor.game.content.global.tutorial.TutorialSession;
import org.crandor.game.content.global.tutorial.TutorialStage;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.*;
import org.crandor.game.node.entity.combat.equipment.ArmourSet;
import org.crandor.game.node.entity.combat.equipment.SpellType;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.tools.RandomFunction;

/**
 * Handles the magic combat swings.
 * @author Emperor
 */
public class MagicSwingHandler extends CombatSwingHandler {

	/**
	 * Constructs a new {@code RangeSwingHandler} {@Code Object}.
	 */
	public MagicSwingHandler() {
		super(CombatStyle.MAGIC);
	}

	@Override
	public InteractionType canSwing(Entity entity, Entity victim) {
		if (!isProjectileClipped(entity, victim, false)) {
			return InteractionType.NO_INTERACT;
		}
		int distance = 10;
		InteractionType type = InteractionType.STILL_INTERACT;
		boolean goodRange = victim.getCenterLocation().withinDistance(entity.getCenterLocation(), getCombatDistance(entity, victim, distance));
		if (victim.getWalkingQueue().isMoving() && !goodRange) {
			goodRange = victim.getCenterLocation().withinDistance(entity.getCenterLocation(), getCombatDistance(entity, victim, ++distance));
			type = InteractionType.MOVE_INTERACT;
		}
		if (goodRange && isAttackable(entity, victim) != InteractionType.NO_INTERACT) {
			if (type == InteractionType.STILL_INTERACT) {
				entity.getWalkingQueue().reset();
			}
			return type;
		}
		return InteractionType.NO_INTERACT;
	}

	@Override
	public int swing(Entity entity, Entity victim, BattleState state) {
		CombatSpell spell = entity.getProperties().getSpell();
		if (spell == null) {
			if ((spell = entity.getProperties().getAutocastSpell()) == null) {
				return -1;
			}
		}
		state.setStyle(CombatStyle.MAGIC);
		if (!spell.meetsRequirements(entity, true, true)) {
			entity.getProperties().setSpell(null);
			WeaponInterface inter = entity.getExtension(WeaponInterface.class);
			if (inter != null) {
				inter.selectAutoSpell(-1, true);
				entity.getProperties().getCombatPulse().updateStyle();
			}
			return -1;
		}
		int max = calculateHit(entity, victim, spell.getMaximumImpact(entity, victim, state));
		state.setTargets(spell.getTargets(entity, victim));
		state.setSpell(spell);
		for (BattleState s : state.getTargets()) {
			int hit = -1;
			s.setSpell(spell);
			if (isAccurateImpact(entity, s.getVictim(), CombatStyle.MAGIC, 1.3, 1.0)) {
				s.setMaximumHit(max);
				hit = RandomFunction.random(max);
			}
			s.setStyle(CombatStyle.MAGIC);
			s.setEstimatedHit(hit);
		}
		if (spell == entity.getProperties().getSpell()) {
			entity.getProperties().setSpell(null);
			if (entity.getProperties().getAutocastSpell() == null) {
				entity.getProperties().getCombatPulse().stop();
			}
		}
		int ticks = 2 + (int) Math.floor(entity.getLocation().getDistance(victim.getLocation()) * 0.5);
		if (spell.getType() == SpellType.BLITZ) {
			ticks++;
		}
		return ticks;
	}

	@Override
	public void adjustBattleState(Entity entity, Entity victim, BattleState state) {
		if (state.getTargets() == null) {
			super.adjustBattleState(entity, victim, state);
			if (state.getSpell() != null) {
				state.getSpell().fireEffect(entity, victim, state);
			}
			return;
		}
		for (BattleState s : state.getTargets()) {
			if (s != null) {
				super.adjustBattleState(entity, s.getVictim(), s);
				if (s.getSpell() != null) {
					s.getSpell().fireEffect(entity, s.getVictim(), s);
				}
			}
		}
	}

	@Override
	public void visualize(Entity entity, Entity victim, BattleState state) {
		state.getSpell().visualize(entity, victim);
	}

	@Override
	public void impact(Entity entity, Entity victim, BattleState state) {
		if (state.getSpell() != null && entity instanceof Player) {
			entity.getSkills().addExperience(Skills.MAGIC, state.getSpell().getExperience());
		}
		if (state.getTargets() == null) {
			if (state.getEstimatedHit() > -1) {
				victim.getImpactHandler().handleImpact(entity, state.getEstimatedHit(), CombatStyle.MAGIC, state);
			}
			return;
		}
		for (BattleState s : state.getTargets()) {
			if (s == null || s.getEstimatedHit() < 0) {
				continue;
			}
			int hit = s.getEstimatedHit();
			s.getVictim().getImpactHandler().handleImpact(entity, hit, CombatStyle.MAGIC, s);
		}
	}

	@Override
	public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
		if (entity instanceof Player) {
			if (TutorialSession.getExtension(((Player) entity)).getStage() == 51) {
				TutorialStage.load(((Player) entity), 52, false);
			}
			if (TutorialSession.getExtension(((Player) entity)).getStage() == 52) {
				TutorialStage.load(((Player) entity), 53, false);
			}
			if (TutorialSession.getExtension(((Player) entity)).getStage() == 54) {
				TutorialStage.load(((Player) entity), 55, false);
			}
		}
		if (state.getTargets() == null) {
			if (state.getSpell() != null) {
				state.getSpell().visualizeImpact(entity, victim, state);
			}
			return;
		}
		for (BattleState s : state.getTargets()) {
			if (s != null) {
				state.getSpell().visualizeImpact(entity, s.getVictim(), s);
			}
		}
	}

	@Override
	public int calculateAccuracy(Entity entity) {
		int baseLevel = entity.getSkills().getStaticLevel(Skills.MAGIC);
		int spellRequirement = baseLevel;
		if (entity instanceof Player) {
			if (entity.getProperties().getSpell() != null) {
				spellRequirement = entity.getProperties().getSpell().getLevel();
			} else if (entity.getProperties().getAutocastSpell() != null) {
				spellRequirement = entity.getProperties().getAutocastSpell().getLevel();
			}
		}
		double spellBonus = 0.0;
		if (baseLevel > spellRequirement) {
			spellBonus = (baseLevel - spellRequirement) * .3;
		}
		int level = entity.getSkills().getLevel(Skills.MAGIC, true);
		double prayer = 1.0;
		if (entity instanceof Player) {
			prayer += ((Player) entity).getPrayer().getSkillBonus(Skills.MAGIC);
		}
		double additional = 1.0; // Slayer helmet/salve/...
		double effective = Math.floor(((level * prayer) * additional) + spellBonus);
		int bonus = entity.getProperties().getBonuses()[WeaponInterface.BONUS_MAGIC];
		return (int) Math.floor(((effective + 8) * (bonus + 64)) / 10);
	}

	@Override
	public int calculateHit(Entity entity, Entity victim, double baseDamage) {
		if (baseDamage < 2.0 || entity instanceof NPC) {
			if (baseDamage > 2.0) {
				baseDamage = 1.0;
			}
			int level = entity.getSkills().getLevel(Skills.MAGIC, true);
			int bonus = entity.getProperties().getBonuses()[entity instanceof Player ? 14 : 13];
			double cumulativeStr = level;
			return 1 + (int) ((14 + cumulativeStr + (bonus / 8) + ((cumulativeStr * bonus) * 0.016865)) * baseDamage) / 10;
		}
		double levelMod = 1.0;
		double entityMod = entity.getLevelMod(entity, victim);
		if (entityMod != 0) {
			levelMod += entityMod;
		}
		return (int) (baseDamage * levelMod) + 1;
	}

	@Override
	public int calculateDefence(Entity entity, Entity attacker) {
		int level = entity.getSkills().getLevel(Skills.DEFENCE, true);
		double prayer = 1.0;
		if (entity instanceof Player) {
			prayer += ((Player) entity).getPrayer().getSkillBonus(Skills.MAGIC);
		}
		double effective = Math.floor((level * prayer) * 0.3) + (entity.getSkills().getLevel(Skills.MAGIC, true) * 0.7);
		int equipment = entity.getProperties().getBonuses()[WeaponInterface.BONUS_MAGIC + 5];
		return (int) Math.floor(((effective + 8) * (equipment + 64)) / 10);
	}

	@Override
	public double getSetMultiplier(Entity e, int skillId) {
		if (e instanceof Player) {
			Container c = ((Player) e).getEquipment();
			if (containsVoidSet(c) && c.getNew(EquipmentContainer.SLOT_HAT).getId() == 11663) {
				return 1.3;
			}
		}
		return 1.0;
	}

	@Override
	public void addExperience(Entity entity, Entity victim, BattleState state) {
		if (entity instanceof Player) {
			if (victim instanceof Player && entity.asPlayer().getIronmanManager().isIronman()) {
				return;
			}
			int hit = 0;
			if (state.getTargets() != null) {
				for (BattleState s : state.getTargets()) {
					if (s != null && s.getEstimatedHit() > 0) {
						hit += s.getEstimatedHit();
					}
				}
			} else if (state.getEstimatedHit() > 0) {
				hit += state.getEstimatedHit();
			}
			if (state.getSpell() != null) {
				state.getSpell().addExperience(entity, hit);
			} else {
				Player p = (Player) entity;
				boolean famExp = entity.getAttribute("fam-exp", false) && p.getFamiliarManager().hasFamiliar();
				if (!famExp) {
					entity.getSkills().addExperience(Skills.HITPOINTS, hit * 1.33, true);
				} else {
					Familiar fam = p.getFamiliarManager().getFamiliar();
					int skill = Skills.MAGIC;
					switch (fam.getAttackStyle()) {
					case WeaponInterface.STYLE_DEFENSIVE:
						skill = Skills.DEFENCE;
						break;
					case WeaponInterface.STYLE_ACCURATE:
						skill = Skills.ATTACK;
						break;
					case WeaponInterface.STYLE_AGGRESSIVE:
						skill = Skills.STRENGTH;
						break;
					case WeaponInterface.STYLE_RANGE_ACCURATE:
						skill = Skills.RANGE;
						break;
					case WeaponInterface.STYLE_CONTROLLED:
						double experience = hit * EXPERIENCE_MOD;
						experience /= 3;
						entity.getSkills().addExperience(Skills.ATTACK, experience, true);
						entity.getSkills().addExperience(Skills.STRENGTH, experience, true);
						entity.getSkills().addExperience(Skills.DEFENCE, experience, true);
						return;
					}
					entity.getSkills().addExperience(skill, hit * EXPERIENCE_MOD, true);
					return;
				}
				entity.getSkills().addExperience(Skills.MAGIC, hit * EXPERIENCE_MOD, true);
			}
		}
	}

	@Override
	public ArmourSet getArmourSet(Entity e) {
		if (ArmourSet.AHRIM.isUsing(e)) {
			return ArmourSet.AHRIM;
		}
		return super.getArmourSet(e);
	}

}
