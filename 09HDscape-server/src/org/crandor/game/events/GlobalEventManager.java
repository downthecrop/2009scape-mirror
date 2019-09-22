package org.crandor.game.events;

import org.crandor.Util;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.system.task.Pulse;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.callback.CallBack;
import org.crandor.game.world.repository.Repository;
import org.crandor.tools.mysql.Results;

/**
 * Class to handle donated events.
 * 
 * @author Vip3r
 * Version 1.0
 */
public class GlobalEventManager implements CallBack {
	
	private long tick = 0;

	private GlobalEvent lastEvent;
	private GlobalEvent currentEvent;
	
	public final GlobalEventManager init() {
		return this;
	}
	
	public final Pulse pulse() {
		
		return new Pulse(1) {
			
			@Override
			public boolean pulse() {
				tick++;
				for(GlobalEvent event : GlobalEvent.values()){
					Long ticksRemaining = event.getRemainingTicks();
					if (ticksRemaining > 0) {
						event.tick();
						--ticksRemaining;
						if (ticksRemaining <= 0)
							message("The " + event.getName() + " event has now ended.");
						else if (ticksRemaining % 3000 == 0)
							message("You have " + Math.round(ticksRemaining / 100) + " minutes before the " + event.getName() + " event ends.");
							
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
	
	public void reActivate(GlobalEvent event, long time) {
		event.setRemainingTicks(time);
	}
	
	public GlobalEventManager save() {
		for(GlobalEvent event : GlobalEvent.values()){

			StringBuilder query = new StringBuilder();

			query.append("INSERT INTO `global_events` ");
			query.append("(`eventName`,`eventTime`,`worldId`)");
			query.append(" VALUES(");
			
			query.append("'" + event.getName() + "'").append(",");
			query.append("'" + event.getRemainingTicks() + "'").append(",");
			query.append("'" + GameWorld.getSettings().getWorldId() + "'");
			
			query.append(")");

			query.append(" ON DUPLICATE KEY UPDATE ");
			query.append("eventTime='" + event.getRemainingTicks() + "'");

			GameWorld.getDatabaseManager().update("server", query.toString());

		}
		return this;
	}
	
	public GlobalEventManager load() {
		try {
			Results result = new Results(GameWorld.getDatabaseManager().query("server", "SELECT * FROM `global_events` WHERE worldid='" + GameWorld.getSettings().getWorldId() + "'"));

			while (!result.empty()) {
				String eventName = result.string("eventName");
				String eventTime = result.string("eventTime");
				GlobalEvent event = getEvent(eventName);
				reActivate(event, Long.valueOf(eventTime));
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
		return message(message, true, "<col=3498db>");
	}

	public GlobalEventManager notify(String message, boolean tag) {
		return message(message, tag, "<col=3498db>");
	}

	public GlobalEventManager message(String message, boolean tag, String color) {
		for (Player player : Repository.getPlayers()) {
			player.getPacketDispatch().sendMessage(color + (tag ? "[Event Manager] " : "") + message);
		}
		return this;
	}
	
	public GlobalEventManager deactivate(GlobalEvent event) {
		// Only deactivate event if already active
		if (!event.isActive()) {
			return this;
		}

		// Event will end in 2 ticks
		event.setRemainingTicks(2L);
		return this;
	}
	
	public GlobalEventManager activate(GlobalEvent event) {
		return activate(event, null, 6000);
	}
	
	public GlobalEventManager activate(GlobalEvent event, String name) {
		return activate(event, name, 6000);
	}
	
	public GlobalEventManager activate(GlobalEvent event, String name, int timeToAdd) {
		if (timeToAdd <= 0) timeToAdd = 6000;

		Player player = Repository.getPlayerByDisplay(name);
		
		StringBuilder message = new StringBuilder();
		message.append("The " + event.getName() + " event has been ");
		message.append(event.isActive() ? "extended by" : "activated for");
		message.append(" " + Math.round(timeToAdd / 100) + " minutes");
		if (player != null) {
			message.append(" by " + player.getUsername());
		}
		message.append(".");

		// start the event after building the string
		event.start(timeToAdd);
		message(message.toString());
		player.getPacketDispatch().sendMessage(event.getDescription());

		return this;
	}
	
	public GlobalEventManager activateHourly(GlobalEvent event) {
		event.start(6000);
		message(event.getName() + " event is now active, and will run for an hour!");
		for (Player player : Repository.getPlayers()) {
			player.getPacketDispatch().sendMessage(event.getDescription());
		}
		return this;
	}
	
	public boolean isActive(GlobalEvent event) {
		return event.isActive();
	}

	public GlobalEventManager alert(Player player) {
		boolean active = false;

		for(GlobalEvent event : GlobalEvent.values()){
			if (event.isActive()) {
				active = true;
			}
		}
		
		if (!active) {
			player.sendMessage("<col=3498db>No events are currently active.");
			return this;
		}
		
		player.sendMessage("<col=3498db>The following events are active:");

		for(GlobalEvent event : GlobalEvent.values()){
			if (event.isActive()) {
				player.sendMessage("<col=3498db> [-] " + event.getName() + ".");
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
	
	public static GlobalEvent getEvent(String name) {
		for(GlobalEvent event : GlobalEvent.values()){
			if (event.getName().equalsIgnoreCase(name) || event.name().equalsIgnoreCase(Util.strToEnum(name)))
				return event;
		}
		return null;
	}

	public GlobalEvent getLastEvent() {
		return lastEvent;
	}

	public void setLastEvent(GlobalEvent event) {
		this.lastEvent = event;
	}

	public GlobalEvent getCurrentEvent() {
		return currentEvent;
	}

	public void setCurrentEvent(GlobalEvent event) {
		this.currentEvent = event;
	}

	
}
