package core.game.node.entity.npc.other;

import core.game.node.entity.npc.AbstractNPC;
import core.plugin.Initializable;
import core.game.world.map.Location;

/**
 * Represents the abstract npc of a generic owner of a shop.
 * @author 'Vexia
 */
@Initializable
public class ShopNPC extends AbstractNPC {

	/**
	 * The NPC ids of NPCs using this plugin.
	 */
	private static final int[] ID = { 410, 528, 529, 525, 524, 1436, 590, 591, 971, 1917, 597, 1083, 3541, 7396, 1040, 563, 5798, 582, 526, 527, 873, 532, 533, 568, 2154, 1334, 2352, 4516, 520, 521, 4716, 3922, 1254, 2086, 3824, 1866, 530, 531, 836, 516, 560, 471, 522, 523, 4651, 4652, 4653, 4654, 4655, 4656, 4650, 534, 588, 2356, 1860, 550, 575, 683, 682, 4563, 4558, 4559, 519, 559, 562, 581, 554, 601, 1301, 1039, 2353, 3166, 2161, 2162, 600, 593, 545, 585, 5268, 2305, 2307, 2304, 1783, 557, 1038, 1433, 7053, 571, 5487, 536, 4294, 4293, 584, 570, 540, 2157, 69669, 569, 572, 573, 1303, 595, 297, 704, 587, 5110, 5109, 556, 1865, 543, 2198, 580, 1862, 583, 553, 461, 4513, 3097, 903, 1435, 606, 2623, 594, 579, 2160, 6750, 6898, 6893, 589, 5485, 542, 549, 3038, 544, 1434, 209, 1980, 546, 6988, 3671, 558, 576, 5266, 586, 602, 552, 551, 541, 692, 797, 564, 2152, 1208, 2233, 3205, 70, 1598, 1596, 1597, 1599, 2259, 3162, 1167, 2620, 2151, 5486, 4250, 1079, 970, 793, 2572 };

	/**
	 * Constructs a new {@code ShopNPC} {@code Object}.
	 */
	public ShopNPC() {
		super(0, null, true);
	}

	/**
	 * Constructs a new {@code ShopNPC} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	private ShopNPC(int id, Location location) {
		super(id, location, true);
	}

	@Override
	public void init() {
		super.init();
		setWalks(true);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new ShopNPC(id, location);
	}

	@Override
	public int getWalkRadius() {
		return 3;
	}

	@Override
	public int[] getIds() {
		return ID;
	}

}
