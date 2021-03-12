package core.game.container.impl;

import core.game.container.Container;
import core.game.container.ContainerEvent;
import core.game.container.ContainerListener;
import core.game.node.entity.skill.summoning.SummoningPouch;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.net.packet.PacketRepository;
import core.net.packet.context.ContainerContext;
import core.net.packet.out.ContainerPacket;

/**
 * Handles the inventory container listening.
 * @author Emperor
 */
public final class InventoryListener implements ContainerListener {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Constructs a new {@code InventoryListener} {@code Object}.
	 * @param player The player.
	 */
	public InventoryListener(Player player) {
		this.player = player;
	}

	/**
	 * Updates the required settings etc for the player when the container
	 * updates.
	 * @param c The container.
	 */
	public void update(Container c) {
		player.getSettings().updateWeight();
		boolean hadPouch = player.getFamiliarManager().isHasPouch();
		boolean pouch = false;
		for (Item item : c.toArray()) {
			if (item != null && SummoningPouch.get(item.getId()) != null) {
				pouch = true;
				break;
			}
		}
		player.getFamiliarManager().setHasPouch(pouch);
		if (hadPouch != pouch && player.getSkullManager().isWilderness()) {
			player.getAppearance().sync();
		}
	}

	@Override
	public void refresh(Container c) {
		PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 149, 0, 93, c, false));
		update(c);
	}

	@Override
	public void update(Container c, ContainerEvent event) {
		PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 149, 0, 93, event.getItems(), false, event.getSlots()));
		update(c);
	}

}