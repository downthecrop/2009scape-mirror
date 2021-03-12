package core.game.content.activity.duel;

import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.request.RequestModule;

/**
 * Handles a duel request getting accepted.
 * @author Emperor
 */
public final class DuelReqModule implements RequestModule {

	/**
	 * If the duel request is staked.
	 */
	private boolean staked;

	/**
	 * Constructs a new {@code DuelReqModule} {@code Object}.
	 * @param staked If the duel is staked.
	 */
	public DuelReqModule(boolean staked) {
		this.staked = staked;
	}

	@Override
	public void open(Player player, Player target) {
		DuelSession session = new DuelSession(player, target, staked);
		player.addExtension(DuelSession.class, session);
		target.addExtension(DuelSession.class, session);
		session.openRules();
	}

}