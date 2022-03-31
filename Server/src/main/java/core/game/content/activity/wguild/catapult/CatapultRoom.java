package core.game.content.activity.wguild.catapult;

import java.util.ArrayList;
import java.util.List;

import core.cache.def.impl.ItemDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.container.impl.EquipmentContainer;
import core.game.content.dialogue.FacialExpression;
import core.game.node.entity.skill.Skills;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.audio.Audio;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * The catapult room.
 * @author Emperor
 */
@Initializable
public final class CatapultRoom extends MapZone implements Plugin<Object> {

	/**
	 * The target location.
	 */
	public static final Location TARGET = Location.create(2842, 3545, 1);

	/**
	 * The shield item id.
	 */
	private static final int SHIELD_ID = 8856;

	/**
	 * Represents a catapult attack.
	 * @author Emperor
	 */
	private static enum CatapultAttack {
		SPIKY_BALL(679, 15617, Animation.create(4169), Animation.create(4173)), FLUNG_ANVIL(680, 15619, Animation.create(4168), Animation.create(4172)), SLASHING_BLADES(681, 15620, Animation.create(4170), Animation.create(4174)), MAGIC_MISSILE(682, 15618, Animation.create(4171), Animation.create(4175));

		/**
		 * The graphic id.
		 */
		private final int graphicId;

		/**
		 * The attack name.
		 */
		private final String name;

		/**
		 * The object id to replace with.
		 */
		private final int objectId;

		/**
		 * The successful animation.
		 */
		private final Animation success;

		/**
		 * The animation when failing to defend.
		 */
		private final Animation fail;

		/**
		 * Constructs a new {@code CatapultAttack} {@code Object}.
		 * @param graphicId The graphic id.
		 * @param success The success animation.
		 * @param fail The fail animation.
		 */
		private CatapultAttack(int graphicId, int objectId, Animation success, Animation fail) {
			this.graphicId = graphicId;
			this.name = name().toLowerCase().replace("_", " ");
			this.objectId = objectId;
			this.success = success;
			this.fail = fail;
		}
	}

	/**
	 * The players in the catapult room.
	 */
	private static List<Player> players = new ArrayList<>(20);

	/**
	 * The current attack.
	 */
	private static CatapultAttack attack;

	/**
	 * The pulse.
	 */
	private static Pulse pulse = new Pulse(10) {
		@Override
		public boolean pulse() {
			attack = RandomFunction.getRandomElement(CatapultAttack.values());
			GameWorld.getPulser().submit(new Pulse(7) {
				@Override
				public boolean pulse() {
					for (Player p : players) {
						if (p.isActive() && p.getLocation().equals(TARGET)) {
							if (p.getEquipment().getNew(EquipmentContainer.SLOT_SHIELD).getId() != SHIELD_ID) {
								p.getDialogueInterpreter().sendDialogues(4287, FacialExpression.FURIOUS, "Watch out! You'll need to equip the shield as soon as", "you're on the target spot else you could get hit! Speak", "to me to get one, and make sure both your hands are", "free to equip it.");
								p.getWalkingQueue().reset();
								p.getWalkingQueue().addPath(p.getLocation().getX() + 1, p.getLocation().getY());
								continue;
							}
							p.faceLocation(Location.create(2842, 3554, 1));
							if (p.getAttribute("catapult_def", CatapultAttack.MAGIC_MISSILE) != attack) {
								p.getPacketDispatch().sendMessage("You fail defending against the " + attack.name + ".");
								p.getImpactHandler().manualHit(p, 3, HitsplatType.NORMAL);
								p.animate(attack.fail);
							} else {
								p.getSkills().addExperience(Skills.DEFENCE, 10.0, true);
								p.getPacketDispatch().sendMessage("You successfully defend against the " + attack.name + ".");
								p.getSavedData().getActivityData().updateWarriorTokens(1);
								p.animate(attack.success);
							}
						}
					}
					return true;
				}
			});
			Projectile.create(Location.create(2842, 3554, 1), Location.create(2842, 3545, 1), attack.graphicId, 70, 32, 80, 220, 20, 11).send();
			Scenery object = RegionManager.getObject(Location.create(2840, 3552, 1));
			if(object != null)
				SceneryBuilder.replace(object, object.transform(attack.objectId), 4);
			Audio sound = new Audio(1911);
			for (Player p : players) {
				p.getAudioManager().send(sound);
			}
			return players.isEmpty();
		}
	};

	/**
	 * Constructs a new {@code CatapultRoom} {@code Object}.
	 */
	public CatapultRoom() {
		super("wg catapult", true);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ZoneBuilder.configure(this);
		ClassScanner.definePlugin(new OptionHandler() {
			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				ItemDefinition.forId(SHIELD_ID).getHandlers().put("option:wield", this);
				SceneryDefinition.forId(15657).getHandlers().put("option:view", this);
				return this;
			}

			@Override
			public boolean handle(Player player, Node node, String option) {
				if (node instanceof Item) {
					if (!player.getLocation().equals(TARGET)) {
						player.getPacketDispatch().sendMessage("You may not equip this shield outside the target area in the Warrior's Guild.");
						return true;
					}
					if (player.getEquipment().get(EquipmentContainer.SLOT_WEAPON) != null) {
						player.getDialogueInterpreter().sendDialogue("You will need to make sure your sword hand is free to equip this", "shield.");
						return true;
					}
					ItemDefinition.getOptionHandlers().get("wield").handle(player, node, option);
					if (player.getEquipment().getNew(EquipmentContainer.SLOT_SHIELD).getId() == SHIELD_ID) {
						player.getInterfaceManager().removeTabs(2, 3, 5, 6, 7, 11, 12);
						player.getInterfaceManager().openTab(4, new Component(411));
						player.getInterfaceManager().setViewedTab(4);
					}
					return true;
				}
				player.getInterfaceManager().open(new Component(410));
				return true;
			}
		});
		ClassScanner.definePlugin(new ComponentPlugin() {
			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				ComponentDefinition.put(411, this);
				return this;
			}

			@Override
			public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
				if (button >= 9 && button <= 12) {
					CatapultAttack attack = CatapultAttack.values()[button - 9];
					player.setAttribute("catapult_def", attack);
					player.getConfigManager().set(788, (button - 8) % 4);
					return true;
				}
				return false;
			}

		});
		return this;
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public void configure() {
		super.register(new ZoneBorders(2837, 3542, 2847, 3556));
		pulse.stop();
	}

	@Override
	public boolean enter(Entity e) {
		if (e instanceof Player && e.getLocation().getZ() == 1) {
			players.add((Player) e);
			if (!pulse.isRunning()) {
				pulse.restart();
				pulse.start();
				GameWorld.getPulser().submit(pulse);
			}
		}
		return super.enter(e);
	}

	@Override
	public boolean leave(Entity e, boolean logout) {
		if (e instanceof Player) {
			players.remove(e);
			unequipShield((Player) e);
		}
		return super.leave(e, logout);
	}

	@Override
	public void locationUpdate(Entity e, Location last) {
		if (e instanceof Player && last.equals(TARGET)) {
			unequipShield((Player) e);
		}
	}

	/**
	 * Unequips the shield.
	 * @param player The player.
	 */
	private static void unequipShield(Player player) {
		if (player.getEquipment().getNew(EquipmentContainer.SLOT_SHIELD).getId() == SHIELD_ID) {
			player.getInventory().add(new Item(SHIELD_ID), player);
			player.getEquipment().replace(null, EquipmentContainer.SLOT_SHIELD);
			player.getInterfaceManager().restoreTabs();
			player.getInterfaceManager().openTab(4, new Component(387));
		}
	}
}
