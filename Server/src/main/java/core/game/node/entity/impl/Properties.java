package core.game.node.entity.impl;

import core.game.container.Container;
import core.game.container.impl.EquipmentContainer;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.CombatSpell;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.equipment.ArmourSet;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import rs09.game.node.entity.combat.CombatPulse;
import rs09.game.system.config.ItemConfigParser;
import rs09.game.system.config.NPCConfigParser;

/**
 * Holds an entity's properties.
 * @author Emperor
 */
public final class Properties {

	/**
	 * The entity.
	 */
	private final Entity entity;

	/**
	 * The entity's combat pulse.
	 */
	private final CombatPulse combatPulse;

	/**
	 * If the entity is retaliating.
	 */
	private boolean retaliating = true;

	/**
	 * If the entity is teleporting.
	 */
	private boolean teleporting;

	/**
	 * The entity's combat level.
	 */
	private int combatLevel;

	/**
	 * The entity's current attack style.
	 */
	private WeaponInterface.AttackStyle attackStyle;

	/**
	 * The current teleport destination location.
	 */
	private Location teleportLocation;

	/**
	 * The spawn location.
	 */
	private Location spawnLocation;

	/**
	 * The bonuses.
	 */
	private int[] bonuses = new int[15];

	/**
	 * The current attack speed.
	 */
	private int attackSpeed = 4;

	/**
	 * The last animation end.
	 */
	private long lastAnimationEnd;

	/**
	 * The attack animation.
	 */
	private Animation attackAnimation = new Animation(422, Animator.Priority.HIGH);

	/**
	 * The defence animation.
	 */
	private Animation defenceAnimation = new Animation(404);

	/**
	 * The death animation.
	 */
	private Animation deathAnimation = new Animation(9055, Animator.Priority.HIGH);

	public Graphics deathGfx = new Graphics(-1);

	/**
	 * The range animation.
	 */
	private Animation rangeAnimation;

	/**
	 * The magic animation.
	 */
	private Animation magicAnimation;

	/**
	 * The current magic spell.
	 */
	private CombatSpell spell;

	/**
	 * The autocasted magic spell.
	 */
	private CombatSpell autocastSpell;

	/**
	 * The current armour set used.
	 */
	private ArmourSet armourSet;

	/**
	 * If the entity is standing in multizone.
	 */
	private boolean multiZone;

	/**
	 * If the entity is in a safe zone.
	 */
	private boolean safeZone;

	/**
	 * The location which the player respawns at the event of death while safeZone = true
	 */
	public Location safeRespawn;

	/**
	 * The combat time out ticks.
	 */
	private int combatTimeOut = 10;

	/**
	 * If the entity can walk over other NPCs in multiway combat zone.
	 */
	private boolean npcWalkable;
	
	/**
	 * The style the npc protects against.
	 */
	private CombatStyle protectStyle;

	/**
	 * Constructs a new {@code Properties} {@code Object}.
	 * @param entity
	 */
	public Properties(Entity entity) {
		this.entity = entity;
		this.combatPulse = new CombatPulse(entity);
	}

	/**
	 * Updates the defence animation.
	 */
	public void updateDefenceAnimation() {
		if (entity instanceof NPC) {
			Animation animation = ((NPC) entity).getDefinition().getConfiguration(NPCConfigParser.DEFENCE_ANIMATION);
			if (animation != null) {
				defenceAnimation = animation;
			}
			return;
		}
		Container c = ((Player) entity).getEquipment();
		Item item = c.get(EquipmentContainer.SLOT_SHIELD);
		if (item != null) {
			defenceAnimation = item.getDefinition().getConfiguration(ItemConfigParser.DEFENCE_ANIMATION, Animation.create(1156));
		} else if ((item = c.get(EquipmentContainer.SLOT_WEAPON)) != null) {
			defenceAnimation = item.getDefinition().getConfiguration(ItemConfigParser.DEFENCE_ANIMATION, Animation.create(424));
		} else {
			defenceAnimation = Animation.create(397);
		}
	}
	
	/**
	 * Gets a combat animation by the index.
	 * @param index the index.
	 * @return the animation.
	 */
	public Animation getCombatAnimation(int index) {
		return index == 0 ? attackAnimation : index == 1 ? magicAnimation : index == 2 ? rangeAnimation : index == 3 ? defenceAnimation : deathAnimation;
	}

