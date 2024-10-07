package content.region.misthalin.varrock.dialogue.surok

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
class AbyssalBook : InteractionListener {
    companion object {
        private val TITLE = "Abyssal book"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("A Compendium of Research", 55),
                    BookLine("Into Chaotic Space.", 56),
                    BookLine("Author Unknown", 57),
                    BookLine("", 58),
                    BookLine("The strange dimension that", 59),
                    BookLine("we have name 'Abyssal Space'", 60),
                    BookLine("is something of an enigma.", 61),
                    BookLine("It was first discovered during", 62),
                    BookLine("a routine teleportation", 63),
                    BookLine("experiment that seemingly", 64),
                    BookLine("went wrong. We are still", 65),
                ),
                Page(
                    BookLine("not sure as to what caused", 66),
                    BookLine("this teleportation failure,", 67),
                    BookLine("but the discovery of a", 68),
                    BookLine("previously unknown dimension", 69),
                    BookLine("led to a flurry of", 70),
                    BookLine("research from the", 71),
                    BookLine("Zamorakian Magical Institute", 72),
                    BookLine("(henceforth known within", 73),
                    BookLine("(this document as the Z.M.I.).", 74),
                )
            ),
            PageSet(
                Page(
                    BookLine("Under direct orders to examine", 55),
                    BookLine("this dimension, I feel I can", 56),
                    BookLine("accurately state the", 57),
                    BookLine("following conclusions.", 58),
                    BookLine("", 59),
                    BookLine("<col=FF0000>Conclusion One</col>", 60),
                    BookLine("", 61),
                    BookLine("Abyssal Space is not", 62),
                    BookLine("a dimension in the way", 63),
                    BookLine("that we understand the term", 64),
                    BookLine("from examples such as", 65),
                ),
                Page(
                    BookLine("Zanaris or Feneskrae.", 66),
                    BookLine("", 67),
                    BookLine("Rather, it is the name", 68),
                    BookLine("we have given the dimension", 69),
                    BookLine("that exists between other", 70),
                    BookLine("more developed dimensions", 71),
                )
            ),
            PageSet(
                Page(
                    BookLine("- the 'glue' that keeps", 55),
                    BookLine("each dimension together yet", 56),
                    BookLine("separate, if you will.", 57),
                    BookLine("", 58),
                    BookLine("The Abyssal space's existence", 59),
                    BookLine("at the 'fringe' of reality,", 60),
                    BookLine("means that it does not", 61),
                    BookLine("confirm to the same guidelines", 62),
                    BookLine("of space and time as Gielinor", 63),
                    BookLine("does; you may enter it and", 64),
                    BookLine("then leave it from an", 65),
                ),
                Page(
                    BookLine("identical spot, yet", 66),
                    BookLine("", 67),
                    BookLine("reappear many hundreds of", 68),
                    BookLine("miles at your target", 69),
                    BookLine("destination", 70),
                    BookLine("(the 'teleportation'", 71),
                    BookLine("phenomenon that we use daily).", 72),
                )
            ),
            PageSet(
                Page(
                    BookLine("From this basic concept,", 55),
                    BookLine("I have extrapolated that", 56),
                    BookLine("all teleport magics in", 57),
                    BookLine("fact use Abyssal Space", 58),
                    BookLine("to make the passing of great", 59),
                    BookLine("distances occur in", 60),
                    BookLine("a very short space", 61),
                    BookLine("of time, from our perception.", 62),
                    BookLine("", 63),
                    BookLine("What is actually happening,", 64),
                    BookLine("is that the spellcaster is", 65),
                ),
                Page(
                    BookLine("entering abyssal space,", 66),
                    BookLine("and then immediately", 67),
                    BookLine("leaving again,", 68),
                    BookLine("with certain values as to speed", 69),
                    BookLine("and direction being taken care", 70),
                    BookLine("of in our spellingcasting to", 71),
                    BookLine("allow some degree of precision", 72),
                    BookLine("in these teleports.", 73),
                )
            ),
            PageSet(
                Page(
                    BookLine("More worryingly,", 55),
                    BookLine("it seems apparent that", 56),
                    BookLine("the barriers between", 57),
                    BookLine("our dimensional space", 58),
                    BookLine("and abyssal space have", 59),
                    BookLine("become somewhat weakened", 60),
                    BookLine("through excessive use", 61),
                    BookLine("There have been isolated", 62),
                    BookLine("reports within the Z.M.I.", 63),
                    BookLine("that creatures not native", 64),
                    BookLine("to our dimension have entered", 65),
                ),
                Page(
                    BookLine("Gielinor through", 66),
                    BookLine("abyssal space,", 67),
                    BookLine("as well as the teleportation", 68),
                    BookLine("malfunction that first", 69),
                    BookLine("resulted in our discovery", 70),
                    BookLine("of this dimension.", 71),
                )
            ),
            PageSet(
                Page(
                    BookLine("I strongly recommend that", 55),
                    BookLine("further research is taken", 56),
                    BookLine("- if the barriers between", 57),
                    BookLine("these dimensions are", 58),
                    BookLine("sufficiently weakened,", 59),
                    BookLine("there may exist the", 60),
                    BookLine("possibility of an alternative", 61),
                    BookLine("method to proceed", 62),
                    BookLine("with Operation:", 63),
                    BookLine("Transient without altering", 64),
                    BookLine("the other deities to our plans.", 65),
                ),
                Page(
                    BookLine("<col=FF0000>Conclusion Two</col>", 66),
                    BookLine("", 67),
                    BookLine("When we have accepted", 68),
                    BookLine("that Abyssal Space is", 69),
                    BookLine("somewhat of a tesseract", 70),
                    BookLine("or hypercube with a direct", 71),
                    BookLine("relation to our dimension,", 72),
                    BookLine("then the benefits of", 73),
                    BookLine("exploiting this resource", 74),
                    BookLine("become more obvious.", 75),
                )
            ),
            PageSet(
                Page(
                    BookLine("I ran some experiments", 55),
                    BookLine("with Sample XJ13", 56),
                    BookLine("(also known as 'rune essence')", 57),
                    BookLine("and managed to place six", 58),
                    BookLine("parts in a space that would", 59),
                    BookLine("seemingly only hold one.", 60),
                    BookLine("Continued", 61),
                    BookLine("experimentation with these", 62),
                    BookLine("stolen samples showed that", 63),
                    BookLine("moving items between our", 64),
                    BookLine("dimension and abyssal space", 65),
                ),
                Page(
                    BookLine("degraded the use of these", 66),
                    BookLine("pouches, but a simple", 67),
                    BookLine("transfiguration spell", 68),
                    BookLine("when cast within the abyss", 69),
                    BookLine("upon these pouches restored", 70),
                    BookLine("their usage back to", 71),
                    BookLine("the original results.", 72),
                )
            ),
            PageSet(
                Page(
                    BookLine("Should we ever locate the", 55),
                    BookLine("source of these 'essence'", 56),
                    BookLine("that the Wizard Tower", 57),
                    BookLine("seem to have an endless", 58),
                    BookLine("supply of, I would strongly", 59),
                    BookLine("recommend the harvesting of", 60),
                    BookLine("these creatures for their", 61),
                    BookLine("organs so as to maximize", 62),
                    BookLine("the efficiency of our", 63),
                    BookLine("rune manufacturing process.", 64),
                ),
                Page(
                    BookLine("Some degree of caution will", 66),
                    BookLine("be necessary, as the creatures", 67),
                    BookLine("the creatures of the abyss", 68),
                    BookLine("are seemingly very aggressive.", 69),
                ),
            ),
            PageSet(
                Page(
                    BookLine("<col=FF0000>Conclusion Three</col>", 55),
                    BookLine("", 56),
                    BookLine("Our first discovery of", 57),
                    BookLine("Abyssal Space was somewhat", 58),
                    BookLine("of a fluke - and a not easily", 59),
                    BookLine("repeatable fluke at that.", 60),
                    BookLine("It proved exceedingly", 61),
                    BookLine("difficult to find the", 62),
                    BookLine("correct mystical resonance", 63),
                    BookLine("for this dimension, due to", 64),
                    BookLine("my original belief that", 65),
                ),
                Page(
                    BookLine("Abyssal space is not a fully", 66),
                    BookLine("fledged dimension of its own,", 67),
                    BookLine("so we have had to resort", 68),
                    BookLine("to unusual measures to", 69),
                    BookLine("gain permanent access", 70),
                    BookLine("to this realm.", 71),
                ),
            ),
            PageSet(
                Page(
                    BookLine("We took a large number of", 55),
                    BookLine("initiates, and gave them", 56),
                    BookLine("each supplies to cast", 57),
                    BookLine("a portal spell. We then", 58),
                    BookLine("had them repeatedly teleport", 59),
                    BookLine("to various locations,", 60),
                    BookLine("seeking to replicate the", 61),
                    BookLine("original error that", 62),
                    BookLine("caused the first entry", 63),
                    BookLine("into Abyssal Space.", 64),
                    BookLine("Once one of our initiates", 65),
                ),
                Page(
                    BookLine("had managed to 'fail' his", 66),
                    BookLine("teleport and appear in", 67),
                    BookLine("Abyssal Space. he was", 68),
                    BookLine("then charged with remaining", 69),
                    BookLine("there and holding a portal", 70),
                    BookLine("spell open, so that more", 71),
                    BookLine("senior members of the Z.M.I.", 72),
                ),
            ),
            PageSet(
                Page(
                    BookLine("could gain entry via his portal.", 55),
                    BookLine("This initiate is still there,", 56),
                    BookLine("and due to the intense", 57),
                    BookLine("concentration required", 58),
                    BookLine("to keep he portal open,", 59),
                    BookLine("it is my recommendation", 60),
                    BookLine("that we leave him there", 61),
                    BookLine("holding the bridge open for us.", 62),
                    BookLine("As an initiate, he is", 63),
                    BookLine("always expendable should", 64),
                    BookLine("something go wrong, and", 65),
                ),
                Page(
                    BookLine("the slow passage of", 66),
                    BookLine("time within Abyssal Space", 67),
                    BookLine("means we don't need to", 68),
                    BookLine("worry about feeding", 69),
                    BookLine("him or anything.", 70),
                    BookLine("At the time of writing,", 71),
                    BookLine("this portal is still active,", 72),
                    BookLine("and will allow us to teleport", 73),
                    BookLine("people at will into Abyssal Space.", 74),
                    BookLine("The only downside to this", 75),
                    BookLine("method of teleportation", 76),
                ),
            ),
            PageSet(
                Page(
                    BookLine("is that we are using", 55),
                    BookLine("magic provided to us", 56),
                    BookLine("by our Lord Zamorak himself,", 57),
                    BookLine("so anybody who uses this", 58),
                    BookLine("teleport will inevitably be", 59),
                    BookLine("marked by him - or become", 60),
                    BookLine("'skulled' as the common", 61),
                    BookLine("folk put it.", 62),
                    BookLine("An interesting side-effect", 63),
                    BookLine("of this portal, is that", 64),
                    BookLine("various teleports within Abyssal", 65),
                ),
                Page(
                    BookLine("Space were opened up by", 66),
                    BookLine("it's casting. These teleports", 67),
                    BookLine("seem to lead to mysterious", 68),
                    BookLine("temples dedicated to various", 69),
                    BookLine("magical elements, which I", 70),
                    BookLine("believe are directly related", 71),
                    BookLine("to the rumours we have", 72),
                ),
            ),
            PageSet(
                Page(
                    BookLine("intercepted of the rediscovery", 55),
                    BookLine("of Runecrafting by the", 56),
                    BookLine("Wizards Tower. Sadly, we", 57),
                    BookLine("must conclude from these", 58),
                    BookLine("temples that the rumours are", 59),
                    BookLine("indeed true, and that the", 60),
                    BookLine("destruction of the Wizards", 61),
                    BookLine("Tower had been in vain,", 62),
                    BookLine("as was the sacrifice of", 63),
                    BookLine("those who died to try", 64),
                    BookLine("and prevent the meddling", 65),
                ),
                Page(
                    BookLine("Saradominists from gaining", 66),
                    BookLine("access to the creation of", 67),
                    BookLine("magical runes.", 68),
                    BookLine("I have detailed my findings", 69),
                    BookLine("relating to RuneCrafting", 70),
                    BookLine("in a separate document, and", 71),
                    BookLine("passed it on to my superiors,", 72),
                    BookLine("along with me recommendations", 73),
                    BookLine("on how best to thwart their", 74),
                    BookLine("research further.", 75),
                )
            ),
            PageSet(
                Page(
                    BookLine("Until a final decision is taken,", 55),
                    BookLine("I suggest we make the best", 56),
                    BookLine("of a bad situation,", 57),
                    BookLine("and increase our own rune", 58),
                    BookLine("production to full", 59),
                    BookLine("manufacturing capabilities.", 60),
                    BookLine("", 61),
                    BookLine("I have already ordered buyers", 62),
                    BookLine("to purchase as much", 63),
                    BookLine("Sample XJ13", 64),
                    BookLine("as can be bought, and to", 65),
                ),
                Page(
                    BookLine("hire some mercenaries to", 66),
                    BookLine("sabotage the research efforts", 67),
                    BookLine("of the Wizards Tower, or", 68),
                    BookLine("failing that to provide us", 69),
                    BookLine("some insight into supplies", 70),
                    BookLine("where their study of these", 71),
                    BookLine("essence are coming from.", 72),
                    BookLine("", 73),
                    BookLine("Until my next report, I", 74),
                    BookLine("remain as ever a loyal servant.", 75),
                    BookLine("Strength Through Chaos!", 76),
                )
            ),
        )
    }

    private fun display(player: Player, pageNum: Int, buttonID: Int): Boolean {
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
        return true
    }

    override fun defineListeners() {
        on(Items.ABYSSAL_BOOK_5520, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}