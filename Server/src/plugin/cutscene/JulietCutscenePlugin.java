package plugin.cutscene;

import org.crandor.game.content.activity.ActivityPlugin;
import org.crandor.game.content.activity.CutscenePlugin;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.node.object.ObjectBuilder;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.map.build.DynamicRegion;
import org.crandor.net.packet.PacketRepository;
import org.crandor.net.packet.context.CameraContext;
import org.crandor.net.packet.context.CameraContext.CameraType;
import org.crandor.net.packet.out.CameraViewPacket;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.PluginManager;

/**
 * Represents the romeo and juliet cutscene plugin.
 * 
 * @author 'Vexia
 * @date 31/12/2013
 */
@InitializablePlugin
public final class JulietCutscenePlugin extends CutscenePlugin {

	/**
	 * Represents the location the player will return to.
	 */
	private static final Location SPAWN_LOCATION = Location.create(3166, 3431, 1);

	/**
	 * Constructs a new {@code JulietCutscenePlugin} {@code Object}.
	 */
	public JulietCutscenePlugin() {
		this(null);
		PluginManager.definePlugin(new JulietDialogue());
	}

	/**
	 * Constructs a new {@code JulietCutscenePlugin} {@code Object}.
	 */
	public JulietCutscenePlugin(final Player player) {
		super("Juliet Cutscene");
		this.player = player;
	}

	@Override
	public boolean start(final Player player, final boolean login, Object... args) {
		NPC juliet = NPC.create(637, base.transform(29, 39, 1));
		juliet.setWalks(false);
		npcs.add(juliet);
		npcs.add(NPC.create(3325, base.transform(29, 38, 1)));
		npcs.add(NPC.create(3324, base.transform(26, 41, 1)));
		final GameObject door = RegionManager.getObject(getBase().transform(29, 41, 1));
		ObjectBuilder.remove(door);
		for (NPC npc : npcs) {
			npc.init();
		}
		return super.start(player, login, args);
	}

	@Override
	public void open() {
		int x = player.getLocation().getX();
		int y = player.getLocation().getY();
		CameraContext rot = null;
		CameraContext pos = null;
		int height = 390;
		int speed = 100;
		int other = 1;
		pos = new CameraContext(player, CameraType.POSITION, x, y - 4, height, other, speed);
		rot = new CameraContext(player, CameraType.ROTATION, x, y - 4, height, other, speed);
		PacketRepository.send(CameraViewPacket.class, pos);
		PacketRepository.send(CameraViewPacket.class, rot);
		for (NPC npc : npcs) {
			npc.face(player);
		}
		npcs.get(1).face(npcs.get(0));
		player.face(npcs.get(0));
		player.getDialogueInterpreter().open(npcs.get(0).getId(), npcs.get(0), this);
		player.lock();
		player.getLocks().lockMovement(1000000);
	}

	@Override
	public void configure() {
		region = DynamicRegion.create(12597);
		setRegionBase();
		registerRegion(region.getId());
	}

	@Override
	public Location getStartLocation() {
		return getBase().transform(30, 39, 1);
	}

	@Override
	public ActivityPlugin newInstance(Player p) throws Throwable {
		return new JulietCutscenePlugin(p);
	}

	@Override
	public Location getSpawnLocation() {
		return SPAWN_LOCATION;
	}

	/**
	 * Gets the phillipia npc.
	 * 
	 * @return the npc.
	 */
	public NPC getPhillipia() {
		return npcs.get(1);
	}

}
