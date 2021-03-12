package core.game.node.entity.npc.familiar;

import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.game.node.item.Item;

/**
 * Represents the Spirit Cobra familiar.
 * @author Aero
 * @author Vexia
 */
@Initializable
public class SpiritCobraNPC extends Familiar {

	/**
	 * Constructs a new {@code SpiritCobraNPC} {@code Object}.
	 */
	public SpiritCobraNPC() {
		this(null, 6802);
	}

	/**
	 * Constructs a new {@code SpiritCobraNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public SpiritCobraNPC(Player owner, int id) {
		super(owner, id, 5600, 12015, 3, WeaponInterface.STYLE_ACCURATE);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new SpiritCobraNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		final Item item = (Item) special.getNode();
		final Egg egg = Egg.forEgg(item);
		if (egg == null) {
			owner.getPacketDispatch().sendMessage("You can't use the special move on this item.");
			return false;
		}
		owner.getInventory().replace(egg.getProduct(), item.getSlot());
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6802, 6803 };
	}

	/**
	 * Represents an incubated egg.
	 * @author Vexia
	 */
	public enum Egg {
		COCKATRICE(new Item(1944), new Item(12109)), SARATRICE(new Item(10533), new Item(12113)), ZAMATRICE(new Item(10532), new Item(12115)), GUTHATRICE(new Item(10531), new Item(12111)), CORACATRICE(new Item(11964), new Item(12119)), PENGATRICE(new Item(12483), new Item(12117)), VULATRICE(new Item(11695), new Item(12121));

		/**
		 * The egg item.
		 */
		private final Item egg;

		/**
		 * The product.
		 */
		private final Item product;

		/**
		 * Constructs a new {@code Egg} {@code Object}.
		 * @param egg the egg.
		 * @param product the product.
		 */
		private Egg(Item egg, Item product) {
			this.egg = egg;
			this.product = product;
		}

		/**
		 * Gets the egg for the item.
		 * @param item the item.
		 * @return the egg.
		 */
		public static Egg forEgg(final Item item) {
			for (Egg egg : values()) {
				if (egg.getEgg().getId() == item.getId()) {
					return egg;
				}
			}
			return null;
		}

		/**
		 * Gets the egg.
		 * @return The egg.
		 */
		public Item getEgg() {
			return egg;
		}

		/**
		 * Gets the product.
		 * @return The product.
		 */
		public Item getProduct() {
			return product;
		}

	}
}
