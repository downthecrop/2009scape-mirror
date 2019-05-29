package org.crandor.game.content.skill.member.farming.compost;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.info.login.SavingModule;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the managing class of compost bins.
 * @author 'Vexia
 * @version 1.0
 */
public final class CompostManager implements SavingModule {

	/**
	 * Represents the list of compost bins.
	 */
	private final List<CompostBin> bins = new ArrayList<>();

	/**
	 * Constructs a new {@code CompostBinManager} {@code Object}.
	 */
	public CompostManager() {
		/**
		 * empty.
		 */
	}

	@Override
	public void save(ByteBuffer buffer) {
		buffer.put((byte) bins.size());
		for (CompostBin bin : bins) {
			buffer.putInt(bin.getWrapperId());
			if (bin.getTimeStamp() != 0L) {
				buffer.put((byte) 1);
				buffer.putLong(bin.getTimeStamp());
			}
			if (bin.getContainer().itemCount() != 0) {
				buffer.put((byte) 2);
				bin.getContainer().save(buffer);
			}
			buffer.put((byte) 0);
		}
	}

	@Override
	public void parse(ByteBuffer buffer) {
		int size = buffer.get();
		int opcode;
		for (int i = 0; i < size; i++) {
			CompostBin bin = new CompostBin(buffer.getInt());
			while ((opcode = buffer.get() & 0xFF) != 0) {
				switch (opcode) {
				case 1:
					bin.setTimeStamp(buffer.getLong());
					break;
				case 2:
					bin.getContainer().parse(buffer);
					break;
				}
			}
			bins.add(bin);
		}
	}

	/**
	 * Method used to fill a compost.
	 * @param player the player.
	 * @param object the object.
	 * @param option the option.
	 * @param delay the delay
	 */
	public void fill(final Player player, final Item item, final GameObject object, String option, int delay) {
		CompostBin bin = getBin(object);
		if (option.equals("bin")) {
			bin.fillBin(player, item, delay);
		} else {
			bin.fillBucket(player, delay);
		}
	}

	/**
	 * Gets the compost bin by the object wrapper.
	 * @param object the object.
	 * @return the wrapper.
	 */
	public CompostBin getBin(final GameObject object) {
		final GameObject wrapper = object.getWrapper();
		for (CompostBin bin : getBins()) {
			if (bin.getWrapperId() == wrapper.getId()) {
				return bin;
			}
		}
		CompostBin bin = new CompostBin(object.getWrapper().getId());
		bins.add(bin);
		return bin;
	}

	/**
	 * Gets the bins.
	 * @return The bins.
	 */
	public List<CompostBin> getBins() {
		return bins;
	}

}
