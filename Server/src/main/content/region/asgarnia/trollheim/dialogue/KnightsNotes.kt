package content.region.asgarnia.trollheim.dialogue

import content.global.handlers.iface.ScrollInterface
import content.global.handlers.iface.ScrollLine
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Items
import org.rs09.consts.Components

/**
 * @author szu
 */
class KnightsNotes : InteractionListener {
    companion object {
        val CONTENTS = arrayOf(
            ScrollLine("My friend, you were right to send me to investigate the",3),
            ScrollLine("dwarf's drunken claims, for I have discovered a treasure",4),
            ScrollLine("beyond our wildest dreams. The aviantese are alive, and I",5),
            ScrollLine("suspect they still guard the godsword! Beneath the remnants",6),
            ScrollLine("of the temple a great battle is being fought between followers",7),
            ScrollLine("of Bandos, Armadyl, Saradomin and Zamorak. My command was",8),
            ScrollLine("slaughtered and I am grieviously wounded. YOU MUST PREVENT",9),
            ScrollLine("THE GODSWORD FROM FALLING INTO THE WRONG HANDS! I do not",10),
            ScrollLine("know how I am going to get this message to you, why is that",11),
            ScrollLine("talking skull never around when he's needed? Your comrade,",12),
            ScrollLine("Sir Gerry.",13),
        )
    }

    override fun defineListeners() {
        // The scroll has not been opened. This will lead to a different dialogue with Sir Tiffy Cashien.
        on(Items.KNIGHTS_NOTES_11734, IntType.ITEM, "read") { player, _ ->
            sendDialogueOptions(player, "The scroll is sealed. Do you still want to open it?", "Yes", "No")
            player!!.packetDispatch.sendInterfaceConfig(228, 6, true)
            player!!.packetDispatch.sendInterfaceConfig(228, 9, false) // childId 7 is left 8 is right
            addDialogueAction(player) { player, button ->
                if (button == 2) {
                    sendMessage(player, "You break the wax seal and open the scroll.")
                    if (removeItem(player, Items.KNIGHTS_NOTES_11734)) {
                        addItem(player, Items.KNIGHTS_NOTES_11735)
                        ScrollInterface.scrollSetup(player, Components.MESSAGESCROLL_220, CONTENTS)
                    }
                }
                return@addDialogueAction
            }
            return@on true
        }
        // The scroll has been read. This will lead to a different dialogue with Sir Tiffy Cashien.
        on(Items.KNIGHTS_NOTES_11735, IntType.ITEM, "read") { player, _ ->
            ScrollInterface.scrollSetup(player, Components.MESSAGESCROLL_220, CONTENTS)
            return@on true
        }
    }
}