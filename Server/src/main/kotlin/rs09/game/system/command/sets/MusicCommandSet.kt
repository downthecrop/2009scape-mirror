package rs09.game.system.command.sets

import core.game.node.entity.player.link.music.MusicEntry
import core.net.packet.PacketRepository
import core.net.packet.context.MusicContext
import core.net.packet.out.MusicPacket
import core.plugin.Initializable
import rs09.game.system.command.Command
import rs09.game.system.command.Privilege

@Initializable
class MusicCommandSet : CommandSet(Privilege.STANDARD){
    override fun defineCommands() {

        /**
         * Command that lets you play a specific song
         */
        define("playsong"){player,args ->
            if(args.size < 2){
                reject(player,"Usage: ::playsong songID")
            }
            val id = args[1].toIntOrNull()
            if(id == null){
                reject(player,"Please use a valid integer for the song id.")
            }
            player.musicPlayer.play(MusicEntry.forId(id!!))
            notify(player,"Now playing song $id")
        }

        /**
         * Command that lets you play a song via the ID instead of the song name
         * this is mostly useful for custom tracks that aren't in the ingame
         * music player yet.
         */
        define("playid"){player,arg ->
            val id = arg[1].toIntOrNull()
            if(id != null){
                PacketRepository.send(MusicPacket::class.java, MusicContext(player, id))
                notify(player,"Now playing song $id")
            }
        }

        /**
         * Command that unlocks all music tracks for the player
         * Restricted to ADMIN access only.
         */
        define("allmusic", Privilege.ADMIN){ player, _ ->
            for (me in MusicEntry.getSongs().values) {
                player.musicPlayer.unlock(me.id)
            }
        }
    }
}