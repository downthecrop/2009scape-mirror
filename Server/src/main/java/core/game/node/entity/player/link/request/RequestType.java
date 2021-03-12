package core.game.node.entity.player.link.request;

import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.request.assist.AssistSession;
import core.game.node.entity.player.link.request.trade.TradeModule;

/**
 * Represents a request type.
 * @author 'Vexia
 * @author Emperor
 */
public class RequestType {

	/**
	 * The trade request type.
	 */
	public static final RequestType TRADE = new RequestType("Sending a trade offer...", ":tradereq:", new TradeModule());

	/**
	 * The assist request type.
	 */
	public static final RequestType ASSIST = new RequestType("Sending assistance request...", ":assistreq:", new AssistSession());

	/**
	 * Represents the message to send for the player when requesting.
	 */
	private final String message;

	/**
	 * Represents the requesting message type.
	 */
	private final String requestMessage;

	/**
	 * Represents the module used for this type of requesting.
	 */
	private final RequestModule module;

	/**
	 * Constructs a new {@code RequestManager {@code Object}.
	 * @param message the message.
	 * @param requestMessage the requesting message.
	 */
	public RequestType(String message, String requestMessage, RequestModule module) {
		this.message = message;
		this.requestMessage = requestMessage;
		this.module = module;
	}

	/**
	 * Checks if the request can be made.
	 * @param player The player.
	 * @param target The target.
	 * @return {@code True} if so.
	 */
	public boolean canRequest(Player player, Player target) {
		return true;
	}

	/**
	 * Gets the requesting message formated with the targets name.
	 * @param target the target.
	 * @return the message to send.
	 */
	public String getRequestMessage(Player target) {
		return target.getUsername() + getRequestMessage();
	}

	/**
	 * Method used to get the request type by the option being handled.
	 * @param option the option being handled.
	 * @return the request type in correlation.
	 */
	public static RequestType forOption(String option) {
		return option.equals("trade with") ? TRADE : ASSIST;
	}

	/**
	 * Gets the message.
	 * @return The message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets the requestMessage.
	 * @return The requestMessage.
	 */
	public String getRequestMessage() {
		return requestMessage;
	}

	/**
	 * Gets the request module.
	 * @return the module.
	 */
	public RequestModule getModule() {
		return module;
	}
}
