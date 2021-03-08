package core.game.content.ame.events.shade;

import java.nio.ByteBuffer;

import core.game.content.ame.AntiMacroEvent;
import core.plugin.Initializable;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;

/**
 * Handles the shade event.
 * @author Vexia
 */
@Initializable
public final class ShadeEvent extends AntiMacroEvent {

	/**
	 * Constructs a new {@code ShadeEvent} {@code Object}.
	 */
	public ShadeEvent() {
		super("shade", true, false, Skills.PRAYER);
	}

	@Override
	public void save(ByteBuffer buffer) {

	}

	@Override
	public void parse(ByteBuffer buffer) {

	}

	@Override
	public boolean start(Player player, boolean login, Object... args) {
		final ShadeNPC shade = new ShadeNPC(getId(player), player.getLocation(), this, player);
		shade.init();
		super.init(player);
		return true;
	}

	@Override
	public AntiMacroEvent create(Player player) {
		ShadeEvent event = new ShadeEvent();
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
		int npcId = 430;
		if (level < 11) {
			npcId = 425;
		} else if (level < 21) {
			npcId = 426;
		} else if (level < 41) {
			npcId = 427;
		} else if (level < 61) {
			npcId = 428;
		} else if (level < 91) {
			npcId = 429;
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
