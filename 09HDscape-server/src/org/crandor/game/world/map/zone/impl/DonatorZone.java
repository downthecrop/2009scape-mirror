package org.crandor.game.world.map.zone.impl;

import org.crandor.game.interaction.Option;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.info.portal.DonatorType;
import org.crandor.game.node.entity.player.link.TeleportManager.TeleportType;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.node.object.ObjectBuilder;
import org.crandor.game.world.map.Direction;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.Region;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.map.build.DynamicRegion;
import org.crandor.game.world.map.zone.MapZone;

/**
 * Represents the donator zone.
 * @author Vexia
 * @author Emperor
 */
public class DonatorZone extends MapZone {

	/**
	 * The instance of the zone.
	 */
	private static final DonatorZone INSTANCE = new DonatorZone();

	/**
	 * The grandpa jack npc.
	 */
	private static NPC jack;

	/**
	 * The region of the zone.
	 */
	private DynamicRegion region;

	/**
	 * The base location.
	 */
	private Location base;

	/**
	 * Constructs a new {@code DonatorZone} {@code Object}
	 */
	public DonatorZone() {
		super("Donator Zone", true);
	}

	@Override
	public boolean enter(Entity entity) {
		if (entity.isPlayer() && (!entity.asPlayer().isDonator() && !entity.asPlayer().isAdmin())) {
			return super.enter(entity);
		}
		return super.enter(entity);
	}

	@Override
	public boolean interact(Entity entity, Node node, Option option) {
		if (entity.isPlayer()) {
			Player player = entity.asPlayer();
			switch (node.getId()) {
			case 14073:
				player.getTeleporter().send(Location.create(3236, 3225, 0), TeleportType.FAIRY_RING);
				return true;
			}
		}
		return super.interact(entity, node, option);
	}

	@Override
	public void locationUpdate(Entity e, Location last) {
		if (e.isPlayer() && e.getLocation().getY() < 44 && e.asPlayer().isDonator() && e.asPlayer().getDonatorType() != DonatorType.EXTREME) {
			if (e.getPulseManager().isMovingPulse()) {
				e.getPulseManager().clear();
			}
			e.getWalkingQueue().reset();
			return;
		}
		super.locationUpdate(e, last);
	}

	@Override
	public boolean move(Entity e, Location from, Location to) {
		if (e.isPlayer() && to.getY() < 44 && e.asPlayer().isDonator() && e.asPlayer().getDonatorType() != DonatorType.EXTREME) {
			e.asPlayer().sendMessages("A magical force blocks you from moving forward. That area is restricted to extreme", "donators only.");
			return false;
		}
		return super.move(e, from, to);
	}

	@Override
	public boolean leave(Entity e, boolean logout) {
		return super.leave(e, logout);
	}

