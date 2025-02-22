package core.game.node.entity.player.link;

import core.game.system.config.ItemConfigParser;
import org.json.simple.JSONObject;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.net.packet.IoBuffer;

import static core.api.ContentAPIKt.*;


/**
 * Holds a player's settings.
 * @author Emperor
 */
public final class Settings {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The run energy.
	 */
	private double runEnergy = 100.0;

	/**
	 * The player's weight.
	 */
	private double weight;

	/**
	 * The brightness setting.
	 */
	private int brightness = 2;

	/**
	 * The music volume.
	 */
	private int musicVolume;

	/**
	 * The sound effects volume.
	 */
	private int soundEffectVolume;

	/**
	 * The area sounds volume.
	 */
	private int areaSoundVolume;

	/**
	 * If the player has the single mouse button setting enabled.
	 */
	private boolean singleMouseButton;

	/**
	 * If the chat effects should be disabled.
	 */
	private boolean disableChatEffects;

	/**
	 * If the private chat should be split from public chat.
	 */
	private boolean splitPrivateChat;

	/**
	 * If the player has the accept aid setting enabled.
	 */
	private boolean acceptAid;

	/**
	 * If the player's run button is toggled.
	 */
	private boolean runToggled;

	/**
	 * The public chat setting.
	 */
	private int publicChatSetting = 0;

	/**
	 * The private chat setting.
	 */
	private int privateChatSetting = 0;

	/**
	 * The clan chat setting.
	 */
	private int clanChatSetting = 0;

	/**
	 * The trade setting.
	 */
	private int tradeSetting = 0;

	/**
	 * The assist setting.
	 */
	private int assistSetting = 0;

	/**
	 * If the special attack is toggled.
	 */
	private boolean specialToggled;

	/**
	 * The current special energy the player has left.
	 */
	private int specialEnergy = 100;

	/**
	 * The current attack style index.
	 */
	private int attackStyleIndex = 0;

	/**
	 * Constructs a new {@code Settings} {@code Object}.
	 * @param player The player.
	 */
	public Settings(Player player) {
		this.player = player;
	}

	/**
	 * Updates the settings.
	 */
	public void update() {
		setVarp(player, 166, brightness + 1);
		setVarp(player, 168, musicVolume);
		setVarp(player, 169, soundEffectVolume);
		setVarp(player, 872, areaSoundVolume);
		setVarp(player, 170, singleMouseButton ? 1 : 0);
		setVarp(player, 171, disableChatEffects ? 1 : 0);
		setVarp(player, 287, splitPrivateChat ? 1 : 0);
		setVarp(player, 427, acceptAid ? 1 : 0);
		setVarp(player, 172, !player.getProperties().isRetaliating() ? 1 : 0);
		setVarp(player, 173, runToggled ? 1 : 0);
		setVarp(player, 1054, clanChatSetting);
		setVarp(player, 1055, assistSetting);
		setVarp(player, 300, specialEnergy * 10);
		setVarp(player, 43, attackStyleIndex);
		player.getPacketDispatch().sendRunEnergy();
		updateChatSettings();
	}

	/**
	 * Toggles the attack style index.
	 * @param index The index.
	 */
	public void toggleAttackStyleIndex(int index) {
		this.attackStyleIndex = index;
		setVarp(player, 43, attackStyleIndex);
	}

	/**
	 * Updates the chat settings.
	 */
	public void updateChatSettings() {
		player.getSession().write(new IoBuffer(232).put(publicChatSetting).put(privateChatSetting).put(tradeSetting));
	}

	/**
	 * Sets the chat settings.
	 * @param pub The public chat setting.
	 * @param priv The private chat setting.
	 * @param trade The trade setting.
	 */
	public void updateChatSettings(int pub, int priv, int trade) {
		boolean update = false;
		if (publicChatSetting != pub) {
			publicChatSetting = pub;
			update = true;
		}
		if (tradeSetting != trade) {
			tradeSetting = trade;
			update = true;
		}
		if (update) {
			updateChatSettings();
		}
	}

