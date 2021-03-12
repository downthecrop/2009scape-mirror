package core.game.content.ame.events.quizmaster;

import core.game.content.ame.AntiMacroEvent;
import core.game.content.ame.AntiMacroNPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.game.world.repository.Repository;

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
