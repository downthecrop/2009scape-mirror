package core.game.node.entity.state.impl;

import api.PersistPlayer;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.node.entity.state.EntityState;
import core.game.node.entity.state.StatePulse;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

import java.nio.ByteBuffer;

/**
 * Handles the teleblock state pulse.
 * @author Vexia
 */
public final class TeleblockStatePulse extends StatePulse implements PersistPlayer {

	/**
	 * The ticks needed to pass.
	 */
	public int ticks;

	/**
	 * The current tick.
	 */
	public int currentTick;

	/**
	 * Constructs a new {@Code TeleblockStatePulse} {@Code Object}
	 * @param entity the entity.
	 * @param ticks the ticks.
	 * @param currentTick the current tick.
	 */
	public TeleblockStatePulse(Entity entity, int ticks, int currentTick) {
		super(entity, 1);
		this.ticks = ticks;
		this.currentTick = currentTick;
	}

	//Required to be instantiated as a ContentListener for PersistPlayer
	public TeleblockStatePulse() {
		super(null, 0);
	}

	@Override
	public void savePlayer(@NotNull Player player, @NotNull JSONObject save) {
		TeleblockStatePulse tbPulse = (TeleblockStatePulse) player.getStateManager().get(EntityState.TELEBLOCK);

		if (tbPulse != null && tbPulse.isSaveRequired()) {
			JSONObject tbObj = new JSONObject();
			tbObj.put("tb-state-current", "" + tbPulse.currentTick);
			tbObj.put("tb-state-total", "" + tbPulse.ticks);
			save.put("teleblock-state", tbObj);
		}
	}

	@Override
	public void parsePlayer(@NotNull Player player, @NotNull JSONObject data) {
		if (data.containsKey("teleblock-state")) {
			JSONObject tbData = (JSONObject) data.get("teleblock-state");
			int currentTick = Integer.parseInt(tbData.get("tb-state-current").toString());
			int totalTicks = Integer.parseInt(tbData.get("tb-state-total").toString());
			player.getStateManager().set(EntityState.TELEBLOCK, totalTicks, currentTick);
		}
	}

	@Override
	public boolean isSaveRequired() {
		return currentTick < ticks;
	}

	@Override
	public void save(ByteBuffer buffer) {
		buffer.putInt(ticks);
		buffer.putInt(currentTick);
	}

	@Override
	public StatePulse parse(Entity entity, ByteBuffer buffer) {
		return new TeleblockStatePulse(entity, buffer.getInt(), buffer.getInt());
	}

	@Override
	public void start() {
		if (currentTick == 0) {
			if (entity instanceof Player) {
				entity.asPlayer().getAudioManager().send(203, true);
				entity.asPlayer().sendMessage("You have been teleblocked.");
			}
		}
		super.start();
	}

	@Override
	public boolean pulse() {
		return ++currentTick >= ticks;
	}

	@Override
	public StatePulse create(Entity entity, Object... args) {
		return new TeleblockStatePulse(entity, (int) args[0], args.length > 1 ? (int) args[1] : 0);
	}

}
