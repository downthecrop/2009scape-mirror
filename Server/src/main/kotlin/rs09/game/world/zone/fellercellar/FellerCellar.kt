package rs09.game.world.zone.fellercellar

import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.net.packet.PacketRepository
import core.net.packet.context.MusicContext
import core.net.packet.out.MusicPacket
import core.tools.RandomFunction
import rs09.tools.stringtools.colorize

object FellerCellar {
    val SONG_LENGTHS = intArrayOf(222,222,100,225,430,400,147,575,141,275,146,635)
    val SONG_NAMES = arrayOf("Red Wings","Clocktower","Pizza Time","Clash On The Big Bridge","Pokke Village","Corridors of Time","Cirno's Theme",
    "Golden Sneer","Crash 3 Warp Room","Slider Theme","Fun Naming","Fight On!")
    var FellerCellarPlayerList = ArrayList<Player>()
    var random = RandomFunction.getRandom(11)
    var lastRoll = random
    var songTime = SONG_LENGTHS[random]

    var fellerPulse = object : Pulse() {
        override fun pulse(): Boolean {
            if(songTime <= 0){
                random = RandomFunction.getRandom(11)
                while(random == lastRoll){
                    random = RandomFunction.getRandom(11)
                }
                lastRoll = random
                songTime = SONG_LENGTHS[random]
                FellerCellarPlayerList.forEach { player ->
                    shuffleTracks(player, random)
                }
                for (player in FellerCellarPlayerList) {
                    if(random == 7){
                        player.sendMessage(colorize("%RNow playing: ${SONG_NAMES[random]}"))
                    }
                    else player.sendMessage(colorize("%GNow playing: ${SONG_NAMES[random]}"))
                }
            }
            songTime--
            return false
        }
    }

    fun shuffleTracks(player: Player, random: Int) {
        when(random){
            0 -> PacketRepository.send(MusicPacket::class.java, MusicContext(player,668))
            1 -> PacketRepository.send(MusicPacket::class.java, MusicContext(player,669))
            2 -> PacketRepository.send(MusicPacket::class.java, MusicContext(player,670))
            3 -> PacketRepository.send(MusicPacket::class.java, MusicContext(player,671))
            4 -> PacketRepository.send(MusicPacket::class.java, MusicContext(player,672))
            5 -> PacketRepository.send(MusicPacket::class.java, MusicContext(player,673))
            6 -> PacketRepository.send(MusicPacket::class.java, MusicContext(player,674))
            7 -> PacketRepository.send(MusicPacket::class.java, MusicContext(player,675))
            8 -> PacketRepository.send(MusicPacket::class.java, MusicContext(player,676))
            9 -> PacketRepository.send(MusicPacket::class.java, MusicContext(player,677))
            10 -> PacketRepository.send(MusicPacket::class.java, MusicContext(player,678))
            11 -> PacketRepository.send(MusicPacket::class.java, MusicContext(player,679))
        }

    }

}