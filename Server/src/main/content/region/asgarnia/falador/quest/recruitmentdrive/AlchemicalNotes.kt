package content.region.asgarnia.falador.quest.recruitmentdrive

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

// https://www.youtube.com/watch?v=o-bAoxIYT-A 7:27
@Initializable
class AlchemicalNotes : InteractionListener {
    companion object {
        private val TITLE = "Alchemical Reactions Study"
        private val CONTENTS = arrayOf(
                PageSet(
                        Page(
                                BookLine("Acetic acid and Cupric", 55),
                                BookLine("Sulphate:", 56),
                                BookLine("Endothermic.", 57),
                                BookLine("The Cupric is in ", 58),
                                BookLine("insufficient quantities to", 59),
                                BookLine("cause any noticeable", 60),
                                BookLine("reaction.", 61),
                        ),
                        Page(
                                BookLine("Acetic acid and Gypsum:", 66),
                                BookLine("Endothermic.", 67),
                                BookLine("Made a particularly bad", 68),
                                BookLine("smell, but little else that", 69),
                                BookLine("was productive.", 70),
                        )
                ),
                PageSet(
                        Page(
                                BookLine("Acetic acid and Sodium", 55),
                                BookLine("Chloride:", 56),
                                BookLine("Endothermic.", 57),
                                BookLine("Very tasty when", 58),
                                BookLine("combined with fried", 59),
                                BookLine("potatoes at room", 60),
                                BookLine("temperature.", 61),
                        ),
                        Page(
                                BookLine("Acetic acid and", 66),
                                BookLine("Dihydrogen Monoxide:", 67),
                                BookLine("Endothermic.", 68),
                                BookLine("The Dihydrogen", 69),
                                BookLine("Monoxide served only to", 70),
                                BookLine("dilute the Acetic acid at", 71),
                                BookLine("room temperature.", 72),
                        )
                ),
                PageSet(
                        Page(
                                BookLine("Acetic acid and Cupric", 55),
                                BookLine("Ore Powder:", 56),
                                BookLine("Endothermic.", 57),
                                BookLine("The powdered form of", 58),
                                BookLine("Cupric Ore allowed a", 59),
                                BookLine("lower than usual melting", 60),
                                BookLine("temperature, but the end", 61),
                                BookLine("product was non-usable.", 62),
                        ),
                        Page(
                                BookLine("Acetic acid and Tin Ore", 66),
                                BookLine("powder:", 67),
                                BookLine("Endothermic.", 68),
                                BookLine("Similar results to those", 69),
                                BookLine("made using Cupric Ore.", 70),
                        )
                ),
                PageSet(
                        Page(
                                BookLine("Cupric Sulphate and", 55),
                                BookLine("Dihyrdogen Monoxide:", 56),
                                BookLine("Exothermic.", 57),
                                BookLine("A blue compound was", 58),
                                BookLine("produced, along with heat.", 59),
                        ),
                        Page(
                                BookLine("Cupric Sulphate and", 66),
                                BookLine("Gypsum:", 67),
                                BookLine("Endothermic.", 68),
                                BookLine("At room temperature, no", 69),
                                BookLine("useful product was", 70),
                                BookLine("created.", 71),
                        )
                ),
                PageSet(
                        Page(
                                BookLine("Cupric Sulphate and", 55),
                                BookLine("Sodium Chloride:", 56),
                                BookLine("Endothermic.", 57),
                                BookLine("A pungent odour was", 58),
                                BookLine("released when combined.", 59),
                        ),
                        Page(
                                BookLine("Cupric Sulphate and", 66),
                                BookLine("Cupric Ore powder:", 67),
                                BookLine("Endothermic.", 68),
                                BookLine("The Cupric did not react", 69),
                                BookLine("with each other at room", 70),
                                BookLine("temperature.", 71),
                        )
                ),
                PageSet(
                        Page(
                                BookLine("Cupric Sulphate and Tin", 55),
                                BookLine("Ore powder:", 56),
                                BookLine("Endothermic.", 57),
                                BookLine("Similar results to those", 58),
                                BookLine("shown with Cupric Ore,", 59),
                                BookLine("despite the increased", 60),
                                BookLine("solubility involved with", 61),
                                BookLine("the powdered form.", 62),
                        ),
                        Page(
                                BookLine("Gypsum and Dihydrogen", 66),
                                BookLine("Monoxide:", 67),
                                BookLine("Exothermic.", 68),
                                BookLine("A white liquid compound", 69),
                                BookLine("was formed, that quickly", 70),
                                BookLine("cooled at room", 71),
                                BookLine("temperature to a white", 72),
                                BookLine("heat resistant solid very", 73),
                                BookLine("similar to plaster.", 74),
                                BookLine("Heat was also produced,", 75),
                                BookLine("although not in the same", 76),
                        )
                ),
                PageSet(
                        Page(
                                BookLine("quantity as Cupric", 55),
                                BookLine("Sulphate with Dihydrogen", 56),
                                BookLine("Monoxide", 57),
                        ),
                        Page(
                                BookLine("Gypsum and Sodium", 66),
                                BookLine("Chloride:", 67),
                                BookLine("Endothermic.", 68),
                                BookLine("The two did not seem to", 69),
                                BookLine("noticably mix together at", 70),
                                BookLine("room temperature.", 71),
                        )
                ),
                PageSet(
                        Page(
                                BookLine("Gypsum and Culpric Ore:", 55),
                                BookLine("Endothermic.", 56),
                                BookLine("The gypsum seems quite", 57),
                                BookLine("resistant to most", 58),
                                BookLine("compounds at normal", 59),
                                BookLine("room temperature.", 60),
                        ),
                        Page(
                                BookLine("Gypsum and Tin Ore:", 66),
                                BookLine("Endothermic.", 67),
                                BookLine("Again, very similar results", 68),
                                BookLine("as those shown with", 69),
                                BookLine("Cupric Ore.", 70),
                        )
                ),

                PageSet(
                        Page(
                                BookLine("Sodium Chloride and", 55),
                                BookLine("Dihydrogen Monoxide:", 56),
                                BookLine("Endothermic.", 57),
                                BookLine("At room temperature, the", 58),
                                BookLine("Sodium Chloride dissolves", 59),
                                BookLine("quite easily. Dissolution is", 60),
                                BookLine("faster at higher", 61),
                                BookLine("temperatures.", 62),
                        ),
                        Page(
                                BookLine("Sodium Chloride and", 66),
                                BookLine("Cupric Ore:", 67),
                                BookLine("Endothermic.", 68),
                                BookLine("No visible combination at", 69),
                                BookLine("room temperature.", 70),
                        )
                ),
                PageSet(
                        Page(
                                BookLine("Sodium Chloride and Tin", 55),
                                BookLine("Ore:", 56),
                                BookLine("Endothermic.", 57),
                                BookLine("Another very similar ", 58),
                                BookLine("result as with Cupric Ore.", 59),
                        ),
                        Page(
                                BookLine("Cupric Ore Powder and", 66),
                                BookLine("Tin Ore Powder:", 67),
                                BookLine("Endothermic.", 68),
                                BookLine("When both ores are in", 69),
                                BookLine("particulate form, a much", 70),
                                BookLine("lower than usual bonding", 71),
                                BookLine("temperature can be", 72),
                                BookLine("obtained.", 73),
                                BookLine("When combined at a", 74),
                                BookLine("moderate heat, (my", 75),
                                BookLine("laboratory heating", 76),
                        )
                ),
                PageSet(
                        Page(
                                BookLine("apparatus) I was able to", 55),
                                BookLine("form liquid Bronze quite", 56),
                                BookLine("easily, which cooled to", 57),
                                BookLine("form a standard Bronze", 58),
                                BookLine("Bar at a temperature far", 60),
                                BookLine("lower than that required", 61),
                                BookLine("to produce in mass at a", 62),
                                BookLine("furnace.", 63),
                        ),
                        Page(
                                BookLine("Nitrous Monoxide:", 66),
                                BookLine("Was not able to perform", 67),
                                BookLine("an experimentation using", 68),
                                BookLine("this substance, as the", 69),
                                BookLine("gaseous form would", 70),
                                BookLine("always escape when the", 71),
                                BookLine("vial was opened.", 72),
                        )
                )
        )
    }

    private fun display(player:Player, pageNum: Int, buttonID: Int) : Boolean {
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
        return true
    }


    override fun defineListeners() {
        on(Items.ALCHEMICAL_NOTES_5588, IntType.ITEM, "read") { player, _ ->
            setAttribute(player, "bookInterfaceCallback", ::display)
            setAttribute(player, "bookInterfaceCurrentPage", 0)
            display(player, 0, 0)
            return@on true
        }
    }
}