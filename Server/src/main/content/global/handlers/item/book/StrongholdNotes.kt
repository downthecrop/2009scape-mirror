package content.global.handlers.item.book

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
 * Stronghold Notes Book
 * https://www.youtube.com/watch?v=gX1jCcfY0l8
 * @author ovenbreado
 */
class StrongholdNotes : InteractionListener {
    companion object {
        private const val BLUE = "<col=08088A>"
        private const val TITLE = "Stronghold of Security - Notes"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine(
                        "Chapters<br><br>" + BLUE + "Description<br>" + BLUE + "Level 1<br>" + BLUE + "Level 2<br>" + BLUE + "Level 3<br>" + BLUE + "Level 4<br>" + BLUE + "Navigation<br>" + BLUE + "Diary<br><br><br><br><br><br><br><br><br><br>",
                        38
                    )
                ), Page(
                    BookLine(
                        BLUE + "Description<br>This stronghold was<br>unearthed by a miner<br>prospecting for new ores<br>around the Barbarian Village.<br>After gathering some<br>equipment he ventured into<br>the maze of tunnels and was<br>missing for a long time. He<br>finally emerged along with<br>copious notes refarding the<br>new beasts and strange<br>experiences which had befallen<br>him. He also mentioned that<br>there was treasure to be had,",
                        53
                    )
                )
            ),
            PageSet(
                Page(
                    BookLine(
                        "but no one has been able to<br>wring a word from him about<br>this, he simply flapped his<br>arms and slapped his head.<br>This book details his notes<br>and my diary of exploration.<br>I am exploring to see if I<br>can find out more...",
                        38
                    )
                ), Page(
                    BookLine(
                        BLUE + "Level 1<br>As well as goblins, creatures<br>like a man but also like a cow<br>infest this place! I have never<br>seen anything like this before.<br>The area itself is reminiscent<br>of frontline castles, with<br>many walls, doors and<br>skeletons of dead enemies.<br>I'm sure I hear voices in my<br>head each time I pass<br>through the gates. I have<br>dubbed this level War as it<br>seems like an eternal<br>battleground. I found only",
                        53
                    )
                )
            ),
            PageSet(
                Page(BookLine("one small peaceful area here.", 38)),
                Page(
                    BookLine(
                        BLUE + "Level 2<br>My supplies are running low<br>and I find myself in barren<br>passages with seemingly<br>endless malnourished beasts<br>attacking me, revenous for<br>food. Nothing appears to be<br>able to grow, many<br>adventurers have died<br>through lack of food and the<br>very air appears to suck<br>vitality from me. I've come to<br>call this place famine.",
                        53
                    )
                )
            ),
            PageSet(
                Page(
                    BookLine(
                        BLUE + "Level 3<br>Just breathing in this place<br>makes me shudder at the<br>thought of what foul disease I<br>may contract. The walls and<br>floor ooze and pulsate like<br>something pox ridden. There<br>is a very strange beast whom<br>I narrowly escaped from. At<br>first I thought it to be a<br>cross between a cow and a<br>sheep, something<br>domesticated... but when it<br>looked up at me I was<br>overcome with weakness and",
                        38
                    )
                ),
                Page(
                    BookLine(
                        "barely got way with my life!<br>Luckily I found a small place<br>where I could heal myself<br>and rest a while. I have<br>named this area pestilence for<br>it reeks with decay.",
                        53
                    )
                )
            ),
            PageSet(
                Page(
                    BookLine(
                        BLUE + "Level 4<br>On my first escapade into<br>this place I was utterly<br>shocked. The adventurers<br>who had come before me<br>must have made up a tiny<br>proportion of the skeletons of<br>the dead. Nothing truely<br>alive exists here, even those<br>beings who do wander the<br>halls are not alive as such,<br>but they do know that I am<br>and I get the distinct<br>impression that were they to<br>have their way, I would not",
                        38
                    )
                ),
                Page(
                    BookLine(
                        "be for long! Death is<br>everywhere and thus I shall<br>name this place. There is one<br>small place of life, which was<br>gladdening to find and very<br>worth my while!",
                        53
                    )
                )
            ),
            PageSet(
                Page(
                    BookLine(
                        BLUE + "Navigation<br>After getting lost several<br>times I finally worked out the<br>key to all the ladders and<br>chains around this death<br>infested place. All ropes and<br>chains will take you to the<br>start of the level that you are<br>on. However most ladders will<br>simply take you to the level<br>above. The one esception is<br>the ladder in the bottom level<br>treasure room, which appears<br>to lead through several<br>extremely twisty passages.",
                        38
                    )
                ),
                Page(
                    BookLine(
                        "and eventually takes you out<br>of the dungeon completely.<br>The portals may be used if<br>you are of sufficient level or<br>have already claimed your<br>reward from the treasure<br>room.",
                        38
                    )
                )
            ),
            PageSet(
                Page(
                    BookLine(
                        BLUE + "Diary<br>Day 1<br>Today I set out to find out<br>more about this place. From<br>my research I knew about<br>the sentient doors, imbued by<br>some unkown force to talk<br>to you and ask questions<br>before they will let you pass.<br>I have so far passed these<br>doors without incident, giving<br>the correct answer seems to<br>work a treat.<br><br>Day 2",
                        38
                    )
                ),
                Page(
                    BookLine(
                        "I have fought my way<br>through the fearsome beasts<br>on the first level and am<br>preparing myself to journey<br>deeper. I hope that things are<br>not too difficult further on as<br>I am already sick of bread<br>and cheese for dinner.<br><br>Day 3<br>I ventured down into the<br>famine level today... I was<br>wounded and have returned<br>to the relative safety of the<br>level above. I am going to",
                        53
                    )
                )
            ),
            PageSet(
                Page(
                    BookLine(
                        "try to make my way out<br>through the goblins and<br>mancow things... I hope I<br>make it.....",
                        38
                    )
                )
            )
        )

        private fun display(player: Player, pageNum: Int, buttonID: Int) : Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_2_27, TITLE, CONTENTS)
            return true
        }
    }

    override fun defineListeners() {
        on(Items.STRONGHOLD_NOTES_9004, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_2_27, ::display)
            return@on true
        }
    }
}