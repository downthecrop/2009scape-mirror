package plugin.npc.familiar;

import org.crandor.game.content.skill.member.summoning.SummoningScroll;
import org.crandor.game.content.skill.member.summoning.familiar.BurdenBeast;
import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.system.mysql.impl.ItemConfigSQLHandler;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.game.world.update.flag.context.Graphics;

/**
 * Represents the Pack Yak familiar.
 * @author Aero
 */
@InitializablePlugin
public class PackYakNPC extends BurdenBeast {

	/**
	 * Constructs a new {@code PackYakNPC} {@code Object}.
	 */
	public PackYakNPC() {
		this(null, 6873);
	}

	/**
	 * Constructs a new {@code PackYakNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public PackYakNPC(Player owner, int id) {
		super(owner, id, 5800, 12093, 12, 30, WeaponInterface.STYLE_AGGRESSIVE);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new PackYakNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		Player player = owner;
		Item item = new Item(special.getItem().getId(), 1);
		if (item.getId() == SummoningScroll.WINTER_STORAGE_SCROLL.getItemId()) {
			return false;
		}
		if (!item.getDefinition().getConfiguration(ItemConfigSQLHandler.BANKABLE, true)) {
			player.sendMessage("A magical force prevents you from banking this item");
			return false;
		}
		Item remove = item;
		if (!item.getDefinition().isUnnoted()) {
			remove = new Item(item.getId(), 1);
			item = new Item(item.getNoteChange(), 1);
		}
		if (player.getInventory().remove(remove) && player.getBank().add(item)) {
			player.getDialogueInterpreter().close();
			graphics(Graphics.create(1358));
			player.getPacketDispatch().sendMessage("The pak yak has sent an item to your bank.");
		} else {
			player.getPacketDispatch().sendMessage("The pak yak can't send that item to your bank.");
		}
		return true;
	}

	@Override
	public void visualizeSpecialMove() {
		owner.visualize(Animation.create(7660), Graphics.create(1316));
	}

	@Override
	public int[] getIds() {
		return new int[] { 6873, 6874 };
	}

	@Override
	protected String getText() {
		return "Baroo!";
	}

}
