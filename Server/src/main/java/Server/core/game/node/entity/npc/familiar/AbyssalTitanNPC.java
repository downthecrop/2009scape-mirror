package core.game.node.entity.npc.familiar;

import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.game.world.update.flag.context.Graphics;

/**
 * Represents the Abyssal Titan familiar.
 * @author Aero
 * @author Splinter
 */
@Initializable
public class AbyssalTitanNPC extends Familiar {

	/**
	 * Constructs a new {@code AbyssalTitanNPC} {@code Object}.
	 */
	public AbyssalTitanNPC() {
		this(null, 7349);
	}

	/**
	 * Constructs a new {@code AbyssalTitanNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public AbyssalTitanNPC(Player owner, int id) {
		super(owner, id, 3200, 12796, 6, WeaponInterface.STYLE_ACCURATE);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new AbyssalTitanNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		Player player = owner;
		if (!player.getInventory().containsItem(new Item(1436, 1)) && !player.getInventory().containsItem(new Item(7936, 1))) {
			player.sendMessage("You have no essence to send to the bank.");
			return false;
		}
		int RUNE_ESSENCE_AMOUNT = player.getInventory().getAmount(1436);
		int PURE_ESSENCE_AMOUNT = player.getInventory().getAmount(7936);
		player.getInventory().remove(new Item(1436, RUNE_ESSENCE_AMOUNT), new Item(7936, PURE_ESSENCE_AMOUNT));
		player.getBank().add(new Item(1436, RUNE_ESSENCE_AMOUNT), new Item(7936, PURE_ESSENCE_AMOUNT));
		player.sendMessage("The titan sends " + RUNE_ESSENCE_AMOUNT + " rune essence and " + PURE_ESSENCE_AMOUNT + " pure essence to your bank.");
		return true;
	}

	@Override
	public void visualizeSpecialMove() {
		owner.visualize(Animation.create(7660), Graphics.create(1316));
		this.visualize(Animation.create(7694), Graphics.create(1457));
	}

	@Override
	public int[] getIds() {
		return new int[] { 7349, 7350 };
	}

}
