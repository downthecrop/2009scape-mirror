package content.region.asgarnia.burthorpe.handlers;

import core.ServerConstants;
import core.cache.def.impl.SceneryDefinition;
import content.data.EnchantedJewellery;
import core.game.global.action.DoorActionHandler;
import content.global.skill.summoning.familiar.Familiar;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.OptionHandler;
import core.game.interaction.UseWithHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;
import core.plugin.Initializable;
import core.plugin.ClassScanner;

import static core.api.ContentAPIKt.hasRequirement;
import static core.api.ContentAPIKt.sendMessage;

import content.data.Quests;
import org.rs09.consts.Items;

/**
 * Represents the hero guild.
 * @author Vexia
 * @author Player Name
 */
@Initializable
public final class HeroGuildPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(2624).getHandlers().put("option:open", this);
		SceneryDefinition.forId(2625).getHandlers().put("option:open", this);
		ClassScanner.definePlugin(new JewelleryRechargePlugin());
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final int id = ((Scenery) node).getId();
		switch (option) {
		case "open":
			switch (id) {
			case 2624:
			case 2625:
				if (!hasRequirement(player, Quests.HEROES_QUEST))
					return true;
				DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
				break;
			}
			return true;
		}
		return true;
	}

	/**
	 * Handles the recharging of dragonstone jewellery.
	 * @author Vexia
	 * @author Player Name
	 */
	public static final class JewelleryRechargePlugin extends UseWithHandler {

		/**
		 * The ids of rechargeable items.
		 */
		private static final int[] IDS = new int[] { 1710, 1708, 1706, 1704, 11107, 11109, 11111, 11113, 11120, 11122, 11124, 11126, 10354, 10356, 10358, 10360, 10362, 14644,14642,14640,14638, 2572 };

		/**
		 * Constructs a new JewelleryRechargePlugin object
		 */
		public JewelleryRechargePlugin() {
			super(IDS);
		}

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			addHandler(36695, OBJECT_TYPE, this);
			addHandler(7339, NPC_TYPE, this);
			addHandler(7340, NPC_TYPE, this);
			return this;
		}

		@Override
		public boolean handle(NodeUsageEvent event) {
			final Player player = event.getPlayer();
			if (!hasRequirement(player, Quests.HEROES_QUEST)) {
				return true; //hasRequirement shows the message
			}
			final EnchantedJewellery jewellery;
			assert event.getUsedItem() != null;
			jewellery = EnchantedJewellery.Companion.getIdMap().get(event.getUsedItem().getId());
			if (jewellery == null) {
				return false; //nothing interesting happens
			}
			if (jewellery == EnchantedJewellery.RING_OF_WEALTH) {
				if (!ServerConstants.RING_OF_WEALTH_TELEPORT) {
					return false;
				}
			}
			if (jewellery == EnchantedJewellery.COMBAT_BRACELET || jewellery == EnchantedJewellery.SKILLS_NECKLACE) {
				if (!hasRequirement(player, Quests.LEGENDS_QUEST)) {
					return true;
				}
			}
			boolean fam = event.getUsedWith() instanceof NPC;
			if (fam && jewellery != EnchantedJewellery.AMULET_OF_GLORY & jewellery != EnchantedJewellery.AMULET_OF_GLORY_T) {
				return false;
			}
			if (fam) {
				Familiar familiar = (Familiar) event.getUsedWith();
				if (!player.getFamiliarManager().isOwner(familiar)) {
					return false;
				}
				familiar.animate(Animation.create(7882));
			}
			player.lock(1);
			player.animate(Animation.create(832));
			Item rechargedItem = new Item(jewellery.getIds()[0]);
			player.getInventory().replace(rechargedItem, event.getUsedItem().getSlot());
			String name = jewellery.getJewelleryName(rechargedItem);
			if (!fam) {
				sendMessage(player, "You dip the " + name.toLowerCase() + " in the fountain...");
			} else {
				sendMessage(player, "Your titan recharges the " + name.toLowerCase() + ".");
			}
			return true;
		}

	}
}
