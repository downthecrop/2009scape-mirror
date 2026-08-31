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
 * Jossik's journal located at the Lighthouse.
 */

class JossikJournal : InteractionListener {
    companion object {
        private val TITLE = "Journal"
        private val CONTENTS =
            arrayOf(
                // pages 1-2
                PageSet(
                    Page(
                        BookLine("Bennath the 3rd", 55),
                        BookLine("The mystery of what has", 56),
                        BookLine("happened to my uncle", 57),
                        BookLine("has still not been", 58),
                        BookLine("revealed,", 59),
                        BookLine("but I must dwell on", 60),
                        BookLine("it no longer. It is", 61),
                        BookLine("slightly unfair that", 62),
                        BookLine("due to his absence the", 63),
                        BookLine("council have forced", 64),
                        BookLine("me to take up his job", 65),
                    ),
                    Page(
                        BookLine("here at the lighthouse,", 66),
                        BookLine("but what can I say to", 67),
                        BookLine("argue against them? They", 68),
                        BookLine("have allowed me to make", 69),
                        BookLine("a small income by selling", 70),
                        BookLine("items without the usual", 71),
                        BookLine("sales tax, but who is", 72),
                        BookLine("ever going to come all", 73),
                        BookLine("the way out to this", 74),
                        BookLine("forsaken spot? Worse,", 75),
                        BookLine("I fear for my life,", 76),
                    ),
                ),
                // pages 3-4
                PageSet(
                    Page(
                        BookLine("being between two barbarian", 55),
                        BookLine("camps!I have not had", 56),
                        BookLine("any particular problems", 57),
                        BookLine("with the locals so far", 58),
                        BookLine("but who knows what can", 59),
                        BookLine("set off a barbarian???", 60),
                        BookLine("Bennath the 15th", 61),
                        BookLine("", 62),
                        BookLine("My fears seem", 63),
                        BookLine("to have been unfounded,", 64),
                        BookLine("the local barbarians,", 65),
                    ),
                    Page(
                        BookLine("who I have learnt prefer", 66),
                        BookLine("the term 'Fremennik',", 67),
                        BookLine("seem to be decent and", 68),
                        BookLine("hard working people", 69),
                        BookLine("after all. They certainly", 70),
                        BookLine("do not live up to the", 71),
                        BookLine("terrible things that", 72),
                        BookLine("I have heard about them", 73),
                        BookLine("before now! I still", 74),
                        BookLine("do not have any clues", 75),
                        BookLine("as to what has happened", 76),
                    ),
                ),
                // pages 5-6
                PageSet(
                    Page(
                        BookLine("to my uncle... Some", 55),
                        BookLine("of the local folks have", 56),
                        BookLine("tried to suggest that", 57),
                        BookLine("he was eaten by a sea", 58),
                        BookLine("monster that they claim", 59),
                        BookLine("lives near here, but", 60),
                        BookLine("I hold no weight to", 61),
                        BookLine("their foolish superstitions!", 62),
                        BookLine("He was always slightly", 63),
                        BookLine("eccentric, and I fear", 64),
                        BookLine("that he was simply overcome", 65),
                    ),
                    Page(
                        BookLine("by the terrible loneliness", 66),
                        BookLine("from living out in this", 67),
                        BookLine("desolate lighthouse,", 68),
                        BookLine("and one night simply", 69),
                        BookLine("gave himself up to the", 70),
                        BookLine("sea...", 71),
                        BookLine("", 72),
                        BookLine("Bennath the 32nd", 73),
                        BookLine("It has now been", 74),
                        BookLine("almost a month since", 75),
                        BookLine("first I was sent to", 76),
                    ),
                ),
                // pages 7-8
                PageSet(
                    Page(
                        BookLine("this forsaken and desolate", 55),
                        BookLine("spot. I still have", 56),
                        BookLine("no clue what happened", 57),
                        BookLine("to my uncle, and frankly,", 58),
                        BookLine("I have almost stopped", 59),
                        BookLine("really caring. All", 60),
                        BookLine("I can think of is somehow", 61),
                        BookLine("getting away from this", 62),
                        BookLine("terrible place... it", 63),
                        BookLine("has been over two weeks", 64),
                        BookLine("now since I last saw", 65),
                    ),
                    Page(
                        BookLine("another human face...", 66),
                        BookLine("I do not know how much", 67),
                        BookLine("longer I can stand the", 68),
                        BookLine("loneliness! I do not", 69),
                        BookLine("know if it is my mind", 70),
                        BookLine("playing tricks on me,", 71),
                        BookLine("but recently I have", 72),
                        BookLine("begun to hear something", 73),
                        BookLine("whispered on the wind...", 74),
                        BookLine("it seems to call my", 75),
                        BookLine("name as I fall asleep", 76),
                    ),
                ),
                // pages 9-10
                PageSet(
                    Page(
                        BookLine("at night. There have", 55),
                        BookLine("also been many strange", 56),
                        BookLine("noises from below the", 57),
                        BookLine("lighthouse, and from", 58),
                        BookLine("behind the strange metal", 59),
                        BookLine("doorway that my uncle", 60),
                        BookLine("had installed in the", 61),
                        BookLine("basement. Perhaps this", 62),
                        BookLine("is how my uncle felt", 63),
                        BookLine("before he... No, I still", 64),
                        BookLine("do not know what has", 65),
                    ),
                    Page(
                        BookLine("happened to him. I am", 66),
                        BookLine("filled with resolve", 67),
                        BookLine("to discover what exactly", 68),
                        BookLine("did happen - if nothing", 69),
                        BookLine("else, it will keep my", 70),
                        BookLine("mind from the voices", 71),
                        BookLine("that call to me nightly.", 72),
                        BookLine("", 73),
                        BookLine("", 74),
                        BookLine("", 75),
                        BookLine("", 76),
                    ),
                ),
                // pages 11-12
                PageSet(
                    Page( // 55-65
                        BookLine("Raktuber 13th", 55),
                        BookLine("", 56),
                        BookLine("It has been some time", 57),
                        BookLine("last I filled out this", 58),
                        BookLine("journal, for my spirits", 59),
                        BookLine("have been lifted greatly in", 60),
                        BookLine("recent times, and it is all", 61),
                        BookLine("down to a single person I", 62),
                        BookLine("encountered while", 63),
                        BookLine("working here!", 64),
                        BookLine("", 65),
                    ),
                    Page( // 66-76
                        BookLine("Her name is Larrissa and", 66),
                        BookLine("she is a local Fremennik", 67),
                        BookLine("girl, who comes here to", 68),
                        BookLine("buy goods not for sale in", 69),
                        BookLine("her home town of", 70),
                        BookLine("Rellekka sometimes.", 71),
                        BookLine("", 72),
                        BookLine("To be honest, I would", 73),
                        BookLine("have tried to strike up a", 74),
                        BookLine("friendship with anyone", 75),
                        BookLine("who came by, for it is a", 76),
                    ),
                ),
                // pages 13-14
                PageSet(
                    Page(
                        BookLine("terribly lonely job", 55),
                        BookLine("working at this light-", 56),
                        BookLine("house, but even if I", 57),
                        BookLine("worked in Varrock I", 58),
                        BookLine("could not have chosen a", 59),
                        BookLine("more pleasant companion!", 60),
                        BookLine("We really struck a chord", 61),
                        BookLine("together, and I think", 62),
                        BookLine("we may become more than", 63),
                        BookLine("friends eventually, but I", 64),
                        BookLine("worry what her family", 65),
                    ),
                    Page(
                        BookLine("will think of me - from", 66),
                        BookLine("what I hear the people of", 67),
                        BookLine("Rellekka do not take", 68),
                        BookLine("kindly to strangers! I", 69),
                        BookLine("have also made some", 70),
                        BookLine("progress in my search", 71),
                        BookLine("for the truth of what", 72),
                        BookLine("happened to my uncle", 73),
                        BookLine("Silas all those months ago!", 74),
                        BookLine("I found a secret", 75),
                        BookLine("compartment with a diary", 76),
                    ),
                ),
                // pages 15-16
                PageSet(
                    Page(
                        BookLine("hidden within it. I have", 55),
                        BookLine("placed the diary in the", 56),
                        BookLine("bookshelf for later", 57),
                        BookLine("examination. It is", 58),
                        BookLine("definitely his hand writing,", 59),
                        BookLine("and I hope that it may", 60),
                        BookLine("shed some light onto his", 61),
                        BookLine("eventual fate. ", 62),
                        BookLine("", 63),
                        BookLine("I cannot help but think", 64),
                        BookLine("that had he never come", 65),
                    ),
                    Page(
                        BookLine("to this isolated place, that", 66),
                        BookLine("whatever befell him might", 67),
                        BookLine("have never happened...", 68),
                        BookLine("", 69),
                        BookLine("", 70),
                    ),
                ),
                // pages 17-18
                PageSet(
                    Page( // 55-65
                        BookLine("Pentember 24th", 55),
                        BookLine("", 56),
                        BookLine("Has it been so long since", 57),
                        BookLine("last I wrote in this thing?", 58),
                        BookLine("These last months have", 59),
                        BookLine("seemed like a glorious", 60),
                        BookLine("dream...", 61),
                        BookLine("", 62),
                        BookLine("Larrissa and I have been", 63),
                        BookLine("slowly falling in love, and", 64),
                        BookLine("the torment that this", 65),
                    ),
                    Page( // 66-76
                        BookLine("lighthouse was to me", 66),
                        BookLine("when first I arrived here,", 67),
                        BookLine("is now the place where I", 68),
                        BookLine("have finally found true", 69), // nice
                        BookLine("happiness!", 70),
                        BookLine("", 71),
                        BookLine("All is not as perfect as I", 72),
                        BookLine("would like to believe it to", 73),
                        BookLine("be however.", 74),
                        BookLine("", 75),
                        BookLine("I have still had no luck in", 76),
                    ),
                ),
                // pages 19-20
                PageSet(
                    Page(
                        BookLine("understanding what", 55),
                        BookLine("happened to my uncle", 56),
                        BookLine("Silas, and his diary is so", 57),
                        BookLine("bizarre I cannot believe", 58),
                        BookLine("that the man who wrote it", 59),
                        BookLine("is the same man who", 60),
                        BookLine("used to take me on", 61),
                        BookLine("fishing trips to Karamja", 62),
                        BookLine("when I was a young lad.", 63),
                        BookLine("", 64),
                        BookLine("The writer seems to have", 65),
                    ),
                    Page(
                        BookLine("been driven horribly", 66),
                        BookLine("insane by living here,", 67),
                        BookLine("or by some event that he", 68),
                        BookLine("witnessed one day, and", 69),
                        BookLine("his diary is full of strange", 70),
                        BookLine("and cryptic passages...", 71),
                        BookLine("", 72),
                        BookLine("I feel sure that there is", 73),
                        BookLine("some message here, but I", 74),
                        BookLine("just cannot find what it", 75),
                        BookLine("says!", 76),
                    ),
                ),
                // pages 21-22
                PageSet(
                    Page(
                        BookLine("My nights alone are", 55),
                        BookLine("getting worse too. During", 56),
                        BookLine("the day when I am with", 57),
                        BookLine("Larrissa, I am always", 58),
                        BookLine("filled with joy, but when", 59),
                        BookLine("she goes home to her", 60),
                        BookLine("family at night, and I am", 61),
                        BookLine("left here alone my mind", 62),
                        BookLine("begins to play tricks upon", 63),
                        BookLine("me.", 64),
                        BookLine("", 65),
                    ),
                    Page(
                        BookLine("Often I think I can hear", 66),
                        BookLine("...things... moving in the", 67),
                        BookLine("crawlspaces of the", 68),
                        BookLine("lighthouse.", 69),
                        BookLine("", 70),
                        BookLine("Whenever the wind is to", 71),
                        BookLine("the North I can also", 72),
                        BookLine("sometimes hear a dread-", 73),
                        BookLine("ful chittering noise. Is it", 74),
                        BookLine("voices? Or some creature", 75),
                        BookLine("of the night? I know not,", 76),
                    ),
                ),
                // pages 23-24
                PageSet(
                    Page(
                        BookLine("but the sound disturbs", 55),
                        BookLine("me...", 56),
                        BookLine("", 57),
                        BookLine("One night I even thought", 58),
                        BookLine("I heard my uncle's voice", 59),
                        BookLine("calling to me to join him,", 60),
                        BookLine("but the voice was", 61),
                        BookLine("distorted, and gurgled as", 62),
                        BookLine("though he were trying to", 63),
                        BookLine("talk through water.", 64),
                    ),
                    Page(
                        BookLine("I dismissed this as a bad", 66),
                        BookLine("dream, yet it did disturb", 67),
                        BookLine("me deeply.", 68),
                        BookLine("", 69),
                        BookLine("I haven't told Larrissa of", 70),
                        BookLine("my concerns, for I would", 71),
                        BookLine("not wish her to worry", 72),
                        BookLine("about me, but I have", 73),
                        BookLine("decided to give her a", 74),
                        BookLine("spare key to this", 75),
                        BookLine("lighthouse, for I cannot", 76),
                    ),
                ),
                // pages 25-26
                PageSet(
                    Page(
                        BookLine("shake this feeling that", 55),
                        BookLine("should I remain here", 56),
                        BookLine("much longer something", 57),
                        BookLine("terrible is going to happen", 58),
                        BookLine("to me...", 59),
                        BookLine("", 60),
                        BookLine("Strange! I thought I", 61),
                        BookLine("heard a noise from", 62),
                        BookLine("downstairs just now! But", 63),
                        BookLine("with the front door", 64),
                        BookLine("locked, there is no way", 65),
                    ),
                    Page(
                        BookLine("for anyone to enter the", 66),
                        BookLine("lighthouse!", 67),
                        BookLine("", 68),
                        BookLine("", 69),
                        BookLine("I must investigate...", 70),
                    ),
                ),
            )
        private fun display(player:Player, pageNum: Int, buttonID: Int) : Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
            return true
        }
    }

    override fun defineListeners() {
        on(Items.JOURNAL_3845, IntType.ITEM, "read") { player, node ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            storeBookInHouse(player, node)
            return@on true
        }
    }
}
