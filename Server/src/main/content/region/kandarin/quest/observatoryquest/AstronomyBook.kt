package content.region.kandarin.quest.observatoryquest

import content.global.handlers.iface.*
import core.ServerConstants
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs09.consts.Items

/**
 * Astronomy Book
 */
class AstronomyBook  : InteractionListener {
    companion object {
        private const val TITLE = "THE TALE OF SCORPIUS"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("THE TALE OF", 55),
                    BookLine("SCORPIUS: ", 56),
                    BookLine("", 57),
                    BookLine("A History of Astronomy", 58),
                    BookLine("in ${ServerConstants.SERVER_NAME}.", 59),
                    BookLine("", 60),
                    BookLine("At the start of the 4th", 61),
                    BookLine("Age, a learned man by", 62),
                    BookLine("the name of Scorpius,", 63),
                    BookLine("known well for his powers", 64),
                    BookLine("of vision and magic,", 65),
                ),
                Page(
                    BookLine("sought communion with", 66),
                    BookLine("the gods of the world. So", 67),
                    BookLine("many unanswered", 68),
                    BookLine("questions had he that he", 69),
                    BookLine("devoted his entire lif to", 70),
                    BookLine("the cause.", 71),
                    BookLine("", 72),
                    BookLine("After many years of", 73),
                    BookLine("study, seeking knowledge", 74),
                    BookLine("from the wise of that", 75),
                    BookLine("time, he developed a", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("machine infused with", 55),
                    BookLine("magical power, infused", 56),
                    BookLine("with the ability to pierce", 57),
                    BookLine("into the very heavens - a", 58),
                    BookLine("huge eye that gave the", 59),
                    BookLine("user incredible sight, like", 60),
                    BookLine("never before.", 61),
                    BookLine("", 62),
                    BookLine("", 63),
                    BookLine("", 64),
                    BookLine("", 65),
                ),
                Page(
                    BookLine("As time passed, Scorpius", 66),
                    BookLine("grew adept at using his", 67),
                    BookLine("specialized skills, and", 68),
                    BookLine("followed the movements of", 69),
                    BookLine("the stars in ${ServerConstants.SERVER_NAME}", 70),
                    BookLine("which he mapped and", 71),
                    BookLine("named, and are still used", 72),
                    BookLine("to this very day.", 73),
                    BookLine("", 74),
                    BookLine("Before long, Scorpius", 75),
                    BookLine("used his knowledge for", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("predicting the future,", 55),
                    BookLine("and, in turn, he called", 56),
                    BookLine("upon the dark knowledge", 57),
                    BookLine("of Zamorakian", 58),
                    BookLine("worshippers to further his", 59),
                    BookLine("cause. Living below", 60),
                    BookLine("ground, the followers of", 61),
                    BookLine("the dark god remained", 62),
                    BookLine("until the civilisation of", 63),
                    BookLine("Ardougne grew in", 64),
                    BookLine("strength and control.", 65),
                ),
                Page(
                    BookLine("", 66),
                    BookLine("", 67),
                    BookLine("", 68),
                    BookLine("", 69),
                    BookLine("", 70),
                    BookLine("", 71),
                    BookLine("The kings of that time", 72),
                    BookLine("worked to banish the", 73),
                    BookLine("Zamorakian followers in", 74),
                    BookLine("the area, hiding all", 75),
                    BookLine("references to Scorpius's", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("invention, due to its 'evil", 55),
                    BookLine("nature'.", 56),
                    BookLine("", 57),
                    BookLine("Years after, when the", 58),
                    BookLine("minds of the kings lent", 59),
                    BookLine("more towards the", 60),
                    BookLine("research of the unknown,", 61),
                    BookLine("the plans of Scorpius", 62),
                    BookLine("were uncovered and the", 63),
                    BookLine("heavenly eye constructed", 64),
                    BookLine("again.", 65),
                ),
                Page(
                    BookLine("Since then, many have", 66),
                    BookLine("studied the ways of the", 67),
                    BookLine("astronomer, Scorpius, and", 68),
                    BookLine("in his memory a grave", 69),
                    BookLine("was constructed near the", 70),
                    BookLine("Observatory.", 71),
                    BookLine("", 72),
                    BookLine("", 73),
                    BookLine("", 74),
                    BookLine("", 75),
                    BookLine("", 76),
                )
            ),

            PageSet(
                Page(),
                Page(
                    BookLine("Some claim his ghost still", 66),
                    BookLine("wanders nearby, in", 67),
                    BookLine("torment as he seeks the", 68),
                    BookLine("secrets of the heavens", 69),
                    BookLine("that can never be solved.", 70),
                    BookLine("Tales tell that he will", 71),
                    BookLine("grant those adept in the", 72),
                    BookLine("arts of the astronomer a", 73),
                    BookLine("blessing of unusual", 74),
                    BookLine("power.", 75),
                    BookLine("", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("Here ends the tale of how", 55),
                    BookLine("astronomy entered the", 56),
                    BookLine("known world.", 57),
                ),
                Page()
            ),
        )
        private fun display(player:Player, pageNum: Int, buttonID: Int) : Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
            BookInterface.setModelOnPage(player,2, 27071, BookInterface.FANCY_BOOK_3_49, 33, 34, 1020, 0, 0)
            BookInterface.setModelOnPage(player,4, 27069, BookInterface.FANCY_BOOK_3_49, 17, 18, 1224, 0, 0)
            return true
        }
    }

    override fun defineListeners() {
        on(Items.ASTRONOMY_BOOK_600, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}