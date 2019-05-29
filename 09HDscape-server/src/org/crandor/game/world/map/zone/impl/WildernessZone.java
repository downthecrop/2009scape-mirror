package org.crandor.game.world.map.zone.impl;

import org.crandor.game.component.Component;
import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.interaction.Option;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.CombatStyle;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.npc.agg.AggressiveBehavior;
import org.crandor.game.node.entity.npc.agg.AggressiveHandler;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.info.Rights;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.zone.MapZone;
import org.crandor.game.world.map.zone.RegionZone;
import org.crandor.game.world.map.zone.ZoneBorders;

/**
 * Handles the wilderness zone.
 * @author Emperor
 */
public final class WildernessZone extends MapZone {

	/**
	 * The wilderness zone.
	 */
	private static final WildernessZone INSTANCE = new WildernessZone(new ZoneBorders(2944, 3525, 3400, 3975), new ZoneBorders(3070, 9924, 3135, 10002), ZoneBorders.forRegion(12193), ZoneBorders.forRegion(11937));

	/**
	 * The zone borders.
	 */
	private ZoneBorders[] borders;

	/**
	 * Constructs a new {@code WildernessZone} {@code Object}.
	 */
	public WildernessZone(ZoneBorders... borders) {
		super("Wilderness", true);
		this.borders = borders;
	}

	@Override
	public void configure() {
		for (ZoneBorders border : borders) {
			register(border);
		}
	}

	@Override
	public boolean enter(Entity e) {
		if (e instanceof Player) {
			Player p = (Player) e;
			show(p);
			for (int i = 0; i < 7; i++) {
				if (i == 5 || i == 3) {
					continue;
				}
				if(p.getAttributes().containsKey("overload") || p.getSkills().getLevel(i) > 118){
					if (p.getSkills().getLevel(i) > p.getSkills().getStaticLevel(i)) {
						p.getSkills().setLevel(i, p.getSkills().getStaticLevel(i));
						p.removeAttribute("overload");
					}
				}
			}
			if (p.getFamiliarManager().hasFamiliar() && !p.getFamiliarManager().hasPet()) {
				Familiar familiar = p.getFamiliarManager().getFamiliar();
				familiar.transform();
			}
			p.getAppearance().sync();
		} else if (e instanceof NPC) {
			NPC n = (NPC) e;
			if (n.getDefinition().hasAttackOption()) {
				n.setAggressive(true);
				n.setAggressiveHandler(new AggressiveHandler(n, AggressiveBehavior.WILDERNESS));
			}
		}
		return true;
	}

	@Override
	public boolean leave(Entity e, boolean logout) {
		if (!logout && e instanceof Player) {
			Player p = (Player) e;
			leave(p);
			if (p.getFamiliarManager().hasFamiliar() && !p.getFamiliarManager().hasPet()) {
				Familiar familiar = p.getFamiliarManager().getFamiliar();
				if (familiar.isCombatFamiliar()) {
					familiar.reTransform();
				}
			}
			p.getAppearance().sync();
		}
		return true;
	}

	/**
	 * Method used to remove traces of being in the zone.
	 * @param p the player.
	 */
	public final void leave(final Player p) {
		Component overlay = new Component(381);
		if (overlay.getId() == 381) {
			p.getInterfaceManager().close(overlay);
		}
		p.getInteraction().remove(Option._P_ATTACK);
		p.getSkullManager().setWilderness(false);
		p.getSkullManager().setLevel(0);
	}

	/**
	 * Method used to show being the wilderness.
	 * @param p the player.
	 */
	public static final void show(final Player p) {
		if (p.getSkullManager().isWildernessDisabled()) {
			return;
		}
		p.getInterfaceManager().openOverlay(new Component(381));
		p.getSkullManager().setLevel(getWilderness(p));
		p.getPacketDispatch().sendString("Level: " + p.getSkullManager().getLevel(), 381, 1);
		p.getInteraction().set(Option._P_ATTACK);
		p.getSkullManager().setWilderness(true);
	}

	@Override
	public boolean teleport(Entity e, int type, Node node) {
		if (e instanceof Player) {
			Player p = (Player) e;
			if (p.getDetails().getRights() == Rights.ADMINISTRATOR) {
				return true;
			}
			if (!checkTeleport(p, (node != null && node instanceof Item && (((Item) node).getName().contains("glory") || ((Item) node).getName().contains("slaying")) ? 30 : 20))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Method used to check if a player can teleport past a level.
	 * @param p the player.
	 * @param level the level.
	 * @return {@code True} if they can teleport.
	 */
	public static boolean checkTeleport(Player p, int level) {
		if (p.getSkullManager().getLevel() > level && !p.getSkullManager().isWildernessDisabled()) {
			message(p, "You can't teleport this deep in the wilderness!");
			return false;
		}
		return true;
	}

	@Override
	public boolean continueAttack(Entity e, Node target, CombatStyle style, boolean message) {
		if (e instanceof Player && target instanceof Player) {
			Player p = (Player) e;
			Player t = (Player) target;
			int level = p.getSkullManager().getLevel();
			if (t.getSkullManager().getLevel() < level) {
				level = t.getSkullManager().getLevel();
			}
			int combat = p.getProperties().getCurrentCombatLevel();
			int targetCombat = t.getProperties().getCurrentCombatLevel();
			if (combat - level > targetCombat || combat + level < targetCombat) {
				if (message) {
					p.getPacketDispatch().sendMessage("The level difference between you and your opponent is too great.");
				}
				return false;
			}
		}
		return true;
	}

	@Override
	public void locationUpdate(Entity e, Location last) {
		if (e instanceof Player) {
			Player p = (Player) e;
			p.getSkullManager().setLevel(getWilderness(p));
			Component overlay = p.getInterfaceManager().getOverlay();
			if (overlay == null || overlay.getId() != 381) {
				show(p);
			}
			if (!p.getSkullManager().isWildernessDisabled()) {
				p.getPacketDispatch().sendString("Level: " + p.getSkullManager().getLevel(), 381, 1);
			}
		}
	}

	/**
	 * Checks if the entity is inside the wilderness.
	 * @param e The entity.
	 * @return {@code True} if so.
	 */
	public static boolean isInZone(Entity e) {
		Location l = e.getLocation();
		for (RegionZone zone : e.getViewport().getRegion().getRegionZones()) {
			if (zone.getZone() == INSTANCE && zone.getBorders().insideBorder(l.getX(), l.getY())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * The wilderness level.
	 * @return the level.
	 */
	public static int getWilderness(Entity e) {
		final int regionId = e.getViewport().getRegion().getId();
		int offsetY = 3524;
		if (regionId == 12443 || e.getViewport().getRegion().getId() == 12444) {
			offsetY = 9923;
		} else if (regionId == 12193) {
			offsetY = 10000 - 80;
		} else if (regionId == 11937) {
			offsetY = 9920;
		}
		int level = (int) Math.ceil((double) (e.getLocation().getY() - offsetY) / 8d);
		if (level < 0) {
			return 1;
		}
		return level;
	}

	/**
	 * Gets the zone borders.
	 * @return The borders.
	 */
	public ZoneBorders[] getBorders() {
		return borders;
	}

	/**
	 * Gets the instance.
	 * @return The instance.
	 */
	public static WildernessZone getInstance() {
		return INSTANCE;
	}

}