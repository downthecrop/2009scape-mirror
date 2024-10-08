package core.game.node.entity.player.link.music;

import core.game.node.entity.player.Player;

import core.game.node.entity.player.link.emote.Emotes;
import core.game.world.GameWorld;
import core.net.packet.PacketRepository;
import core.net.packet.context.MusicContext;
import core.net.packet.context.StringContext;
import core.net.packet.out.MusicPacket;
import core.net.packet.out.StringPacket;

import java.util.*;

import static core.api.ContentAPIKt.*;


/**
 * Handles a music playing for a player.
 * @author Emperor
 */
public final class MusicPlayer {

	/**
	 * The tutorial island music.
	 */
	public static final int TUTORIAL_MUSIC = 62;

	/**
	 * The default music id.
	 */
	public static final int DEFAULT_MUSIC_ID = 76;

	/**
	 * The configuration ids.
	 */
	private static final int[] CONFIG_IDS = { 20, 21, 22, 23, 24, 25, 298, 311, 346, 414, 464, 598, 662, 721, 906, 1009, 1104, 1136, 1180, 1202};

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The currently unlocked songs.
	 */
	private final Map<Integer, MusicEntry> unlocked;

	/**
	 * The music id of the currently played song.
	 */
	private int currentMusicId;

	/**
	 * If a song is currently playing.
	 */
	private boolean playing;

	/**
	 * If the song is looping.
	 */
	private boolean looping;

	/**
	 * Constructs a new {@code MusicPlayer} {@code Object}.
	 * @param player The player.
	 */
	public MusicPlayer(Player player) {
		this.player = player;
		this.unlocked = new HashMap<>();
	}

	/**
	 * Initializes the music player.
	 */
	public void init() {
		refreshList();
		setVarp(player, 19, looping ? 1 : 0);
		int value = 0;
		for (int i = 0; i < CONFIG_IDS.length; i++) {
			value |= 2 << i;
		}
		player.getPacketDispatch().sendIfaceSettings(value, 1, 187, 0, CONFIG_IDS.length * 64);
		if (!unlocked.containsKey(TUTORIAL_MUSIC)) {
			unlock(TUTORIAL_MUSIC, false);
		}
		if (!isMusicPlaying()) {
			playDefault();
		}
		if (!hasAirGuitar() && player.getEmoteManager().isUnlocked(Emotes.AIR_GUITAR)) {
			player.getPacketDispatch().sendMessage("As you no longer have all music unlocked, the Air Guitar emote is locked again.");
			player.getEmoteManager().lock(Emotes.AIR_GUITAR);
		}
	}

	/**
	 * Clears the unlocked songs. This should only be used in the permadeath code.
	 */
	public void clearUnlocked() {
		this.unlocked.clear();
	}

	/**
	 * Checks if the player has enough songs unlocked for the Air guitar emote.
	 * @return {@code True} if so.
	 */
	public boolean hasAirGuitar() {
		return unlocked.size() >= 200 || unlocked.size() == MusicEntry.getSongs().size();
	}

	/**
	 * Checks if the player has unlocked the song.
	 * @param musicId The music id.
	 * @return {@code True} if so.
	 */
	public boolean hasUnlocked(int musicId) {
		MusicEntry entry = MusicEntry.forId(musicId);
		if (entry == null) {
			return false;
		}
		return hasUnlockedIndex(entry.getIndex());
	}

	/**
	 * Checks if the player has unlocked the song for the given list index.
	 * @param index The list index.
	 * @return {@code True} if so.
	 */
	public boolean hasUnlockedIndex(int index) {
		return unlocked.containsKey(index);
	}

	/**
	 * Refreshes the music list.
	 */
	public void refreshList() {
		int[] values = new int[CONFIG_IDS.length];
		for (MusicEntry entry : unlocked.values()) {
			int listIndex = entry.getIndex();
			int index = listIndex / 32;
			if (index >= CONFIG_IDS.length) {
				continue;
			}
			values[index] |= 1 << (listIndex & 31);
		}
		for (int i = 0; i < CONFIG_IDS.length; i++) {
			setVarp(player, CONFIG_IDS[i], values[i]);
		}
	}

