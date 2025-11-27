package content.region.kandarin.quest.observatoryquest

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.world.map.Location

class ObservatoryQuestInterfaces : InterfaceListener {

    companion object {

        val buttonToNameMapping = mapOf(
            19 to "Aquarius",
            20 to "Aries",
            21 to "Cancer",
            22 to "Capricorn",
            23 to "Gemini",
            24 to "Leo",
            25 to "Libra",
            26 to "Pisces",
            27 to "Sagittarius",
            28 to "Scorpio",
            29 to "Taurus",
            30 to "Virgo",
        )

        val buttonToStarObjectMapping = mapOf(
            19 to 27064,
            20 to 27066,
            21 to 27067,
            22 to 27061,
            23 to 27068,
            24 to 27058,
            25 to 27057,
            26 to 27062,
            27 to 27056,
            28 to 27055,
            29 to 27059,
            30 to 27060,
        )
    }

    override fun defineInterfaceListeners() {
        on(ObservatoryQuest.starChartsInterface) { player, component, opcode, buttonID, slot, itemID ->
            if(buttonToStarObjectMapping.contains(buttonID)){
                // 55   57
                player.packetDispatch.sendModelOnInterface(buttonToStarObjectMapping[buttonID]!!, ObservatoryQuest.starChartsInterface, 55, 0)
                setInterfaceText(player, buttonToNameMapping[buttonID]!!, ObservatoryQuest.starChartsInterface, 57)
            }
            return@on true
        }
        on(ObservatoryQuest.dungeonWarning) { player, _, _, buttonID, _, _ ->
            when (buttonID) {
                17 -> teleport(player, Location(2355, 9394)).also { closeInterface(player) }
                18 -> closeInterface(player)
            }
            return@on true
        }
    }
}