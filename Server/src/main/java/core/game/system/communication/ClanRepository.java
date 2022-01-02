package core.game.system.communication;

import core.game.component.Component;
import core.game.content.activity.ActivityPlugin;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.PlayerDetails;
import core.game.node.entity.player.info.Rights;
import core.game.system.monitor.PlayerMonitor;
import rs09.game.world.GameWorld;
import rs09.game.world.repository.Repository;
import core.net.amsc.WorldCommunicator;
import core.net.packet.PacketRepository;
import core.net.packet.context.ClanContext;
import core.net.packet.context.MessageContext;
import core.net.packet.out.CommunicationMessage;
import core.net.packet.out.UpdateClanChat;

import java.util.*;

/**
 * Handles clan communication.
 * @author Emperor
 */
public final class ClanRepository {

	/**
	 * The maximum amount of members to be in a clan chat.
	 */
	private static final int MAX_MEMBERS = 100;

	/**
	 * The clan repository.
	 */
	private static final Map<String, ClanRepository> CLAN_REPOSITORY = new HashMap<>();

	/**
	 * The name of the clan owner.
	 */
	private final String owner;

	/**
	 * The clan name.
	 */
	private String name = "Chat disabled";

	/**
	 * The rank required for joining.
	 */
	private ClanRank joinRequirement = ClanRank.FRIEND;

	/**
	 * The rank required for messaging.
	 */
	private ClanRank messageRequirement = ClanRank.NONE;

	/**
	 * The rank required for kicking members.
	 */
	private ClanRank kickRequirement = ClanRank.OWNER;

	/**
	 * The rank required for loot-share.
	 */
	private ClanRank lootRequirement = ClanRank.ADMINISTRATOR;

	/**
	 * The members mapping.
	 */
	private final Map<String, ClanRank> ranks = new HashMap<>();

	/**
	 * The banned players.
	 */
	private final Map<String, Long> banned = new HashMap<>();

	/**
	 * The players who are currently in the friends chat.
	 */
	private List<ClanEntry> players = new ArrayList<>(MAX_MEMBERS);

	/**
	 * The current clan wars activity.
	 */
	private ActivityPlugin clanWar;

	/**
	 * Constructs a new {@code ClanRepository} {@code Object}.
	 * @param owner The owner of the clan.
	 */
	public ClanRepository(String owner) {
		this.owner = owner;
	}

	/**
	 * Enters the clan chat.
	 * @param player The player.
	 * @return {@code True} if the player successfully entered the clan chat.
	 */
	public boolean enter(Player player) {
		if (!owner.equals("2009scape") && players.size() >= MAX_MEMBERS) {
			player.getPacketDispatch().sendMessage("The channel you tried to join is full.:clan:");
			return false;
		}
		if (!player.getName().equals(owner) && player.getDetails().getRights() != Rights.ADMINISTRATOR) {
			if (isBanned(player.getName())) {
				player.getPacketDispatch().sendMessage("You are temporarily banned from this clan channel.:clan:");
				return false;
			}
			Player o = Repository.getPlayerByName(owner);
			if (o != null) {
				if (o.getCommunication().getBlocked().contains(player.getName())) {
					player.getPacketDispatch().sendMessage("You do not have a high enough rank to join this clan channel.:clan:");
					return false;
				}
			}
			ClanRank rank = getRank(player);
			if (rank.ordinal() < joinRequirement.ordinal()) {
				player.getPacketDispatch().sendMessage("You do not have a high enough rank to join this clan channel.:clan:");
				return false;
			}
		}
		ClanEntry entry = new ClanEntry(player);
		if (!players.contains(entry)) {
			players.add(entry);
		}
		player.getPacketDispatch().sendMessage("Now talking in clan channel " + name + ":clan:");
		player.getPacketDispatch().sendMessage("To talk, start each line of chat with the / symbol.:clan:");
		update();
		return true;
	}

