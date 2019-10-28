package org.crandor.game.events;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.system.task.Pulse;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.callback.CallBack;
import org.crandor.game.world.repository.Repository;
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
					if (entry.getValue() > 0) {
						entry.setValue(entry.getValue() - 1);
						if (entry.getValue() == 3000)
							message("You have 30 minutes before " + entry.getKey() + " ends on world " + GameWorld.getSettings().getWorldId() + ".");
							
						if (entry.getValue() <= 0) {
							message("The event " + entry.getKey() + " has now ended on world " + GameWorld.getSettings().getWorldId() + ".");
						}
					}
				}
				
				if (tick == 50) {
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
		return message(message, true, "<col=027fc7>");
	}

	public GlobalEventManager message(String message, boolean tag) {
		return message(message, tag, "<col=027fc7>");
	}

	public GlobalEventManager notify(String message) {
		return message(message, true, "<col=800000>");
	}

	public GlobalEventManager notify(String message, boolean tag) {
		return message(message, tag, "<col=800000>");
	}



	/*
	 * getEvents().put("Alchemy hellenistic", 0L);
			getEvents().put("Golden retriever", 0L);
			getEvents().put("Harvesting doubles", 0L);
			getEvents().put("Thieves jackpot", 0L);
			getEvents().put("Golden essence", 0L);
			
	 */
	public GlobalEventManager message(String message, boolean tag, String color) {
		/*if (WorldCommunicator.isEnabled()) {
			MSPacketRepository.sendWorldMessage((tag ? "<col=027fc7>[Event Manager] - " : "")+ message);
		} else {*/
			for (Player player : Repository.getPlayers()) {
				player.getPacketDispatch().sendMessage(color + (tag ? "[Event Manager] - " : "") + message);
			}
	//}
		return this;
		
	}
	
	
	public GlobalEventManager deactivate(String eventName) {
		
		if (getEvents().get(eventName) == null) {
			System.out.println("Failed to deactivate event " + eventName + ".");
			return this;
		}
		
		Iterator<Entry<String, Long>> iterator = EVENTS.entrySet().iterator();
		
		while(iterator.hasNext()) {
			Map.Entry<String, Long> entry = (Map.Entry<String, Long>) iterator.next();
			if (entry.getKey().equalsIgnoreCase(eventName)) {
				message(eventName + " has ended. A new event will begin soon.");
				entry.setValue(0L);
			}
		}
		return this;
	}
	
	public GlobalEventManager activate(String eventName, String name) {
		
		Player p = Repository.getPlayerByDisplay(name);
		if (getEvents().get(eventName) == null) {
			System.out.println("Failed to activate event " + eventName + ".");
			return this;
		}
		if (p == null && !eventName.equalsIgnoreCase("clone fest")) {
			System.out.println("Couldnt activate event; " + name + " couldnt be found.");
			return this;
		}

		Iterator<Entry<String, Long>> iterator = EVENTS.entrySet().iterator();

		while(iterator.hasNext()) {
			Map.Entry<String, Long> entry = (Map.Entry<String, Long>) iterator.next();
			if (entry.getKey().equalsIgnoreCase(eventName)) {
				if (eventName.equalsIgnoreCase("clone fest")) {
					notify("The event " + eventName + " is live, clones are located near the mage");
					notify("bank on world " + GameWorld.getSettings().getWorldId() + ".", false);
				} else {
					if (entry.getValue() != 0) {
						message("The event " + eventName + " has been extended for another hour by " + (p == null ? " " : p.getUsername() + " "));
						message("on world " + GameWorld.getSettings().getWorldId() + ".", false);
					} else {
						message("The event " + eventName + " has been activated by " + (p == null ? " " : p.getUsername() + " ") + "on world " + GameWorld.getSettings().getWorldId() + ".");
					}
				}
				entry.setValue(entry.getValue() + 6000);
			}
		}
		return this;
	}
	
	
	/*
	 * getEvents().put("Alchemy hellenistic", 0L);
			getEvents().put("Golden retriever", 0L);
			getEvents().put("Harvesting doubles", 0L);
			getEvents().put("Thieves jackpot", 0L);
			getEvents().put("Golden essence", 0L);
			
	 */
	
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
						player.getPacketDispatch().sendMessages("This event means you'll receive 300% more coins when thieving.");
						break;
					case "Golden essence":
						player.getPacketDispatch().sendMessages("This event means you'll receive x3 more runes than normal when runecrafting.");
						break;
					case "Clone Fest":
						player.getPacketDispatch().sendMessages("This event means 20 clones have been spawned in the wilderness", "near the mage bank.");
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
		if (active)
			player.sendMessage("<col=027fc7>The following events are active:");
		Iterator<Entry<String, Long>> iterator = EVENTS.entrySet().iterator();
		
		while(iterator.hasNext()) {
			Map.Entry<String, Long> entry = (Map.Entry<String, Long>) iterator.next();
				if (entry.getValue() > 0) {
					player.sendMessage("<col=027fc7> [-] " + entry.getKey() + ".");
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
