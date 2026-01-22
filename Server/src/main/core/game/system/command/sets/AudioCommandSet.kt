package core.game.system.command.sets

import core.api.playAudio
import core.api.playGlobalAudio
import core.game.node.entity.player.link.music.MusicEntry
import core.game.system.command.Privilege
import core.game.world.map.Location
import core.game.world.repository.Repository
import core.net.packet.PacketRepository
import core.net.packet.context.MusicContext
import core.net.packet.out.MusicPacket
import core.plugin.Initializable

@Initializable
class MusicCommandSet : CommandSet(Privilege.STANDARD){
    override fun defineCommands() {

        /**
         * Command that lets you play a specific song
         */
        define("playsong", usage = "::playsong <lt>song-id<gt>", description = "Plays the music track <lt>song-id<gt>."){player,args ->
            if(args.size < 2){
                reject(player,"Usage: ::playsong <song-id>")
            }
            val id = args[1].toIntOrNull()
            if(id == null){
                reject(player,"Please use a valid integer for the song ID.")
            }
            player.musicPlayer.play(MusicEntry.forId(id!!))
            notify(player,"Now playing song $id")
        }
        /**
         * Command that lets you play a specific jingle
         */
        define("playjingle", usage = "::playjingle <lt>jingle-id<gt>", description = "Plays the sound effect with ID <lt>jingle-id<gt>."){player,args ->
            if(args.size < 2){
                reject(player,"Usage: ::playjingle <jingle-id>")
            }
            val id = args[1].toIntOrNull()
            if(id == null){
                reject(player,"Please use a valid integer for the jingle ID.")
            }
            PacketRepository.send(MusicPacket::class.java, MusicContext(player, id!!, true))
            notify(player,"Now playing jingle $id")
        }

        /**
         * Command that lets you play a song via the ID instead of the song name
         * this is mostly useful for custom tracks that aren't in the ingame
         * music player yet.
         */
        define("playid", usage = "::playid <lt>song-id<gt>", description = "Plays a music track by musicId instead of numeric music track ID."){player,arg ->
            if (arg.size < 2)
                reject(player, "Needs more args.")
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
        define("allmusic", Privilege.ADMIN, description = "Unlocks every music track for you."){ player, _ ->
            for (me in MusicEntry.getSongs().values) {
                player.musicPlayer.unlock(me.id)
            }
        }
        /**
         * Command that lets you play an audio id
         */
        define("audio", Privilege.ADMIN, "::audio <lt>id<gt> loops[optional]", "Plays audio by ID. Loopable with optional integer argument.") { player, args ->
            if (args.size < 2 || args.size > 3)
                reject(player, "Usage: ::audio <id> loops[optional]")
            val id = args[1].toInt()
            val loops = if (args.size > 2) args[2].toInt() else 1
            playAudio(player, id, 0, loops)
        }
        /**
         * Command that lets you play an audio id globally by playername or coords
         */
        define("globalaudio", Privilege.ADMIN, "::globalaudio <lt>id<gt> radius[max 15] location[<lt>player name<gt> OR <lt>x y z<gt>]", "Play global audio by ID, radius, and location") { player, args ->
            if (args.size < 3)
                reject(player, "Usage: ::globalaudio <lt>id<gt> radius[max 15] location[<lt>player name<gt> OR <lt>x y z<gt>]")
            val loc = when (args.size) {
                6 -> Location(args[3].toInt(),args[4].toInt(),args[5].toInt())
                4 -> Repository.getPlayerByName(args[3])?.location
                else -> null
            }
            if (loc == null)
                reject(player, "Invalid player name / location")
            val id = args[1].toInt()
            val radius = if (args[2].toInt() > 15) 15 else args[2].toInt()
            if (loc != null) playGlobalAudio(loc, id, 0, 1, radius)
        }
    }
}
