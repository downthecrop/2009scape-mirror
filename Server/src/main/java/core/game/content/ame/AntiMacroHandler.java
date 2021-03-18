package core.game.content.ame;

import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.world.map.zone.ZoneRestriction;
import core.game.world.map.zone.impl.WildernessZone;
import core.tools.RandomFunction;
import rs09.game.content.ame.RandomEventNPC;
import rs09.game.system.SystemLogger;
import rs09.game.world.GameWorld;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles anti-macroing.
 * @author Emperor
 * @author ceik
 */
public final class AntiMacroHandler {

	/**
	 * The delay between periods in which it attempts to spawn a random event.
	 * For example, if delay is 500, it will wait 500 ticks before it starts trying to spawn a random event.
	 */
	private static final int DELAY = 3000; // 2000 ticks = 20 minutes

	/**
	 * The chance that, after the delay has passed, a random event has of occuring each tick. Takes the form of (1 / CHANCE)
	 */
	private static final int CHANCE = 750; // 1/500 chance, average of 500 ticks (roughly 5 minutes) before a random will spawn.

	/**
	 * Whether randoms are disabled for this player
	 */
	public boolean isDisabled;

	/**
	 * The list of anti-macro events.
	 */
	public static final Map<String, AntiMacroEvent> EVENTS = new HashMap<>();

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The time at which the next series of attempts to spawn a random will occur. Usually GameWorld.getTicks() + DELAY
	 */
	private int nextPulse;

	/**
	 * The current event.
	 */
	private RandomEventNPC event;

	public ExperienceMonitor[] monitors = new ExperienceMonitor[Skills.SKILL_NAME.length];

	/**
	 * Constructs a new {@code AntiMacroHandler} {@code Object}.
	 * @param player The player.
	 */
	public AntiMacroHandler(Player player) {
		this.player = player;
	}

	/**
	 * Gets called every game pulse.
	 */
	public void pulse() {
		if (GameWorld.getTicks() < nextPulse) {
			return;
		}
		if (!player.getLocks().isInteractionLocked() && !player.getLocks().isTeleportLocked() && !player.getLocks().isMovementLocked() && !isDisabled && !player.isArtificial()) {
			int roll = RandomFunction.random(0,CHANCE);
			int neededRoll = 1 + (CHANCE - RandomFunction.random(CHANCE - 10));
			boolean spawnEvent = roll ==  neededRoll; //checks if the chance is hit this tick
			//SystemLogger.log("roll: " + roll + " needed roll: " + neededRoll);
			int highestIndex = 0;
			int highestAmount = 0;
			if(spawnEvent){
				SystemLogger.logInfo("Anti-Macro: Trying to get event for " + player.getUsername());
				for(int i = 0; i < monitors.length; i++){
					try {
						if (monitors[i].getExperienceAmount() > highestAmount) {
							highestIndex = i;
							highestAmount = monitors[i].getExperienceAmount();
							monitors[i].setExperienceAmount(0);
						}
					} catch (Exception e){}
				}
				fireEvent(highestIndex);
			}
		}
	}
	/**
	 * Initializes the anti macro event handler.
	 */
	public void init() {
		isDisabled = player.getAttribute("randoms:disabled",false);
		if(isDisabled){
			nextPulse = -1;
			return;
		} else {
			nextPulse = GameWorld.getTicks() + DELAY;
		}
		if(!player.isArtificial() && !isDisabled) {
			SystemLogger.logInfo("Anti-Macro: Initialized anti-macro handler for " + player.getUsername());
		}
	}

	/**
	 * Registers a new anti-macro event.
	 * @param event The event.
	 */
	public static void register(AntiMacroEvent event) {
		event.register();
		EVENTS.put(event.getName(), event);
	}

	/**
	 * Checks if the npc is part of the event.
	 * @param npc the npc.
	 * @return {@code True} if so.
	 * @param message the message.
	 */
	public boolean isNPC(NPC npc, boolean message) {
		if (!hasEvent()) {
			if (message) {
				player.getPacketDispatch().sendMessage("They don't seem interested in talking to you.");
			}
			return false;
		}
		AntiMacroNPC n = (AntiMacroNPC) npc;
		/*if (n.getEvent() != event) {
			if (message) {
				player.getPacketDispatch().sendMessage("They don't seem interested in talking to you.");
			}
			return false;
		}*/
		return true;
	}

	/**
	 * Fires an anti-macro event.
	 * @param name The name of the event to start.
	 * @param args The arguments.
	 * @return {@code True} if the event has been fired.
	 */
	public boolean fireEvent(String name, Object... args) {
		nextPulse = GameWorld.getTicks() + DELAY;
		if(WildernessZone.isInZone(player)){
			return false;
		}
		if (hasEvent() || player.getZoneMonitor().isRestricted(ZoneRestriction.RANDOM_EVENTS) || player.isArtificial()) {
			return false;
		}
		AntiMacroEvent event = EVENTS.get(name);
		if (event == null) {
			if (GameWorld.getSettings().isDevMode()) {
				throw new IllegalArgumentException("Could not find event " + name + "!");
			} else {
				return false;
			}
		}
		event = event.create(player);
		if (!event.start(player, false, args)) {
			return false;
		}
		//resetTrigger();
		//this.event = event;
		return true;
	}

	/**
	 * Fires an event.
	 * @param skillId The skill id, -1 for default events..
	 * @param args The arguments.
	 * @return {@code True} if the event has been fired.
	 */
	public boolean fireEvent(int skillId, Object... args) {
		/*nextPulse = DELAY + GameWorld.getTicks();
		if (hasEvent() || EVENTS.isEmpty() || player.getZoneMonitor().isRestricted(ZoneRestriction.RANDOM_EVENTS) || player.isArtificial()) {
			return false;
		}
		event = getRandomEvent(skillId);
		if (event != null) {
			if ((event = event.create(player)).start(player, false, args)) {
				if(event == null) return true;
				System.out.println("Anti-Macro: Firing event " + event.getName() + " for player: " + player.getUsername());
				//resetTrigger();
				return true;
			}
			event = null;
		}*/
		return false;
	}

	/**
	 * Gets an anti marco event.
	 * @param skillId the skillId.
	 * @return {@code AntiMacroEvent} the event.
	 */
	public AntiMacroEvent getRandomEvent(int skillId) {
		int index = RandomFunction.random(EVENTS.size());
		int count = 0;
		AntiMacroEvent event = null;
		AntiMacroEvent[] events = EVENTS.values().toArray(new AntiMacroEvent[EVENTS.size()]);
		while (!(event = events[index]).canFire(skillId)) {
			if (count++ >= events.length) {
				event = null;
				break;
			}
			index = (index + 1) % events.length;
		}
		return event;
	}

	/**
	 * Checks if the player has an anti-macro event running.
	 * @return {@code True} if so.
	 */
	public boolean hasEvent() {
		return event != null;
	}

	/**
	 * Gets the player.
	 * @return The player.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the event.
	 * @return The event.
	 */
	public RandomEventNPC getEvent() {
		return event;
	}

	/**
	 * Sets the event.
	 * @param event The event to set.
	 */
	public void setEvent(RandomEventNPC event) {
		this.event = event;
	}
}