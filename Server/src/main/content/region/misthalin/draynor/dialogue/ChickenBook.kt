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

class ChickenBook : InteractionListener {

    // Book about the Evil Chicken found on the bookshelves in the Wise Old Man's house in Draynor Village.

    companion object {
        private val TITLE = "The Origins of the Bird of Evil"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("Many are the rumours", 55),
                    BookLine("and tales surrounding", 56),
                    BookLine("this foul beast.", 57),
                    BookLine("", 58),
                    BookLine("The earliest of these tales", 59),
                    BookLine("seems to date back to", 60),
                    BookLine("the time of the Mahjarrat.", 61),
                    BookLine("It is a story about a mad", 62),
                    BookLine("mage, who attempts to summon", 63),
                    BookLine("a demon and bind it to his", 64),
                    BookLine("will. Unfortunately his", 65),
                ),
                Page(
                    BookLine("spell failed and all that", 66),
                    BookLine("appeared was one confused", 67),
                    BookLine("chicken. In a fit of anger", 68),
                    BookLine("at his failure the mage", 69),
                    BookLine("banished the chicken to the", 70),
                    BookLine("Abyss. The chicken however", 71),
                    BookLine("appears to have survived", 72),
                    BookLine("and grown in power. Years", 73),
                    BookLine("later, when the mage cast", 74),
                    BookLine("another spell of summoning,", 75),
                    BookLine("the chicken appeared!", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("The story does not tell", 55),
                    BookLine("of what became of the mage.", 56),
                    BookLine("", 57),
                    BookLine("Another popular tale", 58),
                    BookLine("tells of a chicken of", 59),
                    BookLine("higher than average", 60),
                    BookLine("mental ability. Realising", 61),
                    BookLine("that it and its kind were", 62),
                    BookLine("prisoners in their coops", 63),
                    BookLine("he organised a rebellion", 64),
                    BookLine("against the human farmers.", 65),
                ),
                Page(
                    BookLine("The farmers, of course,", 66),
                    BookLine("simply slaughtered the", 67),
                    BookLine("rebellious chickens.", 68),
                    BookLine("However, ", 70),
                    BookLine("the Evil Chicken escaped.", 71),
                    BookLine("Blaming its brethren for", 72),
                    BookLine("the coup's failure it", 73),
                    BookLine("swore revenge on", 74),
                    BookLine("all chickens and all humans.", 75),
                )
            ),
            PageSet(
                Page(
                    BookLine("The Chicken can strike", 55),
                    BookLine("anybody, anywhere. It", 56),
                    BookLine("is said that a coven ", 57),
                    BookLine("worships this fell fowl and", 58),
                    BookLine("have even built a shrine to it.", 59),
                    BookLine("The exact location of this", 60),
                    BookLine("shrine is unknown, but is", 61),
                    BookLine("rumoured to be in the", 62),
                    BookLine("fairyworlds. Further", 63),
                    BookLine("rumour suggests that", 64),
                    BookLine("his lair is guarded by", 65),
                ),
                Page(
                    BookLine("some fearsome beasts, but", 66),
                    BookLine("it may be reached by", 67),
                    BookLine("making a tasty offering...", 68),
                    BookLine("whatever that may be.", 69),
                ),
            )
        )
    }

    private fun display(player: Player, pageNum: Int, buttonID: Int): Boolean {
        BookInterface.pageSetup(
            player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS
        )
        return true
    }

    override fun defineListeners() {
        on(Items.BOOK_ON_CHICKENS_7464, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}