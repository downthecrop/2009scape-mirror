package plugin.zone.phasmatys;

import org.crandor.game.content.global.Bones;
import org.crandor.game.content.global.action.ClimbActionHandler;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.member.agility.AgilityHandler;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.Option;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.node.object.ObjectBuilder;
import org.crandor.game.world.map.Direction;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.zone.MapZone;
import org.crandor.game.world.map.zone.ZoneBorders;
import org.crandor.game.world.map.zone.ZoneBuilder;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.PluginManager;

/**
 * Handles the phasmatyz zone area.
 * @author Vexia
 */
@InitializablePlugin
public final class PhasmatysZone extends MapZone implements Plugin<Object> {

	/**
	 * Constructs a new {@code PhasmatysZone} {@code Object}.
	 */
	public PhasmatysZone() {
		super("Port phasmatys", true);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ZoneBuilder.configure(this);
		PluginManager.definePlugin(new GravingasNPC());
		PluginManager.definePlugin(new BoneLoaderHandler());
		PluginManager.definePlugin(new NecrovarusDialogue());
		PluginManager.definePlugin(new GhostSailorDialogue());
		PluginManager.definePlugin(new EctoplasmFillPlugin());
		PluginManager.definePlugin(new GhostDiscipleDialogue());
		PluginManager.definePlugin(new GhostVillagerDialogue());
		PluginManager.definePlugin(new GhostInkeeperDialogue());
		return this;
	}


	@Override
	public boolean interact(Entity e, Node target, Option option) {
		if (e.isPlayer()) {
			Player player = e.asPlayer();
			if (target instanceof NPC) {
				NPC npc = (NPC) target;
				if ((npc.getName().toLowerCase().contains("ghost") || npc.getName().equalsIgnoreCase("velorina") || npc.getName().contains("husband")) && !hasAmulet(player)) {
					player.getDialogueInterpreter().open("ghosty dialogue", target);
					return true;
				}
			}
			switch (target.getId()) {
			case 5259:
				handleEnergyBarrier(player, (GameObject) target);
				return true;
			case 5267:
				player.animate(Animation.create(536));
				player.getPacketDispatch().sendMessage("The trapdoor opens...");
				ObjectBuilder.replace((GameObject) target, ((GameObject) target).transform(5268));
				return true;
			case 5268:
				if (option.getName().equals("Close")) {
					player.animate(Animation.create(535));
					player.getPacketDispatch().sendMessage("You close the trapdoor.");
					ObjectBuilder.replace((GameObject) target, ((GameObject) target).transform(5267));
				} else {
					player.getPacketDispatch().sendMessage("You climb down through the trapdoor...");
					player.getProperties().setTeleportLocation(Location.create(3669, 9888, 3));
				}
				return true;
			case 9308:
				if (player.getSkills().getStaticLevel(Skills.AGILITY) < 58) {
					player.sendMessage("You need an agility level of at least 58 to climb down this wall.");
					return true;
				}
				player.getProperties().setTeleportLocation(Location.create(3671, 9888, 2));
				return true;
			case 9307:
				if (player.getSkills().getStaticLevel(Skills.AGILITY) < 58) {
					player.sendMessage("You need an agility level of at least 58 to climb up this wall.");
					return true;
				}
				player.getProperties().setTeleportLocation(Location.create(3670, 9888, 3));
				return true;
			case 5264:
				ClimbActionHandler.climb(player, Animation.create(828), Location.create(3654, 3519, 0));
				return true;
			case 5244:
				player.getDialogueInterpreter().open(1686, null, true);
				return true;
			case 11163:
			case 11164:
				GameObject obj = (GameObject) target;
				switch (option.getName().toLowerCase()) {
				case "wind":
					wind(player, obj);
					return true;
				case "empty":
					empty(player, obj);
					return true;
				case "status":
					checkStatus(player, obj);
					return true;
				}
				break;
			case 5282:
				worship(player);
				return true;
			}
		}
		return super.interact(e, target, option);
	}

	/**
	 * Checks if the player has the amulet equipped.
	 * @param player the player.
	 * @return {@code True} if so.
	 */
	public static boolean hasAmulet(Player player) {
		return player.getEquipment().contains(552, 1);
	}
	
	/**
	 * Checks the status of a grinder.
	 * @param player the player.
	 * @param object the object.
	 */
	private void checkStatus(Player player, GameObject object) {
		String status = "The grinder is empty.";
		if (hasBones(player, object, true)) {
			status = "There are some bones in the hopper.";
		} else if (hasBones(player, object, false)) {
			status = "There are some crushed bones in the bin.";
		}
		player.sendMessage(status);
	}

	/**
	 * Emptys the grinder.
	 * @param player the player.
	 * @param object the object.
	 */
	private void empty(Player player, GameObject object) {
		if (hasBones(player, object, true)) {
			player.sendMessage("You need to wind the handle to grind the bones.");
			return;
		}
		if (!hasBones(player, object, false)) {
			player.sendMessage("The grinder is already empty.");
			return;
		}
		if (!player.getInventory().contains(1931, 1)) {
			player.sendMessage("You need an empty pot to put the crushed bones into.");
			return;
		}
		final Bones bone = Bones.forConfigValue(player.getConfigManager().get(408), false);
		player.lock(2);
		player.animate(Animation.create(1650));
		player.getConfigManager().set(408, 0, true);
		player.sendMessage("You fill a pot with crushed bones.");
		player.getInventory().replace(bone.getBoneMeal(), player.getInventory().getSlot(new Item(1931)));
	}

