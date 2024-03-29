package core.game.global.report;

import core.game.node.entity.player.Player;
import core.integrations.discord.Discord;
import core.game.system.command.CommandMapping;

/**
 * Represents an abuse report to file.
 * @author 'Vexia
 */
public final class AbuseReport {

	/**
	 * The name of the reporter.
	 */
	private final String reporter;

	/**
	 * The victim.
	 */
	private final String victim;

	/**
	 * The rule.
	 */
	private final Rule rule;

	/**
	 * The messages.
	 */
	private String messages;

	/**
	 * Constructs a new {@code AbuseReport {@code Object}.
	 * @param reporter the reporter.
	 * @param victim the victim.
	 * @param rule the rule broken.
	 */
	public AbuseReport(String reporter, String victim, Rule rule) {
		this.reporter = reporter;
		this.victim = victim;
		this.rule = rule;
	}

	/**
	 * Constructs the report and dispatches it to the database.
	 * @param player the player.
	 */
	public void construct(Player player, boolean mute) {
		if (mute) {
			CommandMapping.INSTANCE.get("mute").attemptHandling(player, new String[] {"mute", victim, "48h"});
		}
		player.getPacketDispatch().sendMessage("Thank-you, your abuse report has been received.");
		Discord.postPlayerAlert(victim, "Abuse Report - " + rule.name());
	}

	/**
	 * Gets the reporter.
	 * @return The reporter.
	 */
	public String getReporter() {
		return reporter;
	}

	/**
	 * Gets the victim.
	 * @return The victim.
	 */
	public String getVictim() {
		return victim;
	}

	/**
	 * Gets the rule.
	 * @return The rule.
	 */
	public Rule getRule() {
		return rule;
	}

	/**
	 * Gets the messages.
	 * @return The messages.
	 */
	public String getMessages() {
		return messages;
	}

	/**
	 * Sets the messages.
	 * @param messages The messages to set.
	 */
	public void setMessages(String messages) {
		this.messages = messages;
	}

}
