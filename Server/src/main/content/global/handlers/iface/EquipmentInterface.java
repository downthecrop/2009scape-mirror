package content.global.handlers.iface;

import content.global.skill.summoning.familiar.BurdenBeast;
import core.api.ContentAPIKt;
import core.cache.def.impl.ItemDefinition;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.container.Container;
import core.game.container.ContainerEvent;
import core.game.container.ContainerListener;
import core.game.container.access.InterfaceContainer;
import core.game.container.impl.EquipmentContainer;
import core.game.interaction.OptionHandler;
import core.game.node.entity.combat.DeathTask;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.prayer.PrayerType;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import core.net.packet.PacketRepository;
import core.net.packet.context.ContainerContext;
import core.net.packet.out.ContainerPacket;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.game.global.action.EquipHandler;
import core.game.interaction.IntType;
import core.game.interaction.InteractionListeners;
import core.game.world.GameWorld;
import core.tools.Log;

/**
 * Represents the equipment interface.
 * @author Emperor
 * 
 */
@Initializable
public final class EquipmentInterface extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(102, this);
		ComponentDefinition.put(387, this);
		ComponentDefinition.put(667, this);
		ComponentDefinition.put(670, this);
		return this;
	}

	@Override
	public boolean handle(final Player p, Component component, int opcode, int button, final int slot, final int itemId) {
		if (component.getId() == 667) {
			if (button != 14) {
				return false;
			}
			switch (opcode) {
			case 155:
				p.getPulseManager().clear();
				GameWorld.getPulser().submit(new Pulse(1, p) {
					@Override
					public boolean pulse() {
						EquipHandler.unequip(p, slot, itemId);
						return true;
					}
				});
				return true;
			case 9:
				p.sendMessage(p.getEquipment().get(slot).getDefinition().getExamine());
				return true;
			case 196:
				GameWorld.getPulser().submit(new Pulse(1, p) {
					@Override
					public boolean pulse() {
						operate(p, slot, itemId);
						return true;
					}
				});
				return true;
			}
			return false;
		}
		else if (component.getId() == 670) {
			switch (opcode) {
			case 155:
				p.getPulseManager().clear();
				final Item item = p.getInventory().get(slot);
				GameWorld.getPulser().submit(new Pulse(1, p) {
					@Override
					public boolean pulse() {
						if (item == null) return true;
						InteractionListeners.run(item.getId(), IntType.ITEM,"equip",p, item);
						return true;
					}
				});
				return true;
			case 9:
				p.sendMessage(p.getInventory().get(slot).getDefinition().getExamine());
				return true;
			}
		}
		switch (opcode) {
		case 206:
			if (button != 28) {
				return false;
			}
			GameWorld.getPulser().submit(new Pulse(1, p) {
				@Override
				public boolean pulse() {
					operate(p, slot, itemId);
					return true;
				}
			});
			return true;
		default:
			switch (button) {
			case 52:
				if (p.getInterfaceManager().isOpened() && p.getInterfaceManager().getOpened().getId() == 102) {
					return true;
				}

				// (Highlight white items are auto destroyed on death Enum#616 (Items kept on death interface) TODO: Parse server sided
				// SCRIPT 118 - Items kept on death interface CS
				// ARG 0: Safe location check Takes: 0 Safe Area/2 in POH/3 in Castle Wars/4 in Trouble Brewing/5 in Barbass
				int zoneType = p.getZoneMonitor().getType();
				// ARG 1: Amount of items kept on death Takes: 0/1/3/4
				Container[] itemArray = DeathTask.getContainers(p);
				Container kept = itemArray[0];
				int amtKeptOnDeath = kept.itemCount();
				if (amtKeptOnDeath > 4 && zoneType == 0) {
					ContentAPIKt.log(this.getClass(), Log.ERR, "Items kept on death interface should not contain more than 4 items when not in a safe zone!");
				}
				// ARG 2: Item kept on death slot 0
				int slot0 = kept.getId(0);
				// ARG 3: Item kept on death slot 1
				int slot1 = kept.getId(1);
				// ARG 4: Item kept on death slot 2
				int slot2 = kept.getId(2);
				// ARG 5: Item kept on death slot 3
				int slot3 = kept.getId(3);
				// ARG 6: Player skulled Takes: 0 not skulled/1 skulled
				int skulled = p.getSkullManager().isSkulled() ? 1 : 0;
				// ARG 7: Player has summoning creature out Takes: 0 not out/1 Creature summoned
				int hasBoB;
				if (p.getFamiliarManager().hasFamiliar()) {
					if (p.getFamiliarManager().getFamiliar().isBurdenBeast()) {
						hasBoB = ((BurdenBeast) p.getFamiliarManager().getFamiliar()).getContainer().isEmpty() ? 0 : 1;
					} else {
						hasBoB = 0;
					}
				} else {
					hasBoB = 0;
				}
				// ARG 8: String for effect:
				// 			if (arg1 == 0) arg8 + " This reduces the items you keep from three to zero!"
				//			if (arg1 == 1) arg8 + " This reduces the items you keep from three to zero!" + "<br>" + "<br>" + "However, you also have the " + "<col=ff3333>" + "Protect Items" + "<col=ff981f>" + " prayer active, which saves you one extra item!");
				Object[] params = new Object[] { hasBoB, skulled, slot3, slot2, slot1, slot0, amtKeptOnDeath, zoneType, "You are skulled." };
				p.getPacketDispatch().sendRunScript(118, "siiooooii", params);

				p.getInterfaceManager().openComponent(102);
				break;
			case 28:
				if (opcode == 81) {
					p.getPulseManager().clear();
					GameWorld.getPulser().submit(new Pulse(1, p) {
						@Override
						public boolean pulse() {
							EquipHandler.unequip(p, slot, itemId);
							return true;
						}
					});
					return true;
				}
				break;
			case 55:
				if (p.getInterfaceManager().isOpened() && p.getInterfaceManager().getOpened().getId() == 667) {
					return true;
				}
				final ContainerListener listener = new ContainerListener() {
					@Override
					public void update(Container c, ContainerEvent e) {
						PacketRepository.send(ContainerPacket.class, new ContainerContext(p, -1, -1, 98, e.getItems(), false, e.getSlots()));
					}

					@Override
					public void refresh(Container c) {
						PacketRepository.send(ContainerPacket.class, new ContainerContext(p, -1, -1, 98, c, false));
					}
				};
				p.getInterfaceManager().openComponent(667).setCloseEvent((player, c) -> {
                    player.removeAttribute("equip_stats_open");
                    player.getInterfaceManager().closeSingleTab();
                    player.getInventory().getListeners().remove(listener);
                    return true;
                });
				p.setAttribute("equip_stats_open", true);
				EquipmentContainer.update(p);
				p.getInterfaceManager().openSingleTab(new Component(670));
				InterfaceContainer.generateItems(p, p.getInventory().toArray(), new String[] { "Equip" }, 670, 0, 7, 4, 93);
				p.getInventory().getListeners().add(listener);
				p.getInventory().refresh();
				ItemDefinition.statsUpdate(p);
				p.getPacketDispatch().sendIfaceSettings(1278, 14, 667, 0, 13);
				break;
			}
		}
		return true;
	}

	/**
	 * Operates an item.
	 * @param player The player.
	 * @param slot The container slot.
	 * @param itemId The item id.
	 */
	public void operate(Player player, int slot, int itemId) {
		if (slot < 0 || slot > 13) {
			return;
		}
		Item item = player.getEquipment().get(slot);
		if (item == null) {
			return;
		}
		if(InteractionListeners.run(item.getId(),IntType.ITEM,"operate",player,item)){
			return;
		}
		OptionHandler handler = item.getOperateHandler();
		if (handler != null && handler.handle(player, item, "operate")) {
			return;
		}
		player.getPacketDispatch().sendMessage("You can't operate that.");
	}

}
