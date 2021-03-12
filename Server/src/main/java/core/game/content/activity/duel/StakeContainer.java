package core.game.content.activity.duel;

import core.game.component.Component;
import core.game.container.Container;
import core.game.container.ContainerEvent;
import core.game.container.ContainerListener;
import core.game.container.ContainerType;
import core.game.container.SortType;
import core.game.container.access.InterfaceContainer;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.login.PlayerParser;
import core.game.node.item.Item;
import core.net.packet.PacketRepository;
import core.net.packet.context.ContainerContext;
import core.net.packet.out.ContainerPacket;

/**
 * Represents the staking container.
 * @author Vexia
 *
 */
public class StakeContainer extends Container {

	/**
	 * The inventory params.
	 */
	private static final Object[] INVY_PARAMS = new Object[] { "", "", "", "Stake-X", "Stake-All", "Stake-10", "Stake-5", "Stake", -1, 0, 7, 4, 93, (336 << 16) };

	/**
	 * The withdraw options.
	 */
	private static final String[] WITHDRAW_OPTIONS = new String[] { "Remove-X", "Remove-All", "Remove-10", "Remove-5", "Remove" };

	/**
	 * The overlay interface.
	 */
	public static final Component OVERLAY = new Component(336);

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = 3454088488966242754L;

	/**
	 * The player of the container.
	 */
	private final Player player;

	/**
	 * The dueling session.
	 */
	private final DuelSession session;

	/**
	 * The stake listener.
	 */
	private final StakeListener listener;

	/**
	 * If the container has been released.
	 */
	private boolean released;

	/**
	 * Constructs a new {@Code StakeContainer} {@Code Object}
	 * @param player the player.
	 * @param session the session.
	 */
	public StakeContainer(Player player, DuelSession session) {
		super(28, ContainerType.DEFAULT, SortType.ID);
		this.player = player;
		this.session = session;
		this.getListeners().add(listener = new StakeListener());
	}

	/**
	 * Opens the staking container.
	 */
	public void open() {
		player.getInterfaceManager().openSingleTab(OVERLAY);
		player.getPacketDispatch().sendRunScript(150, "IviiiIssssssss", INVY_PARAMS);
		player.getPacketDispatch().sendAccessMask(1278, 0, 336, 0, 27);
		PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, 2, 93, player.getInventory(), false));
	}

	/**
	 * Offers an item to the stake.
	 * @param slot the slot.
	 * @param amount the amount.
	 */
	public void offer(final int slot, int amount) {
		final Item item = player.getInventory().get(slot);
		if (item == null) {
			return;
		}
		if ((slot < 0 || slot > player.getInventory().capacity() || amount < 1)) {
			return;
		}
		if (!item.getDefinition().isTradeable()) {
			player.sendMessage("You can't offer that item.");
			return;
		}
		Item remove = new Item(item.getId(), amount);
		if (item.getAmount() > getMaximumAdd(item)) {
			item.setAmount(getMaximumAdd(item));
			if (item.getAmount() < 1) {
				return;
			}
		}
		remove.setAmount(amount > player.getInventory().getAmount(item) ? player.getInventory().getAmount(item) : amount);
		if (player.getInventory().remove(remove, slot, true)) {
			session.resetAccept();
			add(remove);
			StakeContainer c = session.getOppositeContainer(player);
			c.getListener().update(c, c.getEvent());
		}
	}

	/**
	 * Withdraws an item from the container.
	 * @param slot the slot.
	 * @param amount the amount.
	 */
	public void withdraw(final int slot, int amount) {
		final Item item = get(slot);
		if (item == null) {
			return;
		}
		if ((slot < 0 || slot > player.getInventory().capacity() || amount < 1)) {
			return;
		}
		if (!item.getDefinition().isTradeable()) {
			player.sendMessage("You can't offer that item.");
			return;
		}
		Item remove = new Item(item.getId(), amount);
		if (item.getAmount() > getMaximumAdd(item)) {
			item.setAmount(getMaximumAdd(item));
			if (item.getAmount() < 1) {
				return;
			}
		}
		remove.setAmount(amount > getAmount(item) ? getAmount(item) : amount);
		if (remove(remove, slot, true) && player.getInventory().add(remove)) {
			session.resetAccept();
			shift();
			StakeContainer c = session.getOppositeContainer(player);
			c.getListener().update(c, c.getEvent());
		}
	}

	/**
	 * Releases the container back to the player.
	 */
	public void release() {
		if (released) {
			return;
		}
		released = true;
		if (!player.getInventory().hasSpaceFor(this)) {
			player.getBank().addAll(this);
			player.sendMessage("Your stake was sent to your bake due to invalid inventory space.");
			return;
		}
		player.getInventory().addAll(this);
		PlayerParser.save(player);
	}

	/**
	 * Gets the serialversionuid.
	 * @return the serialversionuid.
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Gets the session.
	 * @return the session.
	 */
	public DuelSession getSession() {
		return session;
	}

	/**
	 * Gets the listener.
	 * @return the listener.
	 */
	public StakeListener getListener() {
		return listener;
	}

	/**
	 * Represents the container listener for a players stake session.
	 * @author Vexia
	 * 
	 */
	public final class StakeListener implements ContainerListener {

		@Override
		public void update(Container c, ContainerEvent event) {
			InterfaceContainer.generateItems(player, c.toArray(), WITHDRAW_OPTIONS, 631, 103, 12, 3);
			InterfaceContainer.generateItems(player, session.getOppositeContainer(player).toArray(), WITHDRAW_OPTIONS, 631, 104, 12, 3);
		}

		@Override
		public void refresh(Container c) {
			PacketRepository.send(ContainerPacket.class, new ContainerContext(player, -1, 2, 93, player.getInventory(), false));
		}

	}
}
