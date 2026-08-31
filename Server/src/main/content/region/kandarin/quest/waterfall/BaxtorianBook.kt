package content.region.kandarin.quest.waterfall

import content.data.Quests
import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.getQuestStage
import core.api.sendMessage
import core.api.setQuestStage
import core.api.storeBookInHouse
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs09.consts.Items

/**
 * The Book on Baxtorian
 * @author ovenbreado
 * @author Bishop
 */

class BaxtorianBook : InteractionListener {

    companion object {
        private const val RED = "<col=8A0808>"

        private val TITLE = "Book on Baxtorian"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine(RED + "The missing relics", 55),
                    BookLine("", 56),
                    BookLine("Many artefacts of elven", 57),
                    BookLine("history were lost after the", 58),
                    BookLine("fourth age, following the", 59),
                    BookLine("departure of the elven", 60),
                    BookLine("colonies from these lands.", 61),
                    BookLine("The greatest loss to our", 62),
                    BookLine("collections of elf history", 63),
                    BookLine("were the hidden treasures", 64),
                    BookLine("of Baxtorian. Some", 65)
                ),
                Page(
                    BookLine("believe these treasures", 66),
                    BookLine("are still unclaimed, but it", 67),
                    BookLine("is more commonly", 68),
                    BookLine("believed that dwarf miners", 69),
                    BookLine("recovered the treasure", 70),
                    BookLine("early in the 5th Age.", 71),
                    BookLine("", 72),
                    BookLine("Another great loss was", 73),
                    BookLine("Glarial's pebble, a key", 74),
                    BookLine("which allowed her family", 75),
                    BookLine("to visit her tomb.", 76)
                )
            ),
            PageSet(
                Page(
                    BookLine("The pebble was taken by", 55),
                    BookLine("a gnome many years", 56),
                    BookLine("ago. It is hoped that", 57),
                    BookLine("descendents " /*sic*/+ "of that", 58),
                    BookLine("gnome may still have the", 59),
                    BookLine("pebble hidden in their", 60),
                    BookLine("cave under the Tree", 61),
                    BookLine("Gnome Village.", 62),
                    BookLine("", 63),
                    BookLine("Unfortunately the maze", 64),
                    BookLine("around that village makes", 65)
                ),
                Page(
                    BookLine("it difficult to contact the", 66),
                    BookLine("gnomes to investigate this", 67),
                    BookLine("matter.", 68),
                    BookLine("", 69),
                    BookLine("", 70),
                    BookLine("", 71),
                    BookLine("", 72),
                    BookLine("", 73),
                    BookLine("", 74),
                    BookLine("", 75),
                    BookLine("", 76)
                )
            ),
            PageSet(
                Page(
                    BookLine(RED + "The fall of Baxtorian", 55),
                    BookLine("", 56),
                    BookLine("The love between", 57),
                    BookLine("Baxtorian and Glarial was", 58),
                    BookLine("said to have lasted over a", 59),
                    BookLine("century. They lived a", 60),
                    BookLine("peaceful life learning and", 61),
                    BookLine("teaching the laws of", 62),
                    BookLine("nature.", 63),
                    BookLine("", 64),
                    BookLine("When their homeland in", 65)
                ),
                Page(
                    BookLine("the far west was plunged", 66),
                    BookLine("into chaos by dark forces,", 67),
                    BookLine("Baxtorian left on a", 68),
                    BookLine("dangerous campaign that", 69),
                    BookLine("lasted for five years. He", 70),
                    BookLine("survived to return to this", 71),
                    BookLine("land, but found his people", 72),
                    BookLine("slaughtered and his wife", 73),
                    BookLine("taken by the enemy.", 74),
                    BookLine("", 75),
                    BookLine("After years of searching", 76)
                )
            ),
            PageSet(
                Page(
                    BookLine("for his love he finally", 55),
                    BookLine("gave up and returned to", 56),
                    BookLine("the home he made for", 57),
                    BookLine("Glarial under the", 58),
                    BookLine("Baxtorian Waterfall.", 59),
                    BookLine("Once he entered he", 60),
                    BookLine("never returned.", 61),
                    BookLine("", 62),
                    BookLine("Only he and Glarial had", 63),
                    BookLine("the power to enter the", 64),
                    BookLine("waterfall. Since Baxtorian", 65)
                ),
                Page(
                    BookLine("entered, no-one else can", 66),
                    BookLine("follow him in, it's as if the", 67),
                    BookLine("powers of nature still", 68),
                    BookLine("work to protect his peace.", 69),
                    BookLine("", 70),
                    BookLine("", 71),
                    BookLine("", 72),
                    BookLine("", 73),
                    BookLine("", 74),
                    BookLine("", 75),
                    BookLine("", 76)
                )
            ),
            PageSet(
                Page(
                    BookLine(RED + "The power of nature", 55),
                    BookLine("", 56),
                    BookLine("Glarial and Baxtorian", 57),
                    BookLine("were masters of nature.", 58),
                    BookLine("Trees and flowers would", 59),
                    BookLine("grow, hills form and", 60),
                    BookLine("rivers flood on their", 61),
                    BookLine("command.", 62),
                    BookLine("", 63),
                    BookLine("Baxtorian in particular", 64),
                    BookLine("had perfected rune lore.", 65)
                ),
                Page(
                    BookLine("It was said that he could", 66),
                    BookLine("use the stones to control", 67),
                    BookLine("water, earth and air.", 68),
                    BookLine("", 69),
                    BookLine("", 70),
                    BookLine("", 71),
                    BookLine("", 72),
                    BookLine("", 73),
                    BookLine("", 74),
                    BookLine("", 75),
                    BookLine("", 76)
                )
            ),
            PageSet(
                Page(
                    BookLine(RED + "Ode to eternity", 55),
                    BookLine("", 56),
                    BookLine("(A short piece written by", 57),
                    BookLine("Baxtorian himself.)", 58),
                    BookLine("", 59),
                    BookLine("What care I for this", 60),
                    BookLine("mortal coil,", 61),
                    BookLine("where treasures are yet", 62),
                    BookLine("so frail,", 63),
                    BookLine("for it is you that is my", 64),
                    BookLine("life blood,", 65)
                ),
                Page(
                    BookLine("the wine to my holy grail", 66),
                    BookLine("and if I see the", 67),
                    BookLine("judgement day,", 68),
                    BookLine("when the gods fill the air", 69),
                    BookLine("with dust,", 70),
                    BookLine("I'll happily choke on your", 71),
                    BookLine("memory,", 72),
                    BookLine("as my kingdom turns to", 73),
                    BookLine("rust.", 74),
                    BookLine("", 75),
                    BookLine("", 76)
                )
            ),
        )
        private fun display(player: Player, pageNum: Int, buttonID: Int) : Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
            if (getQuestStage(player, Quests.WATERFALL_QUEST) == 20) {
                setQuestStage(player, Quests.WATERFALL_QUEST, 30)
            }
            return true
        }
    }

    override fun defineListeners() {
        on(Items.BOOK_ON_BAXTORIAN_292, IntType.ITEM, "read") { player, node ->
            sendMessage(player, "The book is old with many pages missing.")
            sendMessage(player, "A few are translated from elven into common tongue.")
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            storeBookInHouse(player, node)
            return@on true
        }
    }
}