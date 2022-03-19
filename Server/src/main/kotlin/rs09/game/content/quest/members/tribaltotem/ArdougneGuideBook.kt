package rs09.game.content.quest.members.tribaltotem

import core.game.content.dialogue.book.BookLine
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.book.Book
import core.game.content.dialogue.book.Page
import core.game.content.dialogue.book.PageSet
import core.game.node.entity.player.Player
import core.plugin.Initializable

/**
 * This was some code for a post-tutorial RSPS guide book a long ass
 * time ago, but it was using the ID for the guide book for Tribal Totem
 * and I kind of need that, bro
 *
 * @author Splinter
 * @author Phil
 */
@Initializable
class ArdougneGuideBook : Book {
    /**
     * Constructs a new `ShieldofArravBook` `Object`.
     */
    constructor(player: Player?) : super(player, "Ardougne Guide Book", 1856, *PAGES) {}

    /**
     * Constructs a new `ShieldofArravBook` `Object`.
     */
    constructor() {
        /**
         * empty.
         */
    }

    override fun finish() {}
    override fun display(set: Array<Page>) {
        player.lock()
        player.interfaceManager.open(getInterface())
        player.packetDispatch.sendString("Previous", getInterface().id, 77)
        player.packetDispatch.sendString("Next", getInterface().id, 78)
        for (i in 55..76) {
            player.packetDispatch.sendString("", getInterface().id, i)
        }
        player.packetDispatch.sendString(getName(), getInterface().id, 6)
        for (page in set) {
            for (line in page.lines) {
                player.packetDispatch.sendString(line.message, getInterface().id, line.child)
            }
        }
        val lastPage = index == sets.size - 1
        if (lastPage) {
            finish()
        }
        player.unlock()
    }

    override fun newInstance(player: Player): DialoguePlugin {
        return ArdougneGuideBook(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(ID)
    }

    companion object {
        /**
         * Represents the book id
         */
        var ID = 387454

        /**
         * Represents the array of pages for this book.
         */
        private val PAGES = arrayOf(
            PageSet(
                Page(
                    BookLine("Introduction", 57),
                    BookLine("This book is your guide to", 59),
                    BookLine("the vibrant city of Ardougne.", 60),
                    BookLine("Ardougne is known as an", 61),
                    BookLine("exciting modern city located", 62),
                    BookLine("on the sun drenched southern", 63),
                    BookLine("coast of Kandarin", 64),
                ),
                Page(
                    BookLine("Ardougne: City of Shopping!", 66),
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
                    BookLine("Ardougne: City of Fun!", 55),
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
                    BookLine("There is something truly",66),
                    BookLine("for everybody here!",67)
                )
            ),
            PageSet(
                Page(
                    BookLine("Ardougne: City of History!", 55),
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
                    BookLine("Order of Paladins still wander",66),
                    BookLine("the streets of Ardougne, and",67),
                    BookLine("are often of interest to",68),
                    BookLine("tourists.",69)
                )
            ),
            PageSet(
                Page(
                    BookLine("Further Fields", 55),
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
    }
}