package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.setAttribute
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import org.rs09.consts.Items

/**
 * Security Book
 * https://www.youtube.com/watch?v=yXyRUXYafO0
 * @author ovenbreado
 */
class SecurityBookPlugin : InteractionListener {
    companion object {
        private const val BLUE = "<col=08088A>"
        private val TITLE = "" + GameWorld.settings!!.name + " Account Security"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("Chapters", 38),
                    BookLine("<h1>" + BLUE + "Password Tips</col></h1>", 40),
                    BookLine("<h1>" + BLUE + "Recovery Questions</col></h1>", 41),
                    BookLine("<h1>" + BLUE + "Other Security Tips</col></h1>", 42),
                    BookLine("<h1>" + BLUE + "Stringhold of Security</col></h1>", 43)
                ),
                Page(
                    BookLine("<h1>" + BLUE + "Password Tips</col></h1>", 53),
                    BookLine("A good password should be", 54),
                    BookLine("easily remembered by", 55),
                    BookLine("yourself but not easily", 56),
                    BookLine("guessed by anyone else.", 57),
                    BookLine("Choose a password that has", 59),
                    BookLine("both letters and numbers in", 60),
                    BookLine("it for the best security but", 61),
                    BookLine("don't make it so hard that", 62),
                    BookLine("you'll forget it!", 63),
                    BookLine("Never write your password", 65),
                    BookLine("down or leave it in a text file", 66),
                    BookLine("on your computer, someone", 67)
                )
            ),
            PageSet(
                Page(
                    BookLine("could find it easily!", 38),
                    BookLine("Never tell anyone your", 40),
                    BookLine("password in " + GameWorld.settings!!.name + ", not", 41),
                    BookLine("even a Moderator of any", 42),
                    BookLine("kind.", 43)
                ),
                Page(
                    BookLine("<h1>" + BLUE + "Recovery Questions</col></h1>", 53),
                    BookLine("Ideally your recovery", 54),
                    BookLine("questions should be easily", 55),
                    BookLine("remembered by you but not", 56),
                    BookLine("guessable by anyone who", 57),
                    BookLine("may know you or given", 58),
                    BookLine("away in conversation. Choose", 59),
                    BookLine("things that do not change,", 60),
                    BookLine("like dates or names but don't", 61),
                    BookLine("choose obvious ones like your", 62),
                    BookLine("birthday and your sister or", 63),
                    BookLine("bother's name because lots", 64),
                    BookLine("of people will know that.", 65)
                )
            ),
            PageSet(
                Page(
                    BookLine("<h1>" + BLUE + "Recovery Questions</col></h1>", 38),
                    BookLine("Bear in mind that recovery", 39),
                    BookLine("questions will take 14 days to", 40),
                    BookLine("become active after you have", 41),
                    BookLine("applied for them to be", 42),
                    BookLine("changed. This is to protect", 43),
                    BookLine("your account from hijackers", 44),
                    BookLine("who may change them.", 45),
                    BookLine("Never give your password to", 47),
                    BookLine("ANYONE. This includes", 48),
                    BookLine("your friends, family, and", 49),
                    BookLine("moderators in game.", 50),
                    BookLine("Never leave your account", 52)
                ),
                Page(
                    BookLine("logged on if you are away", 53),
                    BookLine("from the computer, it only", 54),
                    BookLine("takes 5 seconds to steal your", 55),
                    BookLine("account!", 56)
                )
            ),
            PageSet(
                Page(
                    BookLine("<h1>" + BLUE + "Stronghold of Security</col></h1>", 38),
                    BookLine("Location: The Stronghold of", 39),
                    BookLine("Security, as we call it, is", 40),
                    BookLine("located under the village filled", 41),
                    BookLine("with Barbarians. It was", 42),
                    BookLine("found after they moved their", 43),
                    BookLine("mining operations and a", 44),
                    BookLine("miner fell through. The", 45),
                    BookLine("Stronghold contains many", 46),
                    BookLine("challenges. Both for those", 47),
                    BookLine("who enjoy combat and those", 48),
                    BookLine("who enjoy challenges of the", 49),
                    BookLine("mind. This book will be very", 50),
                    BookLine("useful to you in your travels", 51),
                    BookLine("there.", 52)
                ),
                Page(
                    BookLine("You can find the Stronghold", 53),
                    BookLine("of Security by looking for a", 54),
                    BookLine("hole in Barbarian Village.", 55),
                    BookLine("Be sure to take your combat", 56),
                    BookLine("equipment though!", 57)
                )
            )
        )
        private fun display(player: Player, pageNum: Int, buttonID: Int) : Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_2_27, TITLE, CONTENTS)
            return true
        }
    }

    override fun defineListeners() {
        on(Items.SECURITY_BOOK_9003, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_2_27, ::display)
            return@on true
        }
    }
}