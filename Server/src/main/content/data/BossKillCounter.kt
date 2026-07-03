package content.data

import core.api.sendMessage
import core.game.node.entity.player.Player
import org.rs09.consts.NPCs

/**
 * Enumerates the bosses that have their kill counts tracked in a player's statistics (::stats).
 * addToBossKillCount(player, npcId) should be added in the finalizeDeath() method of the combat handler for the boss.
 * ORDINAL BOUND
 * @author Bishop
 */

enum class BossKillCounter(val bossId: IntArray, val bossName: String) {

    KING_BLACK_DRAGON(intArrayOf(NPCs.KING_BLACK_DRAGON_50), "King Black Dragon"),
    BORK(intArrayOf(NPCs.BORK_7133, NPCs.BORK_7134), "Bork"),
    DAGANNOTH_SUPREME(intArrayOf(NPCs.DAGANNOTH_SUPREME_2881), "Dagannoth Supreme"),
    DAGANNOTH_PRIME(intArrayOf(NPCs.DAGANNOTH_PRIME_2882), "Dagannoth Prime"),
    DAGANNOTH_REX(intArrayOf(NPCs.DAGANNOTH_REX_2883), "Dagannoth Rex"),
    CHAOS_ELEMENTAL(intArrayOf(NPCs.CHAOS_ELEMENTAL_3200), "Chaos Elemental"),
    GIANT_MOLE(intArrayOf(NPCs.GIANT_MOLE_3340), "Giant Mole"),
    SARADOMIN(intArrayOf(NPCs.COMMANDER_ZILYANA_6247), "Commander Zilyana"),
    ZAMORAK(intArrayOf(NPCs.KRIL_TSUTSAROTH_6203), "K'ril Tsutsaroth"),
    BANDOS(intArrayOf(NPCs.GENERAL_GRAARDOR_6260), "General Graardor"),
    ARMADYL(intArrayOf(NPCs.KREEARRA_6222), "Kree'arra"),
    JAD(intArrayOf(NPCs.TZTOK_JAD_2745), "Tz-Tok Jad"),
    KALPHITE_QUEEN(intArrayOf(NPCs.KALPHITE_QUEEN_1160), "Kalphite Queen"),
    CORPOREAL_BEAST(intArrayOf(NPCs.CORPOREAL_BEAST_8133), "Corporeal Beast"),
    TORMENTED_DEMONS(
        intArrayOf(
            NPCs.TORMENTED_DEMON_8349, NPCs.TORMENTED_DEMON_8350, NPCs.TORMENTED_DEMON_8351,
            NPCs.TORMENTED_DEMON_8352, NPCs.TORMENTED_DEMON_8353, NPCs.TORMENTED_DEMON_8354,
            NPCs.TORMENTED_DEMON_8355, NPCs.TORMENTED_DEMON_8356, NPCs.TORMENTED_DEMON_8357,
            NPCs.TORMENTED_DEMON_8358, NPCs.TORMENTED_DEMON_8359, NPCs.TORMENTED_DEMON_8360,
            NPCs.TORMENTED_DEMON_8361, NPCs.TORMENTED_DEMON_8362, NPCs.TORMENTED_DEMON_8363,
            NPCs.TORMENTED_DEMON_8364, NPCs.TORMENTED_DEMON_8365, NPCs.TORMENTED_DEMON_8366),
        "Tormented demon"
    ),
    PENANCE_QUEEN(intArrayOf(NPCs.PENANCE_QUEEN_5247), "Penance Queen");

    companion object {
        private fun forBossId(npc: Int): BossKillCounter? {
            for (kc in values()) {
                for (i in kc.bossId) {
                    if (npc == i) {
                        return kc
                    }
                }
            }
            return null
        }

        @JvmStatic
        fun addToBossKillCount(killer: Player?, bossId: Int) {
            if (killer == null) {
                return
            }
            val boss: BossKillCounter = forBossId(bossId) ?: return
            killer.getSavedData().globalData.bossCounters[boss.ordinal]++
            sendMessage(killer, "Your ${boss.bossName} killcount is now: <col=ff0000>${killer.getSavedData().globalData.bossCounters[boss.ordinal]}</col>.")
        }

        fun addToBarrowsChestCount(player: Player?) {
            if (player == null) {
                return
            }
            player.getSavedData().globalData.barrowsLoots++
            sendMessage(player, "Your Barrows chest count is: <col=ff0000>${player.getSavedData().globalData.barrowsLoots}</col>.")
        }
    }
}