	/**
	 * Cleans the chat from all players that shouldn't be in it.
	 */
	public void clean(boolean disable) {
		if (WorldCommunicator.isEnabled()) {
			return;
		}
		for (Iterator<ClanEntry> it = players.iterator(); it.hasNext();) {
			ClanEntry entry = it.next();
			Player player = entry.getPlayer();
			boolean remove = disable;
			if (!remove) {
				remove = getRank(player).ordinal() < joinRequirement.ordinal();
			}
			if (remove) {
				leave(player, false);
				player.getCommunication().setClan(null);
				it.remove();
			}
		}
		if (players.isEmpty()) {
			banned.clear();
		}
		update();
	}

	/**
	 * Checks if a player is banned.
	 * @param name The player's name.
	 * @return {@code True} if so.
	 */
	public boolean isBanned(String name) {
		if (banned.containsKey(name)) {
			long time = banned.get(name);
			if (time > System.currentTimeMillis()) {
				return true;
			}
			banned.remove(name);
		}
		return false;
	}

	/**
	 * Sends a message to all players in the chat.
	 * @param player The player sending the message.
	 * @param message The message to send.
	 */
	public void message(Player player, String message) {
		if (player.getLocks().isLocked("cc_message") || isBanned(player.getName())) {
			return;
		}
		player.getLocks().lock("cc_message", 1);
		if (!player.getName().equals(owner) && player.getDetails().getRights() != Rights.ADMINISTRATOR) {
			ClanRank rank = getRank(player);
			if (rank.ordinal() < messageRequirement.ordinal()) {
				player.getPacketDispatch().sendMessage("You do not have a high enough rank to talk in this clan channel.:clan:");
				return;
			}
		}
		StringBuilder sb = new StringBuilder(message);
		sb.append(" => ").append(name).append(" (owned by ").append(owner).append(")");
		player.getMonitor().log(sb.toString(), PlayerMonitor.CLAN_CHAT_LOG);
		for (Iterator<ClanEntry> it = players.iterator(); it.hasNext();) {
			ClanEntry entry = it.next();
			Player p = entry.getPlayer();
			if (p != null) {
				PacketRepository.send(CommunicationMessage.class, new MessageContext(p, player, MessageContext.CLAN_MESSAGE, message));
			}
		}
	}

	/**
	 * Kicks a player from this chat.
	 * @param player the player.
	 * @param target the victim.
	 */
	public void kick(Player player, Player target) {
		ClanRank rank = getRank(player);
		if (target.getDetails().getRights() == Rights.ADMINISTRATOR) {
			player.sendMessage("You can't kick an administrator.:clan:");
			return;
		}
		if (target.getName().equals(player.getName())) {
			player.sendMessage("You can't kick yourself.:clan:");
			return;
		}
		if (player.getDetails().getRights() != Rights.ADMINISTRATOR && rank.ordinal() < kickRequirement.ordinal()) {
			player.getPacketDispatch().sendMessage("You do not have a high enough rank to kick in this clan channel.:clan:");
			return;
		}
		if (target.getName().equals(owner)) {
			player.getPacketDispatch().sendMessage("You can't kick the owner of this clan channel.:clan:");
			return;
		}
		for (ClanEntry e : players) {
			PacketRepository.send(CommunicationMessage.class, new MessageContext(e.getPlayer(), player, MessageContext.CLAN_MESSAGE, "[Attempting to kick/ban " + target.getUsername() + " from this Clan Chat.]"));
		}
		leave(target, true, "You have been kicked from the channel.:clan:");
		target.getCommunication().setClan(null);
		banned.put(target.getName(), System.currentTimeMillis() + (3_600_000));
	}

	/**
	 * Represents the method to leave a clan.
	 * @param player the player.
	 * @param remove If the player should be removed from the list.
	 */
	public void leave(Player player, boolean remove) {
		leave(player, remove, "You have left the channel.:clan:");
	}

