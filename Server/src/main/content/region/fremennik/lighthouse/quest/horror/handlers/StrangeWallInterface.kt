package content.region.fremennik.lighthouse.quest.horror.handlers

import content.data.Quests
import content.region.fremennik.lighthouse.quest.horror.HorrorFromTheDeep
import core.api.*
import core.game.interaction.InterfaceListener
import org.rs09.consts.Components

class StrangeWallInterface : InterfaceListener {

    override fun defineInterfaceListeners() {
        onOpen(Components.HORROR_METALDOOR_142) { player, _ ->
            val questComplete = isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP)

            val runeAttributes = listOf<Array<Any>>(
                //      varbit                             child  default  filled
                arrayOf(HorrorFromTheDeep.HFTD_WALL_FIRE,  2,     4513,    4514),
                arrayOf(HorrorFromTheDeep.HFTD_WALL_AIR,   3,     4509,    4510),
                arrayOf(HorrorFromTheDeep.HFTD_WALL_EARTH, 4,     4511,    4512),
                arrayOf(HorrorFromTheDeep.HFTD_WALL_WATER, 5,     4520,    4521),
                arrayOf(HorrorFromTheDeep.HFTD_WALL_ARROW, 6,     4518,    4519),
                arrayOf(HorrorFromTheDeep.HFTD_WALL_SWORD, 7,     4516,    4517)
            )

            runeAttributes.forEach { data ->
                val save = data[0] as Int
                val child = data[1] as Int
                val default = data[2] as Int
                val filled = data[3] as Int

                val placed = (getVarbit(player, save) == 1) || questComplete
                val show = if (placed) filled else default
                player.packetDispatch.sendModelOnInterface(show, 142, child, 0)
            }

            return@onOpen true
        }
    }
}