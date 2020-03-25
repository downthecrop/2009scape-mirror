package org.crandor.game.node.entity.combat;

import org.crandor.game.container.Container;
import org.crandor.game.container.impl.EquipmentContainer;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.equipment.ArmourSet;
import org.crandor.game.node.entity.combat.equipment.DegradableEquipment;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.audio.Audio;
import org.crandor.game.node.entity.player.link.prayer.PrayerType;
import org.crandor.game.node.entity.state.EntityState;
import org.crandor.game.node.item.Item;
import org.crandor.game.system.mysql.impl.ItemConfigSQLHandler;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.path.Pathfinder;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.tools.RandomFunction;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles a combat swing.
 * @author Emperor
 */
public abstract class CombatSwingHandler {

	/**
	 * The amount of experience to get per hit.
	 */
	public static double EXPERIENCE_MOD = 4;

	/**
	 * The mapping of the special attack handlers.
	 */
	private Map<Integer, CombatSwingHandler> specialHandlers;

	/**
	 * The combat style.
	 */
	private CombatStyle type;

	/**
	 * Constructs a new {@code CombatSwingHandler} {@Code Object}
	 * @param type The combat style.
	 */
	public CombatSwingHandler(CombatStyle type) {
		this.type = type;
	}

	/**
	 * Starts the combat swing.
	 * @param entity The attacking entity.
	 * @param victim The victim.
	 * @param state The battle state instance.
	 * @return The amount of ticks before impact of the attack.
	 */
	public abstract int swing(Entity entity, Entity victim, BattleState state);

	/**
	 * Handles the impact of the combat swing (victim getting hit).
	 * @param entity The attacking entity.
	 * @param victim The victim.
	 * @param state The battle state instance.
	 */
	public abstract void impact(Entity entity, Entity victim, BattleState state);

	/**
	 * Visualizes the impact itself (end animation, end GFX, ...)
	 * @param entity The attacking entity.
	 * @param victim The victim.
	 * @param state The battle state instance.
	 */
	public abstract void visualizeImpact(Entity entity, Entity victim, BattleState state);

	/**
	 * Calculates the maximum accuracy of the entity.
	 * @param entity The entity.
	 * @param modifier The modifier.
	 * @return The maximum accuracy value.
	 */
	public abstract int calculateAccuracy(Entity entity);

	/**
	 * Calculates the maximum strength of the entity.
	 * @param entity The entity.
	 * @param victim The victim.
	 * @param modifier The modifier.
	 * @return The maximum strength value.
	 */
	public abstract int calculateHit(Entity entity, Entity victim, double modifier);

	/**
	 * Calculates the maximum defence of the entity.
	 * @param entity The entity.
	 * @param attacker The entity to defend against.
	 * @param modifier The modifier.
	 * @return The maximum defence value.
	 */
	public abstract int calculateDefence(Entity entity, Entity attacker);

	/**
	 * Gets the void set multiplier.
	 * @param e The entity.
	 * @param skillId The skill id.
	 * @return The multiplier.
	 */
	public abstract double getSetMultiplier(Entity e, int skillId);

	/**
	 * Visualizes the combat swing (start animation, GFX, projectile, ...)
	 * @param entity The attacking entity.
	 * @param victim The victim.
	 * @param state The battle state instance.
	 */
	public void visualize(Entity entity, Entity victim, BattleState state) {
		entity.animate(getAttackAnimation(entity, type));
	}

	/**
	 * Method called when the impact method got called.
	 * @param entity The attacking entity.
	 * @param victim The victim.
	 * @param state The battle state.
	 */
	public void onImpact(final Entity entity, final Entity victim, final BattleState state) {
		if (entity instanceof Player && victim != null) {
			DegradableEquipment.degrade((Player) entity, victim, true);
		}
		if (state == null) {
			return;
		}
		if (state.getTargets() != null && state.getTargets().length > 0) {
			if (!(state.getTargets().length == 1 && state.getTargets()[0] == state)) {
				for (BattleState s : state.getTargets()) {
					if (s != null && s != state) {
						onImpact(entity, s.getVictim(), s);
					}
				}
				return;
			}
		}
		victim.onImpact(entity, state);
	}

	/**
	 * Gets the currently worn armour set, if any.
	 * @param e The entity.
	 * @return The armour set worn.
	 */
	public ArmourSet getArmourSet(Entity e) {
		return null;
	}

	/**
	 * Checks if the container contains a void knight set.
	 * @param c The container to check.
	 * @return {@code True} if so.
	 */
	public boolean containsVoidSet(Container c) {
		Item top = c.getNew(EquipmentContainer.SLOT_CHEST);
		if (top.getId() != 8839 && top.getId() != 10611) {
			return false;
		}
		return c.getNew(EquipmentContainer.SLOT_LEGS).getId() == 8840 && c.getNew(EquipmentContainer.SLOT_HANDS).getId() == 8842;
	}

