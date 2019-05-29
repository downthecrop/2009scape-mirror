package plugin.activity.pyramidplunder;

import org.crandor.game.component.Component;
import org.crandor.game.content.activity.ActivityPlugin;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.PluginManager;

/**
 * Handles the Pyramid plunder activity.
 * @author Emperor
 */
@InitializablePlugin
public final class PyramidPlunderActivity extends ActivityPlugin {

	/**
	 * The overlay.
	 */
	private static final Component OVERLAY = new Component(428);

	/**
	 * The room locations.
	 */
	public static Location[] ROOM_LOCATIONS = { Location.create(1927, 4477, 0) };

	/**
	 * The mummy NPC.
	 */
	private static NPC mummy;

	/**
	 * Constructs a new {@code PyramidPlunderActivity} {@code Object}.
	 */
	public PyramidPlunderActivity() {
		super("Pyramid plunder", false, true, false);
	}

	@Override
	public boolean start(Player player, boolean login, Object... args) {
		enterRoom(player, 0);
		return true;
	}

	@Override
	public boolean enter(Entity e) {
		if (e instanceof Player && e.getLocation().getZ() == 0) {
			((Player) e).getInterfaceManager().openOverlay(OVERLAY);
		}
		return super.enter(e);
	}

	public void sendRoomConfiguration(Player player, int room, int level) {
		player.getConfigManager().set(822, level | (room << 9));
		player.getConfigManager().set(821, level | (room << 9));
	}

	@Override
	public boolean leave(Entity e, boolean logout) {
		if (e instanceof Player) {
			((Player) e).getInterfaceManager().closeOverlay();
		}
		return super.leave(e, logout);
	}

	@Override
	public ActivityPlugin newInstance(Player p) throws Throwable {
		return this;
	}

	@Override
	public void register() {
		PluginManager.definePlugin(new GuardMummyDialogue());
		PluginManager.definePlugin(new PyramidOptionHandler());
		registerRegion(7749);
		mummy = NPC.create(4476, Location.create(1968, 4427, 2));
		mummy.init();
	}

	@Override
	public Location getSpawnLocation() {
		return null;
	}

	@Override
	public void configure() {
	}

	/**
	 * Enters a room.
	 * @param player The player.
	 * @param index The index.
	 */
	public void enterRoom(Player player, int index) {
		player.lock(1);
		player.getProperties().setTeleportLocation(ROOM_LOCATIONS[index]);
	}

}
