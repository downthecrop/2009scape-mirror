package core.game.node.entity.player.info.stats

import core.game.component.Component
import core.game.content.global.BossKillCounter
import core.game.node.entity.player.Player
import core.game.world.repository.Repository
import core.plugin.Initializable
import core.game.system.command.Command
import core.game.system.command.sets.CommandSet

@Initializable
class StatsCommandSet : CommandSet(Command.Privilege.STANDARD) {
    override fun defineCommands() {
        define("stats"){player,args ->
            if(args.size == 1){
                sendStats(player)
                return@define
            }

            if(args.size > 2){
                reject(player,"Usage: ::stats playername")
                return@define
            }

            val other = Repository.getPlayerByName(args[1])
            if(other == null){
                reject(player,"Invalid player or player not online.")
                return@define
            }

            sendStats(player,other)
        }
    }

    fun sendStats(player: Player){
        sendStats(player,player)
    }

    fun sendStats(player: Player, other: Player){
        prepareInterface(player,other)
        val globalData = other.savedData.globalData
        for(i in (67..96)){
            when(i){
                //Various stats
                67 -> sendLine(player,"Easy Clues: ${other.treasureTrailManager.completedClues[0]}",97)
                68 -> sendLine(player,"Medium Clues: ${other.treasureTrailManager.completedClues[1]}",i)
                69 -> sendLine(player,"Hard Clues: ${other.treasureTrailManager.completedClues[2]}",i)
                70 -> sendLine(player,"<str>             </str>",i)
                71 -> sendLine(player,"Slayer Tasks: ${other.slayer.totalTasks}",i)
                72 -> sendLine(player,"Quest Points: ${other.questRepository.points}",i)
                73 -> sendLine(player,"Ironman Mode: ${other.ironmanManager.mode.name.toLowerCase()}",i)
                74 -> sendLine(player,"Deaths: ${other.getAttribute("$STATS_BASE:$STATS_DEATHS",0)}",i)
                75 -> sendLine(player,"<str>             </str>",i)
                76 -> sendLine(player,"Logs Chopped: ${other.getAttribute("$STATS_BASE:$STATS_LOGS",0)}",i)
                77 -> sendLine(player,"Rocks Mined: ${other.getAttribute("$STATS_BASE:$STATS_ROCKS",0)}",i)
                78 -> sendLine(player,"Fish Caught: ${other.getAttribute("$STATS_BASE:$STATS_FISH",0)}",i)

                //Boss KC
                82 -> sendLine(player, "KBD KC: ${globalData.bossCounters.get(BossKillCounter.KING_BLACK_DRAGON.ordinal)}",i)
                83 -> sendLine(player, "Bork KC: ${globalData.bossCounters.get(BossKillCounter.BORK.ordinal)}",i)
                84 -> sendLine(player, "Supreme KC: ${globalData.bossCounters.get(BossKillCounter.DAGANNOTH_SUPREME.ordinal)}",i)
                85 -> sendLine(player, "Rex KC: ${globalData.bossCounters.get(BossKillCounter.DAGANNOTH_REX.ordinal)}",i)
                86 -> sendLine(player, "Prime KC: ${globalData.bossCounters.get(BossKillCounter.DAGANNOTH_PRIME.ordinal)}",i)
                87 -> sendLine(player, "Barrows KC: ${globalData.barrowsLoots}",i)
                88 -> sendLine(player, "Chaos Ele: ${globalData.bossCounters.get(BossKillCounter.CHAOS_ELEMENTAL.ordinal)}",i)
                89 -> sendLine(player, "Mole KC: ${globalData.bossCounters.get(BossKillCounter.GIANT_MOLE.ordinal)}",i)
                90 -> sendLine(player, "Sara KC: ${globalData.bossCounters.get(BossKillCounter.SARADOMIN.ordinal)}",i)
                91 -> sendLine(player, "Zammy KC: ${globalData.bossCounters.get(BossKillCounter.ZAMORAK.ordinal)}",i)
                92 -> sendLine(player, "Bandos KC: ${globalData.bossCounters.get(BossKillCounter.BANDOS.ordinal)}",i)
                93 -> sendLine(player, "Arma KC: ${globalData.bossCounters.get(BossKillCounter.ARMADYL.ordinal)}",i)
                94 -> sendLine(player, "Jad KC: ${globalData.bossCounters.get(BossKillCounter.JAD.ordinal)}",i)
                95 -> sendLine(player, "KQ KC: ${globalData.bossCounters.get(BossKillCounter.KALPHITE_QUEEN.ordinal)}",i)
                96 -> sendLine(player, "Corp KC: ${globalData.bossCounters.get(BossKillCounter.CORPOREAL_BEAST.ordinal)}",i)
                else -> sendLine(player,"",i)
            }
        }
        player.interfaceManager.open(Component(26))
    }

    fun sendLine(player: Player, line: String, child: Int){
        player.packetDispatch.sendString(line,26,child)
    }

    fun prepareInterface(player: Player,other: Player){
        player.packetDispatch.sendInterfaceConfig(26,64,true)
        player.packetDispatch.sendInterfaceConfig(26,62,true)
        player.packetDispatch.sendInterfaceConfig(26,65,true)
        player.packetDispatch.sendInterfaceConfig(26,66,true)
        player.packetDispatch.sendString(other.username + "'s Stats",26,101)
    }
}