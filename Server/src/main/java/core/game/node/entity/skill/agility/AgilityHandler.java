package core.game.node.entity.skill.agility;

import static api.ContentAPIKt.*;
import core.game.node.entity.player.link.TeleportManager;
import core.game.node.entity.skill.Skills;
import core.game.interaction.MovementPulse;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.impl.ForceMovement;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import kotlin.Unit;
import rs09.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;

/**
 * Holds agility-related utility methods.
 * @author Emperor
 */
public final class AgilityHandler {

	/**
	 * Checks if the player has failed crossing an obstacle.
	 * @param player The player.
	 * @param level The level requirement for the obstacle.
	 * @param failChance The chance of failing (0.0 - 0.99).
	 * @return {@code True} if the player has failed.
	 */
	public static boolean hasFailed(Player player, int level, double failChance) {
		int levelDiff = player.getSkills().getLevel(Skills.AGILITY) - level;
		if (levelDiff > 69) {
			return false;
		}
		double chance = (1 + levelDiff) * 0.01;
		chance *= Math.random();
		return chance <= (Math.random() * failChance);
	}

	/**
	 * Handles failing an obstacle (by using force movement).
	 * @param player The player.
	 * @param delay The amount of ticks before teleporting to the destination
	 * and hitting.
	 * @param start The start location.
	 * @param end The end location.
	 * @param dest The destination.
	 * @param anim The animation.
	 * @param hit The amount of damage to hit.
	 * @param message The message to send.
	 */
	public static ForceMovement failWalk(final Player player, int delay, Location start, Location end, final Location dest, Animation anim, int speed, final int hit, final String message, Direction direction) {
		ForceMovement movement = new ForceMovement(player, start, end, anim, speed) {
			@Override
			public void stop() {
				super.stop();
				player.getProperties().setTeleportLocation(dest);
				if (hit > 0) {
					player.getImpactHandler().setDisabledTicks(0);
					player.getImpactHandler().manualHit(player, hit, HitsplatType.NORMAL);
				}
				if (message != null) {
					player.getPacketDispatch().sendMessage(message);
				}
			}
		};
		if (direction != null) {
			movement.setDirection(direction);
		}
		movement.start();
		movement.setDelay(delay);
		GameWorld.getPulser().submit(movement);
		return movement;
	}

	/**
	 * Handles failing an obstacle (by using force movement).
	 * @param player The player.
	 * @param delay The amount of ticks before teleporting to the destination
	 * and hitting.
	 * @param start The start location.
	 * @param end The end location.
	 * @param dest The destination.
	 * @param anim The animation.
	 * @param hit The amount of damage to hit.
	 * @param message The message to send.
	 */
	public static ForceMovement failWalk(final Player player, int delay, Location start, Location end, final Location dest, Animation anim, int speed, final int hit, final String message) {
		return failWalk(player, delay, start, end, dest, anim, speed, hit, message, null);
	}

	/**
	 * Handles failing an obstacle.
	 * @param player The player.
	 * @param delay The amount of ticks before teleporting to the destination
	 * and hitting.
	 * @param dest The destination.
	 * @param anim The animation.
	 * @param hit The amount of damage to hit.
	 * @param message The message to send.
	 */
	public static void fail(final Player player, int delay, final Location dest, Animation anim, final int hit, final String message) {
		if (anim != null) {
			animate(player, anim, true);
			submitWorldPulse(new Pulse(animationDuration(anim), player) {
				boolean dmg = false;

				@Override
				public boolean pulse() {
					teleport(player, dest, TeleportManager.TeleportType.INSTANT);
					animate(player, Animation.RESET, true);
					if (!dmg) {
						if (hit > 0) {
							player.getImpactHandler().setDisabledTicks(0);
							impact(player, hit, HitsplatType.NORMAL);
						}
						if (message != null) {
							sendMessage(player, message);
						}
						dmg = true;
					}
					setDelay(0);
					return player.getLocation().equals(dest);
				}
			});
		}
	}

	/**
	 * Walks across an obstacle using the force movement update mask.
	 * @param start The start location.
	 * @param end The end location.
	 * @param animation The animation.
	 * @param speed The force movement speed.
	 * @param experience The amount of agility experience to give as reward.
	 * @param message The message to send upon completion.
	 * @return The force movement instance, if force movement is used.
	 */
	public static ForceMovement forceWalk(final Player player, final int courseIndex, Location start, Location end, Animation animation, int speed, final double experience, final String message) {
		player.logoutListeners.put("forcewalk", p -> {
			p.setLocation(player.getLocation().transform(0,0,0));
			return Unit.INSTANCE;
		});
		ForceMovement movement = new ForceMovement(player, start, end, animation, speed) {
			@Override
			public void stop() {
				super.stop();
				if (message != null) {
					player.getPacketDispatch().sendMessage(message);
				}
				if (experience > 0.0) {
					player.getSkills().addExperience(Skills.AGILITY, experience, true);
				}
				setObstacleFlag(player, courseIndex);
				player.logoutListeners.remove("forcewalk");
			}
		};
		movement.start();
		GameWorld.getPulser().submit(movement);
		return movement;
	}

