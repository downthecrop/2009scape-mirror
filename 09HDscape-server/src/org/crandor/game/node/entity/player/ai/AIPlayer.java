package org.crandor.game.node.entity.player.ai;

import org.crandor.ServerConstants;
import org.crandor.game.interaction.DestinationFlag;
import org.crandor.game.interaction.MovementPulse;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.info.PlayerDetails;
import org.crandor.game.world.map.Location;
import org.crandor.tools.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an <b>A</b>rtificial <b>I</b>ntelligent <b>P</b>layer.
 * @author Emperor
 */
public class AIPlayer extends Player {

	/**
	 * The current UID.
	 */
	private static int currentUID = 0x1;

	/**
	 * The active Artificial intelligent players mapping.
	 */
	private static final Map<Integer, AIPlayer> botMapping = new HashMap<>();

	/**
	 * The aip control dialogue.
	 */
	private static final AIPControlDialogue CONTROL_DIAL = new AIPControlDialogue();

	/**
	 * The AIP's UID.
	 */
	private final int uid;

	/**
	 * The start location of the AIP.
	 */
	private final Location startLocation;

	/**
	 * The username.
	 */
	private String username;

	/**
	 * The player controlling this AIP.
	 */
	private Player controler;
	
	/**
	 * Constructs a new {@code AIPlayer} {@code Object}.
	 * @param name The name of the AIP.
	 * @param l The location.
	 */
	@SuppressWarnings("deprecation")
	public AIPlayer(String name, Location l) {
		super(new PlayerDetails(/*"/aip" + (currentUID + 1) + ":" + */name));
		super.setLocation(startLocation = l);
		super.artificial = true;
		super.getDetails().setSession(ArtificialSession.getSingleton());
		this.username = StringUtils.formatDisplayName(name);
		this.uid = currentUID++;
	}

	@Override
	public void init() {
		getProperties().setSpawnLocation(/*startLocation*/ ServerConstants.HOME_LOCATION);
		getInterfaceManager().openDefaultTabs();
		getSession().setObject(this);
		botMapping.put(uid, this);
		super.init();
		getSettings().setRunToggled(true);
		/*getInteraction().set(new Option("Control", 7).setHandler(new OptionHandler() {
			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				return null;
			}

			@Override
			public boolean handle(Player p, Node node, String option) {
				DialoguePlugin dial = CONTROL_DIAL.newInstance(p);
				if (dial != null && dial.open(AIPlayer.this)) {
					p.getDialogueInterpreter().setDialogue(dial);
				}
				return true;
			}

			@Override
			public boolean isWalk() {
				return false;
			}

		}));*/
	}

	/**
	 * Handles the following.
	 * @param e The entity to follow.
	 */
	public void follow(final Entity e) {
		getPulseManager().run(new MovementPulse(this, e, DestinationFlag.FOLLOW_ENTITY) {
			@Override
			public boolean pulse() {
				face(e);
				return false;
			}
		}, "movement");
	}

	@Override
	public void clear() {
		botMapping.remove(uid);
		super.clear();
	}

	@Override
	public void reset() {
		if (getPlayerFlags().isUpdateSceneGraph()) {
			getPlayerFlags().setLastSceneGraph(getLocation());
		}
		super.reset();
	}
	
	@Override
	public void finalizeDeath(Entity killer) {
		super.finalizeDeath(killer);
		this.setAttribute("dead", true);
	}

	/**
	 * Gets the UID.
	 * @return the UID.
	 */
	public int getUid() {
		return uid;
	}

	/**
	 * Deregisters an AIP.
	 * @param uid The player's UID.
	 */
	public static void deregister(int uid) {
		if (!botMapping.containsKey(uid)) {
			System.err.println("Could not deregister AIP#" + uid + ": UID not added to the mapping!");
			return;
		}
		AIPlayer player = botMapping.get(uid);
		if (player != null) {
			player.clear();
		}
	}

	@Override
	public String getUsername() {
		return username;
	}

	/**
	 * Gets the AIP for the given UID.
	 * @param uid The UID.
	 * @return The AIPlayer.
	 */
	public static AIPlayer get(int uid) {
		return botMapping.get(uid);
	}

	/**
	 * @return the startLocation.
	 */
	public Location getStartLocation() {
		return startLocation;
	}

	/**
	 * Gets the controler.
	 * @return The controler.
	 */
	public Player getControler() {
		return controler;
	}

	/**
	 * Sets the controler.
	 * @param controler The controler to set.
	 */
	public void setControler(Player controler) {
		this.controler = controler;
	}
	
}