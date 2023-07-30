package content.region.kandarin.quest.waterfall

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
 * Baxtorian Book
 * @author ovenbreado
 * @author Splinter
 */
class BaxtorianBook : InteractionListener {

    companion object {
        private val TITLE = "Book on Baxtorian"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("The missing relics", 55),
                    BookLine("", 56),
                    BookLine("    Many artefacts of", 57),
                    BookLine("elven history were lost", 58),
                    BookLine("after the fourth age. The", 59),
                    BookLine("greatest loss to our", 60),
                    BookLine("collections of elf history", 61),
                    BookLine("were the hidden treasures", 62),
                    BookLine("of Baxtorian.", 63),
                    BookLine("  Some believe these", 64),
                    BookLine("treasures are still", 65)
                ),
                Page(
                    BookLine("unclaimed, but it is more", 66),
                    BookLine("commonly believed that", 67),
                    BookLine("dwarf miners recovered", 68),
                    BookLine("the treasure at the", 69),
                    BookLine("beginning of the third", 70),
                    BookLine("age. Another great loss", 71),
                    BookLine("was Glarial's pebble, a key", 72),
                    BookLine("which allowed her family", 73),
                    BookLine("to visit her tomb.", 74),
                    BookLine("    The stone was taken", 75),
                    BookLine("by a gnome family over a", 76)
                )
            ),
            PageSet(
                Page(
                    BookLine("century ago. It is", 55),
                    BookLine("believed that the gnomes'", 56),
                    BookLine("descendant Golrie still has", 57),
                    BookLine("the stone hidden in the", 58),
                    BookLine("caves under the gnome", 59),
                    BookLine("tree village.", 60),
                    BookLine("", 61),
                    BookLine("The sonnet of Baxtorian", 62),
                    BookLine("", 63),
                    BookLine("The love between", 64),
                    BookLine("Baxtorian and Glarial was", 65)
                ), Page(
                    BookLine("said to have lasted over a", 66),
                    BookLine("century. They lived a", 67),
                    BookLine("peaceful life learning and", 68),
                    BookLine("teaching the laws of", 69),
                    BookLine("nature. When Baxtorian's", 70),
                    BookLine("kingdom was invaded by", 71),
                    BookLine("the dark forces he left on", 72),
                    BookLine("a five year campaign. He", 73),
                    BookLine("returned to find his", 74),
                    BookLine("people slaughtered and his", 75),
                    BookLine("wife taken by the enemy.", 76)
                )
            ),
            PageSet(
                Page(
                    BookLine("    After years of", 55),
                    BookLine("searching for his love he", 56),
                    BookLine("finally gave up and", 57),
                    BookLine("returned to the home he", 58),
                    BookLine("made for Glarial under", 59),
                    BookLine("the Baxtorian Waterfall.", 60),
                    BookLine("Once he entered he", 61),
                    BookLine("never returned. Only", 62),
                    BookLine("Glarial had the power to", 63),
                    BookLine("also enter the waterfall.", 64),
                    BookLine("  Since Baxtorian", 65)
                ),
                Page(
                    BookLine("entered no one but her", 66),
                    BookLine("can follow him in, it's as if", 67),
                    BookLine("the powers of nature still", 68),
                    BookLine("work to protect him.", 69),
                    BookLine("", 70),
                    BookLine("The power of nature", 71),
                    BookLine("", 72),
                    BookLine("    Glarial and Baxtorian", 73),
                    BookLine("were masters of nature.", 74),
                    BookLine("Trees would grow, hills", 75),
                    BookLine("form and rivers flood on", 76)
                )
            ),
            PageSet(
                Page(
                    BookLine("their command. Baxtorian", 55),
                    BookLine("in particular had", 56),
                    BookLine("perfected rune lore. It", 57),
                    BookLine("was said that he could", 58),
                    BookLine("uses the stones to control", 59),
                    BookLine("water, earth, and air.", 60),
                    BookLine("", 61),
                    BookLine("Ode to eternity", 62),
                    BookLine("", 63),
                    BookLine("A short piece written by", 64),
                    BookLine("Baxtorian himself.", 65)
                ),
                Page(
                    BookLine("", 66),
                    BookLine("What care I for this", 67),
                    BookLine("mortal coil,", 68),
                    BookLine("where treasures are yet", 69),
                    BookLine("so frail,", 70),
                    BookLine("for it is you that is my", 71),
                    BookLine("life blood,", 72),
                    BookLine("the wine to my holy grail", 73),
                    BookLine("and if I see the", 74),
                    BookLine("judgement day,", 75),
                    BookLine("when the gods fill the air", 76)
                )
            ),
            PageSet(
                Page(
                    BookLine("with dust,", 55),
                    BookLine("I'll happily choke on your", 56),
                    BookLine("memory,", 57),
                    BookLine("as my kingdom turns to", 58),
                    BookLine("rust", 59)
                )
            )
        )
        private fun display(player: Player, pageNum: Int, buttonID: Int) : Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
            if (player.questRepository.getQuest(WaterFall.NAME).getStage(player) == 20) {
                player.questRepository.getQuest(WaterFall.NAME).setStage(player, 30)
            }
            return true
        }
    }

    override fun defineListeners() {
        on(Items.BOOK_ON_BAXTORIAN_292, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}