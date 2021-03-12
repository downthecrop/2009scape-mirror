package core.game.content.ttrail;

import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.emote.Emotes;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.update.flag.context.Graphics;

/**
 * Handles an emote clue scroll.
 * @author Vexia
 */
public abstract class EmoteClueScroll extends ClueScrollPlugin {

	/**
	 * The emote id.
	 */
	private final Emotes emote;

	/**
	 * The commening emote.
	 */
	private final Emotes commenceEmote;

	/**
	 * The equipment ids.
	 */
	private final int[][] equipment;

	/**
	 * The clue message.
	 */
	private final String clue;

	/**
	 * Constructs a new {@Code EmoteClueScroll} {@Code Object}
	 * @param name the name.
	 * @param clueId the clue id.
	 * @param level the level.
	 * @param emote the emote.
	 * @param commenceEmote the emote.
	 * @param equipment the equipment.
	 * @param clue the clue.
	 * @param borders the borders.
	 */
	public EmoteClueScroll(String name, int clueId, ClueLevel level, Emotes emote, Emotes commenceEmote, int[][] equipment, final String clue, ZoneBorders... borders) {
		super(name, clueId, level, 345, borders);
		this.emote = emote;
		this.commenceEmote = commenceEmote;
		this.equipment = equipment;
		this.clue = clue;
	}

	@Override
	public boolean actionButton(Player player, int interfaceId, int buttonId, int slot, int itemId, int opcode) {
		if (!player.getInventory().contains(clueId, 1)) {
			return false;
		}
		final Emotes emote = Emotes.forId(buttonId);
		if (emote == null) {
			return false;
		}
		if (emote == this.emote) {
			NPC oldUri = player.getAttribute("uri", null);
			if (oldUri != null && oldUri.isActive()) {
				return false;
			}
			spawnUri(player);
		} else if (emote == commenceEmote) {
			player.setAttribute("commence-emote", true);
		}
		if(this.emote == emote) {
			return super.actionButton(player, interfaceId, buttonId, slot, itemId, opcode);
		} else {
			return false;
		}
	}

	@Override
	public void read(Player player) {
		for (int i = 1; i < 9; i++) {
			player.getPacketDispatch().sendString("", interfaceId, i);
		}
		super.read(player);
		player.getPacketDispatch().sendString(clue.replace("<br>", "<br><br>"), interfaceId, 1);
	}

	/**
	 * Spawns the uri npc for the player.
	 * @param player the player.
	 */
	public void spawnUri(Player player) {
		boolean doubleAgent = level == ClueLevel.HARD && player.getAttribute("killed-agent", 0) != clueId;
		//changed  this
		int id = 5141;
		if (doubleAgent) {
			boolean wilderness = player.getSkullManager().isWilderness();
			if (wilderness) {
				id = 5141;
				doubleAgent = false;
			} else {
				id = 5145;
			}
		}
		final NPC uri = NPC.create(id, player.getLocation().transform(1, 0, 0));
		player.setAttribute("uri", uri);
		player.removeAttribute("commence-emote");
		uri.setAttribute("double-agent", doubleAgent);
		uri.setAttribute("player", player);
		uri.setAttribute("clue", this);
		uri.init();
		uri.graphics(Graphics.create(86));
		uri.faceTemporary(player, 1);
		if (doubleAgent) {
			uri.sendChat("I expect you to die!");
			uri.getProperties().getCombatPulse().attack(player);
		}
	}

	/**
	 * Checks if a commence emote is needed.
	 * @return {@code True} if so.
	 */
	public boolean hasCommencEmote() {
		return commenceEmote != null;
	}

	/**
	 * Gets the emote.
	 * @return the emote.
	 */
	public Emotes getEmote() {
		return emote;
	}

	/**
	 * Gets the bequipment.
	 * @return the equipment
	 */
	public int[][] getEquipment() {
		return equipment;
	}

	/**
	 * Gets the bcommenceEmote.
	 * @return the commenceEmote
	 */
	public Emotes getCommenceEmote() {
		return commenceEmote;
	}

}
