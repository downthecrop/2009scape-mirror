package content.region.kandarin.seers.quest.elementalworkshop

import core.api.getVarbitValue
import core.game.dialogue.book.BookLine
import core.game.dialogue.book.Page
import core.game.dialogue.book.PageSet
import core.game.node.entity.player.Player
import org.rs09.consts.Vars

/**
 * Utils for the Elemental Workshop I quest
 * Book/Varp/Method Utils
 *
 *  @author Woah, with love
 */
object EWUtils {

    // Helpers for the battered/slashed book
    /**
     * Represents the book id
     */
    const val BATTERED_BOOK_ID = 49610760
    const val SLASHED_BOOK_ID = 49610761

    /**
     * Represents the array of pages for this book.
     */
    val PAGES = arrayOf(
            core.game.dialogue.book.PageSet(
                    core.game.dialogue.book.Page(
                            core.game.dialogue.book.BookLine("Within the pages of this", 55),
                            core.game.dialogue.book.BookLine("book you will find the", 56),
                            core.game.dialogue.book.BookLine("secret to working the", 57),
                            core.game.dialogue.book.BookLine("very elements themselves.", 58),
                            core.game.dialogue.book.BookLine("Early in the fifth age, a", 59),
                            core.game.dialogue.book.BookLine("new ore was discovered.", 60),
                            core.game.dialogue.book.BookLine("This ore has a unique", 61),
                            core.game.dialogue.book.BookLine("property of absorbing,", 62),
                            core.game.dialogue.book.BookLine("transforming or focusing", 63),
                            core.game.dialogue.book.BookLine("elemental energy. A", 64),
                            core.game.dialogue.book.BookLine("workshop was erected", 65),
                    ),
                    core.game.dialogue.book.Page(
                            core.game.dialogue.book.BookLine("close by to work this new", 66),
                            core.game.dialogue.book.BookLine("material. The workshop", 67),
                            core.game.dialogue.book.BookLine("was set up for artisans", 68),
                            core.game.dialogue.book.BookLine("and inventors to be able", 69),
                            core.game.dialogue.book.BookLine("to come and create", 70),
                            core.game.dialogue.book.BookLine("devices made from the", 71),
                            core.game.dialogue.book.BookLine("unique ore, found only in", 72),
                            core.game.dialogue.book.BookLine("the village of the Seers.", 73)
                    )
            ),
            core.game.dialogue.book.PageSet(
                    core.game.dialogue.book.Page(
                            core.game.dialogue.book.BookLine("After some time of", 55),
                            core.game.dialogue.book.BookLine("successful industry the", 56),
                            core.game.dialogue.book.BookLine("true power of this ore", 57),
                            core.game.dialogue.book.BookLine("became apparent, as", 58),
                            core.game.dialogue.book.BookLine("greater and more", 59),
                            core.game.dialogue.book.BookLine("powerful weapons were", 60),
                            core.game.dialogue.book.BookLine("created. Realising the", 61),
                            core.game.dialogue.book.BookLine("threat this posed, the magi", 62),
                            core.game.dialogue.book.BookLine("of the time closed down", 63),
                            core.game.dialogue.book.BookLine("the workshop and bound", 64),
                            core.game.dialogue.book.BookLine("it under lock and key,", 65)
                    ),
                    core.game.dialogue.book.Page(
                            core.game.dialogue.book.BookLine("also trying to destroy all", 66),
                            core.game.dialogue.book.BookLine("knowledge of ", 67),
                            core.game.dialogue.book.BookLine("manufacturing processes.", 68),
                            core.game.dialogue.book.BookLine("Yet this book remains and", 69),
                            core.game.dialogue.book.BookLine("you may still find a way", 70),
                            core.game.dialogue.book.BookLine("to enter the workshop", 71),
                            core.game.dialogue.book.BookLine("within this leather bound", 72),
                            core.game.dialogue.book.BookLine("volume.", 73),
                    )
            )
    )

    /* OFFSETS */
    const val LEFT_WATER_CONTROL_STATE = 3
    const val RIGHT_WATER_CONTROL_STATE = 4
    const val WATER_WHEEL_STATE = 5
    const val FURNACE_STATE = 7
    const val BELLOWS_STATE = 9

    fun leftWaterControlBit(player: Player): Int {
        return getVarbitValue(player, Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, LEFT_WATER_CONTROL_STATE)
    }

    fun rightWaterControlBit(player: Player): Int {
        return getVarbitValue(player, Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, RIGHT_WATER_CONTROL_STATE)
    }

    fun leftWaterControlEnabled(player: Player): Boolean {
        return getVarbitValue(player, Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, LEFT_WATER_CONTROL_STATE) == 1
    }

    fun rightWaterControlEnabled(player: Player): Boolean {
        return getVarbitValue(player, Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, RIGHT_WATER_CONTROL_STATE) == 1
    }

    fun waterWheelEnabled(player: Player): Boolean {
        return getVarbitValue(player, Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, WATER_WHEEL_STATE) == 1
    }

    fun bellowsEnabled(player: Player): Boolean {
        return getVarbitValue(player, Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, BELLOWS_STATE) == 1
    }

    fun currentStage(player: Player): Int {
        return player.questRepository.getStage("Elemental Workshop I")
    }
}