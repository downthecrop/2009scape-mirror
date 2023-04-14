package content.region.kandarin.quest.grandtree

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.book.Book
import core.game.dialogue.book.BookLine
import core.game.dialogue.book.Page
import core.game.dialogue.book.PageSet
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items

/**
 * Translation Book handler for The Grand Tree quest
 *
 *  @author crop & szu
 */
@Initializable
class TranslationBook : Book {

    constructor(player: Player?) : super(player, "Translation Book", Items.TRANSLATION_BOOK_784, *arrayOf(
        PageSet(
            Page(
                BookLine("Gnome", 55),
                BookLine("English translation", 56),
                BookLine("written by Anita", 57),
                BookLine("", 58),
                BookLine("This text contains", 59),
                BookLine("the ancient Gnome words", 60),
                BookLine("I have managed to", 61),
                BookLine("translate thus far.", 62),
                BookLine("", 63),
            ),
            Page(
                BookLine("-A-", 66),
                BookLine("arpos: rocks", 67),
                BookLine("ando: gate", 68),
                BookLine("andra: city", 69),
                BookLine("ataris: cow", 70),
                BookLine("-C-", 71),
                BookLine("cef: threat", 72),
                BookLine("cheray: lazy", 73),
                BookLine("Cinqo: King", 74),
                BookLine("cretor: bucket", 75),
            )
        ),
        PageSet(
            Page(
                BookLine("-E-", 55),
                BookLine("eis: me", 56),
                BookLine("es: a", 57),
                BookLine("et: and", 58),
                BookLine("eto: will", 59),
                BookLine("-G-", 60),
                BookLine("gandius: jungle", 61),
                BookLine("Gal: All", 62),
                BookLine("gentis: leaf", 63),
                BookLine("gutus: banana", 64),
                BookLine("gomondo: branch", 65),
            ),
            Page(
                BookLine("-H-", 66),
                BookLine("har: old", 67),
                BookLine("harij: harpoon", 68),
                BookLine("hewo: grass", 69),
                BookLine("", 70),
                BookLine("-I-", 71),
                BookLine("ip: you", 72),
                BookLine("imindus: quest", 73),
                BookLine("irno: translate", 74),
                BookLine("", 75),
            )
        ),
        PageSet(
            Page(
                BookLine("-K-", 55),
                BookLine("kar: no", 56),
                BookLine("kai: boat", 57),
                BookLine("ko: sail", 58),
                BookLine("", 59),
                BookLine("-L-", 60),
                BookLine("lauf: eye", 61),
                BookLine("laquinay: common sense", 62),
                BookLine("lemanto: man", 63),
                BookLine("lemantolly: stupid man", 64),
                BookLine("lovos: gave", 65),
            ),
            Page(
                BookLine("-M-", 66),
                BookLine("meriz: kill", 67),
                BookLine("mina: time(s)", 68),
                BookLine("mos: coin", 69),
                BookLine("mi: I", 70),
                BookLine("mond: seal", 71),
                BookLine("", 72),
            )
        ),
        PageSet(
            Page(
                BookLine("-P-", 55),
                BookLine("por: long", 56),
                BookLine("prit: with", 57),
                BookLine("priw: tree", 58),
                BookLine("pro: to", 59),
                BookLine("", 60),
                BookLine("-Q-", 61),
                BookLine("Qui: guard", 62),
                BookLine("Quir: guardian", 63),
                BookLine("-R-", 64),
                BookLine("rentos: agility", 65),
            ),
            Page(
                BookLine("-S-", 66),
                BookLine("sarko: Begone", 67),
                BookLine("sind: big", 68),
                BookLine("", 69),
                BookLine("-T-", 70),
                BookLine("ta: the", 71),
                BookLine("tuzo: open", 72),
                BookLine("-U-", 73),
                BookLine("Undri: lands", 74),
                BookLine("Umesco: Soul", 75),
            )
        )
    )
    ) {}

    constructor() {
        /**
         * empty.
         */
    }

    override fun finish() {
    }

    override fun display(set: Array<Page>) {
        player.lock()
        player.interfaceManager.open(getInterface())

        for (i in 55..76) {
            player.packetDispatch.sendString("", getInterface().id, i)
        }
        player.packetDispatch.sendString("", getInterface().id, 77)
        player.packetDispatch.sendString("", getInterface().id, 78)
        player.packetDispatch.sendString(getName(), getInterface().id, 6)
        for (page in set) {
            for (line in page.lines) {
                player.packetDispatch.sendString(line.message, getInterface().id, line.child)
            }
        }
        player.packetDispatch.sendInterfaceConfig(getInterface().id, 51, index < 1)
        val lastPage = index == sets.size - 1
        player.packetDispatch.sendInterfaceConfig(getInterface().id, 53, lastPage)
        if (lastPage) {
            finish()
        }
        player.unlock()
    }

    override fun newInstance(player: Player): DialoguePlugin {
        return TranslationBook(player)
    }

    override fun getIds(): IntArray {
        // This is all handled in `BookreadOption.java` you need to manually
        // register a new dialogue key for your file there.
        return intArrayOf(49610763)
    }
}