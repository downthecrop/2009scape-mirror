package rs09.game.node.entity.player.info.stats

import core.game.component.Component
import core.game.content.global.BossKillCounter
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items;
import org.rs09.consts.NPCs;
import rs09.game.content.global.GlobalKillCounter;
import rs09.game.interaction.InterfaceListener
import rs09.game.system.command.Command
import rs09.game.system.command.Privilege
import rs09.game.system.command.sets.CommandSet
import rs09.game.world.repository.Repository

val TUROTH_IDS = intArrayOf(NPCs.TUROTH_1622, NPCs.TUROTH_1623, NPCs.TUROTH_1626, NPCs.TUROTH_1627, NPCs.TUROTH_1628, NPCs.TUROTH_1629, NPCs.TUROTH_1630)
val KURASK_IDS = intArrayOf(NPCs.KURASK_1608, NPCs.KURASK_1609, NPCs.KURASK_4229, NPCs.KURASK_7811)
val GARGOYLE_IDS = intArrayOf(NPCs.GARGOYLE_1610, NPCs.GARGOYLE_1827, NPCs.GARGOYLE_6389)
val SPIRITUAL_MAGE_IDS = intArrayOf(NPCs.SPIRITUAL_MAGE_6221, NPCs.SPIRITUAL_MAGE_6231, NPCs.SPIRITUAL_MAGE_6257, NPCs.SPIRITUAL_MAGE_6278)
val GREEN_DRAGON_IDS = intArrayOf(NPCs.GREEN_DRAGON_941, NPCs.GREEN_DRAGON_4677, NPCs.GREEN_DRAGON_4678, NPCs.GREEN_DRAGON_4679, NPCs.GREEN_DRAGON_4680, NPCs.BRUTAL_GREEN_DRAGON_5362)
val BLUE_DRAGON_IDS = intArrayOf(NPCs.BLUE_DRAGON_55, NPCs.BLUE_DRAGON_4681, NPCs.BLUE_DRAGON_4682, NPCs.BLUE_DRAGON_4683, NPCs.BLUE_DRAGON_4684, NPCs.BLUE_DRAGON_5178)
val RED_DRAGON_IDS = intArrayOf(NPCs.RED_DRAGON_53, NPCs.RED_DRAGON_4669, NPCs.RED_DRAGON_4670, NPCs.RED_DRAGON_4671, NPCs.RED_DRAGON_4672)
val BLACK_DRAGON_IDS = intArrayOf(NPCs.BLACK_DRAGON_54, NPCs.BLACK_DRAGON_4673, NPCs.BLACK_DRAGON_4674, NPCs.BLACK_DRAGON_4675, NPCs.BLACK_DRAGON_4676)
val BRONZE_DRAGON_IDS = intArrayOf(NPCs.BRONZE_DRAGON_1590)
val IRON_DRAGON_IDS = intArrayOf(NPCs.IRON_DRAGON_1591)
val STEEL_DRAGON_IDS = intArrayOf(NPCs.STEEL_DRAGON_1592, NPCs.STEEL_DRAGON_3590)
val MITHRIL_DRAGON_IDS = intArrayOf(NPCs.MITHRIL_DRAGON_5363, NPCs.MITHRIL_DRAGON_8424)
val SKELETAL_WYVERN_IDS = intArrayOf(NPCs.SKELETAL_WYVERN_3068, NPCs.SKELETAL_WYVERN_3069, NPCs.SKELETAL_WYVERN_3070, NPCs.SKELETAL_WYVERN_3071)

val SPACER = "<str>             </str>";
val NUM_PAGES = 3