	/**
	 * Checks if the hit will be accurate.
	 * @param entity The entity.
	 * @param victim The victim.
	 * @return {@code True} if the hit is accurate.
	 */
	public boolean isAccurateImpact(Entity entity, Entity victim) {
		return isAccurateImpact(entity, victim, type, 1.0, 1.0);
	}

	/**
	 * Checks if the hit will be accurate.
	 * @param entity The entity.
	 * @param victim The victim.
	 * @param style The combat style used.
	 * @return {@code True} if the hit is accurate.
	 */
	public boolean isAccurateImpact(Entity entity, Entity victim, CombatStyle style) {
		return isAccurateImpact(entity, victim, style, 1.0, 1.0);
	}

	/**
	 * Checks if the hit will be accurate.
	 * @param entity The entity.
	 * @param victim The victim.
	 * @param style The combat style (null to ignore prayers).
	 * @param accuracyMod The accuracy modifier.
	 * @param defenceMod The defence modifier.
	 * @return {@code True} if the hit is accurate.
	 */
	public boolean isAccurateImpact(Entity entity, Entity victim, CombatStyle style, double accuracyMod, double defenceMod) {
		double mod = 1.33;
		if (victim == null || style == null) {
			return false;
		}
		if (style != null) {
			if (victim instanceof Player && entity instanceof Familiar && ((Player) victim).getPrayer().get(PrayerType.PROTECT_FROM_SUMMONING)) {
				mod = 0;
			}
		}
		double attack = calculateAccuracy(entity) * accuracyMod * mod * getSetMultiplier(entity, Skills.ATTACK);
		double defence = calculateDefence(victim, entity) * defenceMod * getSetMultiplier(victim, Skills.DEFENCE);
		double chance = 0.0;
		if (attack < defence) {
			chance = (attack - 1) / (defence * 2);
		} else {
			chance = 1 - ((defence + 1) / (attack * 2));
		}
		double ratio = chance * 100;
		double accuracy = Math.floor(ratio);
		double block = Math.floor(101 - ratio);
		double acc = Math.random() * accuracy;
		double def = Math.random() * block;
		return acc > def;
	}

	/**
	 * Checks if the entity can execute a combat swing at the victim.
	 * @param entity The entity.
	 * @param victim The victim.
	 * @return {@code True} if so.
	 */
	public InteractionType canSwing(Entity entity, Entity victim) {
		return isAttackable(entity, victim);
	}

	/**
	 * Checks if the victim can be attacked by the entity.
	 * @param entity The attacking entity.
	 * @param victim The entity being attacked.
	 * @return {@code True} if so.
	 */
	public InteractionType isAttackable(Entity entity, Entity victim) {
		if (entity.getLocation().equals(victim.getLocation())) {
			if (entity.getIndex() < victim.getIndex() && victim.getProperties().getCombatPulse().getVictim() == entity) {
				return InteractionType.STILL_INTERACT;
			}
			return InteractionType.NO_INTERACT;
		}
		Location el = entity.getLocation();
		Location vl = victim.getLocation();
		Location evl = vl.transform(victim.size(), victim.size(), 0);
		if ((el.getX() >= vl.getX() && el.getX() < evl.getX() && el.getY() >= vl.getY() && el.getY() < evl.getY()) || (el.getZ() != vl.getZ())) {
			return InteractionType.NO_INTERACT;
		}
		if (!victim.isAttackable(entity, type)) {
			entity.getProperties().getCombatPulse().stop();
			return InteractionType.NO_INTERACT;
		}
		return InteractionType.STILL_INTERACT;
	}

	/**
	 * Gets the dragonfire message.
	 * @param protection The protection value.
	 * @param fireName The fire breath name.
	 * @return The message to send.
	 */
	public String getDragonfireMessage(int protection, String fireName) {
		if ((protection & 0x4) != 0) {
			if ((protection & 0x2) != 0) {
				return "Your potion and shield fully protects you from the dragon's " + fireName + ".";
			}
			if ((protection & 0x8) != 0) {
				return "Your prayer and shield absorbs most of the dragon's " + fireName + ".";
			}
			return "Your shield absorbs most of the dragon's " + fireName + ".";
		}
		if ((protection & 0x2) != 0) {
			if ((protection & 0x8) != 0) {
				return "Your prayer and potion absorbs most of the dragon's " + fireName + ".";
			}
			return "Your antifire potion helps you defend the against the dragon's " + fireName + ".";
		}
		if ((protection & 0x8) != 0) {
			return "Your magic prayer absorbs some of the dragon's " + fireName + ".";
		}
		return "You are horribly burnt by the dragon's " + fireName + ".";
	}

