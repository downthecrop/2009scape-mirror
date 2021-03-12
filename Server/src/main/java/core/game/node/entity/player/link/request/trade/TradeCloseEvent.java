package core.game.node.entity.player.link.request.trade;

import core.game.component.CloseEvent;
import core.game.component.Component;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.net.packet.PacketRepository;
import core.net.packet.context.ContainerContext;
import core.net.packet.out.ContainerPacket;

/**
 * Represents the close event invoked at the closing of a trade interface.
 * @author Vexia
 */
public final class TradeCloseEvent implements CloseEvent {

	@Override
	public boolean close(Player player, Component c) {
		final TradeModule module = TradeModule.getExtension(player);
		player.getPacketDispatch().sendRunScript(101, "");
		if (module == null) {
			return true;
		}
		TradeModule otherModule = TradeModule.getExtension(module.getTarget());
		if (otherModule == null) {
			return true;
		}
		if (module.isAccepted() && otherModule.isAccepted()) {
			return true;
		}
		if (module.getStage() != 2) {
			retainContainer(player);
			retainContainer(module.getTarget());
		}
		closeInterfaces(player);
		closeInterfaces(module.getTarget());
		module.getTarget().getInterfaceManager().close();
		end(player);
		player.getInterfaceManager().openDefaultTabs();
		end(module.getTarget());
		module.getTarget().getInterfaceManager().openDefaultTabs();
		return true;
	}

	/**
	 * Method used to close the trade interface.
	 * @param player the player.
	 */
	private void closeInterfaces(final Player player) {
		player.removeExtension(TradeModule.class);
		player.getInterfaceManager().closeSingleTab();
		PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, 2, 24, new Item[] {}, 27, false));
		PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, 2, 23, new Item[] {}, 27, false));
		player.getPacketDispatch().sendRunScript(101, "");
	}

	/**
	 * Method used to end the trade session.
	 * @param player the player.
	 */
	private void end(final Player player) {
		player.getConfigManager().set(1043, 0);
		player.getConfigManager().set(1042, 0);
	}

	/**
	 * Method used to retain the trade container.
	 * @param player the player.
	 */
	private void retainContainer(final Player player) {
		final TradeModule module = TradeModule.getExtension(player);
		if (module == null || module.isRetained()) {
			return;
		}
		module.setRetained(true);
		player.getInventory().addAll(module.getContainer());
	}

}