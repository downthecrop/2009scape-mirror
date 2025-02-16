package content.region.desert.quest.deserttreasure

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs09.consts.Items

class TranslationBook : InteractionListener {

    companion object {
        private val TITLE = "Translation Primer"
        private val CONTENTS = arrayOf(
                PageSet(
                        Page(
                                BookLine("This is a rough", 55),
                                BookLine("translation of the stone", 56),
                                BookLine("tablet brought to me by", 57),
                                BookLine("courier earlier today.", 58),
                                BookLine("", 59),
                                BookLine("", 60),
                                BookLine("The cuneiforms of this", 61),
                                BookLine("particular tablet are far", 62),
                                BookLine("different to anything I", 63),
                                BookLine("have previously seen in", 64),
                                BookLine("my career as an", 65)
                        ),
                        Page(
                                BookLine("archaeological expert.", 66),
                                BookLine("", 67),
                                BookLine("Where possible I have", 68),
                                BookLine("given as accurate a", 69),
                                BookLine("translation as possible, but", 70),
                                BookLine("some of the words I have", 71),
                                BookLine("attempted to translate", 72),
                                BookLine("hold different meanings", 73),
                                BookLine("depending upon their", 74),
                                BookLine("intonation and context;", 75),
                                BookLine("Due to my unfamiliarity", 76)
                        )
                ),
                PageSet(
                        Page(
                                BookLine("with this language, I have", 55),
                                BookLine("given possible translations", 56),
                                BookLine("for these words wherever", 57),
                                BookLine("I have encountered them.", 58),
                                BookLine("Wherever I have a word", 59),
                                BookLine("n brackets, it is a word", 60),
                                BookLine("which has many meanings", 61),
                                BookLine("depending on the context,", 62),
                                BookLine("although the general", 63),
                                BookLine("meaning should be clear", 64),
                                BookLine("to even a casual study.", 65)
                        ),
                        Page(
                                BookLine("", 66),
                                BookLine("", 67),
                                BookLine("Hopefully this translation", 68),
                                BookLine("will help you in your", 69),
                                BookLine("excavations Asgarnia, and", 70),
                                BookLine("as usual I look forward", 71),
                                BookLine("to seeing what relics you", 72),
                                BookLine("bring back to the", 73),
                                BookLine("Museum of Varrock this", 74),
                                BookLine("time!", 75),
                                BookLine("Your friend, as always,", 76)
                        ),
                ),
                PageSet(
                        Page(
                                BookLine("Terry Balando", 55),
                                BookLine("", 56),
                                BookLine("", 57),
                                BookLine("", 58),
                                BookLine("", 59),
                                BookLine("", 60),
                                BookLine("", 61),
                                BookLine("", 62),
                                BookLine("", 63),
                                BookLine("", 64),
                                BookLine("", 65)
                        ),
                        Page(
                                BookLine("Translation follows:", 66),
                                BookLine("", 67),
                                BookLine("", 68),
                                BookLine("(There are some missing", 69),
                                BookLine("sentence fragments here,", 70),
                                BookLine("presumably from a stone", 71),
                                BookLine("tablet preceding this one", 72),
                                BookLine("which you have not yet", 73),
                                BookLine("discovered)", 74),
                                BookLine("", 75),
                                BookLine("...the permanent", 76)
                        )
                ),
                PageSet(
                        Page(
                                BookLine("(exile/journey) of the", 55),
                                BookLine("people ended.", 56),
                                BookLine("And so it came to pass,", 57),
                                BookLine("that deep in the", 58),
                                BookLine("(fiery/uncomfortable)", 59),
                                BookLine("desert, the gods", 60),
                                BookLine("(argued/decided) amongst", 61),
                                BookLine("themselves that the", 62),
                                BookLine("(fortress/home) would be", 63),
                                BookLine("the (selected/chosen) place", 64),
                                BookLine("that would", 65)
                        ),
                        Page(
                                BookLine("(imprison/conceal) the", 66),
                                BookLine("(wealth/power).", 67),
                                BookLine("Thus (guarded/protected)", 68),
                                BookLine("by the (unusually archaic", 69),
                                BookLine("word here, I believe it", 70),
                                BookLine("means either the sick or", 71),
                                BookLine("the dead depending on", 72),
                                BookLine("context) and", 73),
                                BookLine("(defended/trapped) by the", 74),
                                BookLine("(diamonds/crystals) of", 75),
                                BookLine("(this word is", 76)
                        )
                ),
                PageSet(
                        Page(
                                BookLine("untranslatable). So it was", 55),
                                BookLine("that the gods left the four", 56),
                                BookLine("(diamonds/crystals) as the", 57),
                                BookLine("(key/secret).", 58),
                                BookLine("(Guarded/Protected) by", 59),
                                BookLine("the (again, this word has", 60),
                                BookLine("no modern equivalent)", 61),
                                BookLine("and held by the", 62),
                                BookLine("(worthy/strong) so that", 63),
                                BookLine("the (wealth/power) might", 64),
                                BookLine("forever be", 65)
                        ),
                        Page(
                                BookLine("(imprisoned/concealed).", 66),
                                BookLine("", 67),
                                BookLine("", 68),
                                BookLine("", 69),
                                BookLine("", 70),
                                BookLine("", 71),
                                BookLine("", 72),
                                BookLine("", 73),
                                BookLine("", 74),
                                BookLine("", 75),
                                BookLine("", 76)
                        )
                ),
                PageSet(
                        Page(
                                BookLine("There seems to be some", 55),
                                BookLine("further missing", 56),
                                BookLine("information continued", 57),
                                BookLine("onto a further tablet, but", 58),
                                BookLine("from this preliminary", 59),
                                BookLine("translation I think you", 60),
                                BookLine("may be onto something", 61),
                                BookLine("very big indeed!", 62),
                                BookLine("", 63),
                                BookLine("", 64),
                                BookLine("", 65)
                        ),
                ),
        )
        fun display(player: Player, pageNum: Int, buttonID: Int) : Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
            return true
        }
    }

    override fun defineListeners() {
        on(Items.TRANSLATION_4655, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}