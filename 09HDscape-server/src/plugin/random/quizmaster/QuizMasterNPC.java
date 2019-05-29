package plugin.random.quizmaster;

import org.crandor.game.content.ame.AntiMacroEvent;
import org.crandor.game.content.ame.AntiMacroNPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.repository.Repository;

/**
 * Handles the quiz master npc.
 * @author Vexia
 */
public final class QuizMasterNPC extends AntiMacroNPC {

	/**
	 * Constructs a new {@code QuizMasterNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 * @param event the event.
	 * @param player the player.
	 */
	public QuizMasterNPC(int id, Location location, AntiMacroEvent event, Player player) {
		super(id, location, event, player);
	}

	@Override
	public void handleTickActions() {
		if (!getLocks().isMovementLocked()) {
			if (dialoguePlayer == null || !dialoguePlayer.isActive() || !dialoguePlayer.getInterfaceManager().hasChatbox()) {
				dialoguePlayer = null;
			}
		}
		if (!player.isActive()) {
			clear();
		}
		if (!getPulseManager().hasPulseRunning()) {
			startFollowing();
		}
	}

	@Override
	public void clear() {
		Repository.removeRenderableNPC(this);
		Repository.getNpcs().remove(this);
		getViewport().setCurrentPlane(null);
	}

	@Override
	public int[] getIds() {
		return new int[] { 2477 };
	}

}
