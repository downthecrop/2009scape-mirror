package core.game.content.activity.pestcontrol;

import core.cache.def.impl.ItemDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import core.game.content.activity.pestcontrol.monsters.*;

/**
 * Handles the void knight seal.
 * @author Emperor
 */
public final class VoidSealPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (int i = 0; i < 8; i++) {
			ItemDefinition.forId(11666 + i).getHandlers().put("option:rub", this);
		}
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (player.getViewport().getRegion().getRegionId() != 10536) {
			player.getPacketDispatch().sendMessage("You can only use the seal in Pest Control.");
			return true;
		}
		boolean operate = option.equals("operate");
		player.lock(1);
		Item item = (Item) node;
		// TODO: check if the seal is used in inventory or equipment operate.
		player.getPacketDispatch().sendMessage("You unleash the power of the Void Knights!");
		Item replace = null;
		if (item.getId() != 11673) {
			replace = new Item(item.getId() + 1);
		}
		if (operate) {
			player.getEquipment().replace(replace, item.getSlot());
		} else {
			player.getInventory().replace(replace, item.getSlot());
		}
		player.graphics(Graphics.create(1177));
		for (NPC npc : RegionManager.getLocalNpcs(player, 2)) {
			if (canTarget(npc)) {
				npc.getImpactHandler().manualHit(player, 7 + RandomFunction.randomize(5), HitsplatType.NORMAL, 1);
				npc.graphics(Graphics.create(1176));
			}
		}
		return true;
	}

	/**
	 * Checks if the NPC can be targeted.
	 * @param npc The NPC.
	 * @return {@code True} if so.
	 */
	private static boolean canTarget(NPC npc) {
		return npc instanceof PCDefilerNPC || npc instanceof PCRavagerNPC || npc instanceof PCShifterNPC || npc instanceof PCSpinnerNPC || npc instanceof PCSplatterNPC || npc instanceof PCTorcherNPC || npc instanceof PCBrawlerNPC;
	}

}
