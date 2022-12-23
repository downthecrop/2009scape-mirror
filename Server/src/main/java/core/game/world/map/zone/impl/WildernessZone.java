package core.game.world.map.zone.impl;

import core.game.component.Component;
import core.game.container.Container;
import core.game.content.ttrail.UriNPC;
import core.game.interaction.Option;
import core.game.interaction.item.brawling_gloves.BrawlingGloves;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.DeathTask;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.npc.agg.AggressiveBehavior;
import core.game.node.entity.npc.agg.AggressiveHandler;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.Rights;
import core.game.node.entity.player.link.IronmanMode;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.item.GroundItem;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.world.map.Location;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.RegionZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneRestriction;
import core.tools.RandomFunction;
import org.rs09.consts.NPCs;
import rs09.game.system.config.NPCConfigParser;
import rs09.game.world.GameWorld;
import rs09.game.world.repository.Repository;

import java.util.ArrayList;
import java.util.List;

import static rs09.game.node.entity.player.info.stats.StatAttributeKeysKt.STATS_BASE;
import static rs09.game.node.entity.player.info.stats.StatAttributeKeysKt.STATS_DEATHS;


/**
 * Handles the wilderness zone.
 * @author Emperor
 */
public final class WildernessZone extends MapZone {
	/**
	 * The PvP gear items
	 */
	private static int[] PVP_GEAR = { 13887, 13893, 13899, 13905, 13870, 13873, 13876, 13879, 13883, 13884, 13890, 13896, 13902, 13858, 13861, 13864, 13867};

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
		super("Wilderness", true, ZoneRestriction.RANDOM_EVENTS);
		this.borders = borders;
	}

	@Override
	public void configure() {
		for (ZoneBorders border : borders) {
			register(border);
		}
	}

	/**
	 * calculate drop rate for rev items based on combat level
	 * @author ceik
	 * @param combatLevel
	 * @return
	 */
	public int getNewDropRate(int combatLevel){
		double x = combatLevel;
		double A = 44044.5491;
		double B = -7360.19548;
		return (int) (A + (B * Math.log(x))) / 2;
	}

	/**
	 * Handles rev drops
	 * @author ceik
	 * @param e The entity dying.
	 * @param killer The killer.
	 * @return true
	 */
	@Override
	public boolean death(Entity e, Entity killer) {
		if (e instanceof NPC)
			rollWildernessExclusiveLoot(e, killer);
		return false; //DONT override default death handling.
	}

	private void rollWildernessExclusiveLoot(Entity e, Entity killer) {
		//Roll for PVP gear and Brawling Gloves from revenants
		if (e instanceof NPC && killer instanceof Player && (e.asNpc().getName().contains("Revenant") || e.getId() == NPCs.CHAOS_ELEMENTAL_3200)) {

			boolean gloveDrop = e.getId() == NPCs.CHAOS_ELEMENTAL_3200 ? RandomFunction.roll(75) : RandomFunction.roll(100);
			if (gloveDrop) {
				byte glove = (byte) RandomFunction.random(1, 13);
				Item reward = new Item(BrawlingGloves.forIndicator(glove).getId());
				GroundItemManager.create(reward, e.asNpc().getDropLocation(), killer.asPlayer());
				Repository.sendNews(killer.getUsername() + " has received " + reward.getName().toLowerCase() + " from a " + e.asNpc().getName() + "!");
			}

			int combatLevel = e.asNpc().getDefinition().getCombatLevel();
			int dropRate = getNewDropRate(combatLevel);
			for (int j : PVP_GEAR) {
				boolean chance = RandomFunction.roll(dropRate);
				if (chance) {
					Item reward;
					if (j == 13879 || j == 13883) { // checks if it's a javelin or throwing axe
						reward = new Item(j, RandomFunction.random(15, 50));
					} else {
						reward = new Item(j);
					}
					Repository.sendNews(killer.asPlayer().getUsername() + " has received a " + reward.getName() + " from a " + e.asNpc().getName() + "!");
					GroundItemManager.create(reward, ((NPC) e).getDropLocation(), killer.asPlayer());
				}
			}
		}
	}

	/**
	 * Fixes attack options for the revs
	 * @param e The entity.
	 * @param target The target to interact with.
	 * @param option The option.
	 * @return true
	 */
	@Override
	public boolean interact(Entity e, Node target, Option option) {
		if(target instanceof NPC){
			if(target.asNpc().getName().contains("Revenant")){
				e.asPlayer().getProperties().getCombatPulse().attack(target);
				return true;
			}
		}
		return super.interact(e, target, option);
	}



	@Override
	public boolean enter(Entity e) {
		if (e instanceof Player) {
			Player p = (Player) e;
			if(!p.isArtificial()) {
				show(p);
			} else {
				p.getSkullManager().setWilderness(true);
				p.getSkullManager().setLevel(getWilderness(p));
			}
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
			if (n.getDefinition().hasAttackOption() && n.isAggressive()) {
				//n.setAggressive(true);
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
		p.getInterfaceManager().openWildernessOverlay(new Component(381));
		p.getSkullManager().setLevel(getWilderness(p));
		p.getPacketDispatch().sendString("Level: " + p.getSkullManager().getLevel(), 381, 1);
		if(GameWorld.getSettings().getWild_pvp_enabled()) {
			p.getInteraction().set(Option._P_ATTACK);
		}
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
		if (e instanceof Player && !e.asPlayer().isArtificial()) {
			Player p = (Player) e;
			p.getSkullManager().setLevel(getWilderness(p));
		}
	}

	/**
	 * Checks if the entity is inside the wilderness.
	 * @param e The entity.
	 * @return {@code True} if so.
	 */
	public static boolean isInZone(Entity e) {
		Location l = e.getLocation();
		for (ZoneBorders z : INSTANCE.borders) {
			if (z.insideBorder(e))
				return true;
		}
		return false;
	}

	/**
	 * The wilderness level.
	 * @return the level.
	 */
	public static int getWilderness(Entity e) {
        int y = e.getLocation().getY();
        if(6400 < y) {
            return ((y - 9920) / 8) + 1;
        } else {
            return ((y - 3520) / 8) + 1;
        }
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
