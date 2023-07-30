package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs09.consts.Items

/**
 * Grim Diary Book
 * https://www.youtube.com/watch?v=Vhmm_Gg0584
 * @author ovenbreado
 * @author Vexia
 */
class GrimDiary : InteractionListener {
    companion object {
        private const val RED = "<col=8A0808>"
        private const val LEFT_PAGE_CHILD = 97
        private const val RIGHT_PAGE_CHILD = 82
        private const val TITLE = "Diary of Death"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine(
                        "My Diary<br><br>    by Grim<br><br>" + RED + "'''''''''''''''''''''''''''''''<br>" + RED + "''''12th Bennath<br>" + RED + "'''''''''''''''''''''''''''''''<br>I had such a busy day<br>dealing out death today<br>It's not as easy being grim.<br>Realised that Alfonse is<br>such a good servant. He<br><br><br><br><br><br>",
                        LEFT_PAGE_CHILD
                    )
                ),
                Page(
                    BookLine(
                        "seems to have the house<br>in full working order. I<br>shall have to congratulate<br>him tomorrow. Spent<br>some well-deserved time<br>sharpening my scythe -<br>Alfonse kindly reminded<br>me to put the <col=FF0000>sharpener<br></col>back in the <col=FF0000>cabinet</col>. Such<br>a good chap.",
                        RIGHT_PAGE_CHILD
                    )
                )
            ),
            PageSet(
                Page(
                    BookLine(
                        "<br><br>" + RED + "'''''''''''''''''''''''''''''''<br>" + RED + "'''13th Bennath<br>" + RED + "'''''''''''''''''''''''''''''''<br></col>Went to the Wilderness<br>today. Plenty of foolish<br>people surrendering their<br>lives to me without<br>thought. My back is<br>killing me from all the<br>standing around waiting.",
                        LEFT_PAGE_CHILD
                    )
                ),
                Page(
                    BookLine(
                        "Must get that seen to.<br>Got my <col=FF0000>robes</col> stained<br>from one victim. Simply<br>ruined! I decided to<br>throw them in the<br><col=FF0000>fireplace</col> - will light the<br>fire soon. Oh, and I must<br>remember to call mother.",
                        RIGHT_PAGE_CHILD
                    )
                )
            ),
            PageSet(
                Page(
                    BookLine(
                        "<br><br>" + RED + "'''''''''''''''''''''''''''''''<br>" + RED + "'''14th Bennath<br>" + RED + "'''''''''''''''''''''''''''''''<br></col>A tragic day. Alfonse and<br>I were in the garden<br>looking at the state of the<br>spider nest. I patted my<br>trusty servant on the<br>back in thanks for all his<br>hard work. Sadly, my<br>touch of death killed him",
                        LEFT_PAGE_CHILD
                    )
                ), Page(BookLine("instantly. Feel quite guilty.", RIGHT_PAGE_CHILD))
            ),
            PageSet(
                Page(
                    BookLine(
                        "<br><br>" + RED + "'''''''''''''''''''''''''''''''<br>" + RED + "'''20th Bennath<br>" + RED + "'''''''''''''''''''''''''''''''<br></col>House is getting into<br>quite a state without<br>Alfonse - things strewn all<br>over the place. Almost<br>trod on the eye of my<br>mentor, eww. I put the<br><col=FF0000>eye</col> back on the <col=FF0000>shelf</col> so<br>he can watch over me",
                        LEFT_PAGE_CHILD
                    )
                ),
                Page(
                    BookLine(
                        "and make sure I stay<br>true to his teachings.<br>Went into Varrock to<br>buy some new robes -<br>people kept running away<br>in fear, so it was difficult<br>to find a sale.",
                        RIGHT_PAGE_CHILD
                    )
                )
            ),
            PageSet(
                Page(
                    BookLine(
                        "<br><br>" + RED + "'''''''''''''''''''''''''''''''<br>" + RED + "'''21st Bennath<br>" + RED + "'''''''''''''''''''''''''''''''<br></col>Decided to spend a bit of<br>time tidying today - it<br>really isn't an easy job.<br>In my activities I found<br>my old <col=FF0000> 'Voice of Doom'<br></col>potion on the <col=FF0000>bookcase</col> -<br>perfect for giving people a<br>good scare. Oh, I do love",
                        LEFT_PAGE_CHILD
                    )
                ),
                Page(
                    BookLine(
                        "my job.<br><br><br>" + RED + "'''''''''''''''''''''''''''''''<br>" + RED + "'''22nd Bennath<br>" + RED + "'''''''''''''''''''''''''''''''</col><br>Ordered a new servant<br>today from the agency<br>and got a 10% discount<br>for getting past the<br>1000th servant mark.",
                        RIGHT_PAGE_CHILD
                    )
                )
            ),
            PageSet(
                Page(
                    BookLine(
                        "Woo hoo! The agency<br>sent me his <col=FF0000>Last Will and<br>Testement</col>. Shall have to<br><col=FF0000>sit on that</col> for a while.<br><br>" + RED + "'''''''''''''''''''''''''''''''<br>" + RED + "'''23rd Bennath<br>" + RED + "'''''''''''''''''''''''''''''''<br></col>Aquired some bones<br>today. Muncher should<br>appreciate them as a treat",
                        LEFT_PAGE_CHILD
                    )
                ),
                Page(
                    BookLine(
                        "the next time he behaves.<br>The problem is there<br>aren't many barriers he<br>can't devour, so I decided<br>to lock the <col=FF0000>bones</col> up in<br>the <col=FF0000>chest</col>.<br><br>" + RED + "'''''''''''''''''''''''''''''''" + RED + "<br>" + RED + "''''24th Bennath<br>" + RED + "'''''''''''''''''''''''''''''''<br>My plan to make undead",
                        RIGHT_PAGE_CHILD
                    )
                )
            ),
            PageSet(
                Page(
                    BookLine(
                        "fish is going quite well. I<br>managed to obtain a<br>resurrection <col=FF0000>hourglass</col><br>today,so have added that<br>to the <col=FF0000>fishtank </col>to finish<br>off the process. It's so<br>difficult to have pets when<br>everything you touch dies<br>horrifically. I remember<br>having a rabbit once that<br>exploded when I fed it a",
                        LEFT_PAGE_CHILD
                    )
                ),
                Page(
                    BookLine(
                        "carrot!<br><br>" + RED + "'''''''''''''''''''''''''''''''<br>" + RED + "'''25th Bennath<br>" + RED + "'''''''''''''''''''''''''''''''<br></col>Got back home today to<br>find Muncher has run<br>around the house<br>creating havok - he even<br>ate the postman! I",
                        RIGHT_PAGE_CHILD
                    )
                )
            ),
            PageSet(
                Page(
                    BookLine(
                        "scolded him. So hopefully<br>he won't do it again<br>anytime soon. All my<br>things are in such a<br>mess. I'm surely going to<br>have to find someone to<br>tidy things up before my<br>new servant arrives.<br>Don't want to seem<br>totally incapable of looking<br>after myself.",
                        LEFT_PAGE_CHILD
                    )
                )
            )
        )
        private fun display(player:Player, pageNum: Int, buttonID: Int) : Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_26, TITLE, CONTENTS)
            return true
        }
    }

    override fun defineListeners() {
        on(Items.THE_GRIM_REAPERS_DIARY_11780, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_26, ::display)
            return@on true
        }
    }
}