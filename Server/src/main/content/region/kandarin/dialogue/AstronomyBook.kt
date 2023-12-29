package content.region.kandarin.dialogue

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.ServerConstants
import core.api.setAttribute
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items

@Initializable
class AstronomyBook : InteractionListener {
    companion object {
        private val TITLE = "The tales of Scorpius"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("A History of Astronomy", 55),
                    BookLine("in ${ServerConstants.SERVER_NAME}.", 56),
                    BookLine("", 57),
                    BookLine("At the start of the 4th age,", 58),
                    BookLine("a learned man by the", 59),
                    BookLine("name of Scorpius, known well", 60),
                    BookLine("for his powers of vision and", 61),
                    BookLine("magic, sought communion with", 62),
                    BookLine("the gods of the world.", 63),
                    BookLine("So many unanswered questions", 64),
                    BookLine("had he that devoted his entire", 65),
                ),
                Page(
                    BookLine("life to this cause.", 66),
                    BookLine("After many years of study,", 67),
                    BookLine("seeking knowledge from the", 68),
                    BookLine("wise of that time, he developed", 69),
                    BookLine("a machine infused with", 70),
                    BookLine("magical power, infused with", 71),
                    BookLine("the ability to pierce", 72),
                    BookLine("into the heavens - a huge eye", 73),
                    BookLine("that gave the user", 74),
                    BookLine("incredible site, like never", 75),
                    BookLine("seen before.", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("As time passed, Scorpius", 55),
                    BookLine("grew adept at using his", 56),
                    BookLine("specialized skills, and followed", 57),
                    BookLine("the movements of stars", 58),
                    BookLine("in ${ServerConstants.SERVER_NAME}, which are", 59),
                    BookLine("mapped and named, and still", 60),
                    BookLine("used to this day.", 61),
                    BookLine("", 62),
                    BookLine("Before long, Scorpius", 63),
                    BookLine("used his knowledge", 64),
                    BookLine("for predicting the future,", 65),
                ),
                Page(
                    BookLine("and, in turn, he called upon", 66),
                    BookLine("the dark knowledge of", 67),
                    BookLine("Zamorakian worshipers", 68),
                    BookLine("to further his cause. Living", 69),
                    BookLine("underground, the followers", 70),
                    BookLine("of the dark god remained", 71),
                    BookLine("until the civilization of", 72),
                    BookLine("Ardougne grew in strength", 73),
                    BookLine("and control.", 74),
                )
            ),
            PageSet(
                Page(
                    BookLine("The kings of that time", 55),
                    BookLine("worked to banish the", 56),
                    BookLine("Zamorakian followers in", 57),
                    BookLine("the area, hiding all", 58),
                    BookLine("reference to Scorpius's", 59),
                    BookLine("invention, due to", 60),
                    BookLine("its 'evil nature'.", 61),
                    BookLine("", 62),
                    BookLine("Years later, when the", 63),
                    BookLine("minds of the kings lent", 64),
                    BookLine("more towards the research", 65),
                ),
                Page(
                    BookLine("of the unknown, the plans", 66),
                    BookLine("of Scorpius were uncovered", 67),
                    BookLine("and the heavenly eye", 68),
                    BookLine("constructed again. Since then,", 69),
                    BookLine("many have studied the ways of", 70),
                    BookLine("the astronomer, Scorpius", 71),
                    BookLine("and in his memory a grave", 72),
                    BookLine("was constructed near the", 73),
                    BookLine("Observatory. Some claim his", 74),
                    BookLine("ghost still wanders nearby,", 75),
                    BookLine("in torment as he seeks", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("the secrets of the heavens", 55),
                    BookLine("that can never be solved.", 56),
                    BookLine("Tales tell that he will", 57),
                    BookLine("grant those adept in the", 58),
                    BookLine("arts of the astronomer a", 59),
                    BookLine("blessing of unusual power.", 60),
                    BookLine("", 61),
                    BookLine("Here ends the tale of", 62),
                    BookLine("how astronomy entered", 63),
                    BookLine("the known world.", 64),
                )
            ),
        )
    }

    private fun display(player: Player, pageNum: Int, buttonID: Int): Boolean {
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
        return true
    }

    override fun defineListeners() {
        on(Items.ASTRONOMY_BOOK_600, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
