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

class LighthouseManual : InteractionListener {
    companion object {
        private val TITLE = "Manual"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("Lightomatic Deluxe 500", 55),
                        BookLine("Maintenance, Upkeep and", 56),
                        BookLine("Users' Manual: Second", 57),
                        BookLine("Edition", 58),
                    ),
                    Page(
                        BookLine("Thank you for", 66),
                        BookLine("purchasing the", 67),
                        BookLine("Lightomatic Deluxe", 68),
                        BookLine("lighthouse lighting", 69),
                        BookLine("equipment. The following", 70),
                        BookLine("is important user", 71),
                        BookLine("information, and should", 72),
                        BookLine("be read before you put", 73),
                        BookLine("your lightomatic into full", 74),
                        BookLine("use.", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Section 1: Specifications", 55),
                    ),
                    Page(
                        BookLine("The Lightomatic Deluxe", 66),
                        BookLine("has a maximum viewing", 67),
                        BookLine("range of over 3 nautical", 68),
                        BookLine("miles, in optimal", 69),
                        BookLine("conditions. In foggy or", 70),
                        BookLine("wet weather, visible", 71),
                        BookLine("distance is reduced to", 72),
                        BookLine("approximately 1 nautical", 73),
                        BookLine("mile.", 74),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Section 2: Upkeep", 55),
                    ),
                    Page(
                        BookLine("The Lightomatic Deluxe", 66),
                        BookLine("uses an advanced system", 67),
                        BookLine("of a torch and lenses to", 68),
                        BookLine("amplify the ambient visible", 69),
                        BookLine("light levels produced by", 70),
                        BookLine("your torch. We", 71),
                        BookLine("recommend all lighthouse", 72),
                        BookLine("keepers ensure that they", 73),
                        BookLine("keep their lens clean and", 74),
                        BookLine("dust free to ensure", 75),
                        BookLine("optimal lighting conditions.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Section 3: Troubleshooting", 55),
                    ),
                    Page(
                        BookLine("We have included the", 66),
                        BookLine("following Frequently", 67),
                        BookLine("Asked Questions (FAQs)", 68),
                        BookLine("for users who may have", 69),
                        BookLine("problems with the", 70),
                        BookLine("installation or upkeep of", 71),
                        BookLine("their Lightomatic Deluxe.", 72),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("1) My lightomatic does", 55),
                        BookLine("not fit its DPM", 56),
                        BookLine("(Deployment Point", 57),
                        BookLine("Mechanism) is there", 58),
                        BookLine("anything I can do to", 59),
                        BookLine("make it fit?", 60),
                        BookLine("We suggest that users", 61),
                        BookLine("manually inspect their", 62),
                        BookLine("DPMs before purchasing", 63),
                        BookLine("a Lightomatic Deluxe, as", 64),
                        BookLine("you will require a", 65),
                    ),
                    Page(
                        BookLine("specialist constructor to", 66),
                        BookLine("enlarge your DPM, or", 67),
                        BookLine("may even need to", 68),
                        BookLine("upgrade your lighthouse", 69),
                        BookLine("to a newer version.", 70),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("2) There does not seem", 55),
                        BookLine("to be any light coming", 56),
                        BookLine("from my Lightomatic", 57),
                        BookLine("Deluxe, help!", 58),
                        BookLine("If users are not getting", 59),
                        BookLine("light coming from their", 60),
                        BookLine("Lightomatic Deluxe, there", 61),
                        BookLine("are a number of factors", 62),
                        BookLine("that could cause this.", 63),
                        BookLine("Firstly, check that your", 64),
                        BookLine("LSM (Light Source", 65),
                    ),
                    Page(
                        BookLine("Module) has been", 66),
                        BookLine("correctly activated.", 67),
                        BookLine("If it is on fire, then it is", 68),
                        BookLine("at optimal functioning", 69),
                        BookLine("capacity. If it is not on", 70),
                        BookLine("fire, you will need to use", 71),
                        BookLine("a tinderbox to light it.", 72),
                        BookLine("If using a tinderbox on", 73),
                        BookLine("your LSM has no effect,", 74),
                        BookLine("your NRLSF (Non", 75),
                        BookLine("Renewing Light Source", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Fuel) may be at fault.", 55),
                        BookLine("Although we only", 56),
                        BookLine("recommend the use of", 57),
                        BookLine("official Lightomatic", 58),
                        BookLine("NRLSFs (purchased", 59),
                        BookLine("separately) a make do", 60),
                        BookLine("solution can be achieved", 61),
                        BookLine("by using swamp tar on", 62),
                        BookLine("the LSM, which should be", 63),
                        BookLine("capable of restarting the", 64),
                        BookLine("flames when activated with", 65),
                    ),
                    Page(
                        BookLine("a tinderbox.", 66),
                        BookLine("NOTE: Use of swamp", 67),
                        BookLine("tar voids your warranty.", 68),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("3) My LSM works fine,", 55),
                        BookLine("but the visible light at sea", 56),
                        BookLine("is very weak!", 57),
                        BookLine("If ambient light is at", 58),
                        BookLine("optimal conditions, but is", 59),
                        BookLine("below expected viewable", 60),
                        BookLine("range, there could be a", 61),
                        BookLine("fault with your LALS", 62),
                        BookLine("(Light Amplifying Lens", 63),
                        BookLine("System). If this is the", 64),
                        BookLine("case, please do the", 65),
                    ),
                    Page(
                        BookLine("following:", 66),
                        BookLine("a) Ensure that the dark", 67),
                        BookLine("wrapping that the LALS is", 68),
                        BookLine("shipped in has been", 69),
                        BookLine("removed from your", 70),
                        BookLine("LALS. Failure to remove", 71),
                        BookLine("this wrapping can", 72),
                        BookLine("adversely affect your", 73),
                        BookLine("LALS performance.", 74),
                        BookLine("b) If your LALS should", 75),
                        BookLine("develop a crack, this may", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("affect performance. We", 55),
                        BookLine("recommend purchasing", 56),
                        BookLine("the official Lightomatic", 57),
                        BookLine("LALS Maintenance Pack,", 58),
                        BookLine("or using molten glass", 59),
                        BookLine("upon the LALS may", 60),
                        BookLine("provide a stop gap", 61),
                        BookLine("solution.", 62),
                        BookLine("NOTE: The use of", 63),
                        BookLine("unauthorized molten glass", 64),
                        BookLine("on your LALS will", 65),
                    ),
                    Page(
                        BookLine("invalidate your warranty.", 66),
                        BookLine("Should you have any", 67),
                        BookLine("further problems with the", 68),
                        BookLine("operation of your", 69),
                        BookLine("Lightomatic Deluxe, then", 70),
                        BookLine("please contact our service", 71),
                        BookLine("center through our", 72),
                        BookLine("advertisements in the", 73),
                        BookLine("Varrock Herald.", 74),
                        BookLine("If your Lightomatic", 75),
                        BookLine("Deluxe is still under", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("warranty, we will then", 55),
                        BookLine("send an ADE (Authorised", 56),
                        BookLine("Dwarven Engineer) to", 57),
                        BookLine("repair any damage for a", 58),
                        BookLine("nominal fee.", 59),
                        BookLine("NOTE: By reading our", 60),
                        BookLine("advertisement in the", 61),
                        BookLine("Varrock Herald, you will", 62),
                        BookLine("invalidate your warranty.", 63),
                        BookLine("Thank you for", 65),
                    ),
                    Page(
                        BookLine("purchasing the", 66),
                        BookLine("Lightomatic Deluxe.", 67),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Lightomatic - the brand", 60),
                        BookLine("you can sometimes trust.", 61),
                    ),
                ),
            )
        private fun display(player:Player, pageNum: Int, buttonID: Int) : Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
            return true
        }
    }

    override fun defineListeners() {
        on(Items.MANUAL_3847, IntType.ITEM, "read") { player, node ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            storeBookInHouse(player, node)
            return@on true
        }
    }
}
