package plugin.command;

import org.crandor.ServerConstants;
import org.crandor.game.component.Component;
import org.crandor.game.content.global.tutorial.TutorialStage;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.info.Rights;
import org.crandor.game.node.entity.player.link.IronmanMode;
import org.crandor.game.node.entity.player.link.RunScript;
import org.crandor.game.node.entity.player.link.quest.Quest;
import org.crandor.game.node.entity.player.link.quest.QuestRepository;
import org.crandor.game.system.command.CommandPlugin;
import org.crandor.game.system.command.CommandSet;
import org.crandor.game.system.communication.ClanRepository;
import org.crandor.game.system.communication.CommunicationInfo;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.repository.Repository;
import org.crandor.net.amsc.WorldCommunicator;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;
import org.crandor.tools.StringUtils;

import plugin.zone.GrandExchangeZone.CreditStore;

/**
 * Handles a player command.
 * @author Vexia
 */
@InitializablePlugin
public final class PlayerCommandPlugin extends CommandPlugin {

	/**
	 * The store that sells items in exchange for credits.
	 */
	private static final CreditStore CREDIT_STORE = new CreditStore();

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		link(CommandSet.PLAYER);
		return this;
	}

	@Override
	public boolean parse(Player player, String name, String[] arguments) {
		switch (name) {
			/*
			 * Disabled commands
			case "shutdowninterface":
				player.getInterfaceManager().close();
				break;

			case "tut":
				int stage = Integer.parseInt(arguments[1]);
				TutorialStage.load(player, stage, false);
				break;
			*/

			case "shop":
				CREDIT_STORE.open(player);
				break;

			case "credits":
				int credits = CREDIT_STORE.getPoints(player);
				player.sendMessage("<col=3498db>You currently have " + credits + " credits to spend.");
				break;

			case "bank":
				if (!player.isAdmin()) {
					player.sendChat("Hey, everyone, I just tried to do something very silly!");
				}
				break;
				
			case "bankresettabs":
				for (int i = 0; i < player.getBank().getTabStartSlot().length; i++) {
					player.getBank().getTabStartSlot()[i] = 0;
				}
				player.getBank().setTabIndex(10);
				if (player.getBank().isOpen()) {
					player.getInterfaceManager().close();
				}
				player.sendMessage("<col=3498db>Your bank tabs have been reset!");
				return true;
				
			case "bankresetpin":
				if (arguments.length < 2) {
					player.sendMessage("<col=e74c3c>You must specify your current pin!");
					return true;
				}
				String oldPin = arguments[1];
				if (oldPin == null) {
					return true;
				}
				if (!player.getBankPinManager().hasPin()) {
					player.sendMessage("<col=e74c3c>You don't currently have a pin set.");
					return true;
				}
				if (!oldPin.equals(player.getBankPinManager().getPin())) {
					player.sendMessage("<col=e74c3c>" + oldPin + " doesn't match your current pin.");
					return true;
				}
				player.getBankPinManager().setPin(null);
				player.sendMessage("<col=3498db>Your pin has been reset.");
				return true;

			case "players":
				int count = Repository.getPlayers().size();
				int ironCount = 0;
				int ultIronCount = 0;
				for (Player p : Repository.getPlayers()) {
					if (p.getIronmanManager().checkRestriction(IronmanMode.ULTIMATE)) {
						ultIronCount++;
					}
					if (p.getIronmanManager().checkRestriction(IronmanMode.STANDARD)) {
						ironCount++;
					}
				}
				int regular = count - ironCount - ultIronCount;
				if (count == 1) {
					player.sendMessage("<col=3498db>There is 1 active player in this world.");
				} else {
					player.sendMessage("<col=3498db>There are " + count + " active players in this world: " + regular + " regular, " + ironCount + " iron, and " + ultIronCount + " ultimate iron.");
				}
				return player.getRights() == Rights.REGULAR_PLAYER;

			case "yell":
				if (!player.isDonator() && !player.isAdmin()) {
					player.sendMessages("Join clan chat \"" + GameWorld.getName() + "\" to talk globally, or become a donator to have access to", "this benefit.");
					return true;
				}
				if (player.getDetails().isMuted()) {
					player.sendMessage("<col=e74c3c>You have been " + (player.getDetails().isPermMute() ? "permanently" : "temporarily") + " muted due to breaking a rule.");
					return true;
				}
				if(WorldCommunicator.isEnabled()){
					if(ClanRepository.getDefault().isBanned(player.getName())){
						player.sendMessages("<col=e74c3c>You are temporarily unable to yell as you are banned from the main clan chat.", "Don't be annoying!");
						return true;
					}
				}
				if (player.getAttribute("yell-delay", 0.0) > GameWorld.getTicks()) {
					player.sendMessages("<col=e74c3c>You have yelled in the last " + player.getDonatorType().getCooldown() + " seconds. Upgrade to an extreme donator to have", "unlimited yelling abilities.");
					return true;
				}
				String text = getArgumentLine(arguments);
				if(text.contains("<img=") || text.contains("<br>") || text.contains("<col=") || text.contains("<shad=")){
					player.sendMessage("<col=e74c3c>Bad! No images/text effects allowed in yell chat.");
					return true;
				}
				int length = text.length();
				if (length > 100) {
					length = 100;
				}
				if (text.length() >= 2) {
					if (Character.isLowerCase(text.charAt(0))) {
						text = Character.toUpperCase(text.charAt(0)) + text.substring(1, length);
					}
					text = getYellPrefix(player) + text + "</col>";
					for (Player p : Repository.getPlayers()) {
						if (p.isActive()) {
							p.getPacketDispatch().sendMessage(text);
						}
					}
					if (player.getDonatorType().getCooldown() > 0 && !player.isStaff()) {
						player.setAttribute("yell-delay", (int) GameWorld.getTicks() + (player.getDonatorType().getCooldown() / 0.6));
					}
				} else {
					player.sendMessage("<col=e74c3c>Your message was too short.");
				}
				return true;
			case "togglenews":
				player.getSavedData().getGlobalData().setDisableNews(!player.getSavedData().getGlobalData().isDisableNews());
				player.sendMessage("<col=3498db>" + (player.getSavedData().getGlobalData().isDisableNews() ? "You will no longer see news notifications." : "You will now see news notifications."));
				return true;
			case "commands":
			case "command":
			case "commandlist":
				sendCommands(player);
				return true;
			case "quests":
				sendQuests(player);
				return true;
			case "donate":
				sendDonationInfo(player);
				return true;
			case "reply":
				if(player.getInterfaceManager().isOpened()){
					player.sendMessage("<col=e74c3c>Please finish what you're doing first.");
					return true;
				}
				if (player.getAttributes().containsKey("replyTo")) {
					player.setAttribute("keepDialogueAlive", true);
					final String replyTo = (String) player.getAttribute("replyTo", "").replaceAll("_", " ");
					player.setAttribute("runscript", new RunScript() {
						@Override
						public boolean handle() {
							CommunicationInfo.sendMessage(player, replyTo.toLowerCase(), (String) getValue());
							player.removeAttribute("keepDialogueAlive");
							return true;
						}
					});
					player.getDialogueInterpreter().sendMessageInput(StringUtils.formatDisplayName(replyTo));
				} else {
					player.getPacketDispatch().sendMessage("<col=3498db>You have not recieved any recent messages to which you can reply.");
				}
				return true;
		}
		return false;
	}

	/**
	 * Sends commands.
	 * @param player the player.
	 */
	private void sendCommands(Player player) {
		if (player.getInterfaceManager().isOpened()) {
			player.sendMessage("Finish what you're currently doing.");
			return;
		}
		player.getInterfaceManager().close();
		player.getPacketDispatch().sendString("<u>" + GameWorld.getName() + " commands</u>", 239, 1);
		player.getPacketDispatch().sendString("::filter (completely toggles game messages)<br>::players (shows player count)<br>::doublexp (claims double xp)<br>::shop opens up a dialogue so you can use credits<br>::togglenews toggles the news broadcasts.<br>::help shows a small help dialogue<br>::toggleatk toggles left-click attack option mode<br>Shift+Scroll wheel zooms the client in/out", 239, 2);
		player.getPacketDispatch().sendString("", 239, 3);
		player.getPacketDispatch().sendString("", 239, 4);
		player.getPacketDispatch().sendString("", 239, 5);
		player.getInterfaceManager().openComponent(239);
	}

	/**
	 * Sends information about donating.
	 * @param player The player.
	 */
	private void sendDonationInfo(Player player) {
		player.getInterfaceManager().open(new Component(275));
		for (int i = 0; i < 257; i++) {
			player.getPacketDispatch().sendString("", 275, i);
		}
		int lineId = 11;
		player.getPacketDispatch().sendString("<col=8A0808>" + "Donation Information" + "</col>", 275, 2);
		for (String s : ServerConstants.MESSAGES) {
			player.getPacketDispatch().sendString("<col=8A0808>" + s + "<br><br></col>", 275, lineId++);
		}
	}
	/**
	 * Sends the quests.
	 * @param player the player.
	 */
	private void sendQuests(Player player) {
		player.getInterfaceManager().open(new Component(275));
		for (int i = 0; i < 257; i++) {
			player.getPacketDispatch().sendString("", 275, i);
		}
		String red = "<col=8A0808>";
		int lineId = 11;
		player.getPacketDispatch().sendString("<col=8A0808>" + "Available Quests" + "</col>", 275, 2);
		for (Quest q : QuestRepository.getQuests().values()) {
			player.getPacketDispatch().sendString(q.isCompleted(player) ? red + "<str> " + q.getName() + " <br><br>" : red + " " + q.getName() + " <br><br>", 275, lineId++);
		}
	}

	/**
	 * Gets the yell prefix for the given player.
	 * @param player The player.
	 * @return The prefix used in yell.
	 */
	private static String getYellPrefix(Player player) {
		String color = "<col=800080>";
		StringBuilder sb = new StringBuilder("[");
		if (player.getDetails().getRights().isVisible(player)) {
			Rights right = player.getAttribute("visible_rank", player.getDetails().getRights());
			switch (right) {
				case ADMINISTRATOR:
					color = "<col=009999>";
					break;
				case PLAYER_MODERATOR:
					color = "<col=81819B>";
					break;
				default:
					break;
			}
		}
		if (player.isDonator() && !player.isStaff()) {
			color = "<col=" + player.getDonatorType().getColor() + ">";
		}
		int icon = Rights.getChatIcon(player);
		if (icon > 0) {
			sb.append("<img=").append(icon - 1).append(">");
		}
		sb.append(color).append(player.getUsername()).append("</col>");
		sb.append("]: ").append(color);
		return sb.toString();
	}
}
