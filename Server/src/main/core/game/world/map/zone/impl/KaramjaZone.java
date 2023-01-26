package core.game.world.map.zone.impl;

import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.map.zone.MapZone;

/**
 * Represents the karamja zone area.
 * @author 'Vexia
 * @version 1.0
 */
public final class KaramjaZone extends MapZone {

	/**
	 * Represents the region ids.
	 */
	private static final int[] REGIONS = new int[] { 11309, 11054, 11566, 11565, 11567, 11568, 11053, 11821, 11055, 11057, 11569, 11822, 11823, 11825, 11310, 11311, 11312, 11313, 11314, 11056, 11057, 11058, 10802, 10801 };

	/**
	 * Represents the karamjan rum item.
	 */
	private static final Item KARAMJAN_RUM = new Item(431);

	/**
	 * Constructs a new {@code KaramjaZone} {@code Object}.
	 */
	public KaramjaZone() {
		super("karamja", true);
	}

	@Override
	public void configure() {
		for (int regionId : REGIONS) {
			registerRegion(regionId);
		}
	}

	@Override
	public boolean teleport(Entity entity, int type, Node node) {
		if (entity instanceof Player) {
			final Player p = ((Player) entity);
			int amt = p.getInventory().getAmount(KARAMJAN_RUM);
			if (amt != 0) {
				p.getInventory().remove(new Item(KARAMJAN_RUM.getId(), amt));
				p.getPacketDispatch().sendMessage("During the trip you lose your rum to a sailor in a game of dice. Better luck next time!");
			}
		}
		return super.teleport(entity, type, node);
	}

}
