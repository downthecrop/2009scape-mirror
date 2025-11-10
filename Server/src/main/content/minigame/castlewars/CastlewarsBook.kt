package content.minigame.castlewars

import content.data.Quests
import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.getAttribute
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.system.command.sets.ModelViewerCommandSet.Companion.ATTRIBUTE_MODEL_NUMBER
import core.game.system.command.sets.ModelViewerCommandSet.Companion.ATTRIBUTE_PITCH
import core.game.system.command.sets.ModelViewerCommandSet.Companion.ATTRIBUTE_YAW
import core.game.system.command.sets.ModelViewerCommandSet.Companion.ATTRIBUTE_ZOOM
import core.game.system.command.sets.ModelViewerCommandSet.Companion.DEF_BOOK
import core.game.world.GameWorld
import org.rs09.consts.Items

class CastlewarsBook : InteractionListener {

    companion object {
        private val TITLE = "Castle Wars Manual"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("Objective:", 38),
                    BookLine("The aim is to get into your", 39),
                    BookLine("opponents castle and take", 40),
                    BookLine("their team standard. Then", 41),
                    BookLine("bring that back and capture", 42),
                    BookLine("it on your teams standard. ", 43),
                ),
                Page(
                    BookLine("Toolkit:", 58),
                    BookLine("This useful item allows you", 59),
                    BookLine("to repair broken doors and", 60),
                    BookLine("catapults. Simply use it on", 61),
                    BookLine("the item to be repaired, or", 62),
                    BookLine("have one in your inventory", 63),
                    BookLine("when you select the option,", 64),
                    BookLine("and you'll rebuild it!", 65)
                )
            ),
            PageSet(
                Page(
                    BookLine("Bandages:", 43),
                    BookLine("These can be used to heal", 44),
                    BookLine("some health and restore some", 45),
                    BookLine("of your running energy.", 46),
                    BookLine("You can also use them to", 47),
                    BookLine("heal fellow players.", 48),
                ),
                Page(
                    BookLine("Explosive Potion:", 58),
                    BookLine("A simple but effective item,", 59),
                    BookLine("use it to blow up your", 60),
                    BookLine("opponents catapult and", 61),
                    BookLine("barricades! It can also be", 62),
                    BookLine("used to clear the tunnels", 63),
                    BookLine("under the arena for some,", 64),
                    BookLine("sneak attacks into your", 65),
                    BookLine("opponents castle!", 66)
                )
            ),
            PageSet(
                Page(
                    BookLine("Barricade:", 43),
                    BookLine("Use these constructs to block", 44),
                    BookLine("your opponents movement", 45),
                    BookLine("and prevent them accessing", 46),
                    BookLine("your castle. Each team can", 47),
                    BookLine("only have 10 built at any one", 48),
                    BookLine("time.", 49),
                ),
                Page(
                    BookLine("Bucket:", 58),
                    BookLine("Fill a bucket with water and", 59),
                    BookLine("you can use it to put out a", 60),
                    BookLine("burning catapult or barricade,", 61),
                    BookLine("but be quick or it'll be", 62),
                    BookLine("destroyed.", 63),
                )
            ),
            PageSet(
                Page(
                    BookLine("Tinderbox:", 43),
                    BookLine("Logs aren't all that's", 44),
                    BookLine("flammable, use a tinderbox to", 45),
                    BookLine("set light to your opponents", 46),
                    BookLine("You can also use them to", 47),
                    BookLine("catapult and barricades.", 48),
                ),
                Page(
                    BookLine("Pickaxe:", 58),
                    BookLine("Use a pickaxe to mine your", 59),
                    BookLine("way through the tunnels", 60),
                    BookLine("under the arena for a sneak", 61),
                    BookLine("attack into your opponents", 62),
                    BookLine("castle. Don't forget to", 63),
                    BookLine("collapse the tunnels into your", 64),
                    BookLine(" own castle though! ", 65),
                )
            ),
            PageSet(
                Page(
                    BookLine("Catapult:", 43),
                    BookLine("Use this war machine to", 44),
                    BookLine("launch rocks at your", 45),
                    BookLine("opponents. Just give it rough", 46),
                    BookLine("coordinates and let the rock", 47),
                    BookLine("fly, just be careful not to hit", 48),
                    BookLine("your team with it!", 49),
                ),
                Page(
                    BookLine("Rock:", 58),
                    BookLine("Used as ammo for the", 59),
                    BookLine("catapult, and not much else.", 60),
                    BookLine("Brings new meaning to the", 61),
                    BookLine("phrase 'flies like a rock'.", 62),
                )
            ),
        )
        private fun display(player: Player, pageNum: Int, buttonID: Int) : Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_2_27, TITLE, CONTENTS)
            BookInterface.clearModelsOnPage(player, BookInterface.FANCY_BOOK_2_27);
            BookInterface.setItemOnPage(player, 0, Items.TOOLKIT_4051, BookInterface.FANCY_BOOK_2_27, BookInterface.FANCY_BOOK_2_27_IMAGE_ENABLE_DRAW_IDS[17], BookInterface.FANCY_BOOK_2_27_IMAGE_DRAW_IDS[17], 768, 192, 1792)
            BookInterface.setItemOnPage(player, 1, Items.BANDAGES_4049, BookInterface.FANCY_BOOK_2_27, BookInterface.FANCY_BOOK_2_27_IMAGE_ENABLE_DRAW_IDS[2], BookInterface.FANCY_BOOK_2_27_IMAGE_DRAW_IDS[2], 768, 192, 1792)
            BookInterface.setItemOnPage(player, 1, Items.EXPLOSIVE_POTION_4045, BookInterface.FANCY_BOOK_2_27, BookInterface.FANCY_BOOK_2_27_IMAGE_ENABLE_DRAW_IDS[17], BookInterface.FANCY_BOOK_2_27_IMAGE_DRAW_IDS[17], 768, 192, 1792)
            BookInterface.setItemOnPage(player, 2, Items.BARRICADE_4053, BookInterface.FANCY_BOOK_2_27, BookInterface.FANCY_BOOK_2_27_IMAGE_ENABLE_DRAW_IDS[2], BookInterface.FANCY_BOOK_2_27_IMAGE_DRAW_IDS[2], 768, 192, 1792)
            BookInterface.setItemOnPage(player, 2, Items.BUCKET_OF_WATER_1929, BookInterface.FANCY_BOOK_2_27, BookInterface.FANCY_BOOK_2_27_IMAGE_ENABLE_DRAW_IDS[17], BookInterface.FANCY_BOOK_2_27_IMAGE_DRAW_IDS[17], 768, 192, 1792)
            BookInterface.setItemOnPage(player, 3, Items.TINDERBOX_590, BookInterface.FANCY_BOOK_2_27, BookInterface.FANCY_BOOK_2_27_IMAGE_ENABLE_DRAW_IDS[2], BookInterface.FANCY_BOOK_2_27_IMAGE_DRAW_IDS[2], 768, 192, 1792)
            BookInterface.setItemOnPage(player, 3, Items.BRONZE_PICKAXE_1265, BookInterface.FANCY_BOOK_2_27, BookInterface.FANCY_BOOK_2_27_IMAGE_ENABLE_DRAW_IDS[17], BookInterface.FANCY_BOOK_2_27_IMAGE_DRAW_IDS[17], 768, 192, 1792)
            BookInterface.setModelOnPage(player, 4, 38202, BookInterface.FANCY_BOOK_2_27, BookInterface.FANCY_BOOK_2_27_IMAGE_ENABLE_DRAW_IDS[4], BookInterface.FANCY_BOOK_2_27_IMAGE_DRAW_IDS[4], 4048, 192, 768)
            BookInterface.setItemOnPage(player, 4, Items.ROCK_4043, BookInterface.FANCY_BOOK_2_27, BookInterface.FANCY_BOOK_2_27_IMAGE_ENABLE_DRAW_IDS[17], BookInterface.FANCY_BOOK_2_27_IMAGE_DRAW_IDS[17], 768, 192, 1792)
            return true
        }
    }

    override fun defineListeners() {
        on(Items.CASTLEWARS_MANUAL_4055, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_2_27, ::display)
            return@on true
        }
    }
}