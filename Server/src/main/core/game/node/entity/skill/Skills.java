package core.game.node.entity.skill;

import content.global.skill.skillcapeperks.SkillcapePerks;
import core.ServerConstants;
import core.game.event.DynamicSkillLevelChangeEvent;
import core.game.event.XPGainEvent;
import content.global.handlers.item.equipment.brawling_gloves.BrawlingGloves;
import content.global.handlers.item.equipment.brawling_gloves.BrawlingGlovesManager;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.ImpactHandler;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.request.assist.AssistSession;
import core.game.node.item.Item;
import core.net.packet.PacketRepository;
import core.net.packet.context.SkillContext;
import core.net.packet.out.SkillLevel;
import kotlin.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import core.game.node.entity.player.info.PlayerMonitor;
import core.game.world.GameWorld;
import core.game.world.repository.Repository;
import core.plugin.CorePluginTypes.XPGainPlugins;
import org.rs09.consts.Items;
import org.rs09.consts.Sounds;

import java.util.ArrayList;

import static core.api.ContentAPIKt.getWorldTicks;
import static core.api.ContentAPIKt.playAudio;
import static java.lang.Math.floor;
import static java.lang.Math.max;

/**
 * Represents an entity's skills.
 * @author Emperor
 */
public final class Skills {

	/**
	 * Represents the constant modifier of experience.
	 */
	public double experienceMultiplier = 5.0;

	/**
	 * The maximum experience multiplier.
	 */
	public static final double MAX_EXPERIENCE_MOD = 60.0;

	/**
	 * Represents an array of skill names.
	 */
	public static final String[] SKILL_NAME = { "Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting", "Hunter", "Construction", "Summoning" };

	/**
	 * Constants for the skill ids.
	 */
	public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2, HITPOINTS = 3, RANGE = 4, PRAYER = 5, MAGIC = 6, COOKING = 7, WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11, CRAFTING = 12, SMITHING = 13, MINING = 14, HERBLORE = 15, AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19, RUNECRAFTING = 20, HUNTER = 21, CONSTRUCTION = 22, SUMMONING = 23;

	/**
	 * Number of skills in game
	 */
	public static final int NUM_SKILLS = 24;

	/**
	 * Represents the entity instance.
	 */
	private final Entity entity;

	/**
	 * An array containing all the player's experience.
	 */
	private final double[] experience;

	private double[] lastUpdateXp = null;

	private int lastUpdate = GameWorld.getTicks();

	/**
	 * An array containing all the maximum levels.
	 */
	private final int[] staticLevels;

	/**
	 * An array containing all the current levels.
	 */
	private final int[] dynamicLevels;

	/**
	 * Represents the amount of prayer points left.
	 */
	private double prayerPoints = 1.;

	/**
	 * The player's life-points.
	 */
	private int lifepoints = 10;

	/**
	 * The amount of increased maximum lifepoints.
	 */
	private int lifepointsIncrease = 0;

	/**
	 * The total experience gained.
	 */
	private double experienceGained = 0;

	/**
	 * If a lifepoints update should occur.
	 */
	private boolean lifepointsUpdate;

	/**
	 * The combat milestones.
	 */
	private int combatMilestone;

	/**
	 * The skilling milestones.
	 */
	private int skillMilestone;

	public int lastTrainedSkill = -1;
	public int lastXpGain = 0;

	/**
	 * Constructs a new {@code Skills} {@code Object}.
	 * @param entity The entity.
	 */
	public Skills(Entity entity) {
		this.entity = entity;
		this.experience = new double[24];
		this.staticLevels = new int[24];
		this.dynamicLevels = new int[24];
		for (int i = 0; i < 24; i++) {
			this.staticLevels[i] = 1;
			this.dynamicLevels[i] = 1;
		}
		this.experience[HITPOINTS] = 1154;
		this.dynamicLevels[HITPOINTS] = 10;
		this.staticLevels[HITPOINTS] = 10;
		entity.getProperties().setCombatLevel(3);
	}


