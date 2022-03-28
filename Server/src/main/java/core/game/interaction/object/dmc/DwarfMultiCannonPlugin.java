package core.game.interaction.object.dmc;

import core.cache.def.impl.ItemDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.OptionHandler;
import core.game.interaction.UseWithHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.Rights;
import core.game.node.item.Item;
import core.plugin.Plugin;
import core.plugin.Initializable;
import rs09.plugin.ClassScanner;

/**
 * Handles the Dwarf multi-cannon.
 * @author Emperor
 */
@Initializable
public final class DwarfMultiCannonPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(6).getHandlers().put("option:set-up", this);
		SceneryDefinition.forId(6).getHandlers().put("option:fire", this);
		SceneryDefinition.forId(6).getHandlers().put("option:pick-up", this);
		SceneryDefinition.forId(7).getHandlers().put("option:pick-up", this);
		SceneryDefinition.forId(8).getHandlers().put("option:pick-up", this);
		SceneryDefinition.forId(9).getHandlers().put("option:pick-up", this);
		UseWithHandler.addHandler(6, UseWithHandler.OBJECT_TYPE, new UseWithHandler(2) {
			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				return this;
			}

			@Override
			public boolean handle(NodeUsageEvent event) {
				DMCHandler handler = event.getPlayer().getAttribute("dmc");
				if (handler != null && handler.getCannon() == event.getUsedWith()) {
					int maxAmount = 30;
					int amount = maxAmount - handler.getCannonballs();
					if(amount < 0 || amount > 30) {
						handler.setCannonballs(0);
						amount = maxAmount - handler.getCannonballs();
					}
					if (amount > 0) {
						if (amount > event.getUsedItem().getAmount()) {
							amount = event.getUsedItem().getAmount();
						}
						if (event.getPlayer().getInventory().remove(new Item(2, amount))) {
							handler.setCannonballs(handler.getCannonballs() + amount);
							event.getPlayer().getPacketDispatch().sendMessage("You load the cannon with " + amount + " cannonballs.");
							return true;
						}
					}
					event.getPlayer().getPacketDispatch().sendMessage("Your cannon is already full loaded.");
					return true;
				}
				event.getPlayer().getPacketDispatch().sendMessage("This is not your cannon.");
				return true;
			}

		});
		ClassScanner.definePlugin(new DMCZone());
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (node instanceof Item) {
			if (!player.getInventory().containItems(6, 8, 10, 12)) {
				player.getPacketDispatch().sendMessage("You don't have all the cannon components!");
				return true;
			}
			if (!player.getQuestRepository().isComplete("Dwarf Cannon") && player.getDetails().getRights() != Rights.ADMINISTRATOR) {
				player.getPacketDispatch().sendMessage("You have to complete the Dwarf Cannon to know how to use this.");
				return true;
			}
			if (player.getAttribute("dmc") != null) {
				player.getPacketDispatch().sendMessage("You cannot construct more than one cannon at a time.");
				player.getPacketDispatch().sendMessage("If you have lost your cannon, go and see the dwarf cannon engineer.");
				return true;
			}
			DMCHandler.construct(player);
			return true;
		}
		switch (option) {
		case "fire":
			DMCHandler handler = player.getAttribute("dmc");
			if (handler != null && handler.getCannon() == node) {
				handler.startFiring();
				return true;
			}
			player.getPacketDispatch().sendMessage("This is not your cannon.");
			return true;
		case "pick-up":
			handler = player.getAttribute("dmc");
			if (handler != null && handler.getCannon() == node) {
				int count = 4;
				if (handler.getCannonballs() > 0 && !player.getInventory().contains(2, 1)) {
					count++;
				}
				if (player.getInventory().freeSlots() < count) {
					player.getPacketDispatch().sendMessage("You don't have enough space in your inventory.");
					return true;
				}
				player.getPacketDispatch().sendMessage("You pick up the cannon.");
				handler.clear(true);
				return true;
			}
			/*
			 * if (player.getDetails().getRights() == Rights.ADMINISTRATOR) {
			 * ObjectBuilder.remove((GameObject) node);
			 * player.removeAttribute("dmc"); for (int i = 3; i >= 0; i--) {
			 * player.getInventory().add(new Item(6 + (i * 2))); } return true;
			 * }
			 */
			player.getPacketDispatch().sendMessage("This is not your cannon.");
			return true;
		}
		return false;
	}

}
