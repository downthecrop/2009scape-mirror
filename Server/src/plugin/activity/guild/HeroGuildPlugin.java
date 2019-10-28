package plugin.activity.guild;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.global.EnchantedJewellery;
import org.crandor.game.content.global.action.DoorActionHandler;
import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.PluginManager;

/**
 * Represents the hero guild.
 * @author Vexia
 */
@InitializablePlugin
public final class HeroGuildPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(2624).getConfigurations().put("option:open", this);
		ObjectDefinition.forId(2625).getConfigurations().put("option:open", this);
		PluginManager.definePlugin(new JewelleryRechargePlugin());
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final int id = ((GameObject) node).getId();
		switch (option) {
		case "open":
			switch (id) {
			case 2624:
			case 2625:
				// player.getPacketDispatch().sendMessage("You need to complete the Heroes' Quest.");
				DoorActionHandler.handleAutowalkDoor(player, (GameObject) node);
				break;
			}
			return true;
		}
		return true;
	}

	/**
	 * Handles the recharging of dragonstone jewellery.
	 * @author Vexia
	 */
	public static final class JewelleryRechargePlugin extends UseWithHandler {

		/**
		 * The ids of rechargeable items.
		 */
		private static final int[] IDS = new int[] { 1710, 1708, 1706, 1704, 11107, 11109, 11111, 11113, 11120, 11122, 11124, 11126, 10354, 10356, 10358, 10360, 10362 };

		/**
		 * Constructs a new {@Code JewelleryRechargePlugin} {@Code
		 *  Object}
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
			final EnchantedJewellery jewellery = EnchantedJewellery.forItem(event.getUsedItem());
			if (jewellery == null) {
				return true;
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
			player.getInventory().replace(new Item(jewellery.getIds()[0]), event.getUsedItem().getSlot());
			String name = event.getUsedItem().getName().toLowerCase();
			for (int i = 0; i < 4; i++) {
				name = name.replace("(" + (i + 1) + ")", "").trim();
			}
			if (!fam) {
				player.sendMessage("You dip the " + name + " in the fountain...");
			} else {
				player.sendMessage("Your titan recharges the glory.");
			}
			return true;
		}

	}
}
