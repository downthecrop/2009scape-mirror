package core.game.content.activity.gnomecopter;

import core.game.component.Component;
import core.game.node.entity.player.Player;

/**
 * The information signs for the Gnomecopter tours.
 * @author Emperor
 */
public enum GCInformationSign {

	/**
	 * The gnomecopters entrance sign.
	 */
	ENTRANCE("~ Gnomecopter Tours ~", "Welcome to Gnomecopter", "Tours: the unique flying", "experience!", "", "If you're a new flier, talk to", "<col=FF0000>Hieronymous</col> and prepare to be", "amazed by this triumph of", "gnomic engineering.", "", "", "Warning: all riders must be at", "least 2ft, 6ins tall.");

	/**
	 * The button text.
	 */
	private final String button;

	/**
	 * The info.
	 */
	private final String[] info;

	/**
	 * Constructs a new {@code GCInformationSign} {@code Object}.
	 * @param button The button text.
	 * @param info The information.
	 */
	private GCInformationSign(String button, String... info) {
		this.button = button;
		this.info = info;
	}

	/**
	 * Reads the information sign.
	 * @param player The player.
	 */
	public void read(Player player) {
		player.getInterfaceManager().openSingleTab(new Component(723));
		player.getPacketDispatch().sendString(button, 723, 9);
		String information = info[0];
		for (int i = 1; i < info.length; i++) {
			information += "<br>" + info[i];
		}
		player.getPacketDispatch().sendString(information, 723, 10);
	}
}