package core.game.node.entity.skill.slayer.dungeon;

import static api.ContentAPIKt.*;
import core.cache.def.impl.SceneryDefinition;
import core.game.component.CloseEvent;
import core.game.component.Component;
import core.plugin.Initializable;
import core.game.node.entity.skill.agility.AgilityHandler;
import core.game.interaction.Option;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.game.world.map.zone.ZoneRestriction;
import core.game.world.update.flag.context.Animation;
import core.net.packet.PacketRepository;
import core.net.packet.context.MinimapStateContext;
import core.net.packet.out.MinimapState;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;
import core.tools.RandomFunction;

/**
 * Handles the ancient cavern.
 * @author Vexia
 */
@Initializable
public final class AncientCavern extends MapZone implements Plugin<Object> {

	/**
	 * The loots recieved when rummaging a skeleton.
	 */
	private static final Item[] LOOTS = new Item[] { new Item(526), new Item(11337), new Item(11341) };

	/**
	 * The skeleton barbarian id.
	 */
	private static final int[] SKELETONS = new int[] { 6103, 6106 };

	/**
	 * Constructs a new {@code AnicentCavern} {@code Object}.
	 */
	public AncientCavern() {
		super("ancient cavern", true, ZoneRestriction.CANNON);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ZoneBuilder.configure(this);
		ClassScanner.definePlugin(new OptionHandler() {

			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				SceneryDefinition.forId(25274).getHandlers().put("option:dive in", this);
				return this;
			}

			@Override
			public boolean handle(final Player player, Node node, String option) {
				lock(player, 30);
				AgilityHandler.forceWalk(player, -1, player.getLocation(), player.getLocation().transform(0, -6, 0), Animation.create(6723), 10, 0.0, null);
				GameWorld.getPulser().submit(new Pulse(1, player) {
					int count;

					@Override
					public boolean pulse() {
						switch (++count) {
						case 4:
							player.getInterfaceManager().openOverlay(new Component(115));
							break;
						case 7:
							player.getAnimator().reset();
							player.getInterfaceManager().close();
							player.getInterfaceManager().closeOverlay();
							player.getProperties().setTeleportLocation(Location.create(1763, 5365, 1));
							player.getPacketDispatch().sendMessages("You dive into the swirling maelstorm of the whirlpool.", "You are swirled beneath the water, the darkness and pressure are overwhelming.", "Mystical forces guide you into a cavern below the whirlpool.");
							break;
						case 8:
							unlock(player);
							return true;
						}
						return false;
					}
				});
				return true;
			}

			@Override
			public Location getDestination(Node node, Node n) {
				if (node.getLocation().getX() <= 2511) {
					return Location.create(2511, 3516, 0);
				} else {
					return Location.create(2512, 3516, 0);
				}
			}

		});
		return this;
	}

	@Override
	public boolean interact(Entity e, Node target, Option option) {
		if (e instanceof Player) {
			Player player = (Player) e;
			switch (target.getId()) {
			case 25216:
				handleLog(player);
				return true;
			case 25362:
				rummageSkeleton(player, (Scenery) target);
				return true;
			}
		}
		return super.interact(e, target, option);
	}

	/**
	 * Handles the log ride.
	 * @param player the player.
	 */
	private void handleLog(final Player player) {
		player.removeAttribute("canClose");
		player.lock(14);
		player.getImpactHandler().setDisabledTicks(14);
		GameWorld.getPulser().submit(new Pulse(1) {
			int count = 0;

			@Override
			public boolean pulse() {
				switch (count++) {
				case 1:
					Component c = new Component(115);
					c.setCloseEvent(new CloseEvent() {

						@Override
						public boolean close(Player player, Component c) {
							if (player.getAttribute("canClose", false)) {
								return true;
							}
							return false;
						}

					});
					player.getInterfaceManager().open(c);
					break;
				case 3:
					PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 2));
					player.getInterfaceManager().removeTabs(0, 1, 2, 3, 4, 5, 6, 11, 12);
					break;
				case 13:
					player.getProperties().setTeleportLocation(Location.create(2532, 3412, 0));
					break;
				case 14:
					player.setAttribute("canClose", true);
					player.unlock();
					player.getInterfaceManager().close();
					player.getInterfaceManager().closeOverlay();
					player.getInterfaceManager().restoreTabs();
					PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 0));
					return true;
				}
				return false;
			}

		});
	}

	/**
	 * Rummages a skeleton.
	 * @param player the player.
	 * @param object the object.
	 */
	private void rummageSkeleton(Player player, Scenery object) {
		final boolean fullInvy = player.getInventory().freeSlots() < 1;
		final int random = RandomFunction.random(0, 2);
		player.getPacketDispatch().sendMessages("You rummage in the sharp, slimy pile of bones in search of something useful...");
		if (random == 0) {
			if (!fullInvy) {
				player.getInventory().add(LOOTS[RandomFunction.random(LOOTS.length)], player);
				player.getPacketDispatch().sendMessage("...you find something and stow it in your pack.");
			} else {
				player.getPacketDispatch().sendMessage("...you find something, but it drops to the floor.");
			}
		} else if (random == 1) {
			NPC spawn = NPC.create(SKELETONS[RandomFunction.random(SKELETONS.length)], object.getLocation());
			spawn.init();
			spawn.setRespawn(false);
			removeSkeleton(object, spawn);
			spawn.getProperties().getCombatPulse().attack(player);
			player.getPacketDispatch().sendMessage("...the bones object.");
		} else {
			player.getPacketDispatch().sendMessage("...but there's nothing remotely valuable.");
		}
		if (RandomFunction.random(10) < 3) {
			player.getImpactHandler().manualHit(player, RandomFunction.random(14), HitsplatType.NORMAL);
		}
	}

	/**
	 * Removes a skeleton.
	 * @param object the object.
	 * @param spawn the spawn.
	 */
	private void removeSkeleton(final Scenery object, final NPC spawn) {
		SceneryBuilder.remove(object);
		GameWorld.getPulser().submit(new Pulse(200) {
			@Override
			public boolean pulse() {
				if (spawn != null && spawn.isActive()) {
					spawn.clear();
				}
				SceneryBuilder.add(object);
				return true;
			}
		});
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return this;
	}

	@Override
	public void configure() {
		register(new ZoneBorders(1723, 5296, 1831, 5394));
	}

}
