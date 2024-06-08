package content.region.asgarnia.falador.handlers

import content.global.handlers.iface.ScrollInterface
import content.global.handlers.iface.ScrollLine
import core.game.interaction.InteractionListener
import org.rs09.consts.Components
import org.rs09.consts.Scenery

class BankNotices : InteractionListener {
    companion object {
        val CONTENTS_PIN = arrayOf(
                ScrollLine("If you're worried about someone stealing items from your",3),
                ScrollLine("bank, why not protect yourself with a Bank PIN?",4),

                ScrollLine("A Bank PIN is a four-digit number. It's like a password that",6),
                ScrollLine("protects your bank account. If you set one, you'll be asked to",7),
                ScrollLine("enter it before you can take items out of the bank.",8),

                ScrollLine("If you're interested, speak to the bankers and ask about Bank",10),
                ScrollLine("PINs.",11),

                ScrollLine("But remember, KEEP YOUR PIN SECRET!",13),
        )

        val CONTENTS_PASSWORD = arrayOf(
                ScrollLine("The Bank of RuneScape would like to remind customers that",4),
                ScrollLine("they should NEVER tell ANYONE their password.",5),

                ScrollLine("If someone asks you to say your password, please report",7),
                ScrollLine("them using the 'Report Abuse' button at the bottom of your",8),
                ScrollLine("screen. ",9),

                ScrollLine("Change your password regularly, and make sure no-one else",11),
                ScrollLine("could easily guess it!",12),
        )
    }

    override fun defineListeners() {
        on(Scenery.NOTICEBOARD_11755, SCENERY, "read") { player, _ ->
            ScrollInterface.scrollSetup(player, Components.MESSAGESCROLL_220, CONTENTS_PIN)
            return@on true
        }
        on(Scenery.NOTICEBOARD_11756, SCENERY, "read") { player, _ ->
            ScrollInterface.scrollSetup(player, Components.MESSAGESCROLL_220, CONTENTS_PASSWORD)
            return@on true
        }
    }
}