package core.game.node.entity.npc.familiar;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.summoning.familiar.Familiar;
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial;
import core.game.node.entity.skill.summoning.familiar.Forager;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import org.rs09.consts.Items;
import rs09.game.node.entity.skill.farming.FarmingPatch;
import rs09.game.node.entity.skill.farming.PatchType;

/**
 * Represents the Giant Ent familiar.
 * @author Aero
 */
@Initializable
public class GiantEntNPC extends Forager {
	private static final Item[] ITEMS = new Item[] { new Item(Items.OAK_LOGS_1521) };

	/**
	 * Constructs a new {@code GiantEntNPC} {@code Object}.
	 */
	public GiantEntNPC() {
		this(null, 6800);
	}

	/**
	 * Constructs a new {@code GiantEntNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public GiantEntNPC(Player owner, int id) {
		super(owner, id, 4900, 12013, 6, WeaponInterface.STYLE_CONTROLLED, ITEMS);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new GiantEntNPC(owner, id);
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		return false;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6800, 6801 };
	}

	@Override
	protected void configureFamiliar() {
		UseWithHandler.addHandler(6800, UseWithHandler.NPC_TYPE, new UseWithHandler(Items.PURE_ESSENCE_7936) {
			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				addHandler(6800, UseWithHandler.NPC_TYPE, this);
				return this;
			}

			@Override
			public boolean handle(NodeUsageEvent event) {
				Player player = event.getPlayer();
				player.lock(1);
                int runeType = RandomFunction.random(9) < 4 ? Items.EARTH_RUNE_557 : Items.NATURE_RUNE_561;
				Item runes = new Item(runeType, 1);
				if (player.getInventory().remove(event.getUsedItem())) {
					player.getInventory().add(runes);
					player.sendMessage(String.format("The giant ent transmutes the pure essence into a %s.", runes.getName().toLowerCase()));
				}
				return true;
			}
		});
	}

    public void modifyFarmingReward(FarmingPatch fPatch, Item reward) {
		PatchType patchType = fPatch.getType();
		if(patchType == PatchType.FRUIT_TREE ||
			patchType == PatchType.BUSH ||
			patchType == PatchType.BELLADONNA ||
			patchType == PatchType.CACTUS) {
			if(RandomFunction.roll(2)) {
				reward.setAmount(2 * reward.getAmount());
			}
		}
    }
}