	/**
	 * Called when a player leaves a music zone without entering a new one.
	 */
	public void playDefault() {
		MusicEntry entry = MusicEntry.forId(DEFAULT_MUSIC_ID);
		if (entry != null) {
			play(entry);
		}
	}

	/**
	 * Replays the song.
	 */
	public void replay() {
		MusicEntry entry = MusicEntry.forId(currentMusicId);
		if (entry != null) {
			play(entry);
		}
	}

	/**
	 * Plays the song.
	 * @param entry The song.
	 */
	public void play(MusicEntry entry) {
		if (!looping || currentMusicId == entry.getId()) {
			PacketRepository.send(MusicPacket.class, new MusicContext(player, entry.getId()));
			PacketRepository.send(StringPacket.class, new StringContext(player, entry.getName() + (player.isDebug() ? (" (" + entry.getId() + ")") : ""), 187, 14));
			currentMusicId = entry.getId();
			playing = true;
		}
	}

	/**
	 * Unlocks and plays the music id.
	 * @param id The music id to unlock.
	 */
	public void unlock(int id) {
		unlock(id, true);
	}

	/**
	 * Unlocks the music id.
	 * @param id The music id to unlock.
	 * @param play If the song should be played.
	 */
	public void unlock(int id, boolean play) {
		MusicEntry entry = MusicEntry.forId(id);
		if (entry == null) {
			return;
		}
		if (!entry.getName().equals(" ") && !unlocked.containsKey(entry.getIndex())) {
			unlocked.put(entry.getIndex(), entry);
			player.getPacketDispatch().sendMessage("<col=FF0000>You have unlocked a new music track: " + entry.getName() + ".</col>");
			refreshList();
			if (!player.getEmoteManager().isUnlocked(Emotes.AIR_GUITAR) && hasAirGuitar()) {
				player.getEmoteManager().unlock(Emotes.AIR_GUITAR);
				if (unlocked.size() >= 200) {
					player.getPacketDispatch().sendMessage("You've unlocked 200 music tracks and can use a new emote!");
				} else {
					player.getPacketDispatch().sendMessage("You've unlocked all music tracks and can use a new emote!");
				}
			}
		}
		if (play) {
			play(entry);
		}
	}

	public void tick(){
		if(GameWorld.getTicks() % 20 == 0){
			if(!isPlaying()){
				try {
					PacketRepository.send(MusicPacket.class, new MusicContext(player, currentMusicId));
				} catch (Exception e){}
			}
		}
	}

	/**
	 * Toggles the looping option.
	 */
	public void toggleLooping() {
		looping = !looping;
		setVarp(player, 19, looping ? 1 : 0);
	}

	/**
	 * If music is currently playing.
	 * @return {@code True} if so.
	 */
	private boolean isMusicPlaying() {
		return currentMusicId > 0 && playing;
	}

	/**
	 * Gets the unlocked songs list.
	 * @return The unlocked.
	 */
	public Map<Integer, MusicEntry> getUnlocked() {
		return unlocked;
	}

	/**
	 * Gets the currentMusicId.
	 * @return The currentMusicId.
	 */
	public int getCurrentMusicId() {
		return currentMusicId;
	}

	/**
	 * Sets the currentMusicId.
	 * @param currentMusicId The currentMusicId to set.
	 */
	public void setCurrentMusicId(int currentMusicId) {
		this.currentMusicId = currentMusicId;
	}

	/**
	 * Gets the playing.
	 * @return The playing.
	 */
	public boolean isPlaying() {
		return playing;
	}

	/**
	 * Sets the playing.
	 * @param playing The playing to set.
	 */
	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	/**
	 * Gets the looping.
	 * @return The looping.
	 */
	public boolean isLooping() {
		return looping;
	}

	/**
	 * Sets the looping.
	 * @param looping The looping to set.
	 */
	public void setLooping(boolean looping) {
		this.looping = looping;
		setVarp(player, 19, looping ? 1 : 0);
	}
}
