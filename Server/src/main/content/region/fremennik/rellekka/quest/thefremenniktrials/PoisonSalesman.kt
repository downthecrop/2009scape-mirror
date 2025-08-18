package content.region.fremennik.rellekka.quest.thefremenniktrials

import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributePoisonClue
import core.api.inInventory
import core.api.setAttribute
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items
import content.data.Quests

@Initializable
class PoisonSalesman(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        options("Talk about the Murder Mystery Quest","Talk about the The Fremennik Trials")
        stage = START_DIALOGUE
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val murderMysteryStage = player.questRepository.getStage(Quests.MURDER_MYSTERY)
        val fremennikTrialsStage = player.questRepository.getStage(Quests.THE_FREMENNIK_TRIALS)

        when (stage) {
            START_DIALOGUE -> when (buttonId) {
                1 -> {
                    when (murderMysteryStage) {
                        0 -> npcl(FacialExpression.NEUTRAL, "I'm afraid I'm all sold out of poison at the moment. People know a bargain when they see it!").also { stage = END_DIALOGUE }
                        1 -> playerl(FacialExpression.NEUTRAL, "I'm investigating the murder at the Sinclair house.").also { stage = 50 }
                        100 -> npcl(FacialExpression.NEUTRAL, "I hear you're pretty smart to have solved the Sinclair Murder!").also { stage = END_DIALOGUE }
                    }
                }
                2 -> { player("Hello."); stage = 10 }
            }

            //The Fremennik Trials
            10 -> {
                /**when (fremennikTrialsStage) {
                    0 -> { npc("Come see me if you ever need low-alcohol beer!"); stage = END_DIALOGUE }
                }*/
                if(fremennikTrialsStage == 0){
                    npc("Come see me if you ever need low-alcohol beer!"); stage = END_DIALOGUE
                }
                else if(fremennikTrialsStage > 30){
                    npc("Thanks for buying out all that low-alcohol beer!"); stage = END_DIALOGUE
                } else if(fremennikTrialsStage > 0){
                    npc("Howdy! You seem like someone with discerning taste!","Howsabout you try my brand new range of alcohol?"); stage++
                }
            }
            11 -> { player("Didn't you used to sell poison?"); stage++ }
            12 -> { npc("That I did indeed! Peter Potter's Patented","Multipurpose poison! A miracle of modern apothecarys!","My exclusive concoction has been tested on..."); stage++ }
            13 -> { player("Uh, yeah. I've already heard the sales pitch."); stage++ }
            14 -> { npc("Sorry stranger, old habits die hard I guess."); stage++ }
            15 -> { player("So you don't sell poison anymore?"); stage++ }
            16 -> { npc("Well I would, but I ran out of stock. Business wasn't","helped with that stuff that happened up at the Sinclair","Mansion much either, I'll be honest."); stage++ }
            17 -> { npc("So, being the man of enterprise that I am I decided to","branch out a little bit!"); stage++ }
            18 -> { player("Into alcohol?"); stage++ }
            19 -> { npc("Absolutely! The basic premise between alcohol and poison","is pretty much the same, after all! The difference is that","my alcohol has a unique property others do not!"); stage++ }
            20 -> { player("And what is that?"); stage++ }
            21 -> { sendDialogue("The salesman takes a deep breath."); stage++ }
            22 -> { npc("Ever been too drunk to find your own home? Ever","wished that you could party away all night long, and","still wake up fresh as a daisy the next morning?"); stage++ }
            23 -> { npc("Thanks to the miracles of modern magic we have come","up with just the solution you need! Peter Potter's","Patented Party Potions!"); stage++ }
            24 -> { npc("It looks just like beer! It tastes just like beer! It smells","just like beer! But... it's not beer!"); stage++ }
            25 -> { npc("Our mages have mused for many moments to bring","you this miracle of modern magic! It has all the great","tastes you'd expect, but contains absolutely no alcohol!"); stage++ }
            26 -> { npc("That's right! You can drink Peter Potter's Patented","Party Potion as much as you want, and suffer","absolutely no ill effects whatsoever!"); stage++ }
            27 -> { npc("The clean fresh taste you know you can trust, from","the people who brought you: Peter Potters Patented","multipurpose poison, Pete Potters peculiar paint packs"); stage++ }
            28 -> { npc("and Peter Potters paralyzing panic pins. Available now","from all good stockists! Ask your local bartender now,","and experience the taste revolution of the century!"); stage++ }
            29 -> { sendDialogue("He seems to have finished for the time being."); stage++ }
            30 -> { player("So.. when you say 'all good stockists'..."); stage++ }
            31 -> { npc("Yes?"); stage++ }
            32 -> { player("How many inns actually sell this stuff?"); stage++ }
            33 -> { npc("Well.. nobody has actually bought any yet. Everyone I","try and sell it to always asks me what exactly the point","of beer that has absolutely no effect on you is."); stage++ }
            34 -> { player("So what is the point?"); stage++ }
            35 -> { npc("Well... Um... Er... Hmmm. You, er, don't get drunk."); stage++ }
            36 -> { player("I see..."); stage++ }
            37 -> { npc("Aw man.. you don't want any now do you? I've really","tried to push this product, but I just don't think the","world is ready for beer that doesn't get you drunk."); stage++ }
            38 -> { npc("I'm a man ahead of my time I tell you! It's not that","my products are bad, it's that they're too good for the","market!"); stage++ }
            39 -> { player("Actually, I would like some. How much do you want for it?"); stage++ }
            40 -> { npc("Y-you would??? Um, okay! I knew I still had the old","salesmans skills going on!"); stage++ }
            41 -> { npc("I will sell you a keg of it for only 250 gold pieces! So","What do you say?"); stage++ }
            42 -> { options("Yes","No"); stage++ }
            43 -> when(buttonId){
                    1 -> { player("Yes please!"); stage++ }
                    2 -> { player("No, thanks."); stage = END_DIALOGUE }
                 }
                //Yes
                44 -> if(player?.inventory?.containsItem(Item(995,250)) == true){
                            player?.inventory?.remove(Item(995,250))
                            player?.inventory?.add(Item(3712))
                            npc("Here you go.")
                            stage = END_DIALOGUE
                       } else {
                            player("I don't seem to have enough.")
                            stage++
                       }
                45 -> { npc("Well come back when you do!"); stage = END_DIALOGUE }

            //Murder Mystery
            50 -> npcl(FacialExpression.NEUTRAL, "There was a murder at the Sinclair house??? That's terrible! And I was only there the other day too! They bought the last of my Patented Multi Purpose Poison!").also { stage++ }
            51 -> showTopics(
                Topic("Patented Multi Purpose Poison?", 52),
                Topic("Who did you sell Poison to at the house?", 61),
                Topic("Can I buy some Poison?", 65),
                IfTopic("I have this pot I found at the murder scene...", 69, inInventory(player, Items.PUNGENT_POT_1812))
            )
            52 -> npcl(FacialExpression.NEUTRAL, "Aaaaah... a miracle of modern apothecaries!").also { stage++ }
            53 -> npcl(FacialExpression.NEUTRAL, "This exclusive concoction has been tested on all known forms of life and been proven to kill them all in varying dilutions from cockroaches to king dragons!").also { stage++ }
            54 -> npcl(FacialExpression.NEUTRAL, "So incredibly versatile, it can be used as pest control, a cleansing agent, drain cleaner, metal polish and washes whiter than white,").also { stage++ }
            55 -> npcl(FacialExpression.NEUTRAL, "all with our uniquely fragrant concoction that is immediately recognisable across the land as Peter Potter's Patented Poison potion!!!").also { stage++ }
            56 -> sendDialogue("The salesman stops for breath.").also { stage ++ }
            57 -> npcl(FacialExpression.NEUTRAL, "I'd love to sell you some but I've sold out recently. That's just how good it is! Three hundred and twenty eight people in this area alone cannot be wrong!").also { stage++ }
            58 -> npcl(FacialExpression.NEUTRAL, "Nine out of Ten poisoners prefer it in controlled tests!").also { stage++ }
            59 -> npcl(FacialExpression.NEUTRAL, "Can I help you with anything else? Perhaps I can take your name and add it to our mailing list of poison users? We will only send you information related to the use of poison and other Peter Potter Products!").also { stage++ }
            60 -> playerl(FacialExpression.NEUTRAL, "Uh... no, it's ok. Really.").also { stage = END_DIALOGUE }

            61 -> npcl(FacialExpression.HAPPY, "Well, Peter Potter's Patented Multi Purpose Poison is a product of such obvious quality that I am glad to say I managed to sell a bottle to each of the Sinclairs!").also { stage++ }
            62 -> npcl(FacialExpression.HAPPY, "Anna, Bob, Carol, David, Elizabeth and Frank all bought a bottle! In fact they bought the last of my supplies!").also { stage++ }
            63 -> npcl(FacialExpression.HAPPY, "Maybe I can take your name and address and I will personally come and visit you when stocks return?").also { stage++ }
            64 -> playerl(FacialExpression.THINKING, "Uh...no, it's ok.")
                .also { setAttribute(player, attributePoisonClue, 1)}
                .also { stage = END_DIALOGUE }

            65 -> npcl(FacialExpression.NEUTRAL, "I'm afraid I am totally out of stock at the moment after my successful trip to the Sinclairs' House the other day.").also { stage++ }
            66 -> npcl(FacialExpression.NEUTRAL, "But don't worry! Our factories are working overtime to produce Peter Potter's Patented Multi Purpose Poison!").also { stage++ }
            67 -> npcl(FacialExpression.NEUTRAL, "Possibly the finest multi purpose poison and cleaner yet available to the general market.").also { stage++ }
            68 -> npcl(FacialExpression.NEUTRAL, "And its unique fragrance makes it the number one choice for cleaners and exterminators the whole country over!").also { stage = END_DIALOGUE }

            69 -> sendDialogue("You show the poison salesman the pot you found at the murder", "scene with the unusual smell.").also { stage ++ }
            70 -> npcl(FacialExpression.THINKING, "Hmmm... yes, that smells exactly like my Patented Multi Purpose Poison, but I don't see how it could be. It quite clearly says on the label of all bottles").also { stage++ }
            71 -> npcl(FacialExpression.THINKING, "'Not to be taken internally - EXTREMELY POISONOUS'.").also { stage++ }
            72 -> playerl(FacialExpression.THINKING, "Perhaps someone else put it in his wine?").also { stage++ }
            73 -> npcl(FacialExpression.THINKING, "Yes... I suppose that could have happened...").also { stage = END_DIALOGUE }
            END_DIALOGUE -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return PoisonSalesman(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(820)
    }

}