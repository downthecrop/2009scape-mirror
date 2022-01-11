package core.game.interaction.inter;

import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.RunScript;
import core.game.system.communication.ClanRank;
import core.game.system.communication.ClanRepository;
import core.net.amsc.MSPacketRepository;
import core.net.amsc.WorldCommunicator;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.StringUtils;
import kotlin.Unit;

import static api.ContentAPIKt.sendInputDialogue;
import static api.ContentAPIKt.setInterfaceText;

/**
 * Represents the plugin used to handle the clan interfaces.
 * @author Vexia
 */
@Initializable
public final class ClanInterfacePlugin extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(590, this);
		ComponentDefinition.put(589, this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		switch (component.getId()) {
		case 589:
			switch (button) {
			case 9:
				if (player.getInterfaceManager().getComponent(590) != null) {
					player.getPacketDispatch().sendMessage("Please close the interface you have open before using 'Clan Setup'");
					return true;
				}
				ClanRepository.openSettings(player);
				return true;
			case 14:
				player.getDetails().getCommunication().toggleLootshare(player);
				return true;
			}
			break;
		case 590:
			final ClanRepository clan = ClanRepository.get(player.getName(), true);
			switch (button) {
			case 23:
				if (opcode == 155) {
					clan.setJoinRequirement(ClanRank.NONE);
				} else {
					clan.setJoinRequirement(getRank(opcode));
				}
				player.getDetails().getCommunication().setJoinRequirement(clan.getJoinRequirement());
				MSPacketRepository.setClanSetting(player, 0, clan.getJoinRequirement());
				player.getPacketDispatch().sendString(clan.getJoinRequirement().getInfo(), 590, 23);
				break;
			case 24:
				clan.setMessageRequirement(getRank(opcode));
				player.getDetails().getCommunication().setMessageRequirement(clan.getMessageRequirement());
				MSPacketRepository.setClanSetting(player, 1, clan.getMessageRequirement());
				player.getPacketDispatch().sendString(clan.getMessageRequirement().getInfo(), 590, 24);
				break;
			case 25:
				clan.setKickRequirement(getRank(opcode));
				player.getDetails().getCommunication().setKickRequirement(clan.getKickRequirement());
				MSPacketRepository.setClanSetting(player, 2, clan.getKickRequirement());
				player.getPacketDispatch().sendString(clan.getKickRequirement().getInfo(), 590, 25);
				clan.update();
				break;
			case 26:
				if (opcode == 230) {
					clan.setLootRequirement(ClanRank.ADMINISTRATOR);
				} else {
					clan.setLootRequirement(getRank(opcode));
				}
				player.getDetails().getCommunication().setLootRequirement(clan.getLootRequirement());
				MSPacketRepository.setClanSetting(player, 3, clan.getLootRequirement());
				player.getPacketDispatch().sendString(clan.getLootRequirement().getInfo(), 590, 26);
				break;
			case 22:
				switch (opcode) {
				case 196:
					clan.setName("Chat disabled");
					player.getCommunication().setClanName("");
					player.getPacketDispatch().sendString(clan.getName(), 590, 22);
					if (WorldCommunicator.isEnabled()) {
						MSPacketRepository.sendClanRename(player, "");
						break;
					}
					clan.clean(true);
					break;
				default:
					sendInputDialogue(player, false, "Enter clan prefix:", (value) -> {
						String name = StringUtils.formatDisplayName((String) value);
						setInterfaceText(player, name, 590, 22);
						if(WorldCommunicator.isEnabled()){
							MSPacketRepository.sendClanRename(player, name);
							clan.setName(name);
							return Unit.INSTANCE;
						}
						if (clan.getName().equals("Chat disabled")) {
							player.getPacketDispatch().sendMessage("Your clan channel has now been enabled!");
							player.getPacketDispatch().sendMessage("Join your channel by clicking 'Join Chat' and typing: " + player.getUsername());
						}
						clan.setName(name);
						player.getCommunication().setClanName(name);
						clan.update();
						return Unit.INSTANCE;
					});
					break;
				}
				break;
			}
			break;
		}
		return true;
	}

	/**
	 * Gets the value to set.
	 * @param opcode the opcode.
	 * @return the value.
	 */
	public static ClanRank getRank(int opcode) {
		switch (opcode) {
		case 155:
			return ClanRank.NONE;
		case 196:
			return ClanRank.FRIEND;
		case 124:
			return ClanRank.RECRUIT;
		case 199:
			return ClanRank.CORPORAL;
		case 234:
			return ClanRank.SERGEANT;
		case 168:
			return ClanRank.LIEUTENANT;
		case 166:
			return ClanRank.CAPTAIN;
		case 64:
			return ClanRank.GENERAL;
		case 53:
			return ClanRank.OWNER;
		}
		return ClanRank.NONE;
	}

}
