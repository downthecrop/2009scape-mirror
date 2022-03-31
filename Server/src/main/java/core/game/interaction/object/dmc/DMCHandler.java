package core.game.interaction.object.dmc;

import api.LogoutListener;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.audio.Audio;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.map.zone.ZoneRestriction;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import org.jetbrains.annotations.NotNull;
import rs09.game.node.entity.combat.CombatSwingHandler;
import rs09.game.world.GameWorld;

/**
 * Handles a player's Dwarf Multi-cannon.
 * @author Emperor
 */
public final class DMCHandler implements LogoutListener {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The cannon object.
	 */
	private Scenery cannon;

	/**
	 * The amount of cannonballs loaded.
	 */
	private int cannonballs;

	/**
	 * The firing pulse.
	 */
	private Pulse firingPulse;

	/**
	 * The current direction.
	 */
	private DMCRevolution direction = DMCRevolution.NORTH;

	/**
	 * The decaying pulse.
	 */
	private Pulse decayPulse;

	public DMCHandler() {
		this.player = null;
	}

	/**
	 * Constructs a new {@code DMCHandler} {@code Object}
	 * @param player The player owning the cannon.
	 */
	public DMCHandler(final Player player) {
		this.player = player;
		this.firingPulse = new Pulse(1, player) {
			@Override
			public boolean pulse() {
				if (!cannon.isActive()) {
					return true;
				}
				return rotate();
			}

		};
		firingPulse.stop();
		this.decayPulse = new Pulse(2000, player) {
			@Override
			public boolean pulse() {
				if (!cannon.isActive()) {
					return true;
				}
				if (getDelay() == 2000) {
					setDelay(500);
					player.sendMessage("Your cannon is about to decay!");
					return false;
				}
				explode(true);
				return true;
			}

		};
		decayPulse.stop();
	}

	/**
	 * Rotates the cannon.
	 * @return {@code True} if the cannon should stop rotating.
	 */
	private boolean rotate() {
		if (cannonballs < 1) {
			player.getPacketDispatch().sendMessage("Your cannon has run out of ammo!");
			return true;
		}
		player.getPacketDispatch().sendSceneryAnimation(cannon, Animation.create(direction.getAnimationId()));
		Location l = cannon.getLocation().transform(1, 1, 0);
        player.getAudioManager().send(new Audio(2877), true, l);
		direction = DMCRevolution.values()[(direction.ordinal() + 1) % DMCRevolution.values().length];
		for (NPC npc : RegionManager.getLocalNpcs(l, 10)) {
			if (direction.isInSight(npc.getLocation().getX() - l.getX(), npc.getLocation().getY() - l.getY()) && npc.isAttackable(player, CombatStyle.RANGE, false) && CombatSwingHandler.isProjectileClipped(npc, l, false)) {
				int speed = (int) (25 + (l.getDistance(npc.getLocation()) * 10));
				Projectile.create(l, npc.getLocation(), 53, 40, 36, 20, speed, 0, 128).send();
                player.getAudioManager().send(new Audio(1667), true, l);
				cannonballs--;
				int hit = 0;
				if (player.getSwingHandler(false).isAccurateImpact(player, npc, CombatStyle.RANGE, 1.2, 1.0)) {
					hit = RandomFunction.getRandom(30);
				}
				player.getSkills().addExperience(Skills.RANGE, hit * 2);
				npc.getImpactHandler().manualHit(player, hit, HitsplatType.NORMAL, (int) Math.ceil(l.getDistance(npc.getLocation()) * 0.3));
				npc.attack(player);
				break;
			}
		}
		return false;
	}

	/**
	 * Starts rotating the cannon.
	 */
	public void startFiring() {
		if (cannon == null || !cannon.isActive()) {
			player.getPacketDispatch().sendMessage("You don't have a cannon active.");
			return;
		}
		if (firingPulse.isRunning()) {
			firingPulse.stop();
			return;
		}
		if (cannonballs < 1) {
			int amount = player.getInventory().getAmount(new Item(2));
			if (amount < 1) {
				player.getPacketDispatch().sendMessage("Your cannon is out of cannonballs.");
				return;
			}
			int toUse = 30 - cannonballs;
			if(toUse > amount){
				toUse = amount;
			}
			if(toUse > 0){
				cannonballs = toUse;
				player.getPacketDispatch().sendMessage("You load the cannon with " + toUse + " cannonballs.");
				player.getInventory().remove(new Item(2, toUse));
			} else {
				player.sendMessage("Your cannon is already fully loaded.");
			}
		}
		firingPulse.restart();
		firingPulse.start();
		GameWorld.getPulser().submit(firingPulse);
	}

