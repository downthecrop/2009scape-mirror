package rs09.game.interaction.item

import api.*
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.InteractionListener
import rs09.tools.END_DIALOGUE

class GodBookListeners : InteractionListener() {

    val GB_SARADOMIN = Items.HOLY_BOOK_3840
    val GB_ZAMORAK = Items.UNHOLY_BOOK_3842
    val GB_GUTHIX = Items.BOOK_OF_BALANCE_3844

    override fun defineListeners() {
        on(GB_SARADOMIN, ITEM, "preach"){player, _ ->
            openDialogue(player, HOLY_DIALOGUE(BOOK.SARA))
            return@on true
        }

        on(GB_ZAMORAK, ITEM, "preach"){ player, _ ->
            openDialogue(player, HOLY_DIALOGUE(BOOK.ZAM))
            return@on true
        }

        on(GB_GUTHIX, ITEM, "preach"){ player, _ ->
            openDialogue(player, HOLY_DIALOGUE(BOOK.GUTHIX))
            return@on true
        }
    }

    internal enum class BOOK {
        SARA,
        GUTHIX,
        ZAM
    }

    internal class HOLY_DIALOGUE(val book: BOOK) : DialogueFile(){

        val WEDDINGS = arrayOf(
            "In the name of Saradomin, protector of us all, I now join you in the eyes of Saradomin.",
            "Light and dark, day and night, balance arises from contrast. I unify thee in the name of Guthix.",
            "Two great warriors, joined by hand, to spread destruction across the land. In Zamorak's name, now two are one."
        )

        val LAST_RITES = arrayOf(
            "Thy cause was false, thy skills did lack; See you in Lumbridge when you get back.",
            "Thy death was not in vain, for it brought some balance to the world. May Guthix bring you rest.",
            "The weak deserve to die, so the strong may flourish. This is the creed of Zamorak."
        )

        val BLESSINGS = arrayOf(
            "Go in peace in the name of Saradomin; may his glory shine upon you like the sun.",
            "May you walk the path, and never fall, for Guthix walks beside thee on thy journey. May Guthix bring you peace.",
            "May your bloodthirst never be sated, and may all your battles be glorious. Zamorak bring you strength."
        )

        val PREACHINGS = arrayOf(
            arrayOf(
                "Protect your self, protect your friends. Mine is the glory that never ends.",
                "The darkness in life may be avoided, by the light of wisdom shining.",
                "Show love to your friends, and mercy to your enemies, and know that the wisdom of Saradomin will follow. ",
                "A fight begun, when the cause is just, will prevail over all others.",
                "The currency of goodness is honour; It retains its value through scarcity."
            ),
            arrayOf(
                "All things must end, as all begin; Only Guthix knows the role thou must play.",
                "In life, in death, in joy, in sorrow: May thine experience show thee balance.",
                "Thou must do as thou must, no matter what. Thine actions bring balance to this world.",
                "he river flows, the sun ignites, May you stand with Guthix in thy fights.",
                "A journey of a single step, May take thee over a thousand miles."
            ),
            arrayOf(
                "There is no opinion that cannot be proven true...by crushing those who choose to disagree with it.",
                "Battles are not lost and won; They simply remove the weak from the equation.",
                "Those who fight, then run away, shame Zamorak with their cowardice.",
                "Battle is by those who choose to disagree with it.",
                "Strike fast, strike hard, strike true: The strength of Zamorak will be with you."
            )
        )

        val PREACH_SARA_AMEN = "This is Saradomin's Wisdom."
        val PREACH_ZAM_AMEN = "Zamorak give me strength!"
        val PREACH_GUTH_AMEN = "May Guthix bring you balance."

        override fun handle(componentID: Int, buttonID: Int) {
            when(stage){
                0 -> options("Weddings","Last Rites","Blessings","Preaching").also { stage++ }
                1 -> when(buttonID){
                    1 -> say(player!!, WEDDINGS[book.ordinal], book, false)
                    2 -> say(player!!, LAST_RITES[book.ordinal], book, false)
                    3 -> say(player!!, BLESSINGS[book.ordinal], book, false)
                    4 -> say(player!!, PREACHINGS[book.ordinal].random(), book, true)
                    else -> {}
                }.also { end() }
            }
        }

        private fun say(player: Player, message: String, book: BOOK, preach: Boolean){
            val animation = when(book){
                BOOK.SARA -> Animation(1335)
                BOOK.ZAM -> Animation(1336)
                BOOK.GUTHIX -> Animation(1337)
            }

            val preachText = when(book){
                BOOK.SARA -> PREACH_SARA_AMEN
                BOOK.ZAM -> PREACH_ZAM_AMEN
                BOOK.GUTHIX -> PREACH_GUTH_AMEN
            }

            val lastTick = animationDuration(animation)

            lock(player, 100)
            animate(player, animation)

            submitIndividualPulse(player, object : Pulse() {
                var counter = 0
                override fun pulse(): Boolean {
                    when(counter++){
                        0 -> sendChat(player, message)
                        lastTick - 1 -> if(preach) sendChat(player, preachText)
                        lastTick -> unlock(player).also { return true }
                    }
                    return false
                }
            })
        }

    }
}