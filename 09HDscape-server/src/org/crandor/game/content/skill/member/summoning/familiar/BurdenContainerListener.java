package org.crandor.game.content.skill.member.summoning.familiar;

import org.crandor.game.container.Container;
import org.crandor.game.container.ContainerEvent;
import org.crandor.game.container.ContainerListener;
import org.crandor.game.node.entity.player.Player;
import org.crandor.net.packet.PacketRepository;
import org.crandor.net.packet.context.ContainerContext;
import org.crandor.net.packet.out.ContainerPacket;

/**
 * The beast of burden container listener.
 * @author Emperor
 */
public final class BurdenContainerListener implements ContainerListener {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Constructs a new {@code BurdenContainerListener} {@code Object}.
	 * @param player The player.
	 */
	public BurdenContainerListener(Player player) {
		this.player = player;
	}

	@Override
	public void update(Container c, ContainerEvent event) {
		PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, -2, 30, event.getItems(), false, event.getSlots()));
	}

	@Override
	public void refresh(Container c) {
		PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, -2, 30, c.toArray(), c.capacity(), false));
	}

}