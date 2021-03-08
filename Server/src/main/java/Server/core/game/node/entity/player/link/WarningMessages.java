package core.game.node.entity.player.link;

import core.game.component.Component;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.login.SavingModule;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the warning messages of a player.
 * @author Vexia
 */
public final class WarningMessages implements SavingModule {

	/**
	 * Represents the config id.
	 */
	private static final int CONFIG = 1045;

	/**
	 * Represents the warning messages.
	 */
	private final List<WarningMessage> messages = new ArrayList<>();

	@Override
	public void save(ByteBuffer buffer) {

	}

	@Override
	public void parse(ByteBuffer buffer) {

	}

	/**
	 * Method used to open the warning messages.
	 * @param player the player.
	 */
	public void open(final Player player) {
		player.getInterfaceManager().open(new Component(583));
		refresh(player);
	}

	/**
	 * Method used to refresh the config.
	 * @param player the player.
	 */
	private void refresh(final Player player) {
		player.getConfigManager().set(CONFIG, getConfigValue(), true);
	}

	/**
	 * Gets a warning message by its index.
	 * @param index the index.
	 * @return the warning message.
	 */
	public WarningMessage getMessage(int index) {
		for (WarningMessage m : messages) {
			if (m.getIndex() == index) {
				return m;
			}
		}
		WarningMessage message = new WarningMessage(index);
		messages.add(message);
		return message;
	}

	/**
	 * Gets the config value.
	 * @return the value.
	 */
	private int getConfigValue() {
		return 0;
	}

	/**
	 * Represents a warning message.
	 * @author 'Vexia
	 */
	public final class WarningMessage implements SavingModule {

		/**
		 * Represents the button index.
		 */
		private final int index;

		/**
		 * Constructs a new {@code WarningMessage} {@code Object}.
		 * @param index the index.
		 */
		public WarningMessage(final int index) {
			this.index = index;
		}

		@Override
		public void save(ByteBuffer buffer) {

		}

		@Override
		public void parse(ByteBuffer buffer) {

		}

		/**
		 * Method used to toggle this warning message.
		 * @param player the player.
		 */
		public void toggle(final Player player) {
			if (!isActive()) {
				player.getPacketDispatch().sendMessage("You cannot toggle the warning screen on or off. You need to go to the area it");
				player.getPacketDispatch().sendMessage("is linked to enough times to have the option to do so.");
				return;
			}
			final boolean on = !isToggled();
			refresh(player);
			player.getPacketDispatch().sendMessage("You have toggled this warning screen " + (on ? "on" : "off") + ". You will " + (on ? "see this interface again." : "no longer see this warning screen."));
		}

		/**
		 * Checks if the message is toggled.
		 * @return {@code True} if so.
		 */
		private boolean isToggled() {
			return false;
		}

		/**
		 * Checks if the message is toggled.
		 * @return {@code True} if so.
		 */
		private boolean isActive() {
			return false;
		}

		/**
		 * Gets the index.
		 * @return The index.
		 */
		public int getIndex() {
			return index;
		}

	}

}
