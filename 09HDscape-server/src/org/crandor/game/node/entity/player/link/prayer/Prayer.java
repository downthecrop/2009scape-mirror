package org.crandor.game.node.entity.player.link.prayer;

import org.crandor.game.content.skill.SkillBonus;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.CombatStyle;
import org.crandor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.crandor.game.node.entity.impl.Projectile;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.tools.RandomFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a managing class of a players prayers.
 * @author Vexia
 * @author Emperor
 */
public final class Prayer {

	/**
	 * Represents the list of active prayers.
	 */
	private final List<PrayerType> active = new ArrayList<>();

	/**
	 * Represents the current draining task.
	 */
	private final DrainTask task = new DrainTask();

	/**
	 * Represents the player instance.
	 */
	private final Player player;

	/**
	 * Constructs a new {@code Prayer} {@code Object}.
	 */
	public Prayer(Player player) {
		this.player = player;
	}

	/**
	 * Method used to toggle a prayer.
	 * @param type the type of prayer.
	 */
	public final boolean toggle(final PrayerType type) {
		if (!permitted(type)) {
			return false;
		}
		return type.toggle(player, !active.contains(type));
	}

	/**
	 * Method used to reset this prayer managers cached prayers.
	 */
	public void reset() {
		for (PrayerType type : getActive()) {
			player.getConfigManager().set(type.getConfig(), 0);
		}
		getActive().clear();
		player.getAppearance().setHeadIcon(-1);
		player.getAppearance().sync();
	}

	/**
	 * Starts the redemption effect.
	 */
	public void startRedemption() {
		player.getAudioManager().send(2681);
		player.graphics(Graphics.create(436));
		player.getSkills().heal((int) (player.getSkills().getStaticLevel(Skills.PRAYER) * 0.25));
		player.getSkills().setPrayerPoints(0.0);
		reset();
	}

	/**
	 * Starts the retribution effect.
	 * @param killer The entity who killed this player.
	 */
	public void startRetribution(Entity killer) {
		Location l = player.getLocation();
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				if (x != 0 || y != 0) {
					Projectile.create(l, l.transform(x, y, 0), 438, 0, 0, 10, 20, 0, 11).send();
				}
			}
		}
		player.getAudioManager().send(159);
		player.graphics(Graphics.create(437));
		int maximum = (int) (player.getSkills().getStaticLevel(Skills.PRAYER) * 0.25) - 1;
		if (killer != player && killer.getLocation().withinDistance(player.getLocation(), 1)) {
			killer.getImpactHandler().manualHit(player, 1 + RandomFunction.randomize(maximum), HitsplatType.NORMAL);
		}
		if (player.getProperties().isMultiZone()) {
			@SuppressWarnings("rawtypes")
			List targets = null;
			if (killer instanceof NPC) {
				targets = RegionManager.getSurroundingNPCs(player, player, killer);
			} else {
				targets = RegionManager.getSurroundingPlayers(player, player, killer);
			}
			for (Object o : targets) {
				Entity entity = (Entity) o;
				if (entity.isAttackable(player, CombatStyle.MAGIC)) {
					entity.getImpactHandler().manualHit(player, 1 + RandomFunction.randomize(maximum), HitsplatType.NORMAL);
				}
			}
		}
	}

	/**
	 * Gets the skill bonus for the given skill id.
	 * @param skillId The skill id.
	 * @return The bonus for the given skill.
	 */
	public double getSkillBonus(int skillId) {
		double bonus = 0.0;
		for (PrayerType type : active) {
			for (SkillBonus b : type.getBonuses()) {
				if (b.getSkillId() == skillId) {
					bonus += b.getBonus();
				}
			}
		}
		return bonus;
	}

	/**
	 * Method used to check if we're permitted to toggle this prayer.
	 * @param type the type.
	 * @return <code>True</code> if permitted to be toggled.
	 */
	private boolean permitted(final PrayerType type) {
		return player.getSkills().getPrayerPoints() > 0 && type.permitted(player);
	}

	/**
	 * Method used to return value of {@code True} if the {@link #active}
	 * prayers contains the prayer type.
	 * @param type the type of prayer.
	 * @return {@code True} if so.
	 */
	public boolean get(PrayerType type) {
		return active.contains(type);
	}

	/**
	 * Gets the player.
	 * @return The player.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the task.
	 * @return The task.
	 */
	public DrainTask getTask() {
		return task;
	}

	/**
	 * Gets the active prayers.
	 * @return The active.
	 */
	public List<PrayerType> getActive() {
		return active;
	}

}