package core.game.node.entity.skill.magic.lunar;

import core.plugin.Initializable;
import core.game.node.entity.skill.magic.MagicSpell;
import core.game.node.entity.skill.magic.Runes;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.SpellType;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.item.Item;
import core.game.world.repository.Repository;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Plugin;

/**
 * <b>Turns one log into a plank, without need of the sawmill. Coins are still
 * required to convert a log into a plank, but the amount is decreased.</b>
 * @author 'Vexia
 */
@Initializable
public class PlankMeSpell extends MagicSpell {

	/**
	 * The graphic.
	 */
	private static final Graphics GRAPHIC = new Graphics(1063, 120);

	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = new Animation(6298);

	/**
	 * Constructs a new {@code StatRestoreSpell} {@code Object}.
	 */
	public PlankMeSpell() {
		super(SpellBook.LUNAR, 86, 90, null, null, null, new Item[] { new Item(Runes.NATURE_RUNE.getId(), 1), new Item(Runes.ASTRAL_RUNE.getId(), 2), new Item(Runes.EARTH_RUNE.getId(), 15) });
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.LUNAR.register(11, this);
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		final Player player = ((Player) entity);
		final Plank plank = Plank.forLog(((Item) target));
		player.getInterfaceManager().setViewedTab(6);
		final Item item = ((Item) target);
		if (plank == null) {
			player.getPacketDispatch().sendMessage("You need to use this spell on logs.");
			return false;
		}
		if (!player.getInventory().contains(995, plank.getPrice())) {
			player.getPacketDispatch().sendMessage("You need " + plank.getPrice() + " coins to convert that log into a plank.");
			return false;
		}
		player.lock(4);
		create(player, plank, player.getInventory().getAmount(item));
		return true;
	}

	/**
	 * Method used to create a plank from a log.
	 * @param player the player.
	 * @param plank the plank.
	 * @param amount the amount.
	 */
	public final void create(final Player player, final Plank plank, int amount) {
		player.getInterfaceManager().close();
		if (amount > player.getInventory().getAmount(plank.getLog())) {
			amount = player.getInventory().getAmount(plank.getLog());
		}
		if (!player.getInventory().containsItem(plank.getLog())) {
			player.getDialogueInterpreter().sendDialogues(Repository.findNPC(4250), null, "You'll need to bring me some more logs.");
			return;
		}
		int newAmt = 1;
		for (int i = 1; i <= amount; i++) {
			if (player.getInventory().contains(995, plank.getPrice() * amount)) {
				newAmt = i;
				continue;
			}
		}
		amount = newAmt;
		if (!player.getInventory().contains(995, plank.getPrice() * amount)) {
			player.getDialogueInterpreter().sendDialogues(player, null, "Sorry, I don't have enough coins to pay for that.");
			return;
		}
		if (!player.getInventory().contains(plank.getLog().getId(), amount)) {
			return;
		}
		Item coins = new Item(995, plank.getPrice() * amount);
		if (!player.getInventory().containsItem(coins)) {
			return;
		}
		if (super.meetsRequirements(player, true, true)) {
			player.graphics(GRAPHIC);
			player.animate(ANIMATION);
			if (player.getInventory().remove(coins)) {
				Item remove = plank.getLog();
				remove.setAmount(amount);
				if (player.getInventory().remove(remove)) {
					Item planks = plank.getPlank();
					planks.setAmount(amount);
					player.getInventory().add(planks);
				}
			}
		}
	}

	/**
	 * Represents an enum of planks to be boughten.
	 * @author 'Vexia
	 * @date Oct 8, 2013
	 */
	public enum Plank {
		WOOD(new Item(1511), new Item(960), 70), OAK(new Item(1521), new Item(8778), 175), TEAK(new Item(6333), new Item(8780), 350), MAHOGANY(new Item(6332), new Item(8782), 1050);
		/**
		 * Constructs a new {@code SawmillPlankInterface} {@code Object}.
		 * @param log the log.
		 * @param plank the plank.
		 * @param price the price.
		 */
		Plank(Item log, Item plank, int price) {
			this.log = log;
			this.plank = plank;
			this.price = price;
		}

		/**
		 * Represents the item needed.
		 */
		private final Item log;

		/**
		 * Represents the item given.
		 */
		private final Item plank;

		/**
		 * Represents the price of the plank.
		 */
		private final int price;

		/**
		 * Gets the plank.
		 * @param item the item.
		 * @return the plank.
		 */
		public static Plank forLog(final Item item) {
			for (Plank p : Plank.values()) {
				if (p.getLog().getId() == item.getId()) {
					return p;
				}
			}
			return null;
		}

		/**
		 * @return the log.
		 */
		public Item getLog() {
			return log;
		}

		/**
		 * @return the plank.
		 */
		public Item getPlank() {
			return plank;
		}

		/**
		 * @return the price.
		 */
		public int getPrice() {
			return price;
		}
	}
}
