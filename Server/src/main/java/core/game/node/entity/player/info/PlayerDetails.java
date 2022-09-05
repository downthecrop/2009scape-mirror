package core.game.node.entity.player.info;

import core.game.system.communication.CommunicationInfo;
import core.net.IoSession;
import org.jetbrains.annotations.NotNull;
import rs09.auth.UserAccountInfo;
import rs09.game.world.GameWorld;

import java.util.concurrent.TimeUnit;

/**
 * Stores the details of a player's account.
 * @author Vexia
 * 
 */
public class PlayerDetails {
	public UserAccountInfo accountInfo = UserAccountInfo.createDefault();
	
	/**
	 * The communication info.
	 */
	private final CommunicationInfo communicationInfo = new CommunicationInfo();

	public int getCredits() {
		return accountInfo.getCredits();
	}

	public void setCredits(int amount) {
		accountInfo.setCredits(amount);
	}


	/**
	 * The unique id info.
	 */
	private final UIDInfo info = new UIDInfo();

	/**
	 * Represents the session.
	 */
	private IoSession session;

	public boolean saveParsed = false;

	/**
	 * Constructs a new {@code PlayerDetails}.
	 * @param username the username to set.
	 */
	public PlayerDetails(String username) {
		accountInfo.setUsername(username);
	}

	/**
	 * Checks if the player is muted.
	 * @return {@code True} if so.
	 */
	public boolean isBanned() {
		return accountInfo.getBanEndTime() > System.currentTimeMillis();
	}
	
	/**
	 * Checks if the mute is permanent.
	 * @return {@code True} if so.
	 */
	public boolean isPermMute() {
		return TimeUnit.MILLISECONDS.toDays(accountInfo.getMuteEndTime() - System.currentTimeMillis()) > 1000;
	}

	/**
	 * Checks if the player is muted.
	 * @return {@code True} if so.
	 */
	public boolean isMuted() {
		return accountInfo.getMuteEndTime() > System.currentTimeMillis();
	}

	/**
	 * Gets the rights.
	 * @return The rights.
	 */
	public Rights getRights() {
		return Rights.values()[accountInfo.getRights()];
	}

	/**
	 * Sets the credentials.
	 * @param rights The credentials to set.
	 */
	public void setRights(Rights rights) {
		this.accountInfo.setRights(rights.ordinal());
	}

	/**
	 * Gets the session.
	 * @return The session.
	 */
	public IoSession getSession() {
		return session;
	}

	/**
	 * Sets the session.
	 * @param session The session to set.
	 */
	public void setSession(IoSession session) {
		this.session = session;
	}

	/**
	 * Sets the password.
	 * @param password the password.
	 */
	public void setPassword(final String password) {
		this.accountInfo.setPassword(password);
	}

	/**
	 * Gets the username.
	 * @return The username.
	 */

	public String getUsername() {
		return this.accountInfo.getUsername();
	}

	/**
	 * Gets the uid.
	 * @return the uid.
	 */
	public int getUid() {
		return accountInfo.getUid();
	}

	/**
	 * Gets the password.
	 * @return The password.
	 */
	public String getPassword() {
		return this.accountInfo.getPassword();
	}

	/**
	 * Gets the mac address.
	 * @return the address.
	 */
	public String getMacAddress() {
		return info.getMac();
	}

	/**
	 * Gets the computer name.
	 * @return the name.
	 */
	public String getCompName() {
		return info.getCompName();
	}

	/**
	 * Gets the ip address.
	 * @return the ip.
	 */
	public String getIpAddress() {
		if (session == null) {
			return info.getIp();
		}
		return session.getAddress();
	}

	/**
	 * Gets the serial.
	 * @return the serial.
	 */
	public String getSerial() {
		return info.getSerial();
	}

	/**
	 * Gets the info.
	 * @return the info
	 */
	public UIDInfo getInfo() {
		return info;
	}

	/**
	 * Gets the communicationInfo.
	 * @return the communicationInfo
	 */
	public CommunicationInfo getCommunication() {
		return communicationInfo;
	}

	/**
	 * Gets the lastLogin.
	 * @return the lastLogin.
	 */
	public long getLastLogin() {
		return this.accountInfo.getLastLogin();
	}

	/**
	 * Sets the lastLogin.
	 * @param lastLogin the lastLogin to set
	 */
	public void setLastLogin(long lastLogin) {
		this.accountInfo.setLastLogin(lastLogin);
	}

	/**
	 * Gets the timePlayed.
	 * @return the timePlayed.
	 */
	public long getTimePlayed() {
		return this.accountInfo.getTimePlayed();
	}

	/**
	 * Sets the timePlayed.
	 * @param timePlayed the timePlayed to set
	 */
	public void setTimePlayed(long timePlayed) {
		this.accountInfo.setTimePlayed(timePlayed);
	}
	
	/**
	 * Sets the mute time.
	 * @param muteTime the mute time.
	 */
	public void setMuteTime(long muteTime) {
		this.accountInfo.setMuteEndTime(muteTime);
	}
	
	/**
	 * Gets the mute time.
	 * @return The mute time.
	 */
	public long getMuteTime() {
		return this.accountInfo.getMuteEndTime();
	}

	/**
	 * Gets the banTime.
	 * @return the banTime.
	 */
	public long getBanTime() {
		return this.accountInfo.getBanEndTime();
	}

	/**
	 * Sets the banTime.
	 * @param banTime the banTime to set
	 */
	public void setBanTime(long banTime) {
		this.accountInfo.setBanEndTime(banTime);
	}

	public void save() {
		if(!saveParsed) return;
		if(isBanned()) return;
		try {
			accountInfo.setContacts(communicationInfo.getContactString());
			accountInfo.setBlocked(communicationInfo.getBlockedString());
			accountInfo.setClanName(communicationInfo.getClanName());
			accountInfo.setClanReqs(communicationInfo.getClanReqString());
			accountInfo.setCurrentClan(communicationInfo.getCurrentClan());
			GameWorld.getAccountStorage().update(accountInfo);
		} catch (IllegalStateException ignored) {}
	}

	public static PlayerDetails getDetails(@NotNull String username) {
		return new PlayerDetails(username);
	}
}