	/**
	 * Represents the method to leave a clan.
	 * @param player the player.
	 * @param remove If the player should be removed from the list.
	 */
	public void leave(Player player, boolean remove, String message) {
		if (remove) {
			players.remove(new ClanEntry(player));
			update();
			if (players.size() < 1) {
				banned.clear();
			}
		}
		PacketRepository.send(UpdateClanChat.class, new ClanContext(player, this, true));
		player.getPacketDispatch().sendMessage(message);
		if (clanWar != null && !isDefault()) {
			clanWar.fireEvent("leavefc", player);
		}
	}

	/**
	 * Ranks a member of this chat.
	 * @param name The name of the member.
	 * @param rank The rank to set.
	 */
	public void rank(String name, ClanRank rank) {
		boolean update;
		if (rank == ClanRank.NONE) {
			update = ranks.remove(name) != null;
		} else {
			update = ranks.put(name, rank) != rank;
		}
		if (update) {
			clean(false);
		}
	}

	/**
	 * Updates the clan chat for all players in this clan.
	 */
	public void update() {
		for (Iterator<ClanEntry> it = players.iterator(); it.hasNext();) {
			ClanEntry e = it.next();
			if (e.getWorldId() == GameWorld.getSettings().getWorldId() && e.getPlayer() != null) {
				PacketRepository.send(UpdateClanChat.class, new ClanContext(e.getPlayer(), this, false));
			}
		}
	}

	/**
	 * Gets the clan rank for the given clan entry.
	 * @param entry The clan entry.
	 * @return The rank.
	 */
	public ClanRank getRank(ClanEntry entry) {
		if (entry.getPlayer() != null) {
			return getRank(entry.getPlayer());
		}
		ClanRank rank = ranks.get(entry.getName());
		if (rank == null) {
			return ClanRank.NONE;
		}
		return rank;
	}

	/**
	 * Gets the rank of the player.
	 * @param player The player.
	 * @return The clan rank.
	 */
	public ClanRank getRank(Player player) {
		ClanRank rank = ranks.get(player.getName());
		if (player.getDetails().getRights() == Rights.ADMINISTRATOR && !player.getName().equals(owner)) {
			return ClanRank.ADMINISTRATOR;
		}
		if (rank == null) {
			if (player.getName().equals(owner)) {
				return ClanRank.OWNER;
			}
			return ClanRank.NONE;
		}
		return rank;
	}

	/**
	 * Opens the clan settings for the player.
	 * @param player The player.
	 */
	public static void openSettings(Player player) {
		player.getInterfaceManager().open(new Component(590));
		ClanRepository c = get(player.getName());
		if (c != null) {
			c.updateSettings(player);
		}
	}

	/**
	 * Updates the clan settings for the player.
	 * @param player The player.
	 */
	public void updateSettings(Player player) {
		player.getPacketDispatch().sendString(name, 590, 22);
		// player.getPacketDispatch().sendConfig(1083, (isCoinshare() ? 1 : 0)
		// << 18 | (isLootshare() ? 0 : 1));
		player.getPacketDispatch().sendString(joinRequirement.getInfo(), 590, 23);
		player.getPacketDispatch().sendString(messageRequirement.getInfo(), 590, 24);
		player.getPacketDispatch().sendString(kickRequirement.getInfo(), 590, 25);
		player.getPacketDispatch().sendString(lootRequirement.getInfo(), 590, 26);
	}

	/**
	 * Loads the clan data.
	 * @param owner The owner of the clan to load.
	 * @return The clan data.
	 */
	public static ClanRepository get(String owner) {
		return get(owner, false);
	}

