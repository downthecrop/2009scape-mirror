package plugin.skill.hunter;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.game.content.activity.ActivityPlugin;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.member.hunter.falconry.FalconCatch;
import org.crandor.game.content.skill.member.hunter.falconry.FalconryCatchPulse;
import org.crandor.game.interaction.Option;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.HintIconManager;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.zone.ZoneBorders;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.PluginManager;

/**
 * Represents the activity used during falconry practice.
 * @author Vexia
 */
public final class FalconryActivityPlugin extends ActivityPlugin {

	/**
	 * Constructs a new {@code FalconryActivityPlugin} {@code Object}.
	 */
	public FalconryActivityPlugin() {
		this(null);
	}

	/**
	 * Constructs a new {@code FalconryActivityPlugin} {@code Object}.
	 * @param player the player.
	 */
	public FalconryActivityPlugin(final Player player) {
		super("falconry", true, false, false);
		this.player = player;
	}

	@Override
	public ActivityPlugin newInstance(Player p) throws Throwable {
		return new FalconryActivityPlugin(p);
	}

	@Override
	public boolean start(final Player player, boolean login, Object... args) {
		player.setAttribute("/save:falconry", true);
		return super.start(player, login, args);
	}

	@Override
	public boolean leave(final Entity entity, boolean logout) {
		if (!(entity instanceof Player)) {
			return super.leave(entity, logout);
		}
		if (entity instanceof Player) {
			if (!logout) {
				((Player) entity).removeAttribute("falconry");
				removeItems((Player) entity);
			}
		}
		return super.leave(entity, logout);
	}

	/**
	 * Method used to remove the items.
	 */
	public static void removeItems(final Player player) {
		player.getInventory().remove(new Item(10023, player.getInventory().getAmount(new Item(10023))));
		player.getInventory().remove(new Item(10024, player.getInventory().getAmount(new Item(10024))));
		player.getEquipment().remove(new Item(10023));
		player.getEquipment().remove(new Item(10024));
	}

	@Override
	public boolean teleport(final Entity entity, int type, Node node) {
		removeItems((Player) entity);
		return true;
	}

	@Override
	public boolean interact(final Entity e, Node target, Option option) {
		return false;
	}

	@Override
	public Location getSpawnLocation() {
		return null;
	}

	@Override
	public void configure() {
		register(new ZoneBorders(2360, 3571, 2396, 3637));
	}

	@Override
	public void register() {
		PluginManager.definePlugin(new FalconryPlugin());
	}

	/**
	 * Represents the falconry plugin.
	 * @author Vexia
	 */
	public static final class FalconryPlugin extends OptionHandler {

		/**
		 * Represents the bones.
		 */
		private static final Item BONES = new Item(526);

		/**
		 * Represents the falcon item.
		 */
		private static final Item FALCON = new Item(10024);

		/**
		 * Represents the falcon glove.
		 */
		private static final Item GLOVE = new Item(10023);

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			NPCDefinition.forId(5093).getConfigurations().put("option:quick-falconry", this);
			NPCDefinition.forId(5094).getConfigurations().put("option:retrieve", this);
			for (FalconCatch falconCatch : FalconCatch.values()) {
				NPCDefinition.forId(falconCatch.getNpc()).getConfigurations().put("option:catch", this);
			}
			PluginManager.definePlugin(new Plugin<Object>() {

				@Override
				public Plugin<Object> newInstance(Object arg) throws Throwable {
					ItemDefinition.forId(10024).getConfigurations().put("equipment", this);
					ItemDefinition.forId(10023).getConfigurations().put("equipment", this);
					return this;
				}

				@Override
				public Object fireEvent(String identifier, Object... args) {
					final Player player = (Player) args[0];
					switch (identifier) {
					case "unequip":
						if (player.getZoneMonitor().isInZone("falconry")) {
							player.getDialogueInterpreter().sendDialogue("Leave the area in order to remove your falcon.");
							return false;
						}
						break;
					}
					return true;
				}

			});
			return this;
		}

		@Override
		public boolean handle(Player player, Node node, String option) {
			switch (option) {
			case "quick-falconry":
				player.getDialogueInterpreter().open(5093, null, true);
				break;
			case "catch":
				player.face(((NPC) node));
				player.getPulseManager().run(new FalconryCatchPulse(player, ((NPC) node), FalconCatch.forNPC(((NPC) node))));
				break;
			case "retrieve":
				final NPC npc = ((NPC) node);
				if (!npc.getAttribute("falcon:owner", "").equals(player.getUsername())) {
					player.getPacketDispatch().sendMessage("This isn't your falcon.");
					return true;
				}
				if (player.getInventory().freeSlots() == 0) {
					player.getPacketDispatch().sendMessage("You don't have enough inventory space.");
					return true;
				}
				npc.clear();
				HintIconManager.removeHintIcon(player, 1);
				final FalconCatch falconCatch = npc.getAttribute("falcon:catch");
				player.getSkills().addExperience(Skills.HUNTER, falconCatch.getExperience(), true);
				player.getPacketDispatch().sendMessage("You retrieve the falcon as well as the fur of the dead kebbit.");
				player.getInventory().add(falconCatch.getItem());
				player.getInventory().add(BONES);
				if (player.getEquipment().remove(GLOVE)) {
					player.getEquipment().add(FALCON, true, false);
				} else {
					player.getInventory().remove(GLOVE);
					player.getInventory().add(FALCON);
				}
				break;
			}
			return true;
		}

		@Override
		public boolean isWalk() {
			return false;
		}

		@Override
		public boolean isWalk(final Player player, Node node) {
			if (node instanceof NPC) {
				final NPC n = ((NPC) node);
				if (n.getId() == 5093 || n.getId() == 5094) {
					return true;
				}
			}
			return false;
		}

	}

}