	/**
	 * Makes the cannon explode.
	 */
	public void explode(boolean decay) {
		if (!cannon.isActive()) {
			return;
		}
		player.sendMessage("Your cannon has " + (decay ? "decayed" : "been destroyed") + "!");
		for (Player p : RegionManager.getLocalPlayers(player)) {
			p.getPacketDispatch().sendPositionedGraphic(189, 0, 1, cannon.getLocation());
		}
		clear(false);
	}

	/**
	 * Constructs the cannon.
	 */
	public static void construct(final Player player) {
		final Location spawn = RegionManager.getSpawnLocation(player, new Scenery(6, player.getLocation()));
		if (spawn == null) {
			player.getPacketDispatch().sendMessage("There's not enough room for your cannon.");
			return;
		}
		if (player.getZoneMonitor().isRestricted(ZoneRestriction.CANNON)) {
			player.getPacketDispatch().sendMessage("You can't set up a cannon here.");
			return;
		}
		final DMCHandler handler = new DMCHandler(player);
		if (handler.decayPulse.isRunning()) {
			handler.decayPulse.stop();
			return;
		}
		player.setAttribute("dmc", handler);
		player.getPulseManager().clear();
		player.getWalkingQueue().reset();
		player.lock(9);
		player.faceLocation(spawn.transform(Direction.NORTH_EAST));
		GameWorld.getPulser().submit(new Pulse(2, player) {
			int count = 0;
			Scenery object = null;

			@Override
			public boolean pulse() {
				player.animate(Animation.create(827));
				if (!player.getInventory().remove(new Item(6 + (count * 2)))) {
					for (int i = count - 1; i >= 0; i--) {
						player.getInventory().add(new Item(6 + (i * 2)));
					}
					if (object != null) {
						SceneryBuilder.remove(object);
					}
					return true;
				}
				switch (count) {
				case 0:
					object = SceneryBuilder.add(new Scenery(7, spawn));
					player.getPacketDispatch().sendMessage("You place the cannon base on the ground.");
					break;
				case 1:
					player.getPacketDispatch().sendMessage("You add the stand.");
					break;
				case 2:
					player.getPacketDispatch().sendMessage("You add the barrels.");
					break;
				case 3:
					player.getPacketDispatch().sendMessage("You add the furnace.");
					SceneryBuilder.remove(object);
					handler.configure(SceneryBuilder.add(object = object.transform(6)));
					return true;
				}
                player.getAudioManager().send(new Audio(2876), true);
                if(count != 0) {
                    SceneryBuilder.remove(object);
                    SceneryBuilder.add(object = object.transform(object.getId() + 1));
                }
				return ++count == 4;
			}
		});
	}

	/**
	 * Configures the cannon.
	 * @param cannon The cannon.
	 */
	public void configure(Scenery cannon) {
		this.cannon = cannon;
	}

	@Override
	public void logout(@NotNull Player player) {
		if(player.getAttribute("dmc") != null)
		{
			DMCHandler handler = player.getAttribute("dmc");
			handler.clear(false);
		}
	}

	/**
	 * Clears the cannon.
	 * @param pickup If the cannon is getting picked up.
	 */
	public void clear(boolean pickup) {
		if (decayPulse.isRunning()) {
			decayPulse.stop();
		}
		SceneryBuilder.remove(cannon);
		player.removeAttribute("dmc");
		if (!pickup) {
			player.getSavedData().getActivityData().setLostCannon(true);
			return;
		}
		for (int i = 0; i < 4; i++) {
			player.getInventory().add(new Item(12 - (i * 2)));
		}
		if (cannonballs > 0) {
			player.getInventory().add(new Item(2, cannonballs));
			cannonballs = 0;
		}
	}

	/**
	 * Gets the player.
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the cannon.
	 * @return the cannon
	 */
	public Scenery getCannon() {
		return cannon;
	}

	/**
	 * Sets the cannon.
	 * @param cannon the cannon to set.
	 */
	public void setCannon(Scenery cannon) {
		this.cannon = cannon;
	}

	/**
	 * Gets the cannonballs.
	 * @return the cannonballs
	 */
	public int getCannonballs() {
		return cannonballs;
	}

	/**
	 * Sets the bacannonballs.
	 * @param cannonballs the cannonballs to set.
	 */
	public void setCannonballs(int cannonballs) {
		this.cannonballs = cannonballs;
	}

}