	/**
	 * Gets the entity.
	 * @return The entity.
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * Gets the teleporting.
	 * @return The teleporting.
	 */
	public boolean isTeleporting() {
		return teleporting;
	}

	/**
	 * Sets the teleporting.
	 * @param teleporting The teleporting to set.
	 */
	public void setTeleporting(boolean teleporting) {
		this.teleporting = teleporting;
	}

	/**
	 * Gets the combatLevel. <br> Deprecated Due to summoning-based combat level
	 * not being calculated in.
	 * @return The combatLevel.
	 */
	@Deprecated
	public int getCombatLevel() {
		return combatLevel;
	}

	/**
	 * Gets the current combat level.
	 * @return The combat level.
	 */
	public int getCurrentCombatLevel() {
		if (entity instanceof Player) {
			Player player = (Player) entity;
			if (player.getFamiliarManager().isUsingSummoning() || !player.getSkullManager().isWilderness()) {
				return player.getFamiliarManager().getSummoningCombatLevel() + combatLevel;
			}
		}
		return combatLevel;
	}

	/**
	 * Sets the combatLevel.
	 * @param combatLevel The combatLevel to set.
	 */
	public void setCombatLevel(int combatLevel) {
		this.combatLevel = combatLevel;
	}

	/**
	 * Gets the attackStyle.
	 * @return The attackStyle.
	 */
	public WeaponInterface.AttackStyle getAttackStyle() {
		return attackStyle;
	}

	/**
	 * Sets the attackStyle.
	 * @param attackStyle The attackStyle to set.
	 */
	public void setAttackStyle(WeaponInterface.AttackStyle attackStyle) {
		this.attackStyle = attackStyle;
	}

	/**
	 * Gets the teleportLocation.
	 * @return The teleportLocation.
	 */
	public Location getTeleportLocation() {
		return teleportLocation;
	}

	/**
	 * Sets the teleportLocation.
	 * @param teleportLocation The teleportLocation to set.
	 */
	public void setTeleportLocation(Location teleportLocation) {
		this.teleportLocation = teleportLocation;
	}

	/**
	 * Gets the spawnLocation.
	 * @return The spawnLocation.
	 */
	public Location getSpawnLocation() {
		return spawnLocation;
	}

	/**
	 * Sets the spawnLocation.
	 * @param spawnLocation The spawnLocation to set.
	 */
	public void setSpawnLocation(Location spawnLocation) {
		this.spawnLocation = spawnLocation;
	}

	/**
	 * Gets the bonuses.
	 * @return The bonuses.
	 */
	public int[] getBonuses() {
		return bonuses;
	}

	/**
	 * Sets the bonuses.
	 * @param bonuses The bonuses to set.
	 */
	public void setBonuses(int[] bonuses) {
		this.bonuses = bonuses;
	}

	/**
	 * Gets the last animation end.
	 * @return The last animation end.
	 */
	public long getLastAnimationEnd() {
		return lastAnimationEnd;
	}

	/**
	 * Gets the combatPulse.
	 * @return The combatPulse.
	 */
	public CombatPulse getCombatPulse() {
		return combatPulse;
	}

	/**
	 * Gets the retaliating.
	 * @return The retaliating.
	 */
	public boolean isRetaliating() {
		return retaliating;
	}

	/**
	 * Sets the retaliating.
	 * @param retaliating The retaliating to set.
	 */
	public void setRetaliating(boolean retaliating) {
		this.retaliating = retaliating;
	}

	/**
	 * Gets the attackSpeed.
	 * @return The attackSpeed.
	 */
	public int getAttackSpeed() {
		return attackSpeed;
	}

	/**
	 * Sets the attackSpeed.
	 * @param attackSpeed The attackSpeed to set.
	 */
	public void setAttackSpeed(int attackSpeed) {
		this.attackSpeed = attackSpeed;
	}

	/**
	 * Gets the attackAnimation.
	 * @return The attackAnimation.
	 */
	public Animation getAttackAnimation() {
		return attackAnimation;
	}

	/**
	 * Sets the attackAnimation.
	 * @param attackAnimation The attackAnimation to set.
	 */
	public void setAttackAnimation(Animation attackAnimation) {
		this.attackAnimation = attackAnimation;
	}

	/**
	 * Gets the defenceAnimation.
	 * @return The defenceAnimation.
	 */
	public Animation getDefenceAnimation() {
		return defenceAnimation;
	}

