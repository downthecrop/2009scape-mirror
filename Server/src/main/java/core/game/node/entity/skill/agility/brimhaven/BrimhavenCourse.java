package core.game.node.entity.skill.agility.brimhaven;

import core.cache.def.impl.SceneryDefinition;
import core.plugin.Initializable;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.agility.AgilityHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.impl.ForceMovement;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import kotlin.Unit;
import rs09.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;

/**
 * The brimhaven course handling plugin.
 * @author Emperor
 */
@Initializable
public final class BrimhavenCourse extends OptionHandler {

	@Override
	public boolean handle(final Player player, Node node, String option) {
		final Scenery object = (Scenery) node;
		Direction dir = object.getDirection();
		Location start = player.getLocation();
		switch (object.getId()) {
		case 3566:
			// Rope swing
			start = object.getLocation().transform(dir.getStepX() << 1, dir.getStepY() << 1, 0);
			Location end = object.getLocation().transform(-dir.getStepX() * 3, -dir.getStepY() * 3, 0);
			if (AgilityHandler.hasFailed(player, 1, 0.1)) {
				AgilityHandler.failWalk(player, 2, start, start.transform(-dir.getStepX(), -dir.getStepY(), 0), player.getLocation().transform(0, 0, -3), Animation.create(1105), 26, getHitAmount(player), "You missed the rope!").setEndAnimation(Animation.RESET);
				return true;
			}
			AgilityHandler.forceWalk(player, -1, start, end, Animation.create(751), 25, getExp(player, 20.0), null);
			GameWorld.getPulser().submit(new Pulse(1, player) {
				boolean finish;

				@Override
				public boolean pulse() {
					if (!finish) {
						player.getPacketDispatch().sendSceneryAnimation(object, Animation.create(1052));
						finish = true;
						return false;
					}
					player.getPacketDispatch().sendSceneryAnimation(object, Animation.create(-1));
					return true;
				}
			});
			return true;
		case 3578:
			// Pillars
			handlePillarObstacle(player, object);
			return true;
		case 3565:
			// Low wall
			dir = Direction.getLogicalDirection(player.getLocation(), object.getLocation());
			start = player.getLocation();
			if (AgilityHandler.hasFailed(player, 1, 0.05)) {
				end = start.transform(dir);
				AgilityHandler.failWalk(player, 2, start, end, end, Animation.create(1106), 15, getHitAmount(player), "You lost your balance!");
				AgilityHandler.forceWalk(player, -1, end, player.getLocation(), ForceMovement.WALK_ANIMATION, 10, 0.0, null, 4);
				return true;
			}
			AgilityHandler.forceWalk(player, -1, start, start.transform(dir.getStepX() * 3, dir.getStepY() * 3, 0), Animation.create(1252), 6, getExp(player, 8.0), null);
			return true;
		case 3553:
		case 3557:
			handleLogBalance(player, object);
			return true;
		case 3570:
		case 3571:
		case 3572:
			handlePlankObstacle(player, object);
			return true;
		case 3559:
		case 3561:
			handleBalancingLedge(player, object);
			return true;
		case 3564:
			handleMonkeyBars(player, object);
			return true;
		case 3551:
			handleBalancingRope(player, object);
			return true;
		case 3583:
			handleHandHolds(player, object);
			return true;
		}
		return false;
	}

