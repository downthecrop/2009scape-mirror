package core.game.content.activity.clanwars;

import core.game.content.activity.ActivityManager;
import core.game.interaction.Option;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.request.RequestModule;
import core.game.node.entity.player.link.request.RequestType;
import core.game.system.communication.ClanRank;
import core.plugin.Plugin;

/**
 * Handles the Clan wars challenge option.
 * @author Emperor
 * @versuib 1,9
 */
public final class CWChallengeOption extends OptionHandler {

	/**
	 * The challenge option.
	 */
	public static final Option OPTION = new Option("Challenge", 0);

	/**
	 * The request type.
	 */
	private static final RequestType REQUEST_TYPE = new RequestType("Sending challenge request...", ":clanreq:", getModule()) {
		@Override
		public boolean canRequest(Player player, Player target) {
			if (player.getCommunication().getClan() == null) {
				player.getPacketDispatch().sendMessage("You have to be in a clan to challenge players.");
				return false;
			}
			if (player.getCommunication().getClan().getRank(player).ordinal() < ClanRank.CAPTAIN.ordinal()) {
				player.getPacketDispatch().sendMessage("Your clan rank is not high enough to challenge other clans.");
				return false;
			}
			if (player.getCommunication().getClan().getClanWar() != null) {
				player.getPacketDispatch().sendMessage("Your clan is already in a war.");
				return false;
			}
			if (target.getCommunication().getClan() == null) {
				player.getPacketDispatch().sendMessage("This player is not in a clan.");
				return false;
			}
			if (target.getCommunication().getClan().getRank(target).ordinal() < ClanRank.CAPTAIN.ordinal()) {
				player.getPacketDispatch().sendMessage("This player's clan rank is not high enough to accept challenges.");
				return false;
			}
			if (target.getCommunication().getClan().getClanWar() != null) {
				player.getPacketDispatch().sendMessage("This player's clan is already in a war.");
				return false;
			}
			if (target.getCommunication().getClan() == player.getCommunication().getClan()) {
				player.getPacketDispatch().sendMessage("You can't challenge someone from your own clan.");
				return false;
			}
			return true;
		}
	};

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		OPTION.setHandler(this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.getRequestManager().request((Player) node, REQUEST_TYPE);
		return true;
	}

	/**
	 * Gets the request module.
	 * @return The module.
	 */
	private static RequestModule getModule() {
		return new RequestModule() {
			@Override
			public void open(Player player, Player target) {
				ActivityManager.start(player, "Clan wars", false, target);
			}
		};
	}
}