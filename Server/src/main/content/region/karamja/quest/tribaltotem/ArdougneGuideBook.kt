package content.region.karamja.quest.tribaltotem

import core.game.dialogue.book.BookLine
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.book.Book
import core.game.dialogue.book.Page
import core.game.dialogue.book.PageSet
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
class ArdougneGuideBook : core.game.dialogue.book.Book {
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
    override fun display(set: Array<core.game.dialogue.book.Page>) {
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

    override fun newInstance(player: Player): core.game.dialogue.DialoguePlugin {
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
                core.game.dialogue.book.PageSet(
                        core.game.dialogue.book.Page(
                                core.game.dialogue.book.BookLine("Introduction", 57),
                                core.game.dialogue.book.BookLine("This book is your guide to", 59),
                                core.game.dialogue.book.BookLine("the vibrant city of Ardougne.", 60),
                                core.game.dialogue.book.BookLine("Ardougne is known as an", 61),
                                core.game.dialogue.book.BookLine("exciting modern city located", 62),
                                core.game.dialogue.book.BookLine("on the sun drenched southern", 63),
                                core.game.dialogue.book.BookLine("coast of Kandarin", 64),
                        ),
                        core.game.dialogue.book.Page(
                                core.game.dialogue.book.BookLine("Ardougne: City of Shopping!", 66),
                                core.game.dialogue.book.BookLine("Come sample the delights of", 67),
                                core.game.dialogue.book.BookLine("the Ardougne market - the", 68),
                                core.game.dialogue.book.BookLine("biggest in the known world!", 69),
                                core.game.dialogue.book.BookLine("From spices to silk, there", 70),
                                core.game.dialogue.book.BookLine("is something here for", 71),
                                core.game.dialogue.book.BookLine("everybody! Other popular", 72),
                                core.game.dialogue.book.BookLine("shopping destinations in", 73),
                                core.game.dialogue.book.BookLine("the area include the Armoury", 74),
                                core.game.dialogue.book.BookLine("and the ever popular", 75),
                                core.game.dialogue.book.BookLine("Adventurers' supply store.", 76)
                        )
                ),
                core.game.dialogue.book.PageSet(
                        core.game.dialogue.book.Page(
                                core.game.dialogue.book.BookLine("Ardougne: City of Fun!", 55),
                                core.game.dialogue.book.BookLine("If you're looking for", 56),
                                core.game.dialogue.book.BookLine("entertainment in Ardougne,", 57),
                                core.game.dialogue.book.BookLine("why not pay a visit to the", 58),
                                core.game.dialogue.book.BookLine("Ardougne City zoo? Or relax", 59),
                                core.game.dialogue.book.BookLine("with a drink in the ever", 60),
                                core.game.dialogue.book.BookLine("popular Flying Horse Inn?", 61),
                                core.game.dialogue.book.BookLine("And for the adventurous,", 62),
                                core.game.dialogue.book.BookLine("there are always rats to be", 63),
                                core.game.dialogue.book.BookLine("slaughtered in the expansive", 64),
                                core.game.dialogue.book.BookLine("and vermin-ridden sewers", 65)
                        ),
                        core.game.dialogue.book.Page(
                                core.game.dialogue.book.BookLine("There is something truly", 66),
                                core.game.dialogue.book.BookLine("for everybody here!", 67)
                        )
                ),
                core.game.dialogue.book.PageSet(
                        core.game.dialogue.book.Page(
                                core.game.dialogue.book.BookLine("Ardougne: City of History!", 55),
                                core.game.dialogue.book.BookLine("Ardougne is renowned as an", 56),
                                core.game.dialogue.book.BookLine("important city of historical", 57),
                                core.game.dialogue.book.BookLine("interest. One historic building", 58),
                                core.game.dialogue.book.BookLine("is the magnificent Handelmort", 59),
                                core.game.dialogue.book.BookLine("Mansion, currently owned by", 60),
                                core.game.dialogue.book.BookLine("Lord Francis Kurt Handelmort.", 61),
                                core.game.dialogue.book.BookLine("Also of historical interest is", 62),
                                core.game.dialogue.book.BookLine("Ardougne Castle in the east of", 63),
                                core.game.dialogue.book.BookLine("the city recently opened to the", 64),
                                core.game.dialogue.book.BookLine("public. And of course the Holy", 65)
                        ),
                        core.game.dialogue.book.Page(
                                core.game.dialogue.book.BookLine("Order of Paladins still wander", 66),
                                core.game.dialogue.book.BookLine("the streets of Ardougne, and", 67),
                                core.game.dialogue.book.BookLine("are often of interest to", 68),
                                core.game.dialogue.book.BookLine("tourists.", 69)
                        )
                ),
                core.game.dialogue.book.PageSet(
                        core.game.dialogue.book.Page(
                                core.game.dialogue.book.BookLine("Further Fields", 55),
                                core.game.dialogue.book.BookLine("", 56),
                                core.game.dialogue.book.BookLine("The area surrounding Ardougne", 57),
                                core.game.dialogue.book.BookLine("is also of great interest to", 58),
                                core.game.dialogue.book.BookLine("the cultural tourist. If you", 59),
                                core.game.dialogue.book.BookLine("want to go further afield, why", 60),
                                core.game.dialogue.book.BookLine("not have a look at the ominous", 61),
                                core.game.dialogue.book.BookLine("Pillars of Zanash, the", 62),
                                core.game.dialogue.book.BookLine("mysterious marble pillars", 63),
                                core.game.dialogue.book.BookLine("located just West of the city?", 64),
                                core.game.dialogue.book.BookLine("Or perhaps the town of Brimhaven,", 65)
                        ),
                        core.game.dialogue.book.Page(
                                core.game.dialogue.book.BookLine("on the exotic island of Karamja?", 66),
                                core.game.dialogue.book.BookLine("It's only a short boat trip with", 67),
                                core.game.dialogue.book.BookLine("regular transport leaving from", 68),
                                core.game.dialogue.book.BookLine("Ardougne harbor at all times", 69),
                                core.game.dialogue.book.BookLine("of the day, all year round.", 70)
                        )
                )
        )
    }
}