	/**
	 * Handles the hand holds obstacle.
	 * @param player The player.
	 * @param object The object.
	 */
	private static void handleHandHolds(final Player player, final Scenery object) {
		if (player.getSkills().getLevel(Skills.AGILITY) < 20) {
			player.getPacketDispatch().sendMessage("You need an agility of at least 20 to get past this obstacle!");
			return;
		}
		int mod = 0;
		Direction direction = object.getDirection();
		int x = object.getLocation().getX();
		int y = object.getLocation().getY();
		if ((x == 2785 && y == 9544) || (x == 2759 && y == 9566) || (x == 2792 && y == 9592)) {
			mod = 4;
			direction = Direction.get(direction.toInteger() + 2 & 3);
		}
		final int m = mod;
		player.lock(5);
		final Direction dir = direction;
		final Direction faceDirection = Direction.get(object.getDirection().toInteger() - 1 & 3);
		final Location start = player.getLocation();
		player.getAppearance().setAnimations(Animation.create(1118 + m));
		player.getAppearance().sync();
		AgilityHandler.climb(player, -1, new Animation(1117 + m), start.transform(dir), 0.0, null);
		player.logoutListeners.put("brimcourse", p -> {
			p.setLocation(start);
			return Unit.INSTANCE;
		});
		GameWorld.getPulser().submit(new Pulse(3, player) {
			Location last = start.transform(dir);
			int count;

			@Override
			public boolean pulse() {
				if (++count == 1) {
					if (AgilityHandler.hasFailed(player, 1, 0.15)) {
						player.getAppearance().setAnimations();
						player.getAppearance().sync();
						AgilityHandler.fail(player, 2, last.transform(0, 0, -3), Animation.create(1119 + m), getHitAmount(player), "You missed a hand hold!");
						player.logoutListeners.remove("brimcourse");
						return true;
					}
				} else if (count == 6) {
					player.getAppearance().setAnimations();
					player.getAppearance().sync();
					AgilityHandler.forceWalk(player, -1, last, last.transform(dir), Animation.create(1120 + m), 5, getExp(player, 22.0), null).setDirection(faceDirection);
					player.logoutListeners.remove("brimcourse");
					return true;
				}
				player.logoutListeners.remove("brimcourse");
				AgilityHandler.forceWalk(player, -1, last, last = last.transform(dir), Animation.create(1118 + m), 5, 0.0, null).setDirection(faceDirection);
				return false;
			}
		});
	}

	/**
	 * Handles the balancing rope obstacle.
	 * @param player The player.
	 * @param object The balancing rope object.
	 */
	private static void handleBalancingRope(final Player player, Scenery object) {
		final Direction dir = object.getDirection();
		boolean failed = AgilityHandler.hasFailed(player, 1, 0.1);
		if (failed) {
			Location end = player.getLocation().transform(dir);
			AgilityHandler.forceWalk(player, -1, player.getLocation(), end, Animation.create(762), 10, 0.0, null);
			GameWorld.getPulser().submit(new Pulse(2, player) {
				@Override
				public boolean pulse() {
					Direction d = Direction.get((dir.toInteger() + 3) % 4);
					AgilityHandler.fail(player, 2, player.getLocation().transform(d.getStepX() << 1, d.getStepY() << 1, -3), Animation.create(764), getHitAmount(player), "You lost your balance!");
					return true;
				}
			});
			return;
		}
		AgilityHandler.walk(player, -1, player.getLocation(), player.getLocation().transform(dir.getStepX() * 7, dir.getStepY() * 7, 0), Animation.create(155), getExp(player, 10.0), null);
	}