	/**
	 * Sets the chat settings.
	 * @param pub The public chat setting.
	 * @param priv The private chat setting.
	 * @param trade The trade setting.
	 */
	public void setChatSettings(int pub, int priv, int trade) {
		publicChatSetting = pub;
		privateChatSetting = priv;
		tradeSetting = trade;
	}

	/**
	 * Parses the settings from the save file.
	 * @param settingsData The JSON object.
	 */
	public void parse(JSONObject settingsData){
		brightness = Integer.parseInt( settingsData.get("brightness").toString());
		musicVolume = Integer.parseInt( settingsData.get("musicVolume").toString());
		soundEffectVolume = Integer.parseInt( settingsData.get("soundEffectVolume").toString());
		areaSoundVolume = Integer.parseInt( settingsData.get("areaSoundVolume").toString());
		singleMouseButton = (boolean) settingsData.get("singleMouse");
		disableChatEffects = (boolean) settingsData.get("disableChatEffects");
		splitPrivateChat = (boolean) settingsData.get("splitPrivate");
		acceptAid = (boolean) settingsData.get("acceptAid");
		runToggled = (boolean) settingsData.get("runToggled");
		publicChatSetting = Integer.parseInt( settingsData.get("publicChatSetting").toString());
		privateChatSetting = Integer.parseInt( settingsData.get("privateChatSetting").toString());
		clanChatSetting = Integer.parseInt( settingsData.get("clanChatSetting").toString());
		tradeSetting = Integer.parseInt( settingsData.get("tradeSetting").toString());
		assistSetting = Integer.parseInt( settingsData.get("assistSetting").toString());
		runEnergy = Double.parseDouble( settingsData.get("runEnergy").toString());
		specialEnergy = Integer.parseInt( settingsData.get("specialEnergy").toString());
		attackStyleIndex = Integer.parseInt( settingsData.get("attackStyle").toString());
		player.getProperties().setRetaliating((boolean) settingsData.get("retaliation"));
	}

	/**
	 * Toggles the special attack bar.
	 */
	public void toggleSpecialBar() {
		setSpecialToggled(!specialToggled);
	}

	/**
	 * Toggles the special attack bar.
	 * @param enable If the special attack should be enabled.
	 */
	public void setSpecialToggled(boolean enable) {
		specialToggled = !specialToggled;
		setVarp(player, 301, specialToggled ? 1 : 0);
	}

	/**
	 * Checks if the special attack bar is toggled.
	 * @return {@code True} if so.
	 */
	public boolean isSpecialToggled() {
		return specialToggled;
	}

	/**
	 * Drains an amount of special attack energy.
	 * @param amount The amount to drain.
	 * @return {@code True} if succesful, {@code false} if the special attack
	 * energy amount hasn't changed after calling this method.
	 */
	public boolean drainSpecial(int amount) {
		if (!specialToggled) {
			return false;
		}
		setSpecialToggled(false);
		if (amount > specialEnergy) {
			player.getPacketDispatch().sendMessage("You do not have enough special attack energy left.");
			return false;
		}
		setSpecialEnergy(specialEnergy - amount);
		return true;
	}

	/**
	 * Sets the special energy amount.
	 * @param value The amount to set.
	 */
	public void setSpecialEnergy(int value) {
		specialEnergy = value;
		setVarp(player, 300, specialEnergy * 10);
	}

	/**
	 * Gets the amount of special energy left.
	 * @return The amount of energy.
	 */
	public int getSpecialEnergy() {
		return specialEnergy;
	}

	/**
	 * Toggles the retaliating button.
	 */
	public void toggleRetaliating() {
		player.getProperties().setRetaliating(!player.getProperties().isRetaliating());
		setVarp(player, 172, !player.getProperties().isRetaliating() ? 1 : 0);
	}

