package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.ServerConstants
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs09.consts.Items

/**
 * Goblin Book
 * https://youtu.be/6tdNqNa4zGw?t=155 April 23, 2009
 * @author ovenbreado
 */
class GoblinBook : InteractionListener {
    companion object {
        private const val RED = "<col=8A0808>"
        private val TITLE = "The Book of the Big High War God"
        private val CONTENTS = arrayOf(
                PageSet(
                        Page(
                                BookLine(RED + "The Creation of the Goblins", 97),
                                BookLine("", 68),
                                BookLine("In beginning all gods have", 69),
                                BookLine("big war. Each god find army", 70),
                                BookLine("to fight for him.", 71),
                                BookLine("", 72),
                                BookLine("Big High War God not have", 73),
                                BookLine("army. Big High War God go", 74),
                                BookLine("to beardy-short-people and", 75),
                                BookLine("ask, Will you fight in my", 76),
                                BookLine("army? But beardy-short-", 77),
                                BookLine("people say, No, we fight for", 78),
                                BookLine("God of Shiny Light.", 79),
                                BookLine("", 80),
                                BookLine("Then Big High War God go", 81),
                        ),
                        Page(
                                BookLine("to demons and ask, Will you", 82),
                                BookLine("fight in my army? But", 83),
                                BookLine("demons say, No, we fight for", 84),
                                BookLine("God of Dark Fire.", 85),
                                BookLine("", 86),
                                BookLine("Then Big High War God go", 87),
                                BookLine("to tall people with keen blades", 88),
                                BookLine("and say, Will you fight in", 89),
                                BookLine("my army? But tall people", 90),
                                BookLine("with keen blades say, No,", 91),
                                BookLine("some of us fight for God of", 92),
                                BookLine("Shiny Light and some of us", 93),
                                BookLine("fight for God of Dark Fire,", 94),
                                BookLine("but none of us fight for you!", 95),
                                BookLine("", 96),
                        ),
                ),
                PageSet(
                        Page(
                                BookLine("Big High War God very sad.", 97),
                                BookLine("He travel east and west,", 68),
                                BookLine("north and south, across land", 69),
                                BookLine("looking for army to fight for", 70),
                                BookLine("him.", 71),
                                BookLine("", 72),
                                BookLine("Then goblins say, We fight", 73),
                                BookLine("for you! At that time goblins", 74),
                                BookLine("very weak, very small, soft", 75),
                                BookLine("skin. Not like goblins today!", 76),
                                BookLine("But Big High War God say,", 77),
                                BookLine("I will make you my army.", 78),
                                BookLine("So Big High War God train", 79),
                                BookLine("goblins so they very strong.", 80),
                                BookLine("He give them good armour", 81),
                        ),
                        Page(
                                BookLine("so they not be harmed. He", 82),
                                BookLine("make them strong in spirit so", 83),
                                BookLine("they not afraid of battle. He", 84),
                                BookLine("give them commanders so", 85),
                                BookLine("they know which way to go.", 86),
                                BookLine("He divide goblins into twelve", 87),
                                BookLine("tribes and send them into", 88),
                                BookLine("battle!", 89),
                                BookLine("", 90),
                                BookLine("Goblin armies fight very", 91),
                                BookLine("good! When other gods see", 92),
                                BookLine("this, they very jealous. So", 93),
                                BookLine("they say, We want goblins to", 94),
                                BookLine("fight for us too! So Big High", 95),
                                BookLine("War God take some tribes", 96),
                        ),
                ),
                PageSet(
                        Page(
                                BookLine("and sell them, one to God of", 97),
                                BookLine("Shiny Light and one to God", 68),
                                BookLine("of Dark Fire, and other", 69),
                                BookLine("tribes to other gods. But", 70),
                                BookLine("most still worship Big High", 71),
                                BookLine("War God who created them!", 72),
                                BookLine("", 73),
                                BookLine("", 74),
                                BookLine("", 75),
                                BookLine("", 76),
                                BookLine("", 77),
                                BookLine("", 78),
                                BookLine("", 79),
                                BookLine("", 80),
                                BookLine("", 81),
                        ),
                        Page(
                                BookLine(RED + "The Several Commandments", 82),
                                BookLine("", 83),
                                BookLine("These are commands of Big", 84),
                                BookLine("High War God! Obey all", 85),
                                BookLine("commands all time or Big", 86),
                                BookLine("High War God kill you very", 87),
                                BookLine("bad!", 88),
                                BookLine("", 89),
                                BookLine("Always to slay enemies of", 90),
                                BookLine("Big High War God. Enemies", 91),
                                BookLine("must die!", 92),
                                BookLine("", 93),
                                BookLine("Not to run from battle.", 94),
                                BookLine("Cowards must die!", 95),
                                BookLine("", 96),
                        ),
                ),
                PageSet(
                        Page(
                                BookLine("Not to show mercy. Merciful", 97),
                                BookLine("must die!", 68),
                                BookLine("", 69),
                                BookLine("Not to doubt Big High War", 70),
                                BookLine("God. Doubters must die!", 71),
                                BookLine("", 72),
                                BookLine("Not to make own plans.", 73),
                                BookLine("Thinkers must die!", 74),
                                BookLine("", 75),
                                BookLine("", 76),
                                BookLine("", 77),
                                BookLine("", 78),
                                BookLine("", 79),
                                BookLine("", 80),
                                BookLine("", 81),
                        ),
                        Page(
                                BookLine(RED + "The end of the war and the", 82),
                                BookLine(RED + "prophecy", 83),
                                BookLine("", 84),
                                BookLine("The war of gods last many", 85),
                                BookLine("lifetimes. Battle is glorious", 86),
                                BookLine("and many heroes live and", 87),
                                BookLine("die! Then all gods leave", 88),
                                BookLine("world, leave their armies", 89),
                                BookLine("behind. But goblins still", 90),
                                BookLine("soldiers, still fight! Goblins", 91),
                                BookLine("fight against tall people with", 92),
                                BookLine("keen blades. But tall people", 93),
                                BookLine("build cities with walls, they", 94),
                                BookLine("not want to fight. They not", 95),
                                BookLine("true soldiers like goblins! But", 96),
                        ),
                ),
                PageSet(
                        Page(
                                BookLine("now goblins not have enough", 97),
                                BookLine("to eat, and have no", 68),
                                BookLine("commanders to tell them who", 69),
                                BookLine("to fight. So goblin tribes fight", 70),
                                BookLine("one another.", 71),
                                BookLine("", 72),
                                BookLine("At last all goblin tribes have", 73),
                                BookLine("big battle on plain of mud.", 74),
                                BookLine("Battle last many days and", 75),
                                BookLine("many goblins die, battle is", 76),
                                BookLine("glorious! But goblin corpses", 77),
                                BookLine("cover the ground and it look", 78),
                                BookLine("like all die. ", 79),
                                BookLine("", 80),
                                BookLine("Then Hopespear of the", 81),
                        ),
                        Page(
                                BookLine("Narogoshunn tribe have", 82),
                                BookLine("vision of Big High War God.", 83),
                                BookLine("In night while soldiers rest he", 84),
                                BookLine("call leaders of all tribes", 85),
                                BookLine("together to give them", 86),
                                BookLine("message.", 87),
                                BookLine("", 88),
                                BookLine("This is the word of Big High", 89),
                                BookLine("War God: Battle is indeed", 90),
                                BookLine("glorious and goblins are", 91),
                                BookLine("soldiers but if there too", 92),
                                BookLine("much battle all goblins die!", 93),
                                BookLine("No more must goblin fight", 94),
                                BookLine("against goblin or tribe against", 95),
                                BookLine("tribe. Goblins must find other", 96),
                        ),
                ),
                PageSet(
                        Page(
                                BookLine("enemies to fight, but not fight", 97),
                                BookLine("each other!", 68),
                                BookLine("", 69),
                                BookLine("And this is the word of Big", 70),
                                BookLine("High War God: Today I", 71),
                                BookLine("cannot lead you, but", 72),
                                BookLine("someday I will send a new", 73),
                                BookLine("Commander to lead you.", 74),
                                BookLine("Under new Commander", 75),
                                BookLine("goblins will conquer all of", 76),
                                BookLine(ServerConstants.SERVER_NAME + ", every race and", 77),
                                BookLine("every god! And then Big", 78),
                                BookLine("High War God will return", 79),
                                BookLine("and sit on throne of bronze", 80),
                                BookLine("and rule over all. War will", 81),
                        ),
                        Page(
                                BookLine("end in victory and victory", 82),
                                BookLine("will last forever!", 83),
                                BookLine("", 84),
                                BookLine("So leaders of tribes stop", 85),
                                BookLine("battle. And on plain of mud", 86),
                                BookLine("all tribes build temple to Big", 87),
                                BookLine("High War God and offer", 88),
                                BookLine("sacrifices.", 89),
                                BookLine("", 90),
                                BookLine("", 91),
                                BookLine("", 92),
                                BookLine("", 93),
                                BookLine("", 94),
                                BookLine("", 95),
                                BookLine("", 96),
                        ),
                ),
        )
        private fun display(player: Player, pageNum: Int, buttonID: Int) : Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_26, TITLE, CONTENTS)
            return true
        }
    }

    override fun defineListeners() {
        on(Items.GOBLIN_BOOK_10999, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_26, ::display)
            return@on true
        }
    }
}