	/**
	 * Handles the monkey bars obstacle.
	 * @param player The player.
	 * @param object The object.
	 */
	private static void handleMonkeyBars(final Player player, Scenery object) {
		player.lock(5);
		final Direction dir = Direction.get((object.getDirection().toInteger() + 2) % 4);
		player.getAppearance().setAnimations(Animation.create(745));
		final Location start = player.getLocation();
		ForceMovement.run(player, start.transform(dir), start.transform(dir.getStepX() << 1, dir.getStepY() << 1, 0), Animation.create(742), Animation.create(744));
		player.logoutListeners.put("brimcourse", p -> {
			p.setLocation(start);
			return Unit.INSTANCE;
		});
		GameWorld.getPulser().submit(new Pulse(2, player) {
			boolean failed;
			int count;

			@Override
			public boolean pulse() {
				if (++count == 1) {
					if (failed = AgilityHandler.hasFailed(player, 1, 0.15)) {
						setDelay(1);
						player.animate(Animation.create(743));
						return false;
					}
					setDelay(7);
					AgilityHandler.walk(player, -1, player.getLocation().transform(dir), player.getLocation().transform(dir.getStepX() * 7, dir.getStepY() * 7, 0), Animation.create(662), 0.0, null);
				} else if (count == 2) {
					if (failed) {
						player.getAppearance().setAnimations();
						player.getAppearance().sync();
						AgilityHandler.fail(player, 2, player.getLocation().transform(0, 0, -3), Animation.create(768), getHitAmount(player), "You missed a hand hold!");
						player.logoutListeners.remove("brimcourse");
						return true;
					}
					player.logoutListeners.remove("brimcourse");
					AgilityHandler.forceWalk(player, -1, player.getLocation(), player.getLocation().transform(dir), Animation.create(743), 10, getExp(player, 14.0), null);
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * Handles the balancing ledge obstacle.
	 * @param player The player.
	 * @param object The object.
	 */
	private static void handleBalancingLedge(final Player player, Scenery object) {
		final int diff = object.getId() == 3561 ? 0 : 1;
		Location start = player.getLocation();
		final Direction dir = Direction.getLogicalDirection(start, object.getLocation());
		Location end = object.getLocation();
		double xp = 0.0;
		if (AgilityHandler.hasFailed(player, 1, 0.15)) {
			player.lock(3);
			GameWorld.getPulser().submit(new Pulse(2, player) {
				@Override
				public boolean pulse() {
					Direction d = Direction.get((dir.toInteger() + 1) % 4);
					AgilityHandler.fail(player, 1, player.getLocation().transform(d.getStepX(), d.getStepY(), -3), Animation.create(761 - diff), getHitAmount(player), "You lost your balance!");
					return true;
				}
			});
		} else {
			xp = 16.0;
			end = object.getLocation().transform(dir.getStepX() * 6, dir.getStepY() * 6, 0);
		}
		AgilityHandler.walk(player, -1, player.getLocation(), end, Animation.create(157 - diff), getExp(player, xp), null);
	}

	/**
	 * Handles the plank obstacle.
	 * @param player The player.
	 * @param object The object.
	 */
	private static void handlePlankObstacle(final Player player, Scenery object) {
		final Direction dir = Direction.getLogicalDirection(player.getLocation(), object.getLocation());
		final Location start = player.getLocation();
		Location end = start.transform(dir.getStepX() * 7, dir.getStepY() * 7, 0);
		player.faceLocation(end);
		if (object.getCharge() == 500) { // Plank is broken
			player.lock(7);
			AgilityHandler.walk(player, -1, start, start.transform(dir.getStepX() * 2, dir.getStepY() * 2, 0), new Animation(1426), 0.0, null);
			GameWorld.getPulser().submit(new Pulse(3) {
				boolean finish;

				@Override
				public boolean pulse() {
					if (!finish) {
						setDelay(2);
						AgilityHandler.fail(player, 1, player.getLocation().transform(0, 0, -3), Animation.create(189), getHitAmount(player), "You stepped on a broken piece of plank!");
						finish = true;
						return false;
					}
					AgilityHandler.walk(player, -1, player.getLocation(), start.transform(dir), null, 0.0, null);
					return true;
				}
			});
			return;
		}
		AgilityHandler.walk(player, -1, start, end, ForceMovement.WALK_ANIMATION, getExp(player, 6.0), null);
	}

	/**
	 * Handles the pillars obstacle.
	 * @param player The player.
	 * @param object The object.
	 */
	private static void handlePillarObstacle(final Player player, Scenery object) {
		final Direction dir = Direction.getLogicalDirection(player.getLocation(), object.getLocation());
		AgilityHandler.forceWalk(player, -1, player.getLocation(), object.getLocation(), Animation.create(741), 10, 0, null);
		final Location start = player.getLocation();
		player.logoutListeners.put("brimcourse", p -> {
			p.setLocation(start);
			return Unit.INSTANCE;
		});
		player.lock(12);
		GameWorld.getPulser().submit(new Pulse(2, player) {
			int count = 0;

			@Override
			public boolean pulse() {
				if (count == 0 && AgilityHandler.hasFailed(player, 1, 0.15)) {
					Direction d = Direction.get((dir.toInteger() + 3) % 4);
					player.unlock();
					player.lock(2);
					AgilityHandler.fail(player, 2, player.getLocation().transform(d.getStepX() << 1, d.getStepY() << 1, -3), Animation.create(764), getHitAmount(player), "You lost your balance!");
					player.logoutListeners.remove("brimcourse");
					return true;
				}
				AgilityHandler.forceWalk(player, -1, player.getLocation(), player.getLocation().transform(dir), Animation.create(741), 10, count == 5 ? getExp(player, 18.0) : 0, null);
				player.logoutListeners.remove("brimcourse");
				return ++count == 6;
			}
		});
	}

	/**
	 * Handles the log balance obstacle.
	 * @param player The player.
	 * @param object The log balance object.
	 */
	private static void handleLogBalance(final Player player, Scenery object) {
		final Direction dir = Direction.getLogicalDirection(player.getLocation(), object.getLocation());
		boolean failed = AgilityHandler.hasFailed(player, 1, 0.1);
		if (failed) {
			Location end = player.getLocation().transform(dir);
			AgilityHandler.forceWalk(player, -1, player.getLocation(), end, Animation.create(762), 10, 0.0, null);
			GameWorld.getPulser().submit(new Pulse(2, player) {
				@Override
				public boolean pulse() {
					Direction d = Direction.get((dir.toInteger() + 3) % 4);
					AgilityHandler.fail(player, 2, player.getLocation().transform(d.getStepX() << 1, d.getStepY() << 1, -3), Animation.create(764), getHitAmount(player), "You lost your balance!");
					return true;
				}
			});
			return;
		}
		AgilityHandler.walk(player, -1, player.getLocation(), player.getLocation().transform(dir.getStepX() * 7, dir.getStepY() * 7, 0), Animation.create(155), getExp(player, 12.0), null);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(3566).getHandlers().put("option:swing-on", this);
		SceneryDefinition.forId(3578).getHandlers().put("option:jump-on", this);
		SceneryDefinition.forId(3565).getHandlers().put("option:climb-over", this);
		SceneryDefinition.forId(3553).getHandlers().put("option:walk-on", this);
		SceneryDefinition.forId(3557).getHandlers().put("option:walk-on", this);
		SceneryDefinition.forId(3570).getHandlers().put("option:walk-on", this);
		SceneryDefinition.forId(3571).getHandlers().put("option:walk-on", this);
		SceneryDefinition.forId(3572).getHandlers().put("option:walk-on", this);
		SceneryDefinition.forId(3559).getHandlers().put("option:walk-across", this);
		SceneryDefinition.forId(3561).getHandlers().put("option:walk-across", this);
		SceneryDefinition.forId(3564).getHandlers().put("option:swing-across", this);
		SceneryDefinition.forId(3551).getHandlers().put("option:walk-on", this);
		SceneryDefinition.forId(3583).getHandlers().put("option:climb-across", this);
		return this;
	}

	@Override
	public Location getDestination(Node node, Node n) {
		Scenery object = (Scenery) n;
		Direction dir = object.getDirection();
		switch (object.getId()) {
		case 3566:
			return n.getLocation().transform(dir.getStepX() << 1, dir.getStepY() << 1, 0);
		case 3564:
			if (n.getLocation().getX() == 2771 && n.getLocation().getY() == 9570) {
				return Location.create(2772, 9569, 3);
			}
			if (n.getLocation().getX() == 2771 && n.getLocation().getY() == 9577) {
				return Location.create(2772, 9578, 3);
			}
			if (n.getLocation().getX() == 2793 && n.getLocation().getY() == 9566) {
				return Location.create(2794, 9567, 3);
			}
			if (n.getLocation().getX() == 2793 && n.getLocation().getY() == 9559) {
				return Location.create(2794, 9558, 3);
			}
			if (n.getLocation().getX() == 2781 && n.getLocation().getY() == 9545) {
				return Location.create(2782, 9546, 3);
			}
			if (n.getLocation().getX() == 2774 && n.getLocation().getY() == 9545) {
				return Location.create(2773, 9546, 3);
			}
			break;
		case 3551:
			return n.getLocation();
		}
		return null;
	}

	/**
	 * Gets the exp.
	 * @param player the player.
	 * @param exp the exp.
	 * @return the exp.
	 */
	private static double getExp(Player player, double exp) {
		return player.getAchievementDiaryManager().getKaramjaGlove() > 1 ? exp + (exp * 0.10) : exp;
	}

	/**
	 * Gets the amount of damage to deal.
	 * @param player The player.
	 * @return The amount of damage.
	 */
	private static int getHitAmount(Player player) {
		int hit = player.getSkills().getLifepoints() / 12;
		if (hit < 2) {
			hit = 2;
		}
		return hit;
	}
}
