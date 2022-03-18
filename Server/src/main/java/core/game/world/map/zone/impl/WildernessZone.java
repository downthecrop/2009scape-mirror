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
		try {
			if (e instanceof UriNPC) {
				e.finalizeDeath(killer);
			}
			if(e instanceof Player){
				Player player = e.asPlayer();
				player.getSettings().setSpecialEnergy(100);
				player.getSettings().updateRunEnergy(player.getSettings().getRunEnergy() - 100);
				Player owner = killer instanceof Player ? (Player) killer : player;
				player.getPacketDispatch().sendMessage("Oh dear, you are dead!");
				player.incrementAttribute("/save:"+STATS_BASE+":"+STATS_DEATHS);

				//If player was a Hardcore Ironman, announce that they died
				if (player.getIronmanManager().getMode().equals(IronmanMode.HARDCORE)){ //if this was checkRestriction, ultimate irons would be moved to HARDCORE_DEAD as well
					String gender = player.isMale() ? "Man " : "Woman ";
					Repository.sendNews("Hardcore Iron " + gender + " " + player.getUsername() +" has fallen. Total Level: " + player.getSkills().getTotalLevel()); // Not enough room for XP
					player.getIronmanManager().setMode(IronmanMode.STANDARD);
					player.getSavedData().getActivityData().setHardcoreDeath(true);
					player.sendMessage("You have fallen as a Hardcore Iron Man, your Hardcore status has been revoked.");
				}

				player.getPacketDispatch().sendTempMusic(90);
				if (player.getDetails().getRights() != Rights.ADMINISTRATOR) {
					GroundItemManager.create(new Item(526), player.getLocation(), owner);
					final Container[] c = DeathTask.getContainers(player);
					boolean gravestone = player.getGraveManager().generateable() && player.getIronmanManager().getMode() != IronmanMode.ULTIMATE && !(killer instanceof Player);
					int seconds = player.getGraveManager().getType().getDecay() * 60;
					int ticks = (1000 * seconds) / 600;
					List<GroundItem> items = new ArrayList<>(20);
					for (Item item : c[1].toArray()) {
						if (item != null) {
							GroundItem ground;
							if (item.hasItemPlugin()) {
								item = item.getPlugin().getDeathItem(item);
							}
							if (!item.getDefinition().isTradeable()) {
								ground = new GroundItem(item, player.getLocation(), gravestone ? ticks + 100 : 200, player);
							} else {
								ground = new GroundItem(item.getDropItem(), player.getLocation(), owner);
							}
							items.add(ground);
							ground.setDropper(owner); //Checking for ironman mode in any circumstance for death items is inaccurate to how it works in both 2009scapes.
							GroundItemManager.create(ground);
						}
					}
					player.getEquipment().clear();
					player.getInventory().clear();
					if(!player.getSkullManager().isSkulled() || killer instanceof NPC) {
						player.getInventory().addAll(c[0]);
					} else {
						for(Item item : c[0].toArray()){
							GroundItemManager.create(item,player.getLocation(),owner);
						}
					}
					player.getFamiliarManager().dismiss();

				}
				player.getSkullManager().setSkulled(false);
				player.removeAttribute("combat-time");
				player.getPrayer().reset();
				player.getAppearance().sync();
				if (GameWorld.isEconomyWorld() && !player.getSavedData().getGlobalData().isDeathScreenDisabled()) {
					player.getInterfaceManager().open(new Component(153));
				}
				if (!player.getSavedData().getGlobalData().isDeathScreenDisabled()) {
					player.getInterfaceManager().open(new Component(153));
				}
			}

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
						return true;
					}
				}
			}

			if (e instanceof NPC) {
				e.asNpc().getDefinition().getDropTables().drop(e.asNpc(), killer);
				e.asNpc().setRespawnTick(GameWorld.getTicks() + e.asNpc().getDefinition().getConfiguration(NPCConfigParser.RESPAWN_DELAY, 17));
				if (!e.asNpc().isRespawn()) {
					e.asNpc().clear();
				}
			}
		} catch (Exception f){
			System.out.println("Unhandled NPC death in wilderness:  " + e.getId());
		}
		return true;
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