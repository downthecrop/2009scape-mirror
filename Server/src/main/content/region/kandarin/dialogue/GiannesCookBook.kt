package content.region.kandarin.dialogue

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.setAttribute
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items

@Initializable
class GiannesCookBook : InteractionListener {
    companion object {
        private val TITLE = "Gianne's cook book"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("Chocolate Bomb", 55),
                    BookLine("", 56),
                    BookLine("Knead a ball of Gianne", 57),
                    BookLine("dough into a gnomebowl mould.", 58),
                    BookLine("Bake this briefly. Decadently", 59),
                    BookLine("add four bars of chocolate", 60),
                    BookLine("to the bowl and top with", 61),
                    BookLine("one sprig of equa leaves.", 62),
                    BookLine("Bake the bowl in the oven", 63),
                    BookLine("to melt the chocolate.", 64),
                    BookLine("Then mix in two big dollops", 65),
                ),
                Page(
                    BookLine("of cream and finally sprinkle", 66),
                    BookLine("chocolate dust all over.", 67),
                    BookLine("Chocolate is a relatively", 68),
                    BookLine("recent cooking ingredient", 69),
                    BookLine("for gnomes, having been", 70),
                    BookLine("imported from human lands.", 71),
                )
            ),
            PageSet(
                Page(
                    BookLine("Tangled Toad's Legs", 55),
                    BookLine("", 56),
                    BookLine("Shape a portion of fresh Gianne", 57),
                    BookLine("dough into a gnomebowl mould.", 58),
                    BookLine("Bake this until it is", 59),
                    BookLine("slightly springy.", 60),
                    BookLine("Add to the bowl...", 61),
                    BookLine("", 62),
                    BookLine("4 pairs of toad's legs", 63),
                    BookLine("2 portions of cheese", 64),
                    BookLine("2 sprigs of equa leaves", 65),
                ),
                Page(
                    BookLine("2 dashes of gnome spice", 66),
                    BookLine("1 bunch of dwellberries.", 67),
                    BookLine("", 68),
                    BookLine("Bake the dish in the oven", 69),
                    BookLine("once more prior to serving.", 70),
                    BookLine("Tangled Toads Legs was", 71),
                    BookLine("a special dish created", 72),
                    BookLine("by gnome chef Deelie to", 73),
                    BookLine("celebrate the first ", 74),
                    BookLine("Healthorg the Great Day.", 75),
                )
            ),
            PageSet(
                Page(
                    BookLine("Worm Hole", 55),
                    BookLine("", 56),
                    BookLine("Starting with a gnomebowl", 57),
                    BookLine("mould, shape a portion of", 58),
                    BookLine("fresh Gianne dough into a", 59),
                    BookLine("rough bowl. Bake this", 60),
                    BookLine("until it is firm to the", 61),
                    BookLine("touch. Add to the bowl", 62),
                    BookLine("four king worms, two onions", 63),
                    BookLine("and a dash of gnome spices.", 64),
                    BookLine("Bake the bowl in the", 65),
                ),
                Page(
                    BookLine("oven once more.", 66),
                    BookLine("To finish the dish simply", 67),
                    BookLine("add a topping of equa leaves.", 68),
                    BookLine("Worms are specially flavoured", 69),
                    BookLine("by gnomes as they", 70),
                    BookLine("purportedly add virility.", 71),
                )
            ),
            PageSet(
                Page(
                    BookLine("Veg Ball", 55),
                    BookLine("", 56),
                    BookLine("As with all gnomebowl dishes,", 57),
                    BookLine("throw a ball of fresh Gianne", 58),
                    BookLine("dough into a mould. Bake this", 59),
                    BookLine("as usual. Bake this briefly.", 60),
                    BookLine("Add to the bowl two onions,", 61),
                    BookLine("two potatoes and a dash", 62),
                    BookLine("of gnome spices. Bake the bowl", 63),
                    BookLine("in the oven once more.", 64),
                    BookLine("To finish simply top with equa", 65),
                ),
                Page(
                    BookLine("leaves. Vegetable dishes are", 66),
                    BookLine("seen as luxurious food, since", 67),
                    BookLine("for most of gnome history", 68),
                    BookLine("growing vegetables was harder", 69),
                    BookLine("than finding toads and worms.", 70),
                )
            ),
            PageSet(
                Page(
                    BookLine("Worm Crunchies", 55),
                    BookLine("", 56),
                    BookLine("Using a crunchy tray,", 57),
                    BookLine("form a portion of Gianne", 58),
                    BookLine("dough into small evenly", 59),
                    BookLine("sized balls. Heat these briefly", 60),
                    BookLine("in an oven. Mix into the", 61),
                    BookLine("dough balls two king worms,", 62),
                    BookLine("one sprig of equa leaves", 63),
                    BookLine("and a shake of gnome spices.", 64),
                    BookLine("Bake the crunchies for a", 65),
                ),
                Page(
                    BookLine("short time in the oven.", 66),
                    BookLine("Sprinkle generously with", 67),
                    BookLine("gnome spices to finish.", 68),
                    BookLine("Crunchies were invented", 69),
                    BookLine("accidentally by Pukkamay,", 70),
                    BookLine("who was Dellie's assistant", 71),
                    BookLine("before his sacking. He started", 72),
                    BookLine("a successful crunch making", 73),
                    BookLine("business before dying in a", 74),
                    BookLine("bizarre Terrorbird accident.", 75),
                )
            ),
            PageSet(
                Page(
                    BookLine("Choc Chip Crunchies", 55),
                    BookLine("", 56),
                    BookLine("Fill up a crunchy tray with", 57),
                    BookLine("balls of Gianne dough. Heat", 58),
                    BookLine("these briefly in a warm oven.", 59),
                    BookLine("Break up two bars of chocolate", 60),
                    BookLine("and mix these with the balls", 61),
                    BookLine("of dough. Next add a little", 62),
                    BookLine("gnome spice to each ball.", 63),
                    BookLine("Bake the crunchies for a", 64),
                    BookLine("short time in the oven.", 65),
                ),
                Page(
                    BookLine("Top the crunchies with a", 66),
                    BookLine("sprinkling of chocolate", 67),
                    BookLine("dust to finish.", 68),
                    BookLine("(A large chocolate stain", 69),
                    BookLine("covers the rest of the page.)", 70),
                )
            ),
            PageSet(
                Page(
                    BookLine("Spicy Crunchies", 55),
                    BookLine("", 56),
                    BookLine("Using a crunchy tray,", 57),
                    BookLine("form a portion of Gianne", 58),
                    BookLine("dough into small evenly", 59),
                    BookLine("sized balls. Heat these briefly", 60),
                    BookLine("in a warm oven.", 61),
                    BookLine("Add a generous shake", 62),
                    BookLine("of spice and two sprigs of", 63),
                    BookLine("equa leaves to the dough balls.", 64),
                    BookLine("Bake the crunchies for a", 65),
                ),
                Page(
                    BookLine("short time in the oven.", 66),
                    BookLine("Sprinkle a load more gnome", 67),
                    BookLine("spice over the cookies to finish.", 68),
                    BookLine("", 69),
                    BookLine("The special mix of herbs", 70),
                    BookLine("and spices in gnome spice", 71),
                    BookLine("is a closely guarded secret.", 72),
                    BookLine("It is rumoured to contain", 73),
                    BookLine("(the rest is scribbled out)", 74),
                )
            ),
            PageSet(
                Page(
                    BookLine("Toad Crunchies", 55),
                    BookLine("", 56),
                    BookLine("Fill a crunchy tray with", 57),
                    BookLine("Gianne dough as normal.", 58),
                    BookLine("Heat these briefly in a", 59),
                    BookLine("warm oven. Mix into", 60),
                    BookLine("the dough balls two pairs", 61),
                    BookLine("of toad's legs and a", 62),
                    BookLine("shake of gnome spices.", 63),
                    BookLine("Bake the crunchies for a short", 64),
                    BookLine("time in the oven.", 65),
                ),
                Page(
                    BookLine("Finish the crunchies with", 66),
                    BookLine("a sprinkling of equa leaves.", 67),
                    BookLine("", 68),
                    BookLine("When Pukkamay first made", 69),
                    BookLine("toad crunchies, everyone", 70),
                    BookLine("thought he was mad.", 71),
                    BookLine("'Chewy toads, in crunchies?", 72),
                    BookLine("It'll never work' they said", 73),
                    BookLine("- how wrong they were...", 74),
                )
            ),
            PageSet(
                Page(
                    BookLine("Worm Batta", 55),
                    BookLine("", 56),
                    BookLine("First take some fresh", 57),
                    BookLine("Gianne dough and place it", 58),
                    BookLine("in a batta tin. Bake the", 59),
                    BookLine("dough until it is lightly", 60),
                    BookLine("browned. Take one king worm,", 61),
                    BookLine("some gnome spice and", 62),
                    BookLine("a little cheese. Add these", 63),
                    BookLine("to the batta before briefly", 64),
                    BookLine("baking it in the oven once more.", 65),
                ),
                Page(
                    BookLine("Finish the batta with a", 66),
                    BookLine("topping of equa leaves.", 67),
                    BookLine("Battas are usually cooked by", 68),
                    BookLine("gnome mother during the", 69),
                    BookLine("cold winter months.", 70),
                )
            ),
            PageSet(
                Page(
                    BookLine("Toad's Legs Batta", 55),
                    BookLine("", 56),
                    BookLine("Mould some Gianne dough", 57),
                    BookLine("into a batta tin.", 58),
                    BookLine("Bake the tin until it", 59),
                    BookLine("is almost cooked. Next add", 60),
                    BookLine("some prime toad's legs,", 61),
                    BookLine("a sprig of equa leaves", 62),
                    BookLine("and some spice along with", 63),
                    BookLine("some cheese to the batta.", 64),
                    BookLine("Bake the batta in the oven", 65),
                ),
                Page(
                    BookLine("once more and serve hot.", 66),
                    BookLine("Toads legs battas are", 67),
                    BookLine("sometimes called Toad in", 68),
                    BookLine("the Hole. Apparently there", 69),
                    BookLine("is a similar human dish", 70),
                    BookLine("that uses sausages. How odd.", 71),
                )
            ),
            PageSet(
                Page(
                    BookLine("Cheese & Tomato Batta", 55),
                    BookLine("", 56),
                    BookLine("Place some Gianne dough in a", 57),
                    BookLine("batta tin. Bake it as normal.", 58),
                    BookLine("Top the plain batta with equal", 59),
                    BookLine("quantities of cheese and tomato.", 60),
                    BookLine("Place the batta in the oven", 61),
                    BookLine("once more until all the cheese", 62),
                    BookLine("has melted. Finish the dish with", 63),
                    BookLine("a sprinkling of equa leaves.", 64),
                    BookLine("The combination of cheese", 65),
                ),
                Page(
                    BookLine("and tomato was discovered", 66),
                    BookLine("by the explorer Wingstone", 67),
                    BookLine("while was visiting the human", 68),
                    BookLine("lands. Apparently it's used", 69),
                    BookLine("a lot in a strange flat", 70),
                    BookLine("human dish called a pizza.", 71),
                )
            ),
            PageSet(
                Page(
                    BookLine("Fruit Batta", 55),
                    BookLine("", 56),
                    BookLine("Prepare Gianne dough in", 57),
                    BookLine("a batta tin as normal. Bake", 58),
                    BookLine("the dough for a short while.", 59),
                    BookLine("Top the batta with chunks of", 60),
                    BookLine("pineapple, orange and lime.", 61),
                    BookLine("Lay four sprigs of", 62),
                    BookLine("equa leaves on top of the", 63),
                    BookLine("batta before baking it in", 64),
                    BookLine("the oven once more. Finish", 65),
                ),
                Page(
                    BookLine("the batta with a sprinkling", 66),
                    BookLine("of gnome spices. Battas are", 67),
                    BookLine("normally savoury dish,", 68),
                    BookLine("and the fruit batta is definitely", 69),
                    BookLine("an acquired taste.", 70),
                )
            ),
            PageSet(
                Page(
                    BookLine("Vegetable Batta", 55),
                    BookLine("", 56),
                    BookLine("Place some Gianne dough in a", 57),
                    BookLine("batta tin and bake as normal.", 58),
                    BookLine("Add to the plain batta two", 59),
                    BookLine("tomatoes, one onion, one", 60),
                    BookLine("cabbage and some dwellberries.", 61),
                    BookLine("Top the batta with cheese and", 62),
                    BookLine("briefly place it in the oven", 63),
                    BookLine("once more. Finish the dish with", 64),
                    BookLine("with a sprinkling of equa leaves.", 65),
                ),
                Page(
                    BookLine("There's no better batta", 66),
                    BookLine("than a vegetable batta.", 67),
                )
            ),
        )
    }

    private fun display(player: Player, pageNum: Int, buttonID: Int): Boolean {
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
        return true
    }

    override fun defineListeners() {
        on(Items.GIANNES_COOK_BOOK_2167, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}