	/**
	 * Toggles the singleMouseButton.
	 */
	public void toggleMouseButton() {
		singleMouseButton = !singleMouseButton;
		setVarp(player, 170, singleMouseButton ? 1 : 0);
	}

	/**
	 * Toggles the disableChatEffects.
	 */
	public void toggleChatEffects() {
		disableChatEffects = !disableChatEffects;
		setVarp(player, 171, disableChatEffects ? 1 : 0);
	}

	/**
	 * Toggles the splitPrivateChat.
	 */
	public void toggleSplitPrivateChat() {
		splitPrivateChat = !splitPrivateChat;
		setVarp(player, 287, splitPrivateChat ? 1 : 0);
	}

	/**
	 * Toggles the acceptAid.
	 */
	public void toggleAcceptAid() {
		acceptAid = !acceptAid;
		setVarp(player, 427, acceptAid ? 1 : 0);
	}

	/**
	 * Toggles the run button.
	 */
	public void toggleRun() {
		setRunToggled(!runToggled);
	}

	/**
	 * Toggles the run button.
	 * If the run button should be enabled.
	 */
	public void setRunToggled(boolean enabled) {
		runToggled = enabled;
		setVarp(player, 173, runToggled ? 1 : 0);
	}

	/**
	 * Decreases the run energy with the given amount (drain parameter). <br> To
	 * increase, use a negative drain value.
	 * @param drain The drain amount.
	 */
	public void updateRunEnergy(double drain) {
		runEnergy -= drain;
		if (runEnergy < 0) {
			runEnergy = 0.0;
		} else if (runEnergy > 100) {
			runEnergy = 100.0;
		}
		player.getPacketDispatch().sendRunEnergy();
	}

	/**
	 * Updates the weight.
	 */
	public void updateWeight() {
		weight = 0.0;
		for (int i = 0; i < 28; i++) {
			Item item = player.getInventory().get(i);
			if (item == null) {
				continue;
			}
			double value = item.getDefinition().getConfiguration(ItemConfigParser.WEIGHT, 0.0);
			if (value > 0) {
				weight += value;
			}
		}
		for (int i = 0; i < 11; i++) {
			Item item = player.getEquipment().get(i);
			if (item == null) {
				continue;
			}
			weight += item.getDefinition().getConfiguration(ItemConfigParser.WEIGHT, 0.0);
		}
		player.getPacketDispatch().sendString((int) weight + " kg", 667, 32);
	}

	/**
	 * Gets the weight.
	 * @return The weight.
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Gets the brightness.
	 * @return The brightness.
	 */
	public int getBrightness() {
		return brightness;
	}

	/**
	 * Sets the brightness.
	 * @param brightness The brightness to set.
	 */
	public void setBrightness(int brightness) {
		this.brightness = brightness;
		setVarp(player, 166, brightness + 1);
	}

	/**
	 * Gets the musicVolume.
	 * @return The musicVolume.
	 */
	public int getMusicVolume() {
		return musicVolume;
	}

	/**
	 * Sets the musicVolume.
	 * @param musicVolume The musicVolume to set.
	 */
	public void setMusicVolume(int musicVolume) {
		this.musicVolume = musicVolume;
		setVarp(player, 168, musicVolume);
	}

	/**
	 * Gets the soundEffectVolume.
	 * @return The soundEffectVolume.
	 */
	public int getSoundEffectVolume() {
		return soundEffectVolume;
	}

	/**
	 * Sets the soundEffectVolume.
	 * @param soundEffectVolume The soundEffectVolume to set.
	 */
	public void setSoundEffectVolume(int soundEffectVolume) {
		this.soundEffectVolume = soundEffectVolume;
		setVarp(player, 169, soundEffectVolume);
	}

	/**
	 * Gets the areaSoundVolume.
	 * @return The areaSoundVolume.
	 */
	public int getAreaSoundVolume() {
		return areaSoundVolume;
	}