	/**
	 * Windws the grinder.
	 * @param player the player.
	 * @param object the object.
	 */
	private void wind(Player player, GameObject object) {
		final Bones bone = Bones.forConfigValue(player.getConfigManager().get(408), true);
		player.lock(3);
		player.animate(Animation.create(1648));
		player.sendMessage("You wind the grinder handle.");
		if (hasBones(player, object, true)) {
			player.getConfigManager().set(408, bone.getConfigValue(false), true);
			player.sendMessage("Some crushed bones pour into the bin.", 3);
		}
	}

	/**
	 * Checks if bones are in the hopper.
	 * @param player the player.
	 * @param if in the hopper or in the bin.
	 * @param object the object.
	 * @return {@code True} if so.
	 */
	public static boolean hasBones(Player player, GameObject object, boolean inHopper) {
		int value = player.getConfigManager().get(408);
		for (Bones bone : Bones.values()) {
			if (bone.getConfigValue(inHopper) == value) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Worships the ectofuntus.
	 * @param player the player.
	 */
	private void worship(Player player) {
		Bones bone = null;
		for (Item i : player.getInventory().toArray()) {
			if (i == null) {
				continue;
			}
			Bones b = Bones.forBoneMeal(i.getId());
			if (b != null) {
				bone = b;
				break;
			}
		}
		if (!player.getInventory().contains(4286, 1) && bone == null) {
			player.getDialogueInterpreter().sendDialogue("You don't have any ectoplasm or crushed bones to put into the", "Ectofuntus.");
			return;
		}
		if (bone == null) {
			player.getDialogueInterpreter().sendDialogue("You need a pot of crushed bones to put into the Ectofuntus.");
			return;
		}
		if (!player.getInventory().contains(4286, 1)) {
			player.getDialogueInterpreter().sendDialogue("You need ectoplasm to put into the Ectofuntus.");
			return;
		}
		if (player.getInventory().remove(bone.getBoneMeal(), new Item(4286, 1))) {
			player.lock(5);
			player.animate(Animation.create(1651));
			player.getInventory().add(new Item(1925), new Item(1931));
			player.getSkills().addExperience(Skills.PRAYER, bone.getExperience() * 4, true);
			player.sendMessage("You put some ectoplasm and bonemeal into the Ectofuntus, and worship it.");
			player.getSavedData().getGlobalData().setEctoCharges(player.getSavedData().getGlobalData().getEctoCharges() + 1);
		}
	}

	/**
	 * Handles the passing of the energy barrier.
	 * @param player the player.
	 * @param object the object.
	 */
	private void handleEnergyBarrier(Player player, GameObject object) {
		boolean force = object.getLocation().equals(3659, 3508, 0) && player.getLocation().getY() < 3508 || object.getLocation().equals(3652, 3485, 0) && player.getLocation().getX() > 3652;
		if (!force && !player.getInventory().contains(4278, 2)) {
			player.getDialogueInterpreter().open(1706);
			return;
		}
		if (force || player.getInventory().remove(new Item(4278, 2))) {
			final Direction direction = Direction.getLogicalDirection(player.getLocation(), object.getLocation());
			Location end = player.getLocation().transform(direction, 2);
			if (player.getLocation().getY() >= 3508) {
				end = object.getLocation().transform(0, -2, 0);
			}
			if (object.getLocation().equals(new Location(3652, 3485, 0))) {
				end = object.getLocation().transform(player.getLocation().getX() >= 3653 ? -1 : 2, 0, 0);
			}
			AgilityHandler.walk(player, -1, player.getLocation(), end, null, 0.0, null);
		}
	}

	/**
	 * Handles the bone loader.
	 * @author Vexia
	 */
	public static final class BoneLoaderHandler extends UseWithHandler {

		/**
		 * Constructs a new {@code BoneLoaderHandler} {@code Object}.
		 */
		public BoneLoaderHandler() {
			super(Bones.getArray());
		}

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			addHandler(11162, OBJECT_TYPE, this);
			return this;
		}

		@Override
		public boolean handle(NodeUsageEvent event) {
			final Player player = event.getPlayer();
			final GameObject object = (GameObject) event.getUsedWith();
			final Bones bone = Bones.forId(event.getUsedItem().getId());
			if (hasBones(player, object, true)) {
				player.sendMessage("There are already some bones in the grinder's hopper.");
				return true;
			} else if (hasBones(player, object, false)) {
				player.sendMessage("You need to empty the grinder's bin before you put more bones in it.");
				return true;
			}
			if (player.getInventory().remove(event.getUsedItem())) {
				player.lock(2);
				player.animate(Animation.create(1649));
				player.getConfigManager().set(408, bone.getConfigValue(true), true);
				player.sendMessage("You put some bones in the grinder's hopper.", 1);
			}
			return true;
		}

	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	@Override
	public void configure() {
		register(new ZoneBorders(3583, 3456, 3701, 3552));
		register(new ZoneBorders(3667, 9873, 3699, 9914));
	}

}
