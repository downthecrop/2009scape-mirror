package core.game.content.quest.miniquest.barcrawl;

import api.LoginListener;
import api.PersistPlayer;
import core.game.component.Component;
import core.game.node.entity.player.Player;

import core.game.node.item.Item;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.nio.ByteBuffer;

/**
 * Manages the players barcrawl quest.
 * @author 'Vexia
 */
public final class BarcrawlManager implements LoginListener, PersistPlayer {

	/**
	 * The barcrawl card.
	 */
	public static final Item BARCRAWL_CARD = new Item(455);

	/**
	 * The name of bars.
	 */
	public static final String[] NAMES = new String[] { "BlueMoon Inn", "Blueberry's Bar", "Dead Man's Chest", "Dragon Inn", "Flying Horse Inn", "Foresters Arms", "Jolly Boar Inn", "Karamja Spirits bar", "Rising Sun Inn", "Rusty Anchor Inn" };

	/**
	 * The component used to draw the completed bars on.
	 */
	public static final Component COMPONENT = new Component(220);

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The bars completed.
	 */
	private final boolean[] bars = new boolean[10];

	/**
	 * If the quest has been started.
	 */
	private boolean started;

	/**
	 * Constructs a new {@code BarcrawlManager} {@code Object}.
	 * @param player the player.
	 */
	public BarcrawlManager(final Player player) {
		this.player = player;
	}

	public BarcrawlManager() {this.player = null;}

	@Override
	public void login(@NotNull Player player) {
		BarcrawlManager instance = new BarcrawlManager(player);
		player.setAttribute("barcrawl-inst", instance);
	}

	@Override
	public void parsePlayer(@NotNull Player player, @NotNull JSONObject data) {
		JSONObject bcData = (JSONObject) data.get("barCrawl");
		if(bcData == null) return;
		JSONArray barsVisisted = (JSONArray) bcData.get("bars");
		BarcrawlManager instance = getInstance(player);
		instance.started = (boolean) bcData.get("started");
		for(int i = 0; i < barsVisisted.size(); i++){
			instance.bars[i] = (boolean) barsVisisted.get(i);
		}
	}

	@Override
	public void savePlayer(@NotNull Player player, @NotNull JSONObject save) {
		BarcrawlManager instance = getInstance(player);
		JSONObject barCrawl = new JSONObject();
		barCrawl.put("started", instance.started);
		JSONArray barsVisited = new JSONArray();
		for(boolean visited : instance.bars)
			barsVisited.add(visited);
		barCrawl.put("bars",barsVisited);
		save.put("barCrawl",barCrawl);
	}

	/**
	 * Method used to read the card.
	 */
	public void read() {
		if (isFinished()) {
			player.getPacketDispatch().sendMessage("You are too drunk to be able to read the barcrawl card.");
			;
			return;
		}
		player.getInterfaceManager().open(COMPONENT);
		drawCompletions();
	}

	/**
	 * Draws the completed bars on the interface.
	 */
	private void drawCompletions() {
		player.getPacketDispatch().sendString("<col=0000FF>The Official Alfred Grimhand Barcrawl!", 220, 1);
		boolean complete;
		for (int i = 0; i < bars.length; i++) {
			complete = bars[i];
			player.getPacketDispatch().sendString((complete ? "<col=00FF00>" : "<col=FF0000>") + NAMES[i] + " - " + (complete ? "Complete!" : "Not Completed..."), 220, 3 + i);
		}
	}

	/**
	 * Completes a bar challenge.
	 * @param index the index.
	 */
	public void complete(int index) {
		bars[index] = true;
	}

	/**
	 * Checks if the barcrawl quest is completed.
	 * @return {@code True} if so.
	 */
	public boolean isFinished() {
		for (int i = 0; i < bars.length; i++) {
			if (!isCompleted(i)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Resets the bars.
	 */
	public void reset() {
		started = false;
		for (int i = 0; i < bars.length; i++) {
			bars[i] = false;
		}
	}

	/**
	 * Checks if a bar is completed.
	 * @param index the index.
	 * @return {@code True} if completed.
	 */
	public boolean isCompleted(int index) {
		return bars[index];
	}

	/**
	 * Checks if the player has the card.
	 * @return the card.
	 */
	public boolean hasCard() {
		return player.getInventory().containsItem(BARCRAWL_CARD) || player.getBank().containsItem(BARCRAWL_CARD);
	}

	/**
	 * Gets the started.
	 * @return The started.
	 */
	public boolean isStarted() {
		return started;
	}

	/**
	 * Sets the started.
	 * @param started The started to set.
	 */
	public void setStarted(boolean started) {
		this.started = started;
	}

	public boolean[] getBars() {
		return bars;
	}

	public static BarcrawlManager getInstance(Player player)
	{
		return player.getAttribute("barcrawl-inst", new BarcrawlManager());
	}
}
