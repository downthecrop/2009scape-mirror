package content.region.misthalin.varrock.quest.shieldofarrav

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
 * Shield of Arrav Book
 * @author ovenbreado
 * @author 'Vexia
 * @author Empathy
 */
class ShieldofArravBook : InteractionListener {
    companion object {
        private val TITLE = "Shield of Arrav"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("The Shield of Arrav", 55),
                    BookLine("by A.R Wright", 57),
                    BookLine("Arrav is probably the best", 61),
                    BookLine("known hero of the 4th", 62),
                    BookLine("age. Many legends are", 63),
                    BookLine("told of his heroics. One", 64),
                    BookLine("surviving artefact from", 65)
                ),
                Page(
                    BookLine("the 4th age is a fabulous", 66),
                    BookLine("shield.", 67),
                    BookLine("This shield is believed to", 69),
                    BookLine("have once belonged to", 70),
                    BookLine("Arrav and is now indeed", 71),
                    BookLine("known as the Shield of", 72),
                    BookLine("Arrav. For over 150", 73),
                    BookLine("years it was the prize", 74),
                    BookLine("piece in the royal", 75),
                    BookLine("museum of Varrock.", 76)
                )
            ),
            PageSet(
                Page(
                    BookLine("However, in the year 143", 55),
                    BookLine("of the fith age a gang of", 56),
                    BookLine("thieves called the Phoenix", 57),
                    BookLine("Gang broke into the", 58),
                    BookLine("museum and stole the", 59),
                    BookLine("shield in a daring raid. As", 60),
                    BookLine("a result, the current", 61),
                    BookLine("ruler, King Roald, put a", 62),
                    BookLine("1200 gold bounty (a", 63),
                    BookLine("massive sum of money in", 64),
                    BookLine("those days) on the return", 65)
                ),
                Page(
                    BookLine("of the shield, hoping that", 66),
                    BookLine("one of the culprits would", 67),
                    BookLine("betray his fellows out of", 68),
                    BookLine("greed.", 69)
                )
            ),
            PageSet(
                Page(
                    BookLine("This tactic did not work", 55),
                    BookLine("however, and the thieves", 56),
                    BookLine("who stole the shield have", 57),
                    BookLine("since gone on to become", 58),
                    BookLine("the most powerful crime", 59),
                    BookLine("gang in Varrock, despite", 60),
                    BookLine("making an enemy of the", 61),
                    BookLine("Royal Family many", 62),
                    BookLine("years ago.", 63)
                ),
                Page(
                    BookLine("The reward for the", 66),
                    BookLine("return of the shield still", 67),
                    BookLine("stands.", 68)
                )
            )
        )
        private fun display(player: Player, pageNum: Int, buttonID: Int) : Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
            if (BookInterface.isLastPage(pageNum, CONTENTS.size)) {
                if (player.questRepository.getQuest("Shield of Arrav").getStage(player) == 10) {
                    player.questRepository.getQuest("Shield of Arrav").setStage(player, 20)
                }
            }
            return true
        }
    }

    override fun defineListeners() {
        on(Items.BOOK_757, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}