package content.global.handlers.iface.tabs

import core.api.sendInputDialogue
import core.api.sendMessage
import core.game.interaction.InterfaceListener
import core.game.system.communication.ClanRank
import core.game.system.communication.ClanRepository
import core.net.amsc.MSPacketRepository
import core.net.amsc.WorldCommunicator
import core.tools.StringUtils
import org.rs09.consts.Components

class ClanTabInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.CLANJOIN_589) { player, _, _, buttonID, _, _ ->
            if (buttonID == 9) {
                if (player.interfaceManager.opened != null) {
                    sendMessage(player, "Please close the interface you have open before using 'Clan Setup'")
                } else {
                    ClanRepository.openSettings(player)
                }
            }

            if (buttonID == 14) {
                player.communication.toggleLootshare(player)
            }
            return@on true
        }

        on(Components.CLANSETUP_590) { player, _, opcode, buttonID, _, _ ->
            val clan = ClanRepository.get(player.name, true)

            when (buttonID) {
                22 -> {
                    if (opcode == 155) {
                        sendInputDialogue(player, false, "Enter clan prefix:", ) { value ->
                            val clanName = StringUtils.formatDisplayName(value.toString())

                            if (WorldCommunicator.isEnabled()) { MSPacketRepository.sendClanRename(player, clanName) }

                            if (clan.name == "Chat disabled") {
                                sendMessage(player, "Your clan channel has now been enabled!")
                                sendMessage(player, "Join your channel by clicking 'Join Chat' and typing: ${player.username}")
                            }

                            clan.name = clanName
                            player.communication.clanName = clanName
                            clan.updateSettings(player)
                            clan.update()
                        }
                    }

                    if (opcode == 196) {
                        clan.name = "Chat disabled"
                        player.communication.clanName = ""
                        if (WorldCommunicator.isEnabled()) { MSPacketRepository.sendClanRename(player, player.communication.clanName) }
                        clan.updateSettings(player)
                        clan.delete()
                    }
                }

                23 -> {
                    clan.joinRequirement = getRank(opcode)
                    player.communication.joinRequirement = clan.joinRequirement
                    MSPacketRepository.setClanSetting(player, 0, clan.joinRequirement)
                }

                24 -> {
                    clan.messageRequirement = getRank(opcode)
                    player.communication.messageRequirement = clan.messageRequirement
                    MSPacketRepository.setClanSetting(player, 1, clan.messageRequirement)
                }

                25 -> {
                    clan.kickRequirement = getRank(opcode)
                    player.communication.kickRequirement = clan.kickRequirement
                    MSPacketRepository.setClanSetting(player, 2, clan.kickRequirement)
                }

                26 -> {
                    clan.lootRequirement = if (opcode == 155) ClanRank.ADMINISTRATOR else getRank(opcode)
                    player.communication.lootRequirement = clan.lootRequirement
                    MSPacketRepository.setClanSetting(player, 3, clan.lootRequirement)
                }

                33 -> sendMessage(player, "CoinShare is not available.")
            }

            clan.updateSettings(player)
            clan.update()
            return@on true
        }
    }

    fun getRank(opcode: Int): ClanRank {
        return when (opcode) {
            155 -> ClanRank.NONE
            196 -> ClanRank.FRIEND
            124 -> ClanRank.RECRUIT
            199 -> ClanRank.CORPORAL
            234 -> ClanRank.SERGEANT
            168 -> ClanRank.LIEUTENANT
            166 -> ClanRank.CAPTAIN
            64 -> ClanRank.GENERAL
            53 -> ClanRank.OWNER
            else -> ClanRank.NONE
        }
    }
}