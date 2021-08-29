package core.game.node.entity.skill.hunter;

import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the net trap.
 * @author Vexia
 */
public final class NetTrapSetting extends TrapSetting {

	/**
	 * Constructs a new {@code NetTrapSetting} {@code Object}.
	 */
	public NetTrapSetting() {
		super(new int[] { 19652, 19663, 19671, 19679, 28564 }, new Item[] { new Item(303), new Item(954) }, NetTrap.getIds(), new int[] { 10142, 10143, 10144, 10145 }, "set-trap", 29, -1, new Animation(5215), Animation.create(5207), true);
	}

	@Override
	public boolean hasItems(Player player) {
		if (!super.hasItems(player)) {
			player.sendMessage("You need a net and a rope to set a net trap.");
			return false;
		}
		return true;
	}

	@Override
	public boolean clear(TrapWrapper wrapper, int type) {
		if (super.clear(wrapper, type)) {
			if (wrapper.getSecondary() != null && wrapper.getSecondary().isActive()) {
				SceneryBuilder.remove(wrapper.getSecondary());
			}
			SceneryBuilder.add(wrapper.getObject().transform(wrapper.getNetType().getOriginal()));
			return true;
		}
		return false;
	}

	@Override
	public void returnItems(Scenery object, TrapWrapper wrapper, int type) {
		super.returnItems(object, wrapper, type);
		if (type == 0) {
			for (Item i : super.getItems()) {
				createGroundItem(i, object.getLocation(), wrapper.getPlayer());
			}
		}
	}

	@Override
	public void reward(Player player, Node node, TrapWrapper wrapper) {
		Scenery object = wrapper.getObject();
		wrapper.setNetType(NetTrap.forId(node.getId()));
		int rotation = 0;
		int increment = 0;
		boolean x = false;
		Object[] netInfo = getNetInfo(player, node);
		rotation = (int) netInfo[0];
		increment = (int) netInfo[1];
		x = (boolean) netInfo[2];
		Scenery secondary = new Scenery(wrapper.getNetType().getNet(), object.getLocation().transform(x ? increment : 0, !x ? increment : 0, 0), rotation);
		secondary = SceneryBuilder.add(secondary);
		wrapper.setSecondary(secondary);
		player.moveStep();
		wrapper.addItem(getItems());
		player.getInventory().remove(wrapper.getType().getSettings().getItems());
	}

	@Override
	public void handleCatch(int counter, TrapWrapper wrapper, TrapNode node, NPC npc, boolean success) {
		switch (counter) {
		case 2:
			SceneryBuilder.remove(wrapper.getSecondary());
			break;
		case 3:
			npc.moveStep();
			wrapper.setObject(wrapper.getNetType().getFailed());
			break;
		}
	}

	@Override
	public Scenery buildObject(Player player, Node node) {
		return ((Scenery) node).transform(NetTrap.forId(node.getId()).getBent());
	}

	@Override
	public TrapHook createHook(TrapWrapper wrapper) {
		return new TrapHook(wrapper, new Location[] { wrapper.getSecondary().getLocation() });
	}

	@Override
	public int getTransformId(TrapWrapper wrapper, TrapNode node) {
		return wrapper.getNetType().getCatching();
	}

	@Override
	public int getFinalId(TrapWrapper wrapper, TrapNode node) {
		return wrapper.getNetType().getCaught();
	}

	@Override
	public int getFailId(TrapWrapper wrapper, TrapNode node) {
		return wrapper.getNetType().getFailing();
	}

	@Override
	public String getTimeUpMessage() {
		return "The net trap that you constructed has collapsed.";
	}

	/**
	 * Gets the net info.
	 * @param player the player.
	 * @param node the node.
	 * @return the data.
	 */
	private Object[] getNetInfo(Player player, Node node) {
		int rotation;
		int increment;
		boolean x = false;
		if (player.getLocation().getX() < node.getLocation().getX()) {
			rotation = 3;
			increment = -1;
			x = true;
		} else if (player.getLocation().getX() > node.getLocation().getX()) {
			rotation = 1;
			increment = 1;
			x = true;
		} else if (player.getLocation().getY() < node.getLocation().getY()) {
			rotation = 2;
			increment = -1;
		} else {
			rotation = 0;
			increment = 1;
		}
		return new Object[] { rotation, increment, x };
	}

	/**
	 * Represents a net trap.
	 * @author Vexia
	 */
	public enum NetTrap {
		GREEN(19679, 19678, 19676, 19677, 19674, 19675, 19651), SQUIREL(28564, 28563, 28752, 28753, 28750, 28751, 28566), ORANGE(19652, 19650, 19657, 19656, 19655, 19654, 19665), RED(19663, 19662, 19660, 19661, 19658, 19659, 19673), BLACK(19671, 19670, 19668, 19669, 19666, 19667, 19681);

		/**
		 * Represents the original object id.
		 */
		private final int original;

		/**
		 * Represents the bend object id.
		 */
		private final int bent;

		/**
		 * Represents the failing object id.
		 */
		private final int failing;

		/**
		 * Represents the failed object id.
		 */
		private final int failed;

		/**
		 * Represents the catching object id.
		 */
		private final int catching;

		/**
		 * Represents the caught object id.
		 */
		private final int caught;

		/**
		 * Represents the net.
		 */
		private final int net;

		/**
		 * Constructs a new {@code StationaryCatch.java} {@code Object}.
		 * @param original the original.
		 * @param bent the bent.
		 * @param failing the failing.
		 * @param failed the failed.
		 * @param catching the catching.
		 * @param caught the caught.
		 * @param net the net.
		 */
		NetTrap(int original, int bent, int failing, int failed, int catching, int caught, int net) {
			this.original = original;
			this.bent = bent;
			this.failing = failing;
			this.failed = failed;
			this.catching = catching;
			this.caught = caught;
			this.net = net;
		}

		/**
		 * Gets the net trap for the id.
		 * @param id the id.
		 * @return the trap.
		 */
		public static NetTrap forId(int id) {
			for (NetTrap trap : values()) {
				if (trap.getOriginal() == id) {
					return trap;
				}
			}
			return null;
		}

		/**
		 * Gets the id.
		 * @return the array.
		 */
		public static int[] getIds() {
			List<Integer> ids = new ArrayList<>(10);
			for (NetTrap trap : NetTrap.values()) {
				ids.add(trap.getBent());
				ids.add(trap.getCaught());
				ids.add(trap.getNet());
				ids.add(trap.getOriginal());
			}
			int[] array = new int[ids.size()];
			for (int i = 0; i < array.length; i++) {
				array[i] = ids.get(i);
			}
			return array;
		}

		/**
		 * Gets the original.
		 * @return The original.
		 */
		public int getOriginal() {
			return original;
		}

		/**
		 * Gets the bent.
		 * @return The bent.
		 */
		public int getBent() {
			return bent;
		}

		/**
		 * Gets the failed.
		 * @return The failed.
		 */
		public int getFailed() {
			return failed;
		}

		/**
		 * Gets the catching.
		 * @return The catching.
		 */
		public int getCatching() {
			return catching;
		}

		/**
		 * Gets the caught.
		 * @return The caught.
		 */
		public int getCaught() {
			return caught;
		}

		/**
		 * Gets the failing.
		 * @return The failing.
		 */
		public int getFailing() {
			return failing;
		}

		/**
		 * Gets the net.
		 * @return The net.
		 */
		public int getNet() {
			return net;
		}

	}
}