	/**
	 * Determine whether the specified skill is a combat skill.
	 * Prayer and Summoning are included and counted as combat skills.
	 * @param skill
	 * @return true if so.
	 */
	public boolean isCombat(int skill){
		if((skill >= ATTACK && skill <= MAGIC) || (skill == SUMMONING)){
			return true;
		}
		return false;
	}

	/**
	 * Configures the skills.
	 */
	public void configure() {
		updateCombatLevel();
	}

	/**
	 * Called every pulse (600ms).
	 */
	public void pulse() {
		if (lifepoints < 1) {
			return;
		}
	}

	/**
	 * Copies the skills data.
	 * @param skills The skills.
	 */
	public void copy(Skills skills) {
		for (int i = 0; i < 24; i++) {
			this.staticLevels[i] = skills.staticLevels[i];
			this.dynamicLevels[i] = skills.dynamicLevels[i];
			this.experience[i] = skills.experience[i];
		}
		prayerPoints = skills.prayerPoints;
		lifepoints = skills.lifepoints;
		lifepointsIncrease = skills.lifepointsIncrease;
		experienceGained = skills.experienceGained;
	}

	/**
	 * Adds experience to a skill.
	 * @param slot The skill slot.
	 * @param experience The experience.
	 */
	public void addExperience(int slot, double experience, boolean playerMod) {
		if (lastUpdateXp == null)
			lastUpdateXp = this.experience.clone();
		double mod = getExperienceMod(slot, experience, playerMod, true);
		final Player player = entity instanceof Player ? ((Player) entity) : null;
		final AssistSession assist = entity.getExtension(AssistSession.class);
		if (assist != null && assist.translateExperience(player, slot, experience, mod)) {
			return;
		}
		boolean already200m = this.experience[slot] == 200000000;
		double experienceAdd = (experience * mod);
		//check if a player has brawling gloves and, if equipped, modify xp
		BrawlingGlovesManager bgManager = BrawlingGlovesManager.getInstance(player);
		if(!bgManager.GloveCharges.isEmpty()){
			Item gloves = BrawlingGloves.forSkill(slot) == null ? null : new Item(BrawlingGloves.forSkill(slot).getId());
			if(gloves == null && (slot == Skills.STRENGTH || slot == Skills.DEFENCE)){
				gloves = new Item (BrawlingGloves.forSkill(Skills.ATTACK).getId());
			}
			if(gloves != null && player.getEquipment().containsItem(gloves)){
				experienceAdd += experienceAdd * bgManager.getExperienceBonus();
				bgManager.updateCharges(gloves.getId(),1);
			}
		}
		//Check for Flame Gloves and Ring of Fire
		if(player.getEquipment().containsItem(new Item(Items.FLAME_GLOVES_13660)) || player.getEquipment().containsItem(new Item(Items.RING_OF_FIRE_13659))){
			if(slot == Skills.FIREMAKING){
				int count = 0;
				if(player.getEquipment().containsItem(new Item(Items.FLAME_GLOVES_13660))) count += 1;
				if(player.getEquipment().containsItem(new Item(Items.RING_OF_FIRE_13659))) count += 1;
				if(count == 2) experienceAdd += (0.05 * experienceAdd);
				else experienceAdd += (0.02 * experienceAdd);
			}
		}
		this.experience[slot] += experienceAdd;
		if (this.experience[slot] >= 200000000) {
			if(!already200m && !player.isArtificial()){
				Repository.sendNews(entity.asPlayer().getUsername()+" has just reached 200m experience in " + SKILL_NAME[slot] + "!");
			}
			this.experience[slot] = 200000000;
		}
		if (entity instanceof Player && this.experience[slot] > 175) {
			if (!player.getAttribute("tutorial:complete",false) && slot != HITPOINTS) {
				this.experience[slot] = 175;
			}
		}
		experienceGained += experienceAdd;
		XPGainPlugins.run(player,slot,experienceAdd);
		int newLevel = getStaticLevelByExperience(slot);
		if (newLevel > staticLevels[slot]) {
			int amount = newLevel - staticLevels[slot];
			if (dynamicLevels[slot] < newLevel) {
				dynamicLevels[slot] += amount;
			}
			if (slot == HITPOINTS) {
				lifepoints += amount;
			}
			staticLevels[slot] = newLevel;

			if (entity instanceof Player) {
                            player.updateAppearance();
			    LevelUp.levelup(player, slot, amount);
                            updateCombatLevel();
			}
		}
		if (entity instanceof Player) {
			PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, slot));
			entity.dispatch(new XPGainEvent(slot, experienceAdd));
		}
		if (GameWorld.getTicks() - lastUpdate >= 200) {
			ArrayList<Pair<Integer,Double>> diffs = new ArrayList<>();
			for (int i = 0; i < this.experience.length; i++) {
				double diff = this.experience[i] - lastUpdateXp[i];
				if (diff != 0.0) {
					diffs.add(new Pair<>(i, diff));
				}
			}
			PlayerMonitor.logXpGains(player, diffs);
			lastUpdateXp = this.experience.clone();
			lastUpdate = GameWorld.getTicks();
		}
		lastTrainedSkill = slot;
		lastXpGain = getWorldTicks();
	}

	/**
	 * Gets the current experience mod.
	 * @param slot The skill slot.
	 * @param experience The experience gained.
	 * @param playerMod If player mods should be applied.
	 * @return The experience mod.
	 */
	private double getExperienceMod(int slot, double experience, boolean playerMod, boolean multiplyer) {
		//Keywords for people ctrl + Fing the project
		//xprate xp rate xp multiplier skilling rate
		return experienceMultiplier;
		/*if (!(entity instanceof Player)) {
			return 1.0;
		}
		double mod = multiplyer ? (EXPERIENCE_MULTIPLIER) : 1;
		Player p = (Player) entity;
		if (p.getIronmanManager().getMode() == IronmanMode.ULTIMATE) {
			mod /= 4;
		} else if (p.getIronmanManager().getMode() == IronmanMode.STANDARD) {
			mod /= 2;
		}
		//A boost for combat skills that are under level 65.
		if(entity instanceof Player && !this.hasLevel(slot, 65) && isCombat(slot)){
			mod *= 1.5;
		}
		//Grand Exchange region XP boost.
		if(entity.getViewport().getRegion().getRegionId() == 12598){
			mod += 1.5;
		}
		// Pest control, XP halved during the game
		if (entity.getViewport().getRegion().getRegionId() == 10536) {
			mod *= .5;
		}
		if (SystemManager.getSystemConfig().isDoubleExp()) {
			mod *= 2;
		}
		if (HolidayEvent.getCurrent() != null) {
			HolidayEvent.getCurrent().addExperience(p, slot, experience);
		}
		p.getAntiMacroHandler().registerExperience(slot, experience);
		if (TutorialSession.getExtension(p).getStage() < TutorialSession.MAX_STAGE) {
			mod = 1.0;
		} else {
			if (playerMod && p.getExperienceMod() != 0.0) {
				mod *= p.getExperienceMod();
			}
		}
		if (mod > MAX_EXPERIENCE_MOD ) {
			return MAX_EXPERIENCE_MOD;
		}
		return mod;*/
	}

	/**
	 * Adds experience to the skills.
	 */
	public void addExperience(final int slot, double experience) {
		addExperience(slot, experience, false);
	}

	/**
	 * Gets the highest combat skill id.
	 * @return The id of the highest combat skill.
	 */
	public int getHighestCombatSkillId() {
		int id = 0;
		int last = 0;
		for (int i = 0; i < 5; i++) {
			if (staticLevels[i] > last) {
				last = staticLevels[i];
				id = i;
			}
		}
		return id;
	}

	/**
	 * Returns the dynamic levels to the static levels
	 */
	public void restore() {
		for (int i = 0; i < 24; i++) {
			int staticLevel = getStaticLevel(i);
			setLevel(i, staticLevel);
		}
		if (entity instanceof Player) {
			playAudio(entity.asPlayer(), Sounds.PRAYER_RECHARGE_2674);
		}
		rechargePrayerPoints();
	}

	public void parse(JSONArray skillData){
		for(int i = 0; i < skillData.size(); i++){
			JSONObject skill = (JSONObject) skillData.get(i);
			int id = Integer.parseInt( skill.get("id").toString());
			dynamicLevels[id] = Integer.parseInt(skill.get("dynamic").toString());
			staticLevels[id] = Integer.parseInt(skill.get("static").toString());
			experience[id] = Double.parseDouble(skill.get("experience").toString());
			int version = entity instanceof Player ? entity.asPlayer().version : ServerConstants.CURRENT_SAVEFILE_VERSION;
			if (i == HITPOINTS) {
				if (version < 3 && !skill.containsKey("lifepoints")) { //!1881
					lifepoints = dynamicLevels[id];
					dynamicLevels[id] = staticLevels[id];
				} else {
					lifepoints = Integer.parseInt(skill.get("lifepoints").toString());
				}
			}
			if (i == PRAYER) {
				if (version < 3 && !skill.containsKey("prayerPoints")) { //!1881
					prayerPoints = dynamicLevels[id];
					dynamicLevels[id] = staticLevels[id];
				} else {
					prayerPoints = Double.parseDouble(skill.get("prayerPoints").toString());
				}
			}
		}
	}

	public void correct(double divisor){
		for(int i = 0; i < staticLevels.length; i++){
			experience[i] /= divisor;
			staticLevels[i] = getStaticLevelByExperience(i);
			dynamicLevels[i] = staticLevels[i];
			if(i == PRAYER){
				setPrayerPoints(staticLevels[i]);
			}
			if(i == HITPOINTS){
				setLifepoints(staticLevels[i]);
			}
		}
		experienceMultiplier = 5.0;
		updateCombatLevel();
	}

	/**
	 * Refreshes all the skill levels.
	 */
	public void refresh() {
		if (!(entity instanceof Player)) {
			return;
		}
		Player player = (Player) entity;
		for (int i = 0; i < 24; i++) {
			PacketRepository.send(SkillLevel.class, new SkillContext(player, i));
		}
		LevelUp.sendFlashingIcons(player, -1);
	}

	/**
	 * Gets the static level.
	 * @param slot The skill's slot.
	 * @return The level.
	 */
	public int getStaticLevelByExperience(int slot) {
		double exp = experience[slot];

		int points = 0;
		int output = 0;
		for (byte lvl = 1; lvl < 100; lvl++) {
			points += floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) floor(points / 4);
			if ((output - 1) >= exp) {
				return lvl;
			}
		}
		return 99;
	}

	public int levelFromXP(double exp) {

		int points = 0;
		int output = 0;
		for (byte lvl = 1; lvl < 100; lvl++) {
			points += floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) floor(points / 4);
			if ((output - 1) >= exp) {
				return lvl;
			}
		}
		return 99;
	}

	/**
	 * Gets the experience for a certain level.
	 * @param level The level.
	 * @return The experience needed.
	 */
	public int getExperienceByLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) floor(points / 4);
		}
		return 0;
	}

	/**
	 * Updates the combat level.
	 * @return {@code True} if the combat level changed.
	 */
	@SuppressWarnings("deprecation")
	public boolean updateCombatLevel() {
		boolean update = false;
		int level = calculateCombatLevel();
		update = level != entity.getProperties().getCombatLevel();
		if (entity instanceof Player) {
			Player player = (Player) entity;
			int summon = staticLevels[SUMMONING] / 8;
			if (summon != player.getFamiliarManager().getSummoningCombatLevel()) {
				player.getFamiliarManager().setSummoningCombatLevel(summon);
				update = true;
			}
		}
		entity.getProperties().setCombatLevel(level);
		return update;
	}

	/**
	 * Gets the combat level (ignoring summoning).
	 * @return The combat level.
	 */
	private int calculateCombatLevel() {
		if (entity instanceof NPC) {
			return ((NPC) entity).getDefinition().getCombatLevel();
		}

		double base = 0.25 * (staticLevels[DEFENCE] + staticLevels[HITPOINTS] + floor(0.5 * staticLevels[PRAYER]));
		double meleeBase = 0.325 * (staticLevels[ATTACK] + staticLevels[STRENGTH]);
		double rangeBase = 0.325 * (floor(staticLevels[RANGE] / 2.0) * 3.0);
		double magicBase = 0.325 * (floor(staticLevels[MAGIC] / 2.0) * 3.0);

		return (int) (base + max(meleeBase, max(rangeBase, magicBase)));
	}

	/**
	 * @return the player
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * Gets the experience.
	 * @param slot The slot.
	 * @return The experience.
	 */
	public double getExperience(int slot) {
		return experience[slot];
	}

	/**
	 * Gets the static skill level.
	 * @param slot The slot.
	 * @return The static level.
	 */
	public int getStaticLevel(int slot) {
		return staticLevels[slot];
	}

	/**
	 * Sets the experience gained.
	 * @param experienceGained The experience gained.
	 */
	public void setExperienceGained(double experienceGained) {
		this.experienceGained = experienceGained;
	}

	/**
	 * Gets the experience gained.
	 * @return The experience gained.
	 */
	public double getExperienceGained() {
		return experienceGained;
	}

	/**
	 * Sets a dynamic level.
	 * @param slot The skill id.
	 * @param level The level.
	 */
	public void setLevel(int slot, int level) {
		if (slot == HITPOINTS) {
			lifepoints = level;
		} else if (slot == PRAYER) {
			prayerPoints = level;
		}

		int previousLevel = dynamicLevels[slot];
		dynamicLevels[slot] = level;

		if (entity instanceof Player) {
			PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, slot));
			entity.dispatch(new DynamicSkillLevelChangeEvent(slot, previousLevel, level));
		}
	}

	/**
	 * Gets a dynamic level.
	 * @param slot The skill id.
	 * @return The dynamic level.
	 */
	public int getLevel(int slot, boolean discardAssist) {
		if (!discardAssist) {
			if (entity instanceof Player) {
				final Player p = (Player) entity;
				final AssistSession assist = p.getExtension(AssistSession.class);
				if (assist != null && assist.getPlayer() != p) {
					Player assister = assist.getPlayer();
					int index = assist.getSkillIndex(slot);
					if (index != -1 && !assist.isRestricted()) {

						// assist.getSkills()[index] + ", " + SKILL_NAME[slot]);
						if (assist.getSkills()[index]) {
							int assistLevel = assister.getSkills().getLevel(slot);
							int playerLevel = dynamicLevels[slot];
							if (assistLevel > playerLevel) {
								return assistLevel;
							}
						}
					}
				}
			}
		}
		return dynamicLevels[slot];
	}

	/**
	 * Gets the level.
	 * @param slot the slot.
	 * @return the level.
	 */
	public int getLevel(int slot) {
		return getLevel(slot, false);
	}

	/**
	 * Sets the current amount of lifepoints.
	 * @param lifepoints The lifepoints.
	 */
	public void setLifepoints(int lifepoints) {
		this.lifepoints = lifepoints;
		if (this.lifepoints < 0) {
			this.lifepoints = 0;
		}
		lifepointsUpdate = true;
	}

	/**
	 * Gets the lifepoints.
	 * @return The lifepoints.
	 */
	public int getLifepoints() {
		return lifepoints;
	}

	/**
	 * Gets the maximum amount of lifepoints.
	 * @return The maximum amount.
	 */
	public int getMaximumLifepoints() {
		if(this.entity instanceof Player && SkillcapePerks.isActive(SkillcapePerks.DAMAGE_SPONG,this.getEntity().asPlayer())){
			lifepointsIncrease = 11;
		} else {
			lifepointsIncrease = 0;
		}
		return staticLevels[HITPOINTS] + lifepointsIncrease;
	}

	/**
	 * Sets the amount of lifepoints increase.
	 * @param amount The amount.
	 */
	public void setLifepointsIncrease(int amount) {
		this.lifepointsIncrease = amount;
	}

	/**
	 * Adds lifepoints to the entity.
	 * @param health The amount to add.
	 * @return The amount of overflow.
	 */
	public int heal(int health) {
		lifepoints += health;
		int left = 0;
		if (lifepoints > getMaximumLifepoints()) {
			left = lifepoints - getMaximumLifepoints();
			lifepoints = getMaximumLifepoints();
		}
		lifepointsUpdate = true;
		return left;
	}

	public void healNoRestrictions(int amount){
		lifepoints += amount;
		lifepointsUpdate = true;
	}

	/**
	 * @Deprecated Use
	 * {@link ImpactHandler#manualHit(Entity, int, ImpactHandler.HitsplatType)}
	 * or <br> the <b>hitsplat WILL NOT show and combat will be
	 * desynchronized!</b>
	 * @param damage The amount to remove.
	 * @return The amount of overflow.
	 */
	public int hit(int damage) {
		lifepoints -= damage;
		int left = 0;
		if (lifepoints < 0) {
			left = -lifepoints;
			lifepoints = 0;
		}
		lifepointsUpdate = true;
		return left;
	}

	/**
	 * Gets the prayer points.
	 * @return The prayer points.
	 */
	public double getPrayerPoints() {
		return prayerPoints;
	}

	/**
	 * Recharges the prayer points.
	 */
	public void rechargePrayerPoints() {
		prayerPoints = staticLevels[PRAYER];
		if (entity instanceof Player) {
			PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, PRAYER));
		}
	}

	/**
	 * Updates the current amount of prayer points (by decrementing).
	 * @param amount The amount to decrement with.
	 */
	public void decrementPrayerPoints(double amount) {
		prayerPoints -= amount;
		if (prayerPoints < 0) {
			prayerPoints = 0;
		}
		// if (prayerPoints > staticLevels[PRAYER]) {
		// prayerPoints = staticLevels[PRAYER];
		// }
		if (entity instanceof Player) {
			PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, PRAYER));
		}
	}

	/**
	 * Updates the current amount of prayer points (by incrementing)
	 * @param amount The amount to decrement with.
	 */
	public void incrementPrayerPoints(double amount) {
		prayerPoints += amount;
		if (prayerPoints < 0) {
			prayerPoints = 0;
		}
		if (prayerPoints > staticLevels[PRAYER]) {
			prayerPoints = staticLevels[PRAYER];
		}
		if (entity instanceof Player) {
			PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, PRAYER));
		}
	}

	/**
	 * Sets the current prayer points (<b>without checking for being higher than
	 * max</b>)
	 * @param amount The amount.
	 */
	public void setPrayerPoints(double amount) {
		prayerPoints = amount;
		if (entity instanceof Player) {
			PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, PRAYER));
		}
	}

	/**
	 * Updates the current skill level (by incrementing the current amount with
	 * the given amount, up to the given maximum).
	 * @param skill The skill id.
	 * @param amount The amount to increment.
	 * @param maximum The maximum amount the skill can be.
	 * @return The amount of "overflow".
	 */
	public int updateLevel(int skill, int amount, int maximum) {
		if (amount > 0 && dynamicLevels[skill] > maximum) {
			return -amount;
		}
		int left = (dynamicLevels[skill] + amount) - maximum;
		int level = dynamicLevels[skill] += amount;
		if (level < 0) {
			dynamicLevels[skill] = 0;
		} else if (amount < 0 && level < maximum) {
			dynamicLevels[skill] = maximum;
		} else if (amount > 0 && level > maximum) {
			dynamicLevels[skill] = maximum;
		}
		if (entity instanceof Player) {
			PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, skill));
		}
		return left;
	}

	/**
	 * Updates a level.
	 * @param skill the skill.
	 * @param amount the amount.
	 * @return the left.
	 */
	public int updateLevel(int skill, int amount) {
		return updateLevel(skill, amount, amount >= 0 ? getStaticLevel(skill) + amount : getStaticLevel(skill) - amount);
	}

	/**
	 * Drains a certain percentage of a level.
	 * @param skill The skill.
	 * @param drainPercentage The drain percentage (0.05 indicates 5% drain).
	 * @param maximumDrainPercentage The maximum drain percentage (0.05
	 * indicates 5%).
	 */
	public void drainLevel(int skill, double drainPercentage, double maximumDrainPercentage) {
		int drain = (int) (dynamicLevels[skill] * drainPercentage);
		int minimum = (int) (staticLevels[skill] * (1.0 - maximumDrainPercentage));
		updateLevel(skill, -drain, minimum);
	}

	/**
	 * Sets the static level.
	 * @param skill The skill id.
	 * @param level The level to set.
	 */
	public void setStaticLevel(int skill, int level) {
		experience[skill] = getExperienceByLevel(staticLevels[skill] = dynamicLevels[skill] = level);
		if (entity instanceof Player) {
			PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, skill));
		}
	}

	/**
	 * Gets the amount of mastered skills.
	 * @return The amount of mastered skills.
	 */
	public int getMasteredSkills() {
		int count = 0;
		for (int i = 0; i < 23; i++) {
			if (getStaticLevel(i) >= 99) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Method used to get the skill by the name.
	 * @param name the name.
	 * @return the skill.
	 */
	public static int getSkillByName(final String name) {
		for (int i = 0; i < SKILL_NAME.length; i++) {
			if (SKILL_NAME[i].equalsIgnoreCase(name)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the total level.
	 * @return the total level.
	 */
	public int getTotalLevel() {
		int level = 0;
		for (int i = 0; i < 24; i++) {
			level += getStaticLevel(i);
		}
		return level;
	}

	/**
	 * Gets the total exp.
	 * @return the exp.
	 */
	public int getTotalXp() {
		int total = 0;
		for (int skill = 0; skill < Skills.SKILL_NAME.length; skill++) {
			total += this.getExperience(skill);
		}
		return total;
	}

	/**
	 * Gets the lifepointsUpdate.
	 * @return The lifepointsUpdate.
	 */
	public boolean isLifepointsUpdate() {
		return lifepointsUpdate;
	}

	/**
	 * Sets the lifepointsUpdate.
	 * @param lifepointsUpdate The lifepointsUpdate to set.
	 */
	public void setLifepointsUpdate(boolean lifepointsUpdate) {
		this.lifepointsUpdate = lifepointsUpdate;
	}

	/**
	 * Gets the statis levels.
	 * @return the level.
	 */
	public int[] getStaticLevels() {
		return staticLevels;
	}

	/**
	 * Checks if the player has the required level.
	 * @param skillId the skill id.
	 * @param i the level.
	 * @return {@code True} if so.
	 */
	public boolean hasLevel(int skillId, int i) {
		return getStaticLevel(skillId) >= i;
	}

	/**
	 * Gets the combatMilestone value.
	 * @return The combatMilestone.
	 */
	public int getCombatMilestone() {
		return combatMilestone;
	}

	/**
	 * Sets the combatMilestones value.
	 * @param combatMilestone The combatMilestones to set.
	 */
	public void setCombatMilestone(int combatMilestone) {
		this.combatMilestone = combatMilestone;
	}

	/**
	 * Gets the skillMilestone value.
	 * @return The skillMilestone.
	 */
	public int getSkillMilestone() {
		return skillMilestone;
	}

	/**
	 * Sets the skillMilestone value.
	 * @param skillMilestone The skillMilestone to set.
	 */
	public void setSkillMilestone(int skillMilestone) {
		this.skillMilestone = skillMilestone;
	}

	public int[] getDynamicLevels() {
		return dynamicLevels;
	}
}
