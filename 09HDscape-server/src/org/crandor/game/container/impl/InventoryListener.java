package org.crandor.game.container.impl;

import org.crandor.game.container.Container;
import org.crandor.game.container.ContainerEvent;
import org.crandor.game.container.ContainerListener;
import org.crandor.game.content.skill.member.summoning.SummoningPouch;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.net.packet.PacketRepository;
import org.crandor.net.packet.context.ContainerContext;
import org.crandor.net.packet.out.ContainerPacket;

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