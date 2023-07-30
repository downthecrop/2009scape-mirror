package content.region.kandarin.dialogue

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
class BindingBook : InteractionListener {
    companion object {
        private val TITLE = "Book of Binding"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("      Book of Binding:", 60),
                    BookLine("    A treatise on Demons", 61),
                ),
                Page(
                    BookLine("", 67),
                    BookLine("     -- Indexo --", 68),
                    BookLine("    Arcana: I", 71),
                    BookLine("    Instructo: II", 72),
                    BookLine("    Defeati: III", 73),
                    BookLine("    Enchanto: IIII", 74),
                )
            ),
            PageSet(
                Page(
                    BookLine("Arcana I: Use holy water to", 55),
                    BookLine("determine possession. Slight", 56),
                    BookLine("appearance changes may be", 57),
                    BookLine("perceived when doused.", 58),
                    BookLine("", 59),
                    BookLine("Legendary Silverlight will help", 60),
                    BookLine("to defeat any demon by", 61),
                    BookLine("weakening it.", 62),
                ),
                Page(
                    BookLine("Arcana II: Be wary of any", 66),
                    BookLine("demon, it may have special", 67),
                    BookLine("forms of attack.", 68),
                    BookLine("", 69),
                    BookLine("Use an Octagram of fire", 70),
                    BookLine("to confine unearthly", 71),
                    BookLine("creatures of the underworld", 72),
                    BookLine("- the perfect geometry", 73),
                    BookLine("confuses them.", 74),
                    BookLine("", 75),
                    BookLine("Eximus", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("Instructo: Creation of holy", 55),
                    BookLine("water must be undertaken", 56),
                    BookLine("with determination and urgency.", 57),
                    BookLine("", 58),
                    BookLine("Take to yourself empty vials", 59),
                    BookLine("free of all liquids.", 60),
                    BookLine("", 61),
                    BookLine("Read warily the enchantment", 62),
                    BookLine("contained here within in order", 63),
                    BookLine("to magick the vial", 64),
                ),
                Page(
                    BookLine("for the holding of holy or", 66),
                    BookLine("sacred water.", 67),
                    BookLine("", 68),
                    BookLine("Take utmost care as you", 69),
                    BookLine("enchant them. With great care", 70),
                    BookLine("and precision place the sacred", 71),
                    BookLine("water into the magicked vial", 73),
                    BookLine("and stopper it.", 74),
                    BookLine("", 75),
                    BookLine("Eximus", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("Defeati... Ye dreaded", 55),
                    BookLine("demon will be of unholy", 56),
                    BookLine("power and abilities.", 57),
                    BookLine("", 58),
                    BookLine("Present thyself before", 59),
                    BookLine("the possessed with good", 60),
                    BookLine("intent and ready manner.", 61),
                    BookLine("", 62),
                    BookLine("With least obstruction", 63),
                    BookLine("and utmost solemnity hold", 64),
                    BookLine("open the pages of this", 65),
                ),
                Page(
                    BookLine("great tome in order that", 66),
                    BookLine("the goodlight falls upon", 67),
                    BookLine("the victim completely.", 68),
                    BookLine("Be thee prepared in every", 69),
                    BookLine("capacity, for the demon's", 70),
                    BookLine("tricks and wiles will outwit", 71),
                    BookLine("the unready man. Attack with", 72),
                    BookLine("vigour and zest if thee hopes", 73),
                    BookLine("to see another day.", 74),
                    BookLine("", 75),
                    BookLine("Eximus", 76),
                )
            ),
            PageSet(
                Page(
                    BookLine("Enchanto...", 55),
                    BookLine("", 56),
                    BookLine("Possessus valius emptious,", 57),
                    BookLine("projectus spellicus avoir valius", 58),
                    BookLine("magicus.", 59),
                    BookLine("", 60),
                    BookLine("Castus enchant avoir createur", 61),
                    BookLine("valius magicus holious avour", 62),
                    BookLine("defeati Demonicus Absolutus.", 63),
                    BookLine("", 64),
                    BookLine("Demonicus Absolutus.", 65),
                ),
                Page(
                    BookLine("Extralias projectus Magicus", 66),
                    BookLine("Holarius Attackanie demonicus", 67),
                    BookLine("Absolutus distancie airus", 68),
                    BookLine("throwus armiues.", 69),
                    BookLine("", 70),
                    BookLine("Eximus", 71),
                )
            ),
        )
    }

    private fun display(player: Player, pageNum: Int, buttonID: Int): Boolean {
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
        return true
    }

    override fun defineListeners() {
        on(Items.BINDING_BOOK_730, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}