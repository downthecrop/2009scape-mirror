package org.crandor.game.events;

import org.crandor.Util;

public enum GlobalEvent {
	ALCHEMY_HELLENISTIC("Receive 2 x coins when using high alchemy"), // Not implemented
	GOLDEN_RETRIEVER("All gold dropped by monsters will be automatically banked for you"), // Not implemented
	THIEVES_JACKPOT("Receive 3 x more coins when thieving"),
	GOLDEN_ESSENCE("Receive 3 x more runes when runecrafting"),
	CLONE_FEST("20 clones are been spawned in the wilderness near the mage arena."), // Not implemented
	TRY_YOUR_LUCK("Mobs will drop 40% more items when killed"),
	CRAZY_SEEDS("Mobs will drop 2 x more seeds when killed"),
	CHARMED("Mobs will drop 2 x more charms when killed"),
	XP_FEVER("Receive 2 x XP for all skills"),
	PLENTY_OF_FISH("Receive 2 x rewards when fishing"),
	HARVESTING_DOUBLES("Receive 2 x items harvested with woodcutting or mining");

	/**
	 * Represents the object of the altar.
	 */
	private final String eventName;

	/**
	 * Represents the object of the altar.
	 */
	private final String eventDescription;

	/**
	 * Represents the object of the altar.
	 */
	private Long remainingTicks;
	
	GlobalEvent(final String eventDescription) {
		this.eventName = Util.enumToString(this.name());;
		this.eventDescription = eventDescription;
		this.remainingTicks = 0L;
	}
	
	public String getName() {
		return this.eventName;
	}
	
	public String getDescription() {
		return this.eventDescription;
	}
	
	public Long getRemainingTicks() {
		return this.remainingTicks;
	}
	
	public GlobalEvent setRemainingTicks(Long ticks) {
		this.remainingTicks = ticks;
		return this;
	}
	
	public Boolean isActive() {
		return this.remainingTicks > 0;
	}

	public GlobalEvent tick() {
		remainingTicks--;
		return this;
	}

	// Start/Extend the event
	public GlobalEvent extend() {
		return extend(6000);
	}
	
	public GlobalEvent extend(int ticks) {
		remainingTicks += ticks;
		return this;
	}
	
	public GlobalEvent start() {
		return extend(6000);
	}
	
	public GlobalEvent start(int ticks) {
		extend(ticks);
		return this;
	}

}
