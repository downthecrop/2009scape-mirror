package core.game.content.activity.pestcontrol;

import core.game.interaction.Option;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneRestriction;

/**
 * The pest control lander zone handler.
 * @author Emperor
 */
public final class PCLanderZone extends MapZone {

	/**
	 * The activities.
	 */
	private PestControlActivityPlugin[] activities;

	/**
	 * Constructs a new {@code PCLanderZone} {@code Object}.
	 * @param activities The activities.
	 */
	public PCLanderZone(PestControlActivityPlugin[] activities) {
		super("pest control lander", true, ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.FIRES, ZoneRestriction.FOLLOWERS, ZoneRestriction.CANNON);
		this.activities = activities;
	}

	@Override
	public boolean interact(Entity e, Node target, Option option) {
		if (target instanceof Scenery) {
			Scenery o = (Scenery) target;
			switch (o.getId()) {
			case 14314: // Novice exit ladder
				if (activities[0].getWaitingPlayers().contains(e)) {
					activities[0].getWaitingPlayers().remove(e);
					e.getProperties().setTeleportLocation(activities[0].getLeaveLocation());
				}
				return true;
			case 25629: // Intermediate exit ladder
				if (activities[1].getWaitingPlayers().contains(e)) {
					activities[1].getWaitingPlayers().remove(e);
					e.getProperties().setTeleportLocation(activities[1].getLeaveLocation());
				}
				return true;
			case 25630: // Veteran exit ladder
				if (activities[2].getWaitingPlayers().contains(e)) {
					activities[2].getWaitingPlayers().remove(e);
					e.getProperties().setTeleportLocation(activities[2].getLeaveLocation());
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean teleport(Entity e, int type, Node node) {
		if (e instanceof Player && type != -1) {
			for (PestControlActivityPlugin a : activities) {
				if (a.getWaitingPlayers().contains(e)) {
					((Player) e).getPacketDispatch().sendMessage("The knights don't appreciate you teleporting off their craft!");
					return false;
				}
			}
		}
		return super.teleport(e, type, node);
	}

	@Override
	public boolean leave(Entity e, boolean logout) {
		if (e instanceof Player) {
			Player p = (Player) e;
			if (p.getInterfaceManager().getComponent(407) != null) {
				p.getInterfaceManager().closeOverlay();
			}
			for (PestControlActivityPlugin a : activities) {
				if (a.getWaitingPlayers().remove(e)) {
					if (logout) {
						e.getProperties().setTeleportLocation(a.getLeaveLocation());
					}
					break;
				}
			}
		}
		return super.leave(e, logout);
	}

	@Override
	public void configure() {
		ZoneBorders boatA = new ZoneBorders(2659, 2637, 2664, 2664);
		ZoneBorders boatB = new ZoneBorders(2637, 2641, 2642, 2648);
		ZoneBorders boatC = new ZoneBorders(2631, 2648, 2636, 2655);

		register(boatA);
		register(boatB);
		register(boatC);
	}

}