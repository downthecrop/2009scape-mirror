package content.region.karamja.shilo.dialogue

import content.global.handlers.iface.ScrollInterface
import content.global.handlers.iface.ScrollLine
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Components
import org.rs09.consts.Items

class TatteredScroll : InteractionListener {
    // Receive during the Shilo Village quest.
    companion object {
        private const val SCROLL_INTERFACE = Components.MESSAGESCROLL_220
        val TATTERED_SCROLL_TEXT = arrayOf(
            ScrollLine("Bervirius, son of King Danthalas, was killed in battle. His",3),
            ScrollLine("devout Mother Rashiliyia was so heartbroken that she swore",4),
            ScrollLine("fealty to Zamorak if he would return her son to her. Bervirius",5),
            ScrollLine("returned as an undead creature and terrorized the King and",6),
            ScrollLine("Queen. Many guards died fighting the Undead Bervirius.",7),
            ScrollLine("Eventually the undead Bervirius was set on fire and soon only",8),
            ScrollLine("the bones remained.His remains were taken far to the South,",9),
            ScrollLine("and then towards the setting sun to a tomb that is surrounded",10),
            ScrollLine("by and level with the sea. The only remedy for containing the",11),
            ScrollLine("spirits of witches and undead.",12),
        )
    }

    override fun defineListeners() {
        on(Items.TATTERED_SCROLL_607, IntType.ITEM, "read") { player, _ ->
            ScrollInterface.scrollSetup(player, SCROLL_INTERFACE, TATTERED_SCROLL_TEXT)
            return@on true
        }
    }
}