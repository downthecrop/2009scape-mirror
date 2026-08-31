package content.region.fremennik.lighthouse.quest.horror.handlers.bookcase

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.storeBookInHouse
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs09.consts.Items

/**
 * Ancient diary located at Lighthouse.
 */

class AncientDiary : InteractionListener {
    companion object {
        private val TITLE = "Diary"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    // pages 1-2
                    Page(
                        BookLine("I do not know why it is", 55),
                        BookLine("me that has been chosen", 56),
                        BookLine("by them... but I will not", 57),
                        BookLine("fail them!", 58),
                        BookLine("Soft whispers, beckoning", 60),
                        BookLine("me at night... I know", 61),
                        BookLine("what you are!", 62),
                    ),
                    Page(
                        BookLine("I saw one today.", 68),
                        BookLine("Magnificence. Is this", 71),
                        BookLine("then, what we all aspire to", 72),
                        BookLine("someday become? Are", 73),
                        BookLine("these beings even natural,", 74),
                        BookLine("or are they beyond what", 75),
                    ),
                ),
                // pages 3-4
                PageSet(
                    Page( // lines 55-65
                        BookLine("we think of as nature?", 55),
                        BookLine("The beautiful chittering", 58),
                        BookLine("Dagannoth! I know thee!", 61),
                        BookLine("I call thee! For I too am", 62),
                        BookLine("of the ocean, I too know", 63),
                        BookLine("this land as home!", 64),
                    ),
                    Page( // lines 66-76
                        BookLine("Home? Perhaps. Where", 66),
                        BookLine("did you come from?", 67),
                        BookLine("Where do you plan to", 69),
                        BookLine("go?", 69),
                        BookLine("You cannot leave. You", 71),
                        BookLine("MUST not leave. I will", 72),
                        BookLine("ensure it.", 73),
                    ),
                ),
                // pages 5-6
                PageSet(
                    Page(
                        BookLine("I find myself thinking", 56),
                        BookLine("often of the time when", 57),
                        BookLine("we can all be together as", 58),
                        BookLine("one in the deep sea.", 59),
                        BookLine("Problems arose. Can you", 61),
                        BookLine("not see what I want?", 62),
                        BookLine("Can you not see how we", 63),
                        BookLine("should proceed?", 64),
                    ),
                    Page(
                        BookLine("I have no solution.", 70),
                    ),
                ),
                // pages 7-8
                PageSet(
                    Page(
                        BookLine("I sleep during the days", 55),
                        BookLine("now, my nights are only", 56),
                        BookLine("for you and your kind.", 57),
                        BookLine("I am thinking of a door.", 59),
                        BookLine("We cannot let everyone", 62),
                        BookLine("know of my plans!", 63),
                    ),
                    Page(
                        BookLine("I was buying food in the", 66),
                        BookLine("nearby town today, the", 67),
                        BookLine("idiot barbarians look at", 68),
                        BookLine("me as though I have lost", 69),
                        BookLine("my wits.", 70),
                        BookLine("Ha! Like they know what", 75),
                        BookLine("wits even are!", 76),
                    ),
                ),
                // pages 9-10
                PageSet(
                    Page(
                        BookLine("Do they even", 56),
                        BookLine("understand? Of course", 57),
                        BookLine("not they are just", 58),
                        BookLine("barbarians! Soon.", 59),
                        BookLine("Things fall into place so", 64),
                        BookLine("quickly...", 65),
                    ),
                    Page(
                        BookLine("A door. Yes. Definitely a", 66),
                        BookLine("door. But how to keep", 67),
                        BookLine("my secrets secure?", 68),
                        BookLine("Disturbing message from", 70),
                        BookLine("the council today, they", 71),
                        BookLine("feel that I am not", 72),
                        BookLine("fulfilling my contract to", 73),
                        BookLine("keep this lighthouse", 74),
                        BookLine("operative correctly.", 75),
                    ),
                ),
                // pages 11-12
                PageSet(
                    // childs 55-65
                    Page(
                        BookLine("Do they think I care?", 58),
                        BookLine("Do I even care", 60),
                        BookLine("anymore?", 61),
                        BookLine("Things proceed apace.", 63),
                        BookLine("I will begin work on the", 64),
                    ),
                    // childs 66-76
                    Page(
                        BookLine("door as soon as I can.", 66),
                        BookLine("What can they do to me?", 69),
                        BookLine("Your songs of the night", 72),
                        BookLine("call to me, always.", 73),
                    ),
                ),
                // pages 13-14
                PageSet(
                    Page(
                        BookLine("Always the chittering.", 57),
                        BookLine("Did I name you,", 59),
                        BookLine("Dagannoth? Or did you", 60),
                        BookLine("tell me your name in my", 61),
                        BookLine("dreams of you?", 62),
                        BookLine("If I named you, did I also", 63),
                        BookLine("create you just by", 64),
                        BookLine("naming you? Am I then", 65),
                    ),
                    Page(
                        BookLine("also somehow a part of", 66),
                        BookLine("you, or are you a part of", 67),
                        BookLine("me?", 68),
                        BookLine("Things seem so strange", 70),
                        BookLine("nowadays...", 71),
                        BookLine("The door is the key. But", 73),
                        BookLine("what should then be the", 74),
                        BookLine("key to the door?", 75),
                    ),
                ),
                // pages 15-16
                PageSet(
                    Page(
                        BookLine("I do not think I am well", 55),
                        BookLine("anymore, it is good to", 56),
                        BookLine("have company.", 57),
                        BookLine("Stupid council! Always", 59),
                        BookLine("interfering! You do not", 60),
                        BookLine("know the dagannoth, you", 61),
                        BookLine("have no sway here!", 62),
                        BookLine("I am the sea.", 64),
                    ),
                    Page(
                        BookLine("I see that I am.", 66),
                        BookLine("The door. Dagannoth.", 68),
                        BookLine("Will I wake one day and", 69),
                        BookLine("find that my life so far", 70),
                        BookLine("has been naught but a", 71),
                        BookLine("dream, and that under", 72),
                        BookLine("my skin, under my", 73),
                        BookLine("bones, I too am", 74),
                        BookLine("dagannoth?", 75),
                    ),
                ),
                // pages 17-18
                PageSet(
                    Page(
                        BookLine("Such dreams I have in", 56),
                        BookLine("the days while I sleep.", 57),
                    ),
                    Page(
                        BookLine("The chittering is now", 65),
                        BookLine("always ringing in my", 66),
                        BookLine("ears... what are you", 67),
                        BookLine("saying to me??", 68),
                        BookLine("The key IS the door!", 70),
                        BookLine("The red of flame, the", 71),
                        BookLine("blue of water. The white", 72),
                        BookLine("of air and the brown of", 73),
                        BookLine("earth.", 74),
                    ),
                ),
                // pages 19-20
                PageSet(

                    Page(
                        BookLine("Is this all? Can this be?", 58),
                        BookLine("No, dagannoth, it isn't.", 62),
                    ),
                    Page(
                        BookLine("The colours are of deep", 66),
                        BookLine("significance, deep seas,", 67),
                        BookLine("deep thoughts, thoughts of", 68),
                        BookLine("creation, am I a creation,", 69),
                        BookLine("dagannoth? Are you?", 70),
                        BookLine("Then who by?", 71),
                    ),
                ),
                // pages 21-22
                PageSet(
                    Page(
                        BookLine("And what for?", 59),
                    ),
                    Page(
                        BookLine("The door is the key. The", 66),
                        BookLine("key to the door is key.", 67),
                        BookLine("Red and yellow and blue", 68),
                        BookLine("and brown and white and", 69),
                        BookLine("green, put them together", 70),
                        BookLine("and what have you got?", 71),
                        BookLine("The runes. Fire, air,", 72),
                        BookLine("water, earth. The other", 73),
                        BookLine("keys. Weapons.", 74),
                        BookLine("I take two other parts of", 76),
                    ),
                ),
                // pages 23-24
                PageSet(
                    Page(
                        BookLine("the mixture; the sword of", 56),
                        BookLine("the warrior and the", 57),
                        BookLine("arrow of the huntsman.", 58),
                        BookLine("Can this then keep you", 59),
                        BookLine("bound to this place,", 60),
                        BookLine("dagannoth?", 61),
                        BookLine("We shall see.", 63),
                        BookLine("We shall see the sea.", 65),
                    ),
                    Page(
                        BookLine("Days are a stream of", 66),
                        BookLine("people, buying, selling can", 67),
                        BookLine("you not leave me be?", 68),
                        BookLine("I think I understand.", 70),
                        BookLine("I come to thee tonight,", 72),
                        BookLine("dagannoth. I know not", 73),
                        BookLine("how this ends, but will", 74),
                        BookLine("now found out. Farewell", 75),
                        BookLine("land, the sea is now my", 76),
                    ),
                ),
                // pages 25-26
                PageSet(
                    Page(
                        BookLine("home, my place, my life.", 55),
                    ),
                    Page(
                        BookLine("I have not much time left", 66),
                        BookLine("in this place.", 67),
                        BookLine("I return to you in the sea", 69),
                        BookLine("at night.", 70),
                        BookLine("Dagannoth. Me. Can you", 73),
                    ),
                ),
                // pages 27-28
                PageSet(
                    Page(
                        BookLine("become me?", 56),
                        BookLine("Or...", 58),
                        BookLine("Can I somehow... Become", 60),
                        BookLine("you?", 61),
                    ),
                ),
            )
        private fun display(player:Player, pageNum: Int, buttonID: Int) : Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
            return true
        }
    }

    override fun defineListeners() {
        on(Items.DIARY_3846, IntType.ITEM, "read") { player, node ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            storeBookInHouse(player, node)
            return@on true
        }
    }
}
