package content.region.morytania.canifis.handlers

import content.region.morytania.canifis.dialogue.TaxidermistDialogue
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class TaxidermistInteraction : InteractionListener {

    enum class StuffableItem(val toStuff : Int, val product : Int, val message : String, val cost : Int){
        BASS(Items.BIG_BASS_7989, Items.BIG_BASS_7990, "That's a mighty fine sea bass you've caught there.", 1000),
        SWORDFISH(Items.BIG_SWORDFISH_7991, Items.BIG_SWORDFISH_7992, "Don't point that thing at me!", 2500),
        SHARK(Items.BIG_SHARK_7993,Items.BIG_SHARK_7994, "That's quite a fearsome shark! You've done everyone a service by removing it from the sea!", 5000),
        CRAWLING_HAND(Items.CRAWLING_HAND_7975,Items.CRAWLING_HAND_7982, "That's a very fine crawling hand.", 1000),
        COCKATRICE(Items.COCKATRICE_HEAD_7976, Items.COCKATRICE_HEAD_7983, "A cockatrice! Beautiful, isn't it? Look at the plumage!", 2000),
        BASILISK( Items.BASILISK_HEAD_7977, Items.BASILISK_HEAD_7984, "My, he's a scary-looking fellow, isn't he? He'll look good on your wall!",4000),
        KURASK(Items.KURASK_HEAD_7978, Items.KURASK_HEAD_7985, "A kurask? Splendid! Look at those horns!", 6000),
        ABYSSAL(Items.ABYSSAL_HEAD_7979, Items.ABYSSAL_HEAD_7986, "Goodness, an abyssal demon!", 12000),
        KBD(Items.KBD_HEADS_7980, Items.KBD_HEADS_7987, " This must be a King Black Dragon!", 50000),
        KQ(Items.KQ_HEAD_7981, Items.KQ_HEAD_7988, " That must be the biggest kalphite I've ever seen!", 50000)
    }
    class TaxidermistTradeDialogue : DialogueFile() {
        companion object{
            const val TRADE = 10
            const val YES = 20
            const val NO = 30
            const val POOR = 40
        }

        lateinit var stuffableItem : StuffableItem
        override fun handle(componentID: Int, buttonID: Int) {
            when (stage) {
                START_DIALOGUE -> npcl(FacialExpression.HAPPY, stuffableItem.message).also {
                    stage = when(stuffableItem){
                        StuffableItem.ABYSSAL -> stage + 1
                        StuffableItem.KBD -> stage + 2
                        StuffableItem.KQ -> stage + 3
                        else -> TRADE
                    }
                }
                START_DIALOGUE + 1 -> npcl(FacialExpression.HAPPY, "See how it's still glowing? I'll have to use some magic to preserve that.").also { stage = TRADE }
                START_DIALOGUE + 2 -> npcl(FacialExpression.HAPPY, "I'll have to get out my heavy duty tools -- this skin's as tough as iron!").also { stage = TRADE }
                START_DIALOGUE + 3 -> npcl(FacialExpression.HAPPY, "Preserving insects is always tricky. I'll have to be careful...").also { stage = TRADE }

                TRADE -> npcl(FacialExpression.HAPPY, "I can preserve that for you for ${stuffableItem.cost} coins.").also { stage++ }
                TRADE + 1 -> showTopics(
                    Topic("Yes please.", YES),
                    Topic("No thanks.", NO)
                )
                YES -> {
                    if (inInventory(player!!, Items.COINS_995, stuffableItem.cost)){
                        if (removeItem(player!!, stuffableItem.toStuff) && removeItem(player!!, Item(Items.COINS_995, stuffableItem.cost))){
                            addItem(player!!, stuffableItem.product)
                            npcl(FacialExpression.HAPPY, "There you go!").also { stage = END_DIALOGUE }
                        }
                    }
                    else {
                            playerl(FacialExpression.SAD, "But I don't have enough money on me.").also { stage = POOR }
                    }
                }

                POOR -> npcl(FacialExpression.ANNOYED, "Don't waste my time, then!").also { stage = END_DIALOGUE }

                NO -> npcl(FacialExpression.NEUTRAL, "All right, come back if you change your mind, eh?").also { stage = END_DIALOGUE }
            }
        }

    }

    override fun defineListeners() {

        onUseAnyWith(IntType.NPC, NPCs.TAXIDERMIST_4246) { player, used, with ->
            val item = used.id
            StuffableItem.values().map { if(it.toStuff == item){
                val dialogueFile = TaxidermistTradeDialogue()
                dialogueFile.stuffableItem = it
                openDialogue(player, dialogueFile, with as NPC)
                return@onUseAnyWith true
            }
            }

            when (item) {
                // all fish
                Items.RAW_CHICKEN_2138 -> sendNPCDialogue(player, with.id, "Killing a chicken is hardly worth boasting about!")
                else -> sendNPCDialogue(player, with.id, "Don't be silly, I can't preserve that!")
            }
            return@onUseAnyWith true
        }
    }
}