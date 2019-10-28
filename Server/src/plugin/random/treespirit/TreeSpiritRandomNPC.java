package plugin.random.treespirit;

import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.game.content.ame.AntiMacroEvent;
import org.crandor.game.content.ame.AntiMacroNPC;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.tools.RandomFunction;

/**
 * Handles the Tree Spirit NPCs
 * @author Splinter
 */
public final class TreeSpiritRandomNPC extends AntiMacroNPC {

	/**
	 * The tree spirit npc ids.
	 */
	private static final int[] IDS = new int[] { 438, 439, 440, 441, 442, 443 };

	/**
	 * Constructs a new {@code TreeSpiritRandomNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 * @param event the event.
	 * @param player the player.
	 * @param quotes the quotes.
	 */
	public TreeSpiritRandomNPC(int id, Location location, AntiMacroEvent event, Player player) {
		super(id, location, event, player);
	}

	@Override
	public void init() {
		NPCDefinition def = NPCDefinition.forId(getId());
		def.getConfigurations().put("attack_animation", new Animation(94));
		def.getConfigurations().put("defence_animation", new Animation(95));
		def.getConfigurations().put("death_animation", new Animation(96));
		super.init();
		setRespawn(false);
		getProperties().getCombatPulse().attack(player);
		sendChat("Leave these woods and never return!");
	}

	@Override
	public void handleTickActions() {
		super.handleTickActions();
		if (!getProperties().getCombatPulse().isAttacking()) {
			getProperties().getCombatPulse().attack(player);
		}
		if (getProperties().getCombatPulse().isAttacking()) {
			if (RandomFunction.random(40) < 2) {
				sendChat("Leave these woods and never return!");
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
