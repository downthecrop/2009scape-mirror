package content.region.kandarin.quest.dwarfcannon.dmc

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.setAttribute
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items

@Initializable
class DMCManual : InteractionListener {
    companion object {
        private val TITLE = "Dwarf multicannon manual"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("<col=FF0000>Constructing the cannon</col>", 55),
                    BookLine("", 56),
                    BookLine("To construct the cannon, firstly", 57),
                    BookLine("set down the base of the cannon", 58),
                    BookLine("firmly onto the ground, next add", 59),
                    BookLine("the Dwarf stand to the cannon", 60),
                    BookLine("base then add the Barrels. Lastly", 61),
                    BookLine("add the Furnace, which powers", 62),
                    BookLine("the cannon. You should now have", 63),
                    BookLine("a fully set up Multi Cannon to", 64),
                    BookLine("splat nasty creatures!", 65),
                ),
                Page(
                    BookLine("<col=FF0000>Making ammo</col>", 66),
                    BookLine("", 67),
                    BookLine("The ammo for the cannon", 68),
                    BookLine("is made from steel bars.", 69),
                    BookLine("Firstly you must heat up a", 70),
                    BookLine("steel bar in a furnace.", 71),
                    BookLine("Now pour the molten steel", 72),
                    BookLine("into a cannon ammo mould.", 73),
                    BookLine("You should now have a ready", 74),
                    BookLine("to fire Multi cannon ball.", 75),
                )
            ),
            PageSet(
                Page(
                    BookLine("<col=FF0000>Firing the Cannon</col>", 55),
                    BookLine("", 56),
                    BookLine("The cannon will only fire", 57),
                    BookLine("when monsters are available", 58),
                    BookLine("to target. If you are carrying", 59),
                    BookLine("enough ammo the cannon will", 60),
                    BookLine("fire up to 30 rounds before it", 61),
                    BookLine("runs out and stops. The", 62),
                    BookLine("cannon will automatically target", 63),
                    BookLine("non friendly creatures.", 64),
                ),
                Page(
                    BookLine("<col=FF0000>Dwarf Cannon Warranty</col>", 66),
                    BookLine("", 67),
                    BookLine("If your cannon is stolen or", 68),
                    BookLine("has been lost, after or during", 69),
                    BookLine("being being set up, the Dwarf", 70),
                    BookLine("engineer will replace the parts,", 71),
                    BookLine("however cannon parts that", 72),
                    BookLine("were given away or dropped", 73),
                    BookLine("will not be replaced for free.", 74),
                    BookLine("It is only possible to operate", 75),
                    BookLine("one cannon at a time.", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("By order", 55),
                    BookLine("of the members of the noble", 56),
                    BookLine("Dwarven Black Guard.", 57),
                )
            ),
        )
    }

    private fun display(player: Player, pageNum: Int, buttonID: Int): Boolean {
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
        return true
    }


    override fun defineListeners() {
        on(Items.INSTRUCTION_MANUAL_5, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}