	/**
	 * Sets the areaSoundVolume.
	 * @param areaSoundVolume The areaSoundVolume to set.
	 */
	public void setAreaSoundVolume(int areaSoundVolume) {
		this.areaSoundVolume = areaSoundVolume;
		setVarp(player, 872, areaSoundVolume);
	}

	/**
	 * Gets the singleMouseButton.
	 * @return The singleMouseButton.
	 */
	public boolean isSingleMouseButton() {
		return singleMouseButton;
	}

	/**
	 * Gets the disableChatEffects.
	 * @return The disableChatEffects.
	 */
	public boolean isDisableChatEffects() {
		return disableChatEffects;
	}

	/**
	 * Gets the splitPrivateChat.
	 * @return The splitPrivateChat.
	 */
	public boolean isSplitPrivateChat() {
		return splitPrivateChat;
	}

	/**
	 * Gets the acceptAid.
	 * @return The acceptAid.
	 */
	public boolean isAcceptAid() {
		if (player.getIronmanManager().isIronman()) {
			return false;
		}
		return acceptAid;
	}

	/**
	 * Gets the runToggled.
	 * @return The runToggled.
	 */
	public boolean isRunToggled() {
		return runToggled;
	}

	/**
	 * Gets the publicChatSetting.
	 * @return The publicChatSetting.
	 */
	public int getPublicChatSetting() {
		return publicChatSetting;
	}

	/**
	 * Sets the publicChatSetting.
	 * @param publicChatSetting The publicChatSetting to set.
	 */
	public void setPublicChatSetting(int publicChatSetting) {
		this.publicChatSetting = publicChatSetting;
		updateChatSettings();
	}

	/**
	 * Gets the privateChatSetting.
	 * @return The privateChatSetting.
	 */
	public int getPrivateChatSetting() {
		return privateChatSetting;
	}

	/**
	 * Sets the privateChatSetting.
	 * @param privateChatSetting The privateChatSetting to set.
	 */
	public void setPrivateChatSetting(int privateChatSetting) {
		this.privateChatSetting = privateChatSetting;
		updateChatSettings();
	}

	/**
	 * Gets the clanChatSetting.
	 * @return The clanChatSetting.
	 */
	public int getClanChatSetting() {
		return clanChatSetting;
	}

	/**
	 * Sets the clanChatSetting.
	 * @param clanChatSetting The clanChatSetting to set.
	 */
	public void setClanChatSetting(int clanChatSetting) {
		this.clanChatSetting = clanChatSetting;
		setVarp(player, 1054, clanChatSetting);
	}

	/**
	 * Gets the tradeSetting.
	 * @return The tradeSetting.
	 */
	public int getTradeSetting() {
		return tradeSetting;
	}

	/**
	 * Sets the tradeSetting.
	 * @param tradeSetting The tradeSetting to set.
	 */
	public void setTradeSetting(int tradeSetting) {
		this.tradeSetting = tradeSetting;
		updateChatSettings();
	}

	/**
	 * Gets the assistSetting.
	 * @return The assistSetting.
	 */
	public int getAssistSetting() {
		return assistSetting;
	}

	/**
	 * Sets the assistSetting.
	 * @param assistSetting The assistSetting to set.
	 */
	public void setAssistSetting(int assistSetting) {
		this.assistSetting = assistSetting;
                setVarp(player, 1055, assistSetting);
	}

	/**
	 * @return the runEnergy
	 */
	public double getRunEnergy() {
		return runEnergy;
	}

	/**
	 * @param runEnergy the runEnergy to set
	 */
	public void setRunEnergy(double runEnergy) {
		this.runEnergy = runEnergy;
	}

	/**
	 * Gets the attackStyleIndex.
	 * @return The attackStyleIndex.
	 */
	public int getAttackStyleIndex() {
		return attackStyleIndex;
	}

	/**
	 * Sets the attackStyleIndex.
	 * @param attackStyleIndex The attackStyleIndex to set.
	 */
	public void setAttackStyleIndex(int attackStyleIndex) {
		this.attackStyleIndex = attackStyleIndex;
	}

}
