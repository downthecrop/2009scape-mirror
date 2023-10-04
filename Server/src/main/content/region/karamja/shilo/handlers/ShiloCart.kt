package content.region.karamja.shilo.handlers

import core.api.*
import core.game.component.Component
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs09.consts.Components
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class ShiloCart : InteractionListener {

    override fun defineListeners() {

        val BRIMHAVEN_CART = 2230
        val SHILO_CART = 2265

        // Cart IN Brimhaven
        on(BRIMHAVEN_CART, IntType.SCENERY, "board") { player, _ ->
            openDialogue(player, CartTravelDialogue(), NPC(NPCs.HAJEDY_510))
            return@on true
        }
        on(BRIMHAVEN_CART, IntType.SCENERY, "pay-fare") { player, _ ->
            openDialogue(player, CartQuickPay(), NPC(NPCs.HAJEDY_510))
            return@on true
        }

        // Hajedy
        on(NPCs.HAJEDY_510, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, CartTravelDialogue(), NPC(NPCs.HAJEDY_510))
            return@on true
        }
        on(NPCs.HAJEDY_510, IntType.NPC, "pay-fare") { player, _ ->
            openDialogue(player, CartQuickPay(), NPC(NPCs.HAJEDY_510))
            return@on true
        }


        // Cart IN Shilo
        on(SHILO_CART, IntType.SCENERY, "board") { player, _ ->
            openDialogue(player, CartTravelDialogue(), NPC(NPCs.VIGROY_511))
            return@on true
        }
        on(SHILO_CART, IntType.SCENERY, "pay-fare") { player, _ ->
            openDialogue(player, CartQuickPay(), NPC(NPCs.VIGROY_511))
            return@on true
        }

        // Vigroy
        on(NPCs.VIGROY_511, IntType.NPC, "talk-to") { player, npc ->
            openDialogue(player, CartTravelDialogue(), npc)
            return@on true
        }
        on(NPCs.VIGROY_511, IntType.NPC, "pay-fare") { player, npc ->
            openDialogue(player, CartQuickPay(), npc)
            return@on true
        }
    }
}

class CartQuickPay : DialogueFile(){
    override fun handle(interfaceId: Int, buttonId: Int) {
        if (!hasRequirement(player!!, "Shilo Village")) return;
        val shilo = npc?.id == 510;
        when (stage) {
            0 -> if(inInventory(player!!,Items.COINS_995,10)){
                sendDialogue(player!!,"You pay the fare and hand 10 gold coins to "+ (npc?.name ?: "") +".").also { stage++ }
            } else {
                sendMessage(player!!,"You don't have enough coins.").also { stage = END_DIALOGUE }
            }
            1 -> {
                if(removeItem(player!!,Item(Items.COINS_995,10))){
                    queueScript(player!!, 1, QueueStrength.SOFT) { Qstage: Int ->
                        when(Qstage){
                            0 -> {
                                player!!.interfaceManager.closeOverlay()
                                player!!.interfaceManager.openOverlay(Component(Components.FADE_TO_BLACK_120))
                                return@queueScript keepRunning(player!!)
                            }
                            1 -> {
                                teleport(player!!, if (shilo) Location.create(2834, 2951, 0) else Location.create(2780, 3212, 0))
                                player!!.interfaceManager.closeOverlay()
                                player!!.interfaceManager.openOverlay(Component(Components.FADE_FROM_BLACK_170))
                                player!!.achievementDiaryManager.finishTask(player, DiaryType.KARAMJA, 1, 3)
                                sendDialogue(player!!,"You feel tired from the journey, but at least you didn't have to walk all that distance.").also { stage = END_DIALOGUE }
                            }
                        }
                        return@queueScript stopExecuting(player!!)
                    }
                }
                stage = END_DIALOGUE;
            }
        }
    }
}

class CartTravelDialogue : DialogueFile(){
    override fun handle(componentID: Int, buttonID: Int) {
        if (!hasRequirement(player!!, "Shilo Village")) return;
        val shilo = npc?.id == 510;
        when (stage) {
            0 -> npcl("I am offering a cart ride to " + (if (shilo) "Shilo Village" else "Brimhaven") + " if you're interested? It will cost 10 gold coins. Is that Ok?").also { stage++ }
            1 -> if (inInventory(player!!,Items.COINS_995,10)) {
                playerl("Yes please, I'd like to go to " + (if (shilo) "Shilo Village" else "Brimhaven") + ".").also { stage++ }
            } else{
                playerl("Sorry, I don't seem to have enough coins.").also{ stage = END_DIALOGUE }
            }
            2 -> npcl("Great! Just hop into the cart then and we'll go!").also { stage++ }
            3 -> sendDialogue(player!!,"You hop into the cart and the driver urges the horses on. You take a taxing journey through the jungle to " + (if (shilo) "Shilo Village" else "Brimhaven") + ".").also { stage++ }
            4 -> openDialogue(player!!,CartQuickPay(),npc!!)
        }
    }
}