	/**
	 * Visualizes the audio.
	 * @param entity the entity.
	 * @param victim the victim.
	 * @param state the state.
	 */
	public void visualizeAudio(Entity entity, Entity victim, BattleState state) {
		if (entity instanceof Player) {
			int styleIndex = ((Player) entity).getSettings().getAttackStyleIndex();
			if (state.getWeapon() != null && state.getWeapon().getItem() != null) {
				Item weapon = state.getWeapon().getItem();
				Audio[] audios = weapon.getDefinition().getConfiguration(ItemConfigSQLHandler.ATTACK_AUDIO, null);
				if (audios != null) {
					Audio audio = null;
					if (styleIndex < audios.length) {
						audio = audios[styleIndex];
					}
					if (audio == null || audio.getId() == 0) {
						audio = audios[0];
					}
					if (audio != null) {
						entity.asPlayer().getAudioManager().send(audio, true);
					}
				}
			} else {
				entity.asPlayer().getAudioManager().send(2564);
			}
		} else if (entity instanceof NPC && victim instanceof Player) {
			NPC npc = entity.asNpc();
			Audio audio = npc.getAudio(0);
			if (audio != null) {
				audio.send(victim.asPlayer(), true);
			}
		}
	}

	/**
	 * Gets the combat distance.
	 * @param e The entity.
	 * @param v The victim.
	 * @param distance The distance.
	 * @return The actual distance used for combat.
	 */
	public int getCombatDistance(Entity e, Entity v, int distance) {
		if (e instanceof NPC) {
			NPC n = (NPC) e;
			if (n.getDefinition().getCombatDistance() > 0) {
				distance = n.getDefinition().getCombatDistance();
			}
		}
		return (e.size() >> 1) + (v.size() >> 1) + distance;
	}

	/**
	 * Formats the hit for the victim. (called as
	 * victim.getSwingHandler(false).formatHit(victim, hit))
	 * @param e The entity dealing the hit.
	 * @param victim The entity receiving the hit.
	 * @param hit The hit to format.
	 * @return The formatted hit.
	 */
	public int formatHit(Entity e, Entity victim, int hit) {
		if (hit < 1) {
			return hit;
		}
		if (hit > victim.getSkills().getLifepoints()) {
			hit = victim.getSkills().getLifepoints();
		}
		return hit;
	}

	/**
	 * Adjusts the battle state object for this combat swing.
	 * @param entity The attacking entity.
	 * @param victim The victim.
	 * @param state The battle state.
	 */
	public void adjustBattleState(Entity entity, Entity victim, BattleState state) {
		this.EXPERIENCE_MOD = 4;
		int totalHit = 0;
		if (entity instanceof Player) {
			((Player) entity).getFamiliarManager().adjustBattleState(state);
		}
		entity.sendImpact(state);
		victim.checkImpact(state);
		//Prevents lumbridge dummies from dying (true to how rs3 / runescape in 2009 does it)
		if((victim.getId() == 4474 && this.type == CombatStyle.MAGIC) || (victim.getId() == 7891 && this.type == CombatStyle.MELEE)) {
			this.EXPERIENCE_MOD = 0.1;
			victim.fullRestore();
			if(state.getEstimatedHit() >= 15){state.setEstimatedHit(14);};
			if(state.getSecondaryHit() >= 15){state.setSecondaryHit(14);}
		}
		if(victim.getId() == 757){
			this.EXPERIENCE_MOD = 0.01;
		}
		if (state.getEstimatedHit() > 0) {
			state.setEstimatedHit(getFormatedHit(entity, victim, state, state.getEstimatedHit()));
			totalHit += state.getEstimatedHit();
		}
		if (state.getSecondaryHit() > 0) {
			state.setSecondaryHit(getFormatedHit(entity, victim, state, state.getSecondaryHit()));
			totalHit += state.getSecondaryHit();
		}
		if (entity instanceof Player) {
			Player p = (Player) entity;
			if (totalHit > 0 && p.getPrayer().get(PrayerType.SMITE) && victim.getSkills().getPrayerPoints() > 0) {
				victim.getSkills().decrementPrayerPoints(totalHit * 0.25);
			}
			if (entity.getAttribute("1hko", false)) {
				state.setEstimatedHit(victim.getSkills().getLifepoints());
			}
		}
		if (victim instanceof NPC) {
			NPC n = (NPC) victim;
			if (n.getProperties().getProtectStyle() != null && state.getStyle() == n.getProperties().getProtectStyle()) {
				state.neutralizeHits();
			}
		}
	}

