package core.game.node.entity.skill.hunter;

import core.cache.def.Definition;
import core.cache.def.impl.ItemDefinition;
import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.plugin.Initializable;
import core.game.node.entity.skill.hunter.NetTrapSetting.NetTrap;
import core.game.node.entity.skill.hunter.bnet.BNetNode;
import core.game.node.entity.skill.hunter.bnet.BNetTypes;
import core.game.node.entity.skill.hunter.bnet.ImplingNode;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.OptionHandler;
import core.game.interaction.UseWithHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.GroundItem;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the hunter skill.
 * @author Vexia
 */
@Initializable
public final class HunterPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		Definition<?> definition = null;
		for (Traps trap : Traps.values()) {
			for (int nodeId : trap.getSettings().getNodeIds()) {
				definition = trap.getSettings().isObjectTrap() ? SceneryDefinition.forId(nodeId) : ItemDefinition.forId(nodeId);
				definition.getHandlers().put("option:" + trap.getSettings().getOption(), this);
			}
			if (trap.getSettings().getFailId() != -1) {
				definition = SceneryDefinition.forId(trap.getSettings().getFailId());
				definition.getHandlers().put("option:dismantle", this);
				definition.getHandlers().put("option:deactivate", this);
			}
			for (int objectId : trap.getSettings().getObjectIds()) {
				definition = SceneryDefinition.forId(objectId);
				definition.getHandlers().put("option:deactivate", this);
				definition.getHandlers().put("option:dismantle", this);
				definition.getHandlers().put("option:investigate", this);
			}
			for (TrapNode node : trap.getNodes()) {
				for (int objectId : node.getObjectIds()) {
					definition = SceneryDefinition.forId(objectId);
					definition.getHandlers().put("option:check", this);
					definition.getHandlers().put("option:retrieve", this);
				}
			}
		}
		for (NetTrap trap : NetTrap.values()) {
			SceneryDefinition.forId(trap.getBent()).getHandlers().put("option:dismantle", this);
			SceneryDefinition.forId(trap.getFailed()).getHandlers().put("option:dismantle", this);
			SceneryDefinition.forId(trap.getNet()).getHandlers().put("option:dismantle", this);
			SceneryDefinition.forId(trap.getCaught()).getHandlers().put("option:check", this);
			SceneryDefinition.forId(trap.getBent()).getHandlers().put("option:investigate", this);
			SceneryDefinition.forId(trap.getFailed()).getHandlers().put("option:investigate", this);
			SceneryDefinition.forId(trap.getNet()).getHandlers().put("option:investigate", this);
		}
		ClassScanner.definePlugin(new HunterNPC());
		ClassScanner.definePlugin(new HunterNetPlugin());
		ClassScanner.definePlugin(new HunterItemPlugin());
		ClassScanner.definePlugin(new FalconryActivityPlugin());
		ClassScanner.definePlugin(new HuntingItemUseWithHandler());
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
			trap.dismantle(player, (Scenery) node);
			return true;
		case "investigate":
			trap.investigate(player, (Scenery) node);
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
			final Scenery object = event.getUsedWith() instanceof Scenery ? (Scenery) event.getUsedWith() : (Scenery) event.getUsed();
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
			List<Integer> list = new ArrayList<>(10);
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
				BNetTypes.values()[i].getNode().getReward().getDefinition().getHandlers().put("option:loot", this);
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
					NPCDefinition.forId(id).getHandlers().put("option:catch", this);
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
