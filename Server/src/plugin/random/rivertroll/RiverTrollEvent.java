package plugin.random.rivertroll;

import java.nio.ByteBuffer;

import org.crandor.game.content.ame.AntiMacroEvent;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.world.map.Location;

/**
 * Handles the zombie event.
 * @author Vexia
 */
@InitializablePlugin
public final class RiverTrollEvent extends AntiMacroEvent {

	/**
	 * Constructs a new {@code ZombieEvent} {@code Object}.
	 */
	public RiverTrollEvent() {
		super("river troll", true, false, Skills.FISHING);
	}

	@Override
	public void save(ByteBuffer buffer) {

	}

	@Override
	public void parse(ByteBuffer buffer) {

	}

	@Override
	public boolean start(Player player, boolean login, Object... args) {
		final RiverTrollNPC troll = new RiverTrollNPC(getId(player), player.getLocation(), this, player);
		troll.init();
		super.init(player);
		return true;
	}

	@Override
	public AntiMacroEvent create(Player player) {
		RiverTrollEvent event = new RiverTrollEvent();
		event.player = player;
		return event;
	}

	/**
	 * Gets the id of the shade.
	 * @param player the player.
	 * @return the id.
	 */
	private int getId(Player player) {
		int level = player.getProperties().getCurrentCombatLevel();
		int npcId = 396;
		if (level < 11) {
			npcId = 391;
		} else if (level < 21) {
			npcId = 392;
		} else if (level < 41) {
			npcId = 393;
		} else if (level < 61) {
			npcId = 394;
		} else if (level < 91) {
			npcId = 395;
		}
		return npcId;
	}

	@Override
	public Location getSpawnLocation() {
		return null;
	}

	@Override
	public void configure() {
	}

}