	/**
	 * Loads the clan data.
	 * @param owner The owner of the clan to load.
	 * @param create If the clan should be created if it doesn't exist.
	 * @return The clan data.
	 */
	public static ClanRepository get(String owner, boolean create) {
		ClanRepository clan = CLAN_REPOSITORY.get(owner);
		if (clan != null) {
			return clan;
		}
		Player player = Repository.getPlayerByName(owner);
		PlayerDetails details = player != null ? player.getDetails() : null;
		if (details == null) {
			details = PlayerDetails.getDetails(owner);
			if (details == null) {
				return null;
			}
		}
		String name = details.getCommunication().getClanName();
		if (name.length() < 1) {
			if (!create) {
				return null;
			}
			name = "Chat disabled";
		}
		CLAN_REPOSITORY.put(owner, clan = new ClanRepository(owner));
		for (Contact c : details.getCommunication().getContacts().values()) {
			clan.ranks.put(c.getUsername(), c.getRank());
		}
		clan.name = name;
		clan.joinRequirement = details.getCommunication().getJoinRequirement();
		clan.messageRequirement = details.getCommunication().getMessageRequirement();
		clan.kickRequirement = details.getCommunication().getKickRequirement();
		clan.lootRequirement = details.getCommunication().getLootRequirement();
		return clan;
	}

	/**
	 * Checks if this clan chat is the default clan chat.
	 * @return {@code True} if so.
	 */
	public boolean isDefault() {
		return owner.equals(GameWorld.getSettings().getName().toLowerCase());
	}

	/**
	 * Gets the default clan chat.
	 * @return The default clan chat.
	 */
	public static ClanRepository getDefault() {
		return get(GameWorld.getSettings().getName().toLowerCase());
	}

	/**
	 * Deletes a clan.
	 */
	public void delete() {
		CLAN_REPOSITORY.remove(owner);
		clean(true);
	}

	/**
	 * Gets the currently loaded clans.
	 * @return The clans.
	 */
	public static Map<String, ClanRepository> getClans() {
		return CLAN_REPOSITORY;
	}

	/**
	 * Gets the list of players currently in the clan.
	 * @return The list of players.
	 */
	public List<ClanEntry> getPlayers() {
		return players;
	}

	/**
	 * Gets the joinRequirement.
	 * @return The joinRequirement.
	 */
	public ClanRank getJoinRequirement() {
		return joinRequirement;
	}

	/**
	 * Sets the joinRequirement.
	 * @param joinRequirement The joinRequirement to set.
	 */
	public void setJoinRequirement(ClanRank joinRequirement) {
		this.joinRequirement = joinRequirement;
		clean(false);
	}

	/**
	 * Gets the messageRequirement.
	 * @return The messageRequirement.
	 */
	public ClanRank getMessageRequirement() {
		return messageRequirement;
	}

	/**
	 * Sets the messageRequirement.
	 * @param messageRequirement The messageRequirement to set.
	 */
	public void setMessageRequirement(ClanRank messageRequirement) {
		this.messageRequirement = messageRequirement;
	}

	/**
	 * Gets the kickRequirement.
	 * @return The kickRequirement.
	 */
	public ClanRank getKickRequirement() {
		return kickRequirement;
	}

	/**
	 * Sets the kickRequirement.
	 * @param kickRequirement The kickRequirement to set.
	 */
	public void setKickRequirement(ClanRank kickRequirement) {
		this.kickRequirement = kickRequirement;
		update();
	}

	/**
	 * Gets the lootRequirement.
	 * @return The lootRequirement.
	 */
	public ClanRank getLootRequirement() {
		return lootRequirement;
	}

	/**
	 * Gets the banned.
	 * @return the banned
	 */
	public Map<String, Long> getBanned() {
		return banned;
	}

	/**
	 * Sets the lootRequirement.
	 * @param lootRequirement The lootRequirement to set.
	 */
	public void setLootRequirement(ClanRank lootRequirement) {
		this.lootRequirement = lootRequirement;
	}

	/**
	 * Gets the owner.
	 * @return The owner.
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Gets the name.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the members.
	 * @return The members.
	 */
	public Map<String, ClanRank> getRanks() {
		return ranks;
	}

	/**
	 * Gets the clanWar.
	 * @return The clanWar.
	 */
	public ActivityPlugin getClanWar() {
		return clanWar;
	}

	/**
	 * Sets the clanWar.
	 * @param clanWar The clanWar to set.
	 */
	public void setClanWar(ActivityPlugin clanWar) {
		this.clanWar = clanWar;
	}

}