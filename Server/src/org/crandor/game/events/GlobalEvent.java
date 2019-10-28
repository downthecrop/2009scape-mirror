package org.crandor.game.events;

public class GlobalEvent {
	
	private String eventName;
	private long remainingTicks;
	
	public String getEventName() {
		return this.eventName;
	}
	
	public long getRemainingTicks() {
		return this.remainingTicks;
	}
	
	public GlobalEvent(String eventName, long ticks) {
		this.eventName = eventName;
		this.remainingTicks = ticks;
	}

	public GlobalEvent process() {
		remainingTicks--;
		return this;
	}
	
	public GlobalEvent extend() {
		remainingTicks += 6000;
		return this;
	}

}
