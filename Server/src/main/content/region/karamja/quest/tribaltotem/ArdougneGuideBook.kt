package content.region.karamja.quest.tribaltotem

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.setAttribute
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs09.consts.Items

/**
 * Ardougne Guide Book
 * https://www.youtube.com/watch?v=-Qburz3T00o
 * @author ovenbreado
 * @author Splinter
 * @author Phil
 */
class ArdougneGuideBook : InteractionListener {
    companion object {
        private val TITLE = "Tourist Guide to Ardougne"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("<col=ff0000>Introduction", 57),
                    BookLine("This book is your guide to", 59),
                    BookLine("the vibrant city of Ardougne.", 60),
                    BookLine("Ardougne is known as an", 61),
                    BookLine("exciting modern city located", 62),
                    BookLine("on the sun drenched southern", 63),
                    BookLine("coast of Kandarin", 64),
                ),
                Page(
                    BookLine("<col=ff0000>Ardougne: City of Shopping!", 66),
                    BookLine("Come sample the delights of", 67),
                    BookLine("the Ardougne market - the", 68),
                    BookLine("biggest in the known world!", 69),
                    BookLine("From spices to silk, there", 70),
                    BookLine("is something here for", 71),
                    BookLine("everybody! Other popular", 72),
                    BookLine("shopping destinations in", 73),
                    BookLine("the area include the Armoury", 74),
                    BookLine("and the ever popular", 75),
                    BookLine("Adventurers' supply store.", 76)
                )
            ),
            PageSet(
                Page(
                    BookLine("<col=ff0000>Ardougne: City of Fun!", 55),
                    BookLine("If you're looking for", 56),
                    BookLine("entertainment in Ardougne,", 57),
                    BookLine("why not pay a visit to the", 58),
                    BookLine("Ardougne City zoo? Or relax", 59),
                    BookLine("with a drink in the ever", 60),
                    BookLine("popular Flying Horse Inn?", 61),
                    BookLine("And for the adventurous,", 62),
                    BookLine("there are always rats to be", 63),
                    BookLine("slaughtered in the expansive", 64),
                    BookLine("and vermin-ridden sewers", 65)
                ),
                Page(
                    BookLine("There is something truly", 66),
                    BookLine("for everybody here!", 67)
                )
            ),
            PageSet(
                Page(
                    BookLine("<col=ff0000>Ardougne: City of History!", 55),
                    BookLine("Ardougne is renowned as an", 56),
                    BookLine("important city of historical", 57),
                    BookLine("interest. One historic building", 58),
                    BookLine("is the magnificent Handelmort", 59),
                    BookLine("Mansion, currently owned by", 60),
                    BookLine("Lord Francis Kurt Handelmort.", 61),
                    BookLine("Also of historical interest is", 62),
                    BookLine("Ardougne Castle in the east of", 63),
                    BookLine("the city recently opened to the", 64),
                    BookLine("public. And of course the Holy", 65)
                ),
                Page(
                    BookLine("Order of Paladins still wander", 66),
                    BookLine("the streets of Ardougne, and", 67),
                    BookLine("are often of interest to", 68),
                    BookLine("tourists.", 69)
                )
            ),
            PageSet(
                Page(
                    BookLine("<col=ff0000>Further Fields", 55),
                    BookLine("", 56),
                    BookLine("The area surrounding Ardougne", 57),
                    BookLine("is also of great interest to", 58),
                    BookLine("the cultural tourist. If you", 59),
                    BookLine("want to go further afield, why", 60),
                    BookLine("not have a look at the ominous", 61),
                    BookLine("Pillars of Zanash, the", 62),
                    BookLine("mysterious marble pillars", 63),
                    BookLine("located just West of the city?", 64),
                    BookLine("Or perhaps the town of Brimhaven,", 65)
                ),
                Page(
                    BookLine("on the exotic island of Karamja?", 66),
                    BookLine("It's only a short boat trip with", 67),
                    BookLine("regular transport leaving from", 68),
                    BookLine("Ardougne harbor at all times", 69),
                    BookLine("of the day, all year round.", 70)
                )
            )
        )

        private fun display(player:Player, pageNum: Int, buttonID: Int) : Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
            return true
        }
    }

    override fun defineListeners() {
        on(Items.GUIDE_BOOK_1856, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}