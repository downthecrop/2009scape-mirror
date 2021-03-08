package core.game.content.ame.events.treespirit;

import java.nio.ByteBuffer;

import core.game.content.ame.AntiMacroEvent;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.game.world.map.Location;

/**
 * Handles the Tree Spirit event whilst cutting trees.
 * @author Splinter
 */
@Initializable
public final class TreeSpiritEvent extends AntiMacroEvent {

	/**
	 * Constructs a new {@code TreeSpiritEvent} {@code Object}.
	 */
	public TreeSpiritEvent() {
		super("tree spirit", true, false, Skills.WOODCUTTING);
	}

	@Override
	public void save(ByteBuffer buffer) {

	}

	@Override
	public void parse(ByteBuffer buffer) {

	}

	@Override
	public boolean start(Player player, boolean login, Object... args) {
		final TreeSpiritRandomNPC spirit = new TreeSpiritRandomNPC(getId(player), player.getLocation(), this, player);
		spirit.init();
		super.init(player);
		return true;
	}

	@Override
	public AntiMacroEvent create(Player player) {
		TreeSpiritEvent event = new TreeSpiritEvent();
		event.player = player;
		return event;
	}

	/**
	 * Gets the id of the spirit.
	 * @param player the player.
	 * @return the id.
	 */
	private int getId(Player player) {
		int level = player.getProperties().getCurrentCombatLevel();
		int npcId = 443;
		if (level < 11) {
			npcId = 438;
		} else if (level < 21) {
			npcId = 439;
		} else if (level < 41) {
			npcId = 440;
		} else if (level < 61) {
			npcId = 441;
		} else if (level < 91) {
			npcId = 442;
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
