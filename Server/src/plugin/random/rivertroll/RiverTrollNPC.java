package plugin.random.rivertroll;

import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.game.content.ame.AntiMacroEvent;
import org.crandor.game.content.ame.AntiMacroNPC;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.tools.RandomFunction;

/**
 * Handles the river troll npc.
 * @author Vexia
 */
public final class RiverTrollNPC extends AntiMacroNPC {

	/**
	 * The river troll npc.
	 */
	private static final int[] IDS = new int[] { 391, 392, 393, 394, 395, 396 };

	/**
	 * Constructs a new {@code RiverTrollNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 * @param event the event.
	 * @param player the player.
	 * @param quotes the quotes.
	 */
	public RiverTrollNPC(int id, Location location, AntiMacroEvent event, Player player) {
		super(id, location, event, player);
	}

	@Override
	public void init() {
		// Remove.
		NPCDefinition def = NPCDefinition.forId(getId());
		def.getConfigurations().put("attack_animation", new Animation(284));
		def.getConfigurations().put("defence_animation", new Animation(285));
		def.getConfigurations().put("death_animation", new Animation(287));
		super.init();
		setRespawn(false);
		getProperties().getCombatPulse().attack(player);
		sendChat("Fishies be mine! Leave dem fishies!");
	}

	@Override
	public void handleTickActions() {
		super.handleTickActions();
		if (!getProperties().getCombatPulse().isAttacking()) {
			getProperties().getCombatPulse().attack(player);
		}
		if (getProperties().getCombatPulse().isAttacking()) {
			if (RandomFunction.random(20) < 3) {
				sendChat("Fishies be mine! Leave dem fishies!");
			}
		}
	}

	@Override
	public boolean isIgnoreMultiBoundaries(Entity victim) {
		return victim == player;
	}

	@Override
	public int[] getIds() {
		return IDS;
	}

}