	@Override
	public void configure() {
		region = DynamicRegion.create(12102);
		setRegionBase();
		registerRegion(region.getId());
		addNpc(jack = NPC.create(230, base.transform(33, 50, 0)));
		addNpc(NPC.create(6528, base.transform(29, 48, 0), Direction.NORTH));
		addNpc(NPC.create(6528, base.transform(36, 48, 0), Direction.NORTH));
		// banks
		addObject(new GameObject(26972, base.transform(30, 48, 0), 2));
		addObject(new GameObject(26972, base.transform(35, 48, 0), 2));
		// fires
		addObject(new GameObject(2732, base.transform(32, 48, 0), 0));
		addObject(new GameObject(2732, base.transform(33, 48, 0), 0));
		// glory charge
		addObject(new GameObject(36695, base.transform(38, 48, 0), 0));
		// altars
		addObject(new GameObject(409, base.transform(24, 37, 0), 7));
		addObject(new GameObject(6552, base.transform(19, 38, 0), 7));
		addObject(new GameObject(17010, base.transform(16, 31, 0), 7));
		// mining
		// copper
		addObject(new GameObject(2090, base.transform(14, 15, 0)));
		addObject(new GameObject(2091, base.transform(15, 16, 0)));
		addObject(new GameObject(2090, base.transform(13, 16, 0)));
		// tin
		addObject(new GameObject(2094, base.transform(13, 17, 0)));
		addObject(new GameObject(2095, base.transform(12, 16, 0)));
		addObject(new GameObject(2094, base.transform(11, 17, 0)));
		// coal
		addObject(new GameObject(2096, base.transform(20, 12, 0)));
		addObject(new GameObject(2097, base.transform(19, 11, 0)));
		addObject(new GameObject(2096, base.transform(19, 13, 0)));
		// iron
		addObject(new GameObject(2092, base.transform(22, 10, 0)));
		addObject(new GameObject(2093, base.transform(22, 12, 0)));
		addObject(new GameObject(2092, base.transform(21, 11, 0)));
		// gold
		addObject(new GameObject(2099, base.transform(15, 15, 0)));
		addObject(new GameObject(2098, base.transform(16, 14, 0)));
		addObject(new GameObject(2099, base.transform(17, 15, 0)));
		// addy
		addObject(new GameObject(2105, base.transform(17, 14, 0)));
		addObject(new GameObject(2105, base.transform(16, 13, 0)));
		addObject(new GameObject(2105, base.transform(17, 12, 0)));
		// mith
		addObject(new GameObject(2102, base.transform(25, 11, 0)));
		addObject(new GameObject(2103, base.transform(24, 12, 0)));
		addObject(new GameObject(2102, base.transform(24, 11, 0)));
		// runite
		addObject(new GameObject(2107, base.transform(20, 13, 0)));
		addObject(new GameObject(2106, base.transform(21, 12, 0)));
		addObject(new GameObject(2107, base.transform(22, 13, 0)));
		// furnace
		addObject(new GameObject(11666, base.transform(25, 48, 0), 7));
		region.getPlanes()[0].getFlags().flagSolidTile(27, 49);
		region.getPlanes()[0].getFlags().flagSolidTile(25, 49);
		// anvil
		addObject(new GameObject(2782, base.transform(28, 48, 0)));
		// wheel
		addObject(new GameObject(4309, base.transform(25, 54, 0), 3));
		// small obelisk
		addObject(new GameObject(29882, base.transform(36, 56, 0)));
		// big obby
		addObject(new GameObject(28716, base.transform(21, 41, 0)));
		// low level stalls
		addObject(new GameObject(4876, base.transform(29, 56, 0)));
		addObject(new GameObject(4875, base.transform(30, 56, 0)));
		// trees
		// oaks
		addObject(new GameObject(1281, base.transform(28, 17, 0)));
		addObject(new GameObject(1281, base.transform(22, 26, 0)));
		addObject(new GameObject(1281, base.transform(27, 23, 0)));
		addObject(new GameObject(1281, base.transform(23, 15, 0)));
		// willows
		addObject(new GameObject(1308, base.transform(31, 19, 0)));
		addObject(new GameObject(1308, base.transform(28, 15, 0)));
		addObject(new GameObject(1308, base.transform(26, 21, 0)));
		// maples
		addObject(new GameObject(1307, base.transform(11, 22, 0)));
		addObject(new GameObject(1307, base.transform(12, 26, 0)));
		addObject(new GameObject(1307, base.transform(16, 24, 0)));
		// teaks
		addObject(new GameObject(9036, base.transform(32, 9, 0)));
		addObject(new GameObject(9036, base.transform(35, 12, 0)));
		addObject(new GameObject(9036, base.transform(28, 11, 0)));
		// mahogany
		addObject(new GameObject(9034, base.transform(37, 20, 0)));
		addObject(new GameObject(9034, base.transform(31, 22, 0)));
		// yews
		addObject(new GameObject(1309, base.transform(11, 32, 0)));
		addObject(new GameObject(1309, base.transform(10, 29, 0)));
		addObject(new GameObject(1309, base.transform(12, 35, 0)));
		addObject(new GameObject(1309, base.transform(38, 14, 0)));
		// magic
		addObject(new GameObject(1306, base.transform(44, 14, 0)));
		addObject(new GameObject(1306, base.transform(45, 10, 0)));
		addObject(new GameObject(1306, base.transform(41, 10, 0)));
		// fishing dock
		region.getPlanes()[0].getFlags().unflagTileObject(35, 28);
		region.getPlanes()[0].getFlags().unflagTileObject(35, 29);
		// range
		addObject(new GameObject(2728, base.transform(23, 50, 0)));
		// remove stalagmites
		for (GameObject[] o : region.getPlanes()[0].getObjects()) {
			for (GameObject obj : o) {
				if (obj != null) {
					if (obj.getName().equals("Stalagmite")) {
						ObjectBuilder.remove(obj);
					}
				}
			}
		}
		region.setPermanent(true);
		region.getPlanes()[0].getFlags().flag(base.transform(27, 49, 0).getX(), base.transform(27, 49, 0).getY(), 0x100);
		Region region = RegionManager.getRegionCache().get(12102);
		if (region != null) {
			for (NPC n : region.getPlanes()[0].getNpcs()) {
				NPC npc = NPC.create(n.getId(), base.transform(n.getLocation().getLocalX(), n.getLocation().getLocalY(), 0));
				npc.setAggressive(false);
				npc.setAggressiveHandler(null);
				npc.init();
				npc.setAggressive(false);
				npc.setAggressiveHandler(null);
				npc.setWalks(true);
			}
		}
	}

	/**
	 * Adds an npc into the zone.
	 * @param npc the npc.
	 */
	private void addNpc(NPC npc) {
		npc.init();
	}

	/**
	 * Adds a game object into the zone.
	 * @param object the object.
	 */
	private void addObject(GameObject object) {
		if (RegionManager.getObject(object.getLocation()) != null) {
			ObjectBuilder.remove(RegionManager.getObject(object.getLocation()));
		}
		ObjectBuilder.add(object);
	}

	/**
	 * Invites the player to the donator zone.
	 * @param player the player.
	 * @param npc the npc whom invited the player.
	 */
	public void invite(Player player, NPC npc) {
		if (!player.isDonator()) {
			return;
		}
		if (npc != null) {
			npc.faceTemporary(player, 3);
		}
		player.getTeleporter().send(getBase().transform(33, 52, 0), TeleportType.FAIRY_RING);
		jack.sendChat("We welcome " + (player.getDonatorType() == DonatorType.EXTREME ? "the legendary " : "") + "" + player.getUsername() + " to the donator zone!", 5);
	}

	/**
	 * Sets the region base.
	 */
	private void setRegionBase() {
		setBase(Location.create(region.getBorders().getSouthWestX(), region.getBorders().getSouthWestY(), 0));
	}

	/**
	 * Gets the base.
	 * @return the base
	 */
	public Location getBase() {
		return base;
	}

	/**
	 * Sets the base.
	 * @param base the base to set.
	 */
	public void setBase(Location base) {
		this.base = base;
	}

	/**
	 * Gets the instance.
	 * @return the instance
	 */
	public static DonatorZone getInstance() {
		return INSTANCE;
	}

}
