package core.game.node.entity.player.link.prayer;

import core.game.node.entity.skill.SkillBonus;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Graphics;
import core.tools.RandomFunction;
import rs09.game.node.entity.skill.skillcapeperks.SkillcapePerks;
import rs09.game.world.GameWorld;

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
	private final List<PrayerType> active = new ArrayList<>(20);

	/**
	 * Represents the player instance.
	 */
	private final Player player;

	private int prayerActiveTicks = 0;

	/**
	 * Constructs a new {@code Prayer} {@code Object}.
	 */
	public Prayer(Player player) {
		this.player = player;

        // 1050 is checked client-side for making piety/chivalry disallowed sfx, likely due to the minigame requirement.
        // Set it here unconditionally until the minigame is implemented.
        player.varpManager.get(1050).setVarbit(1, 8);
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
        // Clear the overhead prayer icon a tick later
        GameWorld.getPulser().submit(new Pulse(1) {
            @Override
            public boolean pulse() {
                player.getAppearance().setHeadIcon(-1);
                player.getAppearance().sync();
                return true;
            }
        });
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
				if (entity.isAttackable(player, CombatStyle.MAGIC, false)) {
					entity.getImpactHandler().manualHit(player, 1 + RandomFunction.randomize(maximum), HitsplatType.NORMAL);
				}
			}
		}
	}

	public void tick() {
		if(!getActive().isEmpty()) prayerActiveTicks ++;
		else prayerActiveTicks = 0;

		if(prayerActiveTicks > 0 && prayerActiveTicks % 2 == 0){
			if(getPlayer().getSkills().getPrayerPoints() == 0){
                getPlayer().getAudioManager().send(2672);
				reset();
				return;
			}
			double amountDrain = 0;
			for(PrayerType type : getActive()){
				double drain = type.getDrain();
				double bonus = (1/30f) * getPlayer().getProperties().getBonuses()[12];
				drain = drain * (1 + bonus);
				drain = 0.6 / drain;
				amountDrain += drain;
			}
			if(SkillcapePerks.isActive(SkillcapePerks.DIVINE_FAVOR, getPlayer()) && RandomFunction.random(100) <= 10){
				amountDrain = 0;
			}

			getPlayer().getSkills().decrementPrayerPoints(amountDrain);
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
	 * Gets the active prayers.
	 * @return The active.
	 */
	public List<PrayerType> getActive() {
		return active;
	}

}
