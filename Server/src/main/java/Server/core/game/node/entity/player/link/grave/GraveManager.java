package core.game.node.entity.player.link.grave;

import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.Rights;
import core.game.node.entity.player.info.login.SavingModule;
import core.game.node.entity.player.link.HintIconManager;
import core.game.node.entity.player.link.prayer.PrayerType;
import core.game.node.item.GroundItem;
import core.game.world.GameWorld;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the players grave stone.
 * @author Vexia
 */
public class GraveManager implements SavingModule {

	/**
	 * The current grave stones in the world.
	 */
	private static final Map<String, NPC> graves = new HashMap<>();

	/**
	 * The player instance.
	 */
	private final Player player;

	/**
	 * The grave type.
	 */
	private GraveType type = GraveType.MEMORIAL_PLAQUE;

	/**
	 * The current gravestone.
	 */
	private NPC grave;

	/**
	 * Constructs a new {@Code GraveManager} {@Code Object}
	 * @param player the player.
	 */
	public GraveManager(Player player) {
		this.player = player;
	}

	@Override
	public void save(ByteBuffer buffer) {
		buffer.putInt(type.ordinal());
	}

	@Override
	public void parse(ByteBuffer buffer) {
		if (buffer.hasRemaining()) {
			type = GraveType.values()[buffer.getInt()];
		}
	}

	/**
	 * Creates a grave.
	 * @param ticks the ticks.
	 * @param items the items.
	 */
	public void create(int ticks, List<GroundItem> items) {
		if (hasGrave()) {
			grave.clear();
			player.sendMessage("Your previous gravestone has collapsed.");
		}
		NPC npc = NPC.create(type.getNpcId(), player.getLocation(), player.getName(), GameWorld.getTicks() + ticks, items, type, player.getUsername());
		npc.init();
		setGrave(npc);
	}

	/**
	 * Updates the players grave items.
	 */
	public void update() {
		NPC npc = graves.get(player.getName());
		if (npc != null && npc.isActive()) {
			AbstractNPC n = (AbstractNPC) npc;
			n.fireEvent("updateItems", player);
			HintIconManager.registerHintIcon(player, n);
		}
	}

	/**
	 * Checks if a gravestone is generateable at this time.
	 * @return {@code True} if so.
	 */
	public boolean generateable() {
		if (player.getDetails().getRights() == Rights.ADMINISTRATOR && GameWorld.getSettings().isHosted()) {
			return false;
		}
		if (player.getSkullManager().isWilderness()) {
			return false;
		}
		if (player.getInventory().itemCount() + player.getEquipment().itemCount() <= (player.getPrayer().get(PrayerType.PROTECT_ITEMS) ? 4 : 3)) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if the player has an active grave.
	 * @return {@code True} if so.
	 */
	public boolean hasGrave() {
		return grave != null && grave.isActive();
	}

	/**
	 * Gets the type.
	 * @return the type
	 */
	public GraveType getType() {
		return type;
	}

	/**
	 * Sets the grave type.
	 * @param type the type to set.
	 */
	public void setType(GraveType type) {
		this.type = type;
	}

	/**
	 * Gets the player.
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the grave.
	 * @return the grave
	 */
	public NPC getGrave() {
		return grave;
	}

	/**
	 * Sets the bagrave.
	 * @param grave the grave to set.
	 */
	public void setGrave(NPC grave) {
		this.grave = grave;
	}

	/**
	 * Gets the graves.
	 * @return the graves
	 */
	public static Map<String, NPC> getGraves() {
		return graves;
	}
}
