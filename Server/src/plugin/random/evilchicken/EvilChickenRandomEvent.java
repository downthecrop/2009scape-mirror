package plugin.random.evilchicken;

import java.nio.ByteBuffer;

import org.crandor.game.content.ame.AntiMacroEvent;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.HintIconManager;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.PluginManager;

/**
 * Handles the evil chicken random event.
 * @author Emperor
 */
@InitializablePlugin
public final class EvilChickenRandomEvent extends AntiMacroEvent {

	/**
	 * Constructs a new {@code EvilChickenRandomEvent} {@code Object}.
	 */
	public EvilChickenRandomEvent() {
		super("chicken", true, false);
	}

	@Override
	public boolean start(Player player, boolean login, Object... args) {
		super.init(player);
		int level = player.getProperties().getCurrentCombatLevel();
		int npcId = 2468;
		if (level < 11) {
			npcId = 2463;
		} else if (level < 21) {
			npcId = 2464;
		} else if (level < 41) {
			npcId = 2465;
		} else if (level < 61) {
			npcId = 2466;
		} else if (level < 91) {
			npcId = 2467;
		}
		Location location = Location.getRandomLocation(player.getLocation(), 6, true);
		EvilChickenNPC npc = new EvilChickenNPC(npcId, location);
		npc.player = player;
		npc.event = this;
		npc.init();
		if (location == player.getLocation()) {
			npc.moveStep();
		}
		HintIconManager.registerHintIcon(player, npc);
		return true;
	}

	@Override
	public void terminate() {
		super.terminate();
		if (player != null) {
			player.getHintIconManager().clear();
		}
	}

	@Override
	public AntiMacroEvent create(Player player) {
		EvilChickenRandomEvent event = new EvilChickenRandomEvent();
		event.player = player;
		return event;
	}

	@Override
	public Location getSpawnLocation() {
		return null;
	}

	@Override
	public void configure() {
		PluginManager.definePlugin(new EvilChickenNPC());
	}

	@Override
	public void save(ByteBuffer buffer) {

	}

	@Override
	public void parse(ByteBuffer buffer) {

	}

}