	/**
	 * Walks across an obstacle using the force movement update mask.
	 * @param start The start location.
	 * @param end The end location.
	 * @param animation The animation.
	 * @param speed The force movement speed.
	 * @param experience The amount of agility experience to give as reward.
	 * @param message The message to send upon completion.
	 * @return The force movement instance, if force movement is used.
	 */
	public static ForceMovement forceWalk(final Player player, final int courseIndex, Location start, Location end, Animation animation, int speed, final double experience, final String message, int delay) {
		player.logoutListeners.put("forcewalk", p -> {
			p.setLocation(player.getLocation().transform(0,0,0));
			return Unit.INSTANCE;
		});
		if (delay < 1) {
			return forceWalk(player, courseIndex, start, end, animation, speed, experience, message);
		}
		final ForceMovement movement = new ForceMovement(player, start, end, animation, speed) {
			@Override
			public void stop() {
				super.stop();
				if (message != null) {
					player.getPacketDispatch().sendMessage(message);
				}
				if (experience > 0.0) {
					player.getSkills().addExperience(Skills.AGILITY, experience, true);
				}
				setObstacleFlag(player, courseIndex);
				player.logoutListeners.remove("forcewalk");
			}
		};
		GameWorld.getPulser().submit(new Pulse(delay, player) {
			@Override
			public boolean pulse() {
				movement.start();
				GameWorld.getPulser().submit(movement);
				return true;
			}
		});
		return movement;
	}

	/**
	 * Executes the climbing reward.
	 * @param player The player.
	 * @param animation The climbing animation.
	 * @param destination The destination.
	 * @param experience The amount of agility experience to give as reward.
	 * @param message The message to send upon completion.
	 */
	public static void climb(final Player player, final int courseIndex, Animation animation, final Location destination, final double experience, final String message) {
		climb(player, courseIndex, animation, destination, experience, message, 2);
	}

	/**
	 * Executes the climbing reward.
	 * @param player The player.
	 * @param animation The climbing animation.
	 * @param destination The destination.
	 * @param experience The amount of agility experience to give as reward.
	 * @param message The message to send upon completion.
	 */
	public static void climb(final Player player, final int courseIndex, Animation animation, final Location destination, final double experience, final String message, int delay) {
		player.lock(delay + 1);
		player.animate(animation);
		GameWorld.getPulser().submit(new Pulse(delay) {
			@Override
			public boolean pulse() {
				if (message != null) {
					player.getPacketDispatch().sendMessage(message);
				}
				if (experience > 0.0) {
					player.getSkills().addExperience(Skills.AGILITY, experience, true);
				}
				player.getProperties().setTeleportLocation(destination);
				setObstacleFlag(player, courseIndex);
				return true;
			}
		});
	}

	public static void walk(final Player player, final int courseIndex, final Location start, final Location end, final Animation animation, final double experience, final String message){
		walk(player,courseIndex,start,end,animation,experience,message,false);
	}

	/**
	 * Uses the walking queue to walk across an obstacle.
	 * @param player The player.
	 * @param courseIndex The obstacle index for the course, {@code -1} if no
	 * course.
	 * @param start The start location.
	 * @param end The end location.
	 * @param animation The animation.
	 * @param experience The agility experience.
	 * @param message The message to send upon completion.
	 */
	public static void walk(final Player player, final int courseIndex, final Location start, final Location end, final Animation animation, final double experience, final String message, final boolean infiniteRun) {
		if (!player.getLocation().equals(start)) {
			player.getPulseManager().run(new MovementPulse(player, start) {
				@Override
				public boolean pulse() {
					walk(player, courseIndex, start, end, animation, experience, message, infiniteRun);
					return true;
				}
			}, "movement");
			return;
		}
		player.getWalkingQueue().reset();
		player.getWalkingQueue().addPath(end.getX(), end.getY(), !infiniteRun);
		int ticks = player.getWalkingQueue().getQueue().size();
		player.getImpactHandler().setDisabledTicks(ticks);
		player.lock(1 + ticks);
		player.logoutListeners.put("agility", p -> {
			p.setLocation(start);
			return Unit.INSTANCE;
		});
		if (animation != null) {
			player.getAppearance().setAnimations(animation);
		}
		player.getSettings().setRunEnergy(100.0);
		GameWorld.getPulser().submit(new Pulse(ticks, player) {
			@Override
			public boolean pulse() {
				if (animation != null) {
					player.getAppearance().setAnimations();
					player.getAppearance().sync();
				}
				if (message != null) {
					player.getPacketDispatch().sendMessage(message);
				}
				if (experience > 0.0) {
					player.getSkills().addExperience(Skills.AGILITY, experience, true);
				}
				setObstacleFlag(player, courseIndex);
				player.logoutListeners.remove("agility");
				return true;
			}
		});
	}

	/**
	 * Sets the obstacle flag for the agility course.
	 * @param player The player.
	 * @param courseIndex The course index.
	 */
	public static void setObstacleFlag(Player player, int courseIndex) {
		if (courseIndex < 0) {
			return;
		}
		AgilityCourse course = player.getExtension(AgilityCourse.class);
		if (course != null && courseIndex < course.getPassedObstacles().length) {
			course.flag(courseIndex);
		}
	}

}