fun sendStats(player: Player, other: Player, page: Int){
    prepareInterface(player, other, page)
    val globalData = other.savedData.globalData
    for(i in (68..97)) {
        when(page) {
            0 -> {
                when(i) {
                    //Various stats
                    97 -> sendLine(player,"Easy Clues: ${other.treasureTrailManager.completedClues[0]}",i)
                    68 -> sendLine(player,"Medium Clues: ${other.treasureTrailManager.completedClues[1]}",i)
                    69 -> sendLine(player,"Hard Clues: ${other.treasureTrailManager.completedClues[2]}",i)
                    70 -> sendLine(player,SPACER,i)
                    71 -> sendLine(player,"Slayer Tasks: ${other.slayer.flags.completedTasks}",i)
                    72 -> sendLine(player,"Quest Points: ${other.questRepository.points}",i)
                    73 -> sendLine(player,"Ironman Mode: ${other.ironmanManager.mode.name.toLowerCase()}",i)
                    74 -> sendLine(player,"Deaths: ${other.getAttribute("$STATS_BASE:$STATS_DEATHS",0)}",i)
                    75 -> sendLine(player,SPACER,i)
                    76 -> sendLine(player,"Logs Chopped: ${other.getAttribute("$STATS_BASE:$STATS_LOGS",0)}",i)
                    77 -> sendLine(player,"Rocks Mined: ${other.getAttribute("$STATS_BASE:$STATS_ROCKS",0)}",i)
                    78 -> sendLine(player,"Fish Caught: ${other.getAttribute("$STATS_BASE:$STATS_FISH",0)}",i)
                    79 -> sendLine(player, "Essence Crafted: ${other.getAttribute("$STATS_BASE:$STATS_RC",0)}", i)

                    //Boss KC
                    82 -> sendLine(player, "KBD KC: ${globalData.bossCounters.get(BossKillCounter.KING_BLACK_DRAGON.ordinal)}",i)
                    83 -> sendLine(player, "TDs KC: ${globalData.bossCounters.get(BossKillCounter.TORMENTED_DEMONS.ordinal)}",i)
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
            1 -> {
                when(i) {
                    97 -> sendLine(player, "Turoths: ${GlobalKillCounter.getKills(other, TUROTH_IDS)}", i)
                    68 -> sendLine(player, "Kurasks: ${GlobalKillCounter.getKills(other, KURASK_IDS)}", i)
                    69 -> sendLine(player, "Leaf-bladed swords: ${GlobalKillCounter.getRareDrops(other, Items.LEAF_BLADED_SWORD_13290)}", i)
                    70 -> sendLine(player,SPACER,i)
                    71 -> sendLine(player, "Gargoyles: ${GlobalKillCounter.getKills(other, GARGOYLE_IDS)}", i)
                    72 -> sendLine(player, "Granite mauls: ${GlobalKillCounter.getRareDrops(other, Items.GRANITE_MAUL_4153)}", i)
                    73 -> sendLine(player,SPACER,i)
                    74 -> sendLine(player, "Spiritual mages: ${GlobalKillCounter.getKills(other, SPIRITUAL_MAGE_IDS)}", i)
                    75 -> sendLine(player, "Dragon boots: ${GlobalKillCounter.getRareDrops(other, Items.DRAGON_BOOTS_11732)}", i)
                    76 -> sendLine(player,SPACER,i)
                    77 -> sendLine(player, "Abyssal demons: ${GlobalKillCounter.getKills(other, NPCs.ABYSSAL_DEMON_1615)}", i)
                    78 -> sendLine(player, "Abyssal whips: ${GlobalKillCounter.getRareDrops(other, Items.ABYSSAL_WHIP_4151)}", i)
                    79 -> sendLine(player,SPACER,i)
                    80 -> sendLine(player, "Dark beasts: ${GlobalKillCounter.getKills(other, NPCs.DARK_BEAST_2783)}", i)
                    81 -> sendLine(player, "Dark bows: ${GlobalKillCounter.getRareDrops(other, Items.DARK_BOW_11235)}", i)

                    82 -> sendLine(player, "Green Dragons: ${GlobalKillCounter.getKills(other, GREEN_DRAGON_IDS)}", i)
                    83 -> sendLine(player, "Blue Dragons: ${GlobalKillCounter.getKills(other, BLUE_DRAGON_IDS)}", i)
                    84 -> sendLine(player, "Red Dragons: ${GlobalKillCounter.getKills(other, RED_DRAGON_IDS)}", i)
                    85 -> sendLine(player, "Black Dragons: ${GlobalKillCounter.getKills(other, BLACK_DRAGON_IDS)}", i)
                    86 -> sendLine(player,SPACER,i)
                    87 -> sendLine(player, "Bronze Dragons: ${GlobalKillCounter.getKills(other, BRONZE_DRAGON_IDS)}", i)
                    88 -> sendLine(player, "Iron Dragons: ${GlobalKillCounter.getKills(other, IRON_DRAGON_IDS)}", i)
                    89 -> sendLine(player, "Steel Dragons: ${GlobalKillCounter.getKills(other, STEEL_DRAGON_IDS)}", i)
                    90 -> sendLine(player, "Mithril Dragons: ${GlobalKillCounter.getKills(other, MITHRIL_DRAGON_IDS)}", i)
                    91 -> sendLine(player, "Skeletal Wyverns: ${GlobalKillCounter.getKills(other, SKELETAL_WYVERN_IDS)}", i)
                    92 -> sendLine(player,SPACER,i)
                    93 -> sendLine(player, "Draconic visages: ${GlobalKillCounter.getRareDrops(other, Items.DRACONIC_VISAGE_11286)}", i)

                    else -> sendLine(player,"",i)
                }
            }
            2 -> {
                when(i) {
                    97 -> sendLine(player, "Ahrim's hood: ${GlobalKillCounter.getRareDrops(other, Items.AHRIMS_HOOD_4708)}", i)
                    68 -> sendLine(player, "Ahrim's staff: ${GlobalKillCounter.getRareDrops(other, Items.AHRIMS_STAFF_4710)}", i)
                    69 -> sendLine(player, "Ahrim's robetop: ${GlobalKillCounter.getRareDrops(other, Items.AHRIMS_ROBETOP_4712)}", i)
                    70 -> sendLine(player, "Ahrim's robeskirt: ${GlobalKillCounter.getRareDrops(other, Items.AHRIMS_ROBESKIRT_4714)}", i)
                    71 -> sendLine(player,SPACER,i)
                    72 -> sendLine(player, "Dharok's helm: ${GlobalKillCounter.getRareDrops(other, Items.DHAROKS_HELM_4716)}", i)
                    73 -> sendLine(player, "Dharok's greataxe: ${GlobalKillCounter.getRareDrops(other, Items.DHAROKS_GREATAXE_4718)}", i)
                    74 -> sendLine(player, "Dharok's platebody: ${GlobalKillCounter.getRareDrops(other, Items.DHAROKS_PLATEBODY_4720)}", i)
                    75 -> sendLine(player, "Dharok's platelegs: ${GlobalKillCounter.getRareDrops(other, Items.DHAROKS_PLATELEGS_4722)}", i)
                    76 -> sendLine(player,SPACER,i)
                    77 -> sendLine(player, "Guthan's helm: ${GlobalKillCounter.getRareDrops(other, Items.GUTHANS_HELM_4724)}", i)
                    78 -> sendLine(player, "Guthan's warspear: ${GlobalKillCounter.getRareDrops(other, Items.GUTHANS_WARSPEAR_4726)}", i)
                    79 -> sendLine(player, "Guthan's platebody: ${GlobalKillCounter.getRareDrops(other, Items.GUTHANS_PLATEBODY_4728)}", i)
                    80 -> sendLine(player, "Guthan's chainskirt: ${GlobalKillCounter.getRareDrops(other, Items.GUTHANS_CHAINSKIRT_4730)}", i)

                    82 -> sendLine(player, "Karil's coif: ${GlobalKillCounter.getRareDrops(other, Items.KARILS_COIF_4732)}", i)
                    83 -> sendLine(player, "Karil's crossbow: ${GlobalKillCounter.getRareDrops(other, Items.KARILS_CROSSBOW_4734)}", i)
                    84 -> sendLine(player, "Karil's leathertop: ${GlobalKillCounter.getRareDrops(other, Items.KARILS_LEATHERTOP_4736)}", i)
                    85 -> sendLine(player, "Karil's leatherskirt: ${GlobalKillCounter.getRareDrops(other, Items.KARILS_LEATHERSKIRT_4738)}", i)
                    86 -> sendLine(player,SPACER,i)
                    87 -> sendLine(player, "Torag's helm: ${GlobalKillCounter.getRareDrops(other, Items.TORAGS_HELM_4745)}", i)
                    88 -> sendLine(player, "Torag's hammers: ${GlobalKillCounter.getRareDrops(other, Items.TORAGS_HAMMERS_4747)}", i)
                    89 -> sendLine(player, "Torag's platebody: ${GlobalKillCounter.getRareDrops(other, Items.TORAGS_PLATEBODY_4749)}", i)
                    90 -> sendLine(player, "Torag's platelegs: ${GlobalKillCounter.getRareDrops(other, Items.TORAGS_PLATELEGS_4751)}", i)
                    91 -> sendLine(player,SPACER,i)
                    92 -> sendLine(player, "Verac's helm: ${GlobalKillCounter.getRareDrops(other, Items.VERACS_HELM_4753)}", i)
                    93 -> sendLine(player, "Verac's flail: ${GlobalKillCounter.getRareDrops(other, Items.VERACS_FLAIL_4755)}", i)
                    94 -> sendLine(player, "Verac's brassard: ${GlobalKillCounter.getRareDrops(other, Items.VERACS_BRASSARD_4757)}", i)
                    95 -> sendLine(player, "Verac's plateskirt: ${GlobalKillCounter.getRareDrops(other, Items.VERACS_PLATESKIRT_4759)}", i)
                    else -> sendLine(player,"",i)
                }
            }
        }
    }
    player.interfaceManager.open(Component(26))
}

fun sendLine(player: Player, line: String, child: Int) {
    player.packetDispatch.sendString(line,26,child)
}

fun prepareInterface(player: Player,other: Player, page: Int){
    player.setAttribute("stats-page-info", StatsPageInfo(other, page))
    if(page == 0) {
        player.packetDispatch.sendInterfaceConfig(26,62,true)
        player.packetDispatch.sendInterfaceConfig(26,65,true)
    } else {
        sendLine(player, "Previous", 65)
    }
    if(page == NUM_PAGES - 1) {
        player.packetDispatch.sendInterfaceConfig(26,64,true)
        player.packetDispatch.sendInterfaceConfig(26,66,true)
    } else {
        sendLine(player, "Next", 66)
    }
    player.packetDispatch.sendString(other.username + "'s Stats - Page ${page + 1}",26,101)
}

data class StatsPageInfo(val other: Player, val page: Int) {}

@Initializable
class StatsCommandSet : CommandSet(Privilege.STANDARD) {
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

            sendStats(player,other, 0)
        }
    }

    fun sendStats(player: Player){
        sendStats(player,player, 0)
    }

}

class StatsInterface : InterfaceListener() {
    val COMPONENT_ID = 26

    override fun defineListeners() {
        on(COMPONENT_ID, 61) { player, _, _, _, _, _ ->
            val info: StatsPageInfo? = player.getAttribute("stats-page-info", null)
            if(info != null) {
                sendStats(player, info.other, info.page-1)
            }
            return@on true
        }
        on(COMPONENT_ID, 63) { player, _, _, _, _, _ ->
            val info: StatsPageInfo? = player.getAttribute("stats-page-info", null)
            if(info != null) {
                sendStats(player, info.other, info.page+1)
            }
            return@on true
        }
    }
}
