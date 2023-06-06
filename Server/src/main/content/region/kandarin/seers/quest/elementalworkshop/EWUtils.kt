package content.region.kandarin.seers.quest.elementalworkshop

import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.node.entity.player.Player
import org.rs09.consts.Vars
import core.api.*

/**
 * Utils for the Elemental Workshop I quest
 * Book/Varp/Method Utils
 *
 *  @author Woah, with love
 */
object EWUtils {
    /**
     * Represents the array of pages for both the BATTERED_BOOK and SLASHED_BOOK.
     */
    val PAGES = arrayOf(
            PageSet(
                    Page(
                            BookLine("Within the pages of this", 55),
                            BookLine("book you will find the", 56),
                            BookLine("secret to working the", 57),
                            BookLine("very elements themselves.", 58),
                            BookLine("Early in the fifth age, a", 59),
                            BookLine("new ore was discovered.", 60),
                            BookLine("This ore has a unique", 61),
                            BookLine("property of absorbing,", 62),
                            BookLine("transforming or focusing", 63),
                            BookLine("elemental energy. A", 64),
                            BookLine("workshop was erected", 65),
                    ),
                    Page(
                            BookLine("close by to work this new", 66),
                            BookLine("material. The workshop", 67),
                            BookLine("was set up for artisans", 68),
                            BookLine("and inventors to be able", 69),
                            BookLine("to come and create", 70),
                            BookLine("devices made from the", 71),
                            BookLine("unique ore, found only in", 72),
                            BookLine("the village of the Seers.", 73)
                    )
            ),
            PageSet(
                    Page(
                            BookLine("After some time of", 55),
                            BookLine("successful industry the", 56),
                            BookLine("true power of this ore", 57),
                            BookLine("became apparent, as", 58),
                            BookLine("greater and more", 59),
                            BookLine("powerful weapons were", 60),
                            BookLine("created. Realising the", 61),
                            BookLine("threat this posed, the magi", 62),
                            BookLine("of the time closed down", 63),
                            BookLine("the workshop and bound", 64),
                            BookLine("it under lock and key,", 65)
                    ),
                    Page(
                            BookLine("also trying to destroy all", 66),
                            BookLine("knowledge of ", 67),
                            BookLine("manufacturing processes.", 68),
                            BookLine("Yet this book remains and", 69),
                            BookLine("you may still find a way", 70),
                            BookLine("to enter the workshop", 71),
                            BookLine("within this leather bound", 72),
                            BookLine("volume.", 73),
                    )
            )
    )

    /* OFFSETS */
    const val LEFT_WATER_CONTROL_STATE = 2058
    const val RIGHT_WATER_CONTROL_STATE = 2059
    const val WATER_WHEEL_STATE = 2060
    const val FURNACE_STATE = 2062
    const val BELLOWS_STATE = 2063

    fun leftWaterControlBit(player: Player): Int {
        return getVarbit(player, LEFT_WATER_CONTROL_STATE)
    }

    fun rightWaterControlBit(player: Player): Int {
        return getVarbit(player, RIGHT_WATER_CONTROL_STATE)
    }

    fun leftWaterControlEnabled(player: Player): Boolean {
        return getVarbit(player, LEFT_WATER_CONTROL_STATE) == 1
    }

    fun rightWaterControlEnabled(player: Player): Boolean {
        return getVarbit(player, RIGHT_WATER_CONTROL_STATE) == 1
    }

    fun waterWheelEnabled(player: Player): Boolean {
        return getVarbit(player, WATER_WHEEL_STATE) == 1
    }

    fun bellowsEnabled(player: Player): Boolean {
        return getVarbit(player, BELLOWS_STATE) == 1
    }

    fun currentStage(player: Player): Int {
        return player.questRepository.getStage("Elemental Workshop I")
    }
}