	/**
	 * Sets the defenceAnimation.
	 * @param defenceAnimation The defenceAnimation to set.
	 */
	public void setDefenceAnimation(Animation defenceAnimation) {
		this.defenceAnimation = defenceAnimation;
	}

	/**
	 * Sets the spell.
	 * @param spell The spell to set.
	 */
	public void setSpell(CombatSpell spell) {
		this.spell = spell;
		if (spell != null) {
			combatPulse.updateStyle();
		}
	}

	/**
	 * Gets the spell.
	 * @return The spell.
	 */
	public CombatSpell getSpell() {
		return spell;
	}

	/**
	 * @return the autocastSpell.
	 */
	public CombatSpell getAutocastSpell() {
		return autocastSpell;
	}

	/**
	 * @param autocastSpell the autocastSpell to set.
	 */
	public void setAutocastSpell(CombatSpell autocastSpell) {
		this.autocastSpell = autocastSpell;
	}

	/**
	 * @return the armourSet.
	 */
	public ArmourSet getArmourSet() {
		return armourSet;
	}

	/**
	 * @param armourSet the armourSet to set.
	 */
	public void setArmourSet(ArmourSet armourSet) {
		this.armourSet = armourSet;
	}

	/**
	 * Gets the deathAnimation.
	 * @return The deathAnimation.
	 */
	public Animation getDeathAnimation() {
		return deathAnimation;
	}

	/**
	 * Sets the deathAnimation.
	 * @param deathAnimation The deathAnimation to set.
	 */
	public void setDeathAnimation(Animation deathAnimation) {
		this.deathAnimation = deathAnimation;
	}

	/**
	 * Gets the multiZone.
	 * @return The multiZone.
	 */
	public boolean isMultiZone() {
		return multiZone;
	}

	/**
	 * Sets the multiZone.
	 * @param multiZone The multiZone to set.
	 */
	public void setMultiZone(boolean multiZone) {
		this.multiZone = multiZone;
	}

	/**
	 * Gets the safeZone.
	 * @return The safeZone.
	 */
	public boolean isSafeZone() {
		return safeZone;
	}

	/**
	 * Sets the safeZone.
	 * @param safeZone The safeZone to set.
	 */
	public void setSafeZone(boolean safeZone) {
		this.safeZone = safeZone;
	}

	/**
	 * Gets the combatTimeOut.
	 * @return The combatTimeOut.
	 */
	public int getCombatTimeOut() {
		return combatTimeOut;
	}

	/**
	 * Sets the combatTimeOut.
	 * @param combatTimeOut The combatTimeOut to set.
	 */
	public void setCombatTimeOut(int combatTimeOut) {
		this.combatTimeOut = combatTimeOut;
	}

	/**
	 * Gets the npcWalkable.
	 * @return The npcWalkable.
	 */
	public boolean isNPCWalkable() {
		return npcWalkable;
	}

	/**
	 * Sets the npcWalkable.
	 * @param npcWalkable The npcWalkable to set.
	 */
	public void setNPCWalkable(boolean npcWalkable) {
		this.npcWalkable = npcWalkable;
	}

	/**
	 * Gets the rangeAnimation.
	 * @return the rangeAnimation
	 */
	public Animation getRangeAnimation() {
		return rangeAnimation;
	}

	/**
	 * Sets the rangeAnimation.
	 * @param rangeAnimation the rangeAnimation to set.
	 */
	public void setRangeAnimation(Animation rangeAnimation) {
		this.rangeAnimation = rangeAnimation;
	}

	/**
	 * Gets the magicAnimation.
	 * @return the magicAnimation
	 */
	public Animation getMagicAnimation() {
		return magicAnimation;
	}

	/**
	 * Sets the magicAnimation.
	 * @param magicAnimation the magicAnimation to set.
	 */
	public void setMagicAnimation(Animation magicAnimation) {
		this.magicAnimation = magicAnimation;
	}

	/**
	 * Gets the protectStyle.
	 * @return the protectStyle.
	 */
	public CombatStyle getProtectStyle() {
		return protectStyle;
	}

	/**
	 * Sets the protectStyle.
	 * @param protectStyle the protectStyle to set
	 */
	public void setProtectStyle(CombatStyle protectStyle) {
		this.protectStyle = protectStyle;
	}

}