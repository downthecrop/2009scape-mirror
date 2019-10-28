package plugin.skill.hunter;

import org.crandor.cache.def.Definition;
import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.skill.member.hunter.NetTrapSetting.NetTrap;
import org.crandor.game.content.skill.member.hunter.TrapNode;
import org.crandor.game.content.skill.member.hunter.TrapWrapper;
import org.crandor.game.content.skill.member.hunter.Traps;
import org.crandor.game.content.skill.member.hunter.bnet.BNetNode;
import org.crandor.game.content.skill.member.hunter.bnet.BNetTypes;
import org.crandor.game.content.skill.member.hunter.bnet.ImplingNode;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.GroundItem;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the hunter skill.
 * @author Vexia
 */
@InitializablePlugin
public final class HunterPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		Definition<?> definition = null;
		for (Traps trap : Traps.values()) {
			for (int nodeId : trap.getSettings().getNodeIds()) {
				definition = trap.getSettings().isObjectTrap() ? ObjectDefinition.forId(nodeId) : ItemDefinition.forId(nodeId);
				definition.getConfigurations().put("option:" + trap.getSettings().getOption(), this);
			}
			if (trap.getSettings().getFailId() != -1) {
				definition = ObjectDefinition.forId(trap.getSettings().getFailId());
				definition.getConfigurations().put("option:dismantle", this);
				definition.getConfigurations().put("option:deactivate", this);
			}
			for (int objectId : trap.getSettings().getObjectIds()) {
				definition = ObjectDefinition.forId(objectId);
				definition.getConfigurations().put("option:deactivate", this);
				definition.getConfigurations().put("option:dismantle", this);
				definition.getConfigurations().put("option:investigate", this);
			}
			for (TrapNode node : trap.getNodes()) {
				for (int objectId : node.getObjectIds()) {
					definition = ObjectDefinition.forId(objectId);
					definition.getConfigurations().put("option:check", this);
					definition.getConfigurations().put("option:retrieve", this);
				}
			}
		}
		for (NetTrap trap : NetTrap.values()) {
			ObjectDefinition.forId(trap.getBent()).getConfigurations().put("option:dismantle", this);
			ObjectDefinition.forId(trap.getFailed()).getConfigurations().put("option:dismantle", this);
			ObjectDefinition.forId(trap.getNet()).getConfigurations().put("option:dismantle", this);
			ObjectDefinition.forId(trap.getCaught()).getConfigurations().put("option:check", this);
			ObjectDefinition.forId(trap.getBent()).getConfigurations().put("option:investigate", this);
			ObjectDefinition.forId(trap.getFailed()).getConfigurations().put("option:investigate", this);
			ObjectDefinition.forId(trap.getNet()).getConfigurations().put("option:investigate", this);
		}
		PluginManager.definePlugin(new HunterNPC());
		PluginManager.definePlugin(new HunterNetPlugin());
		PluginManager.definePlugin(new HunterItemPlugin());
		PluginManager.definePlugin(new FalconryActivityPlugin());
		PluginManager.definePlugin(new HuntingItemUseWithHandler());
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final Traps trap = Traps.forNode(node);
		switch (option) {
		case "lay":
		case "activate":
		case "set-trap":
		case "trap":
			trap.create(player, node);
			return true;
		case "dismantle":
		case "deactivate":
		case "retrieve":
		case "check":
			trap.dismantle(player, (GameObject) node);
			return true;
		case "investigate":
			trap.investigate(player, (GameObject) node);
			return true;
		}
		return true;
	}

	@Override
	public Location getDestination(Node node, Node n) {
		if (n.getName().startsWith("Bird")) {
			if (node.getLocation().equals(n.getLocation())) {
				return n.getLocation().transform(node.getDirection(), 1);
			}
		}
		return null;
	}

	@Override
	public boolean isWalk(Player player, Node node) {
		return node instanceof GroundItem || !(node instanceof Item);
	}

	@Override
	public boolean isWalk() {
		return false;
	}

	/**
	 * Handles the usage of an item on a trap.
	 * @author Vexia
	 */
	public final static class HuntingItemUseWithHandler extends UseWithHandler {

		/**
		 * Constructs a new {@code HuntingItemUseWithHandler} {@code Object}.
		 */
		public HuntingItemUseWithHandler() {
			super(getIds());
		}

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			for (Traps trap : Traps.values()) {
				for (int objectId : trap.getSettings().getObjectIds()) {
					addHandler(objectId, OBJECT_TYPE, this);
				}
			}
			return this;
		}

		@Override
		public boolean handle(NodeUsageEvent event) {
			final Player player = event.getPlayer();
			final GameObject object = event.getUsedWith() instanceof GameObject ? (GameObject) event.getUsedWith() : (GameObject) event.getUsed();
			final Item item = event.getUsedItem();
			if (!player.getHunterManager().isOwner(object)) {
				player.sendMessage("This isn't your trap!");
				return true;
			}
			final TrapWrapper wrapper = player.getHunterManager().getWrapper(object);
			if (item.getId() == 594) {
				wrapper.smoke();
			} else {
				wrapper.bait(item);
			}
			return true;
		}

		/**
		 * Gets the ids to be used.
		 * @return the ids.
		 */
		public static int[] getIds() {
			List<Integer> list = new ArrayList<>();
			for (Traps trap : Traps.values()) {
				for (int id : trap.getSettings().getBaitIds()) {
					list.add(id);
				}
			}
			list.add(594);
			int[] array = new int[list.size()];
			for (int i = 0; i < array.length; i++) {
				array[i] = list.get(i);
			}
			return array;
		}

	}

	/**
	 * Handles a hunter item plugin.
	 * @author Vexia
	 */
	public static final class HunterItemPlugin extends OptionHandler {

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			ItemDefinition.setOptionHandler("release", this);
			for (int i = BNetTypes.BABY_IMPLING.ordinal() - 1; i < BNetTypes.values().length; i++) {
				BNetTypes.values()[i].getNode().getReward().getDefinition().getConfigurations().put("option:loot", this);
			}
			return this;
		}

		@Override
		public boolean handle(Player player, Node node, String option) {
			switch (option) {
			case "release":
				ReleaseType type = ReleaseType.forId(node.getId());
				if (type != null) {
					type.release(player, (Item) node);
				}
				break;
			case "loot":
				((ImplingNode) BNetTypes.forItem((Item) node)).loot(player, (Item) node);
				break;
			}
			return true;
		}

		@Override
		public boolean isWalk() {
			return false;
		}

		/**
		 * A release type.
		 * @author Vexia
		 */
		public enum ReleaseType {
			TRAPS(10033, 10034, 10092, 10146, 10147, 10148, 10149), BUTTERFLY(10020, 10018, 10016, 10014) {
				@Override
				public void release(final Player player, final Item item) {
					BNetNode node = BNetTypes.forItem(item);
					if (player.getInventory().remove(item)) {
						player.animate(Animation.create(5213));
						player.getInventory().add(new Item(10012));
						player.graphics(node.getGraphics()[1]);
					}
				}
			};

			/**
			 * The ids of the item.
			 */
			private final int[] ids;

			/**
			 * Constructs a new {@code ReleaseType} {@code Object}.
			 * @param ids the ids.
			 */
			private ReleaseType(int... ids) {
				this.ids = ids;
			}

			/**
			 * Releases an item.
			 * @param player the player.
			 * @param item the item.
			 */
			public void release(final Player player, final Item item) {
				boolean multiple = item.getAmount() > 1;
				player.getInventory().remove(item);
				player.sendMessage("You release the " + item.getName().toLowerCase() + (multiple ? "s" : "") + " and " + (multiple ? "they" : "it") + " bound" + (!multiple ? "s" : "") + " away.");
			}

			/**
			 * Handles a release type.
			 * @param id the id.
			 * @return the type.
			 */
			public static ReleaseType forId(int id) {
				for (ReleaseType type : values()) {
					for (int i : type.getIds()) {
						if (i == id) {
							return type;
						}
					}
				}
				return null;
			}

			/**
			 * Gets the ids.
			 * @return The ids.
			 */
			public int[] getIds() {
				return ids;
			}
		}
	}

	/**
	 * Handles the catching of a hunter npc with a net.
	 * @author Vexia
	 */
	public static final class HunterNetPlugin extends OptionHandler {

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			for (BNetTypes type : BNetTypes.values()) {
				for (int id : type.getNode().getNpcs()) {
					NPCDefinition.forId(id).getConfigurations().put("option:catch", this);
				}
			}
			return this;
		}

		@Override
		public boolean handle(Player player, Node node, String option) {
			BNetTypes.forNpc((NPC) node).handle(player, (NPC) node);
			return true;
		}

	}
}
