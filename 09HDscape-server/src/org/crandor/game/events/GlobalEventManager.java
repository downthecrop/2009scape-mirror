package org.crandor.game.events;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.system.task.Pulse;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.callback.CallBack;
import org.crandor.game.world.repository.Repository;
import org.crandor.net.amsc.MSPacketRepository;
import org.crandor.net.amsc.WorldCommunicator;
import org.crandor.tools.mysql.Results;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Class to handle donated events.
 * 
 * @author Vip3r
 * Version 1.0
 */
public class GlobalEventManager implements CallBack {
	
	private static Map<String, Long> EVENTS = new HashMap<String, Long>();
	
	private long tick = 0;

	private String lastEvent;
	private String currentEvent;
	
	public final GlobalEventManager init() {
		try {
			getEvents().put("Alchemy hellenistic", 0L);
			getEvents().put("Golden retriever", 0L);
			getEvents().put("Harvesting doubles", 0L);
			getEvents().put("Thieves jackpot", 0L);
			getEvents().put("Golden essence", 0L);
			getEvents().put("Clone Fest", 0L);
			getEvents().put("XP Fever", 0L);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public final Pulse pulse() {
		
		return new Pulse(1) {
			
			@Override
			public boolean pulse() {
				
				
				tick++;

				Iterator<Entry<String, Long>> iterator = EVENTS.entrySet().iterator();
				
				while(iterator.hasNext()) {
					Map.Entry<String, Long> entry = iterator.next();
					Long ticksRemaining = entry.getValue();
					if (entry.getValue() > 0) {
						entry.setValue(--ticksRemaining);
						if (ticksRemaining == 3000)
							message("You have 30 minutes before the " + entry.getKey() + " event ends on world " + GameWorld.getSettings().getWorldId() + ".");
							
						if (ticksRemaining <= 0) {
							message("The " + entry.getKey() + " event has now ended on world " + GameWorld.getSettings().getWorldId() + ".");
						}
						System.out.println("The " + entry.getKey() + " event has " + ticksRemaining + " ticks remaining");
					}
				}
				
				if (tick == 100) {
					tick = 0;
					save();
				}
				
				return false;
			}
			
		};
		
	}
	
	public void reActivate(String name, long time) {
		Iterator<Entry<String, Long>> iterator = EVENTS.entrySet().iterator();
		
		while(iterator.hasNext()) {
			Map.Entry<String, Long> entry = iterator.next();
			if (entry.getKey().equalsIgnoreCase(name)) {
				entry.setValue(time);
			}
		}
	}
	
	public GlobalEventManager save() {
		if (GameWorld.getDatabaseManager().update("server", "DELETE FROM `globalevents` WHERE worldid='" + GameWorld.getSettings().getWorldId() + "'") < 0)
			return this;

		Iterator<Entry<String, Long>> iterator = EVENTS.entrySet().iterator();
		
		while(iterator.hasNext()) {
			
			Map.Entry<String, Long> entry = iterator.next();
			
			if (entry.getValue() <= 0)
				continue;
			
			StringBuilder query = new StringBuilder();

			query.append("INSERT INTO `globalevents` ");
			query.append("(`eventName`,`eventTime`,`worldId`)");
			query.append(" VALUES(");
			
			query.append("'" + entry.getKey() + "'").append(",");
			query.append("'" + entry.getValue() + "'").append(",");
			query.append("'" + GameWorld.getSettings().getWorldId() + "'");
			
			query.append(")");

			GameWorld.getDatabaseManager().update("server", query.toString());

		}
		return this;
	}
	
	public GlobalEventManager load() {
		try {
			Results result = null;
			
			result = new Results(GameWorld.getDatabaseManager().query("server", "SELECT * FROM `globalevents` WHERE worldid='" + GameWorld.getSettings().getWorldId() + "'"));

			while (!result.empty()) {
				String eventName = result.string("eventName");
				String eventTime = result.string("eventTime");
				reActivate(eventName, Long.valueOf(eventTime));
			}
			
			
		} catch(Exception  e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public GlobalEventManager message(String message) {
		return message(message, true, "<col=3498db>");
	}

	public GlobalEventManager message(String message, boolean tag) {
		return message(message, tag, "<col=3498db>");
	}

	public GlobalEventManager notify(String message) {
		return message(message, true, "<col=c0392b>");
	}

	public GlobalEventManager notify(String message, boolean tag) {
		return message(message, tag, "<col=c0392b>");
	}

	public GlobalEventManager message(String message, boolean tag, String color) {
		for (Player player : Repository.getPlayers()) {
			player.getPacketDispatch().sendMessage(color + (tag ? "[Event Manager] " : "") + message);
		}
		return this;
		
	}
	
	public GlobalEventManager deactivate(String eventName) {
		return deactivate(eventName, false);
	}
	
	public GlobalEventManager deactivate(String eventName, boolean forceMessage) {
		// Only deactivate event if already active
		if (!isActive(eventName) && !forceMessage) {
			return this;
		}
		
		Iterator<Entry<String, Long>> iterator = EVENTS.entrySet().iterator();
		
		while(iterator.hasNext()) {
			Map.Entry<String, Long> entry = (Map.Entry<String, Long>) iterator.next();
			if (entry.getKey().equalsIgnoreCase(eventName)) {
				message(eventName + " event has ended.");
				entry.setValue(0L);
			}
		}
		return this;
	}
	
	public GlobalEventManager activate(String eventName) {
		return activate(eventName, null, 6000);
	}
	
	public GlobalEventManager activate(String eventName, String name) {
		return activate(eventName, name, 6000);
	}
	
	public GlobalEventManager activate(String eventName, String name, int timeToAdd) {
		if (timeToAdd <= 0) timeToAdd = 6000;
		
		Player player = Repository.getPlayerByDisplay(name);
		Boolean eventStarted = false;

		Iterator<Entry<String, Long>> iterator = EVENTS.entrySet().iterator();

		while(iterator.hasNext()) {
			Map.Entry<String, Long> entry = (Map.Entry<String, Long>) iterator.next();
			if (entry.getKey().equalsIgnoreCase(eventName)) {
				if (eventName.equalsIgnoreCase("clone fest")) {
					notify("The " + eventName + " event is live, clones are located near the mage arena.");
				} else {
					if (entry.getValue() == 0) {
						message("The " + eventName + " event has been activated" + (player == null ? "" : " by " + player.getUsername()) + ".");
					} else {
						message("The " + eventName + " event has been extended for another hour" + (player == null ? "" : " by " + player.getUsername()) + ".");
					}
				}
				entry.setValue(entry.getValue() + timeToAdd);
				eventStarted = true;
			}
		}
		if (!eventStarted) {
			player.sendMessage("Failed to activate event " + eventName + ".");
		}
		return this;
	}
	
	public GlobalEventManager activateHourly(String eventName) {
		
		if (getEvents().get(eventName) == null) {
			System.out.println("Failed to activate event " + eventName + ".");
			return this;
		}
		
		Iterator<Entry<String, Long>> iterator = EVENTS.entrySet().iterator();
		
		while(iterator.hasNext()) {
			Map.Entry<String, Long> entry = iterator.next();
			if (entry.getKey().equalsIgnoreCase(eventName)) {
				message(eventName + " is now active, and will run for an hour!");
				for (Player player : Repository.getPlayers()) {
				switch(getCurrentEvent()) {
					case "Alchemy hellenistic":
						player.getPacketDispatch().sendMessage("This event means you'll receive x2 coins when using high alchemy.");
						break;
					case "Golden retriever":
						player.getPacketDispatch().sendMessage("This event means you'll have all gold dropped by monsters banked for you.");
						break;
					case "Harvesting doubles":
						player.getPacketDispatch().sendMessages("This event means you'll receive x2 items when harvesting with woodcutting, mining", "or fishing.");
						break;
					case "Thieves jackpot":
						player.getPacketDispatch().sendMessages("This event means you'll receive 3x more coins when thieving.");
						break;
					case "Golden essence":
						player.getPacketDispatch().sendMessages("This event means you'll receive x3 more runes than normal when runecrafting.");
						break;
					case "Clone Fest":
						player.getPacketDispatch().sendMessages("This event means 20 clones have been spawned in the wilderness", "near the mage bank.");
						break;
					case "XP Fever":
						player.getPacketDispatch().sendMessages("This event means you'll receive x2 XP");
						break;

				}
				}
				entry.setValue(entry.getValue() + 6000);
			}
		}
		return this;
	}
	
	public boolean isActive(String eventName) {
		Iterator<Entry<String, Long>> iterator = EVENTS.entrySet().iterator();
		
		while(iterator.hasNext()) {
			Map.Entry<String, Long> entry = (Map.Entry<String, Long>) iterator.next();
			if (entry.getKey().equalsIgnoreCase(eventName)) {
				if (entry.getValue() > 0)
					return true;
			}
		}
		
		return false;
	}

	public GlobalEventManager alert(Player player) {
		boolean active = false;
		Iterator<Entry<String, Long>> i = EVENTS.entrySet().iterator();
		
		while(i.hasNext()) {
			Map.Entry<String, Long> entry = (Map.Entry<String, Long>) i.next();
				if (entry.getValue() > 0) {
					active = true;
			}
		}
		if (!active) {
			player.sendMessage("<col=3498db>No events are currently active.");
			return this;
		}
		player.sendMessage("<col=3498db>The following events are active:");
		Iterator<Entry<String, Long>> iterator = EVENTS.entrySet().iterator();
		
		while(iterator.hasNext()) {
			Map.Entry<String, Long> entry = (Map.Entry<String, Long>) iterator.next();
				if (entry.getValue() > 0) {
					player.sendMessage("<col=3498db> [-] " + entry.getKey() + ".");
			}
		}
		return this;
	}

	private static GlobalEventManager INSTANCE = new GlobalEventManager();
	
	public static GlobalEventManager get() {
		return INSTANCE;
	}
	
	@Override
	public boolean call() {
		GameWorld.submit(pulse());
		return true;
	}
	
	public static Map<String, Long> getEvents() {
		return EVENTS;
	}

	public String getLastEvent() {
		return lastEvent;
	}

	public void setLastEvent(String lastEvent) {
		this.lastEvent = lastEvent;
	}

	public String getCurrentEvent() {
		return currentEvent;
	}

	public void setCurrentEvent(String currentEvent) {
		this.currentEvent = currentEvent;
	}

	
}
