package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import org.rs09.consts.Items

/**
 * General Rule Book
 * This is not an item book, but opened when asking a Town Crier for the Rules
 * @author ovenbreado
 * @author 'Vexia
 */
class GeneralRuleBook {
    companion object {
        private const val BLUE = "<col=08088A>"
        private val SERVER_NAME = GameWorld.settings!!.name
        private val TITLE = "$SERVER_NAME Rules"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("Rules", 38),
                    BookLine("1 - Offensive Language", 104),
                    BookLine("2 - Item Scamming", 106),
                    BookLine("3 - Password Scamming", 108),
                    BookLine("4 - Cheating/Bug Abuse", 110),
                    BookLine("5 - Staff Impersonation", 112),
                    BookLine("6 - Account Sharing/Trading", 114),
                    BookLine("7 - Using Third Party", 116),
                    BookLine("Software", 118),
                    BookLine("8 - Multi Logging-in", 120),
                    BookLine("9 - Encouraging Others to", 122),
                    BookLine("Break Rules", 124),
                    BookLine("10 - False Representation", 126),
                    BookLine("11 - Website Advertising", 128)
                ),
                Page(
                    BookLine("12 - Read World Item", 134),
                    BookLine("Trading", 136),
                    BookLine("13 - Asking For Personal", 138),
                    BookLine("Details", 140),
                    BookLine("14 - Misuse of Official", 142),
                    BookLine("Forums", 144),
                    BookLine("15 - Advert Blocking", 146)
                )
            ),
            PageSet(
                Page(
                    BookLine(BLUE + "1 - Offensive Language", 38),
                    BookLine("You must not use any", 40),
                    BookLine("language which is offensive,", 41),
                    BookLine("racist or obscene. Remember,", 42),
                    BookLine("it's nice to be nice!", 43)
                ), Page(
                    BookLine(BLUE + "2 - Item Scamming", 53 ),
                    BookLine("You must not scam or", 55),
                    BookLine("deceive other players. This", 56),
                    BookLine("includes claiming that items", 57),
                    BookLine("are rare when they are not,", 58),
                    BookLine("team scamming or telling", 59),
                    BookLine("players that you can", 60),
                    BookLine("upgrade' their armour in", 61),
                    BookLine("any way!", 62)
                )
            ),
            PageSet(
                Page(
                    BookLine(BLUE + "3 - Password Scamming", 38),
                    BookLine("Asking for - or trying to", 40),
                    BookLine("obtain - another player's", 41),
                    BookLine("password in any way will not", 42),
                    BookLine("be tolerated, even in jest!", 43)
                ), Page(
                    BookLine(BLUE + "4 - Cheating/Bug Abuse", 53),
                    BookLine("A bug is a technical glitch", 55),
                    BookLine("found in the game. You", 56),
                    BookLine("must not use or attempt to", 57),
                    BookLine("use any cheats or errors that", 58),
                    BookLine("you find in our software.", 59),
                    BookLine("Any bugs found must be", 60),
                    BookLine("reported to " + SERVER_NAME, 61),
                    BookLine("immediately, by clicking the", 62),
                    BookLine("'Report a bug/fault' link", 63),
                    BookLine("found on the main page.", 64)
                )
            ),
            PageSet(
                Page(
                    BookLine(BLUE + "5 - Staff Impersonation", 38),
                    BookLine("You should not attempt to", 40),
                    BookLine("impersonate " + SERVER_NAME + " staff in", 41),
                    BookLine("any way.", 42)
                ), Page(
                    BookLine(BLUE + "6 - Account Sharing/Trading", 53),
                    BookLine("Each account should only be", 55),
                    BookLine("used by ONE person and", 56),
                    BookLine("ONE person alone.", 57),
                    BookLine("Remember that trying to", 58),
                    BookLine("buy, sell, borrow or give", 59),
                    BookLine("away an account is against", 60),
                    BookLine("the rules and will land you in", 61),
                    BookLine("trouble when caught!", 62)
                )
            ),
            PageSet(
                Page(
                    BookLine(
                        BLUE + "7 - Using Third Party <br>" + BLUE + "Software<br><br>You must not use other<br>programs to gain an unfair<br>advantage in the game.",
                        38
                    )
                ), Page(
                    BookLine(
                        BLUE + "8 - Multi Logging-in</col><br><br>If you create more than one<br>" + SERVER_NAME + " account, they<br>must not interact. This<br>includes giving items to a<br>friend to transfer to another<br>account you own.",
                        53
                    )
                )
            ),
            PageSet(
                Page(
                    BookLine(
                        BLUE + "9 - Encouraging Others to<br>" + BLUE + "Break Rules</col><br><br>You must not encourage<br>others to break any of the<br>" + SERVER_NAME + " rules.",
                        38
                    ), BookLine(
                        BLUE + "10 - False Representation</col><br><br>You must not misuse<br>" + SERVER_NAME + " Customer<br>Support.Trying to get<br>another player into trouble<br>by reporting them for no<br>reason or framing them is an<br>abuse of the Customer<br>Support Team's service.<br> want to help as many<br>players as possible and so the<br>Customer Support Team's<br>service must be used<br>appropriately and treated with",
                        53
                    )
                )
            ),
            PageSet(
                Page(
                    BookLine("respect at all times.", 38),
                    BookLine(
                        BLUE + "11 - Website Advertising</col><br><br>You are not allowed to<br>actively advertise any<br>websites, fan-sites, IRC<br>channels or product<br>anywhere in " + SERVER_NAME + ",<br>including the " + SERVER_NAME + "<br>forums.",
                        53
                    )
                ), Page()
            ),
            PageSet(
                Page(
                    BookLine(
                        BLUE + "12 - Real World Item<br>" + BLUE + "Trading<br><br></col>" + SERVER_NAME + " items must only<br>be exchanged for other items<br>or service within the game.<br>Buying or selling items<br>outside of the " + SERVER_NAME + "<br>environment is not in the<br>spirit of the game and is easy<br>for " + SERVER_NAME + " to trace!",
                        38
                    ), BookLine(
                        BLUE + "13 - Asking for Personal<br>" + BLUE + "Details<br><br></col>To protect player's safety<br>and privacy, you must not<br>ask for personal details. This<br>includes full names, phone<br>numbers, MSN, AIM, email<br>and home/school addresses!",
                        53
                    )
                )
            ),
            PageSet(
                Page(
                    BookLine(
                        BLUE + "14 - Misuse of Official<br>" + BLUE + "Forums</col><br><br>Forums must be used in<br>accordance with the Forum<br>Code of Conduct and treated<br>with respect at all times.",
                        38
                    ), BookLine(
                        BLUE + "15 - Advert Blocking<br><br>Blocking the adverts in the<br>free to play version of<br>" + SERVER_NAME + " is against the<br>rules.",
                        53
                    )
                )
            )
        )
        private fun display(player: Player, pageNum: Int, buttonID: Int) : Boolean {
            if (pageNum == 0) {
                when (buttonID) {
                    104 -> setAttribute(player, "bookInterfaceCurrentPage", 1)
                    106 -> setAttribute(player, "bookInterfaceCurrentPage", 1)
                    108 -> setAttribute(player, "bookInterfaceCurrentPage", 2)
                    110 -> setAttribute(player, "bookInterfaceCurrentPage", 2)
                    112 -> setAttribute(player, "bookInterfaceCurrentPage", 3)
                    114 -> setAttribute(player, "bookInterfaceCurrentPage", 3)
                    116 -> setAttribute(player, "bookInterfaceCurrentPage", 4)
                    118 -> setAttribute(player, "bookInterfaceCurrentPage", 4)
                    120 -> setAttribute(player, "bookInterfaceCurrentPage", 4)
                    122 -> setAttribute(player, "bookInterfaceCurrentPage", 5)
                    124 -> setAttribute(player, "bookInterfaceCurrentPage", 5)
                    126 -> setAttribute(player, "bookInterfaceCurrentPage", 5)
                    128 -> setAttribute(player, "bookInterfaceCurrentPage", 6)
                    134 -> setAttribute(player, "bookInterfaceCurrentPage", 7)
                    136 -> setAttribute(player, "bookInterfaceCurrentPage", 7)
                    138 -> setAttribute(player, "bookInterfaceCurrentPage", 7)
                    140 -> setAttribute(player, "bookInterfaceCurrentPage", 7)
                    142 -> setAttribute(player, "bookInterfaceCurrentPage", 8)
                    144 -> setAttribute(player, "bookInterfaceCurrentPage", 8)
                    146 -> setAttribute(player, "bookInterfaceCurrentPage", 8)
                }
            } else {
                when (buttonID) {
                    159 -> setAttribute(player, "bookInterfaceCurrentPage", 0)
                }
            }
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_2_27, TITLE, CONTENTS)
            if (getAttribute(player,"bookInterfaceCurrentPage", 0) != 0) {
                player.packetDispatch.sendInterfaceConfig(BookInterface.FANCY_BOOK_2_27, 159, false)
                player.packetDispatch.sendString("Index", BookInterface.FANCY_BOOK_2_27, 159)
            }
            return true
        }

        /** Since the Town Crier shows you the book, there is no item here. */
        fun openBook(player: Player) {
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_2_27, ::display)
        }
    }
}