package core.game.node.entity.player.link.prayer;

import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.system.task.NodeTask;
import core.game.world.map.zone.ZoneBorders;
import core.tools.RandomFunction;
import core.game.node.entity.skill.skillcapeperks.SkillcapePerks;

/**
 * Represents an event used to drain prayer points.
 * @author 'Vexia
 */
public final class DrainTask extends NodeTask {

	/**
	 * Constructs a new {@code DrainTask} {@code Object}.
	 */
	public DrainTask() {
		super(2);
	}

	@Override
	public boolean exec(Node node, Node... n) {
		Player player = node.asPlayer();
		if (player.getPrayer().getActive().isEmpty()) {
			getPulse().stop();
			return true;
		}
		player.getSkills().decrementPrayerPoints(getDrain(player.getPrayer()));

		if (player.getPrayer().getActive().contains(PrayerType.PIETY)
				&& new ZoneBorders(2732, 3467, 2739, 3471, 0).insideBorder(player)) {
			player.getAchievementDiaryManager().finishTask(player, DiaryType.SEERS_VILLAGE, 2, 3);
		}

		return player.getSkills().getPrayerPoints() <= 0;
	}

	@Override
	public void stop(Node node, Node... n) {
		final Player player = node.asPlayer();
		if (player.getSkills().getPrayerPoints() <= 0) {
			player.getPrayer().reset();
			player.getAudioManager().send(2672);
			player.getPacketDispatch().sendMessage("You have run out of prayer points, you must recharge at an altar.");
		}
		super.stop(node, n);
	}

	@Override
	public void start(Node node, Node... n) {
		node.asPlayer().removeAttribute("prayer-message");
		super.start(node, n);
	}

	@Override
	public boolean removeFor(String s, Node node, Node... n) {
		return true;
	}

	/**
	 * Method used to initialize this pulse.
	 * @param player the player.
	 */
	public void init(final Player player) {
		if (getPulse() == null || !getPulse().isRunning()) {
			schedule(player);
		}
	}

	/**
	 * Method used to return that drain tick.
	 * @param prayer the prayer manager.
	 * @return the drain tick, converted to an integer.
	 */
	public double getDrain(final Prayer prayer) {
		double amountDrain = 0;
		for (PrayerType type : prayer.getActive()) {
			double drain = type.getDrain();
			double bonus = (1/30f) * prayer.getPlayer().getProperties().getBonuses()[12];
			drain = drain * (1 + bonus);
			drain = 0.6 / drain;
			amountDrain += drain;
		}
		if(SkillcapePerks.isActive(SkillcapePerks.DIVINE_FAVOR,prayer.getPlayer()) && RandomFunction.random(100) <= 10){
			amountDrain = 0;
		}
		return amountDrain;
	}
}
