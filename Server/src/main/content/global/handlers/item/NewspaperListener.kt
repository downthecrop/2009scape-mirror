package content.global.handlers.item

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Items

/**
 * Interaction and interface listener for the Varrock newspaper.
 * This handles component(530) globally.
 * youtu.be/ePVNOiSzOS4
 */
class NewspaperListener : InteractionListener {
    companion object {

        const val NEWSPAPER_INTERFACE_530 = 530 /* Should be in org.rs09.consts.Components but isn't. */

        val leftPage = "" +
                "Varrock gets " +
                "Makeover" +
                "<br><br>" +
                "The city of Varrock " +
                "is the latest recipient " +
                "of a complete " +
                "makeover. When " +
                "interviewed, King " +
                "Roald said, 'In order " +
                "to keep visitors " +
                "coming to see the " +
                "sights of our " +
                "beautiful capital, we " +
                "felt that tidying-up " +
                "the city would be " +
                "more effective than " +
                "just issuing a decree " +
                "- make sure you visit " +
                "the new museum " +
                "while you are here.'"

        val rightPage = "" +
                "Obituaries " +
                "<br><br>" +
                "Goblin-Died<br>" +
                "Giant Rat-Died<br>" +
                "Unicorn-Died<br>" +
                "Varrock Guard-Died<br>" +
                "Varrock Guard-Died<br>" +
                "Varrock Guard-Died<br>" +
                "Bear-Died." +
                "<br><br>" +
                "Classifieds" +
                "<br><br>" +
                "Lowe's Archery " +
                "Emporium for the " +
                "finest ranging " +
                "weapons in town!" +
                "<br><br>" +
                "Time to party! Visit " +
                "the Fancy Dress " +
                "Shop for all your " +
                "party outfits." +
                "<br><br>" +
                "The Dancing " +
                "Donkey - cold beer " +
                "always in stock."
    }
    override fun defineListeners() {
        on(Items.NEWSPAPER_11169, IntType.ITEM, "read") { player, _ ->
            openInterface(player, NEWSPAPER_INTERFACE_530)
            setInterfaceText(player, leftPage ,NEWSPAPER_INTERFACE_530, 7)
            setInterfaceText(player, rightPage ,NEWSPAPER_INTERFACE_530, 8)
            return@on true
        }

        on(Items.AL_KHARID_FLYER_7922, IntType.ITEM, "read") { player, _ ->
            sendDialogueLines(player, "Come to the Al Kharid Market place! High quality", "produce at low, low prices! Show this flyer to a", "merchant for money off your next purchase,", "courtesy of Ali Morrisane!")
            return@on true
        }
    }
}