	/**
	 * Adds the experience for the current combat swing.
	 * @param entity The attacking entity.
	 * @param victim The victim.
	 * @param state The battle state.
	 */
	public void addExperience(Entity entity, Entity victim, BattleState state) {
	}

	/**
	 * Gets the formated hit.
	 * @param attacker The attacking entity.
	 * @param victim The victim.
	 * @param state The battle state.
	 * @param hit The hit to format.
	 * @return The formated hit.
	 */
	protected int getFormatedHit(Entity attacker, Entity victim, BattleState state, int hit) {
		hit = (int) attacker.getFormatedHit(state, hit);
		if(victim instanceof Player){
			Player player = victim.asPlayer();
			Item shield = player.getEquipment().get(EquipmentContainer.SLOT_SHIELD);
			if (shield != null && shield.getId() == 13742) {
				if (RandomFunction.random(100) < 71) {
					hit -= ((double) hit * 0.25D);
					if(hit < 1){
						hit = 0;
					}
				}
			}
			if (shield != null && shield.getId() == 13740) {
				double reduce = ((double) hit * 0.30D);
				double drain = hit * 0.15D;
				if (player.getSkills().getPrayerPoints() > drain && drain > 0) {
					if(drain < 1){
						drain = 1;
					}
					hit -= reduce;
					if(hit < 1){
						hit = 0;
					}
					player.getSkills().decrementPrayerPoints(drain);
				}
			}
			if(player.getStateManager().hasState(EntityState.STAFF_OF_THE_DEAD)){
				if(attacker.getProperties().getCombatPulse().getStyle().equals(CombatStyle.MELEE)){
					player.graphics(new Graphics(1592));
					hit /= 2;
				}
			}
		}
		if(attacker instanceof Player){
			Player player = attacker.asPlayer();
			if(player.getEquipment().get(3) != null && player.getEquipment().get(3).getId() == 14726 && state.getStyle().equals(CombatStyle.MAGIC)){
				hit += ((double) hit * 0.15D);
			}
		}
		if (attacker instanceof Familiar && victim instanceof Player) {
			if (((Player) victim).getPrayer().get(PrayerType.PROTECT_FROM_SUMMONING)) {
				hit = 0;
			}
		}
		return formatHit(attacker, victim, hit);
	}

	/**
	 * Checks if a projectile can be fired from the node location to the victim
	 * location.
	 * @param entity The node.
	 * @param victim The victim.
	 * @param checkClose If we are checking for a melee attack rather than a
	 * projectile.
	 * @return {@code True} if so.
	 */
	public static boolean isProjectileClipped(Node entity, Node victim, boolean checkClose) {
		if (checkClose) {
			if (entity.getId() == 54) {// /temp until emp is back.
				return Pathfinder.find((Entity) entity, victim, false, Pathfinder.SMART).isSuccessful();
			}
			return Pathfinder.find((Entity) entity, victim, false, Pathfinder.DUMB).isSuccessful();
		}
		return Pathfinder.find((Entity) entity, victim, false, Pathfinder.PROJECTILE).isSuccessful();
	}

	/**
	 * Gets the default animation of the entity.
	 * @param e The entity.
	 * @param style The combat style.
	 * @return The attack animation.
	 */
	public Animation getAttackAnimation(Entity e, CombatStyle style) {
		Animation anim = null;
		if (type != null && e instanceof NPC) {
			anim = ((NPC) e).getProperties().getCombatAnimation(style.ordinal() % 3);
		}
		if (anim == null) {
			return e.getProperties().getAttackAnimation();
		}
		return anim;
	}

	/**
	 * Registers a special attack handler.
	 * @param itemId The item id.
	 * @param handler The combat swing handler.
	 * @return {@code True} if succesful.
	 */
	public boolean register(int itemId, CombatSwingHandler handler) {
		if (specialHandlers == null) {
			specialHandlers = new HashMap<>();
		}
		if (specialHandlers.containsKey(itemId)) {
			System.err.println("Already contained special attack handler for item " + itemId + " - [old=" + specialHandlers.get(itemId).getClass().getSimpleName() + ", new=" + handler.getClass().getSimpleName() + "].");
			return false;
		}
		return specialHandlers.put(itemId, handler) == null;
	}

	/**
	 * Gets the special attack handler for the given item id.
	 * @param itemId The item id.
	 * @return The special attack handler, or {@code null} if this item has no
	 * special attack handler.
	 */
	public CombatSwingHandler getSpecial(int itemId) {
		if (specialHandlers == null) {
			specialHandlers = new HashMap<>();
		}
		return specialHandlers.get(itemId);
	}

	/**
	 * Sets the combat style.
	 * @param type The type.
	 */
	public void setType(CombatStyle type) {
		this.type = type;
	}

	/**
	 * @return the type.
	 */
	public CombatStyle getType() {
		return type;
	}

}