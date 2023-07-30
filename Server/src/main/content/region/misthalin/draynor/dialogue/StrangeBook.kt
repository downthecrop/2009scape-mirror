package content.region.misthalin.draynor.dialogue

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.setAttribute
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs09.consts.Items

class StrangeBook : InteractionListener {

    // The strange book is a book found on the bookshelves in the Wise Old Man's.
    companion object {
        private val TITLE = "Stange book"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("<col=0000FF>Perception and Reality", 55),
                    BookLine("... which leads us to the", 56),
                    BookLine("new definition of knowledge", 57),
                    BookLine("as a belief that is both", 58),
                    BookLine("reliably generated and true.", 59),
                    BookLine("Our senses tell us about", 61),
                    BookLine("the world, but to what", 62),
                    BookLine("extent are they reliable?", 63),
                    BookLine("One witness may perceive", 64),
                    BookLine("the weather to be hot,", 65),
                ),
                Page(
                    BookLine("but another witness may", 66),
                    BookLine("consider it to be quite", 67),
                    BookLine("cold.", 68),
                    BookLine("Perception of colour may", 70),
                    BookLine("be similarly affected, as", 71),
                    BookLine("the reader may demonstrate", 72),
                    BookLine("by staring at a piece of", 73),
                    BookLine("brightly-coloured paper.", 74),
                    BookLine("When the paper is removed,", 75),
                    BookLine("the reader may find that", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("everything else appears", 55),
                    BookLine("to be a slightly different", 56),
                    BookLine("colour from normal.", 57),
                    BookLine("We may conclude from this", 59),
                    BookLine("that, although we are", 60),
                    BookLine("sensing some intrinsic", 61),
                    BookLine("property of the object", 62),
                    BookLine("under examination,", 63),
                    BookLine("qualities such as colour", 64),
                    BookLine("and temperature are NOT", 65),
                ),
                Page(
                    BookLine("intrinsic.", 66),
                    BookLine("Henceforth we recommend", 68),
                    BookLine("that the reader consider", 69),
                    BookLine("them to be Secondary", 70),
                    BookLine("qualities. The quantity", 71),
                    BookLine("of caloric within the", 72),
                    BookLine("object that gives it this", 73),
                    BookLine("perceived temperature", 74),
                    BookLine("truly is intrinsic to", 75),
                    BookLine("the object, so this may", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("be considered a Primary", 55),
                    BookLine("quality.", 56),
                    BookLine("In the following chapter", 58),
                    BookLine("we will examine the", 59),
                    BookLine("implications of this", 60),
                    BookLine("distinction for the study", 61),
                    BookLine("of natural philosophy.", 62),
                    BookLine("<col=0000FF>Certainty and Assumption", 65),
                ),
                Page(
                    BookLine("In this chapter we aim", 66),
                    BookLine("to examine the extent to", 67),
                    BookLine("which our senses give", 68),
                    BookLine("us a reliable impression", 69),
                    BookLine("of the world in which", 70),
                    BookLine("we live.", 71),
                    BookLine("Although we may believe", 73),
                    BookLine("that we are seeing and", 74),
                    BookLine("hearing things that exist,", 75),
                    BookLine("we may be deceived by", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("our senses, as every", 55),
                    BookLine("keen drinker will have", 56),
                    BookLine("experienced! But if our", 57),
                    BookLine("senses may be deceived,", 58),
                    BookLine("what can we trust?", 59),
                    BookLine("We may perhaps try to", 61),
                    BookLine("rely only on logical", 62),
                    BookLine("deductions and mathematical", 63),
                    BookLine("principles. Yet it is", 64),
                    BookLine("even possible that some", 65),
                ),
                Page(
                    BookLine("fiend may have confused", 66),
                    BookLine("our minds to the extent", 67),
                    BookLine("that we are making false", 68),
                    BookLine("deductions without being", 69),
                    BookLine("aware of this.", 70),
                    BookLine("We therefore present the", 72),
                    BookLine("following statement as", 73),
                    BookLine("the only piece of knowledge", 74),
                    BookLine("about which we cannot", 75),
                    BookLine("possibly be deceived:", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("'I EXIST'", 55),
                    BookLine("Although all information", 57),
                    BookLine("gained through use of our", 58),
                    BookLine("senses and minds may be", 59),
                    BookLine("distorted, no self-aware", 60),
                    BookLine("being can be led to", 61),
                    BookLine("believe that it does not", 62),
                    BookLine("exist. It may not be the", 63),
                    BookLine("man or woman it believes", 64),
                    BookLine("itself to be, but it can", 65),
                ),
                Page(
                    BookLine("never question its own", 66),
                    BookLine("existence in some form.", 67),
                    BookLine("In the following chapter", 69),
                    BookLine("we will discuss ways in", 70),
                    BookLine("which one may attempt to", 71),
                    BookLine("demonstrate the existence", 72),
                    BookLine("of a world outside of one's", 73),
                    BookLine("own consciousness.", 74),
                    BookLine("<col=0000FF>Graphical solution of", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("<col=0000FF>mathematical problems", 55),
                    BookLine("A simple graph containing", 57),
                    BookLine("an x-axis and a y-axis may", 58),
                    BookLine("be used to represent", 59),
                    BookLine("equations similar to the", 60),
                    BookLine("following: x + 3y = 5", 61),
                    BookLine("For every 'x' and 'y' that", 63),
                    BookLine("satisfy the equation", 64),
                    BookLine("(such as x = 2, y = 1 or", 65),
                ),
                Page(
                    BookLine("x = 5, y = 0 for the", 66),
                    BookLine("above example), the points", 67),
                    BookLine("'x,y' may be plotted on", 68),
                    BookLine("the graph, and it will be", 69),
                    BookLine("found that they form a line.", 70),
                    BookLine("If a second equation of", 72),
                    BookLine("this form is plotted on", 73),
                    BookLine("the same graph, the two", 74),
                    BookLine("lines may cross at some", 75),
                    BookLine("point.", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("Any point where the two", 55),
                    BookLine("lines cross will be given", 56),
                    BookLine("by some 'x' and 'y'", 57),
                    BookLine("that fits both equations.", 58),
                    BookLine("Readers are invited to", 60),
                    BookLine("create two such equations", 61),
                    BookLine("and try it for themselves!", 62),
                    BookLine("<col=0000FF>Readers' Comments", 64),
                ),
                Page(
                    BookLine("This page left blank for", 67),
                    BookLine("readers' comments", 68),
                    BookLine("... better now, walls look", 70),
                    BookLine("decent, nice and elegant.", 71),
                    BookLine("Those new lanterns were a", 72),
                    BookLine("rip-off, blasted dwarf", 73),
                    BookLine("craftsmen. Still, not a", 74),
                    BookLine("problem for me!", 75),
                )
            ),
            PageSet(
                Page(
                    BookLine("Even got my old Saradomin", 55),
                    BookLine("armour gilded, beautiful", 56),
                    BookLine("gold edges on it now. No-one", 57),
                    BookLine("else's going to have armour", 58),
                    BookLine("like that, that'll make", 59),
                    BookLine("them think, heh heh heh...", 60),
                    BookLine("Lick of gold-leaf here and", 62),
                    BookLine("there, even my globe looks", 63),
                    BookLine("smarter now, maybe that", 64),
                    BookLine("bath screen was a bit much?", 65),
                ),
                Page(
                    BookLine("No, I saw it, I wanted it,", 66),
                    BookLine("it's mine.", 67),
                    BookLine("Vidi, volui, mihi est!", 68),
                    BookLine("Still don't know what that", 70),
                    BookLine("'thing' is, but it looks nice", 71),
                    BookLine("on my desk. Probably some", 72),
                    BookLine("kind of transport? Must run", 73),
                    BookLine("off hot air or dragon", 74),
                    BookLine("flatulence? Running a", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("bit short of runes now.", 55),
                    BookLine("Those wizards over to", 56),
                    BookLine("the south always seem to", 57),
                    BookLine("have plenty, but will they", 58),
                    BookLine("spare me some? Bah! Not", 59),
                    BookLine("a chance, poxy mage scum!", 61),
                    BookLine("Perhaps another little expedition", 62),
                    BookLine("is needed - they all", 63),
                    BookLine("look pretty weedy over there...", 64),
                )
            ),
        )
    }

    private fun display(player: Player, pageNum: Int, buttonID: Int): Boolean {
        BookInterface.pageSetup(
            player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS
        )
        return true
    }

    override fun defineListeners() {
        on(Items.STRANGE_BOOK_5507, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
