package content.global.handlers.iface.tabs

import core.api.sendMessage
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.link.music.MusicEntry
import org.rs09.consts.Components

class MusicTabInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.MUSIC_V3_187) { player, _, opcode, buttonID, slot, _ ->
            if (opcode == 155) {
                if (buttonID == 11) {
                    player.musicPlayer.toggleLooping()
                    return@on true
                }

                if (buttonID == 1) {
                    if (player.musicPlayer.unlocked[slot] != null) {
                        player.musicPlayer.play(player.musicPlayer.unlocked[slot])
                        return@on true
                    }

                    if (player.isAdmin) {
                        for (entry in MusicEntry.getSongs().values) {
                            if (entry.index == slot) {
                                player.musicPlayer.unlock(entry.id)
                            }
                        }
                    } else {
                        sendMessage(player, "You have not unlocked this piece of music yet!")
                    }
                    return@on true
                }
            }
            return@on true
        }
    }
}