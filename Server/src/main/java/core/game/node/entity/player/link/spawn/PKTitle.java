package core.game.node.entity.player.link.spawn;

import core.game.node.entity.player.Player;
import rs09.game.world.repository.Repository;
import core.tools.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A PK title received from a certain amount of kills.
 * @author Vexia
 *
 */
public enum PKTitle {
	NOOB(0),
	PEASANT(5),
	SKIRMISHER(20),
	KNIGHT(50),
	WARRIOR(80),
	BOUNTY_HUNTER(100),
	HERO(150),
	SKULLCRACKER(250),
	DESTROYER(400),
	DOMINATOR(600),
	REAPER(800, "DF7401"),
	ASSASSIN(1000, "21610B"),
	MASTER(1500, "0431B4"),
	GODLY(2000, "FF0000");

	/**
	 * The kills needed for the title.
	 */
	private final int kills;
	
	/**
	 * The color for this title.
	 */
	private final String titleColor;

	/**
	 * Constructs a new {@Code PKTitle} {@Code Object}
	 * @param kills the points.
	 * @param titleColor the title color.
	 */
	private PKTitle(int kills, String titleColor) {
		this.kills = kills;
		this.titleColor = titleColor;
	}
	
	/**
	 * Constructs a new {@Code PKTitle} {@Code Object}
	 * @param points the points.
	 */
	private PKTitle(int points) {
		this(points, "874b93");
	}
	
	/**
	 * Checks the title for a player.
	 */
	public static void checkTitle(Player player) {
		PKTitle title = null;
		int kills = player.getSavedData().getSpawnData().getKills();
		for (PKTitle t : values()) {
			if (kills >= t.getKills()) {
				title = t;
			}
		}
		if (title.ordinal() > player.getSavedData().getSpawnData().getTitle().ordinal() && player.getSavedData().getSpawnData().getKills() <= title.getKills()) {
			player.getSavedData().getSpawnData().setTitle(title);
			if (!title.getTitleColor().equals("874b93")) {
				Repository.sendNews("<img=10><col=CC6600>News: " + player.getUsername() + " has just unlocked the title: " + title.getName() + "!");
			}
			player.getAppearance().sync();
		}
	}
	
	/**
	 * Gets the titles.
	 * @param player the player.
	 * @return the titles.
	 */
	public static PKTitle[] getTitles(Player player) {
		int kills = player.getSavedData().getSpawnData().getKills();
		List<PKTitle> titles = new ArrayList<>(20);
		for (PKTitle t : values()) {
			if (kills >= t.getKills()) {
				titles.add(t);
			}
		}
		return titles.toArray(new PKTitle[] {});
	}
	
	/**
	 * Gets the name of the title.
	 * @return the name.
	 */
	public String getName() {
		return StringUtils.formatDisplayName(name().toLowerCase().replace("_", ""));
	}

	/**
	 * Gets the points.
	 * @return the points
	 */
	public int getKills() {
		return kills;
	}

	/**
	 * Gets the titleColor.
	 * @return the titleColor
	 */
	public String getTitleColor() {
		return titleColor;
	}
	
	
}
