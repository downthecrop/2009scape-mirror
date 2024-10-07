package content.region.misthalin.varrock.dialogue

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.setAttribute
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs09.consts.Items

// This is not formatted well. See _-88E9n9jWA
class SinkethsDiary : InteractionListener {
    // Obtainable during the What Lies Below quest.
    companion object {
        private val TITLE = "Sin'keth's diary"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("<col=08088A>'...2nd Pentember,", 55),
                    BookLine("<col=08088A>Fifth Age,70", 56),
                    BookLine("We have worked", 57),
                    BookLine("for days. It is a weary", 58),
                    BookLine("and tiring journey that", 59),
                    BookLine("my brothers and I must", 60),
                    BookLine("take,but we are close", 61),
                    BookLine("to success! Elder Dag'eth", 62),
                    BookLine("has led us well and he", 63),
                    BookLine("has told us that Zamorak", 64),
                    BookLine("will reward us greatly", 65),
                    BookLine("for our service to Him.", 66),
                ),
                Page(
                    BookLine("The priests of Saradomin", 67),
                    BookLine("haunt our very steps and", 68),
                    BookLine("I fear our discovery. Yet,", 69),
                    BookLine("soon will be the hour of", 70),
                    BookLine("our glory. The Dagon'hai", 71),
                    BookLine("will prevail and the city", 72),
                    BookLine("will be ours! We will throw", 73),
                    BookLine("down the vile yoke of", 74),
                    BookLine("Saradominand the", 75),
                    BookLine("Dagon'hai will", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("be victorious! ", 55),
                    BookLine("", 56),
                    BookLine("<col=08088A>9th Pentember,Fifth Age, 70", 57),
                    BookLine("Today we donned the", 58),
                    BookLine("filthy robes of", 59),
                    BookLine("the Saradomin priests.", 60),
                    BookLine("It was a foul deed and", 61),
                    BookLine("distasteful to my very", 62),
                    BookLine("soul,yet it had to be", 63),
                    BookLine("done. Without the disguise,", 64),
                    BookLine("we would surely have been", 65),
                ),
                Page(
                    BookLine("found out and ruined. ", 66),
                    BookLine("We erected a statue", 67),
                    BookLine("of Saradomin himself", 68),
                    BookLine("just outside the city", 69),
                    BookLine("to the east. Our Lord", 70),
                    BookLine("Zamorak must be laughing", 71),
                    BookLine("in the faces of our enemies", 72),
                    BookLine("at such a deception,for", 73),
                    BookLine("this statue holds the key", 74),
                    BookLine("to our success. Beneath", 75),
                    BookLine("the arrogant caricature", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("of this worthless deity", 55),
                    BookLine("lies the entrance to our", 56),
                    BookLine("most sacred work yet: the", 57),
                    BookLine("Tunnel of Chaos. With this", 58),
                    BookLine("tunnel,we are able to", 59),
                    BookLine("traverse to the very", 60),
                    BookLine("source of our power,", 61),
                    BookLine("the Chaos Temple itself.", 62),
                    BookLine("Those foolish followers", 63),
                    BookLine("of Saradomin do not", 64),
                    BookLine("even sense what", 65),
                    BookLine("we have achieved. They", 66),
                ),
                Page(
                    BookLine("have filled the statue", 67),
                    BookLine("with their accursed holy", 68),
                    BookLine("magic,covering even the", 69),
                    BookLine("merest traces of our work", 70),
                    BookLine("beneath. They have granted", 71),
                    BookLine("us the most perfect of", 72),
                    BookLine("disguises.", 73),
                    BookLine("Zamorak be praised!", 74),
                    BookLine("", 75),
                    BookLine("<col=08088A>11th Pentember,Fifth Age, 70", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("Excellent news! I", 55),
                    BookLine("have been chosen", 56),
                    BookLine("by Elder Dag'eth to be", 57),
                    BookLine("the next Hyeraph. I,", 58),
                    BookLine("Sin'keth Magis,", 59),
                    BookLine("will lead our people", 60),
                    BookLine("in the incantation of", 61),
                    BookLine("Zamorak's Will. ", 62),
                    BookLine("Surely this means", 63),
                    BookLine("I will become High Elder!", 64),
                    BookLine("I must prove worthy to", 65),
                    BookLine("Lord Zamorak. He will not", 66),
                ),
                Page(
                    BookLine("find me wanting. There", 67),
                    BookLine("is much to do in ", 68),
                    BookLine("preparation for the", 69),
                    BookLine("ceremony and I do", 70),
                    BookLine("not have long.", 71),
                    BookLine("", 72),
                    BookLine("<col=08088A>24th Septober,Fifth Age, 70", 73),
                    BookLine("Disaster!", 74),
                    BookLine("The incantation of Zamorak's", 75),
                    BookLine("Will was discovered by", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("a loathsome watchman,of", 55),
                    BookLine("all people. ", 56),
                    BookLine("Zamorak's Blood!", 57),
                    BookLine("The fates are cruel! We", 58),
                    BookLine("could not finish the final", 59),
                    BookLine("rites of the spell. Our", 60),
                    BookLine("work has been undone and", 61),
                    BookLine("we have no time to gather", 62),
                    BookLine("our forces together and", 63),
                    BookLine("hide. We are being followed", 64),
                    BookLine("by the guards and the ", 65),
                    BookLine("Priests of Filth are", 66),
                ),
                Page(
                    BookLine("at our heels.", 67),
                    BookLine("We must flee the city!", 68),
                    BookLine("Elder La'nou and Elder", 69),
                    BookLine("Kree'nag were slain whilst", 70),
                    BookLine("protecting the sanctum.", 71),
                    BookLine("Elder Dag'eth will not", 72),
                    BookLine("leave with us. Zamorak", 73),
                    BookLine("take him,he will stand", 74),
                    BookLine("against the hordes that", 75),
                    BookLine("follow us! I am the last", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("of the Elders. The order", 55),
                    BookLine("looks to me now.", 56),
                    BookLine("", 57),
                    BookLine("<col=08088A>27th Septober,Fifth Age, 70", 58),
                    BookLine("Today,the last of", 59),
                    BookLine("our order entered the", 60),
                    BookLine("Tunnel of Chaos. We", 61),
                    BookLine("will journey to the Chaos", 62),
                    BookLine("Temple and let Zamorak", 63),
                    BookLine("Himself decide our fate.", 64),
                    BookLine("What happened to Elder", 65),
                    BookLine("Dag'eth, I know not. As", 66),
                ),
                Page(
                    BookLine("the city guards closed", 67),
                    BookLine("upon us,I cast an Earth", 68),
                    BookLine("Bolt spell to collapse", 69),
                    BookLine("the entrance of the tunnel", 70),
                    BookLine("in an avalanche of earth", 71),
                    BookLine("and stone,saving us and", 72),
                    BookLine("dooming us in one breath.", 73),
                    BookLine("There is only one place", 74),
                    BookLine("for us to go now...", 75),
                ),
            )
        )
    }

    private fun display(player: Player, pageNum: Int, buttonID: Int): Boolean {
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
        return true
    }

    override fun defineListeners() {
        on(Items.SINKETHS_DIARY_11002, IntType.ITEM, "read") { player, _ ->
            setAttribute(player, "bookInterfaceCallback", ::display)
            setAttribute(player, "bookInterfaceCurrentPage", 0)
            display(player, 0, 0)
            return@on true
        }
    }
}