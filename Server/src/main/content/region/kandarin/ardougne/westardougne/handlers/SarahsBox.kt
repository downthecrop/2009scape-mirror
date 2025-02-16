package content.region.kandarin.ardougne.westardougne.handlers

import content.data.Quests
import content.region.kandarin.ardougne.quest.plaguecity.PlagueCity
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.Scenery

class SarahsBox : InteractionListener {
    override fun defineListeners() {

        on(Scenery.BOX_2062, IntType.SCENERY, "open") { _, node ->
            val box = node as core.game.node.scenery.Scenery
            replaceScenery(box, Scenery.BOX_2063, -1)
            return@on true
        }

        on(Scenery.BOX_2063, IntType.SCENERY, "search"){ player, _ ->
            if(isQuestComplete(player, Quests.PLAGUE_CITY)){
                if(hasSpaceFor(player, Item(Items.DOCTORS_GOWN_430)) && !hasAnItem(player, Items.DOCTORS_GOWN_430).exists()){
                    sendMessage(player, "You find a medical gown in the box.")
                    addItem(player, Items.DOCTORS_GOWN_430)
                    return@on true
                }
            }
            sendMessage(player, "You search the box but find nothing")
            return@on true
        }

        on(Scenery.BOX_2063, IntType.SCENERY, "close") { _, node ->
            replaceScenery(node.asScenery(), Scenery.BOX_2062, -1)
            return@on true
        }
    }
}
