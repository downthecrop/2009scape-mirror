package plugin.random.zombie;

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
public final class ZombieEvent extends AntiMacroEvent {

	/**
	 * Constructs a new {@code ZombieEvent} {@code Object}.
	 */
	public ZombieEvent() {
		super("zombie", true, false, Skills.PRAYER);
	}

	@Override
	public void save(ByteBuffer buffer) {

	}

	@Override
	public void parse(ByteBuffer buffer) {

	}

	@Override
	public boolean start(Player player, boolean login, Object... args) {
		final ZombieAmeNPC zombie = new ZombieAmeNPC(getId(player), player.getLocation(), this, player);
		zombie.init();
		super.init(player);
		return true;
	}

	@Override
	public AntiMacroEvent create(Player player) {
		ZombieEvent event = new ZombieEvent();
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
		int npcId = 424;
		if (level < 11) {
			npcId = 419;
		} else if (level < 21) {
			npcId = 420;
		} else if (level < 41) {
			npcId = 421;
		} else if (level < 61) {
			npcId = 422;
		} else if (level < 91) {
			npcId = 423;
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
