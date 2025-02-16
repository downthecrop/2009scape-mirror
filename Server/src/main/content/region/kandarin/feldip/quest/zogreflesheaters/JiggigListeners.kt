package content.region.kandarin.feldip.quest.zogreflesheaters

import core.api.*
import core.game.dialogue.FacialExpression
import core.game.interaction.InteractionListener
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

class JiggigListeners : InteractionListener {
    override fun defineListeners() {
        on(Scenery.OGRE_COFFIN_6848, SCENERY, "open") { player, node ->
            // https://youtu.be/HnRcW2iM8es
            replaceScenery(node as core.game.node.scenery.Scenery, Scenery.OGRE_COFFIN_6890, 10)
            return@on true
        }

        on(NPCs.UGLUG_NAR_2039, NPC, "trade") { player, node ->
            if (getAttribute(player, ZogreFleshEaters.attributeOpenUglugNarShop, false)) {
                openNpcShop(player, NPCs.UGLUG_NAR_2039)
            } else {
                sendNPCDialogue(player, NPCs.UGLUG_NAR_2039, "Me's not got no glug-glugs to sell, yous bring me da sickies glug-glug den me's open da stufsies for ya.", FacialExpression.OLD_NORMAL)
            }
            return@on true
        }

    }
}